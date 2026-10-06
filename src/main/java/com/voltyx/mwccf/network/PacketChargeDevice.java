package com.voltyx.mwccf.network;

import com.voltyx.mwccf.battery.DeviceBatteryHelper;
import com.voltyx.mwccf.geo.ItemBracelet;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChargeDevice implements IMessage {
    private int slotId;

    public PacketChargeDevice() {}

    public PacketChargeDevice(int slotId) {
        this.slotId = slotId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slotId);
    }

    public static class Handler implements IMessageHandler<PacketChargeDevice, IMessage> {
        @Override
        public IMessage onMessage(PacketChargeDevice message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (player.openContainer != null && message.slotId >= 0 && message.slotId < player.openContainer.inventorySlots.size()) {
                    Slot slot = player.openContainer.getSlot(message.slotId);
                    ItemStack target = slot.getStack();
                    ItemStack held = player.inventory.getItemStack(); // Предмет на курсоре мыши

                    if (!target.isEmpty() && DeviceBatteryHelper.isPoweredDevice(target)) {
                        // 1. Установка или свап батарейки / аккумулятора
                        if (!held.isEmpty() && DeviceBatteryHelper.isBattery(held)) {
                            ItemStack oldBattery = DeviceBatteryHelper.getInstalledBattery(target);

                            if (held.getCount() == 1) {
                                // Прямой свап: старая батарейка в курсор, новая в устройство
                                DeviceBatteryHelper.setInstalledBattery(target, held);
                                player.inventory.setItemStack(oldBattery);
                            } else {
                                // В руке стак: берем 1 штуку
                                ItemStack single = held.splitStack(1);
                                DeviceBatteryHelper.setInstalledBattery(target, single);
                                player.inventory.setItemStack(held);

                                if (!oldBattery.isEmpty()) {
                                    if (!player.inventory.addItemStackToInventory(oldBattery)) {
                                        player.dropItem(oldBattery, false);
                                    }
                                }
                            }

                            player.world.playSound(null, player.posX, player.posY, player.posZ,
                                    SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 0.8F, 1.2F);

                            player.sendSlotContents(player.openContainer, slot.slotNumber, target);
                            player.connection.sendPacket(new net.minecraft.network.play.server.SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
                            player.openContainer.detectAndSendChanges();
                            return;
                        }

                        // 2. Извлечение батарейки / аккумулятора пустым курсором
                        if (held.isEmpty()) {
                            ItemStack oldBattery = DeviceBatteryHelper.removeInstalledBattery(target);
                            if (!oldBattery.isEmpty()) {
                                player.inventory.setItemStack(oldBattery);
                                player.world.playSound(null, player.posX, player.posY, player.posZ,
                                        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 0.8F, 0.9F);

                                player.sendSlotContents(player.openContainer, slot.slotNumber, target);
                                player.connection.sendPacket(new net.minecraft.network.play.server.SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
                                player.openContainer.detectAndSendChanges();
                                return;
                            }
                        }
                    }

                    // 3. Заправка морфина в браслет
                    if (!held.isEmpty() && held.getItem() == com.voltyx.mwccf.item.ItemMorphineSyringe.INSTANCE) {
                        if (!target.isEmpty() && target.getItem() instanceof ItemBracelet) {
                            NBTTagCompound tag = target.getTagCompound();
                            int morphineCount = (tag != null && tag.hasKey("morphine_count")) ? tag.getInteger("morphine_count") : 0;
                            if (morphineCount < 6) {
                                if (tag == null) {
                                    tag = new NBTTagCompound();
                                    target.setTagCompound(tag);
                                }
                                tag.setInteger("morphine_count", morphineCount + 1);

                                held.shrink(1);
                                player.inventory.setItemStack(held.isEmpty() ? ItemStack.EMPTY : held);
                                player.world.playSound(null, player.posX, player.posY, player.posZ,
                                        SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 0.8F, 1.2F);
                                player.sendSlotContents(player.openContainer, slot.slotNumber, target);
                                player.connection.sendPacket(new net.minecraft.network.play.server.SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
                                player.openContainer.detectAndSendChanges();
                            }
                        }
                    }
                }
            });
            return null;
        }
    }
}
