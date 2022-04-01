package me.affluent.decay.command.shop;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.shop.*;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MerchantCommand extends BotCommand {

    private static final HashMap<String, ShopCategory> shopCategories = new HashMap<>();

    public MerchantCommand() {
        this.name = "merchant";
        this.aliases = new String[]{"shop"};
        this.cooldown = 1.5;
        ShopCategory DkeysCategory = new ShopCategory("Keys",
                Arrays.asList(
                        new ShopItem("Wood Key", "wood key", 1, new PriceItems("Diamond", 100)),
                        new ShopItem("Metal Key", "metal key", 1, new PriceItems("Diamond", 225)),
                        new ShopItem("Titanium Key", "titanium key", 1, new PriceItems("Diamond", 550)),
                        new ShopItem("Steel Key", "steel key", 1, new PriceItems("Diamond", 1500)),
                        new ShopItem("Dragon Steel Key", "dragon steel key", 1, new PriceItems("Diamond", 5000))
                ));
        ShopCategory MiscCategory = new ShopCategory("Misc",
                Arrays.asList(
                        new ShopItem("Alloy Ingot", "Alloy Ingot", 1, new PriceMoney(2250)),
                        new ShopItem("Scroll", "Scroll", 1, new PriceItems("Diamond", 350)),
                        new ShopItem("Tavern Token", "Tavern Token", 1, new PriceItems("Diamond", 1250)),
                        new ShopItem("Golden Tavern Token", "Golden Tavern Token", 1, new PriceItems("Diamond", 2750))
                ));
        List<ShopCategory> shopCategoryList = Arrays.asList(DkeysCategory, MiscCategory);
        for (ShopCategory shopCategory : shopCategoryList) {
            shopCategories.put(shopCategory.getTitle().toLowerCase(), shopCategory);
        }
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String categoriesString = "- Keys\n- Misc";
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            String msg = Language.getLocalized(uid, "merchant_usage",
                            " Use {usage_shop}.\n__Categories:__\n\n{categories}")
                    .replace("{categories}", categoriesString)
                    .replace("{usage_shop}", "`" + userPrefix + "merchant <category>`");

            e.reply(MessageUtil.info(Language.getLocalized(uid, "merchant", "Merchant"), msg));
            return;
        }
        String ironman = IronmanUtil.getIronmanMode(uid).getIronmanMode();
        if (ironman.equalsIgnoreCase("hardcore")) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "ironman", "Hardcore Ironman players are not allowed to shop!")));
            return;
        }
        List<String> categories = new ArrayList<>(shopCategories.keySet());
        String selectedCategory = args[0].toLowerCase();
        if (selectedCategory.equalsIgnoreCase("list")) {
            String msg = Language.getLocalized(uid, "categories", "__Categories:__\n\n{categories}")
                    .replace("{categories}", categoriesString);
            e.reply(MessageUtil.info(Language.getLocalized(uid, "merchant", "Merchant"), msg));
            return;
        }
        if (!(categories.contains(selectedCategory))) {
            String msg =
                    Language.getLocalized(uid, "invalid_category", "Invalid category.\n__Categories:__\n\n{categories}")
                            .replace("{categories}", categoriesString);
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
            return;
        }
        ShopCategory shopCategory = shopCategories.get(selectedCategory);
        String shopTitle = shopCategory.getTitle();
        List<ShopItem> shopItems = shopCategory.getShopItems();
        // if no item is selected to buy
        if (args.length < 2) {
            StringBuilder itemList = new StringBuilder();
            for (ShopItem shopItem : shopItems) {
                String toAdd =
                        EmoteUtil.getEmoteMention(shopItem.getItemName(true)) + " " + shopItem.getDisplayName(true) +
                                ": ";
                Price price = shopItem.getPrice();
                if (ironman.equalsIgnoreCase("ironman")) {
                    if (price.getPriceType() == Price.PriceType.MONEY) {
                        long priceMoney = (long) (price.getGoldCoins() * 1.35);
                        toAdd += EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(priceMoney);
                    } else if (price.getPriceType() == Price.PriceType.ITEMS) {
                        HashMap<String, Long> originalPriceItems = price.getItems();
                        HashMap<String, Long> priceItems = new HashMap<>();
                        for(String priceItem : originalPriceItems.keySet()) {
                            long thePrice = originalPriceItems.get(priceItem);
                            long newPrice = (long) (thePrice * 1.35);
                            priceItems.put(priceItem, newPrice);
                        }
                        toAdd += FormatUtil.formatItemListOneLine(priceItems,false, true);
                    } else {
                        long priceMoney = (long) (price.getGoldCoins() * 1.35);
                        HashMap<String, Long> priceItems = price.getItems();
                        toAdd += EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(priceMoney) + " | " +
                                FormatUtil.formatItemListOneLine(priceItems, false, true);
                    }
                } else {
                    if (price.getPriceType() == Price.PriceType.MONEY) {
                        long priceMoney = price.getGoldCoins();
                        toAdd += EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(priceMoney);
                    } else if (price.getPriceType() == Price.PriceType.ITEMS) {
                        HashMap<String, Long> priceItems = price.getItems();
                        toAdd += FormatUtil.formatItemListOneLine(priceItems, false, true);
                    } else {
                        long priceMoney = price.getGoldCoins();
                        HashMap<String, Long> priceItems = price.getItems();
                        toAdd += EmoteUtil.getCoin() + " " + FormatUtil.formatCommas(priceMoney) + " | " +
                                FormatUtil.formatItemListOneLine(priceItems, false, true);
                    }
                }
                itemList.append(toAdd).append("\n");
            }
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            String msg =
                    Language.getLocalized(uid, "merchant_shop_usage", "To buy an item, use {usage}.\n\n{item_list}")
                            .replace("{item_list}", itemList.toString())
                            .replace("{usage}", "`" + userPrefix + "merchant " + selectedCategory + " <item>`");
            e.reply(MessageUtil.info(Language.getLocalized(uid, "merchant", "Merchant") + " - " + shopTitle, msg));
            return;
        }
        // if item is requested to buy
        int selectedAmount = 1;
        StringBuilder itemRequested = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i == (args.length - 1)) {
                try {
                    selectedAmount = Integer.parseInt(args[i]);
                    int maxAmount = selectedCategory.equalsIgnoreCase("potions") ? 1 : 100;
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
        ShopItem selectedItem = null;
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getDisplayName(false).equalsIgnoreCase(itemRequested.toString())) {
                selectedItem = shopItem;
                break;
            }
        }
        if (selectedItem == null) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_item", "Invalid item.")));
            return;
        }
        String item = selectedItem.getItemName(false);
        Price price = selectedItem.getPrice();
        Player p = Player.getPlayer(uid);
        if (ironman.equalsIgnoreCase("ironman")) {
            if (price.getPriceType() == Price.PriceType.MONEY) {
                long priceMoney = (long) (price.getGoldCoins() * 1.35);
                if (p.getEcoUser().getBalance().compareTo(new BigInteger(String.valueOf(priceMoney * selectedAmount))) < 0) {
                    e.reply(Constants.CANT_AFFORD(uid));
                } else {
                    boolean cancel = false;
                    if (item.startsWith("Alloy")) IngotUtil.addIngots(uid, selectedAmount);
                    else if (item.startsWith("Golden")) GoldenTokensUtil.addGoldenTokens(uid, selectedAmount);
                    else if (item.startsWith("Tavern")) TokensUtil.addTokens(uid, selectedAmount);
                    else if (item.startsWith("Scroll")) ScrollsUtil.addScrolls(uid, (int) selectedAmount);
                    else if (item.startsWith("§")) cancel = !ShopUtil.applyItem(p, item.substring(1), selectedAmount);
                    else p.getInventoryUser().addItem(item, selectedItem.getAmount() * selectedAmount);
                    if (cancel) {
                        if (item.startsWith("heal")) {
                            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "shop_buy_failed_heal",
                                            "You currently have max health " + p.getHealthUser().getHealth() + " " +
                                                    EmoteUtil.getEmoteMention("HP") + "\nYou can't buy {item}.")
                                    .replace("{item}", selectedItem.getItemName(true))));
                            return;
                        }
                        return;
                    }
                    p.getEcoUser().removeBalance(priceMoney * selectedAmount);
                    String itemPrice = EmoteUtil.getCoin() + " " + (priceMoney * selectedAmount);
                    String itemDisplay = EmoteUtil.getEmoteMention(selectedItem.getItemName(true)) + " " +
                            selectedItem.getItemName(true) + " `x" +
                            FormatUtil.formatCommas((selectedItem.getAmount() * selectedAmount)) + "`";
                    String msg = Language.getLocalized(uid, "item_bought_msg",
                                    "Successfully bought {item_display} for {item_price}!").replace("{item_display}", itemDisplay)
                            .replace("{item_price}", itemPrice);
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "item_bought", "Item bought"), msg));
                }
            } else {
                HashMap<String, Long> originalPriceItems = price.getItems(selectedAmount);
                HashMap<String, Long> priceItems = new HashMap<>();
                boolean hasAllItems = true;
                boolean hasntEnoughDiamonds = false;
                for (String priceItem : originalPriceItems.keySet()) {
                    long priceItemAmount = originalPriceItems.get(priceItem);
                    long newPrice = (long) (priceItemAmount * 1.35);
                    if (p.getInventoryUser().hasItem(priceItem) < newPrice) {
                        hasAllItems = false;
                        if (priceItem.equalsIgnoreCase("diamond")) hasntEnoughDiamonds = true;
                        break;
                    }
                }
                if (hasAllItems) {
                    boolean cancel = false;
                    final long finalAmount = selectedItem.getAmount() * selectedAmount;
                    if (item.startsWith("Alloy")) IngotUtil.addIngots(uid, finalAmount);
                    else if (item.startsWith("Golden")) GoldenTokensUtil.addGoldenTokens(uid, (int) finalAmount);
                    else if (item.startsWith("Tavern")) TokensUtil.addTokens(uid, (int) finalAmount);
                    else if (item.startsWith("Scroll")) ScrollsUtil.addScrolls(uid, (int) finalAmount);
                    else if (item.startsWith("§")) cancel = !ShopUtil.applyItem(p, item.substring(1), selectedAmount);
                    else p.getInventoryUser().addItem(item, finalAmount);
                    if (!cancel) {
                        for (String priceItem : originalPriceItems.keySet()) {
                            long priceItemAmount = originalPriceItems.get(priceItem);
                            long newPrice = (long) (priceItemAmount * 1.35);
                            if (priceItem.equalsIgnoreCase("diamond")) {
                                DiamondsUtil.setDiamonds(uid, DiamondsUtil.getDiamonds(uid) - newPrice);
                                continue;
                            }
                            p.getInventoryUser().removeItem(priceItem);
                        }
                    } else {
                        String msg = Language.getLocalized(uid, "shop_buy_failed", "Failed to buy {item}!")
                                .replace("{item}", selectedItem.getItemName(true));
                        e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                    }
                    String itemPrice = FormatUtil.formatItemListOneLine(originalPriceItems, true, true);
                    String itemDisplay = EmoteUtil.getEmoteMention(selectedItem.getItemName(true)) + " " +
                            selectedItem.getItemName(true) + " `x" +
                            FormatUtil.formatCommas((selectedItem.getAmount() * selectedAmount)) + "`";
                    String msg = Language.getLocalized(uid, "item_bought_msg",
                                    "Successfully bought {item_display} for {item_price}!").replace("{item_display}", itemDisplay)
                            .replace("{item_price}", "" + itemPrice);
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "item_bought", "Item bought"), msg));
                } else {
                    if (hasntEnoughDiamonds) {
                        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(new Color(255, 58, 35));
                        eb.setTitle("Error");
                        eb.setDescription(Language.getLocalized(uid, "missing_needed_items_diamonds",
                                "*checks pockets* \n" + "empty they are, cant afford."));
                        eb.setFooter("Running low on Diamonds? Use `w.donate`");
                        e.reply(eb.build());
                        return;
                    }
                    String msg =
                            Language.getLocalized(uid, "missing_needed_items", "You don't have all needed items for that!");
                    e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                }
            }
            //
            // NOT IRONMAN
            //
        } else {
            if (price.getPriceType() == Price.PriceType.MONEY) {
                long priceMoney = price.getGoldCoins();
                if (p.getEcoUser().getBalance().compareTo(new BigInteger(String.valueOf(priceMoney * selectedAmount))) < 0) {
                    e.reply(Constants.CANT_AFFORD(uid));
                } else {
                    boolean cancel = false;
                    if (item.startsWith("Alloy")) IngotUtil.addIngots(uid, selectedAmount);
                    else if (item.startsWith("Golden")) GoldenTokensUtil.addGoldenTokens(uid, selectedAmount);
                    else if (item.startsWith("Tavern")) TokensUtil.addTokens(uid, selectedAmount);
                    else if (item.startsWith("Scroll")) ScrollsUtil.addScrolls(uid, (int) selectedAmount);
                    else if (item.startsWith("§")) cancel = !ShopUtil.applyItem(p, item.substring(1), selectedAmount);
                    else p.getInventoryUser().addItem(item, selectedItem.getAmount() * selectedAmount);
                    if (cancel) {
                        if (item.startsWith("heal")) {
                            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "shop_buy_failed_heal",
                                            "You currently have max health " + p.getHealthUser().getHealth() + " " +
                                                    EmoteUtil.getEmoteMention("HP") + "\nYou can't buy {item}.")
                                    .replace("{item}", selectedItem.getItemName(true))));
                            return;
                        }
                        return;
                    }
                    p.getEcoUser().removeBalance(priceMoney * selectedAmount);
                    String itemPrice = EmoteUtil.getCoin() + " " + (priceMoney * selectedAmount);
                    String itemDisplay = EmoteUtil.getEmoteMention(selectedItem.getItemName(true)) + " " +
                            selectedItem.getItemName(true) + " `x" +
                            FormatUtil.formatCommas((selectedItem.getAmount() * selectedAmount)) + "`";
                    String msg = Language.getLocalized(uid, "item_bought_msg",
                                    "Successfully bought {item_display} for {item_price}!").replace("{item_display}", itemDisplay)
                            .replace("{item_price}", itemPrice);
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "item_bought", "Item bought"), msg));
                }
            } else {
                HashMap<String, Long> priceItems = price.getItems(selectedAmount);
                boolean hasAllItems = true;
                boolean hasntEnoughDiamonds = false;
                for (String priceItem : priceItems.keySet()) {
                    long priceItemAmount = priceItems.get(priceItem);
                    if (p.getInventoryUser().hasItem(priceItem) < priceItemAmount) {
                        hasAllItems = false;
                        if (priceItem.equalsIgnoreCase("diamond")) hasntEnoughDiamonds = true;
                        break;
                    }
                }
                if (hasAllItems) {
                    boolean cancel = false;
                    final long finalAmount = selectedItem.getAmount() * selectedAmount;
                    if (item.startsWith("Alloy")) IngotUtil.addIngots(uid, finalAmount);
                    else if (item.startsWith("Golden")) GoldenTokensUtil.addGoldenTokens(uid, (int) finalAmount);
                    else if (item.startsWith("Tavern")) TokensUtil.addTokens(uid, (int) finalAmount);
                    else if (item.startsWith("Scroll")) ScrollsUtil.addScrolls(uid, (int) finalAmount);
                    else if (item.startsWith("§")) cancel = !ShopUtil.applyItem(p, item.substring(1), selectedAmount);
                    else p.getInventoryUser().addItem(item, finalAmount);
                    if (!cancel) {
                        for (String priceItem : priceItems.keySet()) {
                            long priceItemAmount = priceItems.get(priceItem);
                            if (priceItem.equalsIgnoreCase("diamond")) {
                                DiamondsUtil.setDiamonds(uid, DiamondsUtil.getDiamonds(uid) - priceItemAmount);
                                continue;
                            }
                            p.getInventoryUser().removeItem(priceItem);
                        }
                    } else {
                        String msg = Language.getLocalized(uid, "shop_buy_failed", "Failed to buy {item}!")
                                .replace("{item}", selectedItem.getItemName(true));
                        e.reply(MessageUtil.err(Constants.ERROR(uid), msg));
                        return;
                    }
                    String itemPrice = FormatUtil.formatItemListOneLine(priceItems, false,true);
                    String itemDisplay = EmoteUtil.getEmoteMention(selectedItem.getItemName(true)) + " " +
                            selectedItem.getItemName(true) + " `x" +
                            FormatUtil.formatCommas((selectedItem.getAmount() * selectedAmount)) + "`";
                    String msg = Language.getLocalized(uid, "item_bought_msg",
                                    "Successfully bought {item_display} for {item_price}!").replace("{item_display}", itemDisplay)
                            .replace("{item_price}", itemPrice);
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "item_bought", "Item bought"), msg));
                } else {
                    if (hasntEnoughDiamonds) {
                        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(new Color(255, 58, 35));
                        eb.setTitle("Error");
                        eb.setDescription(Language.getLocalized(uid, "missing_needed_items_diamonds",
                                "*checks pockets* \n" + "empty they are, cant afford."));
                        eb.setFooter("Running low on Diamonds? Use `w.donate`");
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
}