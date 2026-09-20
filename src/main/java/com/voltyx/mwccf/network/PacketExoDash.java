package com.voltyx.mwccf.network;

import com.voltyx.mwccf.ModSounds;
import com.voltyx.mwccf.armor.SurvivalInstinctArmorHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketExoDash implements IMessage {

    public PacketExoDash() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketExoDash, IMessage> {
        @Override
        public IMessage onMessage(PacketExoDash message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                if (!SurvivalInstinctArmorHandler.isWearingFullExo(player)) return;
                if (SurvivalInstinctArmorHandler.getExoDashCooldown(player) > 0) return;

                // Set cooldown (45 ticks = 2.25s)
                SurvivalInstinctArmorHandler.setExoDashCooldown(player, 45);

                // Play exo dash sound
                player.world.playSound(null, player.posX, player.posY, player.posZ,
                        ModSounds.EXO_DASH != null ? ModSounds.EXO_DASH : ModSounds.DASH,
                        SoundCategory.PLAYERS, 1.0F, 1.0F);

                // Apply propulsion impulse in look direction
                Vec3d look = player.getLookVec();
                player.motionX = player.motionX * 2.5 + look.x * 1.25;
                player.motionY = player.motionY + 0.5 + look.y * 0.15;
                player.motionZ = player.motionZ * 2.5 + look.z * 1.25;
                player.velocityChanged = true;

                // Spawn cloud particles
                if (player.world instanceof WorldServer) {
                    ((WorldServer) player.world).spawnParticle(EnumParticleTypes.CLOUD,
                            player.posX, player.posY + 0.5, player.posZ,
                            10, 0.2, 0.2, 0.2, 0.05);
                }
            });
            return null;
        }
    }
}
