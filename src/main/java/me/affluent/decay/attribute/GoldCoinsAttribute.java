package me.affluent.decay.attribute;

public class GoldCoinsAttribute extends Attribute {

    public GoldCoinsAttribute(int from, int to) {
        super("gold_coins", "Gold Coins", -1, -1, -1, -1, -1, -1, from, to, -1, -1, -1, -1);
    }

    @Override
    public Attribute initalize(int value) {
        return new GoldCoinsAttribute(value, value);
    }
}