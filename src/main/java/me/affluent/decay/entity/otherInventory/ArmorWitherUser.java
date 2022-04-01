package me.affluent.decay.entity.otherInventory;

import me.affluent.decay.Withering;
import me.affluent.decay.armor.*;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherWeapon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArmorWitherUser {

    private static HashMap<String, ArmorWitherUser> cache = new HashMap<>();

    private final String userId;
    private WitherHelmet helmet;
    private WitherChestplate chestplate;
    private WitherGloves gloves;
    private WitherTrousers trousers;
    private WitherBoots boots;
    private WitherWeapon weapon;
    private WitherShield shield;

    public static void clearCache() {
        cache.clear();
    }

    private ArmorWitherUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM armorwither WHERE userId=?;", userId)) {
            if (rs.next()) {
                this.helmet = WitherHelmet.getWitherHelmet(rs.getString("witherhelmet"));
                this.chestplate = WitherChestplate.getWitherChestplate(rs.getString("witherchestplate"));
                this.gloves = WitherGloves.getWitherGloves(rs.getString("withergloves"));
                this.trousers = WitherTrousers.getWitherTrousers(rs.getString("withertrousers"));
                this.boots = WitherBoots.getWitherBoots(rs.getString("witherboots"));
                this.weapon = WitherWeapon.getWeapon(rs.getString("witherweapon"));
                this.shield = WitherShield.getWitherShield(rs.getString("withershield"));
            } else {
                this.helmet = null;
                this.chestplate = null;
                this.gloves = null;
                this.trousers = null;
                this.boots = null;
                this.weapon = null;
                this.shield = null;
                Withering.getBot().getDatabase()
                        .update("INSERT INTO armorwither VALUES (?, ?, ?, ?, ?, ?, ?, ?);", userId, "", "", "", "", "",
                                "", "");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Armor> getAllArmor() {
        List<Armor> armorList = new ArrayList<>();
        WitherHelmet h = getWitherHelmet();
        WitherChestplate c = getWitherChestplate();
        WitherGloves g = getWitherGloves();
        WitherTrousers t = getWitherTrousers();
        WitherBoots b = getWitherBoots();
        if (h != null) armorList.add(h);
        if (c != null) armorList.add(c);
        if (g != null) armorList.add(g);
        if (t != null) armorList.add(t);
        if (b != null) armorList.add(b);
        return armorList;
    }

    public WitherHelmet getWitherHelmet() {
        return helmet;
    }

    public WitherChestplate getWitherChestplate() {
        return chestplate;
    }

    public WitherGloves getWitherGloves() {
        return gloves;
    }

    public WitherTrousers getWitherTrousers() {
        return trousers;
    }

    public WitherBoots getWitherBoots() {
        return boots;
    }

    public WitherWeapon getWitherWeapon() {
        return weapon;
    }

    public WitherShield getWitherShield() {
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

    public void updateWitherHelmet(WitherHelmet helmet) {
        this.helmet = helmet;
        if (helmet != null) Withering.getBot().getDatabase()
                .update("UPDATE armorwither SET witherhelmet=? WHERE userId=?;", helmet.getName(), userId);
        cache();
    }

    public void updateWitherChestplate(WitherChestplate chestplate) {
        this.chestplate = chestplate;
        if (chestplate != null) Withering.getBot().getDatabase()
                .update("UPDATE armorwither SET witherchestplate=? WHERE userId=?;", chestplate.getName(), userId);
        cache();
    }

    public void updateWitherGloves(WitherGloves gloves) {
        this.gloves = gloves;
        if (gloves != null) Withering.getBot().getDatabase()
                .update("UPDATE armorwither SET withergloves=? WHERE userId=?;", gloves.getName(), userId);
        cache();
    }

    public void updateWitherTrousers(WitherTrousers trousers) {
        this.trousers = trousers;
        if (trousers != null) Withering.getBot().getDatabase()
                .update("UPDATE armorwither SET withertrousers=? WHERE userId=?;", trousers.getName(), userId);
        cache();
    }

    public void updateWitherBoots(WitherBoots boots) {
        this.boots = boots;
        if (boots != null) Withering.getBot().getDatabase()
                .update("UPDATE armorwither SET witherboots=? WHERE userId=?;", boots.getName(), userId);
        cache();
    }

    public void updateWitherWeapon(WitherWeapon weapon) {
        this.weapon = weapon;
        Withering.getBot().getDatabase().update("UPDATE armorwither SET witherweapon=? WHERE userId=?;", weapon.getName(), userId);
        cache();
    }

    public void updateWitherShield(WitherShield shield) {
        this.shield = shield;
        String sh = shield == null ? "" : shield.getName();
        Withering.getBot().getDatabase().update("UPDATE armorwither SET withershield=? WHERE userId=?", sh, userId);
        cache();
    }

    public String getUserId() {
        return userId;
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ArmorWitherUser getArmorWitherUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ArmorWitherUser(userId);
    }
}
