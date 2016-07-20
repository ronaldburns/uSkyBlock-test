package me.rojo8399.uSkyBlock.command;

import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Collections;
import java.util.List;

/**
 * Talk to your party
 */
public class PartyTalkCommand extends IslandChatCommand {
    private final uSkyBlock plugin;

    public PartyTalkCommand(uSkyBlock plugin) {
        super(plugin, "partytalk|ptalk|ptk", "usb.party.talk", I18nUtil.tr("talk to your island party"));
        this.plugin = plugin;
    }

    @Override
    protected List<Player> getRecipients(Player player, IslandInfo islandInfo) {
        return islandInfo != null ? islandInfo.getOnlineMembers() : Collections.singletonList(player);
    }

    @Override
    protected String getFormat() {
        return plugin.getConfig().getString("options.party.chat-format", "&9PARTY &r{DISPLAYNAME} &f>&d {MESSAGE}");
    }
}
