package efw.client.gui;

import com.voltyx.mwccf.client.inspect.InspectDustManager;
import com.voltyx.mwccf.client.inspect.InspectTransitionHandler;
import efw.config.NotesConfig;
import efw.item.NoteItem;
import efw.world.inventory.NoteContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class NoteGui extends GuiContainer {

    private final EntityPlayer player;
    private final GuiScreen parentScreen;
    // --- ДОБАВЛЕНО: Предмет, который мы осматриваем ---
    private ItemStack inspectedStack = ItemStack.EMPTY;

    // Scroll state
    private double scrollAmount = 0.0;
    private List<String> splitText = null;

    // Text area (fraction of screen)
    private int textX, textY, visibleWidth, visibleHeight;

    // Rotate & smooth pan item in right panel
    private float rotX = -12f, rotY = -190f;
    private float targetRotX = -12f, targetRotY = -190f;
    private float currentZoom = 180.0f, targetZoom = 180.0f;
    private float currentPanX = 0.0f, targetPanX = 0.0f;
    private float currentPanY = 0.0f, targetPanY = 0.0f;

    private int prevMouseX, prevMouseY;
    private boolean isDragging = false;
    private boolean isPanning = false;

    // Fade Transition
    private float fadeProgress = 0.0f;
    private boolean isExiting = false;
    private static final float FADE_SPEED = 5.0f;
    private long lastFrameTime = 0;

    private final InspectDustManager dustManager = new InspectDustManager();

    public NoteGui(NoteContainer container, EntityPlayer player) {
        this(container, player, null, ItemStack.EMPTY);
    }

    public NoteGui(NoteContainer container, EntityPlayer player, GuiScreen parentScreen, ItemStack inspectedStack) {
        super(container);
        this.player = player;
        this.parentScreen = parentScreen;
        this.inspectedStack = inspectedStack;
        this.xSize = 0;
        this.ySize = 0;
        this.fadeProgress = 0.0f;
        this.isExiting = false;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = this.width;
        this.ySize = this.height;
        this.lastFrameTime = System.currentTimeMillis();
        this.dustManager.init(this.width, this.height);

        this.textX = 60;
        this.textY = 80;
        this.visibleWidth = this.width / 2 - 100;
        this.visibleHeight = this.height - 160;

        buildText();
    }

    private void buildText() {
        // Сначала берем переданный предмет
        ItemStack stack = this.inspectedStack;

        // Если предмет не передали, ищем в руках
        if (stack == null || stack.isEmpty()) {
            stack = player.getHeldItemMainhand();
            if (stack.isEmpty()
                    || (!(stack.getItem() instanceof NoteItem) && !(stack.getItem() instanceof efw.item.DporItem))) {
                stack = player.getHeldItemOffhand();
            }
        }

        String content;
        if (stack.getItem() instanceof efw.item.DporItem) {
            content = net.minecraft.client.resources.I18n.format("tooltip.mwccf.dpor.inspect");
        } else if (stack.getItem() instanceof NoteItem) {
            int id = NoteItem.getNoteId(stack);
            content = NotesConfig.getText(id);
        } else {
            content = "Item not found";
        }

        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        splitText = fr.listFormattedStringToWidth(content, visibleWidth);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Pure solid black background like inspect screen
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        drawRect(0, 0, this.width, this.height, 0xFF000000);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        long now = System.currentTimeMillis();
        float deltaSec = lastFrameTime == 0 ? 0.016f : Math.min((now - lastFrameTime) / 1000.0f, 0.1f);
        lastFrameTime = now;

        // Handle Fade in / Fade out transition
        if (isExiting) {
            fadeProgress = Math.max(0.0f, fadeProgress - deltaSec * FADE_SPEED);
            if (fadeProgress <= 0.001f) {
                InspectTransitionHandler.startTransitionToScreen(this.parentScreen, this);
                return;
            }
        } else {
            fadeProgress = Math.min(1.0f, fadeProgress + deltaSec * FADE_SPEED);
        }

        // Smooth interpolation
        float lerpFactor = Math.min(1.0f, deltaSec * 12.0f);
        rotX += (targetRotX - rotX) * lerpFactor;
        rotY += (targetRotY - rotY) * lerpFactor;
        currentZoom += (targetZoom - currentZoom) * lerpFactor;
        currentPanX += (targetPanX - currentPanX) * lerpFactor;
        currentPanY += (targetPanY - currentPanY) * lerpFactor;

        // 1. Draw Background
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 2. Ambient floating ash dust particles in the background
        this.dustManager.updateAndRender(this.width, this.height, this.mc);

        // 3. Render 3D note item on the right side
        ItemStack renderStack = this.inspectedStack;
        if (renderStack == null || renderStack.isEmpty()) {
            renderStack = player.getHeldItemMainhand();
            if (renderStack.isEmpty() || (!(renderStack.getItem() instanceof NoteItem)
                    && !(renderStack.getItem() instanceof efw.item.DporItem))) {
                renderStack = player.getHeldItemOffhand();
            }
        }
        float itemCenterX = (this.width * 3.0f / 4.0f) + currentPanX;
        float itemCenterY = (this.height / 2.0f) + currentPanY;
        renderRotatingItem((int) itemCenterX, (int) itemCenterY, currentZoom, renderStack);
        // 4. Smooth blackout overlay for fade in/out transitions
        if (fadeProgress < 0.999f) {
            float alpha = 1.0f - fadeProgress;
            int a = (int) (Math.max(0.0f, Math.min(1.0f, alpha)) * 255);
            int blackFadeColor = (a << 24);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            drawRect(0, 0, this.width, this.height, blackFadeColor);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (splitText == null)
            return;

        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

        int totalHeight = splitText.size() * fr.FONT_HEIGHT;
        int maxScroll = Math.max(0, totalHeight - visibleHeight);

        // Clamp scroll
        if (scrollAmount < 0)
            scrollAmount = 0;
        if (scrollAmount > maxScroll)
            scrollAmount = maxScroll;

        // Enable scissor / clip
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scale = this.mc.displayHeight / (double) this.height;
        GL11.glScissor(
                (int) (textX * scale),
                (int) (this.mc.displayHeight - (textY + visibleHeight) * scale),
                (int) (visibleWidth * scale),
                (int) (visibleHeight * scale));

        int y = textY - (int) scrollAmount;
        for (String line : splitText) {
            fr.drawString(line, textX, y, 0xFFFFFF, false);
            y += fr.FONT_HEIGHT;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // Scroll bar
        if (totalHeight > visibleHeight) {
            int barX = textX + visibleWidth + 5;
            int barH = visibleHeight;
            drawRect(barX, textY, barX + 2, textY + barH, 0x44FFFFFF);
            int knobH = Math.max(10, visibleHeight * visibleHeight / totalHeight);
            int knobPos = (int) (scrollAmount * (visibleHeight - knobH) / (double) (totalHeight - visibleHeight));
            drawRect(barX, textY + knobPos, barX + 2, textY + knobPos + knobH, 0xFFFFFFFF);
        }
    }

    private void renderRotatingItem(int x, int y, float size, ItemStack stack) {
        if (stack.isEmpty())
            return;
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 150f);
        GlStateManager.scale(size, -size, size);
        GlStateManager.rotate(rotX, 1f, 0f, 0f);
        GlStateManager.rotate(rotY, 0f, 1f, 0f);

        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

        Minecraft.getMinecraft().getRenderItem().renderItem(stack,
                net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            if (mouseX < this.width / 2) {
                scrollAmount -= (delta > 0 ? 14.0 : -14.0);
            } else {
                if (delta > 0) {
                    targetZoom = Math.min(360.0f, targetZoom + 20.0f);
                } else {
                    targetZoom = Math.max(60.0f, targetZoom - 20.0f);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        prevMouseX = mouseX;
        prevMouseY = mouseY;
        if (mouseButton == 0) {
            isDragging = true;
        } else if (mouseButton == 1) {
            isPanning = true;
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
    protected void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick) {
        int dx = mouseX - prevMouseX;
        int dy = mouseY - prevMouseY;
        if (button == 0 && isDragging) {
            targetRotY += dx * 0.8f;
            targetRotX += dy * 0.8f;
            targetRotX = Math.max(-80f, Math.min(80f, targetRotX));
        } else if (button == 1 && isPanning) {
            targetPanX += dx;
            targetPanY += dy;
        }
        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == Keyboard.KEY_I
                || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            if (!isExiting) {
                isExiting = true;
            }
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
