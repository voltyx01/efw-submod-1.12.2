package com.voltyx.mwccf;

import com.voltyx.mwccf.client.loading.ItemLoadingScreenRenderer;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import com.voltyx.mwccf.backpack.BackpackLayer;
import com.voltyx.mwccf.client.loading.LoadingScreenConfig;
import com.voltyx.mwccf.client.loading.LoadingScreenHook;
import com.voltyx.mwccf.sins.client.render.LayerBlinkingEyes;
import com.voltyx.mwccf.HeadHitboxDebugger;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.util.Map;
import com.teamderpy.shouldersurfing.client.KeyHandler;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import com.teamderpy.shouldersurfing.compatibility.EnumShaderCompatibility;
import com.teamderpy.shouldersurfing.config.Config;
import com.teamderpy.shouldersurfing.event.ClientEventHandler;
import com.teamderpy.shouldersurfing.lockon.LockOnConfig;
import com.teamderpy.shouldersurfing.lockon.LockOnHandler;
import com.teamderpy.shouldersurfing.lockon.RenderLockOnHandler;

public class ClientProxyMwccfMod implements IProxyMwccfMod {
	@Override
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.client.ClientLootingManager());
		com.voltyx.mwccf.dash.DashKeyHandler.register();
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.dash.DashKeyHandler());

		// Shoulder Surfing Keybindings & Handlers
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_CAMERA_LEFT);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_CAMERA_RIGHT);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_CAMERA_IN);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_CAMERA_OUT);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_CAMERA_UP);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_CAMERA_DOWN);
		net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(KeyHandler.KEYBIND_SWAP_SHOULDER);
		net.minecraftforge.fml.client.registry.ClientRegistry
				.registerKeyBinding(KeyHandler.KEYBIND_TOGGLE_SHOULDER_SURFING);

		LockOnHandler.init();
		MinecraftForge.EVENT_BUS.register(new LockOnHandler());
		MinecraftForge.EVENT_BUS.register(new RenderLockOnHandler());

		net.minecraft.client.resources.IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
		loadAllFromFolder(rm, "mwccf", "animations");
		MinecraftForge.EVENT_BUS.register(new efw.biomeinfo.BiomeInfoRenderer());
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.dash.OverlayStamina());
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.client.inspect.InspectTransitionHandler());

		// Pre-enable stencil buffer early at game init to prevent white flash during
		// first weapon modding GUI open
		net.minecraft.client.shader.Framebuffer fb = Minecraft.getMinecraft().getFramebuffer();
		if (fb != null && !fb.isStencilEnabled()) {
			fb.enableStencil();
		}
		registerBlinkingLayer();
	}

	public static void registerBlinkingLayer() {
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();

		// Добавляем к толстому скину (Стив)
		RenderPlayer defaultRender = skinMap.get("default");
		if (defaultRender != null) {
			defaultRender.addLayer(new LayerBlinkingEyes(defaultRender));
		}

		// Добавляем к тонкому скину (Алекс)
		RenderPlayer slimRender = skinMap.get("slim");
		if (slimRender != null) {
			slimRender.addLayer(new LayerBlinkingEyes(slimRender));
		}
	}

	public static void loadAllFromFolder(net.minecraft.client.resources.IResourceManager rm, String domain,
			String folderPath) {
		String[] files = {
				"axe.json", "axe_sneak.json", "backwards_in_water.json", "blank_loop.json", "boat1.json",
				"bow_idle.json", "bow_sneak.json", "climbing.json", "climbing_backwards.json", "climbing_sneak.json",
				"crawling.json", "crawling_backwards.json", "eating.json", "eating_left.json", "eating_left_sneak.json",
				"eating_right.json", "eating_right_sneak.json", "elytra.json", "falling.json", "forward_in_water.json",
				"horse_idle.json", "horse_pickaxe.json", "horse_running.json", "idle_climbing.json",
				"idle_climbing_sneak.json",
				"idle_crawling.json", "idle_creative_flying.json", "idle_creative_flying_item.json",
				"idle_in_water.json",
				"idle_sneak.json", "idle_standing.json", "minecart_idle.json", "minecart_pickaxe.json",
				"pickaxe.json", "pickaxe_sneak.json", "running.json", "shield.json", "shield_sneak.json",
				"shield_left.json", "shield_left_sneak.json",
				"shovel.json", "shovel_sneak.json", "sleeping.json", "swimming.json", "sword_attack.json",
				"sword_attack2.json", "sword_attack_sneak.json", "sword_attack_sneak2.json",
				"turn_left.json", "turn_right.json", "up_in_water.json", "walking.json",
				"walking_backwards.json", "walking_sneak.json", "walking_sneak_backwards.json", "roll.json"
		};

		for (String file : files) {
			String fullPath = domain + ":" + folderPath + "/" + file;
			efw.animation.AnimationRegistry.loadFromResource(rm, fullPath, null);
		}

		// Load weapon animations with namespaces
		efw.animation.AnimationRegistry.loadFromResource(rm,
				domain + ":" + folderPath + "/pistol_default.player_animation.json", "pistol_");
		efw.animation.AnimationRegistry.loadFromResource(rm,
				domain + ":" + folderPath + "/rifle_default.player_animation.json", "rifle_");
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		OBJLoader.INSTANCE.addDomain("mwccf");

		// Shoulder Surfing & Lock-On Config & Events
		LockOnConfig.init(event.getModConfigurationDirectory());
		Config.CLIENT = new Config.ClientConfig(new net.minecraftforge.common.config.Configuration(
				new File(event.getModConfigurationDirectory(), "shouldersurfing.cfg")));
		ShoulderInstance.getInstance().changePerspective(Config.CLIENT.getDefaultPerspective());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new KeyHandler());

		MinecraftForge.EVENT_BUS.register(new LoadingScreenHook());
		MinecraftForge.EVENT_BUS.register(this); // <-- регистрируем сам прокси
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.geo.HeartbeatEventHandler());
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.geo.VisualEffectsHandler());
		File cfgDir = event.getModConfigurationDirectory();
		LoadingScreenConfig.load(cfgDir);
		com.voltyx.mwccf.client.inspect.ItemInspectDescConfig.load(cfgDir);
		com.voltyx.mwccf.client.inspect.ItemInspectConfig.load(cfgDir);
		com.voltyx.mwccf.geo.BraceletInspectHandler.init();

		// Removed HeadlampLight registration
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.geo.HeadlampRenderer());
		MinecraftForge.EVENT_BUS.register(new com.voltyx.mwccf.geo.HeadlampKeyHandler());
		com.voltyx.mwccf.geo.HeadlampKeyHandler.init();

		net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(
				com.voltyx.mwccf.furniture.tileentity.TileEntityFridge.class,
				new com.voltyx.mwccf.furniture.client.renderer.TileEntityFridgeRenderer());
		net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(
				com.voltyx.mwccf.furniture.tileentity.TileEntityMicrowave.class,
				new com.voltyx.mwccf.furniture.client.renderer.TileEntityMicrowaveRenderer());

		net.minecraftforge.fml.client.registry.RenderingRegistry
				.registerEntityRenderingHandler(com.voltyx.mwccf.furniture.EntitySeat.class, manager -> {
					return new net.minecraft.client.renderer.entity.Render<com.voltyx.mwccf.furniture.EntitySeat>(
							manager) {
						@Override
						protected net.minecraft.util.ResourceLocation getEntityTexture(
								com.voltyx.mwccf.furniture.EntitySeat entity) {
							return null;
						}

						@Override
						public void doRender(com.voltyx.mwccf.furniture.EntitySeat entity, double x, double y, double z,
								float entityYaw, float partialTicks) {
						}
					};
				});
		net.minecraftforge.fml.client.registry.RenderingRegistry
				.registerEntityRenderingHandler(com.voltyx.mwccf.geo.EntityHeadlampLight.class, manager -> {
					return new net.minecraft.client.renderer.entity.Render<com.voltyx.mwccf.geo.EntityHeadlampLight>(
							manager) {
						@Override
						protected net.minecraft.util.ResourceLocation getEntityTexture(
								com.voltyx.mwccf.geo.EntityHeadlampLight entity) {
							return null;
						}

						@Override
						public void doRender(com.voltyx.mwccf.geo.EntityHeadlampLight entity, double x, double y,
								double z, float entityYaw, float partialTicks) {
							// Do nothing - invisible entity
						}
					};
				});
	}

	@SubscribeEvent
	public void onTextureStitch(net.minecraftforge.client.event.TextureStitchEvent.Post event) {
		ItemLoadingScreenRenderer.warmupAll();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		java.util.Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		for (java.util.Map.Entry<String, RenderPlayer> entry : skinMap.entrySet()) {
			entry.getValue().addLayer(new BackpackLayer(entry.getValue()));
			// Pass the skin type key so the layer only renders for matching players
			entry.getValue().addLayer(new com.voltyx.mwccf.geo.BraceletLayer(entry.getValue(), entry.getKey()));
			entry.getValue().addLayer(new com.voltyx.mwccf.geo.HeadlampLayer(entry.getValue(), entry.getKey()));
		}
		ItemLoadingScreenRenderer.warmupAll();
		if (net.minecraftforge.fml.common.Loader.isModLoaded("optifine")) {
			ShoulderRenderer.getInstance().setShaderType(EnumShaderCompatibility.NEW);
		}
	}

	@Override
	public void serverLoad(FMLServerStartingEvent event) {
	}

}
