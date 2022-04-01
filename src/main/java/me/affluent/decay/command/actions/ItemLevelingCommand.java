package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.*;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.ItemLevelUpEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.StatsUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.entities.User;

public class ItemLevelingCommand extends BotCommand {

    public ItemLevelingCommand() {
        this.name = "level";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String[] args = e.getArgs();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "level_usage",
                                    "Please use {command_usage}.\n" +
                                            "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                            "You currently have {coin} coins left.")
                            .replace("{command_usage}", "`" + userPrefix + "level <Equipped Gear | Item ID> [amount] [cost]`")
                            .replace("{coin}", EmoteUtil.getCoin() + " `" + p.getEcoUser().getBalanceCom() + "`")));
            return;
        }
        ArmorUser armorUser = p.getArmorUser();
        Armor armor = null;
        Weapon weapon = null;
        Arrow arrow = null;
        Shield shield = null;
        String type = args[0].toLowerCase();
        long itemID = 0;
        if (type.equalsIgnoreCase("helmet")) {
            armor = armorUser.getHelmet();
            itemID = Long.parseLong(armorUser.getHelmetID());
        }
        if (type.equalsIgnoreCase("chestplate")) {
            armor = armorUser.getChestplate();
            itemID = Long.parseLong(armorUser.getChestplateID());
        }
        if (type.equalsIgnoreCase("gloves")) {
            armor = armorUser.getGloves();
            itemID = Long.parseLong(armorUser.getGlovesID());
        }
        if (type.equalsIgnoreCase("trousers")) {
            armor = armorUser.getTrousers();
            itemID = Long.parseLong(armorUser.getTrousersID());
        }
        if (type.equalsIgnoreCase("boots")) {
            armor = armorUser.getBoots();
            itemID = Long.parseLong(armorUser.getBootsID());
        }
        if (type.equalsIgnoreCase("sword") || type.equalsIgnoreCase("bow") || type.equalsIgnoreCase("staff") || type.equalsIgnoreCase("weapon")) {
            weapon = armorUser.getWeapon();
            itemID = Long.parseLong(armorUser.getWeaponID());
        }
        if (type.equalsIgnoreCase("arrow") || type.equalsIgnoreCase("arrows")) {
            arrow = armorUser.getArrow();
            itemID = Long.parseLong(armorUser.getArrowID());
        }
        if (type.equalsIgnoreCase("shield")) {
            shield = armorUser.getShield();
            itemID = Long.parseLong(armorUser.getShieldID());
        }
        if (itemID == 0) {
            try {
                itemID = Long.parseLong(type);
                Item item = InventoryUser.getItemByID(itemID);
                if (item != null) {
                    String in = item.getName().toLowerCase();
                    if (in.endsWith(" helmet")) armor = Helmet.getHelmet(in);
                    if (in.endsWith(" chestplate")) armor = Chestplate.getChestplate(in);
                    if (in.endsWith(" gloves")) armor = Gloves.getGloves(in);
                    if (in.endsWith(" trousers")) armor = Trousers.getTrousers(in);
                    if (in.endsWith(" boots")) armor = Boots.getBoots(in);
                    if (in.endsWith(" sword") || in.endsWith(" bow") || in.endsWith(" staff"))
                        weapon = Weapon.getWeapon(in);
                    if (in.endsWith(" arrow")) arrow = Arrow.getArrow(in);
                    if (in.endsWith(" shield")) shield = Shield.getShield(in);
                }
            } catch (NumberFormatException ex) {
            }
        }
        long balance = p.getEcoUser().getBalance().longValue();
        if (balance <= 124) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_coins",
                    "Majesty, you seem to have ran out. \nYou need Coins " + EmoteUtil.getCoin() + " to use this.")));
            return;
        }

        int currentStars = ItemStarringUtil.getItemStar(uid, itemID);
        int currentLevel = ItemLevelingUtil.getItemLevel(uid, itemID);
        int maxItemLevel = 0;
        boolean maxLevel = false;
        if (currentStars == 0) maxItemLevel = 25;
        if (currentStars == 1) maxItemLevel = 55;
        if (currentStars == 2) maxItemLevel = 90;
        if (currentStars == 3) maxItemLevel = 130;
        if (currentStars == 4) maxItemLevel = 175;
        if (currentStars == 5) maxItemLevel = 215;
        if (currentStars == 6) maxItemLevel = 250;
        if (currentStars == 7) maxItemLevel = 300;
        if (currentLevel >= maxItemLevel) maxLevel = true;

        int levelAmount;
        if (args.length > 1) {
            try {
                levelAmount = Integer.parseInt(args[1]);
                if (levelAmount < 1 || levelAmount > 299) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "argument_between",
                                    "The argument {argument} has to be between {min} and {max}!")
                            .replace("{argument}", "`<level amount>`")
                            .replace("{min}", "`1`")
                            .replace("{max}", "`299`")));
                    return;
                }
            } catch (NumberFormatException ex) {
                levelAmount = 1;
            }
        } else {
            levelAmount = 1;
        }

        int finalLevel = currentLevel + levelAmount;
        int neededStars = 0;
        if (finalLevel > maxItemLevel) maxLevel = true;
        if (finalLevel > 25 && finalLevel <= 55) neededStars = 1;
        if (finalLevel > 55 && finalLevel <= 90) neededStars = 2;
        if (finalLevel > 90 && finalLevel <= 130) neededStars = 3;
        if (finalLevel > 130 && finalLevel <= 175) neededStars = 4;
        if (finalLevel > 175 && finalLevel <= 215) neededStars = 5;
        if (finalLevel > 215 && finalLevel <= 250) neededStars = 6;
        if (finalLevel > 250 && finalLevel <= 300) neededStars = 7;
        boolean cost = false;
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if (i == (args.length - 1)) {
                if (arg.equalsIgnoreCase("cost")) {
                    cost = true;
                    break;
                }
            }
        }
        if (armor != null) {
            if (cost) {
                if (finalLevel > 300) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "Items have a maximum level of `300`.")
                                    .replace("{armor}", armor.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (levelAmount == 1) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level once,\n" +
                                                    "with a total cost of {max_amount} to level {max_level_amount} times.")
                                    .replace("{max_level_amount}", "`" + maxItemLevel + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, maxItemLevel) * getLevelCostMultiplier(armor.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(armor.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", armor.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (currentLevel + levelAmount <= maxItemLevel) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                    "with a total cost of {max_amount} to level {level_amount} times.")
                                    .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                    .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(armor.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(armor.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", armor.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                } else {
                    if (currentLevel + levelAmount > maxItemLevel) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                                Language.getLocalized(uid, "level_cost",
                                                "Your {armor} is currently level {level_count}\n" +
                                                        "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                        "with a total cost of {max_amount} to level {level_amount} times.\n\n" +
                                                        "In order to reach level {desired_level}, your {armor} will need to be {star_count} starred.")
                                        .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                        .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                        .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(armor.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(armor.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{armor}", armor.getDisplay())
                                        .replace("{desired_level}", "`" + (currentLevel + levelAmount) + "`")
                                        .replace("{star_count}", "`" + neededStars + "`")
                                        .replace("{level_count}", "`" + currentLevel + "`")));
                        return;
                    }
                }
                return;
            }
            if (currentLevel >= 300) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "maxed_level", "Your {armor} is level {level}.\n" +
                                        "This armor is maxed and can no longer be leveled up.")
                                .replace("{armor}", armor.getDisplay())
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            if (maxLevel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "max_level", "Your {armor} is currently level {level}, with a max level of {max_level}.\n" +
                                        "If you wish to level it up more, use `" + userPrefix + "star <Item ID> [amount]`")
                                .replace("{armor}", armor.getDisplay())
                                .replace("{max_level}", "`" + maxItemLevel + "`")
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            long totalCost = (long) (getNeededLevelCost(currentLevel, finalLevel) * getLevelCostMultiplier(armor.getItemType()));

            if (balance <= totalCost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_coins",
                        "Majesty, you seem to have ran out. \nYou need `x" + totalCost + "` Coins " + EmoteUtil.getCoin() + " to use this.")));
                return;
            }
            int total_item_levels = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "total_item_levels", "0"));
            total_item_levels += levelAmount;
            StatsUtil.setStat(p.getUserId(), "total_item_levels", String.valueOf(total_item_levels));
            ItemLevelUpEvent itemLevelUpEvent = new ItemLevelUpEvent(p, levelAmount, armor.getItemType());
            EventManager.callEvent(itemLevelUpEvent);
            ItemLevelingUtil.addItemLevel(uid, levelAmount, itemID);
            p.getEcoUser().removeBalance(totalCost);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "level_title", "Item Leveled"),
                    Language.getLocalized(uid, "level_success", "Successfully leveled up {armor} to level {level}!")
                            .replace("{armor}", armor.getDisplay())
                            .replace("{level}", "`" + finalLevel + "`")));
            return;
        }
        //
        if (weapon != null) {
            if (cost) {
                if (finalLevel > 300) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "Items have a maximum level of `300`.")
                                    .replace("{armor}", weapon.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (levelAmount == 1) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level once,\n" +
                                                    "with a total cost of {max_amount} to level {max_level_amount} times.")
                                    .replace("{max_level_amount}", "`" + maxItemLevel + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, maxItemLevel) * getLevelCostMultiplier(weapon.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(weapon.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", weapon.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (currentLevel + levelAmount <= maxItemLevel) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                    "with a total cost of {max_amount} to level {level_amount} times.")
                                    .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                    .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(weapon.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(weapon.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", weapon.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                } else {
                    if (currentLevel + levelAmount > maxItemLevel) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                                Language.getLocalized(uid, "level_cost",
                                                "Your {armor} is currently level {level_count}\n" +
                                                        "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                        "with a total cost of {max_amount} to level {level_amount} times.\n\n" +
                                                        "In order to reach level {desired_level}, your {armor} will need to be {star_count} starred.")
                                        .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                        .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                        .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(weapon.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(weapon.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{armor}", weapon.getDisplay())
                                        .replace("{desired_level}", "`" + (currentLevel + levelAmount) + "`")
                                        .replace("{star_count}", "`" + neededStars + "`")
                                        .replace("{level_count}", "`" + currentLevel + "`")));
                        return;
                    }
                }
                return;
            }
            if (currentLevel >= 300) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "maxed_level", "Your {weapon} is level {level}.\n" +
                                        "This weapon is maxed and can no longer be leveled up.")
                                .replace("{weapon}", weapon.getDisplay())
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            if (maxLevel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "max_level", "Your {weapon} is currently level {level}, with a max level of {max_level}.\n" +
                                        "If you wish to level it up more, use `" + userPrefix + "star <Item ID> [amount]`")
                                .replace("{weapon}", weapon.getDisplay())
                                .replace("{max_level}", "`" + maxItemLevel + "`")
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            long totalCost = (long) (getNeededLevelCost(currentLevel, finalLevel) * getLevelCostMultiplier(weapon.getItemType()));

            if (balance <= totalCost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_coins",
                        "Majesty, you seem to have ran out. \nYou need `x" + totalCost + "` Coins " + EmoteUtil.getCoin() + " to use this.")));
                return;
            }
            int total_item_levels = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "total_item_levels", "0"));
            total_item_levels += levelAmount;
            StatsUtil.setStat(p.getUserId(), "total_item_levels", String.valueOf(total_item_levels));
            ItemLevelUpEvent itemLevelUpEvent = new ItemLevelUpEvent(p, levelAmount, weapon.getItemType());
            EventManager.callEvent(itemLevelUpEvent);
            ItemLevelingUtil.addItemLevel(uid, levelAmount, itemID);
            p.getEcoUser().removeBalance(totalCost);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "level_title", "Item Leveled"),
                    Language.getLocalized(uid, "level_success", "Successfully leveled up {weapon} to level {level}!")
                            .replace("{weapon}", weapon.getDisplay())
                            .replace("{level}", "`" + finalLevel + "`")));
            return;
        }
        //
        if (shield != null) {
            if (cost) {
                if (finalLevel > 300) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "Items have a maximum level of `300`.")
                                    .replace("{armor}", shield.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (levelAmount == 1) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level once,\n" +
                                                    "with a total cost of {max_amount} to level {max_level_amount} times.")
                                    .replace("{max_level_amount}", "`" + maxItemLevel + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, maxItemLevel) * getLevelCostMultiplier(shield.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(shield.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", shield.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (currentLevel + levelAmount <= maxItemLevel) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                    "with a total cost of {max_amount} to level {level_amount} times.")
                                    .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                    .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(shield.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(shield.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", shield.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                } else {
                    if (currentLevel + levelAmount > maxItemLevel) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                                Language.getLocalized(uid, "level_cost",
                                                "Your {armor} is currently level {level_count}\n" +
                                                        "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                        "with a total cost of {max_amount} to level {level_amount} times.\n\n" +
                                                        "In order to reach level {desired_level}, your {armor} will need to be {star_count} starred.")
                                        .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                        .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                        .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(shield.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(shield.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{armor}", shield.getDisplay())
                                        .replace("{desired_level}", "`" + (currentLevel + levelAmount) + "`")
                                        .replace("{star_count}", "`" + neededStars + "`")
                                        .replace("{level_count}", "`" + currentLevel + "`")));
                        return;
                    }
                }
                return;
            }
            if (currentLevel >= 300) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "maxed_level", "Your {shield} is level {level}.\n" +
                                        "This shield is maxed and can no longer be leveled up.")
                                .replace("{shield}", shield.getDisplay())
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            if (maxLevel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "max_level", "Your {shield} is currently level {level}, with a max level of {max_level}.\n" +
                                        "If you wish to level it up more, use `" + userPrefix + "star <Item ID> [amount]`")
                                .replace("{shield}", shield.getDisplay())
                                .replace("{max_level}", "`" + maxItemLevel + "`")
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            long totalCost = (long) (getNeededLevelCost(currentLevel, finalLevel) * getLevelCostMultiplier(shield.getItemType()));

            if (balance <= totalCost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_coins",
                        "Majesty, you seem to have ran out. \nYou need `x" + totalCost + "` Coins " + EmoteUtil.getCoin() + " to use this.")));
                return;
            }
            int total_item_levels = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "total_item_levels", "0"));
            total_item_levels += levelAmount;
            StatsUtil.setStat(p.getUserId(), "total_item_levels", String.valueOf(total_item_levels));
            ItemLevelUpEvent itemLevelUpEvent = new ItemLevelUpEvent(p, levelAmount, shield.getItemType());
            EventManager.callEvent(itemLevelUpEvent);
            ItemLevelingUtil.addItemLevel(uid, levelAmount, itemID);
            p.getEcoUser().removeBalance(totalCost);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "level_title", "Item Leveled"),
                    Language.getLocalized(uid, "level_success", "Successfully leveled up {shield} to level {level}!")
                            .replace("{shield}", shield.getDisplay())
                            .replace("{level}", "`" + finalLevel + "`")));
            return;
        }
        //
        if (arrow != null) {
            if (cost) {
                if (finalLevel > 300) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "Items have a maximum level of `300`.")
                                    .replace("{armor}", arrow.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (levelAmount == 1) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level once,\n" +
                                                    "with a total cost of {max_amount} to level {max_level_amount} times.")
                                    .replace("{max_level_amount}", "`" + maxItemLevel + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, maxItemLevel) * getLevelCostMultiplier(arrow.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(arrow.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", arrow.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                }
                if (currentLevel + levelAmount <= maxItemLevel) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "level_cost",
                                            "Your {armor} is currently level {level_count}\n" +
                                                    "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                    "with a total cost of {max_amount} to level {level_amount} times.")
                                    .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                    .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                    .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(arrow.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(arrow.getItemType()))) + "` " + EmoteUtil.getCoin())
                                    .replace("{armor}", arrow.getDisplay())
                                    .replace("{level_count}", "`" + currentLevel + "`")));
                    return;
                } else {
                    if (currentLevel + levelAmount > maxItemLevel) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                                Language.getLocalized(uid, "level_cost",
                                                "Your {armor} is currently level {level_count}\n" +
                                                        "This will have a cost of {amount} to level {specific_level_amount} times,\n" +
                                                        "with a total cost of {max_amount} to level {level_amount} times.\n\n" +
                                                        "In order to reach level {desired_level}, your {armor} will need to be {star_count} starred.")
                                        .replace("{specific_level_amount}", "`" + levelAmount + "`")
                                        .replace("{level_amount}", "`" + (300 - currentLevel) + "`")
                                        .replace("{max_amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, 300) * getLevelCostMultiplier(arrow.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{amount}", "`x" + FormatUtil.formatCommas((long) ((int) getNeededLevelCost(currentLevel, currentLevel + levelAmount) * getLevelCostMultiplier(arrow.getItemType()))) + "` " + EmoteUtil.getCoin())
                                        .replace("{armor}", arrow.getDisplay())
                                        .replace("{desired_level}", "`" + (currentLevel + levelAmount) + "`")
                                        .replace("{star_count}", "`" + neededStars + "`")
                                        .replace("{level_count}", "`" + currentLevel + "`")));
                        return;
                    }
                }
                return;
            }
            if (currentLevel >= 300) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "maxed_level", "Your {armor} is level {level}.\n" +
                                        "This arrow is maxed and can no longer be leveled up.")
                                .replace("{arrow}", arrow.getDisplay())
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            if (maxLevel) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "max_level", "Your {arrow} is currently level {level}, with a max level of {max_level}.\n" +
                                        "If you wish to level it up more, use `" + userPrefix + "star <Item ID> [amount]`")
                                .replace("{arrow}", arrow.getDisplay())
                                .replace("{max_level}", "`" + maxItemLevel + "`")
                                .replace("{level}", "`" + currentLevel + "`")));
                return;
            }
            long totalCost = (long) (getNeededLevelCost(currentLevel, finalLevel) * getLevelCostMultiplier(arrow.getItemType()));

            if (balance <= totalCost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_coins",
                        "Majesty, you seem to have ran out. \nYou need `x" + totalCost + "` Coins " + EmoteUtil.getCoin() + " to use this.")));
                return;
            }
            int total_item_levels = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "total_item_levels", "0"));
            total_item_levels += levelAmount;
            StatsUtil.setStat(p.getUserId(), "total_item_levels", String.valueOf(total_item_levels));
            ItemLevelUpEvent itemLevelUpEvent = new ItemLevelUpEvent(p, levelAmount, arrow.getItemType());
            EventManager.callEvent(itemLevelUpEvent);
            ItemLevelingUtil.addItemLevel(uid, levelAmount, itemID);
            p.getEcoUser().removeBalance(totalCost);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "level_title", "Item Leveled"),
                    Language.getLocalized(uid, "level_success", "Successfully leveled up {arrow} to level {level}!")
                            .replace("{arrow}", arrow.getDisplay())
                            .replace("{level}", "`" + finalLevel + "`")));
            return;
        }
        e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "cmd_level_invalid",
                "You don't have any equipped items of that type or that item doesn't exist.")));
    }


    private double getLevelCostMultiplier(ItemType itemType) {
        switch (itemType) {
            case COPPER:
                return 1.32;
            case REINFORCED:
                return 1.98;
            case TITANIUM:
                return 2.75;
            case IRON:
                return 3.55;
            case STEEL:
                return 4.38;
            case CARBON_STEEL:
                return 5.5;
            case DRAGON_STEEL:
                return 6.86;
            case TITAN_ALLOY:
                return 8.35;
            case WITHER:
                return 10;
            default:
                return 0.65;
        }
    }

    public static long getNeededLevelCost(int currentLevel, int level) {
        long neededCost = 115;
        for (int i = currentLevel + 1; i <= level; i++) {
            if (i <= 75) neededCost += 115;
            else if (i <= 150) neededCost += 559;
            else if (i <= 225) neededCost += 2717;
            else if (i <= 300) neededCost += 5625;
        }
        return neededCost;
    }
}
