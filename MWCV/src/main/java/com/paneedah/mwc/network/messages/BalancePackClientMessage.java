package com.paneedah.mwc.network.messages;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.paneedah.mwc.network.CompressionUtil;
import com.paneedah.weaponlib.config.BalancePackManager.BalancePack;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class BalancePackClientMessage implements IMessage {

    private BalancePack balancePack;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        if (!byteBuf.readBoolean())
            return;

        byte[] bytes = new byte[byteBuf.readInt()];
        for (int i = 0; i < bytes.length; ++i)
            bytes[i] = byteBuf.readByte();

        balancePack = BalancePack.fromJSONObject(new GsonBuilder().create().fromJson(CompressionUtil.decompressString(bytes), JsonObject.class));
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeBoolean(balancePack != null);
        if (balancePack == null)
            return;

        final byte[] bytes = CompressionUtil.compressString(balancePack.toJSONObject().toString());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public BalancePackClientMessage() {}

    public BalancePackClientMessage(BalancePack balancePack) {
        this.balancePack = balancePack;
    }

    public BalancePack getBalancePack() {
        return this.balancePack;
    }

}