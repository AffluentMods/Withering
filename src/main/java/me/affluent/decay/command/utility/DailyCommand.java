package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.IngotUtil;
import me.affluent.decay.util.system.*;
import me.affluent.decay.vote.VoteSystem;
import net.dv8tion.jda.api.entities.User;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class DailyCommand extends BotCommand {

    public DailyCommand() {
        this.name = "daily";
        this.cooldown = 1.5;
    }
    public long publicTimeLeft;
    private static final TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Berlin"));

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String[] args = e.getArgs();
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date nextRedeem = calendar.getTime();
        long timeLeft = nextRedeem.getTime() - now;
        publicTimeLeft = timeLeft;
        boolean canRedeem = DailyRewardSystem.isDailyAvailable(uid);
        if (!canRedeem) {
            e.reply(MessageUtil.err(Language.getLocalized(uid, "daily_reward_title", "Daily Reward"),
                    Language.getLocalized(uid, "daily_reward_cant_redeem",
                            "Don't be greedy now, you already claimed your daily!\nThe next daily is available in {time_left}.")
                            .replace("{time_left}", CooldownUtil.format(timeLeft, uid))));
            return;
        }
        boolean hasVoted = false;
        if (!Withering.test) hasVoted = VoteSystem.hasVoted(uid);
        else {
            if (args.length > 0 && args[0].equalsIgnoreCase("--voted")) hasVoted = true;
        }
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!hasVoted) {
            e.reply(MessageUtil.err(Language.getLocalized(uid, "daily_reward_title", "Daily Reward"),
                    Language.getLocalized(uid, "daily_reward_not_voted",
                            "You need to vote for our bot in order to receive your daily reward!\nUse {vote_command} " +
                            "to vote.\nYour vote did not count? Use {vote_check_command}.")
                            .replace("{vote_command}", "`" + userPrefix + "vote`")
                            .replace("{vote_check_command}", "`" + userPrefix + "vote check`")));
            return;
        }
        Player p = Player.getPlayer(uid);
        int streak = DailyRewardSystem.getStreak(uid);
        streak++;
        int newDay = Math.min(streak, 10); // smallest value, either the day, or if bigger than 10 it will return 10
        int thisYear = DailyRewardSystem.getThisYear();
        DailyRewardSystem.setDailyReward(uid, streak, now, thisYear);
        HashMap<String, Long> rewards = DailyRewardSystem.getRewards(p, newDay);
        StringBuilder rewardsDisplay = new StringBuilder();
        for (String item : rewards.keySet()) {
            long amount = rewards.get(item);
            if (item.equalsIgnoreCase("money")) {
                rewardsDisplay.append("- ").append(EmoteUtil.getCoin()).append(" `x")
                        .append(FormatUtil.formatCommas(amount)).append("`\n");
                continue;
            }
            rewardsDisplay.append("- ").append(EmoteUtil.getEmoteMention(item)).append(" `x")
                    .append(FormatUtil.formatCommas(amount)).append("`\n");
        }
        e.reply(MessageUtil.success(Language.getLocalized(uid, "daily_reward_title", "Daily Reward"),
                Language.getLocalized(uid, "daily_reward_redeem",
                        "Successfully received your reward of day {day}!\nYou have a daily reward streak of " +
                        "{streak}.\n\n__Rewards:__\n{rewards}\n\nYou can receive your next daily reward in " +
                        "{time_left}.").replace("{day}", "`" + newDay + "`").replace("{streak}", "`" + streak + "`")
                        .replace("{rewards}", rewardsDisplay.toString())
                        .replace("{time_left}", CooldownUtil.format(timeLeft, uid))));
        InventoryUser inv = p.getInventoryUser();
        for (String item : rewards.keySet()) {
            long amount = rewards.get(item);
            if (item.equalsIgnoreCase("money")) {
                p.getEcoUser().addBalance(amount);
                continue;
            }
            if (item.equalsIgnoreCase("alloy ingot")) {
                IngotUtil.addIngots(uid, amount);
                continue;
            }
            inv.addItem(item, amount);
        }
    }
}