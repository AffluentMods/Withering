package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PresentsUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static long getHolidayPresents(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM holidayItems WHERE userId=?;", uid)) {
            if (rs.next()) {
                int holidayItem = rs.getInt("presents");
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

    public static void setHolidayPresents(String uid, int presents) {
        if (presents < 0) presents = 0;
        cache.put(uid, presents);
        Withering.getBot().getDatabase().update("UPDATE holidayItems SET presents=? WHERE userId=?;", presents, uid);
    }

    public static void addHolidayPresents(String uid, int presents) {
        int oldPresents = (int) getHolidayPresents(uid);
        setHolidayPresents(uid, oldPresents + presents);
    }
}
