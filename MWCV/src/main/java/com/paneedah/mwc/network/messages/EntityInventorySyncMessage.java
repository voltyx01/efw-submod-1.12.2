package com.paneedah.mwc.network.messages;

import com.paneedah.mwc.equipment.inventory.EquipmentInventory;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

import static com.paneedah.mwc.utils.ModReference.RED_LOG;

public final class EntityInventorySyncMessage implements IMessage {

    private int entityId;
    private boolean excludeEntity;
    private NBTTagCompound inventoryCompound = new NBTTagCompound();
    
    public EntityInventorySyncMessage(final Entity entity, final boolean excludeEntity, final EquipmentInventory inventory) {
        this.entityId = entity.getEntityId();
        this.excludeEntity = excludeEntity;
        inventory.writeToNBT(inventoryCompound);
    }

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        final PacketBuffer packetBuf = new PacketBuffer(byteBuf);

        entityId = packetBuf.readInt();
        excludeEntity = packetBuf.readBoolean();
        try {
            inventoryCompound  = packetBuf.readCompoundTag();
        } catch (IOException ioException) {
            RED_LOG.printFramedError("Networking","Failed to read equipment inventory from message", "Equipment inventory won't be synced", ioException.getMessage());
        }
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        final PacketBuffer packetBuf = new PacketBuffer(byteBuf);

        packetBuf.writeInt(entityId);
        packetBuf.writeBoolean(excludeEntity);
        packetBuf.writeCompoundTag(inventoryCompound);
    }
    
    public EquipmentInventory getInventory() {
        EquipmentInventory inventory = new EquipmentInventory();

        if(inventoryCompound != null)
            inventory.readFromNBT(inventoryCompound);

        return inventory;
    }
    
    public Entity getEntity(World world) {
        return world.getEntityByID(entityId);
    }

    public EntityInventorySyncMessage() {}

    public EntityInventorySyncMessage(int entityId, boolean excludeEntity, NBTTagCompound inventoryCompound) {
        this.entityId = entityId;
        this.excludeEntity = excludeEntity;
        this.inventoryCompound = inventoryCompound;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean isExcludeEntity() {
        return this.excludeEntity;
    }

    public NBTTagCompound getInventoryCompound() {
        return this.inventoryCompound;
    }

}