package me.affluent.decay.database;

import java.sql.*;

public class Database {

    private Connection con;
    public static long updates = 0;
    public static long queries = 0;

    public Database() {
        mySQL();
    }

    private void mySQL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String password = "EuN6$76SVPi!$j&pp&";
            String database = "withering";
            String ip = "localhost";
            con = DriverManager
                    .getConnection("jdbc:mysql://" + ip + ":3306/" + database + "?autoReconnect=true", "botuser",
                            password);
            System.out.println("[INTERN INFO] Database connection initialized to " + ip + "/" + database);
            setup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void ct(String tn, String ts) {
        update("CREATE TABLE IF NOT EXISTS " + tn + " (" + ts + ");");
    }

    private void setup() {
        ct("profiles",
                "ID INT PRIMARY KEY AUTO_INCREMENT, userId VARCHAR(64) NOT NULL, prefix VARCHAR(4) NOT NULL, language" +
                " VARCHAR(6) NOT NULL, gender int(3) NOT NULL, clan int NOT NULL");
        ct("health", "userId VARCHAR(64) NOT NULL, health int NOT NULL, maxhealth int NOT NULL DEFAULT 10");
        ct("exp", "userId VARCHAR(64) NOT NULL, level bigint(64) NOT NULL, experience VARCHAR(128) NOT NULL");
        ct("petexp", "ID BIGINT(64) NOT NULL, userId VARCHAR(64) NOT NULL, petLevel bigint(64) NOT NULL, petExperience VARCHAR(128) NOT NULL");
        ct("elexp",
                "userId VARCHAR(64) NOT NULL, elixirLevel bigint(64) NOT NULL, elixirExperience VARCHAR(128) NOT NULL");
        ct("armor",
                "userId VARCHAR(64) NOT NULL, helmet VARCHAR(46) NOT NULL, helmetID BIGINT(64) NOT NULL, chestplate VARCHAR(46) " +
                "NOT NULL, chestplateID BIGINT(64) NOT NULL, gloves VARCHAR(46) NOT NULL, glovesID BIGINT(64) NOT NULL, trousers VARCHAR(46) NOT NULL, " +
                        "trousersID BIGINT(64) NOT NULL, boots VARCHAR(46) NOT NULL, bootsID BIGINT(64) NOT NULL, weapon VARCHAR(46) NOT NULL, " +
                        "weaponID BIGINT(64) NOT NULL, arrow VARCHAR(46) NOT NULL, arrowID BIGINT(64) NOT NULL, shield VARCHAR(46) NOT NULL, shieldID BIGINT(64) NOT NULL");
        ct("equippedPet",
                "userId VARCHAR(64) NOT NULL, pet VARCHAR(46) NOT NULL, petID BIGINT(64) NOT NULL");
        ct("armoriron",
                "userId VARCHAR(64) NOT NULL, ironhelmet VARCHAR(46) NOT NULL, ironchestplate VARCHAR(46) NOT NULL, irongloves " +
                        "VARCHAR(46) NOT NULL, irontrousers VARCHAR(46) NOT NULL, ironboots VARCHAR(46) NOT NULL, ironweapon VARCHAR(46) " +
                        "NOT NULL, ironshield VARCHAR(46) NOT NULL");
        ct("armordragon",
                "userId VARCHAR(64) NOT NULL, dragonhelmet VARCHAR(46) NOT NULL, dragonchestplate VARCHAR(46) NOT NULL, dragongloves " +
                        "VARCHAR(46) NOT NULL, dragontrousers VARCHAR(46) NOT NULL, dragonboots VARCHAR(46) NOT NULL, dragonweapon VARCHAR(46) " +
                        "NOT NULL, dragonshield VARCHAR(46) NOT NULL");
        ct("armorwither",
                "userId VARCHAR(64) NOT NULL, witherhelmet VARCHAR(46) NOT NULL, witherchestplate VARCHAR(46) NOT NULL, withergloves " +
                        "VARCHAR(46) NOT NULL, withertrousers VARCHAR(46) NOT NULL, witherboots VARCHAR(46) NOT NULL, witherweapon VARCHAR(46) " +
                        "NOT NULL, withershield VARCHAR(46) NOT NULL");
        ct("inventory",
                "ID BIGINT(64) PRIMARY KEY AUTO_INCREMENT, userId VARCHAR(64) NOT NULL, itemName VARCHAR(76) NOT " +
                "NULL");
        ct("itemStats",
                "ID BIGINT(64) NOT NULL, userId VARCHAR(64) NOT NULL, itemLevel bigint(64) NOT NULL, itemStars INT NOT NULL, gemName VARCHAR(16) NOT NULL," +
                        "gemLevel INT NOT NULL, lockValue int(2) NOT NULL");
        ct("petStats",
                "ID BIGINT(64) NOT NULL, userId VARCHAR(64) NOT NULL, petStars INT NOT NULL");
        ct("economy", "userId VARCHAR(64) NOT NULL, balance VARCHAR(128) NOT NULL");
        ct("playerrank", "userId VARCHAR(64) NOT NULL, playerrank VARCHAR(32) NOT NULL");
        ct("cooldowns",
                "userId VARCHAR(64) NOT NULL, cooldownName VARCHAR(64) NOT NULL, cooldownEnd VARCHAR(48) NOT NULL");
        ct("stats",
                "userId VARCHAR(64) NOT NULL, statistic_name VARCHAR(64) NOT NULL, statistic_value VARCHAR(128) NOT " +
                "NULL");
        ct("donatorRoleRewards", "bundle int NOT NULL, rw_item VARCHAR(64) NOT NULL, rw_amount BIGINT(32) NOT NULL");
        ct("userprefix", "userId VARCHAR(64) NOT NULL, prefix VARCHAR(4) NOT NULL");
        ct("userbadges", "userId VARCHAR(64) NOT NULL, badge VARCHAR(16) NOT NULL");
        ct("boosts", "userId VARCHAR(64) NOT NULL, boostName VARCHAR(64) NOT NULL, boostValue VARCHAR(16) NOT NULL, " +
                     "boostValueDisplay VARCHAR(32) NOT NULL, boostEnd VARCHAR(48) NOT NULL");
        ct("diminishings",
                "userId VARCHAR(64) NOT NULL, targetId VARCHAR(64) NOT NULL, count INT NOT NULL, until bigint(24) NOT" +
                " NULL");
        ct("journal", "userId VARCHAR(64) NOT NULL, chapter INT NOT NULL, quest INT NOT NULL, totalQuests INT NOT NULL");
        ct("userkeys", "userId VARCHAR(64) NOT NULL, keytype VARCHAR(16) NOT NULL, userkeys INT NOT NULL");
        ct("diamonds", "userId VARCHAR(64) NOT NULL, diamonds BIGINT(32) NOT NULL");
        ct("ingots", "userId VARCHAR(64) NOT NULL, ingots INT NOT NULL");
        ct("artifacts", "userId VARCHAR(64) NOT NULL, artifactName VARCHAR(24) NOT NULL, active int(2) NOT NULL");
        ct("codes",
                "userId VARCHAR(64) NOT NULL, code VARCHAR(16) NOT NULL");
        ct("achievements",
                "userId VARCHAR(64) NOT NULL, achievementName VARCHAR(64) NOT NULL, achievementTier INT NOT NULL");
        ct("power",
                "userId VARCHAR(64) NOT NULL, totalPower INT NOT NULL");
        ct("response",
                "userId VARCHAR(64) NOT NULL, responseValue VARCHAR(8) NOT NULL");
        ct("confirm",
                "userId VARCHAR(64) NOT NULL, confirmValue VARCHAR(8) NOT NULL");
        ct("bar",
                "userId VARCHAR(64) NOT NULL, barValue VARCHAR(8) NOT NULL");
        ct("ignoreAchievement",
                "userId VARCHAR(64) NOT NULL, ignoreValue VARCHAR(8) NOT NULL");
        ct("equippedIron",
                "userId VARCHAR(64) NOT NULL, ironhelmet VARCHAR(10) NOT NULL, ironchestplate VARCHAR(10) NOT NULL, irongloves " +
                        "VARCHAR(10) NOT NULL, irontrousers VARCHAR(10) NOT NULL, ironboots VARCHAR(10) NOT NULL, ironsword VARCHAR(10) " +
                        "NOT NULL, ironshield VARCHAR(10) NOT NULL");
        ct("equippedDragon",
                "userId VARCHAR(64) NOT NULL, dragonhelmet VARCHAR(10) NOT NULL, dragonchestplate VARCHAR(10) NOT NULL, dragongloves " +
                        "VARCHAR(10) NOT NULL, dragontrousers VARCHAR(10) NOT NULL, dragonboots VARCHAR(10) NOT NULL, dragonsword VARCHAR(10) " +
                        "NOT NULL, dragonshield VARCHAR(10) NOT NULL");
        ct("equippedWither",
                "userId VARCHAR(64) NOT NULL, witherhelmet VARCHAR(10) NOT NULL, witherchestplate VARCHAR(10) NOT NULL, withergloves " +
                        "VARCHAR(10) NOT NULL, withertrousers VARCHAR(10) NOT NULL, witherboots VARCHAR(46) NOT NULL, withersword VARCHAR(10) " +
                        "NOT NULL, withershield VARCHAR(10) NOT NULL");
        ct("ironScrolls",
                "userId VARCHAR(64) NOT NULL, amount INT NOT NULL");
        ct("dragonScrolls",
                "userId VARCHAR(64) NOT NULL, amount INT NOT NULL");
        ct("witherScrolls",
                "userId VARCHAR(64) NOT NULL, amount INT NOT NULL");
        ct("orbs",
                "userId VARCHAR(64) NOT NULL, amount INT NOT NULL");
        ct("holidayItems",
                "userId VARCHAR(64) NOT NULL, candyCorn INT NOT NULL, purpleCandy INT NOT NULL, candyCane INT NOT NULL, presents INT NOT NULL, fireworks INT NOT NULL");
        ct("ironman",
                "userId VARCHAR(64) NOT NULL, value VARCHAR(8) NOT NULL");
        ct("gemDust",
                "userId VARCHAR(64) NOT NULL, amount INT NOT NULL");
        ct("pets",
                "ID BIGINT(64) PRIMARY KEY AUTO_INCREMENT, userId VARCHAR(64) NOT NULL, petName VARCHAR(64) NOT NULL");
        ct("tutorialRewards",
                "userId VARCHAR(64) NOT NULL, chestReward VARCHAR(10) NOT NULL, finalReward VARCHAR(10) NOT NULL");
        //
        ct("tutorial",
                "userId VARCHAR(64) NOT NULL, initialReward VARCHAR(16) NOT NULL, finalReward VARCHAR(16) NOT NULL");
        ct("bans", "userId VARCHAR(64) NOT NULL, todate VARCHAR(64) NOT NULL");
        ct("votes", "userId VARCHAR(64) NOT NULL, until bigint(24) NOT NULL");
        ct("fireworks", "userId VARCHAR(64) NOT NULL, until bigint(24) NOT NULL");
        ct("gameTime", "minute VARCHAR(8) NOT NULL, hour VARCHAR(8) NOT NULL, day VARCHAR(8) NOT NULL, month VARCHAR(8) NOT NULL, year VARCHAR(8) NOT NULL");
        ct("lang_messages",
                "ID INT PRIMARY KEY AUTO_INCREMENT, msgID VARCHAR(32) NOT NULL, langCode VARCHAR(6) NOT NULL, message" +
                        " VARCHAR(2048) NOT NULL");
    }

    public static long ping = -1L;

    private void updatePing(long now) {
        ping = System.currentTimeMillis() - now;
    }

    public ResultSet query(String sql, Object... parameterObjects) {
        queries++;
        try {
            long now = System.currentTimeMillis();
            PreparedStatement ps = con.prepareStatement(sql);
            int parameterIndex = 1;
            for (Object o : parameterObjects) {
                ps.setObject(parameterIndex, o);
                parameterIndex++;
            }
            ResultSet rs = ps.executeQuery();
            updatePing(now);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(String sql, Object... parameterObjects) {
        updates++;
        try {
            long now = System.currentTimeMillis();
            PreparedStatement ps = con.prepareStatement(sql);
            int parameterIndex = 1;
            for (Object o : parameterObjects) {
                ps.setObject(parameterIndex, o);
                parameterIndex++;
            }
            ps.executeUpdate();
            updatePing(now);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getCon() {
        return con;
    }
}