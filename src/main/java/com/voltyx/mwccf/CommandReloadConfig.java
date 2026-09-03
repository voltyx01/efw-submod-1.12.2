package com.voltyx.mwccf;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

public class CommandReloadConfig extends CommandBase {

    @Override
    public String getName() {
        // Название команды, которое ты будешь вводить в чат (например: /mwccf_reload)
        return "mwccf_reload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mwccf_reload";
    }

    @Override
    public int getRequiredPermissionLevel() {
        // Уровень 2 означает, что команда доступна только операторам (админам/OP)
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // 1. Заставляем Forge принудительно прочитать файл mwccf_config.cfg с диска
        ConfigManager.sync("mwccf", Config.Type.INSTANCE);

        // 2. Обновляем кэш хитбоксов (чтобы новые координаты мобов сразу применились)
        AdvancedHeadshotManager.reloadConfig();

        // 3. Отправляем зеленое сообщение в чат тому, кто ввел команду
        sender.sendMessage(new TextComponentString("§a[MWCCF] Конфигурация успешно перезагружена с диска!"));
    }
}