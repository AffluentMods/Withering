package me.affluent.decay.skill;

import java.util.HashMap;

public class StealthSkill extends Skill<Double> {

    public StealthSkill() {
        super(3, "Stealth");
    }

    private static final HashMap<Integer, Double> values = new HashMap<>();

    @Override
    public Double getDefaultValue() {
        return 0.0;
    }

    @Override
    public HashMap<Integer, Double> getValues() {
        if (values.size() <= 0) {
            values.put(1, 1.0);
            values.put(2, 1.2);
            values.put(3, 1.4);
            values.put(4, 1.6);
            values.put(5, 1.7);
            values.put(6, 1.9);
            values.put(7, 2.1);
            values.put(8, 2.3);
            values.put(9, 2.5);
            values.put(10, 2.7);
            values.put(11, 2.8);
            values.put(12, 3.0);
            values.put(13, 3.2);
            values.put(14, 3.4);
            values.put(15, 3.6);
            values.put(16, 3.8);
            values.put(17, 3.9);
            values.put(18, 4.1);
            values.put(19, 4.3);
            values.put(20, 4.5);
            values.put(21, 4.7);
            values.put(22, 4.9);
            values.put(23, 5.0);
            values.put(24, 5.2);
            values.put(25, 5.4);
            values.put(26, 5.6);
            values.put(27, 5.8);
            values.put(28, 6.0);
            values.put(29, 6.1);
            values.put(30, 6.3);
            values.put(31, 6.5);
            values.put(32, 6.7);
            values.put(33, 6.9);
            values.put(34, 7.1);
            values.put(35, 7.2);
            values.put(36, 7.4);
            values.put(37, 7.6);
            values.put(38, 7.8);
            values.put(39, 8.0);
            values.put(40, 8.2);
            values.put(41, 8.3);
            values.put(42, 8.5);
            values.put(43, 8.7);
            values.put(44, 8.9);
            values.put(45, 9.1);
            values.put(46, 9.3);
            values.put(47, 9.4);
            values.put(48, 9.6);
            values.put(49, 9.8);
            values.put(50, 10.0);
        }
        return values;
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