package com.voltyx.gender.gui;

import com.voltyx.gender.gui.screen.WildfirePlayerListScreen;
import com.voltyx.gender.gui.screen.WardrobeBrowserScreen;
import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WildfirePlayerList extends GuiListExtended {

    private static final ResourceLocation TXTR_SYNC = new ResourceLocation(WildfireGender.MODID, "textures/sync.png");
    private static final ResourceLocation TXTR_CACHED = new ResourceLocation(WildfireGender.MODID, "textures/cached.png");
    private static final ResourceLocation TXTR_UNKNOWN = new ResourceLocation(WildfireGender.MODID, "textures/unknown.png");

    private final WildfirePlayerListScreen parent;
    private final List<Entry> entries = new ArrayList<>();

    public WildfirePlayerList(WildfirePlayerListScreen parent, int listWidth, int top, int bottom) {
        // Вызов конструктора GuiListExtended: mc, width, height, top, bottom, slotHeight
        super(Minecraft.getMinecraft(), parent.width - 4, parent.height, top - 6, bottom, 20);
        this.parent = parent;
        this.refreshList();
    }

    @Override
    protected int getScrollBarX() {
        return parent.width / 2 + 53;
    }

    @Override
    public int getListWidth() {
        return 112; // Фиксированная ширина элемента из 1.18.2
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return entries.get(index);
    }

    @Override
    protected int getSize() {
        return entries.size();
    }

    public void refreshList() {
        this.entries.clear();
        Collection<NetworkPlayerInfo> playersC = this.mc.getConnection().getPlayerInfoMap();

        for (NetworkPlayerInfo loadedPlayer : playersC) {
            this.entries.add(new Entry(loadedPlayer));
        }
    }

    public boolean isLoadingPlayers() {
        boolean loadingPlayers = false;
        for (Entry child : this.entries) {
            GenderPlayer aPlr = WildfireGender.getPlayerById(child.nInfo.getGameProfile().getId());
            if (aPlr == null) {
                loadingPlayers = true;
            }
        }
        return loadingPlayers;
    }

    // --- Внутренний класс для элементов списка ---
    @SideOnly(Side.CLIENT)
    public class Entry implements GuiListExtended.IGuiListEntry {

        private final String name;
        public final NetworkPlayerInfo nInfo;
        private boolean isActive = false;

        private Entry(final NetworkPlayerInfo nInfo) {
            this.nInfo = nInfo;
            this.name = nInfo.getGameProfile().getName();

            GenderPlayer aPlr = WildfireGender.getPlayerById(nInfo.getGameProfile().getId());
            if (aPlr != null) {
                this.isActive = !aPlr.lockSettings;
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            FontRenderer font = mc.fontRenderer;

            EntityPlayer playerentity = mc.world.getPlayerEntityByUUID(nInfo.getGameProfile().getId());
            GenderPlayer aPlr = WildfireGender.getPlayerById(nInfo.getGameProfile().getId());

            // 1. Рисуем голову скина (лицо)
            mc.getTextureManager().bindTexture(nInfo.getLocationSkin());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(x + 2, y + 2, 8.0F, 8.0F, 8, 8, 16, 16, 64.0F, 64.0F);

            // 2. Рисуем слой шапки/волос, если он включен
            if (playerentity != null && playerentity.isWearing(EnumPlayerModelParts.HAT)) {
                Gui.drawScaledCustomSizeModalRect(x + 1, y + 1, 40.0F, 8.0F, 8, 8, 18, 18, 64.0F, 64.0F);
            }

            // 3. Пишем ник
            font.drawStringWithShadow(name, x + 23, y + 2, 0xFFFFFF);

            // 4. Логика отрисовки статуса и пола
            if (aPlr != null) {
                this.isActive = !aPlr.lockSettings;

                font.drawStringWithShadow(aPlr.getGender().getDisplayName().getFormattedText(), x + 23, y + 11, 0xFFFFFF);

                // Иконка синхронизации
                if (aPlr.getSyncStatus() == GenderPlayer.SyncStatus.SYNCED) {
                    mc.getTextureManager().bindTexture(TXTR_SYNC);
                    Gui.drawScaledCustomSizeModalRect(x + 98, y + 11, 0, 0, 12, 8, 12, 8, 12, 8);

                    // Хитбокс тултипа
                    if (mouseX > x + 98 - 2 && mouseY > y + 11 - 2 && mouseX < x + 98 + 12 + 2 && mouseY < y + 20) {
                        parent.setTooltip(I18n.format("wildfire_gender.player_list.state.synced"));
                    }
                } else if (aPlr.getSyncStatus() == GenderPlayer.SyncStatus.UNKNOWN) {
                    mc.getTextureManager().bindTexture(TXTR_UNKNOWN);
                    Gui.drawScaledCustomSizeModalRect(x + 98, y + 11, 0, 0, 12, 8, 12, 8, 12, 8);
                }
            } else {
                this.isActive = false;
                font.drawStringWithShadow(TextFormatting.RED + I18n.format("wildfire_gender.label.too_far"), x + 23, y + 11, 0xFFFFFF);
            }

            // Логика "наведения" из 1.18.2
            boolean isHovered = mouseX >= x && mouseX <= x + listWidth && mouseY >= y && mouseY <= y + slotHeight;
            if (isHovered) {
                WildfirePlayerListScreen.HOVER_PLAYER = aPlr;
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            // Если игрок нажал ЛКМ (0) по этому элементу и кнопка "активна"
            if (mouseEvent == 0 && isActive) {
                GenderPlayer aPlr = WildfireGender.getPlayerById(nInfo.getGameProfile().getId());
                if (aPlr != null) {
                    try {
                        mc.displayGuiScreen(new WardrobeBrowserScreen(parent, nInfo.getGameProfile().getId()));
                        return true;
                    } catch (Exception ignored) {}
                }
            }
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }

        @Override
        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
        }
    }
}