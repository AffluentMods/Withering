package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class IgnoreAchievementUtil {

    private static HashMap<String, IgnoreAchievementUtil> cache = new HashMap<>();

    private final String userId;
    private String ignoreAchievement;

    public static void clearCache() {
        cache.clear();
    }

    private IgnoreAchievementUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT ignoreValue FROM ignoreAchievement WHERE userId=?;", userId)) {
            if (rs.next()) {
                ignoreAchievement = rs.getString("ignoreValue");
            } else {
                ignoreAchievement = "enabled";
                Withering.getBot().getDatabase().update("INSERT INTO ignoreAchievement VALUES (?, ?);", userId, ignoreAchievement);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getIgnoreAchievementSetting() {
        return ignoreAchievement;
    }

    public void setIgnoreAchievementSetting(String ignoreAchievement) {
        this.ignoreAchievement = ignoreAchievement;
        cache();
        Withering.getBot().getDatabase().update("UPDATE ignoreAchievement SET ignoreValue=? WHERE userId=?;", ignoreAchievement, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static IgnoreAchievementUtil getIgnoreAchievementUtil(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new IgnoreAchievementUtil(userId);
    }
}
