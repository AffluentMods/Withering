package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PetStarringUtil {

    private static final HashMap<String, HashMap<String, Integer>> cache = new HashMap<>();

    public static int getPetStar(String userId, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        if (userCache.containsKey(String.valueOf(ID))) return userCache.get(String.valueOf(ID));
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT petStars FROM petStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) {
                int petStars = rs.getInt("petStars");
                userCache.put(String.valueOf(ID), petStars);
                cache.put(userId, userCache);
                return petStars;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setPetStar(String userId, int star, long ID) {
        HashMap<String, Integer> userCache = cache.getOrDefault(userId, new HashMap<>());
        userCache.put(String.valueOf(ID), star); // update user cache
        cache.put(userId, userCache); // update big cache too
        Withering.getBot().getDatabase().update("UPDATE petStats SET petStars=? WHERE userId=? AND ID=?;", star, userId, ID);
    }

    public static void addPetStar(String userId, int addedStars, long ID) {
        int oldStars = getPetStar(userId, ID);
        setPetStar(userId, oldStars + addedStars, ID);
    }
}
