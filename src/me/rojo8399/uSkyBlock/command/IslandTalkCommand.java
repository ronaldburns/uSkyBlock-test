package me.rojo8399.uSkyBlock.command;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Collections;
import java.util.List;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Island Talk
 */
public class IslandTalkCommand extends IslandChatCommand {
    private final uSkyBlock plugin;

    public IslandTalkCommand(uSkyBlock plugin) {
        super(plugin, "islandtalk|istalk|it", "usb.island.talk", tr("talk to players on your island"));
        this.plugin = plugin;
    }
    @Override
    protected List<Player> getRecipients(Player player, IslandInfo islandInfo) {
        if (plugin.isSkyWorld(player.getWorld())) {
            return WorldGuardHandler.getPlayersInRegion(plugin.getWorld(), WorldGuardHandler.getIslandRegionAt(player.getLocation()));
        }
        return Collections.emptyList();
    }

    @Override
    protected String getFormat() {
        return plugin.getConfig().getString("options.island.chat-format", "&9SKY &r{DISPLAYNAME} &f>&b {MESSAGE}");
    }
}
