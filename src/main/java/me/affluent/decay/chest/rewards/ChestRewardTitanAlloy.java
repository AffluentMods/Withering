package me.affluent.decay.chest.rewards;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.enums.ItemType;

public class ChestRewardTitanAlloy extends Chest {

    public ChestRewardTitanAlloy() {
        super(ItemType.TITAN_ALLOY, "Titan-Alloy Chest", 90);
        setMedallion(1200, 1750);
        setReward(ItemType.STEEL, 40);
        setReward(ItemType.CARBON_STEEL, 40);
        setReward(ItemType.DRAGON_STEEL, 17);
        setReward(ItemType.TITAN_ALLOY, 3);
    }

}