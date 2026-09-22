package net.bettercombat.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketAttackRequest implements IMessage {
    private int comboCount;
    private boolean isSneaking;
    private int selectedSlot;
    private int[] targetEntityIds;

    public PacketAttackRequest() {
        this.targetEntityIds = new int[0];
    }

    public PacketAttackRequest(int comboCount, boolean isSneaking, int selectedSlot, int[] targetEntityIds) {
        this.comboCount = comboCount;
        this.isSneaking = isSneaking;
        this.selectedSlot = selectedSlot;
        this.targetEntityIds = targetEntityIds != null ? targetEntityIds : new int[0];
    }

    public int getComboCount() {
        return comboCount;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public int[] getTargetEntityIds() {
        return targetEntityIds;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.comboCount = buf.readInt();
        this.isSneaking = buf.readBoolean();
        this.selectedSlot = buf.readInt();
        int count = buf.readInt();
        this.targetEntityIds = new int[count];
        for (int i = 0; i < count; i++) {
            this.targetEntityIds[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.comboCount);
        buf.writeBoolean(this.isSneaking);
        buf.writeInt(this.selectedSlot);
        buf.writeInt(this.targetEntityIds.length);
        for (int id : this.targetEntityIds) {
            buf.writeInt(id);
        }
    }
}
