package com.voltyx.mwccf.block.lamp;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityFlickeringLampRenderer extends TileEntitySpecialRenderer<TileEntityFlickeringLamp> {

    private static final ResourceLocation TEX_OFF = new ResourceLocation("mwccf", "textures/blocks/flickering_lamp_off.png");
    private static final ResourceLocation TEX_ON  = new ResourceLocation("mwccf", "textures/blocks/flickering_lamp_on.png");

    @Override
    public void render(TileEntityFlickeringLamp te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld()) return;

        World world = te.getWorld();
        BlockPos pos = te.getPos();

        // Check which faces of the lamp block are exposed to air or transparent blocks
        boolean[] renderFaces = new boolean[6];
        boolean hasAnyVisibleFace = false;

        for (int i = 0; i < 6; i++) {
            EnumFacing facing = EnumFacing.VALUES[i];
            BlockPos neighborPos = pos.offset(facing);
            IBlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof BlockFlickeringLamp) {
                renderFaces[i] = false;
                continue;
            }
            if (neighborState.doesSideBlockRendering(world, neighborPos, facing.getOpposite())) {
                renderFaces[i] = false;
                continue;
            }
            renderFaces[i] = true;
            hasAnyVisibleFace = true;
        }

        if (!hasAnyVisibleFace) {
            return;
        }

        int prevLightX = (int) OpenGlHelper.lastBrightnessX;
        int prevLightY = (int) OpenGlHelper.lastBrightnessY;

        float currentAlpha = te.getInterpolatedBrightness(partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        // ==========================================
        // PASS 1: Base unlit lamp (flickering_lamp_off)
        // ==========================================
        this.bindTexture(TEX_OFF);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 6; i++) {
            if (!renderFaces[i]) continue;
            EnumFacing facing = EnumFacing.VALUES[i];
            BlockPos neighborPos = pos.offset(facing);

            int combinedLight = world.getCombinedLight(neighborPos, 0);
            int lx = combinedLight % 65536;
            int ly = combinedLight / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lx, (float) ly);

            float shade = getFaceShading(facing);

            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            renderFace(buf, facing, shade, shade, shade, 1.0F);
            tessellator.draw();
        }

        // ==========================================
        // PASS 2: Glowing overlay (flickering_lamp_on)
        // ==========================================
        if (currentAlpha > 0.005F) {
            this.bindTexture(TEX_ON);

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(-0.2F, -1.0F);
            GlStateManager.depthMask(false);

            // Fullbright for lit filament texture
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            for (int i = 0; i < 6; i++) {
                if (!renderFaces[i]) continue;
                EnumFacing facing = EnumFacing.VALUES[i];
                renderFace(buf, facing, 1.0F, 1.0F, 1.0F, currentAlpha);
            }
            tessellator.draw();

            GlStateManager.disablePolygonOffset();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
        }

        // Restore original lightmap coords and color
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) prevLightX, (float) prevLightY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private float getFaceShading(EnumFacing facing) {
        switch (facing) {
            case DOWN:  return 0.5F;
            case UP:    return 1.0F;
            case NORTH:
            case SOUTH: return 0.8F;
            case WEST:
            case EAST:  return 0.6F;
            default:    return 1.0F;
        }
    }

    private void renderFace(BufferBuilder buf, EnumFacing facing, float r, float g, float b, float a) {
        switch (facing) {
            case DOWN:
                buf.pos(0.0, 0.0, 1.0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 0.0, 0.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 0.0, 1.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                break;
            case UP:
                buf.pos(0.0, 1.0, 0.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 1.0, 1.0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 1.0, 0.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
                break;
            case NORTH:
                buf.pos(1.0, 1.0, 0.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 0.0, 0.0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 0.0, 0.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 1.0, 0.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
                break;
            case SOUTH:
                buf.pos(0.0, 1.0, 1.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 0.0, 1.0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 0.0, 1.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                break;
            case WEST:
                buf.pos(0.0, 1.0, 0.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 0.0, 0.0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 0.0, 1.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(0.0, 1.0, 1.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
                break;
            case EAST:
                buf.pos(1.0, 1.0, 1.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 0.0, 1.0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 0.0, 0.0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
                buf.pos(1.0, 1.0, 0.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
                break;
        }
    }
}
