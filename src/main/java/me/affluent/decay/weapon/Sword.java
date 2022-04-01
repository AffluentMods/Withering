package me.affluent.decay.weapon;

import me.affluent.decay.enums.Rarity;

public class Sword extends Weapon {

    public Sword(String name, int damageFrom, int damageTo, Rarity rarity) {
        this(name, damageFrom, damageTo, rarity, true);
    }

    public Sword(String name, int damageFrom, int damageTo, Rarity rarity, boolean addToList) {
        super(name, damageFrom, damageTo, rarity, addToList);
    }

}