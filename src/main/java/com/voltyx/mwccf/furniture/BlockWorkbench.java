package com.voltyx.mwccf.furniture;

import net.minecraft.block.BlockWorkbench.InterfaceCraftingTable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWorkbench extends BlockFurnitureHorizontal {

    public BlockWorkbench(String name) {
        super(Material.WOOD);
        this.setTranslationKey("refurbished_furniture." + name);
        this.setRegistryName("refurbished_furniture", name);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            worldIn.playSound(null, pos, FurnitureSounds.BLOCK_WORKBENCH_CRAFT, SoundCategory.BLOCKS, 0.8F, 1.0F);
            playerIn.displayGui(new InterfaceCraftingTable(worldIn, pos));
        }
        return true;
    }
}
