package com.voltyx.mwccf.network;

import com.voltyx.mwccf.dash.DashCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDash implements IMessage {
    private float dirX, dirZ;

    public PacketDash() {}
    public PacketDash(Vec3d dir) {
        this.dirX = (float) dir.x;
        this.dirZ = (float) dir.z;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(dirX);
        buf.writeFloat(dirZ);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dirX = buf.readFloat();
        dirZ = buf.readFloat();
    }

    public static class Handler implements IMessageHandler<PacketDash, IMessage> {
        @Override
        public IMessage onMessage(PacketDash message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                DashCapability.IDashData cap = player.getCapability(DashCapability.ROLL_CAP, null);
                if (cap == null || cap.getCooldown() > 0) return;
                if (!player.onGround) return;
                if (isReloadingMWC(player)) return;
                Vec3d dir = new Vec3d(message.dirX, 0, message.dirZ).normalize();
                if (dir.lengthSquared() == 0) return;

                cap.setDashing(true);
                cap.setDashDir(dir);
            });
            return null;
        }

        private static boolean isReloadingMWC(net.minecraft.entity.player.EntityPlayer player) {
            try {
                com.paneedah.weaponlib.ModContext context = com.paneedah.weaponlib.CommonModContext.getContext();
                if (context != null) {
                    com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance = (com.paneedah.weaponlib.PlayerWeaponInstance)
                        context.getPlayerItemInstanceRegistry().getMainHandItemInstance(player, com.paneedah.weaponlib.PlayerWeaponInstance.class);
                    if (weaponInstance != null) {
                        Object state = weaponInstance.getState();
                        if (state != null) {
                            String stateName = state.toString().toUpperCase();
                            if (stateName.contains("RELOAD") || stateName.contains("LOAD") || stateName.contains("UNLOAD")) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}
            return false;
        }
    }
}
