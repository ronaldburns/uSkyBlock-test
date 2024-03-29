package me.rojo8399.uSkyBlock.island;

import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;
import net.minecraft.util.com.google.common.cache.LoadingCache;
import net.minecraft.util.com.google.common.cache.RemovalListener;
import net.minecraft.util.com.google.common.cache.RemovalNotification;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.rojo8399.uSkyBlock.bukkitUtils.file.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import me.rojo8399.uSkyBlock.Settings;
import me.rojo8399.uSkyBlock.api.IslandLevel;
import me.rojo8399.uSkyBlock.api.IslandRank;
import me.rojo8399.uSkyBlock.api.event.uSkyBlockEvent;
import me.rojo8399.uSkyBlock.handler.WorldEditHandler;
import me.rojo8399.uSkyBlock.handler.WorldGuardHandler;
import me.rojo8399.uSkyBlock.handler.task.WorldEditClearFlatlandTask;
import me.rojo8399.uSkyBlock.player.PlayerInfo;
import me.rojo8399.uSkyBlock.uSkyBlock;
import me.rojo8399.uSkyBlock.util.IslandUtil;
import me.rojo8399.uSkyBlock.util.LocationUtil;
import me.rojo8399.uSkyBlock.util.PlayerUtil;
import me.rojo8399.uSkyBlock.util.TimeUtil;
import me.rojo8399.uSkyBlock.uuid.AsyncPlayerNameChangedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.rojo8399.uSkyBlock.poUtils.I18nUtil.tr;
import static org.bukkit.Material.BEDROCK;

/**
 * Responsible for island creation, locating locations, purging, clearing etc.
 */
public class IslandLogic {
    private static final Logger log = Logger.getLogger(IslandLogic.class.getName());
    private final uSkyBlock plugin;
    private final File directoryIslands;
    private final OrphanLogic orphanLogic;

    private final LoadingCache<String, IslandInfo> cache;
    private final boolean showMembers;
    private final boolean flatlandFix;
    private final BukkitTask saveTask;
    private final double topTenCutoff;

    private volatile long lastGenerate = 0;
    private final List<IslandLevel> ranks = new ArrayList<>();

