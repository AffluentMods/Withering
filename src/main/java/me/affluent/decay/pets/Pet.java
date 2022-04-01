package me.affluent.decay.pets;

import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.util.system.EmoteUtil;

public interface Pet {
    String getName();

    int getStarterHP();

    int getStarterDMG();

    double getLevelHP();

    double getLevelDMG();

    PetRarity getRarity();

    default String getDisplay() {
        return EmoteUtil.getEmoteMention(getName()) + " " + capitalizeFully(getName());
    }

    static String capitalizeFully(String string) {
        String capitalized = "";
        for(String word : string.split(" ")) {
            capitalized += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        if(capitalized.endsWith(" ")) capitalized = capitalized.substring(0, capitalized.length()-1);
        return capitalized;
    }
}
