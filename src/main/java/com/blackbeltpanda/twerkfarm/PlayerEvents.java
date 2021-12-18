package com.blackbeltpanda.twerkfarm;

import com.blackbeltpanda.twerkfarm.Settings;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class PlayerEvents implements Listener {

    Settings settings;
    public PlayerEvents(Settings settings) {
        this.settings = settings;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerSneak(final PlayerToggleSneakEvent event) {
        if (settings.SNEAK_ENABLED && event.getPlayer().hasPermission("twerkfortrees.twerk")) {
            twerk(event.getPlayer());
        }
    }

    public void twerk(Player player) {
        if (settings.WORLD_BLACKLIST.contains(player.getWorld().getName())) {
            return;
        }
        List<Block> found = scanForBlocks(player);
        if (!found.isEmpty()) {
            for (Block block : found) {
                if (ThreadLocalRandom.current().nextFloat() < settings.CHANCE / 100f) {
                    boolean grown = settings.INSTANT_GROW;
                    if (!settings.INSTANT_GROW) {
                        if (block.getState().getBlockData() instanceof Sapling sapling) {
                            grown = sapling.getStage() >= sapling.getMaximumStage();
                        }
                        else if (block.getState().getBlockData() instanceof Ageable ageable) {
                            grown = ageable.getAge() >= ageable.getMaximumAge();
                        }
                    }
                    bonemeal(block, grown);
                }
            }
        }
    }

    // Scan all blocks in range of player for whitelisted blocks
    private List<Block> scanForBlocks(Player player) {
        List<Block> found = new ArrayList<>();
        Location sourceLocation = player.getLocation();
        int radius = settings.RANGE/2;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block check = player.getWorld().getBlockAt(sourceLocation.getBlockX() + x, sourceLocation.getBlockY() + y, sourceLocation.getBlockZ() + z);
                    if (settings.GROW_WHITELIST.contains(check.getType()) && player.hasPermission("twerkfortrees.twerk." + check.getType())) {
                        found.add(check);
                    }
                }
            }
        }
        return found;
    }

    // Simulate bonemealing the block
    private void bonemeal(Block block, boolean grown) {
        // If the block hasn't fully grown, bonemeal it once
        if (!grown) {
            block.applyBoneMeal(BlockFace.UP);
            if (settings.GROWING_PARTICLE_ENABLED) {
                block.getWorld().spawnParticle(
                        settings.GROWING_PARTICLE, block.getLocation().add(0.5, 0.5, 0.5), settings.GROWING_PARTICLE_COUNT,
                        settings.GROWING_PARTICLE_OFFSET_X, settings.GROWING_PARTICLE_OFFSET_Y, settings.GROWING_PARTICLE_OFFSET_Z, settings.GROWING_PARTICLE_EXTRA);
            }
            if (settings.GROWING_SOUND_ENABLED) {
                block.getWorld().playSound(block.getLocation(), settings.GROWING_SOUND, SoundCategory.BLOCKS,
                        settings.GROWING_SOUND_VOLUME / 100f, settings.GROWING_SOUND_PITCH / 100f);
            }
        }
        // If it is fully grown, or instantGrow is true, bonemeal the heck out of it to fully grow it
        else {
            if (settings.GROWN_PARTICLE_ENABLED) {
                block.getWorld().spawnParticle(
                        settings.GROWN_PARTICLE, block.getLocation().add(0.5, 0.5, 0.5), settings.GROWN_PARTICLE_COUNT,
                        settings.GROWN_PARTICLE_OFFSET_X, settings.GROWN_PARTICLE_OFFSET_Y, settings.GROWN_PARTICLE_OFFSET_Z, settings.GROWN_PARTICLE_EXTRA);
            }
            if (settings.GROWN_SOUND_ENABLED) {
                block.getWorld().playSound(block.getLocation(), settings.GROWN_SOUND, SoundCategory.BLOCKS,
                        settings.GROWN_SOUND_VOLUME / 100f, settings.GROWN_SOUND_PITCH / 100f);
            }
            IntStream.range(0, 100).forEach($ -> block.applyBoneMeal(BlockFace.UP));
        }
    }

}
