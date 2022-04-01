package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PurpleCandyUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static long getHolidayPurple(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM holidayItems WHERE userId=?;", uid)) {
            if (rs.next()) {
                int holidayItem = rs.getInt("purpleCandy");
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

    public static void setHolidayPurple(String uid, int purpleCandy) {
        if (purpleCandy < 0) purpleCandy = 0;
        cache.put(uid, purpleCandy);
        Withering.getBot().getDatabase().update("UPDATE holidayItems SET purpleCandy=? WHERE userId=?;", purpleCandy, uid);
    }

    public static void addHolidayPurple(String uid, int purpleCandy) {
        int oldPurple = (int) getHolidayPurple(uid);
        setHolidayPurple(uid, oldPurple + purpleCandy);
    }
}
