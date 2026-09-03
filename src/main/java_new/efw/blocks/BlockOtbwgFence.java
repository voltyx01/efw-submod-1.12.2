package efw.blocks;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOtbwgFence extends BlockFence {
    public BlockOtbwgFence(String name, Material materialIn, MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("otbwg." + name);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(2.0f);
        this.setResistance(5.0f);
    }
}
