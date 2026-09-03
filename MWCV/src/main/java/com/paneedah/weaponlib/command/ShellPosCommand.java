package com.paneedah.weaponlib.command;

import com.paneedah.weaponlib.render.shells.ShellPositionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShellPosCommand extends CommandBase {

    @Override
    public String getName() {
        return "shellpos";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/shellpos [toggle/save/reload/print/reset]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || args[0].equalsIgnoreCase("toggle")) {
            ShellPositionManager.isAdjusterOpen = !ShellPositionManager.isAdjusterOpen;
            if (ShellPositionManager.isAdjusterOpen) {
                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[MWC ShellPos] " + TextFormatting.GREEN + "Настройщик позиций гильз ВКЛЮЧЕН!"));
                sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Управление: " + TextFormatting.WHITE + "[Стрелки] X/Y, [PgUp/PgDn или [/]] Z, [TAB] Режим, [R] Категория, [Enter] Сохранить"));
            } else {
                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[MWC ShellPos] " + TextFormatting.RED + "Настройщик позиций гильз ВЫКЛЮЧЕН."));
            }
        } else if (args[0].equalsIgnoreCase("save")) {
            ShellPositionManager.saveConfig();
            ShellPositionManager.printCoordinatesToChat();
        } else if (args[0].equalsIgnoreCase("reload")) {
            ShellPositionManager.loadConfig();
            sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[MWC ShellPos] " + TextFormatting.GREEN + "Конфигурация перезагружена из файла!"));
            ShellPositionManager.printCoordinatesToChat();
        } else if (args[0].equalsIgnoreCase("print") || args[0].equalsIgnoreCase("list")) {
            ShellPositionManager.printCoordinatesToChat();
        } else if (args[0].equalsIgnoreCase("reset")) {
            ShellPositionManager.rifleFirstPerson = new net.minecraft.util.math.Vec3d(-0.40, -0.20, 1.35);
            ShellPositionManager.rifleThirdPerson = new net.minecraft.util.math.Vec3d(-0.20, -0.25, 0.75);
            ShellPositionManager.rifleThirdPersonAim = new net.minecraft.util.math.Vec3d(-0.20, -0.20, 0.70);
            ShellPositionManager.otherFirstPerson = new net.minecraft.util.math.Vec3d(-0.10, -0.20, 1.25);
            ShellPositionManager.otherThirdPerson = new net.minecraft.util.math.Vec3d(-0.15, -0.20, 0.65);
            ShellPositionManager.otherThirdPersonAim = new net.minecraft.util.math.Vec3d(-0.20, -0.15, 0.90);
            ShellPositionManager.saveConfig();
            sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[MWC ShellPos] " + TextFormatting.YELLOW + "Сброшено к значениям по умолчанию!"));
            ShellPositionManager.printCoordinatesToChat();
        }
    }
}
