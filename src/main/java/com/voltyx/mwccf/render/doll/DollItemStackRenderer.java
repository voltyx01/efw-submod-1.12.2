package com.voltyx.mwccf.render.doll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 3D рендерер предмета куклы Сайи для GUI, пола (dropped item), третьих лиц и рамок.
 */
@SideOnly(Side.CLIENT)
public class DollItemStackRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEX_LOC = new ResourceLocation("mwccf", "textures/entity/doll.png");

    @Override
    public void renderByItem(ItemStack itemStack, float partialTicks) {
        BedrockDollModel model = DollRenderer.getModel();
        if (model == null || !model.isLoaded()) return;

        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();

        // Центрируем смещение ваниллы (-0.5, -0.5, -0.5 в RenderItem.renderItem)
        GlStateManager.translate(0.5F, 0.5F, 0.5F);

        float guiScale = (float) efw.biomeinfo.MwccfConfig.doll.guiScale;
        if (guiScale <= 0.001F) guiScale = 1.0F;

        // ModelRenderer в Minecraft использует ось Y вниз. Инвертируем Y и применяем масштаб.
        GlStateManager.scale(guiScale, -guiScale, guiScale);

        // Центрирование пивота модели куклы
        float guiOffsetY = (float) efw.biomeinfo.MwccfConfig.doll.guiOffsetY;
        GlStateManager.translate(0.0F, guiOffsetY, 0.0F);

        mc.getTextureManager().bindTexture(TEX_LOC);

        boolean ready = mc.player != null && com.voltyx.mwccf.doll.SayaDollManager.isReady(mc.player);
        float redG = 1.0F;
        float redB = 1.0F;
        if (ready) {
            long now = System.currentTimeMillis();
            float pulse = (float) (Math.sin((now % 800L) / 800.0 * Math.PI * 2.0) * 0.5 + 0.5);
            redG = 1.0F - pulse * 0.85F;
            redB = 1.0F - pulse * 0.85F;
        }

        GlStateManager.color(1.0F, redG, redB, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();

        // При инверсии Y порядок обхода вершин становится CW.
        // Сообщаем OpenGL, что лицевые грани имеют обход CW, чтобы внешние грани не считались back-face!
        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CW);

        // Поза куклы (аккуратная поза удержания)
        model.applyAnimation("animation", 0.625F);
        model.render(0.0625F);

        if (ready) {
            // Additive crimson glow pass for vibrant blinking
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            float glowAlpha = (1.0F - redG) * 0.7F;
            GlStateManager.color(1.0F, 0.15F, 0.15F, glowAlpha);
            model.render(0.0625F);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }

        // Восстанавливаем стандартный обход и состояние OpenGL для GUI и рендера мира
        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CCW);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.enableCull();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack itemStack) {
        renderByItem(itemStack, 1.0F);
    }
}
