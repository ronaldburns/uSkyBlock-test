package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Command for showing the config-gui.
 */
public class ConfigGUICommand extends AbstractCommand {

    private final uSkyBlock plugin;

    public ConfigGUICommand(uSkyBlock plugin) {
        super("config|c", "usb.admin.config", tr("open GUI for config"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        if (sender instanceof Player) {
            plugin.getConfigMenu().showMenu((Player) sender,
                    args.length > 0 && args[0].matches("[0-9]*") ? Integer.parseInt(args[0], 10) : 1
            );
            return true;
        }
        return false;
    }
}
