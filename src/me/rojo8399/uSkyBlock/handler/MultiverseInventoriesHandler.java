package me.rojo8399.uSkyBlock.handler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import me.rojo8399.uSkyBlock.handler.multiverseinventories.MultiverseInventoriesAdaptor;

/**
 * Handler for accessing Multiverse-Inventories, if enabled.
 */
public enum MultiverseInventoriesHandler {;
    public static void linkWorlds(World... worlds) {
        if (isMVInv()) {
            MultiverseInventoriesAdaptor.linkWorlds(worlds);
        }
    }
    public static boolean isMVInv() {
        return Bukkit.getPluginManager().isPluginEnabled("Multiverse-Inventories");
    }
}
