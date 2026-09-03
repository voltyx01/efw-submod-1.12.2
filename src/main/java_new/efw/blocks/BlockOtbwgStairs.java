package efw.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOtbwgStairs extends BlockStairs {
    public BlockOtbwgStairs(String name, IBlockState modelState) {
        super(modelState);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("otbwg." + name);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.useNeighborBrightness = true;
    }
}
