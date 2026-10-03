package com.voltyx.mwccf.network;

import com.voltyx.mwccf.geo.ItemBodycam;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleBodycam implements IMessage {

    private int slotId;

    public PacketToggleBodycam() {
        this.slotId = -1;
    }

    public PacketToggleBodycam(int slotId) {
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

    public static class Handler implements IMessageHandler<PacketToggleBodycam, IMessage> {
        @Override
        public IMessage onMessage(PacketToggleBodycam message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                ItemStack targetStack = ItemStack.EMPTY;
                if (message.slotId >= 0 && message.slotId < player.openContainer.inventorySlots.size()) {
                    ItemStack inSlot = player.openContainer.getSlot(message.slotId).getStack();
                    if (!inSlot.isEmpty() && inSlot.getItem() instanceof ItemBodycam) {
                        targetStack = inSlot;
                    }
                }

                if (targetStack.isEmpty()) {
                    // Check equipped in baubles / chest / hands
                    targetStack = com.voltyx.mwccf.geo.BodycamLayer.getEquippedBodycam(player);
                    if (targetStack.isEmpty()) {
                        if (player.getHeldItemMainhand().getItem() instanceof ItemBodycam) {
                            targetStack = player.getHeldItemMainhand();
                        } else if (player.getHeldItemOffhand().getItem() instanceof ItemBodycam) {
                            targetStack = player.getHeldItemOffhand();
                        }
                    }
                }

                if (!targetStack.isEmpty() && targetStack.getItem() instanceof ItemBodycam) {
                    boolean newState = ItemBodycam.togglePower(targetStack);
                    float pitch = newState ? 1.4F : 0.8F;
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.7F, pitch);
                    
                    net.minecraft.util.text.ITextComponent statusComp = new net.minecraft.util.text.TextComponentTranslation(
                            newState ? "tooltip.mwccf.bodycam.online_raw" : "tooltip.mwccf.bodycam.offline_raw"
                    );
                    statusComp.getStyle().setColor(newState ? net.minecraft.util.text.TextFormatting.GREEN : net.minecraft.util.text.TextFormatting.RED);
                    player.sendMessage(new net.minecraft.util.text.TextComponentTranslation(
                            "message.mwccf.bodycam.status_fmt",
                            ItemBodycam.getCamId(targetStack),
                            statusComp
                    ));
                }
            });
            return null;
        }
    }
}
