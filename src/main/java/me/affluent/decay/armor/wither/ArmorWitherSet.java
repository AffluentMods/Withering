package me.affluent.decay.armor.wither;

import me.affluent.decay.armor.*;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Bow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Sword;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherSword;

import java.util.Arrays;
import java.util.List;

public class ArmorWitherSet {

    private final String baseName;
    private WitherHelmet helmet;
    private WitherChestplate chestplate;
    private WitherGloves gloves;
    private WitherTrousers trousers;
    private WitherBoots boots;
    private WitherSword sword;
    private WitherShield shield;

    public ArmorWitherSet(String baseName, int protectionFrom, int protectionTo, int damageFrom, int damageTo,
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
                this.helmet = new WitherHelmet(a + " Helmet", protectionFrom, protectionTo, rarity);
                this.chestplate = new WitherChestplate(a + " Chestplate", protectionFrom, protectionTo, rarity);
                this.gloves = new WitherGloves(a + " Gloves", protectionFrom, protectionTo, rarity);
                this.trousers = new WitherTrousers(a + " Trousers", protectionFrom, protectionTo, rarity);
                this.boots = new WitherBoots(a + " Boots", protectionFrom, protectionTo, rarity);
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
                this.sword = new WitherSword(a + " Sword", damageFrom, damageTo, rarity);
            }
        } else {
            this.sword = null;
        }
        if (shieldProtection != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.shield = new WitherShield(a + " Shield", shieldProtection);
            }
        } else {
            this.shield = null;
        }
    }

    public String getBaseName() {
        return baseName;
    }

    public WitherHelmet getHelmet() {
        return helmet;
    }

    public WitherChestplate getChestplate() {
        return chestplate;
    }

    public WitherGloves getGloves() {
        return gloves;
    }

    public WitherTrousers getTrousers() {
        return trousers;
    }

    public WitherBoots getBoots() {
        return boots;
    }

    public WitherSword getSword() {
        return sword;
    }

    public WitherShield getShield() {
        return shield;
    }
}
