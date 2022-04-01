package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FireworksUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static long getHolidayFireworks(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM holidayItems WHERE userId=?;", uid)) {
            if (rs.next()) {
                int holidayItem = rs.getInt("fireworks");
                cache.put(uid, holidayItem);
                return holidayItem;
            } else {
                Withering.getBot().getDatabase().update("INSERT INTO holidayItems VALUES (?, ?, ?, ?, ?, ?);", uid, 0, 0, 0, 0, 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setHolidayFireworks(String uid, int fireworks) {
        if (fireworks < 0) fireworks = 0;
        cache.put(uid, fireworks);
        Withering.getBot().getDatabase().update("UPDATE holidayItems SET fireworks=? WHERE userId=?;", fireworks, uid);
    }

    public static void addHolidayFireworks(String uid, int fireworks) {
        int oldFireworks = (int) getHolidayFireworks(uid);
        setHolidayFireworks(uid, oldFireworks + fireworks);
    }
}
