package com.voltyx.mwccf.geo;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import efw.biomeinfo.MwccfConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class KawaiiEarsLayer implements LayerRenderer<AbstractClientPlayer> {

    private static final ResourceLocation GEO_LOCATION = new ResourceLocation("mwccf", "geo/kawaii_ears.geo.json");
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("mwccf", "textures/models/armor/kawaii_ears.png");

    private static GeoArmorModel cachedModel;

    private final RenderPlayer renderer;
    private final String skinTypeKey;

    public KawaiiEarsLayer(RenderPlayer renderer, String skinTypeKey) {
        this.renderer = renderer;
        this.skinTypeKey = skinTypeKey;
    }

    public static GeoArmorModel getModel() {
        if (cachedModel == null) {
            cachedModel = new GeoArmorModel(GEO_LOCATION);
        }
        return cachedModel;
    }

    public static boolean hasKawaiiEarsEquipped(EntityPlayer player) {
        if (player == null) return false;

        String mode = MwccfConfig.kawaiiEars.mode;
        if ("OFF".equalsIgnoreCase(mode)) {
            return false;
        }
        if ("ALWAYS".equalsIgnoreCase(mode)) {
            return true;
        }

        // 1. Check Baubles
        if (Loader.isModLoaded("baubles")) {
            try {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemKawaiiEars) {
                            return true;
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        // 2. Check helmet slot
        ItemStack headStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (!headStack.isEmpty() && headStack.getItem() instanceof ItemKawaiiEars) {
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (hasKawaiiEarsEquipped(player)) {
                KawaiiEarsAnimationHandler.update(player);
            }
        }
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float delta,
                              float age, float yaw, float pitch, float scale) {
        if (efw.util.RenderContext.isRenderingPlayerInSevenScreen) {
            return;
        }

        // Don't render for self in first-person mode
        if (player == Minecraft.getMinecraft().player &&
                Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        if (!hasKawaiiEarsEquipped(player)) return;

        String playerSkin = "slim".equals(player.getSkinType()) ? "slim" : "default";
        if (!playerSkin.equals(this.skinTypeKey)) return;

        GeoArmorModel model = getModel();
        if (model == null) return;

        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(player, limbSwing, limbSwingAmount, delta);
        model.currentSlot = EntityEquipmentSlot.HEAD;

        this.renderer.bindTexture(TEXTURE_LOCATION);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();

        model.syncedModel = this.renderer.getMainModel();

        // Apply dynamic procedural & state-based animations to ear2 and ear3
        KawaiiEarsAnimationHandler.applyAnimations(model, player, delta);

        // Render GeoArmorModel (synced with player model)
        model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);

        GlStateManager.disableCull();
        model.syncedModel = null;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
