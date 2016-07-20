package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

/**
 * Registers an island to a player.
 */
public class RegisterIslandToPlayerCommand extends AbstractCommand {
    public RegisterIslandToPlayerCommand() {
        super("register", "usb.admin.register", "player", I18nUtil.tr("set a player's island to your location"));
    }
    @Override
    public boolean execute(final CommandSender sender, String alias, Map<String, Object> data, final String... args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        String playerName = args[0];
        Player player = (Player) sender;
        if (uSkyBlock.getInstance().devSetPlayerIsland(player, player.getLocation(), playerName)) {
            sender.sendMessage(I18nUtil.tr("\u00a7aSet {0}'s island to the bedrock nearest you.", playerName));
        } else {
            sender.sendMessage(I18nUtil.tr("\u00a74Bedrock not found: unable to set the island!"));
        }
        return true;
    }
}
