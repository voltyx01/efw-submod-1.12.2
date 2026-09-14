package com.voltyx.mwccf.zone.client;

import com.voltyx.mwccf.item.ItemZoneTool;
import com.voltyx.mwccf.zone.QuestZone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientZoneRenderer {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null) return;

        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        boolean holdingTool = (main.getItem() instanceof ItemZoneTool) || (off.getItem() instanceof ItemZoneTool);
        if (!holdingTool) return;

        ItemStack toolStack = (main.getItem() instanceof ItemZoneTool) ? main : off;
        BlockPos p1 = ItemZoneTool.getPos1(toolStack);
        BlockPos p2 = ItemZoneTool.getPos2(toolStack);

        double viewX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        double viewY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        double viewZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.5F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();

        int currentDim = player.world.provider.getDimension();

        // 1. Отрисовка существующих зон (Голубые рамки)
        List<QuestZone> existing = ClientZoneCache.getZones();
        for (QuestZone zone : existing) {
            if (zone.getDimension() != currentDim) continue;
            AxisAlignedBB bb = zone.getAABB().offset(-viewX, -viewY, -viewZ);
            RenderGlobal.drawSelectionBoundingBox(bb, 0.2F, 0.7F, 1.0F, 0.8F);
            renderFilledBox(bb, 0.2F, 0.7F, 1.0F, 0.12F);
        }

        // 2. Отрисовка текущего выделения (Зеленая рамка)
        if (p1 != null && p2 != null) {
            int minX = Math.min(p1.getX(), p2.getX());
            int minY = Math.min(p1.getY(), p2.getY());
            int minZ = Math.min(p1.getZ(), p2.getZ());
            int maxX = Math.max(p1.getX(), p2.getX());
            int maxY = Math.max(p1.getY(), p2.getY());
            int maxZ = Math.max(p1.getZ(), p2.getZ());

            AxisAlignedBB selBB = new AxisAlignedBB(minX, minY, minZ, maxX + 1.0D, maxY + 1.0D, maxZ + 1.0D).offset(-viewX, -viewY, -viewZ);
            RenderGlobal.drawSelectionBoundingBox(selBB, 0.1F, 1.0F, 0.3F, 0.9F);
            renderFilledBox(selBB, 0.1F, 1.0F, 0.3F, 0.18F);
        } else if (p1 != null) {
            AxisAlignedBB p1BB = new AxisAlignedBB(p1).offset(-viewX, -viewY, -viewZ);
            RenderGlobal.drawSelectionBoundingBox(p1BB, 1.0F, 0.8F, 0.1F, 0.9F);
        } else if (p2 != null) {
            AxisAlignedBB p2BB = new AxisAlignedBB(p2).offset(-viewX, -viewY, -viewZ);
            RenderGlobal.drawSelectionBoundingBox(p2BB, 0.1F, 0.8F, 1.0F, 0.9F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void renderFilledBox(AxisAlignedBB bb, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // Нижняя грань
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();

        // Верхняя грань
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();

        // Север
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();

        // Юг
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();

        // Запад
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();

        // Восток
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();

        tessellator.draw();
    }
}
