package com.voltyx.mwccf.terminal.bodycam;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BodycamEntry {

    public String camId;
    public String carrierName;
    public int carrierEntityId;
    public boolean isOnline;
    public int batteryPercent;
    public boolean isChecked; // For TUI selection

    public BodycamEntry() {
        this.camId = "CAM-0000";
        this.carrierName = "Unknown";
        this.carrierEntityId = -1;
        this.isOnline = false;
        this.batteryPercent = 0;
        this.isChecked = true;
    }

    public BodycamEntry(String camId, String carrierName, int carrierEntityId, boolean isOnline, int batteryPercent) {
        this.camId = camId;
        this.carrierName = carrierName;
        this.carrierEntityId = carrierEntityId;
        this.isOnline = isOnline;
        this.batteryPercent = batteryPercent;
        this.isChecked = true;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, camId);
        ByteBufUtils.writeUTF8String(buf, carrierName);
        buf.writeInt(carrierEntityId);
        buf.writeBoolean(isOnline);
        buf.writeInt(batteryPercent);
        buf.writeBoolean(isChecked);
    }

    public static BodycamEntry fromBytes(ByteBuf buf) {
        BodycamEntry entry = new BodycamEntry();
        entry.camId = ByteBufUtils.readUTF8String(buf);
        entry.carrierName = ByteBufUtils.readUTF8String(buf);
        entry.carrierEntityId = buf.readInt();
        entry.isOnline = buf.readBoolean();
        entry.batteryPercent = buf.readInt();
        entry.isChecked = buf.readBoolean();
        return entry;
    }
}
