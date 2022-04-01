package me.affluent.decay.attribute;

public class DamageAttribute extends Attribute {

    public DamageAttribute(int from, int to) {
        super("damage", "Damage", -1, -1, -1, -1, from, to, -1, -1, -1, -1, -1, -1);
    }

    @Override
    public Attribute initalize(int value) {
        return new DamageAttribute(value, value);
    }
}