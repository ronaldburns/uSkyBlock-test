package me.rojo8399.uSkyBlock.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.handler.actionbarapi.ActionBarAPIAdaptor;
import me.rojo8399.uSkyBlock.handler.titlemanager.TitleManagerAdaptor;

/**
 * Static handler allowing for soft-depend.
 */
public enum ActionBarHandler {;
    public static boolean isEnabled() {
        return isActionBarAPI() || isTitleManager();
    }

    private static boolean isTitleManager() {
        return Bukkit.getPluginManager().isPluginEnabled("TitleManager");
    }

    public static boolean isActionBarAPI() {
        return Bukkit.getPluginManager().isPluginEnabled("ActionBarAPI");
    }

    public static void sendActionBar(Player player, String message) {
        if (isActionBarAPI()) {
            ActionBarAPIAdaptor.sendActionBar(player, message);
        } else if (isTitleManager()) {
            TitleManagerAdaptor.sendActionBar(player, message);
        }
    }
}
