package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ConfirmUtil {

    private static HashMap<String, ConfirmUtil> cache = new HashMap<>();

    private final String userId;
    private String confirm;

    public static void clearCache() {
        cache.clear();
    }

    private ConfirmUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT confirmValue FROM confirm WHERE userId=?;", userId)) {
            if (rs.next()) {
                confirm = rs.getString("confirmValue");
            } else {
                confirm = "enabled";
                Withering.getBot().getDatabase().update("INSERT INTO confirm VALUES (?, ?);", userId, confirm);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getConfirmSetting() {
        return confirm;
    }

    public void setConfirmSetting(String confirm) {
        this.confirm = confirm;
        cache();
        Withering.getBot().getDatabase().update("UPDATE confirm SET confirmValue=? WHERE userId=?;", confirm, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ConfirmUtil getConfirmUtil(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ConfirmUtil(userId);
    }
}
