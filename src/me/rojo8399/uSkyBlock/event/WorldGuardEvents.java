package me.rojo8399.uSkyBlock.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import me.rojo8399.uSkyBlock.handler.VaultHandler;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Replacement for the WG ENTRY/EXIT deny flags.
 */
public class WorldGuardEvents implements Listener {
    private final uSkyBlock plugin;

    public WorldGuardEvents(uSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e == null || e.getTo() == null || e.getPlayer() == null || !plugin.isSkyAssociatedWorld(e.getTo().getWorld())) {
            return;
        }
        String islandNameAt = WorldGuardHandler.getIslandNameAt(e.getTo());
        if (islandNameAt == null) {
            return;
        }
        IslandInfo islandInfo = plugin.getIslandInfo(islandNameAt);
        if (islandInfo == null || islandInfo.getBans().isEmpty()) {
            return;
        }
        Player player = e.getPlayer();
        if (!player.isOp() && !VaultHandler.checkPerm(player, "usb.mod.bypassprotection", plugin.getWorld()) && isBlockedFromEntry(player, islandInfo)) {
            e.setCancelled(true);
            Location l = e.getTo().clone();
            l.subtract(islandInfo.getIslandLocation());
            Vector v = new Vector(l.getX(), l.getY(), l.getZ());
            v.normalize();
            v.multiply(1.5); // Bounce
            player.setVelocity(v);
            if (islandInfo.isBanned(player)) {
                plugin.notifyPlayer(player, tr("\u00a7cBanned:\u00a7e You are banned from this island."));
            } else {
                plugin.notifyPlayer(player, tr("\u00a7cLocked:\u00a7e That island is locked! No entry allowed."));
            }
        }
    }

    private boolean isBlockedFromEntry(Player player, IslandInfo islandInfo) {
        return islandInfo.isBanned(player) || (islandInfo.isLocked() && !(
                islandInfo.getMembers().contains(player.getName()) || islandInfo.getTrustees().contains(player.getName())
                ));
    }
}
