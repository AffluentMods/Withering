package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class GiftingEvent extends Event {

    private final Player gifter;
    private final String reward;

    public GiftingEvent(Player gifter, String reward) {
        this.gifter = gifter;
        this.reward = reward;
    }

    public Player getGifter() {
        return gifter;
    }

    public String getReward() {
        return reward;
    }
}
