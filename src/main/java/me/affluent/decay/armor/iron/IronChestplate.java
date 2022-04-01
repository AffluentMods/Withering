package me.affluent.decay.armor.iron;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.util.system.FormatUtil;

import java.util.HashMap;

public class IronChestplate implements Armor {

    private final String name;
    private final int protectionFrom;
    private final int protectionTo;
    private final Rarity rarity;
    private static final HashMap<String, IronChestplate> ironChestplate = new HashMap<>();

    public IronChestplate(String name, int protectionFrom, int protectionTo, Rarity rarity) {
        this(name, protectionFrom, protectionTo, rarity, true);
    }

    public IronChestplate(String name, int protectionFrom, int protectionTo, Rarity rarity, boolean addToList) {
        this.name = name;
        this.protectionFrom = protectionFrom;
        this.protectionTo = protectionTo;
        this.rarity = rarity;
        if (addToList) ironChestplate.put(name.toLowerCase(), this);
    }

    public static IronChestplate getIronChestplate(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return ironChestplate.get(name.toLowerCase());
    }

    @Override
    public String getName() {
        return name;
    }

    public int getProtectionFrom() {
        return protectionFrom;
    }

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
