package com.voltyx.mwccf.furniture.client.renderer;

import com.voltyx.mwccf.furniture.tileentity.TileEntityMicrowave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityMicrowaveRenderer extends TileEntitySpecialRenderer<TileEntityMicrowave> {

    @Override
    public void render(TileEntityMicrowave te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || !te.hasWorld()) return;

        ItemStack stack = te.getStackInSlot(0);
        if (stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.25F, (float) z + 0.5F);

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

        // Rotate food inside while microwave is cooking
        if (te.cookTime > 0) {
            float rotation = (te.getWorld().getTotalWorldTime() + partialTicks) * 10.0F;
            GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        }

        GlStateManager.scale(0.35F, 0.35F, 0.35F);
        Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.FIXED);

        GlStateManager.popMatrix();
    }
}
