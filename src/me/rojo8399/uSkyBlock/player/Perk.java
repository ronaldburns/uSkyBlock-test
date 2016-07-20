package me.rojo8399.uSkyBlock.player;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Perk is an immutable object holding all the perks,
 */
public class Perk {
    private final int maxPartySize;
    private final int animals;
    private final int monsters;
    private final List<ItemStack> extraItems;
    private final double rewBonus;
    private final double hungerReduction;
    private final List<String> schematics;
    private final int villagers;

    Perk(List<ItemStack> extraItems, int maxPartySize, int animals, int monsters, int villagers, double rewBonus, double hungerReduction, List<String> schematics) {
        this.maxPartySize = maxPartySize >= 0 ? maxPartySize : 0;
        this.animals = animals >= 0 ? animals : 0;
        this.monsters = monsters >= 0 ? monsters : 0;
        this.villagers = villagers >= 0 ? villagers : 0;
        this.extraItems = extraItems != null ? extraItems : Collections.<ItemStack>emptyList();
        this.rewBonus = rewBonus >= 0 ? rewBonus : 0;
        this.hungerReduction = hungerReduction >= 0 && hungerReduction <= 1 ? hungerReduction : 0;
        this.schematics = schematics != null ? new ArrayList<>(schematics) : Collections.<String>emptyList();
    }

    public int getMaxPartySize() {
        return maxPartySize;
    }

    public int getAnimals() {
        return animals;
    }

    public int getMonsters() {
        return monsters;
    }

    public int getVillagers() {
        return villagers;
    }

    public List<ItemStack> getExtraItems() {
        return extraItems;
    }

    public double getRewBonus() {
        return rewBonus;
    }

    public double getHungerReduction() {
        return hungerReduction;
    }

    public List<String> getSchematics() {
        return Collections.unmodifiableList(schematics);
    }

    public Perk combine(Perk other) {
        List<ItemStack> items = new ArrayList<>();
        items.addAll(this.extraItems);
        items.addAll(other.getExtraItems());
        List<String> schems = new ArrayList<>();
        schems.addAll(this.schematics);
        schems.addAll(other.getSchematics());
        return new Perk(
                items,
                Math.max(maxPartySize, other.getMaxPartySize()),
                Math.max(animals, other.getAnimals()),
                Math.max(monsters, other.getMonsters()),
                0, Math.max(rewBonus, other.getRewBonus()),
                Math.max(hungerReduction, other.getHungerReduction()),
                schems);
    }

    @Override
    public String toString() {
        return  (maxPartySize > 0 ? "maxPartySize:" + maxPartySize + "\n" : "") +
                (animals > 0 ? "animals:" + animals +"\n" : "") +
                (monsters > 0 ? "monsters:" + monsters +"\n" : "") +
                (villagers > 0 ? "villagers:" + villagers + "\n" : "") +
                (!extraItems.isEmpty() ? "extraItems:" + extraItems +"\n" : "") +
                (rewBonus > 0 ? "rewBonus:" + rewBonus +"\n" : "") +
                (hungerReduction > 0 ? "hungerReduction:" + hungerReduction +"\n" : "") +
                (!schematics.isEmpty() ? "schematics:" + schematics +"\n" : "");
    }
}