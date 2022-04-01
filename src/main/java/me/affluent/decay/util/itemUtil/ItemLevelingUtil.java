package me.affluent.decay.util.itemUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ItemLevelingUtil {

    // See ItemStarringUtil.java for full explanation

    private static final HashMap<String, HashMap<String, Integer>> cache = new HashMap<>();

    public static int getItemLevel(String userId, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        if (userCache.containsKey(String.valueOf(ID))) return userCache.get(String.valueOf(ID));
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT itemLevel FROM itemStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) {
                int itemLevel = rs.getInt("itemLevel");
                userCache.put(String.valueOf(ID), itemLevel);
                cache.put(userId, userCache);
                return itemLevel;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setItemLevel(String userId, int level, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        userCache.put(String.valueOf(ID), level);
        cache.put(userId, userCache);
        Withering.getBot().getDatabase()
                .update("UPDATE itemStats SET itemLevel=? WHERE userId=? AND ID=?;", level, userId, ID);
    }

    public static void addItemLevel(String userId, int addedLevel, long ID) {
        int oldLevel = getItemLevel(userId, ID);
        setItemLevel(userId, oldLevel + addedLevel, ID);
    }
}