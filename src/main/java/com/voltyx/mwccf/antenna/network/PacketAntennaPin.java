package com.voltyx.mwccf.antenna.network;

import com.voltyx.mwccf.antenna.TileEntityAntenna;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAntennaPin implements IMessage {

    private BlockPos pos;
    private String pin;

    public PacketAntennaPin() {
    }

    public PacketAntennaPin(BlockPos pos, String pin) {
        this.pos = pos;
        this.pin = pin;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.pin = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        ByteBufUtils.writeUTF8String(buf, this.pin);
    }

    public static class Handler implements IMessageHandler<PacketAntennaPin, IMessage> {
        @Override
        public IMessage onMessage(PacketAntennaPin message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null && message.pos != null && message.pin != null) {
                player.getServerWorld().addScheduledTask(() -> {
                    TileEntity te = player.getServerWorld().getTileEntity(message.pos);
                    if (te instanceof TileEntityAntenna) {
                        TileEntityAntenna antenna = (TileEntityAntenna) te;
                        if (antenna.getPinCode().equals(message.pin) && !antenna.isLooted()) {
                            antenna.setUnlocked(true);
                            antenna.setOpen(true);
                            antenna.setLooted(true);

                            net.minecraft.item.ItemStack moduleStack = new net.minecraft.item.ItemStack(com.voltyx.mwccf.mcore.MCoreItems.INTERNET_MODULE);
                            if (!player.inventory.addItemStackToInventory(moduleStack)) {
                                player.dropItem(moduleStack, false);
                            }
                            player.inventoryContainer.detectAndSendChanges();

                            player.getServerWorld().playSound(null, message.pos,
                                    SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, 1.2F);
                            player.getServerWorld().playSound(null, message.pos,
                                    SoundEvents.BLOCK_NOTE_BELL, SoundCategory.BLOCKS, 1.0F, 2.0F);
                        } else if (!antenna.isLooted()) {
                            player.getServerWorld().playSound(null, message.pos,
                                    SoundEvents.BLOCK_NOTE_BASS, SoundCategory.BLOCKS, 1.0F, 0.6F);
                        }
                    }
                });
            }
            return null;
        }
    }
}
