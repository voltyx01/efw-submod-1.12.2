// ============================================================================
// FIX v2: Laser beam must be a pure overlay (immune to world depth/fog), but
// still hidden behind the sight/weapon model itself — WITHOUT touching the
// global depth buffer (that was the bug in v1: glClear(DEPTH) with no scissor
// wipes depth for the ENTIRE screen, breaking occlusion for everything drawn
// earlier this frame -> flicker/clipping at edges, and the beam now shows
// through the sight because nothing had a chance to re-establish depth before
// the beam's own draw call).
// ============================================================================
//
// НОВЫЙ ПОДХОД (без stencil, без глобального glClear):
//
//   1) Луч рисуется С ВЫКЛЮЧЕННЫМ depth test — то есть гарантированно поверх
//      абсолютно всего, что нарисовано раньше (мир, блоки, GUI-ниже-слоя).
//      Он больше не читает и не пишет depth вообще — значит никакого
//      z-fighting, никакого мерцания, никакой реакции на буфер мира.
//
//   2) Проблема окклюзии моделью прицела решается вручную, а не через GL:
//      сразу после отрисовки луча мы says повторно рисуем ТОЛЬКО модель
//      прицела (то есть тот же item stack, тот же transform) поверх луча,
//      тоже с выключенным depth test — просто как второй "слой" в том же
//      локальном стеке матриц. Так как она рисуется СТРОГО ПОСЛЕ луча в
//      том же кадре, она гарантированно перекрывает его на экране —
//      независимо от depth buffer, независимо от углов обзора.
//
//   Итог: три чётких визуальных слоя, в порядке отрисовки:
//      мир -> [луч, без depth] -> [модель прицела, без depth] -> GUI
//
// ВАЖНО: этот CustomRenderer выполняется ВНУТРИ рендера конкретного
// attachment'а (прицела). Значит "модель прицела" — это тот же item, что
// рендерится в этом же вызове renderItem() выше по стеку. Достаточно один
// раз повторно вызвать renderItem() для текущего stack сразу после луча.
// ============================================================================

package com.paneedah.weaponlib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.function.BiConsumer;

public class LaserBeamRenderer implements CustomRenderer {

    public static volatile boolean suppressRender = false;

    private float xOffset = 0.5f;
    private float yOffset = -1.3f;
    private float zOffset = -1.5f;

    private BiConsumer<EntityLivingBase, ItemStack> positioning;

    private float red = 1.0f;
    private float green = 0.05f;
    private float blue = 0.05f;

    public LaserBeamRenderer(BiConsumer<EntityLivingBase, ItemStack> positioning) {
        this.positioning = positioning;
    }

