package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CandyCornUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static long getHolidayCorn(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM holidayItems WHERE userId=?;", uid)) {
            if (rs.next()) {
                int holidayItem = rs.getInt("candyCorn");
                cache.put(uid, holidayItem);
                return holidayItem;
            } else {
                Withering.getBot().getDatabase().update("INSERT INTO holidayItems VALUES (?, ?, ?, ?, ?, ?);", uid, 0, 0, 0, 0,  0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setHolidayCorn(String uid, int candyCorn) {
        if (candyCorn < 0) candyCorn = 0;
        cache.put(uid, candyCorn);
        Withering.getBot().getDatabase().update("UPDATE holidayItems SET candyCorn=? WHERE userId=?;", candyCorn, uid);
    }

    public static void addHolidayCorn(String uid, int candyCorn) {
        int oldCorn = (int) getHolidayCorn(uid);
        setHolidayCorn(uid, oldCorn + candyCorn);
    }
}
