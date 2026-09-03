package com.voltyx.mwccf.client.modding;

import com.paneedah.weaponlib.AttachmentCategory;
import com.paneedah.weaponlib.ClientModContext;
import com.paneedah.weaponlib.CompatibleAttachment;
import com.paneedah.weaponlib.ItemAttachment;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.UniversalSoundLookup;
import com.paneedah.weaponlib.Weapon;
import com.paneedah.weaponlib.WeaponAttachmentAspect;
import com.paneedah.weaponlib.WeaponAttachmentAspect.FlaggedAttachment;
import com.paneedah.weaponlib.WeaponState;
import com.paneedah.weaponlib.config.BalancePackManager;
import com.voltyx.mwccf.backpack.BackpackBaubles;
import com.voltyx.mwccf.client.inspect.InspectDustManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiWeaponModding extends GuiScreen {

    private PlayerWeaponInstance pwi;
    private final GuiScreen parentScreen;
    private final Minecraft mc;
    private final boolean isModifiable;

    // Smooth camera / positioning state
    private float currentPitch = 0.0f;
    private float targetPitch = 0.0f;
    private float currentYaw = 0.0f;
    private float targetYaw = 0.0f;
    private float currentZoom = 1.0f;
    private float targetZoom = 1.0f;

    private float currentCamX = 0.0f;
    private float targetCamX = 0.0f;
    private float currentCamY = 0.0f;
    private float targetCamY = 0.0f;

    // Fade Transition
    private float fadeProgress = 0.0f;
    private boolean isExiting = false;
    private static final float FADE_SPEED = 5.0f;

    // Mouse drag for Free-Look (LMB / RMB drag)
    private boolean isDraggingLMB = false;
    private boolean isDraggingRMB = false;
    private int prevMouseX;
    private int prevMouseY;
    private long lastFrameTime = 0;
    private final InspectDustManager dustManager = new InspectDustManager();

    // Selected attachment category slot
    private AttachmentCategory activeCategory = null;

    // Drop-up scroll & animation
    private float scrollOffset = 0.0f;
    private float targetScrollOffset = 0.0f;

    // Hovered tooltips
    private String tooltipTitle = null;
    private List<String> tooltipLines = new ArrayList<>();
    private int tooltipColor = 0xFFFFFF;

    // Categories list for bottom bar
    private final List<AttachmentCategory> availableCategories = new ArrayList<>();

    public GuiWeaponModding(PlayerWeaponInstance pwi, GuiScreen parentScreen) {
        this.pwi = pwi;
        this.parentScreen = parentScreen;
        this.mc = Minecraft.getMinecraft();
        this.isModifiable = pwi != null && isWeaponInPossession(mc.player, pwi.getItemStack());
        initCategories();
    }

    public static boolean isWeaponInPossession(EntityPlayer player, ItemStack targetWeapon) {
        if (player == null || targetWeapon == null || targetWeapon.isEmpty())
            return false;

        // 1. Check main inventory (0..35)
        for (ItemStack stack : player.inventory.mainInventory) {
            if (!stack.isEmpty() && isMatchingWeapon(stack, targetWeapon))
                return true;
        }

        // 2. Check armor slots
        for (ItemStack stack : player.inventory.armorInventory) {
            if (!stack.isEmpty() && isMatchingWeapon(stack, targetWeapon))
                return true;
        }

        // 3. Check offhand slot
        for (ItemStack stack : player.inventory.offHandInventory) {
            if (!stack.isEmpty() && isMatchingWeapon(stack, targetWeapon))
                return true;
        }

        // 4. Check chestplate backpack
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (checkContainerContains(chest, targetWeapon))
            return true;

        // 5. Check Baubles backpack
        try {
            ItemStack bauble = BackpackBaubles.getBaubleBackpack(player);
            if (checkContainerContains(bauble, targetWeapon))
                return true;
        } catch (Throwable ignored) {
        }

        // 6. Check any item in player inventory with ItemHandler capability
        // (backpacks/bags in inventory)
        for (ItemStack stack : player.inventory.mainInventory) {
            if (!stack.isEmpty() && checkContainerContains(stack, targetWeapon))
                return true;
        }

        return false;
    }

    private static boolean checkContainerContains(ItemStack containerStack, ItemStack target) {
        if (containerStack == null || containerStack.isEmpty())
            return false;
        if (containerStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler handler = containerStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack slotStack = handler.getStackInSlot(i);
                    if (!slotStack.isEmpty() && isMatchingWeapon(slotStack, target)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isMatchingWeapon(ItemStack a, ItemStack b) {
        if (a == b)
            return true;
        if (a == null || b == null || a.isEmpty() || b.isEmpty())
            return false;
        if (a.getItem() != b.getItem())
            return false;
        return ItemStack.areItemStackTagsEqual(a, b);
    }

    private void initCategories() {
        availableCategories.clear();
        if (pwi == null || pwi.getWeapon() == null)
            return;
        Weapon weapon = pwi.getWeapon();

        AttachmentCategory[] allCats = new AttachmentCategory[] {
                AttachmentCategory.SILENCER,
                AttachmentCategory.SCOPE,
                AttachmentCategory.GUARD,
                AttachmentCategory.LASER,
                AttachmentCategory.GRIP,
                AttachmentCategory.STOCK,
                AttachmentCategory.MAGAZINE,
                AttachmentCategory.FRONTSIGHT,
                AttachmentCategory.RECEIVER,
                AttachmentCategory.BACKGRIP,
                AttachmentCategory.RAILING
        };

        pwi.getActiveAttachmentIds();

        if (!isModifiable) {
            // В режиме только осмотра показываем только те категории, где установлен модуль
            for (AttachmentCategory cat : allCats) {
                ItemAttachment<Weapon> equipped = pwi.getAttachmentItemWithCategory(cat);
                if (equipped != null) {
                    availableCategories.add(cat);
                }
            }
            return;
        }

        for (AttachmentCategory cat : allCats) {
            java.util.Collection<com.paneedah.weaponlib.CompatibleAttachment<? extends com.paneedah.weaponlib.AttachmentContainer>> compatibles = weapon
                    .getCompatibleAttachments(cat);
            if (compatibles.isEmpty())
                continue;

            // Filter categories with only 1 attachment if it cannot be removed or swapped
            if (!weapon.isCategoryRemovable(cat) && compatibles.size() <= 1) {
                continue;
            }

            // Filter categories with only 1 permanent (non-removable, non-swappable)
            // attachment
            if (compatibles.size() == 1) {
                com.paneedah.weaponlib.CompatibleAttachment<? extends com.paneedah.weaponlib.AttachmentContainer> single = compatibles
                        .iterator().next();
                if (single.isPermanent())
                    continue; // Skip: only one and can't be changed
            }

            availableCategories.add(cat);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        this.dustManager.init(this.width, this.height);
        this.lastFrameTime = System.currentTimeMillis();
        this.fadeProgress = 0.0f;
        this.isExiting = false;

        com.paneedah.weaponlib.animation.AnimationModeProcessor.getInstance().setActiveCategory(null);

        // Reset to weapon center
        updateWeaponCenter(false);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        com.paneedah.weaponlib.animation.AnimationModeProcessor.getInstance().setActiveCategory(null);
    }

    private void updateWeaponCenter(boolean instant) {
        WeaponModdingCenterHelper.WeaponBounds bounds = WeaponModdingCenterHelper.computeBounds(pwi);

        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = Math.max(1, sr.getScaleFactor());
        float guiScaleRatio = 6.0f / (float) scaleFactor;

        if (activeCategory == null) {
            targetCamX = bounds.centerX;
            targetCamY = bounds.centerY;
            if (!isDraggingLMB && !isDraggingRMB) {
                targetPitch = 0.0f;
                targetYaw = 0.0f;
                targetZoom = 1.0f;
            }
        } else {
            float[] focus = WeaponModdingCenterHelper.getExactAttachmentOffset(pwi, activeCategory, guiScaleRatio);
            targetCamX = focus[0];
            targetCamY = focus[1];
            targetPitch = 0.0f;
            targetYaw = 0.0f;
            targetZoom = 1.20f; // Небольшое плавное приближение к выбранному модулю
        }

        if (instant) {
            currentCamX = targetCamX;
            currentCamY = targetCamY;
            currentZoom = targetZoom;
            currentPitch = targetPitch;
            currentYaw = targetYaw;
        }
    }

    public static class ScreenBounds {
        public float minX;
        public float minY;
        public float maxX;
        public float maxY;
        public float centerX;
        public float centerY;
        public float width;
        public float height;
        public boolean valid;
    }

    private final ScreenBounds outlineBounds = new ScreenBounds();

    public ScreenBounds getOutlineBounds() {
        return outlineBounds;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        long now = System.currentTimeMillis();
        float deltaSec = lastFrameTime > 0 ? (now - lastFrameTime) / 1000.0f : 0.016f;
        lastFrameTime = now;

        // Smooth camera interpolation
        float lerpSpeed = Math.min(1.0f, deltaSec * 10.0f);
        currentPitch += (targetPitch - currentPitch) * lerpSpeed;
        currentYaw += (targetYaw - currentYaw) * lerpSpeed;
        currentZoom += (targetZoom - currentZoom) * lerpSpeed;
        currentCamX += (targetCamX - currentCamX) * lerpSpeed;
        currentCamY += (targetCamY - currentCamY) * lerpSpeed;

        tooltipTitle = null;
        tooltipLines.clear();

        // 1. Dark Vignette / Gradient Background
        drawModernBackground(mouseX, mouseY);

        // 2. Render 3D Weapon Model (Flat by default)
        renderWeapon3D(mouseX, mouseY);

        // Clear Depth buffer so ALL 2D GUI elements, drop-up menus, and tooltips render
        // strictly on top of the 3D model!
        GlStateManager.clear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.disableDepth();

        // 3. Render Top Weapon Info & Real-Time Stats
        renderWeaponStatsHeader();

        // 4. Render Active Category Drop-up Menu (if open)
        if (activeCategory != null) {
            renderAttachmentDropUpMenu(mouseX, mouseY, deltaSec);
        }

        // 5. Render Bottom Category Slots Bar
        renderBottomCategoryBar(mouseX, mouseY);

        // 6. Tooltip (if hovered) - strictly rendered last at high Z-level
        if (tooltipTitle != null) {
            GlStateManager.clear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT);
            renderCustomTooltip(mouseX, mouseY);
        }
    }

    private void drawModernBackground(int mouseX, int mouseY) {
        // Сохраняем чистое состояние OpenGL перед отрисовкой фона
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();

        // Solid clean ultra dark pitch midnight background
        drawRect(0, 0, this.width, this.height, 0xFF020306);

        // Включаем альфу и бленд для корректной отрисовки частиц
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        // Ambient Floating Dust particles
        dustManager.updateAndRender(this.width, this.height, mc);

        // Восстанавливаем состояние OpenGL к тому, каким оно было до фона
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        // КРИТИЧЕСКИ ВАЖНО: Сбрасываем цвет на непрозрачный белый (1, 1, 1, 1).
        // Если частицы пыли оставили прозрачность (alpha = 0), оружие вывернется
        // наизнанку.
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderWeapon3D(int mouseX, int mouseY) {
        if (pwi == null || pwi.getItemStack().isEmpty())
            return;

        ItemStack stack = pwi.getItemStack();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        // Очищаем буфер глубины для VMW оружия, иначе оно будет прозрачным/вывернутым!
        GlStateManager.clear(256);

        GlStateManager.depthFunc(515); // GL_LEQUAL
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableNormalize();
        org.lwjgl.opengl.GL11.glLightModeli(org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_TWO_SIDE,
                org.lwjgl.opengl.GL11.GL_TRUE);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderHelper.enableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = Math.max(1, sr.getScaleFactor());
        float guiScaleRatio = 6.0f / (float) scaleFactor;

        float centerX = this.width / 2.0f + currentCamX * guiScaleRatio;
        float centerY = (this.height / 2.0f - 15.0f) + currentCamY * guiScaleRatio;

        GlStateManager.translate(centerX, centerY, 50.0f);

        float baseScale = WeaponModdingCenterHelper.getBaseScale(pwi) * currentZoom * guiScaleRatio
                * com.voltyx.mwccf.client.inspect.ItemInspectConfig.getGlobalCustomizationWeaponScale();
        GlStateManager.scale(baseScale, baseScale, baseScale);

        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(currentPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(currentYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);

        // 1. Отрисовываем оружие со всеми деталями
        com.paneedah.weaponlib.animation.AnimationModeProcessor.getInstance().setActiveCategory(null);
        RenderHelper.enableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CW);
        mc.getRenderItem().renderItem(stack, TransformType.THIRD_PERSON_LEFT_HAND);
        org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CCW);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 2. Отрисовываем чистую трафаретную (Stencil) X-Ray ОБВОДКУ активного модуля
        if (activeCategory != null && pwi.getAttachmentItemWithCategory(activeCategory) != null) {
            boolean stencilWasEnabled = org.lwjgl.opengl.GL11.glIsEnabled(org.lwjgl.opengl.GL11.GL_STENCIL_TEST);
            try {
                com.paneedah.weaponlib.animation.AnimationModeProcessor.getInstance().setActiveCategory(activeCategory);

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_STENCIL_TEST);
                org.lwjgl.opengl.GL11.glStencilMask(0xFF);
                org.lwjgl.opengl.GL11.glClear(org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT);

                com.paneedah.weaponlib.LaserBeamRenderer.suppressRender = true;

                // ---------- ПАСС A: Помечаем силуэт модуля в stencil ----------
                GlStateManager.colorMask(false, false, false, false);
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();

                org.lwjgl.opengl.GL11.glStencilFunc(org.lwjgl.opengl.GL11.GL_ALWAYS, 1, 0xFF);
                org.lwjgl.opengl.GL11.glStencilOp(org.lwjgl.opengl.GL11.GL_REPLACE, org.lwjgl.opengl.GL11.GL_REPLACE,
                        org.lwjgl.opengl.GL11.GL_REPLACE);
                org.lwjgl.opengl.GL11.glStencilMask(0xFF);

                try {
                    // ИСПРАВЛЕНИЕ: Инвертируем грани для VMW перед рендером в Stencil
                    org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CW);
                    mc.getRenderItem().renderItem(stack, TransformType.THIRD_PERSON_LEFT_HAND);
                } finally {
                    // ИСПРАВЛЕНИЕ: Возвращаем грани обратно в блок finally, чтобы гарантировать
                    // сброс
                    org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CCW);
                    GlStateManager.colorMask(true, true, true, true);
                }

                // ---------- ПАСС B: Отрисовка offset-обводки ----------
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.55F);

                org.lwjgl.opengl.GL11.glStencilFunc(org.lwjgl.opengl.GL11.GL_NOTEQUAL, 1, 0xFF);
                org.lwjgl.opengl.GL11.glStencilOp(org.lwjgl.opengl.GL11.GL_KEEP, org.lwjgl.opengl.GL11.GL_KEEP,
                        org.lwjgl.opengl.GL11.GL_KEEP);

                float d = 0.0055f;
                int samples = 8;
                for (int i = 0; i < samples; i++) {
                    double angle = (Math.PI * 2.0 * i) / (double) samples;
                    float offX = (float) (Math.cos(angle) * d);
                    float offY = (float) (Math.sin(angle) * d);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.0f, offY, offX);

                    // ИСПРАВЛЕНИЕ: Инвертируем грани для каждого семпла обводки
                    org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CW);
                    mc.getRenderItem().renderItem(stack, TransformType.THIRD_PERSON_LEFT_HAND);
                    org.lwjgl.opengl.GL11.glFrontFace(org.lwjgl.opengl.GL11.GL_CCW);

                    GlStateManager.popMatrix();
                }

                updateAttachmentScreenBounds(scaleFactor);

                if (!stencilWasEnabled) {
                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_STENCIL_TEST);
                }

                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.popAttrib();
                GlStateManager.popMatrix();

            } finally {
                com.paneedah.weaponlib.LaserBeamRenderer.suppressRender = false;
                com.paneedah.weaponlib.animation.AnimationModeProcessor.getInstance().setActiveCategory(null);
            }
        } else {
            outlineBounds.valid = false;
        }

        // ИСПРАВЛЕНИЕ: Обязательно возвращаем Culling на место, чтобы не сломать рендер
        // мира/других GUI!
        GlStateManager.enableCull();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        // Total reset of 2D state
        GlStateManager.setActiveTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void updateAttachmentScreenBounds(int scaleFactor) {
        if (pwi == null || activeCategory == null) {
            outlineBounds.valid = false;
            return;
        }
        float guiScaleRatio = 6.0f / (float) scaleFactor;
        float[] focus = WeaponModdingCenterHelper.getExactAttachmentOffset(pwi, activeCategory, guiScaleRatio);
        WeaponModdingCenterHelper.WeaponBounds bounds = WeaponModdingCenterHelper.computeBounds(pwi);
        float baseScale = WeaponModdingCenterHelper.getBaseScale(pwi) * currentZoom * guiScaleRatio;

        // Реальный экранный центр детали с учетом плавной интерполяции камеры
        float attCenterX = this.width / 2.0f + (currentCamX - focus[0]) * guiScaleRatio;
        float attCenterY = (this.height / 2.0f - 15.0f) + (currentCamY - focus[1]) * guiScaleRatio;

        // Экранные габариты рамки обводки
        float halfW = Math.max(16.0f, baseScale * 0.22f);
        float halfH = Math.max(16.0f, baseScale * 0.18f);

        outlineBounds.minX = attCenterX - halfW;
        outlineBounds.maxX = attCenterX + halfW;
        outlineBounds.minY = attCenterY - halfH;
        outlineBounds.maxY = attCenterY + halfH;
        outlineBounds.width = halfW * 2.0f;
        outlineBounds.height = halfH * 2.0f;
        outlineBounds.centerX = attCenterX;
        outlineBounds.centerY = attCenterY;
        outlineBounds.valid = true;
    }

    private boolean isRussian() {
        try {
            String lang = mc.getLanguageManager() != null && mc.getLanguageManager().getCurrentLanguage() != null
                    ? mc.getLanguageManager().getCurrentLanguage().getLanguageCode()
                    : "en_us";
            return lang != null && lang.toLowerCase().startsWith("ru");
        } catch (Throwable t) {
            return false;
        }
    }

    private void renderWeaponStatsHeader() {
        if (pwi == null || pwi.getWeapon() == null)
            return;
        Weapon weapon = pwi.getWeapon();

        int margin = 14;

        // Stats card box 1 (Core Stats) at top-left
        int statsX = margin;
        int statsY = margin;
        int statsW = 136;
        int statsH = 50;

        drawRoundedRect(statsX - 4, statsY - 3, statsW, statsH, 0x60000000, 0x35FFFFFF);

        com.paneedah.weaponlib.stats.AttachmentStatsManager.EffectiveWeaponStats effStats = com.paneedah.weaponlib.stats.AttachmentStatsManager
                .getEffectiveStats(pwi);

        float damage = (float) BalancePackManager.getNetGunDamage(weapon);
        float firerate = pwi.getFireRate();
        float recoil = pwi.getRecoil() * (float) effStats.recoilMultiplier;
        float inaccuracy = pwi.getInaccuracy() * (float) effStats.hipSpreadMultiplier;

        renderStatBar(statsX, statsY, isRussian() ? "УРОН" : "DAMAGE", String.format("%.1f", damage),
                Math.min(1.0f, damage / 20.0f), 0xFF5555);
        renderStatBar(statsX, statsY + 10, isRussian() ? "ТЕМП СТРЕЛЬБЫ" : "FIRE RATE",
                String.format("%.0f RPM", firerate * 600.0f), Math.min(1.0f, firerate), 0x55FF55);
        renderStatBar(statsX, statsY + 20, isRussian() ? "ОТДАЧА КАМЕРЫ" : "CAMERA RECOIL",
                String.format("%.1f", recoil),
                Math.min(1.0f, recoil / 15.0f), 0xFFA040);
        renderStatBar(statsX, statsY + 30, isRussian() ? "ТОЧНОСТЬ В ПРИЦЕЛЕ" : "AIM ACCURACY",
                String.format("%.1f",
                        Math.max(0.0f, 10.0f - (pwi.getInaccuracy() * (float) effStats.aimSpreadMultiplier))),
                Math.min(1.0f, (10.0f - (pwi.getInaccuracy() * (float) effStats.aimSpreadMultiplier)) / 10.0f),
                0x55FFFF);

        // Stats card box 2 (Handling & Modifiers) to the right of card 1
        int stats2X = statsX + statsW + 8;
        int stats2W = 136;
        int stats2H = 50;

        drawRoundedRect(stats2X - 4, statsY - 3, stats2W, stats2H, 0x60000000, 0x35FFFFFF);

        double handKick = effStats.visualRecoilMultiplier;
        double adsSpeed = effStats.adsSpeedMultiplier;
        double drawSpeed = effStats.drawSpeedMultiplier;
        double reloadSpeed = effStats.reloadSpeedMultiplier;
        double weight = effStats.totalWeight;

        renderStatBar(stats2X, statsY, isRussian() ? "ОТДАЧА В РУКАХ" : "HAND KICK",
                String.format("%.0f%%", handKick * 100.0), Math.min(1.0f, (float) (handKick / 1.5)), 0xFF8844);
        renderStatBar(stats2X, statsY + 10, isRussian() ? "СКОРОСТЬ ВСКИДКИ" : "ADS SPEED",
                String.format("%.0f%%", adsSpeed * 100.0), Math.min(1.0f, (float) (adsSpeed / 1.5)), 0x44FF88);
        renderStatBar(stats2X, statsY + 20, isRussian() ? "СКОРОСТЬ ДОСТАВАНИЯ" : "DRAW SPEED",
                String.format("%.0f%%", drawSpeed * 100.0), Math.min(1.0f, (float) (drawSpeed / 1.5)), 0x44AAFF);
        renderStatBar(stats2X, statsY + 30, isRussian() ? "СКОРОСТЬ ПЕРЕЗАРЯДКИ" : "RELOAD SPEED",
                String.format("%.0f%%", reloadSpeed * 100.0), Math.min(1.0f, (float) (reloadSpeed / 1.5)), 0xDDAA44);

        // Weapon Display Name & Weight at top-right
        String weaponName = weapon.getItemStackDisplayName(pwi.getItemStack());
        GlStateManager.pushMatrix();
        float titleScale = 1.15f;
        int nameW = (int) (this.fontRenderer.getStringWidth(weaponName) * titleScale);
        int titleX = this.width - margin - nameW;
        GlStateManager.translate(titleX, margin, 0);
        GlStateManager.scale(titleScale, titleScale, 1.0f);
        this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD + TextFormatting.BOLD.toString() + weaponName, 0, 0,
                0xFFFFFF);
        GlStateManager.popMatrix();

        // Parts Weight under weapon title
        String weightText = (isRussian() ? "Вес модулей: " : "Parts Weight: ") + String.format("%.2f кг", weight);
        GlStateManager.pushMatrix();
        float weightScale = 0.8f;
        int weightW = (int) (this.fontRenderer.getStringWidth(weightText) * weightScale);
        int weightX = this.width - margin - weightW;
        GlStateManager.translate(weightX, margin + 14, 0);
        GlStateManager.scale(weightScale, weightScale, 1.0f);
        this.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + weightText, 0, 0, 0xAAAAAA);
        GlStateManager.popMatrix();
    }

    private void renderStatBar(int x, int y, String label, String value, float fraction, int barColor) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(0.72f, 0.72f, 1.0f);
        this.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + label, 0, 0, 0xAAAAAA);
        this.fontRenderer.drawStringWithShadow(TextFormatting.WHITE + value, 130, 0, 0xFFFFFF);
        GlStateManager.popMatrix();

        int barW = 132;
        int barH = 2;
        int barY = y + 7;

        drawRect(x, barY, x + barW, barY + barH, 0x60000000);
        int fillW = (int) (barW * fraction);
        if (fillW > 0) {
            drawRect(x, barY, x + fillW, barY + barH, 0xFF000000 | barColor);
        }
    }

    private void renderBottomCategoryBar(int mouseX, int mouseY) {
        if (availableCategories.isEmpty())
            return;

        int slotSize = 28;
        int gap = 4;
        int totalW = availableCategories.size() * slotSize + (availableCategories.size() - 1) * gap;
        int startX = (this.width - totalW) / 2;
        int startY = this.height - 42;

        for (int i = 0; i < availableCategories.size(); i++) {
            AttachmentCategory cat = availableCategories.get(i);
            int slotX = startX + i * (slotSize + gap);
            int slotY = startY;

            boolean isSelected = (activeCategory == cat);
            boolean isHovered = mouseX >= slotX && mouseX <= slotX + slotSize && mouseY >= slotY
                    && mouseY <= slotY + slotSize;

            ItemAttachment<Weapon> equipped = pwi.getAttachmentItemWithCategory(cat);
            boolean hasEquipped = (equipped != null);

            // Slot Background
            int bgColor = isSelected ? 0x902A3545 : (isHovered ? 0x70202530 : 0x5010151C);
            int borderColor = isSelected ? 0xFFFFFFFF
                    : (hasEquipped ? 0xFFFFFFFF : (isHovered ? 0xC0FFFFFF : 0x40FFFFFF));

            drawRoundedRect(slotX, slotY, slotSize, slotSize, bgColor, borderColor);

            // Если в слоте установлен модуль - рисуем четкую белую обводку толщиной 2px
            if (hasEquipped) {
                drawOutlineRect(slotX, slotY, slotSize, slotSize, 0xFFFFFFFF, 2);
            }

            // Draw Category Icon or Equipped Item
            if (hasEquipped) {
                GlStateManager.pushAttrib();
                GlStateManager.enableDepth();
                GlStateManager.depthFunc(515); // GL_LEQUAL
                GlStateManager.depthMask(true);
                GlStateManager.clear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT);
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                com.paneedah.weaponlib.LaserBeamRenderer.suppressRender = true;
                RenderHelper.enableGUIStandardItemLighting();
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(equipped), slotX + 6, slotY + 6);
                RenderHelper.disableStandardItemLighting();
                com.paneedah.weaponlib.LaserBeamRenderer.suppressRender = false;
                GlStateManager.disableDepth();
                GlStateManager.popAttrib();

                // Small white dot / equipped indicator
                drawRect(slotX + slotSize - 5, slotY + 3, slotX + slotSize - 2, slotY + 6, 0xFFFFFFFF);
            } else {
                // Render Category Text / Symbol
                String shortName = getCategoryShortName(cat);
                GlStateManager.pushMatrix();
                GlStateManager.translate(slotX, slotY + 10, 0);
                GlStateManager.scale(0.85f, 0.85f, 1.0f);
                int nameW = this.fontRenderer.getStringWidth(shortName);
                this.fontRenderer.drawStringWithShadow(shortName, (slotSize / 0.85f - nameW) / 2.0f, 0,
                        isHovered ? 0xFFFFFF : 0x888888);
                GlStateManager.popMatrix();
            }

            // Hover tooltip for slot
            if (isHovered) {
                tooltipTitle = getCategoryFullName(cat);
                if (hasEquipped) {
                    tooltipLines.add(TextFormatting.WHITE + (isRussian() ? "Установлено: " : "Equipped: ")
                            + TextFormatting.GOLD + equipped.getItemStackDisplayName(new ItemStack(equipped)));
                    if (isModifiable) {
                        tooltipLines
                                .add(TextFormatting.RED + (isRussian() ? "[ПКМ] Снять модуль" : "[RMB] Remove module"));
                    }
                } else if (isModifiable) {
                    tooltipLines.add(
                            TextFormatting.GRAY + (isRussian() ? "[ЛКМ] Выбрать модуль" : "[LMB] Select attachment"));
                }
                tooltipColor = 0xFFFFFF;
            }
        }
    }

    private void renderAttachmentDropUpMenu(int mouseX, int mouseY, float deltaSec) {
        if (activeCategory == null || pwi == null)
            return;
        List<FlaggedAttachment> compatList = getAvailableAttachmentsForCategory(activeCategory);
        if (compatList.isEmpty())
            return;

        int itemSlotSize = 28;
        int gap = 4;
        int padding = 4;
        int count = compatList.size();

        // Если больше 5 элементов, ширина окна вмещает 5.5 элементов (5 полных и
        // половину 6-го для намёка на скролл)
        int visibleSlotsWidth;
        int maxScroll;
        if (count > 5) {
            visibleSlotsWidth = (int) (5 * itemSlotSize + 4 * gap + (itemSlotSize + gap) * 0.5f);
            int totalContentWidth = count * itemSlotSize + (count - 1) * gap;
            maxScroll = totalContentWidth - visibleSlotsWidth;
        } else {
            visibleSlotsWidth = count * itemSlotSize + (count - 1) * gap;
            maxScroll = 0;
        }

        // Плавная интерполяция скролла с защитой от остаточного 1px оффсета
        targetScrollOffset = Math.max(0.0f, Math.min(maxScroll, targetScrollOffset));
        if (Math.abs(targetScrollOffset - scrollOffset) < 0.2f) {
            scrollOffset = targetScrollOffset;
        } else {
            scrollOffset += (targetScrollOffset - scrollOffset) * Math.min(1.0f, deltaSec * 15.0f);
        }

        int menuW = visibleSlotsWidth + (padding * 2);
        int menuH = itemSlotSize + (padding * 2);

        int[] menuPos = getDropUpMenuPosition(menuW, menuH);
        int menuX = menuPos[0];
        int menuY = menuPos[1];

        // Draw Menu Background
        drawRoundedRect(menuX, menuY, menuW, menuH, 0xD0121620, 0x80556677);

        ItemAttachment<Weapon> currentlyEquipped = pwi.getAttachmentItemWithCategory(activeCategory);
        int itemsStartX = menuX + padding;
        int itemsStartY = menuY + padding;

        // Включаем GL Scissor для отсечения выходящих за границы слотов
        enableScissor(itemsStartX, itemsStartY, visibleSlotsWidth, itemSlotSize);

        for (int i = 0; i < count; i++) {
            FlaggedAttachment flag = compatList.get(i);
            ItemAttachment<Weapon> attachment = flag.getAttachment();
            ItemStack stack = flag.getItemStack();

            int slotX = (int) (itemsStartX + i * (itemSlotSize + gap) - scrollOffset);
            int slotY = itemsStartY;

            // Пропускаем рендер невидимых ячеек
            if (slotX + itemSlotSize < itemsStartX || slotX > itemsStartX + visibleSlotsWidth) {
                continue;
            }

            boolean isEquipped = (currentlyEquipped == attachment);
            boolean inInventory = hasInInventory(attachment);
            boolean requiresParts = flag.requiresAnyParts();

            boolean isHovered = mouseX >= slotX && mouseX <= slotX + itemSlotSize && mouseY >= slotY
                    && mouseY <= slotY + itemSlotSize
                    && mouseX >= itemsStartX && mouseX <= itemsStartX + visibleSlotsWidth;

            int slotBg = isEquipped ? 0x90205030 : (isHovered ? 0x80303848 : 0x50181F2A);
            int slotBorder = isEquipped ? 0xFFFFFFFF
                    : (requiresParts ? 0xFFEE4444 : (inInventory ? 0xC0FFFFFF : 0x30FFFFFF));

            drawRoundedRect(slotX, slotY, itemSlotSize, itemSlotSize, slotBg, slotBorder);

            if (isEquipped) {
                drawOutlineRect(slotX, slotY, itemSlotSize, itemSlotSize, 0xFFFFFFFF, 2);
            }

            // Render Item Icon with alpha if not in inventory
            // renderItemIntoGUI positions at (x,y) — do NOT add extra
            // pushMatrix/translate/scale
            GlStateManager.pushAttrib();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(515); // GL_LEQUAL
            GlStateManager.depthMask(true);
            GlStateManager.clear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            if (!inInventory) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.38F);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            com.paneedah.weaponlib.LaserBeamRenderer.suppressRender = true;
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemIntoGUI(stack, slotX + 5, slotY + 5);
            RenderHelper.disableStandardItemLighting();
            com.paneedah.weaponlib.LaserBeamRenderer.suppressRender = false;
            GlStateManager.disableDepth();
            GlStateManager.popAttrib();

            // Hover tooltip
            if (isHovered) {
                tooltipTitle = stack.getDisplayName();
                if (isEquipped) {
                    // Check if this attachment is blocking other attachments (in use)
                    List<String> blockedBy = getAttachmentsBlockedByThis(attachment);
                    if (!blockedBy.isEmpty()) {
                        tooltipLines.add(TextFormatting.GREEN
                                + (isRussian() ? "[УСТАНОВЛЕНО] — нельзя снять" : "[EQUIPPED] — in use"));
                        tooltipLines.add(TextFormatting.RED
                                + (isRussian() ? "От этого модуля зависят:" : "Required by other parts:"));
                        for (String b : blockedBy) {
                            tooltipLines.add(TextFormatting.YELLOW + " • " + b);
                        }
                        tooltipLines.add(TextFormatting.GRAY + (isRussian() ? "Сначала снимите указанные модули"
                                : "Remove dependent attachments first"));
                        tooltipColor = 0xEE4444;
                    } else {
                        tooltipLines.add(TextFormatting.GREEN
                                + (isRussian() ? "[УСТАНОВЛЕНО] Кликните, чтобы снять" : "[EQUIPPED] Click to remove"));
                        tooltipColor = 0x55FF55;
                    }
                } else {
                    // Check if swapping from currently equipped module is blocked because other
                    // attachments depend on it
                    List<String> blockedByCurrent = (currentlyEquipped != null)
                            ? getAttachmentsBlockedByThis(currentlyEquipped)
                            : java.util.Collections.emptyList();
                    if (!blockedByCurrent.isEmpty()) {
                        tooltipLines.add(TextFormatting.RED + (isRussian() ? "НЕЛЬЗЯ СМЕНИТЬ:" : "CANNOT SWAP:"));
                        tooltipLines.add(TextFormatting.RED
                                + (isRussian() ? "От текущего модуля зависят:" : "Required by other parts:"));
                        for (String b : blockedByCurrent) {
                            tooltipLines.add(TextFormatting.YELLOW + " • " + b);
                        }
                        tooltipLines.add(TextFormatting.GRAY + (isRussian() ? "Сначала снимите указанные модули"
                                : "Remove dependent attachments first"));
                        tooltipColor = 0xEE4444;
                    } else if (requiresParts) {
                        tooltipLines.add(
                                TextFormatting.RED + (isRussian() ? "СНАЧАЛА УСТАНОВИТЕ:" : "REQUIRES PREREQUISITES:"));
                        for (ItemAttachment<Weapon> req : flag.getRequiredParts()) {
                            tooltipLines
                                    .add(TextFormatting.RED + " • " + req.getItemStackDisplayName(new ItemStack(req)));
                        }
                        tooltipColor = 0xEE4444;
                    } else if (inInventory) {
                        tooltipLines.add(TextFormatting.YELLOW + (isRussian() ? "[ЛКМ] Установить" : "[LMB] Equip"));
                        tooltipColor = 0xFFFFFF;
                    } else {
                        tooltipLines
                                .add(TextFormatting.DARK_GRAY + (isRussian() ? "Нет в инвентаре" : "Not in inventory"));
                        tooltipColor = 0x888888;
                    }
                }

                // Add attachment stat modifiers to tooltip
                com.paneedah.weaponlib.stats.AttachmentStatData stats = com.paneedah.weaponlib.stats.AttachmentStatsManager
                        .getStats(attachment);
                if (stats != null) {
                    boolean isRu = isRussian();
                    if (Math.abs(stats.recoilMultiplier - 1.0) > 0.001) {
                        double pct = (1.0 - stats.recoilMultiplier) * 100.0;
                        tooltipLines.add(pct > 0
                                ? (TextFormatting.GREEN + " ▲ " + (isRu ? "Контроль отдачи: +" : "Recoil: -")
                                        + String.format("%.0f%%", pct))
                                : (TextFormatting.RED + " ▼ " + (isRu ? "Отдача: +" : "Recoil: +")
                                        + String.format("%.0f%%", -pct)));
                    }
                    if (Math.abs(stats.visualRecoilMultiplier - 1.0) > 0.001) {
                        double pct = (1.0 - stats.visualRecoilMultiplier) * 100.0;
                        tooltipLines.add(pct > 0
                                ? (TextFormatting.GREEN + " ▲ "
                                        + (isRu ? "Стабилизация в руках: +" : "Stabilization: +")
                                        + String.format("%.0f%%", pct))
                                : (TextFormatting.RED + " ▼ " + (isRu ? "Смещение в руках: +" : "Kick: +")
                                        + String.format("%.0f%%", -pct)));
                    }
                    if (Math.abs(stats.hipSpreadMultiplier - 1.0) > 0.001) {
                        double pct = (1.0 - stats.hipSpreadMultiplier) * 100.0;
                        tooltipLines.add(pct > 0
                                ? (TextFormatting.GREEN + " ▲ " + (isRu ? "Точность от бедра: +" : "Hip Accuracy: +")
                                        + String.format("%.0f%%", pct))
                                : (TextFormatting.RED + " ▼ " + (isRu ? "Разброс от бедра: +" : "Hip Spread: +")
                                        + String.format("%.0f%%", -pct)));
                    }
                    if (Math.abs(stats.adsSpeedMultiplier - 1.0) > 0.001) {
                        double pct = (stats.adsSpeedMultiplier - 1.0) * 100.0;
                        tooltipLines.add(pct > 0
                                ? (TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость вскидки: +" : "ADS Speed: +")
                                        + String.format("%.0f%%", pct))
                                : (TextFormatting.RED + " ▼ " + (isRu ? "Скорость вскидки: " : "ADS Speed: ")
                                        + String.format("%.0f%%", pct)));
                    }
                    if (Math.abs(stats.drawSpeedMultiplier - 1.0) > 0.001) {
                        double pct = (stats.drawSpeedMultiplier - 1.0) * 100.0;
                        tooltipLines.add(pct > 0
                                ? (TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость доставания: +" : "Draw Speed: +")
                                        + String.format("%.0f%%", pct))
                                : (TextFormatting.RED + " ▼ " + (isRu ? "Скорость доставания: " : "Draw Speed: ")
                                        + String.format("%.0f%%", pct)));
                    }
                    if (Math.abs(stats.reloadSpeedMultiplier - 1.0) > 0.001) {
                        double pct = (stats.reloadSpeedMultiplier - 1.0) * 100.0;
                        tooltipLines.add(pct > 0
                                ? (TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость перезарядки: +" : "Reload Speed: +")
                                        + String.format("%.0f%%", pct))
                                : (TextFormatting.RED + " ▼ " + (isRu ? "Скорость перезарядки: " : "Reload Speed: ")
                                        + String.format("%.0f%%", pct)));
                    }
                    if (Math.abs(stats.weight) > 0.001) {
                        tooltipLines.add(stats.weight > 0
                                ? (TextFormatting.RED + " ▼ " + (isRu ? "Вес: +" : "Weight: +")
                                        + String.format("%.2f кг", stats.weight))
                                : (TextFormatting.GREEN + " ▲ " + (isRu ? "Вес: " : "Weight: ")
                                        + String.format("%.2f кг", stats.weight)));
                    }
                }
            }
        }

        disableScissor();
    }

    private void enableScissor(int x, int y, int width, int height) {
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc);
        int scale = sr.getScaleFactor();
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor(x * scale, (this.height - (y + height)) * scale, width * scale, height * scale);
    }

    private void disableScissor() {
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
    }

    private int[] getDropUpMenuPosition(int menuW, int menuH) {
        int catIndex = availableCategories.indexOf(activeCategory);
        int bottomSlotSize = 28;
        int bottomGap = 4;
        int bottomTotalW = availableCategories.size() * bottomSlotSize + (availableCategories.size() - 1) * bottomGap;
        int bottomStartX = (this.width - bottomTotalW) / 2;
        int activeSlotX = bottomStartX + (catIndex >= 0 ? catIndex : 0) * (bottomSlotSize + bottomGap);

        // Центрируем меню над слотом выбранной категории
        int menuX = activeSlotX + (bottomSlotSize - menuW) / 2;

        // Если меню упирается в правый край экрана -> сдвигаем влево
        if (menuX + menuW > this.width - 6) {
            menuX = this.width - 6 - menuW;
        }
        // Если меню упирается в левый край экрана -> сдвигаем вправо
        if (menuX < 6) {
            menuX = 6;
        }

        int menuY = this.height - 42 - menuH - 4;
        return new int[] { menuX, menuY };
    }

    private boolean hasInInventory(ItemAttachment<Weapon> attachment) {
        if (mc.player == null)
            return false;
        if (mc.player.isCreative())
            return true;

        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == attachment) {
                return true;
            }
        }
        return false;
    }

    private List<FlaggedAttachment> getAvailableAttachmentsForCategory(AttachmentCategory category) {
        List<FlaggedAttachment> result = new ArrayList<>();
        if (pwi == null || pwi.getWeapon() == null || category == null)
            return result;

        Weapon weapon = pwi.getWeapon();
        ClientModContext context = ClientModContext.getContext();
        WeaponAttachmentAspect aspect = context != null ? context.getAttachmentAspect() : null;

        for (CompatibleAttachment<? extends com.paneedah.weaponlib.AttachmentContainer> compat : weapon
                .getCompatibleAttachments(category)) {
            if (compat.getAttachment() == null || compat.getAttachment().getCategory() != category)
                continue;
            ItemAttachment<Weapon> attach = (ItemAttachment<Weapon>) compat.getAttachment();

            ItemStack attachStack = new ItemStack(attach);
            FlaggedAttachment flag = new FlaggedAttachment(attachStack, attach);

            // Check required parts
            if (aspect != null && !WeaponAttachmentAspect.hasRequiredAttachments(attach, pwi)) {
                flag.setRequiredParts(aspect.getRequiredParts(attach, pwi));
            }

            result.add(flag);
        }

        return result;
    }

    /**
     * Returns the display names of currently equipped attachments that require
     * 'attachment'
     * as a prerequisite — meaning 'attachment' cannot be removed until those are
     * removed first.
     */
    private List<String> getAttachmentsBlockedByThis(ItemAttachment<Weapon> attachment) {
        List<String> blocking = new ArrayList<>();
        if (pwi == null || pwi.getWeapon() == null)
            return blocking;
        for (AttachmentCategory cat : AttachmentCategory.values()) {
            ItemAttachment<Weapon> equipped = pwi.getAttachmentItemWithCategory(cat);
            if (equipped != null && equipped != attachment) {
                for (ItemAttachment<?> req : equipped.getRequiredAttachments()) {
                    if (req == attachment) {
                        blocking.add(equipped.getItemStackDisplayName(new ItemStack(equipped)));
                        break;
                    }
                }
            }
        }
        return blocking;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (!isModifiable) {
            if (mouseButton == 0) {
                isDraggingLMB = true;
                prevMouseX = mouseX;
                prevMouseY = mouseY;
            } else if (mouseButton == 1) {
                isDraggingRMB = true;
                prevMouseX = mouseX;
                prevMouseY = mouseY;
            }
            return;
        }

        if (mouseButton == 1) { // RMB: Instant Unequip on category slot OR Free-Look Drag
            if (!availableCategories.isEmpty()) {
                int slotSize = 28;
                int gap = 4;
                int totalW = availableCategories.size() * slotSize + (availableCategories.size() - 1) * gap;
                int startX = (this.width - totalW) / 2;
                int startY = this.height - 42;

                for (int i = 0; i < availableCategories.size(); i++) {
                    AttachmentCategory cat = availableCategories.get(i);
                    int slotX = startX + i * (slotSize + gap);
                    int slotY = startY;

                    if (mouseX >= slotX && mouseX <= slotX + slotSize && mouseY >= slotY
                            && mouseY <= slotY + slotSize) {
                        ItemAttachment<Weapon> equipped = pwi.getAttachmentItemWithCategory(cat);
                        if (equipped != null) {
                            if (getAttachmentsBlockedByThis(equipped).isEmpty()
                                    && (pwi.getWeapon() == null || pwi.getWeapon().isCategoryRemovable(cat))) {
                                ClientModContext context = ClientModContext.getContext();
                                if (context != null && context.getAttachmentAspect() != null) {
                                    mc.player.playSound(UniversalSoundLookup.lookupSound("attachmentoff"), 1.0F, 1.0F);
                                    context.getAttachmentAspect().forceAttachment(cat, this.pwi, ItemStack.EMPTY);
                                    updateWeaponCenter(false);
                                }
                            } else {
                                mc.player.playSound(UniversalSoundLookup.lookupSound("click"), 1.0F, 0.6F);
                            }
                        }
                        return;
                    }
                }
            }

            isDraggingRMB = true;
            prevMouseX = mouseX;
            prevMouseY = mouseY;
            return;
        }

        if (mouseButton == 0) { // LMB: Select category or attachment or start Free-Look Drag
            boolean clickedUI = false;

            // 1. Check Bottom Category Bar Click
            if (!availableCategories.isEmpty()) {
                int slotSize = 28;
                int gap = 4;
                int totalW = availableCategories.size() * slotSize + (availableCategories.size() - 1) * gap;
                int startX = (this.width - totalW) / 2;
                int startY = this.height - 42;

                for (int i = 0; i < availableCategories.size(); i++) {
                    AttachmentCategory cat = availableCategories.get(i);
                    int slotX = startX + i * (slotSize + gap);
                    int slotY = startY;

                    if (mouseX >= slotX && mouseX <= slotX + slotSize && mouseY >= slotY
                            && mouseY <= slotY + slotSize) {
                        clickedUI = true;
                        if (activeCategory == cat) {
                            activeCategory = null; // Toggle close
                        } else {
                            activeCategory = cat;
                            scrollOffset = 0.0f;
                            targetScrollOffset = 0.0f;
                        }
                        updateWeaponCenter(false);
                        mc.player.playSound(UniversalSoundLookup.lookupSound("click"), 0.8F, 1.2F);
                        return;
                    }
                }
            }

            // 2. Check Drop-up Attachment Menu Click
            if (activeCategory != null) {
                List<FlaggedAttachment> compatList = getAvailableAttachmentsForCategory(activeCategory);
                if (!compatList.isEmpty()) {
                    int itemSlotSize = 28;
                    int gap = 4;
                    int padding = 4;
                    int count = compatList.size();

                    int visibleSlotsWidth;
                    if (count > 5) {
                        visibleSlotsWidth = (int) (5 * itemSlotSize + 4 * gap + (itemSlotSize + gap) * 0.5f);
                    } else {
                        visibleSlotsWidth = count * itemSlotSize + (count - 1) * gap;
                    }

                    int menuW = visibleSlotsWidth + (padding * 2);
                    int menuH = itemSlotSize + (padding * 2);

                    int[] menuPos = getDropUpMenuPosition(menuW, menuH);
                    int menuX = menuPos[0];
                    int menuY = menuPos[1];

                    int itemsStartX = menuX + padding;
                    int itemsStartY = menuY + padding;

                    if (mouseX >= itemsStartX && mouseX <= itemsStartX + visibleSlotsWidth) {
                        for (int i = 0; i < count; i++) {
                            FlaggedAttachment flag = compatList.get(i);
                            int slotX = (int) (itemsStartX + i * (itemSlotSize + gap) - scrollOffset);
                            int slotY = itemsStartY;

                            if (mouseX >= slotX && mouseX <= slotX + itemSlotSize && mouseY >= slotY
                                    && mouseY <= slotY + itemSlotSize) {
                                clickedUI = true;
                                handleAttachmentClick(flag);
                                return;
                            }
                        }
                    }
                }
            }

            // Если ничего не выбрано и клик пришелся в свободное пространство — начинаем
            // вращение по ЛКМ
            if (!clickedUI && activeCategory == null) {
                isDraggingLMB = true;
                prevMouseX = mouseX;
                prevMouseY = mouseY;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = org.lwjgl.input.Mouse.getEventDWheel();
        if (dWheel != 0) {
            if (activeCategory != null) {
                List<FlaggedAttachment> compatList = getAvailableAttachmentsForCategory(activeCategory);
                if (compatList.size() > 5) {
                    int itemSlotSize = 28;
                    int gap = 4;
                    int count = compatList.size();
                    int visibleSlotsWidth = (int) (5 * itemSlotSize + 4 * gap + (itemSlotSize + gap) * 0.5f);
                    int totalContentWidth = count * itemSlotSize + (count - 1) * gap;
                    int maxScroll = totalContentWidth - visibleSlotsWidth;

                    // Wheel up -> scroll left, Wheel down -> scroll right
                    float scrollStep = (itemSlotSize + gap) * 1.0f;
                    if (dWheel > 0) {
                        targetScrollOffset = Math.max(0.0f, targetScrollOffset - scrollStep);
                    } else {
                        targetScrollOffset = Math.min(maxScroll, targetScrollOffset + scrollStep);
                    }
                }
            } else {
                // Если модули не выбраны — колесико мыши плавно приближает и отдаляет оружие
                if (dWheel > 0) {
                    targetZoom = Math.min(2.2f, targetZoom + 0.15f);
                } else {
                    targetZoom = Math.max(0.6f, targetZoom - 0.15f);
                }
            }
        }
    }

    private void handleAttachmentClick(FlaggedAttachment flag) {
        ClientModContext context = ClientModContext.getContext();
        if (context == null || context.getAttachmentAspect() == null || this.pwi == null || activeCategory == null)
            return;

        ItemAttachment<Weapon> attachment = flag.getAttachment();
        ItemAttachment<Weapon> current = this.pwi.getAttachmentItemWithCategory(activeCategory);

        // If current attachment is in use / required by other attachments -> Block
        // unequip and swap
        if (current != null && !getAttachmentsBlockedByThis(current).isEmpty()) {
            mc.player.playSound(UniversalSoundLookup.lookupSound("click"), 1.0F, 0.6F);
            return;
        }

        // If clicking currently equipped attachment -> Unequip
        if (current == attachment) {
            mc.player.playSound(UniversalSoundLookup.lookupSound("attachmentoff"), 1.0F, 1.0F);
            context.getAttachmentAspect().forceAttachment(activeCategory, this.pwi, ItemStack.EMPTY);
            updateWeaponCenter(false);
            return;
        }

        // If missing required parts -> Block
        if (flag.requiresAnyParts()) {
            mc.player.playSound(UniversalSoundLookup.lookupSound("click"), 1.0F, 0.6F);
            return;
        }

        // If in inventory (or creative) -> Equip
        if (hasInInventory(attachment)) {
            mc.player.playSound(UniversalSoundLookup.lookupSound("attachmenton"), 1.0F, 1.0F);
            context.getAttachmentAspect().forceAttachment(activeCategory, this.pwi, flag.getItemStack());
            updateWeaponCenter(false);
        } else {
            mc.player.playSound(UniversalSoundLookup.lookupSound("click"), 1.0F, 0.6F);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            isDraggingLMB = false;
            if (activeCategory == null) {
                targetPitch = 0.0f;
                targetYaw = 0.0f;
                targetZoom = 1.0f;
            }
        }
        if (state == 1) {
            isDraggingRMB = false;
            if (activeCategory == null) {
                targetPitch = 0.0f;
                targetYaw = 0.0f;
                targetZoom = 1.0f;
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if ((isDraggingLMB && clickedMouseButton == 0) || (isDraggingRMB && clickedMouseButton == 1)) {
            float dx = mouseX - prevMouseX;
            float dy = mouseY - prevMouseY;
            targetYaw -= dx * 0.75f;
            targetPitch -= dy * 0.75f;
            targetPitch = Math.max(-60.0f, Math.min(60.0f, targetPitch));
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_X || keyCode == Keyboard.KEY_E) {
            if (activeCategory != null) {
                activeCategory = null;
                updateWeaponCenter(true);
                return;
            }
            com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransitionToScreen(this.parentScreen, this);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    private void renderCustomTooltip(int mouseX, int mouseY) {
        if (tooltipTitle == null)
            return;

        List<String> textList = new ArrayList<>();
        textList.add(tooltipTitle);
        textList.addAll(tooltipLines);

        int maxW = 0;
        for (String line : textList) {
            int w = this.fontRenderer.getStringWidth(line);
            if (w > maxW)
                maxW = w;
        }

        int tooltipX = mouseX + 12;
        int tooltipY = mouseY - 12;
        int tooltipW = maxW + 12;
        int tooltipH = textList.size() * 11 + 8;

        if (tooltipX + tooltipW > this.width) {
            tooltipX = mouseX - 12 - tooltipW;
        }
        if (tooltipY + tooltipH > this.height) {
            tooltipY = this.height - tooltipH - 4;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 400.0f);
        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();

        drawRoundedRect(tooltipX, tooltipY, tooltipW, tooltipH, 0xF010141D, tooltipColor);

        int curY = tooltipY + 4;
        for (int i = 0; i < textList.size(); i++) {
            this.fontRenderer.drawStringWithShadow(textList.get(i), tooltipX + 6, curY,
                    i == 0 ? tooltipColor : 0xCCCCCC);
            curY += 11;
        }

        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private void drawRoundedRect(int x, int y, int w, int h, int fillCol, int borderCol) {
        drawRect(x, y, x + w, y + h, fillCol);
        drawRect(x, y, x + w, y + 1, borderCol);
        drawRect(x, y + h - 1, x + w, y + h, borderCol);
        drawRect(x, y, x + 1, y + h, borderCol);
        drawRect(x + w - 1, y, x + w, y + h, borderCol);
    }

    private void drawOutlineRect(int x, int y, int w, int h, int outlineCol, int thickness) {
        drawRect(x, y, x + w, y + thickness, outlineCol);
        drawRect(x, y + h - thickness, x + w, y + h, outlineCol);
        drawRect(x, y, x + thickness, y + h, outlineCol);
        drawRect(x + w - thickness, y, x + w, y + h, outlineCol);
    }

    private String getCategoryShortName(AttachmentCategory cat) {
        boolean ru = isRussian();
        switch (cat) {
            case SILENCER:
                return ru ? "СТВОЛ" : "MUZZLE";
            case SCOPE:
                return ru ? "ПРИЦЕЛ" : "OPTIC";
            case GUARD:
                return ru ? "ЦЕВЬЕ" : "GUARD";
            case LASER:
                return ru ? "ЛЦУ" : "LASER";
            case GRIP:
                return ru ? "РУЧКА" : "GRIP";
            case STOCK:
                return ru ? "ПРИКЛАД" : "STOCK";
            case MAGAZINE:
                return ru ? "МАГАЗИН" : "MAG";
            case FRONTSIGHT:
                return ru ? "МУШКА" : "SIGHT";
            case RECEIVER:
                return ru ? "РЕСИВЕР" : "RECEIVER";
            case BACKGRIP:
                return ru ? "ХВАТ" : "GRIP";
            case RAILING:
                return ru ? "ПЛАНКА" : "RAIL";
            case SKIN:
                return ru ? "СКИН" : "SKIN";
            default:
                return cat.name();
        }
    }

    private String getCategoryFullName(AttachmentCategory cat) {
        boolean ru = isRussian();
        switch (cat) {
            case SILENCER:
                return ru ? "Глушитель / ДТК (Muzzle)" : "Muzzle Device (Brake / Suppressor)";
            case SCOPE:
                return ru ? "Оптический прицел (Optics / Sight)" : "Optic / Sight";
            case GUARD:
                return ru ? "Цевьё (Handguard)" : "Handguard";
            case LASER:
                return ru ? "Тактический блок / ЛЦУ (Laser)" : "Tactical Laser / Light";
            case GRIP:
                return ru ? "Передняя рукоятка (Grip)" : "Foregrip";
            case STOCK:
                return ru ? "Приклад (Stock)" : "Stock";
            case MAGAZINE:
                return ru ? "Магазин (Magazine)" : "Magazine";
            case FRONTSIGHT:
                return ru ? "Мушка (Front Sight)" : "Front Sight";
            case RECEIVER:
                return ru ? "Ствольная коробка (Receiver)" : "Receiver";
            case BACKGRIP:
                return ru ? "Пистолетная рукоять (Rear Grip)" : "Pistol Grip";
            case RAILING:
                return ru ? "Планка крепления (Railing)" : "Accessory Rail";
            case SKIN:
                return ru ? "Камуфляж оружия (Skin)" : "Weapon Skin";
            default:
                return cat.name();
        }
    }
}
