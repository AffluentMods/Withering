package me.affluent.decay.entity.otherInventory;

import me.affluent.decay.Withering;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArmorIronUser {

    private static HashMap<String, ArmorIronUser> cache = new HashMap<>();

    private final String userId;
    private IronHelmet helmet;
    private IronChestplate chestplate;
    private IronGloves gloves;
    private IronTrousers trousers;
    private IronBoots boots;
    private IronWeapon weapon;
    private IronShield shield;

    public static void clearCache() {
        cache.clear();
    }

    private ArmorIronUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM armoriron WHERE userId=?;", userId)) {
            if (rs.next()) {
                this.helmet = IronHelmet.getIronHelmet(rs.getString("ironhelmet"));
                this.chestplate = IronChestplate.getIronChestplate(rs.getString("ironchestplate"));
                this.gloves = IronGloves.getIronGloves(rs.getString("irongloves"));
                this.trousers = IronTrousers.getIronTrousers(rs.getString("irontrousers"));
                this.boots = IronBoots.getIronBoots(rs.getString("ironboots"));
                this.weapon = IronWeapon.getWeapon(rs.getString("ironweapon"));
                this.shield = IronShield.getIronShield(rs.getString("ironshield"));
            } else {
                this.helmet = null;
                this.chestplate = null;
                this.gloves = null;
                this.trousers = null;
                this.boots = null;
                this.weapon = null;
                this.shield = null;
                Withering.getBot().getDatabase()
                        .update("INSERT INTO armoriron VALUES (?, ?, ?, ?, ?, ?, ?, ?);", userId, "", "", "", "", "",
                                "", "");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Armor> getAllArmor() {
        List<Armor> armorList = new ArrayList<>();
        IronHelmet h = getIronHelmet();
        IronChestplate c = getIronChestplate();
        IronGloves g = getIronGloves();
        IronTrousers t = getIronTrousers();
        IronBoots b = getIronBoots();
        if (h != null) armorList.add(h);
        if (c != null) armorList.add(c);
        if (g != null) armorList.add(g);
        if (t != null) armorList.add(t);
        if (b != null) armorList.add(b);
        return armorList;
    }

    public IronHelmet getIronHelmet() {
        return helmet;
    }

    public IronChestplate getIronChestplate() {
        return chestplate;
    }

    public IronGloves getIronGloves() {
        return gloves;
    }

    public IronTrousers getIronTrousers() {
        return trousers;
    }

    public IronBoots getIronBoots() {
        return boots;
    }

    public IronWeapon getIronWeapon() {
        return weapon;
    }

    public IronShield getIronShield() {
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

    public void updateIronHelmet(IronHelmet helmet) {
        this.helmet = helmet;
        if (helmet != null) Withering.getBot().getDatabase()
                .update("UPDATE armoriron SET ironhelmet=? WHERE userId=?;", helmet.getName(), userId);
        cache();
    }

    public void updateIronChestplate(IronChestplate chestplate) {
        this.chestplate = chestplate;
        if (chestplate != null) Withering.getBot().getDatabase()
                .update("UPDATE armoriron SET ironchestplate=? WHERE userId=?;", chestplate.getName(), userId);
        cache();
    }

    public void updateIronGloves(IronGloves gloves) {
        this.gloves = gloves;
        if (gloves != null) Withering.getBot().getDatabase()
                .update("UPDATE armoriron SET irongloves=? WHERE userId=?;", gloves.getName(), userId);
        cache();
    }

    public void updateIronTrousers(IronTrousers trousers) {
        this.trousers = trousers;
        if (trousers != null) Withering.getBot().getDatabase()
                .update("UPDATE armoriron SET irontrousers=? WHERE userId=?;", trousers.getName(), userId);
        cache();
    }

    public void updateIronBoots(IronBoots boots) {
        this.boots = boots;
        if (boots != null) Withering.getBot().getDatabase()
                .update("UPDATE armoriron SET ironboots=? WHERE userId=?;", boots.getName(), userId);
        cache();
    }

    public void updateIronWeapon(IronWeapon weapon) {
        this.weapon = weapon;
        Withering.getBot().getDatabase().update("UPDATE armoriron SET ironweapon=? WHERE userId=?;", weapon.getName(), userId);
        cache();
    }

    public void updateIronShield(IronShield shield) {
        this.shield = shield;
        String sh = shield == null ? "" : shield.getName();
        Withering.getBot().getDatabase().update("UPDATE armoriron SET ironshield=? WHERE userId=?", sh, userId);
        cache();
    }

    public String getUserId() {
        return userId;
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ArmorIronUser getArmorIronUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ArmorIronUser(userId);
    }
}