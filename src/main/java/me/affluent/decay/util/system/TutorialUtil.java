package me.affluent.decay.util.system;

import me.affluent.decay.Withering;
import me.affluent.decay.command.information.TutorialCommand;
import me.affluent.decay.manager.EventManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TutorialUtil {

    public static void load() {
        EventManager.registerListener(new TutorialCommand());
    }

    public static String getInitialReward(String uid) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT initialReward FROM tutorial WHERE userId=?;", uid)) {
            if (rs.next()) {
                return rs.getString("initialReward");
            } else {
                return "unclaimed";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "unclaimed";
    }

    public static void setInitialReward(String uid, String reward) {
       Withering.getBot().getDatabase().update("DELETE FROM tutorial WHERE userId=?", uid);
       Withering.getBot().getDatabase().update("INSERT INTO tutorial VALUES (?, ?, ?);", uid, reward, "unclaimed");
    }

    public static String getFinalReward(String uid) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT finalReward FROM tutorial WHERE userId=?;", uid)) {
            if (rs.next()) {
                return rs.getString("finalReward");
            } else {
                return "unclaimed";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "unclaimed";
    }

    public static void setFinalReward(String uid, String reward) {
        Withering.getBot().getDatabase().update("DELETE FROM tutorial WHERE userId=?", uid);
        Withering.getBot().getDatabase().update("INSERT INTO tutorial VALUES (?, ?, ?);", uid, "claimed", reward);
    }
}
