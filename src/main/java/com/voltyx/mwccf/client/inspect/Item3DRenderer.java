package com.voltyx.mwccf.client.inspect;

import com.voltyx.mwccf.backpack.AssaultBackpack;
import com.voltyx.mwccf.geo.BraceletInspectHandler;
import com.voltyx.mwccf.geo.GeoArmorModel;
import com.voltyx.mwccf.geo.HeadlampRenderer;
import com.voltyx.mwccf.geo.ItemBracelet;
import com.voltyx.mwccf.geo.ItemGeoArmor;
import com.voltyx.mwccf.geo.ItemHeadlamp;
import com.voltyx.mwccf.mcore.ItemCustomArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class Item3DRenderer {

    private static final AssaultBackpack BACKPACK_MODEL = new AssaultBackpack();
    private static final ResourceLocation BACKPACK_TEX = new ResourceLocation("quark", "textures/misc/backpack_worn.png");
    private static final ResourceLocation BACKPACK_OVERLAY_TEX = new ResourceLocation("quark", "textures/misc/backpack_worn_overlay.png");

    public static void renderConfigured3D(ItemStack stack, ItemInspectConfig.GroupTransform cfg, Minecraft mc) {
        if (stack == null || stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderHelper.enableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc);
        int scaleFactor = Math.max(1, sr.getScaleFactor());
        float guiScaleRatio = 2.0f / (float) scaleFactor;

        float baseScale = 90.0f * cfg.scale * ItemInspectConfig.getGlobalInspectScale() * guiScaleRatio;

        GlStateManager.scale(baseScale, -baseScale, baseScale);
        GlStateManager.rotate(cfg.startPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(cfg.startYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(cfg.startRoll, 0.0F, 0.0F, 1.0F);

        GlStateManager.translate(cfg.pivotX, cfg.pivotY, cfg.pivotZ);

        renderRawItem(stack, mc);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    public static void render3D(ItemStack stack, float yaw, float pitch, float scale, Minecraft mc) {
        if (stack == null || stack.isEmpty()) return;

        ItemInspectConfig.InspectGroup group = ItemInspectConfig.resolveGroup(stack);
        ItemInspectConfig.GroupTransform cfg = ItemInspectConfig.getTransform(group);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderHelper.enableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc);
        int scaleFactor = Math.max(1, sr.getScaleFactor());
        // Standard reference is GUI scale 2/3 (reference scaleFactor = 2) so model physical size on screen is constant
        float guiScaleRatio = 2.0f / (float) scaleFactor;

        float finalScale = scale * cfg.scale * ItemInspectConfig.getGlobalInspectScale() * guiScaleRatio;

        GlStateManager.scale(finalScale, -finalScale, finalScale);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(cfg.startRoll, 0.0F, 0.0F, 1.0F);

        // Apply calibrated group pivot offset
        GlStateManager.translate(cfg.pivotX, cfg.pivotY, cfg.pivotZ);

        renderRawItem(stack, mc);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private static void renderRawItem(ItemStack stack, Minecraft mc) {
        String regName = stack.getItem().getRegistryName() != null ? stack.getItem().getRegistryName().toString() : "";

        // 1. GeoModel Armor (ItemGeoArmor)
        if (stack.getItem() instanceof ItemGeoArmor) {
            renderGeoArmor((ItemGeoArmor) stack.getItem(), stack, mc);
        }
        // 2. CustomArmor (Survival Instinct ModelBiped)
        else if (stack.getItem() instanceof ItemCustomArmor) {
            renderCustomArmor((ItemCustomArmor) stack.getItem(), stack, mc);
        }
        // 3. Headlamp Bauble
        else if (stack.getItem() instanceof ItemHeadlamp) {
            renderHeadlamp(mc);
        }
        // 4. Bracelet Bauble
        else if (stack.getItem() instanceof ItemBracelet) {
            renderBracelet(mc);
        }
        // 5. Backpack
        else if (isBackpack(stack)) {
            renderBackpack(stack, mc);
        }
        // 6. Generic Armor (Vanilla or standard ItemArmor)
        else if (stack.getItem() instanceof ItemArmor) {
            renderGenericArmor((ItemArmor) stack.getItem(), stack, mc);
        }
        // 7. MWC Weapon
        else if (isWeapon(stack)) {
            renderWeapon(stack, mc);
        }
        // 8. Standard Items / 2D Tools / Blocks in 3D
        else {
            renderGenericItem(stack, mc);
        }
    }

    private static boolean isBackpack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        String regName = stack.getItem().getRegistryName() != null ? stack.getItem().getRegistryName().toString() : "";
        return regName.equals("quark:backpack") || regName.contains("backpack");
    }

    private static boolean isWeapon(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem() == null) return false;
        String cls = stack.getItem().getClass().getName();
        return stack.getItem() instanceof com.paneedah.weaponlib.Weapon || cls.contains("weaponlib") || cls.contains("Weapon");
    }

    private static void renderGeoArmor(ItemGeoArmor geoArmor, ItemStack stack, Minecraft mc) {
        EntityEquipmentSlot slot = geoArmor.armorType;
        net.minecraft.entity.EntityLivingBase renderEntity = null;
        ModelBiped armorModel = geoArmor.getArmorModel(renderEntity, stack, slot, null);
        if (armorModel instanceof GeoArmorModel) {
            GeoArmorModel model = (GeoArmorModel) armorModel;
            model.isSneak = false;
            model.isRiding = false;
            model.isChild = false;
            model.rightArmPose = ModelBiped.ArmPose.EMPTY;
            model.leftArmPose = ModelBiped.ArmPose.EMPTY;
            model.currentSlot = slot;

            GlStateManager.pushMatrix();
            adjustArmorSlotOffset(slot);
            String tex = geoArmor.getArmorTexture(stack, renderEntity, slot, null);
            if (tex != null) mc.getTextureManager().bindTexture(new ResourceLocation(tex));
            
            float scale = 0.0625F;
            if (slot == EntityEquipmentSlot.HEAD) {
                if (model.bipedHead != null) model.bipedHead.render(scale);
                if (model.bipedHeadwear != null) model.bipedHeadwear.render(scale);
            } else if (slot == EntityEquipmentSlot.CHEST) {
                if (model.bipedBody != null) model.bipedBody.render(scale);
                if (model.bipedRightArm != null) model.bipedRightArm.render(scale);
                if (model.bipedLeftArm != null) model.bipedLeftArm.render(scale);
            } else if (slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET) {
                if (model.bipedRightLeg != null) model.bipedRightLeg.render(scale);
                if (model.bipedLeftLeg != null) model.bipedLeftLeg.render(scale);
            }
            GlStateManager.popMatrix();
        }
    }

    private static void renderCustomArmor(ItemCustomArmor customArmor, ItemStack stack, Minecraft mc) {
        EntityEquipmentSlot slot = customArmor.armorType;
        ModelBiped model = customArmor.getArmorModel(null, stack, slot, null);
        if (model != null) {
            GlStateManager.pushMatrix();
            adjustArmorSlotOffset(slot);
            String tex = customArmor.getArmorTexture(stack, null, slot, null);
            if (tex != null) mc.getTextureManager().bindTexture(new ResourceLocation(tex));
            
            // Render the specific slot parts directly so setRotationAngles / whole biped aren't triggered
            float scale = 0.0625F;
            if (slot == EntityEquipmentSlot.HEAD) {
                if (model.bipedHead != null) model.bipedHead.render(scale);
                if (model.bipedHeadwear != null) model.bipedHeadwear.render(scale);
            } else if (slot == EntityEquipmentSlot.CHEST) {
                if (model.bipedBody != null) model.bipedBody.render(scale);
                if (model.bipedRightArm != null) model.bipedRightArm.render(scale);
                if (model.bipedLeftArm != null) model.bipedLeftArm.render(scale);
            } else if (slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET) {
                if (model.bipedRightLeg != null) model.bipedRightLeg.render(scale);
                if (model.bipedLeftLeg != null) model.bipedLeftLeg.render(scale);
            }
            GlStateManager.popMatrix();
        }
    }

    private static void renderHeadlamp(Minecraft mc) {
        GeoArmorModel model = HeadlampRenderer.getHeadlampModel();
        if (model != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 1.5F, 0.0F); // Center headlamp
            model.currentSlot = EntityEquipmentSlot.HEAD;

            mc.getTextureManager().bindTexture(HeadlampRenderer.getHeadlampTexture());
            if (model.bipedHead != null) {
                model.bipedHead.render(0.0625F);
            }
            GlStateManager.popMatrix();
        }
    }

    private static void renderBracelet(Minecraft mc) {
        GeoArmorModel model = BraceletInspectHandler.getNormalModel();
        if (model != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.375F, 1.05F, 0.0F); // Pivot align

            mc.getTextureManager().bindTexture(BraceletInspectHandler.getBraceletTexture());
            if (model.bipedLeftArm != null) {
                model.bipedLeftArm.render(0.0625F);
            }
            GlStateManager.popMatrix();
        }
    }

    private static void renderBackpack(ItemStack stack, Minecraft mc) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.35F, -0.35F);

        int color = ((ItemArmor) stack.getItem()).getColor(stack);
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;

        mc.getTextureManager().bindTexture(BACKPACK_TEX);
        GlStateManager.color(r, g, b, 1.0F);
        BACKPACK_MODEL.bipedBody.render(0.0625F);

        mc.getTextureManager().bindTexture(BACKPACK_OVERLAY_TEX);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        BACKPACK_MODEL.bipedBody.render(0.0625F);
        GlStateManager.popMatrix();
    }

    private static void renderGenericArmor(ItemArmor armor, ItemStack stack, Minecraft mc) {
        EntityEquipmentSlot slot = armor.armorType;
        ModelBiped model = ForgeHooksClient.getArmorModel(null, stack, slot, null);
        if (model == null) {
            model = new ModelBiped(slot == EntityEquipmentSlot.LEGS ? 0.5F : 1.0F);
        }

        model.bipedHead.showModel = slot == EntityEquipmentSlot.HEAD;
        model.bipedHeadwear.showModel = slot == EntityEquipmentSlot.HEAD;
        model.bipedBody.showModel = slot == EntityEquipmentSlot.CHEST;
        model.bipedRightArm.showModel = slot == EntityEquipmentSlot.CHEST;
        model.bipedLeftArm.showModel = slot == EntityEquipmentSlot.CHEST;
        model.bipedRightLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
        model.bipedLeftLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;

        GlStateManager.pushMatrix();
        adjustArmorSlotOffset(slot);

        String defaultTex = String.format("minecraft:textures/models/armor/%s_layer_%d.png",
                armor.getArmorMaterial().getName().replace("minecraft:", ""),
                slot == EntityEquipmentSlot.LEGS ? 2 : 1);
        String tex = ForgeHooksClient.getArmorTexture(null, stack, defaultTex, slot, null);
        if (tex != null) {
            mc.getTextureManager().bindTexture(new ResourceLocation(tex));
        }

        int color = armor.getColor(stack);
        if (color != -1) {
            float red = (float)(color >> 16 & 255) / 255.0F;
            float green = (float)(color >> 8 & 255) / 255.0F;
            float blue = (float)(color & 255) / 255.0F;
            GlStateManager.color(red, green, blue, 1.0F);
        } else {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        float scale = 0.0625F;
        if (slot == EntityEquipmentSlot.HEAD) {
            if (model.bipedHead != null) model.bipedHead.render(scale);
            if (model.bipedHeadwear != null) model.bipedHeadwear.render(scale);
        } else if (slot == EntityEquipmentSlot.CHEST) {
            if (model.bipedBody != null) model.bipedBody.render(scale);
            if (model.bipedRightArm != null) model.bipedRightArm.render(scale);
            if (model.bipedLeftArm != null) model.bipedLeftArm.render(scale);
        } else if (slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET) {
            if (model.bipedRightLeg != null) model.bipedRightLeg.render(scale);
            if (model.bipedLeftLeg != null) model.bipedLeftLeg.render(scale);
        }
        GlStateManager.popMatrix();
    }

    private static void renderWeapon(ItemStack stack, Minecraft mc) {
        GlStateManager.pushMatrix();
        mc.getRenderItem().renderItem(stack, TransformType.THIRD_PERSON_LEFT_HAND);
        GlStateManager.popMatrix();
    }

    private static void renderGenericItem(ItemStack stack, Minecraft mc) {
        GlStateManager.pushMatrix();
        // Invert Y for item model so it renders upright under unified camera transform
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        // Only scale down Z thickness for NoteItem / DporItem to look like a thin sheet of paper,
        // while preserving natural 3D dimensions for all blocks, tools, and other items.
        if (stack.getItem() instanceof efw.item.NoteItem || stack.getItem() instanceof efw.item.DporItem) {
            GlStateManager.scale(1.0F, 1.0F, 0.2F);
        }

        mc.getRenderItem().renderItem(stack, TransformType.FIXED);
        GlStateManager.popMatrix();
    }

    private static void adjustArmorSlotOffset(EntityEquipmentSlot slot) {
        switch (slot) {
            case HEAD:
                GlStateManager.translate(0.0F, 0.65F, 0.0F);
                break;
            case CHEST:
                GlStateManager.translate(0.0F, 0.35F, 0.0F);
                break;
            case LEGS:
                GlStateManager.translate(0.0F, -0.15F, 0.0F);
                break;
            case FEET:
                GlStateManager.translate(0.0F, -0.6F, 0.0F);
                break;
        }
    }
}
