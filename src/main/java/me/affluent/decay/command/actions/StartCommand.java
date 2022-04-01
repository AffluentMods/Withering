package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.BadgeUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import static me.affluent.decay.enums.Gender.MADAM;
import static me.affluent.decay.enums.Gender.SIR;
import static me.affluent.decay.enums.Gender.NEUTRAL;

public class StartCommand extends BotCommand {

    public StartCommand() {
        this.name = "start";
        this.help = "Start your adventure!";
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (Player.playerExists(uid)) {
            e.reply(MessageUtil.err(Language.getLocalized(uid, "error", "Error"),
                    Language.getLocalized(uid, "start_already_started", "You already started playing!")));
            return;
        }
        String[] args = e.getArgs();
        if (args.length == 0) {
            String msg1 =
                    Language.getLocalized(uid, "start_message1", "Would you like your character to be a Madam, Sir, or Neutral?");
            String msg2 = Language.getLocalized(uid, "start_message2", "This only affects the names of your rank.");
            String msg3 =
                    Language.getLocalized(uid, "stat_message3", "Do you wish to play Normal Mode, Ironman, or Hardcore Ironman?");
            String msg4 = Language.getLocalized(uid, "start_message4", "This cannot be changed at a later date\n" +
                    "• Hardcore Ironman have trading, and all shops disabled.\n" +
                    Constants.TAB + "However they get this badge " + EmoteUtil.getEmoteMention("hardcore_ironman_badge") + "\n" +
                    "• Ironman players have trading disabled, and all shops cost `35%` more.\n" +
                    Constants.TAB + "However they get this badge " + EmoteUtil.getEmoteMention("ironman_badge") + "\n" +
                    "• Normal Players have no restrictions in gameplay\n");
            e.reply(MessageUtil.info(Language.getLocalized(uid, "start", "Start"),
                    u.getAsMention() + " " + msg1 + "\n" + Constants.TAB + " - " + msg2 + "\n\n" + msg3 + "\n" + Constants.TAB + " - " + msg4 + "\n" +
                            "`w.start Sir <Normal | Ironman | Hardcore>`\n" +
                            "`w.start Madam <Normal | Ironman | Hardcore>`\n" +
                            "`w.start Neutral <Normal | Ironman | Hardcore>`\n\n"));
            return;

        }
        if (args.length < 2) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`w.start <sir | madam | neutral> <normal | ironman | hardcore>`\n" +
                                    "Example: `w.start sir ironman` - will give you a male gender with the ironman gamemode.")));
            return;
        }
        String arg = args[0].toLowerCase();
        String arg1 = args[1].toLowerCase();
        if (arg.equals("sir")) {
            String ironmanValue = "";
            if (arg1.equals("ironman")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("ironman");
                BadgeUser.getBadgeUser(uid).addBadge("ironman");
                ironmanValue = "Ironman";
            }
            if (arg1.equals("hardcore") || arg1.equals("hardcore ironman")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("hardcore");
                BadgeUser.getBadgeUser(uid).addBadge("hardcore_ironman");
                ironmanValue = "Hardcore Ironman";
            }
            if (arg1.equals("normal")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("disabled");
                ironmanValue = "Normal";
            }
            PrefixUser.getPrefixUser(uid).setPrefix("w.");
            Player p = new Player(uid, SIR);
            String success = Language.getLocalized(uid, "welcome", "Welcome!");
            String msg1 = Language.getLocalized(uid, "start_SIR", "You started playing as a Sir, with a gamemode of {gamemode}!\n\n" +
                    "Use `w.tutorial` for help")
                    .replace("{gamemode}", ironmanValue);
            e.reply(MessageUtil.success(success, msg1));
            return;
        }
        if (arg.equals("madam")) {
            String ironmanValue = "";
            if (arg1.equals("ironman")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("ironman");
                BadgeUser.getBadgeUser(uid).addBadge("ironman");
                ironmanValue = "Ironman";
            } else if (arg1.equals("hardcore") || arg1.equals("hardcore ironman")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("hardcore");
                BadgeUser.getBadgeUser(uid).addBadge("hardcore_ironman");
                ironmanValue = "Hardcore Ironman";
            } else {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("disabled");
                ironmanValue = "Normal";
            }
            PrefixUser.getPrefixUser(uid).setPrefix("w.");
            Player p = new Player(uid, MADAM);
            String success = Language.getLocalized(uid, "welcome", "Welcome!");
            String msg1 = Language.getLocalized(uid, "start_MADAM", "You started playing as a Madam, with a gamemode of {gamemode}!\n\n" +
                            "Use `w.tutorial` for help")
                    .replace("{gamemode}", ironmanValue);
            e.reply(MessageUtil.success(success, msg1));
            return;
        }
        if (arg.equals("neutral")) {
            String ironmanValue = "";
            if (arg1.equals("ironman")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("ironman");
                BadgeUser.getBadgeUser(uid).addBadge("ironman");
                ironmanValue = "Ironman";
            } else if (arg1.equals("hardcore") || arg1.equals("hardcore ironman")) {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("hardcore");
                BadgeUser.getBadgeUser(uid).addBadge("hardcore_ironman");
                ironmanValue = "Hardcore Ironman";
            } else {
                IronmanUtil.getIronmanMode(uid).setIronmanMode("disabled");
                ironmanValue = "Normal";
            }
            PrefixUser.getPrefixUser(uid).setPrefix("w.");
            Player p = new Player(uid, NEUTRAL);
            String success = Language.getLocalized(uid, "welcome", "Welcome!");
            String msg1 = Language.getLocalized(uid, "start_NEUTRAL", "You started playing as Neutral, with a gamemode of {gamemode}!\n\n" +
                            "Use `w.tutorial` for help")
                    .replace("{gamemode}", ironmanValue);
            e.reply(MessageUtil.success(success, msg1));
            return;
        }

        e.reply(MessageUtil.err(Language.getLocalized(uid, "error", "Error"),
                Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }
}