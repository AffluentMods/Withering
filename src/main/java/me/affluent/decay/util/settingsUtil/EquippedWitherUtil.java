package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class EquippedWitherUtil {
    
    private static HashMap<String, EquippedWitherUtil> cache = new HashMap<>();

    private final String userId;
    private String witherHelmet;
    private String witherChestplate;
    private String witherTrousers;
    private String witherGloves;
    private String witherBoots;
    private String witherSword;
    private String witherShield;

    private EquippedWitherUtil(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM equippedWither WHERE userId=?;", userId)) {
            if (rs.next()) {
                witherHelmet = rs.getString("witherhelmet");
                witherChestplate = rs.getString("witherchestplate");
                witherTrousers = rs.getString("withertrousers");
                witherGloves = rs.getString("withergloves");
                witherBoots = rs.getString("witherboots");
                witherSword = rs.getString("withersword");
                witherShield = rs.getString("withershield");
            } else {
                witherHelmet = "equipped";
                witherChestplate = "equipped";
                witherTrousers = "equipped";
                witherGloves = "equipped";
                witherBoots = "equipped";
                witherSword = "equipped";
                witherShield = "equipped";
                Withering.getBot().getDatabase().update("INSERT INTO equippedWither VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                        userId, witherHelmet, witherChestplate, witherGloves, witherTrousers, witherBoots, witherSword, witherShield);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getWitherHelmet() {
        return witherHelmet;
    }

    public String getWitherChestplate() {
        return witherChestplate;
    }

    public String getWitherGloves() {
        return witherGloves;
    }

    public String getWitherTrousers() {
        return witherTrousers;
    }

    public String getWitherBoots() {
        return witherBoots;
    }

    public String getWitherSword() {
        return witherSword;
    }

    public String getWitherShield() {
        return witherShield;
    }

    public void setWitherHelmet(String witherHelmet) {
        this.witherHelmet = witherHelmet;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET witherhelmet=? WHERE userId=?;", witherHelmet, userId);
    }

    public void setWitherChestplate(String witherChestplate) {
        this.witherChestplate = witherChestplate;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET witherchestplate=? WHERE userId=?;", witherChestplate, userId);
    }

    public void setWitherGloves(String witherGloves) {
        this.witherGloves = witherGloves;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET withergloves=? WHERE userId=?;", witherGloves, userId);
    }

    public void setWitherTrousers(String witherTrousers) {
        this.witherTrousers = witherTrousers;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET withertrousers=? WHERE userId=?;", witherTrousers, userId);
    }

    public void setWitherBoots(String witherBoots) {
        this.witherBoots = witherBoots;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET witherboots=? WHERE userId=?;", witherBoots, userId);
    }

    public void setWitherSword(String witherSword) {
        this.witherSword = witherSword;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET withersword=? WHERE userId=?;", witherSword, userId);
    }

    public void setWitherShield(String witherShield) {
        this.witherShield = witherShield;
        cache();
        Withering.getBot().getDatabase().update("UPDATE equippedWither SET withershield=? WHERE userId=?;", witherShield, userId);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static EquippedWitherUtil getEquippedWitherUtil(String userId) {
        if (cache.containsKey(userId)) return cache.get(userId);
        return new EquippedWitherUtil(userId);
    }
}

