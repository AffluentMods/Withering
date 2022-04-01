package me.affluent.decay.armor;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.util.system.FormatUtil;

import java.util.HashMap;

public class Chestplate implements Armor {

    private final String name;
    private final int protectionFrom;
    private final int protectionTo;
    private final Rarity rarity;
    private static final HashMap<String, Chestplate> chestplates = new HashMap<>();

    public Chestplate(String name, int protectionFrom, int protectionTo, Rarity rarity) {
        this(name, protectionFrom, protectionTo, rarity, true);
    }

    public Chestplate(String name, int protectionFrom, int protectionTo, Rarity rarity, boolean addToList) {
        this.name = name;
        this.protectionFrom = protectionFrom;
        this.protectionTo = protectionTo;
        this.rarity = rarity;
        if (addToList) chestplates.put(name.toLowerCase(), this);
    }

    public static Chestplate getChestplate(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return chestplates.get(name.toLowerCase());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getProtectionFrom() {
        return protectionFrom;
    }

    @Override
    public int getProtectionTo() {
        return protectionTo;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public int getProtection() {
        return FormatUtil.getBetween(protectionFrom, protectionTo);
    }
}