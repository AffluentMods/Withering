package me.affluent.decay.rarity;

import me.affluent.decay.attribute.*;
import me.affluent.decay.enums.Rarity;

public class ArtifactRarity extends RarityClass {
    public ArtifactRarity() {
        super(Rarity.ARTIFACT, new DodgeAttribute(15, 16), new ProtectionAttribute(50, 50),
                new GoldCoinsAttribute(25, 25), new ExpAttribute(20, 20));
    }
}