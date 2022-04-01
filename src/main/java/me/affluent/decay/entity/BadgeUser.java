package me.affluent.decay.entity;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BadgeUser {

    private static HashMap<String, BadgeUser> cache = new HashMap<>();

    private final String userId;
    private List<String> badgeList;

    public static void clearCache() {
        cache.clear();
    }

    private BadgeUser(String userId) {
        this.userId = userId;
        this.badgeList = new ArrayList<>();
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM userbadges WHERE userId=?;", userId)) {
            while (rs.next()) {
                String badge = rs.getString("badge").toLowerCase();
                if (!badgeList.contains(badge)) badgeList.add(badge);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getBadgeList() {
        return badgeList;
    }

    public void addBadge(String badge) {
        badgeList.add(badge.toLowerCase());
        cache();
        Withering.getBot().getDatabase().update("INSERT INTO userbadges VALUES (?, ?);", userId, badge.toLowerCase());
    }

    public void removeBadge(String badge) {
        badgeList.remove(badge.toLowerCase());
        cache();
        Withering.getBot().getDatabase()
                .update("DELETE FROM userbadges WHERE userId=? AND badge=?;", userId, badge.toLowerCase());
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static BadgeUser getBadgeUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new BadgeUser(userId);
    }
}