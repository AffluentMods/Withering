package me.affluent.decay.util.itemUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ItemLockingUtil {

    // See ItemStarringUtil.java for full explanation

    private static final HashMap<String, HashMap<String, Integer>> cache = new HashMap<>();

    public static int getItemLockValue(String userId, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        if (userCache.containsKey(String.valueOf(ID))) return userCache.get(String.valueOf(ID));
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT lockValue FROM itemStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) {
                int lockValue = rs.getInt("lockValue");
                userCache.put(String.valueOf(ID), lockValue);
                cache.put(userId, userCache);
                return lockValue;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setItemLockValue(String userId, int lockValue, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        userCache.put(String.valueOf(ID), lockValue);
        cache.put(userId, userCache);
        Withering.getBot().getDatabase()
                .update("UPDATE itemStats SET lockValue=? WHERE userId=? AND ID=?;", lockValue, userId, ID);
    }
}
