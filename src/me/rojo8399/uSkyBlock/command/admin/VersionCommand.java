package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

/**
 * Displays detailed version information.
 */
public class VersionCommand extends AbstractCommand {
    private final uSkyBlock plugin;

    public VersionCommand(uSkyBlock plugin) {
        super("version|v", "usb.admin.version", I18nUtil.tr("displays version information"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        sender.sendMessage(plugin.getVersionInfo(true).split("\n"));
        return true;
    }

}
