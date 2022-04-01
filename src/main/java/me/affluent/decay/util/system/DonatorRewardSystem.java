package me.affluent.decay.util.system;

import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class DonatorRewardSystem {

    private static HashMap<String, DonatorReward> cache = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS donatorRewards (userId VARCHAR(24) NOT NULL, lastDay INT NOT NULL);");
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM dailyRewards;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                int lastDay = rs.getInt("lastDay");
                cache.put(userId, new DonatorReward(lastDay));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static HashMap<String, Long> getRewards(Player p, int donator) {
        HashMap<String, Long> rewards = new HashMap<>();
        BigInteger twelvePercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf((long) 8.3333));
        BigInteger fourteenPercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf((long) 7.14285714));
        BigInteger seventeenPercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf((long) 5.88235294));
        BigInteger seventeenTwoPercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf((long) 5.71428571));
        BigInteger twentyPercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf(5));
        BigInteger twentyTwoPercentBalance = p.getEcoUser().getBalance().divide(BigInteger.valueOf((long) 4.65116279));
        switch (donator) {
            case 1:
                rewards.put("money", Math.min(100 + twelvePercentBalance.longValue(), 350000));
                rewards.put("wood key", 1L);
                rewards.put("diamond", 4L);
                break;
            case 2:
                rewards.put("money", Math.min(150 + twelvePercentBalance.longValue(), 350000));
                rewards.put("wood key", 1L);
                rewards.put("diamond", 6L);
                break;
            case 3:
                rewards.put("money", Math.min(225 + fourteenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 2L);
                rewards.put("scroll", 1L);
                rewards.put("diamond", 108L);
                break;
            case 4:
                rewards.put("money", Math.min(315 + seventeenPercentBalance.longValue(), 350000));
                rewards.put("wood key", 4L);
                rewards.put("scroll", 2L);
                rewards.put("diamond", 226L);
                break;
            case 5:
                rewards.put("money", Math.min(425 + seventeenTwoPercentBalance.longValue(), 350000));
                rewards.put("wood key", 5L);
                rewards.put("scroll", 3L);
                rewards.put("diamond", 518L);
                break;
            case 6:
                rewards.put("money",Math.min( 575 + twentyPercentBalance.longValue(), 350000));
                rewards.put("wood key", 9L);
                rewards.put("scroll", 3L);
                rewards.put("dragon steel key", 1L);
                rewards.put("diamond", 1020L);
                break;
            case 7:
                rewards.put("money",Math.min( 750 + twentyTwoPercentBalance.longValue(), 350000));
                rewards.put("wood key", 11L);
                rewards.put("metal key", 3L);
                rewards.put("titan alloy key", 1L);
                rewards.put("scroll", 5L);
                rewards.put("diamond", 1973L);
                break;
        }
        return rewards;
    }

    public static int getThisDay() {
        return Calendar.getInstance(timeZone).get(Calendar.DAY_OF_YEAR);
    }

    public static int getLastDay(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId).getLastDay();
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT lastDay FROM donatorRewards WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("lastDay");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static void setDonatorReward(String userId, int lastDay) {
        DonatorReward newDonatorReward = new DonatorReward(lastDay);
        cache.put(userId, newDonatorReward);
        Withering.getBot().getDatabase().update("DELETE FROM donatorRewards WHERE userId=?;", userId);
        Withering.getBot().getDatabase().update("INSERT INTO donatorRewards VALUES (?, ?);", userId, lastDay);
    }

    private static final TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Berlin"));

    public static boolean isDonatorAvailable(String uid) {
        int lastDay = getLastDay(uid);
        int thisDay = getThisDay();
        return thisDay > lastDay;
    }

    static class DonatorReward {

        private final int lastDay;

        public DonatorReward(int lastDay) {
            this.lastDay = lastDay;
        }

        public int getLastDay() {
            return lastDay;
        }
    }
}