package com.voltyx.mwccf.mcore;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCustomArmor extends ItemArmor {

    private final String texturePath;
    private final String modelClassName;
    
    @SideOnly(Side.CLIENT)
    private ModelBiped armorModel;

    public ItemCustomArmor(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String textureName, String modelClassName) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        this.setRegistryName("mwccf", name);
        ((net.minecraft.item.Item) this).setTranslationKey("mcore." + name);
        ((net.minecraft.item.Item) this).setCreativeTab(net.minecraft.creativetab.CreativeTabs.COMBAT);
        this.texturePath = "mwccf:textures/entities/" + textureName + ".png";
        this.modelClassName = modelClassName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (this.armorModel == null && this.modelClassName != null && !this.modelClassName.equals("null")) {
            try {
                this.armorModel = (ModelBiped) Class.forName("com.voltyx.mwccf.client.model.survivalinstinct." + this.modelClassName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.armorModel != null) {
            if (_default != null) {
                this.armorModel.isSneak = _default.isSneak;
                this.armorModel.isRiding = _default.isRiding;
                this.armorModel.isChild = _default.isChild;
                this.armorModel.rightArmPose = _default.rightArmPose;
                this.armorModel.leftArmPose = _default.leftArmPose;

                // Synchronize rotation angles and points from the active entity model
                this.armorModel.bipedHead.setRotationPoint(_default.bipedHead.rotationPointX, _default.bipedHead.rotationPointY, _default.bipedHead.rotationPointZ);
                this.armorModel.bipedHead.rotateAngleX = _default.bipedHead.rotateAngleX;
                this.armorModel.bipedHead.rotateAngleY = _default.bipedHead.rotateAngleY;
                this.armorModel.bipedHead.rotateAngleZ = _default.bipedHead.rotateAngleZ;

                this.armorModel.bipedHeadwear.setRotationPoint(_default.bipedHeadwear.rotationPointX, _default.bipedHeadwear.rotationPointY, _default.bipedHeadwear.rotationPointZ);
                this.armorModel.bipedHeadwear.rotateAngleX = _default.bipedHeadwear.rotateAngleX;
                this.armorModel.bipedHeadwear.rotateAngleY = _default.bipedHeadwear.rotateAngleY;
                this.armorModel.bipedHeadwear.rotateAngleZ = _default.bipedHeadwear.rotateAngleZ;

                this.armorModel.bipedBody.setRotationPoint(_default.bipedBody.rotationPointX, _default.bipedBody.rotationPointY, _default.bipedBody.rotationPointZ);
                this.armorModel.bipedBody.rotateAngleX = _default.bipedBody.rotateAngleX;
                this.armorModel.bipedBody.rotateAngleY = _default.bipedBody.rotateAngleY;
                this.armorModel.bipedBody.rotateAngleZ = _default.bipedBody.rotateAngleZ;

                this.armorModel.bipedRightArm.setRotationPoint(_default.bipedRightArm.rotationPointX, _default.bipedRightArm.rotationPointY, _default.bipedRightArm.rotationPointZ);
                this.armorModel.bipedRightArm.rotateAngleX = _default.bipedRightArm.rotateAngleX;
                this.armorModel.bipedRightArm.rotateAngleY = _default.bipedRightArm.rotateAngleY;
                this.armorModel.bipedRightArm.rotateAngleZ = _default.bipedRightArm.rotateAngleZ;

                this.armorModel.bipedLeftArm.setRotationPoint(_default.bipedLeftArm.rotationPointX, _default.bipedLeftArm.rotationPointY, _default.bipedLeftArm.rotationPointZ);
                this.armorModel.bipedLeftArm.rotateAngleX = _default.bipedLeftArm.rotateAngleX;
                this.armorModel.bipedLeftArm.rotateAngleY = _default.bipedLeftArm.rotateAngleY;
                this.armorModel.bipedLeftArm.rotateAngleZ = _default.bipedLeftArm.rotateAngleZ;

                this.armorModel.bipedRightLeg.setRotationPoint(_default.bipedRightLeg.rotationPointX, _default.bipedRightLeg.rotationPointY, _default.bipedRightLeg.rotationPointZ);
                this.armorModel.bipedRightLeg.rotateAngleX = _default.bipedRightLeg.rotateAngleX;
                this.armorModel.bipedRightLeg.rotateAngleY = _default.bipedRightLeg.rotateAngleY;
                this.armorModel.bipedRightLeg.rotateAngleZ = _default.bipedRightLeg.rotateAngleZ;

                this.armorModel.bipedLeftLeg.setRotationPoint(_default.bipedLeftLeg.rotationPointX, _default.bipedLeftLeg.rotationPointY, _default.bipedLeftLeg.rotationPointZ);
                this.armorModel.bipedLeftLeg.rotateAngleX = _default.bipedLeftLeg.rotateAngleX;
                this.armorModel.bipedLeftLeg.rotateAngleY = _default.bipedLeftLeg.rotateAngleY;
                this.armorModel.bipedLeftLeg.rotateAngleZ = _default.bipedLeftLeg.rotateAngleZ;
            }
            
            this.armorModel.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
            this.armorModel.bipedHeadwear.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
            this.armorModel.bipedBody.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
            this.armorModel.bipedRightArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
            this.armorModel.bipedLeftArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
            this.armorModel.bipedRightLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET);
            this.armorModel.bipedLeftLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET);

            // Iterate over all fields of the model to toggle custom parts according to armorSlot and synchronize arm angles
            Class<?> modelClass = this.armorModel.getClass();
            while (modelClass != null && modelClass != ModelBiped.class) {
                for (java.lang.reflect.Field field : modelClass.getDeclaredFields()) {
                    if (net.minecraft.client.model.ModelRenderer.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        try {
                            net.minecraft.client.model.ModelRenderer renderer = (net.minecraft.client.model.ModelRenderer) field.get(this.armorModel);
                            if (renderer != null) {
                                String name = field.getName().toLowerCase();
                                if (name.equals("head") || name.startsWith("head_")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
                                } else if (name.equals("body") || name.startsWith("body_")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
                                } else if (name.equals("left_arm") || name.startsWith("left_arm_")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
                                    if (name.equals("left_arm")) {
                                        renderer.rotateAngleX = this.armorModel.bipedLeftArm.rotateAngleX;
                                        renderer.rotateAngleY = this.armorModel.bipedLeftArm.rotateAngleY;
                                        renderer.rotateAngleZ = this.armorModel.bipedLeftArm.rotateAngleZ;
                                    }
                                } else if (name.equals("right_arm") || name.startsWith("right_arm_")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
                                    if (name.equals("right_arm")) {
                                        renderer.rotateAngleX = this.armorModel.bipedRightArm.rotateAngleX;
                                        renderer.rotateAngleY = this.armorModel.bipedRightArm.rotateAngleY;
                                        renderer.rotateAngleZ = this.armorModel.bipedRightArm.rotateAngleZ;
                                    }
                                } else if (name.contains("shoe") || name.contains("boot")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.FEET);
                                } else if (name.contains("leg")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.LEGS);
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }
                modelClass = modelClass.getSuperclass();
            }

            return this.armorModel;
        }
        return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return this.texturePath;
    }
}
