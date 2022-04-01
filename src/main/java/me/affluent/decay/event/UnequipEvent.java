package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class UnequipEvent extends Event {

    private final Player unequipper;

    public UnequipEvent(Player unequipper) {
        this.unequipper = unequipper;
    }

    public Player getUnequipper() {
        return unequipper;
    }
}
