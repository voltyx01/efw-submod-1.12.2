package com.voltyx.mwccf.gui;

import com.teamderpy.shouldersurfing.config.Config;
import com.teamderpy.shouldersurfing.lockon.LockOnConfig;
import com.voltyx.mwccf.MwccfMod;
import efw.biomeinfo.MwccfConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMwccfConfig extends GuiConfig {

    public GuiMwccfConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), MwccfMod.MODID, false, false, "MWCCF Configuration");
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();

        // 1. Все оригинальные категории MWCCF (dash_and_stamina, techguns, combat_feedback и т.д.)
        try {
            IConfigElement root = ConfigElement.from(MwccfConfig.class);
            if (root != null && root.getChildElements() != null) {
                list.addAll(root.getChildElements());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        // 2. Вкладка Extra Gore
        try {
            IConfigElement goreRoot = ConfigElement.from(com.eruannie_9.extragore.ModConfigurationClient.class);
            if (goreRoot != null && goreRoot.getChildElements() != null && !goreRoot.getChildElements().isEmpty()) {
                list.add(new DummyConfigElement.DummyCategoryElement(
                    "Extra Gore",
                    "config.extragore.client",
                    goreRoot.getChildElements()
                ));
            } else if (goreRoot != null) {
                list.add(goreRoot);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        // 3. Отдельная вкладка Shoulder Surfing
        if (Config.CLIENT != null && Config.CLIENT.getConfig() != null) {
            Configuration ssConfig = Config.CLIENT.getConfig();
            List<IConfigElement> ssElements = new ArrayList<>();
            for (String catName : ssConfig.getCategoryNames()) {
                ssElements.addAll(new ConfigElement(ssConfig.getCategory(catName)).getChildElements());
            }
            list.add(new DummyConfigElement.DummyCategoryElement(
                "Shoulder Surfing",
                "mwccf.config.shouldersurfing",
                ssElements
            ));
        }

        // 3. Отдельная вкладка Lock-On
        if (LockOnConfig.config != null) {
            Configuration lockConfig = LockOnConfig.config;
            List<IConfigElement> lockElements = new ArrayList<>();
            for (String catName : lockConfig.getCategoryNames()) {
                lockElements.addAll(new ConfigElement(lockConfig.getCategory(catName)).getChildElements());
            }
            list.add(new DummyConfigElement.DummyCategoryElement(
                "Lock-On",
                "mwccf.config.lockon",
                lockElements
            ));
        }

        return list;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        try {
            net.minecraftforge.common.config.ConfigManager.sync(MwccfMod.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
            if (Config.CLIENT != null && Config.CLIENT.getConfig() != null && Config.CLIENT.getConfig().hasChanged()) {
                Config.CLIENT.getConfig().save();
            }
            if (LockOnConfig.config != null && LockOnConfig.config.hasChanged()) {
                LockOnConfig.config.save();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
