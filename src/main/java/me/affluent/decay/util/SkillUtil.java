package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SkillUtil {

    private static final HashMap<String, HashMap<Integer, Integer>> cache = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS skills (userId VARCHAR(24) NOT NULL, skillId INT NOT NULL, level " +
                        "INT NOT NULL);");
    }

    public static void setLevel(String userId, int skillID, int level) {
        HashMap<Integer, Integer> uc = cache.getOrDefault(userId, new HashMap<>());
        uc.put(skillID, level);
        cache.put(userId, uc);
        Withering.getBot().getDatabase().update("DELETE FROM skills WHERE userId=? AND skillId=?;", userId, skillID);
        Withering.getBot().getDatabase().update("INSERT INTO skills VALUES (?, ?, ?);", userId, skillID, level);
    }

    public static int getLevel(String userId, int skillID) {
        HashMap<Integer, Integer> uc = cache.getOrDefault(userId, new HashMap<>());
        if (uc.containsKey(skillID)) return uc.get(skillID);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT level FROM skills WHERE userId=? AND skillId=?;", userId, skillID)) {
            if (rs.next()) return rs.getInt("level");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

}