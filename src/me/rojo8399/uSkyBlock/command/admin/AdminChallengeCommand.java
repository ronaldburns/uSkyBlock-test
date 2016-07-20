package me.rojo8399.uSkyBlock.command.admin;

import me.rojo8399.uSkyBlock.bukkitUtils.command.AbstractCommand;
import me.rojo8399.uSkyBlock.bukkitUtils.command.CompositeCommand;
import me.rojo8399.uSkyBlock.poUtils.I18nUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import me.rojo8399.uSkyBlock.challenge.ChallengeCompletion;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.Map;

/**
 * The challenge admin command.
 */
public class AdminChallengeCommand extends CompositeCommand {

    private final uSkyBlock plugin;

    public AdminChallengeCommand(final uSkyBlock plugin, TabCompleter challengeCompleter) {
        super("challenge|ch", "usb.mod.challenges", "player", I18nUtil.tr("Manage challenges for a player"));
        this.plugin = plugin;
        add(new ChallengeCommand("complete", null, "completes the challenge for the player") {
            @Override
            protected void doExecute(CommandSender sender, PlayerInfo playerInfo, ChallengeCompletion completion) {
                String challenge = completion.getName();
                if (completion.getTimesCompleted() > 0) {
                    sender.sendMessage(I18nUtil.tr("\u00a74Challenge has already been completed"));
                } else {
                    playerInfo.completeChallenge(challenge, true);
                    playerInfo.save();
                    sender.sendMessage(I18nUtil.tr("\u00a7echallenge: {0} has been completed for {1}", challenge, playerInfo.getPlayerName()));
                }
            }
        });
        add(new ChallengeCommand("reset", null, I18nUtil.tr("resets the challenge for the player")) {
            @Override
            protected void doExecute(CommandSender sender, PlayerInfo pi, ChallengeCompletion completion) {
                String challenge = completion.getName();
                String playerName = pi.getPlayerName();
                if (completion.getTimesCompleted() == 0) {
                    sender.sendMessage(I18nUtil.tr("\u00a74Challenge has never been completed"));
                } else {
                    pi.resetChallenge(challenge);
                    pi.save();
                    sender.sendMessage(I18nUtil.tr("\u00a7echallenge: {0} has been reset for {1}", challenge, playerName));
                }
            }
        });
        add(new AbstractCommand("resetall", null, I18nUtil.tr("resets all challenges for the player")) {
            @Override
            public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
                PlayerInfo playerInfo = (PlayerInfo) data.get("playerInfo");
                if (playerInfo != null) {
                    playerInfo.resetAllChallenges();
                    playerInfo.save();
                    sender.sendMessage(I18nUtil.tr("\u00a7e{0} has had all challenges reset.", playerInfo.getPlayerName()));
                    return true;
                }
                return false;
            }
        });
        addTab("challenge", challengeCompleter);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        if (args.length > 0) {
            PlayerInfo playerInfo = plugin.getPlayerInfo(args[0]);
            if (playerInfo != null) {
                data.put("playerInfo", playerInfo);
            }
        }
        return super.execute(sender, alias, data, args);
    }

    private abstract class ChallengeCommand extends AbstractCommand {
        public ChallengeCommand(String name, String permission, String description) {
            super(name, permission, "challenge", description);
        }
        protected abstract void doExecute(CommandSender sender, PlayerInfo playerInfo, ChallengeCompletion challenge);

        @Override
        public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
            PlayerInfo playerInfo = (PlayerInfo) data.get("playerInfo");
            if (playerInfo != null && args.length > 0) {
                ChallengeCompletion completion = playerInfo.getChallenge(args[0]);
                if (completion != null) {
                    doExecute(sender, playerInfo, completion);
                    return true;
                } else {
                    sender.sendMessage(I18nUtil.tr("\u00a74No challenge named {0} was found!", args[0]));
                }
            } else {
                sender.sendMessage(I18nUtil.tr("\u00a74No player named {0} was found!", data.get("player")));
            }
            return false;
        }
    }
}
