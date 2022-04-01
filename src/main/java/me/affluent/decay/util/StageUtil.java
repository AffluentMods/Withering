package me.affluent.decay.util;

import me.affluent.decay.Withering;
import net.dv8tion.jda.internal.utils.Checks;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StageUtil {

    public static int getCurrentStage(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT stage FROM holidayevent WHERE userId=?;", userId)) {
            if (rs.next()) {
                return rs.getInt("stage");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static void setStage(String userId, int stage) {
        Withering.getBot().getDatabase()
                .update("DELETE FROM holidayStage WHERE userId=?;", userId);
        Withering.getBot().getDatabase()
                .update("INSERT INTO holidayStage VALUES (?, ?);", userId, stage);
    }
}
