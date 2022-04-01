package me.affluent.decay.achievements;

import me.affluent.decay.chest.Chest;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.ChestOpenEvent;
import me.affluent.decay.event.TavernSpinEvent;
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

public class GearSummonerAchievement extends Achievement{

    private static final List<Long> values = Arrays.asList(25L, 75L, 150L, 300L, 500L, 750L, 1000L, 1500L, 2000L, 2500L);

    private static final List<AchievementData> achievementData;

    static {
        achievementData = new ArrayList<>();
        for (long value : values)
            achievementData.add(new AchievementData("Open " + FormatUtil.formatAbbreviated(value) + " Chests. [{{stats-chests_opened}}/" + FormatUtil.formatAbbreviated(value) + "]"));
    }

    @Override
    long getSD() {
        return 75;
    }

    @Override
    long getID() {
        return 75;
    }

    public GearSummonerAchievement() {
        super("Gear Summoner", achievementData);
    }

    @Override
    public void onChestOpenEvent(ChestOpenEvent event) {
        Player p = event.getOpener();
        String uid = p.getUserId();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();

        int chestsOpened = Integer.parseInt(StatsUtil.getStat(uid, "chests_opened", "0")) + 1;

        HashMap<Achievement, Integer> achievements = Achievements.getAchievedAchievements(uid);
        String ignore = IgnoreAchievementUtil.getIgnoreAchievementUtil(uid).getIgnoreAchievementSetting();
        int achievedTier = 0;
        if (achievements.containsKey(this)) achievedTier = achievements.get(this);
        if (achievedTier == achievementData.size()) return;
        int newTier = 0;
        for (long value : values) if (chestsOpened >= value) newTier++;
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
