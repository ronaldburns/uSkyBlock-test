package me.rojo8399.uSkyBlock.command.island;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.marktr;
import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class MakeLeaderCommand extends RequireIslandCommand {
    public MakeLeaderCommand(uSkyBlock plugin) {
        super(plugin, "makeleader|transfer", "usb.island.create", "member", tr("transfer leadership to another member"));
    }

    @Override
    protected boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args) {
        if (args.length == 1) {
            String member = args[0];
            if (!island.getMembers().contains(member)) {
                player.sendMessage(tr("\u00a74You can only transfer ownership to party-members!"));
                return true;
            }
            if (island.getLeader().equals(member)) {
                player.sendMessage(tr("{0}\u00a7e is already leader of your island!", member));
                return true;
            }
            if (!island.isLeader(player)) {
                player.sendMessage(tr("\u00a74Only leader can transfer leadership!"));
                island.sendMessageToIslandGroup(true, marktr("{0} tried to take over the island!"), member);
                return true;
            }
            island.setupPartyLeader(member); // Promote member
            island.setupPartyMember(player.getName()); // Demote leader
            WorldGuardHandler.updateRegion(player, island);
            island.sendMessageToIslandGroup(true, tr("\u00a7bLeadership transferred by {0}\u00a7b to {1}", player.getDisplayName(), member));
            return true;
        }
        return false;
    }
}
