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

    // ──────────────────────────────────────────────────────────────────────────
    // UNIQUE (Переменные и методы из Aqua Acrobatics)
    // ──────────────────────────────────────────────────────────────────────────
    @Unique
    public float swimAnimation;
    @Unique
    public float mwccfSwimAnimation; // Истинное значение swimAnimation от AA (до обнуления)
    @Unique
    private static final float WEAPON_ARM_SPREAD = 1.0f; // На сколько раздвигать руки при удержании оружия MWC

    @Override
    public void setSwimAnimation(float swimAnimation) {
        this.swimAnimation = swimAnimation;
    }

    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entitylivingbaseIn, float limbSwing,
            float limbSwingAmount, float partialTickTime) {
        if (entitylivingbaseIn instanceof IPlayerResizeable) {
            this.mwccfSwimAnimation = ((IPlayerResizeable) entitylivingbaseIn).getSwimAnimation(partialTickTime);
            this.swimAnimation = this.mwccfSwimAnimation;
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

    // ──────────────────────────────────────────────────────────────────────────
    // ИНЖЕКЦИЯ 0: Восстановление базовых смещений перед рендером
    // ──────────────────────────────────────────────────────────────────────────
    @Inject(method = "setRotationAngles", at = @At("HEAD"), cancellable = true)
    public void resetRotationPoints(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!(entityIn instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) entityIn;
        Minecraft mc = Minecraft.getMinecraft();

        // Silence AquaAcrobatics' hardcoded limb stroke logic (setRotationAnglesPost).
        // It cancels setRotationAngles and breaks our custom JSON crawling/swimming animations.
        // We saved the real value in mwccfSwimAnimation before zeroing.
        this.swimAnimation = 0.0f;
        if ((Object) this instanceof IModelBipedSwimming) {
            ((IModelBipedSwimming) (Object) this).setSwimAnimation(0.0f);
        }

        ModelBiped mainBiped = null;
        net.minecraft.client.renderer.entity.RenderManager rm = mc.getRenderManager();
        net.minecraft.client.renderer.entity.Render<?> renderer = rm.getEntityRenderObject(player);
        if (renderer instanceof net.minecraft.client.renderer.entity.RenderLivingBase) {
            ModelBase mainModel = ((net.minecraft.client.renderer.entity.RenderLivingBase<?>) renderer).getMainModel();
            if (mainModel instanceof ModelBiped) {
                mainBiped = (ModelBiped) mainModel;
            }
        }

        // --- БРОНЯ: Идеальная синхронизация ---
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

            // Если это ModelPlayer, синхронизируем слои (рукава, штанины и т.д.)
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

        // ЖЕСТКО сбрасываем ВСЕ координаты в ванильные дефолты перед кадром.
        // Это предотвращает ЛЮБОЕ накопление (улетание в небо), если какой-то мод
        // переопределил setRotationAngles и не вызвал super (ванильный сброс isSneak).
        boolean isSneak = entityIn.isSneaking() && !efw.util.RenderContext.isRenderingPlayerInSevenScreen;

        efw.animation.AnimationPlayer ap = efw.util.RenderContext.isRenderingPlayerInSevenScreen
                ? efw.animation.AnimationRegistry.getSevenScreenPlayer()
                : efw.animation.AnimationRegistry.getPlayer(player);
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

    // ──────────────────────────────────────────────────────────────────────────
    // ИНЖЕКЦИЯ 1: Aqua Acrobatics (Наклон головы в полете/плавании)
    // ──────────────────────────────────────────────────────────────────────────
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V"))
    public void redirectSetRotationAngles(ModelBiped modelBiped, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            boolean isElytra = ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
            if (!isElytra) {
                // Do NOT feed back this.bipedHead.rotateAngleX into headPitch!
                // It causes a recursive feedback loop. Swimming head tilting is smoothly handled in postSetRotationAngles.
                headPitch = ((EntityLivingBase) entityIn).rotationPitch;
            }
        }
        if (entityIn != null) {
            modelBiped.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor,
                    entityIn);
        }
    }



    // ──────────────────────────────────────────────────────────────────────────
    // ИНЖЕКЦИЯ 4: Отключение ванильной анимации взмаха, если играет наша
    // ──────────────────────────────────────────────────────────────────────────
    @Inject(method = "setRotationAngles", at = @At("HEAD"))
    public void mwccf$onSetRotationAnglesHead(float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (entityIn instanceof EntityPlayer) {
            efw.animation.AnimationPlayer ap = efw.animation.AnimationRegistry.getPlayer((EntityPlayer) entityIn);
            if (ap != null && ap.hasActionWeight()) {
                // ПОЛНОСТЬЮ ОТКЛЮЧАЕМ ванильную анимацию взмаха (swingProgress),
                // если проигрывается наша экшн-анимация. Иначе они конфликтуют (смешиваются)
                // и вызывают подергивание (дерганные рывки) при ломании блоков или спаме.
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
    // ИНЖЕКЦИЯ 5: Плавание, Ползание, Сглаживание и Факел (Всё в одном!)
    // ──────────────────────────────────────────────────────────────────────────
    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void applyCustomAnimations(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!(entityIn instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) entityIn;
        Minecraft mc = Minecraft.getMinecraft();

        // EAT/DRINK — применяем анимацию еды/питья как fallback.
        // Если AquaAcrobatics отработал (ci.cancel()) — этот RETURN injection может не запуститься.
        // Если не отработал (ванильный return) — мы применяем анимацию руки сами, по той же формуле.
        if (player.isHandActive()) {
            net.minecraft.item.ItemStack cs = player.getActiveItemStack();
            if (!cs.isEmpty()) {
                net.minecraft.item.EnumAction ca = cs.getItemUseAction();
                if (ca == EnumAction.EAT || ca == EnumAction.DRINK) {
                    int itemInUseCount = player.getItemInUseCount();
                    int maxDuration = cs.getMaxItemUseDuration();
                    if (itemInUseCount > 0 && maxDuration > 0) {
                        // Воспроизводим формулу AquaAcrobatics
                        float fracTick = ageInTicks - (float)((int) ageInTicks);
                        float animCount = (float) itemInUseCount - fracTick + 1.0f;
                        float useRatio = animCount / (float) maxDuration;
                        // Ограничиваем: если useRatio > 1 (первый тик), f18 < 0 — не трогаем
                        if (useRatio <= 1.0f) {
                            float f18 = 1.0f - (float) Math.pow((double) useRatio, 27.0);
                            if (useRatio < 0.8f) {
                                f18 += 0.1f * Math.abs(net.minecraft.util.math.MathHelper.cos(
                                        animCount * (float) Math.PI / 4.0f));
                            }
                            f18 = Math.min(1.5f, f18);
                            if (f18 > 0.001f) {
                                // Определяем основную руку
                                boolean isRightHanded = player.getPrimaryHand() == net.minecraft.util.EnumHandSide.RIGHT;
                                net.minecraft.client.model.ModelRenderer armPrimary =
                                        isRightHanded ? this.bipedRightArm : this.bipedLeftArm;
                                // Вычисляем базовый угол (как в ванили) и применяем подъём
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
                                // РџРѕРІРѕСЂРѕС‚ РІРїРµСЂС‘Рґ (yaw в†’ 0, С‚.Ре. РїСЂСЏРјРѕ): lerp РѕС‚ С‚РµРєСѓС‰РµРіРѕ Рє 0
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

        // --- РџР Р˜РњР•РќР•РќР•РНИЕ РђРќР˜РњРђР¦Р˜Р™ Рљ РђР РњРђРўРЈР Р• Р˜Р“Р РћРљРђ MWCCF ---
        efw.animation.AnimationPlayer ap = efw.util.RenderContext.isRenderingPlayerInSevenScreen
                ? efw.animation.AnimationRegistry.getSevenScreenPlayer()
                : efw.animation.AnimationRegistry.getPlayer(player);

        // --- Р§РђРЎРўР¬ A: AQUA ACROBATICS (РЎР±СЂРѕСЃ Рё Р»РѕРіРёРєР° РїР»Р°РІР°РЅРёСЏ) ---
        // --- ЧАСТЬ A: AQUA ACROBATICS (Сброс и логика плавания / ползания) ---
        String currentAnimName = ap != null ? ap.getCurrentAnimationName() : null;
        boolean hasCustomWaterAnim = ap != null && ap.isPlaying() && (
                "swimming".equals(currentAnimName) ||
                "up_in_water".equals(currentAnimName) ||
                "backwards_in_water".equals(currentAnimName) ||
                "forward_in_water".equals(currentAnimName) ||
                "idle_in_water".equals(currentAnimName)
        );

        boolean isAASwimming = false;
        if (entityIn instanceof com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) {
            com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable res = (com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) entityIn;
            isAASwimming = (entityIn != null && (entityIn.isInWater() || entityIn.isInsideOfMaterial(net.minecraft.block.material.Material.WATER)))
                    && (res.isActuallySwimming() || res.getPose() == com.fuzs.aquaacrobatics.entity.Pose.SWIMMING);
        }

        boolean isWaterAnim = hasCustomWaterAnim || (entityIn != null && entityIn.isInWater());

        String currentAnimForCrawlCheck = currentAnimName;
        boolean isCrawlingAnim = !isWaterAnim && (
                (currentAnimForCrawlCheck != null && (currentAnimForCrawlCheck.contains("lie") || currentAnimForCrawlCheck.contains("crawl")))
                || efw.AnimationTickHandler.isPlayerCrawling(player)
        );

        // Reset vanilla/AA bone transformations so JSON animations apply cleanly
        if (this.swimAnimation > 0.0F || isCrawlingAnim || hasCustomWaterAnim || (isAASwimming && entityIn != null && entityIn.isInWater())) {
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
            this.bipedRightLeg.rotationPointZ = 0.0F;
            this.bipedLeftLeg.rotationPointZ = 0.0F;
            this.bipedRightLeg.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotateAngleY = 0.0F;
            this.bipedRightLeg.rotateAngleZ = 0.0F;
            this.bipedLeftLeg.rotateAngleX = 0.0F;
            this.bipedLeftLeg.rotateAngleY = 0.0F;
            this.bipedLeftLeg.rotateAngleZ = 0.0F;

            this.bipedBody.rotationPointY = 0.0F;
            this.bipedBody.rotationPointZ = 0.0F;
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedBody.rotateAngleY = 0.0F;
            this.bipedBody.rotateAngleZ = 0.0F;

            this.bipedHead.rotationPointY = 0.0F;
            this.bipedHead.rotationPointZ = 0.0F;

            this.bipedRightArm.rotationPointY = 2.0F;
            this.bipedRightArm.rotationPointZ = 0.0F;
            this.bipedRightArm.rotateAngleX = 0.0F;
            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;

            this.bipedLeftArm.rotationPointY = 2.0F;
            this.bipedLeftArm.rotationPointZ = 0.0F;
            this.bipedLeftArm.rotateAngleX = 0.0F;
            this.bipedLeftArm.rotateAngleY = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
        }

        if (this.mwccfSwimAnimation > 0.0F && !hasCustomWaterAnim && isAASwimming) {
            float time = limbSwing * 0.6662F;
            EnumHandSide handside = this.getMainHand(entityIn);
            float f2 = handside == EnumHandSide.RIGHT && this.swingProgress > 0.0F ? 0.0F : this.mwccfSwimAnimation;
            float f3 = handside == EnumHandSide.LEFT && this.swingProgress > 0.0F ? 0.0F : this.mwccfSwimAnimation;

            float basePY = 2.0F;
            float basePZ = 0.0F;

            if (!entityIn.isInWater()) {
                // Crawling logic is handled by JSON animations now.
            } else {
                this.bipedHead.rotateAngleX = this.rotLerpRad(f2, this.bipedHead.rotateAngleX, -0.7853982F);
                this.bipedHead.rotateAngleY = this.rotLerpRad(f2, this.bipedHead.rotateAngleY, 0.0F);
                this.bipedHead.rotateAngleZ = this.rotLerpRad(f2, this.bipedHead.rotateAngleZ, 0.0F);
                this.bipedRightArm.rotateAngleX = this.rotLerpRad(f2, this.bipedRightArm.rotateAngleX, 0.0F);
                this.bipedRightArm.rotateAngleY = this.rotLerpRad(f2, this.bipedRightArm.rotateAngleY, 0.0F);
                this.bipedRightArm.rotateAngleZ = this.rotLerpRad(f2, this.bipedRightArm.rotateAngleZ, 0.0F);
                this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, 0.0F);
                this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, 0.0F);
                this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, 0.0F);
                this.bipedLeftLeg.rotateAngleX = this.rotLerpRad(this.swimAnimation, this.bipedLeftLeg.rotateAngleX, 0.0F);
                this.bipedLeftLeg.rotateAngleY = this.rotLerpRad(this.swimAnimation, this.bipedLeftLeg.rotateAngleY, 0.0F);
                this.bipedLeftLeg.rotateAngleZ = this.rotLerpRad(this.swimAnimation, this.bipedLeftLeg.rotateAngleZ, 0.0F);
                this.bipedRightLeg.rotateAngleX = this.rotLerpRad(this.swimAnimation, this.bipedRightLeg.rotateAngleX, 0.0F);
                this.bipedRightLeg.rotateAngleY = this.rotLerpRad(this.swimAnimation, this.bipedRightLeg.rotateAngleY, 0.0F);
                this.bipedRightLeg.rotateAngleZ = this.rotLerpRad(this.swimAnimation, this.bipedRightLeg.rotateAngleZ, 0.0F);
                this.bipedBody.rotateAngleX = this.rotLerpRad(this.swimAnimation, this.bipedBody.rotateAngleX, 0.0F);
                this.bipedBody.rotateAngleY = this.rotLerpRad(this.swimAnimation, this.bipedBody.rotateAngleY, 0.0F);
                this.bipedBody.rotateAngleZ = this.rotLerpRad(this.swimAnimation, this.bipedBody.rotateAngleZ, 0.0F);

                if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).isSpectator()) {
                    this.bipedHead.rotateAngleX = 0.0F;
                    this.bipedHead.rotateAngleY = 0.0F;
                    this.bipedHead.rotateAngleZ = 0.0F;
                    this.bipedRightArm.rotateAngleX = 0.0F;
                    this.bipedRightArm.rotateAngleY = 0.0F;
                    this.bipedRightArm.rotateAngleZ = 0.0F;
                    this.bipedLeftArm.rotateAngleX = 0.0F;
                    this.bipedLeftArm.rotateAngleY = 0.0F;
                    this.bipedLeftArm.rotateAngleZ = 0.0F;
                    this.bipedLeftLeg.rotateAngleX = 0.0F;
                    this.bipedLeftLeg.rotateAngleY = 0.0F;
                    this.bipedLeftLeg.rotateAngleZ = 0.0F;
                    this.bipedRightLeg.rotateAngleX = 0.0F;
                    this.bipedRightLeg.rotateAngleY = 0.0F;
                    this.bipedRightLeg.rotateAngleZ = 0.0F;
                    this.bipedBody.rotateAngleX = 0.0F;
                    this.bipedBody.rotateAngleY = 0.0F;
                    this.bipedBody.rotateAngleZ = 0.0F;
                } else {
                    float f5 = limbSwingAmount;
                    float f6 = 1.0F;

                    if (f5 > 0.2F) {
                        f6 = 1.0F - (f5 - 0.2F) / 0.8F;
                    }

                    this.bipedHead.rotateAngleX = this.rotLerpRad(f2, this.bipedHead.rotateAngleX,
                            -0.7853982F * (1.0F - f6) + -1.2566371F * f6);
                    this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX,
                            -1.0471976F * (1.0F - f6) + -2.3561945F * f6);
                    this.bipedRightArm.rotateAngleX = this.rotLerpRad(f2, this.bipedRightArm.rotateAngleX,
                            -1.0471976F * (1.0F - f6) + -2.3561945F * f6);
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

        // Detect crawling: lie/crawl animations already handle body posture, so we must
        // NOT apply the vanilla sneak Y-offset on top of them (causes head/arm tilt bugs).
        // (isCrawlingAnim was already computed above)

        if ((ap.isPlaying() || ap.getWeight() > 0f) && !isConsumingItem) {
            String animName = ap.getCurrentAnimationName();
            String prevAnimName = ap.getPrevAnimationName();
            String actionName = ap.getCurrentActionName();
            String fadeActionName = ap.getFadeActionName();
            boolean currBow = animName != null && animName.contains("bow");
            boolean prevBow = prevAnimName != null && prevAnimName.contains("bow");

            boolean isMWCWeapon = !efw.util.RenderContext.isRenderingPlayerInSevenScreen && player.getHeldItemMainhand().getItem() instanceof Weapon;
            boolean hasWeaponAnim = (animName != null && (animName.startsWith("pistol_") || animName.startsWith("rifle_")))
                                 || (actionName != null && (actionName.startsWith("pistol_") || actionName.startsWith("rifle_")))
                                 || (fadeActionName != null && (fadeActionName.startsWith("pistol_") || fadeActionName.startsWith("rifle_")));
            boolean isHoldingWeapon = isMWCWeapon || hasWeaponAnim || currBow || prevBow;
            ap.isHoldingWeapon = isHoldingWeapon;
            ap.setPlayer(player);

            float ww = ap.getWeaponSneakWeight(pt);
            if (!efw.util.RenderContext.isRenderingPlayerInSevenScreen && !isCrawlingAnim) {
                if (ww > 0.001f) {
                    // --- WEAPON SNEAK SYSTEM (from backup) ---
                    // Arms and head smoothly lower into crouch with proper weapon alignment
                    float sw = ap.getSneakOffsetWeight(pt);
                    if (entityIn.isSneaking() || sw > 0.001f) {
                        float targetBodyY = -3.0F;
                        float targetLegY = 9.0F;
                        float targetHeadY = -1.0F;
                        float targetRightArmY = 1.0F;
                        float targetLeftArmY = 1.0F;

                        float w = sw * ww;
                        this.bipedRightLeg.rotationPointY = 12.0F * (1.0f - w) + targetLegY * w;
                        this.bipedLeftLeg.rotationPointY = 12.0F * (1.0f - w) + targetLegY * w;
                        this.bipedRightLeg.rotationPointZ = 0.0F * w + this.bipedRightLeg.rotationPointZ * (1.0f - w);
                        this.bipedLeftLeg.rotationPointZ = 0.0F * w + this.bipedLeftLeg.rotationPointZ * (1.0f - w);

                        this.bipedBody.rotationPointY = 0.0F * (1.0f - w) + targetBodyY * w;
                        this.bipedHead.rotationPointY = 0.0F * (1.0f - w) + targetHeadY * w;
                        this.bipedRightArm.rotationPointY = 2.0F * (1.0f - w) + targetRightArmY * w;
                        this.bipedLeftArm.rotationPointY = 2.0F * (1.0f - w) + targetLeftArmY * w;
                    }
                } else {
                    // --- NON-WEAPON SNEAK (exact Git baseline) ---
                    if (entityIn.isSneaking()) {
                        this.bipedRightLeg.rotationPointY -= 3.0F;
                        this.bipedLeftLeg.rotationPointY -= 3.0F;
                        this.bipedBody.rotationPointY -= 3.0F;
                        this.bipedHead.rotationPointY -= 3.0F;
                    }
                }
            }

            ModelBiped model = (ModelBiped) (Object) this;

            applyBone(this.bipedRightLeg, AnimationApplicator.getOverlayForBone(this.bipedRightLeg, model), ap, "rightLeg", pt);
            applyBone(this.bipedLeftLeg, AnimationApplicator.getOverlayForBone(this.bipedLeftLeg, model), ap, "leftLeg", pt);
            applyBone(this.bipedBody, AnimationApplicator.getOverlayForBone(this.bipedBody, model), ap, "torso", pt);

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
                            animName.contains("aim") || animName.contains("hold") ||
                            animName.contains("lie") || animName.contains("crawl"));
            boolean isGenericItemUse = player.isHandActive() && !hasCustomItemAnim && !ap.hasActionWeight();

            // Capture vanilla head pitch (camera up/down) early so we can apply it to the arms
            float vanillaHeadPitch = this.bipedHead.rotateAngleX;
            boolean currRoll = animName != null && animName.contains("roll");
            boolean prevRoll = prevAnimName != null && prevAnimName.contains("roll");
            
            boolean shouldPitchArms = false;
            boolean skipRightArm = false;
            boolean skipLeftArm = false;

            boolean disableArmPitch = isCrawlingAnim || player.isInWater();
            float armPitchWeight = disableArmPitch ? 0.0f : ap.getArmPitchTrackingWeight(pt, isHoldingWeapon);

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
                if (ww <= 0.001f && entityIn.isSneaking() && !efw.util.RenderContext.isRenderingPlayerInSevenScreen && !isCrawlingAnim) {
                    this.bipedRightArm.rotationPointY -= 3.0F;
                }
                applyBone(this.bipedRightArm, AnimationApplicator.getOverlayForBone(this.bipedRightArm, model), ap, "rightArm", pt);
            }
            if (!disableLeftArmAnim) {
                if (ww <= 0.001f && entityIn.isSneaking() && !efw.util.RenderContext.isRenderingPlayerInSevenScreen && !isCrawlingAnim) {
                    this.bipedLeftArm.rotationPointY -= 3.0F;
                }
                applyBone(this.bipedLeftArm, AnimationApplicator.getOverlayForBone(this.bipedLeftArm, model), ap, "leftArm", pt);
            }

            // Torch Arm Raising
            TorchAnimationHandler.AnimState torchState = TorchAnimationHandler.getState(player);
            if (torchState != null) {
                float torchWeightRight = torchState.prevRight + (torchState.right - torchState.prevRight) * pt;
                float torchWeightLeft = torchState.prevLeft + (torchState.left - torchState.prevLeft) * pt;

                boolean isCrawlingOrRolling = (actionName != null && (actionName.contains("roll") || actionName.contains("lie") || actionName.contains("crawl")))
                        || (fadeActionName != null && (fadeActionName.contains("roll") || fadeActionName.contains("lie") || fadeActionName.contains("crawl")))
                        || (animName != null && (animName.contains("lie") || animName.contains("roll") || animName.contains("crawl")))
                        || isCrawlingAnim || isWaterAnim || currRoll || prevRoll || ap.isRollPlaying();

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

            // Pitch Arms when holding a weapon (from backup system: smoothly scaled and blended after roll)
            if (armPitchWeight > 0.001f) {
                float lookWeight = ap.getRollLookWeight(pt);
                float effectiveArmPitch = vanillaHeadPitch * armPitchWeight * lookWeight;
                if (!skipRightArm) this.bipedRightArm.rotateAngleX += effectiveArmPitch;
                if (!skipLeftArm)  this.bipedLeftArm.rotateAngleX += effectiveArmPitch;
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
                
                float lookWeight = ap.getRollLookWeight(pt);
                float swimWeight = ap != null ? ap.getSwimHeadWeight(pt) : 0.0f;

                float baseHeadX = this.bipedHead.rotateAngleX;
                float baseHeadY = this.bipedHead.rotateAngleY;
                float baseHeadZ = this.bipedHead.rotateAngleZ;

                float targetX;
                float targetY;
                float targetZ;

                if (isCrawlingAnim) {
                    // Like TaCZ (anim 1.20.1): when crawling, the head does NOT pitch up/down into the ground.
                    // The only tracking reaction to aiming is a subtle roll/tilt to the left and right.
                    targetX = baseHeadX;
                    targetY = baseHeadY;
                    float tilt = Math.max(-0.25f, Math.min(0.25f, headY));
                    targetZ = baseHeadZ + tilt * lookWeight;
                } else {
                    // Add vanilla tracking back
                    targetX = baseHeadX + headX * lookWeight;
                    targetY = baseHeadY + headY * lookWeight;
                    targetZ = baseHeadZ + headZ * lookWeight;
                }

                if (swimWeight > 0.001f) {
                    // In swimming, body is tilted horizontally by player pitch in RenderPlayer.
                    // Face should look forward along the swimming vector, so head is raised ~-45° (-0.785 rad).
                    // Smoothly blend between free look tracking and the locked forward angle.
                    float lockedX = -0.7853982F;
                    this.bipedHead.rotateAngleX = shortestAngleLerp(targetX, lockedX, swimWeight);
                    this.bipedHead.rotateAngleY = shortestAngleLerp(targetY, 0.0F, swimWeight);
                    this.bipedHead.rotateAngleZ = shortestAngleLerp(targetZ, 0.0F, swimWeight);
                } else {
                    this.bipedHead.rotateAngleX = targetX;
                    this.bipedHead.rotateAngleY = targetY;
                    this.bipedHead.rotateAngleZ = targetZ;
                }
            }

            // Sync hat (both angles and pivot points so hat layer never separates)
            if (this.bipedHeadwear != null) {
                this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
                this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
                this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
                this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
                this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
                this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
            }
        }
    }
}
