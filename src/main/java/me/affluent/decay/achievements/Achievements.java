package me.affluent.decay.achievements;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Achievements {
    public static HashMap<Achievement, Integer> getAchievedAchievements(String userId) {
        HashMap<Achievement, Integer> achievements = new HashMap<>();
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM achievements WHERE userId=?;", userId)) {
            while (rs.next()) {
                Achievement achievement = Achievement.getAchievementByName(rs.getString("achievementName"));
                achievements.put(achievement, rs.getInt("achievementTier"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return achievements;
    }

    public static void addAchievement(String uid, Achievement achievement, int newTier) {
        Withering.getBot().getDatabase().update("DELETE FROM achievements WHERE userId=? AND achievementName=?;", uid,
                achievement.getName().toLowerCase());
        Withering.getBot().getDatabase()
                .update("INSERT INTO achievements VALUES (?, ?, ?);", uid, achievement.getName().toLowerCase(),
                        newTier);
    }
}
