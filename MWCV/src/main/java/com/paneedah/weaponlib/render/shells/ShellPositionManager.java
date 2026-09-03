package com.paneedah.weaponlib.render.shells;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.Weapon;
import com.paneedah.weaponlib.WeaponFireAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@SideOnly(Side.CLIENT)
public class ShellPositionManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(Minecraft.getMinecraft().gameDir, "config/mwc_shell_positions.json");

    public enum WeaponCategory {
        RIFLE("Винтовки / Автоматы / Снайперки"),
        OTHER("Остальное оружие (ПП, Пистолеты)");

        public final String displayName;

        WeaponCategory(String displayName) {
            this.displayName = displayName;
        }
    }

    public enum ViewMode {
        FIRST_PERSON("1-е лицо"),
        THIRD_PERSON_HIP("3-е лицо (от бедра)"),
        THIRD_PERSON_AIM("3-е лицо (в прицеле)");

        public final String displayName;

        ViewMode(String displayName) {
            this.displayName = displayName;
        }
    }

    // Default configuration values
    public static Vec3d rifleFirstPerson = new Vec3d(-0.40, -0.20, 1.35);
    public static Vec3d rifleThirdPerson = new Vec3d(-0.20, -0.25, 0.75);
    public static Vec3d rifleThirdPersonAim = new Vec3d(-0.20, -0.20, 0.70);

    public static Vec3d otherFirstPerson = new Vec3d(-0.10, -0.20, 1.25);
    public static Vec3d otherThirdPerson = new Vec3d(-0.15, -0.20, 0.65);
    public static Vec3d otherThirdPersonAim = new Vec3d(-0.20, -0.15, 0.90);

    // In-game adjuster state
    public static boolean isAdjusterOpen = false;
    public static WeaponCategory selectedCategory = WeaponCategory.RIFLE;
    public static ViewMode selectedMode = ViewMode.THIRD_PERSON_HIP;
    public static boolean autoFollowState = true;

    static {
        loadConfig();
    }

    public static Vec3d getShellOffset(Weapon weapon, boolean isThirdPerson, boolean isAiming) {
        boolean isRifle = WeaponFireAspect.isRifleOrSniper(weapon);
        if (!isThirdPerson) {
            return isRifle ? rifleFirstPerson : otherFirstPerson;
        } else if (isAiming) {
            return isRifle ? rifleThirdPersonAim : otherThirdPersonAim;
        } else {
            return isRifle ? rifleThirdPerson : otherThirdPerson;
        }
    }

    public static Vec3d getOffset(WeaponCategory category, ViewMode mode) {
        if (category == WeaponCategory.RIFLE) {
            switch (mode) {
                case FIRST_PERSON: return rifleFirstPerson;
                case THIRD_PERSON_AIM: return rifleThirdPersonAim;
                case THIRD_PERSON_HIP:
                default: return rifleThirdPerson;
            }
        } else {
            switch (mode) {
                case FIRST_PERSON: return otherFirstPerson;
                case THIRD_PERSON_AIM: return otherThirdPersonAim;
                case THIRD_PERSON_HIP:
                default: return otherThirdPerson;
            }
        }
    }

    public static void setOffset(WeaponCategory category, ViewMode mode, Vec3d vec) {
        if (category == WeaponCategory.RIFLE) {
            switch (mode) {
                case FIRST_PERSON: rifleFirstPerson = vec; break;
                case THIRD_PERSON_AIM: rifleThirdPersonAim = vec; break;
                case THIRD_PERSON_HIP:
                default: rifleThirdPerson = vec; break;
            }
        } else {
            switch (mode) {
                case FIRST_PERSON: otherFirstPerson = vec; break;
                case THIRD_PERSON_AIM: otherThirdPersonAim = vec; break;
                case THIRD_PERSON_HIP:
                default: otherThirdPerson = vec; break;
            }
        }
    }

    public static void updateAutoState(EntityPlayer player, PlayerWeaponInstance pwi) {
        if (!autoFollowState || player == null) return;

        if (pwi != null && pwi.getWeapon() != null) {
            selectedCategory = WeaponFireAspect.isRifleOrSniper(pwi.getWeapon()) ? WeaponCategory.RIFLE : WeaponCategory.OTHER;
        }

        Minecraft mc = Minecraft.getMinecraft();
        boolean isThirdPerson = mc.gameSettings.thirdPersonView != 0;
        boolean isAiming = (pwi != null && pwi.isAimed()) || player.isSneaking();

        if (!isThirdPerson) {
            selectedMode = ViewMode.FIRST_PERSON;
        } else if (isAiming) {
            selectedMode = ViewMode.THIRD_PERSON_AIM;
        } else {
            selectedMode = ViewMode.THIRD_PERSON_HIP;
        }
    }

    public static boolean handleKeyPress(int keyCode, boolean isShiftDown, boolean isCtrlDown) {
        if (!isAdjusterOpen) return false;

        double step = isShiftDown ? 0.01 : (isCtrlDown ? 0.10 : 0.05);
        Vec3d current = getOffset(selectedCategory, selectedMode);
        double x = current.x;
        double y = current.y;
        double z = current.z;

        boolean changed = false;

        if (keyCode == Keyboard.KEY_LEFT) {
            x = Math.round((x - step) * 1000.0) / 1000.0;
            changed = true;
        } else if (keyCode == Keyboard.KEY_RIGHT) {
            x = Math.round((x + step) * 1000.0) / 1000.0;
            changed = true;
        } else if (keyCode == Keyboard.KEY_UP) {
            y = Math.round((y + step) * 1000.0) / 1000.0;
            changed = true;
        } else if (keyCode == Keyboard.KEY_DOWN) {
            y = Math.round((y - step) * 1000.0) / 1000.0;
            changed = true;
        } else if (keyCode == Keyboard.KEY_PRIOR || keyCode == Keyboard.KEY_RBRACKET) { // PageUp or ]
            z = Math.round((z + step) * 1000.0) / 1000.0;
            changed = true;
        } else if (keyCode == Keyboard.KEY_NEXT || keyCode == Keyboard.KEY_LBRACKET) { // PageDown or [
            z = Math.round((z - step) * 1000.0) / 1000.0;
            changed = true;
        } else if (keyCode == Keyboard.KEY_TAB) {
            int nextMode = (selectedMode.ordinal() + 1) % ViewMode.values().length;
            selectedMode = ViewMode.values()[nextMode];
            autoFollowState = false;
            return true;
        } else if (keyCode == Keyboard.KEY_R) {
            selectedCategory = selectedCategory == WeaponCategory.RIFLE ? WeaponCategory.OTHER : WeaponCategory.RIFLE;
            autoFollowState = false;
            return true;
        } else if (keyCode == Keyboard.KEY_F) {
            autoFollowState = !autoFollowState;
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null) {
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "[MWC ShellPos] " + TextFormatting.YELLOW + "Auto-Follow: " + (autoFollowState ? TextFormatting.GREEN + "ВКЛ" : TextFormatting.RED + "ВЫКЛ")));
            }
            return true;
        } else if (keyCode == Keyboard.KEY_T || keyCode == Keyboard.KEY_SPACE) {
            spawnTestShell();
            return true;
        } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
            saveConfig();
            printCoordinatesToChat();
            return true;
        }

        if (changed) {
            setOffset(selectedCategory, selectedMode, new Vec3d(x, y, z));
            saveConfig();
            spawnTestShell();
            return true;
        }

        return false;
    }

    public static void spawnTestShell() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null) return;

        com.paneedah.weaponlib.PlayerWeaponInstance pwi = com.paneedah.weaponlib.ClientModContext.getContext() != null 
            ? com.paneedah.weaponlib.ClientModContext.getContext().getMainHeldWeapon() : null;
        Weapon weapon = pwi != null ? pwi.getWeapon() : null;

        ShellParticleSimulator.Shell.Type type = weapon != null ? weapon.getShellType() : ShellParticleSimulator.Shell.Type.ASSAULT;
        boolean isThirdPerson = mc.gameSettings.thirdPersonView != 0;
        boolean isAiming = (pwi != null && pwi.isAimed()) || player.isSneaking();
        Vec3d offset = getShellOffset(weapon, isThirdPerson, isAiming);
        float fovExtra = (!isThirdPerson && mc.gameSettings.fovSetting >= 70f) ? -(mc.gameSettings.fovSetting/200f) * 0.3f : 0f;
        Vec3d weaponDir = new Vec3d(offset.x, offset.y, offset.z + fovExtra).rotatePitch((float) Math.toRadians(-player.rotationPitch)).rotateYaw((float) Math.toRadians(-player.rotationYaw));
        Vec3d pos = player.getPositionEyes(1.0f).add(weaponDir);

        Vec3d velocity = new Vec3d(-0.3, 0.1, 0.0);
        velocity = velocity.rotateYaw((float) Math.toRadians(-player.rotationYaw));
        ShellParticleSimulator.Shell shell = new ShellParticleSimulator.Shell(type, pos, new Vec3d(-90, 0, 180 + player.rotationYaw), velocity);
        com.paneedah.weaponlib.ClientEventHandler.SHELL_MANAGER.enqueueShell(shell);
    }

    public static void printCoordinatesToChat() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null) return;

        Vec3d r1 = rifleFirstPerson;
        Vec3d r3 = rifleThirdPerson;
        Vec3d r3a = rifleThirdPersonAim;
        Vec3d o1 = otherFirstPerson;
        Vec3d o3 = otherThirdPerson;
        Vec3d o3a = otherThirdPersonAim;

        player.sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "================ [ MWC SHELL POSITIONS ] ================"));
        player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Винтовки / Снайперки:"));
        player.sendMessage(new TextComponentString(String.format(java.util.Locale.US, "  1-е лицо:        X=%.3f, Y=%.3f, Z=%.3f", r1.x, r1.y, r1.z)));
        player.sendMessage(new TextComponentString(String.format(java.util.Locale.US, "  3-е лицо (бедро): X=%.3f, Y=%.3f, Z=%.3f", r3.x, r3.y, r3.z)));
        player.sendMessage(new TextComponentString(String.format(java.util.Locale.US, "  3-е лицо (зум):   X=%.3f, Y=%.3f, Z=%.3f", r3a.x, r3a.y, r3a.z)));
        player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Остальное оружие (ПП, Пистолеты):"));
        player.sendMessage(new TextComponentString(String.format(java.util.Locale.US, "  1-е лицо:        X=%.3f, Y=%.3f, Z=%.3f", o1.x, o1.y, o1.z)));
        player.sendMessage(new TextComponentString(String.format(java.util.Locale.US, "  3-е лицо (бедро): X=%.3f, Y=%.3f, Z=%.3f", o3.x, o3.y, o3.z)));
        player.sendMessage(new TextComponentString(String.format(java.util.Locale.US, "  3-е лицо (зум):   X=%.3f, Y=%.3f, Z=%.3f", o3a.x, o3a.y, o3a.z)));
        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Сохранено в config/mwc_shell_positions.json!"));
        player.sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "========================================================"));
    }

    public static void loadConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                saveConfig();
                return;
            }
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json != null) {
                    if (json.has("rifle_1st")) rifleFirstPerson = parseVec(json.getAsJsonObject("rifle_1st"), rifleFirstPerson);
                    if (json.has("rifle_3rd_hip")) rifleThirdPerson = parseVec(json.getAsJsonObject("rifle_3rd_hip"), rifleThirdPerson);
                    if (json.has("rifle_3rd_aim")) rifleThirdPersonAim = parseVec(json.getAsJsonObject("rifle_3rd_aim"), rifleThirdPersonAim);
                    if (json.has("other_1st")) otherFirstPerson = parseVec(json.getAsJsonObject("other_1st"), otherFirstPerson);
                    if (json.has("other_3rd_hip")) otherThirdPerson = parseVec(json.getAsJsonObject("other_3rd_hip"), otherThirdPerson);
                    if (json.has("other_3rd_aim")) otherThirdPersonAim = parseVec(json.getAsJsonObject("other_3rd_aim"), otherThirdPersonAim);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
            }
            JsonObject json = new JsonObject();
            json.add("rifle_1st", toJsonObject(rifleFirstPerson));
            json.add("rifle_3rd_hip", toJsonObject(rifleThirdPerson));
            json.add("rifle_3rd_aim", toJsonObject(rifleThirdPersonAim));
            json.add("other_1st", toJsonObject(otherFirstPerson));
            json.add("other_3rd_hip", toJsonObject(otherThirdPerson));
            json.add("other_3rd_aim", toJsonObject(otherThirdPersonAim));

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Vec3d parseVec(JsonObject obj, Vec3d defaultVec) {
        if (obj == null) return defaultVec;
        double x = obj.has("x") ? obj.get("x").getAsDouble() : defaultVec.x;
        double y = obj.has("y") ? obj.get("y").getAsDouble() : defaultVec.y;
        double z = obj.has("z") ? obj.get("z").getAsDouble() : defaultVec.z;
        return new Vec3d(x, y, z);
    }

    private static JsonObject toJsonObject(Vec3d vec) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", Math.round(vec.x * 1000.0) / 1000.0);
        obj.addProperty("y", Math.round(vec.y * 1000.0) / 1000.0);
        obj.addProperty("z", Math.round(vec.z * 1000.0) / 1000.0);
        return obj;
    }
}
