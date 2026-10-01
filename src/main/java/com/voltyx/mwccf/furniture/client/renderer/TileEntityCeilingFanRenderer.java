package com.voltyx.mwccf.furniture.client.renderer;

import com.voltyx.mwccf.furniture.tileentity.TileEntityCeilingFan;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityCeilingFanRenderer extends TileEntitySpecialRenderer<TileEntityCeilingFan> {

    private static final ResourceLocation TEXTURE_LIGHT = new ResourceLocation("refurbished_furniture", "textures/blocks/oak_light_ceiling_fan.png");
    private static final ResourceLocation TEXTURE_DARK = new ResourceLocation("refurbished_furniture", "textures/blocks/oak_dark_ceiling_fan.png");

    @Override
    public void render(TileEntityCeilingFan te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld()) return;

        float delta = te.fanAngle - te.prevFanAngle;
        while (delta < -180.0F) delta += 360.0F;
        while (delta >= 180.0F) delta -= 360.0F;
        float angle = te.prevFanAngle + delta * partialTicks;

        boolean isDark = te.getBlockType().getTranslationKey().contains("dark");
        this.bindTexture(isDark ? TEXTURE_DARK : TEXTURE_LIGHT);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 9.0D / 16.0D, z + 0.5D);
        GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.disableCull();

        for (int i = 0; i < 4; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(i * 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(8.0F, 0.0F, 0.0F, 1.0F);

            double minX = -0.15625D;
            double maxX = 0.15625D;
            double minZ = 0.15D;
            double maxZ = 1.05D;
            double h = 0.015D;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

            buffer.pos(minX, h, minZ).tex(0.15625D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            buffer.pos(minX, h, maxZ).tex(0.15625D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            buffer.pos(maxX, h, maxZ).tex(0.3125D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            buffer.pos(maxX, h, minZ).tex(0.3125D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();

            buffer.pos(minX, -h, minZ).tex(0.15625D, 0.0D).normal(0.0F, -1.0F, 0.0F).endVertex();
            buffer.pos(maxX, -h, minZ).tex(0.3125D, 0.0D).normal(0.0F, -1.0F, 0.0F).endVertex();
            buffer.pos(maxX, -h, maxZ).tex(0.3125D, 1.0D).normal(0.0F, -1.0F, 0.0F).endVertex();
            buffer.pos(minX, -h, maxZ).tex(0.15625D, 1.0D).normal(0.0F, -1.0F, 0.0F).endVertex();

            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
