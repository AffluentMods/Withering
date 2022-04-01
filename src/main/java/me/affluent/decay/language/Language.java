package me.affluent.decay.language;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Language {

    public static String getLocalized(String userId, String msgID) {
        return getLocalized(userId, msgID, "Error 404 " + msgID + " not found");
    }

    public static String getLocalized(String userId, String msgID, String defaultMessage) {
        String langCode = getLangCode(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT message FROM lang_messages WHERE msgID=? AND langCode=?;", msgID, langCode)) {
            if (rs.next()) {
                return rs.getString("message");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error";
        }
        return defaultMessage;
    }

    public static String getLangCode(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT language FROM profiles WHERE userId=?", userId)) {
            if (rs.next()) return rs.getString("language");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "en";
    }
}