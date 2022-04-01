package me.affluent.decay.chest.rewards;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.enums.ItemType;

public class ChestRewardTitanium extends Chest {

    public ChestRewardTitanium() {
        super(ItemType.TITANIUM, "Titanium Chest", 40);
        setMedallion(85, 115);
        setReward(ItemType.REINFORCED, 65);
        setReward(ItemType.TITANIUM, 30);
        setReward(ItemType.IRON, 5);
    }

}