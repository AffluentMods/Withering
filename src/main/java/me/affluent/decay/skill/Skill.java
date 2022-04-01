package me.affluent.decay.skill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Skill<V> {

    private final int ID;
    private final String name;

    private static final HashMap<Integer, Skill<?>> skills = new HashMap<>();

    public Skill(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public abstract int getElixirCost(int toLevel);

    public abstract int getTotalElixirCost(int currentLevel, int toLevel);

    public abstract HashMap<Integer, V> getValues();

    public abstract V getDefaultValue();

    public V getValue(int level) {
        return getValues().getOrDefault(level, getDefaultValue());
    }

    public int getMax() {
        return getValues().size();
    }

    public static Skill getSkill(int ID) {
        return skills.get(ID);
    }

    public void onBuy(String userId) {
    }

    public static void load() {
        List<Skill<?>> skillList =
                Arrays.asList(new JuggernautSkill(), new BerserkerSkill(), new StealthSkill(), new AssassinSkill(), new DwarfSkill());
        for (Skill<?> skill : skillList) {
            if (skill.getValues().size() <= 0 || skill.getValues().isEmpty()) {
                System.out.println(
                        "[FATAL ERROR] !!! Skill ID " + skill.ID + " [name=" + skill.name + "] has no values !!!");
                return;
            }
            if (skills.containsKey(skill.ID)) {
                System.out.println("[FATAL ERROR] !!! Skill [name=" + skill.name + "] with ID " + skill.ID +
                                   " has ID reserved already !!!");
                return;
            }
            skills.put(skill.ID, skill);
        }
    }

    public static HashMap<Integer, Skill<?>> getSkills() {
        return skills;
    }
}