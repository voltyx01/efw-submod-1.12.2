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

import java.util.Map;
import java.util.WeakHashMap;

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

    public static final float HEAD_BASE_ROT_X = (float) Math.toRadians(12.5);
    public static final float HEAD_BASE_ROT_Y = 0.0F;
    public static final float HEAD_BASE_ROT_Z = (float) Math.toRadians(-17.5);

    public static final float LHAND_BASE_ROT_X = 0.0F;
    public static final float LHAND_BASE_ROT_Y = 0.0F;
    public static final float LHAND_BASE_ROT_Z = 0.0F;

    public static final float RHAND_BASE_ROT_X = 0.0F;
    public static final float RHAND_BASE_ROT_Y = 0.0F;
    public static final float RHAND_BASE_ROT_Z = 0.0F;

    public static final float BONE_BASE_ROT_X = (float) Math.toRadians(-100.0);
    public static final float BONE_BASE_ROT_Y = 0.0F;
    public static final float BONE_BASE_ROT_Z = 0.0F;

    public static final float LEGR_BASE_ROT_X = (float) Math.toRadians(22.5);
    public static final float LEGR_BASE_ROT_Y = (float) Math.toRadians(22.5);
    public static final float LEGR_BASE_ROT_Z = 0.0F;

    public static final float LEGL_BASE_ROT_X = (float) Math.toRadians(20.0);
    public static final float LEGL_BASE_ROT_Y = (float) Math.toRadians(-22.5);
    public static final float LEGL_BASE_ROT_Z = 0.0F;

    private static DollScaleModelRenderer scaleBone2;
    private static DollScaleModelRenderer scaleSlimBone2;

    private static ModelRenderer headBone;
    private static ModelRenderer headSlimBone;
    private static ModelRenderer lHandBone;
    private static ModelRenderer lHandSlimBone;
    private static ModelRenderer rHandBone;
    private static ModelRenderer rHandSlimBone;
    private static ModelRenderer legsBone;
    private static ModelRenderer legsSlimBone;
    private static ModelRenderer legrBone;
    private static ModelRenderer legrSlimBone;
    private static ModelRenderer leglBone;
    private static ModelRenderer leglSlimBone;

    private static final Map<EntityPlayer, PlayerDollPhysics> PHYSICS_MAP = new WeakHashMap<>();

    public static class PlayerDollPhysics {
        public int lastTick = -1;
        public double lastPosY;
        public float prevBounceY = 0.0F;
        public float currentBounceY = 0.0F;
        public float bounceVelocityY = 0.0F;

        public float prevInertiaPitch = 0.0F;
        public float currentInertiaPitch = 0.0F;
        public float inertiaPitchVel = 0.0F;

        public boolean wasOnGround = true;
        public float landingImpact = 0.0F;

        public void update(AbstractClientPlayer player) {
            if (lastTick == -1) {
                lastPosY = player.posY;
                wasOnGround = player.onGround;
                lastTick = player.ticksExisted;
                return;
            }

            double velY = player.posY - lastPosY;
            lastPosY = player.posY;

            boolean inAir = !player.onGround && !player.isInWater() && !player.capabilities.isFlying;
            if (!inAir && !wasOnGround) {
                // Игрок только что приземлился — кукла по инерции проседает вниз (+Y)
                float impact = (float) Math.min(1.4, Math.max(0.3, -velY * 2.2));
                landingImpact = impact;
            }
            wasOnGround = player.onGround;

            // Инерция вертикального перемещения:
            // При прыжке вверх (velY > 0) кукла отстает (тянется вниз +Y)
            // При падении вниз (velY < 0) кукла приподнимается (-Y)
            float targetY = (float) Math.max(-0.6, Math.min(1.1, velY * 1.5));

            // Легкое покачивание/подпрыгивание по Y при беге на земле
            if (player.onGround && player.limbSwingAmount > 0.05F) {
                float speedMult = player.isSprinting() ? 1.3F : 0.7F;
                float step = (float) -Math.abs(Math.sin(player.limbSwing * 0.50F)) * 0.30F * player.limbSwingAmount * speedMult;
                targetY += step;
            }

            if (landingImpact > 0.01F) {
                targetY += landingImpact;
                landingImpact *= 0.60F;
            }

            // Пружинный демпфер для плавной и упругой инерции
            prevBounceY = currentBounceY;
            float springK = 0.35F;
            float damping = 0.68F;
            bounceVelocityY += (targetY - currentBounceY) * springK;
            bounceVelocityY *= damping;
            currentBounceY += bounceVelocityY;

            // Угловая инерция для конечностей (ножки и ручки отклоняются при прыжке/приземлении)
            prevInertiaPitch = currentInertiaPitch;
            float targetPitch = (float) Math.max(-0.35, Math.min(0.45, velY * 0.7));
            inertiaPitchVel += (targetPitch - currentInertiaPitch) * 0.30F;
            inertiaPitchVel *= 0.70F;
            currentInertiaPitch += inertiaPitchVel;
        }
    }

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

            headBone = cachedModel.getBone("Head2");
            headSlimBone = cachedModel.getSlimBone("Head2");

            lHandBone = cachedModel.getBone("LHand");
            lHandSlimBone = cachedModel.getSlimBone("LHand");

            rHandBone = cachedModel.getBone("RHand");
            rHandSlimBone = cachedModel.getSlimBone("RHand");

            legsBone = cachedModel.getBone("bone");
            legsSlimBone = cachedModel.getSlimBone("bone");

            legrBone = cachedModel.getBone("legr");
            legrSlimBone = cachedModel.getSlimBone("legr");

            leglBone = cachedModel.getBone("legl");
            leglSlimBone = cachedModel.getSlimBone("legl");
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

    private static void setBoneRotation(ModelRenderer bone, ModelRenderer slimBone, float rotX, float rotY, float rotZ) {
        if (bone != null) {
            bone.rotateAngleX = rotX;
            bone.rotateAngleY = rotY;
            bone.rotateAngleZ = rotZ;
        }
        if (slimBone != null && slimBone != bone) {
            slimBone.rotateAngleX = rotX;
            slimBone.rotateAngleY = rotY;
            slimBone.rotateAngleZ = rotZ;
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
        // Но при открытом инвентаре/GUI отображаем её на модели персонажа
        if (!efw.util.RenderContext.isRenderingPlayerInGui &&
                player == Minecraft.getMinecraft().player &&
                Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            return;
        }

        if (!hasDoll(player)) return;

        // Если кукла взята в руки в реальной игре — перестаем рендерить её на теле (в GUI показываем)
        if (!efw.util.RenderContext.isRenderingPlayerInGui && isHoldingDoll(player)) return;

        String playerSkin = "slim".equals(player.getSkinType()) ? "slim" : "default";
        if (!playerSkin.equals(this.skinTypeKey)) return;

        GeoArmorModel model = getModel();
        if (model == null) return;

        PlayerDollPhysics physics = PHYSICS_MAP.computeIfAbsent(player, p -> new PlayerDollPhysics());
        if (physics.lastTick != player.ticksExisted) {
            physics.update(player);
            physics.lastTick = player.ticksExisted;
        }

        float bounceY = 0.0F;
        float inertiaPitch = 0.0F;
        if (!efw.util.RenderContext.isRenderingPlayerInGui) {
            bounceY = physics.prevBounceY + (physics.currentBounceY - physics.prevBounceY) * delta;
            inertiaPitch = physics.prevInertiaPitch + (physics.currentInertiaPitch - physics.prevInertiaPitch) * delta;
        }

        // Базовые углы костей куклы
        float headRotX = HEAD_BASE_ROT_X;
        float headRotY = HEAD_BASE_ROT_Y;
        float headRotZ = HEAD_BASE_ROT_Z;

        float lHandRotX = LHAND_BASE_ROT_X;
        float lHandRotY = LHAND_BASE_ROT_Y;
        float lHandRotZ = LHAND_BASE_ROT_Z;

        float rHandRotX = RHAND_BASE_ROT_X;
        float rHandRotY = RHAND_BASE_ROT_Y;
        float rHandRotZ = RHAND_BASE_ROT_Z;

        float boneRotX = BONE_BASE_ROT_X;
        float boneRotY = BONE_BASE_ROT_Y;
        float boneRotZ = BONE_BASE_ROT_Z;

        float legrRotX = LEGR_BASE_ROT_X;
        float legrRotY = LEGR_BASE_ROT_Y;
        float legrRotZ = LEGR_BASE_ROT_Z;

        float leglRotX = LEGL_BASE_ROT_X;
        float leglRotY = LEGL_BASE_ROT_Y;
        float leglRotZ = LEGL_BASE_ROT_Z;

        if (!efw.util.RenderContext.isRenderingPlayerInGui) {
            // Более мягкая скорость и частота цикла
            float walkCycle = limbSwing * 0.50F;
            float walkAmount = limbSwingAmount;

            float idleTime = (player.ticksExisted + delta) * 0.05F;

            // Качание головы (кивание вперед-назад, наклон вбок + реакция на прыжок/приземление)
            float headPitch = (float) Math.sin(walkCycle) * 0.11F * walkAmount + (float) Math.sin(idleTime) * 0.02F;
            float headRoll = (float) Math.cos(walkCycle * 0.5F) * 0.07F * walkAmount + (float) Math.cos(idleTime * 0.7F) * 0.015F;
            float headYaw = (float) Math.sin(walkCycle * 0.5F) * 0.05F * walkAmount;
            headPitch += inertiaPitch * 0.4F;

            headRotX += headPitch;
            headRotY += headRoll;
            headRotZ += headYaw;

            // Качание рук (свободно свисают с более деликатной амплитудой + инерция по Y)
            float armLX = (float) Math.sin(walkCycle) * 0.20F * walkAmount + (float) Math.sin(idleTime + 0.5F) * 0.015F + inertiaPitch * 0.5F;
            float armLY = (float) Math.cos(walkCycle * 0.5F) * 0.06F * walkAmount + (float) Math.cos(idleTime * 0.6F) * 0.01F;

            float armRX = (float) Math.sin(walkCycle + (float) Math.PI * 0.7F) * 0.18F * walkAmount + (float) Math.sin(idleTime + 1.8F) * 0.015F + inertiaPitch * 0.5F;
            float armRY = -(float) Math.cos(walkCycle * 0.5F) * 0.06F * walkAmount - (float) Math.cos(idleTime * 0.6F) * 0.01F;

            lHandRotX += armLX;
            lHandRotY += armLY;

            rHandRotX += armRX;
            rHandRotY += armRY;

            // Качание ножек (маятник bone + индивидуальные покачивания + инерция при прыжках)
            float bonePitch = (float) Math.sin(walkCycle) * 0.16F * walkAmount + (float) Math.sin(idleTime * 0.9F) * 0.02F + inertiaPitch * 0.7F;
            boneRotX += bonePitch;

            float legLX = (float) Math.sin(walkCycle - 0.4F) * 0.12F * walkAmount + (float) Math.sin(idleTime + 0.8F) * 0.015F;
            float legLY = (float) Math.cos(walkCycle * 0.5F) * 0.05F * walkAmount;

            float legRX = (float) Math.sin(walkCycle + 0.6F) * 0.12F * walkAmount + (float) Math.sin(idleTime + 2.0F) * 0.015F;
            float legRY = -(float) Math.cos(walkCycle * 0.5F) * 0.05F * walkAmount;

            leglRotX += legLX;
            leglRotY += legLY;

            legrRotX += legRX;
            legrRotY += legRY;
        }

        setBoneRotation(headBone, headSlimBone, headRotX, headRotY, headRotZ);
        setBoneRotation(lHandBone, lHandSlimBone, lHandRotX, lHandRotY, lHandRotZ);
        setBoneRotation(rHandBone, rHandSlimBone, rHandRotX, rHandRotY, rHandRotZ);
        setBoneRotation(legsBone, legsSlimBone, boneRotX, boneRotY, boneRotZ);
        setBoneRotation(legrBone, legrSlimBone, legrRotX, legrRotY, legrRotZ);
        setBoneRotation(leglBone, leglSlimBone, leglRotX, leglRotY, leglRotZ);

        com.voltyx.mwccf.render.doll.DollBodySettings.OffsetData offset =
                com.voltyx.mwccf.render.doll.DollBodySettings.getOffsetForPlayer(player);

        float posX = BASE_POS_X + (offset != null ? offset.posX : 0.0F);
        // Вся игрушка слегка подпрыгивает по Y при беге и более выразительно при прыжках/приземлении с пружинной инерцией
        float posY = BASE_POS_Y + (offset != null ? offset.posY : 0.0F) + bounceY;
        float posZ = BASE_POS_Z + (offset != null ? offset.posZ : 0.0F);
        float rotX = BASE_ROT_X + (offset != null ? (float) Math.toRadians(offset.rotX) : 0.0F);
        float rotY = BASE_ROT_Y + (offset != null ? (float) Math.toRadians(offset.rotY) : 0.0F);
        float rotZ = BASE_ROT_Z + (offset != null ? (float) Math.toRadians(offset.rotZ) : 0.0F);
        float dScale = (offset != null ? offset.scale : 1.0F);

        if (scaleBone2 != null) {
            scaleBone2.rotationPointX = posX;
            scaleBone2.rotationPointY = posY;
            scaleBone2.rotationPointZ = posZ;
            scaleBone2.rotateAngleX = rotX;
            scaleBone2.rotateAngleY = rotY;
            scaleBone2.rotateAngleZ = rotZ;
            scaleBone2.dollScale = dScale;
        }
        if (scaleSlimBone2 != null) {
            scaleSlimBone2.rotationPointX = posX;
            scaleSlimBone2.rotationPointY = posY;
            scaleSlimBone2.rotationPointZ = posZ;
            scaleSlimBone2.rotateAngleX = rotX;
            scaleSlimBone2.rotateAngleY = rotY;
            scaleSlimBone2.rotateAngleZ = rotZ;
            scaleSlimBone2.dollScale = dScale;
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

        GlStateManager.enableCull();
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
