package com.teamderpy.shouldersurfing.lockon;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import com.teamderpy.shouldersurfing.lockon.LockOnConfig;
public class LockOnConfigGui extends GuiConfig {
    public LockOnConfigGui(GuiScreen parent) {
        super(parent, new ArrayList<>(), "lockon", false, false, "Lock On Settings");
        // Заполните список элементов из LockOnConfig.config
    }
}