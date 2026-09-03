package com.voltyx.mwccf.sins.client.render;

import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import com.voltyx.mwccf.sins.client.GuiSevenScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerBlinkingEyes implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer renderPlayer;

    // ─── Скин Minecraft 64×64 ──────────────────────────────────────────────────
    // Передняя грань головы занимает U=[8..16], V=[8..16] (8×8 пикселей).
    // В пространстве модели голова имеет ширину 8 «юнитов» (от -4 до +4 по X).
    // Рендер ЗЕРКАЛИТ U: пиксель u=8 (левый край текстуры) отображается на правый
    // край лица (+4 по X), u=16 — на левый край (-4 по X).
    //
    // Таким образом, для точки модели с X = eyeX (в диапазоне -4..+4):
    //   u_pixel = 12 - eyeX          (центр текстурного ряда = u=12, центр лица = x=0)
    // Для участка шириной w пикселей:
    //   minU_pixel = 12 - eyeX - w
    //   maxU_pixel = 12 - eyeX
    // По вертикали (V) — прямое отображение, без зеркала:
    //   v_pixel = 8 + (eyeY - (-8))  = eyeY + 16
    //   (eyeY находится в [-8..0] для лба, [-4..0] обычно для глаз)
    //
    // lidOffsetX/Y задают смещение ИСТОЧНИКА пикселей на скине относительно того
    // места, которое занимает глаз. Это позволяет брать пиксели из любого участка
    // лица (например, над глазом) независимо от позиции самого глаза.
    // ──────────────────────────────────────────────────────────────────────────────

    // Границы лица в модельных координатах (в «пикселях» головы, 1 юнит = 1 пиксель
    // при scale=1/16). Голова 8×8, центр (0,0) — центр лица.
    private static final float FACE_X_MIN = -4.0f; // левый край лица
    private static final float FACE_X_MAX =  4.0f; // правый край лица
    private static final float FACE_Y_MIN = -8.0f; // верхний край лица (лоб)
    private static final float FACE_Y_MAX =  0.0f; // нижний край лица (подбородок)

    public LayerBlinkingEyes(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GenderPlayer genderPlayer = WildfireGender.getPlayerById(player.getUniqueID());
        if (genderPlayer == null || !genderPlayer.isBlinkEnabled()) {
            return;
        }

        boolean isEditing = false;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiSevenScreen) {
            GuiSevenScreen gui = (GuiSevenScreen) Minecraft.getMinecraft().currentScreen;
            isEditing = gui.currentTab == GuiSevenScreen.TAB_APPEARANCE && gui.blinkEditorOpen;
        }

        // Период моргания = 4000мс / частота. При freq=1.15 → ~3478мс между морганиями.
        // Фаза смещается по UUID, чтобы у разных игроков моргания не совпадали.
        float blinkFreq = genderPlayer.getBlinkFrequency();
        long blinkPeriodMs = Math.max(500L, Math.round(4000.0 / blinkFreq));
        long time = System.currentTimeMillis() + Math.abs(player.getUniqueID().hashCode() % 2000);
        boolean isBlinking = (time % blinkPeriodMs) < 150;

        if (!isBlinking && !isEditing) {
            return;
        }

        // ── Размер глаза ──────────────────────────────────────────────────────────
        int eyeSize = genderPlayer.getEyeSize();
        float eyeW = (eyeSize == 0) ? 1.0f : 2.0f; // ширина в пикселях модели
        float eyeH = (eyeSize == 2) ? 2.0f : 1.0f; // высота в пикселях модели

        // ── Позиция глаз ──────────────────────────────────────────────────────────
        // dist — смещение каждого глаза от центра лица по X.
        // eyeY — вертикальная позиция верхнего края глаза.
        float dist = genderPlayer.getEyeDistance();                 // >=1, целое
        // eyeHeight=0 → y=0 (низ лица), eyeHeight=8 → y=-8 (верх лица). Слайдер вверх = глаз вверх.
        float eyeY = -(float) genderPlayer.getEyeHeight();

        // Левый глаз: X от (-dist - eyeW) до (-dist)
        // Правый глаз: X от (dist) до (dist + eyeW)
        float leftEyeX  = -dist - eyeW;
        float rightEyeX =  dist;

        // Клэмп: не выходим за левый/правый край лица
        leftEyeX  = Math.max(FACE_X_MIN, Math.min(FACE_X_MAX - eyeW, leftEyeX));
        rightEyeX = Math.max(FACE_X_MIN, Math.min(FACE_X_MAX - eyeW, rightEyeX));
        // Клэмп по вертикали
        eyeY = Math.max(FACE_Y_MIN, Math.min(FACE_Y_MAX - eyeH, eyeY));

        // ── Размер века ────────────────────────────────────────────────────────────
        int eyelidSize = Math.min(eyeSize, genderPlayer.getEyelidSize());
        float lidW = (eyelidSize == 0) ? 1.0f : 2.0f;
        float lidH = (eyelidSize == 2) ? 2.0f : 1.0f;

        // ── UV-смещение источника пикселей века ───────────────────────────────────
        // offsetX/Y — это смещение на скине относительно позиции глаза.
        // Например, offsetY=-1 означает: брать пиксели на 1 строку выше глаза.
        int lidOffsetX = genderPlayer.getEyelidOffsetX();
        int lidOffsetY = genderPlayer.getEyelidOffsetY();

        boolean dualEyelid = genderPlayer.isDualEyelid();

        GlStateManager.pushMatrix();
        this.renderPlayer.getMainModel().bipedHead.postRender(scale);
        // Слегка выносим плоскость рисования перед поверхностью головы
        GlStateManager.translate(0.0F, 0.0F, -4.02F * scale);

        if (isEditing) {
            // ── Режим редактора: рисуем обводки глаз и век ────────────────────────
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();

            // Красные прямоугольники — позиция глаз
            drawOutline(leftEyeX,  eyeY, eyeW, eyeH, scale, 1.0f, 0.2f, 0.2f);
            drawOutline(rightEyeX, eyeY, eyeW, eyeH, scale, 1.0f, 0.2f, 0.2f);

            // Зелёная обводка — откуда берутся пиксели века на скине.
            // В режиме инверсии выбирается ОДИН участок (левый глаз), правый зеркалируется
            // автоматически — показываем только одну обводку.
            // В режиме dualEyelid — два независимых участка, показываем оба.
            float leftLidSrcX = leftEyeX + lidOffsetX;
            float lidSrcY     = eyeY     - lidOffsetY; // offsetY>0 = выше

            drawOutline(leftLidSrcX, lidSrcY, lidW, lidH, scale, 0.2f, 1.0f, 0.2f);
            if (dualEyelid) {
                float rightLidSrcX = rightEyeX + lidOffsetX;
                drawOutline(rightLidSrcX, lidSrcY, lidW, lidH, scale, 0.2f, 1.0f, 0.2f);
            }

            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        } else if (isBlinking) {
            // ── Режим моргания: рисуем веки поверх глаз ───────────────────────────
            this.renderPlayer.bindTexture(player.getLocationSkin());
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.glNormal3f(0.0F, 0.0F, -1.0F);

            // Левый глаз: источник UV — левый участок скина.
            drawEyelid(leftEyeX, eyeY, eyeW, eyeH, lidW, lidH, lidOffsetX, lidOffsetY, false, scale);
            // Правый глаз:
            //   dualEyelid=true  → независимый второй участок (mirrorSrc=true)
            //   dualEyelid=false → тот же участок что и левый, зеркалится автоматически UV-формулой.
            //                      Передаём leftEyeX как базу UV, но рисуем на позиции rightEyeX.
            if (dualEyelid) {
                drawEyelid(rightEyeX, eyeY, eyeW, eyeH, lidW, lidH, lidOffsetX, lidOffsetY, true, scale);
            } else {
                drawEyelidMirrored(rightEyeX, leftEyeX, eyeY, eyeW, eyeH, lidW, lidH, lidOffsetX, lidOffsetY, scale);
            }

            GlStateManager.disableBlend();
        }

        GlStateManager.popMatrix();
    }

    /**
     * Рисует веко поверх одного глаза.
     *
     * @param eyeX      X-позиция левого края глаза в модельных координатах
     * @param eyeY      Y-позиция верхнего края глаза
     * @param eyeW      ширина глаза (пиксели)
     * @param eyeH      высота глаза (пиксели)
     * @param lidW      ширина источника пикселей века
     * @param lidH      высота источника пикселей века
     * @param offsetX   смещение источника по X относительно eyeX (UV-пространство)
     * @param offsetY   смещение источника по Y относительно eyeY (UV-пространство)
     * @param mirrorU   если true — берём второй участок (для dualEyelid=true);
     *                  если false — берём зеркально отражённый участок левого глаза
     * @param scale     масштаб модели
     */
    private void drawEyelid(float eyeX, float eyeY, float eyeW, float eyeH,
                            float lidW, float lidH,
                            int offsetX, int offsetY,
                            boolean mirrorU, float scale) {
        float pu = 1.0f / 64.0f;
        float pv = 1.0f / 64.0f;

        // ── UV-расчёт ─────────────────────────────────────────────────────────────
        // Передняя грань головы в текстуре: U=[8..16], V=[8..16].
        // Модельная X зеркалируется: u_pixel = 12 - modelX.
        // Источник пикселей века = позиция глаза + offset (смещение по скину).
        //
        // Позиция пикселей источника по U (с учётом зеркала модели):
        //   Левый глаз (eyeX < 0):  srcX = eyeX + offsetX
        //                            u_right_pixel = 12 - srcX
        //                            u_left_pixel  = 12 - srcX - lidW
        //   Правый глаз без mirror: берём зеркально симметричный участок.
        //     srcX_right = -eyeX - eyeW + offsetX   (симметрия через центр лица)
        //     => u_right_pixel = 12 - srcX_right = 12 + eyeX + eyeW - offsetX
        //   Правый глаз с mirror (dualEyelid): берём тот же абсолютный offsetX,
        //     но применённый к правому eyeX.
        //     srcX_right = eyeX + offsetX
        //     (eyeX здесь уже > 0, т.е. правый глаз)

        float minU, maxU;
        if (!mirrorU) {
            // Левый глаз ИЛИ правый в режиме инверсии (зеркальный источник).
            // Для правого глаза зеркалим источник: используем симметричный участок.
            // eyeX < 0 для левого, eyeX > 0 для правого.
            // Единая формула: srcX = eyeX + offsetX, затем зеркалируем U.
            float srcX = eyeX + offsetX;
            // u-координаты зеркалируются (модель отражает U):
            float uRight = 12.0f - srcX;          // правый край U-участка на скине
            float uLeft  = uRight - lidW;          // левый край
            minU = uLeft  * pu;
            maxU = uRight * pu;
        } else {
            // Правый глаз в режиме dualEyelid: отдельный участок скина.
            // Источник смещён так же, как для правого глаза — симметрично левому.
            // srcX_right = -eyeX - eyeW + offsetX  (зеркало позиции левого глаза)
            float mirroredEyeX = -eyeX - eyeW; // позиция "левого" аналога правого глаза
            float srcX = mirroredEyeX + offsetX;
            float uRight = 12.0f - srcX;
            float uLeft  = uRight - lidW;
            minU = uLeft  * pu;
            maxU = uRight * pu;
        }

        // V-координаты (вертикаль, без зеркала):
        // v_pixel = 16 + eyeY + offsetY
        // (eyeY=-8 → v=8 = верхний край лица; eyeY=0 → v=16 = нижний край)
        // offsetY>0 = источник пикселей ВЫШЕ (более отрицательное Y в координатах лица)
        float srcV = 16.0f + eyeY - offsetY;
        float minV = srcV        * pv;
        float maxV = (srcV + lidH) * pv;

        // ── Геометрия: веко рисуется поверх всего глаза ───────────────────────────
        float x1 = eyeX         * scale;
        float x2 = (eyeX + eyeW) * scale;
        float y1 = eyeY         * scale;
        float y2 = (eyeY + eyeH) * scale;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(x2, y1, 0).tex(maxU, minV).normal(0.0F, 0.0F, -1.0F).endVertex();
        buffer.pos(x1, y1, 0).tex(minU, minV).normal(0.0F, 0.0F, -1.0F).endVertex();
        buffer.pos(x1, y2, 0).tex(minU, maxV).normal(0.0F, 0.0F, -1.0F).endVertex();
        buffer.pos(x2, y2, 0).tex(maxU, maxV).normal(0.0F, 0.0F, -1.0F).endVertex();
        tessellator.draw();
    }

    /**
     * Рисует правый глаз в режиме инверсии.
     * UV берётся из того же участка скина что и левый глаз (uvEyeX = leftEyeX),
     * но геометрически квад рисуется на позиции правого глаза (drawEyeX = rightEyeX).
     * U-координаты при этом НЕ переворачиваются — модель сама зеркалит UV
     * (правая сторона модели = левая сторона скина), поэтому тот же участок
     * текстуры ляжет зеркально на правый глаз.
     */
    private void drawEyelidMirrored(float drawEyeX, float uvEyeX, float eyeY,
                                    float eyeW, float eyeH,
                                    float lidW, float lidH,
                                    int offsetX, int offsetY, float scale) {
        float pu = 1.0f / 64.0f;
        float pv = 1.0f / 64.0f;

        // UV-источник: левый участок (uvEyeX < 0)
        float srcX = uvEyeX + offsetX;
        float uRight = 12.0f - srcX;
        float uLeft  = uRight - lidW;
        float minU = uLeft  * pu;
        float maxU = uRight * pu;

        float srcV = 16.0f + eyeY - offsetY;
        float minV = srcV         * pv;
        float maxV = (srcV + lidH) * pv;

        // Геометрия: правый глаз
        float x1 = drawEyeX         * scale;
        float x2 = (drawEyeX + eyeW) * scale;
        float y1 = eyeY              * scale;
        float y2 = (eyeY + eyeH)     * scale;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(x2, y1, 0).tex(maxU, minV).normal(0.0F, 0.0F, -1.0F).endVertex();
        buffer.pos(x1, y1, 0).tex(minU, minV).normal(0.0F, 0.0F, -1.0F).endVertex();
        buffer.pos(x1, y2, 0).tex(minU, maxV).normal(0.0F, 0.0F, -1.0F).endVertex();
        buffer.pos(x2, y2, 0).tex(maxU, maxV).normal(0.0F, 0.0F, -1.0F).endVertex();
        tessellator.draw();
    }

    private void drawOutline(float x, float y, float w, float h, float scale,
                             float r, float g, float b) {
        float x1 = x       * scale;
        float x2 = (x + w) * scale;
        float y1 = y       * scale;
        float y2 = (y + h) * scale;

        GlStateManager.glLineWidth(2.0F);
        GlStateManager.color(r, g, b, 1.0f);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        buffer.pos(x1, y1, 0).endVertex();
        buffer.pos(x2, y1, 0).endVertex();
        buffer.pos(x2, y2, 0).endVertex();
        buffer.pos(x1, y2, 0).endVertex();
        tessellator.draw();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}