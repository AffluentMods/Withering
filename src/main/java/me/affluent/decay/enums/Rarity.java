package me.affluent.decay.enums;

public enum Rarity {
    JUNK(10),
    COMMON(55),
    UNCOMMON(140),
    RARE(270),
    EPIC(405),
    LEGEND(590),
    MYTHIC(815),
    ANCIENT(1075),
    ARTIFACT(1720);

    private final int power;

    Rarity(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public static boolean isRarity(String a) {
        boolean b = false;
        for (Rarity c : values())
            if (c.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                b = true;
                break;
            }
        return b;
    }

    public static Rarity getRarity(String a) {
        for (Rarity b : values())
            if (b.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                return b;
            }
        return null;
    }

    public Rarity better() {
        switch (this) {
            case JUNK:
                return COMMON;
            case COMMON:
                return UNCOMMON;
            case UNCOMMON:
                return RARE;
            case RARE:
                return EPIC;
            case EPIC:
                return LEGEND;
            case LEGEND:
                return MYTHIC;
            case MYTHIC:
                return ANCIENT;
        }
        return null;
    }

    public Rarity worse() {
        switch (this) {
            case JUNK:
            case COMMON:
                return JUNK;
            case UNCOMMON:
                return COMMON;
            case RARE:
                return UNCOMMON;
            case EPIC:
                return RARE;
            case LEGEND:
                return EPIC;
            case MYTHIC:
                return LEGEND;
            case ANCIENT:
                return MYTHIC;
        }
        return null;
    }

    public Rarity betterOther() {
        switch (this) {
            case JUNK:
                return COMMON;
            case COMMON:
                return UNCOMMON;
            case UNCOMMON:
                return RARE;
            case RARE:
                return EPIC;
            case EPIC:
                return LEGEND;
            case LEGEND:
                return MYTHIC;
            case MYTHIC:
                return ANCIENT;
            case ANCIENT:
                return ARTIFACT;
        }
        return null;
    }
}