package me.affluent.decay.attribute;

public class DodgeAttribute extends Attribute {

    public DodgeAttribute(int from, int to) {
        super("dodge_chance", "Dodge Chance", from, to, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    @Override
    public Attribute initalize(int value) {
        return new DodgeAttribute(value, value);
    }
}