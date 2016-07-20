package me.rojo8399.uSkyBlock.bukkitUtils.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.rojo8399.uSkyBlock.bukkitUtils.command.CompositeCommand;

import java.util.HashMap;

/**
 * Command delegator.
 */
public class AbstractCommandExecutor extends CompositeCommand implements CommandExecutor {

    public AbstractCommandExecutor(String name, String permission, String description) {
        super(name, permission, description);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        return super.execute(commandSender, alias, new HashMap<String, Object>(), args);
    }
}
