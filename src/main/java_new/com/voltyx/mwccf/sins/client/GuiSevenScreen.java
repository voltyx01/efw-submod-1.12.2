package com.voltyx.mwccf.sins.client;

import com.voltyx.gender.gui.WildfireButton;
import com.voltyx.gender.gui.WildfireSlider;
import com.voltyx.gender.gui.screen.WildfirePlayerListScreen;
import com.voltyx.gender.main.Breasts;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.GenderPlayer.Gender;
import com.voltyx.gender.main.WildfireGender;
import com.voltyx.gender.main.config.ClientConfiguration;
import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.client.inspect.InspectDustManager;
import com.voltyx.mwccf.sins.ActiveModifier;
import com.voltyx.mwccf.sins.SinCard;
import com.voltyx.mwccf.sins.SinType;
import com.voltyx.mwccf.sins.capability.ISinCapability;
import com.voltyx.mwccf.sins.capability.SinCapabilityProvider;
import com.voltyx.mwccf.sins.network.PacketAcceptCard;
import com.voltyx.mwccf.sins.network.PacketLevelUpRequest;
import com.voltyx.mwccf.sins.network.PacketSelectSin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class GuiSevenScreen extends GuiScreen {
    // === Фиксированный виртуальный холст под эталонную настройку (2560x1369, GUI Scale = Авто) ===
    private static final float VIRTUAL_W = 512f;
    private static final float VIRTUAL_H = 274f;
    private boolean lastPanelClipValid = false;
    private int lastPanelClipRealX, lastPanelClipRealY, lastPanelClipRealW, lastPanelClipRealH;
    private float uiScaleX = 1f, uiScaleY = 1f;

    private void recomputeUiScale() {
        uiScaleX = this.width / VIRTUAL_W;
        uiScaleY = this.height / VIRTUAL_H;
    }

    private int toVirtX(int realX) { return (int) (realX / uiScaleX); }
    private int toVirtY(int realY) { return (int) (realY / uiScaleY); }

    // Appearance Sliders
    private WildfireButton blinkToggleButton;
    private WildfireSlider eyeSizeSlider, eyeDistanceSlider, eyeHeightSlider;

    public boolean blinkEditorOpen = false;
    private int[] blinkAcceptedSnapshot = null;
    private float animatedFaceYaw = -20.0f;
    public static final int STATE_CLASS_SELECT = 0;
    public static final int STATE_MAIN_HUD = 1;
    public static final int STATE_LEVEL_UP = 2;

    public static final int TAB_GRIEVANCE = 0;
    public static final int TAB_MANUALS = 1;
    public static final int TAB_APPEARANCE = 2;

    // Palette Colors (Darker, Noir)
    private static final int COLOR_BG = 0xFF050505;
    private static final int COLOR_PANEL_TOP = 0xF20F0E0D;
    private static final int COLOR_PANEL_BOTTOM = 0xF7080807;
    private static final int COLOR_PAPER = 0xFFC9C3B6;
    private static final int COLOR_PAPER_DIM = 0xFF8B8578;
    private static final int COLOR_INK = 0xFF3A3630;
    private static final int COLOR_BLOOD = 0xFF7A2418;
    private static final int COLOR_BLOOD_BRIGHT = 0xFFA8341F;
    private static final int COLOR_GOLD = 0xFFC98A2E;
    private static final int COLOR_GOLD_BRIGHT = 0xFFE8A53D;
    private static final int COLOR_LINE = 0xFF221F1C;

    private int currentState = STATE_CLASS_SELECT;
    public int currentTab = TAB_GRIEVANCE;

    private final UUID playerUUID;
    private final GuiScreen parentScreen;

    // Ash dust particles
    private final InspectDustManager dustManager = new InspectDustManager();
    private long lastFrameTime = System.nanoTime();
    private int dustInitWidth = -1;
    private int dustInitHeight = -1;

    // Smooth hover animation for cards
    private final float[] sinCardHoverLift = new float[7];
    private final float[] levelUpCardHoverLift = new float[3];

    // Delta time helper for frame-independent animations
    private float currentDeltaSec = 0f;

    // Smooth scrolling animation fields
    private float appearanceScrollAnim = 0f;
    private float grievanceScrollAnim = 0f;
    private float manualsScrollAnim = 0f;
    private int appearanceScrollBaked = 0;

    // Level-up deck state
    private List<SinCard> offeredCards = new ArrayList<>();
    private long levelUpAnimStartTime = 0;

    private static final int LVLUP_PHASE_ENTER = 0;
    private static final int LVLUP_PHASE_CARDS = 1;
    private static final int LVLUP_PHASE_SELECT = 2;
    private static final int LVLUP_PHASE_EXIT = 3;
    private static final int LVLUP_PHASE_RETURN = 4;

    private static final long LVLUP_ENTER_DURATION_MS = 650L;
    private static final long LVLUP_SELECT_DURATION_MS = 1000L;
    private static final long LVLUP_EXIT_DURATION_MS = 500L;
    private static final long LVLUP_RETURN_DURATION_MS = 650L;
    private static final float LVLUP_SELECTED_SCALE = 1.08f;

    private static final long LVLUP_CARD_INITIAL_DELAY_MS = 180L;
    private static final long LVLUP_CARD_STEP_DELAY_MS = 190L;
    private static final long LVLUP_CARD_FLIGHT_MS = 480L;
    private static final long LVLUP_TITLE_FADE_MS = 380L;

    private int levelUpPhase = LVLUP_PHASE_ENTER;
    private long levelUpPhaseStartTime = 0L;
    private int selectedCardIndex = -1;

    // Tooltip info
    private String tooltipTitle = null;
    private String tooltipFocus = null;
    private String tooltipPrice = null;
    private int tooltipX = 0;
    private int tooltipY = 0;

    // Appearance Sliders
    private WildfireSlider breastSlider, xOffsetBoobSlider, yOffsetBoobSlider, zOffsetBoobSlider, cleavageSlider;
    private WildfireSlider bounceSlider, floppySlider;

    // Scrolling states
    private int appearanceScrollY = 0;
    private int maxAppearanceScroll = 0;
    private int grievanceScrollY = 0;
    private int maxGrievanceScroll = 0;
    private int manualsScrollY = 0;
    private int maxManualsScroll = 0;

    // Fade in Transition
    private float fadeProgress = 0.0f;
    private static final float FADE_SPEED = 4.0f;

    public GuiSevenScreen(GuiScreen parent, UUID uuid) {
        this.parentScreen = parent;
        this.playerUUID = uuid;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC key
            if (this.currentState == STATE_LEVEL_UP
                    && (this.levelUpPhase == LVLUP_PHASE_ENTER || this.levelUpPhase == LVLUP_PHASE_CARDS)) {
                this.selectedCardIndex = -1;
                this.levelUpPhase = LVLUP_PHASE_RETURN;
                this.levelUpPhaseStartTime = System.nanoTime();
                return;
            }
            GuiScreen target = this.parentScreen != null ? this.parentScreen
                    : new net.minecraft.client.gui.inventory.GuiInventory(this.mc.player);
            com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransitionToScreen(target, this);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private ISinCapability getSinCap() {
        if (mc != null && mc.player != null) {
            return mc.player.getCapability(SinCapabilityProvider.SIN_CAP, null);
        }
        return null;
    }

    private GenderPlayer getGenderPlayer() {
        return WildfireGender.getPlayerById(this.playerUUID);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        if ((int) VIRTUAL_W != dustInitWidth || (int) VIRTUAL_H != dustInitHeight) {
            this.dustManager.init((int) VIRTUAL_W, (int) VIRTUAL_H);
            this.dustInitWidth = (int) VIRTUAL_W;
            this.dustInitHeight = (int) VIRTUAL_H;
        }

        this.lastFrameTime = System.nanoTime();

        ISinCapability cap = getSinCap();
        if (cap != null && cap.getChosenSin() != null) {
            this.currentState = (this.currentState == STATE_LEVEL_UP) ? STATE_LEVEL_UP : STATE_MAIN_HUD;
        } else {
            this.currentState = STATE_CLASS_SELECT;
        }

        if (this.currentState == STATE_MAIN_HUD && this.currentTab == TAB_APPEARANCE) {
            initAppearanceControls();
        }
    }

    private void initAppearanceControls() {
        GenderPlayer plr = getGenderPlayer();
        if (plr == null)
            return;
        Breasts breasts = plr.getBreasts();

        Consumer<Float> onSave = value -> GenderPlayer.saveGenderInfo(plr);

        int panelW = 236;
        int rightMargin = 10;
        int startX = (int) VIRTUAL_W - panelW - rightMargin + 8;
        int startY = 46 - Math.round(appearanceScrollAnim);
        int btnW = panelW - 16;
        int btnH = 18;
        int stepY = 22;

        int curY = startY;

        this.buttonList
                .add(new WildfireButton(100, startX, curY, btnW, btnH, getGenderButtonLabel(plr.getGender()), () -> {
                    Gender nextGender;
                    switch (plr.getGender()) {
                        case MALE:
                            nextGender = Gender.FEMALE;
                            break;
                        case FEMALE:
                            nextGender = Gender.OTHER;
                            break;
                        default:
                            nextGender = Gender.MALE;
                            break;
                    }
                    if (plr.updateGender(nextGender)) {
                        updateButtonText(100, getGenderButtonLabel(nextGender));
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
        curY += stepY;

        this.buttonList.add(this.breastSlider = new WildfireSlider(101, startX, curY, btnW, btnH,
                ClientConfiguration.BUST_SIZE.getMinInclusive(), ClientConfiguration.BUST_SIZE.getMaxInclusive(),
                plr.getBustSize(),
                plr::updateBustSize,
                val -> "Размер бюста: " + Math.round(val * 100) + "%", onSave));
        curY += stepY;

        this.buttonList.add(this.xOffsetBoobSlider = new WildfireSlider(102, startX, curY, btnW, btnH,
                ClientConfiguration.BREASTS_OFFSET_X.getMinInclusive(),
                ClientConfiguration.BREASTS_OFFSET_X.getMaxInclusive(), breasts.getXOffset(),
                breasts::updateXOffset,
                val -> "Расстояние (X): " + String.format("%.1f", val * 10), onSave));
        curY += stepY;

        this.buttonList.add(this.yOffsetBoobSlider = new WildfireSlider(103, startX, curY, btnW, btnH,
                ClientConfiguration.BREASTS_OFFSET_Y.getMinInclusive(),
                ClientConfiguration.BREASTS_OFFSET_Y.getMaxInclusive(), breasts.getYOffset(),
                breasts::updateYOffset,
                val -> "Высота (Y): " + String.format("%.1f", val * 10), onSave));
        curY += stepY;

        this.buttonList.add(this.zOffsetBoobSlider = new WildfireSlider(104, startX, curY, btnW, btnH,
                ClientConfiguration.BREASTS_OFFSET_Z.getMinInclusive(),
                ClientConfiguration.BREASTS_OFFSET_Z.getMaxInclusive(), breasts.getZOffset(),
                breasts::updateZOffset,
                val -> "Глубина (Z): " + String.format("%.1f", val * 10), onSave));
        curY += stepY;

        this.buttonList.add(this.cleavageSlider = new WildfireSlider(105, startX, curY, btnW, btnH,
                ClientConfiguration.BREASTS_CLEAVAGE.getMinInclusive(),
                ClientConfiguration.BREASTS_CLEAVAGE.getMaxInclusive(), breasts.getCleavage(),
                breasts::updateCleavage,
                val -> "Поворот / Ложбинка: " + Math.round(val * 100) + "%", onSave));
        curY += stepY;

        this.buttonList.add(new WildfireButton(106, startX, curY, btnW, btnH,
                "Физика тела: " + (plr.hasBreastPhysics() ? TextFormatting.GREEN + "ВКЛ" : TextFormatting.RED + "ВЫКЛ"),
                () -> {
                    boolean p = !plr.hasBreastPhysics();
                    if (plr.updateBreastPhysics(p)) {
                        updateButtonText(106,
                                "Физика тела: " + (p ? TextFormatting.GREEN + "ВКЛ" : TextFormatting.RED + "ВЫКЛ"));
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
        curY += stepY;

        this.buttonList.add(new WildfireButton(107, startX, curY, btnW, btnH,
                "Физика брони: "
                        + (plr.hasArmorBreastPhysics() ? TextFormatting.GREEN + "ВКЛ" : TextFormatting.RED + "ВЫКЛ"),
                () -> {
                    boolean ap = !plr.hasArmorBreastPhysics();
                    if (plr.updateArmorBreastPhysics(ap)) {
                        updateButtonText(107,
                                "Физика брони: " + (ap ? TextFormatting.GREEN + "ВКЛ" : TextFormatting.RED + "ВЫКЛ"));
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
        curY += stepY;

        this.buttonList.add(this.bounceSlider = new WildfireSlider(108, startX, curY, btnW, btnH,
                ClientConfiguration.BOUNCE_MULTIPLIER.getMinInclusive(),
                ClientConfiguration.BOUNCE_MULTIPLIER.getMaxInclusive(), plr.getBounceMultiplierRaw(),
                val -> {
                },
                val -> "Упругость: " + Math.round(val * 100) + "%",
                val -> {
                    if (plr.updateBounceMultiplier(val)) {
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
        curY += stepY;

        this.buttonList.add(this.floppySlider = new WildfireSlider(109, startX, curY, btnW, btnH,
                ClientConfiguration.FLOPPY_MULTIPLIER.getMinInclusive(),
                ClientConfiguration.FLOPPY_MULTIPLIER.getMaxInclusive(), plr.getFloppiness(),
                val -> {
                },
                val -> "Инерция: " + Math.round(val * 100) + "%",
                val -> {
                    if (plr.updateFloppiness(val)) {
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
        curY += stepY;

        this.buttonList.add(new WildfireButton(110, startX, curY, btnW, btnH,
                "Спаренная физика: " + (breasts.isUniboob() ? TextFormatting.RED + "НЕТ" : TextFormatting.GREEN + "ДА"),
                () -> {
                    boolean ub = !breasts.isUniboob();
                    if (breasts.updateUniboob(ub)) {
                        updateButtonText(110,
                                "Спаренная физика: " + (ub ? TextFormatting.RED + "НЕТ" : TextFormatting.GREEN + "ДА"));
                        GenderPlayer.saveGenderInfo(plr);
                    }
                }));
        curY += stepY + 6;

        if (blinkAcceptedSnapshot == null && plr.isBlinkEnabled()) {
            blinkAcceptedSnapshot = snapshotBlink(plr);
        }

        this.buttonList.add(this.blinkToggleButton = new WildfireButton(111, startX, curY, btnW, btnH,
                "    Моргание глаз", () -> {
                    if (!blinkEditorOpen) {
                        blinkEditorOpen = true;
                        if (!plr.isBlinkEnabled() && plr.updateBlinking(true)) {
                            GenderPlayer.saveGenderInfo(plr);
                        }
                        blinkAcceptedSnapshot = snapshotBlink(plr);
                    } else {
                        blinkEditorOpen = false;
                    }
                    initGui();
                }));
        curY += stepY;

        if (blinkEditorOpen) {
            int blinkW = btnW - 12;
            int blinkX = startX + 12;

            this.buttonList.add(this.eyeSizeSlider = new WildfireSlider(112, blinkX, curY, blinkW, btnH,
                    0, 2, plr.getEyeSize(), val -> {
                    },
                    val -> {
                        int v = val.intValue();
                        return "Размер глаз: " + (v == 0 ? "1x1" : (v == 1 ? "2x1" : "2x2"));
                    },
                    val -> {
                        if (plr.updateEyeSize(val.intValue())) {
                            if (plr.getEyelidSize() > plr.getEyeSize()) {
                                plr.updateEyelidSize(plr.getEyeSize());
                            }
                            GenderPlayer.saveGenderInfo(plr);
                            initGui();
                        }
                    }));
            curY += stepY;

            int clampedDistance = Math.max(1, Math.min(4, plr.getEyeDistance()));
            if (clampedDistance != plr.getEyeDistance()) {
                plr.updateEyeDistance(clampedDistance);
            }
            this.buttonList.add(this.eyeDistanceSlider = new WildfireSlider(113, blinkX, curY, blinkW, btnH,
                    1, 4, plr.getEyeDistance(), val -> {
                    },
                    val -> "Расстояние: " + (val.intValue() * 2) + " px",
                    val -> {
                        if (plr.updateEyeDistance(val.intValue()))
                            GenderPlayer.saveGenderInfo(plr);
                    }));
            curY += stepY;

            this.buttonList.add(this.eyeHeightSlider = new WildfireSlider(114, blinkX, curY, blinkW, btnH,
                    0, 8, plr.getEyeHeight(), val -> {
                    },
                    val -> "Высота: " + val.intValue(),
                    val -> {
                        if (plr.updateEyeHeight(val.intValue()))
                            GenderPlayer.saveGenderInfo(plr);
                    }));
            curY += stepY;

            int maxEyelid = plr.getEyeSize();
            if (plr.getEyelidSize() > maxEyelid) {
                plr.updateEyelidSize(maxEyelid);
            }
            this.buttonList.add(this.eyeSizeSlider = new WildfireSlider(115, blinkX, curY, blinkW, btnH,
                    0, maxEyelid, plr.getEyelidSize(), val -> {
                    },
                    val -> {
                        int v = val.intValue();
                        return "Размер века: " + (v == 0 ? "1x1" : (v == 1 ? "2x1" : "2x2"));
                    },
                    val -> {
                        if (plr.updateEyelidSize(val.intValue()))
                            GenderPlayer.saveGenderInfo(plr);
                    }));
            curY += stepY;

            this.buttonList.add(new WildfireButton(116, blinkX, curY, blinkW, btnH,
                    "Режим век: " + (plr.isDualEyelid() ? "2 участка" : "Инверсия (1)"), () -> {
                        boolean dual = !plr.isDualEyelid();
                        if (plr.updateDualEyelid(dual)) {
                            GenderPlayer.saveGenderInfo(plr);
                            initGui();
                        }
                    }));
            curY += stepY;

            this.buttonList.add(new WildfireSlider(118, blinkX, curY, blinkW, btnH,
                    -8, 8, plr.getEyelidOffsetX(), val -> {
                    },
                    val -> "Смещение века (X): " + val.intValue(),
                    val -> {
                        if (plr.updateEyelidOffsetX(val.intValue()))
                            GenderPlayer.saveGenderInfo(plr);
                    }));
            curY += stepY;

            this.buttonList.add(new WildfireSlider(119, blinkX, curY, blinkW, btnH,
                    -8, 8, plr.getEyelidOffsetY(), val -> {
                    },
                    val -> "Смещение века (Y): " + (val.intValue() > 0 ? "+" : "") + val.intValue(),
                    val -> {
                        if (plr.updateEyelidOffsetY(val.intValue()))
                            GenderPlayer.saveGenderInfo(plr);
                    }));
            curY += stepY;

            this.buttonList.add(new WildfireSlider(120, blinkX, curY, blinkW, btnH,
                    0.80f, 1.35f, plr.getBlinkFrequency(), val -> {
                    },
                    val -> {
                        int pct = Math.round((val - 1.0f) * 100);
                        return "Частота: " + (pct >= 0 ? "+" : "") + pct + "%";
                    },
                    val -> {
                        if (plr.updateBlinkFrequency(val))
                            GenderPlayer.saveGenderInfo(plr);
                    }));
            curY += stepY;

            boolean dirty = isBlinkSnapshotDirty(plr);
            this.buttonList.add(new WildfireButton(117, blinkX, curY, blinkW, btnH,
                    dirty ? "Принять" : "Выключить", () -> {
                        GenderPlayer p = getGenderPlayer();
                        if (p == null)
                            return;
                        if (isBlinkSnapshotDirty(p)) {
                            blinkAcceptedSnapshot = snapshotBlink(p);
                            blinkEditorOpen = false;
                        } else {
                            p.updateBlinking(false);
                            GenderPlayer.saveGenderInfo(p);
                            blinkEditorOpen = false;
                            blinkAcceptedSnapshot = null;
                        }
                        initGui();
                    }));
            curY += stepY;
        } else {
            this.eyeSizeSlider = null;
            this.eyeDistanceSlider = null;
            this.eyeHeightSlider = null;
        }

        int totalContentH = curY - startY;
        int visibleH = (int) VIRTUAL_H - 60;
        this.maxAppearanceScroll = Math.max(0, totalContentH - visibleH);

        // Настройки груди доступны для любого пола.

        for (GuiButton btn : this.buttonList) {
            if (btn.id >= 100 && btn.id <= 120) {
                if (btn.y + btn.height <= 38 || btn.y >= (int) VIRTUAL_H - 10) {
                    btn.visible = false;
                }
            }
        }
    }

    private int[] snapshotBlink(GenderPlayer plr) {
        return new int[] {
                plr.getEyeSize(), plr.getEyeDistance(), plr.getEyeHeight(),
                plr.getEyelidSize(), plr.isDualEyelid() ? 1 : 0,
                plr.getEyelidOffsetX(), plr.getEyelidOffsetY(),
                Math.round(plr.getBlinkFrequency() * 100)
        };
    }

    private boolean isBlinkSnapshotDirty(GenderPlayer plr) {
        if (blinkAcceptedSnapshot == null)
            return true;
        return !java.util.Arrays.equals(snapshotBlink(plr), blinkAcceptedSnapshot);
    }

    private void updateButtonText(int id, String text) {
        for (GuiButton btn : this.buttonList) {
            if (btn.id == id) {
                btn.displayString = text;
                break;
            }
        }
    }

    private String getGenderButtonLabel(Gender gender) {
        return "Пол: " + (gender != null ? gender.getDisplayName().getFormattedText() : "MALE");
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.currentState == STATE_MAIN_HUD) {
            int dWheel = org.lwjgl.input.Mouse.getEventDWheel();
            if (dWheel != 0) {
                if (currentTab == TAB_APPEARANCE) {
                    if (dWheel > 0) {
                        appearanceScrollY = Math.max(0, appearanceScrollY - 22);
                    } else {
                        appearanceScrollY = Math.min(maxAppearanceScroll, appearanceScrollY + 22);
                    }
                } else if (currentTab == TAB_GRIEVANCE) {
                    if (dWheel > 0) {
                        grievanceScrollY = Math.max(0, grievanceScrollY - 22);
                    } else {
                        grievanceScrollY = Math.min(maxGrievanceScroll, grievanceScrollY + 22);
                    }
                } else if (currentTab == TAB_MANUALS) {
                    if (dWheel > 0) {
                        manualsScrollY = Math.max(0, manualsScrollY - 22);
                    } else {
                        manualsScrollY = Math.min(maxManualsScroll, manualsScrollY + 22);
                    }
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof WildfireButton) {
            ((WildfireButton) button).press();
        }
    }

    public void receiveLevelUpCards(List<SinCard> cards) {
        this.offeredCards = cards;
        this.currentState = STATE_LEVEL_UP;
        this.selectedCardIndex = -1;
        this.levelUpPhase = LVLUP_PHASE_ENTER;
        this.levelUpPhaseStartTime = System.nanoTime();
        this.levelUpAnimStartTime = 0L;
        java.util.Arrays.fill(levelUpCardHoverLift, 0f);
        this.buttonList.clear();
    }

    private int getDporCount() {
        if (mc.player == null)
            return 0;
        int count = 0;
        for (ItemStack stack : mc.player.inventory.mainInventory) {
            if (!stack.isEmpty() && stack.getItem() == efw.init.EfwModItems.DPOR) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static float approachScroll(float current, float target, float deltaSec) {
        float next = current + (target - current) * Math.min(1.0f, deltaSec * 12.0f);
        if (Math.abs(next - target) < 0.5f) {
            next = target;
        }
        return next;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        recomputeUiScale();
        int vMouseX = toVirtX(mouseX);
        int vMouseY = toVirtY(mouseY);

        tooltipTitle = null;

        long now = System.nanoTime();
        float deltaSec = Math.min(0.1f, (now - lastFrameTime) / 1_000_000_000.0f);
        this.lastFrameTime = now;
        this.currentDeltaSec = deltaSec;

        appearanceScrollAnim = approachScroll(appearanceScrollAnim, appearanceScrollY, deltaSec);
        grievanceScrollAnim = approachScroll(grievanceScrollAnim, grievanceScrollY, deltaSec);
        manualsScrollAnim = approachScroll(manualsScrollAnim, manualsScrollY, deltaSec);

        if (this.currentState == STATE_MAIN_HUD && this.currentTab == TAB_APPEARANCE) {
            int roundedAnim = Math.round(appearanceScrollAnim);
            if (roundedAnim != appearanceScrollBaked) {
                appearanceScrollBaked = roundedAnim;
                initGui();
            }
        }

        if (fadeProgress < 1.0f) {
            fadeProgress = Math.min(1.0f, fadeProgress + FADE_SPEED * deltaSec);
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(uiScaleX, uiScaleY, 1.0f);

        drawRect(0, 0, (int) VIRTUAL_W, (int) VIRTUAL_H, COLOR_BG);

        GlStateManager.pushMatrix();
        this.dustManager.updateAndRender((int) VIRTUAL_W, (int) VIRTUAL_H, mc);
        GlStateManager.popMatrix();

        this.fontRenderer.drawString("DOSSIER // SEVEN", 16, 14, COLOR_BLOOD_BRIGHT);

        if (this.currentState == STATE_CLASS_SELECT) {
            drawClassSelectScreen(vMouseX, vMouseY, deltaSec);
        } else if (this.currentState == STATE_MAIN_HUD) {
            drawMainHudScreen(vMouseX, vMouseY, partialTicks, 0.0f, 0.0f);
        } else if (this.currentState == STATE_LEVEL_UP) {
            drawLevelUpFlow(vMouseX, vMouseY, partialTicks);
        }

        if (tooltipTitle != null) {
            drawCustomTooltip(tooltipX, tooltipY, tooltipTitle, tooltipFocus, tooltipPrice);
        }

        if (lastPanelClipValid) {
            org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
            org.lwjgl.opengl.GL11.glScissor(lastPanelClipRealX, lastPanelClipRealY, lastPanelClipRealW,
                    lastPanelClipRealH);
        }

        super.drawScreen(vMouseX, vMouseY, partialTicks);

        if (lastPanelClipValid) {
            org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        }

        GlStateManager.popMatrix();
    }

    // ==========================================
    // 1. CLASS SELECT SCREEN (COMPACT & SCALED)
    // ==========================================
    private void drawClassSelectScreen(int mouseX, int mouseY, float deltaSec) {
        String title = "ВЫБЕРИ СВОЙ ГРЕХ";
        String subtitle = "ПЕРВОНАЧАЛЬНЫЙ ВЫБОР ОПРЕДЕЛИТ ВАШИ СЛАБОСТИ И СИЛЫ";

        int titleW = this.fontRenderer.getStringWidth(title);
        int subW = this.fontRenderer.getStringWidth(subtitle);

        int centerY = (int) VIRTUAL_H / 2;
        int topHeaderY = Math.max(8, centerY - 130);
        this.fontRenderer.drawString(title, ((int) VIRTUAL_W - titleW) / 2, topHeaderY, COLOR_PAPER);
        this.fontRenderer.drawString(subtitle, ((int) VIRTUAL_W - subW) / 2, topHeaderY + 11, COLOR_PAPER_DIM);

        int cardW = 88;
        int cardH = 110;
        int gapX = 10;
        int gapY = 8;

        SinType[] sins = SinType.values();

        int row1TotalW = 4 * cardW + 3 * gapX;
        int row1StartX = ((int) VIRTUAL_W - row1TotalW) / 2;
        int row1StartY = topHeaderY + 26;

        int row2TotalW = 3 * cardW + 2 * gapX;
        int row2StartX = ((int) VIRTUAL_W - row2TotalW) / 2;
        int row2StartY = row1StartY + cardH + gapY;

        for (int i = 0; i < sins.length; i++) {
            SinType sin = sins[i];
            int cx, cy;
            if (i < 4) {
                cx = row1StartX + i * (cardW + gapX);
                cy = row1StartY;
            } else {
                cx = row2StartX + (i - 4) * (cardW + gapX);
                cy = row2StartY;
            }

            boolean isHovered = mouseX >= cx && mouseX <= cx + cardW && mouseY >= cy && mouseY <= cy + cardH;

            float targetLift = isHovered ? 4.0f : 0.0f;
            sinCardHoverLift[i] += (targetLift - sinCardHoverLift[i]) * Math.min(1.0f, deltaSec * 15.0f);
            if (Math.abs(sinCardHoverLift[i] - targetLift) < 0.05f) {
                sinCardHoverLift[i] = targetLift;
            }
            int drawY = Math.round(cy - sinCardHoverLift[i]);

            drawGradientRect(cx, drawY, cx + cardW, drawY + cardH, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
            drawBoxOutline(cx, drawY, cardW, cardH, isHovered ? COLOR_BLOOD_BRIGHT : COLOR_LINE);

            drawRect(cx, drawY, cx + 4, drawY + 4, COLOR_BG);
            drawLine(cx, drawY + 4, cx + 4, drawY, isHovered ? COLOR_BLOOD_BRIGHT : COLOR_LINE);

            this.fontRenderer.drawString(sin.getNumber(), cx + cardW - 14, drawY + 5, COLOR_INK);

            int infoX = cx + 5;
            int infoY = drawY + 5;
            boolean infoHover = mouseX >= infoX && mouseX <= infoX + 11 && mouseY >= infoY && mouseY <= infoY + 11;
            drawBoxOutline(infoX, infoY, 11, 11, infoHover ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);
            this.fontRenderer.drawString("i", infoX + 3, infoY + 2, infoHover ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);

            if (infoHover) {
                tooltipTitle = sin.getNameRu();
                tooltipFocus = "УПОР: " + sin.getFocusRu();
                tooltipPrice = "ЦЕНА: " + sin.getPriceRu();
                tooltipX = infoX;
                tooltipY = infoY + 14;
            }

            int iconX = cx + (cardW - 34) / 2;
            int iconY = drawY + 22;
            drawDashedOutline(iconX, iconY, 34, 34, isHovered ? COLOR_BLOOD_BRIGHT : COLOR_PAPER_DIM);
            drawCenteredString(this.fontRenderer, "ICON", iconX + 17, iconY + 13,
                    isHovered ? COLOR_BLOOD_BRIGHT : COLOR_INK);

            int nameW = this.fontRenderer.getStringWidth(sin.getNameRu());
            this.fontRenderer.drawString(sin.getNameRu(), cx + (cardW - nameW) / 2, drawY + cardH - 14,
                    isHovered ? COLOR_GOLD_BRIGHT : COLOR_PAPER);
        }
    }

    // ==========================================
    // 2. MAIN HUD SCREEN (3 TABS ALIGNED TO PANEL)
    // ==========================================
    private void drawMainHudScreen(int mouseX, int mouseY, float partialTicks, float modelOffsetX, float panelOffsetX) {
        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;

        // Рендерим модель ровно один раз с учетом смещения
        GlStateManager.pushMatrix();
        GlStateManager.translate(modelOffsetX, 0.0f, 0.0f);
        drawGender3DPlayer();

        // 2D-затемнение поверх области модели
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        // Рендерим правую панель со сдвигом
        GlStateManager.pushMatrix();
        GlStateManager.translate(panelOffsetX, 0.0f, 0.0f);

        int tabGap = 4;
        int tabW = (panelW - tabGap * 2) / 3;
        int tabH = 18;
        int tabY = 16;
        int tab1X = rightX;
        int tab2X = rightX + tabW + tabGap;
        int tab3X = rightX + (tabW + tabGap) * 2;
        drawTab(tab1X, tabY, tabW, tabH, "Grievance", currentTab == TAB_GRIEVANCE, mouseX, mouseY);
        drawTab(tab2X, tabY, tabW, tabH, "Manuals", currentTab == TAB_MANUALS, mouseX, mouseY);
        drawTab(tab3X, tabY, tabW, tabH, "Appearance", currentTab == TAB_APPEARANCE, mouseX, mouseY);

        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc);
        int sf = sr.getScaleFactor();
        float combinedX = uiScaleX * sf;
        float combinedY = uiScaleY * sf;
        int clipX = (int) (rightX - 2 + panelOffsetX);
        int clipY = 38;
        int clipW = panelW + 4;
        int clipH = ((int) VIRTUAL_H) - clipY - 8;

        lastPanelClipValid = (currentTab == TAB_APPEARANCE);
        lastPanelClipRealX = (int) (clipX * combinedX);
        lastPanelClipRealY = (int) (mc.displayHeight - (clipY + clipH) * combinedY);
        lastPanelClipRealW = (int) (clipW * combinedX);
        lastPanelClipRealH = (int) (clipH * combinedY);

        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor(
                lastPanelClipRealX,
                lastPanelClipRealY,
                lastPanelClipRealW,
                lastPanelClipRealH);

        if (currentTab == TAB_GRIEVANCE) {
            drawGrievanceTab(mouseX, mouseY);
        } else if (currentTab == TAB_MANUALS) {
            drawManualsTab(mouseX, mouseY);
        } else {
            drawAppearanceTab(mouseX, mouseY);
        }

        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    private void drawGender3DPlayer() {
        EntityPlayer ent = null;
        try {
            ent = this.mc.world.getPlayerEntityByUUID(this.playerUUID);
        } catch (Throwable ignored) {
        }
        if (ent == null)
            return;

        GlStateManager.pushMatrix();
        try {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableDepth();
            
            // Фиксация освещения: отключаем стандартный свет предметов, 
            // чтобы модель рендерилась в мягких тенях интерфейса без вспышек
            RenderHelper.disableStandardItemLighting();
            net.minecraft.client.renderer.GlStateManager.disableLighting();

            net.minecraft.entity.player.InventoryPlayer inv = ent.inventory;
            ItemStack savedMain = inv.mainInventory.get(inv.currentItem);
            ItemStack savedOffhand = inv.offHandInventory.get(0);
            ItemStack[] savedArmor = new ItemStack[inv.armorInventory.size()];
            for (int i = 0; i < inv.armorInventory.size(); i++) {
                savedArmor[i] = inv.armorInventory.get(i);
                inv.armorInventory.set(i, ItemStack.EMPTY);
            }
            inv.mainInventory.set(inv.currentItem, ItemStack.EMPTY);
            inv.offHandInventory.set(0, ItemStack.EMPTY);

            try {
                boolean isBlinkEdit = (currentTab == TAB_APPEARANCE && blinkEditorOpen);

                int scale = 190;
                int posX = Math.max(70, ((int) VIRTUAL_W - 260) / 2);
                int posY = (int) VIRTUAL_H / 2 + 260;

                float targetYaw = isBlinkEdit ? 0.0f : -20.0f;
                animatedFaceYaw += (targetYaw - animatedFaceYaw) * 0.15f;
                float mX = animatedFaceYaw;
                float mY = animatedFaceYaw;

                WildfirePlayerListScreen.drawEntityOnScreen(posX, posY, scale, mX, mY, ent);
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                inv.mainInventory.set(inv.currentItem, savedMain);
                inv.offHandInventory.set(0, savedOffhand);
                for (int i = 0; i < inv.armorInventory.size(); i++) {
                    inv.armorInventory.set(i, savedArmor[i]);
                }
            }
        } finally {
            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void drawTab(int x, int y, int w, int h, String title, boolean active, int mouseX, int mouseY) {
        boolean hover = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;

        drawGradientRect(x, y, x + w, y + h, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
        drawBoxOutline(x, y, w, h, active ? COLOR_BLOOD_BRIGHT : (hover ? COLOR_PAPER_DIM : COLOR_LINE));

        int textW = this.fontRenderer.getStringWidth(title);
        this.fontRenderer.drawString(title, x + (w - textW) / 2, y + (h - 8) / 2,
                active ? COLOR_PAPER : COLOR_PAPER_DIM);
    }

    private static class StackedModifier {
        String nameRu;
        double totalValue;
        boolean isBuff;

        StackedModifier(String nameRu, double totalValue, boolean isBuff) {
            this.nameRu = nameRu;
            this.totalValue = totalValue;
            this.isBuff = isBuff;
        }

        String getFormattedValue() {
            String sign = totalValue >= 0 ? "+" : "−";
            return sign + String.format("%.0f%%", Math.abs(totalValue));
        }
    }

    private List<StackedModifier> getStackedModifiers(List<ActiveModifier> rawMods) {
        java.util.Map<String, StackedModifier> map = new java.util.LinkedHashMap<>();
        for (ActiveModifier mod : rawMods) {
            String key = mod.getId() + "_" + mod.isBuff();
            if (map.containsKey(key)) {
                map.get(key).totalValue += mod.getValue();
            } else {
                map.put(key, new StackedModifier(mod.getNameRu(), mod.getValue(), mod.isBuff()));
            }
        }
        return new ArrayList<>(map.values());
    }

    private void drawGrievanceTab(int mouseX, int mouseY) {
        ISinCapability cap = getSinCap();
        if (cap == null)
            return;

        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;

        List<StackedModifier> stackedMods = getStackedModifiers(cap.getActiveModifiers());

        int lvlH = 44;
        int effH = Math.max(50, 24 + stackedMods.size() * 16);
        int totalContentH = lvlH + 8 + effH;

        int visibleH = (int) VIRTUAL_H - 52;
        this.maxGrievanceScroll = Math.max(0, totalContentH - visibleH);

        int lvlY = 40 - Math.round(grievanceScrollAnim);
        drawGradientRect(rightX, lvlY, rightX + panelW, lvlY + lvlH, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
        drawBoxOutline(rightX, lvlY, panelW, lvlH, COLOR_LINE);

        int arrowW = 32;
        int dporCount = getDporCount();
        boolean hasDpor = dporCount > 0;
        boolean arrowHover = mouseX >= rightX && mouseX <= rightX + arrowW && mouseY >= lvlY && mouseY <= lvlY + lvlH;

        if (arrowHover && hasDpor) {
            drawRect(rightX, lvlY, rightX + arrowW, lvlY + lvlH, 0x33C98A2E);
        }
        drawLine(rightX + arrowW, lvlY, rightX + arrowW, lvlY + lvlH, COLOR_LINE);

        int arrowColor = hasDpor ? (arrowHover ? 0xFFFFFFFF : COLOR_GOLD_BRIGHT) : COLOR_INK;
        this.fontRenderer.drawString("▲", rightX + 12, lvlY + 18, arrowColor);

        SinType sin = cap.getChosenSin() != null ? cap.getChosenSin() : SinType.WRATH;
        this.fontRenderer.drawString(sin.getNameRu().toUpperCase(), rightX + arrowW + 8, lvlY + 9, COLOR_BLOOD_BRIGHT);
        this.fontRenderer.drawString("УР. " + cap.getSinLevel(), rightX + panelW - 48, lvlY + 9, COLOR_GOLD_BRIGHT);
        this.fontRenderer.drawString(hasDpor ? "● Готов к левел-апу" : "○ Нужна страница",
                rightX + arrowW + 8, lvlY + 25, hasDpor ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);

        int dporIconSize = 16;
        int dporIconX = rightX + panelW - dporIconSize - 6;
        int dporIconY = lvlY + 20;
        ItemStack dporIconStack = new ItemStack(efw.init.EfwModItems.DPOR);

        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        this.itemRender.renderItemAndEffectIntoGUI(mc.player, dporIconStack, dporIconX, dporIconY);
        RenderHelper.disableStandardItemLighting();

        if (!hasDpor) {
            drawRect(dporIconX, dporIconY, dporIconX + dporIconSize, dporIconY + dporIconSize, 0xCC0A0A0A);
        }

        boolean dporIconHover = mouseX >= dporIconX && mouseX <= dporIconX + dporIconSize
                && mouseY >= dporIconY && mouseY <= dporIconY + dporIconSize;
        if (dporIconHover) {
            tooltipTitle = dporIconStack.getDisplayName();
            tooltipFocus = "В инвентаре: " + dporCount;
            tooltipPrice = "Расходуется на левел-ап";
            tooltipX = mouseX + 12;
            tooltipY = mouseY;
        }

        int effY = lvlY + lvlH + 8;
        drawGradientRect(rightX, effY, rightX + panelW, effY + effH, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
        drawBoxOutline(rightX, effY, panelW, effH, COLOR_LINE);

        this.fontRenderer.drawString("АКТИВНЫЕ ЭФФЕКТЫ", rightX + 10, effY + 8, COLOR_PAPER_DIM);

        int curY = effY + 22;
        for (int i = 0; i < stackedMods.size(); i++) {
            StackedModifier mod = stackedMods.get(i);
            int color = mod.isBuff ? COLOR_GOLD_BRIGHT : COLOR_BLOOD_BRIGHT;
            String prefix = mod.isBuff ? "▲ " : "▼ ";

            this.fontRenderer.drawString(prefix + mod.nameRu, rightX + 10, curY, color);

            String valStr = mod.getFormattedValue();
            int valW = this.fontRenderer.getStringWidth(valStr);
            this.fontRenderer.drawString(valStr, rightX + panelW - 10 - valW, curY, color);

            curY += 16;
        }
    }

    private void drawManualsTab(int mouseX, int mouseY) {
        ISinCapability cap = getSinCap();
        if (cap == null)
            return;

        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;

        String[] bookNames = { "Холодное оружие", "Огнестрел", "Медикаменты", "Взлом", "Инструменты" };
        Item[] bookIcons = {
                efw.init.EfwModItems.MANUAL_MELEE,
                efw.init.EfwModItems.MANUAL_FIREARMS,
                efw.init.EfwModItems.MANUAL_MEDS,
                efw.init.EfwModItems.MANUAL_LOCKPICK,
                efw.init.EfwModItems.MANUAL_TOOLS
        };
        int[] progress = cap.getLoreBooksProgress();

        int rowH = 34;
        int bookH = 26 + bookNames.length * rowH;
        int visibleH = (int) VIRTUAL_H - 52;
        this.maxManualsScroll = Math.max(0, bookH - visibleH);

        int bookY = 40 - Math.round(manualsScrollAnim);
        drawGradientRect(rightX, bookY, rightX + panelW, bookY + bookH, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
        drawBoxOutline(rightX, bookY, panelW, bookH, COLOR_LINE);

        this.fontRenderer.drawString("ИЗУЧЕННЫЕ РУКОВОДСТВА", rightX + 10, bookY + 10, COLOR_PAPER_DIM);

        int bCurY = bookY + 26;
        int iconSize = 16;
        for (int i = 0; i < bookNames.length; i++) {
            int prog = i < progress.length ? progress[i] : 0;

            int iconX = rightX + 10;
            int iconY = bCurY;
            drawDashedOutline(iconX - 1, iconY - 1, iconSize + 2, iconSize + 2,
                    prog > 0 ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);

            if (i < bookIcons.length && bookIcons[i] != null) {
                ItemStack manualIconStack = new ItemStack(bookIcons[i]);
                GlStateManager.enableRescaleNormal();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableDepth();
                this.itemRender.renderItemAndEffectIntoGUI(mc.player, manualIconStack, iconX, iconY);
                RenderHelper.disableStandardItemLighting();

                if (prog <= 0) {
                    drawRect(iconX, iconY, iconX + iconSize, iconY + iconSize, 0xB00A0A0A);
                }
            }

            int textX = iconX + iconSize + 8;
            this.fontRenderer.drawString(bookNames[i], textX, bCurY + 2, COLOR_PAPER);

            String progStr = prog + " / 4";
            int progW = this.fontRenderer.getStringWidth(progStr);
            this.fontRenderer.drawString(progStr, rightX + panelW - 10 - progW, bCurY + 2,
                    prog > 0 ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);

            String buffPreview = prog > 0 ? "Бонус: —" : "Бонус: недоступно";
            this.fontRenderer.drawString(buffPreview, textX, bCurY + 15, COLOR_INK);

            bCurY += rowH;
        }
    }

    private void drawAppearanceTab(int mouseX, int mouseY) {
        int panelW = 236;
        int panelX = (int) VIRTUAL_W - panelW - 10;
        int panelY = 40;
        int panelH = (int) VIRTUAL_H - panelY - 10;

        GenderPlayer plr = getGenderPlayer();
        if (plr != null && blinkToggleButton != null && blinkToggleButton.visible) {
            boolean isOn = plr.isBlinkEnabled();
            int circleColor = isOn ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM;

            int btnX = blinkToggleButton.x;
            int btnY = blinkToggleButton.y;

            int cx = btnX + 8;
            int cy = btnY + 9;

            // Рисуем кружок у кнопки
            drawRect(cx - 2, cy - 2, cx + 2, cy + 2, circleColor);

            // Если моргание включено и редактор открыт — прорисовываем разветвленную линию ко всем активным слайдерам моргания
            if (isOn && blinkEditorOpen) {
                // Находим самый нижний видимый слайдер среди относящихся к морганию (id от 112 до 120)
                WildfireSlider lowestSlider = null;
                for (GuiButton btn : this.buttonList) {
                    if (btn instanceof WildfireSlider && btn.id >= 112 && btn.id <= 120 && btn.visible) {
                        if (lowestSlider == null || btn.y > lowestSlider.y) {
                            lowestSlider = (WildfireSlider) btn;
                        }
                    }
                }

                if (lowestSlider != null) {
                    int startLineY = cy + 4;
                    int endLineY = lowestSlider.y + lowestSlider.height / 2;

                    // Главная вертикальная шина проводки
                    drawLine(cx, startLineY, cx, endLineY, COLOR_PAPER_DIM);

                    // Ответвления-черточки к каждому активному слайдеру моргания
                    for (GuiButton btn : this.buttonList) {
                        if (btn instanceof WildfireSlider && btn.id >= 112 && btn.id <= 120 && btn.visible) {
                            int sliderCenterY = btn.y + btn.height / 2;
                            drawLine(cx, sliderCenterY, cx + 4, sliderCenterY, COLOR_PAPER_DIM);
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // 3. LEVEL UP OVERLAY FLOW (ENTER -> CARDS -> SELECT -> EXIT -> RETURN)
    // ==========================================
    private void drawLevelUpFlow(int mouseX, int mouseY, float partialTicks) {
        long now = System.nanoTime();
        long phaseElapsed = (now - levelUpPhaseStartTime) / 1_000_000L;

        switch (levelUpPhase) {
            case LVLUP_PHASE_ENTER: {
                float t = easeOutCubic(phaseElapsed / (float) LVLUP_ENTER_DURATION_MS);
                float modelOffsetX = -VIRTUAL_W * t;
                float panelOffsetX = VIRTUAL_W * t;
                drawMainHudScreen(mouseX, mouseY, partialTicks, modelOffsetX, panelOffsetX);

                if (phaseElapsed >= LVLUP_ENTER_DURATION_MS) {
                    levelUpPhase = LVLUP_PHASE_CARDS;
                    levelUpAnimStartTime = now;
                }
                break;
            }
            case LVLUP_PHASE_CARDS: {
                long cardsElapsed = (now - levelUpAnimStartTime) / 1_000_000L;
                drawLevelUpCardsListing(mouseX, mouseY, cardsElapsed);
                break;
            }
            case LVLUP_PHASE_SELECT: {
                long moveElapsed = Math.min(phaseElapsed, 600L);
                float t = moveElapsed / 600.0f;

                drawLevelUpSelection(t);

                if (phaseElapsed >= 1000L) {
                    levelUpPhase = LVLUP_PHASE_EXIT;
                    levelUpPhaseStartTime = now;
                }
                break;
            }
            case LVLUP_PHASE_EXIT: {
                float t = easeInCubic(phaseElapsed / (float) LVLUP_EXIT_DURATION_MS);
                drawLevelUpExit(t);
                if (phaseElapsed >= LVLUP_EXIT_DURATION_MS) {
                    levelUpPhase = LVLUP_PHASE_RETURN;
                    levelUpPhaseStartTime = now;
                }
                break;
            }
            case LVLUP_PHASE_RETURN: {
                float t = easeOutCubic(phaseElapsed / (float) LVLUP_RETURN_DURATION_MS);
                float modelOffsetX = -VIRTUAL_W * (1.0f - t);
                float panelOffsetX = VIRTUAL_W * (1.0f - t);
                drawMainHudScreen(mouseX, mouseY, partialTicks, modelOffsetX, panelOffsetX);

                if (phaseElapsed >= LVLUP_RETURN_DURATION_MS) {
                    this.currentState = STATE_MAIN_HUD;
                    this.levelUpPhase = LVLUP_PHASE_ENTER;
                    this.offeredCards = new ArrayList<>();
                    this.selectedCardIndex = -1;
                    initGui();
                }
                break;
            }
        }
    }

    private static final int LVLUP_CARD_W = 150;
    private static final int LVLUP_CARD_H = 220;
    private static final int LVLUP_CARD_GAP = 20;

    private int lvlUpCardBaseX(int index) {
        int totalW = 3 * LVLUP_CARD_W + 2 * LVLUP_CARD_GAP;
        int startX = ((int) VIRTUAL_W - totalW) / 2;
        return startX + index * (LVLUP_CARD_W + LVLUP_CARD_GAP);
    }

    private int lvlUpCardBaseY() {
        return ((int) VIRTUAL_H - LVLUP_CARD_H) / 2 + 10;
    }

    private void drawLevelUpCardsListing(int mouseX, int mouseY, long elapsed) {
        int lastCardIndex = Math.max(0, offeredCards.size() - 1);
        long lastCardDelay = LVLUP_CARD_INITIAL_DELAY_MS + lastCardIndex * LVLUP_CARD_STEP_DELAY_MS;
        long lastCardLandTime = lastCardDelay + LVLUP_CARD_FLIGHT_MS;
        float titleFade = clamp01((elapsed - lastCardLandTime) / (float) LVLUP_TITLE_FADE_MS);

        if (titleFade > 0.0f) {
            String title = "ВЫБЕРИТЕ КАРТУ СУДЬБЫ";
            int tW = this.fontRenderer.getStringWidth(title);
            int alphaByte = (int) (titleFade * 255.0f);
            int titleColor = (alphaByte << 24) | (COLOR_GOLD_BRIGHT & 0x00FFFFFF);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            this.fontRenderer.drawString(title, ((int) VIRTUAL_W - tW) / 2, 14, titleColor);
            GlStateManager.disableBlend();
        }

        int targetY = lvlUpCardBaseY();
        for (int i = 0; i < offeredCards.size(); i++) {
            SinCard card = offeredCards.get(i);
            int cx = lvlUpCardBaseX(i);

            long cardDelay = LVLUP_CARD_INITIAL_DELAY_MS + i * LVLUP_CARD_STEP_DELAY_MS;
            float progress = clamp01((elapsed - cardDelay) / (float) LVLUP_CARD_FLIGHT_MS);
            float ease = easeOutCubic(progress);
            int flightY = (int) (-300 + (targetY + 300) * ease);

            boolean landed = progress >= 0.999f;
            boolean isHovered = landed && mouseX >= cx && mouseX <= cx + LVLUP_CARD_W && mouseY >= flightY
                    && mouseY <= flightY + LVLUP_CARD_H;

            float targetLift = isHovered ? 4.0f : 0.0f;
            levelUpCardHoverLift[i] += (targetLift - levelUpCardHoverLift[i]) * Math.min(1.0f, currentDeltaSec * 15.0f);
            if (Math.abs(levelUpCardHoverLift[i] - targetLift) < 0.05f) {
                levelUpCardHoverLift[i] = targetLift;
            }
            int cy = landed ? Math.round(targetY - levelUpCardHoverLift[i]) : flightY;

            drawSingleCard(i, card, cx, cy, isHovered, -1.0f);
        }
    }

    private void drawLevelUpSelection(float t) {
        float ease = easeOutCubic(t);
        int targetY = lvlUpCardBaseY();

        for (int i = 0; i < offeredCards.size(); i++) {
            if (i == selectedCardIndex)
                continue;
            int baseX = lvlUpCardBaseX(i);
            int flyOffY = (int) ((LVLUP_CARD_H + targetY + 60) * ease);
            int cy = targetY - flyOffY;
            drawSingleCard(i, offeredCards.get(i), baseX, cy, false, -1.0f);
        }

        if (selectedCardIndex >= 0 && selectedCardIndex < offeredCards.size()) {
            int baseX = lvlUpCardBaseX(selectedCardIndex);
            int baseY = targetY;

            float scale = 1.0f + (LVLUP_SELECTED_SCALE - 1.0f) * ease;

            float centerX = baseX + LVLUP_CARD_W / 2.0f + (VIRTUAL_W / 2.0f - (baseX + LVLUP_CARD_W / 2.0f)) * ease;
            float centerY = baseY + LVLUP_CARD_H / 2.0f + (VIRTUAL_H / 2.0f - (baseY + LVLUP_CARD_H / 2.0f)) * ease;

            float glowT = t;

            GlStateManager.pushMatrix();
            GlStateManager.translate(centerX, centerY, 0.0f);
            GlStateManager.scale(scale, scale, 1.0f);
            GlStateManager.translate(-LVLUP_CARD_W / 2.0f, -LVLUP_CARD_H / 2.0f, 0.0f);
            drawSingleCard(selectedCardIndex, offeredCards.get(selectedCardIndex), 0, 0, false, glowT);
            GlStateManager.popMatrix();
        }
    }

    private void drawLevelUpExit(float t) {
        if (selectedCardIndex < 0 || selectedCardIndex >= offeredCards.size()) {
            return;
        }

        float centerX = VIRTUAL_W / 2.0f;
        float centerY = VIRTUAL_H / 2.0f + t * (VIRTUAL_H * 1.3f);
        float scale = LVLUP_SELECTED_SCALE;

        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX, centerY, 0.0f);
        GlStateManager.scale(scale, scale, 1.0f);
        GlStateManager.translate(-LVLUP_CARD_W / 2.0f, -LVLUP_CARD_H / 2.0f, 0.0f);
        drawSingleCard(selectedCardIndex, offeredCards.get(selectedCardIndex), 0, 0, false, -1.0f);
        GlStateManager.popMatrix();
    }

    private void drawSingleCard(int index, SinCard card, int cx, int cy, boolean isHovered, float borderGlowT) {
        drawGradientRect(cx, cy, cx + LVLUP_CARD_W, cy + LVLUP_CARD_H, 0xF2141210, 0xF20A0908);
        drawBoxOutline(cx, cy, LVLUP_CARD_W, LVLUP_CARD_H, isHovered ? COLOR_GOLD_BRIGHT : COLOR_BLOOD);

        if (borderGlowT >= 0.0f && borderGlowT < 1.0f) {
            drawPerimeterGlow(cx, cy, LVLUP_CARD_W, LVLUP_CARD_H, borderGlowT);
        }

        this.fontRenderer.drawString("КАРТА 0" + (index + 1), cx + 14, cy + 14, COLOR_PAPER_DIM);

        if (card.getBuff() != null) {
            drawDashedOutline(cx + 14, cy + 40, 22, 22, COLOR_GOLD_BRIGHT);
            this.fontRenderer.drawString("▲", cx + 21, cy + 47, COLOR_GOLD_BRIGHT);
            this.fontRenderer.drawString(card.getBuff().getNameRu(), cx + 42, cy + 42, COLOR_GOLD_BRIGHT);
            this.fontRenderer.drawString(card.getBuff().getFormattedValue(), cx + 42, cy + 53, COLOR_GOLD_BRIGHT);
        }

        if (card.getDebuff() != null) {
            drawDashedOutline(cx + 14, cy + 80, 22, 22, COLOR_BLOOD_BRIGHT);
            this.fontRenderer.drawString("▼", cx + 21, cy + 87, COLOR_BLOOD_BRIGHT);
            this.fontRenderer.drawString(card.getDebuff().getNameRu(), cx + 42, cy + 82, COLOR_BLOOD_BRIGHT);
            this.fontRenderer.drawString(card.getDebuff().getFormattedValue(), cx + 42, cy + 93,
                    COLOR_BLOOD_BRIGHT);
        }

        String prompt = isHovered ? TextFormatting.YELLOW + "▶ ВЫБРАТЬ ◀" : "нажмите, чтобы принять";
        int pW = this.fontRenderer.getStringWidth(prompt);
        this.fontRenderer.drawString(prompt, cx + (LVLUP_CARD_W - pW) / 2, cy + LVLUP_CARD_H - 20,
                isHovered ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);
    }

    private void drawPerimeterGlow(int x, int y, int w, int h, float t) {
        float loopT = t - (float) Math.floor(t);
        float perimeter = 2.0f * (w + h);
        float dist = loopT * perimeter;

        int glowColor = 0xFFFFCB6B;
        int chip = 10;
        int thickness = 3;

        if (dist < w) {
            int px = x + (int) dist;
            drawRect(px, y - thickness / 2, Math.min(px + chip, x + w), y + thickness / 2 + 1, glowColor);
        } else if (dist < w + h) {
            int py = y + (int) (dist - w);
            drawRect(x + w - thickness / 2, py, x + w + thickness / 2 + 1, Math.min(py + chip, y + h), glowColor);
        } else if (dist < 2 * w + h) {
            int px = x + w - (int) (dist - w - h);
            drawRect(Math.max(px - chip, x), y + h - thickness / 2, px, y + h + thickness / 2 + 1, glowColor);
        } else {
            int py = y + h - (int) (dist - 2 * w - h);
            drawRect(x - thickness / 2, Math.max(py - chip, y), x + thickness / 2 + 1, py, glowColor);
        }
    }

    private static float easeOutCubic(float t) {
        float c = clamp01(t);
        return 1.0f - (float) Math.pow(1.0f - c, 3);
    }

    private static float easeInCubic(float t) {
        float c = clamp01(t);
        return c * c * c;
    }

    private static float clamp01(float v) {
        return Math.max(0.0f, Math.min(1.0f, v));
    }

    // ==========================================
    // MOUSE CLICK HANDLING
    // ==========================================
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int vX = toVirtX(mouseX);
        int vY = toVirtY(mouseY);
        super.mouseClicked(vX, vY, mouseButton);

        if (mouseButton == 0) {
            if (this.currentState == STATE_CLASS_SELECT) {
                handleClassSelectClick(vX, vY);
            } else if (this.currentState == STATE_MAIN_HUD) {
                handleMainHudClick(vX, vY);
            } else if (this.currentState == STATE_LEVEL_UP) {
                handleLevelUpClick(vX, vY);
            }
        }
    }

    private void handleClassSelectClick(int mouseX, int mouseY) {
        int cardW = 88;
        int cardH = 110;
        int gapX = 10;
        int gapY = 8;

        int centerY = (int) VIRTUAL_H / 2;
        int topHeaderY = Math.max(8, centerY - 130);

        int row1TotalW = 4 * cardW + 3 * gapX;
        int row1StartX = ((int) VIRTUAL_W - row1TotalW) / 2;
        int row1StartY = topHeaderY + 26;

        int row2TotalW = 3 * cardW + 2 * gapX;
        int row2StartX = ((int) VIRTUAL_W - row2TotalW) / 2;
        int row2StartY = row1StartY + cardH + gapY;

        SinType[] sins = SinType.values();
        for (int i = 0; i < sins.length; i++) {
            int cx, cy;
            if (i < 4) {
                cx = row1StartX + i * (cardW + gapX);
                cy = row1StartY;
            } else {
                cx = row2StartX + (i - 4) * (cardW + gapX);
                cy = row2StartY;
            }

            int infoX = cx + 5;
            int infoY = cy + 5;
            if (mouseX >= infoX && mouseX <= infoX + 11 && mouseY >= infoY && mouseY <= infoY + 11) {
                return;
            }

            if (mouseX >= cx && mouseX <= cx + cardW && mouseY >= cy && mouseY <= cy + cardH) {
                SinType selectedSin = sins[i];
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketSelectSin(selectedSin));
                this.currentState = STATE_MAIN_HUD;
                initGui();
                return;
            }
        }
    }

    private void handleMainHudClick(int mouseX, int mouseY) {
        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;

        int tabGap = 4;
        int tabW = (panelW - tabGap * 2) / 3;
        int tabH = 18;
        int tabY = 16;

        int tab1X = rightX;
        int tab2X = rightX + tabW + tabGap;
        int tab3X = rightX + (tabW + tabGap) * 2;

        if (mouseX >= tab1X && mouseX <= tab1X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            this.currentTab = TAB_GRIEVANCE;
            initGui();
            return;
        }
        if (mouseX >= tab2X && mouseX <= tab2X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            this.currentTab = TAB_MANUALS;
            initGui();
            return;
        }
        if (mouseX >= tab3X && mouseX <= tab3X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            this.currentTab = TAB_APPEARANCE;
            initGui();
            return;
        }

        if (currentTab == TAB_GRIEVANCE) {
            int gRightX = (int) VIRTUAL_W - panelW - 10;
            int lvlY = 40 - Math.round(grievanceScrollAnim);
            int arrowW = 32;
            int lvlH = 44;

            if (mouseX >= gRightX && mouseX <= gRightX + arrowW && mouseY >= lvlY && mouseY <= lvlY + lvlH) {
                if (getDporCount() > 0 || (mc.player != null && mc.player.isCreative())) {
                    MwccfMod.PACKET_HANDLER.sendToServer(new PacketLevelUpRequest());
                }
            }
        }
    }

    private void handleLevelUpClick(int mouseX, int mouseY) {
        if (levelUpPhase != LVLUP_PHASE_CARDS) {
            return;
        }

        int targetY = lvlUpCardBaseY();

        for (int i = 0; i < offeredCards.size(); i++) {
            int cx = lvlUpCardBaseX(i);
            int cy = targetY;

            if (mouseX >= cx && mouseX <= cx + LVLUP_CARD_W && mouseY >= cy && mouseY <= cy + LVLUP_CARD_H) {
                SinCard card = offeredCards.get(i);
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketAcceptCard(card));
                this.selectedCardIndex = i;
                this.levelUpPhase = LVLUP_PHASE_SELECT;
                this.levelUpPhaseStartTime = System.nanoTime();
                return;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        int vX = toVirtX(mouseX);
        int vY = toVirtY(mouseY);
        super.mouseReleased(vX, vY, state);
        if (breastSlider != null)
            breastSlider.mouseReleased(vX, vY);
        if (xOffsetBoobSlider != null)
            xOffsetBoobSlider.mouseReleased(vX, vY);
        if (yOffsetBoobSlider != null)
            yOffsetBoobSlider.mouseReleased(vX, vY);
        if (zOffsetBoobSlider != null)
            zOffsetBoobSlider.mouseReleased(vX, vY);
        if (cleavageSlider != null)
            cleavageSlider.mouseReleased(vX, vY);
        if (bounceSlider != null)
            bounceSlider.mouseReleased(vX, vY);
        if (floppySlider != null)
            floppySlider.mouseReleased(vX, vY);
    }

    // ==========================================
    // DRAWING PRIMITIVES & HELPERS
    // ==========================================
    private void drawCustomTooltip(int x, int y, String title, String focus, String price) {
        int w = 190;
        int h = 54;
        drawRect(x, y, x + w, y + h, 0xF7050505);
        drawBoxOutline(x, y, w, h, COLOR_BLOOD);

        this.fontRenderer.drawString(title, x + 6, y + 6, COLOR_PAPER);
        this.fontRenderer.drawString(focus, x + 6, y + 20, COLOR_GOLD_BRIGHT);
        this.fontRenderer.drawString(price, x + 6, y + 34, COLOR_BLOOD_BRIGHT);
    }

    private void drawBoxOutline(int x, int y, int w, int h, int color) {
        drawLine(x, y, x + w, y, color);
        drawLine(x + w, y, x + w, y + h, color);
        drawLine(x + w, y + h, x, y + h, color);
        drawLine(x, y + h, x, y, color);
    }

    private void drawLine(int x1, int y1, int x2, int y2, int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, a);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        bb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        bb.pos(x1, y1, 0.0D).endVertex();
        bb.pos(x2, y2, 0.0D).endVertex();
        tess.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private void drawDashedLineH(int x1, int x2, int y, int color) {
        int dash = 4;
        for (int x = x1; x < x2; x += dash * 2) {
            drawLine(x, y, Math.min(x + dash, x2), y, color);
        }
    }

    private void drawDashedOutline(int x, int y, int w, int h, int color) {
        drawDashedLineH(x, x + w, y, color);
        drawDashedLineH(x, x + w, y + h, color);
        for (int curY = y; curY < y + h; curY += 8) {
            drawLine(x, curY, x, Math.min(curY + 4, y + h), color);
            drawLine(x + w, curY, x + w, Math.min(curY + 4, y + h), color);
        }
    }

    private void drawDiamond(int cx, int cy, int size, int color) {
        drawLine(cx, cy - size, cx + size, cy, color);
        drawLine(cx + size, cy, cx, cy + size, color);
        drawLine(cx, cy + size, cx - size, cy, color);
        drawLine(cx - size, cy, cx, cy - size, color);
    }
}