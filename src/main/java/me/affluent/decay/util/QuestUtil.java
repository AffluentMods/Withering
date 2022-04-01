package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.database.Database;
import net.dv8tion.jda.internal.utils.Checks;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestUtil {

    public static int getCurrentChapter(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT chapter FROM journal WHERE userId=?;", userId)) {
            while (rs.next()) {
                return rs.getInt("chapter");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static int getCurrentQuest(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT quest FROM journal WHERE userId=?;", userId)) {
            while (rs.next()) {
                return rs.getInt("quest");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static int getTotalQuests(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT totalQuests FROM journal WHERE userId=?;", userId)) {
            while (rs.next()) {
                return rs.getInt("totalQuests");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static void setJournal(String userId, int chapter, int quest) {
        Checks.notNull(userId, "userId");
        Checks.check(chapter > 0, "error at setJournal - chapter may be bigger than 0");
        Checks.check(quest > 0, "error at setJournal - quest may be bigger than 0");
        int totalQuests = ((chapter - 1 ) * 10) + quest;
        Database db = Withering.getBot().getDatabase();
        db.update("DELETE FROM journal WHERE userId=?;", userId);
        db.update("INSERT INTO journal VALUES (?, ?, ?, ?);", userId, chapter, quest, totalQuests);
    }
}