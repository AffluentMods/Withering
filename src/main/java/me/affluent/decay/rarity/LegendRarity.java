package me.affluent.decay.rarity;

import me.affluent.decay.attribute.*;
import me.affluent.decay.enums.Rarity;

public class LegendRarity extends RarityClass {
    public LegendRarity() {
        super(Rarity.LEGEND, new DodgeAttribute(8, 9), new ProtectionAttribute(13, 17),
                new GoldCoinsAttribute(8, 8), new ExpAttribute(5, 5));
    }
}