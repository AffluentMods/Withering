package me.affluent.decay.weapon.iron;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.Weapon;

public class IronSword extends IronWeapon {

    public IronSword(String name, int damageFrom, int damageTo, Rarity rarity) {
        this(name, damageFrom, damageTo, rarity, true);
    }

    public IronSword(String name, int damageFrom, int damageTo, Rarity rarity, boolean addToList) {
        super(name, damageFrom, damageTo, rarity, addToList);
    }

}