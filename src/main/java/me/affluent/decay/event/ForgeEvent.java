package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class ForgeEvent extends Event {

    private final Player forger;
    private final int ingotCost;

    public ForgeEvent(Player forger, int ingotCost) {
        this.forger = forger;
        this.ingotCost = ingotCost;
    }

    public Player getForger() {
        return forger;
    }

    public int getIngotCost() {
        return ingotCost;
    }
}
