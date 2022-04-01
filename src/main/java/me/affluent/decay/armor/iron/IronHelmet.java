package me.affluent.decay.armor.iron;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.util.system.FormatUtil;

import java.util.HashMap;

public class IronHelmet implements Armor {

    private final String name;
    private final int protectionFrom;
    private final int protectionTo;
    private final Rarity rarity;
    private static final HashMap<String, IronHelmet> ironHelmet = new HashMap<>();

    public IronHelmet(String name, int protectionFrom, int protectionTo, Rarity rarity) {
        this(name, protectionFrom, protectionTo, rarity, true);
    }

    public IronHelmet(String name, int protectionFrom, int protectionTo, Rarity rarity, boolean addToList) {
        this.name = name;
        this.protectionFrom = protectionFrom;
        this.protectionTo = protectionTo;
        this.rarity = rarity;
        if (addToList) ironHelmet.put(name.toLowerCase(), this);
    }

    public static IronHelmet getIronHelmet(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return ironHelmet.get(name.toLowerCase());
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
