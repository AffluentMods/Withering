package me.affluent.decay.chest.rewards;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.enums.ItemType;

public class ChestRewardMetal extends Chest {

    public ChestRewardMetal() {
        super(ItemType.METAL, "Metal Chest", 25);
        setMedallion(22, 36);
        setReward(ItemType.COPPER, 65);
        setReward(ItemType.REINFORCED, 30);
        setReward(ItemType.TITANIUM, 5);
    }

}