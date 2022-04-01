package me.affluent.decay.achievements;

import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.AttackEvent;
import me.affluent.decay.util.DiamondsUtil;
import me.affluent.decay.util.IgnoreAchievementUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.StatsUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BrawlerAchievement extends Achievement{

    private static final List<Long> values = Arrays.asList(10L, 25L, 50L, 100L, 200L, 350L, 500L, 750L, 1000L, 1500L, 2000L, 2750L, 3500L);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (long value : values)
            achievementData.add(new AchievementData("Win " + FormatUtil.formatAbbreviated(value) + " Arena battles. [{{stats-attack_kills}}/" + FormatUtil.formatAbbreviated(value) + "]"));
    }

    @Override
    long getSD() {
        return 50;
    }

    @Override
    long getID() {
        return 75;
    }

    public BrawlerAchievement() {
        super("Brawler", achievementData);
    }

    @Override
    public void onAttackEvent(AttackEvent event) {
        String uid = event.getAttacker().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();

        int kills = Integer.parseInt(StatsUtil.getStat(uid, "attack_kills", "0"));

        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        String ignore = IgnoreAchievementUtil.getIgnoreAchievementUtil(uid).getIgnoreAchievementSetting();
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (kills >= value) newTier++;
        if (newTier > achievedTier) {
            Achievements.addAchievement(uid, this, newTier);
            long rr = getReward(newTier);
            DiamondsUtil.addDiamonds(uid, rr);
            MessageEmbed msg = MessageUtil.info("Achievement",
                    "You just achieved " + getName() + " " + newTier + "!\n" +
                            "**+ " + EmoteUtil.getDiamond() + " `x" +
                            FormatUtil.formatCommas(rr) + "`**\n\n" +
                            "Use `" + userPrefix + "achievements` to check all your achievements.");
            if (ignore.equalsIgnoreCase("enabled")) {
                try {
                    p.getUser().openPrivateChannel().queue(pc -> pc.sendMessage(msg).queue());
                } catch (Exception ignored) {
                }
            }
        }
    }
}
