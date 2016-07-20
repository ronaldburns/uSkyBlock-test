package me.rojo8399.uSkyBlock.island.task;

import me.rojo8399.uSkyBlock.api.event.uSkyBlockEvent;
import me.rojo8399.uSkyBlock.async.Callback;
import me.rojo8399.uSkyBlock.async.IncrementalRunnable;
import me.rojo8399.uSkyBlock.island.IslandScore;
import me.rojo8399.uSkyBlock.uSkyBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by R4zorax on 28/09/2015.
 */
public class RecalculateTopTen extends IncrementalRunnable {
    private final List<String> locations;
    private final Runnable onCompletion;

    public RecalculateTopTen(uSkyBlock plugin, Set<String> locations, Runnable onCompletion) {
        super(plugin, onCompletion);
        this.locations = new ArrayList<>(locations);
        this.onCompletion = onCompletion;
    }

    @Override
    protected boolean execute() {
        while (!locations.isEmpty()) {
            getPlugin().calculateScoreAsync(null, locations.remove(0), new Callback<IslandScore>() {
                @Override
                public void run() {
                    if (locations.isEmpty()) {
                        getPlugin().fireChangeEvent(new uSkyBlockEvent(null, getPlugin().getAPI(), uSkyBlockEvent.Cause.RANK_UPDATED));
                    }
                }
            });
            if (!tick()) {
                return false;
            }
        }
        return true;
    }
}
