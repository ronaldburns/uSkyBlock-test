package me.rojo8399.uSkyBlock.command.island;

import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.command.InviteHandler;

import java.util.Map;

public class AcceptRejectCommand extends RequirePlayerCommand {
    private final InviteHandler inviteHandler;

    public AcceptRejectCommand(InviteHandler inviteHandler) {
        super("accept|reject", "usb.party.join", I18nUtil.tr("accept/reject an invitation."));
        this.inviteHandler = inviteHandler;
    }

    @Override
    protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
        if (alias.equalsIgnoreCase("reject")) {
            if (inviteHandler.reject(player)) {
                player.sendMessage(I18nUtil.tr("\u00a7eYou have rejected the invitation to join an island."));
            } else {
                player.sendMessage(I18nUtil.tr("\u00a74You haven't been invited."));
            }
        } else if (alias.equalsIgnoreCase("accept")) {
            if (inviteHandler.accept(player)) {
                player.sendMessage(I18nUtil.tr("\u00a7eYou have accepted the invitation to join an island."));
            } else {
                player.sendMessage(I18nUtil.tr("\u00a74You haven't been invited."));
            }
        }
        return true;
    }
}
