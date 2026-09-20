package com.voltyx.mwccf.network;

import com.voltyx.mwccf.ModSounds;
import com.voltyx.mwccf.mcore.ItemCustomArmor;
import com.voltyx.mwccf.mcore.MCoreItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleNVG implements IMessage {

    public PacketToggleNVG() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketToggleNVG, IMessage> {
        @Override
        public IMessage onMessage(PacketToggleNVG message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (helmet.isEmpty()) return;

                Item item = helmet.getItem();
                if (com.voltyx.mwccf.armor.SurvivalInstinctArmorHandler.isNVGHelmet(item)) {
                    NBTTagCompound tag = helmet.hasTagCompound() ? helmet.getTagCompound() : new NBTTagCompound();
                    int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
                    boolean current = tag.hasKey("nv_active") && tag.getBoolean("nv_active") && charge > 0;
                    if (!current && charge <= 0) {
                        tag.setBoolean("nv_active", false);
                        helmet.setTagCompound(tag);
                        return;
                    }
                    boolean newState = !current;
                    tag.setBoolean("nv_active", newState);
                    helmet.setTagCompound(tag);

                    if (!newState) {
                        player.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
                    }

                    player.getServerWorld().getEntityTracker().sendToTracking(player,
                            new net.minecraft.network.play.server.SPacketEntityEquipment(player.getEntityId(), EntityEquipmentSlot.HEAD, helmet));

                    float pitch = newState ? 1.4F : 1.0F;
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            ModSounds.NVG_TOGGLE != null ? ModSounds.NVG_TOGGLE : net.minecraft.init.SoundEvents.BLOCK_LEVER_CLICK,
                            SoundCategory.PLAYERS, 0.8F, pitch);
                }
            });
            return null;
        }
    }
}
