package com.voltyx.mwccf.render.doll;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandDoll extends CommandBase {

    @Override
    public String getName() {
        return "doll";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/doll [hud|print|reset|pos <x> <y> <z>|rot <x> <y> <z>|scale <s>|speed <s>|blend <0-4>|set <prop> <val>]";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true; // Клиентская команда
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Minecraft mc = Minecraft.getMinecraft();

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("hud"))) {
            DollSettings.debugHudEnabled = !DollSettings.debugHudEnabled;
            sender.sendMessage(new TextComponentString(
                TextFormatting.GOLD + "[Doll] " +
                (DollSettings.debugHudEnabled ? TextFormatting.GREEN + "HUD отладки ВКЛЮЧЕН (Навигация: стрелки, P для копирования)"
                                             : TextFormatting.RED + "HUD отладки ОТКЛЮЧЕН")));
            return;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("body") || sub.equals("armor") || sub.equals("layer")) {
            if (args.length >= 2) {
                String bodyAction = args[1].toLowerCase();
                if (bodyAction.equals("enable") || bodyAction.equals("on")) {
                    efw.biomeinfo.MwccfConfig.doll.enableDebugTweaker = true;
                    net.minecraftforge.common.config.ConfigManager.sync("mwccf", net.minecraftforge.common.config.Config.Type.INSTANCE);
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "[Doll Body] Дебаг-настройка ВКЛЮЧЕНА в конфиге. Нажмите F7 для открытия."));
                    return;
                }
                if (bodyAction.equals("disable") || bodyAction.equals("off")) {
                    efw.biomeinfo.MwccfConfig.doll.enableDebugTweaker = false;
                    DollBodySettings.debugHudEnabled = false;
                    net.minecraftforge.common.config.ConfigManager.sync("mwccf", net.minecraftforge.common.config.Config.Type.INSTANCE);
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "[Doll Body] Дебаг-настройка ВЫКЛЮЧЕНА в конфиге."));
                    return;
                }
                if (bodyAction.equals("save")) {
                    DollBodySettings.save();
                    return;
                }
                if (bodyAction.equals("dump") || bodyAction.equals("print")) {
                    DollBodySettings.dumpAndCopy(mc, mc.player);
                    return;
                }
                if (bodyAction.equals("reset")) {
                    String cat = DollBodySettings.getActiveCategoryName(mc.player);
                    DollBodySettings.OFFSETS.put(cat, new DollBodySettings.OffsetData(0f, 0f, 0f, 0f, 0f, 0f, 1.0f));
                    sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "[Doll Body] Сброшены настройки для " + cat));
                    return;
                }
                // Check if user specified a category name to select
                for (int i = 0; i < DollBodySettings.CATEGORIES.size(); i++) {
                    if (DollBodySettings.CATEGORIES.get(i).equalsIgnoreCase(bodyAction)) {
                        DollBodySettings.selectedCategoryIndex = i;
                        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "[Doll Body] Выбрана категория: " + DollBodySettings.CATEGORIES.get(i)));
                        return;
                    }
                }
            }
            DollBodySettings.toggleHud(mc);
            return;
        }

        if (sub.equals("print") || sub.equals("dump")) {
            DollSettings.dumpAndCopy(mc);
            return;
        }

        if (sub.equals("reset")) {
            DollSettings.resetAll();
            sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "[Doll] Все настройки куклы сброшены к исходным значениям."));
            return;
        }

        if (sub.equals("pos") && args.length >= 4) {
            try {
                DollSettings.posX = Float.parseFloat(args[1]);
                DollSettings.posY = Float.parseFloat(args[2]);
                DollSettings.posZ = Float.parseFloat(args[3]);
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + String.format(
                    "Позиция установлена: X=%.3f, Y=%.3f, Z=%.3f", DollSettings.posX, DollSettings.posY, DollSettings.posZ)));
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Ошибка: неверный формат чисел."));
            }
            return;
        }

        if (sub.equals("rot") && args.length >= 4) {
            try {
                DollSettings.rotX = Float.parseFloat(args[1]);
                DollSettings.rotY = Float.parseFloat(args[2]);
                DollSettings.rotZ = Float.parseFloat(args[3]);
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + String.format(
                    "Вращение установлено: X=%.1f, Y=%.1f, Z=%.1f", DollSettings.rotX, DollSettings.rotY, DollSettings.rotZ)));
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Ошибка: неверный формат чисел."));
            }
            return;
        }

        if (sub.equals("scale") && args.length >= 2) {
            try {
                DollSettings.scale = Math.max(0.01f, Float.parseFloat(args[1]));
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Масштаб установлен: " + DollSettings.scale));
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Ошибка: неверный формат числа."));
            }
            return;
        }

        if (sub.equals("speed") && args.length >= 2) {
            try {
                DollSettings.animSpeed = Math.max(0f, Float.parseFloat(args[1]));
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Скорость анимации установлена: " + DollSettings.animSpeed));
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Ошибка: неверный формат числа."));
            }
            return;
        }

        if (sub.equals("blend") && args.length >= 2) {
            try {
                int b = Integer.parseInt(args[1]);
                if (b < 0 || b > 4) b = 0;
                DollSettings.blendMode = b;
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Режим бленда: " + b + " (" + DollSettings.getBlendModeName(b) + ")"));
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Ошибка: укажите число 0..4"));
            }
            return;
        }

        if (sub.equals("set") && args.length >= 3) {
            String propName = args[1].toLowerCase();
            try {
                float val = Float.parseFloat(args[2]);
                boolean found = false;
                for (DollSettings.Property p : DollSettings.Property.values()) {
                    if (p.name().toLowerCase().replace("_", "").equals(propName.replace("_", ""))) {
                        DollSettings.setValue(p, val);
                        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + p.name + " = " + DollSettings.getValue(p)));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Неизвестный параметр: " + args[1]));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Ошибка: неверный формат числа."));
            }
            return;
        }

        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Использование: " + getUsage(sender)));
    }
}
