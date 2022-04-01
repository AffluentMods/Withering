package me.affluent.decay.armor;

import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.util.system.EmoteUtil;

public interface Armor {
    String getName();

    int getProtection();

    int getProtectionFrom();

    int getProtectionTo();

    Rarity getRarity();

    default String getDisplay() {
        return EmoteUtil.getEmoteMention(getRarity().name().toLowerCase() + " " + getItemType().name() + " " + getGearType().toLowerCase()) + " " + capitalizeFully(getName());    }

    default String getGearType() {
        String[] split = getName().split(" ");
        return split[split.length - 1];
    }

    default ItemType getItemType() {
        String[] split = getName().split(" ");
        String itemTypeName = split[1];
        if (split.length > 3) itemTypeName += " " + split[2];
        for (ItemType itemType : ItemType.values()) {
            if (itemType.name().replace("_", " ").equalsIgnoreCase(itemTypeName)) return itemType;
        }
        return null;
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
