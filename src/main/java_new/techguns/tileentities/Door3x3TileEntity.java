package techguns.tileentities;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class Door3x3TileEntity extends TileEntity {

	private UUID owner;
	private int doorType = 0;

	public void setOwner(EntityPlayer player) {
		if (player != null) {
			this.owner = player.getUniqueID();
			this.markDirty();
		}
	}

	public UUID getOwner() {
		return this.owner;
	}

	public boolean isOwner(EntityPlayer player) {
		return player != null && this.owner != null && this.owner.equals(player.getUniqueID());
	}

	public void setDoorType(int doorType) {
		this.doorType = doorType;
		this.markDirty();
	}

	public int getDoorType() {
		return this.doorType;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (this.owner != null) {
			compound.setString("Owner", this.owner.toString());
		}
		compound.setInteger("DoorType", this.doorType);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("Owner")) {
			this.owner = UUID.fromString(compound.getString("Owner"));
		}
		this.doorType = compound.getInteger("DoorType");
	}
}