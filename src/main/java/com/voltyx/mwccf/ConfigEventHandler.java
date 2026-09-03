package com.voltyx.mwccf;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigEventHandler {

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        // Проверяем, что изменили именно наш конфиг
        if (event.getModID().equals("mwccf")) {

            // Заставляем Forge синхронизировать изменения из меню в файл и в память
            ConfigManager.sync("mwccf", Config.Type.INSTANCE);

            // Даем команду нашему менеджеру заново прочитать список мобов
            AdvancedHeadshotManager.reloadConfig();

            System.out.println("[MWCCF] Конфиг успешно перезагружен!");
        }
    }
}