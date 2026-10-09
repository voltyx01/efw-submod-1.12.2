package com.voltyx.mwccf.fireweapon.client;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Deprecated overlay renderer. Replaced by FireWeaponTextureManager which composites
 * cloth and burning/charring directly into the weapon's texture in UV space.
 */
@SideOnly(Side.CLIENT)
public class FireWeaponItemRenderer {

    @Deprecated
    public static void renderClothOnItem(ItemStack stack, IBakedModel model) {
        // No-op: 3D overlay quads removed. FireWeaponTextureManager handles in-texture compositing.
    }
}
