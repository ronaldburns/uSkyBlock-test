package me.rojo8399.uSkyBlock.command.completion;

import me.rojo8399.uSkyBlock.bukkitUtils.command.completion.AbstractTabCompleter;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * TabCompleter for Biomes.
 */
public class BiomeTabCompleter extends AbstractTabCompleter {
    // TODO: 27/12/2014 - R4zorax: Perhaps this should be read from somewhere?
    private static final List<String> BIOMES = Arrays.asList("jungle","hell","sky","mushroom","ocean","swampland",
            "taiga","desert","forest","plains","extremehills");

    @Override
    protected List<String> getTabList(CommandSender commandSender, String term) {
        return filter(BIOMES, term);
    }
}
