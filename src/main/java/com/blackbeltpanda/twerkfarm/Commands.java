package com.blackbeltpanda.twerkfarm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public record Commands(TwerkFarm plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload" -> {
                    plugin.reloadConfig();
                    plugin.reload();
                    sender.sendMessage("TwerkFarm has been reloaded.");
                    return true;
                }
                case "version" -> {
                    sender.sendMessage("TwerkFarm version: " + plugin.getDescription().getVersion());
                    return true;
                }
            }
        }
        return false;
    }
}
