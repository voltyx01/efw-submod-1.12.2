package com.voltyx.mwccf.doll;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.blood.BloodManager;
import com.voltyx.mwccf.blood.BloodOverlayEventHandler;
import com.voltyx.mwccf.doll.network.PacketDollActivate;
import com.voltyx.mwccf.doll.network.PacketDollBuffSync;
import com.voltyx.mwccf.geo.HeartbeatManager;
import com.voltyx.mwccf.potion.PotionSayaBuff;
import com.voltyx.mwccf.speech.SpeechServerHandler;
import efw.init.EfwModSounds;
import efw.item.ItemDoll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.teamderpy.shouldersurfing.util.WeaponHelper;
import com.voltyx.mwccf.fireweapon.FireWeaponHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the active ability of Saya's Plush Doll:
 * - Ready state when blood is at maximum (>= 0.999f) -> blinks glowing red in inventory/GUI.
 * - Activation when taken into hand:
 *   - Pulse surges to 180 BPM.
 *   - Screen darkening (vignette & blackout) smooth fade-in behind doll.
 *   - Player slows down.
 *   - Plays sound mwccf:item.doll.activate (doll_activate.ogg) at loud volume.
 *   - At ~1.0s (tick 20): personal speech replica above hotbar ("I'm sorry" / "Прости").
 *   - At ~3.0s (tick 60): character puts toy away, darkening and slowness fade out.
 *   - Starts 15-second frenzy buff:
 *     - BPM locked high (>= 178f).
 *     - BPM blackout resistance active (no darkening from high BPM).
 *     - +50% melee weapon damage bonus.
 *     - Accelerated MWC firearm reload speed (1.6x).
 *     - Melee kill: +3s (+60 ticks).
 *     - Firearm kill: +2s (+40 ticks).
 */
@Mod.EventBusSubscriber(modid = "mwccf")
public class SayaDollManager {

    public static final int ACTIVATION_TICKS_TOTAL = 60;  // ~3.0 seconds (gives ~2s to hold doll after replica)
    public static final int ACTIVATION_SPEECH_TICK = 20;  // ~1.0 second into activation
    public static final int BUFF_TICKS_INITIAL     = 300; // 15.0 seconds (20 ticks/sec * 15)

    // Server-side state tracking
    private static final ConcurrentHashMap<UUID, Integer> lastNonDollSlots = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Integer> activationDollSlots = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Integer> activationTicks = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Integer> buffTicks = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Boolean> serverSpeechTriggered = new ConcurrentHashMap<>();

    // Client-side dedicated state tracking (guarantees responsiveness without multi-thread conflicts)
    @SideOnly(Side.CLIENT)
    private static int clientActivationRemainingTicks;
    @SideOnly(Side.CLIENT)
    private static int clientLockedDollSlot = -1;
    @SideOnly(Side.CLIENT)
    private static int clientBuffRemainingTicks;
    @SideOnly(Side.CLIENT)
    private static boolean clientSpeechTriggered;

    // Client-side smooth darkness alpha (0..1)
    private static float darknessAlpha = 0.0f;
    private static float targetDarknessAlpha = 0.0f;

    // Client-side smooth HUD fade alpha (1.0 = normal HUD, 0.0 = completely faded out)
    @SideOnly(Side.CLIENT)
    private static float hudFadeAlpha = 1.0f;
    @SideOnly(Side.CLIENT)
    private static float targetHudFadeAlpha = 1.0f;

    // Client-side debounce to prevent re-triggering activation while already starting
    private static long clientLastActivateAttempt = 0L;

    /** Tracks player entity ID to detect respawns on the client side. */
    @SideOnly(Side.CLIENT)
    private static int lastKnownClientEntityId = -1;

    public static boolean isReady(EntityPlayer player) {
        if (player == null) return false;
        if (isActivating(player) || isBuffActive(player)) return false;
        return BloodManager.getBloodLevel(player.getUniqueID()) >= 0.999f;
    }

