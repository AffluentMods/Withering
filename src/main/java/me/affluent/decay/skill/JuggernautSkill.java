package me.affluent.decay.skill;

import me.affluent.decay.entity.HealthUser;

import java.util.HashMap;

public class JuggernautSkill extends Skill<Integer> {

    public JuggernautSkill() {
        super(1, "Juggernaut");
    }

    private static final HashMap<Integer, Integer> values = new HashMap<>();

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

    @Override
    public HashMap<Integer, Integer> getValues() {
        if (values.size() <= 0) {
            for (int i = 0; i < 50; i++) values.put(i + 1, (i + 1) * 6);
        }
        return values;
    }

    @Override
    public void onBuy(String userId) {
        HealthUser.clearCache(userId);
    }

    @Override
    public int getElixirCost(int toLevel) {
        if (toLevel == 1) return 1000;
        if (toLevel <= 10) return 1300;
        if (toLevel <= 20) return 2026;
        if (toLevel <= 30) return 3000;
        if (toLevel <= 40) return 4000;
        if (toLevel <= 50) return 5000;
        return -1;
    }

    @Override
    public int getTotalElixirCost(int currentLevel, int toLevel) {
        int totalCost = 0;
        for (int i = currentLevel + 1; i <= toLevel; i++) {
            if (i == 1) {
                totalCost += 1000;
            } else if (i <= 10) {
                totalCost += 1300;
            } else if (i <= 20) {
                totalCost += 2026;
            } else if (i <= 30) {
                totalCost += 3000;
            } else if (i <= 40) {
                totalCost += 4000;
            } else if (i <= 50) {
                totalCost += 5000;
            }
        }
        return totalCost;
    }
}