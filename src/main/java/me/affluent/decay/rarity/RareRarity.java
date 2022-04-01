package me.affluent.decay.rarity;

import me.affluent.decay.attribute.*;
import me.affluent.decay.enums.Rarity;

public class RareRarity extends RarityClass {
    public RareRarity() {
        super(Rarity.RARE, new DodgeAttribute(4, 5), new ProtectionAttribute(6, 8),
                new GoldCoinsAttribute(3, 3), new ExpAttribute(2, 2));
    }
}