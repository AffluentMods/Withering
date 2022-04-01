package me.affluent.decay.armor;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.util.system.FormatUtil;

import java.util.HashMap;

public class Gloves implements Armor {

    private final String name;
    private final int protectionFrom;
    private final int protectionTo;
    private final Rarity rarity;
    private static final HashMap<String, Gloves> gloves = new HashMap<>();

    public Gloves(String name, int protectionFrom, int protectionTo, Rarity rarity) {
        this(name, protectionFrom, protectionTo, rarity, true);
    }

    public Gloves(String name, int protectionFrom, int protectionTo, Rarity rarity, boolean addToList) {
        this.name = name;
        this.protectionFrom = protectionFrom;
        this.protectionTo = protectionTo;
        this.rarity = rarity;
        if (addToList) gloves.put(name.toLowerCase(), this);
    }

    public static Gloves getGloves(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return gloves.get(name.toLowerCase());
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