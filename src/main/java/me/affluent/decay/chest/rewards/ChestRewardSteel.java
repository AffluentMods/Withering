package me.affluent.decay.chest.rewards;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.enums.ItemType;

public class ChestRewardSteel extends Chest {

    public ChestRewardSteel() {
        super(ItemType.STEEL, "Steel Chest", 60);
        setMedallion(225, 335);
        setReward(ItemType.TITANIUM, 65);
        setReward(ItemType.IRON, 30);
        setReward(ItemType.STEEL, 5);
    }

}