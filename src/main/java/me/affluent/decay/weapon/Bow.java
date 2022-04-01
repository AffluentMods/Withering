package me.affluent.decay.weapon;

import me.affluent.decay.enums.Rarity;

public class Bow extends Weapon {

    public Bow(String name, int damageFrom, int damageTo, Rarity rarity) {
        this(name, damageFrom, damageTo, rarity, true);
    }

    public Bow(String name, int damageFrom, int damageTo, Rarity rarity, boolean addToList) {
        super(name, damageFrom, damageTo, rarity);
    }

}