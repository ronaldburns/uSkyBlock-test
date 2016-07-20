package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

/**
 * Reloads the config-files for USB.
 */
public class ReloadCommand extends AbstractCommand {
    public ReloadCommand() {
        super("reload", "usb.admin.reload", I18nUtil.tr("reload configuration from file."));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        uSkyBlock.getInstance().reloadConfig();
        sender.sendMessage(I18nUtil.tr("\u00a7eConfiguration reloaded from file."));
        return true;
    }
}
