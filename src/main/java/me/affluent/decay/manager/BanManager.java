package me.affluent.decay.manager;

import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.util.system.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class BanManager {

    public static void checkBans() {
        try {
            if (Withering.test) return;
            ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM bans;");
            while (rs.next()) isBanned(rs.getString("userId"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void unban(String uid) {
        Withering.getBot().getDatabase().update("DELETE FROM bans WHERE userId=?;", uid);
    }

    public static boolean isBanned(String uid) {
        if (Withering.test) return false;
        try {
            ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM bans WHERE userId=?;", uid);
            if (rs.next()) {
                boolean banned = FormatUtil.getNow()
                        .before(Objects.requireNonNull(FormatUtil.fromString(rs.getString("todate"))));
                if (!banned) Withering.getBot().getDatabase().update("DELETE FROM bans WHERE userId=?;", uid);
                return banned;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void ban(String uid, Date when, String reason, String by) {
        String name = "User not found#0000 - null";
        User user = Player.getUser(uid);
        if (user != null) name = user.getName() + "#" + user.getDiscriminator();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Banned user");
        eb.setColor(Color.RED);
        eb.addField("User ID", uid, true);
        eb.addField("Name", name, true);
        eb.addField("Banned by", by, true);
        eb.addField("Time", "From: " + FormatUtil.fromDate(FormatUtil.getNow()) + "\nTo: " + FormatUtil.fromDate(when),
                true);
        eb.addField("Reason", reason, true);
        Withering.getModLog().sendMessage(eb.build()).queue();
        Withering.getBot().getDatabase()
                .update("INSERT INTO bans VALUES ('" + uid + "', '" + FormatUtil.fromDate(when) + "');");
    }
}