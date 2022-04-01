package me.affluent.decay.entity.otherInventory;

import me.affluent.decay.Withering;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonWeapon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArmorDragonUser {

    private static HashMap<String, ArmorDragonUser> cache = new HashMap<>();

    private final String userId;
    private DragonHelmet helmet;
    private DragonChestplate chestplate;
    private DragonGloves gloves;
    private DragonTrousers trousers;
    private DragonBoots boots;
    private DragonWeapon weapon;
    private DragonShield shield;

    public static void clearCache() {
        cache.clear();
    }

    private ArmorDragonUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM armordragon WHERE userId=?;", userId)) {
            if (rs.next()) {
                this.helmet = DragonHelmet.getDragonHelmet(rs.getString("dragonhelmet"));
                this.chestplate = DragonChestplate.getDragonChestplate(rs.getString("dragonchestplate"));
                this.gloves = DragonGloves.getDragonGloves(rs.getString("dragongloves"));
                this.trousers = DragonTrousers.getDragonTrousers(rs.getString("dragontrousers"));
                this.boots = DragonBoots.getDragonBoots(rs.getString("dragonboots"));
                this.weapon = DragonWeapon.getWeapon(rs.getString("dragonweapon"));
                this.shield = DragonShield.getDragonShield(rs.getString("dragonshield"));
            } else {
                this.helmet = null;
                this.chestplate = null;
                this.gloves = null;
                this.trousers = null;
                this.boots = null;
                this.weapon = null;
                this.shield = null;
                Withering.getBot().getDatabase()
                        .update("INSERT INTO armordragon VALUES (?, ?, ?, ?, ?, ?, ?, ?);", userId, "", "", "", "", "",
                                "", "");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Armor> getAllArmor() {
        List<Armor> armorList = new ArrayList<>();
        DragonHelmet h = getDragonHelmet();
        DragonChestplate c = getDragonChestplate();
        DragonGloves g = getDragonGloves();
        DragonTrousers t = getDragonTrousers();
        DragonBoots b = getDragonBoots();
        if (h != null) armorList.add(h);
        if (c != null) armorList.add(c);
        if (g != null) armorList.add(g);
        if (t != null) armorList.add(t);
        if (b != null) armorList.add(b);
        return armorList;
    }

    public DragonHelmet getDragonHelmet() {
        return helmet;
    }

    public DragonChestplate getDragonChestplate() {
        return chestplate;
    }

    public DragonGloves getDragonGloves() {
        return gloves;
    }

    public DragonTrousers getDragonTrousers() {
        return trousers;
    }

    public DragonBoots getDragonBoots() {
        return boots;
    }

    public DragonWeapon getDragonWeapon() {
        return weapon;
    }

    public DragonShield getDragonShield() {
        return shield;
    }

    public int getAllProtection() {
        int prot = 0;
        if (helmet != null) prot += helmet.getProtection();
        if (chestplate != null) prot += chestplate.getProtection();
        if (gloves != null) prot += gloves.getProtection();
        if (trousers != null) prot += trousers.getProtection();
        if (boots != null) prot += boots.getProtection();
        return prot;
    }

    public void updateDragonHelmet(DragonHelmet helmet) {
        this.helmet = helmet;
        if (helmet != null) Withering.getBot().getDatabase()
                .update("UPDATE armordragon SET dragonhelmet=? WHERE userId=?;", helmet.getName(), userId);
        cache();
    }

    public void updateDragonChestplate(DragonChestplate chestplate) {
        this.chestplate = chestplate;
        if (chestplate != null) Withering.getBot().getDatabase()
                .update("UPDATE armordragon SET dragonchestplate=? WHERE userId=?;", chestplate.getName(), userId);
        cache();
    }

    public void updateDragonGloves(DragonGloves gloves) {
        this.gloves = gloves;
        if (gloves != null) Withering.getBot().getDatabase()
                .update("UPDATE armordragon SET dragongloves=? WHERE userId=?;", gloves.getName(), userId);
        cache();
    }

    public void updateDragonTrousers(DragonTrousers trousers) {
        this.trousers = trousers;
        if (trousers != null) Withering.getBot().getDatabase()
                .update("UPDATE armordragon SET dragontrousers=? WHERE userId=?;", trousers.getName(), userId);
        cache();
    }

    public void updateDragonBoots(DragonBoots boots) {
        this.boots = boots;
        if (boots != null) Withering.getBot().getDatabase()
                .update("UPDATE armordragon SET dragonboots=? WHERE userId=?;", boots.getName(), userId);
        cache();
    }

    public void updateDragonWeapon(DragonWeapon weapon) {
        this.weapon = weapon;
        Withering.getBot().getDatabase().update("UPDATE armordragon SET dragonweapon=? WHERE userId=?;", weapon.getName(), userId);
        cache();
    }

    public void updateDragonShield(DragonShield shield) {
        this.shield = shield;
        String sh = shield == null ? "" : shield.getName();
        Withering.getBot().getDatabase().update("UPDATE armordragon SET dragonshield=? WHERE userId=?", sh, userId);
        cache();
    }

    public String getUserId() {
        return userId;
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ArmorDragonUser getArmorDragonUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ArmorDragonUser(userId);
    }
}
