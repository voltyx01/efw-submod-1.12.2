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

    public static void renderPlacedInWorld(ItemStack stack, Minecraft mc) {
        if (stack == null || stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        // Standard entity and ModelBiped scale in Minecraft: scale(-1, -1, 1).
        // This inverts Y while keeping positive determinant (det = +1, CCW winding),
        // preventing outside faces from being culled as back-faces.
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);

        renderRawItem(stack, mc, true);

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void renderRawItem(ItemStack stack, Minecraft mc) {
        renderRawItem(stack, mc, false);
    }

    public static void renderRawItem(ItemStack stack, Minecraft mc, boolean inWorld) {
        String regName = stack.getItem().getRegistryName() != null ? stack.getItem().getRegistryName().toString() : "";

        // 1. GeoModel Armor (ItemGeoArmor)
        if (stack.getItem() instanceof ItemGeoArmor) {
            renderGeoArmor((ItemGeoArmor) stack.getItem(), stack, mc, inWorld);
        }
        // 2. CustomArmor (Survival Instinct ModelBiped)
        else if (stack.getItem() instanceof ItemCustomArmor) {
            renderCustomArmor((ItemCustomArmor) stack.getItem(), stack, mc, inWorld);
        }
        // 3. Headlamp Bauble
        else if (stack.getItem() instanceof ItemHeadlamp) {
            renderHeadlamp(mc, inWorld);
        }
        // 4. Bracelet Bauble
        else if (stack.getItem() instanceof ItemBracelet) {
            renderBracelet(mc, inWorld);
        }
        // 5. Backpack
        else if (isBackpack(stack)) {
            renderBackpack(stack, mc, inWorld);
        }
        // 6. Generic Armor (Vanilla or standard ItemArmor)
        else if (stack.getItem() instanceof ItemArmor) {
            renderGenericArmor((ItemArmor) stack.getItem(), stack, mc, inWorld);
        }
        // 7. MWC Weapon
        else if (isWeapon(stack)) {
            renderWeapon(stack, mc, inWorld);
        }
        // 8. Standard Items / 2D Tools / Blocks in 3D
        else {
            renderGenericItem(stack, mc);
        }
    }

    public static boolean isBackpack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        String regName = stack.getItem().getRegistryName() != null ? stack.getItem().getRegistryName().toString() : "";
        return regName.equals("quark:backpack") || regName.contains("backpack");
    }

    public static boolean isWeapon(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem() == null) return false;
        String cls = stack.getItem().getClass().getName();
        return stack.getItem() instanceof com.paneedah.weaponlib.Weapon || cls.contains("weaponlib") || cls.contains("Weapon");
    }

    private static void renderGeoArmor(ItemGeoArmor geoArmor, ItemStack stack, Minecraft mc, boolean inWorld) {
        EntityEquipmentSlot slot = geoArmor.armorType;
        net.minecraft.entity.EntityLivingBase renderEntity = null;
        ModelBiped armorModel = geoArmor.getArmorModel(renderEntity, stack, slot, null);
        if (armorModel instanceof GeoArmorModel) {
            GeoArmorModel model = (GeoArmorModel) armorModel;
            model.resetBipedTransforms();
            model.currentSlot = slot;

            GlStateManager.pushMatrix();
            adjustArmorSlotOffset(slot, inWorld);
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

    private static void renderCustomArmor(ItemCustomArmor customArmor, ItemStack stack, Minecraft mc, boolean inWorld) {
        EntityEquipmentSlot slot = customArmor.armorType;
        ModelBiped model = customArmor.getArmorModel(null, stack, slot, null);
        if (model != null) {
            resetGenericBiped(model);
            GlStateManager.pushMatrix();
            adjustArmorSlotOffset(slot, inWorld);
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

    private static void renderHeadlamp(Minecraft mc, boolean inWorld) {
        GeoArmorModel model = HeadlampRenderer.getHeadlampModel();
        if (model != null) {
            model.resetBipedTransforms();
            GlStateManager.pushMatrix();
            if (!inWorld) {
                GlStateManager.translate(0.0F, 1.5F, 0.0F); // Center headlamp in GUI
            }
            model.currentSlot = EntityEquipmentSlot.HEAD;

            mc.getTextureManager().bindTexture(HeadlampRenderer.getHeadlampTexture());
            if (model.bipedHead != null) {
                model.bipedHead.render(0.0625F);
            }
            GlStateManager.popMatrix();
        }
    }

    private static void renderBracelet(Minecraft mc, boolean inWorld) {
        GeoArmorModel model = BraceletInspectHandler.getNormalModel();
        if (model != null) {
            model.resetBipedTransforms();
            GlStateManager.pushMatrix();
            if (inWorld) {
                GlStateManager.translate(-0.375F, -0.65F, 0.0F);
            } else {
                GlStateManager.translate(-0.375F, 1.05F, 0.0F); // Pivot align in GUI
            }

            mc.getTextureManager().bindTexture(BraceletInspectHandler.getBraceletTexture());
            if (model.bipedLeftArm != null) {
                model.bipedLeftArm.render(0.0625F);
            }
            GlStateManager.popMatrix();
        }
    }

    private static void renderBackpack(ItemStack stack, Minecraft mc, boolean inWorld) {
        GlStateManager.pushMatrix();
        if (inWorld) {
            GlStateManager.translate(0.0F, -0.75F, -0.2F);
        } else {
            GlStateManager.translate(0.0F, 0.35F, -0.35F);
        }

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

    private static void renderGenericArmor(ItemArmor armor, ItemStack stack, Minecraft mc, boolean inWorld) {
        EntityEquipmentSlot slot = armor.armorType;
        ModelBiped model = ForgeHooksClient.getArmorModel(null, stack, slot, null);
        if (model == null) {
            model = new ModelBiped(slot == EntityEquipmentSlot.LEGS ? 0.5F : 1.0F);
        }

        resetGenericBiped(model);

        model.bipedHead.showModel = slot == EntityEquipmentSlot.HEAD;
        model.bipedHeadwear.showModel = slot == EntityEquipmentSlot.HEAD;
        model.bipedBody.showModel = slot == EntityEquipmentSlot.CHEST;
        model.bipedRightArm.showModel = slot == EntityEquipmentSlot.CHEST;
        model.bipedLeftArm.showModel = slot == EntityEquipmentSlot.CHEST;
        model.bipedRightLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
        model.bipedLeftLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;

        GlStateManager.pushMatrix();
        adjustArmorSlotOffset(slot, inWorld);

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

    private static void resetGenericBiped(ModelBiped model) {
        if (model == null) return;
        if (model instanceof GeoArmorModel) {
            ((GeoArmorModel) model).resetBipedTransforms();
            return;
        }
        model.isSneak = false;
        model.isRiding = false;
        model.isChild = false;
        model.rightArmPose = ModelBiped.ArmPose.EMPTY;
        model.leftArmPose = ModelBiped.ArmPose.EMPTY;

        if (model.bipedHead != null) {
            model.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
            model.bipedHead.rotateAngleX = 0.0F;
            model.bipedHead.rotateAngleY = 0.0F;
            model.bipedHead.rotateAngleZ = 0.0F;
            model.bipedHead.offsetX = 0.0F;
            model.bipedHead.offsetY = 0.0F;
            model.bipedHead.offsetZ = 0.0F;
        }
        if (model.bipedHeadwear != null) {
            model.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            model.bipedHeadwear.rotateAngleX = 0.0F;
            model.bipedHeadwear.rotateAngleY = 0.0F;
            model.bipedHeadwear.rotateAngleZ = 0.0F;
            model.bipedHeadwear.offsetX = 0.0F;
            model.bipedHeadwear.offsetY = 0.0F;
            model.bipedHeadwear.offsetZ = 0.0F;
        }
        if (model.bipedBody != null) {
            model.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
            model.bipedBody.rotateAngleX = 0.0F;
            model.bipedBody.rotateAngleY = 0.0F;
            model.bipedBody.rotateAngleZ = 0.0F;
            model.bipedBody.offsetX = 0.0F;
            model.bipedBody.offsetY = 0.0F;
            model.bipedBody.offsetZ = 0.0F;
        }
        if (model.bipedRightArm != null) {
            model.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
            model.bipedRightArm.rotateAngleX = 0.0F;
            model.bipedRightArm.rotateAngleY = 0.0F;
            model.bipedRightArm.rotateAngleZ = 0.0F;
            model.bipedRightArm.offsetX = 0.0F;
            model.bipedRightArm.offsetY = 0.0F;
            model.bipedRightArm.offsetZ = 0.0F;
        }
        if (model.bipedLeftArm != null) {
            model.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            model.bipedLeftArm.rotateAngleX = 0.0F;
            model.bipedLeftArm.rotateAngleY = 0.0F;
            model.bipedLeftArm.rotateAngleZ = 0.0F;
            model.bipedLeftArm.offsetX = 0.0F;
            model.bipedLeftArm.offsetY = 0.0F;
            model.bipedLeftArm.offsetZ = 0.0F;
        }
        if (model.bipedRightLeg != null) {
            model.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
            model.bipedRightLeg.rotateAngleX = 0.0F;
            model.bipedRightLeg.rotateAngleY = 0.0F;
            model.bipedRightLeg.rotateAngleZ = 0.0F;
            model.bipedRightLeg.offsetX = 0.0F;
            model.bipedRightLeg.offsetY = 0.0F;
            model.bipedRightLeg.offsetZ = 0.0F;
        }
        if (model.bipedLeftLeg != null) {
            model.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            model.bipedLeftLeg.rotateAngleX = 0.0F;
            model.bipedLeftLeg.rotateAngleY = 0.0F;
            model.bipedLeftLeg.rotateAngleZ = 0.0F;
            model.bipedLeftLeg.offsetX = 0.0F;
            model.bipedLeftLeg.offsetY = 0.0F;
            model.bipedLeftLeg.offsetZ = 0.0F;
        }
    }

    private static void renderWeapon(ItemStack stack, Minecraft mc, boolean inWorld) {
        GlStateManager.pushMatrix();
        if (inWorld) {
            GlStateManager.translate(0.0F, 0.15F, 0.2F);
        }
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

    private static void adjustArmorSlotOffset(EntityEquipmentSlot slot, boolean inWorld) {
        if (inWorld) {
            switch (slot) {
                case HEAD:
                    // Bottom of helmet (neck) is at y=0. Sits directly on table surface!
                    break;
                case CHEST:
                    // Waist is at y=12 (0.75 blocks down). In inverted Y space, -0.75F shifts waist up to 0:
                    GlStateManager.translate(0.0F, -0.75F, 0.0F);
                    break;
                case LEGS:
                case FEET:
                    // Feet are at y=24 (1.5 blocks down). -1.5F shifts soles up to 0:
                    GlStateManager.translate(0.0F, -1.5F, 0.0F);
                    break;
            }
        } else {
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
}
