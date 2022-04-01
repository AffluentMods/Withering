package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class FightEndEvent extends Event {

    private final Player attacker;
    private final Player defender;
    private final Player winner;
    private final boolean practice;
    private final boolean random;

    public FightEndEvent(Player attacker, Player defender, Player winner, boolean practice, boolean random) {
        this.attacker = attacker;
        this.defender = defender;
        this.winner = winner;
        this.practice = practice;
        this.random = random;
    }

    public boolean isPractice() {
        return practice;
    }

    public boolean isRandom() {
        return random;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getDefender() {
        return defender;
    }

    public Player getWinner() {
        return winner;
    }
}