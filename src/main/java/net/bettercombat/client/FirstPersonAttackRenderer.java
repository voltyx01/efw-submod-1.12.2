package net.bettercombat.client;

import com.paneedah.weaponlib.Weapon;
import efw.biomeinfo.MwccfConfig;
import efw.animation.AnimationClip;
import efw.animation.AnimationPlayer;
import efw.animation.AnimationRegistry;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.client.animation.AttackAnimationHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * First-person attack animation renderer for Better Combat on Minecraft 1.12.2.
 * Exact 1-to-1 port of original AnimationHandler.java from bettercombat-main:
 * - Exact SWEEP, CHOP, STAB kinematics and transforms
 * - Authentic camera pitch & yaw swing during attack strokes
 * - Authentic re-equip lower/raise settle animation (-1.0F) after attack ends
 */
@SideOnly(Side.CLIENT)
public class FirstPersonAttackRenderer {

    private static final float PI = (float) Math.PI;

    // Re-equip settle progress after attack ends (-1.0 to 0.0, matching original equippedProgressMainhand)
    private float equippedProgressMainhand = 0.0F;
    private float equippedProgressOffhand  = 0.0F;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        if (!MwccfConfig.betterCombat.enabled || !MwccfConfig.betterCombat.isFirstPersonAttackAnimationsEnabled) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || mc.world == null) return;
        if (efw.AnimationTickHandler.isPlayerCrawling(player)) return;
        if (isBetterCombatAttack(player)) {
            event.setCanceled(true);
            return;
        }

        boolean isAttackActive = (BetterCombatClient.swingTimer > 0) || BetterCombatClient.isUpswingActive();
        EnumHand hand = event.getHand();
        boolean isMainHand = (hand == EnumHand.MAIN_HAND);
        ItemStack renderedStack = isMainHand ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
        if (!renderedStack.isEmpty() && renderedStack.getItem() instanceof Weapon) return;

        // Re-equip settle animation when attack ends (faithful port of positionEquippedProgressMainhand)
        if (!isAttackActive) {
            float equipProg = isMainHand ? equippedProgressMainhand : equippedProgressOffhand;
            if (equipProg < 0.0F) {
                event.setCanceled(true);
                event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
                float fps = Minecraft.getDebugFPS();
                float delta = (fps <= 0.0F) ? 0.33F : (20.0F / fps);
                float increment = 2.0F / Math.max(1.0F, (float) BetterCombatClient.attackCooldown);
                equipProg += increment * delta;
                if (equipProg >= 0.0F) equipProg = 0.0F;
                if (isMainHand) equippedProgressMainhand = equipProg;
                else            equippedProgressOffhand  = equipProg;

                ItemStack stack = isMainHand ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
                if (!stack.isEmpty()) {
                    renderRestingWeapon(player, stack, isMainHand, equipProg);
                }
            }
            return;
        }

        ItemStack mainStack = player.getHeldItemMainhand();
        WeaponAttributes mainAttributes = WeaponRegistry.getAttributes(mainStack);
        if (mainAttributes == null) return;

        event.setCanceled(true);
        event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);

        boolean isTwoHanded = mainAttributes.isTwoHanded();
        if (!isMainHand && isTwoHanded) return;

        boolean isOffHandAttack = BetterCombatClient.currentIsOffHand;
        ItemStack stackToRender = isMainHand ? mainStack : player.getHeldItemOffhand();
        if (stackToRender == null || stackToRender.isEmpty()) return;

        if ( isMainHand && isOffHandAttack  && !MwccfConfig.betterCombat.isShowingOtherHandFirstPerson) return;
        if (!isMainHand && !isOffHandAttack && !isTwoHanded && !MwccfConfig.betterCombat.isShowingOtherHandFirstPerson) return;

        // Energy: 0 = animation start, 1 = animation end (1-to-1 with original calculateMainhandEnergy)
        float timer = Math.max(0.0f, (float) BetterCombatClient.swingTimer - event.getPartialTicks());
        float cap   = Math.max(1.0f, (float) BetterCombatClient.swingTimerCap);
        float energy = MathHelper.clamp(1.0f - (timer / cap), 0.0f, 1.0f);

        String anim = BetterCombatClient.currentAnimation != null
                ? BetterCombatClient.currentAnimation.toLowerCase() : "";

        // Prime re-equip settle when attack completes (faithful to original reequipAnimationMainhand)
        if (energy >= 0.92F) {
            boolean isStab = anim.contains("stab") || anim.contains("punch");
            if (isStab) {
                if (isMainHand) equippedProgressMainhand = 0.0F;
                else            equippedProgressOffhand  = 0.0F;
            } else {
                if (isMainHand) equippedProgressMainhand = -1.0F;
                else            equippedProgressOffhand  = -1.0F;
            }
        }

        renderAnimatedWeapon(player, stackToRender, isMainHand, energy, anim);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Resting render (re-equip settle) — identical to original positionMainWeapon
    // ─────────────────────────────────────────────────────────────────────────
    private void renderRestingWeapon(AbstractClientPlayer player, ItemStack stack,
                                     boolean isMainHand, float equipProg) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 0.685F, -0.6F + equipProg, -1.0F + equipProg * -0.25F);
        GlStateManager.rotate(-13.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * -13.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * -13.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(1.36F, 1.36F, 1.36F);

        ItemCameraTransforms.TransformType tt = isMainHand
                ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        mc.getRenderItem().renderItem(stack, player, tt, !isMainHand);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Animated render dispatcher
    // ─────────────────────────────────────────────────────────────────────────
    private void renderAnimatedWeapon(AbstractClientPlayer player, ItemStack stack,
                                      boolean isMainHand, float energy, String anim) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // ── 1. Base weapon position (identical to original positionMainWeapon) ──
        positionWeaponBase(isMainHand, anim);

        dispatchAnimation(energy, isMainHand, anim);
        applyCameraSwing(energy, anim, isMainHand);

        // ── 4. Scale & render (identical to original renderMainWeapon) ──
        GlStateManager.scale(1.36F, 1.36F, 1.36F);

        boolean isTranslucent = mc.getRenderItem().shouldRenderItemIn3D(stack)
                && Block.getBlockFromItem(stack.getItem()).getRenderLayer() == BlockRenderLayer.TRANSLUCENT;
        if (isTranslucent) GlStateManager.depthMask(false);

        ItemCameraTransforms.TransformType tt = isMainHand
                ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        mc.getRenderItem().renderItem(stack, player, tt, !isMainHand);

        if (isTranslucent) GlStateManager.depthMask(true);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    private boolean isBetterCombatAttack(AbstractClientPlayer player) {
        AnimationPlayer animationPlayer = AnimationRegistry.getPlayer(player);
        AnimationClip clip = animationPlayer.getActionClip();
        return animationPlayer.hasActionWeight() && clip != null && clip.isBetterCombat
                && AttackAnimationHelper.isVanillaWeaponAttack(player, animationPlayer.getAnimatedHand());
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Base weapon position — 1-to-1 from AnimationHandler.positionMainWeapon()
    // ─────────────────────────────────────────────────────────────────────────
    private boolean isSweep2(String anim, boolean isMainHand) {
        if (anim.contains("dual_handed_slash_cross")) {
            return !isMainHand;
        } else if (anim.contains("dual_handed_slash_uncross")) {
            return isMainHand;
        } else {
            return anim.contains("horizontal_left") || anim.contains("swipe") || anim.contains("sweep2");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Base weapon position — 1-to-1 from AnimationHandler.positionMainWeapon()
    // ─────────────────────────────────────────────────────────────────────────
    private void positionWeaponBase(boolean isMainHand, String anim) {
        float sign = isMainHand ? 1.0F : -1.0F;

        boolean isChop = anim.contains("chop") || anim.contains("vertical") || anim.contains("slam");
        boolean isStab = anim.contains("stab") || anim.contains("punch");
        boolean sweep2 = isSweep2(anim, isMainHand);

        if (sweep2) {
            GlStateManager.translate(sign * -0.685F, -0.6F, -1.0F);
        } else {
            GlStateManager.translate(sign * 0.685F, -0.6F, -1.0F);
        }

        if (isChop) {
            // Chopping rotation (Axe, Katana, Hammer, Slam in original Better Combat)
            GlStateManager.rotate(-11.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(sign * 0.02F, 0.08F, 0.0F);
            GlStateManager.rotate(sign * -16.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(sign * -8.0F,  0.0F, 0.0F, 1.0F);
        } else if (isStab) {
            // Spear / rapier / stab
            GlStateManager.rotate(-44.0F, 1.0F, 0.0F, 0.0F);
        } else if (sweep2) {
            // Mirrored sweep stance for Sweep 2 (Left-to-Right)
            GlStateManager.rotate(-13.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(sign * 13.0F,  0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(sign * 13.0F,  0.0F, 0.0F, 1.0F);
        } else {
            // Sweep stance (Sword, Greatsword, default in original Better Combat)
            GlStateManager.rotate(-13.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(sign * -13.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(sign * -13.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Camera pitch & yaw rotation during swing
    // ─────────────────────────────────────────────────────────────────────────
    private void applyCameraSwing(float energy, String anim, boolean isMainHand) {
        if (!isMainHand) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        float fps = Minecraft.getDebugFPS();
        float delta = (fps <= 0.0F) ? 0.33F : (20.0F / fps);
        float f = BetterCombatClient.swingTimerCap * delta;

        if (anim.contains("stab") || anim.contains("punch")) {
            float rotation = MathHelper.sin(-1.8F + energy * PI);
            mc.player.cameraPitch += rotation * 0.06F * f;
            mc.player.rotationYaw -= rotation * 0.16F * f;
        } else if (anim.contains("vertical_left")) {
            float rotation = MathHelper.cos(1.0F + energy * 2.18169F * PI);
            mc.player.cameraPitch -= rotation * 0.08F * f;
            mc.player.rotationYaw -= rotation * 0.16F * 0.3F * f;
        } else if (anim.contains("vertical_right")) {
            float rotation = MathHelper.cos(1.0F + energy * 2.18169F * PI);
            mc.player.cameraPitch -= rotation * 0.08F * f;
            mc.player.rotationYaw += rotation * 0.16F * 0.3F * f;
        } else if (anim.contains("chop") || anim.contains("slam")) {
            float rotation = MathHelper.cos(1.0F + energy * 2.18169F * PI);
            if (rotation > 0.0F) {
                mc.player.cameraPitch -= rotation * 0.06F * f;
            } else {
                mc.player.cameraPitch -= rotation * 0.06F * 2.0F * f;
            }
            mc.player.rotationYaw += rotation * 0.16F * 0.2F * f;
        } else if (isSweep2(anim, isMainHand)) {
            // Left to right sweep: camera yaw rotates right
            float rotation = MathHelper.sin(-0.3F + energy * 2.09549297F * PI);
            mc.player.cameraPitch -= rotation * 0.06F * f;
            mc.player.rotationYaw += rotation * 0.16F * f;
        } else {
            // Right to left sweep: camera yaw rotates left
            float rotation = MathHelper.sin(-0.3F + energy * 2.09549297F * PI);
            mc.player.cameraPitch -= rotation * 0.06F * f;
            mc.player.rotationYaw -= rotation * 0.16F * f;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Animation dispatch
    // ─────────────────────────────────────────────────────────────────────────
    private void dispatchAnimation(float energy, boolean isMainHand, String anim) {
        if (anim.contains("dual_handed_slash_cross")) {
            if (isMainHand) animationSweep1(energy, true);
            else            animationSweep2(energy, false);
        } else if (anim.contains("dual_handed_slash_uncross")) {
            if (isMainHand) animationSweep2(energy, true);
            else            animationSweep1(energy, false);
        } else if (anim.contains("spin")) {
            animationSpin(energy, isMainHand);
        } else if (anim.contains("uppercut")) {
            animationUppercut(energy, isMainHand);
        } else if (anim.contains("vertical_left")) {
            animationVerticalSlashLeft(energy, isMainHand);
        } else if (anim.contains("vertical_right")) {
            animationVerticalSlashRight(energy, isMainHand);
        } else if (anim.contains("chop") || anim.contains("slam")) {
            animationChop(energy, isMainHand);
        } else if (anim.contains("switch_blade")) {
            animationDagger(energy, isMainHand, anim.contains("left"));
        } else if (anim.contains("stab") || anim.contains("punch")) {
            animationStab(energy, isMainHand);
        } else if (anim.contains("horizontal_left") || anim.contains("swipe")) {
            animationSweep2(energy, isMainHand);
        } else {
            // Default: horizontal_right (right-to-left slash)
            animationSweep1(energy, isMainHand);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  1 & 2. Horizontal Sweeps (Sweep 1: Right-to-Left, Sweep 2: Left-to-Right)
    // ═════════════════════════════════════════════════════════════════════════
    private void renderSweep(float energy, boolean isMainHand, boolean isReverse) {
        float closeCap = 0.4F;

        // Authentic Better Combat rotations:
        float rotateUp = -bcClampMul(energy, 6.0F, 140.0F + closeCap * 40.0F);
        float rotateCounterClockwise = bcClampMul(energy, 12.0F, 150.0F)
                                      - bcClampMul(energy, 3.0F, 50.0F + closeCap * 100.0F)
                                      - energy * 15.0F;
        float rotateLeft = bcClampMul(energy, 6.0F, 85.0F);

        // Authentic Better Combat translations:
        float moveRight = bcClampMul(energy, 12.0F, 3.5F) + 0.5F;
        float moveUp    = bcClamp(energy * 10.0F, 0.47F);
        float moveClose = -bcClamp(energy * 10.0F, closeCap);

        // Extended travel so the blade crosses the full screen without stopping at center
        float maxTravel = 6.2F + closeCap;
        if (energy > 0.6F) {
            moveRight -= maxTravel - (1.0F - MathHelper.sin(energy * PI)) * 0.3F;
            if (energy > 0.85F) {
                float f = energy - 0.85F;
                moveUp    -= MathHelper.sin(f) * 6.0F;
                moveClose += MathHelper.sin(f) * 6.0F;
                moveRight += f * 17.0F;
            }
        } else {
            moveRight -= bcClamp(MathHelper.sin(energy * PI) * 7.5F, maxTravel);
        }

        // Exact mirror for Sweep 2 (Left-to-Right)
        if (isReverse) {
            moveRight = -moveRight;
            rotateCounterClockwise = -rotateCounterClockwise;
            rotateLeft = -rotateLeft;
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 1.2F * moveRight, 1.1F * moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    private void animationSweep1(float energy, boolean isMainHand) {
        renderSweep(energy, isMainHand, false);
    }

    private void animationSweep2(float energy, boolean isMainHand) {
        renderSweep(energy, isMainHand, true);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  3. Vertical Slash Left — Katana Hit 2 (Diagonal Cut on LEFT of center)
    // ═════════════════════════════════════════════════════════════════════════
    private void animationVerticalSlashLeft(float energy, boolean isMainHand) {
        float moveRight = -1.35F; // Net X = 0.685 + 1.1 * (-1.35) = -0.80F (left of center)
        float moveUp, moveClose, rotateUp;
        float rotateCounterClockwise = -20.0F;
        float rotateLeft = -25.0F;

        if (energy < 0.20F) {
            float f = 1.0F - MathHelper.cos(energy * PI * 2.5F);
            moveUp = MathHelper.sin(energy * PI * 4.6F) * 0.25F;
            moveClose = f * -0.3F;
            rotateUp = f * -95.0F;
        } else if (energy < 0.70F) {
            moveUp = 0.15F - (energy - 0.20F) * 1.0F;
            moveClose = -0.3F;
            rotateUp = -95.0F;
        } else {
            float f = energy - 0.70F;
            moveUp = -0.35F - f * 0.5F;
            moveClose = -0.3F + f * 5.0F;
            rotateUp = -95.0F + f * 40.0F;
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 1.1F * moveRight, moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  4. Vertical Slash Right — Katana Hit 3 (Diagonal Cut on RIGHT of center)
    // ═════════════════════════════════════════════════════════════════════════
    private void animationVerticalSlashRight(float energy, boolean isMainHand) {
        float moveRight = -0.15F; // Net X = 0.685 + 1.1 * (-0.15) = +0.52F (right of center)
        float moveUp, moveClose, rotateUp;
        float rotateCounterClockwise = 20.0F;
        float rotateLeft = 30.0F;

        if (energy < 0.20F) {
            float f = 1.0F - MathHelper.cos(energy * PI * 2.5F);
            moveUp = MathHelper.sin(energy * PI * 4.6F) * 0.25F;
            moveClose = f * -0.3F;
            rotateUp = f * -95.0F;
        } else if (energy < 0.70F) {
            moveUp = 0.15F - (energy - 0.20F) * 1.0F;
            moveClose = -0.3F;
            rotateUp = -95.0F;
        } else {
            float f = energy - 0.70F;
            moveUp = -0.35F - f * 0.5F;
            moveClose = -0.3F + f * 5.0F;
            rotateUp = -95.0F + f * 40.0F;
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 1.1F * moveRight, moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  5. Chop / Slam — Overhead Cleave (Axes, Halberds, Heavy Slams)
    // ═════════════════════════════════════════════════════════════════════════
    private void animationChop(float energy, boolean isMainHand) {
        float closeCap = -0.4F;

        float rotateCounterClockwise = bcClamp(energy * 300.0F, 15.0F) - energy * 15.0F;
        float rotateLeft             = bcClamp(energy * 100.0F, 30.0F);

        float moveRight = 0.0F, moveUp = 0.0F, moveClose = 0.0F, rotateUp = 0.0F;

        if (energy > 0.2F) {
            if (energy > 0.7F) {
                float f = energy - 0.7F;
                moveUp    = -f;
                moveRight = -0.7F + f * 2.0F;
                moveClose = closeCap + f * 6.0F;
                rotateUp  = -95.0F;
            } else {
                moveRight = -0.7F;
                moveClose = closeCap;
                rotateUp  = -95.0F;
                if (energy < 0.4F) {
                    rotateUp += MathHelper.sin((energy - 0.2F) * PI * 5.0F) * 5.0F;
                }
            }
        } else {
            float f = 1.0F - MathHelper.cos(energy * PI * 2.5F);
            moveRight = f * -0.7F;
            moveClose = f * closeCap;
            rotateUp  = f * -95.0F;
        }

        if (energy <= 0.22F) {
            moveUp = MathHelper.sin(energy * PI * 4.6F) * 0.2F;
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 1.1F * moveRight, moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  4. Stab — 1-to-1 from AnimationHandler.animationStabMainhand
    // ═════════════════════════════════════════════════════════════════════════
    private void animationStab(float energy, boolean isMainHand) {
        float closeCap = 0.6F;

        float rotateUp               = -bcClamp(energy * 240.0F, 60.0F);
        float rotateCounterClockwise =  bcClamp(energy * 125.0F, 50.0F) - energy * 10.0F;
        float rotateLeft             =  bcClamp(energy * 75.0F,  30.0F) - energy * 10.0F;

        float moveRight = 0.0F, moveUp = 0.0F, moveClose = 0.0F;

        if (energy > 0.2F) {
            if (energy > 0.4F) {
                if (energy > 0.8F) {
                    float f = energy - 0.8F;
                    moveClose = -closeCap + f * 2.5F;
                    moveRight = moveClose;
                    moveUp    = -moveClose;
                    float ff = f * f;
                    rotateUp               += bcClampMul(ff, 25.0F, 70.0F);
                    rotateCounterClockwise -= bcClampMul(ff, 25.0F, 30.0F);
                    rotateLeft             -= bcClampMul(ff, 25.0F, 10.0F);
                } else {
                    moveClose = -closeCap * (energy * 0.25F + 0.9F);
                    moveRight = moveClose;
                    moveUp    = -moveClose;
                }
            } else {
                moveClose = (0.2F - energy) * closeCap * 5.0F;
                moveRight = moveClose;
                moveUp    = -moveClose - 0.25F * closeCap;
            }
        } else {
            moveClose = MathHelper.sin(energy * PI * 5.0F) * 0.2F;
            moveUp    = -energy * closeCap * 2.5F;
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * moveRight, 1.3F * moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  5. Uppercut
    // ═════════════════════════════════════════════════════════════════════════
    private void animationUppercut(float energy, boolean isMainHand) {
        float closeCap = 0.4F;

        float rotateUp               = -bcClampMul(energy, 6.0F, 140.0F);
        float rotateCounterClockwise =  bcClampMul(energy, 12.0F, 150.0F)
                                      - bcClampMul(energy, 3.0F, 50.0F)
                                      - energy * 15.0F;
        float rotateLeft             =  bcClampMul(energy, 6.0F, 85.0F);

        float moveRight = bcClampMul(energy, 12.0F, 3.5F) + 0.5F;
        float moveUp    = bcClamp(energy * 10.0F, 0.60F);
        float moveClose = -bcClamp(energy * 10.0F, closeCap);

        if (energy > 0.6F) {
            moveRight -= 4.5F + closeCap - (1.0F - MathHelper.sin(energy * PI)) * 0.3F;
        } else {
            moveRight -= bcClamp(MathHelper.sin(energy * PI) * 5.5F, 4.5F + closeCap);
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 1.2F * moveRight, -1.1F * moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  6. 360° Spin
    // ═════════════════════════════════════════════════════════════════════════
    private void animationSpin(float energy, boolean isMainHand) {
        float closeCap = 0.4F;

        float rotateUp               = -bcClamp(energy * 200.0F, 80.0F);
        float rotateCounterClockwise =  bcClampMul(energy, 12.0F, 150.0F)
                                      - bcClampMul(energy, 3.0F, 50.0F)
                                      - energy * 15.0F
                                      - energy * 200.0F;
        float rotateLeft             =  bcClampMul(energy, 6.0F, 85.0F);

        float moveRight =  bcClampMul(energy, 12.0F, 3.5F) + 0.5F;
        float moveUp    =  bcClamp(energy * 10.0F, 0.47F);
        float moveClose = -bcClamp(energy * 10.0F, closeCap);

        if (energy > 0.6F) {
            moveRight -= 4.5F + closeCap - (1.0F - MathHelper.sin(energy * PI)) * 0.3F;
        } else {
            moveRight -= bcClamp(MathHelper.sin(energy * PI) * 5.5F, 4.5F + closeCap);
        }

        float sign = isMainHand ? 1.0F : -1.0F;
        GlStateManager.translate(sign * 1.2F * moveRight, 1.1F * moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  7. Dagger / Switch-blade
    // ═════════════════════════════════════════════════════════════════════════
    private void animationDagger(float energy, boolean isMainHand, boolean isLeft) {
        float f = MathHelper.sin(energy * PI);
        float dirMul = isLeft ? -1.0F : 1.0F;
        float sign   = isMainHand ? 1.0F : -1.0F;

        float moveRight = dirMul * f * 0.40F - 0.20F;
        float moveUp    = -MathHelper.sin(energy * PI * 2.0F) * 0.15F;
        float moveClose = -f * 0.25F;

        float rotateUp               = -f * 15.0F;
        float rotateCounterClockwise = dirMul * f * 60.0F;
        float rotateLeft             = dirMul * f * 30.0F;

        GlStateManager.translate(sign * moveRight, moveUp, moveClose);
        GlStateManager.rotate(rotateUp,                      1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * rotateCounterClockwise, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * rotateLeft,             0.0F, 0.0F, 1.0F);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Math helpers — 1-to-1 ported from AnimationHandler
    // ─────────────────────────────────────────────────────────────────────────
    private static float bcClamp(float f0, float f1) {
        return f0 > f1 ? f1 : f0;
    }

    private static float bcClampMul(float base, float multiplier, float cap) {
        float f = base * multiplier * cap;
        return f > cap ? cap : f;
    }
}