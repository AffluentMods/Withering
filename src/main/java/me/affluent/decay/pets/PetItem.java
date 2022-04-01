package me.affluent.decay.pets;

import me.affluent.decay.enums.PetRarity;

import java.util.HashMap;

public class PetItem implements Pet {

    private final String name;
    private final int starterHP;
    private final int starterDMG;
    private final double perLevelHP;
    private final double perLevelDMG;
    private final int maxLevel;
    private final PetRarity rarity;
    private static final HashMap<String, PetItem> petItems = new HashMap<>();

    public PetItem(String name, int starterHP, int starterDMG, double perLevelHP, double perLevelDMG, int maxLevel, PetRarity rarity) {
        this(name, starterHP, starterDMG, perLevelHP, perLevelDMG, maxLevel, rarity, true);
    }

    public PetItem(String name, int starterHP, int starterDMG, double perLevelHP, double perLevelDMG, int maxLevel, PetRarity rarity, boolean addToList) {
        this.name = name;
        this.starterHP = starterHP;
        this.starterDMG = starterDMG;
        this.perLevelHP = perLevelHP;
        this.perLevelDMG = perLevelDMG;
        this.maxLevel = maxLevel;
        this.rarity = rarity;
        if (addToList) petItems.put(name.toLowerCase(), this);
    }

    public static PetItem getPetItem(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return petItems.get(name.toLowerCase());
    }

    @Override
    public String getName() {
        return name;
    }

    public int getStarterHP() { return starterHP;
    }

    public int getStarterDMG() { return starterDMG;
    }

    public double getLevelHP() { return perLevelHP;
    }

    public double getLevelDMG() { return perLevelDMG;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public PetRarity getRarity() {
        return rarity;
    }
}
