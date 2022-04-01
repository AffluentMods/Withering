package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.util.system.FormatUtil;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class EcoUser {

    private static HashMap<String, EcoUser> cache = new HashMap<>();

    private final String userId;
    private BigInteger balance;

    private EcoUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT balance FROM economy WHERE userId=?;", userId)) {
            if (rs.next()) {
                balance = new BigInteger(rs.getString("balance"));
            } else {
                balance = new BigInteger("0");
                Withering.getBot().getDatabase()
                        .update("INSERT INTO economy VALUES (?, ?);", userId, balance.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void removeBalance(long amount) {
        removeBalance(new BigInteger(String.valueOf(amount)));
    }

    public void removeBalance(BigInteger amount) {
        setBalance(this.balance.subtract(amount));
    }

    public void addBalance(BigInteger amount) {
        setBalance(this.balance.add(amount));
    }

    public void addBalance(long amount) {
        addBalance(new BigInteger(String.valueOf(amount)));
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
        cache();
        Withering.getBot().getDatabase()
                .update("UPDATE economy SET balance=? WHERE userId=?;", balance.toString(), userId);
    }

    public String getBalanceAbr() {
        return FormatUtil.formatAbbreviated(this.balance.toString());
    }

    public String getBalanceCom() {
        return FormatUtil.formatCommas(this.balance.toString());
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static EcoUser getEcoUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new EcoUser(userId);
    }

}