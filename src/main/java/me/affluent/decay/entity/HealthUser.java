package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.skill.Skill;
import me.affluent.decay.util.SkillUtil;
import me.affluent.decay.util.system.EmoteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class HealthUser {

    private static HashMap<String, HealthUser> cache = new HashMap<>();

    private final String userId;
    private int health;
    private double maxhealth;
    private double healthB;
    private double exp;
    private double maxexp;

    public static void clearCache() {
        cache.clear();
    }

    public static void clearCache(String userId) {
        cache.remove(userId);
    }

    private HealthUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM health WHERE userId=?;", userId)) {
            if (rs.next()) {
                health = rs.getInt("health");
                int maxHealth = getMaxHealth();
                if (health > maxHealth) setHealth(maxHealth);
            } else {
                health = 10;
                Withering.getBot().getDatabase().update("INSERT INTO health VALUES (?, ?, ?);", userId, 10, 0);
            }
            Withering.getBot().getDatabase()
                    .update("UPDATE health SET maxhealth=? WHERE userId=?;", getMaxHealth(), userId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static int getJuggernaut(String userId) {
        int juggernaut = 0;
        int jsl = SkillUtil.getLevel(userId, 1);
        if (jsl > 0) {
            Skill juggernautSkill = Skill.getSkill(1);
            juggernaut = (int) juggernautSkill.getValue(jsl);
        }
        return juggernaut;
    }

    private static int getLevelHealth(String userId) {
        int levelHealth = 0;
        int currentLevel = ExpUser.getExpUser(userId).getLevel();
                if (currentLevel > 0) {
                    levelHealth = currentLevel * 2;
                }
                return levelHealth;
    }

    public String getUserId() {
        return userId;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        Withering.getBot().getDatabase().update("UPDATE health SET health=? WHERE userId=?;", health, userId);
        cache();
    }

    public int getMaxHealth() {
        return Rank.getRank(ExpUser.getExpUser(userId).getLevel()).getHealth() + getJuggernaut(userId) + getLevelHealth(userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static HealthUser getHealthUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new HealthUser(userId);
    }
}