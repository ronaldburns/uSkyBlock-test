package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import me.rojo8399.uSkyBlock.imports.impl.ImportTabCompleter;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Command for importing external formats.
 */
public class ImportCommand extends AbstractCommand {

    private final ImportTabCompleter completer;

    public ImportCommand() {
        super("import", "usb.admin.import", "format", tr("imports players and islands from other formats"));
        completer = new ImportTabCompleter();
    }

    @Override
    public TabCompleter getTabCompleter() {
        return completer;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        if (args.length == 1) {
            uSkyBlock.getInstance().getPlayerImporter().importUSB(sender, args[args.length - 1]);
            return true;
        }
        return false;
    }
}
