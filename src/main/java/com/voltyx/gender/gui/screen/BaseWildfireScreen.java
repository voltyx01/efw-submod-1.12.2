package com.voltyx.gender.gui.screen;

import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public abstract class BaseWildfireScreen extends GuiScreen {

    protected final UUID playerUUID;
    protected final GuiScreen parent;
    protected String title; // В 1.12.2 нет встроенного title, добавляем его сами

    protected BaseWildfireScreen(String title, GuiScreen parent, UUID uuid) {
        this.title = title;
        this.parent = parent;
        this.playerUUID = uuid;
    }

    protected GenderPlayer getPlayer() {
        return WildfireGender.getPlayerById(this.playerUUID);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}