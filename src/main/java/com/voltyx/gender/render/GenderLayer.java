package com.voltyx.gender.render;

import com.voltyx.gender.api.IGenderArmor;
import com.voltyx.gender.main.Breasts;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import com.voltyx.gender.main.WildfireHelper;
import com.voltyx.gender.physics.BreastPhysics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class GenderLayer implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer playerRenderer;
    private WildfireModelRenderer lBreast, rBreast;
    private WildfireModelRenderer lBreastWear, rBreastWear;
    private WildfireModelRenderer lBoobArmor, rBoobArmor;

    private float preBreastSize = 0f;
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = new HashMap<>();

    public GenderLayer(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
        initModels();
    }

    private void initModels() {
        lBreast = new WildfireModelRenderer(64, 64, 16, 17, -4F, 0.0F, 0F, 4, 5, 4, 0.0F, false);
        rBreast = new WildfireModelRenderer(64, 64, 20, 17, 0F, 0.0F, 0F, 4, 5, 4, 0.0F, false);

        lBreastWear = new WildfireModelRenderer(true, 64, 64, 17, 34, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
        rBreastWear = new WildfireModelRenderer(false, 64, 64, 21, 34, 0F, 0.0F, 0F, 4, 5, 3, 0.0F, false);

        lBoobArmor = new WildfireModelRenderer(64, 32, 16, 17, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
        rBoobArmor = new WildfireModelRenderer(64, 32, 20, 17, 0F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
    }

    private ResourceLocation getArmorResource(AbstractClientPlayer entity, ItemStack stack, EntityEquipmentSlot slot,
            String type) {
        ItemArmor item = (ItemArmor) stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
                (slot == EntityEquipmentSlot.LEGS ? 2 : 1),
                type == null ? "" : String.format(Locale.ROOT, "_%s", type));

        s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }
        return resourcelocation;
    }

    @Override
    public void doRenderLayer(@Nonnull AbstractClientPlayer ent, float limbAngle, float limbDistance,
            float partialTicks, float animationProgress, float headYaw, float headPitch, float scale) {
        if (ent.isInvisibleToPlayer(Minecraft.getMinecraft().player))
            return;

        try {
            UUID playerUUID = ent.getUniqueID();
            GenderPlayer plr = WildfireGender.getPlayerById(playerUUID);
            if (plr == null)
                return;

            ItemStack armorStack = ent.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            IGenderArmor genderArmor = WildfireHelper.getArmorConfig(armorStack);
            boolean isChestplateOccupied = genderArmor.coversBreasts();

            boolean hideBreastsCompletely = genderArmor.alwaysHidesBreasts()
                    || (!plr.showBreastsInArmor() && isChestplateOccupied);
            if (!armorStack.isEmpty() && armorStack.getItem().getRegistryName() != null) {
                String armorId = armorStack.getItem().getRegistryName().toString();
                if (armorId.equals("mwccf:hazmat_chestplate") ||
                        armorId.equals("mwccf:exo_heavy_desert_chestplate") ||
                        armorId.equals("mwccf:desert_juggernaut_chestplate") ||
                        armorId.equals("mwccf:exo_heavy_black_chestplate") ||
                        armorId.equals("mwccf:guillie_chestplate") ||
                        armorId.equals("mwccf:artic_guillie_chestplate") ||
                        armorId.equals("mwccf:black_juggernaut_chestplate") ||
                        armorId.equals("mwccf:fire_fighter_chestplate") ||
                        armorId.equals("mwccf:exo_heavy_green_chestplate") ||
                        armorId.equals("mwccf:green_juggernaut_chestplate") ||
                        armorId.equals("mwccf:spruce_guillie_chestplate")) {
                    hideBreastsCompletely = true;
                }
            }
            if (hideBreastsCompletely)
                return;

            ModelPlayer model = this.playerRenderer.getMainModel();
            Breasts breasts = plr.getBreasts();

            float breastOffsetX = Math.round((Math.round(breasts.getXOffset() * 100f) / 100f) * 10) / 10f;
            float breastOffsetY = -Math.round((Math.round(breasts.getYOffset() * 100f) / 100f) * 10) / 10f;
            float breastOffsetZ = -Math.round((Math.round(breasts.getZOffset() * 100f) / 100f) * 10) / 10f;

            BreastPhysics leftBreastPhysics = plr.getLeftBreastPhysics();
            final float bSize = leftBreastPhysics.getBreastSize(partialTicks);
            float outwardAngle = (Math.round(breasts.getCleavage() * 100f) / 100f) * 100f;
            outwardAngle = Math.min(outwardAngle, 10);

            float reducer = 0;
            if (bSize < 0.84f)
                reducer++;
            if (bSize < 0.72f)
                reducer++;

            if (preBreastSize != bSize) {
                int breastDepth = (int) (4 - breastOffsetZ - reducer);
                lBreast = new WildfireModelRenderer(64, 64, 16, 17, -4F, 0.0F, 0F, 4, 5, breastDepth, 0.0F, false);
                rBreast = new WildfireModelRenderer(64, 64, 20, 17, 0F, 0.0F, 0F, 4, 5, breastDepth, 0.0F, false);
                preBreastSize = bSize;
            }

            float lTotal = lerp(partialTicks, leftBreastPhysics.getPreBounceY(), leftBreastPhysics.getBounceY());
            float lTotalX = lerp(partialTicks, leftBreastPhysics.getPreBounceX(), leftBreastPhysics.getBounceX());
            float leftBounceRotation = lerp(partialTicks, leftBreastPhysics.getPreBounceRotation(),
                    leftBreastPhysics.getBounceRotation());
            float rTotal, rTotalX, rightBounceRotation;

            if (breasts.isUniboob()) {
                rTotal = lTotal;
                rTotalX = lTotalX;
                rightBounceRotation = leftBounceRotation;
            } else {
                BreastPhysics rightBreastPhysics = plr.getRightBreastPhysics();
                rTotal = lerp(partialTicks, rightBreastPhysics.getPreBounceY(), rightBreastPhysics.getBounceY());
                rTotalX = lerp(partialTicks, rightBreastPhysics.getPreBounceX(), rightBreastPhysics.getBounceX());
                rightBounceRotation = lerp(partialTicks, rightBreastPhysics.getPreBounceRotation(),
                        rightBreastPhysics.getBounceRotation());
            }

            float breastSize = bSize * 1.5f;
            if (breastSize > 0.7f)
                breastSize = 0.7f;
            if (bSize > 0.7f)
                breastSize = bSize;
            if (breastSize < 0.02f)
                return;

            float zOff = 0.0625f - (bSize * 0.0625f);
            breastSize = bSize + 0.5f * Math.abs(bSize - 0.7f) * 2f;

            float resistance = MathHelper.clamp(genderArmor.physicsResistance(), 0, 1);
            boolean breathingAnimation = resistance <= 0.5F
                    && !ent.isInsideOfMaterial(net.minecraft.block.material.Material.WATER);
            boolean bounceEnabled = plr.hasBreastPhysics()
                    && (!isChestplateOccupied || (plr.hasArmorBreastPhysics() && resistance < 1));

            // Рендер левой и правой груди
            renderBreastWithTransforms(ent, model.bipedBody, armorStack, partialTicks, bounceEnabled, lTotalX, lTotal,
                    leftBounceRotation, breastSize, breastOffsetX, breastOffsetY, breastOffsetZ, zOff, outwardAngle,
                    breasts.isUniboob(), isChestplateOccupied, breathingAnimation, true, scale);
            renderBreastWithTransforms(ent, model.bipedBody, armorStack, partialTicks, bounceEnabled, rTotalX, rTotal,
                    rightBounceRotation, breastSize, -breastOffsetX, breastOffsetY, breastOffsetZ, zOff, -outwardAngle,
                    breasts.isUniboob(), isChestplateOccupied, breathingAnimation, false, scale);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderBreastWithTransforms(AbstractClientPlayer entity, net.minecraft.client.model.ModelRenderer body,
            ItemStack armorStack, float partialTicks, boolean bounceEnabled, float totalX, float total,
            float bounceRotation, float breastSize, float breastOffsetX, float breastOffsetY, float breastOffsetZ,
            float zOff, float outwardAngle, boolean uniboob, boolean isChestplateOccupied, boolean breathingAnimation,
            boolean left, float scale) {
        GlStateManager.pushMatrix();
        // Временно добавь в начало renderBreastWithTransforms:

        try {
            // Точно как в 1.18.2 — берём позицию и углы body вручную

            GlStateManager.translate(body.rotationPointX * scale, body.rotationPointY * scale,
                    body.rotationPointZ * scale);
            if (body.rotateAngleZ != 0)
                GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleZ), 0, 0, 1);
            if (body.rotateAngleY != 0)
                GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleY), 0, 1, 0);
            if (body.rotateAngleX != 0)
                GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleX), 1, 0, 0);

            // Добавь это — компенсация сдвига тела при приседании
            if (entity.isSneaking()) {
                GlStateManager.translate(0, 0.2f, 0);
                GlStateManager.translate(0, 0, -0.1);
            }

            if (bounceEnabled) {
                GlStateManager.translate(totalX / 32f, total / 32f, 0);
            }

            GlStateManager.translate(
                    breastOffsetX * 0.0625f,
                    0.05625f + (breastOffsetY * 0.0625f),
                    zOff - 0.0625f * 2f + (breastOffsetZ * 0.0625f) - 0.01f);
            if (!uniboob) {
                GlStateManager.translate(-0.0625f * 2 * (left ? 1 : -1), 0, 0);
            }
            if (bounceEnabled) {
                GlStateManager.rotate((float) Math.toDegrees(bounceRotation), 0, 1, 0);
            }
            if (!uniboob) {
                GlStateManager.translate(0.0625f * 2 * (left ? 1 : -1), 0, 0);
            }

            float rotationMultiplier = bounceEnabled ? -total / 12f : 0;
            if (bounceEnabled) {
                GlStateManager.translate(0, -0.035f * breastSize, 0);
            }
            float totalRotation = Math.min(breastSize + rotationMultiplier, 1);

            if (isChestplateOccupied) {
                GlStateManager.translate(0, 0, 0.01f);
            }

            GlStateManager.rotate(outwardAngle, 0, 1, 0);
            GlStateManager.rotate(-35f * totalRotation, 1, 0, 0);

            GlStateManager.scale(0.9995f, 1f, 1f);

            renderBreast(entity, armorStack, scale, left);
        } catch (Exception e) {
            System.out.println("GENDER MOD NPE:");
            e.printStackTrace(System.out);
        }

        GlStateManager.popMatrix();
    }

    private void renderBreast(AbstractClientPlayer entity, ItemStack armorStack, float scale, boolean left) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(entity.getLocationSkin());

        WildfireModelRenderer breastModel = left ? lBreast : rBreast;
        breastModel.render(scale);

        if (entity.isWearing(EnumPlayerModelParts.JACKET)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, -0.015f);
            GlStateManager.scale(1.05f, 1.05f, 1.05f);
            WildfireModelRenderer breastWearModel = left ? lBreastWear : rBreastWear;
            breastWearModel.render(scale);
            GlStateManager.popMatrix();
        }

        if (!armorStack.isEmpty() && armorStack.getItem() instanceof ItemArmor) {
            boolean skipBreastArmor = false;
            if (armorStack.getItem().getRegistryName() != null) {
                String armorId = armorStack.getItem().getRegistryName().toString();
                if (armorId.equals("mwccf:police_chestplate") ||
                        armorId.equals("mwccf:green_rockie_armor_chestplate") ||
                        armorId.equals("mwccf:desert_rockie_armor_chestplate") ||
                        armorId.equals("mwccf:black_rockie_armor_chestplate")) {
                    skipBreastArmor = true;
                }
            }
            if (skipBreastArmor)
                return;

            ItemArmor armorItem = (ItemArmor) armorStack.getItem();
            ResourceLocation armorTexture = getArmorResource(entity, armorStack, EntityEquipmentSlot.CHEST, null);

            Minecraft.getMinecraft().getTextureManager().bindTexture(armorTexture);
            GlStateManager.pushMatrix();
            try {
                GlStateManager.translate(left ? 0.001f : -0.001f, 0.015f, -0.015f);
                GlStateManager.scale(1.05f, 1, 1);

                if (armorItem.hasColor(armorStack)) {
                    int color = armorItem.getColor(armorStack);
                    float r = (float) (color >> 16 & 255) / 255.0F;
                    float g = (float) (color >> 8 & 255) / 255.0F;
                    float b = (float) (color & 255) / 255.0F;
                    GlStateManager.color(r, g, b, 1.0F);
                }

                if (armorItem instanceof com.voltyx.mwccf.geo.ItemGeoArmor) {
                    com.voltyx.mwccf.geo.ItemGeoArmor geoItem = (com.voltyx.mwccf.geo.ItemGeoArmor) armorItem;
                    com.voltyx.mwccf.geo.GeoArmorModel geoModel = (com.voltyx.mwccf.geo.GeoArmorModel) geoItem
                            .getArmorModel(entity, armorStack, EntityEquipmentSlot.CHEST, null);
                    if (geoModel != null) {
                        WildfireModelRenderer geoArmorModel = (WildfireModelRenderer) (left ? geoModel.leftBoob
                                : geoModel.rightBoob);
                        if (geoArmorModel != null) {
                            geoArmorModel.render(scale);
                        }
                    }
                } else {
                    WildfireModelRenderer armorModel = left ? lBoobArmor : rBoobArmor;
                    armorModel.render(scale);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); // Сбрасываем цвет
            } finally {
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private float lerp(float pct, float start, float end) {
        return start + pct * (end - start);
    }
}