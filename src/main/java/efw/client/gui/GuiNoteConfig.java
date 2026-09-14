package efw.client.gui;

import com.voltyx.mwccf.MwccfMod;
import efw.config.NotesConfig;
import efw.init.EfwModItems;
import efw.item.NoteItem;
import efw.network.PacketConfigureNote;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiNoteConfig extends GuiScreen {

    private final EntityPlayer player;
    private final EnumHand hand;
    private final ItemStack heldStack;

    private boolean isQuest;
    private int noteId;
    private int variant;

    // GUI Controls
    private GuiTextField idInputField;
    private static final int BTN_TOGGLE_TYPE = 1;
    private static final int BTN_ID_PREV_10 = 2;
    private static final int BTN_ID_PREV_1  = 3;
    private static final int BTN_ID_NEXT_1  = 4;
    private static final int BTN_ID_NEXT_10 = 5;

    private static final int BTN_VAR_PREV = 6;
    private static final int BTN_VAR_NEXT = 7;

    private static final int BTN_APPLY = 8;
    private static final int BTN_CANCEL = 9;

    // 3D Preview Rotation
    private float rotX = -10.0f;
    private float rotY = -160.0f;
    private float zoom = 140.0f;
    private int prevMouseX, prevMouseY;
    private boolean isDraggingPreview = false;

    // Preview Text scroll
    private double textScroll = 0.0;

    public GuiNoteConfig(EntityPlayer player, EnumHand hand) {
        this.player = player;
        this.hand = hand;
        this.heldStack = player.getHeldItem(hand);

        this.noteId = NoteItem.getNoteId(heldStack);
        this.variant = NoteItem.getVariant(heldStack);
        this.isQuest = NoteItem.isQuest(heldStack);

        if (this.variant < 1 || this.variant > 10) {
            this.variant = 1;
        }
        if (this.noteId < 1) {
            this.noteId = 1;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        int panelW = 340;
        int panelH = 220;
        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        // Type toggle button (Обычная / Квестовая)
        String typeLabel = isQuest ? (TextFormatting.GOLD + "Тип: Квестовая записка") : (TextFormatting.AQUA + "Тип: Обычная записка");
        this.buttonList.add(new GuiButton(BTN_TOGGLE_TYPE, panelX + 16, panelY + 30, 160, 20, typeLabel));

        // Note ID navigation buttons & text field
        int idY = panelY + 70;
        this.buttonList.add(new GuiButton(BTN_ID_PREV_10, panelX + 16, idY, 28, 20, "-10"));
        this.buttonList.add(new GuiButton(BTN_ID_PREV_1, panelX + 46, idY, 22, 20, "-1"));

        idInputField = new GuiTextField(100, this.fontRenderer, panelX + 72, idY + 1, 48, 18);
        idInputField.setMaxStringLength(6);
        idInputField.setText(String.valueOf(this.noteId));
        idInputField.setFocused(false);

        this.buttonList.add(new GuiButton(BTN_ID_NEXT_1, panelX + 124, idY, 22, 20, "+1"));
        this.buttonList.add(new GuiButton(BTN_ID_NEXT_10, panelX + 148, idY, 28, 20, "+10"));

        // Variant navigation buttons
        int varY = panelY + 115;
        this.buttonList.add(new GuiButton(BTN_VAR_PREV, panelX + 16, varY, 26, 20, "<"));
        this.buttonList.add(new GuiButton(BTN_VAR_NEXT, panelX + 150, varY, 26, 20, ">"));

        // Bottom Action buttons
        this.buttonList.add(new GuiButton(BTN_APPLY, panelX + 16, panelY + 185, 90, 20, TextFormatting.GREEN + "Применить"));
        this.buttonList.add(new GuiButton(BTN_CANCEL, panelX + 112, panelY + 185, 64, 20, TextFormatting.GRAY + "Закрыть"));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (idInputField != null) {
            idInputField.updateCursorCounter();
        }
    }

    private void updateIdFromField() {
        try {
            int parsed = Integer.parseInt(idInputField.getText().trim());
            if (parsed > 0) {
                this.noteId = parsed;
            }
        } catch (NumberFormatException ignored) {}
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        updateIdFromField();

        switch (button.id) {
            case BTN_TOGGLE_TYPE:
                this.isQuest = !this.isQuest;
                button.displayString = isQuest ? (TextFormatting.GOLD + "Тип: Квестовая записка") : (TextFormatting.AQUA + "Тип: Обычная записка");
                textScroll = 0.0;
                break;

            case BTN_ID_PREV_10:
                this.noteId = Math.max(1, this.noteId - 10);
                idInputField.setText(String.valueOf(this.noteId));
                textScroll = 0.0;
                break;

            case BTN_ID_PREV_1:
                this.noteId = Math.max(1, this.noteId - 1);
                idInputField.setText(String.valueOf(this.noteId));
                textScroll = 0.0;
                break;

            case BTN_ID_NEXT_1:
                this.noteId++;
                idInputField.setText(String.valueOf(this.noteId));
                textScroll = 0.0;
                break;

            case BTN_ID_NEXT_10:
                this.noteId += 10;
                idInputField.setText(String.valueOf(this.noteId));
                textScroll = 0.0;
                break;

            case BTN_VAR_PREV:
                this.variant--;
                if (this.variant < 1) this.variant = 10;
                break;

            case BTN_VAR_NEXT:
                this.variant++;
                if (this.variant > 10) this.variant = 1;
                break;

            case BTN_APPLY:
                applyChanges();
                this.mc.displayGuiScreen(null);
                break;

            case BTN_CANCEL:
                this.mc.displayGuiScreen(null);
                break;
        }
    }

    private void applyChanges() {
        updateIdFromField();
        MwccfMod.PACKET_HANDLER.sendToServer(new PacketConfigureNote(hand, noteId, variant, isQuest));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (idInputField != null && idInputField.isFocused()) {
            if (Character.isDigit(typedChar) || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE
                    || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT) {
                idInputField.textboxKeyTyped(typedChar, keyCode);
                updateIdFromField();
                return;
            } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                idInputField.setFocused(false);
                updateIdFromField();
                return;
            }
        }

        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            int panelW = 340;
            int panelH = 220;
            int panelX = (this.width - panelW) / 2;
            int panelY = (this.height - panelH) / 2;
            int previewBoxX = panelX + 188;
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;

            if (mouseX >= previewBoxX) {
                textScroll -= (delta > 0 ? 12.0 : -12.0);
                if (textScroll < 0) textScroll = 0;
            } else {
                zoom += (delta > 0 ? 15.0f : -15.0f);
                zoom = Math.max(60.0f, Math.min(260.0f, zoom));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (idInputField != null) {
            idInputField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        int panelW = 340;
        int panelH = 220;
        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        // Check if clicked in 3D preview zone (below variant buttons)
        if (mouseX >= panelX + 16 && mouseX <= panelX + 176 && mouseY >= panelY + 138 && mouseY <= panelY + 180) {
            isDraggingPreview = true;
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDraggingPreview = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (isDraggingPreview) {
            int dx = mouseX - prevMouseX;
            int dy = mouseY - prevMouseY;
            rotY += dx * 1.0f;
            rotX += dy * 1.0f;
            rotX = Math.max(-75f, Math.min(75f, rotX));
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int panelW = 340;
        int panelH = 220;
        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        // Main background panel
        drawRect(panelX, panelY, panelX + panelW, panelY + panelH, 0xEE121418);
        drawRect(panelX + 2, panelY + 2, panelX + panelW - 2, panelY + panelH - 2, 0xFF1C2026);

        // Header
        drawRect(panelX + 2, panelY + 2, panelX + panelW - 2, panelY + 24, 0xFF242B35);
        this.fontRenderer.drawStringWithShadow(TextFormatting.YELLOW + "Настройка записки (Креатив)", panelX + 12, panelY + 8, 0xFFFFFF);

        // Section labels
        this.fontRenderer.drawString("Номер текста (ID):", panelX + 16, panelY + 58, 0xAAAAAA);
        this.fontRenderer.drawString("Текстура (Вариант):", panelX + 16, panelY + 102, 0xAAAAAA);

        // Variant text label between buttons
        String varStr = "Вариант " + this.variant + " / 10";
        int varStrW = this.fontRenderer.getStringWidth(varStr);
        this.fontRenderer.drawString(varStr, panelX + 96 - varStrW / 2, panelY + 121, 0xFFFFFF);

        // Text field for ID
        if (idInputField != null) {
            idInputField.drawTextBox();
        }

        // Preview Area Box on the Right
        int previewBoxX = panelX + 188;
        int previewBoxY = panelY + 30;
        int previewBoxW = 136;
        int previewBoxH = 175;

        drawRect(previewBoxX, previewBoxY, previewBoxX + previewBoxW, previewBoxY + previewBoxH, 0xFF121418);
        drawRect(previewBoxX + 1, previewBoxY + 1, previewBoxX + previewBoxW - 1, previewBoxY + previewBoxH - 1, 0xFF181B20);

        this.fontRenderer.drawString(TextFormatting.GRAY + "Предпросмотр текста:", previewBoxX + 6, previewBoxY + 6, 0xCCCCCC);

        // Get text to preview
        String textToPreview = NotesConfig.getText(this.noteId, this.isQuest);
        int textMaxW = previewBoxW - 14;
        List<String> lines = this.fontRenderer.listFormattedStringToWidth(textToPreview, textMaxW);

        int textVisibleH = previewBoxH - 24;
        int totalTextH = lines.size() * this.fontRenderer.FONT_HEIGHT;

        if (textScroll > Math.max(0, totalTextH - textVisibleH)) {
            textScroll = Math.max(0, totalTextH - textVisibleH);
        }

        // Clip text preview
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scale = this.mc.displayHeight / (double) this.height;
        int scissorX = (int) (previewBoxX * scale);
        int scissorY = (int) (this.mc.displayHeight - (previewBoxY + previewBoxH - 6) * scale);
        int scissorW = (int) (previewBoxW * scale);
        int scissorH = (int) (textVisibleH * scale);
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);

        int lineY = previewBoxY + 20 - (int) textScroll;
        for (String line : lines) {
            this.fontRenderer.drawString(line, previewBoxX + 6, lineY, 0xEEEEEE);
            lineY += this.fontRenderer.FONT_HEIGHT;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // Render mini 3D model of the selected note texture
        renderNoteModel(panelX + 96, panelY + 158);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderNoteModel(int x, int y) {
        ItemStack previewStack = new ItemStack(EfwModItems.NOTE);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagCompound sub = new NBTTagCompound();
        sub.setInteger("noteId", this.noteId);
        sub.setInteger("variant", this.variant);
        sub.setBoolean("isQuest", this.isQuest);
        root.setTag("efw_note", sub);
        previewStack.setTagCompound(root);

        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 100f);
        GlStateManager.scale(zoom, -zoom, zoom);
        GlStateManager.rotate(rotX, 1f, 0f, 0f);
        GlStateManager.rotate(rotY, 0f, 1f, 0f);

        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(previewStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        GlStateManager.disableDepth();
    }
}