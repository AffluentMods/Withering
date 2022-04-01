package me.affluent.decay.util.system;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class FireworkSystem {

    private static final HashMap<String, Long> activeFireworks = new HashMap<>();

    public static void load() {
        long now = System.currentTimeMillis();
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM fireworks;")) {
            while (rs.next()) {
                String uid = rs.getString("userId");
                long until = rs.getLong("until");
                if (until <= now) {
                    Withering.getBot().getDatabase().update("DELETE FROM fireworks WHERE userId=?;", uid);
                } else {
                    long diff = until - now;
                    activeFireworks.put(uid, until);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            activeFireworks.remove(uid, until);
                            Withering.getBot().getDatabase().update("DELETE FROM fireworks WHERE userId=?;", uid);
                        }
                    }, diff);
                }
            }
        }  catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void addFirework(String uid, long until) {
        activeFireworks.put(uid, until);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activeFireworks.remove(uid, until);
            }
        }, until - System.currentTimeMillis());
    }

    public static boolean hasFirework(String uid) {
        return activeFireworks.getOrDefault(uid, -1L) > System.currentTimeMillis();
    }

    public static long getFireworkTime(String uid) {
        return activeFireworks.getOrDefault(uid, -1L);
    }
}
