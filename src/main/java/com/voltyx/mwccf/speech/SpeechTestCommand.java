package com.voltyx.mwccf.speech;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class SpeechTestCommand extends CommandBase {
    @Override
    public String getName() {
        return "speechtest";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/speechtest";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        SpeechServerHandler.sendPersonal(player, "gui.mwccf.speech.test_personal");
    }
}
