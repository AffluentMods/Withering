package me.affluent.decay.armor.iron;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronSword;

import java.util.Arrays;
import java.util.List;

public class ArmorIronSet {

    private final String baseName;
    private IronHelmet helmet;
    private IronChestplate chestplate;
    private IronGloves gloves;
    private IronTrousers trousers;
    private IronBoots boots;
    private IronSword sword;
    private IronShield shield;

    public ArmorIronSet(String baseName, int protectionFrom, int protectionTo, int damageFrom, int damageTo,
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
                this.helmet = new IronHelmet(a + " Helmet", protectionFrom, protectionTo, rarity);
                this.chestplate = new IronChestplate(a + " Chestplate", protectionFrom, protectionTo, rarity);
                this.gloves = new IronGloves(a + " Gloves", protectionFrom, protectionTo, rarity);
                this.trousers = new IronTrousers(a + " Trousers", protectionFrom, protectionTo, rarity);
                this.boots = new IronBoots(a + " Boots", protectionFrom, protectionTo, rarity);
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
                this.sword = new IronSword(a + " Sword", damageFrom, damageTo, rarity);
            }
        } else {
            this.sword = null;
        }
        if (shieldProtection != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.shield = new IronShield(a + " Shield", shieldProtection);
            }
        } else {
            this.shield = null;
        }
    }

    public String getBaseName() {
        return baseName;
    }

    public IronHelmet getHelmet() {
        return helmet;
    }

    public IronChestplate getChestplate() {
        return chestplate;
    }

    public IronGloves getGloves() {
        return gloves;
    }

    public IronTrousers getTrousers() {
        return trousers;
    }

    public IronBoots getBoots() {
        return boots;
    }

    public IronSword getSword() {
        return sword;
    }

    public IronShield getShield() {
        return shield;
    }
}