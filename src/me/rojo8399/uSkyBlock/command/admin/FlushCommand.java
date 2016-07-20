package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class FlushCommand extends AbstractCommand {
    private final uSkyBlock plugin;

    public FlushCommand(uSkyBlock plugin) {
        super("flush", "usb.admin.cache", tr("flushes all caches to files"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        long flushedIslands = plugin.getIslandLogic().flushCache();
        long flushedPlayers = plugin.getPlayerLogic().flushCache();
        long flushedChallenges = plugin.getChallengeLogic().flushCache();
        sender.sendMessage(tr("\u00a7eFlushed \u00a7a{0} islands\u00a7e, \u00a7b{1} players and \u00a76{2} challenge-completions.", flushedIslands, flushedPlayers, flushedChallenges));
        return true;
    }
}
