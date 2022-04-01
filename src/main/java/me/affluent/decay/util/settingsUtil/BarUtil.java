package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BarUtil {

    private static HashMap<String, BarUtil> cache = new HashMap<>();

    private final String userId;
    private String bar;

    public static void clearCache() {
        cache.clear();
    }

    private BarUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT barValue FROM bar WHERE userId=?;", userId)) {
            if (rs.next()) {
                bar = rs.getString("barValue");
            } else {
                bar = "enabled";
                Withering.getBot().getDatabase().update("INSERT INTO bar VALUES (?, ?);", userId, bar);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getBarSetting() {
        return bar;
    }

    public void setBarSetting(String bar) {
        this.bar = bar;
        cache();
        Withering.getBot().getDatabase().update("UPDATE bar SET barValue=? WHERE userId=?;", bar, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static BarUtil getBarUtil(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new BarUtil(userId);
    }
}
