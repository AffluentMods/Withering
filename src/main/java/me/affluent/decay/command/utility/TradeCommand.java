package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.trade.Trade;
import me.affluent.decay.trade.TradeAPI;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.IronmanUtil;
import me.affluent.decay.util.MentionUtil;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.math.BigInteger;
import java.util.HashMap;

public class TradeCommand extends BotCommand {

    public TradeCommand() {
        this.name = "trade";
        this.cooldown = 0.75;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "trade_usage",
                            "Please use {command_usage}.\n" +
                                    "`<>` - Required Parameter\n" +
                                    "`[]` - Optional Parameter\n\n" +

                                    "__Commands:__\n" +
                                    "{trade_user} to trade with a user.\n" +
                                    "{trade_accept} - Accepts your current inbound trade.\n" +
                                    "{trade_decline} - Declines your current trade.\n\n" +
                                    "{trade_add} - Adds money, an item (by ID), or pets (by ID) to the trade.\n" +
                                    "{trade_remove} - Removes money, or an item (by ID), or pets (by ID) from the trade.\n\n" +

                                    "{trade_check} - Checks whats currently up for trade.\n" +
                                    "{trade_confirm} - Locks the trade so nothing can be changed. Finishes trade once both parties confirm.\n\n " +

                                    "Trading has a `5%` tax on all trades involving " + EmoteUtil.getCoin() + ". While trading is " +
                                    "open to all players with no limits, All trades are logged; anyone who abuses the trading system " +
                                    "i.e Using alts to boost a main, may be banned from trading.")
                            .replace("{command_usage}", "`" + userPrefix + "trade <parameter>`")
                            .replace("{trade_user}", "`" + userPrefix + "trade @user`")
                            .replace("{trade_accept}", "`" + userPrefix + "trade accept`")
                            .replace("{trade_decline}", "`" + userPrefix + "trade decline`")
                            .replace("{trade_add}", "`" + userPrefix + "trade add <money | item ID | pet> [amount | pet ID]`")
                            .replace("{trade_remove}", "`" + userPrefix + "trade remove <money | item ID | pet> [pet ID]`")
                            .replace("{trade_check}", "`" + userPrefix + "trade check`")
                            .replace("{trade_confirm}", "`" + userPrefix + "trade confirm`")));
            return;
        }
        String ironman = IronmanUtil.getIronmanMode(uid).getIronmanMode();
        if (ironman.equalsIgnoreCase("ironman") || ironman.equalsIgnoreCase("hardcore")) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "ironman", "Ironman players are not allowed to trade!")));
            return;
        }
        String arg = args[0].toLowerCase();
        boolean mentioned = false;
        User t = MentionUtil.getUser(e.getMessage());
        if (t != null) {
            mentioned = true;
        }
        if (arg.equals("confirm")) {
            if (!TradeAPI.isInTrade(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_trade", "You aren't in a trade!")));
                return;
            }
            Trade trade = TradeAPI.getTrade(uid);
            boolean isInChannel = isInChannel(e.getTextChannel(), trade);
            if (!isInChannel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_modify_only_channel",
                        "You can modify the trade only in {channel}!")
                        .replace("{channel}", "#" + trade.getTextChannel().getName())));
                return;
            }
            if (trade.isLocked()) {
                if (trade.getLockedBy().equals(uid)) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_already_locked",
                            "The trade is already locked!\nThe other trader needs to confirm for completing the trade" +
                            ".")));
                    return;
                }
                trade.finishTrade();
                if (trade.tl1 > 0 || trade.tl2 > 0) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "trade_plain", "Trade"),
                        Language.getLocalized(uid, "trade_completed", "Successfully completed trade!\n" + "{Trade Tax}")
                                .replace("{Trade Tax}", EmoteUtil.getCoin() + " 5% was taken in the process for tax." )));
                } else {
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "trade_plain", "Trade"),
                            Language.getLocalized(uid, "trade_completed", "Successfully completed trade!\n")));
                }
                return;
            }
            trade.lock(uid);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "trade_plain", "Trade"),
                    Language.getLocalized(uid, "trade_locked",
                            "The trade is now locked! No one can modify it anymore.\nThe other trader needs to " +
                            "confirm for completing the trade.")));
            return;
        }
        if (arg.equals("remove")) {
            if (!TradeAPI.isInTrade(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_trade", "You aren't in a trade!")));
                return;
            }
            Trade trade = TradeAPI.getTrade(uid);
            boolean isInChannel = isInChannel(e.getTextChannel(), trade);
            if (!isInChannel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_modify_only_channel",
                        "You can modify the trade only in {channel}!")
                        .replace("{channel}", "#" + trade.getTextChannel().getName())));
                return;
            }
            if (trade.isLocked()) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "trade_locked", "The trade is locked!\nYou can't modify it!")));
                return;
            }
            if (args.length < 2) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "trade_invalid_argument", "Please use {command_usage}.")
                                .replace("{command_usage}", "`" + userPrefix + "trade remove <money/ID>`")));
                return;
            }
            long money = -1L;
            boolean invalidPet = false;
            boolean invalidItem = false;
            boolean invalidMoney = false;
            boolean invalidArgument = false;
            Pets petItem = null;
            Item item = null;
            String firstSubarg = args[1].toLowerCase();
            try {
                long itemID = Long.parseLong(firstSubarg);
                HashMap<Long, Item> tradeItems = trade.getItems(uid);
                if (tradeItems.containsKey(itemID)) item = tradeItems.get(itemID);
                if (item == null || !item.getOwner().equalsIgnoreCase(uid)) invalidItem = true;
            } catch (NumberFormatException ex) {
                if (firstSubarg.equalsIgnoreCase("money")) {
                    if (args.length < 3) {
                        invalidMoney = true;
                    } else {
                        String secondSubarg = args[2].toLowerCase();
                        try {
                            money = Long.parseLong(secondSubarg);
                            if (money <= 0) invalidMoney = true;
                        } catch (NumberFormatException ex1) {
                            if (secondSubarg.equalsIgnoreCase("all")) money = trade.getMoney(uid);
                            else invalidMoney = true;
                        }
                    }
                } else if (firstSubarg.equalsIgnoreCase("pet")) {
                    String secondSubarg = args[2].toLowerCase();
                    try {
                        long petID = Long.parseLong(secondSubarg);
                        HashMap<Long, Pets> tradePetItems = trade.getPetItems(uid);
                        if (tradePetItems.containsKey(petID)) petItem = tradePetItems.get(petID);
                        if (petItem == null || !petItem.getPetOwner().equalsIgnoreCase(uid)) invalidPet = true;
                    } catch (NumberFormatException exception) {
                        invalidArgument = true;
                    }
                } else invalidArgument = true;
            }
            if (invalidItem) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_item", "Invalid Item.")));
                return;
            }
            if (invalidPet) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_pet", "Invalid Pet.")));
                return;
            }
            if (invalidMoney) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_remove_money_invalid",
                        "Please provide a valid amount of money.\nUsage: {trade_remove_money_usage}\nThe argument " +
                        "<money> must be bigger than {trade_remove_min_money}!").replace("{trade_remove_money_usage}",
                        "`" + userPrefix + "trade remove money <amount/all>`")
                        .replace("{trade_remove_min_money}", "`0`")));
                return;
            }
            if (invalidArgument) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
                return;
            }
            Player p = Player.getPlayer(uid);
            if (item != null) {
                if (trade.getP1().equalsIgnoreCase(uid)) trade.removeP1Item(item);
                else trade.removeP2Item(item);
                p.getInventoryUser().addItem(item.getID(), item.getName());
                e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_plain", "Trade"),
                        Language.getLocalized(uid, "trade_item_removed", "Removed {item} from the trade!")
                                .replace("{item}", "`" + item.getName() + "`")));
                return;
            }
            if (petItem != null) {
                if (trade.getP1().equalsIgnoreCase(uid)) trade.removeP1PetItem(petItem);
                else trade.removeP2PetItem(petItem);
                p.getPetUtil().addPetOld(petItem.getPetID(), petItem.getPetName());
                e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_plain", "Trade"),
                        Language.getLocalized(uid, "trade_pet_removed", "Removed {pet} from the trade!")
                                .replace("{pet}", "`" + petItem.getPetName() + "`")));
                return;
            }
            if (trade.getP1().equalsIgnoreCase(uid)) trade.removeP1Money(money);
            else trade.removeP2Money(money);
            p.getEcoUser().addBalance(money);
            long tradeMoney = trade.getMoney(uid);
            e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_plain", "Trade"),
                    Language.getLocalized(uid, "trade_money_removed",
                            "Removed {amount} from the trade!\nYou now offer {trade_money} to trade.")
                            .replace("{amount}", EmoteUtil.getCoin() + " `" + FormatUtil.formatCommas(money) + "`")
                            .replace("{trade_money}",
                                    EmoteUtil.getCoin() + " `" + FormatUtil.formatCommas(tradeMoney) + "`")));
            return;
        }
        if (arg.equals("add")) {
            if (!TradeAPI.isInTrade(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_trade", "You aren't in a trade!")));
                return;
            }
            Trade trade = TradeAPI.getTrade(uid);
            boolean isInChannel = isInChannel(e.getTextChannel(), trade);
            if (!isInChannel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_modify_only_channel",
                        "You can modify the trade only in {channel}!")
                        .replace("{channel}", "#" + trade.getTextChannel().getName())));
                return;
            }
            if (trade.isLocked()) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "trade_locked", "The trade is locked!\nYou can't modify it!")));
                return;
            }
            if (args.length < 2) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "trade_invalid_argument", "Please use {command_usage}.")
                                .replace("{command_usage}", "`" + userPrefix + "trade add <money/ID>`")));
                return;
            }
            long money = -1L;
            boolean invalidPet = false;
            boolean invalidItem = false;
            boolean invalidMoney = false;
            boolean invalidArgument = false;
            Item item = null;
            Pets petItem = null;
            String firstSubarg = args[1].toLowerCase();
            try {
                long itemID = Long.parseLong(firstSubarg);
                item = InventoryUser.getItemByID(itemID);
                if (item == null || !item.getOwner().equalsIgnoreCase(uid)) invalidItem = true;
            } catch (NumberFormatException ex) {
                if (firstSubarg.equalsIgnoreCase("money")) {
                    if (args.length < 3) {
                        invalidMoney = true;
                        money = -1L;
                    } else {
                        String secondSubarg = args[2].toLowerCase();
                        try {
                            money = Long.parseLong(secondSubarg);
                            if (money <= 0) invalidMoney = true;
                        } catch (NumberFormatException ex1) {
                            invalidMoney = true;
                            money = -1L;
                        }
                    }
                } else if (firstSubarg.equalsIgnoreCase("pet")) {
                    String secondSubarg = args[2].toLowerCase();
                    try {
                        long petID = Long.parseLong(secondSubarg);
                        petItem = PetUtil.getPetByID(petID);
                        if (petItem == null || !petItem.getPetOwner().equalsIgnoreCase(uid)) invalidPet = true;
                    } catch (NumberFormatException exception) {
                        invalidArgument = true;
                    }
                } else invalidArgument = true;
            }
            if (invalidItem) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_item", "Invalid Item.")));
                return;
            }
            if (invalidPet) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_pet", "Invalid Pet.")));
                return;
            }
            if (invalidMoney) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_add_money_invalid",
                        "Please provide a valid amount of money.\nUsage: {trade_add_money_usage}\nThe argument " +
                        "<money> must be bigger than {trade_add_min_money}!")
                        .replace("{trade_add_money_usage}", "`" + userPrefix + "trade add money <amount>`")
                        .replace("{trade_add_min_money}", "`0`")));
                return;
            }
            if (invalidArgument) {
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
                return;
            }
            Player p = Player.getPlayer(uid);
            if (item != null) {
                p.getInventoryUser().removeItem(item.getID());
                if (trade.getP1().equalsIgnoreCase(uid)) trade.addP1Item(item);
                else trade.addP2Item(item);
                e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_plain", "Trade"),
                        Language.getLocalized(uid, "trade_item_added", "Added {item} to the trade!")
                                .replace("{item}", "`" + item.getName() + "`")));
                return;
            }
            if (petItem != null) {
                p.getPetUtil().removePet(petItem.getPetID());
                if (trade.getP1().equalsIgnoreCase(uid)) trade.addP1PetItem(petItem);
                else trade.addP2PetItem(petItem);
                e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_plain", "Trade"),
                        Language.getLocalized(uid, "trade_pet_added", "Added {pet} to the trade!")
                                .replace("{pet}", "`" + petItem.getPetName() + "`")));
                return;
            }
            if (p.getEcoUser().getBalance().compareTo(BigInteger.valueOf(money)) < 0) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "not_enough_money",
                        "You don't have so much money!\nYou have {balance}.").replace("{balance}",
                        "`" + FormatUtil.formatCommas(p.getEcoUser().getBalance().toString()) + "`" +
                        EmoteUtil.getCoin())));
                return;
            }
            p.getEcoUser().removeBalance(money);
            if (trade.getP1().equalsIgnoreCase(uid)) trade.addP1Money(money);
            else trade.addP2Money(money);
            long tradeMoney = trade.getMoney(uid);
            e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_plain", "Trade"),
                    Language.getLocalized(uid, "trade_money_added",
                                    "Added {amount} to the trade!\nYou now offer {trade_money} to trade.")
                            .replace("{amount}", EmoteUtil.getCoin() + " `" + FormatUtil.formatCommas(money) + "`")
                            .replace("{trade_money}",
                                    EmoteUtil.getCoin() + " `" + FormatUtil.formatCommas(tradeMoney) + "`")));
            return;
        }
        if (arg.equals("check") || arg.equals("info")) {
            if (!TradeAPI.isInTrade(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_trade", "You aren't in a trade!")));
                return;
            }
            Trade trade = TradeAPI.getTrade(uid);
            double money1 = trade.getMoney1();
            double money2 = trade.getMoney2();
            HashMap<Long, Item> items1 = trade.getItemsP1();
            HashMap<Long, Item> items2 = trade.getItemsP2();
            HashMap<Long, Pets> petItems1 = trade.getPetItemsP1();
            HashMap<Long, Pets> petItems2 = trade.getPetItemsP2();
            //
            String moneyvalue1 = "Gold Coins: " + EmoteUtil.getCoin() + " " + money1;
            String moneyvalue2 = "Gold Coins: " + EmoteUtil.getCoin() + " " + money2;
            StringBuilder value1 = new StringBuilder("Items: ");
            StringBuilder value2 = new StringBuilder("Items: ");
            Player p1 = trade.getPlayer1();
            Player p2 = trade.getPlayer2();
            User u1 = p1.getUser();
            User u2 = p2.getUser();
            for (long petID : petItems1.keySet()) {
                Pets petItem = petItems1.get(petID);
                value1.append("\n[`").append(petID).append("`] ").append(petItem.getPetEmote()).append("`")
                        .append(petItem.getPetName().replace("_", " ").toLowerCase()).append("` ").append(petItem.getPetStars(u1.getId(), String.valueOf(petID))).append(" ")
                        .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(petItem.getPetLevel(u1.getId(), String.valueOf(petID)));
            }
            for (long itemID : items1.keySet()) {
                Item item = items1.get(itemID);
                value1.append("\n[`").append(itemID).append("`] ").append(item.getEmote()).append("`")
                        .append(item.getName()).append("` ").append(item.getSpecificStars(u1.getId(), Math.toIntExact(itemID))).append(" ")
                        .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(item.getSpecificLevel(u1.getId(), Math.toIntExact(itemID)));
            }
            for (long petID : petItems2.keySet()) {
                Pets petItem = petItems2.get(petID);
                value2.append("\n[`").append(petID).append("`] ").append(petItem.getPetEmote()).append("`")
                        .append(petItem.getPetName().replace("_", " ").toLowerCase()).append("` ").append(petItem.getPetStars(u2.getId(), String.valueOf(petID))).append(" ")
                        .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(petItem.getPetLevel(u2.getId(), String.valueOf(petID)));
            }
            for (long itemID : items2.keySet()) {
                Item item = items2.get(itemID);
                value2.append("\n[`").append(itemID).append("`] ").append(item.getEmote()).append("`")
                        .append(item.getName()).append("` ").append(item.getSpecificStars(u2.getId(), Math.toIntExact(itemID))).append(" ")
                        .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(item.getSpecificLevel(u2.getId(), Math.toIntExact(itemID)));
            }
            //
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "trade_plain", "Trade"));
            eb.addField(Player.getDisplay(u1), moneyvalue1 + "\n" + value1, true);
            eb.addField(Player.getDisplay(u2), moneyvalue2 + "\n" + value2, true);
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            return;
        }
        if (arg.equals("accept")) {
            if (!TradeAPI.hasTradeRequest(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "trade_no_request", "You didn't receive a trade request!")));
                return;
            }
            String requester = TradeAPI.getRequestFrom(uid);
            if (TradeAPI.isInTrade(requester)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_target_trading",
                        "This player is already trading with somebody!")));
                return;
            }
            Trade trade = new Trade(uid, requester, e.getTextChannel());
            TradeAPI.setTrade(uid, trade);
            TradeAPI.setTrade(requester, trade);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "trade_plain", "Trade"),
                    Language.getLocalized(uid, "trade_now_in_trade", "You are now in a trade!")));
            TradeAPI.cancelTimeoutTimer(uid);
            TradeAPI.runTradeTimer(uid);
            TradeAPI.setRequests(uid, null);
            return;
        }
        if (arg.equals("deny") || arg.equals("decline")) {
            if (!TradeAPI.hasTradeRequest(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "trade_no_request", "You didn't receive a trade request!")));
                return;
            }
            TradeAPI.setRequests(uid, null);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "trade_plain", "Trade"),
                    Language.getLocalized(uid, "trade_declined", "You declined the trade request.")));
            TradeAPI.cancelTimeoutTimer(uid);
            return;
        }
        if (arg.equals("cancel")) {
            if (!TradeAPI.isInTrade(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_trade", "You aren't in a trade!")));
                return;
            }
            Trade trade = TradeAPI.getTrade(uid);
            boolean isInChannel = isInChannel(e.getTextChannel(), trade);
            if (!isInChannel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_modify_only_channel",
                        "You can modify the trade only in {channel}!")
                        .replace("{channel}", "#" + trade.getTextChannel().getName())));
                return;
            }
            cancelTrade(trade);
            e.reply(MessageUtil.success("Trade cancelled", "Successfully cancelled the trade!"));
            return;
        }
        if (mentioned) {
            String mentioned_id = t.getId();

            if (TradeAPI.hasTradeRequest(mentioned_id)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_target_pending_request",
                        "{target_user} already has a pending trade request!")
                        .replace("{target_user}", t.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))));
                return;
            }
            if (TradeAPI.isInTrade(mentioned_id)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "trade_target_trading",
                        "This player is already trading with somebody!")));
                return;
            }
            TradeAPI.setRequests(mentioned_id, uid);
            TradeAPI.runTimeoutTimer(mentioned_id);
            String ironman2 = IronmanUtil.getIronmanMode(mentioned_id).getIronmanMode();
            if (ironman2.equalsIgnoreCase("ironman") || ironman.equalsIgnoreCase("hardcore")) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "ironman", t.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">") + " is an Ironman player. They are not allowed to trade.")));
                return;
            }
            e.reply(Language.getLocalized(uid, "trade_request",
                    "{target_user} > You got a trade request from {requester_user}!\nAccept with {trade_accept} " +
                    "or deny with {trade_decline}.").replace("{target_user}", t.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                    .replace("{requester_user}", u.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                    .replace("{trade_accept}", "`" + userPrefix + "trade accept`")
                    .replace("{trade_decline}", "`" + userPrefix + "trade decline`"));
            return;
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    public static void cancelTrade(Trade trade) {
        HashMap<Long, Item> items1 = trade.getItemsP1();
        HashMap<Long, Item> items2 = trade.getItemsP2();
        HashMap<Long, Pets> petItems1 = trade.getPetItemsP1();
        HashMap<Long, Pets> petItems2 = trade.getPetItemsP2();
        Player p1 = trade.getPlayer1();
        Player p2 = trade.getPlayer2();
        InventoryUser inv1 = p1.getInventoryUser();
        for (long itemID : items1.keySet()) {
            Item item = items1.get(itemID);
            inv1.addItem(itemID, item.getName());
        }
        InventoryUser inv2 = p2.getInventoryUser();
        for (long itemID : items2.keySet()) {
            Item item = items2.get(itemID);
            inv2.addItem(itemID, item.getName());
        }
        PetUtil petInv1 = p1.getPetUtil();
        for (long petID : petItems1.keySet()) {
            Pets petItem = petItems1.get(petID);
            petInv1.addPetOld(petID, petItem.getPetName());
        }
        PetUtil petInv2 = p2.getPetUtil();
        for (long petID : petItems2.keySet()) {
            Pets petItem = petItems2.get(petID);
            petInv2.addPetOld(petID, petItem.getPetName());
        }
        p1.getEcoUser().addBalance(trade.getMoney1());
        p2.getEcoUser().addBalance(trade.getMoney2());
        String userId = p1.getUserId();
        String otherUid = p2.getUserId();
        TradeAPI.cancelTradeTimer(userId);
        TradeAPI.cancelTradeTimer(otherUid);
        TradeAPI.cancelTimeoutTimer(userId);
        TradeAPI.cancelTimeoutTimer(otherUid);
        TradeAPI.setTrade(userId, null);
        TradeAPI.setTrade(otherUid, null);
        trade.clear();
    }

    private boolean isInChannel(TextChannel tc, Trade trade) {
        return trade.getTextChannel().getId().equals(tc.getId());
    }
}