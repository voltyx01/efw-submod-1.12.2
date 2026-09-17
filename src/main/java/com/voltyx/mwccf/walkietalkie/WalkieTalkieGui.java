package com.voltyx.mwccf.walkietalkie;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Locale;

/**
 * Military Radio GUI matching the HTML design mockup (olive green case, backlit LCD screen,
 * frequency slider, volume slider, power & mic buttons).
 */
@SideOnly(Side.CLIENT)
public class WalkieTalkieGui extends GuiScreen {

    private final ItemStack stack;
    private final GuiScreen parentScreen;
    private final int guiWidth = 230;
    private final int guiHeight = 210;

    private int guiLeft;
    private int guiTop;

    private boolean active;
    private boolean muted;
    private int channel;
    private int volume;

    private boolean draggingFreq = false;
    private boolean draggingVol = false;

    private RadioCustomButton btnPower;
    private RadioCustomButton btnMic;
    private RadioCustomButton btnPrevCh;
    private RadioCustomButton btnNextCh;

    public WalkieTalkieGui(ItemStack stack) {
        this(stack, null);
    }

    public WalkieTalkieGui(ItemStack stack, GuiScreen parentScreen) {
        this.stack = stack;
        this.parentScreen = parentScreen;
        this.active = ItemWalkieTalkie.isActive(stack);
        this.muted = ItemWalkieTalkie.isMuted(stack);
        this.channel = ItemWalkieTalkie.getChannel(stack);
        this.volume = ItemWalkieTalkie.getVolume(stack);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || (this.mc.gameSettings != null && this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) || keyCode == org.lwjgl.input.Keyboard.KEY_X) {
            this.mc.displayGuiScreen(this.parentScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.guiWidth) / 2;
        this.guiTop = (this.height - this.guiHeight) / 2;
        this.buttonList.clear();

        int btnY = this.guiTop + 172;

        // Power button (ID 0)
        this.btnPower = new RadioCustomButton(0, this.guiLeft + 10, btnY, 28, 26, 0);
        this.btnPower.setState(this.active, this.muted);
        this.buttonList.add(this.btnPower);

        // Mic / Mute button (ID 1)
        this.btnMic = new RadioCustomButton(1, this.guiLeft + 44, btnY, 28, 26, 1);
        this.btnMic.setState(this.active, this.muted);
        this.buttonList.add(this.btnMic);

        // "<" Channel Down button (ID 2)
        this.btnPrevCh = new RadioCustomButton(2, this.guiLeft + this.guiWidth - 10 - 52, btnY, 24, 26, 2);
        this.buttonList.add(this.btnPrevCh);

        // ">" Channel Up button (ID 3)
        this.btnNextCh = new RadioCustomButton(3, this.guiLeft + this.guiWidth - 10 - 24, btnY, 24, 26, 3);
        this.buttonList.add(this.btnNextCh);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) return;

