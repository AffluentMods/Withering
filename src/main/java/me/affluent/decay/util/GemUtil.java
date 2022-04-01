package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.gems.Gem;
import me.affluent.decay.gems.UserGem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class GemUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static int getGemLevel(String userId, long ID) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT gemLevel FROM itemStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) return rs.getInt("gemLevel");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public String getGemName(String userId, long ID) {
        if (cache.containsKey(userId)) return String.valueOf(cache.get(userId));
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT gemName FROM itemStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) return rs.getString("gemName");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public static int getGemDust(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT amount FROM gemDust WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void clearCache() {
        cache.clear();
    }
}
