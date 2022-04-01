package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GoldenTokensUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS goldentokens (userId VARCHAR(24) NOT NULL, goldentokens INT NOT NULL);");
    }

    public static int getGoldenTokens(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT goldentokens FROM goldentokens WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("goldentokens");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setGoldenTokens(String userId, int goldentokens) {
        cache.put(userId, goldentokens);
        Withering.getBot().getDatabase().update("DELETE FROM goldentokens WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO goldentokens VALUES (?, ?);", userId, goldentokens);
    }

    public static void addGoldenTokens(String userId, int goldentokens) {
        int oldGoldenTokens = getGoldenTokens(userId);
        setGoldenTokens(userId, oldGoldenTokens + goldentokens);
    }
}