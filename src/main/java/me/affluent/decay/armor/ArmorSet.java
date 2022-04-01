package me.affluent.decay.armor;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.*;

import java.util.Arrays;
import java.util.List;

public class ArmorSet {

    private final String baseName;
    private Helmet helmet;
    private Chestplate chestplate;
    private Gloves gloves;
    private Trousers trousers;
    private Boots boots;
    private Sword sword;
    private Bow bow;
    private Arrow arrow;
    private Shield shield;
    private Staff staff;


    public ArmorSet(String baseName, int protectionFrom, int protectionTo, int damageFrom, int damageTo,
                    int arrowDamage, int shieldProtection) {
        String b = baseName.substring(0, 1).toUpperCase() + baseName.substring(1);
        this.baseName = b;
        List<Rarity> rarities = Arrays.asList(Rarity.ARTIFACT, Rarity.ANCIENT, Rarity.MYTHIC, Rarity.LEGEND, Rarity.EPIC, Rarity.RARE,
                Rarity.UNCOMMON, Rarity.COMMON, Rarity.JUNK);
        if (protectionFrom != -1 && protectionTo != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.helmet = new Helmet(a + " Helmet", protectionFrom, protectionTo, rarity);
                this.chestplate = new Chestplate(a + " Chestplate", protectionFrom, protectionTo, rarity);
                this.gloves = new Gloves(a + " Gloves", protectionFrom, protectionTo, rarity);
                this.trousers = new Trousers(a + " Trousers", protectionFrom, protectionTo, rarity);
                this.boots = new Boots(a + " Boots", protectionFrom, protectionTo, rarity);
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
                int value1 = (int) (damageFrom * 1.37);
                int value2 = (int) (damageTo * 1.37);
                this.sword = new Sword(a + " Sword", damageFrom, damageTo, rarity);
                this.bow = new Bow(a + " Bow", damageFrom, damageTo, rarity);
                this.staff = new Staff(a + " Staff", value1, value2, rarity);
            }
        } else {
            this.sword = null;
        }
        if (arrowDamage != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.arrow = new Arrow(a + " Arrow", arrowDamage);
            }
        } else {
            this.arrow = null;
        }
        if (shieldProtection != -1) {
            for (Rarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                a += " " + b;
                this.shield = new Shield(a + " Shield", shieldProtection);
            }
        } else {
            this.shield = null;
        }
    }

    public String getBaseName() {
        return baseName;
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public Chestplate getChestplate() {
        return chestplate;
    }

    public Gloves getGloves() {
        return gloves;
    }

    public Trousers getTrousers() {
        return trousers;
    }

    public Boots getBoots() {
        return boots;
    }

    public Sword getSword() {
        return sword;
    }

    public Staff getStaff() {
        return staff;
    }

    public Bow getBow() {
        return bow;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public Shield getShield() {
        return shield;
    }
}