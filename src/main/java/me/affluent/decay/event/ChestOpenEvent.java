package me.affluent.decay.event;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.ItemType;

import java.util.HashMap;

public class ChestOpenEvent extends Event {

    private final Player opener;
    private final Chest chest;
    private final HashMap<String, Integer> rewards;
    private final int openAmount;

    public ChestOpenEvent(Player opener, ItemType chestType, HashMap<String, Integer> rewards, int openAmount) {
        this.opener = opener;
        this.chest = Chest.getChest(chestType);
        this.rewards = rewards;
        this.openAmount = openAmount;
    }

    public int getOpenAmount() {
        return openAmount;
    }

    public HashMap<String, Integer> getRewards() {
        return rewards;
    }

    public Chest getChest() {
        return chest;
    }

    public Player getOpener() {
        return opener;
    }
}