package me.affluent.decay.util.itemUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ItemStarringUtil {

    // We could be using HashMap<String, HashMap<Integer, Integer>> since its userId -> itemId=level
    // but HashMap.get(int) means get the index, not the value stored
    // .get(0) means: get the first item in the hashmap
    // .get(1353) where 1353 is an itemid would mean get the 1354th item in the hashmap, instead of the value 1354
    // so we use string instead
    private static final HashMap<String, HashMap<String, Integer>> cache = new HashMap<>();

    public static int getItemStar(String userId, long ID) {
        // we use getOrDefault, which means, userCache will be the HashMap inside the cache-HashMap for userId
        // but if it doesnt exist, we will use a new HashMap<>()
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        // --> use ID as String
        if (userCache.containsKey(String.valueOf(ID))) return userCache.get(String.valueOf(ID));
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT itemStars FROM itemStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) {
                int itemStars = rs.getInt("itemStars");
                userCache.put(String.valueOf(ID), itemStars); // update user cache inside this method
                cache.put(userId, userCache); // dont forget to update the global/big cache in this class too
                return itemStars;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setItemStar(String userId, int star, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        userCache.put(String.valueOf(ID), star); // update user cache
        cache.put(userId, userCache); // update big cache too
        Withering.getBot().getDatabase().update("UPDATE itemStats SET itemStars=? WHERE userId=? AND ID=?;", star, userId, ID);
    }

    public static void addItemStar(String userId, int addedStars, long ID) {
        int oldStars = getItemStar(userId, ID);
        setItemStar(userId, oldStars + addedStars, ID);
    }
}