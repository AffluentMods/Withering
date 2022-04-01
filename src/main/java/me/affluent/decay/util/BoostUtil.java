package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.util.system.CooldownUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoostUtil {

    private static final HashMap<String, List<Boost>> boosts = new HashMap<>();

    private static String getBoostValue(String userId, String boostName) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT boostValue FROM boosts WHERE userId=? AND boostName=?;", userId,
                        boostName.toLowerCase())) {
            if (rs.next()) return rs.getString("boostValue");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<Boost> getAllBoosts(String userId) {
        return boosts.getOrDefault(userId, new ArrayList<>());
    }

    public static Boost getBoost(String userId, String boostName) {
        for (Boost boost : boosts.getOrDefault(userId, new ArrayList<>())) {
            if (boost.getName().toLowerCase().equalsIgnoreCase(boostName.toLowerCase())) return boost;
        }
        return null;
    }

    public static boolean hasBoost(String userId, String boostName) {
        Boost boost = getBoost(userId, boostName);
        if (boost != null) return boost.getEnd() > System.currentTimeMillis();
        return false;
    }

    public static void addBoost(String userId, String boostName, int value, long millis) {
        addBoost(userId, boostName, value, String.valueOf(value), millis);
    }

    static void addBoost(String userId, String boostName, int value, String valueDisplay, long millis) {
        long end = System.currentTimeMillis() + millis;
        Withering.getBot().getDatabase()
                .update("INSERT INTO boosts VALUES (?, ?, ?, ?, ?);", userId, boostName.toLowerCase(),
                        String.valueOf(value), valueDisplay, String.valueOf(end));
        CooldownUtil.addCooldown(userId, boostName.toLowerCase(), end, true);
        List<Boost> userBoosts = boosts.getOrDefault(userId, new ArrayList<>());
        userBoosts.add(new Boost(boostName.toLowerCase(), String.valueOf(value), valueDisplay, end));
        boosts.put(userId, userBoosts);
    }

    public static void removeBoost(String userId, String boostName) {
        List<Boost> userBoosts = boosts.getOrDefault(userId, new ArrayList<>());
        List<Boost> newUserBoosts = new ArrayList<>();
        for (Boost boost : userBoosts) {
            if (boost.getName().equalsIgnoreCase(boostName)) continue;
            newUserBoosts.add(boost);
        }
        if (newUserBoosts.size() > 0) boosts.put(userId, newUserBoosts);
        else boosts.remove(userId);
    }

    public static void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM boosts;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                String boostName = rs.getString("boostName");
                String boostValue = rs.getString("boostValue");
                String boostValueDisplay = rs.getString("boostValueDisplay");
                long boostEnd = Long.parseLong(rs.getString("boostEnd"));
                if (boostEnd <= System.currentTimeMillis()) continue;
                List<Boost> userBoosts = boosts.getOrDefault(userId, new ArrayList<>());
                userBoosts.add(new Boost(boostName.toLowerCase(), boostValue, boostValueDisplay, boostEnd));
                boosts.put(userId, userBoosts);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static class Boost {

        private final String name;
        private final String value;
        private final String valueDisplay;
        private final long end;

        Boost(String boostName, String boostValue, String valueDisplay, long end) {
            this.name = boostName;
            this.value = boostValue;
            this.valueDisplay = valueDisplay;
            this.end = end;
        }

        public Boost(String boostName, String boostValue, long end) {
            this(boostName, boostValue, boostValue, end);
        }

        public long getEnd() {
            return end;
        }

        public String getValueDisplay() {
            return valueDisplay;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}