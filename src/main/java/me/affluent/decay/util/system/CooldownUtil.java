package me.affluent.decay.util.system;

import me.affluent.decay.Withering;
import me.affluent.decay.language.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CooldownUtil {

    private static final HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<>();

    public static void addCooldown(String uid, String name, long until, boolean sql) {
        HashMap<String, Long> userCooldowns = cooldowns.getOrDefault(uid, new HashMap<>());
        userCooldowns.put(name.toLowerCase(), until);
        cooldowns.put(uid, userCooldowns);
        if (sql) Withering.getBot().getDatabase()
                .update("INSERT INTO cooldowns VALUES (?, ?, ?);", uid, name.toLowerCase(), String.valueOf(until));
    }

    public static void removeCooldown(String uid, String name) {
        HashMap<String, Long> userCooldowns = cooldowns.getOrDefault(uid, new HashMap<>());
        userCooldowns.remove(name.toLowerCase());
        cooldowns.put(uid, userCooldowns);
        Withering.getBot().getDatabase()
                .update("DELETE FROM cooldowns WHERE userId=? AND cooldownName=?;", uid, name.toLowerCase());
    }

    public static void removeAllCooldowns(String name) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM cooldowns WHERE cooldownName=?;", name.toLowerCase())) {
            while (rs.next()) removeCooldown(rs.getString("userId"), rs.getString("cooldownName"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean hasCooldown(String uid, String name) {
        return cooldowns.getOrDefault(uid, new HashMap<>()).getOrDefault(name, -1L) > System.currentTimeMillis();
    }

    public static long getCooldown(String uid, String name) {
        HashMap<String, Long> userCooldowns = cooldowns.getOrDefault(uid, new HashMap<>());
        return userCooldowns.getOrDefault(name, -1L);
    }

    public static String format(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = 0;
        long hours = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        String hoursString = String.valueOf(hours);
        String minutesString = String.valueOf(minutes);
        String secondsString = String.valueOf(seconds);
        if (hoursString.length() == 1) hoursString = "0" + hoursString;
        if (minutesString.length() == 1) minutesString = "0" + minutesString;
        if (secondsString.length() == 1) secondsString = "0" + secondsString;
        return hoursString + ":" + minutesString + ":" + secondsString;
    }

    public static String format(long milliseconds, String uid) {
        long seconds = milliseconds / 1000;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        while (hours >= 24) {
            hours -= 24;
            days++;
        }
        String lDays = Language.getLocalized(uid, "days", "days");
        String lHours = Language.getLocalized(uid, "hours", "hours");
        String lMinutes = Language.getLocalized(uid, "minutes", "minutes");
        String lSeconds = Language.getLocalized(uid, "seconds", "seconds");
        String format = "";
        if (days > 0) format += days + " " + lDays + ", ";
        if (hours > 0) format += hours + " " + lHours + ", ";
        if (minutes > 0) format += minutes + " " + lMinutes + ", ";
        format += seconds + " " + lSeconds;
        return format;
    }

    public static void loadCooldowns() {
        long now = System.currentTimeMillis();
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM cooldowns;")) {
            while (rs.next()) {
                String uid = rs.getString("userId");
                String cdName = rs.getString("cooldownName");
                String cdEnd = rs.getString("cooldownEnd");
                long cdEndLong = Long.parseLong(cdEnd);
                if (cdEndLong <= now) {
                    Withering.getBot().getDatabase()
                            .update("DELETE FROM cooldowns WHERE userId=? AND cooldownName=? AND cooldownEnd=?;", uid,
                                    cdName, cdEnd);
                    continue;
                }
                addCooldown(uid, cdName, cdEndLong, false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}