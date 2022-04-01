package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class LevelUpEvent extends Event{

    private final Player leveler;
    private final int level;

    public LevelUpEvent(Player leveler, int level) {
        this.leveler = leveler;
        this.level = level;
    }

    public int getLevelUp() {
        return level;
    }

    public Player getLeveler() {
        return leveler;
    }
}
