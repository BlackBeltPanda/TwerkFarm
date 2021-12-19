package com.blackbeltpanda.twerkfarm;

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

public record PlayerEvents(Settings settings) implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerSneak(final PlayerToggleSneakEvent event) {
        if (settings.SNEAK_ENABLED && event.getPlayer().hasPermission("twerkfarm.twerk")) {
            twerk(event.getPlayer());
        }
    }

    public void twerk(Player player) {
        if (!settings.WORLD_BLACKLIST.contains(player.getWorld().getName())) {
            scanForBlocks(player).stream().filter($ -> ThreadLocalRandom.current().nextFloat() < settings.CHANCE / 100f).forEach(this::bonemeal);
        }
    }

    // Scan all blocks in range of player for whitelisted blocks
    private List<Block> scanForBlocks(Player player) {
        List<Block> found = new ArrayList<>();
        Location sourceLocation = player.getLocation();
        // I wish there was a better way to do this
        for (int x = -settings.RANGE; x < settings.RANGE; x++) {
            for (int y = -settings.RANGE; y < settings.RANGE; y++) {
                for (int z = -settings.RANGE; z < settings.RANGE; z++) {
                    Block check = player.getWorld().getBlockAt(sourceLocation.getBlockX() + x, sourceLocation.getBlockY() + y, sourceLocation.getBlockZ() + z);
                    if (settings.GROW_WHITELIST.contains(check.getType()) && player.hasPermission("twerkfarm.twerk." + check.getType().toString().toLowerCase())) {
                        // Make sure it isn't already fully grown
                        if (!(check.getState().getBlockData() instanceof Ageable ageable && ageable.getAge() >= ageable.getMaximumAge())) {
                            found.add(check);
                        }
                    }
                }
            }
        }
        return found;
    }

    // Simulate bonemealing the block
    private void bonemeal(Block block) {
        boolean fullyGrown = false;
        if (settings().INSTANT_GROW) {
            // Lots of bonemeal to make sure it fully grows
            IntStream.range(0, 10).forEach($ -> block.applyBoneMeal(BlockFace.UP));
            fullyGrown = true;
        }
        else {
            // Saplings turn into trees after they hit their max age, so we need to check for that
            if (block.getState().getBlockData() instanceof Sapling sapling) {
                // If it is max age, turn it into a tree
                if (sapling.getStage() >= sapling.getMaximumStage()) {
                    // Lots of bonemeal to make sure it fully grows
                    IntStream.range(0, 10).forEach($ -> block.applyBoneMeal(BlockFace.UP));
                    fullyGrown = true;
                }
                else {
                    block.applyBoneMeal(BlockFace.UP);
                }
            }
            // Everything else should just bonemeal normally
            else {
                block.applyBoneMeal(BlockFace.UP);
                // After bonemealing it, gotta check if it hit its max age as a result
                fullyGrown = block.getState().getBlockData() instanceof Ageable ageable && ageable.getAge() >= ageable.getMaximumAge();
            }
        }
        // Effects if the block hasn't fully grown
        if (!fullyGrown) {
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
        // Effects if it has fully grown
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
        }
    }
}
