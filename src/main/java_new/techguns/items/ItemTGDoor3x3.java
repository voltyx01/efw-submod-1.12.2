package techguns.items;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import techguns.blocks.BlockTGDoor3x3;
import techguns.tileentities.Door3x3TileEntity;

public class ItemTGDoor3x3<T extends Enum<T> & IStringSerializable> extends Item {

	protected BlockTGDoor3x3<T> block;
	protected Class<T> clazz;

	public ItemTGDoor3x3(String name, Class<T> clazz) {
		super();
		this.clazz = clazz;
		this.setRegistryName(new ResourceLocation("mwccf", name));
		this.setTranslationKey("techguns." + name);
		this.setCreativeTab(net.minecraft.creativetab.CreativeTabs.REDSTONE);
	}

	public void setBlock(BlockTGDoor3x3<T> block) {
		this.block = block;
	}

	@net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
	public void initModel() {
		int variants = this.clazz.getEnumConstants().length;
		for (int meta = 0; meta < variants; meta++) {
			net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(this, meta,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(
							this.getRegistryName(), "inventory"));
		}
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 0; i < this.clazz.getEnumConstants().length; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + (stack.getItemDamage() > 0 ? "_" + stack.getItemDamage() : "");
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(facing);
			}

			ItemStack itemstack = player.getHeldItem(hand);
			EnumFacing enumfacing = EnumFacing.fromAngle((double) player.rotationYaw);

			if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceDoor(worldIn, pos, enumfacing)) {
				placeDoor(worldIn, pos, enumfacing, itemstack.getMetadata(), player);
				SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos),
						worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
						(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

	public void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, int meta, EntityPlayer player) {
		boolean zplane = (facing == EnumFacing.EAST || facing == EnumFacing.WEST);
		T type = this.block.getEnumClazz().getEnumConstants()[meta];

		IBlockState slavestate = this.block.getDefaultState().withProperty(this.block.ZPLANE, zplane)
				.withProperty(this.block.MASTER, false);
		IBlockState masterstate = slavestate.withProperty(this.block.MASTER, true);

		worldIn.setBlockState(pos, slavestate, 3);
		worldIn.setBlockState(pos.up(), masterstate, 3);
		worldIn.setBlockState(pos.up(2), slavestate, 3);

		if (zplane) {
			worldIn.setBlockState(pos.north(), slavestate, 3);
			worldIn.setBlockState(pos.north().up(), slavestate, 3);
			worldIn.setBlockState(pos.north().up(2), slavestate, 3);

			worldIn.setBlockState(pos.south(), slavestate, 3);
			worldIn.setBlockState(pos.south().up(), slavestate, 3);
			worldIn.setBlockState(pos.south().up(2), slavestate, 3);
		} else {
			worldIn.setBlockState(pos.east(), slavestate, 3);
			worldIn.setBlockState(pos.east().up(), slavestate, 3);
			worldIn.setBlockState(pos.east().up(2), slavestate, 3);

			worldIn.setBlockState(pos.west(), slavestate, 3);
			worldIn.setBlockState(pos.west().up(), slavestate, 3);
			worldIn.setBlockState(pos.west().up(2), slavestate, 3);
		}

		TileEntity tile = worldIn.getTileEntity(pos.up());
		if (tile != null && tile instanceof Door3x3TileEntity) {
			Door3x3TileEntity door = (Door3x3TileEntity) tile;
			door.setOwner(player);
			door.setDoorType(meta);
		}
	}
}