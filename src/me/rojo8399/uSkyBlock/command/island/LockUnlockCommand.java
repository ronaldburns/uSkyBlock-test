package me.rojo8399.uSkyBlock.command.island;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.Settings;
import me.rojo8399.uSkyBlock.handler.VaultHandler;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class LockUnlockCommand extends RequireIslandCommand {
    public LockUnlockCommand(uSkyBlock plugin) {
        super(plugin, "lock|unlock", "usb.lock", tr("lock your island to non-party members."));
    }

    @Override
    protected boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args) {
        if (alias.equalsIgnoreCase("lock") && pi.getHasIsland()) {
            if (Settings.island_allowIslandLock && VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld())) {
                if (island.hasPerm(player, "canToggleLock")) {
                    island.lock(player);
                } else {
                    player.sendMessage(tr("\u00a74You do not have permission to lock your island!"));
                }
            } else {
                player.sendMessage(tr("\u00a74You don't have access to this command!"));
            }
            return true;
        }
        if (alias.equalsIgnoreCase("unlock") && pi.getHasIsland()) {
            if (Settings.island_allowIslandLock && VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld())) {
                if (island.hasPerm(player, "canToggleLock")) {
                    island.unlock(player);
                } else {
                    player.sendMessage(tr("\u00a74You do not have permission to unlock your island!"));
                }
            } else {
                player.sendMessage(tr("\u00a74You don't have access to this command!"));
            }
            return true;
        }
        return false;
    }
}
