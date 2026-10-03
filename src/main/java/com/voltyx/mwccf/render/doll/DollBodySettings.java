package com.voltyx.mwccf.render.doll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.voltyx.mwccf.backpack.BackpackBaubles;
import com.voltyx.mwccf.mcore.ItemCustomArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Настройки позиционирования куклы Сайи на теле игрока (3-е лицо / пояс)
 * для различных типов брони и рюкзака.
 */
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class DollBodySettings {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile = null;

    public static boolean debugHudEnabled = false;
    public static int selectedPropertyIndex = 0;
    public static int selectedCategoryIndex = 0; // 0 = AUTO, 1..N = конкретная категория

    public static final List<String> CATEGORIES = Arrays.asList(
        "AUTO",
        "NONE",
        "BACKPACK",
        "ROCKIE",
        "RECLUIT",
        "JUGGERNAUT",
        "EXO",
        "EXO_HEAVY",
        "HAZMAT",
        "MILITARY",
        "POLICE",
        "FIRE_FIGHTER",
        "GUILLIE",
        "PLATE_CARRIER",
        "SWAT",
        "RIOT",
        "STEEL",
        "TITANIUM"
    );

    public static class OffsetData {
        public float posX = 0.0f;
        public float posY = 0.0f;
        public float posZ = 0.0f;
        public float rotX = 0.0f;
        public float rotY = 0.0f;
        public float rotZ = 0.0f;
        public float scale = 1.0f;

        public OffsetData() {}

        public OffsetData(float x, float y, float z, float rx, float ry, float rz, float s) {
            this.posX = x; this.posY = y; this.posZ = z;
            this.rotX = rx; this.rotY = ry; this.rotZ = rz;
            this.scale = s;
        }

        public OffsetData copy() {
            return new OffsetData(posX, posY, posZ, rotX, rotY, rotZ, scale);
        }
    }

    public enum Property {
        POS_X("Смещение X (влево/вправо)", 0.1f, 0.5f, 0.02f),
        POS_Y("Смещение Y (вверх/вниз)", 0.1f, 0.5f, 0.02f),
        POS_Z("Смещение Z (вперед/назад)", 0.1f, 0.5f, 0.02f),
        ROT_X("Вращение X (наклон)", 1.0f, 5.0f, 0.2f),
        ROT_Y("Вращение Y (поворот)", 2.0f, 10.0f, 0.5f),
        ROT_Z("Вращение Z (крен)", 1.0f, 5.0f, 0.2f),
        SCALE("Масштаб (Scale)", 0.02f, 0.1f, 0.005f);

        public final String name;
        public final float stepNormal;
        public final float stepFast;
        public final float stepFine;

        Property(String name, float norm, float fast, float fine) {
            this.name = name;
            this.stepNormal = norm;
            this.stepFast = fast;
            this.stepFine = fine;
        }
    }

    public static final Map<String, OffsetData> OFFSETS = new LinkedHashMap<>();

    static {
        initDefaults();
        load();
    }

    private static void initDefaults() {
        for (String cat : CATEGORIES) {
            if (!cat.equals("AUTO")) {
                OFFSETS.put(cat, new OffsetData(0f, 0f, 0f, 0f, 0f, 0f, 1.0f));
            }
        }
    }

    private static File getConfigFile() {
        if (configFile == null) {
            Minecraft mc = Minecraft.getMinecraft();
            File base = (mc != null && mc.gameDir != null) ? mc.gameDir : new File(".");
            File dir = new File(base, "config/mwccf");
            if (!dir.exists()) dir.mkdirs();
            configFile = new File(dir, "doll_body_offsets.json");
        }
        return configFile;
    }

    public static void load() {
        File file = getConfigFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, OffsetData>>(){}.getType();
                Map<String, OffsetData> loaded = GSON.fromJson(reader, type);
                if (loaded != null) {
                    for (Map.Entry<String, OffsetData> entry : loaded.entrySet()) {
                        OFFSETS.put(entry.getKey().toUpperCase(Locale.US), entry.getValue());
                    }
                }
            } catch (Exception e) {
                System.err.println("[DollBodySettings] Failed to load offsets: " + e.getMessage());
            }
        }
    }

    public static void save() {
        File file = getConfigFile();
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(OFFSETS, writer);
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                mc.player.sendStatusMessage(new TextComponentString(TextFormatting.GREEN + "[Doll Body] Настройки сохранены в doll_body_offsets.json"), true);
            }
        } catch (Exception e) {
            System.err.println("[DollBodySettings] Failed to save offsets: " + e.getMessage());
        }
    }

    public static boolean isBackpackRendered(EntityPlayer player) {
        if (player == null) return false;

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (!chest.isEmpty() && chest.getItem().getRegistryName() != null && chest.getItem().getRegistryName().toString().contains("backpack")) {
            return true;
        }

        ItemStack baubleBackpack = BackpackBaubles.getBaubleBackpack(player);
        if (!baubleBackpack.isEmpty()) {
            if (chest.isEmpty()) {
                return true;
            }
            if (chest.getItem() instanceof ItemCustomArmor) {
                return "mwccf:fire_fighter_chestplate".equals(chest.getItem().getRegistryName().toString());
            }
            return true;
        }

        return false;
    }

    public static String detectCategory(EntityPlayer player) {
        if (player == null) return "NONE";

        // Если рюкзак фактически рендерится на игроке — преимущество у его конфига
        if (isBackpackRendered(player)) {
            return "BACKPACK";
        }

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chest.isEmpty()) {
            return "NONE";
        }

        String name = "";
        if (chest.getItem().getRegistryName() != null) {
            name = chest.getItem().getRegistryName().toString().toLowerCase(Locale.US);
        }

        if (name.contains("exo_heavy")) return "EXO_HEAVY";
        if (name.contains("exo")) return "EXO";
        if (name.contains("juggernaut")) return "JUGGERNAUT";
        if (name.contains("rockie")) return "ROCKIE";
        if (name.contains("recluit")) return "RECLUIT";
        if (name.contains("hazmat")) return "HAZMAT";
        if (name.contains("military")) return "MILITARY";
        if (name.contains("police")) return "POLICE";
        if (name.contains("fire_fighter")) return "FIRE_FIGHTER";
        if (name.contains("ghillie") || name.contains("guillie")) return "GUILLIE";
        if (name.contains("plate_carrier")) return "PLATE_CARRIER";
        if (name.contains("swat")) return "SWAT";
        if (name.contains("riot")) return "RIOT";
        if (name.contains("steel_chestplate")) return "STEEL";
        if (name.contains("titanium_chestplate")) return "TITANIUM";

        return "NONE";
    }

    public static String getActiveCategoryName(EntityPlayer player) {
        if (player != Minecraft.getMinecraft().player) {
            return detectCategory(player);
        }
        if (selectedCategoryIndex == 0) { // AUTO
            return detectCategory(player);
        }
        if (selectedCategoryIndex >= 0 && selectedCategoryIndex < CATEGORIES.size()) {
            return CATEGORIES.get(selectedCategoryIndex);
        }
        return "NONE";
    }

    public static OffsetData getOffsetForPlayer(EntityPlayer player) {
        String cat = getActiveCategoryName(player);
        OffsetData data = OFFSETS.get(cat);
        if (data == null) {
            data = new OffsetData();
            OFFSETS.put(cat, data);
        }
        return data;
    }

    public static float getValue(OffsetData data, Property prop) {
        if (data == null) return 0f;
        switch (prop) {
            case POS_X: return data.posX;
            case POS_Y: return data.posY;
            case POS_Z: return data.posZ;
            case ROT_X: return data.rotX;
            case ROT_Y: return data.rotY;
            case ROT_Z: return data.rotZ;
            case SCALE: return data.scale;
            default: return 0f;
        }
    }

    public static void setValue(OffsetData data, Property prop, float val) {
        if (data == null) return;
        switch (prop) {
            case POS_X: data.posX = val; break;
            case POS_Y: data.posY = val; break;
            case POS_Z: data.posZ = val; break;
            case ROT_X: data.rotX = val; break;
            case ROT_Y: data.rotY = val; break;
            case ROT_Z: data.rotZ = val; break;
            case SCALE: data.scale = Math.max(0.01f, val); break;
        }
    }

    public static void adjust(OffsetData data, Property prop, int direction, boolean shift, boolean ctrl) {
        float step = prop.stepNormal;
        if (shift) step = prop.stepFast;
        if (ctrl) step = prop.stepFine;

        float cur = getValue(data, prop);
        setValue(data, prop, cur + step * direction);
    }

    public static void dumpAndCopy(Minecraft mc, EntityPlayer player) {
        String cat = getActiveCategoryName(player);
        OffsetData d = getOffsetForPlayer(player);

        String dump = String.format(Locale.US,
            "\"%s\": {\n  \"posX\": %.4f,\n  \"posY\": %.4f,\n  \"posZ\": %.4f,\n  \"rotX\": %.2f,\n  \"rotY\": %.2f,\n  \"rotZ\": %.2f,\n  \"scale\": %.4f\n}",
            cat, d.posX, d.posY, d.posZ, d.rotX, d.rotY, d.rotZ, d.scale);

        try {
            GuiScreen.setClipboardString(dump);
        } catch (Throwable ignored) {}

        if (mc.player != null) {
            mc.player.sendMessage(new TextComponentString(TextFormatting.GOLD + "=== НАСТРОЙКИ КУКЛЫ НА ТЕЛЕ [" + cat + "] (СКОПИРОВАНО) ==="));
            mc.player.sendMessage(new TextComponentString(TextFormatting.AQUA + dump));
            mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Скопировано в буфер обмена! Нажмите Ctrl+V."));
        }
    }

    // =========================================================
    // Горячие клавиши отладки тела
    // =========================================================

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;
        if (!Keyboard.getEventKeyState()) return;

        int key = Keyboard.getEventKey();

        // F7 - переключение HUD отладки куклы на теле
        if (key == Keyboard.KEY_F7) {
            toggleHud(mc);
            return;
        }

        if (!debugHudEnabled || !efw.biomeinfo.MwccfConfig.doll.enableDebugTweaker) return;

        boolean isShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean isCtrlOrAlt = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)
                           || Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);

        Property[] props = Property.values();
        OffsetData currentData = getOffsetForPlayer(mc.player);

        // Стрелки Вверх/Вниз - выбор параметра
        if (key == Keyboard.KEY_UP) {
            selectedPropertyIndex = (selectedPropertyIndex - 1 + props.length) % props.length;
            return;
        }
        if (key == Keyboard.KEY_DOWN) {
            selectedPropertyIndex = (selectedPropertyIndex + 1) % props.length;
            return;
        }

        // Стрелки Влево/Вправо - изменение значения
        if (key == Keyboard.KEY_LEFT) {
            adjust(currentData, props[selectedPropertyIndex], -1, isShift, isCtrlOrAlt);
            return;
        }
        if (key == Keyboard.KEY_RIGHT) {
            adjust(currentData, props[selectedPropertyIndex], 1, isShift, isCtrlOrAlt);
            return;
        }

        // PageUp / PageDown или [ / ] - переключение категории
        if (key == Keyboard.KEY_PRIOR || key == Keyboard.KEY_LBRACKET) {
            selectedCategoryIndex = (selectedCategoryIndex - 1 + CATEGORIES.size()) % CATEGORIES.size();
            String catName = CATEGORIES.get(selectedCategoryIndex);
            mc.player.sendStatusMessage(new TextComponentString(TextFormatting.YELLOW + "[Doll Body] Режим: " + catName), true);
            return;
        }
        if (key == Keyboard.KEY_NEXT || key == Keyboard.KEY_RBRACKET) {
            selectedCategoryIndex = (selectedCategoryIndex + 1) % CATEGORIES.size();
            String catName = CATEGORIES.get(selectedCategoryIndex);
            mc.player.sendStatusMessage(new TextComponentString(TextFormatting.YELLOW + "[Doll Body] Режим: " + catName), true);
            return;
        }

        // S - сохранить в файл
        if (key == Keyboard.KEY_S) {
            save();
            return;
        }

        // P - скопировать в буфер
        if (key == Keyboard.KEY_P) {
            dumpAndCopy(mc, mc.player);
            return;
        }

        // R - сброс текущей категории
        if (key == Keyboard.KEY_R) {
            String activeCat = getActiveCategoryName(mc.player);
            OFFSETS.put(activeCat, new OffsetData(0f, 0f, 0f, 0f, 0f, 0f, 1.0f));
            mc.player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "[Doll Body] Настройки для " + activeCat + " сброшены!"), true);
            return;
        }
    }

    public static void toggleHud(Minecraft mc) {
        if (!efw.biomeinfo.MwccfConfig.doll.enableDebugTweaker) {
            debugHudEnabled = false;
            if (mc.player != null) {
                mc.player.sendStatusMessage(new TextComponentString(
                    TextFormatting.RED + "[Doll Body] Дебаг-настройка отключена в конфиге! (Mod Options -> MWCCF -> Config -> doll_settings)"), true);
            }
            return;
        }

        debugHudEnabled = !debugHudEnabled;
        if (debugHudEnabled) {
            // Переключаем на 3-е лицо, если игрок от 1-го лица, чтобы видеть куклу
            if (mc.gameSettings.thirdPersonView == 0) {
                mc.gameSettings.thirdPersonView = 1;
            }
            mc.player.sendStatusMessage(new TextComponentString(
                TextFormatting.GOLD + "[Doll Body] " + TextFormatting.GREEN + "HUD настройки куклы на броне ВКЛЮЧЕН (F7 для выключения)"), true);
        } else {
            mc.player.sendStatusMessage(new TextComponentString(
                TextFormatting.GOLD + "[Doll Body] " + TextFormatting.RED + "HUD настройки куклы на броне ОТКЛЮЧЕН"), true);
        }
    }

    // =========================================================
    // Отрисовка HUD отладки на экране
    // =========================================================

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!debugHudEnabled || !efw.biomeinfo.MwccfConfig.doll.enableDebugTweaker) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;
        if (mc.gameSettings.showDebugInfo) return;

        renderDebugHud(mc);
    }

    private static void renderDebugHud(Minecraft mc) {
        int x = 6;
        int y = 6;
        int w = 295;
        Property[] props = Property.values();
        int h = 18 + props.length * 10 + 58;

        Gui.drawRect(x - 2, y - 2, x + w + 2, y + h + 2, 0xDD111111);
        Gui.drawRect(x - 2, y - 2, x + w + 2, y - 1, 0xFF555555);
        Gui.drawRect(x - 2, y + h + 1, x + w + 2, y + h + 2, 0xFF555555);
        Gui.drawRect(x - 2, y - 2, x - 1, y + h + 2, 0xFF555555);
        Gui.drawRect(x + w + 1, y - 2, x + w + 2, y + h + 2, 0xFF555555);

        int curY = y + 2;
        mc.fontRenderer.drawStringWithShadow("§6§l=== НАСТРОЙКА КУКЛЫ НА БРОНЕ (F7) ===", x + 4, curY, 0xFFFFFF);
        curY += 12;

        String autoDetected = detectCategory(mc.player);
        String activeCat = getActiveCategoryName(mc.player);
        boolean isAuto = (selectedCategoryIndex == 0);

        String catModeStr = isAuto ? "§a[AUTO -> " + activeCat + "]" : "§e[" + activeCat + " (Ручной)]";
        mc.fontRenderer.drawStringWithShadow("§7Режим: " + catModeStr, x + 4, curY, 0xFFFFFF);
        curY += 10;

        ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        String chestName = chest.isEmpty() ? "§8(Нет)" : "§b" + (chest.getItem().getRegistryName() != null ? chest.getItem().getRegistryName().getPath() : chest.getDisplayName());
        ItemStack backpack = BackpackBaubles.getBaubleBackpack(mc.player);
        boolean bpRendered = isBackpackRendered(mc.player);
        String bpStr = backpack.isEmpty() ? "§8(Рюкзак: нет)" : (bpRendered ? "§6Рюкзак: виден" : "§7Рюкзак: скрыт");

        mc.fontRenderer.drawStringWithShadow("§7Броня: " + chestName + " §7| " + bpStr, x + 4, curY, 0xCCCCCC);
        curY += 12;

        OffsetData data = getOffsetForPlayer(mc.player);

        for (int i = 0; i < props.length; i++) {
            Property p = props[i];
            boolean sel = (i == selectedPropertyIndex);
            float val = getValue(data, p);

            String valStr;
            if (p == Property.ROT_X || p == Property.ROT_Y || p == Property.ROT_Z) {
                valStr = String.format(Locale.US, "%.1f°", val);
            } else {
                valStr = String.format(Locale.US, "%.3f", val);
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
        mc.fontRenderer.drawStringWithShadow("§8---------------------------------------------", x + 4, curY, 0x888888);
        curY += 9;
        mc.fontRenderer.drawStringWithShadow("§b[↑/↓]: выбор | [←/→]: значение | [PgUp/PgDn]: категория", x + 4, curY, 0x55FFFF);
        curY += 9;
        mc.fontRenderer.drawStringWithShadow("§b[Shift]: x5 быстрее | [Ctrl]: x0.1 точнее", x + 4, curY, 0x55FFFF);
        curY += 9;
        mc.fontRenderer.drawStringWithShadow("§a§l[S]: СОХРАНИТЬ §7| §e§l[P]: КОПИРОВАТЬ §7| §c[R]: СБРОС", x + 4, curY, 0x55FF55);
    }
}
