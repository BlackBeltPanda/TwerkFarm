package com.blackbeltpanda.twerkfarm;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Settings {

    public boolean SNEAK_ENABLED;
    public boolean SPRINT_ENABLED;
    public long SPRINT_DELAY;

    public int RANGE;
    public int CHANCE;
    public boolean INSTANT_GROW;

    public List<String> WORLD_BLACKLIST = new ArrayList<>();
    public final List<Material> GROW_WHITELIST = new ArrayList<>();

    public boolean GROWING_PARTICLE_ENABLED;
    public Particle GROWING_PARTICLE;
    public int GROWING_PARTICLE_COUNT;
    public double GROWING_PARTICLE_OFFSET_X;
    public double GROWING_PARTICLE_OFFSET_Y;
    public double GROWING_PARTICLE_OFFSET_Z;
    public double GROWING_PARTICLE_EXTRA;

    public boolean GROWING_SOUND_ENABLED;
    public Sound GROWING_SOUND;
    public int GROWING_SOUND_VOLUME;
    public int GROWING_SOUND_PITCH;

    public boolean GROWN_PARTICLE_ENABLED;
    public Particle GROWN_PARTICLE;
    public int GROWN_PARTICLE_COUNT;
    public double GROWN_PARTICLE_OFFSET_X;
    public double GROWN_PARTICLE_OFFSET_Y;
    public double GROWN_PARTICLE_OFFSET_Z;
    public double GROWN_PARTICLE_EXTRA;

    public boolean GROWN_SOUND_ENABLED;
    public Sound GROWN_SOUND;
    public int GROWN_SOUND_VOLUME;
    public int GROWN_SOUND_PITCH;

    final Plugin plugin;

    public Settings(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        SNEAK_ENABLED = config.getBoolean("sneak_enabled");
        SPRINT_ENABLED = config.getBoolean("sprint_enabled");
        SPRINT_DELAY = config.getLong("sprint_delay");

        RANGE = config.getInt("range");
        CHANCE = config.getInt("chance");
        INSTANT_GROW = config.getBoolean("instant_grow");

        WORLD_BLACKLIST = config.getStringList("world_blacklist");
        for (String material : config.getStringList("grow_whitelist")) {
            GROW_WHITELIST.add(Material.valueOf(material.toUpperCase()));
        }

        GROWING_PARTICLE_ENABLED = config.getBoolean("growing_effects.particle.enabled");
        GROWING_PARTICLE = Particle.valueOf(Objects.requireNonNull(config.getString("growing_effects.particle.type")).toUpperCase());
        GROWING_PARTICLE_COUNT = config.getInt("growing_effects.particle.amount");
        GROWING_PARTICLE_OFFSET_X = config.getDouble("growing_effects.particle.offset_x");
        GROWING_PARTICLE_OFFSET_Y = config.getDouble("growing_effects.particle.offset_y");
        GROWING_PARTICLE_OFFSET_Z = config.getDouble("growing_effects.particle.offset_z");
        GROWING_PARTICLE_EXTRA = config.getDouble("growing_effects.particle.extra");

        GROWING_SOUND_ENABLED = config.getBoolean("growing_effects.sound.enabled");
        GROWING_SOUND = Sound.valueOf(Objects.requireNonNull(config.getString("growing_effects.sound.type")).toUpperCase());
        GROWING_SOUND_VOLUME = config.getInt("growing_effects.sound.volume");
        GROWING_SOUND_PITCH = config.getInt("growing_effects.sound.pitch");

        GROWN_PARTICLE_ENABLED = config.getBoolean("grown_effects.particle.enabled");
        GROWN_PARTICLE = Particle.valueOf(Objects.requireNonNull(config.getString("grown_effects.particle.type")).toUpperCase());
        GROWN_PARTICLE_COUNT = config.getInt("grown_effects.particle.amount");
        GROWN_PARTICLE_OFFSET_X = config.getDouble("grown_effects.particle.offset_x");
        GROWN_PARTICLE_OFFSET_Y = config.getDouble("grown_effects.particle.offset_y");
        GROWN_PARTICLE_OFFSET_Z = config.getDouble("grown_effects.particle.offset_z");
        GROWN_PARTICLE_EXTRA = config.getDouble("grown_effects.particle.extra");

        GROWN_SOUND_ENABLED = config.getBoolean("grown_effects.sound.enabled");
        GROWN_SOUND = Sound.valueOf(Objects.requireNonNull(config.getString("grown_effects.sound.type")).toUpperCase());
        GROWN_SOUND_VOLUME = config.getInt("grown_effects.sound.volume");
        GROWN_SOUND_PITCH = config.getInt("grown_effects.sound.pitch");
    }

}
