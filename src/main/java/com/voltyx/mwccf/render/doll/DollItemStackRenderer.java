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

        // ModelRenderer в Minecraft использует ось Y вниз. Инвертируем Y.
        GlStateManager.scale(1.0F, -1.0F, 1.0F);

        // Центрирование пивота модели куклы
        GlStateManager.translate(0.0F, -0.65F, 0.0F);

        mc.getTextureManager().bindTexture(TEX_LOC);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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

        // Восстанавливаем стандартный обход и состояние OpenGL без рассинхронизации GlStateManager
        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CCW);
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack itemStack) {
        renderByItem(itemStack, 1.0F);
    }
}
