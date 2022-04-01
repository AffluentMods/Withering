package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.TavernSpinEvent;
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

public class TavernCommand extends BotCommand {

    public TavernCommand() {
        this.name = "tavern";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "tokens_usage",
                            "Please use {command_usage}.\n`<>` - Required Parameter\n`[]` - Optional Parameter\n\nYou currently have {tokens} Tokens.")
                            .replace("{command_usage}", "`" + userPrefix + "tavern <spin> [spin amount/all]`")
                            .replace("{tokens}", "`" + TokensUtil.getTokens(uid) + "` " + EmoteUtil.getEmoteMention("Tavern_Token"))));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("spin")) {
            int tokens = TokensUtil.getTokens(uid);
            if (tokens <= 0) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_tokens",
                        "You're out, now quit gambling!\nYou need tokens to use this.")));
                return;
            }
            final int spinLimit = 100;
            int spinAmount = 1;
            if (args.length > 1) {
                try {
                    spinAmount = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    if (args[1].equalsIgnoreCase("all")) spinAmount = Math.min(100, TokensUtil.getTokens(uid)); //change to 10
                    else {
                        e.reply(MessageUtil.err(Constants.ERROR(uid),
                                Language.getLocalized(uid, "parameter_number_required",
                                        "The argument {argument} must be a number!")
                                        .replace("{argument}", "`[spin amount]`")));
                        return;
                    }
                }
            }
            if (spinAmount > spinLimit || spinAmount < 1) {
                String msg = Language.getLocalized(uid, "argument_between",
                        "The argument {argument} has to be between {min} and {max}!")
                        .replace("{argument}", "[spin amount]").replace("{min}", "1").replace("{max}", spinLimit + "");
                e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                return;
            }
            if (tokens < spinAmount) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "too_few_tokens",
                        "Looking a little empty there!\nYou have {tokens} tokens.")
                        .replace("{tokens}", "`" + tokens + "`")));
                return;
            }
            if (spinAmount == 100) {
                TokensUtil.setTokens(uid, tokens - (spinAmount -  10));
            } else {
                TokensUtil.setTokens(uid, tokens - spinAmount);
            }
            Player p = Player.getPlayer(uid);
            int tokens_spun = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "tavern_spins", "0"));
            tokens_spun += spinAmount;
            StatsUtil.setStat(p.getUserId(), "tavern_spins", String.valueOf(tokens_spun));
            TavernSpinEvent tavernSpinEvent = new TavernSpinEvent(p, spinAmount);
            EventManager.callEvent(tavernSpinEvent);
            HashMap<String, Integer> tavernRewards = new HashMap<>();
            BigInteger totalMoneyReward = BigInteger.ZERO;
            String rewards = "";
            for (int i = 0; i < spinAmount; i++) {
                String[] wonData = getPrize().split(":");
                String prize = wonData[0].toLowerCase();
                int amount = Integer.parseInt(wonData[1]);
                String theReward = "";
                if (prize.endsWith(" key")) {
                    theReward = prize.toLowerCase();
                }
                if (prize.equals("money")) {
                    totalMoneyReward = totalMoneyReward.add(BigInteger.valueOf(amount));
                    theReward = "money";
                }
                if (prize.equals("{money}")) {
                    BigInteger moneyReward =
                            new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf((amount / 100.0)))
                                    .toBigInteger();
                    if (moneyReward.compareTo(BigInteger.ZERO) > 0) {
                        if (moneyReward.compareTo(BigInteger.valueOf(35000)) > 0) {
                            moneyReward = BigInteger.valueOf(35000);
                        }
                    }
                    totalMoneyReward = totalMoneyReward.add(moneyReward);
                    theReward = "money";
                }
                if (prize.equals("diamonds")) {
                    theReward = "diamonds";
                }
                if (prize.equalsIgnoreCase("alloy ingot")) {
                    theReward = "alloy ingot";
                }
                if (prize.equalsIgnoreCase("scrolls")) {
                    theReward = "scrolls";
                }
                if (prize.equalsIgnoreCase("tokens")) {
                    theReward = "tokens";
                }
                if (prize.equalsIgnoreCase("rare")) {
                    ItemType it = Rank.getHighestMaterial(p.getExpUser().getLevel());
                    if (it == ItemType.DRAGON_STEEL || it == ItemType.TITAN_ALLOY || it == ItemType.WITHER) it = ItemType.CARBON_STEEL;
                    String theArmor = RandomUtil.getRandomArmor(it);
                    String item = "rare " + theArmor;
                    theReward = item;
                }
                tavernRewards
                        .put(theReward.toLowerCase(), tavernRewards.getOrDefault(theReward.toLowerCase(), 0) + amount);
            }
            for (String prize : tavernRewards.keySet()) {
                if (prize.equalsIgnoreCase("money")) continue;
                int amount = tavernRewards.get(prize);
                String emote = null;
                if (prize.equalsIgnoreCase("diamonds")) emote = EmoteUtil.getEmoteMention("Diamond");
                if (prize.equalsIgnoreCase("scrolls")) emote = EmoteUtil.getEmoteMention("Scroll");
                if (prize.equalsIgnoreCase("tokens")) emote = EmoteUtil.getEmoteMention("Tavern_Token");
                if (prize.equalsIgnoreCase("alloy ingot")) emote = EmoteUtil.getEmoteMention("Alloy_Ingot");
                if (prize.startsWith("rare")) emote = new Item(-1, null, prize).getEmote();
                if (emote == null) emote = EmoteUtil.getEmoteMention(prize);
                rewards += "- " + emote + " `x" + FormatUtil.formatCommas(amount) + "`\n";
            }
            p.getEcoUser().addBalance(totalMoneyReward);
            if (totalMoneyReward.compareTo(BigInteger.ZERO) > 0) {
                rewards += "- " + EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(totalMoneyReward.toString());
            }
            e.reply(MessageUtil.success(Language.getLocalized(uid, "tavern_plain", "Tavern"),
                    Language.getLocalized(uid, "tavern_reward", "Congratulations!\nYou won:\n{rewards}")
                            .replace("{rewards}", rewards)));
            for (String prize : tavernRewards.keySet()) {
                if (prize.equalsIgnoreCase("money")) continue;
                int amount = tavernRewards.get(prize);
                if (prize.endsWith(" key")) KeysUtil.addKeys(uid, prize, amount);
                if (prize.equals("diamonds")) DiamondsUtil.addDiamonds(uid, amount);
                if (prize.equalsIgnoreCase("scrolls")) ScrollsUtil.addScrolls(uid, amount);
                if (prize.equalsIgnoreCase("tokens")) TokensUtil.addTokens(uid, amount);
                if (prize.equalsIgnoreCase("alloy ingot")) IngotUtil.addIngots(uid, amount);
                if (prize.startsWith("rare")) p.getInventoryUser().addItem(prize, 1);
            }
            return;
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    private static String getPrize() {
        HashMap<String, Integer> prizes = new HashMap<>();
        prizes.put("alloy ingot:10", 60);
        prizes.put("dragon steel key:1", 12);
        prizes.put("titan alloy key:1", 3);
        prizes.put("steel key:1", 30);
        prizes.put("{money}:2", 440);
        prizes.put("diamonds:350", 160);
        prizes.put("scrolls:1", 180);
        prizes.put("tokens:2", 100);
        prizes.put("rare:1", 25);
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