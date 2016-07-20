package me.rojo8399.uSkyBlock.island.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.rojo8399.uSkyBlock.api.event.uSkyBlockEvent;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.HashSet;
import java.util.Set;

public class RecalculateRunnable extends BukkitRunnable {
    private final uSkyBlock plugin;

    public RecalculateRunnable(uSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Set<String> recalcIslands = new HashSet<>();
        for (Player player : plugin.getWorld().getPlayers()) {
            if (player.isOnline() && plugin.playerIsOnIsland(player)) {
                recalcIslands.add(plugin.getPlayerInfo(player).locationForParty());
            }
        }
        if (!recalcIslands.isEmpty()) {
            RecalculateTopTen runnable = new RecalculateTopTen(plugin, recalcIslands, new Runnable() {
                @Override
                public void run() {
                    plugin.fireChangeEvent(new uSkyBlockEvent(null, plugin, uSkyBlockEvent.Cause.RANK_UPDATED));
                }
            });
            runnable.runTask(plugin);
        }
    }
}
