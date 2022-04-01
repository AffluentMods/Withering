package me.affluent.decay.pvp;

public enum AttackRate {
    SLOW(11),
    DECENT(10),
    QUICK(1);

    private final int chanceToMiss;

    AttackRate(int chanceToMiss) {
        this.chanceToMiss = chanceToMiss;
    }

    public int getChanceToMiss() {
        return chanceToMiss;
    }
}