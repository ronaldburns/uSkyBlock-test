package me.rojo8399.uSkyBlock.imports.impl;

import me.rojo8399.uSkyBlock.bukkitUtils.command.completion.AbstractTabCompleter;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.List;

/**
 * Tab support for the registered importers.
 */
public class ImportTabCompleter extends AbstractTabCompleter {
    @Override
    protected List<String> getTabList(CommandSender commandSender, String term) {
        return uSkyBlock.getInstance().getPlayerImporter().getImporterNames();
    }
}
