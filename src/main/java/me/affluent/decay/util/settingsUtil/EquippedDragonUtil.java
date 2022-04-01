package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class EquippedDragonUtil {
    
    private static HashMap<String, EquippedDragonUtil> cache = new HashMap<>();

    private final String userId;
    private String dragonHelmet;
    private String dragonChestplate;
    private String dragonTrousers;
    private String dragonGloves;
    private String dragonBoots;
    private String dragonSword;
    private String dragonShield;

    private EquippedDragonUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM equippedDragon WHERE userId=?;", userId)) {
            if (rs.next()) {
                dragonHelmet = rs.getString("dragonhelmet");
                dragonChestplate = rs.getString("dragonchestplate");
                dragonTrousers = rs.getString("dragontrousers");
                dragonGloves = rs.getString("dragongloves");
                dragonBoots = rs.getString("dragonboots");
                dragonSword = rs.getString("dragonsword");
                dragonShield = rs.getString("dragonshield");
            } else {
                dragonHelmet = "equipped";
                dragonChestplate = "equipped";
                dragonTrousers = "equipped";
                dragonGloves = "equipped";
                dragonBoots = "equipped";
                dragonSword = "equipped";
                dragonShield = "equipped";
                Withering.getBot().getDatabase().update("INSERT INTO equippedDragon VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                        userId, dragonHelmet, dragonChestplate, dragonGloves, dragonTrousers, dragonBoots, dragonSword, dragonShield);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getDragonHelmet() {
        return dragonHelmet;
    }

    public String getDragonChestplate() {
        return dragonChestplate;
    }

    public String getDragonGloves() {
        return dragonGloves;
    }

    public String getDragonTrousers() {
        return dragonTrousers;
    }

    public String getDragonBoots() {
        return dragonBoots;
    }

    public String getDragonSword() {
        return dragonSword;
    }

    public String getDragonShield() {
        return dragonShield;
    }

    public void setDragonHelmet(String dragonHelmet) {
        this.dragonHelmet = dragonHelmet;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragonhelmet=? WHERE userId=?;", dragonHelmet, userId);
    }

    public void setDragonChestplate(String dragonChestplate) {
        this.dragonChestplate = dragonChestplate;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragonchestplate=? WHERE userId=?;", dragonChestplate, userId);
    }

    public void setDragonGloves(String dragonGloves) {
        this.dragonGloves = dragonGloves;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragongloves=? WHERE userId=?;", dragonGloves, userId);
    }

    public void setDragonTrousers(String dragonTrousers) {
        this.dragonTrousers = dragonTrousers;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragontrousers=? WHERE userId=?;", dragonTrousers, userId);
    }

    public void setDragonBoots(String dragonBoots) {
        this.dragonBoots = dragonBoots;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragonboots=? WHERE userId=?;", dragonBoots, userId);
    }

    public void setDragonSword(String dragonSword) {
        this.dragonSword = dragonSword;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragonsword=? WHERE userId=?;", dragonSword, userId);
    }

    public void setDragonShield(String dragonShield) {
        this.dragonShield = dragonShield;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedDragon SET dragonshield=? WHERE userId=?;", dragonShield, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static EquippedDragonUtil getEquippedDragonUtil(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        return new EquippedDragonUtil(userId);
    }
}

