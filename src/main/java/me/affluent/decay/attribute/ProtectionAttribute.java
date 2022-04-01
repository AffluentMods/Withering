package me.affluent.decay.attribute;

public class ProtectionAttribute extends Attribute {

    public ProtectionAttribute(int from, int to) {
        super("protection", "Protection", -1, -1, -1, -1, -1, -1, -1, -1, from, to, -1, -1);
    }

    @Override
    public Attribute initalize(int value) {
        return new ProtectionAttribute(value, value);
    }
}