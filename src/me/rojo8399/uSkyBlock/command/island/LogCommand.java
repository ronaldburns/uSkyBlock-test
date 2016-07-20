package me.rojo8399.uSkyBlock.command.island;

import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.menu.SkyBlockMenu;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

public class LogCommand extends RequireIslandCommand {
    private final SkyBlockMenu menu;

    public LogCommand(uSkyBlock plugin, SkyBlockMenu menu) {
        super(plugin, "log|l", "usb.island.create", tr("display log"));
        this.menu = menu;
    }

    @Override
    protected boolean doExecute(String alias, Player player, PlayerInfo pi, IslandInfo island, Map<String, Object> data, String... args) {
        player.openInventory(menu.displayLogGUI(player));
        return true;
    }
}
