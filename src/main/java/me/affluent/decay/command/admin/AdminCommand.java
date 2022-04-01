package me.affluent.decay.command.admin;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.BadgeUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.entity.otherInventory.ArmorDragonUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.entity.otherInventory.ArmorWitherUser;
import me.affluent.decay.enums.Gender;
import me.affluent.decay.holidayevent.HolidayEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.BanManager;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.specialevent.SpecialEvent;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.system.CooldownUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.vote.VoteClient;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonWeapon;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AdminCommand extends BotCommand {

    private static final HashMap<String, String> cache = new HashMap<>();

    public AdminCommand() {
        this.name = "admin";
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (args.length < 1) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "admin_plain", "Admin"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "admin","" +
                    EmoteUtil.getCoin() + "**Economy Commands**" + EmoteUtil.getCoin() + "\n" +
                    "`adddiamonds <id> <amount>`,\n `removeddiamonds <id> <amount>`,\n `setdiamonds <id> <amount>`,\n\n" +
                    "`addmoney <id> <amount>`,\n `removemoney <id> <amount>`,\n `setmoney <id> <amount>`,\n\n" +
                    "`setelixirlevel <id> <level>`,\n `setelixirexp <id> <exp>`,\n" +
                    "`setlevel <id> <level>`,\n `setexp <id> <exp>`,\n" +
                    "`setpetlevel <userid> <petid> <amount>`,\n" +
                    "`sethealth <id> <amount>`,\n" +
                    "`setpetexp <userid> <petid> <amount>`,\n\n" +

                    EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getName()) + " **Item Commands** " + EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getName()) + "\n" +
                    "`keyall <key>`,\n" +
                    "`addkey <id> <key> <amount>`,\n" +
                    "`removekey <id> <key> <amount>`,\n" +
                    "`additem <id> <item> <amount>`,\n" +
                    "`removeitem <id> <itemid> <amount>`,\n" +
                    "`setitemlevel <id> <itemid> <amount>`,\n" +
                    "`setitemstar <id> <itemid> <amount>`,\n" +
                    "`addpet <id> <item> <amount>`,\n" +
                    "`removepet <id> <itemid> <amount>`,\n" +
                    "`giveall <item>`,\n" +
                    "`givepackageall <id> <level>`,\n" +
                    "`givebetapackage <id> <level>`,\n" +
                    "`givealphapackage <id>`,\n\n" +
                    "`addscroll <id> <amount>`,\n `removescroll <id> <amount>`,\n `setscroll <id> <amount>`,\n\n" +
                    "`addironscroll <id> <amount>`,\n `removeironscroll <id> <amount>`,\n `setironscroll <id> <amount>`,\n\n" +
                    "`adddragonscroll <id> <amount>`,\n `removedragonscroll <id> <amount>`,\n `setdragonscroll <id> <amount>`,\n\n" +
                    "`addwitherscroll <id> <amount>`,\n `removewitherscroll <id> <amount>`,\n `setwitherscroll <id> <amount>`,\n\n" +
                    "`addorb <id> <amount>`,\n `removeorb <id> <amount>`,\n `setorb <id> <amount>`,\n\n" +
                    "`addtoken <id> <amount>`,\n `removetoken <id> <amount>`,\n `settoken <id> <amount>`,\n\n" +
                    "`addingot <id> <amount>`,\n `removeingot <id> <amount>`,\n `setingot <id> <amount>`,\n\n" +
                    "`addfirework <id> <amount>`,\n `removefirework <id> <amount>`,\n `setfirework <id> <amount>`,\n\n" +
                    "`addpresent <id> <amount>`,\n `removepresent <id> <amount>`,\n `setpresent <id> <amount>`,\n\n" +
                    "`addpurplecandy <id> <amount>`,\n `removepurplecandy <id> <amount>`,\n `setpurplecandy <id> <amount>`,\n\n" +
                    "`addcandycane <id> <amount>`,\n `removecandycane <id> <amount>`,\n `setcandycane <id> <amount>`,\n\n" +
                    "`addcandycorn <id> <amount>`,\n `removecandycorn <id> <amount>`,\n `setcandycorn <id> <amount>`,\n\n" +
                    "`setinviron <id> <rarity> <itemType>`,\n" +
                    "`setinvdragon <id> <rarity> <itemType>`,\n" +
                    "`setinvwither <id> <rarity> <itemType>`,\n\n" +

                    EmoteUtil.getEmoteMention("Common_Gear") + " **Miscellaneous Commands** " + EmoteUtil.getEmoteMention("Common_Gear") + "\n" +
                    "`vote <id>`,\n" +
                    "`event <start/end> [event]`,\n" +
                    "`setgender <new gender>`,\n\n" +
                    "`fullban <id> <time>`,\n `unban <id>`,\n `msguser <id> <msg>`,\n\n" +
                    "`clearbarriers <all | rank | id>`,\n `removecooldown <id> <cooldown name>`,\n `clearcooldowns <id>`,\n\n" +
                    "`maintenance`,\n `maintenance-message <message>`,\n" +
                    "`addbadge <id> <badge name>`,\n `removebadge <id> <badge name>`,\n" +
                    "`settime <minute> <hour> <day> <month> <year>`,\n" +
                    "`reset <id> <confirm>`"));
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.getTextChannel().sendMessage(eb.build()).queue();
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("addfirework")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addfirework <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            FireworksUtil.addHolidayFireworks(tid, amount);
            e.reply(MessageUtil.success("Fireworks added",
                    "Added " + "`" + FormatUtil.formatCommas(amount) + "` Fireworks to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("removefirework")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removefirework <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String amount = args[2];
            long finalamount = FireworksUtil.getHolidayFireworks(tid) - Integer.parseInt(amount);
            FireworksUtil.setHolidayFireworks(tid, (int) finalamount);
            e.reply(MessageUtil.success("Fireworks removed",
                    "Removed " + "`" + FormatUtil.formatCommas(amount) + "` Fireworks from" + tid));
            return;
        }
        if (arg.equalsIgnoreCase("setfirework")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setfirework <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            FireworksUtil.setHolidayFireworks(tid, amount);
            e.reply(MessageUtil.success("Fireworks set",
                    "Set Fireworks of " + tid + " to `" + FormatUtil.formatCommas(amount) + "`"));
            return;

        }
        if (arg.equalsIgnoreCase("addpresent")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addpresent <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            PresentsUtil.addHolidayPresents(tid, amount);
            e.reply(MessageUtil.success("Presents added",
                    "Added " + "`" + FormatUtil.formatCommas(amount) + "` Presents to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("removepresent")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removepresent <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String amount = args[2];
            long finalamount = PresentsUtil.getHolidayPresents(tid) - Integer.parseInt(amount);
            PresentsUtil.setHolidayPresents(tid, (int) finalamount);
            e.reply(MessageUtil.success("Presents removed",
                    "Removed " + "`" + FormatUtil.formatCommas(amount) + "` Presents from " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("setpresent")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setpresent <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            PresentsUtil.setHolidayPresents(tid, amount);
            e.reply(MessageUtil.success("Present set",
                    "Set Presents of " + tid + " to `" + FormatUtil.formatCommas(amount) + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("addpurplecandy")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addpurplecandy <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            PurpleCandyUtil.addHolidayPurple(tid, amount);
            e.reply(MessageUtil.success("Purple Candy added",
                    "Added " + "`" + FormatUtil.formatCommas(amount) + "` Purple Candy to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("removepurplecandy")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removepurplecandy <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String amount = args[2];
            long finalamount = PurpleCandyUtil.getHolidayPurple(tid) - Integer.parseInt(amount);
            PurpleCandyUtil.setHolidayPurple(tid, (int) finalamount);
            e.reply(MessageUtil.success("Purple Candy removed",
                    "Removed " + "`" + FormatUtil.formatCommas(amount) + "` Purple Candy from " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("setpurplecandy")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setpurplecandy <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            PurpleCandyUtil.setHolidayPurple(tid, amount);
            e.reply(MessageUtil.success("Purple Candy set",
                    "Set Purple Candy of " + tid + " to `" + FormatUtil.formatCommas(amount) + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("addcandycane")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addcandycane <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            CandyCaneUtil.addHolidayCandyCane(tid, amount);
            e.reply(MessageUtil.success("Candy Cane added",
                    "Added " + "`" + FormatUtil.formatCommas(amount) + "` Candy Cane to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("removecandycane")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removecandycane <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String amount = args[2];
            long finalamount = CandyCaneUtil.getHolidayCandyCane(tid) - Integer.parseInt(amount);
            CandyCaneUtil.setHolidayCandyCane(tid, (int) finalamount);
            e.reply(MessageUtil.success("Candy Cane removed",
                    "Removed " + "`" + FormatUtil.formatCommas(amount) + "` Candy Cane from " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("setcandycane")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setcandycane <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            CandyCaneUtil.setHolidayCandyCane(tid, amount);
            e.reply(MessageUtil.success("Candy Cane set",
                    "Set Candy Cane of " + tid + " to `" + FormatUtil.formatCommas(amount) + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("addcandycorn")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addcandycorn <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            CandyCornUtil.addHolidayCorn(tid, amount);
            e.reply(MessageUtil.success("Candy Corn added",
                    "Added " + "`" + FormatUtil.formatCommas(amount) + "` Candy Corn to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("removecandycorn")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removecandycorn <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String amount = args[2];
            long finalamount = CandyCornUtil.getHolidayCorn(tid) - Integer.parseInt(amount);
            CandyCornUtil.setHolidayCorn(tid, (int) finalamount);
            e.reply(MessageUtil.success("Candy Corn removed",
                    "Removed " + "`" + FormatUtil.formatCommas(amount) + "` Candy Corn from " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("setcandycorn")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setcandycorn <userid> <amount>>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            int amount = Integer.parseInt(args[2]);
            CandyCornUtil.setHolidayCorn(tid, amount);
            e.reply(MessageUtil.success("Candy Corn set",
                    "Set Candy Corn of " + tid + " to `" + FormatUtil.formatCommas(amount) + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("setdiamonds")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setdiamonds <userid> <amount | long/bigint>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            long amount = Long.parseLong(args[2]);
            DiamondsUtil.setDiamonds(tid, amount);
            e.reply(MessageUtil.success("Diamonds set",
                    "Set diamonds of " + tid + " to `" + FormatUtil.formatCommas(amount) + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("adddiamonds")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin adddiamonds <userid> <amount | long/bigint>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            long amount = Long.parseLong(args[2]);
            DiamondsUtil.addDiamonds(tid, amount);
            e.reply(MessageUtil.success("Diamonds added",
                    "Added `" + FormatUtil.formatCommas(amount) + "` diamonds to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("setinviron") || (arg.equalsIgnoreCase("setinvi"))) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setinviron <userid> <rarity> <itemType>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String rarity = args[2];
            String itemType = args[3];
            Player p = Player.getPlayer(tid);
            ArmorIronUser au = p.getArmorIronUser();
            String newItem = rarity + " iron " + itemType;
            String finalItem = "";
            if (itemType.equalsIgnoreCase("weapon") || (itemType.equalsIgnoreCase("sword"))) {
                IronWeapon ironWeapon = IronWeapon.getWeapon(newItem);
                if (ironWeapon != null) {
                    au.updateIronWeapon(ironWeapon);
                    finalItem = String.valueOf(au.getIronWeapon());
                }
            }
            if (itemType.equalsIgnoreCase("helmet")) {
                IronHelmet ironHelmet = IronHelmet.getIronHelmet(newItem);
                if (ironHelmet != null) {
                    au.updateIronHelmet(ironHelmet);
                    finalItem = String.valueOf(au.getIronHelmet());
                }
            }
            if (itemType.equalsIgnoreCase("chestplate")) {
                IronChestplate ironChestplate = IronChestplate.getIronChestplate(newItem);
                if (ironChestplate != null) {
                    au.updateIronChestplate(ironChestplate);
                    finalItem = String.valueOf(au.getIronChestplate());
                }
            }
            if (itemType.equalsIgnoreCase("gloves")) {
                IronGloves ironGloves = IronGloves.getIronGloves(newItem);
                if (ironGloves != null) {
                    au.updateIronGloves(ironGloves);
                    finalItem = String.valueOf(au.getIronGloves());
                }
            }
            if (itemType.equalsIgnoreCase("trousers")) {
                IronTrousers ironTrousers = IronTrousers.getIronTrousers(newItem);
                if (ironTrousers != null) {
                    au.updateIronTrousers(ironTrousers);
                    finalItem = String.valueOf(au.getIronTrousers());
                }
            }
            if (itemType.equalsIgnoreCase("booots")) {
                IronBoots ironBoots = IronBoots.getIronBoots(newItem);
                if (ironBoots != null) {
                    au.updateIronBoots(ironBoots);
                    finalItem = String.valueOf(au.getIronBoots());
                }
            }
            if (itemType.equalsIgnoreCase("shield")) {
                IronShield ironShield = IronShield.getIronShield(newItem);
                if (ironShield != null) {
                    au.updateIronShield(ironShield);
                    finalItem = String.valueOf(au.getIronShield());
                }
            }
            if (!finalItem.equalsIgnoreCase("")) {
                e.reply(MessageUtil.info("Usage",
                        "Successfully set " + p.getMention() + "'s iron " + itemType + " to " + finalItem));
                return;
            }
        }
        if (arg.equalsIgnoreCase("setinvdragon") || (arg.equalsIgnoreCase("setinvd"))) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setinvdragon <userid> <rarity> <itemType>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String rarity = args[2];
            String itemType = args[3];
            Player p = Player.getPlayer(tid);
            ArmorDragonUser au = p.getArmorDragonUser();
            String newItem = rarity + " dragon " + itemType;
            String finalItem = "";
            if (itemType.equalsIgnoreCase("weapon") || (itemType.equalsIgnoreCase("sword"))) {
                DragonWeapon dragonWeapon = DragonWeapon.getWeapon(newItem);
                if (dragonWeapon != null) {
                    au.updateDragonWeapon(dragonWeapon);
                    finalItem = String.valueOf(au.getDragonWeapon());
                }
            }
            if (itemType.equalsIgnoreCase("helmet")) {
                DragonHelmet dragonHelmet = DragonHelmet.getDragonHelmet(newItem);
                if (dragonHelmet != null) {
                    au.updateDragonHelmet(dragonHelmet);
                    finalItem = String.valueOf(au.getDragonHelmet());
                }
            }
            if (itemType.equalsIgnoreCase("chestplate")) {
                DragonChestplate dragonChestplate = DragonChestplate.getDragonChestplate(newItem);
                if (dragonChestplate != null) {
                    au.updateDragonChestplate(dragonChestplate);
                    finalItem = String.valueOf(au.getDragonChestplate());
                }
            }
            if (itemType.equalsIgnoreCase("gloves")) {
                DragonGloves dragonGloves = DragonGloves.getDragonGloves(newItem);
                if (dragonGloves != null) {
                    au.updateDragonGloves(dragonGloves);
                    finalItem = String.valueOf(au.getDragonGloves());
                }
            }
            if (itemType.equalsIgnoreCase("trousers")) {
                DragonTrousers dragonTrousers = DragonTrousers.getDragonTrousers(newItem);
                if (dragonTrousers != null) {
                    au.updateDragonTrousers(dragonTrousers);
                    finalItem = String.valueOf(au.getDragonTrousers());
                }
            }
            if (itemType.equalsIgnoreCase("booots")) {
                DragonBoots dragonBoots = DragonBoots.getDragonBoots(newItem);
                if (dragonBoots != null) {
                    au.updateDragonBoots(dragonBoots);
                    finalItem = String.valueOf(au.getDragonBoots());
                }
            }
            if (itemType.equalsIgnoreCase("shield")) {
                DragonShield dragonShield = DragonShield.getDragonShield(newItem);
                if (dragonShield != null) {
                    au.updateDragonShield(dragonShield);
                    finalItem = String.valueOf(au.getDragonShield());
                }
            }
            if (!finalItem.equalsIgnoreCase("")) {
                e.reply(MessageUtil.info("Usage",
                        "Successfully set " + p.getMention() + "'s dragon " + itemType + " to " + finalItem));
                return;
            }
        }
        if (arg.equalsIgnoreCase("setinvwither") || (arg.equalsIgnoreCase("setinvw"))) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setinvwither <userid> <rarity> <itemType>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String rarity = args[2];
            String itemType = args[3];
            Player p = Player.getPlayer(tid);
            ArmorWitherUser au = p.getArmorWitherUser();
            String newItem = rarity + " wither " + itemType;
            String finalItem = "";
            if (itemType.equalsIgnoreCase("weapon") || (itemType.equalsIgnoreCase("sword"))) {
                WitherWeapon witherWeapon = WitherWeapon.getWeapon(newItem);
                if (witherWeapon != null) {
                    au.updateWitherWeapon(witherWeapon);
                    finalItem = String.valueOf(au.getWitherWeapon());
                }
            }
            if (itemType.equalsIgnoreCase("helmet")) {
                WitherHelmet witherHelmet = WitherHelmet.getWitherHelmet(newItem);
                if (witherHelmet != null) {
                    au.updateWitherHelmet(witherHelmet);
                    finalItem = String.valueOf(au.getWitherHelmet());
                }
            }
            if (itemType.equalsIgnoreCase("chestplate")) {
                WitherChestplate witherChestplate = WitherChestplate.getWitherChestplate(newItem);
                if (witherChestplate != null) {
                    au.updateWitherChestplate(witherChestplate);
                    finalItem = String.valueOf(au.getWitherChestplate());
                }
            }
            if (itemType.equalsIgnoreCase("gloves")) {
                WitherGloves witherGloves = WitherGloves.getWitherGloves(newItem);
                if (witherGloves != null) {
                    au.updateWitherGloves(witherGloves);
                    finalItem = String.valueOf(au.getWitherGloves());
                }
            }
            if (itemType.equalsIgnoreCase("trousers")) {
                WitherTrousers witherTrousers = WitherTrousers.getWitherTrousers(newItem);
                if (witherTrousers != null) {
                    au.updateWitherTrousers(witherTrousers);
                    finalItem = String.valueOf(au.getWitherTrousers());
                }
            }
            if (itemType.equalsIgnoreCase("booots")) {
                WitherBoots witherBoots = WitherBoots.getWitherBoots(newItem);
                if (witherBoots != null) {
                    au.updateWitherBoots(witherBoots);
                    finalItem = String.valueOf(au.getWitherBoots());
                }
            }
            if (itemType.equalsIgnoreCase("shield")) {
                WitherShield witherShield = WitherShield.getWitherShield(newItem);
                if (witherShield != null) {
                    au.updateWitherShield(witherShield);
                    finalItem = String.valueOf(au.getWitherShield());
                }
            }
            if (!finalItem.equalsIgnoreCase("")) {
                e.reply(MessageUtil.info("Usage",
                        "Successfully set " + p.getMention() + "'s wither " + itemType + " to " + finalItem));
                return;
            }
        }

        if (arg.equalsIgnoreCase("removediamonds")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removediamonds <userid> <amount | long/bigint>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            String amount = args[2];
            long finalamount = DiamondsUtil.getDiamonds(tid) - Integer.parseInt(amount);
            DiamondsUtil.setDiamonds(tid, finalamount);
            e.reply(MessageUtil.success("Diamonds removed",
                    "Removed `" + FormatUtil.formatCommas(amount) + "` diamonds to " + tid));
            return;
        }
        if (arg.equalsIgnoreCase("vote")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin vote <user id>`"));
                return;
            }
            String tid = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                tid = uid;
            }
            VoteClient.vote(tid, VoteClient.isWeekend(), true);
            e.reply("Done.");
            return;
        }
        if (arg.equalsIgnoreCase("event")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin event <event_name> start/end`"));
                return;
            }
            String eventName = args[1].replace("_", " ");
            String action = args[2].toLowerCase();
            SpecialEvent specialEvent = SpecialEvent.getSpecialEvent(eventName);
            if (specialEvent == null) {
                String events = "";
                for (SpecialEvent specialEvent1 : SpecialEvent.getSpecialEvents()) {
                    events += "- " + specialEvent1.getName() + "\n";
                }
                e.reply(MessageUtil.err("Error",
                        "Invalid event - make sure to use underscores instead of spaces.\nEvents:\n" + events));
                return;
            }
            if (action.equals("start")) {
                if (SpecialEvent.isEventActive()) {
                    e.reply(MessageUtil.err("Error",
                            "There is already an active event!\nMake sure to end it before starting another one."));
                    return;
                }
                specialEvent.start();
                e.reply(MessageUtil.success("Event", "Started the " + specialEvent.getName() + " Event!"));
                return;
            }
            if (action.equals("end")) {
                if (!SpecialEvent.isEventActive()) {
                    e.reply(MessageUtil.err("Error", "There is no active event."));
                    return;
                }
                if (!SpecialEvent.getCurrentEvent().getName().equals(specialEvent.getName())) {
                    e.reply(MessageUtil.err("Error",
                            "The active event is a " + SpecialEvent.getCurrentEvent().getName() +
                                    " event.\nYou tried to end the " + specialEvent.getName() + " event."));
                    return;
                }
                SpecialEvent.getCurrentEvent().end();
                e.reply(MessageUtil.success("Event", "Stopped the " + specialEvent.getName() + " event."));
                return;
            }
            e.reply(MessageUtil.err("Error", "Invalid action. Use the action `start` or `end`."));
            return;
        }
        if (arg.equalsIgnoreCase("holidayevent")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin holidayevent <event_name> start/end`"));
                return;
            }
            String eventName = args[1].replace("_", " ");
            String action = args[2].toLowerCase();
            HolidayEvent holidayEvent = HolidayEvent.getHolidayEvent(eventName);
            if (action.equals("start")) {
                if (HolidayEvent.isEventActive()) {
                    e.reply(MessageUtil.err("Error",
                            "There is already an active holiday event!\nMake sure to end it before starting another one."));
                    return;
                }
                holidayEvent.start();
                e.reply(MessageUtil.success("Event", "Started the " + holidayEvent.getName() + " Event!"));
                return;
            }
            if (action.equals("end")) {
                if (!HolidayEvent.isEventActive()) {
                    e.reply(MessageUtil.err("Error", "There is no active event."));
                    return;
                }
                if (!HolidayEvent.getCurrentEvent().getName().equals(holidayEvent.getName())) {
                    e.reply(MessageUtil.err("Error",
                            "The active event is a " + HolidayEvent.getCurrentEvent().getName() +
                                    " event.\nYou tried to end the " + holidayEvent.getName() + " event."));
                    return;
                }
                HolidayEvent.getCurrentEvent().end();
                e.reply(MessageUtil.success("Event", "Stopped the " + holidayEvent.getName() + " event."));
                return;
            }
            e.reply(MessageUtil.err("Error", "Invalid action. Use the action `start` or `end`."));
            return;
        }
        if (arg.equalsIgnoreCase("unban")) {
            String user = args[1];
            BanManager.unban(user);
            e.reply("Successfully unbanned <@!" + user + ">");
            return;
        }
        if (arg.equalsIgnoreCase("msguser")) {
            String user = args[1];
            StringBuilder msg = new StringBuilder();
            for (int i = 2; i < args.length; i++)
                msg.append(args[i]).append(" ");
            User t = Player.getUser(user);
            if (t == null) {
                e.reply(MessageUtil.err("Error", "Can't find user."));
                return;
            }
            final String msg1 = msg.toString();
            boolean sent = MessageUtil.sendMessage(t, msg1, true);
            if (sent) {
                e.reply(MessageUtil.success("Message sent", "Successfully sent the message to the user."));
                return;
            }
            e.reply(MessageUtil.err("Error",
                    "Message could not be delivered.\nThe user isn't in any server with Withering or blocked messages" +
                            " from the bot."));
            return;
        }
        if (arg.equalsIgnoreCase("fullban")) {
            String banUid = args[1];
            StringBuilder reason = new StringBuilder();
            for (int i = 3; i < args.length; i++) reason.append(args[i]).append(" ");
            if (reason.toString().endsWith(" ")) reason = new StringBuilder(reason.substring(0, reason.length() - 1));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
            //
            int field;
            int timeAmount;
            String timeString = args[2];
            try {
                timeAmount = Integer.parseInt(timeString.substring(0, timeString.length() - 1));
                char lastChar = timeString.substring(timeString.length() - 1).toCharArray()[0];
                if (lastChar != 'm' && lastChar != 'h' && lastChar != 'd') field = -1;
                else {
                    if (lastChar == 'h') field = Calendar.HOUR;
                    else if (lastChar == 'd') field = Calendar.DAY_OF_YEAR;
                    else field = Calendar.MINUTE;
                }
                if (field == -1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.err("Error",
                        "Invalid time format!\nFormat: `#X`\n\n# = Number\nX = Unit (only `m` for minutes, `h` for " +
                                "hours and `d` for days is working)\nExample: `3d` = 3 days"));
                return;
            }
            //
            calendar.add(field, timeAmount);
            Date toDate = calendar.getTime();
            BanManager.ban(banUid, toDate, reason.toString(), e.getAuthor().getId());
            e.reply(MessageUtil.success("User banned",
                    "User ID: " + banUid + "\nReason: " + reason + "\nTo date: " + FormatUtil.fromDate(toDate)));
            return;
        }
        if (arg.equalsIgnoreCase("keyall")) {
            String keyType = "";
            for (int i = 1; i < args.length; i++) keyType += args[i] + " ";
            if (keyType.endsWith(" ")) keyType = keyType.substring(0, keyType.length() - 1);
            if (!keyType.endsWith(" key")) keyType = keyType + " key";
            e.reply("Key all started: " + keyType + "\nPlease wait");
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId FROM profiles;")) {
                while (rs.next()) {
                    String rs_uid = rs.getString("userId");
                    KeysUtil.setKeys(rs_uid, keyType, KeysUtil.getKeys(rs_uid, keyType) + 1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.reply("Key'd all a " + keyType + "!");
            return;
        }

        if (arg.equalsIgnoreCase("clearbarriers")) {
            if (args.length < 2) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin clearbarriers <all | rank | ID>`"));
                return;
            }
            String clear = args[1];
            if (clear.equalsIgnoreCase("all")) {
                CooldownUtil.removeAllCooldowns("pvp_barrier");
                e.reply(MessageUtil.success("barriers cleared", "Cleared all barriers"));
                return;
            }
            return;
        }
        if (arg.equalsIgnoreCase("addkey")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addkey <user id> <key> <amount>`\nExample: `" + userPrefix +
                                "admin addkey <user id> metal` - the `key` at the end is not needed"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            long amount = 1;
            StringBuilder key = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                if (i == (args.length - 1)) {
                    try {
                        amount = Long.parseLong(args[i]);
                    } catch (NumberFormatException ex) {
                        key.append(args[i]).append(" ");
                    }
                    continue;
                }
                key.append(args[i]).append(" ");
            }
            if (key.toString().endsWith(" ")) key = new StringBuilder(key.substring(0, key.length() - 1));
            if (!key.toString().endsWith(" key")) {
                key = new StringBuilder(key.toString() + " key");
            }
            KeysUtil.setKeys(userId, key.toString(), KeysUtil.getKeys(userId, key.toString()) + amount);
            e.reply(MessageUtil
                    .success("Key given", "Successfully gave `x" + amount + "`" + key.toString() + " to " + userId + "!"));
            return;
        }
        if (arg.equalsIgnoreCase("removekey")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removekey <user id> <key> <amount>`\nExample: `" + userPrefix +
                                "admin removekey <user id> metal` - the `key` at the end is not needed"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            long amount = 1;
            StringBuilder key = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                if (i == (args.length - 1)) {
                    try {
                        amount = Long.parseLong(args[i]);
                    } catch (NumberFormatException ex) {
                        key.append(args[i]).append(" ");
                    }
                    continue;
                }
                key.append(args[i]).append(" ");
            }
            if (key.toString().endsWith(" ")) key = new StringBuilder(key.substring(0, key.length() - 1));
            if (!key.toString().endsWith(" key")) {
                key = new StringBuilder(key.toString() + " key");
            }
            KeysUtil.setKeys(userId, key.toString(), KeysUtil.getKeys(userId, key.toString()) - amount);
            e.reply(MessageUtil
                    .success("Key removed", "Successfully removed `x" + amount + "`" + key.toString() + " from " + userId + "!"));
            return;
        }
        if (arg.equalsIgnoreCase("giveall")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin giveall <item name>`"));
                return;
            }
            StringBuilder item = new StringBuilder();
            for (int i = 1; i < args.length; i++) item.append(args[i]).append(" ");
            if (item.toString().endsWith(" ")) item = new StringBuilder(item.substring(0, item.length() - 1));
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM profiles;")) {
                while (rs.next()) {
                    Player.getPlayer(rs.getString("userId")).getInventoryUser().addItem(item.toString(), 1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.reply(MessageUtil.success("GiveAll", "Gave everyone `1x` of `" + item + "`"));
            return;
        }


        if (arg.equalsIgnoreCase("givepackageall")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM profiles;")) {
                while (rs.next()) {
                    String uid1 = rs.getString("userId");
                    Player p = Player.getPlayer(uid1);
                    boolean rank50 = p.getExpUser().getLevel() >= 50 && p.getExpUser().getLevel() < 75;
                    boolean rank75 = p.getExpUser().getLevel() >= 75 && p.getExpUser().getLevel() < 115;

                    p.getInventoryUser().addItem("wood key", 5);
                    p.getInventoryUser().addItem("metal key", 8);
                    if (rank50) {
                        p.getInventoryUser().addItem("wood key", 8);
                        p.getInventoryUser().addItem("metal key", 11);
                        p.getInventoryUser().addItem("titanium key", 15);
                    }
                    if (rank75) {
                        p.getInventoryUser().addItem("wood key", 5);
                        p.getInventoryUser().addItem("metal key", 8);
                        p.getInventoryUser().addItem("titanium key", 11);
                        p.getInventoryUser().addItem("steel key", 15);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.reply(MessageUtil.success("Done", "Done"));
            return;
        }

        if (arg.equalsIgnoreCase("givebetapackage")) {
            String uid1 = args[1];
            Player p = Player.getPlayer(uid1);
            int level = Integer.parseInt(args[2]);
            boolean rank50 = level >= 50 && level < 75;
            boolean rank75 = level >= 75 && level < 115;
            boolean rank115 = level >= 115 && level < 150;
            boolean rank150 = level >= 150;
            User u1 = Player.getUser(uid1);
            if (u1 != null) {
                final String t = Constants.TAB;
                final String extra =
                        rank50 ? "\n\n" +
                                "[Level 50+ Rewards]" +
                                "\n" + t + "- 1 Wood Key " + EmoteUtil.getEmoteMention("wood key") +
                                "\n" + t + "- 1 Metal Key " + EmoteUtil.getEmoteMention("metal key") +
                                "\n" + t + "- 1 titanium Key " + EmoteUtil.getEmoteMention("titanium key") +
                                "\n" + t + "- 1 Scroll " + EmoteUtil.getEmoteMention("scroll") +
                                "\n" + t + "- 1 Alloy Ingot " + EmoteUtil.getEmoteMention("alloy ingot")
                                : "";

                final String extra2 =
                        rank75 ? "\n\n" +
                                "[Level 75+ Rewards]" +
                                "\n" + t + "- 2 Wood Keys " + EmoteUtil.getEmoteMention("wood key") +
                                "\n" + t + "- 1 Metal Key " + EmoteUtil.getEmoteMention("metal key") +
                                "\n" + t + "- 1 titanium Key " + EmoteUtil.getEmoteMention("titanium key") +
                                "\n" + t + "- 1 Steel Key " + EmoteUtil.getEmoteMention("steel key") +
                                "\n" + t + "- 2 Scrolls " + EmoteUtil.getEmoteMention("scroll") +
                                "\n" + t + "- 1 Iron Scroll " + EmoteUtil.getEmoteMention("iron scroll") +
                                "\n" + t + "- 2 Alloy Ingots " + EmoteUtil.getEmoteMention("alloy ingot")
                                : "";

                final String extra3 =
                        rank115 ? "\n\n" +
                                "[Level 115+ Rewards]" +
                                "\n" + t + "- 3 Wood Keys " + EmoteUtil.getEmoteMention("wood key") +
                                "\n" + t + "- 2 Metal Keys " + EmoteUtil.getEmoteMention("metal key") +
                                "\n" + t + "- 1 titanium Key " + EmoteUtil.getEmoteMention("titanium key") +
                                "\n" + t + "- 1 Steel Key " + EmoteUtil.getEmoteMention("steel key") +
                                "\n" + t + "- 1 Dragon Steel Key " + EmoteUtil.getEmoteMention("dragon steel key") +
                                "\n" + t + "- 3 Scrolls " + EmoteUtil.getEmoteMention("scroll") +
                                "\n" + t + "- 2 Iron Scrolls " + EmoteUtil.getEmoteMention("iron scroll") +
                                "\n" + t + "- 1 Dragon Steel Scroll " + EmoteUtil.getEmoteMention("dragon steel scroll") +
                                "\n" + t + "- 3 Alloy Ingots " + EmoteUtil.getEmoteMention("alloy ingot") +
                                "\n" + t + "- 1 Betta Fish Pet [Rare] " + EmoteUtil.getEmoteMention("rare_betta_fish")
                                : "";

                final String extra4 =
                        rank150 ? "\n\n" +
                                "[Level 150+ Rewards]" +
                                "\n" + t + "- 4 Wood Keys " + EmoteUtil.getEmoteMention("wood key") +
                                "\n" + t + "- 2 Metal Keys " + EmoteUtil.getEmoteMention("metal key") +
                                "\n" + t + "- 1 titanium Key " + EmoteUtil.getEmoteMention("titanium key") +
                                "\n" + t + "- 1 Steel Key " + EmoteUtil.getEmoteMention("steel key") +
                                "\n" + t + "- 1 Dragon Steel Key " + EmoteUtil.getEmoteMention("dragon steel key") +
                                "\n" + t + "- 1 Titan Alloy Key " + EmoteUtil.getEmoteMention("titan alloy key") +
                                "\n" + t + "- 4 Scrolls " + EmoteUtil.getEmoteMention("scroll") +
                                "\n" + t + "- 3 Iron Scrolls " + EmoteUtil.getEmoteMention("iron scroll") +
                                "\n" + t + "- 2 Dragon Steel Scroll " + EmoteUtil.getEmoteMention("dragon steel scroll") +
                                "\n" + t + "- 5 Alloy Ingots " + EmoteUtil.getEmoteMention("alloy ingot") +
                                "\n" + t + "- 1 Betta Fish Pet [Rare] " + EmoteUtil.getEmoteMention("rare_betta_fish")
                                : "";

                u1.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("Beta Package",
                        "Thank you for playing Withering's Beta release! Withering has been fully reset" +
                                "All your items have been set to 0, however since you played BETA, you get to start with additional items.\n\n" +
                                "**Beta Package**\n" + t +
                                "- 1 Metal Key\n" + t + "- 2 Wood Keys" + extra + extra2 + extra3 + extra4)).queue());
            }
            p.getInventoryUser().addItem("wood key", 2);
            p.getInventoryUser().addItem("metal key", 1);
            BadgeUser.getBadgeUser(uid1).addBadge("betta_tester");
            if (rank50) {
                p.getInventoryUser().addItem("wood key", 1);
                p.getInventoryUser().addItem("metal key", 1);
                p.getInventoryUser().addItem("titanium key", 1);
                ScrollsUtil.addScrolls(uid1, 1);
                IngotUtil.addIngots(uid1, 1);
            }
            if (rank75) {
                p.getInventoryUser().addItem("wood key", 2);
                p.getInventoryUser().addItem("metal key", 1);
                p.getInventoryUser().addItem("titanium steel key", 1);
                p.getInventoryUser().addItem("steel steel key", 1);
                ScrollsUtil.addScrolls(uid1, 2);
                IronScrollsUtil.addIronScrolls(uid1, 1);
                IngotUtil.addIngots(uid1, 2);
            }
            if (rank115) {
                p.getInventoryUser().addItem("wood key", 3);
                p.getInventoryUser().addItem("metal key", 2);
                p.getInventoryUser().addItem("titanium steel key", 1);
                p.getInventoryUser().addItem("steel steel key", 1);
                p.getInventoryUser().addItem("dragon steel key", 1);
                ScrollsUtil.addScrolls(uid1, 3);
                IronScrollsUtil.addIronScrolls(uid1, 2);
                DragonScrollsUtil.addDragonScrolls(uid1, 1);
                IngotUtil.addIngots(uid1, 3);
                PetUtil.getPetUtil(uid1).addPet("RARE_BETTA_FISH", 1);
            }
            if (rank150) {
                p.getInventoryUser().addItem("wood key", 4);
                p.getInventoryUser().addItem("metal key", 2);
                p.getInventoryUser().addItem("titanium steel key", 1);
                p.getInventoryUser().addItem("steel steel key", 1);
                p.getInventoryUser().addItem("dragon steel key", 1);
                p.getInventoryUser().addItem("titan alloy key", 1);
                ScrollsUtil.addScrolls(uid1, 4);
                IronScrollsUtil.addIronScrolls(uid1, 3);
                DragonScrollsUtil.addDragonScrolls(uid1, 2);
                IngotUtil.addIngots(uid1, 5);
                PetUtil.getPetUtil(uid1).addPet("RARE_BETTA_FISH", 1);
            }
            return;
        }

        if (arg.equalsIgnoreCase("givealphapackage")) {
            String uid1 = args[1];
            Player p = Player.getPlayer(uid1);
            User u1 = Player.getUser(uid1);
            if (u1 != null) {
                final String t = Constants.TAB;
                u1.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("Alpha Package",
                        "Thank you for being an Alpha Player :)\n\n" +
                                "**Beta Package**\n" + t +
                                "- Alpha Tester Badge\n" + t +
                                "- Legend Alpha Predator")).queue());
            }
            BadgeUser.getBadgeUser(uid1).addBadge("alpha_tester");
            PetUtil.getPetUtil(uid1).addPet("LEGEND_ALPHA_PREDATOR", 1);
            return;
        }


        if (arg.equalsIgnoreCase("maintenance-message")) {
            if (args.length < 2) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin maintenance-message <message>`"));
                return;
            }
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) message.append(args[i]).append(" ");
            if (message.toString().endsWith(" "))
                message = new StringBuilder(message.substring(0, message.length() - 1));
            Withering.maintenance_message = message.toString();
            e.reply(MessageUtil.info("Maintenance", "Maintenance message set:\n" + message));
            return;
        }
        if (arg.equalsIgnoreCase("maintenance")) {
            boolean maintenance = Withering.maintenance;
            Withering.maintenance = !maintenance;
            e.reply(MessageUtil.info("Maintenance",
                    "Maintenance mode: " + Withering.maintenance + "\nMessage: " + Withering.maintenance_message));
            return;
        }
        if (arg.equalsIgnoreCase("addBadge")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin addBadge <user id> <badge>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String badge = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getBadgeUser().addBadge(badge);
            e.reply(MessageUtil.success("Badge added", "Added " + badge + " badge to `" + userId + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("removeBadge")) {
            if (args.length < 3) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin removeBadge <user id> <badge>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String badge = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getBadgeUser().removeBadge(badge);
            e.reply(MessageUtil.success("Badge removed", "Removed " + badge + " badge from `" + userId + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setlevel")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin setlevel <user id> <level>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            int level = Integer.parseInt(args[2]);
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getExpUser().setLevel(level);
            e.reply(MessageUtil.success("Level set", "Set level of `" + userId + "` to `" + level + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setelixirlevel")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin setelixirlevel <user id> <level>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            int level = Integer.parseInt(args[2]);
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getExpUser().setElixirLevel(level);
            e.reply(MessageUtil.success("Elixir Level set", "Set elixir level of `" + userId + "` to `" + level + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setmoney")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin setmoney <user id> <balance>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String balance = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getEcoUser().setBalance(new BigInteger(balance));
            e.reply(MessageUtil.success("Balance set",
                    "Set balance of `" + userId + "` to `" + FormatUtil.formatCommas(balance) + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("addmoney")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin addmoney <user id> <balance>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String balance = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getEcoUser().addBalance(new BigInteger(balance));
            e.reply(MessageUtil.success("Balance added",
                    "Added `" + FormatUtil.formatCommas(balance) + "` to the balance of `" + userId + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("removemoney")) {
            if (args.length < 3) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin removemoney <user id> <balance>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String balance = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getEcoUser().removeBalance(new BigInteger(balance));
            e.reply(MessageUtil.success("Balance removed",
                    "Removed `" + FormatUtil.formatCommas(balance) + "` from the balance of `" + userId + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setelixirexp")) {
            if (args.length < 3) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin setelixirexp <user id> <experience>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String xp = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getExpUser().setElixirExperience(new BigInteger(xp));
            e.reply(MessageUtil.success("Elixir Experience set",
                    "Set Elixir EXP of `" + userId + "` to `" + FormatUtil.formatCommas(xp) + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setexp")) {
            if (args.length < 3) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin setexp <user id> <experience>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String xp = args[2];
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player.getPlayer(userId).getExpUser().setExperience(new BigInteger(xp));
            e.reply(MessageUtil.success("Experience set",
                    "Set EXP of `" + userId + "` to `" + FormatUtil.formatCommas(xp) + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setpetexp")) {
            if (args.length < 3) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin setpetexp <user Id> <pet Id> <experience>`"));
                return;
            }
            String petId = args[2];
            String userId = args[1];
            Pets pets = PetUtil.getPetByID(Long.parseLong(petId));
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            if (pets == null) {
                e.reply(MessageUtil.err("Error", "This pet doesn't exist!"));
                return;
            }
            String xp = args[3];
            Player.getPlayer(userId).getPetExpUser().setPetExp(Integer.parseInt(xp), Integer.parseInt(petId));
            e.reply(MessageUtil.success("Experience set",
                    "Set EXP of `" + petId + "` " + pets.getPetName().toLowerCase().replace("_", " ") + " to `" + FormatUtil.formatCommas(xp) + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setpetlevel")) {
            if (args.length < 3) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin setpetlevel <user Id> <pet Id> <level>`"));
                return;
            }
            String petId = args[2];
            String userId = args[1];
            Pets pets = PetUtil.getPetByID(Long.parseLong(petId));
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            if (pets == null) {
                e.reply(MessageUtil.err("Error", "This pet doesn't exist!"));
                return;
            }
            String level = args[3];
            Player.getPlayer(userId).getPetExpUser().setPetLevel(Integer.parseInt(level), Integer.parseInt(petId));
            e.reply(MessageUtil.success("Experience set",
                    "Set Level of `" + petId + "` " + pets.getPetName() + " to `" + level + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("reset")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin reset <user id>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            if (!(Player.playerExists(userId))) {
                e.reply(MessageUtil.err("Error", "This user isn't registered!"));
                return;
            }
            Player p = Player.getPlayer(userId);
            if (args.length < 3) {
                e.reply(MessageUtil.info("Confirmation",
                        "You will be resetting " + p.getUser().getAsTag() + " `" + userId + "` " + p.getRankDisplay() +
                                "\nConfirm using `" + userPrefix + "admin reset " + userId + " confirm`"));
                return;
            }
            if (args[2].equalsIgnoreCase("confirm")) {
                Player.getPlayer(userId).reset();
                e.reply(MessageUtil.success("Resetted", "Successfully reset " + p.getUser().getAsTag()));
                return;
            }
            e.reply(MessageUtil.err("Error", "Invalid argument.\nDid you mean `confirm`?"));
            return;
        }

        if (arg.equalsIgnoreCase("removecooldown")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removecooldown <user id> <cooldown_name>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String cdname = args[2];
            CooldownUtil.removeCooldown(userId, cdname);
            e.reply(MessageUtil.success("Success", "Cooldown removed"));
            return;
        }
        if (arg.equalsIgnoreCase("clearcooldowns")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin clearcooldowns <user id>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            CooldownUtil.removeCooldown(userId, "pvp_barrier");
            CooldownUtil.removeCooldown(userId, "pvp_cooldown");
            CooldownUtil.removeCooldown(userId, "conquest_cooldown");
            CooldownUtil.removeCooldown(userId, "cmd_cd_");
            CooldownUtil.removeCooldown(userId, "mine_cooldown");
            e.reply(MessageUtil.success("Success", "All Cooldowns removed from " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("addorb")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addorbs <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            OrbUtil.addOrbs(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` Orbs to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removeorb")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removeorb <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            int finalamount = OrbUtil.getOrbs(userId) - Integer.parseInt(amount);
            OrbUtil.setOrbs(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Orbs Removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("setorb")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setorb <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            OrbUtil.setOrbs(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Set Orbs to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("addscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            ScrollsUtil.addScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` scrolls to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removescroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removescroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            int finalamount = ScrollsUtil.getScrolls(userId) - Integer.parseInt(amount);
            ScrollsUtil.setScrolls(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Scrolls Removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("setscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            ScrollsUtil.setScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Set scrolls to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("addwitherscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addwitherscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            WitherScrollsUtil.addWitherScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` Wither Scrolls to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removewitherscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removewitherscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            int finalamount = WitherScrollsUtil.getWitherScrolls(userId) - Integer.parseInt(amount);
            WitherScrollsUtil.setWitherScrolls(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Wither Scrolls Removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("setwitherscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setwitherscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            WitherScrollsUtil.setWitherScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Set Wither Scrolls to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("adddragonscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin adddragonscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            DragonScrollsUtil.addDragonScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` Dragon Scrolls to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removedragonscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removedragonscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            int finalamount = DragonScrollsUtil.getDragonScrolls(userId) - Integer.parseInt(amount);
            DragonScrollsUtil.setDragonScrolls(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Dragon Scrolls Removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("setdragonscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setdragonscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            DragonScrollsUtil.setDragonScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Set Dragon Scrolls to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("addironscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addironscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            IronScrollsUtil.addIronScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` Iron Scrolls to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removeironscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removeironscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            int finalamount = IronScrollsUtil.getIronScrolls(userId) - Integer.parseInt(amount);
            IronScrollsUtil.setIronScrolls(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Iron Scrolls Removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("setironscroll")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setironscroll <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            IronScrollsUtil.setIronScrolls(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Set Iron Scrolls to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("addtoken")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addtoken <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            TokensUtil.addTokens(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` tokens to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removetoken")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removetoken <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            int finalamount = TokensUtil.getTokens(userId) - Integer.parseInt(amount);
            TokensUtil.setTokens(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Tokens removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("settoken")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin settoken <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            TokensUtil.setTokens(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Tokens Set to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("addingot")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin addingot <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            IngotUtil.addIngots(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Added `" + amount + "` ingots to " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("removeingot")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removeingot <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            long finalamount = IngotUtil.getIngots(userId) - Integer.parseInt(amount);
            IngotUtil.setIngots(userId, finalamount);
            e.reply(MessageUtil.success("Success", "`" + amount + "` Ingots removed from " + userId));
            return;
        }
        if (arg.equalsIgnoreCase("setingot")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setingot <user id> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String amount = args[2];
            IngotUtil.setIngots(userId, Integer.parseInt(amount));
            e.reply(MessageUtil.success("Success", "Ingots Set to `" + amount + "` for " + userId));
            return;
        }

        if (arg.equalsIgnoreCase("sethealth")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin sethealth <user id> <health>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            int health = Integer.parseInt(args[2]);
            Player t = Player.getPlayer(userId);
            t.getHealthUser().setHealth(health);
            e.reply(MessageUtil
                    .success("Success", "Set health of " + t.getUser().getAsTag() + " to `" + health + "`!"));
            return;
        }
        if (arg.equalsIgnoreCase("setgender")) {
            if (args.length < 3) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "admin setgender <user id> <2, 1, 0 (Sir, Madam, Neutral)>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String newGender = args[2];
            Player p = Player.getPlayer(userId);
            Gender currentGender =  p.getGender();
            cache.put(uid, newGender);
            Withering.getBot().getDatabase().update("UPDATE profiles SET gender=? WHERE userId=?;", newGender, userId);
            e.reply(MessageUtil
                    .success("Success", "Set gender of " + p.getUser().getAsTag() + " to `" + newGender + "`!"));
            return;
        }

        if (arg.equalsIgnoreCase("settime")) {
            int minute = Integer.parseInt(args[1]);
            int hour = Integer.parseInt(args[2]);
            int day = Integer.parseInt(args[3]);
            int month = Integer.parseInt(args[4]);
            int year = Integer.parseInt(args[5]);
            //if (minute % 15 != 0 || minute % 30 != 0 || minute % 45 != 0 || minute % 60 != 0) {
             //   e.reply(MessageUtil.err("Error", "Please use 15 minute intervals for `minutes`. i.e 15, 30, 45, 60"));
             //   return;
            //}
            if (hour >= 25) {
                e.reply(MessageUtil.err("Error", "There's only 24 hours in a day."));
                return;
            }
            if (day >= 32) {
                e.reply(MessageUtil.err("Error", "Months dont have " + day + " days in them."));
                return;
            }
            if (month >= 13) {
                e.reply(MessageUtil.err("Error", "Each year has 12 months."));
                return;
            }
            InGameTime.setTime(minute, hour, day, month, year);
            InGameTime.updateTime();
            e.reply(MessageUtil
                    .success("Success", "The date is now;\n" + InGameTime.getCurrentTime() + "\n" + InGameTime.getCurrentDate()));
            return;
        }

        if (arg.equalsIgnoreCase("additem")) {
            if (args.length < 4) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin additem <user id> <item> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            StringBuilder item = new StringBuilder();
            long amount = 1;
            for (int i = 2; i < args.length; i++) {
                if (i == (args.length - 1)) {
                    try {
                        amount = Long.parseLong(args[i]);
                    } catch (NumberFormatException ex) {
                        item.append(args[i]).append(" ");
                    }
                    continue;
                }
                item.append(args[i]).append(" ");
            }
            if (item.toString().endsWith(" "))
                item = new StringBuilder(item.toString().substring(0, item.toString().length() - 1));
            if (!Player.playerExists(userId)) {
                e.reply(MessageUtil.err("Error", "Player not found or not started!"));
                return;
            }
            Player t = Player.getPlayer(userId);
            t.getInventoryUser().addItem(item.toString(), amount);
            e.reply(MessageUtil.success("Success",
                    "Player " + t.getUser().getAsTag() + " obtained `" + amount + "x` of `" + item + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("addpet")) {
            if (args.length < 4) {
                e.reply(MessageUtil
                        .info("Usage", "Please use `" + userPrefix + "admin additem <user id> <pet> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            StringBuilder pet = new StringBuilder();
            long amount = 1;
            for (int i = 2; i < args.length; i++) {
                if (i == (args.length - 1)) {
                    try {
                        amount = Long.parseLong(args[i]);
                    } catch (NumberFormatException ex) {
                        pet.append(args[i]).append(" ");
                    }
                    continue;
                }
                pet.append(args[i]).append(" ");
            }
            if (pet.toString().endsWith(" ")) pet = new StringBuilder(pet.toString().substring(0, pet.toString().length() - 1));
            if (!Player.playerExists(userId)) {
                e.reply(MessageUtil.err("Error", "Player not found or not started!"));
                return;
            }
            Player t = Player.getPlayer(userId);
            t.getPetUtil().addPet(pet.toString(), amount);
            e.reply(MessageUtil.success("Success",
                    "Player " + t.getUser().getAsTag() + " obtained `" + amount + "x` of `" + pet + "`"));
            return;
        }
        if (arg.equalsIgnoreCase("removeitem")) {
            if (args.length < 4) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removeitem <user id> <item_name/ID> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String item = args[2].replaceAll("_", " ");
            long amount;
            try {
                amount = Long.parseLong(args[3]);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.
                        err("Error", "<amount> must be a number!"));
                return;
            }
            if (!Player.playerExists(userId)) {
                e.reply(MessageUtil.err("Error", "Player not found or not started!"));
                return;
            }
            Player t = Player.getPlayer(userId);
            try {
                long itemID = Long.parseLong(item);
                t.getInventoryUser().fullRemoveItem(itemID);
            } catch (NumberFormatException ex) {
                t.getInventoryUser().removeItem(item, amount);
            }
            e.reply(MessageUtil.success("Success",
                    "Player " + t.getUser().getAsTag() + " lost `" + amount + "x` of `" + item + "`"));
            return;
        }

        if (arg.equalsIgnoreCase("setitemlevel")) {
            if (args.length < 4) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setitemlevel <user id> <item_name/ID> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String item = args[2].replaceAll("_", " ");
            long amount;
            try {
                amount = Long.parseLong(args[3]);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.
                        err("Error", "<amount> must be a number!"));
                return;
            }
            if (!Player.playerExists(userId)) {
                e.reply(MessageUtil.err("Error", "Player not found or not started!"));
                return;
            }
            Player t = Player.getPlayer(userId);
            try {
                long itemID = Long.parseLong(item);
                ItemLevelingUtil.setItemLevel(userId, (int) amount, itemID);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.
                        err("Error", "<amount> must be a valid ID!"));
                return;
            }
            e.reply(MessageUtil.success("Success",
                    "Player " + t.getUser().getAsTag() + " set item `" + item + "` to level `" + amount + "`"));
            return;
        }

        if (arg.equalsIgnoreCase("setitemstar")) {
            if (args.length < 4) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin setitemstar <user id> <item_name/ID> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String item = args[2].replaceAll("_", " ");
            long amount;
            try {
                amount = Long.parseLong(args[3]);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.
                        err("Error", "<amount> must be a number!"));
                return;
            }
            if (!Player.playerExists(userId)) {
                e.reply(MessageUtil.err("Error", "Player not found or not started!"));
                return;
            }
            Player t = Player.getPlayer(userId);
            try {
                long itemID = Long.parseLong(item);
                ItemStarringUtil.setItemStar(userId, (int) amount, itemID);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.
                        err("Error", "<amount> must be a valid ID!"));
                return;
            }
            e.reply(MessageUtil.success("Success",
                    "Player " + t.getUser().getAsTag() + " set star count `" + item + "` to `" + amount + "` stars"));
            return;
        }

        if (arg.equalsIgnoreCase("removepet")) {
            if (args.length < 4) {
                e.reply(MessageUtil.info("Usage",
                        "Please use `" + userPrefix + "admin removepet <user id> <item_name/ID> <amount>`"));
                return;
            }
            String userId = args[1];
            if (args[1].equalsIgnoreCase("self")) {
                userId = uid;
            }
            String pet = args[2].replaceAll("_", " ");
            long amount;
            try {
                amount = Long.parseLong(args[3]);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.
                        err("Error", "<amount> must be a number!"));
                return;
            }
            if (!Player.playerExists(userId)) {
                e.reply(MessageUtil.err("Error", "Player not found or not started!"));
                return;
            }
            Player t = Player.getPlayer(userId);
            try {
                long petID = Long.parseLong(pet);
                t.getPetUtil().removePet(petID);
            } catch (NumberFormatException ex) {
                t.getPetUtil().removePet(pet, amount);
            }
            e.reply(MessageUtil.success("Success",
                    "Player " + t.getUser().getAsTag() + " lost `" + amount + "x` of `" + pet + "`"));
            return;
        }
        e.reply(MessageUtil.err("Error", "Invalid argument!"));
    }
}