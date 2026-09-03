package techguns;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techguns.blocks.*;
import techguns.plugins.chisel.ChiselIMCHelper;


public class TGBlocks {
	
	public static BlockMilitaryCrate MILITARY_CRATE;
	public static BlockTGDoor3x3<EnumDoorType> DOOR3x3;
	public static GenericBlockMetaEnum<TGMetalPanelType> METAL_PANEL;
	public static GenericBlockMetaEnum<EnumConcreteType> CONCRETE;
	public static BlockTGStairs METAL_STAIRS;
	public static BlockTGStairs CONCRETE_STAIRS;
	public static GenericBlockMetaEnumCamoChangeable<EnumLightblockType> NEONLIGHT_BLOCK;
	public static BlockTGLamp<EnumLampType> LAMP_0;
	public static BlockTGLadder<EnumLadderType> LADDER_0;
	public static BlockTGCamoNet CAMONET;
	public static BlockTGCamoNetTop CAMONET_TOP;
	public static Block BUNKER_DOOR;
	public static Block ORE_CLUSTER;
	public static Block MONSTER_SPAWNER;
	public static Block SAND_HARD;
	public static Block SANDBAGS;
	public static Block SLIMY_BLOCK;
	public static Block SLIMY_LADDER;

	public static List<Block> blockList = new ArrayList<>();

