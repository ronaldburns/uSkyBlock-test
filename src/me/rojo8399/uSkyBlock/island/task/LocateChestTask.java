package me.rojo8399.uSkyBlock.island.task;

import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.rojo8399.uSkyBlock.uSkyBlock;
import me.rojo8399.uSkyBlock.util.LocationUtil;
import me.rojo8399.uSkyBlock.util.TimeUtil;

/**
 * A task that looks for a chest at an island location.
 */
public class LocateChestTask extends BukkitRunnable {
    private final uSkyBlock plugin;
    private final Player player;
    private final Location islandLocation;
    private final Runnable onCompletion;
    private final long timeout;
    private final long heartBeatTicks;

    private long tStart;

    public LocateChestTask(uSkyBlock plugin, Player player, Location islandLocation, Runnable onCompletion) {
        this.plugin = plugin;
        this.player = player;
        this.islandLocation = islandLocation;
        this.onCompletion = onCompletion;
        timeout = System.currentTimeMillis() + TimeUtil.stringAsMillis(plugin.getConfig().getString("asyncworldedit.watchDog.timeout", "5m"));
        heartBeatTicks = TimeUtil.millisAsTicks(plugin.getConfig().getInt("asyncworldedit.watchDog.heartBeatMs", 2000));
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        if (tStart == 0) {
            tStart = now;
        }
        // Check for completion
        Location chestLocation = LocationUtil.findChestLocation(islandLocation);
        if (chestLocation == null && now < timeout) {
            Bukkit.getScheduler().runTaskLater(plugin, this, heartBeatTicks);
        } else {
            if (player != null && player.isOnline() && now >= timeout) {
                player.sendMessage(I18nUtil.tr("\u00a7cWatchdog!\u00a79 Unable to locate a chest within {0}, bailing out.", TimeUtil.millisAsString(timeout)));
            }
            LocationUtil.loadChunkAt(chestLocation);
            if (onCompletion != null) {
                onCompletion.run();
            }
        }
    }
}
