package me.rojo8399.uSkyBlock.bukkitUtils.command;

/**
 * Simple visitor for the USBCommands
 */
public interface CommandVisitor {
    void visit(Command cmd);
}
