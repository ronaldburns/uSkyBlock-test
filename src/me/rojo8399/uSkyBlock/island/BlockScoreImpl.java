package me.rojo8399.uSkyBlock.island;

import org.bukkit.inventory.ItemStack;
import me.rojo8399.uSkyBlock.handler.VaultHandler;


public class BlockScoreImpl implements me.rojo8399.uSkyBlock.api.model.BlockScore {

    private final ItemStack block;
    private final int count;
    private final double score;
    private final State state;
    private final String name;

    public BlockScoreImpl(ItemStack block, int count, double score, State state) {
        this(block, count, score, state, null);
    }

    public BlockScoreImpl(ItemStack block, int count, double score, State state, String name) {
        this.block = block;
        this.count = count;
        this.score = score;
        this.state = state;
        this.name = name != null ? name : VaultHandler.getItemName(getBlock());
    }

    @Override
    public String toString() {
        return "BlockScore{" +
                "name=" + name +
                ", block=" + block +
                ", count=" + count +
                ", score=" + score +
                ", state=" + state +
                '}';
    }


    @Override
    public ItemStack getBlock() {
        return block;
    }


    @Override
    public int getCount() {
        return count;
    }


    @Override
    public double getScore() {
        return score;
    }


    @Override
    public State getState() {
        return state;
    }


    @Override
    public String getName() {
        return name;
    }
}
