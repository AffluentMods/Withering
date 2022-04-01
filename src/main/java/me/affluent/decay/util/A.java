package me.affluent.decay.util;

import me.affluent.decay.armor.*;
import me.affluent.decay.attribute.DamageAttribute;
import me.affluent.decay.attribute.DodgeAttribute;
import me.affluent.decay.attribute.HealthAttribute;
import me.affluent.decay.attribute.ProtectionAttribute;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.weapon.Sword;

public class A {
    public static Helmet h(String n, int p) {
        return new Helmet(n, p, p, Rarity.valueOf(n.split(" ")[0].toUpperCase()), false);
    }

    public static Chestplate c(String n, int p) {
        return new Chestplate(n, p, p, Rarity.valueOf(n.split(" ")[0].toUpperCase()), false);
    }

    public static Gloves g(String n, int p) {
        return new Gloves(n, p, p, Rarity.valueOf(n.split(" ")[0].toUpperCase()), false);
    }

    public static Trousers t(String n, int p) {
        return new Trousers(n, p, p, Rarity.valueOf(n.split(" ")[0].toUpperCase()), false);
    }

    public static Boots b(String n, int p) {
        return new Boots(n, p, p, Rarity.valueOf(n.split(" ")[0].toUpperCase()), false);
    }

    public static Sword s(String n, int damage) {
        return new Sword(n, damage, damage, Rarity.valueOf(n.split(" ")[0].toUpperCase()), false);
    }

    public static HealthAttribute ha(int val) {
        return new HealthAttribute(val, val);
    }

    public static DamageAttribute da(int val) {
        return new DamageAttribute(val, val);
    }

    public static ProtectionAttribute pa(int val) {
        return new ProtectionAttribute(val, val);
    }

    public static DodgeAttribute dc(int val) {
        return new DodgeAttribute(val, val);
    }

}