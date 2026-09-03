package com.voltyx.mwccf.furniture.client.renderer;

import com.voltyx.mwccf.furniture.tileentity.TileEntityFridge;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityFridgeRenderer extends TileEntitySpecialRenderer<TileEntityFridge> {

    @Override
    public void render(TileEntityFridge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld()) return;

        float angle = te.prevDoorAngle + (te.doorAngle - te.prevDoorAngle) * partialTicks;
        if (angle <= 0.0F) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        int meta = te.getBlockMetadata();
        EnumFacing facing = EnumFacing.byIndex(meta & 7);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        switch (facing) {
            case NORTH: GlStateManager.rotate(0, 0, 1, 0); break;
            case SOUTH: GlStateManager.rotate(180, 0, 1, 0); break;
            case WEST: GlStateManager.rotate(90, 0, 1, 0); break;
            case EAST: GlStateManager.rotate(270, 0, 1, 0); break;
        }

        // Smooth door rotation effect
        GlStateManager.translate(-0.4F, 0.0F, 0.4F);
        GlStateManager.rotate(-angle * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.4F, 0.0F, -0.4F);

        GlStateManager.popMatrix();
    }
}