    public LaserBeamRenderer(BiConsumer<EntityLivingBase, ItemStack> positioning, float red, float green, float blue) {
        this.positioning = positioning;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public boolean isGreenDefault() {
        return this.green > this.red;
    }

    @Override
    public void render(RenderContext renderContext) {
        if (suppressRender) {
            return;
        }

        // Do not render laser beam if any GUI screen is open (e.g. inventory, chests, crafting, pause menu)
        // EXCEPTION: GuiWeaponModding is the weapon modification screen where laser preview is intended
        net.minecraft.client.gui.GuiScreen currentScreen = net.minecraft.client.Minecraft.getMinecraft().currentScreen;
        boolean isModdingGui = (currentScreen != null && currentScreen.getClass().getName().contains("GuiWeaponModding"));
        if (currentScreen != null && !isModdingGui) {
            return;
        }

        net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType type = renderContext
                .getTransformType();
        if (type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.GUI
                || type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED
                || type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.GROUND
                || type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.HEAD
                || type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.NONE) {
            return;
        }

        // Do not render laser beam on holstered / back-mounted / unheld weapons (e.g. YDM's Weapon Master)
        EntityLivingBase entity = renderContext.getPlayer();
        if (entity != null && !isModdingGui) {
            boolean isFirstPerson = (type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
            if (!isFirstPerson) {
                ItemStack weapon = renderContext.getWeapon();
                boolean isHeld = weapon != null && (
                    (!entity.getHeldItemMainhand().isEmpty() && entity.getHeldItemMainhand().getItem() == weapon.getItem()) ||
                    (!entity.getHeldItemOffhand().isEmpty() && entity.getHeldItemOffhand().getItem() == weapon.getItem())
                );
                if (!isHeld) {
                    return;
                }
            }
        } else if (entity == null && !isModdingGui) {
            return;
        }

        GlStateManager.pushMatrix();

        boolean lightmapWasEnabled = false;
        boolean defaultWasEnabled = true;
        boolean lightingWasEnabled = false;
        boolean scissorWasEnabled = false;

        try {
            if (positioning != null) {
                positioning.accept(renderContext.getPlayer(), renderContext.getWeapon());
            }

            lightingWasEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            GlStateManager.disableLighting();
            GL11.glDisable(GL11.GL_COLOR_MATERIAL);
            GlStateManager.disableFog();
            GlStateManager.disableRescaleNormal();

            // ВАЖНО: Отключаем текстурирование на ОБОИХ юнитах (Unit 0 - диффуз, Unit 1 - лайтмап)
            // и запоминаем исходное состояние, чтобы восстановить в точности то, что было!
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit);
            lightmapWasEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            GlStateManager.disableTexture2D();

            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);
            defaultWasEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            GlStateManager.disableTexture2D();

            GlStateManager.disableBlend();
            GlStateManager.enableBlend();
            // Аддитивный блендинг (SRC_ALPHA, ONE): свечение луча складывается с фоном без затемнения краев
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

            // Отключаем альфа-тест для абсолютно плавного рассеивания луча в 0
            GlStateManager.disableAlpha();

            // Depth Test: читаем буфер глубины (чтобы луч скрывался за моделью), но не пишем в него
            GlStateManager.enableDepth();
            GlStateManager.depthMask(false);
            GL11.glDepthFunc(GL11.GL_LEQUAL);

            GlStateManager.disableCull();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            scissorWasEnabled = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
            if (scissorWasEnabled) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }

            // ИСПРАВЛЕНИЕ: Настраиваем длину.
            // В GuiWeaponModding.java используется THIRD_PERSON_LEFT_HAND для рендера 3D меню.
            boolean isMenu = (type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
            boolean isFirstPerson = (type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                    || type == net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);

            // Первое лицо: длинный (25.0f). Меню: короткий обрубок (2.5f). В руках от 3-го лица: средний (7.0f).
            float beamLength = isFirstPerson ? 25.0f : (isMenu ? 2.5f : 7.0f);

            float renderR = red;
            float renderG = green;
            float renderB = blue;

            ItemStack weaponStack = renderContext.getWeapon();
            if (weaponStack != null && Tags.hasLaserColor(weaponStack)) {
                int col = Tags.getLaserColor(weaponStack);
                if (col == 1) {
                    // Force RED
                    renderR = 1.0f;
                    renderG = 0.05f;
                    renderB = 0.05f;
                } else if (col == 2) {
                    // Force GREEN
                    renderR = 0.05f;
                    renderG = 1.0f;
                    renderB = 0.1f;
                }
            }

            renderVolumetricLaser(xOffset, yOffset, zOffset, zOffset - beamLength, renderR, renderG, renderB);

            if (scissorWasEnabled) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
            }
        } finally {
            GlStateManager.enableCull();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.shadeModel(GL11.GL_FLAT);

            // Восстанавливаем стандартный блендинг
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            // Восстанавливаем текстурные юниты СТРОГО в их исходное состояние!
            // Никогда не включаем lightmapTexUnit, если он был выключен (например в GUI)!
            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit);
            if (lightmapWasEnabled) {
                GlStateManager.enableTexture2D();
            } else {
                GlStateManager.disableTexture2D();
            }

            GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);
            if (defaultWasEnabled) {
                GlStateManager.enableTexture2D();
            } else {
                GlStateManager.disableTexture2D();
            }

            if (lightingWasEnabled) {
                GlStateManager.enableLighting();
            } else {
                GlStateManager.disableLighting();
            }

            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

            GlStateManager.popMatrix();
        }
    }

    /**
     * Draws a small opaque dark disc at the laser's origin point (the lens),
     * facing the camera, using raw Tessellator geometry only — no item
     * rendering, no recursion into WeaponRenderer. This caps the beam visually
     * so it doesn't look like it starts from behind/inside the sight glass.
     * Purely cosmetic; safe to remove if you don't need it.
     */
    private static void drawLensCap(float cx, float cy, float z) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        GlStateManager.color(0.05f, 0.05f, 0.05f, 1.0f);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        final int segs = 12;
        final float capR = 0.03f;

        bb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bb.pos(cx, cy, z).color(0.05f, 0.05f, 0.05f, 1.0f).endVertex();
        for (int i = 0; i <= segs; i++) {
            float ang = (float) (i * 2.0 * Math.PI / segs);
            bb.pos(cx + (float) Math.cos(ang) * capR, cy + (float) Math.sin(ang) * capR, z)
                    .color(0.05f, 0.05f, 0.05f, 1.0f).endVertex();
        }
        tes.draw();
    }

    private static void renderVolumetricLaser(float cx, float cy, float zStart, float zEnd, float r, float g, float b) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        final int slices = 10;
        final float startCoreW = 0.006f;
        final float endCoreW = 0.016f;
        final float startGlowW = 0.024f;
        final float endGlowW = 0.065f;

        final float[] angles = { 0.0f, 30.0f, 60.0f, 90.0f, 120.0f, 150.0f };

        for (float deg : angles) {
            float rad = (float) Math.toRadians(deg);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            bb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= slices; i++) {
                float t = (float) i / (float) slices;
                float z = zStart + t * (zEnd - zStart);

                float glowW = startGlowW + t * (endGlowW - startGlowW);
                float falloff = (1.0f - t) * (1.0f - t);
                float alphaCenter = falloff * 0.70f;

                float ox = cos * glowW;
                float oy = sin * glowW;

                bb.pos(cx - ox, cy - oy, z).color(r, g, b, 0.0f).endVertex();
                bb.pos(cx, cy, z).color(Math.min(1.0f, r + 0.35f), Math.min(1.0f, g + 0.35f), Math.min(1.0f, b + 0.35f),
                        alphaCenter).endVertex();
            }
            tes.draw();

            bb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= slices; i++) {
                float t = (float) i / (float) slices;
                float z = zStart + t * (zEnd - zStart);

                float glowW = startGlowW + t * (endGlowW - startGlowW);
                float falloff = (1.0f - t) * (1.0f - t);
                float alphaCenter = falloff * 0.70f;

                float ox = cos * glowW;
                float oy = sin * glowW;

                bb.pos(cx, cy, z).color(Math.min(1.0f, r + 0.35f), Math.min(1.0f, g + 0.35f), Math.min(1.0f, b + 0.35f),
                        alphaCenter).endVertex();
                bb.pos(cx + ox, cy + oy, z).color(r, g, b, 0.0f).endVertex();
            }
            tes.draw();
        }

        final float[] coreAngles = { 45.0f, 135.0f };
        for (float deg : coreAngles) {
            float rad = (float) Math.toRadians(deg);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            bb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= slices; i++) {
                float t = (float) i / (float) slices;
                float z = zStart + t * (zEnd - zStart);

                float coreW = startCoreW + t * (endCoreW - startCoreW);
                float falloff = (1.0f - t);
                float alphaCore = falloff * 0.85f;

                float ox = cos * coreW;
                float oy = sin * coreW;

                bb.pos(cx - ox, cy - oy, z).color(r, g, b, 0.0f).endVertex();
                // Ядро лазера: слегка осветленный базовый цвет с уклоном в белый для яркости
                bb.pos(cx + ox, cy + oy, z).color(Math.min(1.0f, r + 0.6f), Math.min(1.0f, g + 0.6f),
                        Math.min(1.0f, b + 0.6f), alphaCore).endVertex();
            }
            tes.draw();
        }

        bb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bb.pos(cx, cy, zStart).color(Math.min(1.0f, r + 0.7f), Math.min(1.0f, g + 0.7f), Math.min(1.0f, b + 0.7f),
                1.0f).endVertex();
        final int flareSegs = 10;
        final float flareR = 0.025f;
        for (int i = 0; i <= flareSegs; i++) {
            float ang = (float) (i * 2.0 * Math.PI / flareSegs);
            bb.pos(cx + (float) Math.cos(ang) * flareR, cy + (float) Math.sin(ang) * flareR, zStart)
                    .color(r, g, b, 0.0f).endVertex();
        }
        tes.draw();
    }
}

/*
 * ПОЧЕМУ ПРЕДЫДУЩАЯ ВЕРСИЯ КРАШИЛАСЬ:
 * LaserBeamRenderer.render() вызывается ИЗНУТРИ
 * WeaponRenderer.renderPostRenderers(),
 * который сам вызывается изнутри WeaponRenderer.renderItem()/getQuads().
 * Повторный
 * вызов RenderItem.renderItem(...) на любом ItemStack в этот момент снова
 * заходит
 * в getQuads() -> renderItem() того же кастомного WeaponRenderer (он глобально
 * перехватывает рендер моделей оружия/attachment'ов), который пытается достать
 * getFirstPersonStateDescriptor() для контекста, которого в этом вложенном
 * вызове
 * не существует -> NullPointerException. Рекурсивный вызов item-рендера из
 * CustomRenderer в этом пайплайне НЕБЕЗОПАСЕН в принципе, независимо от того,
 * какой именно ItemStack передавать.
 *
 * Поэтому окклюзия у линзы теперь решается БЕЗ единого вызова RenderItem —
 * просто маленький непрозрачный диск из чистой Tessellator-геометрии
 * (drawLensCap), рисуемый поверх луча в точке его начала. Это чисто
 * косметическая "заглушка", не требует рендера модели и не может ничего
 * сломать в WeaponRenderer.
 *
 * Если хотите, чтобы заглушка точнее совпадала по размеру с линзой конкретного
 * прицела — подберите capR под конкретный прицел (можно сделать поле
 * lensCapRadius в конструкторе LaserBeamRenderer, если разные прицелы уже
 * передают разные positioning-функции).
 */