        switch (button.id) {
            case 0: // Power
                if (!this.active && !ItemWalkieTalkie.hasBattery(this.stack)) {
                    return;
                }
                this.active = !this.active;
                this.btnPower.setState(this.active, this.muted);
                this.btnMic.setState(this.active, this.muted);
                PacketToggleWalkieTalkie.sendToggle();
                break;
            case 1: // Mic / Mute
                this.muted = !this.muted;
                this.btnMic.setState(this.active, this.muted);
                PacketToggleWalkieTalkie.sendMuteToggle();
                break;
            case 2: // Channel Down
                this.channel = (this.channel <= 1) ? ItemWalkieTalkie.MAX_CHANNEL : this.channel - 1;
                PacketToggleWalkieTalkie.sendSetChannel(this.channel);
                break;
            case 3: // Channel Up
                this.channel = (this.channel >= ItemWalkieTalkie.MAX_CHANNEL) ? 1 : this.channel + 1;
                PacketToggleWalkieTalkie.sendSetChannel(this.channel);
                break;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            if (dWheel > 0) {
                this.channel = (this.channel >= ItemWalkieTalkie.MAX_CHANNEL) ? 1 : this.channel + 1;
            } else {
                this.channel = (this.channel <= 1) ? ItemWalkieTalkie.MAX_CHANNEL : this.channel - 1;
            }
            PacketToggleWalkieTalkie.sendSetChannel(this.channel);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            int trackX = this.guiLeft + 10;
            int trackW = 210;

            // Check Frequency Slider Track
            int fTrackY = this.guiTop + 97;
            if (mouseX >= trackX && mouseX <= trackX + trackW && mouseY >= fTrackY - 2 && mouseY <= fTrackY + 27) {
                this.draggingFreq = true;
                updateFreqFromMouse(mouseX, trackX, trackW);
            }

            // Check Volume Slider Track
            int vTrackY = this.guiTop + 137;
            if (mouseX >= trackX && mouseX <= trackX + trackW && mouseY >= vTrackY - 2 && mouseY <= vTrackY + 27) {
                this.draggingVol = true;
                updateVolFromMouse(mouseX, trackX, trackW);
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        int trackX = this.guiLeft + 10;
        int trackW = 210;
        if (this.draggingFreq) {
            updateFreqFromMouse(mouseX, trackX, trackW);
        }
        if (this.draggingVol) {
            updateVolFromMouse(mouseX, trackX, trackW);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.draggingFreq = false;
            this.draggingVol = false;
        }
    }

    private void updateFreqFromMouse(int mouseX, int trackX, int trackW) {
        float frac = Math.max(0.0F, Math.min(1.0F, (mouseX - (trackX + 6)) / (float) (trackW - 14)));
        int newCh = 1 + Math.round(frac * (ItemWalkieTalkie.MAX_CHANNEL - 1));
        if (newCh != this.channel) {
            this.channel = newCh;
            PacketToggleWalkieTalkie.sendSetChannel(this.channel);
        }
    }

    private void updateVolFromMouse(int mouseX, int trackX, int trackW) {
        float frac = Math.max(0.0F, Math.min(1.0F, (mouseX - (trackX + 6)) / (float) (trackW - 14)));
        int newVol = Math.round(frac * 100);
        if (newVol != this.volume) {
            this.volume = newVol;
            PacketToggleWalkieTalkie.sendSetVolume(this.volume);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // 1. Radio outer casing: #3a5a2f with 3D bevel (#5e8a4b / #1f3318) and inner shadow (#2a441f)
        draw3DBox(this.guiLeft, this.guiTop, this.guiWidth, this.guiHeight, 0xFF3A5A2F, 0xFF5E8A4B, 0xFF1F3318, 3);
        // Inner inset line (box-shadow)
        drawRect(this.guiLeft + 3, this.guiTop + 3, this.guiLeft + this.guiWidth - 3, this.guiTop + 5, 0xFF2A441F);
        drawRect(this.guiLeft + 3, this.guiTop + 3, this.guiLeft + 5, this.guiTop + this.guiHeight - 3, 0xFF2A441F);
        drawRect(this.guiLeft + 3, this.guiTop + this.guiHeight - 5, this.guiLeft + this.guiWidth - 3, this.guiTop + this.guiHeight - 3, 0xFF2A441F);
        drawRect(this.guiLeft + this.guiWidth - 5, this.guiTop + 3, this.guiLeft + this.guiWidth - 3, this.guiTop + this.guiHeight - 3, 0xFF2A441F);

        // 2. Header
        String title = I18n.format("gui.mwccf.walkie_talkie.title");
        this.fontRenderer.drawString(title, this.guiLeft + 12, this.guiTop + 10, 0xFFDFE8D5, true);

        // Battery info
        int charge = (this.stack.hasTagCompound() && this.stack.getTagCompound().hasKey("battery_charge"))
                ? this.stack.getTagCompound().getInteger("battery_charge") : 0;
        int pct = (int) ((charge / 48000f) * 100);
        String pctStr = pct + "%";
        int pctStrW = this.fontRenderer.getStringWidth(pctStr);
        int batteryX = this.guiLeft + this.guiWidth - 12 - pctStrW - 20;
        int batteryY = this.guiTop + 11;
        drawBatteryIcon(batteryX, batteryY, pct);
        this.fontRenderer.drawString(pctStr, batteryX + 20, batteryY, 0xFFDFE8D5, true);

        // 3. LCD Screen
        int screenX = this.guiLeft + 10;
        int screenY = this.guiTop + 24;
        int screenW = 210;
        int screenH = 56;
        int screenBg = this.active ? 0xFFA8D896 : 0xFF6D8565;
        draw3DBox(screenX, screenY, screenW, screenH, screenBg, 0xFF4A6B3A, 0xFFDFFFCE, 2);

        if (this.active) {
            // Line 1: Frequency title & Channel
            this.fontRenderer.drawString(I18n.format("gui.mwccf.walkie_talkie.freq_mhz"), screenX + 8, screenY + 6, 0xFF2D4A20);
            String chStr = String.format("CH-%02d", this.channel);
            this.fontRenderer.drawString(chStr, screenX + screenW - 8 - this.fontRenderer.getStringWidth(chStr), screenY + 6, 0xFF2D4A20);

            // Line 2: Big Frequency (2.0x scale)
            double mhz = 144.000 + (this.channel * 0.025);
            String freqStr = String.format(Locale.US, "%.3f", mhz);
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 1.0F);
            this.fontRenderer.drawString(freqStr, (screenX + 8) / 2.0F, (screenY + 18) / 2.0F, 0xFF1F3315, false);
            GlStateManager.popMatrix();

            // Line 3: Signal and TX/RX status
            String signalText = I18n.format("gui.mwccf.walkie_talkie.signal", "●●●○○");
            this.fontRenderer.drawString(signalText, screenX + 8, screenY + 42, 0xFF2D4A20);

            String statusStr = this.muted ? "MUTED" : "TX";
            this.fontRenderer.drawString(statusStr, screenX + screenW - 8 - this.fontRenderer.getStringWidth(statusStr), screenY + 42, 0xFF2D4A20);
        } else {
            // Powered off screen
            String offStr = "[ OFF ]";
            this.fontRenderer.drawString(offStr, screenX + (screenW - this.fontRenderer.getStringWidth(offStr)) / 2, screenY + 24, 0xFF3D5237);
        }

        // 4. Frequency Slider
        this.fontRenderer.drawString(I18n.format("gui.mwccf.walkie_talkie.tuning"), this.guiLeft + 12, this.guiTop + 86, 0xFFDFE8D5);
        int fTrackX = this.guiLeft + 10;
        int fTrackY = this.guiTop + 97;
        int fTrackW = 210;
        int fTrackH = 20;

        // Groove box
        drawRect(fTrackX, fTrackY, fTrackX + fTrackW, fTrackY + fTrackH, 0xFF2A441F);
        draw3DBox(fTrackX, fTrackY, fTrackW, fTrackH, 0x00000000, 0xFF1F3318, 0xFF1F3318, 1);

        // Rail line
        drawRect(fTrackX + 4, fTrackY + 8, fTrackX + fTrackW - 4, fTrackY + 12, 0xFF4A6B3A);

        // Pointer needle & bottom indicator
        float fFrac = (this.channel - 1) / (float) (ItemWalkieTalkie.MAX_CHANNEL - 1);
        int fThumbX = fTrackX + 6 + (int) (fFrac * (fTrackW - 14));
        drawRect(fThumbX, fTrackY + 2, fThumbX + 1, fTrackY + fTrackH - 1, 0xFFDFE8D5);
        drawSliderPointerBottom(fThumbX, fTrackY + fTrackH, 0xFFDFE8D5);

        // 5. Volume Slider
        String volStr = I18n.format("gui.mwccf.walkie_talkie.volume") + "  " + this.volume + "%";
        this.fontRenderer.drawString(volStr, this.guiLeft + 12, this.guiTop + 126, 0xFFDFE8D5);
        int vTrackX = this.guiLeft + 10;
        int vTrackY = this.guiTop + 137;
        int vTrackW = 210;
        int vTrackH = 20;

        // Groove box
        drawRect(vTrackX, vTrackY, vTrackX + vTrackW, vTrackY + vTrackH, 0xFF2A441F);
        draw3DBox(vTrackX, vTrackY, vTrackW, vTrackH, 0x00000000, 0xFF1F3318, 0xFF1F3318, 1);

        // Rail line
        drawRect(vTrackX + 4, vTrackY + 8, vTrackX + vTrackW - 4, vTrackY + 12, 0xFF4A6B3A);

        // Pointer needle & bottom indicator
        float vFrac = this.volume / 100.0F;
        int vThumbX = vTrackX + 6 + (int) (vFrac * (vTrackW - 14));
        drawRect(vThumbX, vTrackY + 2, vThumbX + 1, vTrackY + vTrackH - 1, 0xFFDFE8D5);
        drawSliderPointerBottom(vThumbX, vTrackY + vTrackH, 0xFFDFE8D5);

        // 6. Draw Buttons
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void draw3DBox(int x, int y, int w, int h, int bg, int topColor, int bottomColor, int borderWidth) {
        if ((bg & 0xFF000000) != 0) {
            drawRect(x, y, x + w, y + h, bg);
        }
        drawRect(x, y, x + w, y + borderWidth, topColor);
        drawRect(x, y, x + borderWidth, y + h, topColor);
        drawRect(x, y + h - borderWidth, x + w, y + h, bottomColor);
        drawRect(x + w - borderWidth, y, x + w, y + h, bottomColor);
    }

    private void drawSliderPointerBottom(int cx, int y, int color) {
        // Small compact triangle pointing UP from the bottom of the track:
        drawRect(cx, y + 1, cx + 1, y + 2, color);      // apex (1px)
        drawRect(cx - 1, y + 2, cx + 2, y + 3, color);  // 3px
        drawRect(cx - 2, y + 3, cx + 3, y + 4, color);  // 5px
        drawRect(cx - 2, y + 4, cx + 3, y + 6, color);  // base (5x2px)
    }

    private void drawBatteryIcon(int x, int y, int pct) {
        int color = 0xFFDFE8D5;
        // Outer frame 16x7
        drawRect(x, y, x + 16, y + 1, color);
        drawRect(x, y + 6, x + 16, y + 7, color);
        drawRect(x, y, x + 1, y + 7, color);
        drawRect(x + 15, y, x + 16, y + 7, color);
        // Positive terminal (nub) 2x3
        drawRect(x + 16, y + 2, x + 18, y + 5, color);

        // Fill bars
        int fillW = (int) Math.round((pct / 100.0) * 12);
        if (fillW > 0) {
            int fillColor = pct > 50 ? 0xFF44FF44 : (pct > 20 ? 0xFFFFFF33 : 0xFFFF4444);
            drawRect(x + 2, y + 2, x + 2 + fillW, y + 5, fillColor);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static class RadioCustomButton extends GuiButton {
        private final int iconType; // 0 = power, 1 = mic, 2 = left, 3 = right
        private boolean activeState;
        private boolean mutedState;

        public RadioCustomButton(int buttonId, int x, int y, int widthIn, int heightIn, int iconType) {
            super(buttonId, x, y, widthIn, heightIn, "");
            this.iconType = iconType;
        }

        public void setState(boolean activeState, boolean mutedState) {
            this.activeState = activeState;
            this.mutedState = mutedState;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (!this.visible) return;
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            int bg = this.hovered ? 0xFF547A44 : 0xFF4A6B3A;
            int topBorder = this.hovered ? 0xFF7EA86B : 0xFF6E9A5B;
            int botBorder = 0xFF1F3318;

            // Bevel box
            drawRect(this.x, this.y, this.x + this.width, this.y + this.height, bg);
            drawRect(this.x, this.y, this.x + this.width, this.y + 2, topBorder);
            drawRect(this.x, this.y, this.x + 2, this.y + this.height, topBorder);
            drawRect(this.x, this.y + this.height - 2, this.x + this.width, this.y + this.height, botBorder);
            drawRect(this.x + this.width - 2, this.y, this.x + this.width, this.y + this.height, botBorder);

            int cx = this.x + this.width / 2;
            int cy = this.y + this.height / 2;
            int color = 0xFFDFE8D5;

            if (this.iconType == 0) { // Power button
                drawPowerIcon(cx, cy, color);
                int ledColor = this.activeState ? 0xFF33FF33 : 0xFF882222;
                drawRect(this.x + this.width - 5, this.y + 3, this.x + this.width - 2, this.y + 6, ledColor);
            } else if (this.iconType == 1) { // Mic button
                drawMicIcon(cx, cy, color, this.mutedState);
                int ledColor = !this.mutedState ? 0xFF33FF33 : 0xFFFF3333;
                drawRect(this.x + this.width - 5, this.y + 3, this.x + this.width - 2, this.y + 6, ledColor);
            } else if (this.iconType == 2) { // "<"
                mc.fontRenderer.drawStringWithShadow("<", cx - mc.fontRenderer.getStringWidth("<") / 2, cy - 4, color);
            } else if (this.iconType == 3) { // ">"
                mc.fontRenderer.drawStringWithShadow(">", cx - mc.fontRenderer.getStringWidth(">") / 2, cy - 4, color);
            }
        }

        private void drawPowerIcon(int cx, int cy, int color) {
            // Power icon: circle arc + vertical top notch
            drawRect(cx - 1, cy - 5, cx + 1, cy - 1, color);
            drawRect(cx - 4, cy - 3, cx - 3, cy + 3, color);
            drawRect(cx + 3, cy - 3, cx + 4, cy + 3, color);
            drawRect(cx - 3, cy + 3, cx + 3, cy + 4, color);
        }

        private void drawMicIcon(int cx, int cy, int color, boolean muted) {
            // Capsule
            drawRect(cx - 2, cy - 5, cx + 2, cy + 1, color);
            // U-bracket
            drawRect(cx - 4, cy - 1, cx - 3, cy + 2, color);
            drawRect(cx + 3, cy - 1, cx + 4, cy + 2, color);
            drawRect(cx - 3, cy + 2, cx + 3, cy + 3, color);
            // Base stand
            drawRect(cx - 1, cy + 3, cx + 1, cy + 5, color);
            drawRect(cx - 3, cy + 5, cx + 3, cy + 6, color);
            if (muted) {
                // Red diagonal strike line
                for (int i = -4; i <= 4; i++) {
                    drawRect(cx + i, cy + i, cx + i + 1, cy + i + 1, 0xFFFF3333);
                }
            }
        }
    }
}
