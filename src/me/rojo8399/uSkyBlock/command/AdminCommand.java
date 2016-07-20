package me.rojo8399.uSkyBlock.command;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommandExecutor;
import me.rojo8399.uSkyBlock.bukkitUtils.command.DocumentCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import me.rojo8399.uSkyBlock.command.admin.AbstractPlayerInfoCommand;
import me.rojo8399.uSkyBlock.command.admin.AdminChallengeCommand;
import me.rojo8399.uSkyBlock.command.admin.AdminIslandCommand;
import me.rojo8399.uSkyBlock.command.admin.ConfigGUICommand;
import me.rojo8399.uSkyBlock.command.admin.CooldownCommand;
import me.rojo8399.uSkyBlock.command.admin.DebugCommand;
import me.rojo8399.uSkyBlock.command.admin.FlatlandFixCommand;
import me.rojo8399.uSkyBlock.command.admin.FlushCommand;
import me.rojo8399.uSkyBlock.command.admin.GenTopTenCommand;
import me.rojo8399.uSkyBlock.command.admin.GotoIslandCommand;
import me.rojo8399.uSkyBlock.command.admin.ImportCommand;
import me.rojo8399.uSkyBlock.command.admin.JobsCommand;
import me.rojo8399.uSkyBlock.command.admin.LanguageCommand;
import me.rojo8399.uSkyBlock.command.admin.OrphanCommand;
import me.rojo8399.uSkyBlock.command.admin.PerkCommand;
import me.rojo8399.uSkyBlock.command.admin.PurgeCommand;
import me.rojo8399.uSkyBlock.command.admin.ReloadCommand;
import me.rojo8399.uSkyBlock.command.admin.VersionCommand;
import me.rojo8399.uSkyBlock.command.admin.WGCommand;
import me.rojo8399.uSkyBlock.command.completion.AllPlayerTabCompleter;
import me.rojo8399.uSkyBlock.command.completion.BiomeTabCompleter;
import me.rojo8399.uSkyBlock.command.completion.ChallengeTabCompleter;
import me.rojo8399.uSkyBlock.command.completion.OnlinePlayerTabCompleter;
import me.rojo8399.uSkyBlock.handler.ConfirmHandler;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

/**
 * The new admin command, alias /usb
 */
public class AdminCommand extends AbstractCommandExecutor {
    public AdminCommand(final uSkyBlock plugin, ConfirmHandler confirmHandler) {
        super("usb", "usb.admin", I18nUtil.tr("Ultimate SkyBlock Admin"));
        OnlinePlayerTabCompleter playerCompleter = new OnlinePlayerTabCompleter();
        TabCompleter challengeCompleter = new ChallengeTabCompleter();
        TabCompleter allPlayerCompleter = new AllPlayerTabCompleter(playerCompleter);
        TabCompleter biomeCompleter = new BiomeTabCompleter();
        addTab("oplayer", playerCompleter);
        addTab("player", allPlayerCompleter);
        addTab("island", allPlayerCompleter);
        addTab("leader", allPlayerCompleter);
        addTab("challenge", challengeCompleter);
        addTab("biome", biomeCompleter);
        add(new ReloadCommand());
        add(new ImportCommand());
        add(new GenTopTenCommand(plugin));
        //add(new RegisterIslandToPlayerCommand());
        add(new AdminChallengeCommand(plugin, challengeCompleter));
        add(new OrphanCommand(plugin));
        add(new AdminIslandCommand(plugin, confirmHandler));
        add(new PurgeCommand(plugin));
        add(new GotoIslandCommand(plugin));
        add(new AbstractPlayerInfoCommand("info", "usb.admin.info", I18nUtil.tr("show player-information")) {
            @Override
            protected void doExecute(CommandSender sender, PlayerInfo playerInfo) {
                sender.sendMessage(playerInfo.toString());
            }
        });
        add(new FlatlandFixCommand(plugin));
        add(new DebugCommand(plugin));
        add(new WGCommand(plugin));
        add(new VersionCommand(plugin));
            add(new CooldownCommand(plugin));
        add(new PerkCommand(plugin));
        add(new LanguageCommand(plugin));
        add(new FlushCommand(plugin));
        add(new JobsCommand(plugin));
        add(new ConfigGUICommand(plugin));
        add(new DocumentCommand(plugin, "doc", "usb.admin.doc"));
    }
}
