package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BraceletUI {
    
    public static boolean hasBraceletEquipped() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return false;
        return hasBraceletEquipped(mc.player);
    }

    public static boolean hasBraceletEquipped(net.minecraft.entity.player.EntityPlayer player) {
        if (player == null) return false;
        if (net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) {
            baubles.api.cap.IBaublesItemHandler handler = baubles.api.BaublesApi.getBaublesHandler(player);
            if (handler != null) {
                for (int i = 1; i <= 2; i++) {
                    net.minecraft.item.ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem().getRegistryName() != null && stack.getItem().getRegistryName().getPath().equals("bracelet")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasBattery(net.minecraft.entity.player.EntityPlayer player) {
        if (player == null) return false;
        if (net.minecraftforge.fml.common.Loader.isModLoaded("baubles")) {
            baubles.api.cap.IBaublesItemHandler handler = baubles.api.BaublesApi.getBaublesHandler(player);
            if (handler != null) {
                for (int i = 1; i <= 2; i++) {
                    net.minecraft.item.ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem().getRegistryName() != null && stack.getItem().getRegistryName().getPath().equals("bracelet")) {
                        net.minecraft.nbt.NBTTagCompound tag = stack.getTagCompound();
                        return tag != null && tag.hasKey("battery_charge") && tag.getInteger("battery_charge") > 0;
                    }
                }
            }
        }
        return false;
    }

    // Плейсхолдеры для будущих картинок
    public static final net.minecraft.util.ResourceLocation BOOT_LOGO = new net.minecraft.util.ResourceLocation("mwccf", "textures/gui/boot_logo.png");
    public static final net.minecraft.util.ResourceLocation HEART_ICON = new net.minecraft.util.ResourceLocation("mwccf", "textures/gui/heart_icon.png");
    public static final net.minecraft.util.ResourceLocation SCREEN_BACKGROUND = new net.minecraft.util.ResourceLocation("mwccf", "textures/gui/bracelet_background.png");
    
    private static net.minecraft.client.shader.Framebuffer fbo;

    private static final float[][] GLOW_OFFSETS = {
        {1.0f, 0.0f}, {-1.0f, 0.0f}, {0.0f, 1.0f}, {0.0f, -1.0f},
        {0.7f, 0.7f}, {-0.7f, 0.7f}, {0.7f, -0.7f}, {-0.7f, -0.7f}
    };

    // Вызывается перед рендером руки
    public static void updateFBO() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.thirdPersonView != 0) return; // Только от 1-го лица
        
        boolean isMWCWeapon = mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() 
            && efw.animation.WeaponTypeHelper.getWeaponType(mc.player.getHeldItemMainhand()) != efw.animation.WeaponTypeHelper.WeaponType.NONE;

        if (!hasBraceletEquipped()) return;
        if (!BraceletInspectHandler.isInspecting && BraceletInspectHandler.inspectProgress <= 1.0f && !isMWCWeapon) return;

        if (fbo == null) {
            fbo = new net.minecraft.client.shader.Framebuffer(256, 256, true);
            fbo.setFramebufferColor(0.0F, 0.0F, 0.0F, 1.0F);
        }

        int previousFBO = GL11.glGetInteger(36006); // GL_FRAMEBUFFER_BINDING
        java.nio.IntBuffer viewport = org.lwjgl.BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        fbo.bindFramebuffer(true);
        GL11.glViewport(0, 0, 256, 256);

        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, 256, 256, 0, 1000, 3000);

        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -2000);

        GlStateManager.clearColor(0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GlStateManager.disableLighting();
        GlStateManager.disableDepth(); // Отключаем глубину для правильного порядка отрисовки (Z-fighting фикс)
        GlStateManager.disableFog();
        GlStateManager.colorMask(true, true, true, false); // Protect FBO alpha from being ruined by blend ops
        GlStateManager.enableBlend();

        boolean hasBat = hasBattery(mc.player);
        boolean isActive = hasBat && (BraceletInspectHandler.isInspecting || BraceletInspectHandler.isBackgroundRunning);

        if (isActive) {
            // Применяем 2D калибровку FBO
            GlStateManager.pushMatrix();
            GlStateManager.translate(128 + BraceletInspectHandler.uiOffsetX, 128 + BraceletInspectHandler.uiOffsetY, 0);
            GlStateManager.rotate(BraceletInspectHandler.uiRotZ, 0, 0, 1);
            GlStateManager.scale(BraceletInspectHandler.uiScale, BraceletInspectHandler.uiScale, 1.0f);
            GlStateManager.translate(-128, -128, 0);

            FontRenderer font = mc.fontRenderer;
            String textNum = String.valueOf(HeartbeatManager.displayBPM);
            float heartScale = HeartbeatManager.getHeartScale();
            
            int r = com.voltyx.mwccf.geo.BraceletSettings.displayColorR;
            int g = com.voltyx.mwccf.geo.BraceletSettings.displayColorG;
            int b = com.voltyx.mwccf.geo.BraceletSettings.displayColorB;
            
            if (HeartbeatManager.displayBPM >= 150) {
                long time = System.currentTimeMillis();
                // Pulse every 500ms
                if ((time / 250) % 2 == 0) {
                    r = 255; g = 0; b = 0;
                } else {
                    r = 100; g = 0; b = 0; // Dark red
                }
            }

            // ФОН — горизонтальные полосы цвета из конфига (5% яркости)
            drawHorizontalScanlines(r, g, b, 0, 256, 0, 256);
            
            float rf = r / 255.0f;
            float gf = g / 255.0f;
            float bf = b / 255.0f;

            int flickerAlpha = 120 + (int)(Math.random() * 60);
            float flickerAlphaFloat = flickerAlpha / 255.0f;
            int flickerColorText = (flickerAlpha << 24) | (r << 16) | (g << 8) | b;

            // ================= КАРДИОГРАММА =================
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableTexture2D();
            
            GL11.glLineWidth(2.25f);
            drawCardioPath(r, g, b, flickerAlpha);

            // Маска — РОВНО ОДИН РАЗ, сразу после кардиограммы
            drawHorizontalScanlines(r, g, b, 177, 256, 0, 256);

            GlStateManager.enableTexture2D();

        // ================= РАЗДЕЛИТЕЛЬ =================
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(2.25f);
        drawSeparatorLine(r, g, b, flickerAlpha);
        GlStateManager.enableTexture2D();

        // ================= ТЕКСТ =================
        GlStateManager.pushMatrix();
        GlStateManager.translate(216, 150, 0);
        GlStateManager.scale(3.0f, 3.0f, 1.0f);
        font.drawString(textNum, -font.getStringWidth(textNum) / 2, 0, flickerColorText);
        GlStateManager.popMatrix();

        // ================= СЕРДЦЕ =================
        mc.getTextureManager().bindTexture(HEART_ICON);
        GlStateManager.pushMatrix();
        GlStateManager.translate(216, 115, 0);
        GlStateManager.scale(heartScale, heartScale, 1.0f);
        GlStateManager.color(rf, gf, bf, flickerAlphaFloat);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(-24, -24, 0, 0, 48, 48, 48, 48);
        GlStateManager.popMatrix();

        float uiProgress;
        float logoWipeProgress = 1.0f; // 1.0 = fully visible, 0.0 = completely hidden
        
        if (isMWCWeapon) {
            if (com.voltyx.mwccf.geo.BraceletInspectHandler.isBackgroundRunning) {
                float totalProgress = com.voltyx.mwccf.geo.BraceletInspectHandler.prevMwcBootProgress + (com.voltyx.mwccf.geo.BraceletInspectHandler.mwcBootProgress - com.voltyx.mwccf.geo.BraceletInspectHandler.prevMwcBootProgress) * mc.getRenderPartialTicks();
                uiProgress = Math.min(1.0f, totalProgress / 0.25f);
                if (totalProgress > 0.65f) {
                    logoWipeProgress = Math.max(0.0f, 1.0f - (totalProgress - 0.65f) / 0.4f);
                } else {
                    logoWipeProgress = 1.0f;
                }
            } else {
                uiProgress = 0.0f;
                logoWipeProgress = 0.0f;
            }
        } else if (com.voltyx.mwccf.geo.BraceletInspectHandler.isBackgroundRunning) {
            uiProgress = 1.0f;
            logoWipeProgress = 0.0f;
        } else {
            float totalProgress = com.voltyx.mwccf.geo.BraceletInspectHandler.prevInspectProgress + (com.voltyx.mwccf.geo.BraceletInspectHandler.inspectProgress - com.voltyx.mwccf.geo.BraceletInspectHandler.prevInspectProgress) * mc.getRenderPartialTicks();
            float progress = Math.max(0.0f, totalProgress - 2.0f); 
            
            uiProgress = Math.min(1.0f, progress / 0.25f);
            
            if (com.voltyx.mwccf.geo.BraceletInspectHandler.isInspecting) {
                if (progress > 0.65f) {
                    logoWipeProgress = Math.max(0.0f, 1.0f - (progress - 0.65f) / 0.4f);
                }
            } else {
                logoWipeProgress = 0.0f;
            }
        }
        
        if (logoWipeProgress > 0.0f) {
            float lineX = logoWipeProgress * 256.0f;
            int wipeW = (int)lineX;

            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            // Черный фон под логотипом, рисуется только до wipeW
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0, 256, 400.0f).color(0, 0, 0, 255).endVertex();
            buffer.pos(wipeW, 256, 400.0f).color(0, 0, 0, 255).endVertex();
            buffer.pos(wipeW, 0, 400.0f).color(0, 0, 0, 255).endVertex();
            buffer.pos(0, 0, 400.0f).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            
            GlStateManager.enableTexture2D();
            mc.getTextureManager().bindTexture(BOOT_LOGO);
            GlStateManager.color(rf, gf, bf, flickerAlphaFloat);
            
            // Отрисовка текстуры точно по размеру видимого экрана (X: 0..256, Y: 64..192)
            float f = 1.0F / 256.0f;
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0, 192, 0.0D).tex(0, 1.0D).endVertex();
            buffer.pos(wipeW, 192, 0.0D).tex(wipeW * f, 1.0D).endVertex();
            buffer.pos(wipeW, 64, 0.0D).tex(wipeW * f, 0.0D).endVertex();
            buffer.pos(0, 64, 0.0D).tex(0, 0.0D).endVertex();
            tessellator.draw();

            // Линия-ворота, которая стирает логотип
            if (logoWipeProgress < 1.0f) {
                GlStateManager.disableTexture2D();
                GL11.glLineWidth(4.0f);
                buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(wipeW, 64, 500.0f).color(r, g, b, flickerAlpha).endVertex();
                buffer.pos(wipeW, 192, 500.0f).color(r, g, b, flickerAlpha).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
        }
        
        if (uiProgress < 1.0f) {
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            
            float lineY1 = 128.0f;
            float lineY2 = 128.0f;
            
            if (uiProgress > 0.1f) {
                float splitProgress = (uiProgress - 0.1f) / 0.9f; 
                lineY1 = 128.0f - (64.0f * splitProgress);
                lineY2 = 128.0f + (64.0f * splitProgress);
            }
            
            // Черные прямоугольники (шторки)
            float zLevel = 500.0f;
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            // Верхняя
            buffer.pos(0, lineY1, zLevel).color(0, 0, 0, 255).endVertex();
            buffer.pos(256, lineY1, zLevel).color(0, 0, 0, 255).endVertex();
            buffer.pos(256, 0, zLevel).color(0, 0, 0, 255).endVertex();
            buffer.pos(0, 0, zLevel).color(0, 0, 0, 255).endVertex();
            // Нижняя
            buffer.pos(0, 256, zLevel).color(0, 0, 0, 255).endVertex();
            buffer.pos(256, 256, zLevel).color(0, 0, 0, 255).endVertex();
            buffer.pos(256, lineY2, zLevel).color(0, 0, 0, 255).endVertex();
            buffer.pos(0, lineY2, zLevel).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            
            // Линии раздвигающихся шторок
            GL11.glLineWidth(4.0f);
            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            if (uiProgress <= 0.2f) {
                buffer.pos(0, 128, zLevel).color(r, g, b, flickerAlpha).endVertex();
                buffer.pos(256, 128, zLevel).color(r, g, b, flickerAlpha).endVertex();
            } else {
                buffer.pos(0, lineY1, zLevel).color(r, g, b, flickerAlpha).endVertex();
                buffer.pos(256, lineY1, zLevel).color(r, g, b, flickerAlpha).endVertex();
                buffer.pos(0, lineY2, zLevel).color(r, g, b, flickerAlpha).endVertex();
                buffer.pos(256, lineY2, zLevel).color(r, g, b, flickerAlpha).endVertex();
            }
            tessellator.draw();
            
            GlStateManager.enableTexture2D();
        }

        // Горизонтальные сканлинии поверх всего экрана (включая Boot Logo)
        drawScanlineOverlay();

        GlStateManager.popMatrix();
        } // End of isActive

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        // ВАЖНО: НЕ используем GL11.glPushAttrib/glPopAttrib — это создаёт
        // рассинхрон между реальным состоянием GPU и внутренним кэшем
        // GlStateManager, из-за чего последующие вызовы GlStateManager.enableXxx()
        // могут "думать", что состояние уже правильное, и пропускать реальный
        // вызов в GL. Именно это давало утечку на весь рендер игры (небо,
        // фон меню и т.д.). Вместо этого — явный сброс через сам GlStateManager,
        // чтобы кэш и реальность совпадали.
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.colorMask(true, true, true, true);
        GL11.glLineWidth(1.0F);

        net.minecraft.client.renderer.OpenGlHelper.glBindFramebuffer(net.minecraft.client.renderer.OpenGlHelper.GL_FRAMEBUFFER, previousFBO);
        GlStateManager.viewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));
    }

    private static void drawCardioOffsetBlur(float radius, int alpha) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float a = alpha / 255.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f, a);
        for (float[] off : GLOW_OFFSETS) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(off[0] * radius, off[1] * radius, 0);
            drawCardioPath(255, 255, 255, alpha);
            GlStateManager.popMatrix();
        }
    }

    private static void drawSeparatorOffsetBlur(float radius, int alpha) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float a = alpha / 255.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f, a);
        for (float[] off : GLOW_OFFSETS) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(off[0] * radius, off[1] * radius, 0);
            drawSeparatorLine(255, 255, 255, alpha);
            GlStateManager.popMatrix();
        }
    }

    private static void drawTextOffsetBlur(String text, float x, float y, float scale, float radius, int alpha, FontRenderer font) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int width = font.getStringWidth(text);
        int color = (alpha << 24) | 0xFFFFFF;
        for (float[] off : GLOW_OFFSETS) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + off[0] * radius, y + off[1] * radius, 0);
            GlStateManager.scale(scale, scale, 1.0f);
            font.drawString(text, -width / 2, 0, color);
            GlStateManager.popMatrix();
        }
    }

    private static void drawHeartOffsetBlur(float x, float y, float scale, float radius, float alpha) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        for (float[] off : GLOW_OFFSETS) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + off[0] * radius, y + off[1] * radius, 0);
            GlStateManager.scale(scale, scale, 1.0f);
            net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(-24, -24, 0, 0, 48, 48, 48, 48);
            GlStateManager.popMatrix();
        }
    }

    public static boolean bindScreenTexture() {
        if (fbo != null) {
            Minecraft mc = Minecraft.getMinecraft();
            boolean isMWCWeapon = mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() 
                && efw.animation.WeaponTypeHelper.getWeaponType(mc.player.getHeldItemMainhand()) != efw.animation.WeaponTypeHelper.WeaponType.NONE;

            if (BraceletInspectHandler.isInspecting || BraceletInspectHandler.inspectProgress > 0.0f || isMWCWeapon) {
                GlStateManager.bindTexture(fbo.framebufferTexture);
                return true;
            }
        }
        return false;
    }

    // ВАЖНО: теперь реально использует alpha (a), а не хардкодит 255 —
    // именно это и было причиной "непрозрачного свечения".
    private static void drawSeparatorLine(int r, int g, int b, int a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(177, 64, 0).color(r, g, b, a).endVertex();
        buffer.pos(177, 192, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    // ВАЖНО: тоже теперь использует alpha (a) вместо хардкода 255.
    private static void drawCardioPath(int r, int g, int b, int a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        long currentTime = System.currentTimeMillis();
        float speed = 176.0f / 1500.0f; // Crosses 176 pixels in 1.5 seconds

        for (int x = 0; x <= 176; x += 2) {
            float y = 127;
            for (long t : HeartbeatManager.spikes) {
                float spikeCenter = 216.0f - (currentTime - t) * speed; // 216 ensures the spike starts entering from the right
                float dx = x - spikeCenter;
                
                if (dx > -16 && dx < 16) {
                    if (dx < -8) {
                        float p = (dx + 16) / 8.0f;
                        y = 127 + (79 - 127) * p;
                    } else if (dx < 8) {
                        float p = (dx + 8) / 16.0f;
                        y = 79 + (184 - 79) * p;
                    } else {
                        float p = (dx - 8) / 8.0f;
                        y = 184 + (127 - 184) * p;
                    }
                }
            }
            buffer.pos(x, y, 0).color(r, g, b, a).endVertex();
        }

        tessellator.draw();
    }

    private static void drawHorizontalScanlines(int r, int g, int b, int startX, int endX, int startY, int endY) {
        // Very, very dark version of the config display color (5% brightness)
        int bgR = Math.max(0, (int)(r * 0.05f));
        int bgG = Math.max(0, (int)(g * 0.05f));
        int bgB = Math.max(0, (int)(b * 0.05f));
        
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        // Base solid black background
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(startX, endY, 0).color(0, 0, 0, 255).endVertex();
        buffer.pos(endX, endY, 0).color(0, 0, 0, 255).endVertex();
        buffer.pos(endX, startY, 0).color(0, 0, 0, 255).endVertex();
        buffer.pos(startX, startY, 0).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        
        // Continuous horizontal scanlines along X
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        int step = 3;
        int size = 2;
        for (int y = startY; y < endY; y += step) {
            int y2 = Math.min(y + size, endY);
            buffer.pos(startX, y2, 0).color(bgR, bgG, bgB, 255).endVertex();
            buffer.pos(endX, y2, 0).color(bgR, bgG, bgB, 255).endVertex();
            buffer.pos(endX, y, 0).color(bgR, bgG, bgB, 255).endVertex();
            buffer.pos(startX, y, 0).color(bgR, bgG, bgB, 255).endVertex();
        }
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
    }

    private static void drawScanlineOverlay() {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        
        // Horizontal scanline dark gaps across the whole screen X: 0..256, Y: 64..192
        int step = 3;
        for (int y = 64; y < 192; y += step) {
            buffer.pos(0, y + 1, 600.0f).color(0, 0, 0, 180).endVertex();
            buffer.pos(256, y + 1, 600.0f).color(0, 0, 0, 180).endVertex();
            buffer.pos(256, y, 600.0f).color(0, 0, 0, 180).endVertex();
            buffer.pos(0, y, 600.0f).color(0, 0, 0, 180).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }
}