    public IslandLogic(uSkyBlock plugin, File directoryIslands, OrphanLogic orphanLogic) {
        this.plugin = plugin;
        this.directoryIslands = directoryIslands;
        this.orphanLogic = orphanLogic;
        this.showMembers = plugin.getConfig().getBoolean("options.island.topTenShowMembers", true);
        this.flatlandFix = plugin.getConfig().getBoolean("options.island.fixFlatland", false);
        topTenCutoff = plugin.getConfig().getDouble("options.advanced.topTenCutoff", plugin.getConfig().getDouble("options.advanced.purgeLevel", 10));
        cache = CacheBuilder
                .from(plugin.getConfig().getString("options.advanced.islandCache",
                        "maximumSize=200,expireAfterWrite=15m,expireAfterAccess=10m"))
                .removalListener(new RemovalListener<String, IslandInfo>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, IslandInfo> removal) {
                        removal.getValue().saveToFile();
                    }
                })
                .build(new CacheLoader<String, IslandInfo>() {
                    @Override
                    public IslandInfo load(String islandName) throws Exception {
                        return new IslandInfo(islandName);
                    }
                });
        int every = plugin.getConfig().getInt("options.advanced.island.saveEvery", 20*60*2);
        saveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                saveDirtyToFiles();
            }
        }, every, every);
    }

    private void saveDirtyToFiles() {
        // asMap.values() should NOT touch the cache.
        for (IslandInfo islandInfo : cache.asMap().values()) {
            if (islandInfo.isDirty()) {
                islandInfo.saveToFile();
            }
        }
    }

    public synchronized IslandInfo getIslandInfo(String islandName) {
        if (islandName == null) {
            return null;
        }
        IslandInfo islandInfo = null;
        try {
            islandInfo = cache.get(islandName);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Unable to load island", e);
        }
        if (islandInfo.exists()) {
            return islandInfo;
        }
        return null;
    }
    
    public synchronized IslandInfo createIslandInfo(String islandName) {
        if (islandName == null) {
            return null;
        }
        try {
            return cache.get(islandName);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Unable to create island", e);
        }
    }

    public IslandInfo getIslandInfo(PlayerInfo playerInfo) {
        if (playerInfo != null && playerInfo.getHasIsland()) {
            return getIslandInfo(playerInfo.locationForParty());
        }
        return null;
    }

    public void loadIslandChunks(Location l, int radius) {
        World world = l.getWorld();
        final int px = l.getBlockX();
        final int pz = l.getBlockZ();
        for (int x = -radius-16; x <= radius+16; x += 16) {
            for (int z = -radius-16; z <= radius+16; z += 16) {
                world.loadChunk((px + x) / 16, (pz + z) / 16, true);
            }
        }
    }

    public void clearIsland(Location loc, Runnable afterDeletion) {
        log.log(Level.FINE, "clearing island at {0}", loc);
        World skyBlockWorld = plugin.getWorld();
        ProtectedRegion region = WorldGuardHandler.getIslandRegionAt(loc);
        if (region != null) {
            for (Player player : WorldGuardHandler.getPlayersInRegion(plugin.getWorld(), region)) {
                if (player != null && player.isOnline() && plugin.isSkyWorld(player.getWorld()) && !player.isFlying()) {
                    player.sendMessage(tr("\u00a7cThe island you are on is being deleted! Sending you to spawn."));
                    plugin.spawnTeleport(player, true);
                }
            }
            WorldEditHandler.clearIsland(skyBlockWorld, region, afterDeletion);
        } else {
            log.log(Level.WARNING, "Trying to delete an island - with no WG region! ({0})", LocationUtil.asString(loc));
            afterDeletion.run();
        }
        Location netherIsland = getNetherLocation(loc);
        ProtectedRegion netherRegion = WorldGuardHandler.getNetherRegionAt(netherIsland);
        if (netherRegion != null) {
            for (Player player : WorldGuardHandler.getPlayersInRegion(netherIsland.getWorld(), netherRegion)) {
                if (player != null && player.isOnline() && plugin.isSkyNether(player.getWorld()) && !player.isFlying()) {
                    player.sendMessage(tr("\u00a7cThe island owning this piece of nether is being deleted! Sending you to spawn."));
                    plugin.spawnTeleport(player, true);
                }
            }
            WorldEditHandler.clearNetherIsland(netherIsland.getWorld(), netherRegion, null);
        }
    }

    private Location getNetherLocation(Location loc) {
        Location netherIsland = loc.clone();
        netherIsland.setWorld(plugin.getSkyBlockNetherWorld());
        netherIsland.setY(loc.getY()/2);
        return netherIsland;
    }

    public boolean clearFlatland(final CommandSender sender, final Location loc, final int delay) {
        if (loc == null) {
            return false;
        }
        if (delay > 0 && !flatlandFix) {
            return false; // Skip
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final World w = loc.getWorld();
                final int px = loc.getBlockX();
                final int pz = loc.getBlockZ();
                final int py = 0;
                final int range = Math.max(Settings.island_protectionRange, Settings.island_distance) + 1;
                final int radius = range/2;
                // 5 sampling points...
                if (w.getBlockAt(px, py, pz).getType() == BEDROCK
                        || w.getBlockAt(px+radius, py, pz+radius).getType() == BEDROCK
                        || w.getBlockAt(px+radius, py, pz-radius).getType() == BEDROCK
                        || w.getBlockAt(px-radius, py, pz+radius).getType() == BEDROCK
                        || w.getBlockAt(px-radius, py, pz-radius).getType() == BEDROCK)
                {
                    sender.sendMessage(String.format("\u00a7c-----------------------------------\n\u00a7cFlatland detected under your island!\n\u00a7e Clearing it in %s, stay clear.\n\u00a7c-----------------------------------\n", TimeUtil.ticksAsString(delay)));
                    new WorldEditClearFlatlandTask(plugin, sender, new CuboidRegion(new Vector(px-radius, 0, pz-radius),
                            new Vector(px+radius, 4, pz+radius)),
                            "\u00a7eFlatland was cleared under your island (%s). Take care.").runTaskLater(plugin, delay);
                }
            }
        };
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
        return false;
    }

    public void displayTopTen(final CommandSender sender, int page) {
        synchronized (ranks) {
            int maxpage = (( ranks.size()-1) / 10) + 1;
            if (page > maxpage) {
                page = maxpage;
            }
            if (page < 1) {
                page = 1;
            }
            sender.sendMessage(tr("\u00a7eWALL OF FAME (page {0} of {1}):", page, maxpage));
            if (ranks == null || ranks.isEmpty()) {
                sender.sendMessage(tr("\u00a74Top ten list is empty! (perhaps the generation failed?)"));
            }
            int place = 1;
            PlayerInfo playerInfo = plugin.getPlayerInfo(sender.getName());
            IslandRank rank = null;
            if (playerInfo != null && playerInfo.getHasIsland()) {
                rank = getRank(playerInfo.locationForParty());
            }
            int offset = (page-1) * 10;
            place += offset;
            for (final IslandLevel level : ranks.subList(offset, Math.min(ranks.size(), 10*page))) {
                String members = "";
                if (showMembers && !level.getMembers().isEmpty()) {
                    members = Arrays.toString(level.getMembers().toArray(new String[level.getMembers().size()]));
                }
                sender.sendMessage(String.format(tr("\u00a7a#%2d \u00a77(%5.2f): \u00a7e%s \u00a77%s"), place, level.getScore(),
                        level.getLeaderName(), members));
                place++;
            }
            if (rank != null) {
                sender.sendMessage(tr("\u00a7eYour rank is: \u00a7f{0}", rank.getRank()));
            }
        }

    }

    public void showTopTen(final CommandSender sender, final int page) {
        long t = System.currentTimeMillis();
        if (t > (lastGenerate + (Settings.island_topTenTimeout*60000)) || (sender.hasPermission("usb.admin.topten") || sender.isOp() || sender instanceof ConsoleCommandSender)) {
            lastGenerate = t;
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    generateTopTen(sender);
                    displayTopTen(sender, page);
                }
            });
        } else {
            displayTopTen(sender, page);
        }
    }

    public List<IslandLevel> getRanks(int offset, int length) {
        synchronized (ranks) {
            int size = ranks.size();
            if (size <= offset) {
                return Collections.emptyList();
            }
            return ranks.subList(offset, Math.min(size-offset, length));
        }
    }

    public void generateTopTen(final CommandSender sender) {
        List<IslandLevel> topTen = new ArrayList<>();
        final File folder = directoryIslands;
        final String[] listOfFiles = folder.list(IslandUtil.createIslandFilenameFilter());
        for (String file : listOfFiles) {
            String islandName = FileUtil.getBasename(file);
            try {
                boolean wasLoaded = cache.getIfPresent(islandName) != null;
                IslandInfo islandInfo = getIslandInfo(islandName);
                double level = islandInfo != null ? islandInfo.getLevel() : 0;
                if (islandInfo != null && level > topTenCutoff && !islandInfo.ignore()) {
                    IslandLevel islandLevel = createIslandLevel(islandInfo, level);
                    topTen.add(islandLevel);
                }
                if (!wasLoaded) {
                    cache.invalidate(islandName);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error during rank generation", e);
            }
        }
        Collections.sort(topTen);
        synchronized (ranks) {
            lastGenerate = System.currentTimeMillis();
            ranks.clear();
            ranks.addAll(topTen);
        }
        plugin.fireChangeEvent(sender, uSkyBlockEvent.Cause.RANK_UPDATED);
    }

    private IslandLevel createIslandLevel(IslandInfo islandInfo, double level) {
        String partyLeader = islandInfo.getLeader();
        String partyLeaderName = PlayerUtil.getPlayerDisplayName(partyLeader);
        List<String> memberList = new ArrayList<>(islandInfo.getMembers());
        memberList.remove(partyLeader);
        List<String> names = new ArrayList<>();
        for (String name : memberList) {
            String displayName = PlayerUtil.getPlayerDisplayName(name);
            if (displayName != null) {
                names.add(displayName);
            }
        }
        return new IslandLevel(islandInfo.getName(), partyLeaderName, names, level);
    }

    public synchronized IslandInfo createIslandInfo(String location, String player) {
        IslandInfo info = createIslandInfo(location);
        cache.put(location, info);
        info.resetIslandConfig(player);
        return info;
    }

    public synchronized void deleteIslandConfig(final String location) {
        try {
            IslandInfo islandInfo = cache.get(location);
            if (islandInfo.exists()) {
                islandInfo.delete();
            }
            cache.invalidate(location);
            orphanLogic.addOrphan(location);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Unable to delete island " + location, e);
        }
    }

    public synchronized void removeIslandFromMemory(String islandName) {
        cache.invalidate(islandName);
    }

    public void renamePlayer(PlayerInfo playerInfo, AsyncPlayerNameChangedEvent change) {
        List<String> islands = new ArrayList<>();
        islands.add(playerInfo.locationForParty());
        islands.addAll(playerInfo.getBannedFrom());
        for (String islandName : islands) {
            renamePlayer(islandName, change);
        }
    }

    public void renamePlayer(String islandName, AsyncPlayerNameChangedEvent e) {
        IslandInfo islandInfo = getIslandInfo(islandName);
        if (islandInfo != null) {
            islandInfo.renamePlayer(e.getPlayer(), e.getOldName());
            if (!islandInfo.hasOnlineMembers()) {
                removeIslandFromMemory(islandInfo.getName());
            }
        }
    }

    public void updateRank(IslandInfo islandInfo, IslandScore score) {
        synchronized (ranks) {
            IslandLevel islandLevel = createIslandLevel(islandInfo, score.getScore());
            ranks.remove(islandLevel);
            ranks.add(islandLevel);
            Collections.sort(ranks);
        }
    }

    public boolean hasIsland(Location loc) {
        return loc == null || new File(directoryIslands, LocationUtil.getIslandName(loc) + ".yml").exists();
    }

    public IslandRank getRank(String islandName) {
        if (ranks != null) {
            ArrayList<IslandLevel> rankList = new ArrayList<>(ranks);
            for (int i = 0; i < rankList.size(); i++) {
                IslandLevel level = rankList.get(i);
                if (level.getIslandName().equalsIgnoreCase(islandName)) {
                    return new IslandRank(level, i+1);
                }
            }
        }
        return null;
    }

    public boolean purge(String islandName) {
        IslandInfo islandInfo = getIslandInfo(islandName);
        if (islandInfo != null && !islandInfo.ignore()) {
            for (String member : islandInfo.getMembers()) {
                PlayerInfo pi = plugin.getPlayerInfo(member);
                if (pi != null) {
                    islandInfo.removeMember(pi);
                }
            }
            WorldGuardHandler.removeIslandRegion(islandName);
            deleteIslandConfig(islandName);
            return true;
        }
        return false;
    }

    public void shutdown() {
        saveTask.cancel();
        flushCache();
    }

    public long flushCache() {
        long size = cache.size();
        cache.invalidateAll(); // Flush to files
        return size;
    }
}
