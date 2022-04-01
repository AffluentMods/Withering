package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ResponseUtil {

    private static HashMap<String, ResponseUtil> cache = new HashMap<>();

    private final String userId;
    private String response;

    public static void clearCache() {
        cache.clear();
    }

    private ResponseUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT responseValue FROM response WHERE userId=?;", userId)) {
            if (rs.next()) {
                response = rs.getString("responseValue");
            } else {
                response = "pc";
                Withering.getBot().getDatabase().update("INSERT INTO response VALUES (?, ?);", userId, response);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        cache();
        Withering.getBot().getDatabase().update("UPDATE response SET responseValue=? WHERE userId=?;", response, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ResponseUtil getResponseUtil(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ResponseUtil(userId);
    }
}
