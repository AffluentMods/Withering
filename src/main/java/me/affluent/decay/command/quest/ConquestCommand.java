package me.affluent.decay.command.quest;

import me.affluent.decay.Constants;
import me.affluent.decay.conquest.Quest;
import me.affluent.decay.conquest.QuestFight;
import me.affluent.decay.conquest.chapter.Chapter;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.ConquestWinEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.QuestUtil;
import me.affluent.decay.util.system.CooldownUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class ConquestCommand extends BotCommand {

    public ConquestCommand() {
        this.name = "conquest";
        this.aliases = new String[]{"cq"};
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "conquest next`")));
            return;
        }
        long conquestCooldown = CooldownUtil.getCooldown(uid, "conquest_cooldown");
        if (conquestCooldown > 0) {
            long now = System.currentTimeMillis();
            long cdDiff = conquestCooldown - now;
            if (cdDiff > 0) {
                String cooldownString = CooldownUtil.format(cdDiff, uid);
                String msg1 = Constants.COOLDOWN(uid).replace("{cooldown}", cooldownString);
                e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
                return;
            }
        }
        String arg = args[0].toLowerCase();
        if (arg.equals("next")) {
            if (!(Player.playerExists(uid))) {
                e.reply(Constants.PROFILE_404(uid));
                return;
            }
            Player p = Player.getPlayer(uid);
            int chapterNr = QuestUtil.getCurrentChapter(uid);
            if (!(Chapter.chapterExists(chapterNr))) {
                String msg = Language.getLocalized(uid, "quest_finished",
                        "Congratulations! You completed all chapters of Withering!\nWe are currently working on " +
                        "adding more chapters, please check back later!");
                e.reply(MessageUtil.info(Language.getLocalized(uid, "quest_plain", "Quest"), msg));
                return;
            }
            int questNr = QuestUtil.getCurrentQuest(uid);
            long diamondGain = getDiamondGain(chapterNr, questNr);
            Chapter chapter = Chapter.getChapter(chapterNr);
            Quest quest = chapter.getQuest(questNr);
            QuestFight questFight = new QuestFight(p, chapter, quest);
            boolean win = questFight.doFight();
            String pTag = u.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">");
            String response_language_id = win ? "quest_fight_win" : "quest_fight_lose";
            String response_default_win =
                    "{player} killed the Bandit\n{player} dealt {player_damage} " +
                    "damage with {player_remaining_health} remaining, while the Bandit dealt {enemy_damage} damage.\n" +
                            "You stole {diamonds} from the bandit.";
            String response_default_lose = "{player} died against the Bandit\nThe Bandit dealt " +
                                           "{enemy_damage} damage with {enemy_remaining_health} remaining, while " +
                                           "{player} dealt {player_damage} damage.";
            int playerDamage = questFight.getPlayerDamage();
            int playerHealth = questFight.getPlayerHealth();
            int enemyDamage = questFight.getEnemyDamage();
            int enemyHealth = questFight.getEnemyHealth();
            String response_default = win ? response_default_win : response_default_lose;
            response_default = "Chapter " + chapterNr + " | Quest " + questNr + "\n" + response_default;
            response_default =
                    response_default
                            .replace("{player}", pTag).replace("{player_damage}", String.valueOf(playerDamage))
                            .replace("{player_remaining_health}", EmoteUtil.getEmoteMention("HP") + " " + playerHealth)
                            .replace("{enemy_damage}", String.valueOf(enemyDamage))
                            .replace("{enemy_remaining_health}", EmoteUtil.getEmoteMention("HP") + " " + enemyHealth)
                            .replace("{diamonds}", "`x" + diamondGain + "` diamonds " + EmoteUtil.getDiamond());
            String response = Language.getLocalized(uid, response_language_id, response_default);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "quest_ended", "PvE Ended"));
            eb.setDescription(response);
            eb.setColor(win ? new Color(19, 255, 58) : new Color(250, 58, 35));
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            if (win) {
                int newChapter = questNr == 10 ? chapterNr + 1 : chapterNr;
                int newQuest = questNr == 10 ? 1 : questNr + 1;
                int diamonds = chapter.getDiamondsReward(questNr);
                p.getInventoryUser().addItem("diamond", diamonds);
                QuestUtil.setJournal(uid, newChapter, newQuest);
                int chaptersBeat = 0;
                if (questNr == 10) {
                    chaptersBeat = chapterNr;
                }
                ConquestWinEvent conquestWinEvent = new ConquestWinEvent(p, chaptersBeat);
                EventManager.callEvent(conquestWinEvent);
            }
            /*if (!win) {
                boolean stealItem = new Random().nextInt(101) < 5;
                if (stealItem) {
                    Withering.getBot().getDatabase()
                            .update("DELETE FROM inventory WHERE userId=? AND ID= RAND() LIMIT 1;", uid);
                }
            }*/
        }
    }

    public static long getDiamondGain(int chapter, int quest) {
        int diamonds = 50;
        int totalQuests = ((chapter * 10) + quest) - 10;
        for (int i = 1; i < totalQuests; i++) {
            diamonds += 5;
        }
        return diamonds;
    }
}