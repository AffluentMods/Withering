package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.settingsUtil.BarUtil;
import me.affluent.decay.util.settingsUtil.ConfirmUtil;
import me.affluent.decay.util.settingsUtil.ResponseUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class SettingsCommand extends BotCommand {

    public SettingsCommand() {
        this.name = "settings";
        this.aliases = new String[]{"setting"};
        this.cooldown = 2;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "settings <achievementdm | confirm | format | profilebar>`")));
            return;
        }
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        if (args[0].equalsIgnoreCase("format")) {
            if (args.length < 2) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Please use `" + userPrefix + "settings format <mobile/computer>`")));
            }
            if (args[1].equalsIgnoreCase( "pc") || (args[1].equalsIgnoreCase("computer"))) {
                ResponseUtil.getResponseUtil(uid).setResponse("pc");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "format_set", "UI Formatted"),
                        Language.getLocalized(uid, "format_set_msg", "Successfully set your UI format to `pc`!")));
                return;
            }
            if (args[1].equalsIgnoreCase("mobile") || (args[1].equalsIgnoreCase("m"))) {
                ResponseUtil.getResponseUtil(uid).setResponse("mobile");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "format_set", "UI Formatted"),
                        Language.getLocalized(uid, "format_set_msg", "Successfully set your UI format to `mobile`!")));
                return;
            }
        }

        if (args[0].equalsIgnoreCase("confirm")) {
            if (args.length < 2) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Please use `" + userPrefix + "settings confirm <enable/disable>`")));
            }
            if (args[1].equalsIgnoreCase("off") || (args[1].equalsIgnoreCase("disable"))) {
                ConfirmUtil.getConfirmUtil(uid).setConfirmSetting("disabled");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "confirm_set", "Confirmations Disabled"),
                        Language.getLocalized(uid, "confirm_set_msg", "Successfully disabled all confirmations!")));
                return;
            }
            if (args[1].equalsIgnoreCase("on") || (args[1].equalsIgnoreCase("enable"))) {
                ConfirmUtil.getConfirmUtil(uid).setConfirmSetting("enabled");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "confirm_set", "Confirmations Enabled"),
                        Language.getLocalized(uid, "confirm_set_msg", "Successfully enabled all confirmations!")));
                return;
            }
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid Argument")));
        }

        if (args[0].equalsIgnoreCase("bar") || (args[0].equalsIgnoreCase("profilebar"))) {
            if (args.length < 2) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Please use `" + userPrefix + "settings bar <enable/disable>`")));
            }
            if (args[1].equalsIgnoreCase("off") || (args[1].equalsIgnoreCase("disable"))) {
                BarUtil.getBarUtil(uid).setBarSetting("disabled");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "bar_set", "Profile Bar Disabled"),
                        Language.getLocalized(uid, "bar_set_msg", "Successfully disabled Profile Bar!")));
                return;
            }
            if (args[1].equalsIgnoreCase("on") || (args[1].equalsIgnoreCase("enable"))) {
                BarUtil.getBarUtil(uid).setBarSetting("enabled");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "bar_set", "Profile Bar Enabled"),
                        Language.getLocalized(uid, "bar_set_msg", "Successfully enabled Profile Bar!")));
                return;
            }
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid Argument")));
        }

        if (args[0].equalsIgnoreCase("achievement") || (args[0].equalsIgnoreCase("achievementdm"))) {
            if (args.length < 2) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Please use `" + userPrefix + "settings achievementdm <enable/disable>`")));
            }
            if (args[1].equalsIgnoreCase("off") || (args[1].equalsIgnoreCase("disable"))) {
                IgnoreAchievementUtil.getIgnoreAchievementUtil(uid).setIgnoreAchievementSetting("disabled");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "ignore_set", "Achievement DM's Disabled"),
                        Language.getLocalized(uid, "ignore_set_msg", "Successfully disabled DM's on new Achievement Tiers!")));
                return;
            }
            if (args[1].equalsIgnoreCase("on") || (args[1].equalsIgnoreCase("enable"))) {
                IgnoreAchievementUtil.getIgnoreAchievementUtil(uid).setIgnoreAchievementSetting("enabled");
                e.reply(MessageUtil.success(Language.getLocalized(uid, "ignore_set", "Achievement DM's Enabled"),
                        Language.getLocalized(uid, "ignore_set_msg", "Successfully enabled DM's on new Achievement Tiers!")));
                return;
            }
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid Argument")));
        }
    }
}