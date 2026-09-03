package com.paneedah.weaponlib;

import io.redstudioragnarok.redcore.vectors.Vector3D;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static com.paneedah.mwc.proxies.ClientProxy.MC;
import static com.paneedah.mwc.utils.ModReference.ID;

public class BulletHoleRenderer {

    private final LinkedBlockingQueue<BulletHole> holeQueue = new LinkedBlockingQueue<>();
    private final ArrayList<BulletHole> bulletHoles = new ArrayList<>();

    public static class BulletHole {
        private Vector3D pos;
        private net.minecraft.util.math.BlockPos blockPos;
        private boolean shouldDie;
        private EnumFacing direction;
        private double size;
        private long timeExisted;

        public BulletHole(Vector3D pos, net.minecraft.util.math.BlockPos blockPos, EnumFacing direction, double size) {
            this.pos = pos;
            this.blockPos = blockPos;
            this.direction = direction;
            this.size = size;
            this.timeExisted = System.currentTimeMillis();
        }

        public BulletHole(Vector3D pos, EnumFacing direction, double size) {
            this(pos, null, direction, size);
        }
    }


    public void addBulletHole(BulletHole hole) {
        if (!com.paneedah.weaponlib.config.ModernConfigManager.enableBulletHoles) {
            return;
        }
        this.holeQueue.add(hole);
    }

    public void render() {
        if (!com.paneedah.weaponlib.config.ModernConfigManager.enableBulletHoles) {
            this.holeQueue.clear();
            return;
        }

        if (MC.world == null || MC.player == null) {
            return;
        }

        for (BulletHole hole : this.holeQueue) {
            if (hole.blockPos != null) {
                if (MC.world.isAirBlock(hole.blockPos) || MC.world.getBlockState(hole.blockPos).getMaterial() == net.minecraft.block.material.Material.AIR) {
                    hole.shouldDie = true;
                }
            }
        }

        this.holeQueue.removeIf(bulletHole -> bulletHole.shouldDie);

        if (this.holeQueue.isEmpty()) {
            return;
        }

        GlStateManager.pushMatrix();

        EntityPlayer player = MC.player;
        double iPosX = player.prevPosX + (player.posX - player.prevPosX) * MC.getRenderPartialTicks();
        double iPosY = player.prevPosY + (player.posY - player.prevPosY) * MC.getRenderPartialTicks();
        double iPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * MC.getRenderPartialTicks();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.disableCull();

         MC.getTextureManager().bindTexture(new ResourceLocation(ID + ":textures/entity/bullethole.png"));

        //GL14.glBlendEquation(GL14.GL_FUNC_ADD);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        final double size = 0.05;
        final double lift = 0.01;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (BulletHole hole : holeQueue) {
            long age = System.currentTimeMillis() - hole.timeExisted;
            long maxAge = 8000;
            
            if (age > maxAge)
                hole.shouldDie = true;

            // Transition from lighter orange to black over the first 4 seconds (4000ms)
            float colorRatio = Math.min(1.0f, (float) age / 4000f);
            float red = 1.0f * (1.0f - colorRatio);
            float green = 0.6f * (1.0f - colorRatio);
            float blue = 0.15f * (1.0f - colorRatio);

            // Fade out over the last 2 seconds (6000ms to 8000ms)
            float alpha = 1.0f;
            if (age > maxAge - 2000) {
                alpha = 1.0f - ((float) (age - (maxAge - 2000)) / 2000f);
                alpha = Math.max(0.0f, alpha);
            }

            double dx = hole.pos.x - iPosX;
            double dy = hole.pos.y - iPosY;
            double dz = hole.pos.z - iPosZ;

            switch (hole.direction) {
                case UP:
                    buffer.pos(dx + size, dy + lift, dz + size).tex(0, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy + lift, dz + size).tex(1, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy + lift, dz - size).tex(1, 1).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + size, dy + lift, dz - size).tex(0, 1).color(red, green, blue, alpha).endVertex();
                    break;
                case DOWN:
                    buffer.pos(dx + size, dy - lift, dz + size).tex(0, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy - lift, dz + size).tex(1, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy - lift, dz - size).tex(1, 1).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + size, dy - lift, dz - size).tex(0, 1).color(red, green, blue, alpha).endVertex();
                    break;
                case EAST:
                    buffer.pos(dx + lift, dy + size, dz + size).tex(0, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + lift, dy - size, dz + size).tex(1, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + lift, dy - size, dz - size).tex(1, 1).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + lift, dy + size, dz - size).tex(0, 1).color(red, green, blue, alpha).endVertex();
                    break;
                case WEST:
                    buffer.pos(dx - lift, dy + size, dz + size).tex(0, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - lift, dy - size, dz + size).tex(1, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - lift, dy - size, dz - size).tex(1, 1).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - lift, dy + size, dz - size).tex(0, 1).color(red, green, blue, alpha).endVertex();
                    break;
                case SOUTH:
                    buffer.pos(dx + size, dy + size, dz + lift).tex(0, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + size, dy - size, dz + lift).tex(1, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy - size, dz + lift).tex(1, 1).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy + size, dz + lift).tex(0, 1).color(red, green, blue, alpha).endVertex();
                    break;
                case NORTH:
                    buffer.pos(dx + size, dy + size, dz - lift).tex(0, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx + size, dy - size, dz - lift).tex(1, 0).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy - size, dz - lift).tex(1, 1).color(red, green, blue, alpha).endVertex();
                    buffer.pos(dx - size, dy + size, dz - lift).tex(0, 1).color(red, green, blue, alpha).endVertex();
                    break;
            }
        }

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();

        GlStateManager.popMatrix();
    }
}
