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
import net.bettercombat.network.ServerAttackHandler;
import net.bettercombat.registry.BetterCombatSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class BetterCombatClient {
    public static int swingTimer = 0;
    public static int swingTimerCap = 0;
    public static int swingTimestampSound = 0;
    public static int swingTimestampDamage = 0;
    public static int upswingTicks = 0;
    public static int attackCooldown = 0;
    public static int lastAttacked = 1000;
    public static float lastSwingDuration = 0.0f;
    public static boolean isPerformingAttack = false;
    public static int comboReset = 20;
    public static boolean isHarvesting = false;
    private static ItemStack upswingStack = ItemStack.EMPTY;
    private static ItemStack lastAttackedWithStack = ItemStack.EMPTY;
    private static ItemStack lastHeldStack = ItemStack.EMPTY;

    public static String currentAnimation = "";
    public static boolean currentIsOffHand = false;
    public static boolean currentIsDualHanded = false;

    // Stored swing sound for deferred playback
    private static String pendingSwingSoundId = "";
    private static WeaponAttributes.Sound pendingSwingSoundConfig = null;

    // Queued attack input buffer for seamless combo chaining without dropped clicks
    private static int attackBufferTicks = 0;

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

    public static boolean isAttackHeld(Minecraft mc) {
        if (mc == null || mc.gameSettings == null || mc.gameSettings.keyBindAttack == null) return false;
        if (mc.gameSettings.keyBindAttack.isKeyDown()) return true;
        if (mc.inGameHasFocus && mc.gameSettings.keyBindAttack.getKeyCode() < 0) {
            int button = mc.gameSettings.keyBindAttack.getKeyCode() + 100;
            if (button >= 0 && button <= 15 && org.lwjgl.input.Mouse.isButtonDown(button)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canStartAttack(EntityPlayerSP player) {
        if (player == null || player.isRiding() || player.isHandActive()) {
            return false;
        }
        efw.animation.AnimationPlayer localAp = efw.animation.AnimationRegistry.getPlayer(player);
        if (localAp != null && localAp.isRollPlaying()) {
            return false;
        }
        if (attackCooldown > 0 || upswingTicks > 0) {
            return false;
        }
        AttackHand hand = PlayerAttackHelper.getCurrentAttack(player, getComboCount());
        if (hand == null) {
            return false;
        }
        float upswingRate = (float) hand.upswingRate();
        float cooledStrength = player.getCooledAttackStrength(0.0f);
        return cooledStrength >= (1.0f - upswingRate - 0.01f);
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

        ItemStack stack = player.getHeldItemMainhand();
        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        if (attributes == null || attributes.attacks() == null || attributes.attacks().length == 0) {
            return false;
        }

        // If holding a mineable block and targeting it
        if (isTargetingMineableBlock(mc, player)) {
            isHarvesting = true;
            return false; // let vanilla handle block mining
        }
        isHarvesting = false;

        efw.animation.AnimationPlayer localAp = efw.animation.AnimationRegistry.getPlayer(player);
        if (localAp != null && localAp.isRollPlaying()) {
            attackBufferTicks = 12;
            return true;
        }

        if (canStartAttack(player)) {
            startUpswing(attributes);
            attackBufferTicks = 0;
            return true;
        }

        // Input buffering: queue attack during recovery so combos flow seamlessly without dropped clicks
        if (upswingTicks == 0 && attackCooldown > 0 && attackCooldown <= 6) {
            attackBufferTicks = 6;
            return true;
        }
        return false;
    }

    public static boolean isNonMiningWeapon(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        net.minecraft.item.Item item = stack.getItem();
        if (item instanceof net.minecraft.item.ItemSword) {
            return true;
        }
        if (item instanceof com.voltyx.mwccf.si.ItemSIFist) {
            return true;
        }
        if (item instanceof net.minecraft.item.ItemTool) {
            if (item instanceof net.minecraft.item.ItemAxe || item instanceof net.minecraft.item.ItemPickaxe || item instanceof net.minecraft.item.ItemSpade) {
                return false;
            }
        }
        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        if (attributes != null) {
            String cat = attributes.category();
            if (cat != null) {
                String c = cat.toLowerCase();
                if (c.contains("axe") || c.contains("pickaxe") || c.contains("shovel") || c.contains("hoe")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isTargetingMineableBlock(Minecraft mc, EntityPlayerSP player) {
        if (!MwccfConfig.betterCombat.isMiningWithWeaponsEnabled) {
            return false;
        }

        ItemStack stack = player.getHeldItemMainhand();
        if (isNonMiningWeapon(stack)) {
            return false;
        }

        RayTraceResult hit = mc.objectMouseOver;
        if (hit == null || hit.typeOfHit != RayTraceResult.Type.BLOCK) {
            return false;
        }

        BlockPos pos = hit.getBlockPos();
        IBlockState state = mc.world.getBlockState(pos);
        if (state.getBlock().isAir(state, mc.world, pos)) {
            return false;
        }

        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
            WeaponAttributes.Attack attack = attributes.attacks()[getComboCount() % attributes.attacks().length];
            List<Entity> visibleTargets = TargetFinder.findAttackTargets(player, mc.pointedEntity, attack, attributes.attackRange());

            // If swing-thru-grass is enabled and we are hitting grass/replaceable/0-hardness block:
            // ONLY skip mining if there is actually an enemy to hit behind it!
            if (MwccfConfig.betterCombat.isSwingThruGrassEnabled) {
                if (state.getMaterial().isReplaceable() || state.getBlockHardness(mc.world, pos) == 0.0f) {
                    if (!visibleTargets.isEmpty()) {
                        return false; // swing through grass to hit the enemy
                    }
                    return true; // no enemy, break the grass/flower/torch
                }
            }

            // If attack-instead-of-mine is enabled:
            // ONLY prioritize attack if there is a visible target in front of the player within attack range!
            if (MwccfConfig.betterCombat.isAttackInsteadOfMineWhenEnemiesCloseEnabled) {
                if (!visibleTargets.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void startUpswing(WeaponAttributes attributes) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || player.isRiding() || player.isHandActive()) {
            return;
        }

        efw.animation.AnimationPlayer localAp = efw.animation.AnimationRegistry.getPlayer(player);
        if (localAp != null) {
            if (localAp.isRollPlaying()) {
                return;
            }
            if (localAp.isRollActive(0.0f)) {
                localAp.snapRoll();
            }
        }

        if (attackCooldown > 0 || upswingTicks > 0) {
            return;
        }

        AttackHand hand = PlayerAttackHelper.getCurrentAttack(player, getComboCount());
        if (hand == null) {
            return;
        }

        float upswingRate = (float) hand.upswingRate();
        if (player.getCooledAttackStrength(0.0f) < (1.0f - upswingRate - 0.01f)) {
            return;
        }

        player.resetActiveHand();
        upswingStack = player.getHeldItemMainhand().copy();

        float cooldownTicks = PlayerAttackHelper.getAttackCooldownLengthTicks(player, hand.itemStack());
        int cooldownTicksInt = Math.max(1, Math.round(cooldownTicks));
        attackCooldown = cooldownTicksInt;
        lastSwingDuration = cooldownTicks;
        lastAttacked = 0;
        comboReset = Math.max(10, Math.round(cooldownTicks * (float) MwccfConfig.betterCombat.comboResetRate));

        String anim = hand.attack().animation();
        AnimatedHand animatedHand = AnimatedHand.from(hand.isOffHand(), attributes.isTwoHanded());

        currentAnimation = anim != null ? anim : "";
        currentIsOffHand = hand.isOffHand();
        currentIsDualHanded = (animatedHand == AnimatedHand.DUAL_HANDED);

        int strikeTicks = Math.max(1, Math.round(cooldownTicks * upswingRate));
        upswingTicks = strikeTicks;

        // Visual swing duration: faithfully match original Better Combat (MathHelper.clamp(i, 3, 14) - 2)
        int visualTicks = Math.max(4, MathHelper.clamp(cooldownTicksInt, 4, 14) - 2);
        swingTimerCap = visualTicks;
        swingTimer = visualTicks;

        // Store swing sound
        pendingSwingSoundId = "";
        pendingSwingSoundConfig = null;
        WeaponAttributes.Sound soundConfig = hand.attack().swingSound();
        if (soundConfig != null && soundConfig.id() != null) {
            pendingSwingSoundId = soundConfig.id();
            pendingSwingSoundConfig = soundConfig;
        }

        // Swing player's arm for vanilla first-person item rendering
        EnumHand swingHand = hand.isOffHand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        player.swingArm(swingHand);

        // Play third-person animation synchronized to visual swing timer and strike ticks (if not crawling)
        if (!efw.AnimationTickHandler.isPlayerCrawling(player)) {
            AttackAnimationHelper.playAttackAnimation(player, anim, animatedHand, cooldownTicks, upswingRate);

            // Send animation packet to server
            BetterCombatNetwork.NETWORK.sendToServer(new PacketAttackAnimation(
                    player.getEntityId(),
                    animatedHand.ordinal(),
                    anim,
                    cooldownTicks,
                    upswingRate,
                    pendingSwingSoundId));
        }
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

        if (swingTimer > 0) {
            swingTimer--;
            if (swingTimer == 0) {
                currentAnimation = "";
                currentIsOffHand = false;
                currentIsDualHanded = false;
            }
        }

        if (upswingTicks > 0) {
            upswingTicks--;
            if (upswingTicks == 0) {
                playSwingSound(mc, player);
                performAttack(mc, player);
                upswingStack = ItemStack.EMPTY;
            }
        }

        // Track weapon swap to apply equip cooldown for BetterCombat weapons
        ItemStack currentHeld = player.getHeldItemMainhand();
        if (currentHeld.getItem() != lastHeldStack.getItem()) {
            lastHeldStack = currentHeld.copy();
            WeaponAttributes attributes = WeaponRegistry.getAttributes(currentHeld);
            if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
                float cd = PlayerAttackHelper.getAttackCooldownLengthTicks(player, currentHeld);
                lastSwingDuration = cd;
                lastAttacked = 0;
                attackCooldown = Math.max(1, Math.round(cd));
            }
        }

        cancelSwingIfNeeded(player);
        resetComboIfNeeded(player);

        // Process buffered attack input (click queued during recovery)
        if (attackBufferTicks > 0) {
            attackBufferTicks--;
            if (canStartAttack(player)) {
                ItemStack held = player.getHeldItemMainhand();
                WeaponAttributes attributes = WeaponRegistry.getAttributes(held);
                if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
                    if (!isTargetingMineableBlock(mc, player)) {
                        startUpswing(attributes);
                        attackBufferTicks = 0;
                    }
                }
            }
        }

        // Manage attack animation completion for the local player:
        // Fade out smoothly once the attack finishes and no further attack is queued or held
        efw.animation.AnimationPlayer localAp = efw.animation.AnimationRegistry.getPlayer(player);
        ItemStack heldMain = player.getHeldItemMainhand();
        boolean isBCWeaponHeld = !heldMain.isEmpty() && WeaponRegistry.getAttributes(heldMain) != null;
        boolean attackHeld = MwccfConfig.betterCombat.isHoldToAttackEnabled && isBCWeaponHeld && isAttackHeld(mc);
        if (localAp != null) {
            boolean hasQueuedAttack = (attackBufferTicks > 0);
            boolean isHoldingAttack = attackHeld && !isHarvesting;
            boolean isAttackActive = (attackCooldown > 0 || upswingTicks > 0);

            if (!hasQueuedAttack && !isHoldingAttack && !isAttackActive
                    && localAp.isActionAttack()
                    && localAp.getActionClip() != null && localAp.getActionClip().isBetterCombat
                    && !localAp.isActionFadingOut()) {
                localAp.stopAction(5);
            }
        }

        // Continuous attack (hold to attack)
        if (attackHeld) {
            if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
                net.minecraft.client.settings.KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
            }
            if (isTargetingMineableBlock(mc, player)) {
                isHarvesting = true;
            } else {
                isHarvesting = false;
                if (localAp != null && localAp.isRollPlaying()) {
                    attackBufferTicks = 6;
                } else if (canStartAttack(player)) {
                    ItemStack held = player.getHeldItemMainhand();
                    WeaponAttributes attributes = WeaponRegistry.getAttributes(held);
                    if (attributes != null && attributes.attacks() != null && attributes.attacks().length > 0) {
                        startUpswing(attributes);
                    }
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
        efw.animation.AnimationPlayer localAp = efw.animation.AnimationRegistry.getPlayer(player);
        if (player.getHeldItemMainhand().isEmpty()) {
            if (swingTimer > 0 || attackCooldown > 0 || isUpswingActive()) {
                cancelCurrentSwing(player);
            }
            return;
        }

        if (localAp != null && localAp.isRollPlaying()) {
            if (swingTimer > 0 || upswingTicks > 0 || isUpswingActive() || (localAp.isActionAttack() && localAp.hasActionWeight())) {
                cancelCurrentSwing(player, false);
            }
            return;
        }

        if ((swingTimer > 0 || attackCooldown > 0) && !upswingStack.isEmpty()) {
            ItemStack current = player.getHeldItemMainhand();
            if (current.getItem() != upswingStack.getItem()) {
                cancelCurrentSwing(player);
            }
        }
    }

    public static void cancelCurrentSwing(EntityPlayer player) {
        cancelCurrentSwing(player, true);
    }

    public static void cancelCurrentSwing(EntityPlayer player, boolean clearBuffer) {
        swingTimer = 0;
        swingTimerCap = 0;
        attackCooldown = 0;
        upswingTicks = 0;
        if (clearBuffer) {
            attackBufferTicks = 0;
        }
        upswingStack = ItemStack.EMPTY;
        currentAnimation = "";
        currentIsOffHand = false;
        currentIsDualHanded = false;
        if (player != null) {
            efw.animation.AnimationPlayer localAp = efw.animation.AnimationRegistry.getPlayer(player);
            if (localAp != null && localAp.isActionAttack()) {
                localAp.snapAction();
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

    private static void playSwingSound(Minecraft mc, EntityPlayerSP player) {
        SoundEvent soundEvent = null;
        float pitch = 1.0f;
        if (pendingSwingSoundConfig != null && pendingSwingSoundConfig.id() != null) {
            soundEvent = BetterCombatSounds.getSound(pendingSwingSoundConfig.id());
            pitch = pendingSwingSoundConfig.pitch() * (1.0f + (player.getRNG().nextFloat() - 0.5f) * pendingSwingSoundConfig.randomness());
        } else if (!pendingSwingSoundId.isEmpty()) {
            soundEvent = BetterCombatSounds.getSound(pendingSwingSoundId);
        }
        if (soundEvent == null) {
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof ItemAxe) {
                soundEvent = BetterCombatSounds.getSound("axe_slash");
            } else {
                soundEvent = BetterCombatSounds.getSound("sword_slash");
            }
        }
        if (soundEvent != null) {
            final SoundEvent snd = soundEvent;
            final float p = pitch;
            mc.addScheduledTask(() ->
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(snd, p)));
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
        isPerformingAttack = true;
        try {
            for (Entity target : targets) {
                net.bettercombat.utils.AttackCooldownHelper.setFullAttackStrength(player);
                player.attackTargetEntityWithCurrentItem(target);
            }
        } finally {
            isPerformingAttack = false;
        }

        if (!targets.isEmpty() && ServerAttackHandler.isHorizontalOrSpinAttack(hand.attack())) {
            spawnClientSweepParticles(player, hand.attack());
        }

        setComboCount(combo + 1);
        player.resetCooldown();

        if (!hand.isOffHand()) {
            lastAttackedWithStack = hand.itemStack().copy();
        }
    }

    public static void spawnClientSweepParticles(EntityPlayerSP player, WeaponAttributes.Attack attack) {
        if (player == null || player.world == null || attack == null) return;
        if (ServerAttackHandler.isSpinAttack(attack)) {
            for (int i = 0; i < 8; i++) {
                double rad = Math.toRadians(player.rotationYaw + (i * 45.0));
                double px = -Math.sin(rad) * 1.35;
                double pz = Math.cos(rad) * 1.35;
                player.world.spawnParticle(
                        EnumParticleTypes.SWEEP_ATTACK,
                        player.posX + px,
                        player.posY + player.height * 0.5,
                        player.posZ + pz,
                        0.0, 0.0, 0.0);
            }
        } else if (attack.angle() >= 150.0) {
            double[] offsets = new double[] { -40.0, 0.0, 40.0 };
            for (double off : offsets) {
                double rad = Math.toRadians(player.rotationYaw + off);
                double px = -Math.sin(rad) * 1.15;
                double pz = Math.cos(rad) * 1.15;
                player.world.spawnParticle(
                        EnumParticleTypes.SWEEP_ATTACK,
                        player.posX + px,
                        player.posY + player.height * 0.5,
                        player.posZ + pz,
                        0.0, 0.0, 0.0);
            }
        } else {
            ItemStack stack = player.getHeldItemMainhand();
            if (!(stack.getItem() instanceof ItemSword)) {
                double rad = Math.toRadians(player.rotationYaw);
                double px = -Math.sin(rad) * 1.0;
                double pz = Math.cos(rad) * 1.0;
                player.world.spawnParticle(
                        EnumParticleTypes.SWEEP_ATTACK,
                        player.posX + px,
                        player.posY + player.height * 0.5,
                        player.posZ + pz,
                        0.0, 0.0, 0.0);
            }
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

    public static float getCooledAttackStrength(EntityPlayer player, float adjustTicks) {
        if (attackCooldown <= 0) {
            return 1.0f;
        }
        if (lastSwingDuration <= 0.0f) {
            return 1.0f;
        }
        float remaining = Math.max(0.0f, (float) attackCooldown - adjustTicks);
        float progress = 1.0f - (remaining / lastSwingDuration);
        return MathHelper.clamp(progress, 0.0f, 1.0f);
    }
}
