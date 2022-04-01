package me.affluent.decay.rarity;

import me.affluent.decay.attribute.DodgeAttribute;
import me.affluent.decay.attribute.HealthAttribute;
import me.affluent.decay.attribute.ProtectionAttribute;
import me.affluent.decay.enums.Rarity;

public class CommonRarity extends RarityClass {

    public CommonRarity() {
        super(Rarity.COMMON, new DodgeAttribute(1, 1), new ProtectionAttribute(2, 3));
    }

}