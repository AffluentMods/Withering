package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.enums.Rarity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class IngotUtil {

    private static final HashMap<String, Long> cache = new HashMap<>();

    public static long getIngots(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM ingots WHERE userId=?;", uid)) {
            if (rs.next()) {
                long ingots = rs.getLong("ingots");
                cache.put(uid, ingots);
                return ingots;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setIngots(String uid, long ingots) {
        if (ingots < 0) ingots = 0;
        cache.put(uid, ingots);
        Withering.getBot().getDatabase().update("DELETE FROM ingots WHERE userId=?;", uid);
        Withering.getBot().getDatabase().update("INSERT INTO ingots VALUES (?, ?);", uid, ingots);
    }

    public static void addIngots(String uid, long ingots) {
        long oldIngots = getIngots(uid);
        setIngots(uid, oldIngots + ingots);
    }

    public static int getIngotCost(Rarity rarity) {
        switch (rarity) {
            case JUNK:
                return 2;
            case COMMON:
                return 5;
            case UNCOMMON:
                return 9;
            case RARE:
                return 15;
            case EPIC:
                return 22;
            case LEGEND:
                return 33;
            case MYTHIC:
                return 50;
            default:
                return 0;
        }
    }
}