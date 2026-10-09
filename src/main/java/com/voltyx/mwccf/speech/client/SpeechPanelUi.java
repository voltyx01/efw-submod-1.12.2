package com.voltyx.mwccf.speech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

@net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
public final class SpeechPanelUi {
    public static final int BUTTON_SIZE = 20;
    private static final int PANEL_WIDTH = 220;
    private static final int PADDING = 7;
    private static final int LINE_HEIGHT = 11;

    public static final class Layout {
        public final int x;
        public final int y;
        public final int width;
        public final int height;
        public final int historyY;
        public final int inputY;

        private Layout(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.historyY = y + 23;
            this.inputY = y + height - 23;
        }
    }

    private static final class DisplayRow {
        final String text;
        final int color;

        DisplayRow(String text, int color) {
            this.text = text;
            this.color = color;
        }
    }

    private SpeechPanelUi() {}

    public static Layout inventoryLayout(int screenWidth, int guiLeft, int guiTop, int guiHeight) {
        int width = Math.max(40, Math.min(PANEL_WIDTH, guiLeft - BUTTON_SIZE - 16));
        int x = Math.max(4, guiLeft - width - BUTTON_SIZE - 12);
        int y = Math.max(4, guiTop);
        int height = Math.max(100, Math.min(guiHeight, 230));
        return new Layout(x, y, width, height);
    }

    public static Layout screenLayout(int screenWidth, int screenHeight) {
        int width = Math.min(PANEL_WIDTH, screenWidth - 16);
        int height = Math.min(230, screenHeight - 24);
        return new Layout(8, Math.max(8, (screenHeight - height) / 2), width, height);
    }

