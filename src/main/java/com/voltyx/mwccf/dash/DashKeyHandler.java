package com.voltyx.mwccf.dash;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.network.PacketDash;
import efw.biomeinfo.MwccfConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class DashKeyHandler {

    public static final KeyBinding ROLL_KEY = new KeyBinding(
            "key.mwccf.dash",
            Keyboard.KEY_R,
            "key.categories.movement");

    public static void register() {
        ClientRegistry.registerKeyBinding(ROLL_KEY);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null) return;

        com.voltyx.mwccf.dash.DashCapability.IDashData cap = player.getCapability(com.voltyx.mwccf.dash.DashCapability.ROLL_CAP, null);
        if (cap != null && cap.getCooldown() > 0) {
            cap.setCooldown(cap.getCooldown() - 1);
        }

        // Block all vanilla swinging, breaking, and placing animations while rolling
        if (efw.AnimationTickHandler.isPlayerRolling(player)) {
            player.isSwingInProgress = false;
            player.swingProgressInt = 0;
            player.swingProgress = 0.0f;
            player.prevSwingProgress = 0.0f;
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            if (mc.playerController != null) {
                mc.playerController.resetBlockRemoving();
            }
        }

        if (ROLL_KEY.isPressed()) {
            if (!player.onGround) return;
            if (player.isInWater() || player.isInLava()) {
                if (!MwccfConfig.dashAndStamina.dash.allowDashInLiquids) return;
            }
            if (player.isSneaking() || player.height < 1.0F || player.isRiding() || player.isElytraFlying()) return;
            if (isReloadingMWC(player)) return;

            if (cap == null || cap.getCooldown() > 0) return;

            double currentStamina = player.getEntityData().getDouble("stamina");
            if (currentStamina < MwccfConfig.dashAndStamina.dash.staminaCost) return;

            Vec3d dir = getWASDDirection(player);
            if (dir.lengthSquared() > 0) {
                double distance = 0.475 * MwccfConfig.dashAndStamina.dash.speed;
                Vec3d velocity = dir.scale(distance);

                if (player.isInWater()) {
                    velocity = velocity.scale(0.5);
                } else if (player.isInLava()) {
                    velocity = velocity.scale(0.3);
                }

                net.minecraft.block.Block block = player.world.getBlockState(new net.minecraft.util.math.BlockPos(player.posX, player.getEntityBoundingBox().minY - 0.1, player.posZ)).getBlock();
                float slipperiness = block.slipperiness;
                float defaultSlipperiness = net.minecraft.init.Blocks.DIRT.slipperiness;
                if (slipperiness > defaultSlipperiness) {
                    float multiplier = defaultSlipperiness / slipperiness;
                    velocity = velocity.scale(multiplier * multiplier);
                }

                player.addVelocity(velocity.x, 0, velocity.z);
                
                // Immediately trigger roll animation without waiting for server roundtrip
                efw.AnimationTickHandler.triggerRoll(player, dir);

                // Immediately trigger visual HUD cooldown animation
                OverlayStamina.triggerAltDashAnimation();

                // Set client-side cooldown immediately
                if (cap != null) {
                    cap.setCooldown(MwccfConfig.dashAndStamina.dash.cooldownTicks);
                }

                MwccfMod.PACKET_HANDLER.sendToServer(new PacketDash(dir));
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onMouse(net.minecraftforge.client.event.MouseEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player != null) {
            com.voltyx.mwccf.dash.DashCapability.IDashData cap = player
                    .getCapability(com.voltyx.mwccf.dash.DashCapability.ROLL_CAP, null);
            if (cap != null && cap.isDashing()) {
                if (event.getDx() != 0 || event.getDy() != 0) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private Vec3d getWASDDirection(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
        boolean back = mc.gameSettings.keyBindBack.isKeyDown();
        boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
        boolean right = mc.gameSettings.keyBindRight.isKeyDown();

        if (!forward && !back && !left && !right)
            return Vec3d.ZERO;

        boolean isHoldingRanged = efw.animation.WeaponTypeHelper.getWeaponType(player.getHeldItemMainhand()) != efw.animation.WeaponTypeHelper.WeaponType.NONE;
        boolean doShoulderSurfing = false;
        boolean isAiming = false;
        boolean isLockedOn = false;
        int followTimer = 0;

        try {
            com.teamderpy.shouldersurfing.client.ShoulderInstance instance = com.teamderpy.shouldersurfing.client.ShoulderInstance.getInstance();
            if (instance != null) {
                doShoulderSurfing = instance.doShoulderSurfing();
                isAiming = instance.isAiming();
            }
            isLockedOn = com.teamderpy.shouldersurfing.lockon.LockOnHandler.lockedOn;
            followTimer = com.teamderpy.shouldersurfing.event.ClientEventHandler.followTimer;
        } catch (Throwable t) {}

        // При ShoulderSurfing без прицела, без оружия в руках и без локона — кувырок всегда идет вперед по направлению взгляда (rotationYaw)
        if (doShoulderSurfing && !isLockedOn && !isHoldingRanged && !isAiming && followTimer <= 0) {
            float yaw = player.rotationYaw * 0.017453292F;
            double dashX = -net.minecraft.util.math.MathHelper.sin(yaw);
            double dashZ = net.minecraft.util.math.MathHelper.cos(yaw);
            return new Vec3d(dashX, 0.0D, dashZ).normalize();
        }

        double x = 0, z = 0;
        if (forward)
            z += 1;
        if (back)
            z -= 1;
        if (left)
            x += 1;
        if (right)
            x -= 1;

        Vec3d dir = new Vec3d(x, 0, z);
        if (dir.lengthSquared() == 0)
            return Vec3d.ZERO;

        dir = dir.normalize();

        float yawRad = (float) Math.toRadians(player.rotationYaw);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);

        double rotatedX = dir.x * cos - dir.z * sin;
        double rotatedZ = dir.x * sin + dir.z * cos;

        return new Vec3d(rotatedX, 0, rotatedZ).normalize();
    }

    public static boolean isReloadingMWC(EntityPlayer player) {
        try {
            com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance = (com.paneedah.weaponlib.PlayerWeaponInstance)
                com.paneedah.weaponlib.ClientModContext.getContext()
                    .getPlayerItemInstanceRegistry()
                    .getMainHandItemInstance(player, com.paneedah.weaponlib.PlayerWeaponInstance.class);
            if (weaponInstance != null) {
                Object state = weaponInstance.getState();
                if (state != null) {
                    String stateName = state.toString().toUpperCase();
                    if (stateName.contains("RELOAD") || stateName.contains("LOAD") || stateName.contains("UNLOAD")) {
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) {}

        efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
        if (ap != null) {
            String act = ap.getCurrentActionName();
            if (act != null && act.contains("reload") && ap.isActionPlaying()) {
                return true;
            }
        }
        return false;
    }
}
