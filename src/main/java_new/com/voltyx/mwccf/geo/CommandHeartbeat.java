package com.voltyx.mwccf.geo;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandHeartbeat extends CommandBase {
    @Override
    public String getName() {
        return "heartbeat";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/heartbeat <bpm>";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true; // Any player can use it client-side
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + getUsage(sender)));
            return;
        }

        try {
            int bpm = Integer.parseInt(args[0]);
            HeartbeatManager.currentBPM = Math.max(0, bpm);
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Heartbeat set to " + (int)HeartbeatManager.currentBPM + " BPM."));
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid number format."));
        }
    }
}
