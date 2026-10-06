package com.voltyx.mwccf.geo;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import efw.biomeinfo.MwccfConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
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

@SideOnly(Side.CLIENT)
public class BodycamLayer implements LayerRenderer<AbstractClientPlayer> {

    private static final ResourceLocation GEO_LOCATION = new ResourceLocation("mwccf", "geo/bodycam.geo.json");
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("mwccf", "textures/models/armor/bodycam.png");
    private static final ResourceLocation TEXTURE_OFF_LOCATION = new ResourceLocation("mwccf", "textures/models/armor/bodycam_off.png");

    private static GeoArmorModel cachedModel;

    private final RenderPlayer renderer;
    private final String skinTypeKey;

    public BodycamLayer(RenderPlayer renderer, String skinTypeKey) {
        this.renderer = renderer;
        this.skinTypeKey = skinTypeKey;
    }

    public static GeoArmorModel getModel() {
        if (cachedModel == null) {
            cachedModel = new GeoArmorModel(GEO_LOCATION);
        }
        return cachedModel;
    }

    public static ItemStack getEquippedBodycam(EntityPlayer player) {
        if (player == null) return ItemStack.EMPTY;

        // 1. Check Baubles
        if (Loader.isModLoaded("baubles")) {
            try {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemBodycam) {
                            return stack;
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        // 2. Check chest slot
        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (!chestStack.isEmpty() && chestStack.getItem() instanceof ItemBodycam) {
            return chestStack;
        }

        return ItemStack.EMPTY;
    }

    public static boolean hasBodycamEquipped(EntityPlayer player) {
        if (player == null) return false;

        String mode = MwccfConfig.bodycam.mode;
        if ("OFF".equalsIgnoreCase(mode)) {
            return false;
        }
        if ("ALWAYS".equalsIgnoreCase(mode)) {
            return true;
        }

        return !getEquippedBodycam(player).isEmpty();
    }

    public static boolean isBodycamActive(EntityPlayer player) {
        ItemStack stack = getEquippedBodycam(player);
        if (stack.isEmpty()) {
            return "ALWAYS".equalsIgnoreCase(MwccfConfig.bodycam.mode);
        }
        if (!ItemBodycam.isPowerEnabled(stack)) {
            return false;
        }
        net.minecraft.nbt.NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return false;
        int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
        return charge > 0;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player,
                              float limbSwing, float limbSwingAmount, float delta,
                              float age, float yaw, float pitch, float scale) {
        if (efw.util.RenderContext.isRenderingPlayerInSevenScreen || com.voltyx.mwccf.terminal.bodycam.BodycamFeedRenderer.isRendering()) {
            return;
        }

        // Don't render for self in first-person mode (unless viewing in GUI)
        if (!efw.util.RenderContext.isRenderingPlayerInGui &&
                player == Minecraft.getMinecraft().player &&
                Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        if (!hasBodycamEquipped(player)) return;

        String playerSkin = "slim".equals(player.getSkinType()) ? "slim" : "default";
        if (!playerSkin.equals(this.skinTypeKey)) return;

        GeoArmorModel model = getModel();
        if (model == null) return;

        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(player, limbSwing, limbSwingAmount, delta);
        model.currentSlot = EntityEquipmentSlot.CHEST;

        ResourceLocation texture = isBodycamActive(player) ? TEXTURE_LOCATION : TEXTURE_OFF_LOCATION;
        this.renderer.bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();

        model.syncedModel = this.renderer.getMainModel();

        net.minecraft.client.model.ModelRenderer bodycamBone = model.getBone("bodycam");
        float baseRotationPointZ = 2.35F;
        if (bodycamBone != null) {
            float breastShift = getBreastZShift(player, delta);
            bodycamBone.rotationPointZ = baseRotationPointZ - breastShift;
        }

        // Render GeoArmorModel (synced with player model)
        model.render(player, limbSwing, limbSwingAmount, age, yaw, pitch, scale);

        if (bodycamBone != null) {
            bodycamBone.rotationPointZ = baseRotationPointZ;
        }

        GlStateManager.disableCull();
        model.syncedModel = null;
    }

    public static float getBreastZShift(EntityPlayer player, float partialTicks) {
        if (player == null) return 0.0F;
        try {
            com.voltyx.gender.main.GenderPlayer plr = com.voltyx.gender.main.WildfireGender.getPlayerById(player.getUniqueID());
            if (plr != null && plr.getGender().canHaveBreasts()) {
                ItemStack armorStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                com.voltyx.gender.api.IGenderArmor genderArmor = com.voltyx.gender.main.WildfireHelper.getArmorConfig(armorStack);
                boolean isChestplateOccupied = genderArmor != null && genderArmor.coversBreasts();
                boolean hideBreasts = genderArmor != null && (genderArmor.alwaysHidesBreasts() || (!plr.showBreastsInArmor() && isChestplateOccupied));
                if (!armorStack.isEmpty() && armorStack.getItem().getRegistryName() != null) {
                    String armorId = armorStack.getItem().getRegistryName().toString();
                    if (armorId.contains("juggernaut") || armorId.contains("hazmat") || armorId.contains("guillie") || armorId.contains("heavy")) {
                        hideBreasts = true;
                    }
                }
                float shift = 0.0F;
                if (!armorStack.isEmpty()) {
                    shift += 0.125F; // Additional thickness if wearing chestplate / vest
                }
                if (!hideBreasts) {
                    com.voltyx.gender.physics.BreastPhysics phys = plr.getLeftBreastPhysics();
                    float bSize = phys != null ? phys.getBreastSize(partialTicks) : plr.getBustSize();
                    if (bSize > 0.02F) {
                        shift += bSize * 0.625F;
                        if (plr.getBreasts() != null) {
                            shift += plr.getBreasts().getZOffset() * 0.375F;
                        }
                    }
                }
                return shift;
            } else {
                ItemStack armorStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (!armorStack.isEmpty()) {
                    return 0.125F; // Armor thickness for male
                }
            }
        } catch (Throwable ignored) {
        }
        return 0.0F;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
