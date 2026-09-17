package com.voltyx.mwccf.walkietalkie;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.voltyx.mwccf.MwccfMod;
import net.minecraftforge.fml.relauncher.Side;

/**
 * C→S: client tells server to toggle the walkie-talkie on/off.
 * S→C: server confirms the new NBT state to client (same packet reused).
 */
public class PacketToggleWalkieTalkie implements IMessage {

    /** 0 = toggle on/off, 1 = mute toggle, 2 = channel up, 3 = channel down */
    private int action;
    /** For channel set: new channel value; otherwise unused */
    private int value;

    public PacketToggleWalkieTalkie() {}

    public PacketToggleWalkieTalkie(int action) {
        this(action, 0);
    }

    public PacketToggleWalkieTalkie(int action, int value) {
        this.action = action;
        this.value = value;
    }

    @Override public void fromBytes(ByteBuf buf) { action = buf.readInt(); value = buf.readInt(); }
    @Override public void toBytes(ByteBuf buf)   { buf.writeInt(action);   buf.writeInt(value);  }

    /** Server-side handler */
    public static class Handler implements IMessageHandler<PacketToggleWalkieTalkie, IMessage> {
        @Override
        public IMessage onMessage(PacketToggleWalkieTalkie msg, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack stack = WalkieTalkieUtil.findWalkieTalkie(player);
                if (stack == null) return;

                NBTTagCompound tag = stack.hasTagCompound()
                        ? stack.getTagCompound() : new NBTTagCompound();

                switch (msg.action) {
                    case 0: // toggle on/off
                        boolean current = tag.getBoolean(ItemWalkieTalkie.NBT_ACTIVE);
                        if (!current && tag.getInteger("battery_charge") <= 0) return; // no battery
                        tag.setBoolean(ItemWalkieTalkie.NBT_ACTIVE, !current);
                        break;
                    case 1: // mute toggle
                        tag.setBoolean(ItemWalkieTalkie.NBT_MUTED,
                                !tag.getBoolean(ItemWalkieTalkie.NBT_MUTED));
                        break;
                    case 2: // channel up
                        int ch = tag.getInteger(ItemWalkieTalkie.NBT_CHANNEL);
                        ch = (ch >= ItemWalkieTalkie.MAX_CHANNEL) ? 1 : ch + 1;
                        tag.setInteger(ItemWalkieTalkie.NBT_CHANNEL, ch);
                        break;
                    case 3: // channel down
                        int cld = tag.getInteger(ItemWalkieTalkie.NBT_CHANNEL);
                        cld = (cld <= 1) ? ItemWalkieTalkie.MAX_CHANNEL : cld - 1;
                        tag.setInteger(ItemWalkieTalkie.NBT_CHANNEL, cld);
                        break;
                    case 4: // set channel directly
                        int newCh = Math.max(1, Math.min(ItemWalkieTalkie.MAX_CHANNEL, msg.value));
                        tag.setInteger(ItemWalkieTalkie.NBT_CHANNEL, newCh);
                        break;
                    case 5: // set volume directly (0..100)
                        int newVol = Math.max(0, Math.min(100, msg.value));
                        tag.setInteger(ItemWalkieTalkie.NBT_VOLUME, newVol);
                        break;
                }
                stack.setTagCompound(tag);
            });
            return null;
        }
    }

    // -----------------------------------------------------------------------
    //  Helpers to send from client
    // -----------------------------------------------------------------------
    public static void sendToggle() {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleWalkieTalkie(0));
    }
    public static void sendMuteToggle() {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleWalkieTalkie(1));
    }
    public static void sendChannelUp() {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleWalkieTalkie(2));
    }
    public static void sendChannelDown() {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleWalkieTalkie(3));
    }
    public static void sendSetChannel(int ch) {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleWalkieTalkie(4, ch));
    }
    public static void sendSetVolume(int vol) {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleWalkieTalkie(5, vol));
    }
}
