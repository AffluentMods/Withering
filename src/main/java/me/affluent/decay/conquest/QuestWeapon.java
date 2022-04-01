package me.affluent.decay.conquest;

import me.affluent.decay.weapon.Weapon;

public class QuestWeapon {

    private final Weapon weapon;
    private final double damageMultiplier;

    public QuestWeapon(Weapon weapon) {
        this(weapon, 0);
    }

    public QuestWeapon(Weapon weapon, double damageMultiplier) {
        this.weapon = weapon;
        this.damageMultiplier = damageMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}