package me.rojo8399.uSkyBlock.command.island;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Common command for all the /is commands that require an island.
 */
public abstract class RequireIslandCommand extends RequirePlayerCommand {
    protected final uSkyBlock plugin;

    protected RequireIslandCommand(uSkyBlock plugin, String name, String description) {
        this(plugin, name, null, description);
    }

    protected RequireIslandCommand(uSkyBlock plugin, String name, String perm, String description) {
        this(plugin, name, perm, null,description);
    }

    protected RequireIslandCommand(uSkyBlock plugin, String name, String perm, String param, String description) {
        super(name, perm, param, description);
        this.plugin = plugin;
    }

    protected uSkyBlock getPlugin() {
        return plugin;
    }

    protected abstract boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args);

    @Override
    protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
        PlayerInfo playerInfo = plugin.getPlayerInfo(player);
        if (playerInfo != null && playerInfo.getHasIsland()) {
            IslandInfo islandInfo = plugin.getIslandInfo(playerInfo);
            return doExecute(alias, player, playerInfo, islandInfo, data, args);
        } else {
            player.sendMessage(tr("\u00a74No Island. \u00a7eUse \u00a7b/is create\u00a7e to get one"));
        }
        return false;
    }
}
