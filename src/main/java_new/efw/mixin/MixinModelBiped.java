package efw.mixin;

import com.voltyx.mwccf.TorchAnimationHandler;
import com.fuzs.aquaacrobatics.client.model.IModelBipedSwimming;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.util.math.MathHelperNew;
import efw.animation.AnimationApplicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import com.paneedah.weaponlib.Weapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

import static efw.animation.AnimationApplicator.applyBone;

@SuppressWarnings("unused")
@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase implements IModelBipedSwimming {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinModelBiped class loaded!");
    }

    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    // SHADOWS (Р”РѕСЃС‚СѓРї Рє РІР°РЅРёР»СЊРЅС‹Рј РїРѕР»СЏРј ModelBiped)
    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    @Shadow
    public ModelRenderer bipedBody;
    @Shadow
    public ModelRenderer bipedHead;
    @Shadow
    public ModelRenderer bipedHeadwear;
    @Shadow
    public ModelRenderer bipedRightArm;
    @Shadow
    public ModelRenderer bipedLeftArm;
    @Shadow
    public ModelRenderer bipedRightLeg;
    @Shadow
    public ModelRenderer bipedLeftLeg;

    @Shadow
    protected abstract EnumHandSide getMainHand(Entity entityIn);

    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    // UNIQUE (РџРµСЂРµРјРµРЅРЅС‹Рµ Рё РјРµС‚РѕРґС‹ РёР· Aqua Acrobatics)
    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    @Unique
    public float swimAnimation;
    @Unique
    private static final float WEAPON_ARM_SPREAD = 1.0f; // РќР° СЃРєРѕР»СЊРєРѕ СЂР°Р·РґРІРёРіР°С‚СЊ СЂСѓРєРё РїСЂРё СѓРґРµСЂР¶Р°РЅРёРё РѕСЂСѓР¶РёСЏ MWC

    @Override
    public void setSwimAnimation(float swimAnimation) {
        this.swimAnimation = swimAnimation;
    }

    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entitylivingbaseIn, float limbSwing,
            float limbSwingAmount, float partialTickTime) {
        if (entitylivingbaseIn instanceof IPlayerResizeable) {
            this.swimAnimation = ((IPlayerResizeable) entitylivingbaseIn).getSwimAnimation(partialTickTime);
        }
    }

    @Unique
    protected float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {
        float f = (mulIn - maxAngleIn) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI)
            f += ((float) Math.PI * 2F);
        if (f >= (float) Math.PI)
            f -= ((float) Math.PI * 2F);
        return maxAngleIn + angleIn * f;
    }

    @Unique
    private float getArmAngleSq(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    // РРќР–Р•РљР¦РРЇ 0: Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёРµ Р±Р°Р·РѕРІС‹С… СЃРјРµС‰РµРЅРёР№ РїРµСЂРµРґ СЂРµРЅРґРµСЂРѕРј
    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    @Inject(method = "setRotationAngles", at = @At("HEAD"), cancellable = true)
    public void resetRotationPoints(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!(entityIn instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) entityIn;
        Minecraft mc = Minecraft.getMinecraft();

        ModelBiped mainBiped = null;
        net.minecraft.client.renderer.entity.RenderManager rm = mc.getRenderManager();
        net.minecraft.client.renderer.entity.Render<?> renderer = rm.getEntityRenderObject(player);
        if (renderer instanceof net.minecraft.client.renderer.entity.RenderLivingBase) {
            ModelBase mainModel = ((net.minecraft.client.renderer.entity.RenderLivingBase<?>) renderer).getMainModel();
            if (mainModel instanceof ModelBiped) {
                mainBiped = (ModelBiped) mainModel;
            }
        }

        // --- Р‘Р РћРќРЇ: РРґРµР°Р»СЊРЅР°СЏ СЃРёРЅС…СЂРѕРЅРёР·Р°С†РёСЏ ---
        if (mainBiped != null && mainBiped != (Object) this) {
            this.bipedRightArm.rotateAngleX = mainBiped.bipedRightArm.rotateAngleX;
            this.bipedRightArm.rotateAngleY = mainBiped.bipedRightArm.rotateAngleY;
            this.bipedRightArm.rotateAngleZ = mainBiped.bipedRightArm.rotateAngleZ;
            this.bipedRightArm.rotationPointX = mainBiped.bipedRightArm.rotationPointX;
            this.bipedRightArm.rotationPointY = mainBiped.bipedRightArm.rotationPointY;
            this.bipedRightArm.rotationPointZ = mainBiped.bipedRightArm.rotationPointZ;

            this.bipedLeftArm.rotateAngleX = mainBiped.bipedLeftArm.rotateAngleX;
            this.bipedLeftArm.rotateAngleY = mainBiped.bipedLeftArm.rotateAngleY;
            this.bipedLeftArm.rotateAngleZ = mainBiped.bipedLeftArm.rotateAngleZ;
            this.bipedLeftArm.rotationPointX = mainBiped.bipedLeftArm.rotationPointX;
            this.bipedLeftArm.rotationPointY = mainBiped.bipedLeftArm.rotationPointY;
            this.bipedLeftArm.rotationPointZ = mainBiped.bipedLeftArm.rotationPointZ;

            this.bipedRightLeg.rotateAngleX = mainBiped.bipedRightLeg.rotateAngleX;
            this.bipedRightLeg.rotateAngleY = mainBiped.bipedRightLeg.rotateAngleY;
            this.bipedRightLeg.rotateAngleZ = mainBiped.bipedRightLeg.rotateAngleZ;
            this.bipedRightLeg.rotationPointX = mainBiped.bipedRightLeg.rotationPointX;
            this.bipedRightLeg.rotationPointY = mainBiped.bipedRightLeg.rotationPointY;
            this.bipedRightLeg.rotationPointZ = mainBiped.bipedRightLeg.rotationPointZ;

            this.bipedLeftLeg.rotateAngleX = mainBiped.bipedLeftLeg.rotateAngleX;
            this.bipedLeftLeg.rotateAngleY = mainBiped.bipedLeftLeg.rotateAngleY;
            this.bipedLeftLeg.rotateAngleZ = mainBiped.bipedLeftLeg.rotateAngleZ;
            this.bipedLeftLeg.rotationPointX = mainBiped.bipedLeftLeg.rotationPointX;
            this.bipedLeftLeg.rotationPointY = mainBiped.bipedLeftLeg.rotationPointY;
            this.bipedLeftLeg.rotationPointZ = mainBiped.bipedLeftLeg.rotationPointZ;

            this.bipedBody.rotateAngleX = mainBiped.bipedBody.rotateAngleX;
            this.bipedBody.rotateAngleY = mainBiped.bipedBody.rotateAngleY;
            this.bipedBody.rotateAngleZ = mainBiped.bipedBody.rotateAngleZ;
            this.bipedBody.rotationPointX = mainBiped.bipedBody.rotationPointX;
            this.bipedBody.rotationPointY = mainBiped.bipedBody.rotationPointY;
            this.bipedBody.rotationPointZ = mainBiped.bipedBody.rotationPointZ;

            this.bipedHead.rotateAngleX = mainBiped.bipedHead.rotateAngleX;
            this.bipedHead.rotateAngleY = mainBiped.bipedHead.rotateAngleY;
            this.bipedHead.rotateAngleZ = mainBiped.bipedHead.rotateAngleZ;
            this.bipedHead.rotationPointX = mainBiped.bipedHead.rotationPointX;
            this.bipedHead.rotationPointY = mainBiped.bipedHead.rotationPointY;
            this.bipedHead.rotationPointZ = mainBiped.bipedHead.rotationPointZ;

            // Р•СЃР»Рё СЌС‚Рѕ ModelPlayer, СЃРёРЅС…СЂРѕРЅРёР·РёСЂСѓРµРј СЃР»РѕРё (СЂСѓРєР°РІР°, С€С‚Р°РЅРёРЅС‹ Рё С‚.Рґ.)
            if ((Object) this instanceof net.minecraft.client.model.ModelPlayer
                    && mainBiped instanceof net.minecraft.client.model.ModelPlayer) {
                net.minecraft.client.model.ModelPlayer mpThis = (net.minecraft.client.model.ModelPlayer) (Object) this;
                net.minecraft.client.model.ModelPlayer mpMain = (net.minecraft.client.model.ModelPlayer) mainBiped;

                mpThis.bipedRightArmwear.rotateAngleX = mpMain.bipedRightArmwear.rotateAngleX;
                mpThis.bipedRightArmwear.rotateAngleY = mpMain.bipedRightArmwear.rotateAngleY;
                mpThis.bipedRightArmwear.rotateAngleZ = mpMain.bipedRightArmwear.rotateAngleZ;
                mpThis.bipedRightArmwear.rotationPointX = mpMain.bipedRightArmwear.rotationPointX;
                mpThis.bipedRightArmwear.rotationPointY = mpMain.bipedRightArmwear.rotationPointY;
                mpThis.bipedRightArmwear.rotationPointZ = mpMain.bipedRightArmwear.rotationPointZ;

                mpThis.bipedLeftArmwear.rotateAngleX = mpMain.bipedLeftArmwear.rotateAngleX;
                mpThis.bipedLeftArmwear.rotateAngleY = mpMain.bipedLeftArmwear.rotateAngleY;
                mpThis.bipedLeftArmwear.rotateAngleZ = mpMain.bipedLeftArmwear.rotateAngleZ;
                mpThis.bipedLeftArmwear.rotationPointX = mpMain.bipedLeftArmwear.rotationPointX;
                mpThis.bipedLeftArmwear.rotationPointY = mpMain.bipedLeftArmwear.rotationPointY;
                mpThis.bipedLeftArmwear.rotationPointZ = mpMain.bipedLeftArmwear.rotationPointZ;

                mpThis.bipedRightLegwear.rotateAngleX = mpMain.bipedRightLegwear.rotateAngleX;
                mpThis.bipedRightLegwear.rotateAngleY = mpMain.bipedRightLegwear.rotateAngleY;
                mpThis.bipedRightLegwear.rotateAngleZ = mpMain.bipedRightLegwear.rotateAngleZ;
                mpThis.bipedRightLegwear.rotationPointX = mpMain.bipedRightLegwear.rotationPointX;
                mpThis.bipedRightLegwear.rotationPointY = mpMain.bipedRightLegwear.rotationPointY;
                mpThis.bipedRightLegwear.rotationPointZ = mpMain.bipedRightLegwear.rotationPointZ;

                mpThis.bipedLeftLegwear.rotateAngleX = mpMain.bipedLeftLegwear.rotateAngleX;
                mpThis.bipedLeftLegwear.rotateAngleY = mpMain.bipedLeftLegwear.rotateAngleY;
                mpThis.bipedLeftLegwear.rotateAngleZ = mpMain.bipedLeftLegwear.rotateAngleZ;
                mpThis.bipedLeftLegwear.rotationPointX = mpMain.bipedLeftLegwear.rotationPointX;
                mpThis.bipedLeftLegwear.rotationPointY = mpMain.bipedLeftLegwear.rotationPointY;
                mpThis.bipedLeftLegwear.rotationPointZ = mpMain.bipedLeftLegwear.rotationPointZ;

                mpThis.bipedBodyWear.rotateAngleX = mpMain.bipedBodyWear.rotateAngleX;
                mpThis.bipedBodyWear.rotateAngleY = mpMain.bipedBodyWear.rotateAngleY;
                mpThis.bipedBodyWear.rotateAngleZ = mpMain.bipedBodyWear.rotateAngleZ;
                mpThis.bipedBodyWear.rotationPointX = mpMain.bipedBodyWear.rotationPointX;
                mpThis.bipedBodyWear.rotationPointY = mpMain.bipedBodyWear.rotationPointY;
                mpThis.bipedBodyWear.rotationPointZ = mpMain.bipedBodyWear.rotationPointZ;
            }
            if (this.bipedHeadwear != null && mainBiped.bipedHeadwear != null) {
                this.bipedHeadwear.rotateAngleX = mainBiped.bipedHeadwear.rotateAngleX;
                this.bipedHeadwear.rotateAngleY = mainBiped.bipedHeadwear.rotateAngleY;
                this.bipedHeadwear.rotateAngleZ = mainBiped.bipedHeadwear.rotateAngleZ;
                this.bipedHeadwear.rotationPointX = mainBiped.bipedHeadwear.rotationPointX;
                this.bipedHeadwear.rotationPointY = mainBiped.bipedHeadwear.rotationPointY;
                this.bipedHeadwear.rotationPointZ = mainBiped.bipedHeadwear.rotationPointZ;
            }

            // Also copy transforms if this model is GeoArmorModel or custom survival instinct ModelBiped
            if ((Object) this instanceof com.voltyx.mwccf.geo.GeoArmorModel) {
                com.voltyx.mwccf.geo.GeoArmorModel geoThis = (com.voltyx.mwccf.geo.GeoArmorModel) (Object) this;
                geoThis.syncedModel = mainBiped;
            } else {
                Class<?> cls = this.getClass();
                if (cls.getName().startsWith("com.voltyx.mwccf.client.model.survivalinstinct.")) {
                    while (cls != null && cls != ModelBiped.class) {
                        for (java.lang.reflect.Field field : cls.getDeclaredFields()) {
                            if (ModelRenderer.class.isAssignableFrom(field.getType())) {
                                field.setAccessible(true);
                                try {
                                    ModelRenderer mr = (ModelRenderer) field.get(this);
                                    if (mr != null) {
                                        String fn = field.getName().toLowerCase();
                                        if (fn.equals("left_arm")) {
                                            mr.rotateAngleX = this.bipedLeftArm.rotateAngleX;
                                            mr.rotateAngleY = this.bipedLeftArm.rotateAngleY;
                                            mr.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
                                        } else if (fn.equals("right_arm")) {
                                            mr.rotateAngleX = this.bipedRightArm.rotateAngleX;
                                            mr.rotateAngleY = this.bipedRightArm.rotateAngleY;
                                            mr.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
                                        }
                                    }
                                } catch (Exception ignored) {}
                            }
                        }
                        cls = cls.getSuperclass();
                    }
                }
            }

            ci.cancel();
            return;
        }

        // Р–Р•РЎРўРљРћ СЃР±СЂР°СЃС‹РІР°РµРј Р’РЎР• РєРѕРѕСЂРґРёРЅР°С‚С‹ РІ РІР°РЅРёР»СЊРЅС‹Рµ РґРµС„РѕР»С‚С‹ РїРµСЂРµРґ РєР°РґСЂРѕРј.
        // Р­С‚Рѕ РїСЂРµРґРѕС‚РІСЂР°С‰Р°РµС‚ Р›Р®Р‘РћР• РЅР°РєРѕРїР»РµРЅРёРµ (СѓР»РµС‚Р°РЅРёРµ РІ РЅРµР±Рѕ), РµСЃР»Рё РєР°РєРѕР№-С‚Рѕ РјРѕРґ
        // РїРµСЂРµРѕРїСЂРµРґРµР»РёР» setRotationAngles Рё РЅРµ РІС‹Р·РІР°Р» super (РІР°РЅРёР»СЊРЅС‹Р№ СЃР±СЂРѕСЃ isSneak).
        boolean isSneak = entityIn.isSneaking();

        efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);
        net.minecraft.item.ItemStack resetActiveStack = player.getActiveItemStack();
        boolean isResetConsuming = player.isHandActive() && !resetActiveStack.isEmpty() 
                && (resetActiveStack.getItemUseAction() == EnumAction.EAT 
                 || resetActiveStack.getItemUseAction() == EnumAction.DRINK);

        if (ap != null && (ap.isPlaying() || ap.getWeight() > 0f) && !isResetConsuming) {
            // Если играет кастомная анимация (которая сама опускает игрока при шифте),
            // мы временно отключаем ванильный сдвиг, чтобы модель не уходила под землю
            // дважды!
            isSneak = false;
            ((ModelBiped) (Object) this).isSneak = false;
            
            // Если играет анимация оружия, щита или лука, убираем ванильные позы (лука/предмета/щита), 
            // иначе ванилла выкрутит руки на -90 градусов перед тем как наложится наша анимация
            String currentAnim = ap.getCurrentAnimationName();
            String action = ap.getCurrentActionName();
            if (action == null) {
                action = ap.getFadeActionName();
            }
            boolean hasWeaponOrSpecial = (action != null && (action.contains("rifle") || action.contains("pistol") || action.contains("bow") || action.contains("melee") || action.contains("reload") || action.contains("aim")))
                    || (currentAnim != null && (currentAnim.contains("shield") || currentAnim.contains("bow") || currentAnim.startsWith("pistol_") || currentAnim.startsWith("rifle_")))
                    || ap.isHoldingWeapon;
            if (hasWeaponOrSpecial) {
                ((ModelBiped) (Object) this).rightArmPose = ModelBiped.ArmPose.EMPTY;
                ((ModelBiped) (Object) this).leftArmPose = ModelBiped.ArmPose.EMPTY;
            }
        }

        if (!isResetConsuming) {
            this.bipedRightArm.rotationPointX = -5.0F;
            this.bipedRightArm.rotationPointY = isSneak ? 5.2F : 2.0F;
            this.bipedRightArm.rotationPointZ = 0.0F;

            this.bipedLeftArm.rotationPointX = 5.0F;
            this.bipedLeftArm.rotationPointY = isSneak ? 5.2F : 2.0F;
            this.bipedLeftArm.rotationPointZ = 0.0F;

            this.bipedRightLeg.rotationPointX = -1.9F;
            this.bipedRightLeg.rotationPointY = isSneak ? 12.2F : 12.0F;
            this.bipedRightLeg.rotationPointZ = 0.0F;

            this.bipedLeftLeg.rotationPointX = 1.9F;
            this.bipedLeftLeg.rotationPointY = isSneak ? 12.2F : 12.0F;
            this.bipedLeftLeg.rotationPointZ = 0.0F;

            this.bipedHead.rotationPointX = 0.0F;
            this.bipedHead.rotationPointY = isSneak ? 1.0F : 0.0F;
            this.bipedHead.rotationPointZ = 0.0F;

            this.bipedBody.rotationPointX = 0.0F;
            this.bipedBody.rotationPointY = isSneak ? 3.2F : 0.0F;
            this.bipedBody.rotationPointZ = 0.0F;
        }
    }

    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    // РРќР–Р•РљР¦РРЇ 1: Aqua Acrobatics (РќР°РєР»РѕРЅ РіРѕР»РѕРІС‹ РІ РїРѕР»РµС‚Рµ/РїР»Р°РІР°РЅРёРё)
    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V"))
    public void redirectSetRotationAngles(ModelBiped modelBiped, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof IPlayerResizeable) {
            boolean isElytra = ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
            boolean isSwimming = ((IPlayerResizeable) entityIn).isActuallySwimming();
            if (!isElytra && this.swimAnimation > 0.0F) {
                if (isSwimming) {
                    headPitch = this.rotLerpRad(this.swimAnimation, this.bipedHead.rotateAngleX,
                            ((float) -Math.PI / 4F)) / 0.017453292F;
                } else {
                    headPitch = this.rotLerpRad(this.swimAnimation, this.bipedHead.rotateAngleX,
                            headPitch * ((float) Math.PI / 180F)) / 0.017453292F;
                }
            }
        }
        if (entityIn != null) {
            modelBiped.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor,
                    entityIn);
        }
    }



    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    // РРќР–Р•РљР¦РРЇ 4: РћС‚РєР»СЋС‡РµРЅРёРµ РІР°РЅРёР»СЊРЅРѕР№ Р°РЅРёРјР°С†РёРё РІР·РјР°С…Р°, РµСЃР»Рё РёРіСЂР°РµС‚ РЅР°С€Р°
    @Inject(method = "setRotationAngles", at = @At("HEAD"))
    public void mwccf$onSetRotationAnglesHead(float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (entityIn instanceof EntityPlayer) {
            efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer((EntityPlayer) entityIn);
            if (ap != null && ap.hasActionWeight()) {
                // РџРћР›РќРћРЎРўР¬Р® РћРўРљР›Р®Р§РђР•Рњ РІР°РЅРёР»СЊРЅСѓСЋ Р°РЅРёРјР°С†РёСЋ РІР·РјР°С…Р° (swingProgress),
                // РµСЃР»Рё РїСЂРѕРёРіСЂС‹РІР°РµС‚СЃСЏ РЅР°С€Р° СЌРєС€РЅ-Р°РЅРёРјР°С†РёСЏ. РРЅР°С‡Рµ РѕРЅРё РєРѕРЅС„Р»РёРєС‚СѓСЋС‚ (СЃРјРµС€РёРІР°СЋС‚СЃСЏ)
                // Рё РІС‹Р·С‹РІР°СЋС‚ РїРѕРґРµСЂРіРёРІР°РЅРёРµ (РґРµСЂРіР°РЅРЅС‹Рµ СЂС‹РІРєРё) РїСЂРё Р»РѕРјР°РЅРёРё Р±Р»РѕРєРѕРІ РёР»Рё СЃРїР°РјРµ.
                this.swingProgress = 0.0f;
            }
        }
    }
    @Unique
    private float shortestAngleLerp(float a, float b, float t) {
        float delta = (b - a) % (2f * (float) Math.PI);
        if (delta < -(float) Math.PI) delta += 2f * (float) Math.PI;
        if (delta >= (float) Math.PI) delta -= 2f * (float) Math.PI;
        return a + delta * t;
    }
    // РРќР–Р•РљР¦РРЇ 5: РџР»Р°РІР°РЅРёРµ, РџРѕР»Р·Р°РЅРёРµ, РЎРіР»Р°Р¶РёРІР°РЅРёРµ Рё Р¤Р°РєРµР» (Р’СЃС‘ РІ РѕРґРЅРѕРј!)
    // в”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђв”Ђ
    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void applyCustomAnimations(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!(entityIn instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) entityIn;
        Minecraft mc = Minecraft.getMinecraft();

        // EAT/DRINK вЂ” РїСЂРёРјРµРЅСЏРµРј Р°РЅРёРјР°С†РёСЋ РµРґС‹/РїРёС‚СЊСЏ РєР°Рє fallback.
        // Р•СЃР»Рё AquaAcrobatics РѕС‚СЂР°Р±РѕС‚Р°Р» (ci.cancel()) вЂ” СЌС‚РѕС‚ RETURN injection РјРѕР¶РµС‚ РЅРµ Р·Р°РїСѓСЃС‚РёС‚СЊСЃСЏ.
        // Р•СЃР»Рё РЅРµ РѕС‚СЂР°Р±РѕС‚Р°Р» (РІР°РЅРёР»СЊРЅС‹Р№ return) вЂ” РјС‹ РїСЂРёРјРµРЅСЏРµРј Р°РЅРёРјР°С†РёСЋ СЂСѓРєРё СЃР°РјРё, РїРѕ С‚РѕР№ Р¶Рµ С„РѕСЂРјСѓР»Рµ.
        if (player.isHandActive()) {
            net.minecraft.item.ItemStack cs = player.getActiveItemStack();
            if (!cs.isEmpty()) {
                net.minecraft.item.EnumAction ca = cs.getItemUseAction();
                if (ca == EnumAction.EAT || ca == EnumAction.DRINK) {
                    int itemInUseCount = player.getItemInUseCount();
                    int maxDuration = cs.getMaxItemUseDuration();
                    if (itemInUseCount > 0 && maxDuration > 0) {
                        // Р’РѕСЃРїСЂРѕРёР·РІРѕРґРёРј С„РѕСЂРјСѓР»Сѓ AquaAcrobatics
                        float fracTick = ageInTicks - (float)((int) ageInTicks);
                        float animCount = (float) itemInUseCount - fracTick + 1.0f;
                        float useRatio = animCount / (float) maxDuration;
                        // РћРіСЂР°РЅРёС‡РёРІР°РµРј: РµСЃР»Рё useRatio > 1 (РїРµСЂРІС‹Р№ С‚РёРє), f18 < 0 вЂ” РЅРµ С‚СЂРѕРіР°РµРј
                        if (useRatio <= 1.0f) {
                            float f18 = 1.0f - (float) Math.pow((double) useRatio, 27.0);
                            if (useRatio < 0.8f) {
                                f18 += 0.1f * Math.abs(net.minecraft.util.math.MathHelper.cos(
                                        animCount * (float) Math.PI / 4.0f));
                            }
                            f18 = Math.min(1.5f, f18);
                            if (f18 > 0.001f) {
                                // РћРїСЂРµРґРµР»СЏРµРј РѕСЃРЅРѕРІРЅСѓСЋ СЂСѓРєСѓ
                                boolean isRightHanded = player.getPrimaryHand() == net.minecraft.util.EnumHandSide.RIGHT;
                                net.minecraft.client.model.ModelRenderer armPrimary =
                                        isRightHanded ? this.bipedRightArm : this.bipedLeftArm;
                                // Р’С‹С‡РёСЃР»СЏРµРј Р±Р°Р·РѕРІС‹Р№ СѓРіРѕР» (РєР°Рє РІ РІР°РЅРёР»Рё) Рё РїСЂРёРјРµРЅСЏРµРј РїРѕРґСЉС‘Рј
                                float baseArmX = isRightHanded
                                        ? net.minecraft.util.math.MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)
                                                * 2.0F * limbSwingAmount * 0.5F
                                        : net.minecraft.util.math.MathHelper.cos(limbSwing * 0.6662F)
                                                * 2.0F * limbSwingAmount * 0.5F;
                                armPrimary.rotateAngleX = f18 * (baseArmX * 0.5f - 1.2566371f);

                                // Р“РѕР»РѕРІР° Р°РЅРёРјРёСЂСѓРµС‚СЃСЏ РїСЂРё РµРґРµ вЂ” РїРѕРІРѕСЂР°С‡РёРІР°РµС‚СЃСЏ РІРїРµСЂС‘Рґ Рё РЅР°РєР»РѕРЅСЏРµС‚СЃСЏ РІРЅРёР·
                                // (AquaAcrobatics РЅРµ Р°РЅРёРјРёСЂСѓРµС‚ РіРѕР»РѕРІСѓ РґР»СЏ РѕР±С‹С‡РЅРѕР№ РµРґС‹ вЂ” РґРѕР±Р°РІР»СЏРµРј СЃР°РјРё)
                                // РќР°РєР»РѕРЅ РІРїРµСЂС‘Рґ/РІРЅРёР· (+X)
                                float headTilt = f18 * 0.35f;  // max ~20В°
                                this.bipedHead.rotateAngleX += headTilt;
                                // РџРѕРІРѕСЂРѕС‚ РІРїРµСЂС‘Рґ (yaw в†’ 0, С‚.Рµ. РїСЂСЏРјРѕ): lerp РѕС‚ С‚РµРєСѓС‰РµРіРѕ Рє 0
                                this.bipedHead.rotateAngleY *= (1.0f - f18);
                                // РЈР±РёСЂР°РµРј Р±РѕРєРѕРІРѕР№ РЅР°РєР»РѕРЅ (Z в†’ 0)
                                this.bipedHead.rotateAngleZ *= (1.0f - f18);
                                // РЎРёРЅС…СЂРѕРЅРёР·РёСЂСѓРµРј С€Р»РµРј (РєР°Рє copyModelAngles Сѓ AquaAcrobatics)
                                if (this.bipedHeadwear != null) {
                                    this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
                                    this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
                                    this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
                                }
                            }  // end if (f18 > 0.001f)
                        }  // end if (useRatio <= 1.0f)
                    }  // end if (itemInUseCount > 0 && maxDuration > 0)
                    return;
                }
            }
        }

        // Skip custom animations in first-person view, unless we're rendering the player inside a GUI (inventory)
        if (player == mc.player && mc.gameSettings.thirdPersonView == 0 && !efw.util.RenderContext.isRenderingPlayerInGui)
            return;

        float pt = mc.isGamePaused() ? 1.0f : mc.getRenderPartialTicks();

        TorchAnimationHandler.AnimState state = TorchAnimationHandler.getState(player);
        if (state == null)
            return;

        // --- РџР РРњР•РќР•РќРР• РђРќРРњРђР¦РР™ Рљ РђР РњРђРўРЈР Р• РР“Р РћРљРђ MWCCF ---
        efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer(player);

        // --- Р§РђРЎРўР¬ A: AQUA ACROBATICS (РЎР±СЂРѕСЃ Рё Р»РѕРіРёРєР° РїР»Р°РІР°РЅРёСЏ) ---
        boolean hasCustomSwim = ap != null && ap.isPlaying() && "swimming".equals(ap.getCurrentAnimationName());

        if (this.swimAnimation > 0.0F && !entityIn.isInWater() && !hasCustomSwim) {
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
            this.bipedRightLeg.rotationPointZ = 0.0F;
            this.bipedLeftLeg.rotationPointZ = 0.0F;
            this.bipedBody.rotationPointY = 0.0F;
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedHead.rotationPointY = 0.0F;
        }

        if (this.swimAnimation > 0.0F && !hasCustomSwim) {
            float time = limbSwing * 0.6662F;
            EnumHandSide handside = this.getMainHand(entityIn);
            float f2 = handside == EnumHandSide.RIGHT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
            float f3 = handside == EnumHandSide.LEFT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;

            float basePY = 2.0F;
            float basePZ = 0.0F;

            if (!entityIn.isInWater()) {
                // Crawling logic is handled by JSON animations now.
            } else {
                this.bipedRightArm.rotationPointY = basePY;
                this.bipedRightArm.rotationPointZ = basePZ;
                this.bipedLeftArm.rotationPointY = basePY;
                this.bipedLeftArm.rotationPointZ = basePZ;

                float f1 = limbSwing % 26.0F;
                if (f1 < 14.0F) {
                    this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, 0.0F);
                    this.bipedRightArm.rotateAngleX = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleX, 0.0F);
                    this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float) Math.PI);
                    this.bipedRightArm.rotateAngleY = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleY, (float) Math.PI);
                    this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ,
                            (float) Math.PI + 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
                    this.bipedRightArm.rotateAngleZ = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleZ,
                            (float) Math.PI - 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
                } else if (f1 >= 14.0F && f1 < 22.0F) {
                    float f10 = (f1 - 14.0F) / 8.0F;
                    this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX,
                            ((float) Math.PI / 2F) * f10);
                    this.bipedRightArm.rotateAngleX = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleX,
                            ((float) Math.PI / 2F) * f10);
                    this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY,
                            (float) Math.PI);
                    this.bipedRightArm.rotateAngleY = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleY,
                            (float) Math.PI);
                    this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ,
                            5.012389F - 1.8707964F * f10);
                    this.bipedRightArm.rotateAngleZ = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleZ,
                            1.2707963F + 1.8707964F * f10);
                } else if (f1 >= 22.0F && f1 < 26.0F) {
                    float f9 = (f1 - 22.0F) / 4.0F;
                    this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX,
                            ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f9);
                    this.bipedRightArm.rotateAngleX = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleX,
                            ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f9);
                    this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY,
                            (float) Math.PI);
                    this.bipedRightArm.rotateAngleY = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleY,
                            (float) Math.PI);
                    this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ,
                            (float) Math.PI);
                    this.bipedRightArm.rotateAngleZ = MathHelperNew.lerp(f2, this.bipedRightArm.rotateAngleZ,
                            (float) Math.PI);
                }
                this.bipedLeftLeg.rotateAngleX += (0.3F * net.minecraft.util.math.MathHelper.cos(limbSwing * 0.33333334F + (float) Math.PI)
                        - this.bipedLeftLeg.rotateAngleX) * this.swimAnimation;
                this.bipedRightLeg.rotateAngleX += (0.3F * net.minecraft.util.math.MathHelper.cos(limbSwing * 0.33333334F)
                        - this.bipedRightLeg.rotateAngleX) * this.swimAnimation;

                this.bipedRightArm.rotationPointX += WEAPON_ARM_SPREAD * 0.5f * this.swimAnimation;
                this.bipedLeftArm.rotationPointX -= WEAPON_ARM_SPREAD * 0.5f * this.swimAnimation;
            }
        }

        net.minecraft.item.ItemStack activeStack = player.getActiveItemStack();
        boolean isConsumingItem = player.isHandActive() && !activeStack.isEmpty() 
                && (activeStack.getItemUseAction() == EnumAction.EAT 
                 || activeStack.getItemUseAction() == EnumAction.DRINK);

        if ((ap.isPlaying() || ap.getWeight() > 0f) && !isConsumingItem) {
            if (entityIn.isSneaking()) {
                this.bipedRightArm.rotationPointY -= 3.0F;
                this.bipedLeftArm.rotationPointY -= 3.0F;
                this.bipedRightLeg.rotationPointY -= 3.0F;
                this.bipedLeftLeg.rotationPointY -= 3.0F;
                this.bipedBody.rotationPointY -= 3.0F;
                this.bipedHead.rotationPointY -= 3.0F;
            }

            ModelBiped model = (ModelBiped) (Object) this;

            applyBone(this.bipedRightLeg, AnimationApplicator.getOverlayForBone(this.bipedRightLeg, model), ap, "rightLeg", pt);
            applyBone(this.bipedLeftLeg, AnimationApplicator.getOverlayForBone(this.bipedLeftLeg, model), ap, "leftLeg", pt);
            applyBone(this.bipedBody, AnimationApplicator.getOverlayForBone(this.bipedBody, model), ap, "torso", pt);

            String animName = ap.getCurrentAnimationName();
            String prevAnimName = ap.getPrevAnimationName();
            String actionName = ap.getCurrentActionName();
            String fadeActionName = ap.getFadeActionName();
            boolean currBow = animName != null && animName.contains("bow");
            boolean prevBow = prevAnimName != null && prevAnimName.contains("bow");

            float bowWeight = 0f;
            float baseW = ap.getPrevWeight() + (ap.getWeight() - ap.getPrevWeight()) * pt;
            float crossW = ap.getPrevCrossfadeWeight() + (ap.getCrossfadeWeight() - ap.getPrevCrossfadeWeight()) * pt;

            if (currBow && prevBow) {
                bowWeight = baseW;
            } else if (currBow) {
                bowWeight = baseW * (1f - crossW);
            } else if (prevBow) {
                bowWeight = baseW * crossW;
            }

            // Determine if vanilla item-use arm should take priority over our animation.
            boolean hasCustomItemAnim = animName != null && (
                    animName.contains("eating") || animName.contains("bow") ||
                            animName.contains("shield") || animName.contains("reload") ||
                            animName.contains("aim") || animName.contains("hold"));
            boolean isGenericItemUse = player.isHandActive() && !hasCustomItemAnim && !ap.hasActionWeight();

            // Capture vanilla head pitch (camera up/down) early so we can apply it to the arms
            float vanillaHeadPitch = this.bipedHead.rotateAngleX;
            boolean currRoll = animName != null && animName.contains("roll");
            boolean prevRoll = prevAnimName != null && prevAnimName.contains("roll");
            
            boolean shouldPitchArms = false;
            boolean skipRightArm = false;
            boolean skipLeftArm = false;

            // --- ANIMATION PRIORITY LOGIC ---
            // We want the arms to track the head when aiming or holding a weapon,
            // but NOT during sprint/run, reload, roll, or crawling.
            boolean isMWCWeapon = player.getHeldItemMainhand().getItem() instanceof Weapon;
            boolean hasWeaponAnim = (animName != null && (animName.startsWith("pistol_") || animName.startsWith("rifle_")))
                                 || (actionName != null && (actionName.startsWith("pistol_") || actionName.startsWith("rifle_")))
                                 || (fadeActionName != null && (fadeActionName.startsWith("pistol_") || fadeActionName.startsWith("rifle_")));
            boolean isHoldingWeapon = isMWCWeapon || hasWeaponAnim || currBow || prevBow;

            float armPitchWeight = ap.getArmPitchTrackingWeight(pt, isHoldingWeapon);

            float aimWeight = 0.0f;
            if (actionName != null && actionName.contains("aim")) {
                aimWeight = ap.getActionWeight();
            } else if (fadeActionName != null && fadeActionName.contains("aim")) {
                aimWeight = ap.getFadeWeight();
            }

            // Apply Arm Bones
            boolean disableRightArmAnim = false;
            boolean disableLeftArmAnim = false;

            if (this.swingProgress > 0.0F && !isMWCWeapon) {
                EnumHandSide swingingHandSide = (player.swingingHand == net.minecraft.util.EnumHand.OFF_HAND) ? this.getMainHand(entityIn).opposite() : this.getMainHand(entityIn);
                if (swingingHandSide == net.minecraft.util.EnumHandSide.RIGHT) {
                    disableRightArmAnim = true;
                } else {
                    disableLeftArmAnim = true;
                }
            }

            if (isGenericItemUse) {
                disableRightArmAnim = true;
                disableLeftArmAnim = true;
            }

            if (!disableRightArmAnim) {
                applyBone(this.bipedRightArm, AnimationApplicator.getOverlayForBone(this.bipedRightArm, model), ap, "rightArm", pt);
            }
            if (!disableLeftArmAnim) {
                applyBone(this.bipedLeftArm, AnimationApplicator.getOverlayForBone(this.bipedLeftArm, model), ap, "leftArm", pt);
            }

            // Torch Arm Raising
            TorchAnimationHandler.AnimState torchState = TorchAnimationHandler.getState(player);
            if (torchState != null) {
                float torchWeightRight = torchState.prevRight + (torchState.right - torchState.prevRight) * pt;
                float torchWeightLeft = torchState.prevLeft + (torchState.left - torchState.prevLeft) * pt;

                boolean isCrawlingOrRolling = (actionName != null && (actionName.contains("roll") || actionName.contains("lie")))
                        || (fadeActionName != null && (fadeActionName.contains("roll") || fadeActionName.contains("lie")))
                        || (animName != null && (animName.contains("lie") || animName.contains("roll")))
                        || currRoll || prevRoll;

                if (!isCrawlingOrRolling) {
                    float torchRotX = -1.35F + vanillaHeadPitch * 0.6F;

                    if (torchWeightRight > 0.001f && !disableRightArmAnim && !isHoldingWeapon) {
                        this.bipedRightArm.rotateAngleX = shortestAngleLerp(this.bipedRightArm.rotateAngleX, torchRotX, torchWeightRight);
                        this.bipedRightArm.rotateAngleY = shortestAngleLerp(this.bipedRightArm.rotateAngleY, -0.15F, torchWeightRight);
                        this.bipedRightArm.rotateAngleZ = shortestAngleLerp(this.bipedRightArm.rotateAngleZ, 0.05F, torchWeightRight);
                    }

                    if (torchWeightLeft > 0.001f && !disableLeftArmAnim && !(isHoldingWeapon && actionName != null && !actionName.contains("pistol"))) {
                        this.bipedLeftArm.rotateAngleX = shortestAngleLerp(this.bipedLeftArm.rotateAngleX, torchRotX, torchWeightLeft);
                        this.bipedLeftArm.rotateAngleY = shortestAngleLerp(this.bipedLeftArm.rotateAngleY, 0.15F, torchWeightLeft);
                        this.bipedLeftArm.rotateAngleZ = shortestAngleLerp(this.bipedLeftArm.rotateAngleZ, -0.05F, torchWeightLeft);
                    }
                }
            }

            // Apply Head Bones
            if (!isGenericItemUse) {
                float headX = this.bipedHead.rotateAngleX;
                float headY = this.bipedHead.rotateAngleY;
                float headZ = this.bipedHead.rotateAngleZ;
                this.bipedHead.rotateAngleX = 0;
                this.bipedHead.rotateAngleY = 0;
                this.bipedHead.rotateAngleZ = 0;
                applyBone(this.bipedHead, AnimationApplicator.getOverlayForBone(this.bipedHead, model), ap, "head", pt);
                
                // Add vanilla tracking back
                this.bipedHead.rotateAngleX += headX;
                this.bipedHead.rotateAngleY += headY;
                this.bipedHead.rotateAngleZ += headZ;
            }

            // Pitch Arms if required (smoothly scaled during crossfade)
            if (armPitchWeight > 0.0f) {
                if (!skipRightArm) this.bipedRightArm.rotateAngleX += vanillaHeadPitch * armPitchWeight;
                if (!skipLeftArm)  this.bipedLeftArm.rotateAngleX += vanillaHeadPitch * armPitchWeight;
            }

            // Sync hat
            if (this.bipedHeadwear != null) {
                this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
                this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
                this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
            }
        }
    }
}
