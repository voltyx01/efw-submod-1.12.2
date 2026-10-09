package com.voltyx.mwccf.speech.client;

import com.voltyx.mwccf.speech.SpeechConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

@net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
public final class SpeechPanelController {
    private static boolean inventoryPanelOpen;
    private static boolean inputFocused = true;
    private static boolean draggingScrollbar;
    private static long inventoryOpenAt;
    private static long inventoryClosingAt;
    private static GuiTextField inputField;
    private static int scrollOffset;
    private static int historyIndex = -1;
    private static String historyDraft = "";
    private static boolean awaitingSuggestions;
    private static final List<String> suggestions = new ArrayList<>();
    private static int suggestionIndex = -1;

    private SpeechPanelController() {}

    public static boolean isInventoryPanelOpen() {
        if (inventoryPanelOpen) return true;
        if (inventoryClosingAt > 0L) {
            if (System.currentTimeMillis() - inventoryClosingAt < 160L) {
                return true;
            } else {
                inventoryClosingAt = 0L;
            }
        }
        return false;
    }

    public static boolean isInventoryPanelActive() {
        return inventoryPanelOpen;
    }

    public static String getInput() {
        return getInputField().getText();
    }

    public static void setInitialText(String text) {
        GuiTextField field = getInputField();
        field.setText(text == null ? "" : text);
        field.setCursorPositionEnd();
        inputFocused = true;
        field.setFocused(true);
    }

    private static GuiTextField getInputField() {
        if (inputField == null) {
            Minecraft mc = Minecraft.getMinecraft();
            inputField = new GuiTextField(6202, mc.fontRenderer, 0, 0, 100, 12);
            inputField.setMaxStringLength(SpeechConfig.maxMessageLength);
            inputField.setEnableBackgroundDrawing(false);
            inputField.setTextColor(0xFFFFFF);
            inputField.setCanLoseFocus(false);
        }
        inputField.setMaxStringLength(SpeechConfig.maxMessageLength);
        inputField.setFocused(inputFocused);
        return inputField;
    }

    public static void drawInputField(int x, int y, int width) {
        GuiTextField field = getInputField();
        field.x = x;
        field.y = y;
        field.width = width;
        field.drawTextBox();
    }

    public static int getScrollOffset() {
        return scrollOffset;
    }

    public static void setScrollOffset(int value) {
        int max = SpeechClientManager.getHistory().size()
            * Math.max(1, SpeechConfig.maxMessageLength / 10);
        scrollOffset = Math.max(0, Math.min(max, value));
    }

