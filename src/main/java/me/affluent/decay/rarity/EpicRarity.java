package me.affluent.decay.rarity;

import me.affluent.decay.attribute.*;
import me.affluent.decay.enums.Rarity;

public class EpicRarity extends RarityClass {
    public EpicRarity() {
        super(Rarity.EPIC, new DodgeAttribute(6, 7), new ProtectionAttribute(9, 12),
                new GoldCoinsAttribute(5, 5), new ExpAttribute(3, 3));
    }
}