    public static boolean isActivating(EntityPlayer player) {
        if (player == null) return false;
        if (player.world != null && player.world.isRemote) {
            return clientActivationRemainingTicks > 0;
        }
        Integer ticks = activationTicks.get(player.getUniqueID());
        return ticks != null && ticks > 0;
    }

    public static boolean isBuffActive(EntityPlayer player) {
        if (player == null) return false;
        if (player.isPotionActive(PotionSayaBuff.INSTANCE)) {
            return true;
        }
        if (player.world != null && player.world.isRemote) {
            return clientBuffRemainingTicks > 0;
        }
        Integer ticks = buffTicks.get(player.getUniqueID());
        return ticks != null && ticks > 0;
    }

    public static boolean hasBpmResistance(EntityPlayer player) {
        if (player == null) return false;
        if (player.isPotionActive(PotionSayaBuff.INSTANCE)) {
            return true;
        }
        if (player.world != null && player.world.isRemote) {
            return clientActivationRemainingTicks > 0 || clientBuffRemainingTicks > 0;
        }
        return isActivating(player) || isBuffActive(player);
    }

    public static float getDarknessAlpha() {
        return darknessAlpha;
    }

    public static float getHudFadeAlpha() {
        return hudFadeAlpha;
    }

    /**
     * Called when a player equips or selects an item, or every player tick.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        if (player == null || player.world == null) return;

        UUID id = player.getUniqueID();
        boolean holdingDoll = ItemDoll.isDollItem(player.getHeldItemMainhand());

        // Track last held non-doll hotbar slot so character can switch back to weapon
        if (!holdingDoll && !isActivating(player)) {
            lastNonDollSlots.put(id, player.inventory.currentItem);
        }

        // Check if player took doll into hands while ready
        if (holdingDoll && isReady(player)) {
            if (player.world.isRemote) {
                // Client-side: trigger activation request with 500ms debounce
                long now = System.currentTimeMillis();
                if (now - clientLastActivateAttempt > 500L) {
                    clientLastActivateAttempt = now;
                    triggerClientActivation(player);
                }
            } else {
                // Server-side: initiate activation directly
                startActivation(player);
            }
        }

        // Only manage server-side counters on server thread to prevent singleplayer JVM double-ticking
        if (!player.world.isRemote) {
            // Lock hotbar slot on server during activation to prevent item switching
            if (isActivating(player)) {
                Integer act = activationTicks.get(id);
                // Do not lock slot during the last 4 ticks to prevent packet bouncing when client puts doll away
                if (act != null && act > 4) {
                    Integer locked = activationDollSlots.computeIfAbsent(id, k -> player.inventory.currentItem);
                    if (locked != null && locked >= 0 && locked < 9) {
                        if (player.inventory.currentItem != locked) {
                            player.inventory.currentItem = locked;
                            if (player instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) player).connection.sendPacket(new SPacketHeldItemChange(locked));
                            }
                        }
                    }
                }
            }

            // Tick activation state
            Integer act = activationTicks.get(id);
            if (act != null && act > 0) {
                int nextAct = act - 1;
                activationTicks.put(id, nextAct);

                // Keep slowness refreshed during activation
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 3, false, false));

                // Trigger replica at speech tick
                int elapsed = ACTIVATION_TICKS_TOTAL - nextAct;
                if (elapsed >= ACTIVATION_SPEECH_TICK && !serverSpeechTriggered.getOrDefault(id, false)) {
                    serverSpeechTriggered.put(id, true);
                    if (player instanceof EntityPlayerMP) {
                        SpeechServerHandler.sendPersonal((EntityPlayerMP) player, "speech.mwccf.doll.sorry");
                    }
                }

                // End of activation: put toy away, remove slowness, clear blood, start 15s buff
                if (nextAct <= 0) {
                    activationTicks.remove(id);
                    activationDollSlots.remove(id);
                    serverSpeechTriggered.remove(id);
                    finishActivationAndStartBuff(player);
                }
            }

            // Tick buff duration
            Integer bTicks = buffTicks.get(id);
            if (bTicks != null && bTicks > 0) {
                int nextB = bTicks - 1;
                if (nextB <= 0) {
                    buffTicks.remove(id);
                    if (player instanceof EntityPlayerMP) {
                        MwccfMod.PACKET_HANDLER.sendTo(new PacketDollBuffSync(player.getEntityId(), 0), (EntityPlayerMP) player);
                    }
                } else {
                    buffTicks.put(id, nextB);
                }
            }
        }
    }

    /**
     * Client tick: updates local ability timers, visual darkness, and forces high BPM.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            lastKnownClientEntityId = -1;
            return;
        }

        // In START phase, consume hotbar inputs to prevent switching during activation
        if (event.phase == TickEvent.Phase.START) {
            if (isActivating(mc.player)) {
                consumeHotbarSwitchKeys(mc);
                if (clientLockedDollSlot >= 0 && clientLockedDollSlot < 9) {
                    if (mc.player.inventory.currentItem != clientLockedDollSlot) {
                        mc.player.inventory.currentItem = clientLockedDollSlot;
                    }
                }
            }
            return;
        }

        // END phase: update timers and visuals

        // Detect player entity change (respawn) — clear all state immediately
        int currentEntityId = mc.player.getEntityId();
        if (lastKnownClientEntityId != -1 && lastKnownClientEntityId != currentEntityId) {
            clientActivationRemainingTicks = 0;
            clientLockedDollSlot = -1;
            clientBuffRemainingTicks = 0;
            clientSpeechTriggered = false;
            targetDarknessAlpha = 0.0f;
            darknessAlpha = 0.0f;
            targetHudFadeAlpha = 1.0f;
            hudFadeAlpha = 1.0f;
            efw.util.ShoulderSurfingCompat.resetDollCamera();
            com.voltyx.mwccf.speech.client.SpeechClientManager.clearPersonal();
            com.voltyx.mwccf.render.doll.DollRenderer.resetClientState();
            efw.util.MWCLoweringResetHelper.resetLoweringState();
        }
        lastKnownClientEntityId = currentEntityId;

        // Reset immediately if player died
        if (mc.player.isDead || mc.player.getHealth() <= 0.0f) {
            clientActivationRemainingTicks = 0;
            clientLockedDollSlot = -1;
            clientBuffRemainingTicks = 0;
            clientSpeechTriggered = false;
            targetDarknessAlpha = 0.0f;
            darknessAlpha = 0.0f;
            targetHudFadeAlpha = 1.0f;
            hudFadeAlpha = 1.0f;
            efw.util.ShoulderSurfingCompat.resetDollCamera();
            com.voltyx.mwccf.speech.client.SpeechClientManager.clearPersonal();
            com.voltyx.mwccf.render.doll.DollRenderer.resetClientState();
            efw.util.MWCLoweringResetHelper.resetLoweringState();
            return;
        }

        // Tick local client activation timer
        if (clientActivationRemainingTicks > 0) {
            if (clientLockedDollSlot < 0 || clientLockedDollSlot > 8) {
                clientLockedDollSlot = mc.player.inventory.currentItem;
            }
            consumeHotbarSwitchKeys(mc);
            if (mc.player.inventory.currentItem != clientLockedDollSlot) {
                mc.player.inventory.currentItem = clientLockedDollSlot;
            }

            clientActivationRemainingTicks--;
            int elapsed = ACTIVATION_TICKS_TOTAL - clientActivationRemainingTicks;

            // Trigger speech replica on client at speech tick
            if (elapsed >= ACTIVATION_SPEECH_TICK && !clientSpeechTriggered) {
                clientSpeechTriggered = true;
                triggerSorryReplica(mc.player);
            }

            // End of client activation
            if (clientActivationRemainingTicks <= 0) {
                clientLockedDollSlot = -1;
                clientBuffRemainingTicks = BUFF_TICKS_INITIAL;
                targetDarknessAlpha = 0.0f;
                com.voltyx.mwccf.speech.client.SpeechClientManager.startPersonalFadeOut();
                putDollAway(mc.player);
            }
        }

        // Tick local client buff timer
        if (clientBuffRemainingTicks > 0) {
            clientBuffRemainingTicks--;
        }

        // Smoothly lerp darkness alpha (fade in during activation, fade out smoothly after)
        if (isActivating(mc.player)) {
            targetDarknessAlpha = 0.95f;
            darknessAlpha = lerp(darknessAlpha, targetDarknessAlpha, 0.12f);
        } else {
            targetDarknessAlpha = 0.0f;
            darknessAlpha = lerp(darknessAlpha, targetDarknessAlpha, 0.08f);
            if (darknessAlpha < 0.005f) {
                darknessAlpha = 0.0f;
            }
        }

        // Smooth HUD fade out during doll activation, and fade back in when doll is put away
        if (isActivating(mc.player)) {
            targetHudFadeAlpha = 0.0f;
        } else {
            targetHudFadeAlpha = 1.0f;
        }
        hudFadeAlpha = lerp(hudFadeAlpha, targetHudFadeAlpha, 0.09f);
        if (hudFadeAlpha > 0.995f) hudFadeAlpha = 1.0f;
        if (hudFadeAlpha < 0.005f) hudFadeAlpha = 0.0f;

        // Keep BPM high during activation and 15s buff
        if (isActivating(mc.player) || isBuffActive(mc.player)) {
            HeartbeatManager.currentBPM = Math.max(HeartbeatManager.currentBPM, 180f);
            HeartbeatManager.displayBPM = Math.max(HeartbeatManager.displayBPM, 180);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void consumeHotbarSwitchKeys(Minecraft mc) {
        if (mc.gameSettings == null) return;
        if (mc.gameSettings.keyBindsHotbar != null) {
            for (KeyBinding kb : mc.gameSettings.keyBindsHotbar) {
                while (kb.isPressed()) {}
            }
        }
        if (mc.gameSettings.keyBindSwapHands != null) {
            while (mc.gameSettings.keyBindSwapHands.isPressed()) {}
        }
        if (mc.gameSettings.keyBindDrop != null) {
            while (mc.gameSettings.keyBindDrop.isPressed()) {}
        }
        if (mc.gameSettings.keyBindInventory != null) {
            while (mc.gameSettings.keyBindInventory.isPressed()) {}
        }
    }

    /**
     * Prevents hotbar scrolling via mouse wheel during doll activation.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onMouseEvent(MouseEvent event) {
        if (event.getDwheel() != 0) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null && isActivating(mc.player)) {
                event.setCanceled(true);
            }
        }
    }

    /**
     * Consumes hotbar number keys immediately upon key press.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && isActivating(mc.player)) {
            consumeHotbarSwitchKeys(mc);
            if (clientLockedDollSlot >= 0 && clientLockedDollSlot < 9) {
                if (mc.player.inventory.currentItem != clientLockedDollSlot) {
                    mc.player.inventory.currentItem = clientLockedDollSlot;
                }
            }
        }
    }

    /**
     * Disallows opening inventory / container GUI while activation animation is playing.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiContainer) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null && isActivating(mc.player)) {
                event.setCanceled(true);
            }
        }
    }

    /**
     * Prevents dropping the doll while activation sequence is playing.
     */
    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        if (event.getPlayer() != null && isActivating(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    private static float lerp(float cur, float target, float speed) {
        return cur + (target - cur) * speed;
    }

    /**
     * Client triggers activation locally for immediate responsive audio & visuals,
     * and sends packet to server.
     */
    @SideOnly(Side.CLIENT)
    private static void triggerClientActivation(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        if (player == mc.player) {
            clientActivationRemainingTicks = ACTIVATION_TICKS_TOTAL;
            clientLockedDollSlot = player.inventory.currentItem;
            clientBuffRemainingTicks = 0;
            clientSpeechTriggered = false;
            targetDarknessAlpha = 0.95f;

            // Spike BPM immediately
            HeartbeatManager.currentBPM = 180f;
            HeartbeatManager.displayBPM = 180;

            // Switch to first-person cleanly if ShoulderSurfing is active
            efw.util.ShoulderSurfingCompat.switchForDoll();

            // Play activate sound locally with maximum volume and no distance attenuation
            if (EfwModSounds.DOLL_ACTIVATE != null) {
                mc.getSoundHandler().playSound(new PositionedSoundRecord(
                        EfwModSounds.DOLL_ACTIVATE.getSoundName(),
                        SoundCategory.MASTER,
                        4.0F, 1.0F, false, 0,
                        PositionedSoundRecord.AttenuationType.NONE,
                        0.0F, 0.0F, 0.0F));
            }

            // Send packet to server
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketDollActivate());
        }
    }

    /**
     * Server receives client activation request.
     */
    public static void handleClientActivationRequest(EntityPlayerMP player) {
        if (player == null) return;
        // Verify readiness (with small tolerance for float blood value)
        if (isActivating(player) || isBuffActive(player)) return;
        if (BloodManager.getBloodLevel(player.getUniqueID()) < 0.95f) return;
        if (!ItemDoll.isDollItem(player.getHeldItemMainhand())) return;

        startActivation(player);
    }

    /**
     * Starts activation sequence on server.
     */
    public static void startActivation(EntityPlayer player) {
        UUID id = player.getUniqueID();
        activationTicks.put(id, ACTIVATION_TICKS_TOTAL);
        activationDollSlots.put(id, player.inventory.currentItem);
        serverSpeechTriggered.put(id, false);

        // Apply slowness (amplifier 3 = slowness IV)
        player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, ACTIVATION_TICKS_TOTAL + 10, 3, false, false));

