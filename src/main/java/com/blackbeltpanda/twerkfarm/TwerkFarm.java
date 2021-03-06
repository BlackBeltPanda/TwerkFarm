package com.blackbeltpanda.twerkfarm;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TwerkFarm extends JavaPlugin {

    Settings settings;
    PlayerEvents events;

    @Override
    public void onEnable() {
        settings = new Settings(this);
        events = new PlayerEvents(settings);
        getServer().getPluginManager().registerEvents(events, this);
        Objects.requireNonNull(getCommand("twerkfarm")).setExecutor(new Commands(this));

        reload();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public void reload() {
        getServer().getScheduler().cancelTasks(this);
        settings.loadConfig();
        registerPermissions();
        scheduleSprintTask();
    }

    private void registerPermissions() {
        PluginManager pluginManager = getServer().getPluginManager();
        for (Material material : settings.GROW_WHITELIST) {
            Permission permission = new Permission(material.toString().toLowerCase(),
                    "Allows player to twerk to grow " + material, PermissionDefault.FALSE);
            permission.addParent("twerkfarm.twerk.*", true);
            if (pluginManager.getPermission(permission.getName()) == null) {
                pluginManager.addPermission(permission);
            }
        }
    }

    private void scheduleSprintTask() {
        if (settings.SPRINT_ENABLED) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->
                    Bukkit.getOnlinePlayers().stream().filter(Player::isSprinting).forEach(player -> events.twerk(player)),
                    settings.SPRINT_DELAY, settings.SPRINT_DELAY);
        }
    }
}
