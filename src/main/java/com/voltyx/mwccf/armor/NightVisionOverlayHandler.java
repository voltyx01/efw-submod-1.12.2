package com.voltyx.mwccf.armor;

import com.voltyx.mwccf.mcore.ItemCustomArmor;
import com.voltyx.mwccf.mcore.MCoreItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NightVisionOverlayHandler {

    private static final ResourceLocation NV_OVERLAY = new ResourceLocation("mwccf", "textures/screens/night_vision_hunter.png");
    private static final ResourceLocation VIGNETTE_TEX = new ResourceLocation("textures/misc/vignette.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HELMET) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null) return;

        ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helm.isEmpty()) return;

        Item item = helm.getItem();
        boolean isNVG = (item == MCoreItems.NIGHTVISIONGOGGLES_HELMET ||
                         item == MCoreItems.GREENHUNTER_HELMET ||
                         item == MCoreItems.DESERTHUNTER_HELMET ||
                         item == MCoreItems.BLACKHUNTER_HELMET ||
                         item == MCoreItems.EXOHEAVYBLACK_HELMET ||
                         item == MCoreItems.EXOHEAVYDESERT_HELMET ||
                         item == MCoreItems.EXOHEAVYGREEN_HELMET);

        // When toggled off, overlay immediately disappears
        if (isNVG && ItemCustomArmor.isNVGActive(helm)) {
            float progress = NVGAnimationHelper.getProgress(player, true);
            if (progress > 0.01f) {
                ScaledResolution res = event.getResolution();
                int width = res.getScaledWidth();
                int height = res.getScaledHeight();

                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();

                // 1. Draw green NVG screen overlay (visible in both 1st and 3rd person)
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.color(1.0F, 1.0F, 1.0F, progress);

                mc.getTextureManager().bindTexture(NV_OVERLAY);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(0.0D, height, -90.0D).tex(0.0D, 1.0D).endVertex();
                bufferbuilder.pos(width, height, -90.0D).tex(1.0D, 1.0D).endVertex();
                bufferbuilder.pos(width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
                bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
                tessellator.draw();

                // 2. Draw Vignette (ONLY in 1st person, not in 3rd person)
                if (mc.gameSettings.thirdPersonView == 0) {
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    float vig = Math.min(1.0F, progress);
                    GlStateManager.color(vig, vig, vig, 1.0F);

                    mc.getTextureManager().bindTexture(VIGNETTE_TEX);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    bufferbuilder.pos(0.0D, height, -90.0D).tex(0.0D, 1.0D).endVertex();
                    bufferbuilder.pos(width, height, -90.0D).tex(1.0D, 1.0D).endVertex();
                    bufferbuilder.pos(width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
                    bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
                    tessellator.draw();

                    // Second pass for authentic NVG optic tube shadow depth
                    GlStateManager.color(vig * 0.75F, vig * 0.75F, vig * 0.75F, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    bufferbuilder.pos(0.0D, height, -90.0D).tex(0.0D, 1.0D).endVertex();
                    bufferbuilder.pos(width, height, -90.0D).tex(1.0D, 1.0D).endVertex();
                    bufferbuilder.pos(width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
                    bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
                    tessellator.draw();
                }

                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                // 3. Tactical HUD Battery indicator (visible when visor is lowered)
                if (progress > 0.5f && helm.hasTagCompound()) {
                    net.minecraft.nbt.NBTTagCompound tag = helm.getTagCompound();
                    int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
                    int percent = Math.max(0, Math.min(100, (int) ((charge / 48000.0f) * 100)));
                    String batText = "BAT: " + percent + "%";
                    int textWidth = mc.fontRenderer.getStringWidth(batText);
                    int textX = width - textWidth - 16;
                    int textY = 16;
                    int color = percent > 50 ? 0x55FF55 : (percent > 20 ? 0xFFFF55 : 0xFF5555);
                    mc.fontRenderer.drawStringWithShadow(batText, textX, textY, color);
                }

                // Cleanly reset GL color and blend state so subsequent HUD elements (e.g. MWC ammo counter) are not tainted
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
        }
    }
}
