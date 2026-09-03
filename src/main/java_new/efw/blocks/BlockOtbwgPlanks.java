package efw.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOtbwgPlanks extends Block {
    public BlockOtbwgPlanks(String name) {
        super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("otbwg." + name);
        this.setHardness(2.0f);
        this.setResistance(5.0f);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
