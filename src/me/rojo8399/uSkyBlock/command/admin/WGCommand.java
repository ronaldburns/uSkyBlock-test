package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.CompositeCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.command.island.RequirePlayerCommand;
import me.rojo8399.uSkyBlock.handler.WorldEditHandler;
import me.rojo8399.uSkyBlock.uSkyBlock;
import me.rojo8399.uSkyBlock.util.LocationUtil;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * Some hooks into the WG Handler
 */
public class WGCommand extends CompositeCommand {
    private final uSkyBlock plugin;

    public WGCommand(uSkyBlock plugin) {
        super("wg", "usb.admin.wg", I18nUtil.tr("various WorldGuard utilities"));
        this.plugin = plugin;
        add(new RequirePlayerCommand("refresh", null, I18nUtil.tr("refreshes the chunks around the player")) {
            @Override
            protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
                WorldEditHandler.refreshRegion(player.getLocation());
                player.sendMessage(I18nUtil.tr("\u00a7eResending chunks to the client"));
                return true;
            }
        });
        add(new RequirePlayerCommand("load", null, I18nUtil.tr("load the region chunks")) {
            @Override
            protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
                WorldEditHandler.loadRegion(player.getLocation());
                player.sendMessage(tr("\u00a7eLoading chunks at {0}", LocationUtil.asString(player.getLocation())));
                return true;
            }
        });
        add(new RequirePlayerCommand("unload", null, I18nUtil.tr("load the region chunks")) {
            @Override
            protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
                LocationUtil.loadChunkAt(player.getLocation());
                player.sendMessage(tr("\u00a7eUnloading chunks at {0}", LocationUtil.asString(player.getLocation())));
                return true;
            }
        });
    }
}
