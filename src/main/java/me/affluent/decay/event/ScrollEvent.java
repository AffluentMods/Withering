package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class ScrollEvent extends Event {

    private final Player scroller;

    public ScrollEvent(Player scroller) {
        this.scroller = scroller;
    }

    public Player getScroller() {
        return scroller;
    }
}
