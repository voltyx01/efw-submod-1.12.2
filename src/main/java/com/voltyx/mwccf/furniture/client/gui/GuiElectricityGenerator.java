package com.voltyx.mwccf.furniture.client.gui;

import com.voltyx.mwccf.furniture.tileentity.ContainerElectricityGenerator;
import com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiElectricityGenerator extends GuiContainer {

    private static final ResourceLocation GENERATOR_GUI_TEXTURE = new ResourceLocation("refurbished_furniture", "textures/gui/container/electricity_generator.png");
    private static final ResourceLocation CHARGING_SLOT_TEXTURE = new ResourceLocation("mwccf", "textures/gui/charging_slot.png");
    private final TileEntityElectricityGenerator generatorTile;

    // In-game Editor Mode
    public boolean editMode = false;
    private GuiButton btnEdit;
    private GuiElement selectedElement = GuiElement.CHARGE_SLOT_1;
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public enum GuiElement {
        CHARGE_SLOT_1("Слот зарядки 1 (Charge 1)", 18, 18),
        CHARGE_SLOT_2("Слот зарядки 2 (Charge 2)", 18, 18),
        STATUS_TEXT("Статус (Status)", 55, 10),
        POWER_TEXT("Мощность (Power)", 55, 10),
        FUEL_TEXT("Время топлива (Fuel)", 55, 10),
        CHARGE_LABEL("Надпись зарядки (Label)", 45, 10),
        FUEL_SLOT("Слот топлива (Fuel Slot)", 18, 18);

        public final String name;
        public final int width;
        public final int height;

        GuiElement(String name, int width, int height) {
            this.name = name;
            this.width = width;
            this.height = height;
        }
    }

    public GuiElectricityGenerator(InventoryPlayer playerInv, TileEntityElectricityGenerator generatorTile) {
        super(new ContainerElectricityGenerator(playerInv, generatorTile));
        this.generatorTile = generatorTile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        GeneratorGuiConfig.load();
        syncSlotPositions();
    }

    private void syncSlotPositions() {
        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();
        if (this.inventorySlots != null && this.inventorySlots.inventorySlots.size() >= 3) {
            this.inventorySlots.inventorySlots.get(0).xPos = cfg.fuelSlotX;
            this.inventorySlots.inventorySlots.get(0).yPos = cfg.fuelSlotY;
            this.inventorySlots.inventorySlots.get(1).xPos = cfg.chargeSlot1X;
            this.inventorySlots.inventorySlots.get(1).yPos = cfg.chargeSlot1Y;
            this.inventorySlots.inventorySlots.get(2).xPos = cfg.chargeSlot2X;
            this.inventorySlots.inventorySlots.get(2).yPos = cfg.chargeSlot2Y;
        }
    }

    private int getElementX(GuiElement elem) {
        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();
        switch (elem) {
            case FUEL_SLOT: return cfg.fuelSlotX - 1;
            case CHARGE_SLOT_1: return cfg.chargeSlot1X - 1;
            case CHARGE_SLOT_2: return cfg.chargeSlot2X - 1;
            case STATUS_TEXT: return cfg.statusX;
            case POWER_TEXT: return cfg.powerX;
            case FUEL_TEXT: return cfg.fuelTextX;
            case CHARGE_LABEL: return cfg.chargeLabelX;
        }
        return 0;
    }

    private int getElementY(GuiElement elem) {
        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();
        switch (elem) {
            case FUEL_SLOT: return cfg.fuelSlotY - 1;
            case CHARGE_SLOT_1: return cfg.chargeSlot1Y - 1;
            case CHARGE_SLOT_2: return cfg.chargeSlot2Y - 1;
            case STATUS_TEXT: return cfg.statusY;
            case POWER_TEXT: return cfg.powerY;
            case FUEL_TEXT: return cfg.fuelTextY;
            case CHARGE_LABEL: return cfg.chargeLabelY;
        }
        return 0;
    }

    private void setElementPos(GuiElement elem, int x, int y) {
        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();
        switch (elem) {
            case FUEL_SLOT:
                cfg.fuelSlotX = x + 1;
                cfg.fuelSlotY = y + 1;
                break;
            case CHARGE_SLOT_1:
                cfg.chargeSlot1X = x + 1;
                cfg.chargeSlot1Y = y + 1;
                break;
            case CHARGE_SLOT_2:
                cfg.chargeSlot2X = x + 1;
                cfg.chargeSlot2Y = y + 1;
                break;
            case STATUS_TEXT:
                cfg.statusX = x;
                cfg.statusY = y;
                break;
            case POWER_TEXT:
                cfg.powerX = x;
                cfg.powerY = y;
                break;
            case FUEL_TEXT:
                cfg.fuelTextX = x;
                cfg.fuelTextY = y;
                break;
            case CHARGE_LABEL:
                cfg.chargeLabelX = x;
                cfg.chargeLabelY = y;
                break;
        }
        syncSlotPositions();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 100) {
            toggleEditMode();
        }
    }

    private void toggleEditMode() {
        this.editMode = !this.editMode;
        if (!this.editMode) {
            GeneratorGuiConfig.save();
            if (this.mc.player != null) {
                this.mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.8F, 1.2F);
            }
            if (this.btnEdit != null) {
                this.buttonList.remove(this.btnEdit);
                this.btnEdit = null;
            }
        } else {
            if (this.btnEdit == null) {
                int btnX = this.guiLeft + this.xSize - 44;
                int btnY = this.guiTop + 4;
                this.btnEdit = new GuiButton(100, btnX, btnY, 40, 14, "✔ SAVE");
                this.buttonList.add(this.btnEdit);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.editMode && mouseButton == 0) {
            int relX = mouseX - this.guiLeft;
            int relY = mouseY - this.guiTop;

            for (GuiElement elem : GuiElement.values()) {
                int ex = getElementX(elem);
                int ey = getElementY(elem);
                if (relX >= ex && relX <= ex + elem.width && relY >= ey && relY <= ey + elem.height) {
                    this.selectedElement = elem;
                    this.isDragging = true;
                    this.dragOffsetX = relX - ex;
                    this.dragOffsetY = relY - ey;
                    return; // Don't pick up items in edit mode!
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.editMode && this.isDragging && this.selectedElement != null) {
            int relX = mouseX - this.guiLeft;
            int relY = mouseY - this.guiTop;
            int newX = relX - this.dragOffsetX;
            int newY = relY - this.dragOffsetY;
            setElementPos(this.selectedElement, newX, newY);
            return;
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.editMode) {
            this.isDragging = false;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.editMode) {
            if (keyCode == Keyboard.KEY_F8 || keyCode == Keyboard.KEY_RETURN) {
                toggleEditMode();
                return;
            }
            if (keyCode == Keyboard.KEY_ESCAPE) {
                GeneratorGuiConfig.load();
                syncSlotPositions();
                toggleEditMode();
                return;
            }
            if (keyCode == Keyboard.KEY_TAB) {
                int next = (this.selectedElement.ordinal() + 1) % GuiElement.values().length;
                this.selectedElement = GuiElement.values()[next];
                return;
            }

            int step = isShiftKeyDown() ? 5 : 1;
            int curX = getElementX(this.selectedElement);
            int curY = getElementY(this.selectedElement);

            if (keyCode == Keyboard.KEY_LEFT) {
                setElementPos(this.selectedElement, curX - step, curY);
                return;
            } else if (keyCode == Keyboard.KEY_RIGHT) {
                setElementPos(this.selectedElement, curX + step, curY);
                return;
            } else if (keyCode == Keyboard.KEY_UP) {
                setElementPos(this.selectedElement, curX, curY - step);
                return;
            } else if (keyCode == Keyboard.KEY_DOWN) {
                setElementPos(this.selectedElement, curX, curY + step);
                return;
            }
        } else {
            if (keyCode == Keyboard.KEY_F8) {
                toggleEditMode();
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.editMode) {
            drawEditorOverlay(mouseX, mouseY);
        }
    }

    private void drawEditorOverlay(int mouseX, int mouseY) {
        // Top dark HUD
        drawRect(0, 0, this.width, 36, 0xDD111111);
        drawRect(0, 36, this.width, 37, 0xFF44AAFF);

        this.fontRenderer.drawString("§e[РЕЖИМ НАСТРОЙКИ GUI ГЕНЕРАТОРА]", 10, 5, 0xFFFFFF);
        String elemInfo = "§fВыбран элемент: §b" + this.selectedElement.name + " §7| §fКоординаты: §aX=" + getElementX(this.selectedElement) + ", Y=" + getElementY(this.selectedElement);
        this.fontRenderer.drawString(elemInfo, 10, 15, 0xFFFFFF);
        this.fontRenderer.drawString("§7[ЛКМ: Перетаскивать] [Стрелки: Смещение] [Shift: x5] [TAB: Выбрать следующий] [F8/SAVE: Сохранить]", 10, 25, 0xAAAAAA);

        // Highlight boxes around elements
        for (GuiElement elem : GuiElement.values()) {
            int ex = this.guiLeft + getElementX(elem);
            int ey = this.guiTop + getElementY(elem);
            boolean isSel = (elem == this.selectedElement);

            int color = isSel ? 0xFF00FF00 : 0x88FFFFFF;
            int fill = isSel ? 0x4400FF00 : 0x18FFFFFF;

            drawRect(ex, ey, ex + elem.width, ey + elem.height, fill);
            drawBoxOutline(ex, ey, elem.width, elem.height, color);

            if (isSel) {
                String tag = "§a" + elem.name;
                this.fontRenderer.drawStringWithShadow(tag, ex, ey - 9, 0x00FF00);
            }
        }
    }

    private void drawBoxOutline(int x, int y, int w, int h, int color) {
        drawRect(x, y, x + w, y + 1, color);
        drawRect(x, y + h - 1, x + w, y + h, color);
        drawRect(x, y, x + 1, y + h, color);
        drawRect(x + w - 1, y, x + w, y + h, color);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();
        String title = I18n.format("container.refurbished_furniture.electricity_generator");
        this.fontRenderer.drawString(title, 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        boolean active = this.generatorTile.isGeneratingPower();
        int secs = this.generatorTile.burnTime / 20;
        String statusText;
        if (active) {
            statusText = "§aRUNNING";
        } else if (secs > 0) {
            statusText = "§eSTANDBY";
        } else {
            statusText = "§cOFF";
        }
        this.fontRenderer.drawStringWithShadow(statusText, cfg.statusX, cfg.statusY, 0xFFFFFF);
        this.fontRenderer.drawStringWithShadow("POWER: " + (active ? "§e15" : "§70"), cfg.powerX, cfg.powerY, 0xFFFFFF);
        String fuelText = secs > 0 ? "§b" + secs + "s" : "§7EMPTY";
        this.fontRenderer.drawStringWithShadow("FUEL: " + fuelText, cfg.fuelTextX, cfg.fuelTextY, 0xFFFFFF);

        this.fontRenderer.drawStringWithShadow("§b" + I18n.format("container.mwccf.charging") + ":", cfg.chargeLabelX, cfg.chargeLabelY, 0xFFFFFF);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GeneratorGuiConfig cfg = GeneratorGuiConfig.get();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GENERATOR_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (this.generatorTile.isGeneratingPower()) {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i + 27, j + 26 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        // Draw fuel slot frame if moved from standard (26, 42)
        if (cfg.fuelSlotX != 26 || cfg.fuelSlotY != 42) {
            this.mc.getTextureManager().bindTexture(GENERATOR_GUI_TEXTURE);
            this.drawTexturedModalRect(i + cfg.fuelSlotX - 1, j + cfg.fuelSlotY - 1, 25, 41, 18, 18);
        }

        // Draw slot boxes for charging slots 1 and 2
        this.mc.getTextureManager().bindTexture(GENERATOR_GUI_TEXTURE);
        this.drawTexturedModalRect(i + cfg.chargeSlot1X - 1, j + cfg.chargeSlot1Y - 1, 25, 41, 18, 18);
        this.drawTexturedModalRect(i + cfg.chargeSlot2X - 1, j + cfg.chargeSlot2Y - 1, 25, 41, 18, 18);

        // Draw ghost battery silhouette if slot is empty
        this.mc.getTextureManager().bindTexture(CHARGING_SLOT_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.45F);
        if (this.generatorTile.getStackInSlot(1).isEmpty()) {
            drawModalRectWithCustomSizedTexture(i + cfg.chargeSlot1X, j + cfg.chargeSlot1Y, 0, 0, 16, 16, 16, 16);
        }
        if (this.generatorTile.getStackInSlot(2).isEmpty()) {
            drawModalRectWithCustomSizedTexture(i + cfg.chargeSlot2X, j + cfg.chargeSlot2Y, 0, 0, 16, 16, 16, 16);
        }
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int getBurnLeftScaled(int pixels) {
        int i = this.generatorTile.currentItemBurnTime;
        if (i == 0) {
            i = 200;
        }
        return this.generatorTile.burnTime * pixels / i;
    }
}
