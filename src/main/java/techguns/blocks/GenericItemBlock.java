package techguns.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;


public class GenericItemBlock extends ItemBlock {

	public GenericItemBlock(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		//
		setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);
	}

}
