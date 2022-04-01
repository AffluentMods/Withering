package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.GoldenTavernSpinEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.*;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Random;

public class GoldenTavernCommand extends BotCommand {

    public GoldenTavernCommand() {
        this.name = "goldentavern";
        this.aliases = new String[]{"gt"};
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
                    Language.getLocalized(uid, "golden_tokens_usage",
                                    "Please use {command_usage}.\n`<>` - Required Parameter\n`[]` - Optional Parameter\n\nYou currently have {goldentokens} Golden Gokens.")
                            .replace("{command_usage}", "`" + userPrefix + "goldentavern <spin> [spin amount/all]`")
                            .replace("{goldentokens}", "`" +GoldenTokensUtil.getGoldenTokens(uid) + "` " + EmoteUtil.getEmoteMention("Golden_Tavern_Token"))));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("spin")) {
            int goldentokens = GoldenTokensUtil.getGoldenTokens(uid);
            if (goldentokens <= 0) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_golden_tokens",
                        "You're out, now quit gambling!\nYou need golden tokens to use this.")));
                return;
            }
            final int spinLimit = 100;
            int spinAmount = 1;
            if (args.length > 1) {
                try {
                    spinAmount = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    if (args[1].equalsIgnoreCase("all")) spinAmount = Math.min(100, GoldenTokensUtil.getGoldenTokens(uid)); //change to 10
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
            if (goldentokens < spinAmount) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "too_few_tokens",
                                "Looking a little empty there!\nYou have {goldentokens} golden tokens.")
                        .replace("{goldentokens}", "`" + goldentokens + "`")));
                return;
            }
            if (spinAmount == 10) {
                GoldenTokensUtil.setGoldenTokens(uid, goldentokens - spinAmount + 1);
            } else if (spinAmount == 100) {
                GoldenTokensUtil.setGoldenTokens(uid, goldentokens - spinAmount + 10);
            } else {
                GoldenTokensUtil.setGoldenTokens(uid, goldentokens - spinAmount);
            }
            Player p = Player.getPlayer(uid);
            GoldenTavernSpinEvent goldenTavernSpinEvent = new GoldenTavernSpinEvent(p, spinAmount);
            EventManager.callEvent(goldenTavernSpinEvent);
            int tokens_spun = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "tavern_spins", "0"));
            tokens_spun += spinAmount;
            StatsUtil.setStat(p.getUserId(), "tavern_spins", String.valueOf(tokens_spun));
            HashMap<String, Integer> goldentavernRewards = new HashMap<>();
            String rewards = "";
            for (int i = 0; i < spinAmount; i++) {
                String[] wonData = getPrize().split(":");
                String prize = wonData[0].toLowerCase();
                int amount = Integer.parseInt(wonData[1]);
                String theReward = "";
                if (prize.endsWith(" key")) {
                    theReward = prize.toLowerCase();
                }
                if (prize.equals("diamonds")) {
                    theReward = "diamonds";
                }
                if (prize.equalsIgnoreCase("scrolls")) {
                    theReward = "scrolls";
                }
                if (prize.equalsIgnoreCase("iron scrolls")) {
                    theReward = "iron scrolls";
                }
                if (prize.equalsIgnoreCase("dragon steel scrolls")) {
                    theReward = "dragon steel scrolls";
                }
                if (prize.equalsIgnoreCase("golden tokens")) {
                    theReward = "golden tokens";
                }
                if (prize.equalsIgnoreCase("legend")) {
                    ItemType it = Rank.getHighestMaterial(p.getExpUser().getLevel());
                    if (it == ItemType.TITAN_ALLOY || it == ItemType.WITHER)  it = ItemType.DRAGON_STEEL;
                    String theArmor = RandomUtil.getRandomArmor(it);
                    String item = "legend " + theArmor;
                    theReward = item;
                }
                goldentavernRewards
                        .put(theReward.toLowerCase(), goldentavernRewards.getOrDefault(theReward.toLowerCase(), 0) + amount);
            }
            for (String prize : goldentavernRewards.keySet()) {
                int amount = goldentavernRewards.get(prize);
                String emote = null;
                if (prize.equalsIgnoreCase("diamonds")) emote = EmoteUtil.getEmoteMention("Diamond");
                if (prize.equalsIgnoreCase("scrolls")) emote = EmoteUtil.getEmoteMention("Scroll");
                if (prize.equalsIgnoreCase("iron scrolls")) emote = EmoteUtil.getEmoteMention("Iron Scroll");
                if (prize.equalsIgnoreCase("dragon steel scrolls")) emote = EmoteUtil.getEmoteMention("Dragon Steel Scroll");
                if (prize.equalsIgnoreCase("golden tokens")) emote = EmoteUtil.getEmoteMention("Golden_Tavern_Token");
                if (prize.startsWith("legend")) emote = new Item(-1, null, prize).getEmote();
                if (emote == null) emote = EmoteUtil.getEmoteMention(prize);
                rewards += "- " + emote + " `x" + FormatUtil.formatCommas(amount) + "`\n";
            }
            e.reply(MessageUtil.success(Language.getLocalized(uid, "golden_tavern_plain", "Golden Tavern"),
                    Language.getLocalized(uid, "golden_tavern_reward", "Congratulations!\nYou won:\n{rewards}")
                            .replace("{rewards}", rewards)));
            for (String prize : goldentavernRewards.keySet()) {
                if (prize.equalsIgnoreCase("money")) continue;
                int amount = goldentavernRewards.get(prize);
                if (prize.endsWith(" key")) KeysUtil.addKeys(uid, prize, amount);
                if (prize.equals("diamonds")) DiamondsUtil.addDiamonds(uid, amount);
                if (prize.equalsIgnoreCase("scrolls")) ScrollsUtil.addScrolls(uid, amount);
                if (prize.equalsIgnoreCase("iron scrolls")) IronScrollsUtil.addIronScrolls(uid, amount);
                if (prize.equalsIgnoreCase("dragon steel scrolls")) DragonScrollsUtil.addDragonScrolls(uid, amount);
                if (prize.equalsIgnoreCase("golden tokens")) GoldenTokensUtil.addGoldenTokens(uid, amount);
                if (prize.startsWith("legend")) p.getInventoryUser().addItem(prize, 1);
            }
            return;
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    private static String getPrize() {
        HashMap<String, Integer> prizes = new HashMap<>();
        prizes.put("dragon steel key:1", 80);
        prizes.put("titan alloy key:1", 20);
        prizes.put("diamonds:500", 325);
        prizes.put("scrolls:1", 250);
        prizes.put("iron scrolls:1", 150);
        prizes.put("dragon steel scrolls:1", 50);
        prizes.put("golden tokens:2", 100);
        prizes.put("legend:1", 25);
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