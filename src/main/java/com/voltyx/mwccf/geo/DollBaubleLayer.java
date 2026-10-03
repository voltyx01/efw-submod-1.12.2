package com.voltyx.mwccf.geo;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import efw.item.ItemDoll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Рендерит 3D-модель куклы на поясе/бедре игрока,
 * если кукла находится в инвентаре, рюкзаке или любом слоте Baubles.
 * При загрязнении кровью текстура куклы также покрывается брызгами.
 */
@SideOnly(Side.CLIENT)
public class DollBaubleLayer implements LayerRenderer<AbstractClientPlayer> {

    private static final ResourceLocation GEO_LOCATION = new ResourceLocation("mwccf", "geo/doll_bauble.geo.json");
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("mwccf", "textures/models/armor/doll_bauble.png");

    private static GeoArmorModel cachedModel;

    public static final float BASE_POS_X = 3.0F;
    public static final float BASE_POS_Y = 11.0F;
    public static final float BASE_POS_Z = 2.0F;

    public static final float BASE_ROT_X = (float) Math.toRadians(-95.26349);
    public static final float BASE_ROT_Y = (float) Math.toRadians(-24.78894);
    public static final float BASE_ROT_Z = (float) Math.toRadians(179.62707);

    private static DollScaleModelRenderer scaleBone2;
    private static DollScaleModelRenderer scaleSlimBone2;

    private final RenderPlayer renderer;
    private final String skinTypeKey;

    public DollBaubleLayer(RenderPlayer renderer, String skinTypeKey) {
        this.renderer = renderer;
        this.skinTypeKey = skinTypeKey;
    }

    public static GeoArmorModel getModel() {
        if (cachedModel == null) {
            cachedModel = new GeoArmorModel(GEO_LOCATION);
            cachedModel.filterBonesBySlot = false;

            ModelRenderer bone2 = cachedModel.getBone("bone2");
            if (bone2 != null) {
                scaleBone2 = new DollScaleModelRenderer(cachedModel, bone2);
                replaceChild(cachedModel.bipedBody, bone2, scaleBone2);
                cachedModel.replaceBone("bone2", scaleBone2);
            }
            ModelRenderer slimBone2 = cachedModel.getSlimBone("bone2");
            if (slimBone2 != null) {
                scaleSlimBone2 = new DollScaleModelRenderer(cachedModel, slimBone2);
                replaceChild(cachedModel.bipedBody, slimBone2, scaleSlimBone2);
                cachedModel.replaceSlimBone("bone2", scaleSlimBone2);
            }
        }
        return cachedModel;
    }

    private static void replaceChild(ModelRenderer parent, ModelRenderer oldChild, ModelRenderer newChild) {
        if (parent == null || oldChild == null || newChild == null) return;
        if (parent.childModels != null) {
            int idx = parent.childModels.indexOf(oldChild);
            if (idx != -1) {
                parent.childModels.set(idx, newChild);
            } else {
                parent.childModels.add(newChild);
            }
        } else {
            parent.addChild(newChild);
        }
    }

    public static boolean isHoldingDoll(EntityPlayer player) {
        return ItemDoll.isHoldingDoll(player);
    }

    public static boolean hasDoll(EntityPlayer player) {
        return ItemDoll.hasDoll(player);
    }

    public static boolean isDollItem(ItemStack stack) {
        return ItemDoll.isDollItem(stack);
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float delta,
                              float age, float yaw, float pitch, float scale) {
        if (efw.util.RenderContext.isRenderingPlayerInSevenScreen || com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            return;
        }

        // В виде от первого лица на себе слой не рисуем (в 1-м лице кукла держится в руках через DollRenderer)
        if (player == Minecraft.getMinecraft().player &&
                Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        if (!hasDoll(player)) return;

        // Если кукла взята в руки — перестаем рендерить её на теле
        if (isHoldingDoll(player)) return;

        String playerSkin = "slim".equals(player.getSkinType()) ? "slim" : "default";
        if (!playerSkin.equals(this.skinTypeKey)) return;

        GeoArmorModel model = getModel();
        if (model == null) return;

        com.voltyx.mwccf.render.doll.DollBodySettings.OffsetData offset =
                com.voltyx.mwccf.render.doll.DollBodySettings.getOffsetForPlayer(player);
        if (offset != null) {
            if (scaleBone2 != null) {
                scaleBone2.rotationPointX = BASE_POS_X + offset.posX;
                scaleBone2.rotationPointY = BASE_POS_Y + offset.posY;
                scaleBone2.rotationPointZ = BASE_POS_Z + offset.posZ;
                scaleBone2.rotateAngleX = BASE_ROT_X + (float) Math.toRadians(offset.rotX);
                scaleBone2.rotateAngleY = BASE_ROT_Y + (float) Math.toRadians(offset.rotY);
                scaleBone2.rotateAngleZ = BASE_ROT_Z + (float) Math.toRadians(offset.rotZ);
                scaleBone2.dollScale = offset.scale;
            }
            if (scaleSlimBone2 != null) {
                scaleSlimBone2.rotationPointX = BASE_POS_X + offset.posX;
                scaleSlimBone2.rotationPointY = BASE_POS_Y + offset.posY;
                scaleSlimBone2.rotationPointZ = BASE_POS_Z + offset.posZ;
                scaleSlimBone2.rotateAngleX = BASE_ROT_X + (float) Math.toRadians(offset.rotX);
                scaleSlimBone2.rotateAngleY = BASE_ROT_Y + (float) Math.toRadians(offset.rotY);
                scaleSlimBone2.rotateAngleZ = BASE_ROT_Z + (float) Math.toRadians(offset.rotZ);
                scaleSlimBone2.dollScale = offset.scale;
            }
        }

        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(player, limbSwing, limbSwingAmount, delta);
        model.currentSlot = EntityEquipmentSlot.CHEST;

        this.renderer.bindTexture(TEXTURE_LOCATION);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();

        model.syncedModel = this.renderer.getMainModel();

        // Рендерим GeoArmorModel (кукла привязана к bipedBody / поясу)
        model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);

        GlStateManager.disableCull();
        model.syncedModel = null;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public static class DollScaleModelRenderer extends ModelRenderer {
        public float dollScale = 1.0F;

        public DollScaleModelRenderer(net.minecraft.client.model.ModelBase model, ModelRenderer original) {
            super(model);
            this.rotationPointX = original.rotationPointX;
            this.rotationPointY = original.rotationPointY;
            this.rotationPointZ = original.rotationPointZ;
            this.rotateAngleX = original.rotateAngleX;
            this.rotateAngleY = original.rotateAngleY;
            this.rotateAngleZ = original.rotateAngleZ;
            this.mirror = original.mirror;
            this.showModel = original.showModel;
            this.isHidden = original.isHidden;
            this.childModels = original.childModels;
        }

        @Override
        public void render(float scale) {
            if (!this.isHidden && this.showModel) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }
                if (this.dollScale != 1.0F) {
                    GlStateManager.scale(this.dollScale, this.dollScale, this.dollScale);
                }

                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); ++i) {
                        this.childModels.get(i).render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
}
