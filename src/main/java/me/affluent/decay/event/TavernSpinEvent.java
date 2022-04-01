package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class TavernSpinEvent extends Event{

    private final Player spinner;
    private final int spinAmount;

    public TavernSpinEvent(Player spinner, int spinAmount) {
        this.spinner = spinner;
        this.spinAmount = spinAmount;
    }

    public int getSpinAmount() {
        return spinAmount;
    }

    public Player getSpinner() {
        return spinner;
    }
}
