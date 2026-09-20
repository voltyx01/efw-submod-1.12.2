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
                                        renderer.rotateAngleX = 0.0F;
                                        renderer.rotateAngleY = 0.0F;
                                        renderer.rotateAngleZ = 0.0F;
                                        renderer.rotationPointX = 0.0F;
                                        renderer.rotationPointY = 0.0F;
                                        renderer.rotationPointZ = 0.0F;
                                    }
                                } else if (name.equals("right_arm") || name.startsWith("right_arm_")) {
                                    renderer.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
                                    if (name.equals("right_arm")) {
                                        renderer.rotateAngleX = 0.0F;
                                        renderer.rotateAngleY = 0.0F;
                                        renderer.rotateAngleZ = 0.0F;
                                        renderer.rotationPointX = 0.0F;
                                        renderer.rotationPointY = 0.0F;
                                        renderer.rotationPointZ = 0.0F;
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

            if (this.armorModel instanceof com.voltyx.mwccf.client.model.survivalinstinct.Modelnight_vision_goggles) {
                float angle = com.voltyx.mwccf.armor.NVGAnimationHelper.getVisorAngle(entityLiving, isNVGActive(itemStack));
                ((com.voltyx.mwccf.client.model.survivalinstinct.Modelnight_vision_goggles) this.armorModel).setVisorAngle(angle);
            } else if (this.armorModel instanceof com.voltyx.mwccf.client.model.survivalinstinct.Modelhunter_armor) {
                float angle = com.voltyx.mwccf.armor.NVGAnimationHelper.getVisorAngle(entityLiving, isNVGActive(itemStack));
                ((com.voltyx.mwccf.client.model.survivalinstinct.Modelhunter_armor) this.armorModel).setVisorAngle(angle);
            }

            return this.armorModel;
        }
        return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    }

    public static boolean isNVGActive(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!stack.hasTagCompound()) return false;
        net.minecraft.nbt.NBTTagCompound tag = stack.getTagCompound();
        int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        if (charge <= 0) return false;
        if (tag.hasKey("nv_active")) {
            return tag.getBoolean("nv_active");
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String name = this.getRegistryName() != null ? this.getRegistryName().getPath() : "";
        String nvgKey = com.voltyx.mwccf.armor.NVGKeyHandler.KEY_TOGGLE_NVG != null ? 
                org.lwjgl.input.Keyboard.getKeyName(com.voltyx.mwccf.armor.NVGKeyHandler.KEY_TOGGLE_NVG.getKeyCode()) : "N";
        String dashKey = com.voltyx.mwccf.armor.ExoDashKeyHandler.KEY_EXO_DASH != null ? 
                org.lwjgl.input.Keyboard.getKeyName(com.voltyx.mwccf.armor.ExoDashKeyHandler.KEY_EXO_DASH.getKeyCode()) : "X";

        if (com.voltyx.mwccf.armor.SurvivalInstinctArmorHandler.isNVGHelmet(this)) {
            net.minecraft.nbt.NBTTagCompound tag = stack.getTagCompound();
            int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
            int percent = (int) ((charge / 48000.0f) * 100);

            if (charge <= 0) {
                tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"));
            } else {
                String color = percent > 50 ? "\u00a7a" : (percent > 20 ? "\u00a7e" : "\u00a7c");
                tooltip.add(color + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.charge", percent));
            }
            tooltip.add("\u00a78" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.battery.charge_hint"));
        }

        if (name.contains("gas_mask")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.gas_mask"));
        }
        if (name.contains("night_vision") || name.contains("hunter_helmet")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.nvg_toggle", nvgKey));
        }
        if (name.contains("hazmat")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.hazmat_set"));
        }
        if (name.contains("fire_fighter")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.firefighter_set"));
        }
        if (name.contains("guillie") || name.contains("ghillie")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.ghillie_set"));
        }
        if (name.contains("juggernaut")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.juggernaut_set"));
        }
        if (name.startsWith("exo_heavy")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.exo_heavy_set"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.exo_dash", dashKey));
        } else if (name.startsWith("exo_")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.exo_set"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.exo_dash", dashKey));
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return this.texturePath;
    }
}
