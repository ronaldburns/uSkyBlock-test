package me.rojo8399.uSkyBlock.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.rojo8399.uSkyBlock.uSkyBlock;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;
import static me.rojo8399.uSkyBlock.util.FormatUtil.stripFormatting;

/**
 * Menu events.
 */
public class MenuEvents implements Listener {
    private final uSkyBlock plugin;

    public MenuEvents(uSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void guiClick(final InventoryClickEvent event) {
        if (stripFormatting(event.getInventory().getTitle()).startsWith(stripFormatting(tr("Config:")))) {
            plugin.getConfigMenu().onClick(event);
        } else {
            plugin.getMenu().onClick(event);
        }
    }
}
