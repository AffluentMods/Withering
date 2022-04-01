package me.affluent.decay.rarity;

import me.affluent.decay.attribute.*;
import me.affluent.decay.enums.Rarity;

public class AncientRarity extends RarityClass {
    public AncientRarity() {
        super(Rarity.ANCIENT, new DodgeAttribute(12, 14), new ProtectionAttribute(23, 28),
                new GoldCoinsAttribute(17, 17), new ExpAttribute(13, 13));
    }
}