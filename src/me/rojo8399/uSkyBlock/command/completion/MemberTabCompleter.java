package me.rojo8399.uSkyBlock.command.completion;

import me.rojo8399.uSkyBlock.bukkitUtils.command.completion.AbstractTabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.List;

/**
 * Only list members of your current party.
 */
public class MemberTabCompleter implements TabCompleter {
    private final uSkyBlock plugin;

    public MemberTabCompleter(uSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            PlayerInfo playerInfo = plugin.getPlayerInfo((Player) sender);
            if (playerInfo != null && playerInfo.getHasIsland()) {
                IslandInfo islandInfo = plugin.getIslandInfo(playerInfo);
                if (islandInfo != null) {
                    String member = args.length > 0 ? args[args.length-1] : "";
                    return AbstractTabCompleter.filter(islandInfo.getMembers(), member);
                }
            }
        }
        return null;
    }
}
