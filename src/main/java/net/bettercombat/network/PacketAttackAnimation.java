package net.bettercombat.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketAttackAnimation implements IMessage {
    private int playerId;
    private int animatedHandOrdinal;
    private String animationName;
    private float length;
    private float upswing;
    private String soundId;

    public PacketAttackAnimation() {
        this.animationName = "";
        this.soundId = "";
    }

    public PacketAttackAnimation(int playerId, int animatedHandOrdinal, String animationName, float length, float upswing, String soundId) {
        this.playerId = playerId;
        this.animatedHandOrdinal = animatedHandOrdinal;
        this.animationName = animationName != null ? animationName : "";
        this.length = length;
        this.upswing = upswing;
        this.soundId = soundId != null ? soundId : "";
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getAnimatedHandOrdinal() {
        return animatedHandOrdinal;
    }

    public String getAnimationName() {
        return animationName;
    }

    public float getLength() {
        return length;
    }

    public float getUpswing() {
        return upswing;
    }

    public String getSoundId() {
        return soundId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerId = buf.readInt();
        this.animatedHandOrdinal = buf.readByte();
        this.animationName = ByteBufUtils.readUTF8String(buf);
        this.length = buf.readFloat();
        this.upswing = buf.readFloat();
        this.soundId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.playerId);
        buf.writeByte(this.animatedHandOrdinal);
        ByteBufUtils.writeUTF8String(buf, this.animationName);
        buf.writeFloat(this.length);
        buf.writeFloat(this.upswing);
        ByteBufUtils.writeUTF8String(buf, this.soundId);
    }
}
