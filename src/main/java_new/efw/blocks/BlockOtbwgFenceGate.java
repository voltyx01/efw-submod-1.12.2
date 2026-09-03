package efw.blocks;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOtbwgFenceGate extends BlockFenceGate {
    public BlockOtbwgFenceGate(String name) {
        super(BlockPlanks.EnumType.OAK);
        this.setRegistryName("mwccf", name);
        this.setTranslationKey("otbwg." + name);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setHardness(2.0f);
        this.setResistance(5.0f);
    }
}
