package com.voltyx.mwccf.antenna.client;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.antenna.TileEntityAntenna;
import com.voltyx.mwccf.antenna.network.PacketAntennaPin;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class AntennaSession {

    private static final AntennaSession INSTANCE = new AntennaSession();

    private String inputBuffer = "";
    private float screenFade = 0.0f; // 0.0 (off / dark) -> 1.0 (bright blue active)
    private String statusMessage = ""; // "", "ACCESS", "ERROR"
    private float statusTimer = 0.0f;
    private boolean cursorVisible = true;
    private float cursorBlinkTimer = 0.0f;

    public static AntennaSession getInstance() {
        return INSTANCE;
    }

    public void reset() {
        inputBuffer = "";
        statusMessage = "";
        statusTimer = 0.0f;
        cursorBlinkTimer = 0.0f;
        cursorVisible = true;
    }

    public void update(float dt, boolean active) {
        if (active) {
            screenFade = Math.min(1.0f, screenFade + dt * 3.5f);
        } else {
            screenFade = Math.max(0.0f, screenFade - dt * 4.0f);
        }

        cursorBlinkTimer += dt;
        if (cursorBlinkTimer >= 0.45f) {
            cursorBlinkTimer = 0.0f;
            cursorVisible = !cursorVisible;
        }

        if (statusTimer > 0.0f) {
            statusTimer -= dt;
            if (statusTimer <= 0.0f) {
                if ("ACCESS".equals(statusMessage)) {
                    // Automatically close session after successful door open
                    AntennaCameraController.close();
                }
                statusMessage = "";
            }
        }
    }

    public void handleKeyTyped(char typedChar, int keyCode, TileEntityAntenna antenna, BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();

        // Numbers 0-9
        if (typedChar >= '0' && typedChar <= '9') {
            if (statusTimer > 0.0f && "ERROR".equals(statusMessage)) {
                statusMessage = "";
                inputBuffer = "";
            }
            if (inputBuffer.length() < 4) {
                inputBuffer += typedChar;
                String btnName = getButtonNameForChar(typedChar);
                if (antenna != null && btnName != null) {
                    antenna.triggerButtonPress(btnName);
                }
                if (mc.player != null) {
                    mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.45F, 1.8F);
                }
            }
            return;
        }

        // Clear (Backspace, 'C', 'c')
        if (keyCode == Keyboard.KEY_BACK || typedChar == 'c' || typedChar == 'C' || keyCode == Keyboard.KEY_DELETE) {
            if (antenna != null) {
                antenna.triggerButtonPress("clear");
            }
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.45F, 1.4F);
            }
            if (!inputBuffer.isEmpty()) {
                inputBuffer = inputBuffer.substring(0, inputBuffer.length() - 1);
            }
            statusMessage = "";
            return;
        }

        // Okay (Enter, Numpad Enter, 'K', 'k', Space)
        if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER || typedChar == 'k' || typedChar == 'K' || keyCode == Keyboard.KEY_SPACE) {
            if (antenna != null) {
                antenna.triggerButtonPress("okay");
            }
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.55F, 1.6F);
            }
            validatePin(antenna, pos);
        }
    }

    public void pressVirtualButton(String btnName, TileEntityAntenna antenna, BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (antenna != null) {
            antenna.triggerButtonPress(btnName);
        }

        char c = getCharForButtonName(btnName);
        if (c >= '0' && c <= '9') {
            if (statusTimer > 0.0f && "ERROR".equals(statusMessage)) {
                statusMessage = "";
                inputBuffer = "";
            }
            if (inputBuffer.length() < 4) {
                inputBuffer += c;
                if (mc.player != null) {
                    mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.45F, 1.8F);
                }
            }
        } else if ("clear".equals(btnName)) {
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.45F, 1.4F);
            }
            if (!inputBuffer.isEmpty()) {
                inputBuffer = inputBuffer.substring(0, inputBuffer.length() - 1);
            }
            statusMessage = "";
        } else if ("okay".equals(btnName)) {
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.55F, 1.6F);
            }
            validatePin(antenna, pos);
        }
    }

    private void validatePin(TileEntityAntenna antenna, BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (pos != null) {
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketAntennaPin(pos, inputBuffer));
        }

        if (antenna != null) {
            if (antenna.getPinCode().equals(inputBuffer)) {
                statusMessage = "ACCESS";
                statusTimer = 2.4f;
                antenna.setUnlocked(true);
                antenna.setOpen(true);
                if (mc.player != null) {
                    mc.player.playSound(SoundEvents.BLOCK_NOTE_BELL, 1.0F, 2.0F);
                }
            } else {
                statusMessage = "ERROR";
                statusTimer = 1.0f;
                inputBuffer = "";
                if (mc.player != null) {
                    mc.player.playSound(SoundEvents.BLOCK_NOTE_BASS, 1.0F, 0.6F);
                }
            }
        }
    }

    public static String getButtonNameForChar(char c) {
        switch (c) {
            case '1': return "one";
            case '2': return "two";
            case '3': return "three";
            case '4': return "four";
            case '5': return "five";
            case '6': return "six";
            case '7': return "seven";
            case '8': return "eight";
            case '9': return "nine";
            case '0': return "zero";
            default: return null;
        }
    }

    public static char getCharForButtonName(String btn) {
        switch (btn) {
            case "one": return '1';
            case "two": return '2';
            case "three": return '3';
            case "four": return '4';
            case "five": return '5';
            case "six": return '6';
            case "seven": return '7';
            case "eight": return '8';
            case "nine": return '9';
            case "zero": return '0';
            case "clear": return 'C';
            case "okay": return 'K';
            default: return ' ';
        }
    }

    public String getInputBuffer() {
        return inputBuffer;
    }

    public float getScreenFade() {
        return screenFade;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }
}
