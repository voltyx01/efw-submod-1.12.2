package com.voltyx.mwccf.terminal.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class TerminalSession {

    private static final TerminalSession INSTANCE = new TerminalSession();

    public static TerminalSession getInstance() {
        return INSTANCE;
    }

    public enum Stage {
        LOGIN_USER,
        LOGIN_PASS,
        LOGIN_ERROR,
        BOOT_SEQUENCE,
        SHELL
    }

    public static class ConsoleLine {
        public final String tag;
        public final int tagColor;
        public final String text;
        public final int textColor;

        public ConsoleLine(String tag, int tagColor, String text, int textColor) {
            this.tag = tag;
            this.tagColor = tagColor;
            this.text = text;
            this.textColor = textColor;
        }

        public ConsoleLine(String text, int textColor) {
            this(null, 0, text, textColor);
        }
    }

    public static class TerminalCommand {
        public final String name;
        public final String syntax;
        public final String description;
        public final String detailedHelp;

        public TerminalCommand(String name, String syntax, String description, String detailedHelp) {
            this.name = name;
            this.syntax = syntax;
            this.description = description;
            this.detailedHelp = detailedHelp;
        }

        public TerminalCommand(String name, String syntax, String description) {
            this(name, syntax, description, description);
        }
    }

    public static final List<TerminalCommand> COMMANDS = new ArrayList<>();

    static {
        COMMANDS.add(new TerminalCommand("help", "help [cmd]", "Show command manual or list",
                "Displays available commands list or detailed info for a specific command."));
        COMMANDS.add(new TerminalCommand("status", "status", "Show system & link status",
                "Displays CPU load, system power, and network interface status."));
        COMMANDS.add(new TerminalCommand("ifup", "ifup [eth0]", "Bring network interface UP",
                "Activates interface eth0 and initiates network link connection."));
        COMMANDS.add(new TerminalCommand("ifdown", "ifdown [eth0]", "Take network interface DOWN",
                "Deactivates interface eth0 and disconnects network link."));
        COMMANDS.add(new TerminalCommand("whoami", "whoami", "Show user identity & clearance",
                "Displays current username, system UID/GID, and security clearance level."));
        COMMANDS.add(new TerminalCommand("clear", "clear", "Clear screen buffer",
                "Clears all history lines from the terminal screen."));
        COMMANDS.add(new TerminalCommand("exit", "exit", "Log out and exit terminal",
                "Closes the terminal session and steps away."));
    }

    public static final ConsoleLine[] BOOT_LINES = new ConsoleLine[]{
            new ConsoleLine("OK", 0xFF00FF66, "Mounted /dev/nvme0n1 on /", 0xFF44FFAA),
            new ConsoleLine("OK", 0xFF00FF66, "Cryptographic engine active", 0xFF44FFAA),
            new ConsoleLine("DOWN", 0xFFFF4444, "Network interface eth0: link DOWN", 0xFFFF7777),
            new ConsoleLine("OK", 0xFF00FF66, "Terminal security node active", 0xFF44FFAA),
            new ConsoleLine("OK", 0xFF00FF66, "Core subsystems initialized. Ready.", 0xFF44FFAA),
            new ConsoleLine("Security Clearance: LEVEL 5 (ROOT)", 0xFF33CC88)
    };

    private Stage stage = Stage.LOGIN_USER;
    private final StringBuilder inputBuffer = new StringBuilder();
    private String enteredUser = "";
    private float screenFade = 0.0f;
    private float bootTimer = 0.0f;
    private int bootStep = 0;
    private float errorTimer = 0.0f;
    private float cursorTimer = 0.0f;
    private boolean cursorVisible = true;
    private boolean eth0Up = false;

    private final List<ConsoleLine> shellOutput = new ArrayList<>();

    private TerminalSession() {
        reset();
    }

    public void reset() {
        stage = Stage.LOGIN_USER;
        inputBuffer.setLength(0);
        enteredUser = "";
        screenFade = 0.0f;
        bootTimer = 0.0f;
        bootStep = 0;
        errorTimer = 0.0f;
        cursorTimer = 0.0f;
        cursorVisible = true;
        eth0Up = false;
        shellOutput.clear();
    }

    public void update(float dt, boolean active) {
        if (active) {
            // Only begin fading in once camera transition starts moving towards the terminal
            if (TerminalCameraController.getTransitionProgress() > 0.08f) {
                screenFade = Math.min(1.0f, screenFade + dt * 2.0f);
            } else {
                screenFade = 0.0f;
            }
        } else {
            screenFade = Math.max(0.0f, screenFade - dt * 4.0f);
            if (screenFade <= 0.0f) {
                reset();
            }
        }

        // Blinking cursor toggle every 0.5s
        cursorTimer += dt;
        if (cursorTimer >= 0.5f) {
            cursorTimer -= 0.5f;
            cursorVisible = !cursorVisible;
        }

        // Login error countdown
        if (stage == Stage.LOGIN_ERROR) {
            errorTimer -= dt;
            if (errorTimer <= 0.0f) {
                stage = Stage.LOGIN_USER;
                enteredUser = "";
                inputBuffer.setLength(0);
            }
        }

        // Sequential boot lines appearance
        if (stage == Stage.BOOT_SEQUENCE) {
            bootTimer += dt;
            if (bootStep < BOOT_LINES.length) {
                if (bootTimer >= (bootStep + 1) * 0.38f) {
                    bootStep++;
                    playKeySound(1.4f + bootStep * 0.05f);
                }
            } else {
                if (bootTimer >= BOOT_LINES.length * 0.38f + 0.45f) {
                    stage = Stage.SHELL;
                    inputBuffer.setLength(0);
                    shellOutput.clear();
                    shellOutput.add(new ConsoleLine("Type 'help' to see available commands.", 0xFF55FFBB));
                    playKeySound(1.8f);
                }
            }
        }
    }

    public void handleKeyTyped(char c, int keyCode) {
        if (stage == Stage.BOOT_SEQUENCE) return;

        if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
            handleEnter();
            playKeySound(1.2f);
            return;
        }

        if (keyCode == Keyboard.KEY_BACK) {
            if (inputBuffer.length() > 0) {
                inputBuffer.deleteCharAt(inputBuffer.length() - 1);
                playKeySound(1.5f);
            }
            return;
        }

        // Printable ASCII or Russian characters
        if ((c >= 32 && c <= 126) || (c >= 1040 && c <= 1103) || c == 'ё' || c == 'Ё') {
            if (inputBuffer.length() < 30) {
                inputBuffer.append(c);
                playKeySound(1.7f);
            }
        }
    }

    private void handleEnter() {
        String input = inputBuffer.toString().trim();
        inputBuffer.setLength(0);

        if (stage == Stage.LOGIN_USER) {
            enteredUser = input;
            stage = Stage.LOGIN_PASS;
        } else if (stage == Stage.LOGIN_PASS) {
            if ("root".equalsIgnoreCase(enteredUser) && "root".equals(input)) {
                stage = Stage.BOOT_SEQUENCE;
                bootTimer = 0.0f;
                bootStep = 0;
            } else {
                stage = Stage.LOGIN_ERROR;
                errorTimer = 1.2f;
            }
        } else if (stage == Stage.SHELL) {
            executeCommand(input);
        }
    }

    private void executeCommand(String rawCmd) {
        String cmd = rawCmd.trim();
        shellOutput.add(new ConsoleLine("root@mw-terminal:~# " + cmd, 0xFF44FFAA));

        if (cmd.isEmpty()) {
            trimHistory();
            return;
        }

        String[] parts = cmd.split("\\s+");
        String mainCmd = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1].toLowerCase() : null;

        if ("help".equals(mainCmd)) {
            if (arg != null) {
                TerminalCommand found = null;
                for (TerminalCommand c : COMMANDS) {
                    if (c.name.equalsIgnoreCase(arg)) {
                        found = c;
                        break;
                    }
                }
                if (found != null) {
                    shellOutput.add(new ConsoleLine("Command: " + found.syntax, 0xFF55FFBB));
                    shellOutput.add(new ConsoleLine(found.detailedHelp, 0xFF44FFAA));
                } else {
                    shellOutput.add(new ConsoleLine("help: unknown command '" + arg + "'", 0xFFFF6666));
                }
            } else {
                shellOutput.add(new ConsoleLine("=== Available Commands ===", 0xFF55FFBB));
                for (TerminalCommand c : COMMANDS) {
                    shellOutput.add(new ConsoleLine(c.syntax + " - " + c.description, 0xFF44FFAA));
                }
                shellOutput.add(new ConsoleLine("Type 'help <cmd>' for details.", 0xFF88DDAA));
            }
        } else if ("status".equals(mainCmd)) {
            shellOutput.add(new ConsoleLine("OK", 0xFF00FF66, "Core: ONLINE | Load: 0.08 | Power: NOMINAL", 0xFF55FFBB));
            shellOutput.add(new ConsoleLine("eth0 status: " + (eth0Up ? "UP (1000Mbps)" : "DOWN (No carrier)"), eth0Up ? 0xFF00FF66 : 0xFFFF4444));
        } else if ("ifup".equals(mainCmd) || "eth0 up".equals(cmd.toLowerCase())) {
            eth0Up = true;
            shellOutput.add(new ConsoleLine("OK", 0xFF00FF66, "Interface eth0: link UP (1000Mbps)", 0xFF00FF66));
            shellOutput.add(new ConsoleLine("eth0: IPv4 10.0.0.42/24 DHCP ACK", 0xFF44FFAA));
        } else if ("ifdown".equals(mainCmd) || "eth0 down".equals(cmd.toLowerCase())) {
            eth0Up = false;
            shellOutput.add(new ConsoleLine("DOWN", 0xFFFF4444, "Interface eth0: link DOWN", 0xFFFF4444));
            shellOutput.add(new ConsoleLine("eth0: link carrier lost.", 0xFFFF7777));
        } else if ("whoami".equals(mainCmd) || "id".equals(mainCmd)) {
            shellOutput.add(new ConsoleLine("root (UID 0, GID 0) - Clearance: LEVEL 5", 0xFF33CC88));
        } else if ("clear".equals(mainCmd)) {
            shellOutput.clear();
            shellOutput.add(new ConsoleLine("Type 'help' to see available commands.", 0xFF55FFBB));
        } else if ("exit".equals(mainCmd) || "quit".equals(mainCmd) || "logout".equals(mainCmd)) {
            TerminalCameraController.close();
        } else {
            shellOutput.add(new ConsoleLine("bash: " + cmd + ": command not found. Try 'help'.", 0xFFFF6666));
        }

        trimHistory();
    }

    private void trimHistory() {
        while (shellOutput.size() > 10) {
            shellOutput.remove(0);
        }
    }

    private void playKeySound(float pitch) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getSoundHandler() != null) {
            mc.getSoundHandler().playSound(
                    PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, pitch)
            );
        }
    }

    public Stage getStage() {
        return stage;
    }

    public String getInputBuffer() {
        return inputBuffer.toString();
    }

    public String getEnteredUser() {
        return enteredUser;
    }

    public float getScreenFade() {
        return screenFade;
    }

    public int getBootStep() {
        return bootStep;
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    public boolean isEth0Up() {
        return eth0Up;
    }

    public List<ConsoleLine> getShellOutput() {
        return shellOutput;
    }
}
