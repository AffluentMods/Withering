package me.affluent.decay.conquest;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

public class QuestArmor {

    private final Armor armor;
    private final List<Attribute> attributes;

    public QuestArmor(Armor armor, List<Attribute> attributes) {
        this.armor = armor;
        if (attributes == null) attributes = new ArrayList<>();
        this.attributes = attributes;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Armor getArmor() {
        return armor;
    }
}