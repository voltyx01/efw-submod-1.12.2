/*
 * MCreator note: ...
 */
package com.voltyx.mwccf;

import efw.AnimationTickHandler;
import efw.animation.AnimationRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGBlocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import java.io.File;
import net.minecraft.world.biome.Biome;
import net.minecraft.potion.Potion;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import java.util.function.Supplier;
import com.voltyx.mwccf.HeadshotDamageHandler;
import com.voltyx.mwccf.network.HitSoundMessage;
import com.voltyx.mwccf.network.PacketLootingComplete;
import com.voltyx.mwccf.network.PacketStartLooting;

// Импорты для Wildfire Gender Mod
import techguns.tileentities.TGSpawnerTileEnt;
import com.voltyx.gender.main.WildfireGender;
import com.voltyx.gender.main.WildfireEventHandler;
import com.voltyx.gender.main.networking.PacketSync;
import com.voltyx.gender.main.networking.PacketSendGenderInfo;
import com.voltyx.gender.api.IGenderArmor;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import com.voltyx.mwccf.CommandReloadConfig;

@Mod(modid = MwccfMod.MODID, version = MwccfMod.VERSION, dependencies = "required-after:mwc", guiFactory = "com.voltyx.mwccf.gui.MwccfGuiFactory")
public class MwccfMod {
	public static final String MODID = "mwccf";
	public static final String VERSION = "1.0.0";
	public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("mwccf:a");
	public static net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper NETWORK;
	@SidedProxy(clientSide = "com.voltyx.mwccf.ClientProxyMwccfMod", serverSide = "com.voltyx.mwccf.ServerProxyMwccfMod")
	public static IProxyMwccfMod proxy;

