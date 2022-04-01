package me.affluent.decay.command.actions;

import com.mysql.cj.util.StringUtils;
import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.GiftingEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.*;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class GiftCommand extends BotCommand {

    public GiftCommand() {
        this.name = "gift";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        Player p = Player.getPlayer(uid);
        String[] args = e.getArgs();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "gift <Player ID | Player Mention>`")));
            return;
        }
        String id = args[0];
        User tmention = null;
        Player t = null;
        String idValue;
        if (StringUtils.isStrictlyNumeric(id) && id.length() == 18) {
            t = Player.getPlayer(id);
        } else {
            tmention = MentionUtil.getUser(e.getMessage());
            if (tmention == null || !Player.playerExists(tmention.getId())) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_user", "Invalid user")));
                return;
            }
            t = Player.getPlayer(tmention.getId());
        }
        if (t == null) {
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_user", "Invalid user")));
            return;
        }
        long presentAmount = PresentsUtil.getHolidayPresents(uid);
        if (presentAmount < 1) {
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "no_presents", "You need more " + EmoteUtil.getEmoteMention("Holiday_Present") + " Presents in order to gift")));
            return;
        }
        int gifts_given = Integer.parseInt(StatsUtil.getStat(uid, "gifts_given", "0"));
        gifts_given++;
        StatsUtil.setStat(uid, "gifts_given", String.valueOf(gifts_given));
        int gifts_received = Integer.parseInt(StatsUtil.getStat(t.getUserId(), "gifts_received", "0"));
        gifts_received++;
        StatsUtil.setStat(t.getUserId(), "gifts_received", String.valueOf(gifts_received));
        String rewards = "";
        String rewards2 = "";
        GiftingEvent giftingEvent = new GiftingEvent(p, rewards);
        EventManager.callEvent(giftingEvent);

        // Giving Rewards
        HashMap<String, Integer> presentRewards = new HashMap<>();
        HashMap<String, Integer> presentRewards2 = new HashMap<>();
        BigInteger totalMoneyReward = BigInteger.ZERO;
        BigInteger totalMoneyReward2 = BigInteger.ZERO;
        int giftingAmount = 1;
        PresentsUtil.setHolidayPresents(uid, (int) (presentAmount - giftingAmount));
        for (int i = 0; i < giftingAmount; i++) {
        String[] wonData = getPrize().split(":");
        String prize = wonData[0].toLowerCase();
        int amount = Integer.parseInt(wonData[1]);
        String theReward = "";
        String theReward2 = "";
        if (prize.endsWith(" key")) {
            theReward = prize.toLowerCase();
            theReward2 = prize.toLowerCase();
        }
        if (prize.equals("diamonds")) {
            theReward = "diamonds";
            theReward2 = "diamonds";
        }
        if (prize.equalsIgnoreCase("token")) {
            theReward = "token";
            theReward2 = "token";
        }
        if (prize.equalsIgnoreCase("goldentoken")) {
            theReward = "goldentoken";
            theReward2 = "goldentoken";
        }
        if (prize.equals("{money1}")) {
            BigInteger moneyReward =
                    new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 50.0)))
                            .toBigInteger();
            if (moneyReward.compareTo(BigInteger.ZERO) > 0) {
                if (moneyReward.compareTo(BigInteger.valueOf(175000)) > 0) {
                    moneyReward = BigInteger.valueOf(175000);
                }
            }
            totalMoneyReward = totalMoneyReward.add(moneyReward);
            theReward = "money";

            BigInteger moneyReward2 =
                    new BigDecimal(t.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 50.0)))
                            .toBigInteger();
            if (moneyReward2.compareTo(BigInteger.ZERO) > 0) {
                if (moneyReward2.compareTo(BigInteger.valueOf(175000)) > 0) {
                    moneyReward2 = BigInteger.valueOf(175000);
                }
            }
            totalMoneyReward2 = totalMoneyReward2.add(moneyReward2);
            theReward2 = "money";
        }
        if (prize.equals("{money2}")) {
            BigInteger moneyReward =
                    new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 20.0)))
                            .toBigInteger();
            if (moneyReward.compareTo(BigInteger.ZERO) > 0) {
                if (moneyReward.compareTo(BigInteger.valueOf(175000)) > 0) {
                    moneyReward = BigInteger.valueOf(175000);
                }
            }
            totalMoneyReward = totalMoneyReward.add(moneyReward);
            theReward = "money";

            BigInteger moneyReward2 =
                    new BigDecimal(t.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 20.0)))
                            .toBigInteger();
            if (moneyReward2.compareTo(BigInteger.ZERO) > 0) {
                if (moneyReward2.compareTo(BigInteger.valueOf(175000)) > 0) {
                    moneyReward2 = BigInteger.valueOf(175000);
                }
            }
            totalMoneyReward2 = totalMoneyReward2.add(moneyReward2);
            theReward2 = "money";
        }
        if (prize.equals("{money3}")) {
            BigInteger moneyReward =
                    new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 14.28)))
                            .toBigInteger();
            if (moneyReward.compareTo(BigInteger.ZERO) > 0) {
                if (moneyReward.compareTo(BigInteger.valueOf(175000)) > 0) {
                    moneyReward = BigInteger.valueOf(175000);
                }
            }
            totalMoneyReward = totalMoneyReward.add(moneyReward);
            theReward = "money";

            BigInteger moneyReward2 =
                    new BigDecimal(t.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 14.28)))
                            .toBigInteger();
            if (moneyReward2.compareTo(BigInteger.ZERO) > 0) {
                if (moneyReward2.compareTo(BigInteger.valueOf(175000)) > 0) {
                    moneyReward2 = BigInteger.valueOf(175000);
                }
            }
            totalMoneyReward2 = totalMoneyReward2.add(moneyReward2);
            theReward2 = "money";
        }
        if (prize.equalsIgnoreCase("epic")) {
            ItemType it = Rank.getHighestMaterial(p.getExpUser().getLevel());
            if (it == ItemType.DRAGON_STEEL || it == ItemType.TITAN_ALLOY || it == ItemType.WITHER)
                it = ItemType.CARBON_STEEL;
            String theArmor = RandomUtil.getRandomArmor(it);
            theReward = "epic " + theArmor;

            ItemType it2 = Rank.getHighestMaterial(t.getExpUser().getLevel());
            if (it2 == ItemType.DRAGON_STEEL || it2 == ItemType.TITAN_ALLOY || it2 == ItemType.WITHER)
                it2 = ItemType.CARBON_STEEL;
            String theArmor2 = RandomUtil.getRandomArmor(it2);
            theReward = "epic " + theArmor2;
        }
        presentRewards.put(theReward.toLowerCase(), presentRewards.getOrDefault(theReward.toLowerCase(), 0) + amount);
        presentRewards2.put(theReward2.toLowerCase(), presentRewards2.getOrDefault(theReward2.toLowerCase(), 0) + amount);
    }
    for (String prize : presentRewards.keySet()) {
        if (prize.equalsIgnoreCase("money")) continue;
        int amount = presentRewards.get(prize);
        String emote = null;
        if (prize.equalsIgnoreCase("diamonds")) emote = EmoteUtil.getEmoteMention("Diamond");
        if (prize.equalsIgnoreCase("token")) emote = EmoteUtil.getEmoteMention("Tavern_Token");
        if (prize.equalsIgnoreCase("goldentoken")) emote = EmoteUtil.getEmoteMention("Golden_Tavern_Token");
        if (prize.startsWith("epic")) emote = new Item(-1, null, prize).getEmote();
        if (emote == null) emote = EmoteUtil.getEmoteMention(prize);
        rewards += "- " + emote + " `x" + FormatUtil.formatCommas(amount) + "`";
    }
        for (String prize : presentRewards2.keySet()) {
            if (prize.equalsIgnoreCase("money")) continue;
            int amount = presentRewards2.get(prize);
            String emote = null;
            if (prize.equalsIgnoreCase("diamonds")) emote = EmoteUtil.getEmoteMention("Diamond");
            if (prize.equalsIgnoreCase("token")) emote = EmoteUtil.getEmoteMention("Tavern_Token");
            if (prize.equalsIgnoreCase("goldentoken")) emote = EmoteUtil.getEmoteMention("Golden_Tavern_Token");
            if (prize.startsWith("epic")) emote = new Item(-1, null, prize).getEmote();
            if (emote == null) emote = EmoteUtil.getEmoteMention(prize);
            rewards2 += "- " + emote + " `x" + FormatUtil.formatCommas(amount) + "`";
        }
    p.getEcoUser().addBalance(totalMoneyReward);
    t.getEcoUser().addBalance(totalMoneyReward2);
    if (totalMoneyReward.compareTo(BigInteger.ZERO) > 0) {
        rewards += "- " + EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(totalMoneyReward.toString());
    }
    if (totalMoneyReward2.compareTo(BigInteger.ZERO) > 0) {
        rewards2 += "- " + EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(totalMoneyReward2.toString());
    }
        e.reply(MessageUtil.success(Language.getLocalized(uid, "tavern_plain", EmoteUtil.getEmoteMention("Holiday_Open_Present") + " Present"),
                Language.getLocalized(uid, "present_reward", p.getMention() + " Contents;\n" + "{rewards}\n\n" +
                                t.getMention() + " Contents;\n" + "{rewards2}\n\n" +
                                "You have {gifts_remaining} " + EmoteUtil.getEmoteMention("Holiday_Present") + " Gifts remaining.")
                        .replace("{gifts_remaining}", "" + PresentsUtil.getHolidayPresents(uid))
                        .replace("{rewards}", rewards)
                        .replace("{rewards2}", rewards2)));
        for (String prize : presentRewards.keySet()) {
            if (prize.equalsIgnoreCase("money")) continue;
            int amount = presentRewards.get(prize);
            if (prize.endsWith(" key")) {
                KeysUtil.addKeys(uid, prize, amount);
            }
            if (prize.equals("diamonds")) {
                DiamondsUtil.addDiamonds(uid, amount);
            }
            if (prize.equalsIgnoreCase("token")) {
                TokensUtil.addTokens(uid, amount);
            }
            if (prize.equalsIgnoreCase("goldentoken")) {
                GoldenTokensUtil.addGoldenTokens(uid, amount);
            }
            if (prize.startsWith("epic")) {
                p.getInventoryUser().addItem(prize, 1);
            }
        }
        for (String prize : presentRewards2.keySet()) {
            if (prize.equalsIgnoreCase("money")) continue;
            int amount = presentRewards.get(prize);
            if (prize.endsWith(" key")) {
                KeysUtil.addKeys(t.getUserId(), prize, amount);
            }
            if (prize.equals("diamonds")) {
                DiamondsUtil.addDiamonds(t.getUserId(), amount);
            }
            if (prize.equalsIgnoreCase("token")) {
                TokensUtil.addTokens(t.getUserId(), amount);
            }
            if (prize.equalsIgnoreCase("goldentoken")) {
                GoldenTokensUtil.addGoldenTokens(t.getUserId(), amount);
            }
            if (prize.startsWith("epic")) {
                t.getInventoryUser().addItem(prize, 1);
            }
        }
}

    private static String getPrize() {
        HashMap<String, Integer> prizes = new HashMap<>();
        prizes.put("dragon steel key:1", 12);
        prizes.put("titan alloy key:1", 3);
        prizes.put("steel key:1", 30);
        prizes.put("{money2}:5", 215);
        prizes.put("{money1}:2", 255);
        prizes.put("{money3}:7", 150);
        prizes.put("diamonds:500", 160);
        prizes.put("token:1", 50);
        prizes.put("goldentoken:1", 100);
        prizes.put("epic:1", 25);
        //
        final int[] percentage = new int[]{0};
        prizes.forEach((s, p) -> percentage[0] += p);
        int perc = percentage[0];
        String won = null;
        Random random1 = new Random();
        for (String reward : prizes.keySet()) {
            int random = random1.nextInt(perc);
            int chance = prizes.get(reward);
            if (random < chance) {
                won = reward;
                break;
            }
            perc -= chance;
        }
        return won;
    }
}
