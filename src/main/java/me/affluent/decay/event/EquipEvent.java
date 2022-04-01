package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class EquipEvent extends Event {

    private final Player equipper;

    public EquipEvent(Player equipper) {
        this.equipper = equipper;
    }

    public Player getEquipper() {
        return equipper;
    }
}
