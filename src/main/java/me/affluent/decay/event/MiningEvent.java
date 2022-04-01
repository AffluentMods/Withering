package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class MiningEvent extends Event {

    private final Player miner;
    private final int mineAmount;

    public MiningEvent(Player miner, int mineAmount) {
        this.mineAmount = mineAmount;
        this.miner = miner;
    }

    public int getMineAmount() {
        return mineAmount;
    }

    public Player getMiner() {
        return miner;
    }
}
