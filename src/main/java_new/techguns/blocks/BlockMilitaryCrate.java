package techguns.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.storage.loot.LootTableList;


public class BlockMilitaryCrate extends GenericBlockMetaEnum<EnumMilitaryCrateType>{

	protected static final ResourceLocation loottable_ammo = new ResourceLocation("mwccf", "blocks/military_crate_ammo");
	protected static final ResourceLocation loottable_gun = new ResourceLocation("mwccf", "blocks/military_crate_gun");
	protected static final ResourceLocation loottable_armor = new ResourceLocation("mwccf", "blocks/military_crate_armor");
	protected static final ResourceLocation loottable_medical = new ResourceLocation("mwccf", "blocks/military_crate_medical");
	protected static final ResourceLocation loottable_explosives = new ResourceLocation("mwccf", "blocks/military_crate_explosives");
	protected static final ResourceLocation loottable_generic = new ResourceLocation("mwccf", "blocks/military_crate_generic");
	
	protected static final AxisAlignedBB boundingbox = new AxisAlignedBB(0.03125, 0, 0.03125, 0.96875, 1, 0.96875);
	
	static {
		LootTableList.register(loottable_ammo);
		LootTableList.register(loottable_gun);
		LootTableList.register(loottable_armor);
		LootTableList.register(loottable_medical);
		LootTableList.register(loottable_explosives);
		LootTableList.register(loottable_generic);
	}
	
	public BlockMilitaryCrate(String name, Material mat) {
		super(name, mat, EnumMilitaryCrateType.class);
	}

	public BlockMilitaryCrate(String name, Material mat, MapColor mc, SoundType soundType) {
		super(name, mat, mc, soundType, EnumMilitaryCrateType.class);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return boundingbox;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public void getDrops(net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		String[] lootConfig;
		switch(state.getValue(this.TYPE)) {
		case AMMO: lootConfig = efw.biomeinfo.MwccfConfig.techguns.crates.ammoCrateLoot; break;
		case ARMOR: lootConfig = efw.biomeinfo.MwccfConfig.techguns.crates.armorCrateLoot; break;
		case EXPLOSIVE: lootConfig = efw.biomeinfo.MwccfConfig.techguns.crates.explosiveCrateLoot; break;
		case GUN: lootConfig = efw.biomeinfo.MwccfConfig.techguns.crates.weaponCrateLoot; break;
		case MEDICAL: lootConfig = efw.biomeinfo.MwccfConfig.techguns.crates.medicalCrateLoot; break;
		default: lootConfig = efw.biomeinfo.MwccfConfig.techguns.crates.genericCrateLoot; break;
		}

		if (lootConfig == null || lootConfig.length == 0) return;

		java.util.List<String> items = new java.util.ArrayList<>();
		java.util.List<Integer> weights = new java.util.ArrayList<>();
		int totalWeight = 0;

		for (String entry : lootConfig) {
			String[] parts = entry.split(",");
			if (parts.length >= 2) {
				try {
					String id = parts[0].trim();
					int weight = (int) Float.parseFloat(parts[1].trim());
					if (weight > 0) {
						items.add(id);
						weights.add(weight);
						totalWeight += weight;
					}
				} catch (Exception e) {}
            } else if (parts.length == 1) {
                items.add(parts[0].trim());
                weights.add(10);
                totalWeight += 10;
            }
		}

		if (totalWeight <= 0) return;

		int numDrops = (Math.random() > 0.5) ? 2 : 1;
		for (int i = 0; i < numDrops; i++) {
			int roll = (int) (Math.random() * totalWeight);
			int currentWeight = 0;
			for (int j = 0; j < items.size(); j++) {
				currentWeight += weights.get(j);
				if (roll < currentWeight) {
					net.minecraft.item.Item item = net.minecraft.item.Item.getByNameOrId(items.get(j));
					if (item != null) {
						drops.add(new net.minecraft.item.ItemStack(item, 1));
					}
					break;
				}
			}
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		if (face==EnumFacing.DOWN || face==EnumFacing.UP) {
			return BlockFaceShape.CENTER_BIG;
		}
		return BlockFaceShape.UNDEFINED;
	}
}
