package me.affluent.decay.enums;

public enum PetRarity {
    JUNK(),
    COMMON(),
    UNCOMMON(),
    RARE(),
    EPIC(),
    LEGEND(),
    MYTHIC(),
    ANCIENT(),
    ARTIFACT();

    public static boolean isPetRarity(String a) {
        boolean b = false;
        for (PetRarity c : values())
            if (c.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                b = true;
                break;
            }
        return b;
    }

    public static PetRarity getPetRarity(String a) {
        for (PetRarity b : values())
            if (b.name().replaceAll("_", " ").equalsIgnoreCase(a.replaceAll("_", " "))) {
                return b;
            }
        return null;
    }

    public PetRarity betterPetRarity() {
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

