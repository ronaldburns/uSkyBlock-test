package me.rojo8399.uSkyBlock.command;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rojo8399.uSkyBlock.Settings;
import me.rojo8399.uSkyBlock.command.completion.AllPlayerTabCompleter;
import me.rojo8399.uSkyBlock.command.completion.BiomeTabCompleter;
import me.rojo8399.uSkyBlock.command.completion.MemberTabCompleter;
import me.rojo8399.uSkyBlock.command.completion.OnlinePlayerTabCompleter;
import me.rojo8399.uSkyBlock.command.island.*;
import me.rojo8399.uSkyBlock.menu.SkyBlockMenu;
import me.rojo8399.uSkyBlock.uSkyBlock;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;

/**
 * The main /island command
 */
public class IslandCommand extends AbstractCommandExecutor {
    private final uSkyBlock plugin;
    private final SkyBlockMenu menu;

    public IslandCommand(uSkyBlock plugin, SkyBlockMenu menu) {
        super("island|is", "usb.island.create", tr("general island command"));
        this.plugin = plugin;
        this.menu = menu;
        InviteHandler inviteHandler = new InviteHandler(plugin);
        OnlinePlayerTabCompleter onlinePlayerTabCompleter = new OnlinePlayerTabCompleter();
        AllPlayerTabCompleter playerTabCompleter = new AllPlayerTabCompleter(onlinePlayerTabCompleter);
        addTab("island", playerTabCompleter);
        addTab("player", playerTabCompleter);
        addTab("oplayer", onlinePlayerTabCompleter);
        addTab("biome", new BiomeTabCompleter());
        addTab("member", new MemberTabCompleter(plugin));
        add(new RestartCommand(plugin));
        add(new LogCommand(plugin, menu));
        add(new CreateCommand(plugin));
        add(new SetHomeCommand(plugin));
        add(new HomeCommand(plugin));
        add(new SetWarpCommand(plugin));
        add(new WarpCommand(plugin));
        add(new ToggleWarp(plugin));
        add(new BanCommand(plugin));
        add(new LockUnlockCommand(plugin));
        if (Settings.island_useTopTen) {
            add(new TopCommand(plugin));
        }
        add(new BiomeCommand(plugin, menu));
        add(new LevelCommand(plugin));
        add(new InfoCommand(plugin));
        add(new InviteCommand(plugin, inviteHandler));
        add(new AcceptRejectCommand(inviteHandler));
        add(new LeaveCommand(plugin));
        add(new KickCommand(plugin));
        add(new PartyCommand(plugin, menu, inviteHandler));
        add(new MakeLeaderCommand(plugin));
        add(new SpawnCommand(plugin));
        add(new TrustCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.openInventory(menu.displayIslandGUI(player));
                return true;
            }
        }
        return super.onCommand(sender, command, alias, args);
    }
}
