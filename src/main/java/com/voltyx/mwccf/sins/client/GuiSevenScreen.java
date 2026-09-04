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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;

import com.voltyx.mwccf.render.flower.BedrockFlowerRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class GuiSevenScreen extends GuiScreen {
    // ===1 Фиксированный виртуальный холст под эталонную настройку (2560x1369, GUI Scale = Авто) ===
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
    public static final int TAB_SKILLS = 1;
    public static final int TAB_MANUALS = 2;
    public static final int TAB_APPEARANCE = 3;

    // Palette Colors (Warm Noir / Deep Vintage Umber)
    private static final int COLOR_BG_TOP = 0xFF181310;
    private static final int COLOR_BG_BOTTOM = 0xFF0E0B09;
    private static final int COLOR_BG = 0xFF14100D;
    private static final int COLOR_PANEL_TOP = 0xF41E1814;
    private static final int COLOR_PANEL_BOTTOM = 0xF7120E0C;
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

    // ================== FLOWER MODEL & SKILL TREE TUNING ==================
    public static boolean FLOWER_TUNE_MODE = true; // Enabled debug tuning HUD
    private final BedrockFlowerRenderer flowerModel = new BedrockFlowerRenderer();

    // Flower screen position, rotation and scale parameters
    private int flowerPosX = 134;
    private int flowerPosY = 321;
    private float flowerScreenScale = 303.18015f;
    private float flowerOffsetX = -42.174496f;
    private float flowerOffsetY = -7.199999f;
    private float flowerOffsetZ = 0.0f;
    private float flowerYaw = 20.95074f;
    private float flowerPitch = 60.949986f;
    private float flowerRoll = 20.45f;
    private float flowerAnimSpeed = 2.4f;

    // Petal & Wave Ingame Debug Maker Parameters
    private int debugPetalSelect = 0;   // 0..11 petal
    private int debugQueueSlotSelect = 0; // 0..11 queue slot (skill 1..12)
    private float debugWaveProgress = 0f; // 0.0 .. 1.0 manual scrub
    private boolean debugManualWave = false; // whether to manually control waveProgress
    private float debugDistStart = 0.0f; // custom wave start dist (0.0=base)
    private float debugDistEnd = 1.0f;   // custom wave end dist (1.0=tip)

    private static final String[] FLOWER_PARAM_NAMES = {
            "Лепесток [0..11]",
            "Слот очереди [1..12]",
            "Подсветка (ВКЛ/ВЫКЛ)",
            "Направление волны",
            "Старт волны (distStart)",
            "Конец волны (distEnd)",
            "Ширина огня (px)",
            "Ширина пепла",
            "Скорость волны",
            "Угол волны (deg)",
            "Ручной прогресс (0..1)",
            "Карта дистанций (Debug)",
            "posX", "posY", "screenScale", "yaw", "pitch", "roll"
    };
    private int flowerSelectedParam = 0;
    private float flowerStepMultiplier = 1f; // LEFT/RIGHT change this (x0.1 / x10)

    // Smooth flower slide-in animation from bottom
    private float flowerSlideAnim = 0f;
    // ======================================================================
    private final GuiScreen parentScreen;

    // Ash dust particles
    private final InspectDustManager dustManager = new InspectDustManager();
    private long lastFrameTime = System.nanoTime();
    private int dustInitWidth = -1;
    private int dustInitHeight = -1;

    // Smooth hover animation for cards
    private final float[] sinCardHoverLift = new float[7];

    // Class-select flow sub-phases: mirrors the level-up SELECT/EXIT/RETURN flow so
    // choosing a sin feels the same as accepting a level-up card, and doesn't just
    // instantly cut to the main HUD.
    private static final int CLASS_PHASE_LISTING = 0; // grid of 7 sins, clickable
    private static final int CLASS_PHASE_SELECT = 1;  // chosen card centers+grows, others fly up, orange trace
    private static final int CLASS_PHASE_EXIT = 2;    // chosen card slides down off screen
    private static final int CLASS_PHASE_RETURN = 3;  // main HUD model+panel slide in from the sides

    private int classSelectPhase = CLASS_PHASE_LISTING;
    private long classSelectPhaseStartTime = 0L;
    private int selectedSinIndex = -1;
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

    // ===== Skill tree (per-sin passive skills) =====
    private static final int SKILL_BRANCHES = 3;
    private static final int SKILLS_PER_BRANCH = 4;
    private static final int SKILL_COUNT = 1 + SKILL_BRANCHES * SKILLS_PER_BRANCH; // 13
    private static final int SKILL_NODE_SIZE = 22;
    private static final int SKILL_ROW_GAP = 30;
    private static final long SKILL_UNLOCK_GLOW_MS = 900L;

    private static final class SkillNode {
        final int id;
        final int branch; // -1 for the root
        final int depth;  // 0 for the root, 1..SKILLS_PER_BRANCH within a branch
        final String name;
        final String description;

        SkillNode(int id, int branch, int depth, String name, String description) {
            this.id = id;
            this.branch = branch;
            this.depth = depth;
            this.name = name;
            this.description = description;
        }
    }

    // TODO(placeholder content): real per-sin skill data should come from a
    // data-driven source (JSON/registry) keyed by SinType once the design is
    // final. This dummy Wrath-flavored set exists only to build/test the tree
    // UI itself - entries tagged [ЗАГЛУШКА] are pure filler text.
    private static final SkillNode[] SKILL_NODES = {
            new SkillNode(0, -1, 0, "Притупление",
                    "Пока вы под угрозой: тряска экрана и сбивание прицела от полученного урона ощутимо слабее."),
            new SkillNode(1, 0, 1, "Загнанный зверь",
                    "Пока здоровье ниже 50%, скорость бега увеличена."),
            new SkillNode(2, 0, 2, "Инерция боли",
                    "Чем меньше остаётся здоровья, тем быстрее перезаряжается оружие."),
            new SkillNode(3, 0, 3, "Шрамы",
                    "Атаки заражённых паразитом врагов с шансом не вызывают кровотечение."),
            new SkillNode(4, 0, 4, "Мёртвый нерв",
                    "Раз в 10 минут смертельный урон игнорируется: здоровье остаётся на 1, и вы неуязвимы следующие 2 секунды."),
            new SkillNode(5, 1, 1, "Холодная ярость",
                    "Расход выносливости в бою снижен."),
            new SkillNode(6, 1, 2, "[ЗАГЛУШКА] Стойкость плоти",
                    "Пока рядом заражённый паразитом враг, регенерация здоровья не прекращается."),
            new SkillNode(7, 1, 3, "[ЗАГЛУШКА] Хладнокровие",
                    "После добивания врага следующий выстрел не имеет разброса и отдачи."),
            new SkillNode(8, 1, 4, "[ЗАГЛУШКА] Второе дыхание",
                    "При падении здоровья ниже 20% вы на 3 секунды получаете иммунитет к оглушению и замедлению."),
            new SkillNode(9, 2, 1, "[ЗАГЛУШКА] Верный прицел",
                    "Первый выстрел по новой цели всегда критический."),
            new SkillNode(10, 2, 2, "[ЗАГЛУШКА] Стальные нервы",
                    "Получение урона больше не сбивает прицеливание."),
            new SkillNode(11, 2, 3, "[ЗАГЛУШКА] Голод хищника",
                    "Добивание врага восстанавливает часть выносливости."),
            new SkillNode(12, 2, 4, "[ЗАГЛУШКА] Последний вдох",
                    "Если удар должен был убить вас, здоровье не может опуститься ниже 1 в течение следующих 4 секунд."),
    };

    // Client-side placeholder state. TODO: replace with real data wired to
    // ISinCapability + a network packet (e.g. PacketUnlockSkill) once the actual
    // skill-tree backend/persistence exists. This is purely visual for now.
    private boolean skillTreeInitialized = false;
    private final boolean[] skillUnlocked = new boolean[SKILL_COUNT];
    private final long[] skillUnlockAnimStart = new long[SKILL_COUNT];
    private int skillPoints = 5; // placeholder value for testing the UI

    private int skillsScrollY = 0;
    private int maxSkillsScroll = 0;
    private float skillsScrollAnim = 0f;

    private SkillNode hoveredSkillNodeForTooltip = null;
    private int hoveredSkillTooltipX = 0;
    private int hoveredSkillTooltipY = 0;

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
        if (FLOWER_TUNE_MODE && currentTab == TAB_SKILLS) {
            if (keyCode == Keyboard.KEY_TAB) {
                flowerSelectedParam = (flowerSelectedParam + 1) % FLOWER_PARAM_NAMES.length;
                return;
            }
            if (keyCode == Keyboard.KEY_UP) {
                flowerAdjustParam(flowerSelectedParam,
                        flowerBaseStep(flowerSelectedParam) * flowerStepMultiplier);
                return;
            }
            if (keyCode == Keyboard.KEY_DOWN) {
                flowerAdjustParam(flowerSelectedParam,
                        -flowerBaseStep(flowerSelectedParam) * flowerStepMultiplier);
                return;
            }
            if (keyCode == Keyboard.KEY_LEFT) {
                flowerStepMultiplier = Math.max(0.01f, flowerStepMultiplier * 0.1f);
                return;
            }
            if (keyCode == Keyboard.KEY_RIGHT) {
                flowerStepMultiplier = Math.min(1000f, flowerStepMultiplier * 10f);
                return;
            }
            if (keyCode == Keyboard.KEY_P) {
                // Ignite selected petal (or random)
                debugManualWave = false;
                flowerModel.triggerPetalBurn(debugPetalSelect);
                return;
            }
            if (keyCode == Keyboard.KEY_R) {
                // Reset all smolder and burns
                debugManualWave = false;
                debugWaveProgress = 0f;
                flowerModel.resetPetalBurns();
                return;
            }
            if (keyCode == Keyboard.KEY_H) {
                // Toggle debug overlay visibility
                FLOWER_TUNE_MODE = !FLOWER_TUNE_MODE;
                return;
            }
            if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                flowerPrintValuesToLog();
                return;
            }
        }
        if (currentTab == TAB_SKILLS && keyCode == Keyboard.KEY_H) {
            FLOWER_TUNE_MODE = !FLOWER_TUNE_MODE;
            return;
        }
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

        if (this.currentState == STATE_MAIN_HUD && this.currentTab == TAB_SKILLS) {
            flowerModel.playOpen();
        }

        if (this.currentState == STATE_MAIN_HUD && this.currentTab == TAB_APPEARANCE) {
            initAppearanceControls();
        }
    }

    public void setTab(int newTab) {
        if (this.currentTab != newTab) {
            int oldTab = this.currentTab;
            this.currentTab = newTab;
            if (newTab == TAB_SKILLS) {
                flowerModel.playOpen();
            } else if (oldTab == TAB_SKILLS) {
                flowerModel.playClose(1.8f);
            }
            initGui();
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
                } else if (currentTab == TAB_SKILLS) {
                    if (dWheel > 0) {
                        skillsScrollY = Math.max(0, skillsScrollY - 22);
                    } else {
                        skillsScrollY = Math.min(maxSkillsScroll, skillsScrollY + 22);
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
        skillsScrollAnim = approachScroll(skillsScrollAnim, skillsScrollY, deltaSec);

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

        drawGradientRect(0, 0, (int) VIRTUAL_W, (int) VIRTUAL_H, COLOR_BG_TOP, COLOR_BG_BOTTOM);

        GlStateManager.pushMatrix();
        this.dustManager.updateAndRender((int) VIRTUAL_W, (int) VIRTUAL_H, mc);
        GlStateManager.popMatrix();

        this.fontRenderer.drawString("DOSSIER // SEVEN", 16, 14, COLOR_BLOOD_BRIGHT);

        if (this.currentState == STATE_CLASS_SELECT) {
            drawClassSelectFlow(vMouseX, vMouseY, deltaSec, partialTicks);
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
    private static final int SIN_CARD_W = 88;
    private static final int SIN_CARD_H = 110;
    private static final int SIN_GAP_X = 10;
    private static final int SIN_GAP_Y = 8;

    /** Base (un-lifted, un-animated) grid position for sin card index i. */
    private int sinCardBaseX(int i) {
        int row1TotalW = 4 * SIN_CARD_W + 3 * SIN_GAP_X;
        int row1StartX = ((int) VIRTUAL_W - row1TotalW) / 2;
        int row2TotalW = 3 * SIN_CARD_W + 2 * SIN_GAP_X;
        int row2StartX = ((int) VIRTUAL_W - row2TotalW) / 2;
        return i < 4 ? row1StartX + i * (SIN_CARD_W + SIN_GAP_X) : row2StartX + (i - 4) * (SIN_CARD_W + SIN_GAP_X);
    }

    private int sinCardBaseY(int i) {
        int centerY = (int) VIRTUAL_H / 2;
        int topHeaderY = Math.max(8, centerY - 130);
        int row1StartY = topHeaderY + 26;
        int row2StartY = row1StartY + SIN_CARD_H + SIN_GAP_Y;
        return i < 4 ? row1StartY : row2StartY;
    }

    private void drawClassSelectScreen(int mouseX, int mouseY, float deltaSec) {
        String title = "ВЫБЕРИ СВОЙ ГРЕХ";
        String subtitle = "ПЕРВОНАЧАЛЬНЫЙ ВЫБОР ОПРЕДЕЛИТ ВАШИ СЛАБОСТИ И СИЛЫ";

        int titleW = this.fontRenderer.getStringWidth(title);
        int subW = this.fontRenderer.getStringWidth(subtitle);

        int centerY = (int) VIRTUAL_H / 2;
        int topHeaderY = Math.max(8, centerY - 130);
        this.fontRenderer.drawString(title, ((int) VIRTUAL_W - titleW) / 2, topHeaderY, COLOR_PAPER);
        this.fontRenderer.drawString(subtitle, ((int) VIRTUAL_W - subW) / 2, topHeaderY + 11, COLOR_PAPER_DIM);

        SinType[] sins = SinType.values();

        for (int i = 0; i < sins.length; i++) {
            SinType sin = sins[i];
            int cx = sinCardBaseX(i);
            int cy = sinCardBaseY(i);

            boolean isHovered = mouseX >= cx && mouseX <= cx + SIN_CARD_W && mouseY >= cy && mouseY <= cy + SIN_CARD_H;

            float targetLift = isHovered ? 4.0f : 0.0f;
            sinCardHoverLift[i] += (targetLift - sinCardHoverLift[i]) * Math.min(1.0f, deltaSec * 15.0f);
            if (Math.abs(sinCardHoverLift[i] - targetLift) < 0.05f) {
                sinCardHoverLift[i] = targetLift;
            }
            int drawY = Math.round(cy - sinCardHoverLift[i]);

            drawSinCard(sin, i, cx, drawY, mouseX, mouseY, isHovered, -1.0f);
        }
    }

    /**
     * Draws one sin card at (cx,cy) in whatever coordinate space is currently
     * active (callers may have pushed a translate/scale matrix for the SELECT/EXIT
     * animation). Pass borderGlowT >= 0 to draw the chasing orange perimeter glow
     * used while the card is being confirmed as the player's pick.
     */
    private void drawSinCard(SinType sin, int index, int cx, int cy, int mouseX, int mouseY, boolean isHovered,
            float borderGlowT) {
        drawGradientRect(cx, cy, cx + SIN_CARD_W, cy + SIN_CARD_H, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
        drawBoxOutline(cx, cy, SIN_CARD_W, SIN_CARD_H, isHovered ? COLOR_BLOOD_BRIGHT : COLOR_LINE);

        if (borderGlowT >= 0.0f) {
            drawPerimeterGlow(cx, cy, SIN_CARD_W, SIN_CARD_H, borderGlowT);
        }

        drawRect(cx, cy, cx + 4, cy + 4, COLOR_BG);
        drawLine(cx, cy + 4, cx + 4, cy, isHovered ? COLOR_BLOOD_BRIGHT : COLOR_LINE);

        this.fontRenderer.drawString(sin.getNumber(), cx + SIN_CARD_W - 14, cy + 5, COLOR_INK);

        int infoX = cx + 5;
        int infoY = cy + 5;
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

        int iconX = cx + (SIN_CARD_W - 34) / 2;
        int iconY = cy + 22;
        drawDashedOutline(iconX, iconY, 34, 34, isHovered ? COLOR_BLOOD_BRIGHT : COLOR_PAPER_DIM);
        drawCenteredString(this.fontRenderer, "ICON", iconX + 17, iconY + 13,
                isHovered ? COLOR_BLOOD_BRIGHT : COLOR_INK);

        int nameW = this.fontRenderer.getStringWidth(sin.getNameRu());
        this.fontRenderer.drawString(sin.getNameRu(), cx + (SIN_CARD_W - nameW) / 2, cy + SIN_CARD_H - 14,
                isHovered ? COLOR_GOLD_BRIGHT : COLOR_PAPER);
    }

    /**
     * Dispatches the class-select screen through its sub-phases: normal clickable
     * LISTING, then (once a sin is clicked) SELECT -> EXIT -> RETURN, mirroring the
     * level-up card flow so choosing a sin doesn't just instantly cut to the HUD.
     */
    private void drawClassSelectFlow(int mouseX, int mouseY, float deltaSec, float partialTicks) {
        long now = System.nanoTime();
        long phaseElapsed = (now - classSelectPhaseStartTime) / 1_000_000L;

        switch (classSelectPhase) {
            case CLASS_PHASE_LISTING: {
                drawClassSelectScreen(mouseX, mouseY, deltaSec);
                break;
            }
            case CLASS_PHASE_SELECT: {
                float t = clamp01(phaseElapsed / (float) LVLUP_SELECT_DURATION_MS);
                drawClassSelectHighlight(t);
                if (phaseElapsed >= LVLUP_SELECT_DURATION_MS) {
                    classSelectPhase = CLASS_PHASE_EXIT;
                    classSelectPhaseStartTime = now;
                }
                break;
            }
            case CLASS_PHASE_EXIT: {
                float t = easeInCubic(phaseElapsed / (float) LVLUP_EXIT_DURATION_MS);
                drawClassSelectExit(t);
                if (phaseElapsed >= LVLUP_EXIT_DURATION_MS) {
                    classSelectPhase = CLASS_PHASE_RETURN;
                    classSelectPhaseStartTime = now;
                }
                break;
            }
            case CLASS_PHASE_RETURN: {
                // Mirror of the level-up RETURN phase: model+panel slide in from the
                // sides into the normal HUD position. Background/dust stay as-is.
                float t = easeOutCubic(phaseElapsed / (float) LVLUP_RETURN_DURATION_MS);
                float modelOffsetX = -VIRTUAL_W * (1.0f - t);
                float panelOffsetX = VIRTUAL_W * (1.0f - t);
                drawMainHudScreen(mouseX, mouseY, partialTicks, modelOffsetX, panelOffsetX);

                if (phaseElapsed >= LVLUP_RETURN_DURATION_MS) {
                    this.currentState = STATE_MAIN_HUD;
                    this.classSelectPhase = CLASS_PHASE_LISTING;
                    this.selectedSinIndex = -1;
                    initGui();
                }
                break;
            }
        }
    }

    /** PHASE_SELECT: chosen sin centers+grows+glows, the other six fly up off screen. */
    private void drawClassSelectHighlight(float t) {
        float ease = easeOutCubic(t);
        SinType[] sins = SinType.values();

        for (int i = 0; i < sins.length; i++) {
            if (i == selectedSinIndex) {
                continue; // drawn last, on top
            }
            int baseX = sinCardBaseX(i);
            int baseY = sinCardBaseY(i);
            int flyOffY = (int) ((SIN_CARD_H + baseY + 60) * ease);
            int cy = baseY - flyOffY;
            drawSinCard(sins[i], i, baseX, cy, -9999, -9999, false, -1.0f);
        }

        if (selectedSinIndex >= 0 && selectedSinIndex < sins.length) {
            int baseX = sinCardBaseX(selectedSinIndex);
            int baseY = sinCardBaseY(selectedSinIndex);

            float scale = 1.0f + (LVLUP_SELECTED_SCALE - 1.0f) * ease;
            float centerX = baseX + SIN_CARD_W / 2.0f + (VIRTUAL_W / 2.0f - (baseX + SIN_CARD_W / 2.0f)) * ease;
            float centerY = baseY + SIN_CARD_H / 2.0f + (VIRTUAL_H / 2.0f - (baseY + SIN_CARD_H / 2.0f)) * ease;

            // Orange highlight travels around the perimeter ~1.5 times over the phase.
            float glowT = (t * 1.5f) % 1.0f;

            GlStateManager.pushMatrix();
            GlStateManager.translate(centerX, centerY, 0.0f);
            GlStateManager.scale(scale, scale, 1.0f);
            GlStateManager.translate(-SIN_CARD_W / 2.0f, -SIN_CARD_H / 2.0f, 0.0f);
            drawSinCard(sins[selectedSinIndex], selectedSinIndex, 0, 0, -9999, -9999, false, glowT);
            GlStateManager.popMatrix();
        }
    }

    /** PHASE_EXIT: the chosen sin card (still centered+enlarged) slides down off the bottom. */
    private void drawClassSelectExit(float t) {
        SinType[] sins = SinType.values();
        if (selectedSinIndex < 0 || selectedSinIndex >= sins.length) {
            return;
        }

        float centerX = VIRTUAL_W / 2.0f;
        float centerY = VIRTUAL_H / 2.0f + t * (VIRTUAL_H * 1.3f);
        float scale = LVLUP_SELECTED_SCALE;

        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX, centerY, 0.0f);
        GlStateManager.scale(scale, scale, 1.0f);
        GlStateManager.translate(-SIN_CARD_W / 2.0f, -SIN_CARD_H / 2.0f, 0.0f);
        drawSinCard(sins[selectedSinIndex], selectedSinIndex, 0, 0, -9999, -9999, false, -1.0f);
        GlStateManager.popMatrix();
    }

    // ==========================================
    // 2. MAIN HUD SCREEN (4 TABS ALIGNED TO PANEL)
    // ==========================================
    private void drawMainHudScreen(int mouseX, int mouseY, float partialTicks, float modelOffsetX, float panelOffsetX) {
        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;

        // Smooth transition between Player and Flower
        float targetSlide = (currentTab == TAB_SKILLS) ? 1.0f : 0.0f;
        flowerSlideAnim += (targetSlide - flowerSlideAnim) * Math.min(1.0f, currentDeltaSec * 8.0f);
        if (Math.abs(targetSlide - flowerSlideAnim) < 0.001f) {
            flowerSlideAnim = targetSlide;
        }

        // 1. Рендерим 3D модель цветка (слой ниже) и 3D модель игрока (слой выше)
        GlStateManager.pushMatrix();
        GlStateManager.translate(modelOffsetX, 0.0f, 0.0f);

        // Цветок рендерится первым (слой ниже персонажа)
        if (flowerSlideAnim > 0.001f || !flowerModel.isFullyClosed()) {
            drawFlowerDisplay(mouseX, mouseY, partialTicks, currentDeltaSec, flowerSlideAnim);
        }

        // Очищаем Depth Buffer, чтобы модель персонажа всегда рисовалась строго ПОВЕРХ цветка
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

        // Модель персонажа рендерится поверх
        if (flowerSlideAnim < 0.999f) {
            drawGender3DPlayer();
        }
        GlStateManager.popMatrix();

        // 2. Рендерим правую панель со сдвигом
        GlStateManager.pushMatrix();
        GlStateManager.translate(panelOffsetX, 0.0f, 0.0f);

        int tabGap = 3;
        int tabW = (panelW - tabGap * 3) / 4;
        int tabH = 18;
        int tabY = 16;
        int tab1X = rightX;
        int tab2X = rightX + (tabW + tabGap);
        int tab3X = rightX + (tabW + tabGap) * 2;
        int tab4X = rightX + (tabW + tabGap) * 3;

        drawTab(tab1X, tabY, tabW, tabH, "Grievance", currentTab == TAB_GRIEVANCE, mouseX, mouseY);
        drawTab(tab2X, tabY, tabW, tabH, "Skills", currentTab == TAB_SKILLS, mouseX, mouseY);
        drawTab(tab3X, tabY, tabW, tabH, "Manuals", currentTab == TAB_MANUALS, mouseX, mouseY);
        drawTab(tab4X, tabY, tabW, tabH, "Form", currentTab == TAB_APPEARANCE, mouseX, mouseY);

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
        } else if (currentTab == TAB_SKILLS) {
            drawSkillsTab(mouseX, mouseY);
        } else if (currentTab == TAB_MANUALS) {
            drawManualsTab(mouseX, mouseY);
        } else {
            drawAppearanceTab(mouseX, mouseY);
        }

        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);

        if (currentTab == TAB_SKILLS && hoveredSkillNodeForTooltip != null) {
            drawSkillTooltip(hoveredSkillNodeForTooltip, hoveredSkillTooltipX, hoveredSkillTooltipY);
        }

        GlStateManager.popMatrix();

        // Topmost GUI Layer: Render Flower Debug Maker on the right side over everything
        if (FLOWER_TUNE_MODE && currentTab == TAB_SKILLS) {
            drawFlowerTuneOverlay();
        }
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
                int posY = (int) VIRTUAL_H / 2 + 260 + (int) (flowerSlideAnim * 320f);

                float targetYaw = isBlinkEdit ? 0.0f : -20.0f;
                animatedFaceYaw += (targetYaw - animatedFaceYaw) * 0.15f;
                float mX = animatedFaceYaw;
                float mY = animatedFaceYaw;

                efw.util.RenderContext.isRenderingPlayerInGui = true;
                efw.util.RenderContext.isRenderingPlayerInSevenScreen = true;

                // Tick SevenScreen player animation player so idle_standing animates in real-time
                efw.animation.AnimationPlayer sevenPlayer = efw.animation.AnimationRegistry.getSevenScreenPlayer();
                if (sevenPlayer != null) {
                    sevenPlayer.tick(currentDeltaSec * 20.0f);
                }

                WildfirePlayerListScreen.drawEntityOnScreen(posX, posY, scale, mX, mY, ent);
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                efw.util.RenderContext.isRenderingPlayerInSevenScreen = false;
                efw.util.RenderContext.isRenderingPlayerInGui = false;
                inv.mainInventory.set(inv.currentItem, savedMain);
                inv.offHandInventory.set(0, savedOffhand);
                for (int i = 0; i < inv.armorInventory.size(); i++) {
                    inv.armorInventory.set(i, savedArmor[i]);
                }
            }
        } finally {
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    // ================== FLOWER MODEL & SKILL TREE RENDER ==================
    private void drawFlowerDisplay(int mouseX, int mouseY, float partialTicks, float deltaSec, float slideProgress) {
        if (!skillTreeInitialized) {
            skillUnlocked[0] = true;
            java.util.Arrays.fill(skillUnlockAnimStart, -1L);
            skillTreeInitialized = true;
        }

        // Smooth slide-in offset from bottom
        float slideOffsetY = (1.0f - easeOutCubic(slideProgress)) * 160f;

        float curX = (float) flowerPosX + flowerOffsetX;
        float curY = (float) flowerPosY + flowerOffsetY + slideOffsetY;
        float curZ = 20f + flowerOffsetZ; // Lower Z than player model
        float scaleNorm = flowerScreenScale / 300.0f;

        // Update flower animation, smoldering burn spots, and ember/ash particles with real screen coordinates
        flowerModel.update(deltaSec, flowerAnimSpeed, skillUnlocked, curX, curY - 20.0f * scaleNorm, scaleNorm);
        if (debugManualWave) {
            BedrockFlowerRenderer.Petal p = flowerModel.getPetal(debugPetalSelect);
            if (p != null) {
                p.burning = true;
                p.burnProgress = debugWaveProgress;
            }
        }

        // Disappear slightly earlier before completely sliding down to the bottom
        if (slideProgress <= 0.15f) {
            return;
        }

        // Smooth fade-out opacity when sliding down
        float fadeAlpha = clamp01((slideProgress - 0.15f) / 0.35f);

        GlStateManager.pushMatrix();
        try {
            GlStateManager.enableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, fadeAlpha);

            GlStateManager.translate(curX, curY, curZ);
            GlStateManager.scale(flowerScreenScale, -flowerScreenScale, flowerScreenScale); // -Y for Minecraft 3D GUI orientation

            // Apply tunable angles
            if (flowerRoll != 0f) GlStateManager.rotate(flowerRoll, 0, 0, 1);
            if (flowerYaw != 0f) GlStateManager.rotate(flowerYaw, 0, 1, 0);
            if (flowerPitch != 0f) GlStateManager.rotate(flowerPitch, 1, 0, 0);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();

            flowerModel.render(1.0f / 16.0f);

            RenderHelper.disableStandardItemLighting();

            // Render 3D sparks directly inside the flower model's 3D coordinate space!
            flowerModel.renderParticles(mc, fadeAlpha);
        } finally {
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void drawSkillsTab(int mouseX, int mouseY) {
        if (!skillTreeInitialized) {
            skillUnlocked[0] = true; // root starts already learned
            java.util.Arrays.fill(skillUnlockAnimStart, -1L);
            skillTreeInitialized = true;
        }

        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;
        int panelH = (int) VIRTUAL_H - 50;
        int panelY = 40;

        drawGradientRect(rightX, panelY, rightX + panelW, panelY + panelH, COLOR_PANEL_TOP, COLOR_PANEL_BOTTOM);
        drawBoxOutline(rightX, panelY, panelW, panelH, COLOR_LINE);

        int contentX = rightX + 12;
        int contentW = panelW - 24;

        // Total content height at zero scroll, used purely to clamp the scrollbar -
        // computed from fixed layout constants so it doesn't depend on scroll itself.
        int treeAreaH = SKILLS_PER_BRANCH * SKILL_ROW_GAP + SKILL_NODE_SIZE + 14;
        int totalContentH = 10 + 11 + 10 + treeAreaH;
        int visibleH = panelH - 10;
        this.maxSkillsScroll = Math.max(0, totalContentH - visibleH);

        int scrollOff = Math.round(skillsScrollAnim);
        int baseY = panelY + 10 - scrollOff;

        // --- Compact header: title + points on one line (was a whole separate
        // banner before; a single "5◆" reads just as clearly and costs far less
        // vertical space, which the tree badly needs). ---
        this.fontRenderer.drawString(TextFormatting.BOLD + "ДРЕВО НАВЫКОВ", contentX, baseY, COLOR_GOLD_BRIGHT);
        String pointsStr = skillPoints + " \u25C6";
        int pointsW = this.fontRenderer.getStringWidth(pointsStr);
        this.fontRenderer.drawString(pointsStr, contentX + contentW - pointsW, baseY,
                skillPoints > 0 ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);

        int sepY = baseY + 11;
        drawRect(contentX, sepY, contentX + contentW, sepY + 1, COLOR_LINE);

        int treeTop = sepY + 10;

        int colW = contentW / SKILL_BRANCHES;
        int rootX = contentX + contentW / 2;
        int rootY = treeTop + SKILL_NODE_SIZE / 2;

        hoveredSkillNodeForTooltip = null;

        // Connectors first, so node boxes draw cleanly on top of the lines.
        for (int b = 0; b < SKILL_BRANCHES; b++) {
            int colX = contentX + colW * b + colW / 2;
            int prevX = rootX;
            int prevY = rootY;
            boolean prevUnlocked = skillUnlocked[0];
            for (int d = 1; d <= SKILLS_PER_BRANCH; d++) {
                SkillNode node = findSkillNode(b, d);
                int nodeY = treeTop + d * SKILL_ROW_GAP + SKILL_NODE_SIZE / 2;
                boolean lit = prevUnlocked && skillUnlocked[node.id];
                drawSkillConnector(prevX, prevY, colX, nodeY, lit);
                prevX = colX;
                prevY = nodeY;
                prevUnlocked = skillUnlocked[node.id];
            }
        }

        // Root node
        boolean rootHover = isPointInSkillNode(mouseX, mouseY, rootX, rootY);
        drawSkillNode(SKILL_NODES[0], rootX, rootY, rootHover);
        if (rootHover) {
            hoveredSkillNodeForTooltip = SKILL_NODES[0];
            hoveredSkillTooltipX = rootX;
            hoveredSkillTooltipY = rootY;
        }

        // Branch nodes
        for (int b = 0; b < SKILL_BRANCHES; b++) {
            int colX = contentX + colW * b + colW / 2;
            for (int d = 1; d <= SKILLS_PER_BRANCH; d++) {
                SkillNode node = findSkillNode(b, d);
                int nodeY = treeTop + d * SKILL_ROW_GAP + SKILL_NODE_SIZE / 2;
                boolean hover = isPointInSkillNode(mouseX, mouseY, colX, nodeY);
                drawSkillNode(node, colX, nodeY, hover);
                if (hover) {
                    hoveredSkillNodeForTooltip = node;
                    hoveredSkillTooltipX = colX;
                    hoveredSkillTooltipY = nodeY;
                }
            }
        }
    }

    private SkillNode findSkillNode(int branch, int depth) {
        for (SkillNode n : SKILL_NODES) {
            if (n.branch == branch && n.depth == depth) {
                return n;
            }
        }
        return null;
    }

    private boolean isPointInSkillNode(int mouseX, int mouseY, int cx, int cy) {
        int half = SKILL_NODE_SIZE / 2;
        return mouseX >= cx - half && mouseX <= cx + half && mouseY >= cy - half && mouseY <= cy + half;
    }

    private boolean isSkillAvailable(SkillNode node) {
        if (node.depth == 0 || skillUnlocked[node.id]) {
            return false;
        }
        if (skillPoints <= 0) {
            return false;
        }
        int prereqId = (node.depth <= 1) ? 0 : findSkillNode(node.branch, node.depth - 1).id;
        return skillUnlocked[prereqId];
    }

    private void drawSkillConnector(int x1, int y1, int x2, int y2, boolean lit) {
        drawLine(x1, y1, x2, y2, lit ? COLOR_GOLD : COLOR_LINE);
    }

    /**
     * Node palette:
     * - locked (prerequisite not met): grey, dim icon
     * - available (prereq met, unspent point): white icon, RED outline; GOLD on hover
     * - unlocked: pale-gold fill, thin gold outline, white icon
     * Unlocking briefly plays the same chasing perimeter glow used on level-up cards.
     */
    private void drawSkillNode(SkillNode node, int cx, int cy, boolean isHovered) {
        int half = SKILL_NODE_SIZE / 2;
        int x = cx - half;
        int y = cy - half;

        boolean unlocked = skillUnlocked[node.id];
        boolean available = isSkillAvailable(node);

        int fillColor;
        int borderColor;
        int iconColor;

        if (unlocked) {
            fillColor = 0x2AFFF6DC;
            borderColor = isHovered ? COLOR_GOLD_BRIGHT : COLOR_GOLD;
            iconColor = COLOR_PAPER;
        } else if (available) {
            fillColor = isHovered ? 0x33E8A53D : 0x18FFFFFF;
            borderColor = isHovered ? COLOR_GOLD_BRIGHT : COLOR_BLOOD_BRIGHT;
            iconColor = isHovered ? COLOR_GOLD_BRIGHT : COLOR_PAPER;
        } else {
            fillColor = 0x00000000;
            borderColor = COLOR_LINE;
            iconColor = COLOR_PAPER_DIM;
        }

        drawRect(x, y, x + SKILL_NODE_SIZE, y + SKILL_NODE_SIZE, fillColor);
        drawBoxOutline(x, y, SKILL_NODE_SIZE, SKILL_NODE_SIZE, borderColor);

        long animStart = skillUnlockAnimStart[node.id];
        if (animStart > 0) {
            long elapsed = (System.nanoTime() - animStart) / 1_000_000L;
            if (elapsed < SKILL_UNLOCK_GLOW_MS) {
                drawPerimeterGlow(x, y, SKILL_NODE_SIZE, SKILL_NODE_SIZE, elapsed / (float) SKILL_UNLOCK_GLOW_MS);
            } else {
                skillUnlockAnimStart[node.id] = -1L;
            }
        }

        drawSkillIcon(node, cx, cy, iconColor);
    }

    /** Placeholder icon shapes until real per-skill icons exist: diamond = root, dot = normal, double-ring = capstone. */
    private void drawSkillIcon(SkillNode node, int cx, int cy, int color) {
        if (node.depth == 0) {
            drawRect(cx - 3, cy - 3, cx + 3, cy + 3, color);
        } else if (node.depth == SKILLS_PER_BRANCH) {
            drawBoxOutline(cx - 5, cy - 5, 10, 10, color);
            drawRect(cx - 1, cy - 1, cx + 1, cy + 1, color);
        } else {
            drawRect(cx - 2, cy - 2, cx + 2, cy + 2, color);
        }
    }

    private void drawSkillTooltip(SkillNode node, int anchorX, int anchorY) {
        int w = 168;
        int innerW = w - 12;
        java.util.List<String> lines = this.fontRenderer.listFormattedStringToWidth(node.description, innerW);
        int lineH = this.fontRenderer.FONT_HEIGHT + 2;
        int titleH = 11;
        int pad = 6;
        int h = pad * 2 + titleH + 4 + lines.size() * lineH;

        int x = anchorX + 16;
        int y = anchorY - h / 2;
        if (x + w > VIRTUAL_W - 4) {
            x = anchorX - w - 16;
        }
        x = Math.max(4, Math.min(x, (int) VIRTUAL_W - 4 - w));
        y = Math.max(4, Math.min(y, (int) VIRTUAL_H - 4 - h));

        drawRect(x, y, x + w, y + h, 0xF7050505);
        drawBoxOutline(x, y, w, h, skillUnlocked[node.id] ? COLOR_GOLD : COLOR_BLOOD);

        this.fontRenderer.drawString(TextFormatting.BOLD + node.name, x + pad, y + pad, COLOR_GOLD_BRIGHT);

        int ty = y + pad + titleH + 4;
        for (String line : lines) {
            this.fontRenderer.drawString(line, x + pad, ty, COLOR_PAPER);
            ty += lineH;
        }
    }

    private void tryUnlockSkill(SkillNode node) {
        if (!isSkillAvailable(node)) {
            return;
        }
        // TODO: replace with a real network round-trip (e.g. send
        // PacketUnlockSkill(sinType, node.id) and let the server validate points +
        // apply the actual gameplay effect via capability). Purely client-visual
        // for now so the tree UI can be built and tested on its own.
        skillUnlocked[node.id] = true;
        skillPoints = Math.max(0, skillPoints - 1);
        skillUnlockAnimStart[node.id] = System.nanoTime();
    }

    private void drawFlowerTuneOverlay() {
        int w = 205;
        int h = 210;
        int x = (int) VIRTUAL_W - w - 10;
        int y = 38;

        // Card background & outline
        drawGradientRect(x, y, x + w, y + h, 0xEE141210, 0xF50A0908);
        drawBoxOutline(x, y, w, h, COLOR_GOLD);

        int cx = x + 8;
        int cy = y + 8;

        this.fontRenderer.drawString(TextFormatting.BOLD + "✦ Flower Smolder Debug Maker", cx, cy, COLOR_GOLD_BRIGHT);
        cy += 12;
        this.fontRenderer.drawString(TextFormatting.GRAY + "TAB: выбор | ↑↓: изм.", cx, cy, 0xAAAAAA);
        cy += 10;
        this.fontRenderer.drawString(TextFormatting.GRAY + "P: Зажечь | R: Сброс | H: Скрыть", cx, cy, 0xAAAAAA);
        cy += 10;
        this.fontRenderer.drawString(TextFormatting.GRAY + "←→: шаг | ENTER: лог конфига", cx, cy, 0xAAAAAA);
        cy += 12;

        drawRect(cx, cy, cx + w - 16, cy + 1, COLOR_LINE);
        cy += 5;

        for (int i = 0; i < FLOWER_PARAM_NAMES.length; i++) {
            boolean sel = (i == flowerSelectedParam);
            String value = flowerFormatParam(i);
            String name = FLOWER_PARAM_NAMES[i];

            if (sel) {
                drawRect(cx - 3, cy - 1, cx + w - 13, cy + 9, 0x33FFD700);
            }

            String prefix = sel ? (TextFormatting.GOLD + "► ") : "  ";
            String text = prefix + TextFormatting.WHITE + name + ": " + (sel ? TextFormatting.YELLOW : TextFormatting.GRAY) + value;
            this.fontRenderer.drawString(text, cx, cy, sel ? COLOR_GOLD_BRIGHT : 0xCCCCCC);
            cy += 10;
        }

        cy += 3;
        this.fontRenderer.drawString(TextFormatting.ITALIC + "Шаг: x" + flowerStepMultiplier, cx, cy, COLOR_PAPER_DIM);
    }

    private String flowerFormatParam(int index) {
        switch (index) {
            case 0: {
                BedrockFlowerRenderer.Petal p = flowerModel.getPetal(debugPetalSelect);
                String name = (p != null) ? (p.isBig ? "Большой " : "Малый ") + debugPetalSelect : String.valueOf(debugPetalSelect);
                return name;
            }
            case 1: {
                int qLen = BedrockFlowerRenderer.PETAL_BURN_QUEUE.length;
                int petal = BedrockFlowerRenderer.PETAL_BURN_QUEUE[debugQueueSlotSelect % qLen];
                BedrockFlowerRenderer.Petal p = flowerModel.getPetal(petal);
                String name = (p != null) ? (p.isBig ? "Большой " : "Малый ") + petal : String.valueOf(petal);
                return (debugQueueSlotSelect + 1) + " -> Лепесток " + name;
            }
            case 2: return (BedrockFlowerRenderer.debugHighlightPetal == debugPetalSelect) ? TextFormatting.GREEN + "ВКЛ" : TextFormatting.RED + "ВЫКЛ";
            case 3: {
                BedrockFlowerRenderer.Petal p = flowerModel.getPetal(debugPetalSelect);
                return (p != null && p.flipDirection) ? "От кончика к центру" : "От центра к краю";
            }
            case 4: return String.format("%.2f", debugDistStart);
            case 5: return String.format("%.2f", debugDistEnd);
            case 6: return String.format("%.3f", BedrockFlowerRenderer.waveFireWidth);
            case 7: return String.format("%.3f", BedrockFlowerRenderer.waveAshWidth);
            case 8: return String.format("%.2f", BedrockFlowerRenderer.waveSpeed);
            case 9: return String.format("%.1f°", BedrockFlowerRenderer.waveAngle);
            case 10: return debugManualWave ? String.format("%.2f (Ручной)", debugWaveProgress) : "Авто (по таймеру)";
            case 11: return BedrockFlowerRenderer.showTexelDistDebug ? TextFormatting.GREEN + "ВКЛ (Зеленый->Красный)" : TextFormatting.RED + "ВЫКЛ";
            case 12: return String.valueOf(flowerPosX);
            case 13: return String.valueOf(flowerPosY);
            case 14: return String.format("%.1f", flowerScreenScale);
            case 15: return String.format("%.1f", flowerYaw);
            case 16: return String.format("%.1f", flowerPitch);
            case 17: return String.format("%.1f", flowerRoll);
            default: return "?";
        }
    }

    private static float flowerBaseStep(int index) {
        switch (index) {
            case 0: return 1f;    // Petal ID
            case 1: return 1f;    // Queue slot (skill 1..12)
            case 2: return 1f;    // Highlight toggle
            case 3: return 1f;    // Direction toggle
            case 4: return 0.05f; // distStart
            case 5: return 0.05f; // distEnd
            case 6: return 0.005f;// waveFireWidth
            case 7: return 0.01f; // waveAshWidth
            case 8: return 0.05f; // waveSpeed
            case 9: return 5f;    // waveAngle (degrees)
            case 10: return 0.05f; // debugWaveProgress
            case 11: return 1f;   // showTexelDistDebug toggle
            case 12: return 1f;   // posX
            case 13: return 1f;   // posY
            case 14: return 2f;   // screenScale
            case 15: return 5f;   // yaw
            case 16: return 5f;   // pitch
            case 17: return 5f;   // roll
            default: return 1f;
        }
    }

    private void flowerAdjustParam(int index, float delta) {
        switch (index) {
            case 0: {
                int count = flowerModel.getPetalCount();
                if (count > 0) {
                    debugPetalSelect = (int) ((debugPetalSelect + (int) Math.signum(delta) + count) % count);
                    BedrockFlowerRenderer.debugHighlightPetal = debugPetalSelect;
                }
                break;
            }
            case 1: {
                int qLen = BedrockFlowerRenderer.PETAL_BURN_QUEUE.length;
                if (qLen > 0) {
                    debugQueueSlotSelect = (int) ((debugQueueSlotSelect + (int) Math.signum(delta) + qLen) % qLen);
                    int targetPetal = BedrockFlowerRenderer.PETAL_BURN_QUEUE[debugQueueSlotSelect];
                    debugPetalSelect = targetPetal;
                    BedrockFlowerRenderer.debugHighlightPetal = targetPetal;
                }
                break;
            }
            case 2: {
                if (BedrockFlowerRenderer.debugHighlightPetal == debugPetalSelect) {
                    BedrockFlowerRenderer.debugHighlightPetal = -1;
                } else {
                    BedrockFlowerRenderer.debugHighlightPetal = debugPetalSelect;
                }
                break;
            }
            case 3: {
                BedrockFlowerRenderer.Petal p = flowerModel.getPetal(debugPetalSelect);
                if (p != null) p.flipDirection = !p.flipDirection;
                break;
            }
            case 4: debugDistStart = Math.max(0.0f, Math.min(1.0f, debugDistStart + delta)); break;
            case 5: debugDistEnd = Math.max(0.0f, Math.min(1.0f, debugDistEnd + delta)); break;
            case 6: BedrockFlowerRenderer.waveFireWidth = Math.max(0.01f, Math.min(0.25f, BedrockFlowerRenderer.waveFireWidth + delta)); break;
            case 7: BedrockFlowerRenderer.waveAshWidth = Math.max(0.02f, Math.min(0.40f, BedrockFlowerRenderer.waveAshWidth + delta)); break;
            case 8: BedrockFlowerRenderer.waveSpeed = Math.max(0.05f, Math.min(3.0f, BedrockFlowerRenderer.waveSpeed + delta)); break;
            case 9: BedrockFlowerRenderer.waveAngle = Math.max(-75.0f, Math.min(75.0f, BedrockFlowerRenderer.waveAngle + delta)); break;
            case 10: {
                debugManualWave = true;
                debugWaveProgress = Math.max(0.0f, Math.min(1.0f, debugWaveProgress + delta));
                BedrockFlowerRenderer.Petal p = flowerModel.getPetal(debugPetalSelect);
                if (p != null) {
                    p.burning = true;
                    p.burnProgress = debugWaveProgress;
                }
                break;
            }
            case 11: BedrockFlowerRenderer.showTexelDistDebug = !BedrockFlowerRenderer.showTexelDistDebug; break;
            case 12: flowerPosX += (int) delta; break;
            case 13: flowerPosY += (int) delta; break;
            case 14: flowerScreenScale = Math.max(1f, flowerScreenScale + delta); break;
            case 15: flowerYaw += delta; break;
            case 16: flowerPitch += delta; break;
            case 17: flowerRoll += delta; break;
        }
    }

    private void flowerPrintValuesToLog() {
        StringBuilder queueSb = new StringBuilder("[");
        for (int i = 0; i < BedrockFlowerRenderer.PETAL_BURN_QUEUE.length; i++) {
            if (i > 0) queueSb.append(", ");
            queueSb.append(BedrockFlowerRenderer.PETAL_BURN_QUEUE[i]);
        }
        queueSb.append("]");

        String msg = "FLOWER SMOLDER CONFIG VALUES:\n"
                + "  PETAL_BURN_QUEUE = " + queueSb.toString() + ";\n"
                + "  Selected Petal = " + debugPetalSelect + ";\n"
                + "  Selected Queue Slot = " + (debugQueueSlotSelect + 1) + ";\n"
                + "  distStart = " + debugDistStart + "f;\n"
                + "  distEnd = " + debugDistEnd + "f;\n"
                + "  waveFireWidth = " + BedrockFlowerRenderer.waveFireWidth + "f;\n"
                + "  waveAshWidth = " + BedrockFlowerRenderer.waveAshWidth + "f;\n"
                + "  waveSpeed = " + BedrockFlowerRenderer.waveSpeed + "f;\n"
                + "  flowerPosX = " + flowerPosX + ";\n"
                + "  flowerPosY = " + flowerPosY + ";\n"
                + "  flowerScreenScale = " + flowerScreenScale + "f;\n"
                + "  flowerYaw = " + flowerYaw + "f;\n"
                + "  flowerPitch = " + flowerPitch + "f;\n"
                + "  flowerRoll = " + flowerRoll + "f;";
        System.out.println(msg);
        if (mc != null && mc.player != null) {
            mc.player.sendMessage(new net.minecraft.util.text.TextComponentString(TextFormatting.GOLD + "[Flower Debug] Настройки выведены в консоль"));
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
            tooltipX = dporIconX - 194;
            tooltipY = dporIconY - 6;
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

        // Text column starts right of the icon slot and stops with a small right
        // margin, so wrapped lines never run into (or past) the card's edge.
        int textX = cx + 42;
        int maxTextWidth = LVLUP_CARD_W - 42 - 10;

        int y = cy + 40;
        if (card.getBuff() != null) {
            int blockH = drawEffectBlock(cx + 14, y, textX, maxTextWidth, COLOR_GOLD_BRIGHT, "▲",
                    card.getBuff().getNameRu(), card.getBuff().getFormattedValue());
            y += blockH + 10; // gap before the next block, so wrapped buff text never touches the debuff block
        }

        if (card.getDebuff() != null) {
            drawEffectBlock(cx + 14, y, textX, maxTextWidth, COLOR_BLOOD_BRIGHT, "▼",
                    card.getDebuff().getNameRu(), card.getDebuff().getFormattedValue());
        }

        String prompt = isHovered ? TextFormatting.YELLOW + "▶ ВЫБРАТЬ ◀" : "нажмите, чтобы принять";
        int pW = this.fontRenderer.getStringWidth(prompt);
        this.fontRenderer.drawString(prompt, cx + (LVLUP_CARD_W - pW) / 2, cy + LVLUP_CARD_H - 20,
                isHovered ? COLOR_GOLD_BRIGHT : COLOR_PAPER_DIM);
    }

    /**
     * Draws one buff/debuff row: a dashed icon slot at (iconX,blockY) plus the
     * arrow/name/value word-wrapped to maxTextWidth starting at textX. Returns the
     * total height the block ended up using (icon size or wrapped text, whichever
     * is taller) so the caller can stack the next block below it without overlap.
     */
    private int drawEffectBlock(int iconX, int blockY, int textX, int maxTextWidth, int color, String arrow,
            String name, String value) {
        int iconSize = 22;
        drawDashedOutline(iconX, blockY, iconSize, iconSize, color);
        this.fontRenderer.drawString(arrow, iconX + 7, blockY + 7, color);

        int lineHeight = this.fontRenderer.FONT_HEIGHT + 1;
        java.util.List<String> nameLines = this.fontRenderer.listFormattedStringToWidth(name, maxTextWidth);
        java.util.List<String> valueLines = this.fontRenderer.listFormattedStringToWidth(value, maxTextWidth);

        int ty = blockY + 2;
        for (String line : nameLines) {
            this.fontRenderer.drawString(line, textX, ty, color);
            ty += lineHeight;
        }
        for (String line : valueLines) {
            this.fontRenderer.drawString(line, textX, ty, color);
            ty += lineHeight;
        }

        int textBlockHeight = (nameLines.size() + valueLines.size()) * lineHeight;
        return Math.max(iconSize, textBlockHeight);
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
        if (classSelectPhase != CLASS_PHASE_LISTING) {
            return; // sins are only clickable while the grid is up (not mid-animation)
        }

        SinType[] sins = SinType.values();
        for (int i = 0; i < sins.length; i++) {
            int cx = sinCardBaseX(i);
            int cy = sinCardBaseY(i);

            // IMPORTANT: use the card's *current rendered* Y (base minus the animated
            // hover-lift), not the base grid Y. Previously this hitbox used the base Y
            // while the card visually renders up to 4px higher when hovered, so the top
            // of the (visually lifted) card fell outside the click box -- clicks only
            // reliably landed near the icon in the card's vertical center, which is why
            // it felt like you had to click exactly on the icon / click several times.
            int drawY = Math.round(cy - sinCardHoverLift[i]);

            int infoX = cx + 5;
            int infoY = drawY + 5;
            if (mouseX >= infoX && mouseX <= infoX + 11 && mouseY >= infoY && mouseY <= infoY + 11) {
                return; // clicking the info icon shows the tooltip, doesn't select
            }

            if (mouseX >= cx && mouseX <= cx + SIN_CARD_W && mouseY >= drawY && mouseY <= drawY + SIN_CARD_H) {
                SinType selectedSin = sins[i];
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketSelectSin(selectedSin));

                // Don't cut to the main HUD instantly -- play the same
                // center+grow+glow-then-slide-down flow used for accepting a
                // level-up card, then slide the HUD in from the sides.
                this.selectedSinIndex = i;
                this.classSelectPhase = CLASS_PHASE_SELECT;
                this.classSelectPhaseStartTime = System.nanoTime();
                return;
            }
        }
    }


    private void handleMainHudClick(int mouseX, int mouseY) {
        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;

        int tabGap = 3;
        int tabW = (panelW - tabGap * 3) / 4;
        int tabH = 18;
        int tabY = 16;

        int tab1X = rightX;
        int tab2X = rightX + (tabW + tabGap);
        int tab3X = rightX + (tabW + tabGap) * 2;
        int tab4X = rightX + (tabW + tabGap) * 3;

        if (mouseX >= tab1X && mouseX <= tab1X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            setTab(TAB_GRIEVANCE);
            return;
        }
        if (mouseX >= tab2X && mouseX <= tab2X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            setTab(TAB_SKILLS);
            return;
        }
        if (mouseX >= tab3X && mouseX <= tab3X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            setTab(TAB_MANUALS);
            return;
        }
        if (mouseX >= tab4X && mouseX <= tab4X + tabW && mouseY >= tabY && mouseY <= tabY + tabH) {
            setTab(TAB_APPEARANCE);
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
        } else if (currentTab == TAB_SKILLS) {
            handleSkillsClick(mouseX, mouseY);
        }
    }

    private void handleSkillsClick(int mouseX, int mouseY) {
        int panelW = 236;
        int rightX = (int) VIRTUAL_W - panelW - 10;
        int panelY = 40;
        int contentX = rightX + 12;
        int contentW = panelW - 24;

        int scrollOff = Math.round(skillsScrollAnim);
        int baseY = panelY + 10 - scrollOff;
        int sepY = baseY + 11;
        int treeTop = sepY + 10;

        int colW = contentW / SKILL_BRANCHES;
        int rootX = contentX + contentW / 2;
        int rootY = treeTop + SKILL_NODE_SIZE / 2;

        if (isPointInSkillNode(mouseX, mouseY, rootX, rootY)) {
            tryUnlockSkill(SKILL_NODES[0]);
            return;
        }

        for (int b = 0; b < SKILL_BRANCHES; b++) {
            int colX = contentX + colW * b + colW / 2;
            for (int d = 1; d <= SKILLS_PER_BRANCH; d++) {
                SkillNode node = findSkillNode(b, d);
                int nodeY = treeTop + d * SKILL_ROW_GAP + SKILL_NODE_SIZE / 2;
                if (isPointInSkillNode(mouseX, mouseY, colX, nodeY)) {
                    tryUnlockSkill(node);
                    return;
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