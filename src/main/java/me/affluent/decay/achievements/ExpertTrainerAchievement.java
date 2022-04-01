package me.affluent.decay.achievements;

import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.PetLevelUpEvent;
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

public class ExpertTrainerAchievement extends Achievement {

    private static final List<Long> values = Arrays.asList(10L, 20L, 35L, 50L, 70L, 90L, 100L);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (long value : values)
            achievementData.add(new AchievementData("Level a pet to level " + FormatUtil.formatAbbreviated(value) + ". [{{stats-highest_pet_level}}/" + FormatUtil.formatAbbreviated(value) + "]"));
    }

    @Override
    long getSD() {
        return 50;
    }

    @Override
    long getID() {
        return 125;
    }

    public ExpertTrainerAchievement() {
        super("Expert Trainer", achievementData);
    }

    @Override
    public void onPetLevelUpEvent(PetLevelUpEvent event) {
        String uid = event.getLeveler().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();

        int level = Integer.parseInt(StatsUtil.getStat(uid, "highest_pet_level", "0"));

        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        String ignore = IgnoreAchievementUtil.getIgnoreAchievementUtil(uid).getIgnoreAchievementSetting();
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (level >= value) newTier++;
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