    public static void drawInventoryButton(int mouseX, int mouseY, int screenWidth, int guiLeft, int guiTop) {
        int x = Math.max(2, guiLeft - BUTTON_SIZE - 4);
        int y = guiTop + 4;
        GuiButton button = new GuiButton(6201, x, y, BUTTON_SIZE, BUTTON_SIZE,
            net.minecraft.client.resources.I18n.format("gui.mwccf.speech.button"));
        button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0F);
    }

    public static void drawInventoryPanel(int mouseX, int mouseY, int screenWidth,
                                          int guiLeft, int guiTop, int guiHeight) {
        drawInventoryButton(mouseX, mouseY, screenWidth, guiLeft, guiTop);
        if (!SpeechPanelController.isInventoryPanelOpen()) return;
        Layout layout = inventoryLayout(screenWidth, guiLeft, guiTop, guiHeight);
        float progress = SpeechPanelController.getInventoryOpenProgress();
        float eased = 1.0F - (1.0F - progress) * (1.0F - progress);
        drawPanel(layout, mouseX, mouseY, -Math.round(layout.width * (1.0F - eased)));
    }

    public static void drawStandalonePanel(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        drawPanel(screenLayout(screenWidth, screenHeight), mouseX, mouseY);
    }

    public static void drawStandalonePanel(int mouseX, int mouseY, int screenWidth, int screenHeight,
                                           int slideOffset, float alpha) {
        Layout layout = screenLayout(screenWidth, screenHeight);
        drawPanel(layout, mouseX, mouseY, slideOffset);
    }

    private static void drawPanel(Layout layout, int mouseX, int mouseY) {
        drawPanel(layout, mouseX, mouseY, 0);
    }

    private static void drawPanel(Layout layout, int mouseX, int mouseY, int slideOffset) {
        net.minecraft.client.renderer.GlStateManager.pushMatrix();
        net.minecraft.client.renderer.GlStateManager.translate(slideOffset, 0.0F, 0.0F);
        drawPanelContent(layout, mouseX - slideOffset, mouseY);
        net.minecraft.client.renderer.GlStateManager.popMatrix();
    }

    private static void drawPanelContent(Layout layout, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getMinecraft();
        Gui.drawRect(layout.x, layout.y, layout.x + layout.width, layout.y + layout.height, 0xE8141A20);
        Gui.drawRect(layout.x, layout.y, layout.x + layout.width, layout.y + 1, 0xFFE0B84F);
        mc.fontRenderer.drawStringWithShadow(
                net.minecraft.client.resources.I18n.format("gui.mwccf.speech.history"),
                layout.x + PADDING, layout.y + 7, 0xFFFFD66B);

        int historyBottom = layout.inputY - 3;
        Gui.drawRect(layout.x + 3, layout.historyY, layout.x + layout.width - 3, historyBottom, 0x7020272E);
        List<DisplayRow> rows = flattenHistory(SpeechClientManager.getHistory(), layout.width - PADDING * 2 - 5);
        int visibleRows = Math.max(1, (historyBottom - layout.historyY - 4) / LINE_HEIGHT);
        int maxScroll = Math.max(0, rows.size() - visibleRows);
        int scroll = Math.min(maxScroll, SpeechPanelController.getScrollOffset());
        int end = Math.max(0, rows.size() - scroll);
        int start = Math.max(0, end - visibleRows);
        int textY = historyBottom - 4 - (end - start) * LINE_HEIGHT;
        for (int i = start; i < end; i++) {
            DisplayRow row = rows.get(i);
            mc.fontRenderer.drawString(row.text, layout.x + PADDING, textY, row.color, false);
            textY += LINE_HEIGHT;
        }
        if (rows.isEmpty()) {
            mc.fontRenderer.drawStringWithShadow(
                    net.minecraft.client.resources.I18n.format("gui.mwccf.speech.empty"),
                    layout.x + PADDING, layout.historyY + 7, 0xFFB7BEC7);
        }

        int trackX = layout.x + layout.width - 4;
        Gui.drawRect(trackX, layout.historyY + 2, trackX + 2, historyBottom - 2, 0x704A555F);
        if (rows.size() > visibleRows) {
            int trackHeight = historyBottom - layout.historyY - 4;
            int thumbHeight = Math.max(12, trackHeight * visibleRows / rows.size());
            int thumbTravel = trackHeight - thumbHeight;
            int thumbY = layout.historyY + 2 + (maxScroll == 0 ? 0 : thumbTravel * (maxScroll - scroll) / maxScroll);
            Gui.drawRect(trackX, thumbY, trackX + 2, thumbY + thumbHeight, 0xFFE0B84F);
        }

        Gui.drawRect(layout.x + 3, layout.inputY, layout.x + layout.width - 3,
                layout.y + layout.height - 3, 0xFF0D1116);
        mc.fontRenderer.drawStringWithShadow(">", layout.x + PADDING, layout.inputY + 7, 0xFFFFD66B);
        if (SpeechPanelController.getInput().isEmpty() && !SpeechPanelController.isInputFocused()) {
            mc.fontRenderer.drawStringWithShadow(
                net.minecraft.client.resources.I18n.format("gui.mwccf.speech.input"),
                layout.x + PADDING + 10, layout.inputY + 7, 0xFF8D959E);
        }
        SpeechPanelController.drawInputField(layout.x + PADDING + 10, layout.inputY + 6,
            layout.width - PADDING * 2 - 17);
    }

    private static List<DisplayRow> flattenHistory(List<SpeechClientManager.HistoryMessage> messages, int maxWidth) {
        Minecraft mc = Minecraft.getMinecraft();
        List<DisplayRow> rows = new ArrayList<>();
        for (SpeechClientManager.HistoryMessage message : messages) {
            String prefix = TextFormatting.GOLD + message.speakerName + TextFormatting.GRAY + ": ";
            int prefixWidth = mc.fontRenderer.getStringWidth(prefix);
            String available = message.text;
            List<String> messageLines = mc.fontRenderer.listFormattedStringToWidth(available,
                    Math.max(20, maxWidth - Math.min(prefixWidth, maxWidth / 2)));
            if (messageLines.isEmpty()) messageLines.add("");
            rows.add(new DisplayRow(prefix + messageLines.get(0), 0xFFFFFFFF));
            for (int i = 1; i < messageLines.size(); i++) {
                rows.add(new DisplayRow("  " + messageLines.get(i), 0xFFDDDDDD));
            }
        }
        return rows;
    }

    public static boolean hitScrollbar(Layout layout, int mouseX, int mouseY) {
        int historyBottom = layout.inputY - 3;
        int trackX = layout.x + layout.width - 6;
        return mouseX >= trackX && mouseX <= trackX + 5
                && mouseY >= layout.historyY && mouseY < historyBottom;
    }

    public static void scrollTo(Layout layout, int mouseY) {
        List<DisplayRow> rows = flattenHistory(SpeechClientManager.getHistory(), layout.width - PADDING * 2 - 5);
        int historyBottom = layout.inputY - 3;
        int visibleRows = Math.max(1, (historyBottom - layout.historyY - 4) / LINE_HEIGHT);
        int maxScroll = Math.max(0, rows.size() - visibleRows);
        int trackTop = layout.historyY + 2;
        int trackHeight = historyBottom - layout.historyY - 4;
        int thumbHeight = rows.size() <= visibleRows ? trackHeight
                : Math.max(12, trackHeight * visibleRows / rows.size());
        int travel = Math.max(1, trackHeight - thumbHeight);
        float fraction = Math.max(0.0F, Math.min(1.0F,
                (mouseY - trackTop - thumbHeight / 2.0F) / travel));
        SpeechPanelController.setScrollOffset(Math.round((1.0F - fraction) * maxScroll));
    }
}
