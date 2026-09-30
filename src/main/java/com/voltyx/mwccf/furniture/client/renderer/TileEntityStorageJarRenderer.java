package com.voltyx.mwccf.furniture.client.renderer;

import com.voltyx.mwccf.furniture.tileentity.TileEntityStorageJar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityStorageJarRenderer extends TileEntitySpecialRenderer<TileEntityStorageJar> {

    @Override
    public void render(TileEntityStorageJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || te.isEmpty()) return;

        ItemStack stack = te.getItem();
        if (stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.22D, z + 0.5D);
        GlStateManager.scale(0.42F, 0.42F, 0.42F);

        float angle = 0.0F;
        if (te.getWorld() != null) {
            net.minecraft.block.state.IBlockState state = te.getWorld().getBlockState(te.getPos());
            if (state.getPropertyKeys().contains(com.voltyx.mwccf.furniture.BlockFurnitureHorizontal.FACING)) {
                net.minecraft.util.EnumFacing facing = state.getValue(com.voltyx.mwccf.furniture.BlockFurnitureHorizontal.FACING);
                switch (facing) {
                    case NORTH: angle = 0.0F; break;
                    case SOUTH: angle = 180.0F; break;
                    case WEST: angle = 90.0F; break;
                    case EAST: angle = 270.0F; break;
                    default: break;
                }
            }
        }
        GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);

        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
    }
}
