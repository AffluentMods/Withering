package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.List;

public class HelpCommand extends BotCommand {

    public HelpCommand() {
        this.name = "help";
        this.cooldown = 0.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        Player p = Player.getPlayer(uid);
        if (args.length < 1) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "help_plain", "Commands"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "help",
                    "For further information use: `" + userPrefix + "help <command>`\n\n" +

                            EmoteUtil.getEmoteMention("Scroll") + " **Statistical Commands** " + EmoteUtil.getEmoteMention("Scroll") + "\n" +
                            "`achievements`, `attribute`, `backpack`, `compare`, `cooldowns`, `event`, `inventory`, `inventoryiron`, `inventorydragon`, `inventorywither`, `keys`, `leaderboard`, `pets`\n\n" +

                            EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getName()) + " **Combat Commands** " + EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getName()) + "\n" +
                            "`arena`, `equip`, `conquest`, `holiday`, `heal`, `practice`, `random`, `unequip`\n\n" +

                            EmoteUtil.getCoin() + " **Economy Commands** " + EmoteUtil.getCoin() + "\n" +
                            "`elixir`, `forge`, `firework`, `gift`, `goldentavern`, `level`, `merchant`, `mine`, `open`, `scroll`, `star`, `tavern`, `trade`\n\n" +

                            EmoteUtil.getEmoteMention("Common_Gear") + " **Miscellaneous Commands** " + EmoteUtil.getEmoteMention("Common_Gear") + "\n" +
                            "`calendar`, `help`, `information`, `invite`, `journal-entry`, `lock`, `prefix`, `ping`, `rules`, `stats`, `start`, `settings`, `tutorial`, `unlock`, `verify`\n\n" +

                            EmoteUtil.getDiamond() + " **Bonus Rewards** " + EmoteUtil.getDiamond() + "\n" +
                            "`daily`, `donate`, `redeem`, `vote`\n\n" +

                            "**Links**\n" +
                            "[Support Server](https://discord.gg/withering) **|** [Invite Link](https://discord.com/api/oauth2/authorize?client_id=887850655862112268&permissions=379904&scope=bot) **|** [Patreon](https://www.patreon.com/withering)"));
            eb.setFooter("Hope you enjoy Withering :D", "https://i.imgur.com/RbHmy82.png");
            e.getTextChannel().sendMessage(eb.build()).queue();
        } else {
            if (args[0].equalsIgnoreCase("holiday")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "holiday_help", "Holiday Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "holiday <next | shop | info>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Holiday Commands only work if there is currently a holiday\n" +
                                "Holiday command is a unique dungeon like experience during the holidays, specifically for Christmas, and Halloween.\n" +
                                "Which, for these holidays also has a store to spend the unique items you obtain.\n\n" +
                                "<next> - Continues on to the next stage" +
                                "<shop> - Opens the store for the specific holiday" +
                                "<info> - Displays information for the current holiday")));
                return;
            }
            if (args[0].equalsIgnoreCase("calendar") || (args[0].equalsIgnoreCase("cal"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "calendar_help", "Calendar Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "calendar [month]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `cal`\n\n" +
                                "The calendar command can be ran to check the current Date and Time.\n" +
                                "[Month] - You can use this argument to check what specific events on coming in whichever month.")));
                return;
            }
            if (args[0].equalsIgnoreCase("inventorywither") || (args[0].equalsIgnoreCase("invwither") ||
                    (args[0].equalsIgnoreCase("pflwither") || args[0].equalsIgnoreCase("profilewither") || args[0].equalsIgnoreCase("invw")))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "wither_inventory_help", "Wither Inventory Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "inventorywither [helmet | chestplate | trousers | gloves | boots | sword | shield] [cost]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `invwither`, `pflwither`, `profilewither`, `invw`\n\n" +
                                "Wither is one of the 'other' types of inventory. These inventories provide an additional way to obtain" +
                                "a vast amount of power and upgrade items even further. Obtain/Upgrade this inventory with " +
                                "Wither Scrolls\n" +
                                "You can use (for example): `" + userPrefix + "invw chestplate cost` to check the cost.")));
                return;
            }
            if (args[0].equalsIgnoreCase("firework")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "firework_help", "Firework Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "firework`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "The Firework command can be used to launch any fireworks you may have (from New Years holiday).\n" +
                                "Fireworks give you `100%` more " + EmoteUtil.getCoin() + " coins, and `100%` more " + EmoteUtil.getEmoteMention("XP_Orb") + " experience\n" +
                                "The experience gain does not effect pet experience.")));
                return;
            }
            if (args[0].equalsIgnoreCase("gift")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "gift_help", "Gift Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "gift <player>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "During the Christmas holiday you have a small chance to obtain " + EmoteUtil.getEmoteMention("Holiday_Present") + " presents.\n" +
                                "Which can be used to gift other players, both players receive the same item, the amount of coins depend on whoever receives them.\n" +
                                "<player> - you can use someone's ID (887850655862112268) or Ping them (@Withering RPG#8258)")));
                return;
            }
            if (args[0].equalsIgnoreCase("backpack") || (args[0].equalsIgnoreCase("bp"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "backpack_help", "Backpack Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "backpack`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `bp`\n\n" +
                                "You can use the backpack command to check the amount of all your miscellaneous items like," +
                                " Diamonds, Coins, Scrolls, Holiday Items, Keys, Orbs, Ingots, etc all in one location.")));
                return;
            }
            if (args[0].equalsIgnoreCase("achievement") || (args[0].equalsIgnoreCase("achievements") || (args[0].equalsIgnoreCase("ach") ||
                    (args[0].equalsIgnoreCase("mission") || (args[0].equalsIgnoreCase("missions")))))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "achievements_help", "Achievement Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "achievements`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `achievement`, `ach`, `missions`, `mission`\n\n" +
                                "Achievements are another way to obtain " + EmoteUtil.getDiamond() + "Diamonds." +
                                "You don't need to claim achievements, diamonds are given automatically, you'll be dm'd when you unlock an achievement")));
                return;
            }
            if (args[0].equalsIgnoreCase("help")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "help_help", "Help Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "help <paramter>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "ehm, not sure why you needed help...with using help.\n" +
                                "But I shall provide help with using help, so you can get help.\n" +
                                "Helpception, hope you enjoy Withering my friend.")));
                return;
            }
            if (args[0].equalsIgnoreCase("lock")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "lock_help", "Lock Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "lock <Equipped Gear | item Id>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Locking items will prevent them from being dissolved for " + EmoteUtil.getEmoteMention("Orb") + " Orbs in the `" +
                                userPrefix + "forge`.\n" +
                                "You can check if an item is unlocked by using `" + userPrefix + "att <Equipped Gear | item Id>` and looking in the upper left for the " + EmoteUtil.getEmoteMention("locked"))));
                return;
            }
            if (args[0].equalsIgnoreCase("unlock")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "unlock_help", "Unlock Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "unlock <Equipped Gear | item Id>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Unlocking items will allow them to be dissolved for " + EmoteUtil.getEmoteMention("Orb") + " Orbs in the `" +
                                userPrefix + "forge`.\n" +
                                "You can check if an item is unlocked by using `" + userPrefix + "att <Equipped Gear | item Id>` and looking in the upper left for the " + EmoteUtil.getEmoteMention("locked"))));
                return;
            }
            if (args[0].equalsIgnoreCase("code") || (args[0].equalsIgnoreCase("redeem"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "code_help", "Code Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "code <code>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `redeem`\n\n" +
                                "Codes are unique and can only be used once per player.\n" +
                                "They are mainly given out if Withering is offline for a while, but they" +
                                "may also be given out on special occasions, like if theres a new update.\n" +
                                "Best way to stay in the loop of new codes is to be in the [Official Server](https://discord.gg/withering)")));
                return;
            }
            if (args[0].equalsIgnoreCase("tutorial") || (args[0].equalsIgnoreCase("tut"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "tutorial_help", "Tutorial Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "tutorial`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `tut`\n\n" +
                                "The tutorial will give you a quick run down on where to start in Withering. While it doesn't" +
                                "inform you of every command as that's what `" + userPrefix + "help` is for, " +
                                "it should assist you on your journey!")));
                return;
            }
            if (args[0].equalsIgnoreCase("donate")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "donate_help", "Donate Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "donate`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "The `" + userPrefix + "donate` command gives you a link to [Patreon](https://www.patreon.com/withering)\n" +
                                "Donator rewards are not meant to be p2w.")));
                return;
            }
            if (args[0].equalsIgnoreCase("compare") || (args[0].equalsIgnoreCase("com"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "cooldowns_help", "Cooldown Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "compare <ItemID>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `com`\n\n" +
                                "`" + userPrefix + "compare 123` - compares the item `123` with your equipped equivalent." + "\n" +
                                "You can compare non equipped gear with your currently equipped gear.")));
                return;
            }
            if (args[0].equalsIgnoreCase("cooldowns") || (args[0].equalsIgnoreCase("cd") || (args[0].equalsIgnoreCase("cds") ||
                    args[0].equalsIgnoreCase("timer") || args[0].equalsIgnoreCase("timers")))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "cooldowns_help", "Cooldown Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "cooldowns`.\n" +
                                "Aliases: `cd`, `cds`, `timer`, `timers`\n\n" +
                                "Check your current Timers including:\n" +
                                "- Cooldowns\n - barrier Duration\n - Bonuses\n\n" +
                                "Looking for something else? Check `" + userPrefix + "benefits`")));
                return;
            }
            if (args[0].equalsIgnoreCase("event")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "event_help", "Event Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "event <info | list>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Events are an ongoing feature that reset weekly, then the event changes.\n" +
                                "Once events reset, you'll be given all your rewards, which should be DM'd to you.\n\n" +
                                "<list> - Displays all possible events\n" +
                                "<info> - Displays information and rewards on the current event\n" +
                                "- Every event can be completed up to 3 times to obtain max rewards.")));
                return;
            }
            if (args[0].equalsIgnoreCase("inventory") || (args[0].equalsIgnoreCase("inv") ||
                    (args[0].equalsIgnoreCase("pfl") || args[0].equalsIgnoreCase("profile")))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "inventory_help", "Inventory Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "inventory [Player] [Sort] [Page]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `inv`, `pfl`, `profile`\n\n" +
                                "[Player] - You can obtain the inventory of other playes with the Player parameter by using (for example) `" +
                                userPrefix + "inventory Withering RPG#8258`, or by pinging `" + userPrefix + "inventory @Withering RPG#8258`\n\n" +
                                "[Sort] - You can sort the inventory by using (for example) `" +
                                userPrefix + "inventory wood` to sort inventory by wood.\n\n" +
                                "[Page] - Each inventory page consists of 15 items, which you can change the page of with (for example) `" +
                                userPrefix + "inventory 3` for page 3.\n\n" +
                                "You can sort the inventory with:\n" +
                                "- Materials [Wood, Copper, etc]\n - Rarity [Junk, Common, etc]\n - Gear Type [Helmets, Swords, etc]")));
                return;
            }
            if (args[0].equalsIgnoreCase("pet") || (args[0].equalsIgnoreCase("pets"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "pet_inventory_help", "Pet Inventory Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "pets [Sort] [Page]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `pet`\n\n" +
                                "[Player] - You can obtain the inventory of other playes with the Player parameter by using (for example) `" +
                                userPrefix + "pets Withering RPG#8258`, or by pinging `" + userPrefix + "pets @Withering RPG#8258`\n\n" +
                                "[Sort] - You can sort the inventory by using (for example) `" +
                                userPrefix + "pet rare` to sort inventory by rare pets.\n\n" +
                                "[Page] - Each inventory page consists of 15 pets, which you can change the page of with (for example) `" +
                                userPrefix + "pet 3` for page 3.\n\n" +
                                "You can sort the inventory with:\n" +
                                "- Rarity [Junk, Common, etc]")));
                return;
            }
            if (args[0].equalsIgnoreCase("inventoryiron") || (args[0].equalsIgnoreCase("inviron") ||
                    (args[0].equalsIgnoreCase("pfliron") || args[0].equalsIgnoreCase("profileiron") || args[0].equalsIgnoreCase("invi")))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "iron_inventory_help", "Iron Inventory Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "inventoryiron [helmet | chestplate | trousers | gloves | boots | sword | shield] [cost]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `inviron`, `pfliron`, `profileiron`, `invi`\n\n" +
                                "Iron is one of the 'other' types of inventory. These inventories provide an additional way to obtain" +
                                "a vast amount of power and upgrade items even further. Obtain/Upgrade this inventory with " +
                                "Iron Scrolls\n" +
                                "You can use (for example): `" + userPrefix + "invi helmet cost` to check the cost.")));
                return;
            }
            if (args[0].equalsIgnoreCase("inventorydragon") || (args[0].equalsIgnoreCase("invdragon") ||
                    (args[0].equalsIgnoreCase("pfldragon") || args[0].equalsIgnoreCase("profiledragon") || args[0].equalsIgnoreCase("invd")))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "dragon_inventory_help", "Dragon Inventory Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "inventorydragon [helmet | chestplate | trousers | gloves | boots | sword | shield] [cost]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `invdragon`, `pfldragon`, `profiledragon`, `invd`\n\n" +
                                "Dragon is one of the 'other' types of inventory. These inventories provide an additional way to obtain" +
                                "a vast amount of power and upgrade items even further. Obtain/Upgrade this inventory with " +
                                "Dragon Scrolls\n" +
                                "You can use (for example): `" + userPrefix + "invd chestplate cost` to check the cost.")));
                return;
            }
            if (args[0].equalsIgnoreCase("keys")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "keys_help", "Key Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "keys`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "To use your keys, use `" + userPrefix + "open <key> [amount]`")));
                return;
            }
            if (args[0].equalsIgnoreCase("top") || args[0].equalsIgnoreCase("lb") || args[0].equalsIgnoreCase("leaderboard") || args[0].equalsIgnoreCase("topboard")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "top_help", "Leaderboard Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "leaderboard <daily | diamonds | event | level | money> [page (1-10)]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `top`, `lb`, `topboard`\n\n" +
                                "`" + userPrefix + "top money 2` - Views the 2nd page of money leaderboard (#11 - #20)\n" +
                                "Check leaderboards to see top 100 players.")));
                return;
            }
            if (args[0].equalsIgnoreCase("pvp") || args[0].equalsIgnoreCase("attack") || args[0].equalsIgnoreCase("arena")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "arena_help", "Arena Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "arena <@user | user#tag>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `pvp`, `attack`\n\n" +
                                "`" + userPrefix + "arena Withering#8258` - Attacks Withering\n" +
                                "Attack a specific player to gain Gold Coins, Experience, and some possible Diamonds, and or Keys.")));
                return;
            }
            if (args[0].equalsIgnoreCase("equip") || args[0].equalsIgnoreCase("eq")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "equip_help", "Equip Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "equip <Item ID>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `eq`\n\n" +
                                "`" + userPrefix + "equip 123` - Equips the item ID `123`.\n" +
                                "`" + userPrefix + "equip pet 22` - Equips a pet with the ID of `22`.\n" +
                                "`" + userPrefix + "equip invi` - Equips your Iron Inventory `" + userPrefix + "help invi`.\n" +
                                "Equip items to grow stronger!")));
                return;
            }
            if (args[0].equalsIgnoreCase("unequip") || args[0].equalsIgnoreCase("ueq") || args[0].equalsIgnoreCase("uneq") || args[0].equalsIgnoreCase("unq")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "unequip_help", "Unequip Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "unequip <Helmet | Chestplate | Gloves | Trousers | Boots>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `ueq`, `uneq`, `unq`\n\n" +
                                "`" + userPrefix + "unequip helmet` - Unequips your currently equipped helmet.\n" +
                                "`" + userPrefix + "unequip pet` - Unequips currently equipped pet.\n" +
                                "`" + userPrefix + "unequip invi` - Unequips your Iron Inventory`, `" + userPrefix + "help invi`.\n" +
                                "Unequip your items | pets | others. You could also just `" + userPrefix + "equip <Item ID>` to equip a different item.\n" +
                                "You don't have to unequip first. Weapons cannot be unequipped.")));
                return;
            }
            if (args[0].equalsIgnoreCase("random")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "random_help", "Random Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "random`.\n\n" +
                                "Attack a random player to gain Gold Coins, Experience, and some possible Diamonds, and or Keys.")));
                return;
            }
            if (args[0].equalsIgnoreCase("practice")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "practice_help", "Practice Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "practice <@user | user#tag>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "practice Withering#8258` - Practices against Withering\n" +
                                "Practice a specific player to check how your gear holds up against them.")));
                return;
            }
            if (args[0].equalsIgnoreCase("heal")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "heal_help", "Heal Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "heal [confirm]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "heal confirm`\n" +
                                "Healing costs " + EmoteUtil.getCoin() + " Gold Coins.")));
                return;
            }
            if (args[0].equalsIgnoreCase("conquest") || (args[0].equalsIgnoreCase("cq"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "conquest_help", "Conquest Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "conquest <next>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `cq`\n\n" +
                                "`" + userPrefix + "conquest next` - Attacks the next NPC.\n" +
                                "Enter the conquest of many chapters to obtain Diamonds.\n\n" +
                                "Interested in some Withering lore? Check out `" + userPrefix + "help journal-entry`")));
                return;
            }
            if (args[0].equalsIgnoreCase("elixir")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "elixir_help", "Elixir Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "elixir <skill #> [amount]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "elixir 2` - Levels up the 2nd Elixir Skill.\n" +
                                "Elixir is a unique substance obtainable at level 150. Which you can spend on a select 4 skills that increase useful stats.")));
                return;
            }
            if (args[0].equalsIgnoreCase("merchant") || args[0].equalsIgnoreCase("shop") || args[0].equalsIgnoreCase("store")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "merchant_help", "Merchant Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "merchant <category> [item] [amount]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `store`, `shop`\n\n" +
                                "`" + userPrefix + "merchant keys wood key 1` - Purchases 1 Wood Key for" + EmoteUtil.getDiamond() + "\n" +
                                "Categories:\n" +
                                "- Keys (Buy keys with " + EmoteUtil.getDiamond() + ")\n" +
                                "- Misc (Buy miscellaneous items with " + EmoteUtil.getCoin() + ")")));
                return;
            }
            if (args[0].equalsIgnoreCase("mine") || args[0].equalsIgnoreCase("m")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "mine_help", "Mining Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "mine`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `m`\n\n" +
                                "Go on mining adventures to obtain Titan Alloy, which is used in the `" + userPrefix + "help forge`")));
                return;
            }
            if (args[0].equalsIgnoreCase("forge")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "forge_help", "Forge Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "forge <itemId>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Forge is one of the best ways to upgrade the rarity of your gear!\n" +
                                "You can also merge Titan Alloy items to make a Wither item, and Staff's at the forge!\n" +
                                "`" + userPrefix + "forge <itemId>` - will attempt to upgrade the rarity of an item\n" +
                                "`" + userPrefix + "forge smith wither <helmet | chestplate | etc> <itemId 1> <itemId 2> <itemId 3>` - will attempt to create a wither item\n" +
                                "`" + userPrefix + "forge smith staff <wood | copper | etc>` - will attempt to create a staff\n" +
                                "`" + userPrefix + "forge dissolve <itemID | material | rarity| etc>` - will attempt to dissolve items into orbs (for staffs)")));
                return;
            }
            if (args[0].equalsIgnoreCase("level")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "level_help", "Leveling Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "level <itemId> [amount] [cost]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Leveling items increases the base Protection | Damage by `0.525%`.\n" +
                                "Items can be leveled up to 300, the maximum level is determined by the amount of stars (`"
                                + userPrefix + "info itemstats` for additional information on maximum level).\n" +
                                "leveling Items cost's " + EmoteUtil.getCoin() + " add the word `cost` at the end of the level command to check the cost.\n" +
                                "Ex: `" + userPrefix + "level 18 [amount] cost`")));
                return;
            }
            if (args[0].equalsIgnoreCase("star")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "star_help", "Starring Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "star <itemId> [amount] [cost]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Starring items increases the base Protection | Damage by `2%`. Items can be starred 7 times.\n" +
                                "Starring pets increases the base Protection | Damage by `5%`. Pets can be starred 10 times.\n" +
                                "When items are starred the 7th time, they will obtain the rarity of `Artifact` (assuming its currently `Ancient` rarity).\n" +
                                "Pet's rarity cannot be changed in any way." +
                                "Starring items cost's " + EmoteUtil.getDiamond() + " add the word `cost` at the end of the star command to check the cost.\n" +
                                "Ex: `" + userPrefix + "star pet 18 [amount] cost`")));
                return;
            }
            if (args[0].equalsIgnoreCase("open")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "open_help", "Open Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "open <key> [amount]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "open wood 5` - Opens 5 Wood Chests\n" +
                                "Open chests to receive amazing items! Check your keys with `" + userPrefix + "keys`.")));
                return;
            }
            if (args[0].equalsIgnoreCase("scroll")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "scroll_help", "Scroll Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "scroll <Item id>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "scroll 123` - Scrolls the Item `123`\n" +
                                "You can scroll your gear to randomize the gear type, and or upgrade the rarity (`2%` chance).")));
                return;
            }
            if (args[0].equalsIgnoreCase("tavern")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "tavern_help", "Tavern Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "tavern <spin> [amount]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "tavern spin 50` - Spins in the tavern `x50` times\n" +
                                "You can spin in the tavern to obtain great rewards.")));
                return;
            }
            if (args[0].equalsIgnoreCase("goldentavern") || (args[0].equalsIgnoreCase("gt"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "golden_tavern_help", "Golden Tavern Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "goldentavern <spin> [amount]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `gt`\n\n" +
                                "`" + userPrefix + "goldentavern spin 50` - Spins in the golden tavern `x50` times\n" +
                                "You can spin in the golden tavern to obtain even greater rewards.")));
                return;
            }
            if (args[0].equalsIgnoreCase("trade")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "trade_help", "Trade Usage"),
                        Language.getLocalized(uid, "usage", "Please use {command_usage}.\n" +
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

                                        "__Example Commands:\n" +
                                        "`" + userPrefix + "trade add 5` - This would add an Item by the ID of `5` to the trade.\n" +
                                        "`" + userPrefix + "trade add money 1000` - This would 1,000 coins to the trade.\n" +
                                        "`" + userPrefix + "trade add pet 7` - This would add a Pet by the ID of `7` to the trade.\n" +
                                        "`" + userPrefix + "trade remove pet 7` - This would remove that same pet (`7`).\n" +

                                        "Trading has a `5%` tax on all trades involving " + EmoteUtil.getCoin() + ". While trading is " +
                                        "open to all players with no limits, All trades are logged; anyone who abuses the trading system " +
                                        "i.e Using alts to boost a main, may be banned from trading.\n\n"
                                )
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
            if (args[0].equalsIgnoreCase("attribute") || args[0].equalsIgnoreCase("att")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "attribute_help", "Attribute Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "attribute <id/item>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `att`\n\n" +
                                "`" + userPrefix + "attribute 50` - Checks the attributes of item ID `50`.\n" +
                                "`" + userPrefix + "attribute helmet` - Checks the attributes of your currently equipped `helmet`.\n" +
                                "You can check the attributes of select items.")));
                return;
            }
            if (args[0].equalsIgnoreCase("information") || args[0].equalsIgnoreCase("info")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "information_help", "Information Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "information <armor/chest/rarity/rank/weapons>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `info`\n\n" +
                                "`" + userPrefix + "information armor` - Provides additional information on armor.\n" +
                                "You can check out statistical information on multiple subjects.")));
                return;
            }
            if (args[0].equalsIgnoreCase("invite")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "information_help", "Information Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "invite`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Invite the bot to another server.")));
                return;
            }
            if (args[0].equalsIgnoreCase("journal-entry") || args[0].equalsIgnoreCase("je")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "journal-entry_help", "Journal-Entry Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "journal-entry <#chapter>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Indulge in the lore of Withering: Vast Kingdoms")));
                return;
            }
            if (args[0].equalsIgnoreCase("prefix")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "prefix_help", "Prefix Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "prefix <new prefix>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Change the prefix to a unique prefix specific to you, and you only.")));
                return;
            }
            if (args[0].equalsIgnoreCase("ping")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "ping_help", "Ping Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "ping`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Check the response rate of the gateway, and database.")));
                return;
            }
            if (args[0].equalsIgnoreCase("settings") || (args[0].equalsIgnoreCase("setting"))) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "settings_help", "Settings Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "settings <format | confirm>`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `setting`\n\n" +
                                "`" + userPrefix + "settings confirm <enable | disable>` - Enables or Disables the need of using `confirm`.\n" +
                                "`" + userPrefix + "settings format <pc | mobile>` - Changes the formatting of certain responses to be more mobile or pc friendly\n" +
                                "`" + userPrefix + "settings profilebar <enable | disable>` - Swaps out the HP/XP Bar in profile to HP, and XP" + "\n" +
                                "`" + userPrefix + "settings achievementdm <enable | disable>` - Enables or Disables the Direct Message from Withering when a new achievement tier is obtained\n\n" +
                                "Settings are a great way to make Withering more customized to your preferences")));
                return;
            }
            if (args[0].equalsIgnoreCase("rules")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "rules_help", "Rules Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "rules`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "This informs you of any current rules of Withering.")));
                return;
            }
            if (args[0].equalsIgnoreCase("stats")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "stats_help", "Stats Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "stats`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Check your total stats from your currently equipped gear. Along with your Kills/Deaths and KD ratio.")));
                return;
            }
            if (args[0].equalsIgnoreCase("start")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "start_help", "Start Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "start`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "Begin your adventure.")));
                return;
            }
            if (args[0].equalsIgnoreCase("verify")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "verify_help", "Verify Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "verify <captcha> [resend]`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "`" + userPrefix + "verify <captcha>` - Solve a captcha to continue playing.\n" +
                                "`" + userPrefix + "verify resend` - Lost the verify? Resend the verify!\n" +
                                "Verifies are here to keep the bot fair.")));
                return;
            }
            if (args[0].equalsIgnoreCase("daily")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "daily_help", "Daily Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "daily`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                "After you vote you can get daily rewards. Daily rewards have a streak of 10, " +
                                "once you reach the 10th day you reach the max day. If you miss a `" + userPrefix + "daily`, your streak will be reset to day 1.")));
                return;
            }
            if (args[0].equalsIgnoreCase("vote") || args[0].equalsIgnoreCase("v")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "vote_help", "Vote Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "vote`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `v`\n\n" +
                                "You can vote every 12 hours to obtain Tavern Tokens.")));
                return;
            }
            if (args[0].equalsIgnoreCase("benefits") || args[0].equalsIgnoreCase("benefit") || args[0].equalsIgnoreCase("b")) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "benefit_help", "Benefit Usage"),
                        Language.getLocalized(uid, "usage", "Please use `" + userPrefix + "benefits`.\n" +
                                "`<>` - Required Parameter\n`[]` - Optional Parameter\n" +
                                "Aliases: `benefit`, `b`\n\n" +
                                "Receive your daily donator benefits.")));
            }
        }
    }
}