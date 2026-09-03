package com.voltyx.mwccf.sins.capability;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.sins.network.PacketSyncSinData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class SinCapabilityEvents {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(SinCapabilityProvider.KEY, new SinCapabilityProvider());
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer original = event.getOriginal();
        EntityPlayer newPlayer = event.getEntityPlayer();

        if (original.hasCapability(SinCapabilityProvider.SIN_CAP, null) &&
            newPlayer.hasCapability(SinCapabilityProvider.SIN_CAP, null)) {
            
            ISinCapability origCap = original.getCapability(SinCapabilityProvider.SIN_CAP, null);
            ISinCapability newCap = newPlayer.getCapability(SinCapabilityProvider.SIN_CAP, null);
            
            if (origCap != null && newCap != null) {
                // Полностью переносим все данные греха при смерти и при смене измерений
                newCap.copyFrom(origCap);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            syncToPlayer((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            syncToPlayer((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            syncToPlayer((EntityPlayerMP) event.player);
        }
    }

    public static void syncToPlayer(EntityPlayerMP player) {
        if (player.hasCapability(SinCapabilityProvider.SIN_CAP, null)) {
            ISinCapability cap = player.getCapability(SinCapabilityProvider.SIN_CAP, null);
            if (cap != null) {
                NBTTagCompound nbt = cap.writeToNBT();
                MwccfMod.PACKET_HANDLER.sendTo(new PacketSyncSinData(nbt), player);
            }
        }
    }
}
