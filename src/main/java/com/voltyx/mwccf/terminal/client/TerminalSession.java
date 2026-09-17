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
        SHELL,
        CHAT_MENU,
        APP_CHAT
    }

    public enum ChatSubMenu {
        MAIN,
        PRIVATE,
        GROUPS
    }

    /** Верхнеуровневые пункты меню чата */
    public static final String[] CHAT_MENU_ITEMS = {
            "Global Chat",
            "Private Chats",
            "Groups"
    };

    /** Подпункты для Groups */
    public static final String[] GROUPS_MENU_ITEMS = {
            "Create Group",
            "Invites",
            "  Alpha Recon",
            "  Delta Squad",
            "  Sector-7 Outpost"
    };

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
        COMMANDS.add(new TerminalCommand("chat", "chat", "Open interactive chat menu",
                "Opens the network chat menu (Global, Private, Groups). Requires eth0 UP."));
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
    private boolean hasModule = false; // Has internet module been physically installed?

    // Chat menu state
    private int chatMenuIndex = 0;          // top-level selection
    private ChatSubMenu subMenu = ChatSubMenu.MAIN;
    private int privateMenuIndex = 0;
    private int groupsMenuIndex = 0;

    private String currentChatRoom = "Global Chat";
    private final List<ConsoleLine> chatMessages = new ArrayList<>();

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
        hasModule = false;
        chatMenuIndex = 0;
        subMenu = ChatSubMenu.MAIN;
        privateMenuIndex = 0;
        groupsMenuIndex = 0;
        currentChatRoom = "Global Chat";
        chatMessages.clear();
        shellOutput.clear();
    }

    private final List<String> moduleUsers = new ArrayList<>();

    /** Called when the terminal is opened — passes in current module state from TileEntity. */
    public void setModuleInstalled(boolean installed) {
        this.hasModule = installed;
        if (installed) {
            this.eth0Up = true;
        }
    }

    public void setModuleUsers(List<String> users) {
        this.moduleUsers.clear();
        if (users != null) {
            this.moduleUsers.addAll(users);
        }
    }

    public List<String> getModuleUsers() {
        List<String> list = new ArrayList<>(moduleUsers);
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && !list.contains(mc.player.getName())) {
            list.add(mc.player.getName());
        }
        if (list.isEmpty()) {
            list.add("root");
        }
        return list;
    }

    public boolean hasModule() {
        return hasModule;
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

        // ── Ctrl+C Process Interrupt ─────────────────────────────────────────
        boolean isCtrlDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        if (isCtrlDown && keyCode == Keyboard.KEY_C) {
            if (stage == Stage.APP_CHAT) {
                stage = Stage.SHELL;
                inputBuffer.setLength(0);
                shellOutput.add(new ConsoleLine("^C", 0xFFFF5555));
                shellOutput.add(new ConsoleLine("Process [chat: " + currentChatRoom + "] terminated (SIGINT).", 0xFF888888));
                trimHistory();
                playKeySound(1.1f);
                return;
            } else if (stage == Stage.CHAT_MENU) {
                stage = Stage.SHELL;
                inputBuffer.setLength(0);
                shellOutput.add(new ConsoleLine("^C", 0xFFFF5555));
                trimHistory();
                playKeySound(1.1f);
                return;
            } else if (stage == Stage.SHELL) {
                if (inputBuffer.length() > 0) {
                    shellOutput.add(new ConsoleLine("root@mw-terminal:~# " + inputBuffer.toString() + "^C", 0xFF44FFAA));
                    inputBuffer.setLength(0);
                    trimHistory();
                    playKeySound(1.1f);
                }
                return;
            }
        }

        // ── Chat application active process ──────────────────────────────────
        if (stage == Stage.APP_CHAT) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                stage = Stage.CHAT_MENU;
                inputBuffer.setLength(0);
                playKeySound(1.3f);
                return;
            }
            if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                String msg = inputBuffer.toString().trim();
                inputBuffer.setLength(0);
                if (!msg.isEmpty()) {
                    String sender = (Minecraft.getMinecraft().player != null)
                            ? Minecraft.getMinecraft().player.getName()
                            : "root";
                    chatMessages.add(new ConsoleLine("<" + sender + "> " + msg, 0xFFFFFFFF));
                    if (chatMessages.size() > 7) {
                        chatMessages.remove(0);
                    }
                }
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
            if ((c >= 32 && c <= 126) || (c >= 1040 && c <= 1103) || c == 'ё' || c == 'Ё') {
                if (inputBuffer.length() < 30) {
                    inputBuffer.append(c);
                    playKeySound(1.7f);
                }
            }
            return;
        }

        // ── Chat menu navigation ──────────────────────────────────────────────
        if (stage == Stage.CHAT_MENU) {
            if (keyCode == Keyboard.KEY_UP) {
                if (subMenu == ChatSubMenu.GROUPS) {
                    groupsMenuIndex = (groupsMenuIndex - 1 + GROUPS_MENU_ITEMS.length) % GROUPS_MENU_ITEMS.length;
                } else if (subMenu == ChatSubMenu.PRIVATE) {
                    List<String> users = getModuleUsers();
                    if (!users.isEmpty()) {
                        privateMenuIndex = (privateMenuIndex - 1 + users.size()) % users.size();
                    }
                } else {
                    chatMenuIndex = (chatMenuIndex - 1 + CHAT_MENU_ITEMS.length) % CHAT_MENU_ITEMS.length;
                }
                playKeySound(1.4f);
            } else if (keyCode == Keyboard.KEY_DOWN) {
                if (subMenu == ChatSubMenu.GROUPS) {
                    groupsMenuIndex = (groupsMenuIndex + 1) % GROUPS_MENU_ITEMS.length;
                } else if (subMenu == ChatSubMenu.PRIVATE) {
                    List<String> users = getModuleUsers();
                    if (!users.isEmpty()) {
                        privateMenuIndex = (privateMenuIndex + 1) % users.size();
                    }
                } else {
                    chatMenuIndex = (chatMenuIndex + 1) % CHAT_MENU_ITEMS.length;
                }
                playKeySound(1.4f);
            } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                handleChatMenuSelect();
                playKeySound(1.2f);
            } else if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK) {
                if (subMenu != ChatSubMenu.MAIN) {
                    subMenu = ChatSubMenu.MAIN;
                    privateMenuIndex = 0;
                    groupsMenuIndex = 0;
                } else {
                    stage = Stage.SHELL;
                    shellOutput.add(new ConsoleLine("Chat session closed.", 0xFF888888));
                    trimHistory();
                }
                playKeySound(1.3f);
            }
            return;
        }
        // ─────────────────────────────────────────────────────────────────────

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

    private void handleChatMenuSelect() {
        if (subMenu == ChatSubMenu.GROUPS) {
            String selected = GROUPS_MENU_ITEMS[groupsMenuIndex].trim();
            if (groupsMenuIndex == 0) {
                currentChatRoom = "Group: #Alpha";
            } else if (groupsMenuIndex == 1) {
                currentChatRoom = "Group: #Invites";
            } else {
                currentChatRoom = "Group: #" + selected;
            }
            chatMessages.clear();
            chatMessages.add(new ConsoleLine("GRP", 0xFF00FF66, "Joined room: " + currentChatRoom, 0xFF88DDAA));
            chatMessages.add(new ConsoleLine("GRP", 0xFF00FF66, "Multicast routing ready. 3 operators in group.", 0xFF88DDAA));
            inputBuffer.setLength(0);
            stage = Stage.APP_CHAT;
        } else if (subMenu == ChatSubMenu.PRIVATE) {
            List<String> users = getModuleUsers();
            String target = (!users.isEmpty() && privateMenuIndex < users.size()) ? users.get(privateMenuIndex) : "root";
            currentChatRoom = "@" + target;
            chatMessages.clear();
            chatMessages.add(new ConsoleLine("P2P", 0xFF00FF66, "Secure direct link established with " + target, 0xFF88DDAA));
            chatMessages.add(new ConsoleLine("P2P", 0xFF00FF66, "Diffie-Hellman Key Exchange OK. AES-256 active.", 0xFF88DDAA));
            inputBuffer.setLength(0);
            stage = Stage.APP_CHAT;
        } else {
            switch (chatMenuIndex) {
                case 0: // Global
                    currentChatRoom = "Global Chat";
                    chatMessages.clear();
                    chatMessages.add(new ConsoleLine("SYS", 0xFF00FF66, "Broadcast channel #global opened (eth0).", 0xFF88DDAA));
                    chatMessages.add(new ConsoleLine("SYS", 0xFF00FF66, "Connected to network frequency 144.200 MHz.", 0xFF88DDAA));
                    inputBuffer.setLength(0);
                    stage = Stage.APP_CHAT;
                    break;
                case 1: // Private
                    subMenu = ChatSubMenu.PRIVATE;
                    privateMenuIndex = 0;
                    break;
                case 2: // Groups
                    subMenu = ChatSubMenu.GROUPS;
                    groupsMenuIndex = 0;
                    break;
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
            String ethStatus = !hasModule ? "NO CARRIER (module not installed)"
                    : (eth0Up ? "UP (1000Mbps)" : "DOWN (No carrier)");
            int ethColor = (!hasModule || !eth0Up) ? 0xFFFF4444 : 0xFF00FF66;
            shellOutput.add(new ConsoleLine("eth0 status: " + ethStatus, ethColor));
        } else if ("chat".equals(mainCmd)) {
            if (!hasModule) {
                shellOutput.add(new ConsoleLine("ERROR", 0xFFFF4444, "eth0: No internet module installed.", 0xFFFF7777));
                shellOutput.add(new ConsoleLine("Install an Internet Module into this terminal first.", 0xFFFF9944));
            } else if (!eth0Up) {
                shellOutput.add(new ConsoleLine("ERROR", 0xFFFF4444, "eth0: Interface is DOWN. Run 'ifup eth0' first.", 0xFFFF7777));
            } else {
                stage = Stage.CHAT_MENU;
                chatMenuIndex = 0;
                subMenu = ChatSubMenu.MAIN;
            }
        } else if ("ifup".equals(mainCmd) || "eth0 up".equals(cmd.toLowerCase())) {
            if (!hasModule) {
                shellOutput.add(new ConsoleLine("ERROR", 0xFFFF4444, "eth0: No hardware module detected.", 0xFFFF7777));
                shellOutput.add(new ConsoleLine("Install an Internet Module to enable networking.", 0xFFFF9944));
            } else {
                eth0Up = true;
                shellOutput.add(new ConsoleLine("OK", 0xFF00FF66, "Interface eth0: link UP (1000Mbps)", 0xFF00FF66));
                shellOutput.add(new ConsoleLine("eth0: IPv4 10.0.0.42/24 DHCP ACK", 0xFF44FFAA));
            }
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

    public int getChatMenuIndex() {
        return chatMenuIndex;
    }

    public ChatSubMenu getSubMenu() {
        return subMenu;
    }

    public boolean isChatInGroups() {
        return subMenu == ChatSubMenu.GROUPS;
    }

    public int getPrivateMenuIndex() {
        return privateMenuIndex;
    }

    public int getGroupsMenuIndex() {
        return groupsMenuIndex;
    }

    public List<ConsoleLine> getShellOutput() {
        return shellOutput;
    }

    public String getCurrentChatRoom() {
        return currentChatRoom;
    }

    public List<ConsoleLine> getChatMessages() {
        return chatMessages;
    }
}
