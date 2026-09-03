package com.voltyx.mwccf.client.inspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import efw.animation.WeaponTypeHelper;
import net.minecraft.client.Minecraft;
import com.voltyx.mwccf.geo.ItemGeoArmor;
import com.voltyx.mwccf.mcore.ItemCustomArmor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.FMLLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemInspectConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    public static class TextSettings {
        public float titleScale = 1.25f;
        public int titleOffsetX = 0;
        public int titleOffsetY = 0;
        public float descScale = 1.0f;
        public int textOffsetY = -145; // Raised higher as requested
        public int dividerWidth = 260;
        public int dividerGap = 16;
        public int descGap = 8;
        public int maxTextWidth = 380;
    }

    public static boolean isBlacklisted(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return false;
        Item item = stack.getItem();
        if (item == null)
            return false;

        // Allow ammo packs (packs of ammo)
        if (item instanceof com.paneedah.mwc.items.equipment.ItemAmmoPack) {
            return false;
        }

        // Blacklist MWC attachments, weapon parts, magazines, and single bullets
        // 1. Single bullets
        if (item instanceof com.paneedah.weaponlib.ItemBullet) {
            return true;
        }
        // 2. Magazines
        if (item instanceof com.paneedah.weaponlib.ItemMagazine) {
            return true;
        }
        // 3. Attachments and weapon parts (ItemAttachment, Part, ItemAttachment
        // subclasses like scopes, grips, silencers, skins, etc.)
        if (item instanceof com.paneedah.weaponlib.ItemAttachment || item instanceof com.paneedah.weaponlib.Part) {
            return true;
        }

        String regName = item.getRegistryName() != null ? item.getRegistryName().toString().toLowerCase() : "";
        String cls = item.getClass().getName().toLowerCase();

        // Safety fallback checks for MWC attachments, magazines, bullets, parts
        if (regName.startsWith("mwc:") || cls.contains("weaponlib") || cls.contains("paneedah")) {
            if (item instanceof com.paneedah.weaponlib.Weapon || item instanceof com.paneedah.weaponlib.ItemVest
                    || item instanceof com.paneedah.mwc.items.equipment.carryable.ItemCarryable) {
                return false;
            }
            if (cls.contains("attachment") || cls.contains("magazine") || cls.contains("bullet")
                    || cls.contains("ammo") || cls.contains("scope") || cls.contains("part") || cls.contains("skin")) {
                return true;
            }
        }

        return false;
    }

    public static TextSettings textSettings = new TextSettings();

    public static class GroupTransform {
        public float scale = 1.0f;
        public float pivotX = 0.0f;
        public float pivotY = 0.0f;
        public float pivotZ = 0.0f;
        public float startYaw = 25.0f;
        public float startPitch = 15.0f;
        public float startRoll = 0.0f;

        public GroupTransform() {
        }

        public GroupTransform(float scale, float px, float py, float pz, float yaw, float pitch, float roll) {
            this.scale = scale;
            this.pivotX = px;
            this.pivotY = py;
            this.pivotZ = pz;
            this.startYaw = yaw;
            this.startPitch = pitch;
            this.startRoll = roll;
        }

        public GroupTransform copy() {
            return new GroupTransform(scale, pivotX, pivotY, pivotZ, startYaw, startPitch, startRoll);
        }
    }

    public enum InspectGroup {
        PISTOLS("Пистолеты", "pistols"),
        SMG("ПП (SMG)", "smg"),
        WEAPONS_OTHER("Остальные оружия MWC", "weapons_other"),
        HELMET_MWCCF("Шлемы / Головные уборы MWCCF (Geo)", "helmet_mwccf"),
        ARMOR_GEO_MWCCF("3D Geo Броня MWCCF (Не MCore)", "armor_geo_mwccf"),
        HELMET_MCORE("Шлемы / Маски Survival Instinct (MCore)", "helmet_mcore"),
        ARMOR_MCORE("3D MCore Броня (Survival Instinct)", "armor_mcore"),
        HELMET_VANILLA("Шлемы Ванильные / Моды", "helmet_vanilla"),
        ARMOR_VANILLA("Броня Ванильная (Тело/Ноги)", "armor_vanilla"),
        HEADLAMP("Фонарик (Headlamp)", "headlamp"),
        BRACELET("Браслет (Volttech)", "bracelet"),
        NOTE("Записка (Note)", "note"),
        DIARY("Дневник (Diary)", "diary"),
        DPOR("Осквернённая страница (DPOR)", "dpor"),
        BAUBLES("Рюкзак / Прочее Baubles", "baubles"),
        TOOLS_2D("2D Tools (Оружие/Инструменты)", "tools_2d"),
        ITEMS_2D("2D Предметы / Блоки", "items_2d"),
        TEXT_SETTINGS("Текст и разделитель", "text_settings");

        public final String displayName;
        public final String key;

        InspectGroup(String displayName, String key) {
            this.displayName = displayName;
            this.key = key;
        }
    }

    private static final Set<String> SMG_KEYWORDS = new HashSet<>(Arrays.asList(
            "smg", "mp5", "mp5a5", "mp5k", "mp5sd", "uzi", "micro_uzi", "vector", "kriss_vector",
            "p90", "mac10", "bizon", "pp19", "mp7", "pp2000", "scorpion", "evo3", "cz_scorpion",
            "ump45", "ump9", "mp9", "apc9", "vityaz"));

    public static float globalCustomizationWeaponScale = 1.0f;
    public static float globalInspectScale = 1.0f;

    public static float getGlobalCustomizationWeaponScale() {
        if (efw.biomeinfo.MwccfConfig.itemInspect != null
                && efw.biomeinfo.MwccfConfig.itemInspect.globalCustomizationWeaponScale > 0.0) {
            return (float) efw.biomeinfo.MwccfConfig.itemInspect.globalCustomizationWeaponScale;
        }
        return globalCustomizationWeaponScale;
    }

    public static float getGlobalInspectScale() {
        if (efw.biomeinfo.MwccfConfig.itemInspect != null
                && efw.biomeinfo.MwccfConfig.itemInspect.globalInspectScale > 0.0) {
            return (float) efw.biomeinfo.MwccfConfig.itemInspect.globalInspectScale;
        }
        return globalInspectScale;
    }

    public static Map<String, GroupTransform> groups = new HashMap<>();

    public static class ConfigFileContent {
        public float globalCustomizationWeaponScale = 1.0f;
        public float globalInspectScale = 1.0f;
        public TextSettings text = new TextSettings();
        public Map<String, GroupTransform> groups = new HashMap<>();
    }

    public static void load(File configDir) {
        configFile = new File(configDir, "item_inspect_transforms.json");
        initDefaults();

        if (configFile.exists()) {
            try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
                ConfigFileContent content = GSON.fromJson(reader, ConfigFileContent.class);
                if (content != null) {
                    if (content.globalCustomizationWeaponScale > 0.0f) {
                        globalCustomizationWeaponScale = content.globalCustomizationWeaponScale;
                    }
                    if (content.globalInspectScale > 0.0f) {
                        globalInspectScale = content.globalInspectScale;
                    }
                    if (content.text != null) {
                        textSettings = content.text;
                    }
                    if (content.groups != null) {
                        for (Map.Entry<String, GroupTransform> entry : content.groups.entrySet()) {
                            groups.put(entry.getKey(), entry.getValue());
                        }
                    }
                    FMLLog.info("[ItemInspect] Loaded transforms and text configuration from %s", configFile.getName());
                }
            } catch (Exception e) {
                FMLLog.warning("[ItemInspect] Failed to load transforms config: %s", e.getMessage());
            }
        } else {
            save();
        }
    }

    public static void save() {
        if (configFile == null) {
            configFile = new File(Minecraft.getMinecraft().gameDir, "config/item_inspect_transforms.json");
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            ConfigFileContent content = new ConfigFileContent();
            content.globalCustomizationWeaponScale = globalCustomizationWeaponScale;
            content.globalInspectScale = globalInspectScale;
            content.text = textSettings;
            content.groups = groups;
            GSON.toJson(content, writer);
        } catch (Exception e) {
            FMLLog.warning("[ItemInspect] Failed to save transforms config: %s", e.getMessage());
        }
    }

    private static void initDefaults() {
        groups.put(InspectGroup.PISTOLS.key, new GroupTransform(2.2f, 0.0f, 0.1f, 0.1f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.SMG.key, new GroupTransform(1.6f, 0.0f, 0.15f, 0.15f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.WEAPONS_OTHER.key, new GroupTransform(1.2f, 0.0f, 0.15f, 0.25f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.HELMET_MWCCF.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.ARMOR_GEO_MWCCF.key, new GroupTransform(1.8f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.HELMET_MCORE.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.ARMOR_MCORE.key, new GroupTransform(1.8f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.HELMET_VANILLA.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.ARMOR_VANILLA.key, new GroupTransform(1.8f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.HEADLAMP.key, new GroupTransform(2.5f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.BRACELET.key, new GroupTransform(2.5f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.NOTE.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.DIARY.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        // DPOR рендерится в Inspect GUI точно так же, как записка (тот же трансформ по
        // умолчанию)
        groups.put(InspectGroup.DPOR.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.BAUBLES.key, new GroupTransform(2.0f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.TOOLS_2D.key, new GroupTransform(1.8f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
        groups.put(InspectGroup.ITEMS_2D.key, new GroupTransform(1.8f, 0.0f, 0.0f, 0.0f, 25.0f, 15.0f, 0.0f));
    }

    public static InspectGroup resolveGroup(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return InspectGroup.ITEMS_2D;
        Item item = stack.getItem();

        // If it's a block item (ItemBlock), it should never be classified as armor
        if (item instanceof net.minecraft.item.ItemBlock) {
            return InspectGroup.ITEMS_2D;
        }

        String regName = item.getRegistryName() != null ? item.getRegistryName().toString().toLowerCase() : "";
        String cls = item.getClass().getName().toLowerCase();

        // Check DPOR (Defiled Page of Revelation) — renders like a note, fixed
        // description
        if (item instanceof efw.item.DporItem || regName.equals("mwccf:dpor")) {
            return InspectGroup.DPOR;
        }

        // Check Note item specifically
        if (item instanceof efw.item.NoteItem || regName.equals("mwccf:note") || regName.contains("note")
                || cls.contains("noteitem")) {
            return InspectGroup.NOTE;
        }

        // Check Diary item specifically
        if (item instanceof efw.item.CDiaryItem || regName.equals("mwccf:c_diary") || regName.contains("diary")
                || cls.contains("diary")) {
            return InspectGroup.DIARY;
        }

        boolean isMWC = item instanceof com.paneedah.weaponlib.Weapon || cls.contains("weaponlib")
                || cls.contains("weapon");

        if (isMWC) {
            // Check pistol using efw WeaponTypeHelper
            if (WeaponTypeHelper.getWeaponType(stack) == WeaponTypeHelper.WeaponType.PISTOL) {
                return InspectGroup.PISTOLS;
            }

            for (String smgKey : SMG_KEYWORDS) {
                if (regName.contains(smgKey)) {
                    return InspectGroup.SMG;
                }
            }
            return InspectGroup.WEAPONS_OTHER;
        }

        // Check Headlamp specifically
        if (item instanceof com.voltyx.mwccf.geo.ItemHeadlamp || regName.contains("headlamp")
                || cls.contains("headlamp")) {
            return InspectGroup.HEADLAMP;
        }

        // Check Volttech Bracelet specifically
        if (item instanceof com.voltyx.mwccf.geo.ItemBracelet || regName.contains("bracelet")
                || cls.contains("bracelet")) {
            return InspectGroup.BRACELET;
        }

        // Check other Baubles: Backpack, etc.
        if (regName.contains("backpack") || regName.equals("quark:backpack") || cls.contains("backpack")) {
            return InspectGroup.BAUBLES;
        }

        // Check MWCCF Geo Armor (ItemGeoArmor)
        if (item instanceof ItemGeoArmor) {
            ItemGeoArmor geoArmor = (ItemGeoArmor) item;
            if (geoArmor.armorType == net.minecraft.inventory.EntityEquipmentSlot.HEAD) {
                return InspectGroup.HELMET_MWCCF;
            }
            return InspectGroup.ARMOR_GEO_MWCCF;
        }

        // Check MWCCF MCore Custom Armor (ItemCustomArmor)
        if (item instanceof ItemCustomArmor) {
            ItemCustomArmor customArmor = (ItemCustomArmor) item;
            if (customArmor.armorType == net.minecraft.inventory.EntityEquipmentSlot.HEAD) {
                return InspectGroup.HELMET_MCORE;
            }
            return InspectGroup.ARMOR_MCORE;
        }

        // Check Vanilla / Generic Armor (ItemArmor)
        if (item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) item;
            if (armor.armorType == net.minecraft.inventory.EntityEquipmentSlot.HEAD) {
                return InspectGroup.HELMET_VANILLA;
            }
            return InspectGroup.ARMOR_VANILLA;
        }

        // Check 2D Tools / Weapons
        if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemHoe
                || item instanceof ItemPickaxe || item instanceof ItemAxe || item instanceof ItemSpade
                || regName.contains("sword") || regName.contains("pickaxe") || regName.contains("axe")
                || regName.contains("shovel") || regName.contains("hoe") || regName.contains("bow")
                || regName.contains("gun") || regName.contains("dagger") || regName.contains("knife")
                || regName.contains("shield")) {
            return InspectGroup.TOOLS_2D;
        }

        // Regular 2D items and blocks
        return InspectGroup.ITEMS_2D;
    }

    public static GroupTransform getTransform(InspectGroup group) {
        // Если это DPOR, берем ключ от обычных записок (NOTE)
        String configKey = (group == InspectGroup.DPOR) ? InspectGroup.NOTE.key : group.key;

        GroupTransform gt = groups.get(configKey);
        if (gt == null) {
            gt = new GroupTransform();
            groups.put(configKey, gt);
        }
        return gt;
    }
}
