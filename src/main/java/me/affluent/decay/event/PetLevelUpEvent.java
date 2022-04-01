package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class PetLevelUpEvent extends Event {

    private final Player leveler;
    private final int level;

    public PetLevelUpEvent(Player leveler, int level) {
        this.leveler = leveler;
        this.level = level;
    }

    public int getPetLevelUp() {
        return level;
    }

    public Player getLeveler() {
        return leveler;
    }
}
