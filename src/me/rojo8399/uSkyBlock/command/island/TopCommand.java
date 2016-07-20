package me.rojo8399.uSkyBlock.command.island;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class TopCommand extends AbstractCommand {
    private final uSkyBlock plugin;

    public TopCommand(uSkyBlock plugin) {
        super("top", "usb.island.topten", "?page", tr("display the top10 of islands"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        int page = 1;
        if (args.length == 1 && args[0].matches("\\d*")) {
            page = Integer.parseInt(args[0]);
        }
        plugin.getIslandLogic().showTopTen(sender, page);
        return true;
    }
}
