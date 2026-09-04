package com.voltyx.mwccf.backpack;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import com.voltyx.mwccf.mcore.ItemCustomArmor;

public final class BackpackLayer implements LayerRenderer<AbstractClientPlayer> {
    private static final ResourceLocation WORN_TEXTURE = new ResourceLocation("quark", "textures/misc/backpack_worn.png");
    private static final ResourceLocation WORN_OVERLAY_TEXTURE = new ResourceLocation("quark", "textures/misc/backpack_worn_overlay.png");
    private final AssaultBackpack model = new AssaultBackpack();
    private final RenderPlayer renderer;

    public BackpackLayer(RenderPlayer renderer) {
        this.renderer = renderer;
        this.model.setVisible(false);
        this.model.bipedBody.showModel = true;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float delta, float age, float yaw, float pitch, float scale) {
        ItemStack stack = BackpackBaubles.getBaubleBackpack(player);
        if (!stack.isEmpty()) {
            boolean hasSurvivalInstinctChest = false;
            ItemStack armorStack = player.getItemStackFromSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST);
            if (!armorStack.isEmpty() && armorStack.getItem() instanceof ItemCustomArmor) {
                if (!"mwccf:fire_fighter_chestplate".equals(armorStack.getItem().getRegistryName().toString())) {
                    hasSurvivalInstinctChest = true;
                }
            }

            if (!hasSurvivalInstinctChest) {
                this.renderBackpack(stack, player, limbSwing, limbSwingAmount, delta, age, yaw, pitch, scale);
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderBackpack(ItemStack stack, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float delta, float age, float yaw, float pitch, float scale) {
        this.model.setModelAttributes(this.renderer.getMainModel());
        this.model.setLivingAnimations(player, limbSwing, limbSwingAmount, delta);
        int color = ((ItemArmor)stack.getItem()).getColor(stack);
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        this.renderer.bindTexture(WORN_TEXTURE);
        GlStateManager.color(red, green, blue, 1.0f);
        this.model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
        this.renderer.bindTexture(WORN_OVERLAY_TEXTURE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
    }
}

