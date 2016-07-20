package me.rojo8399.uSkyBlock.command.completion;

import me.rojo8399.uSkyBlock.bukkitUtils.command.completion.AbstractTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab completion for players.
 */
public class OnlinePlayerTabCompleter extends AbstractTabCompleter {
    @Override
    public List<String> getTabList(CommandSender commandSender, String term) {
        List<String> list = new ArrayList<>();
        // TODO: 27/12/2014 - R4zorax: What about display-names?
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            list.add(player.getName());
        }
        return list;
    }
}
