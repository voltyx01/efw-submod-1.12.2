package com.teamderpy.shouldersurfing.lockon;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class LockOnConfigGuiFactory implements IModGuiFactory {
    public void initialize(Minecraft mc) {}
    public boolean hasConfigGui() { return true; }
    public GuiScreen createConfigGui(GuiScreen parent) { return new LockOnConfigGui(parent); }
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
}