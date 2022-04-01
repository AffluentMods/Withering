package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class OrbUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static int getOrbs(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT amount FROM orbs WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setOrbs(String userId, int orbs) {
        cache.put(userId, orbs);
        Withering.getBot().getDatabase().update("DELETE FROM orbs WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO orbs VALUES (?, ?);", userId, orbs);
    }

    public static void addOrbs(String userId, int orbs) {
        int oldOrbs = getOrbs(userId);
        setOrbs(userId, oldOrbs + orbs);
    }
}