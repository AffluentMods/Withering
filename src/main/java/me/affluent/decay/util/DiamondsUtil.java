package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DiamondsUtil {

    private static final HashMap<String, Long> cache = new HashMap<>();

    public static long getDiamonds(String uid) {
        if (cache.containsKey(uid)) return cache.get(uid);
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM diamonds WHERE userId=?;", uid)) {
            if (rs.next()) {
                long diamonds = rs.getLong("diamonds");
                cache.put(uid, diamonds);
                return diamonds;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setDiamonds(String uid, long diamonds) {
        if (diamonds < 0) diamonds = 0;
        cache.put(uid, diamonds);
        Withering.getBot().getDatabase().update("DELETE FROM diamonds WHERE userId=?;", uid);
        Withering.getBot().getDatabase().update("INSERT INTO diamonds VALUES (?, ?);", uid, diamonds);
    }

    public static void addDiamonds(String uid, long diamonds) {
        long oldDiamonds = getDiamonds(uid);
        setDiamonds(uid, oldDiamonds + diamonds);
    }
}