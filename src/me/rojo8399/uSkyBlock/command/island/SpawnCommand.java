package me.rojo8399.uSkyBlock.command.island;

import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

/**
 * Convernience to get to spawn.
 */
public class SpawnCommand extends RequirePlayerCommand {
    private final uSkyBlock plugin;

    public SpawnCommand(uSkyBlock plugin) {
        super("spawn", null, I18nUtil.tr("teleports you to the skyblock spawn"));
        this.plugin = plugin;
    }

    @Override
    protected boolean doExecute(String alias, Player player, Map<String, Object> data, String... args) {
        plugin.spawnTeleport(player);
        return true;
    }
}
