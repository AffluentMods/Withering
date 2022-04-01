package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ElixirUtil {

    private static final HashMap<String, Integer> cache = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS elixir (userId VARCHAR(24) NOT NULL, elixir INT NOT NULL);");
    }

    public static int getElixir(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT elixir FROM elixir WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("elixir");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setElixir(String userId, int elixir) {
        cache.put(userId, elixir);
        Withering.getBot().getDatabase().update("DELETE FROM elixir WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO elixir VALUES (?, ?);", userId, elixir);
    }
}