package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class WitherScrollsUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static int getWitherScrolls(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT amount FROM witherScrolls WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setWitherScrolls(String userId, int scrolls) {
        cache.put(userId, scrolls);
        Withering.getBot().getDatabase().update("DELETE FROM witherScrolls WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO witherScrolls VALUES (?, ?);", userId, scrolls);
    }

    public static void addWitherScrolls(String userId, int scrolls) {
        int oldScrolls = getWitherScrolls(userId);
        setWitherScrolls(userId, oldScrolls + scrolls);
    }
}
