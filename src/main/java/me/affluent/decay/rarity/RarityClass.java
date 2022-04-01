package me.affluent.decay.rarity;

import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.enums.Rarity;

import java.util.Arrays;
import java.util.List;

public abstract class RarityClass {

    private final Rarity rarity;
    private final List<Attribute> attributes;

    public RarityClass(Rarity rarity, Attribute... attributes) {
        this.rarity = rarity;
        this.attributes = Arrays.asList(attributes);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Rarity getRarity() {
        return rarity;
    }
}