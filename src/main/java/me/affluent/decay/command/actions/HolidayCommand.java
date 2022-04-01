package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.HolidayWinEvent;
import me.affluent.decay.holidaydungeon.HolidayFight;
import me.affluent.decay.holidayevent.HolidayEvent;
import me.affluent.decay.holidayshop.HolidayPrice;
import me.affluent.decay.holidayshop.HolidayPriceItems;
import me.affluent.decay.holidayshop.HolidayShopCategory;
import me.affluent.decay.holidayshop.HolidayShopItem;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

public class HolidayCommand extends BotCommand {

    private static final HashMap<String, HolidayShopCategory> shopCategories = new HashMap<>();

    public HolidayCommand() {
        this.name = "holiday";
        this.cooldown = 2;
        this.aliases = new String[]{"hol", "hd"};
        if (HolidayEvent.getCurrentEvent() != null) {
            HolidayShopCategory ChristmasCategory = new HolidayShopCategory("Christmas",
                    Arrays.asList(
                            new HolidayShopItem("Mythic Snowman", "Mythic_Snowman1", 1, new HolidayPriceItems("Holiday Candy Cane", 1200)),
                            new HolidayShopItem("Scroll", "Scroll", 1, new HolidayPriceItems("Holiday Candy Cane", 15)),
                            new HolidayShopItem("Iron Scroll", "Iron Scroll", 1, new HolidayPriceItems("Holiday Candy Cane", 25)),
                            new HolidayShopItem("Dragon Steel Scroll", "Dragon Steel Scroll", 1, new HolidayPriceItems("Holiday Candy Cane", 35)),
                            new HolidayShopItem("Golden Tavern Token", "Golden Tavern Token", 1, new HolidayPriceItems("Holiday Candy Cane", 40)),
                            new HolidayShopItem("Christmas Badge", "Christmas Badge", 1, new HolidayPriceItems("Holiday Candy Cane", 400))
                    ));
            HolidayShopCategory HalloweenCategory = new HolidayShopCategory("Halloween",
                    Arrays.asList(
                            new HolidayShopItem("Mythic Reaper", "Mythic_Reaper", 1, new HolidayPriceItems("Holiday Purple Candy", 150)),
                            new HolidayShopItem("Scroll", "Scroll", 1, new HolidayPriceItems("Holiday Candy Corn", 15)),
                            new HolidayShopItem("Iron Scroll", "Iron Scroll", 1, new HolidayPriceItems("Holiday Candy Corn", 25)),
                            new HolidayShopItem("Dragon Steel Scroll", "Dragon Steel Scroll", 1, new HolidayPriceItems("Holiday Candy Corn", 35)),
                            new HolidayShopItem("Golden Tavern Token", "Golden Tavern Token", 1, new HolidayPriceItems("Holiday Candy Corn", 40)),
                            new HolidayShopItem("Halloween Badge", "Halloween Badge", 1, new HolidayPriceItems("Holiday Purple Candy", 50))
                    ));
            HolidayShopCategory MiscCategory = new HolidayShopCategory("Misc",
                    Arrays.asList(
                            new HolidayShopItem("Uncommon Scorpion", "Uncommon_Scorpion", 1, new HolidayPriceItems("Diamond", 3000)),
                            new HolidayShopItem("Epic Minotaur", "Epic_Minotaur", 1, new HolidayPriceItems("Diamond", 7500)),
                            new HolidayShopItem("Mythic Serpent", "Mythic_Serpent", 1, new HolidayPriceItems("Diamond", 15000))
                    ));
            if (HolidayEvent.getCurrentEvent().getName().equalsIgnoreCase("christmas")) {
                List<HolidayShopCategory> holidayShopCategoryList = Arrays.asList(ChristmasCategory);
                for (HolidayShopCategory holidayShopCategory : holidayShopCategoryList) {
                    shopCategories.put(holidayShopCategory.getHolidayTitle().toLowerCase(), holidayShopCategory);
                }
            }
            if (HolidayEvent.getCurrentEvent().getName().equalsIgnoreCase("halloween")) {
                List<HolidayShopCategory> holidayShopCategoryList = Arrays.asList(HalloweenCategory);
                for (HolidayShopCategory holidayShopCategory : holidayShopCategoryList) {
                    shopCategories.put(holidayShopCategory.getHolidayTitle().toLowerCase(), holidayShopCategory);
                }
            }
        }
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        boolean isEventActive = HolidayEvent.isEventActive();
        if (!isEventActive) {
            e.reply(MessageUtil.err(Language.getLocalized(uid, "event_plain", "Holiday Event"),
                    Language.getLocalized(uid, "event_not_active", "There is no active Holiday Event, " +
                            "check the `" + userPrefix + "calendar` command for more information.")));
            return;
        }
        HolidayEvent holidayEvent = HolidayEvent.getCurrentEvent();
        String timeLeftDisplay = HolidayEvent.getTimeLeftDisplay(uid) + " left";
        if (args.length < 1) {
            if (holidayEvent.getName().equalsIgnoreCase("new years")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(15, 141, 255));
                eb.setTitle(
                        Language.getLocalized(uid, "event_title", "{event} Event").replace("{event}", holidayEvent.getName()));
                eb.setDescription(Language.getLocalized(uid, "event_description",
                                "{event_description}\n\n__Rewards:__\n{rewards}")
                        .replace("{top_event_command}", "`" + userPrefix + "top holiday`")
                        .replace("{event_description}", holidayEvent.getDescription())
                        .replace("{rewards}", holidayEvent.getRewardsDisplay()));
                eb.setFooter(timeLeftDisplay, "https://i.imgur.com/RbHmy82.png");
                e.reply(eb.build());
                return;
            }
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "holiday <next | info | shop>`")));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equals("info")) {
            if (holidayEvent.getName().equalsIgnoreCase("new years")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(15, 141, 255));
                eb.setTitle(
                        Language.getLocalized(uid, "event_title", "{event} Event").replace("{event}", holidayEvent.getName()));
                eb.setDescription(Language.getLocalized(uid, "event_description",
                                "{event_description}\n\n__Rewards:__\n{rewards}")
                        .replace("{top_event_command}", "`" + userPrefix + "top holiday`")
                        .replace("{event_description}", holidayEvent.getDescription())
                        .replace("{rewards}", holidayEvent.getRewardsDisplay()));
                eb.setFooter(timeLeftDisplay, "https://i.imgur.com/RbHmy82.png");
                e.reply(eb.build());
                return;
            }
            String description = "Use {top_event_command} to view the leaderboard.\n\n " +
            "{event_description}\n\n__Rewards:__\n{rewards}";
            description = description
                    .replace("{top_event_command}", "`" + userPrefix + "top holiday`")
                    .replace("{event_description}", holidayEvent.getDescription())
                    .replace("{rewards}", holidayEvent.getRewardsDisplay());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(15, 141, 255));
            eb.setTitle(
                    Language.getLocalized(uid, "event_title", "{event} Event").replace("{event}", holidayEvent.getName()));
            eb.setDescription(Language.getLocalized(uid, "event_description",
                            description.replace("w.", userPrefix)));
            eb.setFooter(timeLeftDisplay, "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            return;
        }
        if (arg.equals("next")) {
            if (holidayEvent.getName().equalsIgnoreCase("new years")) {
                e.reply(MessageUtil.err(Language.getLocalized(uid, "event_plain", "New Years Event"),
                        Language.getLocalized(uid, "event_not_active", "New Years has no dungeon.")));
                return;
            }
            int stageNumber = HolidayEvent.getCurrentEvent().getStage(uid) + 1;
            HolidayFight holidayFight = new HolidayFight(p, stageNumber);
            boolean win = holidayFight.doFight();
            String npc = "";
            String item = "";
            String itemName = "";
            long holidayNormalItem = getHolidayGain(stageNumber);
            long holidaySpecialItem = getSpecialHolidayGain();
            String specialItem = "";
            if (holidayEvent.getName().equalsIgnoreCase("christmas")) {
                npc = "Grinch";
                item = EmoteUtil.getEmoteMention("Holiday_Candy_Cane");
                itemName = "Candy Cane";
                if (holidaySpecialItem > 0) {
                    specialItem = "\nWith a bonus of " + EmoteUtil.getEmoteMention("Holiday_Present") + "`" + holidaySpecialItem + "`";
                }
            }
            if (holidayEvent.getName().equalsIgnoreCase("halloween")) {
                npc = "Spirit";
                item = EmoteUtil.getEmoteMention("Holiday_Candy_Corn");
                itemName = "Candy Corn";
                if (holidaySpecialItem > 0) {
                    specialItem = "\nWith a bonus of " + EmoteUtil.getEmoteMention("Holiday_Purple_Candy") + "`" + holidaySpecialItem + "`";
                }
            }
            String pTag = u.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">");
            String response_language_id = win ? "quest_fight_win" : "quest_fight_lose";
            String response_default_win =
                    "{player} killed the {npc}\n{player} dealt {player_damage} " +
                            "damage with {player_remaining_health} remaining, while the {npc} dealt {enemy_damage} damage.\n" +
                            "You gained {item} {amount}" +
                            "{special_item}";
            String response_default_lose = "{player} died against the {npc}\nThe {npc} dealt " +
                    "{enemy_damage} damage with {enemy_remaining_health} remaining, while " +
                    "{player} dealt {player_damage} damage.";
            int playerDamage = holidayFight.getPlayerDamage();
            int playerHealth = holidayFight.getPlayerHealth();
            int enemyDamage = holidayFight.getEnemyDamage();
            int enemyHealth = holidayFight.getEnemyHealth();
            String response_default = win ? response_default_win : response_default_lose;
            response_default = "Stage " + stageNumber + "\n" + response_default;
            response_default =
                    response_default
                            .replace("{special_item}", specialItem)
                            .replace("{item}", item)
                            .replace("{npc}", npc)
                            .replace("{player}", pTag).replace("{player_damage}", String.valueOf(playerDamage))
                            .replace("{player_remaining_health}", EmoteUtil.getEmoteMention("HP") + " " + playerHealth)
                            .replace("{enemy_damage}", String.valueOf(enemyDamage))
                            .replace("{enemy_remaining_health}", EmoteUtil.getEmoteMention("HP") + " " + enemyHealth)
                            .replace("{amount}", "`x" + holidayNormalItem + "`" + itemName);
            String response = Language.getLocalized(uid, response_language_id, response_default);
            EmbedBuilder eb = new EmbedBuilder();
            if (win) eb.setAuthor("Dungeon Completed", null, "https://thewithering.com/" + npc + ".png");
            else eb.setAuthor("Dungeon Failed", null, "https://thewithering.com/" + npc + ".png");
            eb.setDescription(response);
            eb.setColor(win ? new Color(19, 255, 58) : new Color(250, 58, 35));
            eb.setFooter(timeLeftDisplay, "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            if (win) {
                if (holidayEvent.getName().equalsIgnoreCase("christmas")) {
                    CandyCaneUtil.addHolidayCandyCane(uid, (int) holidayNormalItem);
                    PresentsUtil.addHolidayPresents(uid, (int) holidaySpecialItem);
                }
                if (holidayEvent.getName().equalsIgnoreCase("halloween")) {
                    CandyCornUtil.addHolidayCorn(uid, (int) holidayNormalItem);
                    PurpleCandyUtil.addHolidayPurple(uid, (int) holidaySpecialItem);
                }
                HolidayEvent.getCurrentEvent().setData(uid, stageNumber);
                HolidayWinEvent holidayWinEvent = new HolidayWinEvent(p, stageNumber);
                EventManager.callEvent(holidayWinEvent);
            }
        }

        if (arg.equalsIgnoreCase("shop") || (arg.equalsIgnoreCase("store")) || (arg.equalsIgnoreCase("merchant"))) {
            if (holidayEvent.getName().equalsIgnoreCase("new years")) {
                e.reply(MessageUtil.err(Language.getLocalized(uid, "event_plain", "New Years Event"),
                        Language.getLocalized(uid, "event_not_active", "New Years has no shop.")));
                return;
            }
            String categoriesString = "- Holiday";
            if (holidayEvent.getName().equalsIgnoreCase("halloween")) categoriesString = "- Halloween";
            if (holidayEvent.getName().equalsIgnoreCase("christmas")) categoriesString = "- Christmas";
            if (args.length < 2) {
                String msg = Language.getLocalized(uid, "holiday_shop_usage",
                                " Use {usage_shop}.\n__Categories:__\n\n{categories}")
                        .replace("{categories}", categoriesString)
                        .replace("{usage_shop}", "`" + userPrefix + "holiday shop <category>`");
                e.reply(MessageUtil.info(Language.getLocalized(uid, "holiday_shop", "Holiday Shop"), msg));
                return;
            }
            List<String> categories = new ArrayList<>(shopCategories.keySet());
            String selectedCategory = args[1].toLowerCase();
            if (selectedCategory.equalsIgnoreCase("list")) {
                String msg = Language.getLocalized(uid, "categories", "__Categories:__\n\n{categories}")
                        .replace("{categories}", categoriesString);
                e.reply(MessageUtil.info(Language.getLocalized(uid, "merchant", "Holiday Shop"), msg));
                return;
            }
            if (!(categories.contains(selectedCategory))) {
                String msg =
                        Language.getLocalized(uid, "invalid_category", "Invalid category.\n__Categories:__\n\n{categories}")
                                .replace("{categories}", categoriesString);
                e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                return;
            }
            HolidayShopCategory holidayShopCategory = shopCategories.get(selectedCategory);
            String shopTitle = holidayShopCategory.getHolidayTitle();
            List<HolidayShopItem> holidayShopItems = holidayShopCategory.getHolidayShopItems();

            if (args.length < 3) {
                StringBuilder itemList = new StringBuilder();
                for (HolidayShopItem holidayShopItem: holidayShopItems) {
                    String toAdd = EmoteUtil.getEmoteMention(holidayShopItem.getHolidayItemName(true)) + " " + holidayShopItem.getHolidayDisplayName(true) + ": ";
                    HolidayPrice price = holidayShopItem.getHolidayPrice();
                    if (price.getHolidayPriceType() == HolidayPrice.HolidayPriceType.MONEY) {
                        long priceMoney = price.getHolidayGoldCoins();
                        toAdd += EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(priceMoney);
                    } else if (price.getHolidayPriceType() == HolidayPrice.HolidayPriceType.ITEMS) {
                        HashMap<String, Long> priceItems = price.getHolidayItems();
                        toAdd += FormatUtil.formatItemListOneLine(priceItems, false, true);
                    } else {
                        long priceMoney = price.getHolidayGoldCoins();
                        HashMap<String, Long> priceItems = price.getHolidayItems();
                        toAdd += EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(priceMoney) + " | " +
                                FormatUtil.formatItemListOneLine(priceItems, false, true);
                    }
                    itemList.append(toAdd).append("\n");
                }
                String msg =
                        Language.getLocalized(uid, "holiday_shop_usage", "To buy an item, use {usage}.\n\n{item_list}")
                                .replace("{item_list}", itemList.toString())
                                .replace("{usage}", "`" + userPrefix + "holiday shop " + selectedCategory + " <item>`");
                e.reply(MessageUtil.info(Language.getLocalized(uid, "shop", "") + shopTitle + " Shop", msg));
                return;
            }
            int selectedAmount = 1;
            StringBuilder itemRequested = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                if (i == (args.length - 1)) {
                    try {
                        selectedAmount = Integer.parseInt(args[i]);
                        int maxAmount = 100;
                        if (selectedAmount < 1 || selectedAmount > maxAmount) {
                            String msg = Language.getLocalized(uid, "argument_between",
                                            "The argument {argument} has to be between {min} and {max}!")
                                    .replace("{argument}", "<amount>").replace("{min}", "1").replace("{max}", "100");
                            e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                            return;
                        }
                        continue;
                    } catch (NumberFormatException ex) {
                        itemRequested.append(args[i]).append(" ");
                        continue;
                    }
                }
                itemRequested.append(args[i]).append(" ");
            }
            if (itemRequested.toString().endsWith(" ")) {
                itemRequested = new StringBuilder(itemRequested.substring(0, itemRequested.length() - 1));
            }
            HolidayShopItem selectedItem = null;
            for (HolidayShopItem holidayShopItem : holidayShopItems) {
                if (holidayShopItem.getHolidayDisplayName(false).equalsIgnoreCase(itemRequested.toString())) {
                    selectedItem = holidayShopItem;
                    break;
                }
            }
            if (selectedItem == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_item", "Invalid item.")));
                return;
            }
            String item = selectedItem.getHolidayItemName(false);
            HolidayPrice holidayPrice = selectedItem.getHolidayPrice();
            if (holidayPrice.getHolidayPriceType() == HolidayPrice.HolidayPriceType.MONEY) {
                long priceMoney = holidayPrice.getHolidayGoldCoins();
                if (p.getEcoUser().getBalance().compareTo(new BigInteger(String.valueOf(priceMoney * selectedAmount))) < 0) {
                    e.reply(Constants.CANT_AFFORD(uid));
                } else {
                    if (item.equalsIgnoreCase("mythic snowman pet")) p.getPetUtil().addPet("mythic snowman", selectedAmount);
                    else if (item.equalsIgnoreCase("mythic reaper pet")) p.getPetUtil().addPet("mythic reaper", selectedAmount);
                    else if (item.startsWith("Iron")) IronScrollsUtil.addIronScrolls(uid, selectedAmount);
                    else if (item.startsWith("Dragon")) DragonScrollsUtil.addDragonScrolls(uid, selectedAmount);
                    else if (item.startsWith("Golden")) GoldenTokensUtil.addGoldenTokens(uid, selectedAmount);
                    else if (item.startsWith("Scroll")) ScrollsUtil.addScrolls(uid, selectedAmount);
                    else p.getInventoryUser().addItem(item, selectedItem.getHolidayAmount() * selectedAmount);
                    p.getEcoUser().removeBalance(priceMoney * selectedAmount);
                    String itemPrice = EmoteUtil.getCoin() + " " + (priceMoney * selectedAmount);
                    String itemDisplay = EmoteUtil.getEmoteMention(selectedItem.getHolidayItemName(true)) + " " +
                            selectedItem.getHolidayItemName(true) + " `x" +
                            FormatUtil.formatCommas((selectedItem.getHolidayAmount() * selectedAmount)) + "`";
                    String msg = Language.getLocalized(uid, "item_bought_msg",
                                    "Successfully bought {item_display} for {item_price}!").replace("{item_display}", itemDisplay)
                            .replace("{item_price}", itemPrice);
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "item_bought", "Item bought"), msg));
                }
            } else {
                HashMap<String, Long> priceItems = holidayPrice.getHolidayItems(selectedAmount);
                boolean hasAllItems = true;
                boolean hasntEnoughDiamonds = false;
                for (String priceItem : priceItems.keySet()) {
                    long priceItemAmount = priceItems.get(priceItem);
                    System.out.println(priceItem);
                    if (priceItem.equalsIgnoreCase("holiday candy corn")) {
                        if (CandyCornUtil.getHolidayCorn(uid) < priceItemAmount) hasAllItems = false;
                    }
                    else if (priceItem.equalsIgnoreCase("holiday purple candy")) {
                        if (PurpleCandyUtil.getHolidayPurple(uid) < priceItemAmount) hasAllItems = false;
                    }
                    else if (priceItem.equalsIgnoreCase("holiday candy cane")) {
                        if (CandyCaneUtil.getHolidayCandyCane(uid) < priceItemAmount) hasAllItems = false;
                    }
                    else if (p.getInventoryUser().hasItem(priceItem) < priceItemAmount) {
                        hasAllItems = false;
                        if (priceItem.equalsIgnoreCase("diamond")) hasntEnoughDiamonds = true;
                        break;
                    }
                }
                if (hasAllItems) {
                    final long finalAmount = selectedItem.getHolidayAmount() * selectedAmount;
                    if (item.equalsIgnoreCase("mythic snowman pet")) p.getPetUtil().addPet("mythic snowman", finalAmount);
                    else if (item.equalsIgnoreCase("mythic reaper pet")) p.getPetUtil().addPet("mythic reaper", finalAmount);
                    else if (item.startsWith("Iron")) IronScrollsUtil.addIronScrolls(uid, (int) finalAmount);
                    else if (item.startsWith("Dragon")) DragonScrollsUtil.addDragonScrolls(uid, (int) finalAmount);
                    else if (item.startsWith("Golden")) GoldenTokensUtil.addGoldenTokens(uid, (int) finalAmount);
                    else if (item.startsWith("Scroll")) ScrollsUtil.addScrolls(uid, (int) finalAmount);
                    for (String priceItem : priceItems.keySet()) {
                        long priceItemAmount = priceItems.get(priceItem);
                        if (priceItem.equalsIgnoreCase("diamond")) {
                            DiamondsUtil.setDiamonds(uid, DiamondsUtil.getDiamonds(uid) - priceItemAmount);
                            continue;
                        } else if (priceItem.equalsIgnoreCase("holiday candy corn")) {
                            CandyCornUtil.setHolidayCorn(uid, (int) (CandyCornUtil.getHolidayCorn(uid) - priceItemAmount));
                            continue;
                        } else if (priceItem.equalsIgnoreCase("holiday candy cane")) {
                            CandyCaneUtil.setHolidayCandyCane(uid, (int) (CandyCaneUtil.getHolidayCandyCane(uid) - priceItemAmount));
                            continue;
                        } else if (priceItem.equalsIgnoreCase("holiday purple candy")) {
                            PurpleCandyUtil.setHolidayPurple(uid, (int) (PurpleCandyUtil.getHolidayPurple(uid) - priceItemAmount));
                            continue;
                        }
                        p.getInventoryUser().removeItem(priceItem);
                    }
                    String itemPrice = FormatUtil.formatItemListOneLine(priceItems, false,true);
                    String itemDisplay = EmoteUtil.getEmoteMention(selectedItem.getHolidayItemName(true)) + " " +
                            selectedItem.getHolidayItemName(true) + " `x" +
                            FormatUtil.formatCommas((selectedItem.getHolidayAmount() * selectedAmount)) + "`";
                    String msg = Language.getLocalized(uid, "item_bought_msg",
                                    "Successfully bought {item_display} for {item_price}!").replace("{item_display}", itemDisplay)
                            .replace("{item_price}", itemPrice);
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "item_bought", "Item bought"), msg));
                } else {
                    if (hasntEnoughDiamonds) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(new Color(255, 58, 35));
                        eb.setTitle("Error");
                        eb.setDescription(Language.getLocalized(uid, "missing_needed_items_diamonds",
                                "*checks pockets* \n" + "empty they are, cant afford."));
                        e.reply(eb.build());
                        return;
                    }
                    String msg =
                            Language.getLocalized(uid, "missing_needed_items", "You don't have all needed items for that!");
                    e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                }
            }
        }
    }

    public static long getHolidayGain(int stageNumber) {
        int item = 0;
            if (stageNumber < 51) {
                item++;
            } else if (stageNumber < 151) {
                item += 2;
            } else if (stageNumber < 301) {
                item += 3;
            } else {
                item += 5;
            }
        return item;
    }

    public static long getSpecialHolidayGain() {
        int item = 0;
        boolean purpleCandy = new Random().nextInt(101) < 15;
            if (purpleCandy) {
                item++;
            }
        return item;
    }
}
