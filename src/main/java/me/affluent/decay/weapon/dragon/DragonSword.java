package me.affluent.decay.weapon.dragon;

import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.Weapon;

public class DragonSword extends DragonWeapon {

    public DragonSword(String name, int damageFrom, int damageTo, Rarity rarity) {
        this(name, damageFrom, damageTo, rarity, true);
    }

    public DragonSword(String name, int damageFrom, int damageTo, Rarity rarity, boolean addToList) {
        super(name, damageFrom, damageTo, rarity, addToList);
    }

}