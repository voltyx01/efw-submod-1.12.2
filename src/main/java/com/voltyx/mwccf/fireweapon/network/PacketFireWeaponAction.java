package com.voltyx.mwccf.fireweapon.network;

import com.voltyx.mwccf.fireweapon.FireWeaponHelper;
import efw.item.ItemCloth;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFireWeaponAction implements IMessage {

    public static final byte ACTION_WRAP   = 0;
    public static final byte ACTION_IGNITE = 1;
    public static final byte ACTION_SOAK   = 2;

    private int windowId;
    private int slotId;
    private byte action;

    public PacketFireWeaponAction() {}

    public PacketFireWeaponAction(int windowId, int slotId, byte action) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.action = action;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.windowId = buf.readInt();
        this.slotId = buf.readInt();
        this.action = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.windowId);
        buf.writeInt(this.slotId);
        buf.writeByte(this.action);
    }

    public static class Handler implements IMessageHandler<PacketFireWeaponAction, IMessage> {
        @Override
        public IMessage onMessage(PacketFireWeaponAction msg, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                Container container = player.openContainer;
                if (container == null || container.windowId != msg.windowId) {
                    container = player.inventoryContainer;
                }

                if (msg.slotId < 0 || msg.slotId >= container.inventorySlots.size()) return;
                Slot slot = container.getSlot(msg.slotId);
                if (slot == null || !slot.getHasStack()) return;

                ItemStack targetStack = slot.getStack();
                if (!FireWeaponHelper.isWeapon(targetStack)) return;

                ItemStack cursorStack = player.inventory.getItemStack();
                if (cursorStack.isEmpty()) return;

                if (msg.action == ACTION_WRAP) {
                        if (cursorStack.getItem() instanceof ItemCloth
                            && (!FireWeaponHelper.isWrapped(targetStack) || FireWeaponHelper.isInCharPhase(targetStack))) {
                        FireWeaponHelper.setWrapped(targetStack, true);
                    boolean flamingCloth = cursorStack.getItem() instanceof efw.item.ItemFlamingCloth;
                    if (flamingCloth) FireWeaponHelper.soakAndIgnite(targetStack);
                    if (!player.capabilities.isCreativeMode) cursorStack.shrink(1);
                        player.world.playSound(null, player.posX, player.posY, player.posZ,
                                SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.2F);
                    if (flamingCloth) {
                        player.world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                        container.detectAndSendChanges();
                    }
                } else if (msg.action == ACTION_IGNITE) {
                    if (cursorStack.getItem() instanceof ItemFlintAndSteel
                            && FireWeaponHelper.isWrapped(targetStack)
                            && !FireWeaponHelper.isIgnited(targetStack)
                            && !FireWeaponHelper.isInCharPhase(targetStack)
                            && !FireWeaponHelper.isSoakedReady(targetStack)) {
                        FireWeaponHelper.igniteCloth(targetStack);
                        cursorStack.damageItem(1, player);
                        player.world.playSound(null, player.posX, player.posY, player.posZ,
                                SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        container.detectAndSendChanges();
                    }
                } else if (msg.action == ACTION_SOAK) {
                    if (FireWeaponHelper.isAlcohol(cursorStack) && FireWeaponHelper.isWrapped(targetStack) && !FireWeaponHelper.isInCharPhase(targetStack)) {
                        boolean isBurning = FireWeaponHelper.isIgnited(targetStack);
                        if (!isBurning && FireWeaponHelper.isSoakedReady(targetStack)) return;

                        if (isBurning) {
                            FireWeaponHelper.soakAndIgnite(targetStack);
                            player.world.playSound(null, player.posX, player.posY, player.posZ,
                                    SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F);
                        } else {
                            FireWeaponHelper.markSoaked(targetStack);
                        }

                        if (!player.capabilities.isCreativeMode) {
                            boolean returnBottle = cursorStack.getItem() == com.voltyx.mwccf.si.SIItems.WHISKEY || cursorStack.getItem() == com.voltyx.mwccf.si.SIItems.TEQUILA;
                            cursorStack.shrink(1);
                            if (returnBottle) {
                                ItemStack bottle = new ItemStack(com.voltyx.mwccf.si.SIItems.EMPTY_BOTTLE);
                                if (!player.inventory.addItemStackToInventory(bottle)) {
                                    player.dropItem(bottle, false);
                                }
                            }
                        }
                        player.world.playSound(null, player.posX, player.posY, player.posZ,
                                SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 0.8F, 0.9F);
                        container.detectAndSendChanges();
                    } else if (cursorStack.getItem() instanceof ItemFlintAndSteel
                            && FireWeaponHelper.isSoakedReady(targetStack)
                            && !FireWeaponHelper.isIgnited(targetStack)) {
                        FireWeaponHelper.soakAndIgnite(targetStack);
                        cursorStack.damageItem(1, player);
                        player.world.playSound(null, player.posX, player.posY, player.posZ,
                                SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F);
                        container.detectAndSendChanges();
                    }
                }
            });

            return null;
        }
    }
}
