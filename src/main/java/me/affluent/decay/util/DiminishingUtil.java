package me.affluent.decay.util;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiminishingUtil {

    private static final HashMap<String, List<Diminish>> diminishings = new HashMap<>();
    private static final long diminishTimer = (60 * 60 * 1000) + (30 * 60 * 1000);

    public static void pushDiminish(String userId, String targetId) {
        Diminish diminish = getDiminish(userId, targetId);
        if (diminish != null && diminish.getUntil() <= System.currentTimeMillis()) diminish = null;
        long until = diminish == null ? System.currentTimeMillis() + diminishTimer : diminish.until;
        int count = diminish == null ? 1 : diminish.getCount() + 1;
        Diminish newDiminish = new Diminish(userId, targetId, count, until);
        removeDiminish(userId, targetId);
        List<Diminish> newDiminishes = getAllDiminishes(userId);
        newDiminishes.add(newDiminish);
        diminishings.put(userId, newDiminishes);
    }

    public static Diminish getDiminish(String userId, String targetId) {
        Diminish diminish1 = null;
        for (Diminish diminish : getAllDiminishes(userId))
            if (diminish.getTargetId().equals(targetId)) diminish1 = diminish;
        if (diminish1 != null && diminish1.getUntil() <= System.currentTimeMillis()) {
            removeDiminish(userId, targetId);
            diminish1 = null;
        }
        return diminish1;
    }

    private static void removeDiminish(String userId, String targetId) {
        List<Diminish> diminishes = getAllDiminishes(userId);
        List<Diminish> newDiminishes = new ArrayList<>();
        for (Diminish diminish1 : diminishes) {
            if (diminish1.getTargetId().equals(targetId)) continue;
            newDiminishes.add(diminish1);
        }
        diminishings.put(userId, newDiminishes);
    }

    private static List<Diminish> getAllDiminishes(String userId) {
        return diminishings.getOrDefault(userId, new ArrayList<>());
    }

    public static void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM diminishings;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                String targetId = rs.getString("targetId");
                int count = rs.getInt("count");
                long until = rs.getLong("until");
                List<Diminish> diminishes = diminishings.getOrDefault(userId, new ArrayList<>());
                diminishes.add(new Diminish(userId, targetId, count, until));
                diminishings.put(userId, diminishes);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static class Diminish {

        private final String userId;
        private final String targetId;
        private final int count;
        private final long until;

        Diminish(String userId, String targetId, int count, long until) {
            this.userId = userId;
            this.targetId = targetId;
            this.count = count;
            this.until = until;
        }

        public String getUserId() {
            return userId;
        }

        public int getCount() {
            return count;
        }

        public long getUntil() {
            return until;
        }

        public String getTargetId() {
            return targetId;
        }
    }
}