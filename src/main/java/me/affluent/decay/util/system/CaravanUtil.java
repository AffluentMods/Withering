package me.affluent.decay.util.system;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.util.HashMap;

public class CaravanUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    /*public static void updateRewards() {
        String userId = "";
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId FROM profiles LEFT JOIN exp ON exp.userId = profiles.userId " +
                "WHERE exp.level >= 10;")) {
            if (rs.next()) userId = rs.getString("userId");
        }
        if (cache.containsKey(uid)) return cache.get(uid);
        //144 times is the max
    }

    public static*/
}
