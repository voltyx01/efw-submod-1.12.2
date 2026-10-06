package com.voltyx.mwccf.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenInspect implements IMessage {

    private ItemStack stack = ItemStack.EMPTY;

    public PacketOpenInspect() {}

    public PacketOpenInspect(ItemStack stack) {
        this.stack = stack != null ? stack.copy() : ItemStack.EMPTY;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.stack = ByteBufUtils.readItemStack(buf);
    }

    public static class Handler implements IMessageHandler<PacketOpenInspect, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenInspect message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient(PacketOpenInspect message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.player != null) {
                    mc.player.playSound(efw.init.EfwModSounds.ITEMSOUND, 1.5f, 1.0f);
                }
                if (message.stack != null && !message.stack.isEmpty()) {
                    com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransition(message.stack, mc.currentScreen);
                }
            });
        }
    }
}
