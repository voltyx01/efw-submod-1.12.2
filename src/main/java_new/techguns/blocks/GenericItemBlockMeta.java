package techguns.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import techguns.util.TextUtil;

public class GenericItemBlockMeta extends ItemBlock {

	public GenericItemBlockMeta(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setTranslationKey(block.getTranslationKey());
		//
		setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);

		this.setHasSubtypes(true);
		// this.setMaxDamage(0);
	}

	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + "." + stack.getItemDamage();
	}

	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null && tags.hasKey("TileEntityData")) {
			tooltip.add(TextUtil.trans("mwccf" + ".block.hasTileEntityData"));
		}
	}

}
