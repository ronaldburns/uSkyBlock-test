package me.rojo8399.uSkyBlock.command.island;

import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.Settings;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

public class RestartCommand extends RequireIslandCommand {
    public RestartCommand(uSkyBlock plugin) {
        super(plugin, "restart|reset", I18nUtil.tr("delete your island and start a new one."));
    }

    @Override
    protected boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args) {
        if (island.getPartySize() > 1) {
            if (!island.isLeader(player)) {
                player.sendMessage(I18nUtil.tr("\u00a74Only the owner may restart this island. Leave this island in order to start your own (/island leave)."));
            } else {
                player.sendMessage(I18nUtil.tr("\u00a7eYou must remove all players from your island before you can restart it (/island kick <player>). See a list of players currently part of your island using /island party."));
            }
            return true;
        }
        int cooldown = plugin.getCooldownHandler().getCooldown(player, "restart");
        if (cooldown > 0) {
            player.sendMessage(I18nUtil.tr("\u00a7cYou can restart your island in {0} seconds.", cooldown));
            return true;
        } else {
            if (pi.isIslandGenerating()) {
                player.sendMessage(I18nUtil.tr("\u00a7cYour island is in the process of generating, you cannot restart now."));
                return true;
            }

            if (plugin.getConfirmHandler().checkCommand(player, "/is restart")) {
                plugin.getCooldownHandler().resetCooldown(player, "restart", Settings.general_cooldownRestart);
                return plugin.restartPlayerIsland(player, pi.getIslandLocation());
            } else {
                player.sendMessage(I18nUtil.tr("\u00a7eNOTE: Your entire island and all your belongings will be RESET!"));
                return true;
            }
        }
    }
}
