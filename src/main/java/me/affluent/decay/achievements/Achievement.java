package me.affluent.decay.achievements;

import me.affluent.decay.manager.EventManager;
import me.affluent.decay.listener.EventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Achievement implements EventListener {

    private final String name;
    private final HashMap<Integer, AchievementData> achievementDataMap;
    private static final HashMap<String, Achievement> allAchievements = new HashMap<>();

    public Achievement(final String name, final List<AchievementData> achievementDatas) {
        this.name = name;
        HashMap<Integer, AchievementData> achievementDataMap = new HashMap<>();
        int dataTier = 0;
        for (final AchievementData achievementData : achievementDatas) {
            dataTier++;
            achievementDataMap.put(dataTier, achievementData);
        }
        this.achievementDataMap = achievementDataMap;
    }

    //Amount obtained on achievement tier 1
    abstract long getSD();

    //Amount increase on every tier after
    abstract long getID();

    public long getReward(int times) {
        if (times < 2) return getSD();
        return getSD() + (getID() * (times - 1));
    }

    public static Achievement getAchievementByName(String achievementName) {
        return allAchievements.get(achievementName.toLowerCase());
    }

    public static void loadAchievements() {
        List<Achievement> achievements =
                Arrays.asList(new DiamondHandsAchievement(), new BrawlerAchievement(), new ExperienceAchievement(),
                        new TrialByCombatAchievement(), new GearSummonerAchievement(), new BlacksmithAchievement(),
                new PetExpertiseAchievement(), new ExpertTrainerAchievement());
        for (Achievement achievement : achievements) {
            EventManager.registerListener(achievement);
            allAchievements.put(achievement.getName().toLowerCase(), achievement);
        }
    }

    public String getName() {
        return name;
    }

    public static HashMap<String, Achievement> getAllAchievements() {
        return allAchievements;
    }

    public HashMap<Integer, AchievementData> getAchievementDataMap() {
        return achievementDataMap;
    }

    public AchievementData getAchievementData(int index) {
        return achievementDataMap.get(index);
    }
}
