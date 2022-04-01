package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class KeysUtil {

    public static HashMap<String, Integer> getAllKeys(String uid) {
        HashMap<String, Integer> allKeys = new HashMap<>();
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM userkeys WHERE userId=?;", uid)) {
            while (rs.next()) allKeys.put(rs.getString("keytype").toLowerCase(), rs.getInt("userkeys"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return allKeys;
    }

    public static long getKeys(String uid, String keytype) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM userkeys WHERE keytype=? AND userId=?;", keytype, uid)) {
            if (rs.next()) return rs.getInt("userkeys");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setKeys(String uid, String keytype, long userkeys) {
        Withering.getBot().getDatabase()
                .update("DELETE FROM userkeys WHERE keytype='" + keytype + "' AND userId=?;", uid);
        if (userkeys < 0) userkeys = 0;
        if (userkeys > 0)
            Withering.getBot().getDatabase().update("INSERT INTO userkeys VALUES (?, ?, ?);", uid, keytype, userkeys);
    }

    public static void addKeys(String uid, String keytype, long userkeys) {
        setKeys(uid, keytype, getKeys(uid, keytype) + userkeys);
    }

}