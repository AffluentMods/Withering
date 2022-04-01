package me.affluent.decay.chest.rewards;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.enums.ItemType;

public class ChestRewardWood extends Chest {

    public ChestRewardWood() {
        super(ItemType.WOOD, "Wood Chest", 1);
        setMedallion(5, 8);
        setReward(ItemType.WOOD, 65);
        setReward(ItemType.COPPER, 30);
        setReward(ItemType.REINFORCED, 5);
    }

}