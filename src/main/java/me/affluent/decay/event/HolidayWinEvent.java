package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class HolidayWinEvent extends Event {

    private final Player winner;
    private final int stageNumber;

    public HolidayWinEvent(Player winner, int stageNumber) {
        this.winner = winner;
        this.stageNumber = stageNumber;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public Player getWinner() {
        return winner;
    }
}
