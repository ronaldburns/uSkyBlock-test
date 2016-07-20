package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Re-generates the topten.
 */
public class GenTopTenCommand extends AbstractCommand {
    private final uSkyBlock plugin;

    public GenTopTenCommand(uSkyBlock plugin) {
        super("topten", "usb.mod.topten", tr("manually update the top 10 list"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, String alias, Map<String, Object> data, String... args) {
        sender.sendMessage(tr("\u00a7eGenerating the Top Ten list"));
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getIslandLogic().generateTopTen(sender);
                plugin.getIslandLogic().showTopTen(sender, 1);
                sender.sendMessage(tr("\u00a7eFinished generation of the Top Ten list"));
            }
        });
        return true;
    }
}
