package me.affluent.decay.rarity;

import me.affluent.decay.attribute.*;
import me.affluent.decay.enums.Rarity;

public class MythicRarity extends RarityClass {
    public MythicRarity() {
        super(Rarity.MYTHIC, new DodgeAttribute(10, 11), new ProtectionAttribute(19, 24),
                new GoldCoinsAttribute(11, 11), new ExpAttribute(8, 8));
    }
}