    public static void openFromChatKey() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiContainer) {
            if (inventoryPanelOpen) {
                closeInventoryPanel();
            } else {
                openOrFocusInventoryPanel();
            }
        } else if (mc.currentScreen instanceof SpeechPanelScreen) {
            mc.displayGuiScreen(null);
        } else if (mc.currentScreen == null) {
            inputFocused = true;
            mc.displayGuiScreen(new SpeechPanelScreen());
        }
    }

    public static void toggleInventoryPanel() {
        if (inventoryPanelOpen) {
            closeInventoryPanel();
        } else {
            openOrFocusInventoryPanel();
        }
    }

    public static void openOrFocusInventoryPanel() {
        openOrFocusInventoryPanel("");
    }

    public static void openOrFocusInventoryPanel(String initialText) {
        inventoryPanelOpen = true;
        inventoryClosingAt = 0L;
        inventoryOpenAt = System.currentTimeMillis();
        org.lwjgl.input.Keyboard.enableRepeatEvents(true);
        if (initialText != null && !initialText.isEmpty()) {
            setInitialText(initialText);
        } else {
            inputFocused = true;
            getInputField().setFocused(true);
        }
    }

    public static void closeInventoryPanel() {
        if (inventoryPanelOpen) {
            inventoryPanelOpen = false;
            inventoryClosingAt = System.currentTimeMillis();
            inputFocused = false;
            org.lwjgl.input.Keyboard.enableRepeatEvents(false);
            clearInput();
        }
    }

    public static void resetInventoryState() {
        inventoryPanelOpen = false;
        inventoryClosingAt = 0L;
        inputFocused = false;
        draggingScrollbar = false;
        org.lwjgl.input.Keyboard.enableRepeatEvents(false);
        clearInput();
    }

    public static float getInventoryOpenProgress() {
        long now = System.currentTimeMillis();
        if (inventoryClosingAt > 0L) {
            long elapsed = now - inventoryClosingAt;
            if (elapsed >= 160L) {
                inventoryClosingAt = 0L;
                return 0.0F;
            }
            return Math.max(0.0F, 1.0F - elapsed / 160.0F);
        }
        if (!inventoryPanelOpen) return 0.0F;
        return Math.max(0.0F, Math.min(1.0F, (now - inventoryOpenAt) / 180.0F));
    }

    public static boolean handleInventoryMouseClick(int mouseX, int mouseY, int button,
                                                    int screenWidth, int guiLeft, int guiTop, int guiHeight) {
        SpeechPanelUi.Layout layout = SpeechPanelUi.inventoryLayout(screenWidth, guiLeft, guiTop, guiHeight);
        int buttonX = Math.max(2, guiLeft - SpeechPanelUi.BUTTON_SIZE - 4);
        int buttonY = guiTop + 4;
        if (mouseX >= buttonX && mouseX < buttonX + SpeechPanelUi.BUTTON_SIZE
                && mouseY >= buttonY && mouseY < buttonY + SpeechPanelUi.BUTTON_SIZE) {
            toggleInventoryPanel();
            return true;
        }
        if (!inventoryPanelOpen) return false;
        if (button == 0 && SpeechPanelUi.hitScrollbar(layout, mouseX, mouseY)) {
            draggingScrollbar = true;
            SpeechPanelUi.scrollTo(layout, mouseY);
            return true;
        }
        if (mouseX >= layout.x && mouseX <= layout.x + layout.width
                && mouseY >= layout.y && mouseY <= layout.y + layout.height) {
            inputFocused = mouseY >= layout.inputY;
            if (inputFocused) getInputField().mouseClicked(mouseX, mouseY, button);
            getInputField().setFocused(inputFocused);
            return true;
        }
        return false;
    }

    public static boolean handleMouseWheel(int mouseX, int mouseY, int wheel,
                                          int screenWidth, int guiLeft, int guiTop, int guiHeight) {
        if (!inventoryPanelOpen || wheel == 0) return false;
        SpeechPanelUi.Layout layout = SpeechPanelUi.inventoryLayout(screenWidth, guiLeft, guiTop, guiHeight);
        if (mouseX < layout.x || mouseX > layout.x + layout.width
                || mouseY < layout.historyY || mouseY >= layout.inputY) return false;
        setScrollOffset(scrollOffset + (wheel > 0 ? 3 : -3));
        return true;
    }

    public static void handleInventoryMouseDrag(int mouseX, int mouseY,
                                                int screenWidth, int guiLeft, int guiTop, int guiHeight) {
        if (!draggingScrollbar || !inventoryPanelOpen) return;
        SpeechPanelUi.scrollTo(SpeechPanelUi.inventoryLayout(screenWidth, guiLeft, guiTop, guiHeight), mouseY);
    }

    public static void stopDraggingScrollbar() {
        draggingScrollbar = false;
    }

    public static boolean handleKey(char typedChar, int keyCode) {
        if (keyCode == 1) {
            if (Minecraft.getMinecraft().currentScreen instanceof SpeechPanelScreen) {
                ((SpeechPanelScreen) Minecraft.getMinecraft().currentScreen).startClosing();
            } else {
                closeInventoryPanel();
            }
            return true;
        }
        if (!inputFocused) return false;
        if (keyCode == 28 || keyCode == 156) {
            send();
            return true;
        }
        if (keyCode == org.lwjgl.input.Keyboard.KEY_UP) {
            navigateSentHistory(-1);
            return true;
        }
        if (keyCode == org.lwjgl.input.Keyboard.KEY_DOWN) {
            navigateSentHistory(1);
            return true;
        }
        if (keyCode == org.lwjgl.input.Keyboard.KEY_TAB) {
            completeInput();
            return true;
        }
        boolean handled = getInputField().textboxKeyTyped(typedChar, keyCode);
        if (handled) {
            suggestions.clear();
            suggestionIndex = -1;
        }
        return handled;
    }

    public static void clearInput() {
        getInputField().setText("");
        historyIndex = -1;
        historyDraft = "";
        suggestions.clear();
        suggestionIndex = -1;
    }

    private static void send() {
        Minecraft mc = Minecraft.getMinecraft();
        String message = getInputField().getText().trim();
        boolean isCommand = message.startsWith("/");
        if (mc.player != null && !message.isEmpty()) {
            mc.ingameGUI.getChatGUI().addToSentMessages(message);
            mc.player.sendChatMessage(message);
        }
        clearInput();
        scrollOffset = 0;
        inputFocused = true;
        getInputField().setFocused(true);

        if (isCommand && mc.currentScreen instanceof SpeechPanelScreen) {
            ((SpeechPanelScreen) mc.currentScreen).startClosing();
        }
    }

    private static void navigateSentHistory(int direction) {
        Minecraft mc = Minecraft.getMinecraft();
        List<String> history = mc.ingameGUI.getChatGUI().getSentMessages();
        if (history.isEmpty()) return;
        if (historyIndex < 0) {
            historyDraft = getInputField().getText();
            historyIndex = history.size();
        }
        historyIndex = Math.max(0, Math.min(history.size(), historyIndex + direction));
        getInputField().setText(historyIndex == history.size() ? historyDraft : history.get(historyIndex));
        getInputField().setCursorPositionEnd();
    }

    private static void completeInput() {
        if (suggestionIndex >= 0 && !suggestions.isEmpty()) {
            suggestionIndex = (suggestionIndex + 1) % suggestions.size();
            setCompletedText(suggestions.get(suggestionIndex));
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.player.connection == null) return;
        awaitingSuggestions = true;
        mc.player.connection.sendPacket(new CPacketTabComplete(getInputField().getText(), (BlockPos) null, false));
    }

    public static void acceptSuggestions(String[] values) {
        if (!awaitingSuggestions) return;
        awaitingSuggestions = false;
        suggestions.clear();
        if (values != null) {
            for (String value : values) if (value != null && !value.isEmpty()) suggestions.add(value);
        }
        if (suggestions.isEmpty()) return;
        suggestionIndex = 0;
        setCompletedText(suggestions.get(0));
    }

    private static void setCompletedText(String value) {
        getInputField().setText(value);
        getInputField().setCursorPositionEnd();
    }

    public static boolean isInputFocused() {
        return inputFocused;
    }

    public static void blurInput() {
        inputFocused = false;
    }

    public static void focusInput() {
        inputFocused = true;
    }

    public static boolean handleStandaloneMouseClick(int mouseX, int mouseY, int button, int width, int height) {
        SpeechPanelUi.Layout layout = SpeechPanelUi.screenLayout(width, height);
        if (button == 0 && SpeechPanelUi.hitScrollbar(layout, mouseX, mouseY)) {
            draggingScrollbar = true;
            SpeechPanelUi.scrollTo(layout, mouseY);
            return true;
        }
        if (mouseX >= layout.x && mouseX <= layout.x + layout.width
                && mouseY >= layout.y && mouseY <= layout.y + layout.height) {
            inputFocused = mouseY >= layout.inputY;
            if (inputFocused) getInputField().mouseClicked(mouseX, mouseY, button);
            getInputField().setFocused(inputFocused);
            return true;
        }
        Minecraft.getMinecraft().displayGuiScreen(null);
        return true;
    }

    public static boolean handleStandaloneMouseWheel(int mouseX, int mouseY, int wheel, int width, int height) {
        if (wheel == 0) return false;
        SpeechPanelUi.Layout layout = SpeechPanelUi.screenLayout(width, height);
        if (mouseX < layout.x || mouseX > layout.x + layout.width
                || mouseY < layout.historyY || mouseY >= layout.inputY) return false;
        setScrollOffset(scrollOffset + (wheel > 0 ? 3 : -3));
        return true;
    }

    public static void handleStandaloneMouseDrag(int mouseY, int width, int height) {
        if (!draggingScrollbar) return;
        SpeechPanelUi.scrollTo(SpeechPanelUi.screenLayout(width, height), mouseY);
    }
}
