package com.voltyx.gender.main;

import com.voltyx.gender.api.IGenderArmor;
import com.voltyx.gender.gui.screen.WildfirePlayerListScreen;
import com.voltyx.gender.main.networking.PacketSendGenderInfo;
import com.voltyx.gender.render.GenderLayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class WildfireEventHandler {

    // В 1.12.2 используется KeyBinding вместо KeyMapping, и org.lwjgl.input.Keyboard
    public static final KeyBinding toggleEditGUI = new KeyBinding("key.wildfire_gender.gender_menu", Keyboard.KEY_G, "category.wildfire_gender.generic");

    private int timer = 0;
    private boolean showedWelcomeMessage = false;

    // В 1.12.2 нет кустов ягод и замерзания, используем то, что есть
    private final Set<SoundEvent> playerHurtSounds = new HashSet<>(Arrays.asList(
            SoundEvents.ENTITY_PLAYER_HURT,
            SoundEvents.ENTITY_PLAYER_HURT_DROWN,
            SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE
    ));

    /**
     * ВНИМАНИЕ: Вызови этот метод в ClientProxy в фазе init() или postInit()
     * Это заменит событие AddLayers из 1.18.2
     */
    public static void injectLayers() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        for (RenderPlayer renderPlayer : skinMap.values()) {
            ((net.minecraft.client.renderer.entity.RenderLivingBase) renderPlayer).addLayer(new GenderLayer(renderPlayer));
        }
    }

    public static void registerKeybinds() {
        ClientRegistry.registerKeyBinding(toggleEditGUI);
        MinecraftForge.EVENT_BUS.register(new WildfireEventHandler());
    }

    @SubscribeEvent
    public void onGUI(TickEvent.ClientTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) return;

        if (Minecraft.getMinecraft().world == null) {
            WildfireGender.CLOTHING_PLAYERS.clear();
            showedWelcomeMessage = false;
        } else {
            // Заменил сложные Toasts на простое сообщение в чат для надежности на 1.12.2
            if (!showedWelcomeMessage && Minecraft.getMinecraft().player != null) {
                if (((net.minecraft.entity.Entity) Minecraft.getMinecraft().player).ticksExisted > 100) {
                    showedWelcomeMessage = true;
                }
            }
        }

        // Синхронизация (отправка пакетов)
        timer++;
        if (timer >= 5) {
            try {
                if (Minecraft.getMinecraft().player != null) {
                    GenderPlayer aPlr = WildfireGender.getPlayerById(Minecraft.getMinecraft().player.getUniqueID());
                    if (aPlr != null && aPlr.needsSync) {
                        // Здесь мы будем вызывать наш SimpleNetworkWrapper для отправки на сервер
                        PacketSendGenderInfo.send(aPlr);
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            timer = 0;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END && evt.side.isClient()) {
            GenderPlayer aPlr = WildfireGender.getPlayerById(evt.player.getUniqueID());
            if (aPlr == null) return;
            IGenderArmor armor = WildfireHelper.getArmorConfig(evt.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
            aPlr.getLeftBreastPhysics().update(evt.player, armor);
            aPlr.getRightBreastPhysics().update(evt.player, armor);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent evt) {
        if (toggleEditGUI.isPressed()) {
            if (WildfireGender.modEnabled) {
                WildfireGender.refreshAllGenders();
                // Screen в 1.18.2 = GuiScreen в 1.12.2
                Minecraft.getMinecraft().displayGuiScreen(new WildfirePlayerListScreen(Minecraft.getMinecraft()));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent evt) {
        if (evt.getWorld().isRemote && evt.getEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer plr = (AbstractClientPlayer) evt.getEntity();
            UUID uuid = plr.getUniqueID();
            GenderPlayer aPlr = WildfireGender.getPlayerById(uuid);
            if (aPlr == null) {
                aPlr = new GenderPlayer(uuid);
                WildfireGender.CLOTHING_PLAYERS.put(uuid, aPlr);

                // Помечаем для синхронизации, если это сам клиент
                WildfireGender.loadGenderInfoAsync(uuid, uuid.equals(Minecraft.getMinecraft().player.getUniqueID()));
                WildfireGender.refreshAllGenders();
            }
        }
    }


}