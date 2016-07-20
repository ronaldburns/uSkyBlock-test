package me.rojo8399.uSkyBlock.command.island;

import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

public class BanCommand extends RequireIslandCommand {
    public BanCommand(uSkyBlock plugin) {
        super(plugin, "ban|unban", "usb.island.ban", "player", I18nUtil.tr("ban/unban a player from your island."));
    }

    @Override
    protected boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args) {    	
    	if (args.length == 0) {
            player.sendMessage(I18nUtil.tr("\u00a7eThe following players are banned from warping to your island:"));
            player.sendMessage(I18nUtil.tr("\u00a74{0}", island.getBans()));
            player.sendMessage(I18nUtil.tr("\u00a7eTo ban/unban from your island, use /island ban <player>"));
            return true;
        } else if (args.length == 1) {
            String name = args[0];
            if (island.getMembers().contains(name)) {
                player.sendMessage(I18nUtil.tr("\u00a74You can't ban members. Remove them first!"));
                return true;
            }
            if (!island.hasPerm(player.getName(), "canKickOthers")) {
                player.sendMessage(I18nUtil.tr("\u00a74You do not have permission to kick/ban players."));
                return true;
            }
            if (!island.isBanned(name)) {
                island.banPlayer(name);
                player.sendMessage(I18nUtil.tr("\u00a7eYou have banned \u00a74{0}\u00a7e from warping to your island.", name));
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                if (offlinePlayer != null && offlinePlayer.isOnline()) {
                    offlinePlayer.getPlayer().sendMessage(I18nUtil.tr("\u00a7eYou have been \u00a7cBANNED\u00a7e from {0}\u00a7e''s island.", player.getDisplayName()));
                    if (plugin.locationIsOnIsland(player, offlinePlayer.getPlayer().getLocation())) {
                        plugin.spawnTeleport(offlinePlayer.getPlayer(), true);
                    }
                }
            } else {
                island.unbanPlayer(name);
                player.sendMessage(I18nUtil.tr("\u00a7eYou have unbanned \u00a7a{0}\u00a7e from warping to your island.", name));
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                if (offlinePlayer != null && offlinePlayer.isOnline()) {
                    offlinePlayer.getPlayer().sendMessage(I18nUtil.tr("\u00a7eYou have been \u00a7aUNBANNED\u00a7e from {0}\u00a7e''s island.", player.getDisplayName()));
                }
            }
            WorldGuardHandler.updateRegion(player, island);
            return true;
        }
        return false;
    }
}
