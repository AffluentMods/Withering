package me.affluent.decay.command.admin;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.database.Database;
import me.affluent.decay.entity.*;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DevCommand extends BotCommand {

    public DevCommand() {
        this.name = "dev";
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (args.length < 1) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "dev <sql | ...>`")));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("stats")) {
            OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
            double avgSystemLoad = os.getSystemLoadAverage();
            MemoryMXBean mm = ManagementFactory.getMemoryMXBean();
            MemoryUsage heap = mm.getHeapMemoryUsage();
            long heapUsed = heap.getUsed() / 1000 / 1000;
            long heapMax = heap.getMax() / 1000 / 1000;
            double avg_ping = Withering.getBot().getShardManager().getAverageGatewayPing();
            int activeThreads = Thread.activeCount();
            long now = System.currentTimeMillis();
            long started = now - Withering.getStarted();
            int sh = 0;
            int sm = 0;
            int ss = 0;
            while (started >= 1000) {
                started -= 1000;
                ss++;
            }
            while (ss >= 60) {
                ss -= 60;
                sm++;
            }
            while (sm >= 60) {
                sm -= 60;
                sh++;
            }
            long loaded = now - Withering.getLoaded();
            int lh = 0;
            int lm = 0;
            int ls = 0;
            while (loaded >= 1000) {
                loaded -= 1000;
                ls++;
            }
            while (ls >= 60) {
                ls -= 60;
                lm++;
            }
            while (lm >= 60) {
                lm -= 60;
                lh++;
            }
            String startedString = sh + "h, " + sm + "min and " + ss + " second(s) ago";
            String loadedString = lh + "h, " + lm + "min and " + ls + " second(s) ago";

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Withering Stats");
            Thread thread = Thread.currentThread();
            eb.addField("Thread", thread.getName() + " - " + thread.getThreadGroup().getName(), true);
            eb.addField("Average Gateway Ping // Last Database Ping", avg_ping + "ms // " + Database.ping + "ms", true);
            eb.addField("OS - System Load", avgSystemLoad + " average", true);
            eb.addField("Heap Memory", heapUsed + "M / " + heapMax + "M used", true);
            eb.addField("Threads", activeThreads + " active threads", true);
            eb.addField("SQL Stats", Database.updates + " updates and " + Database.queries + " queries", true);
            eb.addField("Uptime", "Bot started " + startedString + "\nBot loaded " + loadedString, true);
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            return;
        }
        if (arg.equalsIgnoreCase("fixweapon")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId FROM profiles;")) {
                while (rs.next()) {
                    String userId = rs.getString("userId");
                    try (ResultSet rs1 = Withering.getBot().getDatabase()
                            .query("SELECT weapon FROM armor WHERE userId=?;", userId)) {
                        if (rs1.next()) {
                            if (rs1.getString("weapon").equalsIgnoreCase("")) Withering.getBot().getDatabase()
                                    .update("UPDATE armor SET weapon='junk wood sword' WHERE userId=?;", userId);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (arg.equalsIgnoreCase("sql")) {
            String sql = "";
            for (int i = 1; i < args.length; i++) {
                sql += args[i] + " ";
            }
            Withering.getBot().getDatabase().update(sql);
            e.reply(MessageUtil.success("SQL", "Executed " + sql));
            return;
        }
        if (arg.equalsIgnoreCase("clearcache")) {
            InventoryUser.clearCache();
            HealthUser.clearCache();
            ArmorUser.clearCache();
            ExpUser.clearCache();
            EmoteUtil.clearCache();
            e.reply(MessageUtil.success("Development", "Cache cleared"));
            return;
        }
        e.reply(MessageUtil.err("Error", "Invalid argument"));
    }
}