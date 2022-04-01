package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class ItemStarEvent extends Event {

    private final Player starrer;
    private final int starAmount;

    public ItemStarEvent(Player starrer, int starAmount) {
        this.starAmount = starAmount;
        this.starrer = starrer;
    }

    public int getStarAmount() {
        return starAmount;
    }

    public Player getStarrer() {
        return starrer;
    }
}