	static {
		TGItems.BUNKER_DOOR = new techguns.items.ItemTGDoor2x1("bunkerdoor");
		TGItems.DOOR3x3 = new techguns.items.ItemTGDoor3x3<EnumDoorType>("door3x3", EnumDoorType.class);

		MILITARY_CRATE = (BlockMilitaryCrate) new BlockMilitaryCrate("military_crate", Material.WOOD).setHardness(4.0f);
		LAMP_0 = (BlockTGLamp<EnumLampType>) new BlockTGLamp<EnumLampType>("lamp0", EnumLampType.class).setHardness(4.0f);
		LADDER_0 = (BlockTGLadder<EnumLadderType>) new BlockTGLadder<EnumLadderType>("ladder0", EnumLadderType.class).setHardness(6.0f);
		DOOR3x3 = (BlockTGDoor3x3<EnumDoorType>) new BlockTGDoor3x3<EnumDoorType>("door3x3", EnumDoorType.class, (techguns.items.ItemTGDoor3x3<EnumDoorType>) TGItems.DOOR3x3).setHardness(6.0f);
		((techguns.items.ItemTGDoor3x3<EnumDoorType>) TGItems.DOOR3x3).setBlock(DOOR3x3);

		CAMONET = new BlockTGCamoNet("camonet");
		CAMONET_TOP = new BlockTGCamoNetTop("camonet_top");

		BUNKER_DOOR = (BlockTGDoor2x1) new BlockTGDoor2x1("bunkerdoor", TGItems.BUNKER_DOOR).setHardness(8.0f);
		((techguns.items.ItemTGDoor2x1) TGItems.BUNKER_DOOR).setBlock(BUNKER_DOOR);

		METAL_STAIRS = (BlockTGStairs) new BlockTGStairs("stairs_metal", Material.IRON, SoundType.METAL).setHardness(8.0f);
		CONCRETE_STAIRS = (BlockTGStairs) new BlockTGStairs("stairs_concrete", Material.ROCK, SoundType.STONE).setHardness(6.0f);

		SANDBAGS = (BlockSandbags) new BlockSandbags("sandbags").setHardness(6.0f);
		SAND_HARD = new BlockTGSandHard("sand_hard", EnumTGSandHardTypes.class);

		MONSTER_SPAWNER = new BlockTGSpawner("tg_spawner");
		SLIMY_BLOCK = net.minecraft.init.Blocks.AIR;
		SLIMY_LADDER = net.minecraft.init.Blocks.AIR;

		METAL_PANEL = new GenericBlockMetaEnum<TGMetalPanelType>("metalpanel", Material.IRON, TGMetalPanelType.class);
		CONCRETE = new GenericBlockMetaEnum<EnumConcreteType>("concrete", Material.ROCK, EnumConcreteType.class);
		NEONLIGHT_BLOCK = new GenericBlockMetaEnumCamoChangeable<EnumLightblockType>("neonlights", Material.GLASS, EnumLightblockType.class);

		ORE_CLUSTER = net.minecraft.init.Blocks.AIR; // We did not port this, but keep the reference for structures

		Block[] toRegister = {
			MILITARY_CRATE, LAMP_0, LADDER_0, DOOR3x3, CAMONET, CAMONET_TOP, BUNKER_DOOR,
			METAL_STAIRS, CONCRETE_STAIRS, MONSTER_SPAWNER, SAND_HARD, SANDBAGS,
			METAL_PANEL, CONCRETE, NEONLIGHT_BLOCK
		};
		for (Block b : toRegister) {
			blockList.add(b);
		}
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		for (Block b : blockList) {
			if(b != net.minecraft.init.Blocks.AIR) {
				if (b instanceof IGenericBlock) {
					((IGenericBlock) b).registerBlock(event);
				} else {
					event.getRegistry().register(b);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		// Register explicit door items
		event.getRegistry().register(TGItems.BUNKER_DOOR);
		event.getRegistry().register(TGItems.DOOR3x3);

		for (Block b : blockList) {
			if(b != net.minecraft.init.Blocks.AIR) {
				if (b instanceof IGenericBlock) {
					ItemBlock ib = ((IGenericBlock) b).createItemBlock();
					if(ib != null) {
						if (ib.getRegistryName() == null) ib.setRegistryName(b.getRegistryName());
						event.getRegistry().register(ib);
					}
				} else {
					ItemBlock ib = new ItemBlock(b);
					if (ib.getRegistryName() == null) ib.setRegistryName(b.getRegistryName());
					event.getRegistry().register(ib);
				}
			}
		}
	}

	/**
	 * Должен вызываться вручную из главного класса мода (@Mod) в обработчике
	 * FMLInitializationEvent, например:
	 *
	 *   @Mod.EventHandler
	 *   public void init(FMLInitializationEvent event) {
	 *       TGBlocks.init(event);
	 *   }
	 *
	 * Раньше эта логика лежала в старом TGBlocks (через ITGInitializer),
	 * но при рефакторинге на @EventBusSubscriber вызов потерялся,
	 * из-за чего перестала работать интеграция с Chisel.
	 */
	public static void init(FMLInitializationEvent event) {
		ChiselIMCHelper.addChiselVariants("techguns:camonet", TGBlocks.CAMONET, EnumCamoNetType.class);
		ChiselIMCHelper.addChiselVariants("techguns:camonettop", TGBlocks.CAMONET_TOP, EnumCamoNetType.class);
		ChiselIMCHelper.addChiselVariants("techguns:metalpanel", TGBlocks.METAL_PANEL, TGMetalPanelType.class);
		ChiselIMCHelper.addChiselVariants("techguns:neonlights", TGBlocks.NEONLIGHT_BLOCK, EnumLightblockType.class);
		ChiselIMCHelper.addChiselVariants("reinforced_concrete", TGBlocks.CONCRETE, EnumConcreteType.class);

		for (EnumLadderType t : EnumLadderType.values()) {
			ChiselIMCHelper.addChiselVariation(
					"techguns:metalladder",
					TGBlocks.LADDER_0.getRegistryName(),
					TGBlocks.LADDER_0.getMetaFromState(
							TGBlocks.LADDER_0.getDefaultState().withProperty(TGBlocks.LADDER_0.TYPE, t)));
		}
	}

	@SubscribeEvent
	@net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
	public static void onModelRegister(net.minecraftforge.client.event.ModelRegistryEvent event) {
		// Register models for explicit door items
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(TGItems.BUNKER_DOOR, 0, new net.minecraft.client.renderer.block.model.ModelResourceLocation(TGItems.BUNKER_DOOR.getRegistryName(), "inventory"));
		if (TGItems.DOOR3x3 instanceof techguns.items.ItemTGDoor3x3) {
			((techguns.items.ItemTGDoor3x3) TGItems.DOOR3x3).initModel();
		}

		for (Block b : blockList) {
			if(b != net.minecraft.init.Blocks.AIR) {
				if (b instanceof IGenericBlock) {
					((IGenericBlock) b).registerItemBlockModels();
				} else {
					Item item = Item.getItemFromBlock(b);
					if (item != net.minecraft.init.Items.AIR) {
						net.minecraft.client.renderer.block.model.ModelResourceLocation location = new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName(), "inventory");
						net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0, location);
					}
				}
			}
		}
	}
}