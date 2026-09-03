package com.voltyx.mwccf.dash;

import efw.biomeinfo.MwccfConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayStamina {

    // Default Style
    private static final ResourceLocation BASE_LAYER = new ResourceLocation("mwccf:textures/sloi_2.png");
    private static final ResourceLocation FULL_LAYER = new ResourceLocation("mwccf:textures/sloi_1.png");
    private static final ResourceLocation HALF_LAYER = new ResourceLocation("mwccf:textures/sloi_3.png");
    private static final ResourceLocation ENERGY_LAYER = new ResourceLocation("mwccf:textures/sloi_5.png");

    // Alt Style
    private static final ResourceLocation ALT_BAR = new ResourceLocation("mwccf:textures/altlockndash.png");
    private static final ResourceLocation ALT_CELL = new ResourceLocation("mwccf:textures/altlockndash2.png");
    private static final ResourceLocation ALT_BAR_BOOST = new ResourceLocation("mwccf:textures/altlockndash3.png");

    // Dash Cooldown — default style
    private static final ResourceLocation DASH_ROLLBAR = new ResourceLocation("mwccf:textures/dashbar.png");
    private static final ResourceLocation DASH_CELL = new ResourceLocation("mwccf:textures/dashbar_2.png");
    private static final int DASH_TEX_W = 25;
    private static final int DASH_TEX_H = 5;
    private static final int MAX_COOLDOWN = 15;
    private static final float FADE_SPEED = 0.05f;

    // Dash Cooldown — alt style
    private static final ResourceLocation ALT_DASH_BAR = new ResourceLocation("mwccf:textures/gui/alt_dash_bar.png");
    private static final ResourceLocation ALT_DASH_CELL = new ResourceLocation("mwccf:textures/gui/alt_dash_cell.png");
    private static final int ALT_TEX_W = 18;
    private static final int ALT_TEX_H = 3;

    // Default style animation state
    private static float dashAlpha = 1.0f;
    private static boolean fading = false;

    // Alt style: pure client-side visual timer
    private static boolean altDashActive = false;
    private static float altDashProgress = 0.0f; // 0.0 (just dashed) -> 1.0 (ready)
    private static float slideProgress = 0.0f;   // 0.0 (hidden) -> 1.0 (visible)
    private static long lastSlideMs = 0;
    private static int prevRawCooldown = 0;

    // Default style
    private static int lastRawCooldown = 0;
    private static int dashCooldownMax = MAX_COOLDOWN;

    public static void triggerAltDashAnimation() {
        altDashActive = true;
        altDashProgress = 0.0f;
    }

    // -------------------------------------------------------------------------

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPreRenderAir(RenderGameOverlayEvent.Pre event) {
        if (!MwccfConfig.dashAndStamina.overlay.useAlternativeStaminaHUD) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
                GuiIngameForge.right_height += 10;
            }
        } else {
            if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.HELMET)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null)
            return;
        GameType gamemode = mc.playerController.getCurrentGameType();
        if (gamemode != GameType.SURVIVAL && gamemode != GameType.ADVENTURE)
            return;

        double stamina = player.getEntityData().getDouble("stamina");

        boolean hasEnergyBoost = false;
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getPotion() == PotionEnergyBoost.INSTANCE) {
                hasEnergyBoost = true;
                break;
            }
        }

        if (MwccfConfig.dashAndStamina.overlay.useAlternativeStaminaHUD) {
            renderDashOverlay(mc, player, event.getResolution());
            renderAltOverlay(mc, player, stamina, hasEnergyBoost, event.getResolution());
        } else {
            renderDefaultOverlay(mc, player, stamina, hasEnergyBoost, event.getResolution());
            renderDashOverlay(mc, player, event.getResolution());
        }
    }

    // -------------------------------------------------------------------------

    private void renderDefaultOverlay(Minecraft mc, EntityPlayer player, double stamina,
            boolean hasEnergyBoost, ScaledResolution scaled) {
        int barWidth = 9;
        int overlap = 1;
        int totalBars = 10;

        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();

        int hotbarY = screenHeight - 10;
        int baseY = hotbarY - MwccfConfig.dashAndStamina.overlay.overlayBaseY - 39;

        if (Loader.isModLoaded("simpledifficulty"))
            baseY -= 10;

        int baseX = screenWidth / 2 + MwccfConfig.dashAndStamina.overlay.overlayBaseX - 82 + 164;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableAlpha();

        mc.renderEngine.bindTexture(BASE_LAYER);
        for (int i = 0; i < totalBars; i++) {
            int x = baseX - i * (barWidth - overlap);
            mc.ingameGUI.drawModalRectWithCustomSizedTexture(x, baseY, 0, 0, barWidth, barWidth, barWidth, barWidth);
        }

        if (hasEnergyBoost) {
            mc.renderEngine.bindTexture(ENERGY_LAYER);
            for (int i = 0; i < totalBars; i++) {
                int x = baseX - i * (barWidth - overlap);
                mc.ingameGUI.drawModalRectWithCustomSizedTexture(x, baseY, 0, 0, barWidth, barWidth, barWidth,
                        barWidth);
            }
        }

        mc.renderEngine.bindTexture(FULL_LAYER);
        for (int i = 0; i < totalBars; i++) {
            if (stamina >= (i + 1) * 2) {
                int x = baseX - i * (barWidth - overlap);
                mc.ingameGUI.drawModalRectWithCustomSizedTexture(x, baseY, 0, 0, barWidth, barWidth, barWidth,
                        barWidth);
            }
        }

        mc.renderEngine.bindTexture(HALF_LAYER);
        for (int i = 0; i < totalBars; i++) {
            if (stamina >= (i * 2 + 1)) {
                int x = baseX - i * (barWidth - overlap);
                mc.ingameGUI.drawModalRectWithCustomSizedTexture(x, baseY, 0, 0, barWidth, barWidth, barWidth,
                        barWidth);
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    // -------------------------------------------------------------------------

    private void renderAltOverlay(Minecraft mc, EntityPlayer player, double stamina,
            boolean hasEnergyBoost, ScaledResolution scaled) {
        int TEX_W = 182;
        int TEX_H = 5;

        stamina = Math.max(0, Math.min(stamina, MwccfConfig.dashAndStamina.stamina.maxStamina));

        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();

        int hotbarY = screenHeight - 10;
        int posY = hotbarY
                - MwccfConfig.dashAndStamina.overlay.overlayBaseY
                - MwccfConfig.dashAndStamina.overlay.altOverlayBaseY
                - TEX_H - 40 + 26;
        int posX = screenWidth / 2 - TEX_W / 2
                + MwccfConfig.dashAndStamina.overlay.overlayBaseX
                + MwccfConfig.dashAndStamina.overlay.altOverlayBaseX;

        int filledCells = (int) Math.floor(stamina);

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableAlpha();

        mc.renderEngine.bindTexture(ALT_BAR);
        mc.ingameGUI.drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, TEX_W, TEX_H, TEX_W, TEX_H);

        if (filledCells > 0) {
            mc.renderEngine.bindTexture(ALT_CELL);
            for (int i = 0; i < filledCells; i++) {
                int offsetX = i * 3;
                if (i == 59)
                    offsetX -= 2;
                mc.ingameGUI.drawModalRectWithCustomSizedTexture(posX + offsetX, posY, 0, 0, TEX_W, TEX_H, TEX_W,
                        TEX_H);
            }
        }

        if (hasEnergyBoost) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            mc.renderEngine.bindTexture(ALT_BAR_BOOST);
            mc.ingameGUI.drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, TEX_W, TEX_H, TEX_W, TEX_H);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    // -------------------------------------------------------------------------

    private void renderDashOverlay(Minecraft mc, EntityPlayer player, ScaledResolution scaled) {
        DashCapability.IDashData cap = player.getCapability(DashCapability.ROLL_CAP, null);
        if (cap == null)
            return;

        int rawCooldown = cap.getCooldown();
        boolean isAlt = MwccfConfig.dashAndStamina.overlay.useAlternativeStaminaHUD;

        if (isAlt) {
            // Fallback trigger if server capability cooldown started
            if (prevRawCooldown <= 0 && rawCooldown > 0 && !altDashActive) {
                triggerAltDashAnimation();
            }
            prevRawCooldown = rawCooldown;

            // Frame delta time (freezes when game is paused on ESC)
            long nowMs = System.currentTimeMillis();
            long deltaMs = (lastSlideMs == 0) ? 0 : Math.min(nowMs - lastSlideMs, 50);
            lastSlideMs = nowMs;
            float dt = mc.isGamePaused() ? 0f : (deltaMs / 1000f);

            if (altDashActive) {
                // Cooldown duration in seconds from config (ticks * 0.05s)
                float durationSec = Math.max(0.1f, MwccfConfig.dashAndStamina.dash.cooldownTicks * 0.05f);
                altDashProgress += dt / durationSec;
                // Slide up smoothly (~150ms)
                slideProgress = Math.min(1.0f, slideProgress + 6.0f * dt);

                if (altDashProgress >= 1.0f) {
                    altDashProgress = 1.0f;
                    altDashActive = false; // Cooldown finished, slide back down
                }
            } else {
                // Slide down smoothly (~200ms)
                slideProgress = Math.max(0.0f, slideProgress - 5.0f * dt);
            }

            if (slideProgress <= 0f && !altDashActive) return;
            renderAltDashOverlay(mc, scaled, altDashProgress);
            return;
        }

        // --- Default style: оставляем как было ---
        if (rawCooldown > lastRawCooldown && rawCooldown > 0) {
            dashCooldownMax = rawCooldown;
        }
        lastRawCooldown = rawCooldown;

        int cooldown = Math.min(rawCooldown, MAX_COOLDOWN);

        if (cooldown == 0) {
            fading = true;
            if (dashAlpha > 0)
                dashAlpha -= FADE_SPEED;
        } else {
            fading = false;
            dashAlpha = 1.0f;
        }

        if (dashAlpha <= 0f)
            return;

        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();

        int hotbarY = screenHeight - 10;
        int posY = hotbarY - MwccfConfig.dashAndStamina.overlay.dashBarBaseY - DASH_TEX_H - 40;
        int posX = screenWidth / 2 + MwccfConfig.dashAndStamina.overlay.dashBarBaseX - 13;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1F, 1F, 1F, dashAlpha);
        GlStateManager.disableAlpha();

        mc.renderEngine.bindTexture(DASH_ROLLBAR);
        mc.ingameGUI.drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, DASH_TEX_W, DASH_TEX_H, DASH_TEX_W,
                DASH_TEX_H);

        mc.renderEngine.bindTexture(DASH_CELL);
        int filledCells = MAX_COOLDOWN - cooldown;
        if (filledCells == 0)
            filledCells = 1;

        for (int i = 0; i < filledCells; i++) {
            mc.ingameGUI.drawModalRectWithCustomSizedTexture(posX + i, posY, 0, 0, DASH_TEX_W, DASH_TEX_H, DASH_TEX_W,
                    DASH_TEX_H);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    // -------------------------------------------------------------------------

    private void renderAltDashOverlay(Minecraft mc, ScaledResolution scaled, float progress) {
        int STAMINA_TEX_H = 5;
        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();

        int hotbarY = screenHeight - 10;
        int staminaY = hotbarY
                - MwccfConfig.dashAndStamina.overlay.overlayBaseY
                - MwccfConfig.dashAndStamina.overlay.altOverlayBaseY
                - STAMINA_TEX_H - 40 + 26;

        int extraY = MwccfConfig.dashAndStamina.overlay.altDashBarOffsetY;

        int targetY = staminaY - ALT_TEX_H + extraY;

        int staminaX = screenWidth / 2
                - 182 / 2
                + MwccfConfig.dashAndStamina.overlay.overlayBaseX
                + MwccfConfig.dashAndStamina.overlay.altOverlayBaseX;
        int dashBarX = staminaX + 182 / 2 - ALT_TEX_W / 2;

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,       GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableAlpha();

        // Subpixel smooth translation using OpenGL float matrix
        GlStateManager.pushMatrix();
        float translateY = (1.0f - slideProgress) * ALT_TEX_H;
        GlStateManager.translate(0.0f, translateY, 0.0f);

        mc.renderEngine.bindTexture(ALT_DASH_BAR);
        mc.ingameGUI.drawModalRectWithCustomSizedTexture(
                dashBarX, targetY, 0, 0,
                ALT_TEX_W, ALT_TEX_H, ALT_TEX_W, ALT_TEX_H);

        // Smooth visual fill based on progress (0.0 -> 1.0)
        int filledCells = Math.round(progress * MAX_COOLDOWN);
        if (filledCells <= 0) filledCells = 1; // always show at least 1 cell when bar is visible
        if (filledCells > MAX_COOLDOWN) filledCells = MAX_COOLDOWN;
        mc.renderEngine.bindTexture(ALT_DASH_CELL);
        for (int i = 0; i < filledCells; i++) {
            mc.ingameGUI.drawModalRectWithCustomSizedTexture(
                    dashBarX + i, targetY, 0, 0,
                    ALT_TEX_W, ALT_TEX_H, ALT_TEX_W, ALT_TEX_H);
        }

        GlStateManager.popMatrix();

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}