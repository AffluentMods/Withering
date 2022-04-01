package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.database.Database;
import me.affluent.decay.entity.otherInventory.ArmorDragonUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.entity.otherInventory.ArmorWitherUser;
import me.affluent.decay.enums.Gender;
import me.affluent.decay.pets.PetExpUser;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.weapon.Sword;
import net.dv8tion.jda.api.entities.User;

import java.net.IDN;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Player {

    private static HashMap<String, Player> cache = new HashMap<>();

    private static final HashMap<String, User> userObjectCache = new HashMap<>();

    private final String userId;
    private Gender gender;

    private Player(String userId) {
        this.userId = userId;
        cache.put(userId, this);
    }

    public Player(String userId, Gender gender) {
        this.userId = userId;
        this.gender = gender;
        load(true);
    }

    public BadgeUser getBadgeUser() {
        return BadgeUser.getBadgeUser(userId);
    }

    public ExpUser getExpUser() {
        return ExpUser.getExpUser(userId);
    }

    public PetExpUser getPetExpUser() {
        return PetExpUser.getPetExpUser(userId);
    }

    public HealthUser getHealthUser() {
        return HealthUser.getHealthUser(userId);
    }

    public ArmorIronUser getArmorIronUser() { return ArmorIronUser.getArmorIronUser(userId); }

    public ArmorDragonUser getArmorDragonUser() { return ArmorDragonUser.getArmorDragonUser(userId); }

    public ArmorWitherUser getArmorWitherUser() { return ArmorWitherUser.getArmorWitherUser(userId); }

    public ArmorUser getArmorUser() {
        return ArmorUser.getArmorUser(userId);
    }

    public PetUser getPetUser() {
        return PetUser.getPetUser(userId);
    }

    public PetUtil getPetUtil() {
        return PetUtil.getPetUtil(userId);
    }

    public InventoryUser getInventoryUser() {
        return InventoryUser.getInventoryUser(userId);
    }

    public EcoUser getEcoUser() {
        return EcoUser.getEcoUser(userId);
    }

    public User getUser() {
        return Player.getUser(userId);
    }

    public static User getUser(String userId) {
        if (userObjectCache.containsKey(userId)) return userObjectCache.get(userId);
        User user = Withering.getBot().getShardManager().retrieveUserById(userId).complete();
        userObjectCache.put(userId, user);
        return user;
    }

    public Rank getRank() {
        return Rank.getRank(getExpUser().getLevel());
    }

    public Gender getGender() {
        return gender;
    }

    public String getRankDisplay() {
        if (gender == Gender.SIR) {
            return getRank().getsirName();
        }
        if (gender == Gender.MADAM) {
            return getRank().getmadamName();
        }
        if (gender == Gender.NEUTRAL) {
            return getRank().getneutralName();
        }
        return null;
    }

    public int getInvLimit() {
        int level = getExpUser().getLevel();
        int invLimit = 0;
        if (level <= 19) {
            invLimit = 100;
        } else if (level <= 74) {
            invLimit = 150;
        } else if (level <= 149) {
            invLimit = 200;
        } else {
            invLimit = 250;
        }
        int elevel = getExpUser().getElixirLevel();
        int invLimit2 = 0;
        if (elevel <= 50) {
            invLimit2 = 10;
        } else if (elevel <= 100) {
            invLimit2 = 20;
        } else if (elevel <= 150) {
            invLimit2 = 30;
        } else if (elevel <= 200) {
            invLimit2 = 40;
        } else if (elevel <= 250) {
            invLimit2 = 50;
        } else if (elevel <= 300) {
            invLimit2 = 60;
        } else if (elevel <= 350) {
            invLimit2 = 70;
        } else if (elevel <= 400) {
            invLimit2 = 80;
        } else if (elevel <= 450) {
            invLimit2 = 90;
        } else if (elevel <= 500) {
            invLimit2 = 100;
        } else if (elevel <= 550) {
            invLimit2 = 110;
        } else if (elevel <= 600) {
            invLimit2 = 120;
        } else if (elevel <= 650) {
            invLimit2 = 130;
        } else if (elevel <= 700) {
            invLimit2 = 140;
        } else if (elevel <= 750) {
            invLimit2 = 150;
        } else if (elevel <= 800) {
            invLimit2 = 160;
        } else if (elevel <= 850) {
            invLimit2 = 170;
        } else if (elevel <= 900) {
            invLimit2 = 180;
        } else if (elevel <= 950) {
            invLimit2 = 190;
        } else if (elevel <= 1000) {
            invLimit2 = 200;
        }
        int add = getBadgeUser().getBadgeList().contains("donator") ? 150 : 0;
        return invLimit + invLimit2 + add;
    }

    public String getMention() {
        return getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">");
    }

    public static boolean playerExists(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT userId FROM profiles WHERE userId=?;", userId)) {
            if (rs.next()) return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Player getPlayer(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        Player player = new Player(userId);
        player.load(false);
        cache.put(userId, player);
        return player;
    }

    private void load(boolean isNewPlayer) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM profiles WHERE userId=?;", userId)) {
            if (rs.next()) {
                int genderInt = rs.getInt("gender");
                this.gender = genderInt == 2 ? Gender.SIR : (genderInt == 1 ? Gender.MADAM : Gender.NEUTRAL);
            } else {
                if (isNewPlayer) {
                    Withering.getBot().getDatabase()
                            .update("INSERT INTO profiles (userId, prefix, language, gender, clan) VALUES (?, ?, ?, " +
                                    "?, ?);", userId, "w.", "en", gender == Gender.SIR ? 2 : (gender == Gender.MADAM ? 1 : 0), -1);

                    String itemName = "junk wood sword";
                    try (PreparedStatement ps = Withering.getBot().getDatabase().getCon()
                            .prepareStatement("INSERT INTO inventory (userId, itemName) VALUES (?, ?);",
                                    Statement.RETURN_GENERATED_KEYS)) {
                        ps.setObject(1, userId);
                        ps.setObject(2, itemName);
                        ps.executeUpdate();
                        ResultSet rs2 = ps.getGeneratedKeys();
                        if (rs2.next()) {
                            long id = rs2.getInt(1);
                                Withering.getBot().getDatabase()
                                        .update("UPDATE armor SET weapon=?, weaponID=? WHERE userId=?;", itemName, id, userId);
                                Withering.getBot().getDatabase()
                                        .update("DELETE FROM inventory WHERE userId=? AND ID=?;", userId, id);
                            cache();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void reset() {
        Database db = Withering.getBot().getDatabase();
        db.update("DELETE FROM armor WHERE userId=?;", userId);
        db.update("DELETE FROM armoriron WHERE userId=?;", userId);
        db.update("DELETE FROM armordragon WHERE userId=?;", userId);
        db.update("DELETE FROM armorwither WHERE userId=?;", userId);
        db.update("DELETE FROM artifacts WHERE userId=?;", userId);
        db.update("DELETE FROM boosts WHERE userId=?;", userId);
        db.update("DELETE FROM cooldowns WHERE userId=?;", userId);
        db.update("DELETE FROM dailyRewards WHERE userId=?;", userId);
        db.update("DELETE FROM economy WHERE userId=?;", userId);
        db.update("DELETE FROM elexp WHERE userId=?;", userId);
        db.update("DELETE FROM elixir WHERE userId=?;", userId);
        db.update("DELETE FROM exp WHERE userId=?;", userId);
        db.update("DELETE FROM gems WHERE userId=?;", userId);
        db.update("DELETE FROM goldentokens WHERE userId=?;", userId);
        db.update("DELETE FROM health WHERE userId=?;", userId);
        db.update("DELETE FROM inventory WHERE userId=?;", userId);
        db.update("DELETE FROM journal WHERE userId=?;", userId);
        db.update("DELETE FROM journalTotal WHERE userId=?;", userId);
        db.update("DELETE FROM playerrank WHERE userId=?;", userId);
        db.update("DELETE FROM profiles WHERE userId=?;", userId);
        db.update("DELETE FROM scrolls WHERE userId=?;", userId);
        db.update("DELETE FROM skills WHERE userId=?;", userId);
        db.update("DELETE FROM stats WHERE userId=?;", userId);
        db.update("DELETE FROM tokens WHERE userId=?;", userId);
        db.update("DELETE FROM userbadges WHERE userId=?;", userId);
        db.update("DELETE FROM userkeys WHERE userId=?;", userId);
        db.update("DELETE FROM userprefix WHERE userId=?;", userId);
        db.update("DELETE FROM votes WHERE userId=?;", userId);
        db.update("DELETE FROM power WHERE userId=?;", userId);
        db.update("DELETE FROM codes WHERE userId=?;", userId);
        db.update("DELETE FROM ingots WHERE userId=?;", userId);
        db.update("DELETE FROM skills WHERE userId=?", userId);
        db.update("DELETE FROM orbs WHERE userId=?", userId);
        db.update("DELETE FROM itemStats WHERE userId=?", userId);
        db.update("DELETE FROM ironScrolls WHERE userId=?", userId);
        db.update("DELETE FROM dragonScrolls WHERE userId=?", userId);
        db.update("DELETE FROM witherScrolls WHERE userId=?", userId);
        cache.remove(userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static String getDisplay(User u) {
        return u.getName() + "#" + u.getDiscriminator();
    }

    public String getUserId() {
        return userId;
    }
}