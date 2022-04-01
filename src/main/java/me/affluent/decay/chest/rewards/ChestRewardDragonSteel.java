package me.affluent.decay.chest.rewards;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.enums.ItemType;

public class ChestRewardDragonSteel extends Chest {

    public ChestRewardDragonSteel() {
        super(ItemType.DRAGON_STEEL, "Dragon-Steel Chest", 75);
        setMedallion(420, 640);
        setReward(ItemType.IRON, 50);
        setReward(ItemType.STEEL, 35);
        setReward(ItemType.CARBON_STEEL, 10);
        setReward(ItemType.DRAGON_STEEL, 5);
    }

}