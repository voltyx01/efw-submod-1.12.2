package com.voltyx.mwccf.furniture.client.renderer;

import com.voltyx.mwccf.client.inspect.Item3DRenderer;
import com.voltyx.mwccf.furniture.BlockPlacedItem;
import com.voltyx.mwccf.furniture.tileentity.TileEntityPlacedItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class TileEntityPlacedItemRenderer extends TileEntitySpecialRenderer<TileEntityPlacedItem> {

    private static final ResourceLocation INSPECT_ICON = new ResourceLocation("mwccf", "textures/items/buildingscantool.png");

    @SubscribeEvent
    public static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.getTarget() != null && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = event.getTarget().getBlockPos();
            if (event.getPlayer() != null && event.getPlayer().world != null) {
                if (event.getPlayer().world.getBlockState(pos).getBlock() instanceof BlockPlacedItem) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void render(TileEntityPlacedItem te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld()) {
            return;
        }

        ItemStack stack = te.getStack();
        if (stack.isEmpty()) {
            return;
        }

        // 1. Render placed 3D item model
        GlStateManager.pushMatrix();

        // Position offset
        GlStateManager.translate((float) x + te.getOffsetX(), (float) y + te.getOffsetY(), (float) z + te.getOffsetZ());

        // Rotations (Yaw around Y, Pitch around X, Roll around Z)
        GlStateManager.rotate(te.getRotationYaw(), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(te.getRotationPitch(), 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(te.getRotationRoll(), 0.0F, 0.0F, 1.0F);

        // User scale
        float scale = te.getScale();
        if (scale != 1.0F) {
            GlStateManager.scale(scale, scale, scale);
        }

        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Lightmap coords from block position
        int light = te.getWorld().getCombinedLight(te.getPos(), 0);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) (light % 65536), (float) (light / 65536));

        // Enable two-sided lighting and disable culling for thin geometry / attachments
        GlStateManager.disableCull();
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);

        // Render full 3D model (Armor, MWC weapons with attachments/skins, baubles, or generic items)
        Item3DRenderer.renderPlacedInWorld(stack, Minecraft.getMinecraft());

        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_FALSE);
        GlStateManager.enableCull();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();

        GlStateManager.popMatrix();

        // 2. Render floating inspect / magnifying glass icon above the placed item
        renderInspectIcon(te, x, y, z, partialTicks);
    }

    private void renderInspectIcon(TileEntityPlacedItem te, double x, double y, double z, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity viewEntity = mc.getRenderViewEntity();
        if (viewEntity == null) {
            viewEntity = mc.player;
        }
        if (viewEntity == null || te.getWorld() == null) {
            return;
        }

        // Exact world position of the placed item
        double worldItemX = te.getPos().getX() + te.getOffsetX();
        double worldItemY = te.getPos().getY() + te.getOffsetY();
        double worldItemZ = te.getPos().getZ() + te.getOffsetZ();

        // Eye position
        Vec3d eyes = viewEntity.getPositionEyes(partialTicks);
        double dx = worldItemX - eyes.x;
        double dy = worldItemY - eyes.y;
        double dz = worldItemZ - eyes.z;
        double distSq = dx * dx + dy * dy + dz * dz;

        // Check if player is aiming directly at this placed item block
        RayTraceResult mop = mc.objectMouseOver;
        boolean isHovered = (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK && te.getPos().equals(mop.getBlockPos()));

        // Max distance for proximity is ~3.5 blocks
        double maxDist = 3.5D;
        if (!isHovered && distSq > maxDist * maxDist) {
            return;
        }

        // Line-of-sight check to ensure icon is not visible through solid terrain/walls
        if (!isHovered) {
            Vec3d targetVec = new Vec3d(worldItemX, worldItemY + 0.15D, worldItemZ);
            RayTraceResult losTrace = te.getWorld().rayTraceBlocks(eyes, targetVec, false, true, false);
            if (losTrace != null && losTrace.typeOfHit == RayTraceResult.Type.BLOCK && !losTrace.getBlockPos().equals(te.getPos())) {
                return;
            }
        }

        // Transparency:
        // Hovered = 100% brightness & 100% opacity
        // Proximity (<= 3 blocks) = semi-transparent (fades smoothly from 0.45 to 0.0 at edge)
        float alpha;
        if (isHovered) {
            alpha = 1.0F;
        } else {
            double dist = Math.sqrt(distSq);
            float factor = 1.0F;
            if (dist > 2.0D) {
                factor = (float) ((maxDist - dist) / (maxDist - 2.0D));
                if (factor < 0.0F) factor = 0.0F;
                if (factor > 1.0F) factor = 1.0F;
            }
            alpha = 0.45F * factor;
            if (alpha < 0.02F) {
                return;
            }
        }

        // Position icon above the item:
        // Default item offset is ~0.05, height is ~0.2, so 0.32 above puts it right above
        float scale = te.getScale();
        double iconOffsetY = te.getOffsetY() + 0.32D + Math.max(0.0D, (scale - 1.0F) * 0.15D) + te.getIconOffsetY();

        double iconX = x + te.getOffsetX() + te.getIconOffsetX();
        double iconY = y + iconOffsetY;
        double iconZ = z + te.getOffsetZ() + te.getIconOffsetZ();

        GlStateManager.pushMatrix();
        GlStateManager.translate(iconX, iconY, iconZ);

        // Billboard rotation facing camera (with ShoulderSurfing support)
        float yaw = mc.getRenderManager().playerViewY;
        float pitch = mc.getRenderManager().playerViewX;

        if (com.teamderpy.shouldersurfing.client.ShoulderInstance.getInstance().doShoulderSurfing()) {
            yaw = com.teamderpy.shouldersurfing.client.ShoulderRenderer.getInstance().cameraYaw - 180.0F;
            pitch = com.teamderpy.shouldersurfing.client.ShoulderRenderer.getInstance().cameraPitch;
        } else if (mc.gameSettings.thirdPersonView == 2) {
            pitch = -pitch;
        }

        GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);

        float iconScale = isHovered ? -0.018F : -0.015F;
        GlStateManager.scale(iconScale, iconScale, iconScale);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);

        // DO NOT LET THE PLACED ITEM OCCLUDE THE ICON:
        // Disable depth testing so the icon is drawn over the placed item
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        if (isHovered) {
            // 100% brightness
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            // Proximity brightness
            int light = te.getWorld().getCombinedLight(te.getPos().up(), 0);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) (light % 65536), (float) (light / 65536));
            GlStateManager.color(0.85F, 0.85F, 0.85F, alpha);
        }

        mc.getTextureManager().bindTexture(INSPECT_ICON);
        Gui.drawModalRectWithCustomSizedTexture(-8, -8, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.popMatrix();
    }
}
