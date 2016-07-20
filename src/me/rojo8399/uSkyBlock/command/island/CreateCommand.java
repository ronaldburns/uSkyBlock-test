package me.rojo8399.uSkyBlock.command.island;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;
import me.rojo8399.uSkyBlock.util.LocationUtil;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class CreateCommand extends RequirePlayerCommand {
    private final uSkyBlock plugin;

    public CreateCommand(uSkyBlock plugin) {
        super("create|c", "usb.island.create", tr("create an island"));
        this.plugin = plugin;
    }

    @Override
    protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
        PlayerInfo pi = plugin.getPlayerInfo(player);
        int cooldown = plugin.getCooldownHandler().getCooldown(player, "restart");
        if (LocationUtil.isEmptyLocation(pi.getIslandLocation()) && cooldown == 0) {
            if (pi.isIslandGenerating()) {
                player.sendMessage(tr("\u00a7cYour island is in the process of generating, you cannot create now."));
                return true;
            }
            plugin.createIsland(player, pi);
        } else if (pi.getHasIsland()) {
            IslandInfo island = plugin.getIslandInfo(pi);
            if (island.isLeader(player)) {
                player.sendMessage(tr("\u00a74Island found!" +
                        "\u00a7e You already have an island. If you want a fresh island, type" +
                        "\u00a7b /is restart\u00a7e to get one"));
            } else {
                player.sendMessage(tr("\u00a74Island found!" +
                        "\u00a7e You are already a member of an island. To start your own, first" +
                        "\u00a7b /is leave"));
            }
        } else {
            player.sendMessage(tr("\u00a7eYou can create a new island in {0,number,#} seconds.", cooldown));
        }
        return true;
    }
}
