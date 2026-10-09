package com.voltyx.mwccf.blood;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

/**
 * Admin test command to immediately maximize blood contamination level to 100% (1.0f).
 * Usage:
 *   /bloodmax
 *   /bloodmax <player>
 */
public class CommandBloodMax extends CommandBase {

    @Override
    public String getName() {
        return "bloodmax";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("maxblood");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/bloodmax [player]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Accessible in singleplayer / testing
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer target = null;
        if (args.length > 0 && server != null) {
            target = getPlayer(server, sender, args[0]);
        } else if (sender instanceof EntityPlayer) {
            target = (EntityPlayer) sender;
        } else if (server != null && !server.getPlayerList().getPlayers().isEmpty()) {
            target = server.getPlayerList().getPlayers().get(0);
        }

        if (target == null) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Целевой игрок не найден."));
            return;
        }

        BloodManager.setBloodLevel(target.getUniqueID(), 1.0f);
        sender.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "[Blood] " +
                TextFormatting.GOLD + "Уровень загрязнения кровью для " + TextFormatting.WHITE + target.getName() +
                TextFormatting.GOLD + " установлен на максимум (" + TextFormatting.RED + "100%" + TextFormatting.GOLD + ")!"));
    }
}
