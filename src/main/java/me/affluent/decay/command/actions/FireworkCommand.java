package me.affluent.decay.command.actions;

import me.affluent.decay.Bot;
import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.FireworksUtil;
import me.affluent.decay.util.TokensUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FireworkSystem;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class FireworkCommand extends BotCommand {

    public FireworkCommand() {
        this.name = "firework";
        this.cooldown = 2;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        long amount = FireworksUtil.getHolidayFireworks(uid);
        if (amount < 1) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_fireworks",
                    "Majesty, you seem to have ran out. \nYou need fireworks " + EmoteUtil.getEmoteMention("Holiday_Firework") + " to use this.")));
            return;
        }
        long now = System.currentTimeMillis();
        final long diff = 1200000L;
        long until = now + diff;
        FireworksUtil.setHolidayFireworks(uid, (int) (amount - 1));
        FireworkSystem.addFirework(uid, until);
        e.reply(MessageUtil.success(Language.getLocalized(uid, "firework", "Launched Firework"),
                Language.getLocalized(uid, "usage", "You have launched a " + EmoteUtil.getEmoteMention("Holiday_Firework") + " Firework\n" +
                        "You will receive 100% more EXP, and Gold for `20` Minutes!\n\n" +
                                "You have {fireworks_remaining} " + EmoteUtil.getEmoteMention("Holiday_Firework") + " Fireworks remaining.")
                        .replace("{fireworks_remaining}", "" + FireworksUtil.getHolidayFireworks(uid))));
    }
}
