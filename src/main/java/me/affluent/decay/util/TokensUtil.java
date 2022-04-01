package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TokensUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS tokens (userId VARCHAR(24) NOT NULL, tokens INT NOT NULL);");
    }

    public static int getTokens(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT tokens FROM tokens WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("tokens");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setTokens(String userId, int tokens) {
        cache.put(userId, tokens);
        Withering.getBot().getDatabase().update("DELETE FROM tokens WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO tokens VALUES (?, ?);", userId, tokens);
    }

    public static void addTokens(String userId, int tokens) {
        int oldTokens = getTokens(userId);
        setTokens(userId, oldTokens + tokens);
    }
}