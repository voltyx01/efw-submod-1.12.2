package com.voltyx.mwccf.furniture.client.gui;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.furniture.tileentity.TileEntityPlacedItem;
import com.voltyx.mwccf.network.PacketUpdatePlacedItem;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPlacedItemConfig extends GuiScreen {

    private final TileEntityPlacedItem te;

    private float curX, curY, curZ;
    private float curYaw, curPitch, curRoll;
    private float curScale;
    private float curIconX, curIconY, curIconZ;
    private boolean curLocked;

    // Button IDs
    private static final int BTN_DONE = 0;
    private static final int BTN_PRESET_FLAT = 1;
    private static final int BTN_PRESET_SIDE = 2;
    private static final int BTN_PRESET_WALL = 3;
    private static final int BTN_PRESET_STAND = 4;
    private static final int BTN_YAW_M45 = 10;
    private static final int BTN_YAW_M15 = 11;
    private static final int BTN_YAW_P15 = 12;
    private static final int BTN_YAW_P45 = 13;
    private static final int BTN_PITCH_M15 = 20;
    private static final int BTN_PITCH_M5 = 21;
    private static final int BTN_PITCH_P5 = 22;
    private static final int BTN_PITCH_P15 = 23;
    private static final int BTN_ROLL_M15 = 30;
    private static final int BTN_ROLL_M5 = 31;
    private static final int BTN_ROLL_P5 = 32;
    private static final int BTN_ROLL_P15 = 33;
    private static final int BTN_X_M = 40;
    private static final int BTN_X_P = 41;
    private static final int BTN_Y_M = 42;
    private static final int BTN_Y_P = 43;
    private static final int BTN_Z_M = 44;
    private static final int BTN_Z_P = 45;
    private static final int BTN_LOCK_TOGGLE = 50;
    private static final int BTN_ROTATE_180 = 51;

    // Scale Button IDs
    private static final int BTN_SCALE_M10 = 60;
    private static final int BTN_SCALE_M02 = 61;
    private static final int BTN_SCALE_RESET = 62;
    private static final int BTN_SCALE_P02 = 63;
    private static final int BTN_SCALE_P10 = 64;

    // Icon Position Button IDs
    private static final int BTN_ICON_XM = 70;
    private static final int BTN_ICON_XP = 71;
    private static final int BTN_ICON_YM = 72;
    private static final int BTN_ICON_YP = 73;
    private static final int BTN_ICON_ZM = 74;
    private static final int BTN_ICON_ZP = 75;
    private static final int BTN_ICON_RESET = 76;

    public GuiPlacedItemConfig(TileEntityPlacedItem te) {
        this.te = te;
        this.curX = te.getOffsetX();
        this.curY = te.getOffsetY();
        this.curZ = te.getOffsetZ();
        this.curYaw = te.getRotationYaw();
        this.curPitch = te.getRotationPitch();
        this.curRoll = te.getRotationRoll();
        this.curScale = te.getScale();
        this.curIconX = te.getIconOffsetX();
        this.curIconY = te.getIconOffsetY();
        this.curIconZ = te.getIconOffsetZ();
        this.curLocked = te.isLocked();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int startX = 20; // Anchor to left side so the player can see the object in the world!
        int y = 30;

        // Presets
        int pw = 58;
        this.buttonList.add(new GuiButton(BTN_PRESET_FLAT, startX, y, pw, 20, "Плашмя"));
        this.buttonList.add(new GuiButton(BTN_PRESET_SIDE, startX + 62, y, pw, 20, "На боку"));
        this.buttonList.add(new GuiButton(BTN_PRESET_WALL, startX + 124, y, pw, 20, "К стене"));
        this.buttonList.add(new GuiButton(BTN_PRESET_STAND, startX + 186, y, pw, 20, "Стоя"));

        y += 24;
        // Yaw
        this.buttonList.add(new GuiButton(BTN_YAW_M45, startX, y, 35, 18, "-45°"));
        this.buttonList.add(new GuiButton(BTN_YAW_M15, startX + 37, y, 35, 18, "-15°"));
        this.buttonList.add(new GuiButton(BTN_ROTATE_180, startX + 74, y, 50, 18, "180°"));
        this.buttonList.add(new GuiButton(BTN_YAW_P15, startX + 126, y, 35, 18, "+15°"));
        this.buttonList.add(new GuiButton(BTN_YAW_P45, startX + 163, y, 35, 18, "+45°"));

        y += 22;
        // Pitch
        this.buttonList.add(new GuiButton(BTN_PITCH_M15, startX, y, 35, 18, "-15°"));
        this.buttonList.add(new GuiButton(BTN_PITCH_M5, startX + 37, y, 35, 18, "-5°"));
        this.buttonList.add(new GuiButton(BTN_PITCH_P5, startX + 126, y, 35, 18, "+5°"));
        this.buttonList.add(new GuiButton(BTN_PITCH_P15, startX + 163, y, 35, 18, "+15°"));

        y += 22;
        // Roll
        this.buttonList.add(new GuiButton(BTN_ROLL_M15, startX, y, 35, 18, "-15°"));
        this.buttonList.add(new GuiButton(BTN_ROLL_M5, startX + 37, y, 35, 18, "-5°"));
        this.buttonList.add(new GuiButton(BTN_ROLL_P5, startX + 126, y, 35, 18, "+5°"));
        this.buttonList.add(new GuiButton(BTN_ROLL_P15, startX + 163, y, 35, 18, "+15°"));

        y += 24;
        // Item Position Offsets
        this.buttonList.add(new GuiButton(BTN_X_M, startX, y, 35, 18, "X-"));
        this.buttonList.add(new GuiButton(BTN_X_P, startX + 37, y, 35, 18, "X+"));

        this.buttonList.add(new GuiButton(BTN_Y_M, startX + 80, y, 35, 18, "Y-"));
        this.buttonList.add(new GuiButton(BTN_Y_P, startX + 117, y, 35, 18, "Y+"));

        this.buttonList.add(new GuiButton(BTN_Z_M, startX + 160, y, 35, 18, "Z-"));
        this.buttonList.add(new GuiButton(BTN_Z_P, startX + 197, y, 35, 18, "Z+"));

        y += 24;
        // Item Scale buttons
        this.buttonList.add(new GuiButton(BTN_SCALE_M10, startX, y, 35, 18, "-0.1"));
        this.buttonList.add(new GuiButton(BTN_SCALE_M02, startX + 37, y, 40, 18, "-0.02"));
        this.buttonList.add(new GuiButton(BTN_SCALE_RESET, startX + 79, y, 40, 18, "1.0x"));
        this.buttonList.add(new GuiButton(BTN_SCALE_P02, startX + 121, y, 40, 18, "+0.02"));
        this.buttonList.add(new GuiButton(BTN_SCALE_P10, startX + 163, y, 35, 18, "+0.1"));

        y += 24;
        // Icon Position Offsets
        this.buttonList.add(new GuiButton(BTN_ICON_XM, startX, y, 32, 18, "IX-"));
        this.buttonList.add(new GuiButton(BTN_ICON_XP, startX + 34, y, 32, 18, "IX+"));

        this.buttonList.add(new GuiButton(BTN_ICON_YM, startX + 70, y, 32, 18, "IY-"));
        this.buttonList.add(new GuiButton(BTN_ICON_YP, startX + 104, y, 32, 18, "IY+"));

        this.buttonList.add(new GuiButton(BTN_ICON_ZM, startX + 140, y, 32, 18, "IZ-"));
        this.buttonList.add(new GuiButton(BTN_ICON_ZP, startX + 174, y, 32, 18, "IZ+"));

        this.buttonList.add(new GuiButton(BTN_ICON_RESET, startX + 210, y, 34, 18, "0"));

        y += 25;
        // Lock button
        this.buttonList.add(new GuiButton(BTN_LOCK_TOGGLE, startX, y, 140, 20, getLockText()));

        // Done button
        this.buttonList.add(new GuiButton(BTN_DONE, startX + 150, y, 94, 20, TextFormatting.GREEN + "Готово"));
    }

    private String getLockText() {
        return curLocked ? TextFormatting.RED + "Подбор: Заблокирован" : TextFormatting.GRAY + "Подбор: Разрешён";
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case BTN_DONE:
                syncAndClose();
                return;
            case BTN_PRESET_FLAT:
                curPitch = 0.0f;
                curRoll = 0.0f;
                break;
            case BTN_PRESET_SIDE:
                curPitch = 0.0f;
                curRoll = 90.0f;
                break;
            case BTN_PRESET_WALL:
                curPitch = 60.0f;
                curRoll = 0.0f;
                break;
            case BTN_PRESET_STAND:
                curPitch = 90.0f;
                curRoll = 0.0f;
                break;
            case BTN_YAW_M45:
                curYaw = normalizeAngle(curYaw - 45.0f);
                break;
            case BTN_YAW_M15:
                curYaw = normalizeAngle(curYaw - 15.0f);
                break;
            case BTN_YAW_P15:
                curYaw = normalizeAngle(curYaw + 15.0f);
                break;
            case BTN_YAW_P45:
                curYaw = normalizeAngle(curYaw + 45.0f);
                break;
            case BTN_ROTATE_180:
                curYaw = normalizeAngle(curYaw + 180.0f);
                break;
            case BTN_PITCH_M15:
                curPitch = normalizeAngle(curPitch - 15.0f);
                break;
            case BTN_PITCH_M5:
                curPitch = normalizeAngle(curPitch - 5.0f);
                break;
            case BTN_PITCH_P5:
                curPitch = normalizeAngle(curPitch + 5.0f);
                break;
            case BTN_PITCH_P15:
                curPitch = normalizeAngle(curPitch + 15.0f);
                break;
            case BTN_ROLL_M15:
                curRoll = normalizeAngle(curRoll - 15.0f);
                break;
            case BTN_ROLL_M5:
                curRoll = normalizeAngle(curRoll - 5.0f);
                break;
            case BTN_ROLL_P5:
                curRoll = normalizeAngle(curRoll + 5.0f);
                break;
            case BTN_ROLL_P15:
                curRoll = normalizeAngle(curRoll + 15.0f);
                break;
            case BTN_X_M:
                curX = round(curX - 0.05f);
                break;
            case BTN_X_P:
                curX = round(curX + 0.05f);
                break;
            case BTN_Y_M:
                curY = round(curY - 0.02f);
                break;
            case BTN_Y_P:
                curY = round(curY + 0.02f);
                break;
            case BTN_Z_M:
                curZ = round(curZ - 0.05f);
                break;
            case BTN_Z_P:
                curZ = round(curZ + 0.05f);
                break;
            case BTN_SCALE_M10:
                curScale = Math.max(0.1f, round(curScale - 0.1f));
                break;
            case BTN_SCALE_M02:
                curScale = Math.max(0.1f, round(curScale - 0.02f));
                break;
            case BTN_SCALE_RESET:
                curScale = 1.0f;
                break;
            case BTN_SCALE_P02:
                curScale = Math.min(5.0f, round(curScale + 0.02f));
                break;
            case BTN_SCALE_P10:
                curScale = Math.min(5.0f, round(curScale + 0.1f));
                break;
            case BTN_ICON_XM:
                curIconX = round(curIconX - 0.05f);
                break;
            case BTN_ICON_XP:
                curIconX = round(curIconX + 0.05f);
                break;
            case BTN_ICON_YM:
                curIconY = round(curIconY - 0.05f);
                break;
            case BTN_ICON_YP:
                curIconY = round(curIconY + 0.05f);
                break;
            case BTN_ICON_ZM:
                curIconZ = round(curIconZ - 0.05f);
                break;
            case BTN_ICON_ZP:
                curIconZ = round(curIconZ + 0.05f);
                break;
            case BTN_ICON_RESET:
                curIconX = 0.0f;
                curIconY = 0.0f;
                curIconZ = 0.0f;
                break;
            case BTN_LOCK_TOGGLE:
                curLocked = !curLocked;
                button.displayString = getLockText();
                break;
        }

        // Apply immediately to local client TE for live instant view!
        te.setOffsetX(curX);
        te.setOffsetY(curY);
        te.setOffsetZ(curZ);
        te.setRotationYaw(curYaw);
        te.setRotationPitch(curPitch);
        te.setRotationRoll(curRoll);
        te.setScale(curScale);
        te.setIconOffsetX(curIconX);
        te.setIconOffsetY(curIconY);
        te.setIconOffsetZ(curIconZ);
        te.setLocked(curLocked);

        // Send packet to server
        sendUpdatePacket();
    }

    private float normalizeAngle(float angle) {
        angle = angle % 360.0f;
        if (angle < 0.0f) angle += 360.0f;
        return round(angle);
    }

    private float round(float val) {
        return Math.round(val * 100.0f) / 100.0f;
    }

    private void sendUpdatePacket() {
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketUpdatePlacedItem(
                te.getPos(), curX, curY, curZ, curYaw, curPitch, curRoll, curScale, curLocked,
                curIconX, curIconY, curIconZ
        ));
    }

    private void syncAndClose() {
        sendUpdatePacket();
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw elegant semi-transparent dark panel on the left side
        drawRect(12, 10, 268, 235, 0xBB101418);
        drawRect(14, 12, 266, 233, 0x66222830);

        int startX = 20;
        int textY = 16;

        String title = TextFormatting.YELLOW + "Настройка положения предмета";
        this.fontRenderer.drawStringWithShadow(title, startX, textY, 0xFFFFFF);

        String itemName = te.getStack().isEmpty() ? "Пусто" : te.getStack().getDisplayName();
        this.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + "Предмет: " + TextFormatting.WHITE + itemName, startX, textY + 11, 0xAAAAAA);

        // Labels beside control rows
        this.fontRenderer.drawStringWithShadow("Yaw: " + (int) curYaw + "°", startX + 203, 58, 0xE0E0E0);
        this.fontRenderer.drawStringWithShadow("Pitch: " + (int) curPitch + "°", startX + 75, 80, 0xE0E0E0);
        this.fontRenderer.drawStringWithShadow("Roll: " + (int) curRoll + "°", startX + 75, 102, 0xE0E0E0);

        String posText = String.format("Поз: X:%.2f Y:%.2f Z:%.2f", curX, curY, curZ);
        this.fontRenderer.drawStringWithShadow(TextFormatting.DARK_AQUA + posText, startX, 123, 0x88CCEE);

        String scaleText = String.format("Размер: %.2fx", curScale);
        this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD + scaleText, startX + 200, 151, 0xFFCC44);

        String iconText = String.format("Лупа: X:%.2f Y:%.2f Z:%.2f", curIconX, curIconY, curIconZ);
        this.fontRenderer.drawStringWithShadow(TextFormatting.AQUA + iconText, startX, 172, 0x77DDFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
