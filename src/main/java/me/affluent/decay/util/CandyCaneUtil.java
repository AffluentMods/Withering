package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CandyCaneUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static long getHolidayCandyCane(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM holidayItems WHERE userId=?;", uid)) {
            if (rs.next()) {
                int holidayItem = rs.getInt("candyCane");
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

    public static void setHolidayCandyCane(String uid, int candyCane) {
        if (candyCane < 0) candyCane = 0;
        cache.put(uid, candyCane);
        Withering.getBot().getDatabase().update("UPDATE holidayItems SET candyCane=? WHERE userId=?;", candyCane, uid);
    }

    public static void addHolidayCandyCane(String uid, int candyCane) {
        int oldCandyCane = (int) getHolidayCandyCane(uid);
        setHolidayCandyCane(uid, oldCandyCane + candyCane);
    }
}