        // Play activate sound for other players
        if (!player.world.isRemote && EfwModSounds.DOLL_ACTIVATE != null) {
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    EfwModSounds.DOLL_ACTIVATE, SoundCategory.PLAYERS, 3.5F, 1.0F);
        }
    }

    /**
     * Plays personal replica "speech.mwccf.doll.sorry" ("I'm sorry" / "Прости меня.") above hotbar.
     */
    private static void triggerSorryReplica(EntityPlayer player) {
        if (player.world.isRemote) {
            try {
                String text = I18n.format("speech.mwccf.doll.sorry");
                if (text == null || text.isEmpty() || text.equals("speech.mwccf.doll.sorry")) {
                    text = "Прости меня.";
                }
                com.voltyx.mwccf.speech.client.SpeechClientManager.showPersonal(text);
            } catch (Throwable ignored) {
                com.voltyx.mwccf.speech.client.SpeechClientManager.showPersonal("Прости меня.");
            }
        } else if (player instanceof EntityPlayerMP) {
            SpeechServerHandler.sendPersonal((EntityPlayerMP) player, "speech.mwccf.doll.sorry");
        }
    }

    /**
     * Completes activation: puts toy away, grants 15s buff (blood is not consumed).
     */
    private static void finishActivationAndStartBuff(EntityPlayer player) {
        UUID id = player.getUniqueID();

        // 1. Remove slowness
        player.removePotionEffect(MobEffects.SLOWNESS);

        // 2. Put toy away: switch to last non-doll slot
        putDollAway(player);

        // 3. User request: Do not clear or spend blood!
        // BloodManager.clear(id);

        // 4. Start 15s frenzy buff via real Potion effect
        player.addPotionEffect(new PotionEffect(PotionSayaBuff.INSTANCE, BUFF_TICKS_INITIAL, 0, false, false));
        buffTicks.put(id, BUFF_TICKS_INITIAL);

        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            MwccfMod.PACKET_HANDLER.sendTo(new PacketDollBuffSync(player.getEntityId(), BUFF_TICKS_INITIAL), (EntityPlayerMP) player);
        }
    }

    /**
     * Checks if the stack is a cold (melee) weapon or MWC firearm/grenade.
     */
    public static boolean isMeleeOrMwcWeapon(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (WeaponHelper.isMwcWeapon(stack)) {
            return true;
        }
        if (FireWeaponHelper.isWeapon(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
            return true;
        }
        return false;
    }

    /**
     * Automatically switches player's active hotbar slot away from the doll.
     * Priority:
     * 1. Cold weapon or MWC weapon from hotbar (preferring the weapon held before activation)
     * 2. Empty hand (empty hotbar slot)
     * 3. Any non-doll hotbar slot
     * 4. Slot 0 fallback
     */
    public static void putDollAway(EntityPlayer player) {
        UUID id = player.getUniqueID();
        int targetSlot = -1;

        // 1. Check if previously held non-doll slot was a cold or MWC weapon
        int lastSlot = lastNonDollSlots.getOrDefault(id, -1);
        if (lastSlot >= 0 && lastSlot < 9 && isMeleeOrMwcWeapon(player.inventory.getStackInSlot(lastSlot))) {
            targetSlot = lastSlot;
        }

        // If not, search hotbar (0..8) for any cold or MWC weapon
        if (targetSlot == -1) {
            for (int s = 0; s < 9; s++) {
                if (isMeleeOrMwcWeapon(player.inventory.getStackInSlot(s))) {
                    targetSlot = s;
                    break;
                }
            }
        }

        // 2. If no weapon in hotbar, choose empty hand (empty hotbar slot)
        if (targetSlot == -1) {
            for (int s = 0; s < 9; s++) {
                if (player.inventory.getStackInSlot(s).isEmpty()) {
                    targetSlot = s;
                    break;
                }
            }
        }

        // 3. If no empty slot, choose any slot not holding a doll (prefer lastSlot if not a doll)
        if (targetSlot == -1) {
            if (lastSlot >= 0 && lastSlot < 9 && !ItemDoll.isDollItem(player.inventory.getStackInSlot(lastSlot))) {
                targetSlot = lastSlot;
            } else {
                for (int s = 0; s < 9; s++) {
                    if (!ItemDoll.isDollItem(player.inventory.getStackInSlot(s))) {
                        targetSlot = s;
                        break;
                    }
                }
            }
        }

        // 4. Fallback: slot 0
        if (targetSlot == -1) {
            targetSlot = 0;
        }

        player.inventory.currentItem = targetSlot;
        if (player.world.isRemote) {
            clientLockedDollSlot = -1;
            com.voltyx.mwccf.speech.client.SpeechClientManager.startPersonalFadeOut();
            // Restore shoulder-surfing perspective that was saved when doll activated
            efw.util.ShoulderSurfingCompat.resetDollCamera();
            Minecraft mc = Minecraft.getMinecraft();
            if (player == mc.player && mc.player.connection != null) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(targetSlot));
            }
        } else if (player instanceof EntityPlayerMP) {
            activationDollSlots.remove(id);
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketHeldItemChange(targetSlot));
        }
    }

    /**
     * Sync packet received on client.
     */
    @SideOnly(Side.CLIENT)
    public static void handleClientBuffSync(int entityId, int durationTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && (entityId == mc.player.getEntityId() || entityId == -1)) {
            clientBuffRemainingTicks = durationTicks;
            if (durationTicks <= 0) {
                clientActivationRemainingTicks = 0;
            }
        }
        if (mc.world != null) {
            Entity entity = mc.world.getEntityByID(entityId);
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (durationTicks > 0) {
                    buffTicks.put(player.getUniqueID(), durationTicks);
                } else {
                    buffTicks.remove(player.getUniqueID());
                }
            }
        }
    }

    /**
     * Extends buff duration (e.g. from kills).
     */
    public static void extendBuff(EntityPlayer player, int addTicks) {
        if (player == null || addTicks <= 0) return;
        UUID id = player.getUniqueID();
        PotionEffect currentEffect = player.getActivePotionEffect(PotionSayaBuff.INSTANCE);
        int updated = addTicks;
        if (currentEffect != null) {
            updated += currentEffect.getDuration();
        } else {
            Integer cur = buffTicks.get(id);
            if (cur != null) updated += cur;
        }
        player.addPotionEffect(new PotionEffect(PotionSayaBuff.INSTANCE, updated, 0, false, false));
        buffTicks.put(id, updated);

        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            MwccfMod.PACKET_HANDLER.sendTo(new PacketDollBuffSync(player.getEntityId(), updated), (EntityPlayerMP) player);
        }
    }

    /**
     * LivingHurtEvent: applies +50% melee damage bonus during the buff.
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        Entity trueSource = source.getTrueSource();
        if (!(trueSource instanceof EntityPlayer)) return;

        EntityPlayer attacker = (EntityPlayer) trueSource;
        if (!isBuffActive(attacker)) return;

        // Firearms are handled by MWC; melee attacks get +50% bonus
        boolean isMwcRanged = BloodOverlayEventHandler.isMwcRangedDamage(source, attacker);
        if (!isMwcRanged) {
            float amount = event.getAmount();
            event.setAmount(amount * 1.50f);
        }
    }

    /**
     * LivingDeathEvent:
     * - If player died: resets doll ability/buff immediately.
     * - If player killed target: melee kill adds +3s (+60 ticks), firearm kill adds +2s (+40 ticks).
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        // 1. Reset effect immediately if player died
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer dead = (EntityPlayer) event.getEntityLiving();
            clear(dead.getUniqueID());
            dead.removePotionEffect(MobEffects.SLOWNESS);
            dead.removePotionEffect(PotionSayaBuff.INSTANCE);
            if (!dead.world.isRemote && dead instanceof EntityPlayerMP) {
                MwccfMod.PACKET_HANDLER.sendTo(new PacketDollBuffSync(dead.getEntityId(), 0), (EntityPlayerMP) dead);
            }
            if (dead.world.isRemote || (Minecraft.getMinecraft().player != null && dead.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()))) {
                efw.util.MWCLoweringResetHelper.resetLoweringState();
                efw.util.ShoulderSurfingCompat.resetDollCamera();
                com.voltyx.mwccf.render.doll.DollRenderer.resetClientState();
            }
        }

        // 2. Kill extensions for attacker
        DamageSource source = event.getSource();
        if (source == null) return;
        Entity trueSource = source.getTrueSource();
        if (!(trueSource instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) trueSource;
        if (!isBuffActive(player)) return;

        boolean isMwcRanged = BloodOverlayEventHandler.isMwcRangedDamage(source, player);
        if (isMwcRanged) {
            // Firearm kill: +2 seconds (+40 ticks)
            extendBuff(player, 40);
        } else {
            // Melee kill: +3 seconds (+60 ticks)
            extendBuff(player, 60);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            clear(event.getEntityPlayer().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        if (event.player != null) {
            clear(event.player.getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        clear(null);
    }

    @SubscribeEvent
    public static void onWorldUnload(net.minecraftforge.event.world.WorldEvent.Unload event) {
        if (event.getWorld() != null && event.getWorld().isRemote) {
            clear(null);
        }
    }

    /**
     * Clears all state on player death / respawn / disconnect.
     */
    public static void clear(UUID id) {
        if (id == null) return;
        activationTicks.remove(id);
        activationDollSlots.remove(id);
        buffTicks.remove(id);
        serverSpeechTriggered.remove(id);
        lastNonDollSlots.remove(id);

        try {
            clearClientStateIfLocal(id);
        } catch (Throwable ignored) {}
    }

    @SideOnly(Side.CLIENT)
    private static void clearClientStateIfLocal(UUID id) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && (id == null || id.equals(mc.player.getUniqueID()))) {
            clientActivationRemainingTicks = 0;
            clientLockedDollSlot = -1;
            clientBuffRemainingTicks = 0;
            clientSpeechTriggered = false;
            targetDarknessAlpha = 0.0f;
            darknessAlpha = 0.0f;
            targetHudFadeAlpha = 1.0f;
            hudFadeAlpha = 1.0f;
            com.voltyx.mwccf.speech.client.SpeechClientManager.clearPersonal();
            // Also reset doll renderer so isDollHeld doesn't linger after death
            efw.util.ShoulderSurfingCompat.resetDollCamera();
            com.voltyx.mwccf.render.doll.DollRenderer.resetClientState();
            efw.util.MWCLoweringResetHelper.resetLoweringState();
        }
    }
}
