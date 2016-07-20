package me.rojo8399.uSkyBlock.command.completion;

import me.rojo8399.uSkyBlock.bukkitUtils.command.completion.AbstractTabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.List;

/**
 * TabCompleter for challenge-names
 */
public class AvailableChallengeTabCompleter extends AbstractTabCompleter {
    @Override
    protected List<String> getTabList(CommandSender commandSender, String term) {
        PlayerInfo pi = null;
        if (commandSender instanceof Player) {
            pi = uSkyBlock.getInstance().getPlayerInfo((Player)commandSender);
            if (pi != null) {
                uSkyBlock.getInstance().getChallengeLogic().getAvailableChallengeNames(pi);
            }
        }
        return uSkyBlock.getInstance().getChallengeLogic().getAllChallengeNames();
    }
}
