package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class EquippedIronUtil {

    private static HashMap<String, EquippedIronUtil> cache = new HashMap<>();

    private final String userId;
    private String ironHelmet;
    private String ironChestplate;
    private String ironTrousers;
    private String ironGloves;
    private String ironBoots;
    private String ironSword;
    private String ironShield;

    private EquippedIronUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM equippedIron WHERE userId=?;", userId)) {
            if (rs.next()) {
                ironHelmet = rs.getString("ironhelmet");
                ironChestplate = rs.getString("ironchestplate");
                ironTrousers = rs.getString("irontrousers");
                ironGloves = rs.getString("irongloves");
                ironBoots = rs.getString("ironboots");
                ironSword = rs.getString("ironsword");
                ironShield = rs.getString("ironshield");
            } else {
                ironHelmet = "equipped";
                ironChestplate = "equipped";
                ironTrousers = "equipped";
                ironGloves = "equipped";
                ironBoots = "equipped";
                ironSword = "equipped";
                ironShield = "equipped";
                Withering.getBot().getDatabase().update("INSERT INTO equippedIron VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                        userId, ironHelmet, ironChestplate, ironGloves, ironTrousers, ironBoots, ironSword, ironShield);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getIronHelmet() {
        return ironHelmet;
    }

    public String getIronChestplate() {
        return ironChestplate;
    }

    public String getIronGloves() {
        return ironGloves;
    }

    public String getIronTrousers() {
        return ironTrousers;
    }

    public String getIronBoots() {
        return ironBoots;
    }

    public String getIronSword() {
        return ironSword;
    }

    public String getIronShield() {
        return ironShield;
    }

    public void setIronHelmet(String ironHelmet) {
        this.ironHelmet = ironHelmet;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET ironhelmet=? WHERE userId=?;", ironHelmet, userId);
    }
    
    public void setIronChestplate(String ironChestplate) {
        this.ironChestplate = ironChestplate;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET ironchestplate=? WHERE userId=?;", ironChestplate, userId);
    }

    public void setIronGloves(String ironGloves) {
        this.ironGloves = ironGloves;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET irongloves=? WHERE userId=?;", ironGloves, userId);
    }

    public void setIronTrousers(String ironTrousers) {
        this.ironTrousers = ironTrousers;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET irontrousers=? WHERE userId=?;", ironTrousers, userId);
    }

    public void setIronBoots(String ironBoots) {
        this.ironBoots = ironBoots;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET ironboots=? WHERE userId=?;", ironBoots, userId);
    }

    public void setIronSword(String ironSword) {
        this.ironSword = ironSword;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET ironsword=? WHERE userId=?;", ironSword, userId);
    }

    public void setIronShield(String ironShield) {
        this.ironShield = ironShield;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedIron SET ironshield=? WHERE userId=?;", ironShield, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static EquippedIronUtil getEquippedIronUtil(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        return new EquippedIronUtil(userId);
    }
}
