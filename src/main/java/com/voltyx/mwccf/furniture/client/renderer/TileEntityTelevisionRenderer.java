package com.voltyx.mwccf.furniture.client.renderer;

import com.voltyx.mwccf.furniture.BlockFurnitureHorizontal;
import com.voltyx.mwccf.furniture.tileentity.TileEntityTelevision;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TileEntityTelevisionRenderer extends TileEntitySpecialRenderer<TileEntityTelevision> {

    private static class ChannelInfo {
        final int frames;
        final int frametime;
        ChannelInfo(int frames, int frametime) {
            this.frames = frames;
            this.frametime = Math.max(1, frametime);
        }
    }

    private static final Map<String, ResourceLocation> CHANNEL_TEXTURES = new HashMap<>();
    private static final Map<String, ChannelInfo> CHANNEL_INFO = new HashMap<>();
    static {
        CHANNEL_INFO.put("black_noise", new ChannelInfo(4, 1));
        CHANNEL_INFO.put("block_game", new ChannelInfo(4, 6));
        CHANNEL_INFO.put("colour_test", new ChannelInfo(17, 1));
        CHANNEL_INFO.put("dance_music", new ChannelInfo(9, 2));
        CHANNEL_INFO.put("heart_screensaver", new ChannelInfo(22, 1));
        CHANNEL_INFO.put("herobrine", new ChannelInfo(4, 3));
        CHANNEL_INFO.put("ocean_sunset", new ChannelInfo(16, 4));
        CHANNEL_INFO.put("pong", new ChannelInfo(40, 2));
        CHANNEL_INFO.put("rip_blizzard", new ChannelInfo(2, 4));
        CHANNEL_INFO.put("silly_face", new ChannelInfo(4, 5));
        CHANNEL_INFO.put("villager_news", new ChannelInfo(17, 5));
        CHANNEL_INFO.put("white_noise", new ChannelInfo(4, 1));
    }

    private static ResourceLocation getChannelTexture(String name) {
        return CHANNEL_TEXTURES.computeIfAbsent(name, n ->
                new ResourceLocation("refurbished_furniture", "textures/tv_channels/" + n + ".png"));
    }

    @Override
    public void render(TileEntityTelevision te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld() || !te.isPowered()) return;

        IBlockState state = te.getWorld().getBlockState(te.getPos());
        EnumFacing facing = EnumFacing.NORTH;
        if (state.getPropertyKeys().contains(BlockFurnitureHorizontal.FACING)) {
            facing = state.getValue(BlockFurnitureHorizontal.FACING);
        }

        String channelName = te.getCurrentChannelName();
        this.bindTexture(getChannelTexture(channelName));

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

        switch (facing) {
            case NORTH: GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F); break;
            case SOUTH: GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F); break;
            case WEST: GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F); break;
            case EAST: GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F); break;
        }

        GlStateManager.translate(-0.5D, -0.5D, -0.5D);

        // Make the TV screen glow in the dark and prevent directional shading
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int prevLightX = (int) OpenGlHelper.lastBrightnessX;
        int prevLightY = (int) OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        double minX = 2.95D / 16.0D;
        double maxX = 13.05D / 16.0D;
        double minY = 1.95D / 16.0D;
        double maxY = 11.05D / 16.0D;
        double screenZ = 14.01D / 16.0D;

        ChannelInfo info = CHANNEL_INFO.getOrDefault(channelName, new ChannelInfo(1, 1));
        long time = te.getWorld().getTotalWorldTime();
        int frameIndex = (int) ((time / info.frametime) % info.frames);
        double minV = (double) frameIndex / (double) info.frames;
        double maxV = (double) (frameIndex + 1) / (double) info.frames;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(minX, minY, screenZ).tex(0.0D, maxV).endVertex();
        buffer.pos(maxX, minY, screenZ).tex(1.0D, maxV).endVertex();
        buffer.pos(maxX, maxY, screenZ).tex(1.0D, minV).endVertex();
        buffer.pos(minX, maxY, screenZ).tex(0.0D, minV).endVertex();
        tessellator.draw();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevLightX, prevLightY);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }
}
