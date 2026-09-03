package com.paneedah.mwc.network.handlers;

import com.paneedah.mwc.network.messages.ShellMessageClient;
import com.paneedah.weaponlib.ClientEventHandler;
import com.paneedah.weaponlib.render.shells.ShellParticleSimulator.Shell;
import io.redstudioragnarok.redcore.utils.NetworkUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.paneedah.mwc.proxies.ClientProxy.MC;

public final class ShellMessageHandler implements IMessageHandler<ShellMessageClient, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(final ShellMessageClient shellMessageClient, final MessageContext messageContext) {
        NetworkUtil.processMessage(messageContext, () -> {
            if (MC.player != null && MC.player.getEntityId() == shellMessageClient.getShooter())
                return;

            ClientEventHandler.lastShotTimeByEntity.put(shellMessageClient.getShooter(), System.currentTimeMillis());
            net.minecraft.entity.Entity shooter = MC.world != null ? MC.world.getEntityByID(shellMessageClient.getShooter()) : null;
            float yaw = shooter != null ? shooter.rotationYaw : 0f;
            ClientEventHandler.SHELL_MANAGER.enqueueShell(new Shell(shellMessageClient.getType(), shellMessageClient.getPosition().toVec3d(), new Vec3d(-90, 0, 180 + yaw), shellMessageClient.getVelocity().toVec3d()));
        });

        return null;
    }

    public ShellMessageHandler() {}

}