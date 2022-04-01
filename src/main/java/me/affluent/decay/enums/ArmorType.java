package me.affluent.decay.enums;

public enum ArmorType {

    HELMET,
    CHESTPLATE,
    GLOVES,
    TROUSERS,
    BOOTS,
    SWORD,
    STAFF,
    BOW,
    ARROW,
    SHIELD;

    public static boolean isArmorType(String a) {
        boolean b = false;
        for (ArmorType c : values())
            if (c.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                b = true;
                break;
            }
        return b;
    }

    public static ArmorType getArmorType(String a) {
        for (ArmorType b : values())
            if (b.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                return b;
            }
        return null;
    }
}
