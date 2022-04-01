package me.affluent.decay.achievements;

import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.ItemLevelUpEvent;
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

public class BlacksmithAchievement extends Achievement {

    private static final List<Long> values = Arrays.asList(35L, 75L, 125L, 185L, 250L, 325L, 425L, 550L, 750L, 1250L, 2000L);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (long value : values)
            achievementData.add(new AchievementData("Level up gear " + FormatUtil.formatAbbreviated(value) + " times. [{{stats-item_levels}}/" + FormatUtil.formatAbbreviated(value) + "]"));
    }

    @Override
    long getSD() {
        return 50;
    }

    @Override
    long getID() {
        return 75;
    }

    public BlacksmithAchievement() {
        super("Blacksmith", achievementData);
    }

    @Override
    public void onItemLevelUpEvent(ItemLevelUpEvent event) {
        String uid = event.getLeveler().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();

        int totalLevels = Integer.parseInt(StatsUtil.getStat(uid, "total_item_levels", "0"));

        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        String ignore = IgnoreAchievementUtil.getIgnoreAchievementUtil(uid).getIgnoreAchievementSetting();
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (totalLevels >= value) newTier++;
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
