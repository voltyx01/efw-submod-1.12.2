package com.voltyx.mwccf.geo;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGeoArmor extends ItemArmor {

    private final String texturePath;
    private final ResourceLocation geoModelPath;
    private final ResourceLocation slimGeoModelPath;
    private boolean checkedSlim = false;
    private boolean hasSlim = false;
    
    @SideOnly(Side.CLIENT)
    private GeoArmorModel cachedModel;

    @SideOnly(Side.CLIENT)
    private GeoArmorModel cachedSlimModel;

    public ItemGeoArmor(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String textureName, String geoModelName) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        this.setRegistryName("mwccf", name);
        ((net.minecraft.item.Item) this).setTranslationKey("mcore." + name);
        ((net.minecraft.item.Item) this).setCreativeTab(net.minecraft.creativetab.CreativeTabs.COMBAT);
        this.texturePath = "mwccf:textures/models/armor/" + textureName + ".png";
        this.geoModelPath = new ResourceLocation("mwccf", "geo/" + geoModelName + ".geo.json");
        this.slimGeoModelPath = new ResourceLocation("mwccf", "geo/" + geoModelName + "_slim.geo.json");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return this.texturePath;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (!this.checkedSlim) {
            this.checkedSlim = true;
            try {
                net.minecraft.client.Minecraft.getMinecraft().getResourceManager().getResource(this.slimGeoModelPath);
                this.hasSlim = true;
            } catch (Exception e) {
                this.hasSlim = false;
            }
        }

        boolean isSlim = false;
        if (entityLiving instanceof net.minecraft.client.entity.AbstractClientPlayer) {
            isSlim = "slim".equals(((net.minecraft.client.entity.AbstractClientPlayer)entityLiving).getSkinType());
        }

        GeoArmorModel modelToUse;

        if (isSlim && this.hasSlim) {
            if (this.cachedSlimModel == null) {
                this.cachedSlimModel = new GeoArmorModel(this.slimGeoModelPath);
            }
            modelToUse = this.cachedSlimModel;
        } else {
            if (this.cachedModel == null) {
                this.cachedModel = new GeoArmorModel(this.geoModelPath);
            }
            modelToUse = this.cachedModel;
        }
        
        // Sync visibility and states from default model
        if (_default != null) {
            modelToUse.isSneak = _default.isSneak;
            modelToUse.isRiding = _default.isRiding;
            modelToUse.isChild = _default.isChild;
            modelToUse.rightArmPose = _default.rightArmPose;
            modelToUse.leftArmPose = _default.leftArmPose;
            modelToUse.syncedModel = _default;
        }

        // Set the current slot to enforce precise rendering in GeoArmorModel
        modelToUse.currentSlot = armorSlot;

        return modelToUse;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String name = this.getRegistryName() != null ? this.getRegistryName().getPath() : "";

        if (name.contains("gas_mask")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.gas_mask"));
        }
        if (name.contains("hazmat")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.hazmat_set"));
        }
        if (name.contains("guillie") || name.contains("ghillie")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.ghillie_set"));
        }
        if (name.contains("juggernaut")) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.juggernaut_set"));
        }
    }
}
