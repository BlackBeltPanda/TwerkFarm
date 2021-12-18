package com.blackbeltpanda.twerkfarm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    TwerkFarm plugin;

    public Commands(TwerkFarm plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.reload();
                sender.sendMessage("TwerkFarm has been reloaded.");
                return true;
            }
        }
        return false;
    }
}
