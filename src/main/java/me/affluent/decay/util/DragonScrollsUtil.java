package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DragonScrollsUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static int getDragonScrolls(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT amount FROM dragonScrolls WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setDragonScrolls(String userId, int scrolls) {
        cache.put(userId, scrolls);
        Withering.getBot().getDatabase().update("DELETE FROM dragonScrolls WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO dragonScrolls VALUES (?, ?);", userId, scrolls);
    }

    public static void addDragonScrolls(String userId, int scrolls) {
        int oldScrolls = getDragonScrolls(userId);
        setDragonScrolls(userId, oldScrolls + scrolls);
    }
}
