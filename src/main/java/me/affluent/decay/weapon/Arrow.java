package me.affluent.decay.weapon;

import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.pvp.Fight;
import me.affluent.decay.util.system.EmoteUtil;

import java.util.HashMap;

public class Arrow {

    private final String name;
    private final int damage;
    private static final HashMap<String, Arrow> arrows = new HashMap<>();

    public Arrow(String name, int damage) {
        this.name = name;
        this.damage = damage;
        arrows.put(name.toLowerCase(), this);
    }

    public static Arrow getArrow(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return arrows.get(name.toLowerCase());
    }

    public ItemType getItemType() {
        String[] split = name.split(" ");
        String itemTypeName = split[1];
        if (split.length > 3) itemTypeName += " " + split[2];
        for (ItemType itemType : ItemType.values()) {
            if (itemType.name().replace("_", " ").equalsIgnoreCase(itemTypeName)) return itemType;
        }
        return null;
    }

    public Rarity getRarity() {
        String rarityName = name.split(" ")[0];
        for (Rarity rarity : Rarity.values()) {
            if (rarity.name().equalsIgnoreCase(rarityName)) return rarity;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getRarityDamage() {
        return Fight.round(getDamage(), Fight.getRarityDamage(getRarity()));
    }

    public String getGearType() {
        String[] split = getName().split(" ");
        return split[split.length - 1];
    }

    public String getDisplay() {
        return EmoteUtil.getEmoteMention(getRarity().name().toLowerCase() + " " + getItemType().name() + " " + getGearType().toLowerCase()) + " " +
                capitalizeFully(getName());
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