package me.affluent.decay.weapon.wither;

import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.pvp.AttackRate;
import me.affluent.decay.pvp.Fight;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.weapon.Bow;
import me.affluent.decay.weapon.Sword;

import java.util.HashMap;

public abstract class WitherWeapon {

    private final String name;
    private final int damageFrom;
    private final int damageTo;
    private final Rarity rarity;
    private static final HashMap<String, WitherWeapon> weapons = new HashMap<>();

    public WitherWeapon(String name, int damageFrom, int damageTo, Rarity rarity) {
        this(name, damageFrom, damageTo, rarity, true);
    }

    public WitherWeapon(String name, int damageFrom, int damageTo, Rarity rarity, boolean addToList) {
        this.name = name;
        this.damageFrom = damageFrom;
        this.damageTo = damageTo;
        this.rarity = rarity;
        if (addToList) {
            weapons.put(name.toLowerCase(), this);
        }
    }

    public static WitherWeapon getWeapon(String name) {
        if (name.equalsIgnoreCase("")) return null;
        return weapons.get(name.toLowerCase());
    }

    public String getName() {
        return name;
    }

    public int getDamageFrom() {
        return damageFrom;
    }

    public int getDamageTo() {
        return damageTo;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getDamageBetween() {
        return FormatUtil.getBetween(damageFrom, damageTo);
    }

    public int getDamageMiddle() {
        return FormatUtil.getMiddle(damageFrom, damageTo);
    }

    public int getRarityDamage() {
        return Fight.round(getDamageMiddle(), Fight.getRarityDamage(getRarity()));
    }

    public int getRarityProtection() {
        return Fight.round(getDamageMiddle(), Fight.getRarityDamage(getRarity()));
    }

    public String getGearType() {
        String[] split = getName().split(" ");
        return split[split.length - 1];
    }

    public String getDisplay() {
        return EmoteUtil.getEmoteMention(getRarity().name().toLowerCase() + " " + getItemType().name() + " " + getGearType().toLowerCase()) + " " +
                capitalizeFully(getName());
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

    static String capitalizeFully(String string) {
        String capitalized = "";
        for(String word : string.split(" ")) {
            capitalized += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        if(capitalized.endsWith(" ")) capitalized = capitalized.substring(0, capitalized.length()-1);
        return capitalized;
    }
}
