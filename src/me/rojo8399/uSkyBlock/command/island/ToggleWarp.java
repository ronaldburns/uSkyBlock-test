package me.rojo8399.uSkyBlock.command.island;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.marktr;
import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class ToggleWarp extends RequireIslandCommand {
    public ToggleWarp(uSkyBlock plugin) {
        super(plugin, "togglewarp|tw", "usb.extra.addwarp", tr("enable/disable warping to your island."));
    }

    @Override
    protected boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args) {
        if (island.hasPerm(player, "canToggleWarp")) {
            if (!island.hasWarp()) {
                if (island.isLocked()) {
                    player.sendMessage(tr("\u00a74Your island is locked. You must unlock it before enabling your warp."));
                    return true;
                }
                island.sendMessageToIslandGroup(true, marktr("\u00a7b{0}\u00a7d activated the island warp."), player.getName());
                island.setWarpActive(true);
            } else {
                island.sendMessageToIslandGroup(true, marktr("\u00a7b{0}\u00a7d deactivated the island warp."), player.getName());
                island.setWarpActive(false);
            }
        } else {
            player.sendMessage(tr("\u00a7cYou do not have permission to enable/disable your island's warp!"));
        }
        return true;
    }
}
