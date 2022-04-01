package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.armor.*;
import me.affluent.decay.entity.otherInventory.ArmorDragonUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.entity.otherInventory.ArmorWitherUser;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArmorUser {

    private static HashMap<String, ArmorUser> cache = new HashMap<>();

    private final String userId;
    private long helmetID;
    private Helmet helmet;
    private Chestplate chestplate;
    private long chestplateID;
    private Gloves gloves;
    private long glovesID;
    private Trousers trousers;
    private long trousersID;
    private Boots boots;
    private long bootsID;
    private Weapon weapon;
    private long weaponID;
    private Arrow arrow;
    private long arrowID;
    private Shield shield;
    private long shieldID;

    public static void clearCache() {
        cache.clear();
    }

    private ArmorUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM armor WHERE userId=?;", userId)) {
            if (rs.next()) {
                this.helmet = Helmet.getHelmet(rs.getString("helmet"));
                this.helmetID = rs.getInt("helmetID");
                this.chestplate = Chestplate.getChestplate(rs.getString("chestplate"));
                this.chestplateID = rs.getInt("chestplateID");
                this.gloves = Gloves.getGloves(rs.getString("gloves"));
                this.glovesID = rs.getInt("glovesID");
                this.trousers = Trousers.getTrousers(rs.getString("trousers"));
                this.trousersID = rs.getInt("trousersID");
                this.boots = Boots.getBoots(rs.getString("boots"));
                this.bootsID = rs.getInt("bootsID");
                this.weapon = Weapon.getWeapon(rs.getString("weapon"));
                this.weaponID = rs.getInt("weaponID");
                this.arrow = Arrow.getArrow(rs.getString("arrow"));
                this.arrowID = rs.getInt("arrowID");
                this.shield = Shield.getShield(rs.getString("shield"));
                this.shieldID = rs.getInt("shieldID");
            } else {
                this.helmet = null;
                this.helmetID = -1;
                this.chestplate = null;
                this.chestplateID = -1;
                this.gloves = null;
                this.glovesID = -1;
                this.trousers = null;
                this.trousersID = -1;
                this.boots = null;
                this.bootsID = -1;
                int id = 0;
                String itemName = "junk wood sword";
                try (PreparedStatement ps = Withering.getBot().getDatabase().getCon()
                        .prepareStatement("INSERT INTO inventory (userId, itemName) VALUES (?, ?);",
                                Statement.RETURN_GENERATED_KEYS)) {
                    ps.setObject(1, userId);
                    ps.setObject(2, itemName);
                    ps.executeUpdate();
                    ResultSet rs2 = ps.getGeneratedKeys();
                    if (rs2.next()) {
                        id = rs2.getInt(1);
                        Withering.getBot().getDatabase()
                                .update("DELETE FROM inventory WHERE userId=? AND ID=?;", userId, id);
                        cache();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                this.weapon = Weapon.getWeapon("junk wood sword");
                this.weaponID = id;
                this.arrow = null;
                this.arrowID = -1;
                this.shield = null;
                this.shieldID = -1;

                Withering.getBot().getDatabase()
                        .update("INSERT INTO armor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", userId, "", -1, "", -1, "", -1, "", -1, "", -1,
                                itemName, id, "", -1, "", -1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Armor> getAllArmor() {
        List<Armor> armorList = new ArrayList<>();
        Helmet h = getHelmet();
        Chestplate c = getChestplate();
        Gloves g = getGloves();
        Trousers t = getTrousers();
        Boots b = getBoots();
        if (h != null) armorList.add(h);
        if (c != null) armorList.add(c);
        if (g != null) armorList.add(g);
        if (t != null) armorList.add(t);
        if (b != null) armorList.add(b);
        return armorList;
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public String getHelmetID() {
        return String.valueOf(helmetID);
    }

    public Chestplate getChestplate() {
        return chestplate;
    }

    public String getChestplateID() {
        return String.valueOf(chestplateID);
    }

    public Gloves getGloves() {
        return gloves;
    }

    public String getGlovesID() {
        return String.valueOf(glovesID);
    }

    public Trousers getTrousers() {
        return trousers;
    }

    public String getTrousersID() {
        return String.valueOf(trousersID);
    }

    public Boots getBoots() {
        return boots;
    }

    public String getBootsID() {
        return String.valueOf(bootsID);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getWeaponID() {
        return String.valueOf(weaponID);
    }

    public Arrow getArrow() {
        return arrow;
    }

    public String getArrowID() {
        return String.valueOf(arrowID);
    }

    public Shield getShield() {
        return shield;
    }

    public String getShieldID() {
        return String.valueOf(shieldID);
    }

    public int getAllProtection() {
        int prot = 0;
        if (helmet != null) {
            String id = getHelmetID();
            int level = ItemLevelingUtil.getItemLevel(userId, Long.parseLong(id));
            int stars = ItemStarringUtil.getItemStar(userId, Long.parseLong(id));
            prot = helmet.getProtection();
            prot += getLevelProtection(helmet.getProtection(), level);
            prot += getStarsProtection(helmet.getProtection(), stars);
        }
        if (chestplate != null) {
            String id = getChestplateID();
            int level = ItemLevelingUtil.getItemLevel(userId, Long.parseLong(id));
            int stars = ItemStarringUtil.getItemStar(userId, Long.parseLong(id));
            prot = chestplate.getProtection();
            prot += getLevelProtection(chestplate.getProtection(), level);
            prot += getStarsProtection(chestplate.getProtection(), stars);
        }
        if (gloves != null) {
            String id = getGlovesID();
            int level = ItemLevelingUtil.getItemLevel(userId, Long.parseLong(id));
            int stars = ItemStarringUtil.getItemStar(userId, Long.parseLong(id));
            prot = gloves.getProtection();
            prot += getLevelProtection(gloves.getProtection(), level);
            prot += getStarsProtection(gloves.getProtection(), stars);
        }
        if (trousers != null) {
            String id = getTrousersID();
            int level = ItemLevelingUtil.getItemLevel(userId, Long.parseLong(id));
            int stars = ItemStarringUtil.getItemStar(userId, Long.parseLong(id));
            prot = trousers.getProtection();
            prot += getLevelProtection(trousers.getProtection(), level);
            prot += getStarsProtection(trousers.getProtection(), stars);
        }
        if (boots != null) {
            String id = getBootsID();
            int level = ItemLevelingUtil.getItemLevel(userId, Long.parseLong(id));
            int stars = ItemStarringUtil.getItemStar(userId, Long.parseLong(id));
            prot = boots.getProtection();
            prot += getLevelProtection(boots.getProtection(), level);
            prot += getStarsProtection(boots.getProtection(), stars);
        }
        prot += ArmorIronUser.getArmorIronUser(userId).getAllProtection();
        prot += ArmorDragonUser.getArmorDragonUser(userId).getAllProtection();
        prot += ArmorWitherUser.getArmorWitherUser(userId).getAllProtection();
        return prot;
    }

    public void updateHelmet(Long ID, Helmet helmet) {
        this.helmet = helmet;
        this.helmetID = ID;
        String he = helmet == null ? "" : helmet.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET helmet=?, helmetID=? WHERE userId=?;", he, ID, userId);
        cache();
    }

    public void updateChestplate(Long ID, Chestplate chestplate) {
        this.chestplate = chestplate;
        this.chestplateID = ID;
        String ch = chestplate == null ? "" : chestplate.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET chestplate=?, chestplateID=? WHERE userId=?;", ch, ID, userId);
        cache();
    }

    public void updateGloves(Long ID, Gloves gloves) {
        this.gloves = gloves;
        this.glovesID = ID;
        String gl = gloves == null ? "" : gloves.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET gloves=?, glovesID=? WHERE userId=?;", gl, ID, userId);
        cache();
    }

    public void updateTrousers(Long ID, Trousers trousers) {
        this.trousers = trousers;
        this.trousersID = ID;
        String tr = trousers == null ? "" : trousers.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET trousers=?, trousersID=? WHERE userId=?;", tr, ID, userId);
        cache();
    }

    public void updateBoots(Long ID, Boots boots) {
        this.boots = boots;
        this.bootsID = ID;
        String bo = boots == null ? "" : boots.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET boots=?, bootsID=? WHERE userId=?;", bo, ID, userId);
        cache();
    }

    public void updateWeapon(Long ID, Weapon weapon) {
        this.weapon = weapon;
        this.weaponID = ID;
        Withering.getBot().getDatabase().update("UPDATE armor SET weapon=?, weaponID=? WHERE userId=?;", weapon.getName(), ID, userId);
        cache();
    }

    public void updateArrow(Long ID, Arrow arrow) {
        this.arrow = arrow;
        this.arrowID = ID;
        String an = arrow == null ? "" : arrow.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET arrow=?, arrowID=? WHERE userId=?;", an, ID, userId);
        cache();
    }

    public void updateShield(Long ID, Shield shield) {
        this.shield = shield;
        this.shieldID = ID;
        String sh = shield == null ? "" : shield.getName();
        Withering.getBot().getDatabase().update("UPDATE armor SET shield=?, shieldID=? WHERE userId=?", sh, ID, userId);
        cache();
    }

    public String getUserId() {
        return userId;
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static ArmorUser getArmorUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new ArmorUser(userId);
    }

    public static long getLevelProtection(int baseProtection, int level) {
        int finalProtection = 0;
        int protection = baseProtection;
        for (int i = 1; i < level + 1; i++) {
            protection *= (1.00725);
        }
        finalProtection = (protection - baseProtection);
        return finalProtection;
    }

    public static long getStarsProtection(int baseProtection, int stars) {
        int finalProtection = 0;
        int protection = baseProtection;
        for (int i = 1; i < stars + 1; i++) {
            protection *= (1.045);
        }
        finalProtection = (protection - baseProtection);
        return finalProtection;
    }
}