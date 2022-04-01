package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.ExpUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.IngotUtil;
import me.affluent.decay.util.IronmanUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.StatsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopCommand extends BotCommand {

    public TopCommand() {
        this.name = "top";
        this.cooldown = 2.5;
        this.aliases = new String[]{"leaderboard", "topboard", "lb"};
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.\n`<>` - Required Parameter\n`[]` - Optional Parameter").replace("{command_usage}",
                            "`" + userPrefix + "top <conquest | diamonds | daily | event | holiday | kd | level | money | power> [page (1-10)]`")));
            return;
        }
        String arg = args[0].toLowerCase();
        int page;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1 || page > 10) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "argument_between",
                            "The argument {argument} has to be between {min} and {max}!")
                            .replace("{argument}", "`<page>`").replace("{min}", "`1`").replace("{max}", "`10`")));
                    return;
                }
            } catch (NumberFormatException ex) {
                page = 1;
            }
        } else {
            page = 1;
        }
        int from = page * 10 - 9;
        int to = page * 10;
        if (arg.equalsIgnoreCase("level")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_level", "Top {from}-{to} | Level")
                            .replace("{from}", String.valueOf(from))
                            .replace("{to}", String.valueOf(to)),
                    getTopLevel(from, to, uid)));
            return;
        }
        if (arg.equalsIgnoreCase("money")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_money", "Top {from}-{to} | Money")
                            .replace("{from}", String.valueOf(from)).replace("{to}", String.valueOf(to)),
                    getTopMoney(from, to, uid)));
            return;
        }
        if (arg.equals("event")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_event", "Top {from}-{to} | Event Points")
                            .replace("{from}", String.valueOf(from)).replace("{to}", String.valueOf(to)),
                    getTopEvent(from, to, uid)));
            return;
        }
        if (arg.equals("holiday")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_holiday", "Top {from}-{to} | Holiday Stage")
                            .replace("{from}", String.valueOf(from)).replace("{to}", String.valueOf(to)),
                    getTopHoliday(from, to, uid)));
            return;
        }
        if (arg.equalsIgnoreCase("daily")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_dailystreak", "Top {from}-{to} | Daily Streak")
                    .replace("{from}", "" + from).replace("{to}", "" + to), getTopVotes(from, to, uid)));
            return;
        }
        if (arg.equalsIgnoreCase("diamonds") || (arg.equalsIgnoreCase("diamond"))) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_diamonds", "Top {from}-{to} | Diamonds")
                    .replace("{from}", "" + from).replace("{to}", "" + to), getTopDiamonds(from, to, uid)));
            return;
        }
        if (arg.equalsIgnoreCase("power")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_power", "Top {from}-{to} | Power")
                    .replace("{from}", "" + from).replace("{to}", "" + to), getTopPower(from, to, uid)));
            return;
        }
        if (arg.equalsIgnoreCase("quest") || (arg.equalsIgnoreCase("conquest"))) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_conquest", "Top {from}-{to} | Conquest")
                    .replace("{from}", "" + from).replace("{to}", "" + to), getTopQuest(from, to, uid)));
            return;
        }
        if (arg.equalsIgnoreCase("kd") || (arg.equalsIgnoreCase("kills") || (arg.equalsIgnoreCase("deaths")))) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "top_kd", "Top {from}-{to} | KD")
                    .replace("{from}", "" + from).replace("{to}", "" + to), getTopKD(from, to, uid)));
            return;
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    private String getTopLevel(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM exp INNER JOIN elexp ON exp.userId = elexp.userId ORDER BY exp.level DESC, " +
                       "elexp.elixirLevel " + "DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | Level ").append(rs.getInt("level"))
                            .append(" ").append(badge).append("\n");
                    continue;
                }
                ExpUser expUser = ExpUser.getExpUser(userId);
                int level = expUser.getLevel();
                int elixirLevel = expUser.getElixirLevel();
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | Level ").append(level);
                if (level == 150) top.append(" (EL: ").append(elixirLevel).append(")");
                top.append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopMoney(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM economy ORDER BY (balance+0) DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | ").append(EmoteUtil.getCoin()).append(" ")
                            .append(FormatUtil.formatCommas(rs.getString("balance"))).append(" ").append(badge).append("\n");
                    continue;
                }
                String balance = player.getEcoUser().getBalanceAbr();
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | ")
                        .append(EmoteUtil.getCoin()).append(" ").append(balance).append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopEvent(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM specialevent ORDER BY points DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int points = rs.getInt("points");
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | `")
                            .append(FormatUtil.formatCommas(points)).append("` Points").append(" ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | `")
                        .append(FormatUtil.formatCommas(points)).append("` Points").append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopHoliday(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM holidayevent ORDER BY stage DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int points = rs.getInt("stage");
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | Stage `")
                            .append(FormatUtil.formatCommas(points)).append("` ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | Stage `")
                        .append(FormatUtil.formatCommas(points)).append("` ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopVotes(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM dailyRewards ORDER BY streak DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int streak = rs.getInt("streak");
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | Claimed `")
                            .append(FormatUtil.formatCommas(streak)).append("` daily rewards in a row").append(" ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | Claimed `")
                        .append(FormatUtil.formatCommas(streak)).append("` daily rewards in a row").append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopDiamonds(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM diamonds ORDER BY diamonds DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int diamonds = rs.getInt("diamonds");
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | ")
                            .append(EmoteUtil.getEmoteMention("Diamond")).append(" ")
                            .append(FormatUtil.formatCommas(diamonds)).append(" ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | ")
                        .append(EmoteUtil.getEmoteMention("Diamond")).append(" ")
                        .append(FormatUtil.formatCommas(diamonds)).append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopPower(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM power ORDER BY totalPower DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int power = rs.getInt("totalPower");
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | ")
                            .append(FormatUtil.formatCommas(power)).append(" ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | ")
                        .append(FormatUtil.formatCommas(power)).append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopKD(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM stats WHERE statistic_name='kd' ORDER BY statistic_value DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int kills1 = Integer.parseInt(StatsUtil.getStat(userId, "attack_kills", "0"));
                int kills2 = Integer.parseInt(StatsUtil.getStat(userId, "defend_kills", "0"));
                int totalKills = kills1 + kills2;
                int deaths1 = Integer.parseInt(StatsUtil.getStat(userId, "attack_deaths", "0"));
                int deaths2 = Integer.parseInt(StatsUtil.getStat(userId, "defend_deaths", "0"));
                int totalDeaths = deaths1 + deaths2;
                double kdKills;
                double kdDeaths;
                if (totalDeaths == 0) {
                    kdDeaths = 1.0;
                } else {
                    kdDeaths = totalDeaths;
                }
                if (totalKills == 0) {
                    kdKills = 1.0;
                } else {
                    kdKills = totalKills;
                }
                double kd = round(kdKills/kdDeaths, 2);
                int finalKills = (int) kdKills;
                int finalDeaths = (int) kdDeaths;
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | ")
                            .append("KD: ").append(kd).append(" ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | ")
                        .append("**KD:** ").append(kd).append(", **K:** ").append(finalKills).append(", **D:** ").append(finalDeaths).append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    private String getTopQuest(int offset, int to, String selfId) {
        StringBuilder top = new StringBuilder();
        int rank = 0;
        boolean printedSelf = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM journal ORDER BY totalQuests DESC LIMIT 500;")) {
            while (rs.next()) {
                rank++;
                if (rank < offset) continue;
                if (rank > to && printedSelf) break;
                String userId = rs.getString("userId");
                if (!printedSelf && rank > to && !userId.equals(selfId)) continue;
                if (!Player.playerExists(userId)) {
                    rank--;
                    continue;
                }
                if (userId.equals(selfId)) {
                    printedSelf = true;
                    if (rank > to) top.append("\n");
                }
                Player player = Player.getPlayer(userId);
                User u = player.getUser();
                int chapter = rs.getInt("chapter");
                int quest = rs.getInt("quest");
                String badge = "";
                String mode = IronmanUtil.getIronmanMode(userId).getIronmanMode();
                if (!mode.equals("disabled")) {
                    if (mode.equals("hardcore")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_ironman_badge");
                    }
                    if (mode.equals("ironman")) {
                        badge = EmoteUtil.getEmoteMention(mode + "_badge");
                    }
                }
                if (u == null) {
                    top.append("**#").append(rank).append("** <unavailable> | ")
                            .append("Chapter ").append(chapter).append(", Quest ").append(quest).append(" ").append(badge).append("\n");
                    continue;
                }
                top.append("**#").append(rank).append("** ").append(u.getAsTag()).append(" | ")
                        .append("Chapter ").append(chapter).append(", Quest ").append(quest).append(" ").append(badge).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return top.toString();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}