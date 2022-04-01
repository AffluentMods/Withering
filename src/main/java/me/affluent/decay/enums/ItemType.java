package me.affluent.decay.enums;

public enum ItemType {
    WOOD(1),
    METAL(-1),
    COPPER(15),
    REINFORCED(25),
    TITANIUM(40),
    IRON(55),
    STEEL(75),
    CARBON_STEEL(90),
    DRAGON_STEEL(110),
    TITAN_ALLOY(150),
    WITHER(151);

    private final int levelRequirement;

    ItemType(int levelRequirement) {
        this.levelRequirement = levelRequirement;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public static boolean isItemType(String a) {
        boolean b = false;
        for (ItemType c : values())
            if (c.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                b = true;
                break;
            }
        return b;
    }

    public static ItemType getItemType(String a) {
        for (ItemType b : values())
            if (b.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                return b;
            }
        return null;
    }

    public static String getByLevel(int level) {
        String itn = "-";
        for (ItemType it : values()) {
            if (it.getLevelRequirement() <= level) itn = it.name().replaceAll("_", " ").toLowerCase();
        }
        return itn;
    }
}