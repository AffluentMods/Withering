package me.affluent.decay.weapon.wither;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.Weapon;

public class WitherSword extends WitherWeapon {

    public WitherSword(String name, int damageFrom, int damageTo, Rarity rarity) {
        this(name, damageFrom, damageTo, rarity, true);
    }

    public WitherSword(String name, int damageFrom, int damageTo, Rarity rarity, boolean addToList) {
        super(name, damageFrom, damageTo, rarity, addToList);
    }

}