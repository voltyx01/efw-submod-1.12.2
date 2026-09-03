package com.voltyx.mwccf.client.inspect;

import com.voltyx.mwccf.client.loading.LoadingScreenEntry;
import efw.config.NotesConfig;
import efw.init.EfwModItems;
import efw.item.CDiaryItem;
import efw.item.NoteItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiItemInspect extends GuiScreen {

    private final ItemStack targetStack;
    private final GuiScreen parentScreen;
    private final LoadingScreenEntry entry;
    private final ItemInspectConfig.InspectGroup group;

    private float currentYaw = 0.0f;
    private float targetYaw = 0.0f;
    private float currentPitch = 0.0f;
    private float targetPitch = 0.0f;
    private float currentZoom = 95.0f;
    private float targetZoom = 95.0f;

    // Smooth Pan offsets (dragged with RMB, smoothly snaps back on release)
    private float currentPanX = 0.0f;
    private float targetPanX = 0.0f;
    private float currentPanY = 0.0f;
    private float targetPanY = 0.0f;

    // Fade Transition (Blackout fade-in on open, fade-out on close)
    private float fadeProgress = 0.0f;
    private boolean isExiting = false;
    private static final float FADE_SPEED = 5.0f;

    private boolean isDragging = false;
    private boolean isPanning = false;
    private int prevMouseX;
    private int prevMouseY;
    private long lastFrameTime = 0;
    private final InspectDustManager dustManager = new InspectDustManager();

    // Text Scrolling (Max 7 visible lines)
    private static final int MAX_VISIBLE_LINES = 7;
    private double scrollAmount = 0.0;
    private double targetScrollAmount = 0.0;
    private boolean isScrollingWithMouse = false;

    // Diary pagination
    private NBTTagList storedNotes = null;
    private int currentDiaryPage = 0;
    private ItemStack activeRenderStack;

    private static final int TITLE_COLOR = 0xC8B86A;
    private static final int DIVIDER_COLOR = 0xFF3E3E3E;
    private static final int DESC_COLOR = 0xA0A098;
    private static final int LORE_COLOR = 0x666660;

    public GuiItemInspect(ItemStack targetStack, GuiScreen parentScreen) {
        this.targetStack = targetStack;
        this.activeRenderStack = targetStack;
        this.parentScreen = parentScreen;
        this.group = ItemInspectConfig.resolveGroup(targetStack);
        this.entry = ItemInspectDescConfig.getEntryForStack(targetStack);

        if (this.group == ItemInspectConfig.InspectGroup.DIARY) {
            this.storedNotes = CDiaryItem.getStoredNotes(targetStack);
            updateDiaryActiveNote();
        }

        ItemInspectConfig.GroupTransform cfg = ItemInspectConfig.getTransform(this.group);
        this.currentYaw = cfg.startYaw;
        this.targetYaw = cfg.startYaw;
        this.currentPitch = cfg.startPitch;
        this.targetPitch = cfg.startPitch;
        this.currentZoom = 95.0f;
        this.targetZoom = 95.0f;
        this.fadeProgress = 0.0f;
        this.isExiting = false;
    }

    private void updateDiaryActiveNote() {
        if (storedNotes != null && storedNotes.tagCount() > 0 && currentDiaryPage < storedNotes.tagCount()) {
            NBTTagCompound noteTag = storedNotes.getCompoundTagAt(currentDiaryPage);
            int noteId = noteTag.getInteger("noteId");
            int variant = noteTag.hasKey("variant") ? noteTag.getInteger("variant") : 1;

            ItemStack fakeNote = new ItemStack(EfwModItems.NOTE);
            NBTTagCompound sub = new NBTTagCompound();
            sub.setInteger("noteId", noteId);
            sub.setInteger("variant", variant);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("efw_note", sub);
            fakeNote.setTagCompound(tag);
            this.activeRenderStack = fakeNote;
        } else {
            this.activeRenderStack = targetStack;
        }
        this.scrollAmount = 0.0;
        this.targetScrollAmount = 0.0;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.lastFrameTime = System.currentTimeMillis();
        this.dustManager.init(this.width, this.height);
        this.buttonList.clear();

        if (efw.biomeinfo.MwccfConfig.itemInspect.enableInspectMaker) {
            this.buttonList.add(new net.minecraft.client.gui.GuiButton(999, this.width - 95, this.height - 25, 90, 20,
                    "Настройки [M]"));
        }
    }

    @Override
    protected void actionPerformed(net.minecraft.client.gui.GuiButton button) throws IOException {
        if (button.id == 999 && efw.biomeinfo.MwccfConfig.itemInspect.enableInspectMaker) {
            this.mc.displayGuiScreen(new GuiItemInspectMaker(this, this.targetStack));
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

            ItemInspectConfig.TextSettings ts = ItemInspectConfig.textSettings;
            int infoY = this.height + ts.textOffsetY;

            if (mouseY >= infoY - 20) {
                // Scroll text smoothly
                targetScrollAmount -= (dWheel > 0 ? 20.0 : -20.0);
            } else {
                // Zoom item 3D
                if (dWheel > 0) {
                    targetZoom = Math.min(260.0f, targetZoom + 14.0f);
                } else {
                    targetZoom = Math.max(35.0f, targetZoom - 14.0f);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Check diary page arrows click
        if (this.group == ItemInspectConfig.InspectGroup.DIARY && storedNotes != null && storedNotes.tagCount() > 1
                && mouseButton == 0) {
            int midY = this.height / 2;
            int leftArrowX = 30;
            int rightArrowX = this.width - 30;

            // Hitbox for left arrow
            if (currentDiaryPage > 0 && Math.abs(mouseX - leftArrowX) <= 20 && Math.abs(mouseY - midY) <= 30) {
                currentDiaryPage--;
                updateDiaryActiveNote();
                if (this.mc.player != null) {
                    this.mc.player.playSound(efw.init.EfwModSounds.NOTES, 1.0f, 1.0f);
                }
                return;
            }

            // Hitbox for right arrow
            if (currentDiaryPage < storedNotes.tagCount() - 1 && Math.abs(mouseX - rightArrowX) <= 20
                    && Math.abs(mouseY - midY) <= 30) {
                currentDiaryPage++;
                updateDiaryActiveNote();
                if (this.mc.player != null) {
                    this.mc.player.playSound(efw.init.EfwModSounds.NOTES, 1.0f, 1.0f);
                }
                return;
            }
        }

        if (mouseButton == 0) {
            isDragging = true;
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        } else if (mouseButton == 1) {
            isPanning = true;
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            isDragging = false;
        } else if (state == 1) {
            isPanning = false;
            targetPanX = 0.0f;
            targetPanY = 0.0f;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        int dx = mouseX - prevMouseX;
        int dy = mouseY - prevMouseY;

        if (isDragging && clickedMouseButton == 0) {
            targetYaw += dx * 0.8f;
            targetPitch += dy * 0.8f;
            targetPitch = Math.max(-85.0f, Math.min(85.0f, targetPitch));
        } else if (isPanning && clickedMouseButton == 1) {
            targetPanX += dx;
            targetPanY += dy;
        }

        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_I
                || keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            InspectTransitionHandler.startTransitionToScreen(this.parentScreen, this);
            return;
        }
        if (keyCode == Keyboard.KEY_M && efw.biomeinfo.MwccfConfig.itemInspect.enableInspectMaker) {
            this.mc.displayGuiScreen(new GuiItemInspectMaker(this, this.targetStack));
            return;
        }

        // Left / Right arrow navigation for Diary
        if (this.group == ItemInspectConfig.InspectGroup.DIARY && storedNotes != null && storedNotes.tagCount() > 1) {
            if (keyCode == Keyboard.KEY_LEFT && currentDiaryPage > 0) {
                currentDiaryPage--;
                updateDiaryActiveNote();
                if (this.mc.player != null) {
                    this.mc.player.playSound(efw.init.EfwModSounds.NOTES, 1.0f, 1.0f);
                }
                return;
            } else if (keyCode == Keyboard.KEY_RIGHT && currentDiaryPage < storedNotes.tagCount() - 1) {
                currentDiaryPage++;
                updateDiaryActiveNote();
                if (this.mc.player != null) {
                    this.mc.player.playSound(efw.init.EfwModSounds.NOTES, 1.0f, 1.0f);
                }
                return;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        long now = System.currentTimeMillis();
        float deltaSec = lastFrameTime == 0 ? 0.016f : Math.min((now - lastFrameTime) / 1000.0f, 0.1f);
        lastFrameTime = now;

        // Auto idle rotation (6.0 deg/sec). Disabled while dragging (LMB) OR panning
        // (RMB)
        if (!isDragging && !isPanning) {
            targetYaw += 6.0f * deltaSec;
        }

        float lerpFactor = Math.min(1.0f, deltaSec * 10.0f);
        currentYaw += (targetYaw - currentYaw) * lerpFactor;
        currentPitch += (targetPitch - currentPitch) * lerpFactor;
        currentZoom += (targetZoom - currentZoom) * lerpFactor;
        currentPanX += (targetPanX - currentPanX) * lerpFactor;
        currentPanY += (targetPanY - currentPanY) * lerpFactor;

        // Smooth scroll interpolation
        float scrollLerp = Math.min(1.0f, deltaSec * 12.0f);
        scrollAmount += (targetScrollAmount - scrollAmount) * scrollLerp;

        // 1. Solid Clean Dark Midnight Background
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib(); // Сохраняем чистое состояние OpenGL

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();

        drawRect(0, 0, this.width, this.height, 0xFF020306);

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        this.dustManager.updateAndRender(this.width, this.height, this.mc);

        GlStateManager.popAttrib(); // Восстанавливаем состояние обратно
        GlStateManager.popMatrix();

        // КРИТИЧЕСКИ ВАЖНО: Сбрасываем цвет на белый (1, 1, 1, 1)!
        // Если dustManager в конце оставил прозрачный или черный цвет, 3D модель
        // сломается.
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 3. Render 3D item BEHIND the text and gradient
        float itemCenterX = (this.width / 2.0f) + currentPanX;
        float itemCenterY = (this.height / 2.0f - 25.0f) + currentPanY;

        GlStateManager.pushMatrix();
        GlStateManager.translate(itemCenterX, itemCenterY, 0.0F);
        Item3DRenderer.render3D(this.activeRenderStack, currentYaw, currentPitch, currentZoom, this.mc);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(itemCenterX, itemCenterY, 0.0F);
        Item3DRenderer.render3D(this.activeRenderStack, currentYaw, currentPitch, currentZoom, this.mc);
        GlStateManager.popMatrix();

        // 4. Reset OpenGL 2D state completely before drawing UI gradient
        GlStateManager.clear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.setActiveTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
        GlStateManager.bindTexture(0);
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.shadeModel(org.lwjgl.opengl.GL11.GL_FLAT);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 5. Black gradient covering 3/4 of the screen from the bottom
        int gradientTop = this.height / 4;
        drawSafeBlackGradient(0, gradientTop, this.width, this.height);

        // 6. Draw side arrows for diary pagination if multiple notes exist
        if (this.group == ItemInspectConfig.InspectGroup.DIARY && storedNotes != null && storedNotes.tagCount() > 1) {
            drawDiarySideArrows(mouseX, mouseY);
        }

        // 7. Render Title, Divider & Description text ON TOP
        drawItemInfo();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawSafeBlackGradient(int left, int top, int right, int bottom) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        org.lwjgl.opengl.GL11.glBegin(org.lwjgl.opengl.GL11.GL_QUADS);
        org.lwjgl.opengl.GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        org.lwjgl.opengl.GL11.glVertex3d((double) right, (double) top, (double) this.zLevel);
        org.lwjgl.opengl.GL11.glVertex3d((double) left, (double) top, (double) this.zLevel);
        org.lwjgl.opengl.GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.95F);
        org.lwjgl.opengl.GL11.glVertex3d((double) left, (double) bottom, (double) this.zLevel);
        org.lwjgl.opengl.GL11.glVertex3d((double) right, (double) bottom, (double) this.zLevel);
        org.lwjgl.opengl.GL11.glEnd();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private void drawDiarySideArrows(int mouseX, int mouseY) {
        int midY = this.height / 2;
        int leftArrowX = 30;
        int rightArrowX = this.width - 30;

        // Left Arrow (pointing left: < ) - Wide/tall flat mountain shape (height 28px,
        // width 14px)
        if (currentDiaryPage > 0) {
            boolean hoverLeft = Math.abs(mouseX - leftArrowX) <= 20 && Math.abs(mouseY - midY) <= 30;
            int colorLeft = hoverLeft ? 0xFFFFFFFF : 0x88FFFFFF;
            drawFlatMountainArrow(leftArrowX, midY, 14, 28, true, colorLeft);
        }

        // Right Arrow (pointing right: > ) - Wide/tall flat mountain shape (height
        // 28px, width 14px)
        if (currentDiaryPage < storedNotes.tagCount() - 1) {
            boolean hoverRight = Math.abs(mouseX - rightArrowX) <= 20 && Math.abs(mouseY - midY) <= 30;
            int colorRight = hoverRight ? 0xFFFFFFFF : 0x88FFFFFF;
            drawFlatMountainArrow(rightArrowX, midY, 14, 28, false, colorRight);
        }
    }

    /**
     * Draws a 1-pixel wide pixelated mountain arrow pointing left or right.
     * Flat / wide shape: height is larger than width, no center stick.
     */
    private void drawFlatMountainArrow(int centerX, int centerY, int width, int height, boolean pointLeft, int color) {
        int halfH = height / 2;
        for (int dy = -halfH; dy <= halfH; dy++) {
            // Linear slope: tip is at centerX, base is at (centerX + width) or (centerX -
            // width)
            float t = (float) Math.abs(dy) / (float) halfH;
            int xOffset = (int) (t * width);
            int px = pointLeft ? (centerX + xOffset) : (centerX - xOffset);
            drawRect(px, centerY + dy, px + 1, centerY + dy + 1, color);
        }
    }

    private void drawItemInfo() {
        FontRenderer font = this.mc.fontRenderer;
        String displayName = this.targetStack.getDisplayName();
        if (this.group == ItemInspectConfig.InspectGroup.DIARY && storedNotes != null && storedNotes.tagCount() > 0) {
            displayName = displayName + "  §7(" + (currentDiaryPage + 1) + " / " + storedNotes.tagCount() + ")";
        }

        int centerX = this.width / 2;
        ItemInspectConfig.TextSettings ts = ItemInspectConfig.textSettings;
        int infoY = this.height + ts.textOffsetY;

        // Draw Display Name (Gold stylized with customizable scale & independent pos
        // offset)
        GlStateManager.pushMatrix();
        float titleScale = ts.titleScale;
        int rawTitleW = font.getStringWidth(displayName);
        int titlePosX = (int) (centerX + ts.titleOffsetX - (rawTitleW * titleScale) / 2.0f);
        int titlePosY = infoY + ts.titleOffsetY;
        GlStateManager.translate(titlePosX, titlePosY, 0);
        GlStateManager.scale(titleScale, titleScale, 1.0f);
        font.drawStringWithShadow(displayName, 0, 0, TITLE_COLOR);
        GlStateManager.popMatrix();

        boolean isGun = group == ItemInspectConfig.InspectGroup.PISTOLS
                || group == ItemInspectConfig.InspectGroup.SMG
                || group == ItemInspectConfig.InspectGroup.WEAPONS_OTHER;

        String desc = "";
        String lore = "";

        if (this.group == ItemInspectConfig.InspectGroup.NOTE) {
            int id = NoteItem.getNoteId(this.targetStack);
            desc = NotesConfig.getText(id);
        } else if (this.group == ItemInspectConfig.InspectGroup.DPOR) {
            // Фиксированный (не рандомный) текст — берётся из lang-файла
            desc = I18n.format("tooltip.mwccf.dpor.inspect");
        } else if (this.group == ItemInspectConfig.InspectGroup.DIARY) {
            if (storedNotes != null && storedNotes.tagCount() > 0 && currentDiaryPage < storedNotes.tagCount()) {
                NBTTagCompound noteTag = storedNotes.getCompoundTagAt(currentDiaryPage);
                int noteId = noteTag.getInteger("noteId");
                desc = NotesConfig.getText(noteId);
            } else {
                desc = "This diary is empty.";
            }
        } else {
            LoadingScreenEntry activeEntry = this.entry != null ? this.entry
                    : ItemInspectDescConfig.getEntryForStack(this.targetStack);
            desc = (activeEntry != null && activeEntry.description != null && !activeEntry.description.isEmpty())
                    ? activeEntry.description
                    : "";
            lore = (isGun || activeEntry == null || activeEntry.lore == null || activeEntry.lore.isEmpty()) ? ""
                    : activeEntry.lore;
        }

        if (!desc.isEmpty() || !lore.isEmpty()) {
            int maxW = Math.min(ts.maxTextWidth, this.width - 60);
            int currentY = (int) (infoY + (font.FONT_HEIGHT * ts.titleScale) + ts.dividerGap);

            // Divider line
            int divHalfW = Math.min(ts.dividerWidth, maxW) / 2;
            drawRect(centerX - divHalfW, currentY, centerX + divHalfW, currentY + 1, DIVIDER_COLOR);
            currentY += ts.descGap;

            float descScale = ts.descScale;
            int scaledMaxW = (int) (maxW / descScale);
            int lineHeight = (int) ((font.FONT_HEIGHT + 3) * descScale);

            List<String> allLines = new ArrayList<>();
            List<Boolean> isLoreLine = new ArrayList<>();

            if (!desc.isEmpty()) {
                List<String> descLines = font.listFormattedStringToWidth(desc, scaledMaxW);
                for (String l : descLines) {
                    allLines.add(l);
                    isLoreLine.add(false);
                }
            }

            if (!lore.isEmpty()) {
                List<String> loreLines = font.listFormattedStringToWidth("§o" + lore, scaledMaxW);
                for (String l : loreLines) {
                    allLines.add(l);
                    isLoreLine.add(true);
                }
            }

            int maxLines = (this.group == ItemInspectConfig.InspectGroup.NOTE
                    || this.group == ItemInspectConfig.InspectGroup.DIARY
                    || this.group == ItemInspectConfig.InspectGroup.DPOR) ? 4 : 7;
            int totalTextHeight = allLines.size() * lineHeight;
            int maxVisibleHeight = maxLines * lineHeight;
            int maxScroll = Math.max(0, totalTextHeight - maxVisibleHeight);

            // Clamp target and current scroll amounts
            if (targetScrollAmount < 0)
                targetScrollAmount = 0;
            if (targetScrollAmount > maxScroll)
                targetScrollAmount = maxScroll;
            if (scrollAmount < 0)
                scrollAmount = 0;
            if (scrollAmount > maxScroll)
                scrollAmount = maxScroll;

            // Clip text rendering to max 7 lines
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            double mcScale = this.mc.displayHeight / (double) this.height;
            int scissorX = (int) ((centerX - maxW / 2 - 4) * mcScale);
            int scissorW = (int) ((maxW + 8) * mcScale);
            int scissorY = (int) ((this.height - (currentY + maxVisibleHeight)) * mcScale);
            int scissorH = (int) (maxVisibleHeight * mcScale);

            GL11.glScissor(scissorX, scissorY, scissorW, scissorH);

            int drawY = currentY - (int) scrollAmount;
            int textColor = isGun ? 0xFFFFFF : DESC_COLOR;

            for (int i = 0; i < allLines.size(); i++) {
                String line = allLines.get(i);
                boolean isLore = isLoreLine.get(i);

                if (drawY + lineHeight >= currentY && drawY <= currentY + maxVisibleHeight) {
                    GlStateManager.pushMatrix();
                    int lineW = font.getStringWidth(line);
                    GlStateManager.translate(centerX - (lineW * descScale) / 2.0f, drawY, 0);
                    GlStateManager.scale(descScale, descScale, 1.0f);
                    font.drawStringWithShadow(line, 0, 0, isLore ? LORE_COLOR : textColor);
                    GlStateManager.popMatrix();
                }
                drawY += lineHeight;
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            // Draw scrollbar slider if text exceeds max visible lines
            if (allLines.size() > maxLines) {
                int barX = centerX + divHalfW + 6;
                drawRect(barX, currentY, barX + 2, currentY + maxVisibleHeight, 0x44FFFFFF);
                int knobH = Math.max(8, (maxVisibleHeight * maxVisibleHeight) / totalTextHeight);
                int knobPos = (int) (scrollAmount * (maxVisibleHeight - knobH)
                        / (double) (totalTextHeight - maxVisibleHeight));
                drawRect(barX, currentY + knobPos, barX + 2, currentY + knobPos + knobH, 0xFFFFFFFF);
            }
        }
    }
}
