package me.rojo8399.uSkyBlock.handler.asyncworldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.rojo8399.uSkyBlock.player.PlayerPerk;

import java.io.File;

/**
 * Interface for various AWE version-adaptors.
 */
public interface AWEAdaptor {
    void onEnable(Plugin plugin);

    void registerCompletion(Player player);

    void loadIslandSchematic(File file, Location origin, PlayerPerk playerPerk);

    void onDisable(Plugin plugin);

    EditSession createEditSession(BukkitWorld world, int maxBlocks);
}
