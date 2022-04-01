package me.affluent.decay.util.system;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class DailyRewardSystem {

    private static HashMap<String, DailyReward> cache = new HashMap<>();
    private static final HashMap<String, Integer> daHourlyChecked = new HashMap<>();
    private static final HashMap<String, Boolean> daHourlyCheckedResult = new HashMap<>();
    
    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS dailyRewards (userId VARCHAR(24) NOT NULL, streak INT NOT NULL, " +
                        "lastTime VARCHAR(24) NOT NULL, lastYear INT NOT NULL);");
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM dailyRewards;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                int streak = rs.getInt("streak");
                long lastTime = Long.parseLong(rs.getString("lastTime"));
                int lastYear = rs.getInt("lastYear");
                cache.put(userId, new DailyReward(streak, lastTime, lastYear));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static HashMap<String, Long> getRewards(Player p, int streak) {
        HashMap<String, Long> rewards = new HashMap<>();
        BigInteger tenPercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf(10));
        switch (streak) {
            case 1:
                rewards.put("money", Math.min(100 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 1L);
                rewards.put("diamond", 20L);
                rewards.put("alloy ingot", 10L);
                break;
            case 2:
                rewards.put("money", Math.min(150 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 1L);
                rewards.put("diamond", 30L);
                rewards.put("alloy ingot", 10L);
                break;
            case 3:
                rewards.put("money", Math.min(225 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 2L);
                rewards.put("diamond", 45L);
                rewards.put("alloy ingot", 20L);
                break;
            case 4:
                rewards.put("money", Math.min(315 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 2L);
                rewards.put("diamond", 65L);
                rewards.put("alloy ingot", 20L);
                break;
            case 5:
                rewards.put("money", Math.min(425 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 2L);
                rewards.put("diamond", 90L);
                rewards.put("alloy ingot", 20L);
                break;
            case 6:
                rewards.put("money",Math.min( 575 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 3L);
                rewards.put("diamond", 120L);
                rewards.put("alloy ingot", 30L);
                break;
            case 7:
                rewards.put("money",Math.min( 750 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 3L);
                rewards.put("diamond", 150L);
                rewards.put("alloy ingot", 30L);
                break;
            case 8:
                rewards.put("money", Math.min(950 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 3L);
                rewards.put("metal key", 1L);
                rewards.put("diamond", 185L);
                rewards.put("alloy ingot", 30L);
                break;
            case 9:
                rewards.put("money", Math.min(1200 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 4L);
                rewards.put("metal key", 1L);
                rewards.put("diamond", 225L);
                rewards.put("alloy ingot", 40L);
                break;
            case 10:
                rewards.put("money", Math.min(1500 + tenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 4L);
                rewards.put("metal key", 1L);
                rewards.put("diamond", 275L);
                rewards.put("alloy ingot", 50L);
                break;
        }
        return rewards;
    }

    public static int getStreak(String userId) {
        if (!cache.containsKey(userId)) {
            try (ResultSet rs = Withering.getBot().getDatabase()
                    .query("SELECT * FROM dailyRewards WHERE userId=?;", userId)) {
                if (rs.next()) {
                    DailyReward dailyReward = new DailyReward(
                            rs.getInt("streak"), Long.parseLong(rs.getString("lastTime")), rs.getInt("lastYear"));
                    cache.put(userId, dailyReward);
                    return dailyReward.getStreak();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else return cache.get(userId).getStreak();
        return 0;
    }

    public static int getThisYear() {
        return getCalendar(System.currentTimeMillis()).get(Calendar.YEAR);
    }

    public static int getThisDay() {
        return getCalendar(System.currentTimeMillis()).get(Calendar.DAY_OF_YEAR);
    }

    public static int getLastDay(String userId) {
        long lastTime = getLastTime(userId);
        if (lastTime < 0L) return -1;
        return getCalendar(lastTime).get(Calendar.DAY_OF_YEAR);
    }

    public static long getLastTime(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId).getLastTime();
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT lastTime FROM dailyRewards WHERE userId=?;", userId)) {
            if (rs.next()) return Long.parseLong(rs.getString("lastTime"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static void setDailyReward(String userId, int streak, long time, int year) {
        DailyReward newDailyReward = new DailyReward(streak, time, year);
        cache.put(userId, newDailyReward);
        Withering.getBot().getDatabase().update("DELETE FROM dailyRewards WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO dailyRewards VALUES (?, ?, ?, ?);", userId, streak, String.valueOf(time), year);
        daHourlyCheckedResult.remove(userId);
        daHourlyChecked.remove(userId);
    }

    public static boolean isDailyAvailable(String userId) {
        Calendar c = getCalendar(System.currentTimeMillis());
        int hourNow = c.get(Calendar.HOUR_OF_DAY);
        int lastHourChecked = daHourlyChecked.getOrDefault(userId, -1);
        if (hourNow == lastHourChecked) {
            if (daHourlyCheckedResult.containsKey(userId)) return daHourlyCheckedResult.get(userId);
        }
        int lastDay = getLastDay(userId);
        int lastYear = getLastYear(userId);
        if (lastDay == -1 || lastYear == -1) {
            daHourlyChecked.put(userId, hourNow);
            daHourlyCheckedResult.put(userId, true);
            return true;
        }
        int today = getThisDay();
        int thisYear = c.get(Calendar.YEAR);
        if (thisYear == lastYear) {
            boolean result = today > lastDay;
            daHourlyChecked.put(userId, hourNow);
            daHourlyCheckedResult.put(userId, result);
            return result;
        } else {
            daHourlyChecked.put(userId, hourNow);
            daHourlyCheckedResult.put(userId, true);
            return true;
        }
    }

    private static Calendar getCalendar(long millis) {
        Calendar c = getCalendar();
        c.setTimeInMillis(millis);
        return c;
    }

    private static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
    }

    public static int getLastYear(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId).getLastYear();
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT lastYear FROM dailyRewards WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("lastYear");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    static class DailyReward {

        private final int streak;
        private final long lastTime;
        private final int lastYear;

        public DailyReward(int streak, long lastTime, int lastYear) {
            this.streak = streak;
            this.lastTime = lastTime;
            this.lastYear = lastYear;
        }


        public int getStreak() {
            return streak;
        }

        public long getLastTime() {
            return lastTime;
        }

        public int getLastYear() {
            return lastYear;
        }
    }
}