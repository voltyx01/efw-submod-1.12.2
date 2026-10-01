package com.voltyx.mwccf.render.doll;

import efw.item.ItemDoll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Рендерер куклы Сайи от первого лица.
 *
 * Логика:
 * - При удержании куклы в ГЛАВНОЙ руке:
 *   - Левая рука (off-hand) визуально убирается.
 *   - Кукла выдвигается снизу по центру с анимацией "animation" (hold_on_last_frame).
 *   - При уборке — анимация "down", кукла уходит вниз.
 *
 * Масштаб/позиция: кукла в Bedrock-единицах, scale = 1/16 блока.
 * Кукла центрируется горизонтально (X=0) и расположена чуть ниже центра экрана.
 */
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class DollRenderer {

    // === Ресурсы ===
    private static final ResourceLocation GEO_LOC  = new ResourceLocation("mwccf", "geo/doll.geo.json");
    private static final ResourceLocation ANIM_LOC = new ResourceLocation("mwccf", "animations/doll.animation.json");
    private static final ResourceLocation TEX_LOC  = new ResourceLocation("mwccf", "textures/entity/doll.png");

    /**
     * Масштаб: Bedrock-единица → блок. 1/16 = 0.0625 (стандарт skin-scale).
     * Кукла ~20 единиц высотой → ~1.25 блока. Подбери под вкус.
     */
    private static final float RENDER_SCALE = 0.0525f;

    // === Скорости анимации показа/скрытия ===
    private static final float APPEAR_SPEED = 0.08f; // progress/тик
    private static final float HIDE_SPEED   = 0.12f;

    // === State ===
    private static float showProgress     = 0f;
    private static float prevShowProgress = 0f;

    private static float animTime     = 0f; // текущее время анимации "animation"
    private static float hideAnimTime = 0f; // текущее время анимации "down"

    private static boolean isDollHeld = false;
    private static boolean isHiding   = false;

    // === Модель ===
    private static BedrockDollModel model = null;

    private static BedrockDollModel getModel() {
        if (model == null) {
            model = new BedrockDollModel();
            model.load(GEO_LOC, ANIM_LOC);
        }
        return model;
    }

    // =========================================================
    // Client tick — обновляем прогресс
    // =========================================================

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        boolean holdingDoll = isDollHeldByPlayer(mc);
        prevShowProgress = showProgress;

        BedrockDollModel m = getModel();

        if (holdingDoll) {
            isDollHeld   = true;
            isHiding     = false;
            hideAnimTime = 0f;

            showProgress = Math.min(1f, showProgress + APPEAR_SPEED);

            // Прогресс анимации "animation" (hold_on_last_frame)
            float len = m.getAnimationLength("animation");
            if (len <= 0f) len = 0.625f;
            animTime += 1f / 20f;
            if (animTime > len) animTime = len;

        } else {
            if (isDollHeld || showProgress > 0f) {
                isDollHeld = false;
                isHiding   = true;

                // Прогресс анимации "down"
                float downLen = m.getAnimationLength("down");
                if (downLen <= 0f) downLen = 0.25f;
                hideAnimTime += 1f / 20f;
                if (hideAnimTime > downLen) hideAnimTime = downLen;
            }

            showProgress = Math.max(0f, showProgress - HIDE_SPEED);

            if (showProgress <= 0f) {
                isHiding     = false;
                animTime     = 0f;
                hideAnimTime = 0f;
            }
        }
    }

    private static boolean isDollHeldByPlayer(Minecraft mc) {
        if (mc.player == null) return false;
        ItemStack main = mc.player.getHeldItemMainhand();
        ItemStack off  = mc.player.getHeldItemOffhand();
        return (main != null && !main.isEmpty() && main.getItem() instanceof ItemDoll)
            || (off  != null && !off.isEmpty()  && off.getItem()  instanceof ItemDoll);
    }

    // =========================================================
    // Рендер руки
    // =========================================================

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderHand(RenderSpecificHandEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        float pt       = event.getPartialTicks();
        float progress = prevShowProgress + (showProgress - prevShowProgress) * pt;

        boolean playerHoldsDoll = isDollHeldByPlayer(mc);
        if (!playerHoldsDoll && progress <= 0f) return;

        ItemStack stack   = event.getItemStack();
        boolean isDollHere = (stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemDoll);
        boolean isOffHand  = isOffHand(mc, event.getHand());

        // Убираем off-hand
        if (isOffHand) {
            event.setCanceled(true);
            return;
        }

        // Главная рука с куклой: заменяем на кастомный рендер
        if (isDollHere) {
            event.setCanceled(true);
            if (progress > 0f) {
                renderDoll(mc, progress);
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

        // Выбор анимации
        String animName;
        float  animT;
        if (isHiding) {
            animName = "down";
            animT    = hideAnimTime;
        } else {
            animName = "animation";
            animT    = animTime;
        }
        m.applyAnimation(animName, animT);

        // Кукла выезжает снизу: при progress=0 ниже экрана, при progress=1 — на месте
        float slideY = -(1f - progress) * 0.55f;

        GlStateManager.pushMatrix();

        // Позиция в camera-space (единицы = блоки)
        // X=0 — центр, Y — небольшой сдвиг вниз + плавный въезд, Z — расстояние от камеры
        GlStateManager.translate(0f, slideY - 0.20f, -0.45f);

        // Поворачиваем так чтобы кукла смотрела на игрока
        // Модель ориентирована "лицом в +Z Bedrock", это становится -X после mirror,
        // поворачиваем 90° по Y чтобы она смотрела вперёд на игрока
        GlStateManager.rotate(180f, 0f, 1f, 0f);

        // Небольшой наклон вперёд для "поднесения к игроку" (опционально)
        GlStateManager.rotate(-10f, 1f, 0f, 0f);

        // Текстура
        mc.getTextureManager().bindTexture(TEX_LOC);
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        // Рендер с масштабом (RENDER_SCALE переводит Bedrock-пиксели в блоки)
        m.render(RENDER_SCALE);

        GlStateManager.enableCull();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }
}
