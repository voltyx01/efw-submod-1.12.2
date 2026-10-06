package com.voltyx.mwccf.render.doll;

import efw.item.ItemDoll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

/**
 * Рендерер куклы Сайи от первого лица:
 * - Плавные переходы и убирание без резких скачков
 * - Блендинг поз между анимациями ("animation" -> "down")
 * - Настоящее динамическое освещение мира Minecraft (тени, свет факелов, день/ночь)
 * - Физическая инерция рук и головы куклы при движении игрока и поворотах мыши
 * - Интерактивный внутриигровой HUD настройки (F8 / INSERT)
 */
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class DollRenderer {

    // === Ресурсы ===
    private static final ResourceLocation GEO_LOC  = new ResourceLocation("mwccf", "geo/doll.geo.json");
    private static final ResourceLocation ANIM_LOC = new ResourceLocation("mwccf", "animations/doll.animation.json");
    private static final ResourceLocation TEX_LOC  = new ResourceLocation("mwccf", "textures/entity/doll.png");

    /** Стандартный MC scale: 1 Bedrock-пиксель = 1/16 блока. */
    private static final float RENDER_SCALE = 0.0625f;

    // === State ===
    private static float showProgress     = 0f;
    private static float prevShowProgress = 0f;

    private static float animTime         = 0f;
    private static float prevAnimTime     = 0f;
    private static float hideAnimTime     = 0f;
    private static float prevHideAnimTime = 0f;

    private static boolean isDollHeld = false;
    private static boolean isHiding   = false;
    private static boolean hintShown  = false;

    // === Физика инерции (sway / inertia) ===
    private static float lastYaw       = 0f;
    private static float lastPitch     = 0f;
    private static float swayYaw       = 0f;
    private static float prevSwayYaw   = 0f;
    private static float swayPitch     = 0f;
    private static float prevSwayPitch = 0f;
    private static float swayY         = 0f;
    private static float prevSwayY     = 0f;

    private static float swayYawVel    = 0f;
    private static float swayPitchVel  = 0f;
    private static float swayYVel      = 0f;
    private static float walkBob       = 0f;

    // === Покачивание в конце доставания (settling sway) ===
    private static float settleTime     = 0f;
    private static float prevSettleTime = 0f;

    // === Модель ===
    private static BedrockDollModel model = null;

    public static BedrockDollModel getModel() {
        if (model == null) {
            model = new BedrockDollModel();
            model.load(GEO_LOC, ANIM_LOC);
        }
        return model;
    }

    // =========================================================
    // Client tick
    // =========================================================

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        boolean holdingDoll = isDollHeldByPlayer(mc.player) && !isLyingOrCrawling(mc.player);
        prevShowProgress  = showProgress;
        prevAnimTime      = animTime;
        prevHideAnimTime  = hideAnimTime;
        prevSettleTime    = settleTime;

        BedrockDollModel m = getModel();

        if (holdingDoll) {
            if (!isDollHeld) {
                if (efw.init.EfwModSounds.DOLL_UP != null) {
                    mc.player.playSound(efw.init.EfwModSounds.DOLL_UP, 0.9F, 1.0F);
                }
            }

            if (!hintShown) {
                hintShown = true;
                mc.player.sendStatusMessage(new TextComponentString(
                    TextFormatting.GOLD + "[Doll] Нажмите " + TextFormatting.YELLOW + "F8" +
                    TextFormatting.GOLD + " или " + TextFormatting.YELLOW + "INSERT" +
                    TextFormatting.GOLD + " для настройки параметров куклы"), false);
            }

            isDollHeld   = true;
            isHiding     = false;
            hideAnimTime = 0f;

            showProgress = Math.min(1f, showProgress + DollSettings.appearSpeed);

            // Прогресс анимации "animation" (hold_on_last_frame)
            float len = m.getAnimationLength("animation");
            if (len <= 0f) len = 0.625f;
            animTime += (1f / 20f) * DollSettings.animSpeed;
            if (animTime > len) {
                animTime = len;
            }
            // Запуск покачивания головы на треть секунды (~0.33с) раньше конца анимации доставания
            float settleThreshold = Math.max(0f, len - 0.33f);
            if (animTime >= settleThreshold) {
                settleTime += (1f / 20f);
                if (settleTime > 2.0f) settleTime = 2.0f;
            } else {
                settleTime = 0f;
            }

        } else {
            settleTime = 0f;
            if (isDollHeld || showProgress > 0f) {
                if (isDollHeld) {
                    if (efw.init.EfwModSounds.DOLL_DOWN != null) {
                        mc.player.playSound(efw.init.EfwModSounds.DOLL_DOWN, 0.9F, 1.0F);
                    }
                }
                isDollHeld = false;
                isHiding   = true;

                // Прогресс анимации "down"
                float downLen = m.getAnimationLength("down");
                if (downLen <= 0f) downLen = 0.25f;
                hideAnimTime += (1f / 20f) * DollSettings.animSpeed;
                if (hideAnimTime > downLen) hideAnimTime = downLen;
            }

            showProgress = Math.max(0f, showProgress - DollSettings.hideSpeed);

            if (showProgress <= 0f) {
                isHiding     = false;
                animTime     = 0f;
                hideAnimTime = 0f;
            }
        }

        // === Обновление инерции мыши и движения игрока ===
        prevSwayYaw   = swayYaw;
        prevSwayPitch = swayPitch;
        prevSwayY     = swayY;

        float curYaw   = mc.player.rotationYaw;
        float curPitch = mc.player.rotationPitch;

        float dYaw   = curYaw - lastYaw;
        float dPitch = curPitch - lastPitch;

        while (dYaw > 180f)  dYaw -= 360f;
        while (dYaw < -180f) dYaw += 360f;

        dYaw   = Math.max(-30f, Math.min(30f, dYaw));
        dPitch = Math.max(-30f, Math.min(30f, dPitch));

        lastYaw   = curYaw;
        lastPitch = curPitch;

        double motY = mc.player.posY - mc.player.prevPosY;

        float targetSwayYaw   = -dYaw * 0.35f;
        float targetSwayPitch = -dPitch * 0.30f;
        float targetSwayY     = (float) (-motY * 3.5f);

        swayYawVel   += (targetSwayYaw - swayYaw) * 0.45f;
        swayYawVel   *= 0.65f;
        swayYaw      += swayYawVel;

        swayPitchVel += (targetSwayPitch - swayPitch) * 0.45f;
        swayPitchVel *= 0.65f;
        swayPitch    += swayPitchVel;

        swayYVel     += (targetSwayY - swayY) * 0.45f;
        swayYVel     *= 0.65f;
        swayY        += swayYVel;

        double horizSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
        if (mc.player.onGround && horizSpeed > 0.05) {
            walkBob += 0.35f;
        } else {
            walkBob = 0f; // В idle дыхание полностью выключено — кукла спокойна и не дергается
        }

        // Пока кукла активна, сбрасываем прогресс рук ваниллы на ноль,
        // чтобы при окончании убирания рука плавно поднялась снизу
        if (isDollActive()) {
            resetItemRendererProgress(mc);
        }
    }

    public static boolean isLyingOrCrawling(EntityPlayer player) {
        if (player == null) return false;
        if (player.isPlayerSleeping()) return true;
        if (efw.AnimationTickHandler.isPlayerCrawling(player)) return true;
        if (player.height < 1.0F) return true;
        if (player.isElytraFlying()) return true;
        if (player instanceof com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) {
            com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable res = (com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable) player;
            if (res.isForcingCrawling() || res.isVisuallySwimming() || res.getPose() == com.fuzs.aquaacrobatics.entity.Pose.SWIMMING) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDollActive() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && isLyingOrCrawling(mc.player)) {
            return false;
        }
        return isDollHeld || showProgress > 0f;
    }

    private static java.lang.reflect.Field fieldEquippedProgressMain = null;
    private static java.lang.reflect.Field fieldPrevEquippedProgressMain = null;
    private static java.lang.reflect.Field fieldEquippedProgressOff = null;
    private static java.lang.reflect.Field fieldPrevEquippedProgressOff = null;
    private static boolean reflectionInitDone = false;

    private static void resetItemRendererProgress(Minecraft mc) {
        try {
            net.minecraft.client.renderer.ItemRenderer ir = mc.getItemRenderer();
            if (ir == null) return;
            if (!reflectionInitDone) {
                reflectionInitDone = true;
                String[] mainNames = {"equippedProgressMainHand", "field_187469_f", "f"};
                String[] prevMainNames = {"prevEquippedProgressMainHand", "field_187470_g", "g"};
                String[] offNames = {"equippedProgressOffHand", "field_187471_h", "h"};
                String[] prevOffNames = {"prevEquippedProgressOffHand", "field_187472_i", "i"};
                for (java.lang.reflect.Field f : net.minecraft.client.renderer.ItemRenderer.class.getDeclaredFields()) {
                    for (String name : mainNames) if (f.getName().equals(name)) { f.setAccessible(true); fieldEquippedProgressMain = f; break; }
                    for (String name : prevMainNames) if (f.getName().equals(name)) { f.setAccessible(true); fieldPrevEquippedProgressMain = f; break; }
                    for (String name : offNames) if (f.getName().equals(name)) { f.setAccessible(true); fieldEquippedProgressOff = f; break; }
                    for (String name : prevOffNames) if (f.getName().equals(name)) { f.setAccessible(true); fieldPrevEquippedProgressOff = f; break; }
                }
            }
            if (fieldEquippedProgressMain != null) fieldEquippedProgressMain.setFloat(ir, 0.0f);
            if (fieldPrevEquippedProgressMain != null) fieldPrevEquippedProgressMain.setFloat(ir, 0.0f);
            if (fieldEquippedProgressOff != null) fieldEquippedProgressOff.setFloat(ir, 0.0f);
            if (fieldPrevEquippedProgressOff != null) fieldPrevEquippedProgressOff.setFloat(ir, 0.0f);
        } catch (Throwable ignored) {}
    }

    public static boolean isDoll(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemDoll;
    }

    public static boolean isDollHeldByPlayer(EntityPlayer player) {
        if (player == null) return false;
        ItemStack main = player.getHeldItemMainhand();
        ItemStack off  = player.getHeldItemOffhand();
        return isDoll(main) || isDoll(off);
    }

    public static float getHoldProgress(EntityPlayer player, float pt) {
        if (isLyingOrCrawling(player)) {
            return 0.0f;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (player == mc.player) {
            float p = prevShowProgress + (showProgress - prevShowProgress) * pt;
            p = Math.max(0f, Math.min(1f, p));
            return p * p * (3f - 2f * p);
        }
        return isDollHeldByPlayer(player) ? 1.0f : 0.0f;
    }

    // =========================================================
    // Рендер руки
    // =========================================================

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderHand(RenderSpecificHandEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;
        if (isLyingOrCrawling(mc.player)) return;

        float pt       = event.getPartialTicks();
        float progress = prevShowProgress + (showProgress - prevShowProgress) * pt;

        boolean playerHoldsDoll = isDollHeldByPlayer(mc.player);
        if (!playerHoldsDoll && progress <= 0f) return;

        boolean isOffHand = isOffHand(mc, event.getHand());

        // Убираем off-hand пока кукла на экране или в руках
        if (isOffHand) {
            if (progress > 0f || playerHoldsDoll) {
                event.setCanceled(true);
            }
            return;
        }

        // Главная рука:
        // Пока кукла еще на экране (даже если игрок уже переключил слот на меч/руку),
        // отменяем стандартную руку и дорисовываем плавный уход куклы до самого конца!
        if (progress > 0f) {
            event.setCanceled(true);
            com.voltyx.mwccf.blood.BloodTextureManager.setRenderingPlayer(mc.player);
            try {
                renderDoll(mc, progress);
            } finally {
                com.voltyx.mwccf.blood.BloodTextureManager.clearRenderingPlayer();
            }
        }
    }

    private static boolean isOffHand(Minecraft mc, EnumHand hand) {
        EnumHandSide mainSide = mc.gameSettings.mainHand;
        return mainSide == EnumHandSide.RIGHT ? hand == EnumHand.OFF_HAND
                                              : hand == EnumHand.MAIN_HAND;
    }

    // =========================================================
    // Кастомный рендер куклы
    // =========================================================

    private static void renderDoll(Minecraft mc, float progress) {
        BedrockDollModel m = getModel();
        if (!m.isLoaded()) return;

        float pt = mc.getRenderPartialTicks();

        // Анимация и плавный переход (crossfade) при убирании
        float animT;
        if (isHiding) {
            animT = prevHideAnimTime + (hideAnimTime - prevHideAnimTime) * pt;
            // Плавное смешивание от текущей позы удержания к анимации "down" (кроссфейд за 0.12с)
            float blendWeight = Math.min(1f, animT / 0.12f);
            float holdTime    = prevAnimTime + (animTime - prevAnimTime) * pt;
            m.applyAnimationBlended("animation", holdTime, "down", animT, blendWeight);
        } else {
            animT = prevAnimTime + (animTime - prevAnimTime) * pt;
            m.applyAnimation("animation", animT);
        }

        // Физика инерции рук и головы от мыши и движения
        float curSYaw   = prevSwayYaw + (swayYaw - prevSwayYaw) * pt;
        float curSPitch = prevSwayPitch + (swayPitch - prevSwayPitch) * pt;
        float curSY     = prevSwayY + (swayY - prevSwayY) * pt;
        float bob       = (walkBob > 0f) ? (float) Math.sin(walkBob) : 0f;

        float inert = DollSettings.inertia;
        if (inert > 0f) {
            // Голова мягко отстает от поворотов камеры и покачивается
            float headYRad = (float) Math.toRadians(curSYaw * 0.6f * inert);
            float headXRad = (float) Math.toRadians((curSPitch * 0.7f + bob * 1.5f) * inert);
            float headZRad = (float) Math.toRadians(-curSYaw * 0.3f * inert);
            m.addBoneRotation("Head", headXRad, headYRad, headZRad);

            // Руки раскачиваются с большей амплитудой и подпрыгивают при прыжках/шагах
            float rHandXRad = (float) Math.toRadians((curSPitch * 1.1f + curSY * 15f + bob * 3.0f) * inert);
            float rHandYRad = (float) Math.toRadians(curSYaw * 1.2f * inert);
            float rHandZRad = (float) Math.toRadians((curSYaw * 0.5f - curSY * 10f) * inert);
            m.addBoneRotation("RHand", rHandXRad, rHandYRad, rHandZRad);

            float lHandXRad = (float) Math.toRadians((curSPitch * 1.1f + curSY * 15f - bob * 3.0f) * inert);
            float lHandYRad = (float) Math.toRadians(curSYaw * 1.2f * inert);
            float lHandZRad = (float) Math.toRadians((curSYaw * 0.5f + curSY * 10f) * inert);
            m.addBoneRotation("LHand", lHandXRad, lHandYRad, lHandZRad);
        }

        // Покачивание головы куклы в конце анимации доставания (settling sway - только голова)
        float curSettle = prevSettleTime + (settleTime - prevSettleTime) * pt;
        float settleDuration = 0.55f; // ~11 тиков приятного затухающего кивка
        if (curSettle > 0f && curSettle < settleDuration && !isHiding) {
            float t = curSettle / settleDuration;
            // Затухающая синусоида (мягкий кивок головы вперед и откат назад)
            float env = (1.0f - t) * (1.0f - t);
            float wave = (float) Math.sin(t * Math.PI * 2.5) * env;
            float headPitch = wave * 9.0f; // градусы (чуть сильнее)

            // Только голова!
            m.addBoneRotation("Head", (float) Math.toRadians(headPitch), 0f, 0f);
        }

        // Плавный выезд (smoothstep) без резких рывков на границах
        float clampedP = Math.max(0f, Math.min(1f, progress));
        float p = clampedP * clampedP * (3f - 2f * clampedP);
        float slideY = -(1f - p) * DollSettings.slideDist;

        GlStateManager.pushMatrix();

        // Позиция в camera-space
        GlStateManager.translate(DollSettings.posX, slideY + DollSettings.posY, DollSettings.posZ);

        // Вращение
        GlStateManager.rotate(DollSettings.rotY, 0f, 1f, 0f);
        GlStateManager.rotate(DollSettings.rotX, 1f, 0f, 0f);
        GlStateManager.rotate(DollSettings.rotZ, 0f, 0f, 1f);

        // Инвертируем Y для camera-space
        GlStateManager.scale(DollSettings.scale, -DollSettings.scale, DollSettings.scale);

        // Текстура
        mc.getTextureManager().bindTexture(TEX_LOC);
        GlStateManager.color(1f, 1f, 1f, DollSettings.alpha);

        DollSettings.applyGLBlend();
        GlStateManager.disableCull();
        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CW);

        // Сохраняем исходные координаты лайтмапы
        float prevBrightnessX = OpenGlHelper.lastBrightnessX;
        float prevBrightnessY = OpenGlHelper.lastBrightnessY;

        // Освещение (настоящее динамическое освещение мира или full-bright)
        if (DollSettings.worldLighting) {
            BlockPos eyePos = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            int light = mc.world.getCombinedLight(eyePos, 0);
            int lx = light % 65536;
            int ly = light / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lx, (float) ly);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.enableRescaleNormal();
        } else {
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        }

        // Рендер
        m.render(RENDER_SCALE);

        // Гарантированно выключаем standard item lighting, иначе весь мир и экран чернеют
        if (DollSettings.worldLighting) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
        }
        GlStateManager.disableLighting();

        // Восстанавливаем лайтмап координаты
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevBrightnessX, prevBrightnessY);

        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CCW);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

        // Восстанавливаем нейтральные параметры для последующих элементов рендера
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableBlend();
    }

    // =========================================================
    // Горячие клавиши отладки
    // =========================================================

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;
        if (!Keyboard.getEventKeyState()) return;

        int key = Keyboard.getEventKey();

        // F8 или INSERT - переключение HUD отладки
        if (key == Keyboard.KEY_F8 || key == Keyboard.KEY_INSERT) {
            DollSettings.debugHudEnabled = !DollSettings.debugHudEnabled;
            mc.player.sendStatusMessage(new TextComponentString(
                TextFormatting.GOLD + "[Doll Debug] " +
                (DollSettings.debugHudEnabled ? TextFormatting.GREEN + "HUD включен (Стрелки для настройки, P для копирования)"
                                             : TextFormatting.RED + "HUD отключен")), true);
            return;
        }

        if (!DollSettings.debugHudEnabled) return;

        boolean isShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean isCtrlOrAlt = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)
                           || Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);

        DollSettings.Property[] props = DollSettings.Property.values();

        // Стрелки Вверх/Вниз, [ / ], Numpad 8/2 - выбор параметра
        if (key == Keyboard.KEY_UP || key == Keyboard.KEY_LBRACKET || key == Keyboard.KEY_NUMPAD8) {
            DollSettings.selectedIndex = (DollSettings.selectedIndex - 1 + props.length) % props.length;
            return;
        }
        if (key == Keyboard.KEY_DOWN || key == Keyboard.KEY_RBRACKET || key == Keyboard.KEY_NUMPAD2) {
            DollSettings.selectedIndex = (DollSettings.selectedIndex + 1) % props.length;
            return;
        }

        // Стрелки Влево/Вправо, - / +, Numpad 4/6 - изменение значения
        if (key == Keyboard.KEY_LEFT || key == Keyboard.KEY_MINUS || key == Keyboard.KEY_NUMPAD4 || key == Keyboard.KEY_SUBTRACT) {
            DollSettings.adjust(props[DollSettings.selectedIndex], -1, isShift, isCtrlOrAlt);
            return;
        }
        if (key == Keyboard.KEY_RIGHT || key == Keyboard.KEY_EQUALS || key == Keyboard.KEY_NUMPAD6 || key == Keyboard.KEY_ADD) {
            DollSettings.adjust(props[DollSettings.selectedIndex], 1, isShift, isCtrlOrAlt);
            return;
        }

        // P или Numpad Multiply - распечатать и скопировать в буфер
        if (key == Keyboard.KEY_P || key == Keyboard.KEY_MULTIPLY) {
            DollSettings.dumpAndCopy(mc);
            return;
        }

        // R или HOME - сброс
        if (key == Keyboard.KEY_R || key == Keyboard.KEY_HOME) {
            if (isShift) {
                DollSettings.resetAll();
                mc.player.sendStatusMessage(new TextComponentString(TextFormatting.YELLOW + "[Doll] Все настройки сброшены"), true);
            } else {
                DollSettings.reset(props[DollSettings.selectedIndex]);
                mc.player.sendStatusMessage(new TextComponentString(TextFormatting.YELLOW + "[Doll] " + props[DollSettings.selectedIndex].name + " сброшен"), true);
            }
            return;
        }
    }

    // =========================================================
    // Отрисовка HUD отладки на экране
    // =========================================================

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderCrosshair(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;
        // Отключаем crosshair от первого лица, когда держим куклу на экране
        if (mc.gameSettings.thirdPersonView == 0 && isDollActive()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!DollSettings.debugHudEnabled) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;
        if (mc.gameSettings.showDebugInfo) return;

        renderDebugHud(mc);
    }

    private static void renderDebugHud(Minecraft mc) {
        int x = 6;
        int y = 6;
        int w = 275;
        DollSettings.Property[] props = DollSettings.Property.values();
        int h = 16 + props.length * 10 + 38;

        Gui.drawRect(x - 2, y - 2, x + w + 2, y + h + 2, 0xCC111111);
        Gui.drawRect(x - 2, y - 2, x + w + 2, y - 1, 0xFF555555);
        Gui.drawRect(x - 2, y + h + 1, x + w + 2, y + h + 2, 0xFF555555);
        Gui.drawRect(x - 2, y - 2, x - 1, y + h + 2, 0xFF555555);
        Gui.drawRect(x + w + 1, y - 2, x + w + 2, y + h + 2, 0xFF555555);

        int curY = y + 2;
        mc.fontRenderer.drawStringWithShadow("§6§l=== НАСТРОЙКА КУКЛЫ В ИГРЕ ===", x + 4, curY, 0xFFFFFF);
        curY += 13;

        for (int i = 0; i < props.length; i++) {
            DollSettings.Property p = props[i];
            boolean sel = (i == DollSettings.selectedIndex);
            String valStr;
            if (p == DollSettings.Property.BLEND_MODE) {
                valStr = DollSettings.blendMode + " (" + DollSettings.getBlendModeName(DollSettings.blendMode) + ")";
            } else if (p == DollSettings.Property.LIGHTING) {
                valStr = DollSettings.worldLighting ? "§aВКЛ (Тени мира)" : "§cВЫКЛ (Full-bright)";
            } else if (p == DollSettings.Property.ROT_X || p == DollSettings.Property.ROT_Y || p == DollSettings.Property.ROT_Z) {
                valStr = String.format(Locale.US, "%.1f°", DollSettings.getValue(p));
            } else {
                valStr = String.format(Locale.US, "%.3f", DollSettings.getValue(p));
            }

            if (sel) {
                Gui.drawRect(x, curY - 1, x + w, curY + 9, 0x44FFFF00);
                mc.fontRenderer.drawStringWithShadow("§e> §l" + p.name + ": §a§l" + valStr, x + 4, curY, 0xFFFF55);
            } else {
                mc.fontRenderer.drawStringWithShadow("§7  " + p.name + ": §f" + valStr, x + 4, curY, 0xCCCCCC);
            }
            curY += 10;
        }

        curY += 3;
        mc.fontRenderer.drawStringWithShadow("§8----------------------------------------", x + 4, curY, 0x888888);
        curY += 9;
        mc.fontRenderer.drawStringWithShadow("§b[Вверх/Вниз]: выбор | [Влево/Вправо]: изм.", x + 4, curY, 0x55FFFF);
        curY += 9;
        mc.fontRenderer.drawStringWithShadow("§b[Shift]: x10 | [Ctrl]: x0.1 | [R]: сброс", x + 4, curY, 0x55FFFF);
        curY += 9;
        mc.fontRenderer.drawStringWithShadow("§a§l[P]: СКОПИРОВАТЬ §7| §e[F8/INSERT]: скрыть", x + 4, curY, 0x55FF55);
    }
}
