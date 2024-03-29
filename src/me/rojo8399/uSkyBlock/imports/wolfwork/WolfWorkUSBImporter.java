package me.rojo8399.uSkyBlock.imports.wolfwork;

import org.bukkit.Bukkit;
import me.rojo8399.uSkyBlock.challenge.ChallengeCompletion;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.imports.USBImporter;
import me.rojo8399.uSkyBlock.island.IslandInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Stack;
import java.util.logging.Level;

/**
 * An importer for the wolfwork branch.
 */
public class WolfWorkUSBImporter implements USBImporter {

    @Override
    public String getName() {
        return "wolfwork";
    }

    @Override
    public boolean importFile(uSkyBlock plugin, File file) {
        try {
            PlayerInfo playerInfo = readPlayerInfo(file);
            if (playerInfo == null) {
                return false;
            }
            me.rojo8399.uSkyBlock.player.PlayerInfo pi = importPlayerInfo(plugin, playerInfo);
            importIsland(plugin, playerInfo, pi);
            plugin.getPlayerLogic().removeActivePlayer(pi);
            if (!file.delete()) {
                file.deleteOnExit();
            }
            return true;
        } catch (Exception e) {
            plugin.log(Level.WARNING, "Unable to import " + file, e);
            return false;
        }
    }

    @Override
    public int importOrphans(uSkyBlock plugin, File configFolder) {
        int orphanCount = 0;
        File orphans = new File(configFolder, "orphanedIslands.bin");
        if (orphans.exists()) {
            orphanCount += importOrphanedIslands(plugin, orphans);
        }
        return orphanCount;
    }

    private int importOrphanedIslands(uSkyBlock plugin, File orphanFile) {
        try (ObjectInputStream in = new WolfWorkObjectInputStream(new FileInputStream(orphanFile))) {
            Object stackObj = in.readObject();
            if (stackObj instanceof Stack) {
                int countOrphan = 0;
                Stack<SerializableLocation> stack = (Stack) stackObj;
                while (!stack.isEmpty()) {
                    SerializableLocation remove = stack.remove(0);
                    plugin.getOrphanLogic().addOrphan(remove.getLocation());
                    countOrphan++;
                }
                if (!orphanFile.delete()) {
                    orphanFile.deleteOnExit();
                }
                return countOrphan;
            }
        } catch (IOException | ClassNotFoundException e) {
            plugin.log(Level.WARNING, "Unable to read the orphanedIslands.bin file", e);
        }
        return 0;
    }

    private me.rojo8399.uSkyBlock.player.PlayerInfo importPlayerInfo(uSkyBlock plugin, PlayerInfo playerInfo) {
        // Copy PlayerInfo
        me.rojo8399.uSkyBlock.player.PlayerInfo pi = plugin.getPlayerInfo(playerInfo.getPlayerName());
        pi.setHasIsland(playerInfo.getHasIsland());
        if (playerInfo.getIslandLocation() != null) {
            pi.setIslandLocation(playerInfo.getIslandLocation());
        } else if (playerInfo.getPartyIslandLocation() != null) {
            pi.setIslandLocation(playerInfo.getPartyIslandLocation());
        }
        pi.setHomeLocation(playerInfo.getHomeLocation());
        // Challenges
        long now = System.currentTimeMillis();
        for (ChallengeCompletion challenge : pi.getChallenges()) {
            if (playerInfo.checkChallenge(challenge.getName())) {
                challenge.setFirstCompleted(now);
                challenge.setTimesCompleted(1);
            } else {
                challenge.setTimesCompleted(0);
            }
        }
        pi.save();
        return pi;
    }

    private void importIsland(uSkyBlock plugin, PlayerInfo playerInfo, me.rojo8399.uSkyBlock.player.PlayerInfo pi) {
        // Copy IslandInfo
        IslandInfo islandInfo = plugin.getIslandInfo(pi);
        if (islandInfo != null) {
            if (playerInfo.getPartyLeader() != null) {
                islandInfo.setupPartyLeader(playerInfo.getPartyLeader());
            } else {
                islandInfo.setupPartyLeader(playerInfo.getPlayerName());
            }
            for (String member : playerInfo.getMembers()) {
                islandInfo.setupPartyMember(member);
            }
            islandInfo.setWarpActive(playerInfo.isWarpActive());
            islandInfo.setWarpLocation(playerInfo.getWarpLocation());
            for (String banned : playerInfo.getBanned()) {
                islandInfo.banPlayer(banned);
            }
            // Not really that important - since it's most likely different!
            islandInfo.setLevel(playerInfo.getIslandLevel());
            islandInfo.setBiome("OCEAN");
            islandInfo.save();
            WorldGuardHandler.updateRegion(Bukkit.getConsoleSender(), islandInfo);
        }
    }

    private PlayerInfo readPlayerInfo(File playerFile) {
        try (ObjectInputStream in = new WolfWorkObjectInputStream(new FileInputStream(playerFile))) {
            Object o = in.readObject();
            if (o instanceof PlayerInfo) {
                return (PlayerInfo) o;
            }
        } catch (ClassNotFoundException|IOException e) {
            // Ignore...
        }
        return null;
    }

    @Override
    public File[] getFiles(uSkyBlock plugin) {
        return plugin.directoryPlayers.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name != null && !name.endsWith(".yml");
            }
        });
    }

    @Override
    public void completed(uSkyBlock plugin, int success, int failed) {
        // Do nothing
    }
}
