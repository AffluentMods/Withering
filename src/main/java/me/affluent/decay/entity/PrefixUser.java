package me.affluent.decay.entity;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PrefixUser {

    private static HashMap<String, PrefixUser> cache = new HashMap<>();

    private final String userId;
    private String prefix;

    public static void clearCache() {
        cache.clear();
    }

    private PrefixUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT prefix FROM userprefix WHERE userId=?;", userId)) {
            if (rs.next()) {
                prefix = rs.getString("prefix");
            } else {
                Withering.getBot().getDatabase().update("INSERT INTO userprefix VALUES (?, ?);", userId, "w.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        cache();
        Withering.getBot().getDatabase().update("UPDATE userprefix SET prefix=? WHERE userId=?;", prefix, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static PrefixUser getPrefixUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new PrefixUser(userId);
    }
}