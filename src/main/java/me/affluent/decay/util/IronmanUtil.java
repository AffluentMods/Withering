package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class IronmanUtil {

    private static HashMap<String, IronmanUtil> cache = new HashMap<>();

    private final String userId;
    private String ironman;

    public static void clearCache() {
        cache.clear();
    }

    private IronmanUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT value FROM ironman WHERE userId=?;", userId)) {
            if (rs.next()) {
                ironman = rs.getString("value");
            } else {
                ironman = "disabled";
                Withering.getBot().getDatabase().update("INSERT INTO ironman VALUES (?, ?);", userId, ironman);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getIronmanMode() {
        return ironman;
    }

    public void setIronmanMode(String ironman) {
        this.ironman = ironman;
        cache();
                Withering.getBot().getDatabase().update("UPDATE ironman SET value=? WHERE userId=?;", ironman, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static IronmanUtil getIronmanMode(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new IronmanUtil(userId);
    }
}