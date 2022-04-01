package me.affluent.decay.armor.dragon;

import me.affluent.decay.armor.wither.*;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonSword;

import java.util.Arrays;
import java.util.List;

public class ArmorDragonSet {

    private final String baseName;
    private DragonHelmet helmet;
    private DragonChestplate chestplate;
    private DragonGloves gloves;
    private DragonTrousers trousers;
    private DragonBoots boots;
    private DragonSword sword;
    private DragonShield shield;

    public ArmorDragonSet(String baseName, int protectionFrom, int protectionTo, int damageFrom, int damageTo,
                          int shieldProtection) {
        String b = baseName.substring(0, 1).toUpperCase() + baseName.substring(1);
        this.baseName = b;
        List<Rarity> rarities = Arrays.asList(Rarity.ARTIFACT, Rarity.ANCIENT, Rarity.MYTHIC, Rarity.LEGEND, Rarity.EPIC, Rarity.RARE,
                Rarity.UNCOMMON, Rarity.COMMON, Rarity.JUNK);
        if (protectionFrom != -1 && protectionTo != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.helmet = new DragonHelmet(a + " Helmet", protectionFrom, protectionTo, rarity);
                this.chestplate = new DragonChestplate(a + " Chestplate", protectionFrom, protectionTo, rarity);
                this.gloves = new DragonGloves(a + " Gloves", protectionFrom, protectionTo, rarity);
                this.trousers = new DragonTrousers(a + " Trousers", protectionFrom, protectionTo, rarity);
                this.boots = new DragonBoots(a + " Boots", protectionFrom, protectionTo, rarity);
            }
        } else {
            this.helmet = null;
            this.chestplate = null;
            this.gloves = null;
            this.trousers = null;
            this.boots = null;
        }
        if (damageFrom != -1 && damageTo != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.sword = new DragonSword(a + " Sword", damageFrom, damageTo, rarity);
            }
        } else {
            this.sword = null;
        }
        if (shieldProtection != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.shield = new DragonShield(a + " Shield", shieldProtection);
            }
        } else {
            this.shield = null;
        }
    }

    public String getBaseName() {
        return baseName;
    }

    public DragonHelmet getHelmet() {
        return helmet;
    }

    public DragonChestplate getChestplate() {
        return chestplate;
    }

    public DragonGloves getGloves() {
        return gloves;
    }

    public DragonTrousers getTrousers() {
        return trousers;
    }

    public DragonBoots getBoots() {
        return boots;
    }

    public DragonSword getSword() {
        return sword;
    }

    public DragonShield getShield() {
        return shield;
    }
}
