package techguns.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGBlocks;


/**
 * May only be implemented by Blocks
 */
public interface IGenericBlock {

	public default void init(Block b, String name, boolean addToList) {
		b.setRegistryName(new ResourceLocation("mwccf", name));
		// Set translation key to match techguns lang file entries (tile.techguns.<name>.name)
		b.setTranslationKey("techguns." + name);
		//
		b.setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);
		
		if(addToList) {
			//
		}
	}
	
	/**
	 * return the correct itemblock for initialization
	 */
	public ItemBlock createItemBlock();
	
	public void registerBlock(RegistryEvent.Register<Block> event);
	
	@SideOnly(Side.CLIENT)
	public void registerItemBlockModels();
}
