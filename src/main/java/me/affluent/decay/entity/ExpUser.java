package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.api.XPApi;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.LevelUpEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.util.DonatorUtil;
import me.affluent.decay.util.ElixirUtil;
import net.dv8tion.jda.api.entities.TextChannel;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ExpUser {

    private static final HashMap<String, ExpUser> cache = new HashMap<>();
    private static final HashMap<String, Double> boost = new HashMap<>();

    private final String userId;
    private int level;
    private BigInteger experience;
    private int elixirLevel;
    private BigInteger elixirExperience;

    public static void clearCache() {
        cache.clear();
    }

    private ExpUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    public static double getBoost(String uid) {
        return boost.getOrDefault(uid, -1.0);
    }

    public static void addBoost(String uid, long percentage) {
        boost.put(uid, (percentage / 100.0));
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM elexp WHERE userId=?;", userId)) {
            if (rs.next()) {
                elixirLevel = rs.getInt("elixirLevel");
                String elexpString = rs.getString("elixirExperience");
                elixirExperience = new BigInteger(elexpString);
            } else {
                elixirLevel = 0;
                elixirExperience = BigInteger.ZERO;
                Withering.getBot().getDatabase().update("INSERT INTO elexp VALUES (?, ?, ?);", userId, 0, "0");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM exp WHERE userId=?;", userId)) {
            if (rs.next()) {
                level = rs.getInt("level");
                String expString = rs.getString("experience");
                experience = new BigInteger(expString);
            } else {
                level = 1;
                experience = BigInteger.ZERO;
                Withering.getBot().getDatabase().update("INSERT INTO exp VALUES (?, ?, ?);", userId, 1, "0");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public int getLevel() {
        return level;
    }

    public BigInteger getExperience() {
        return experience;
    }

    public double getEstimatedExperience() {
        return experience.compareTo(BigInteger.valueOf(0));
    }

    public int getElixirLevel() {
        return elixirLevel;
    }

    public BigInteger getElixirExperience() {
        return elixirExperience;
    }

    public double maxXP() {
        return XPApi.getNeededXP(level);
    }

    public boolean isMaxed() {
        return getLevel() >= 150;
    }

    public boolean isElixirMaxed() {
        return getElixirLevel() >= 999;
    }

    public void addExp(long amount, TextChannel tc) {
        if (boost.containsKey(userId)) amount += amount * boost.get(userId);
        if (DonatorUtil.isBoosting(userId)) amount += amount * 0.15; //TODO boosters get mroe xp
        if (isMaxed()) {
            addElixirExp(amount);
            return;
        }
        if (isElixirMaxed()) {
            setLevel(151);
            return;
        }
        setExperience(this.experience.add(BigInteger.valueOf(amount)));
        while (XPApi.canLevelUp(this.experience.doubleValue(), level)) {
            double neededEXP = XPApi.getNeededXP(level);
            double expnow = this.experience.doubleValue();
            setExperience(BigInteger.valueOf(new Double(expnow - neededEXP).longValue()));
            Rank rankBefore = Rank.getRank(level);
            final int newLevel = level + 1;
            setLevel(newLevel);
            Player p = Player.getPlayer(userId);
            LevelUpEvent levelUpEvent = new LevelUpEvent(p, p.getExpUser().getLevel());
            EventManager.callEvent(levelUpEvent);
            Rank rank = Rank.getRank(newLevel);
            if (rank.getHealth() != rankBefore.getHealth()) {
                String roleName = Player.getPlayer(userId).getRankDisplay();
                Withering.updateRole(userId, roleName);
                HealthUser hu = HealthUser.getHealthUser(userId);
                int upgradedHealth = hu.getHealth() + rank.getHealth();
                if (upgradedHealth > hu.getMaxHealth()) upgradedHealth = hu.getMaxHealth();
                hu.setHealth(upgradedHealth);
                for (ItemType it : ItemType.values()) {
                    if (it.getLevelRequirement() == newLevel) {
                        tc.sendMessage(Language.getLocalized(userId, "rankup_message",
                                "Congratulations {user_mention}! You just hit the {new_rank} rank. You can now use " +
                                "{material}.").replace("{user_mention}", "<@!" + userId + ">")
                                .replace("{new_rank}", "`" + roleName + "`")
                                .replace("{material}", "`" + it.name().toLowerCase().replaceAll("_", " ") + "`"))
                                .queue();
                    }
                }
            }
        }
        cache();
    }

    public void addElixirExp(long amount) {
        setElixirExperience(this.elixirExperience.add(BigInteger.valueOf(amount)));
        while (XPApi.canLevelUpElixir(this.elixirExperience.doubleValue(), elixirLevel)) {
            double neededElixirExp = XPApi.getNeededElixirXP(elixirLevel);
            double elixirexpnow = this.elixirExperience.doubleValue();
            setElixirExperience(BigInteger.valueOf(new Double(elixirexpnow - neededElixirExp).longValue()));
            int newElixirLevel = elixirLevel + 1;
            ElixirUtil.setElixir(userId, ElixirUtil.getElixir(userId) + ((int) (newElixirLevel * 1.25)));
            setElixirLevel(newElixirLevel);
        }
    }

    public void setElixirExperience(BigInteger elixirExperience) {
        this.elixirExperience = elixirExperience;
        cache();
        Withering.getBot().getDatabase()
                .update("UPDATE elexp SET elixirExperience=? WHERE userId=?;", elixirExperience.toString(), userId);
    }

    public void setElixirLevel(int elixirLevel) {
        this.elixirLevel = elixirLevel;
        cache();
        Withering.getBot().getDatabase().update("UPDATE elexp SET elixirLevel=? WHERE userId=?;", elixirLevel, userId);
    }

    public void setExperience(BigInteger experience) {
        this.experience = experience;
        cache();
        Withering.getBot().getDatabase()
                .update("UPDATE exp SET experience=? WHERE userId=?;", experience.toString(), userId);
    }

    public void setLevel(int level) {
        this.level = level;
        cache();
        Withering.getBot().getDatabase().update("UPDATE exp SET level=? WHERE userId=?;", level, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ExpUser getExpUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ExpUser(userId);
    }

}