	@Mod.Instance(MODID)
	public static MwccfMod instance;
	public ElementsMwccfMod elements = new ElementsMwccfMod();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new LootContainerHandler());
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
		// Принудительно читаем конфиг один раз при запуске игры
		AdvancedHeadshotManager.reloadConfig();
		MinecraftForge.EVENT_BUS.register(this);
		if (event.getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.geo.HeadlampLightManager());
		}
		GameRegistry.registerWorldGenerator(elements, 5);
		GameRegistry.registerWorldGenerator(new techguns.world.WorldGenTGStructureSpawn(), 10);
		GameRegistry.registerFuelHandler(elements);
		// GameRegistry.registerTileEntity(TGSpawnerTileEnt.class, new
		// ResourceLocation("mwccf", "tg_spawner"));
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new com.voltyx.mwccf.furniture.client.gui.FurnitureGuiHandler());
		elements.preInit(event);
		MinecraftForge.EVENT_BUS.register(elements);
		elements.getElements().forEach(element -> element.preInit(event));
		proxy.preInit(event);

		// --- НОВОЕ: Инициализация сети для хитмаркеров (звуков пушек) ---
		// Создаем канал связи (имя должно быть коротким, до 20 символов)
		NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("mwccf_main");
		PACKET_HANDLER.registerMessage(PacketStartLooting.Handler.class, PacketStartLooting.class, 1, Side.CLIENT);
		PACKET_HANDLER.registerMessage(PacketLootingComplete.Handler.class, PacketLootingComplete.class, 2,
				Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.geo.HeadlampNetwork.Handler.class, com.voltyx.mwccf.geo.HeadlampNetwork.PacketToggleHeadlamp.class, 3, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketChargeDevice.Handler.class, com.voltyx.mwccf.network.PacketChargeDevice.class, 4, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketUpdateDeviceState.Handler.class, com.voltyx.mwccf.network.PacketUpdateDeviceState.class, 5, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketSyncAnimations.Handler.class, com.voltyx.mwccf.network.PacketSyncAnimations.class, 6, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketSyncAnimations.Handler.class, com.voltyx.mwccf.network.PacketSyncAnimations.class, 7, Side.CLIENT);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketDash.Handler.class, com.voltyx.mwccf.network.PacketDash.class, 8, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketSyncStamina.Handler.class, com.voltyx.mwccf.network.PacketSyncStamina.class, 9, Side.CLIENT);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketSyncDashCooldown.Handler.class, com.voltyx.mwccf.network.PacketSyncDashCooldown.class, 10, Side.CLIENT);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.network.PacketToggleLaserColor.Handler.class, com.voltyx.mwccf.network.PacketToggleLaserColor.class, 11, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.sins.network.PacketSelectSin.Handler.class, com.voltyx.mwccf.sins.network.PacketSelectSin.class, 12, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.sins.network.PacketLevelUpRequest.Handler.class, com.voltyx.mwccf.sins.network.PacketLevelUpRequest.class, 13, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.sins.network.PacketLevelUpCardsOffer.Handler.class, com.voltyx.mwccf.sins.network.PacketLevelUpCardsOffer.class, 14, Side.CLIENT);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.sins.network.PacketAcceptCard.Handler.class, com.voltyx.mwccf.sins.network.PacketAcceptCard.class, 15, Side.SERVER);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.sins.network.PacketSyncSinData.Handler.class, com.voltyx.mwccf.sins.network.PacketSyncSinData.class, 16, Side.CLIENT);
		PACKET_HANDLER.registerMessage(com.voltyx.mwccf.sins.network.PacketUseManual.Handler.class, com.voltyx.mwccf.sins.network.PacketUseManual.class, 17, Side.SERVER);

		// Регистрация Capability грехов
		CapabilityManager.INSTANCE.register(com.voltyx.mwccf.sins.capability.ISinCapability.class,
				new net.minecraftforge.common.capabilities.Capability.IStorage<com.voltyx.mwccf.sins.capability.ISinCapability>() {
					@Override
					public NBTBase writeNBT(net.minecraftforge.common.capabilities.Capability<com.voltyx.mwccf.sins.capability.ISinCapability> capability,
							com.voltyx.mwccf.sins.capability.ISinCapability instance, EnumFacing side) {
						return instance.writeToNBT();
					}

					@Override
					public void readNBT(net.minecraftforge.common.capabilities.Capability<com.voltyx.mwccf.sins.capability.ISinCapability> capability,
							com.voltyx.mwccf.sins.capability.ISinCapability instance, EnumFacing side, NBTBase nbt) {
						if (nbt instanceof NBTTagCompound) {
							instance.readFromNBT((NBTTagCompound) nbt);
						}
					}
				}, com.voltyx.mwccf.sins.capability.SinCapability::new);
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.sins.capability.SinCapabilityEvents());

		// Регистрируем наш пакет (Идет с сервера на клиент)
		NETWORK.registerMessage(com.voltyx.mwccf.network.HitSoundMessage.Handler.class,
				com.voltyx.mwccf.network.HitSoundMessage.class,
				0, Side.CLIENT);
		// -----------------------------------------------------------------

		// --- Initialize ExtraGore ---
		if (event.getSide() == Side.CLIENT) {
			com.eruannie_9.extragore.ExtraGore.proxy = new com.eruannie_9.extragore.ClientProxy();
		} else {
			com.eruannie_9.extragore.ExtraGore.proxy = new com.eruannie_9.extragore.CommonProxy();
		}
		com.eruannie_9.extragore.ExtraGore.preInit(event);

		// --- Регистрация предметов и звуков Записок/Дневника ---
		efw.init.EfwModItems.register();
		efw.init.EfwModSounds.register();
		// Регистрация моделей для клиента (должна быть в preInit)
		if (event.getSide() == Side.CLIENT) {
			efw.init.EfwModItems.registerModels();
		}

		// --- Инициализация Wildfire's Gender Mod ---
		File configDir = event.getModConfigurationDirectory();
		File legacyFolder = new File(configDir, "KittGender");
		if (legacyFolder.exists()) {
			legacyFolder.renameTo(new File(configDir, "WildfireGender"));
		}
		// Регистрируем, что пакет идет на КЛИЕНТ (Side.CLIENT)
		// Сеть (Используем отдельный канал, чтобы не ломать пакеты MCreator)
		WildfireGender.NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(WildfireGender.MODID);
		int packetId = 0;
		WildfireGender.NETWORK.registerMessage(PacketSync.Handler.class, PacketSync.class, packetId++, Side.CLIENT);
		WildfireGender.NETWORK.registerMessage(PacketSendGenderInfo.Handler.class, PacketSendGenderInfo.class,
				packetId++, Side.SERVER);

		// Капы для брони
		CapabilityManager.INSTANCE.register(IGenderArmor.class,
				new net.minecraftforge.common.capabilities.Capability.IStorage<IGenderArmor>() {
					@Override
					public NBTBase writeNBT(net.minecraftforge.common.capabilities.Capability<IGenderArmor> capability,
							IGenderArmor inst, EnumFacing side) {
						return null;
					}

					@Override
					public void readNBT(net.minecraftforge.common.capabilities.Capability<IGenderArmor> capability,
							IGenderArmor inst, EnumFacing side, NBTBase nbt) {
					}
				}, () -> null);

		// Капы для Dash & Stamina
		CapabilityManager.INSTANCE.register(com.voltyx.mwccf.dash.DashCapability.IDashData.class, new com.voltyx.mwccf.dash.DashCapability.DashStorage(), () -> new com.voltyx.mwccf.dash.DashCapability.DashData());
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.dash.DashEvents());

		// Регистрируем класс для обработки логина
		MinecraftForge.EVENT_BUS.register(WildfireGender.class);
		// -------------------------------------------
		MinecraftForge.EVENT_BUS.register(techguns.TGBlocks.class);
		MinecraftForge.EVENT_BUS.register(new efw.events.OffhandWeaponBlocker());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		TGBlocks.init(event);
		// MinecraftForge.EVENT_BUS.register(new ToolAIFreezer());
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
		// Принудительно читаем конфиг один раз при запуске игры
		AdvancedHeadshotManager.reloadConfig();
		elements.getElements().forEach(element -> element.init(event));
		proxy.init(event);
		if (event.getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new AnimationTickHandler());
		}

		com.eruannie_9.extragore.ExtraGore.init(event);
		MinecraftForge.EVENT_BUS.register(new HeadshotDamageHandler());
		// ----------------------

		// MCore OreDictionary
		// --- Загрузка конфига текстов записок ---
		efw.config.NotesConfig.load();
		efw.blocks.OtbwgBlocks.registerOres();
		net.minecraftforge.oredict.OreDictionary.registerOre("ingotSteel",
				com.voltyx.mwccf.mcore.MCoreItems.STEEL_INGOT);
		net.minecraftforge.oredict.OreDictionary.registerOre("ingotTitanium",
				com.voltyx.mwccf.mcore.MCoreItems.TITANIUM_INGOT);
		net.minecraftforge.oredict.OreDictionary.registerOre("blockSteel",
				com.voltyx.mwccf.mcore.MCoreBlocks.STEEL_BLOCK);
		net.minecraftforge.oredict.OreDictionary.registerOre("blockTitanium",
				com.voltyx.mwccf.mcore.MCoreBlocks.TITANIUM_BLOCK);
		net.minecraftforge.oredict.OreDictionary.registerOre("nuggetSteel",
				com.voltyx.mwccf.mcore.MCoreItems.STEEL_NUGGET);
		net.minecraftforge.oredict.OreDictionary.registerOre("nuggetTitanium",
				com.voltyx.mwccf.mcore.MCoreItems.TITANIUM_NUGGET);
		net.minecraftforge.oredict.OreDictionary.registerOre("plateSteel",
				com.voltyx.mwccf.mcore.MCoreItems.STEEL_SHEET);
		net.minecraftforge.oredict.OreDictionary.registerOre("plateTitanium",
				com.voltyx.mwccf.mcore.MCoreItems.TITANIUM_SHEET);
		net.minecraftforge.oredict.OreDictionary.registerOre("oreTitanium",
				com.voltyx.mwccf.mcore.MCoreBlocks.DEEPSLATE_TITANIUM_ORE);

		net.minecraftforge.fml.common.registry.GameRegistry.addSmelting(com.voltyx.mwccf.mcore.MCoreItems.STEEL_SCRAP,
				new net.minecraft.item.ItemStack(com.voltyx.mwccf.mcore.MCoreItems.STEEL_INGOT), 0.1F);
		net.minecraftforge.fml.common.registry.GameRegistry.addSmelting(com.voltyx.mwccf.mcore.MCoreItems.RAW_TITANIUM,
				new net.minecraft.item.ItemStack(com.voltyx.mwccf.mcore.MCoreItems.TITANIUM_INGOT), 0.1F);
		net.minecraftforge.fml.common.registry.GameRegistry.addSmelting(
				com.voltyx.mwccf.mcore.MCoreBlocks.DEEPSLATE_TITANIUM_ORE,
				new net.minecraft.item.ItemStack(com.voltyx.mwccf.mcore.MCoreItems.TITANIUM_INGOT), 0.1F);

		// Your Wildfire Gender Mod initialization...
		if (event.getSide() == Side.CLIENT) {
			WildfireEventHandler.registerKeybinds();
			WildfireEventHandler.injectLayers();
			MinecraftForge.EVENT_BUS.register(new com.voltyx.gender.client.event.WardrobeGuiEvents());
		}

		efw.world.biome.rtg.RTGIntegration.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		techguns.plugins.chisel.TGChiselBlocks.postInit();
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		elements.getElements().forEach(element -> element.serverLoad(event));
		proxy.serverLoad(event);
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		// Регистрируем нашу команду
		event.registerServerCommand(new CommandReloadConfig());
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		// 1. Регистрация блоков из Elements
		event.getRegistry().registerAll(elements.getBlocks().stream().map(Supplier::get).toArray(Block[]::new));

		// 2. Регистрация блоков из TGBlocks (БЕЗ дублирования)

		event.getRegistry().register(com.voltyx.mwccf.mcore.MCoreBlocks.STEEL_BLOCK);
		event.getRegistry().register(com.voltyx.mwccf.mcore.MCoreBlocks.TITANIUM_BLOCK);
		event.getRegistry().register(com.voltyx.mwccf.mcore.MCoreBlocks.DEEPSLATE_TITANIUM_ORE);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(elements.getItems().stream().map(Supplier::get).toArray(Item[]::new));

		for (Item item : com.voltyx.mwccf.mcore.MCoreItems.ITEMS) {
			if (item instanceof com.voltyx.mwccf.geo.ItemGeoArmor) {
				if (efw.biomeinfo.MwccfConfig.armor.disableMarbledArmor && item.getRegistryName() != null && !item.getRegistryName().getPath().equals("bracelet"))
					continue;
			} else if (item instanceof com.voltyx.mwccf.mcore.ItemCustomArmor) {
				if (efw.biomeinfo.MwccfConfig.armor.disableInstinctArmor)
					continue;
			}
			event.getRegistry().register(item);
		}

		event.getRegistry().register(com.voltyx.mwccf.dash.ItemRedbull.INSTANCE);
		event.getRegistry().register(com.voltyx.mwccf.item.ItemAdrenaline.INSTANCE);
		event.getRegistry().register(com.voltyx.mwccf.item.ItemMorphineSyringe.INSTANCE);
		event.getRegistry().register(com.voltyx.mwccf.item.ItemSyringe.INSTANCE);

		event.getRegistry().register(new net.minecraft.item.ItemBlock(com.voltyx.mwccf.mcore.MCoreBlocks.STEEL_BLOCK)
				.setRegistryName(com.voltyx.mwccf.mcore.MCoreBlocks.STEEL_BLOCK.getRegistryName()));
		event.getRegistry().register(new net.minecraft.item.ItemBlock(com.voltyx.mwccf.mcore.MCoreBlocks.TITANIUM_BLOCK)
				.setRegistryName(com.voltyx.mwccf.mcore.MCoreBlocks.TITANIUM_BLOCK.getRegistryName()));
		event.getRegistry()
				.register(new net.minecraft.item.ItemBlock(com.voltyx.mwccf.mcore.MCoreBlocks.DEEPSLATE_TITANIUM_ORE)
						.setRegistryName(com.voltyx.mwccf.mcore.MCoreBlocks.DEEPSLATE_TITANIUM_ORE.getRegistryName()));
	}

	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event) {
		event.getRegistry().registerAll(elements.getBiomes().stream().map(Supplier::get).toArray(Biome[]::new));
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().registerAll(elements.getEntities().stream().map(Supplier::get).toArray(EntityEntry[]::new));
		event.getRegistry().register(net.minecraftforge.fml.common.registry.EntityEntryBuilder.create()
				.entity(com.voltyx.mwccf.geo.EntityHeadlampLight.class)
				.id(new net.minecraft.util.ResourceLocation(MODID, "headlamp_light"), 1099)
				.name("headlamp_light")
				.tracker(64, 3, false)
				.build());
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll(elements.getPotions().stream().map(Supplier::get).toArray(Potion[]::new));
		event.getRegistry().register(com.voltyx.mwccf.dash.PotionEnergyBoost.INSTANCE);
		event.getRegistry().register(com.voltyx.mwccf.potion.PotionAdrenalineEffect.INSTANCE);
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
		elements.registerSounds(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		elements.getElements().forEach(element -> element.registerModels(event));
		for (Item item : com.voltyx.mwccf.mcore.MCoreItems.ITEMS) {
			net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName(),
							"inventory"));
		}
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(com.voltyx.mwccf.dash.ItemRedbull.INSTANCE, 0,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(com.voltyx.mwccf.dash.ItemRedbull.INSTANCE.getRegistryName(),
							"inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(com.voltyx.mwccf.item.ItemAdrenaline.INSTANCE, 0,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(com.voltyx.mwccf.item.ItemAdrenaline.INSTANCE.getRegistryName(),
							"inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(com.voltyx.mwccf.item.ItemMorphineSyringe.INSTANCE, 0,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(com.voltyx.mwccf.item.ItemMorphineSyringe.INSTANCE.getRegistryName(),
							"inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(com.voltyx.mwccf.item.ItemSyringe.INSTANCE, 0,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(com.voltyx.mwccf.item.ItemSyringe.INSTANCE.getRegistryName(),
							"inventory"));
		Item[] blockItems = {
				Item.getItemFromBlock(com.voltyx.mwccf.mcore.MCoreBlocks.STEEL_BLOCK),
				Item.getItemFromBlock(com.voltyx.mwccf.mcore.MCoreBlocks.TITANIUM_BLOCK),
				Item.getItemFromBlock(com.voltyx.mwccf.mcore.MCoreBlocks.DEEPSLATE_TITANIUM_ORE)
		};
		for (Item item : blockItems) {
			net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0,
					new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName(),
							"inventory"));
		}
	}

	static {
		FluidRegistry.enableUniversalBucket();
	}
}