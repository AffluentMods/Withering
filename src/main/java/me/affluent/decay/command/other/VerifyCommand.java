package me.affluent.decay.command.other;

import me.affluent.decay.captcha.CaptchaAPI;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class VerifyCommand extends BotCommand {

    public VerifyCommand() {
        this.name = "verify";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (CaptchaAPI.isPending(uid)) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            CaptchaAPI.CaptchaData cd = CaptchaAPI.getCaptchaData(uid);
            if (args.length < 1) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                        Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                                .replace("{command_usage}", "`" + userPrefix + "verify <code>`")));
                return;
            }
            String code = args[0];
            if (code.equals(cd.getText())) {
                CaptchaAPI.remove(uid);
                e.reply(MessageUtil.success(Language.getLocalized(uid, "verify", "Verify"),
                        Language.getLocalized(uid, "verify_success", "Successfully verified!")));
                return;
            }
            if (code.equalsIgnoreCase("resend")) {
                e.getTextChannel().sendMessage("**Verify captcha**\n`" + userPrefix + "verify <code>`")
                        .addFile(cd.getData(), "verify_" + uid + ".jpg").queue();
                return;
            }
            e.reply(MessageUtil.err(Language.getLocalized(uid, "verify", "Verify"),
                    Language.getLocalized(uid, "verify_failed", "Verification failed - wrong code!")));
        }
    }
}