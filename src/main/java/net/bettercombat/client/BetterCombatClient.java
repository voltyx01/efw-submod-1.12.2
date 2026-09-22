package net.bettercombat.client;

import efw.biomeinfo.MwccfConfig;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.client.animation.AttackAnimationHelper;
import net.bettercombat.client.animation.PoseHelper;
import net.bettercombat.client.collision.TargetFinder;
import net.bettercombat.logic.AnimatedHand;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.PlayerAttackProperties;
import net.bettercombat.logic.WeaponRegistry;
import net.bettercombat.network.BetterCombatNetwork;
import net.bettercombat.network.PacketAttackAnimation;
import net.bettercombat.network.PacketAttackRequest;
import net.bettercombat.registry.BetterCombatSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class BetterCombatClient {
    public static int upswingTicks = 0;
    public static int attackCooldown = 0;
    public static int lastAttacked = 1000;
    public static int comboReset = 20;
    public static boolean isHarvesting = false;
    private static ItemStack upswingStack = ItemStack.EMPTY;
    private static ItemStack lastAttackedWithStack = ItemStack.EMPTY;

    public static boolean isUpswingActive() {
        return upswingTicks > 0;
    }

    public static int getComboCount() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        return PlayerAttackProperties.getComboCount(player);
    }

    public static void setComboCount(int count) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        PlayerAttackProperties.setComboCount(player, count);
    }

    public static boolean onAttackInput() {
        if (!MwccfConfig.betterCombat.enabled) {
            return false;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null) {
            return false;
        }

        if (upswingTicks > 0 || attackCooldown > 0) {
            return true;
        }

        // If holding a mineable block and targeting it
        if (isTargetingMineableBlock(mc, player)) {
            isHarvesting = true;
            return false; // let vanilla handle block mining
        }
        isHarvesting = false;

        ItemStack stack = player.getHeldItemMainhand();
        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
            startUpswing(attributes);
            return true; // handled by Better Combat!
        }
        return false;
    }

    public static boolean isTargetingMineableBlock(Minecraft mc, EntityPlayerSP player) {
        if (!MwccfConfig.betterCombat.isMiningWithWeaponsEnabled) {
            return false;
        }

        if (MwccfConfig.betterCombat.isAttackInsteadOfMineWhenEnemiesCloseEnabled) {
            ItemStack stack = player.getHeldItemMainhand();
            WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
            if (attributes != null) {
                List<Entity> targets = TargetFinder.getInitialTargets(player, null, attributes.attackRange());
                if (!targets.isEmpty()) {
                    return false;
                }
            }
        }

        RayTraceResult hit = mc.objectMouseOver;
        if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            IBlockState state = mc.world.getBlockState(pos);
            if (MwccfConfig.betterCombat.isSwingThruGrassEnabled) {
                if (state.getMaterial().isReplaceable() || state.getBlockHardness(mc.world, pos) == 0.0f) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void startUpswing(WeaponAttributes attributes) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || player.isRiding() || player.isHandActive()) {
            return;
        }

        AttackHand hand = PlayerAttackHelper.getCurrentAttack(player, getComboCount());
        if (hand == null) {
            return;
        }

        float upswingRate = (float) hand.upswingRate();
        if (upswingTicks > 0 || attackCooldown > 0 || player.isHandActive() || player.getCooledAttackStrength(0.0F) < (1.0F - upswingRate)) {
            return;
        }

        player.resetActiveHand();
        lastAttacked = 0;
        upswingStack = player.getHeldItemMainhand().copy();

        float cooldownTicks = PlayerAttackHelper.getAttackCooldownLengthTicks(player, hand.itemStack());
        comboReset = Math.max(10, Math.round(cooldownTicks * (float) MwccfConfig.betterCombat.comboResetRate));
        upswingTicks = Math.max(1, Math.round(cooldownTicks * upswingRate));
        int cooldownTicksInt = Math.max(1, Math.round(cooldownTicks));
        attackCooldown = cooldownTicksInt;

        String anim = hand.attack().animation();
        AnimatedHand animatedHand = AnimatedHand.from(hand.isOffHand(), attributes.isTwoHanded());

        // Play animation
        AttackAnimationHelper.playAttackAnimation(player, anim, animatedHand, cooldownTicks, upswingRate);

        // Play swing sound
        String soundId = "";
        WeaponAttributes.Sound soundConfig = hand.attack().swingSound();
        if (soundConfig != null && soundConfig.id() != null) {
            soundId = soundConfig.id();
            SoundEvent soundEvent = BetterCombatSounds.getSound(soundId);
            if (soundEvent != null) {
                float vol = (soundConfig.volume() * MwccfConfig.betterCombat.weaponSwingSoundVolume) / 100.0f;
                float pitch = soundConfig.pitch();
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(soundEvent, pitch * (1.0f + (player.getRNG().nextFloat() - 0.5f) * soundConfig.randomness())));
            }
        }

        // Send animation packet to server
        BetterCombatNetwork.NETWORK.sendToServer(new PacketAttackAnimation(
                player.getEntityId(),
                animatedHand.ordinal(),
                anim,
                cooldownTicks,
                upswingRate,
                soundId));
    }

    public static void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || mc.world == null) {
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }
        lastAttacked++;
        cancelSwingIfNeeded(player);
        attackFromUpswingIfNeeded(mc, player);
        resetComboIfNeeded(player);

        // Continuous attack (hold to attack)
        if (MwccfConfig.betterCombat.isHoldToAttackEnabled && mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (!isHarvesting && upswingTicks == 0 && attackCooldown == 0 && player.getCooledAttackStrength(0.0F) >= 0.9F) {
                WeaponAttributes attributes = WeaponRegistry.getAttributes(player.getHeldItemMainhand());
                if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
                    startUpswing(attributes);
                }
            }
        } else {
            isHarvesting = false;
        }

        // Update weapon idle poses
        PoseHelper.update(player);
        for (EntityPlayer other : mc.world.playerEntities) {
            if (other != player) {
                PoseHelper.update(other);
            }
        }
    }

    private static void cancelSwingIfNeeded(EntityPlayerSP player) {
        if ((upswingTicks > 0 || attackCooldown > 0) && !upswingStack.isEmpty()) {
            ItemStack current = player.getHeldItemMainhand();
            if (current.getItem() != upswingStack.getItem()) {
                upswingTicks = 0;
                attackCooldown = 0;
                upswingStack = ItemStack.EMPTY;
            }
        }
    }

    private static void attackFromUpswingIfNeeded(Minecraft mc, EntityPlayerSP player) {
        if (upswingTicks > 0) {
            upswingTicks--;
            if (upswingTicks == 0) {
                performAttack(mc, player);
                upswingStack = ItemStack.EMPTY;
            }
        }
    }

    private static void resetComboIfNeeded(EntityPlayerSP player) {
        if (lastAttacked > comboReset && getComboCount() > 0) {
            setComboCount(0);
        }

        ItemStack current = player.getHeldItemMainhand();
        if (!PlayerAttackHelper.shouldAttackWithOffHand(player, getComboCount())) {
            if (current.isEmpty() || (!lastAttackedWithStack.isEmpty() && lastAttackedWithStack.getItem() != current.getItem())) {
                setComboCount(0);
            }
        }
    }

    private static void performAttack(Minecraft mc, EntityPlayerSP player) {
        int combo = getComboCount();
        AttackHand hand = PlayerAttackHelper.getCurrentAttack(player, combo);
        if (hand == null) {
            return;
        }

        Entity cursorTarget = mc.pointedEntity;
        double range = hand.attributes().attackRange();
        List<Entity> targets = TargetFinder.findAttackTargets(player, cursorTarget, hand.attack(), range);

        int[] targetIds = new int[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            targetIds[i] = targets.get(i).getEntityId();
        }

        // Send attack packet to server
        BetterCombatNetwork.NETWORK.sendToServer(new PacketAttackRequest(
                combo,
                player.isSneaking(),
                player.inventory.currentItem,
                targetIds));

        // Client prediction: attack each target
        for (Entity target : targets) {
            player.attackTargetEntityWithCurrentItem(target);
        }

        player.resetCooldown();
        setComboCount(combo + 1);
        lastAttacked = 0;

        if (!hand.isOffHand()) {
            lastAttackedWithStack = hand.itemStack().copy();
        }
    }

    public static void handleAnimationPacket(PacketAttackAnimation message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;

        Entity entity = mc.world.getEntityByID(message.getPlayerId());
        if (entity instanceof EntityPlayer && entity != mc.player) {
            EntityPlayer player = (EntityPlayer) entity;
            AnimatedHand hand = message.getAnimatedHandOrdinal() >= 0 && message.getAnimatedHandOrdinal() < AnimatedHand.values().length
                    ? AnimatedHand.values()[message.getAnimatedHandOrdinal()]
                    : AnimatedHand.MAIN_HAND;

            AttackAnimationHelper.playAttackAnimation(player, message.getAnimationName(), hand, message.getLength(), message.getUpswing());

            if (!message.getSoundId().isEmpty()) {
                SoundEvent sound = BetterCombatSounds.getSound(message.getSoundId());
                if (sound != null) {
                    float vol = (1.0f * MwccfConfig.betterCombat.weaponSwingSoundVolume) / 100.0f;
                    mc.world.playSound(player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, vol, 1.0f, false);
                }
            }
        }
    }
}
