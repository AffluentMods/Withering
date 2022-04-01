package me.affluent.decay.command.information;

import com.mysql.cj.util.StringUtils;
import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.InGameTime;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class CalendarCommand extends BotCommand {

    public CalendarCommand() {
        this.name = "calendar";
        this.cooldown = 2;
        this.aliases = new String[]{"cal"};
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        if (!(Player.playerExists(u.getId()))) {
            e.reply(Constants.PROFILE_404(u.getId()));
            return;
        }
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (args.length < 1) {
           e.reply(MessageUtil.info(Language.getLocalized(uid, "calendar_title", "Calendar"),
                   Language.getLocalized(uid, "calendar",
                           "Use {command_usage} for more information.\n\n" +
                   "**" + InGameTime.getCurrentDate() + "**\n" +
                   InGameTime.getCurrentTime() + "\n\n" +
                                   "Next Up; \n" +
                                   "{next_event}\n" +
                                   "{next_holiday}")
                           .replace("{next_event}", getMonthlyEvents(InGameTime.getCurrentMonth() + 2))
                           .replace("{next_holiday}", getHolidayEvents(InGameTime.getCurrentMonth() + 1))
                           .replace("{command_usage}", "`" + userPrefix + "calendar <month>`")));
           return;
        }
        String month = args[0].toLowerCase();
        String monthlyEvent = "";
        int monthInteger;
        if (StringUtils.isStrictlyNumeric(month)) {
            monthInteger = Integer.parseInt(month);
        } else {
            monthInteger = monthString(month);
        }
        if (monthInteger > 0 && monthInteger < 13) {
            monthlyEvent = getMonthlyEvents(monthInteger);
        } else {
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_month", "Invalid month")));
            return;
        }
        e.reply(MessageUtil.info(capitalizeFully(monthWordString(monthInteger)), monthlyEvent));
    }

    public String getMonthlyEvents(int month) {
        return switch (month) {
            case 1, 2, 13, 14 -> "**Event:** Striker";
            case 3, 4 -> "**Event:** Gambler";
            case 5, 6 -> "**Event:** Chest Breacher";
            case 7, 8 -> "**Event:** Golden Hand";
            case 9, 10 -> "**Event:** Golden Gambler";
            case 11, 12 -> "**Event:** Dwarven Miner";
            default -> null;
        };
    }

    public String getHolidayEvents(int month) {
        return switch (month) {
            case 1, 13 -> "**Holiday Event:** New Years";
            case 10 -> "**Holiday Event:** Halloween";
            case 12 -> "**Holiday Event:** Christmas";
            default -> "";
        };
    }

    public static int monthString(String month) {
        return switch (month) {
            case "january", "jan" -> 1;
            case "february", "feb" -> 2;
            case "march", "mar" -> 3;
            case "april", "apr" -> 4;
            case "may" -> 5;
            case "june", "jun" -> 6;
            case "july", "jul" -> 7;
            case "august", "aug" -> 8;
            case "september", "sep" -> 9;
            case "october", "oct" -> 10;
            case "november", "nov" -> 11;
            case "december", "dec" -> 12;
            default -> 0;
        };
    }

    public static String monthWordString(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> null;
        };
    }

    static String capitalizeFully(String string) {
        String capitalized = "";
        for(String word : string.split(" ")) {
            capitalized += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        if(capitalized.endsWith(" ")) capitalized = capitalized.substring(0, capitalized.length()-1);
        return capitalized;
    }
}
