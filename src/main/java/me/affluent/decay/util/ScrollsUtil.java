package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ScrollsUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS scrolls (userId VARCHAR(24) NOT NULL, scrolls INT NOT NULL);");
    }

    public static int getScrolls(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT scrolls FROM scrolls WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("scrolls");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setScrolls(String userId, int scrolls) {
        cache.put(userId, scrolls);
        Withering.getBot().getDatabase().update("DELETE FROM scrolls WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO scrolls VALUES (?, ?);", userId, scrolls);
    }

    public static void addScrolls(String userId, int scrolls) {
        int oldScrolls = getScrolls(userId);
        setScrolls(userId, oldScrolls + scrolls);
    }
}