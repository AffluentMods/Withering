package me.affluent.decay.patreon;

import me.affluent.decay.Withering;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PatreonSystem {

    private static final HashMap<String, PatreonPledger> pledgers = new HashMap<>();

    public static void load() {
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS patreonsystem (ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                        + "userId VARCHAR(24) NOT NULL, pledge VARCHAR(64) NOT NULL, end VARCHAR(32) NOT "
                        + "NULL);");
        //final long now = System.currentTimeMillis();
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM patreonsystem;")) {
            while (rs.next()) {
                PatreonPledger pledger = new PatreonPledger(
                        rs.getString("userId"), rs.getString("pledge"), Long.parseLong(rs.getString("end")));
                pledgers.put(pledger.getUserId(), pledger);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static HashMap<String, PatreonPledger> getPledgers() {
        return pledgers;
    }
}
