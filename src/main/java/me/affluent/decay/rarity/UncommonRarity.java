package me.affluent.decay.rarity;

import me.affluent.decay.attribute.DodgeAttribute;
import me.affluent.decay.attribute.GoldCoinsAttribute;
import me.affluent.decay.attribute.HealthAttribute;
import me.affluent.decay.attribute.ProtectionAttribute;
import me.affluent.decay.enums.Rarity;

public class UncommonRarity extends RarityClass {
    public UncommonRarity() {
        super(Rarity.UNCOMMON, new DodgeAttribute(2, 3), new ProtectionAttribute(3, 5),
                new GoldCoinsAttribute(2, 2));
    }
}