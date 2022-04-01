package me.affluent.decay.event;

import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.ItemType;

public class ItemLevelUpEvent extends Event{

    private final Player leveler;
    private final int levelAmount;
    private final ItemType itemType;

    public ItemLevelUpEvent(Player leveler, int levelAmount, ItemType itemType) {
        this.leveler = leveler;
        this.levelAmount = levelAmount;
        this.itemType = itemType;
    }

    public int getItemLevelUpAmount() {
        return levelAmount;
    }

    public Player getLeveler() {
        return leveler;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
