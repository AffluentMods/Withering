package me.affluent.decay.attribute;

public class ExpAttribute extends Attribute {

    public ExpAttribute(int from, int to) {
        super("exp", "Exp", -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, from, to);
    }

    @Override
    public Attribute initalize(int value) {
        return new ExpAttribute(value, value);
    }
}