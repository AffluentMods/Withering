package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.*;
import net.dv8tion.jda.api.entities.User;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class BenefitsCommand extends BotCommand {

    public BenefitsCommand() {
        this.name = "benefits";
        this.aliases = new String[]{"benefit"};
        this.cooldown = 0.75;
    }

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
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date nextRedeem = calendar.getTime();
        long timeLeft = nextRedeem.getTime() - now;
        int lastDay = DonatorRewardSystem.getLastDay(uid);
        int thisDay = DonatorRewardSystem.getThisDay();
        boolean canRedeem = thisDay > lastDay;
        if (!canRedeem) {
            e.reply(MessageUtil.err(Language.getLocalized(uid, "donator_reward_title", "Donator Reward"),
                    Language.getLocalized(uid, "donator_reward_cant_redeem",
                                    "Don't be greedy now, you already claimed your donator reward!\nThe next donator reward is available in {time_left}.")
                            .replace("{time_left}", CooldownUtil.format(timeLeft, uid))));
            return;
        }
        Player p = Player.getPlayer(uid);
        DonatorRewardSystem.setDonatorReward(uid, thisDay);
        int donator = 0; //TODO CHANGE THIS
        HashMap<String, Long> rewards = DonatorRewardSystem.getRewards(p, donator);
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
        e.reply(MessageUtil.success(Language.getLocalized(uid, "donator_reward_title", "Donator Reward"),
                Language.getLocalized(uid, "donator_reward_redeem",
                                "Successfully received your donator reward!\n\n" +
                                        "__Rewards:__\n" +
                                        "{rewards}\n\n" +
                                        "You can receive your next donator reward in {time_left}.")
                        .replace("{rewards}", rewardsDisplay.toString())
                        .replace("{time_left}", CooldownUtil.format(timeLeft, uid))));
        InventoryUser inv = p.getInventoryUser();
        for (String item : rewards.keySet()) {
            long amount = rewards.get(item);
            if (item.equalsIgnoreCase("money")) {
                p.getEcoUser().addBalance(amount);
                continue;
            }
            inv.addItem(item, amount);
        }
    }
}