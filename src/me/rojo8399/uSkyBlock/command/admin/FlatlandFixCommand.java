package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;
import me.rojo8399.uSkyBlock.util.LocationUtil;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Tries to clear an area of flatland.
 */
public class FlatlandFixCommand extends AbstractCommand {
    private final uSkyBlock plugin;

    public FlatlandFixCommand(uSkyBlock plugin) {
        super("fix-flatland", "usb.admin.remove", "?player", I18nUtil.tr("tries to fix the the area of flatland."));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        Location islandLocation = null;
        if (args.length == 1) {
            PlayerInfo playerInfo = plugin.getPlayerInfo(args[0]);
            if (playerInfo != null) {
                islandLocation = playerInfo.getHasIsland() ? playerInfo.getIslandLocation() : null;
            }
        } else if (args.length == 0 && sender instanceof Player) {
            String islandName = WorldGuardHandler.getIslandNameAt(((Player) sender).getLocation());
            IslandInfo islandInfo = plugin.getIslandInfo(islandName);
            if (islandInfo != null) {
                islandLocation = islandInfo.getIslandLocation();
            }
        }
        if (islandLocation == null || !tryFlatlandFix(sender, islandLocation)) {
            sender.sendMessage(I18nUtil.tr("\u00a74No valid island found"));
        }
        return true;
    }

    private boolean tryFlatlandFix(CommandSender sender, Location islandLocation) {
        // TODO: 29/12/2014 - R4zorax: Load chunks first?
        if (!plugin.getIslandLogic().clearFlatland(sender, islandLocation, 0)) {
            sender.sendMessage(tr("\u00a74No flatland detected at {0}'s island!", LocationUtil.asString(islandLocation)));
        }
        return true;
    }
}
