package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class ConquestWinEvent extends Event{

    private final Player conquerer;
    private final int questAmount;

    public ConquestWinEvent(Player conquerer, int questAmount) {
        this.conquerer = conquerer;
        this.questAmount = questAmount;
    }

    public int getQuestAmount() {
        return questAmount;
    }

    public Player getConquerer() {
        return conquerer;
    }
}
