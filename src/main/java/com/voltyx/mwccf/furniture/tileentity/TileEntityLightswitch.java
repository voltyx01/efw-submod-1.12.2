package com.voltyx.mwccf.furniture.tileentity;

import com.voltyx.mwccf.furniture.BlockCeilingLight;
import com.voltyx.mwccf.furniture.BlockLamp;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileEntityLightswitch extends TileEntity {

    public static final double MAX_RANGE = 48.0D;
    public static final double MAX_RANGE_SQ = MAX_RANGE * MAX_RANGE;

    // Relative block coordinates (dx, dy, dz) from this switch pos.
    // Storing relative coordinates guarantees structure block compatibility!
    private final List<BlockPos> linkedOffsets = new ArrayList<>();

    public List<BlockPos> getLinkedOffsets() {
        return linkedOffsets;
    }

    public void addLinkedOffset(BlockPos offset) {
        if (!linkedOffsets.contains(offset)) {
            linkedOffsets.add(offset);
            markDirty();
        }
    }

    public void removeLinkedOffset(BlockPos offset) {
        if (linkedOffsets.remove(offset)) {
            markDirty();
        }
    }

    public void initFromPlacedStack(ItemStack stack, BlockPos switchPos) {
        if (stack == null || !stack.hasTagCompound()) return;

        NBTTagCompound tag = stack.getTagCompound();
        NBTTagList list = null;

        if (tag.hasKey("BlockEntityTag", 10)) {
            NBTTagCompound teTag = tag.getCompoundTag("BlockEntityTag");
            if (teTag.hasKey("relative_lights", 9)) {
                readFromNBT(teTag);
                return;
            } else if (teTag.hasKey("lights", 9)) {
                list = teTag.getTagList("lights", 10);
            }
        } else if (tag.hasKey("lights", 9)) {
            list = tag.getTagList("lights", 10);
        }

        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound item = list.getCompoundTagAt(i);
                int lx = item.getInteger("x");
                int ly = item.getInteger("y");
                int lz = item.getInteger("z");
                BlockPos offset = new BlockPos(lx - switchPos.getX(), ly - switchPos.getY(), lz - switchPos.getZ());
                addLinkedOffset(offset);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        for (BlockPos offset : linkedOffsets) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("dx", offset.getX());
            tag.setInteger("dy", offset.getY());
            tag.setInteger("dz", offset.getZ());
            list.appendTag(tag);
        }
        compound.setTag("relative_lights", list);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        linkedOffsets.clear();

        if (compound.hasKey("relative_lights", 9)) {
            NBTTagList list = compound.getTagList("relative_lights", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                linkedOffsets.add(new BlockPos(tag.getInteger("dx"), tag.getInteger("dy"), tag.getInteger("dz")));
            }
        } else if (compound.hasKey("lights", 9)) {
            // Backward compatibility for absolute coords
            NBTTagList list = compound.getTagList("lights", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                if (pos != null) {
                    int lx = tag.getInteger("x");
                    int ly = tag.getInteger("y");
                    int lz = tag.getInteger("z");
                    linkedOffsets.add(new BlockPos(lx - pos.getX(), ly - pos.getY(), lz - pos.getZ()));
                }
            }
        }
    }

    @Override
    public void rotate(Rotation rotation) {
        for (int i = 0; i < linkedOffsets.size(); i++) {
            BlockPos oldOffset = linkedOffsets.get(i);
            linkedOffsets.set(i, rotateOffset(oldOffset, rotation));
        }
        markDirty();
    }

    @Override
    public void mirror(Mirror mirror) {
        for (int i = 0; i < linkedOffsets.size(); i++) {
            BlockPos oldOffset = linkedOffsets.get(i);
            linkedOffsets.set(i, mirrorOffset(oldOffset, mirror));
        }
        markDirty();
    }

    private static BlockPos rotateOffset(BlockPos offset, Rotation rotation) {
        int x = offset.getX();
        int y = offset.getY();
        int z = offset.getZ();
        switch (rotation) {
            case CLOCKWISE_90:
                return new BlockPos(-z, y, x);
            case CLOCKWISE_180:
                return new BlockPos(-x, y, -z);
            case COUNTERCLOCKWISE_90:
                return new BlockPos(z, y, -x);
            default:
                return offset;
        }
    }

    private static BlockPos mirrorOffset(BlockPos offset, Mirror mirror) {
        int x = offset.getX();
        int y = offset.getY();
        int z = offset.getZ();
        switch (mirror) {
            case LEFT_RIGHT:
                return new BlockPos(x, y, -z);
            case FRONT_BACK:
                return new BlockPos(-x, y, z);
            default:
                return offset;
        }
    }

    public void toggleLights(boolean powered) {
        if (world == null || world.isRemote) return;

        for (BlockPos offset : linkedOffsets) {
            BlockPos targetPos = this.pos.add(offset);
            if (this.pos.distanceSq(targetPos) <= MAX_RANGE_SQ) {
                toggleLampAt(world, targetPos, powered);
            }
        }
    }

    public static boolean isLamp(Block block) {
        if (block == null) return false;
        if (block instanceof BlockCeilingLight || block instanceof BlockLamp) {
            return true;
        }
        ResourceLocation reg = block.getRegistryName();
        if (reg != null) {
            String path = reg.getPath();
            if (path.contains("ceiling_light") || path.contains("lamp")) {
                return true;
            }
        }
        return false;
    }

    public static void toggleLampAt(World world, BlockPos targetPos, boolean powered) {
        IBlockState targetState = world.getBlockState(targetPos);
        Block block = targetState.getBlock();

        if (block instanceof BlockCeilingLight) {
            world.setBlockState(targetPos, targetState.withProperty(BlockCeilingLight.LIT, powered), 3);
        } else {
            ResourceLocation reg = block.getRegistryName();
            if (reg != null) {
                String path = reg.getPath();
                if ("cfm".equals(reg.getNamespace()) && path.contains("ceiling_light")) {
                    Block onBlock = Block.getBlockFromName("cfm:ceiling_light_on");
                    Block offBlock = Block.getBlockFromName("cfm:ceiling_light_off");
                    if (onBlock != null && offBlock != null) {
                        Block newBlock = powered ? onBlock : offBlock;
                        world.setBlockState(targetPos, newBlock.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }
}
