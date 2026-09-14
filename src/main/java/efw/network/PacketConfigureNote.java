package efw.network;

import efw.item.NoteItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfigureNote implements IMessage {
    private int handId; // 0 = MAIN_HAND, 1 = OFF_HAND
    private int noteId;
    private int variant;
    private boolean isQuest;

    public PacketConfigureNote() {
    }

    public PacketConfigureNote(EnumHand hand, int noteId, int variant, boolean isQuest) {
        this.handId = hand == EnumHand.OFF_HAND ? 1 : 0;
        this.noteId = noteId;
        this.variant = variant;
        this.isQuest = isQuest;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.handId = buf.readByte();
        this.noteId = buf.readInt();
        this.variant = buf.readInt();
        this.isQuest = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.handId);
        buf.writeInt(this.noteId);
        buf.writeInt(this.variant);
        buf.writeBoolean(this.isQuest);
    }

    public static class Handler implements IMessageHandler<PacketConfigureNote, IMessage> {
        @Override
        public IMessage onMessage(PacketConfigureNote message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (player.capabilities.isCreativeMode) {
                    EnumHand hand = message.handId == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                    ItemStack stack = player.getHeldItem(hand);
                    if (!stack.isEmpty() && stack.getItem() instanceof NoteItem) {
                        NoteItem.setNoteData(stack, message.noteId, message.variant, message.isQuest);
                        player.inventory.markDirty();
                        player.openContainer.detectAndSendChanges();
                    }
                }
            });
            return null;
        }
    }
}