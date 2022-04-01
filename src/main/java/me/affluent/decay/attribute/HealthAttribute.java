package me.affluent.decay.attribute;

public class HealthAttribute extends Attribute {

    public HealthAttribute(int from, int to) {
        super("health", "Health", -1, -1, from, to, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    @Override
    public Attribute initalize(int value) {
        return new HealthAttribute(value, value);
    }
}