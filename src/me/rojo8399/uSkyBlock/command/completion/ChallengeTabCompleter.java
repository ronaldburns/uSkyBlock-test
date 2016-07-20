package me.rojo8399.uSkyBlock.command.completion;

import me.rojo8399.uSkyBlock.bukkitUtils.command.completion.AbstractTabCompleter;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.List;

/**
 * Lists all registered challenges.
 */
public class ChallengeTabCompleter extends AbstractTabCompleter {
    @Override
    protected List<String> getTabList(CommandSender commandSender, String term) {
        return uSkyBlock.getInstance().getChallengeLogic().getAllChallengeNames();
    }
}
