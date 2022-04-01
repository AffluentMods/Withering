package me.affluent.decay.command.information;

import me.affluent.decay.achievements.Achievement;
import me.affluent.decay.achievements.AchievementData;
import me.affluent.decay.achievements.Achievements;
import me.affluent.decay.entity.Player;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.QuestUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.StatsUtil;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class AchievementsCommand extends BotCommand {

    public AchievementsCommand() {
        this.name = "achievements";
        this.cooldown = 1.25;
        this.aliases = new String[]{"achievement", "mission", "missions", "ach"};
    }

    @Override
    public void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p = Player.getPlayer(uid);
        HashMap<Achievement, Integer> userAchievements = Achievements.getAchievedAchievements(u.getId());
        HashMap<String, Achievement> achievementList = Achievement.getAllAchievements();
        StringBuilder response = new StringBuilder();
        for (Achievement achievement : achievementList.values()) {
            String name = achievement.getName();
            int tier = 0;
            if (userAchievements.containsKey(achievement)) tier = userAchievements.get(achievement);
            String tierDisplay = "";
            for (int i = 0; i < achievement.getAchievementDataMap().size(); i++) {
                if (i < tier) tierDisplay += EmoteUtil.getEmoteMention("S_") + " ";
                else tierDisplay += EmoteUtil.getEmoteMention("E_S") + " ";
            }
            final int index = tier;
            AchievementData ad = achievement.getAchievementData(index + 1);
            String desc;
            if (tier == achievement.getAchievementDataMap().size()) {
                desc = "Completed";
                response.append("**").append(name).append("**\n").append(tierDisplay).append("\n").append(desc)
                        .append("\n\n");
            } else {
                int currentQuestsBeaten = QuestUtil.getCurrentQuest(p.getUserId()) - 1;
                desc = ad.getDescription()
                        .replaceAll("\\{\\{stats-attack_kills\\}\\}", StatsUtil.getStat(uid, "attack_kills", "0"))
                        .replaceAll("\\{\\{stats-chests_opened\\}\\}", StatsUtil.getStat(uid, "chests_opened", "0"))
                        .replaceAll("\\{\\{stats-total_pet_levels\\}\\}", StatsUtil.getStat(uid, "total_pet_levels", "0"))
                        .replaceAll("\\{\\{stats-highest_pet_level\\}\\}", StatsUtil.getStat(uid, "highest_pet_level", "0"))
                        .replaceAll("\\{\\{stats-tavern_spins\\}\\}", StatsUtil.getStat(uid, "tavern_spins", "0"))
                        .replaceAll("\\{\\{stats-item_levels\\}\\}", StatsUtil.getStat(uid, "total_item_levels", "0"))
                        .replaceAll("\\{\\{stats-total_quests\\}\\}", String.valueOf(currentQuestsBeaten))
                        .replaceAll("\\{\\{stats-current_level\\}\\}", String.valueOf(p.getExpUser().getLevel()))
                ;
                long rr = achievement.getReward(tier + 1);
                response.append("**").append(name).append("**\n").append(tierDisplay).append("\n").append(desc)
                        .append("\n- Reward: ").append(EmoteUtil.getDiamond()).append(" ")
                        .append(FormatUtil.formatCommas(rr)).append("\n\n");
            }
        }
        e.reply(MessageUtil.info("Achievements of " + p.getUser().getAsTag(), response.toString()));
    }
}
