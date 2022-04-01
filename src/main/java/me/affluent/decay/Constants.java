package me.affluent.decay;

import me.affluent.decay.language.Language;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Random;

public class Constants {

    public static final String PREFIX = "w.";
    public static final String main_guild = "888858645029322763"; //the main/official discord server
    public static final String TAB = "\u2004\u2004\u2004\u2004";
    public static final String dbl_vote_auth = "121212121212"; //Vote authorization code
    public static final String dbl_botid = "1212121212"; //Your bot ID
    public static final String dbl_token = "212121212121"; //DBL token

    public static MessageEmbed PROFILE_404(String userId) {
        return MessageUtil.err(UNKNOWN(userId), Language.getLocalized(userId, "no_profile",
                "Lone traveler, drop your weapon if you wish to visit the last Kingdom `" + PREFIX + "start`\n\n" +
                        "[Support Server](https://discord.gg/withering)"));
    }

    public static MessageEmbed CANT_AFFORD(String userId) {
        return MessageUtil.err(ERROR(userId), Language.getLocalized(userId, "cant_afford", "*checks pockets*\nempty they are, cant afford." ));
    }

    public static String ERROR(String userId) {
        return Language.getLocalized(userId, "error", "Error");
    }

    public static String UNKNOWN(String userId) {
        return Language.getLocalized(userId, "unknown", "Unknown");
    }

    public static String COOLDOWN(String userId) {
        int whichCooldown = new Random().nextInt(100);
        if (whichCooldown <= 20) {
            return Language.getLocalized(userId, "cooldown", "Calm down there warrior, sit by the fire, catch some sleep. You have work to do at dawn,\nwait **{cooldown}**!");
        } else if (whichCooldown <= 40) {
            return Language.getLocalized(userId, "cooldown", "Calm down, you have plenty of time to kill that annoying scoundrel\nwait **{cooldown}**!");
        } else if (whichCooldown <= 60) {
            return Language.getLocalized(userId, "cooldown", "Calm down, you have plenty of time to explore the vast kingdom\nwait **{cooldown}**!");
        } else if (whichCooldown <= 80) {
            return Language.getLocalized(userId, "cooldown", "Calm down, you have plenty of time to explore the land\nwait **{cooldown}**!");
        } else {
            return Language.getLocalized(userId, "cooldown", "Calm down there warrior, sit by the fire, and sharpen your blade while you\nwait **{cooldown}**!");
        }
    }
}
