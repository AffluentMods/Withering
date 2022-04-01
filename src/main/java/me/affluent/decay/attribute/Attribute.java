package me.affluent.decay.attribute;

import me.affluent.decay.listener.EventListener;

import java.util.HashMap;

public abstract class Attribute implements EventListener {

    private static final HashMap<String, Attribute> attributes = new HashMap<>();

    private final String name;
    private final String title;
    private final AttributeData<Integer, Integer> dodgeChance;
    private final AttributeData<Integer, Integer> health;
    private final AttributeData<Integer, Integer> damage;
    private final AttributeData<Integer, Integer> goldcoins;
    private final AttributeData<Integer, Integer> protection;
    private final AttributeData<Integer, Integer> exp;

    Attribute(String name, String title, int dodgeFrom, int dodgeTo, int healthFrom, int healthTo, int damageFrom,
              int damageTo, int goldcoinsFrom, int goldcoinsTo, int protectionFrom, int protectionTo, int expFrom,
              int expTo) {
        this.name = name;
        this.title = title;
        this.dodgeChance = new AttributeData<>(dodgeFrom, dodgeTo);
        this.health = new AttributeData<>(healthFrom, healthTo);
        this.damage = new AttributeData<>(damageFrom, damageTo);
        this.goldcoins = new AttributeData<>(goldcoinsFrom, goldcoinsTo);
        this.protection = new AttributeData<>(protectionFrom, protectionTo);
        this.exp = new AttributeData<>(expFrom, expTo);
        attributes.put(name.toLowerCase(), this);
    }

    public AttributeData<Integer, Integer> getExp() {
        return exp;
    }

    public AttributeData<Integer, Integer> getGoldCoins() {
        return goldcoins;
    }

    public AttributeData<Integer, Integer> getDamage() {
        return damage;
    }

    public AttributeData<Integer, Integer> getHealth() {
        return health;
    }

    public AttributeData<Integer, Integer> getProtection() {
        return protection;
    }

    public AttributeData<Integer, Integer> getDodgeChance() {
        return dodgeChance;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public AttributeData<Integer, Integer> getValue() {
        if (dodgeChance.getValue1() != -1) return dodgeChance;
        if (exp.getValue1() != -1) return exp;
        if (goldcoins.getValue1() != -1) return goldcoins;
        if (health.getValue1() != -1) return health;
        if (damage.getValue1() != -1) return damage;
        if (protection.getValue1() != -1) return protection;
        return new AttributeData<>(-1, -1);
    }

    public static Attribute getAttribute(String name) {
        return attributes.get(name.toLowerCase());
    }

    public static Attribute getAttribute(String name, String value) {
        return getAttribute(name).initalize(Integer.parseInt(value));
    }

    public abstract Attribute initalize(int value);

    public static class AttributeData<V1, V2> {

        private V1 value1;
        private V2 value2;

        AttributeData(V1 value1, V2 value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public V1 getValue1() {
            return value1;
        }

        public V2 getValue2() {
            return value2;
        }
    }
}