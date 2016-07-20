package me.rojo8399.uSkyBlock.imports.update;

import me.rojo8399.uSkyBlock.bukkitUtils.file.FileUtil;
import me.rojo8399.uSkyBlock.imports.USBImporter;
import me.rojo8399.uSkyBlock.uSkyBlock;
import me.rojo8399.uSkyBlock.util.IslandUtil;

import java.io.File;

/**
 * An importer that simply loads all island-infos into memory - activating the built-in yml
 * file updating.
 */
public class USBUpdateImporter implements USBImporter {
    @Override
    public String getName() {
        return "update-islands";
    }

    @Override
    public boolean importFile(uSkyBlock plugin, File file) {
        String islandName = FileUtil.getBasename(file.getName());
        plugin.getIslandLogic().getIslandInfo(islandName);
        plugin.getIslandLogic().removeIslandFromMemory(islandName);
        return true;
    }

    @Override
    public int importOrphans(uSkyBlock plugin, File configFolder) {
        return 0;
    }

    @Override
    public File[] getFiles(uSkyBlock plugin) {
        return plugin.directoryIslands.listFiles(IslandUtil.createIslandFilenameFilter());
    }

    @Override
    public void completed(uSkyBlock plugin, int success, int failed) {
        // Do nothing
    }
}
