package me.affluent.decay.skill;

import me.affluent.decay.listener.EventListener;

import java.util.HashMap;

public class AssassinSkill extends Skill<Double> implements EventListener {

    public AssassinSkill() {
        super(4, "Assassin");
    }

    private static final HashMap<Integer, Double> values = new HashMap<>();

    @Override
    public Double getDefaultValue() {
        return 0.0;
    }

    @Override
    public HashMap<Integer, Double> getValues() {
        if (values.size() <= 0) {
            for (int i = 0; i < 50; i++) values.put(i + 1, 1.5 + (i * 0.5));
        }
        return values;
    }

    @Override
    public int getElixirCost(int toLevel) {
        if (toLevel == 1) return 1719;
        if (toLevel <= 5) return 3750;
        if (toLevel <= 10) return 6250;
        if (toLevel <= 15) return 9000;
        if (toLevel <= 20) return 12500;
        return -1;
    }

    @Override
    public int getTotalElixirCost(int currentLevel, int toLevel) {
        int totalCost = 0;
        for (int i = currentLevel + 1; i <= toLevel; i++) {
            if (i == 1) {
                totalCost += 1719;
            } else if (i <= 5) {
                totalCost += 3750;
            } else if (i <= 10) {
                totalCost += 6250;
            } else if (i <= 15) {
                totalCost += 9000;
            } else if (i <= 20) {
                totalCost += 12500;
            }
        }
        return totalCost;
    }
}