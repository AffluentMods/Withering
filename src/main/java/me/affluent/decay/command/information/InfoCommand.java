package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.Helmet;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.Gender;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.language.Language;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigInteger;
import java.util.TreeMap;

public class InfoCommand extends BotCommand {

    public InfoCommand() {
        this.name = "info";
        this.aliases = new String[]{"information"};
        this.cooldown = 0.8;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length == 0) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info("Usage", Language.getLocalized(u.getId(), "usage", "Please use {Command_Usage}\n\n" +
                            " `armor` - Provides statistical information\n for Armor, along with level requirement\n\n" +
                            " `itemstats` - Provides statistical information\n for maximum level based on star count, along with costs\n\n" +
                            " `weapons` - Provides statistical information\n for Swords | Bows | Staffs, along with level requirement\n\n" +
                            " `miscweapons` - Provides statistical information\n for Arrows | Shields, along with level requirement\n\n" +
                            " `rank` - Provides statistical information\n for ranks, along with level requirement\n\n" +
                            " `tavern` - Provides statistical information\n for tavern rewards\n\n" +
                            " `goldentavern` - Provides statistical information\n for golden tavern rewards\n\n" +
                            " `chest` - Provides statistical information\n for chests\n\n" +
                            " `petstats` - Provides statistical information\n for Pets, along with any requirements to obtaining\n\n")
                    .replace("{Command_Usage}", "`" + userPrefix + "info <parameter>`")));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("weapons") || (arg.equalsIgnoreCase("weapon"))) {
            StringBuilder weapons = new StringBuilder();
            for (ItemType it : ItemType.values()) {
                String itn = it.name().toLowerCase().replaceAll("_", " ");
                itn = itn.substring(0, 1).toUpperCase() + itn.substring(1).toLowerCase();
                Weapon weapon = Weapon.getWeapon("common " + itn + " sword");
                if (weapon == null) continue;
                weapons.append(" ").append(EmoteUtil.getEmoteMention(itn+"_sword")).append(EmoteUtil.getEmoteMention(itn+"_bow")).append(EmoteUtil.getEmoteMention(itn+"_staff")).append(" ").append(itn).append(" - Damage: `").append(weapon.getDamageFrom()).append("-")
                        .append(weapon.getDamageTo()).append("`").append(". Need to be level `")
                        .append(it.getLevelRequirement()).append("` to equip.\n");
            }
            weapons.append("\n\n Staffs deal `37%` more damage than Swords | Bows");
            e.reply(MessageUtil.info("Weapons", weapons.toString()));
            return;
        }
        if (arg.equalsIgnoreCase("miscweapons") || (arg.equalsIgnoreCase("mw") || (arg.equalsIgnoreCase("miscweapon")))) {
            e.reply(MessageUtil.info("Weapons",
                    "Wood: Level `1` Required.\n" +
                            EmoteUtil.getEmoteMention("Wood_Arrow") + " Wood Arrow Damage: `1`.\n" +
                            EmoteUtil.getEmoteMention("Wood_Shield") + " Wood Shield Protection: `2`\n\n" +
                            "Copper: Level `15` Required.\n" +
                            EmoteUtil.getEmoteMention("Copper_Arrow") + " Copper Arrow Damage: `3`.\n" +
                            EmoteUtil.getEmoteMention("Copper_Shield") + " Copper Shield Protection: `4`\n\n" +
                            "Reinforced: Level `25` Required.\n" +
                            EmoteUtil.getEmoteMention("Reinforced_Arrow") + " Reinforced Arrow Damage: `5`.\n" +
                            EmoteUtil.getEmoteMention("Reinforced_Shield") + " Reinforced Shield Protection: `8`\n\n" +
                            "Titanium: Level `40` Required.\n" +
                            EmoteUtil.getEmoteMention("Titanium_Arrow") + " Titanium Arrow Damage: `8`.\n" +
                            EmoteUtil.getEmoteMention("Titanium_Shield") + " Titanium Shield Protection: `15`\n\n" +
                            "Iron: Level `55` Required.\n" +
                            EmoteUtil.getEmoteMention("Iron_Arrow") + " Iron Arrow Damage: `11`.\n" +
                            EmoteUtil.getEmoteMention("Iron_Shield") + " Iron Shield Protection: `23`\n\n" +
                            "Steel: Level `75` Required.\n" +
                            EmoteUtil.getEmoteMention("Steel_Arrow") + " Steel Arrow Damage: `15`.\n" +
                            EmoteUtil.getEmoteMention("Steel_Shield") + " Steel Shield Protection: `35`\n\n" +
                            "Carbon Steel: Level `90` Required.\n" +
                            EmoteUtil.getEmoteMention("Carbon_Steel_Arrow") + " Carbon Steel Arrow Damage: `19`.\n" +
                            EmoteUtil.getEmoteMention("Carbon_Steel_Shield") + "Carbon Steel Shield Protection: `54`\n\n" +
                            "Dragon Steel: Level `110` Required.\n" +
                            EmoteUtil.getEmoteMention("Dragon_Steel_Arrow") + " Dragon Steel Arrow Damage: `24`.\n" +
                            EmoteUtil.getEmoteMention("Dragon_Steel_Shield") + " Dragon Steel Shield Protection: `82`\n\n" +
                            "Titan Alloy: Level `150` Required.\n" +
                            EmoteUtil.getEmoteMention("Titan_Alloy_Arrow") + " Titan Alloy Arrow Damage: `30`.\n" +
                            EmoteUtil.getEmoteMention("Titan_Alloy_Shield") + " Titan Alloy Shield Protection: `107`\n\n" +
                            "Wither: Level `151` Required.\n" +
                            EmoteUtil.getEmoteMention("Wither_Arrow") + " Wither Arrow Damage: `55`.\n" +
                            EmoteUtil.getEmoteMention("Wither_Shield") + " Wither Shield Protection: `195`\n\n"));
            return;
        }
        if (arg.equalsIgnoreCase("itemstats")) {
            e.reply(MessageUtil.info("Item Stats",
                    "0 " + EmoteUtil.getEmoteMention("E_S") + "\n" +
                                    "Maximum Level; 25\n\n" +
                            "1 " + EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 55\n\n" +
                            "2 " + EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 90\n\n" +
                            "3 " + EmoteUtil.getEmoteMention("S_") +
                            EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 130\n\n" +
                            "4 " + EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                            EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 175\n\n" +
                            "5 " + EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                            EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                            EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 215\n\n" +
                            "6 " + EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                            EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                            EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 250\n\n" +
                            "7 " + EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                           EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                           EmoteUtil.getEmoteMention("S_") + EmoteUtil.getEmoteMention("S_") +
                           EmoteUtil.getEmoteMention("S_") + "\n" +
                                    "Maximum Level; 300\n\n" +
                           "Every " + EmoteUtil.getEmoteMention("S_") + "costs " + EmoteUtil.getDiamond() + " " +
                           "100 Diamonds multiplied by the star count, except for tier 7, which costs " + EmoteUtil.getDiamond() + " 1,000 Diamonds."
                            ));
            return;
        }
        if (arg.equalsIgnoreCase("goldentavern") || (arg.equalsIgnoreCase("gtavern"))) {
         e.reply(MessageUtil.info("Golden Tavern",
                 EmoteUtil.getEmoteMention("Scroll") + " [Scroll] | `25%` Chance | `x1`\n" +
                         EmoteUtil.getEmoteMention("Iron_Scroll") + " [Iron Scroll] | `15%` Chance | `x2`\n" +
                         EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " [Dragon Steel Scroll] | `5%` Chance | `x1`\n" +
                         EmoteUtil.getEmoteMention("Diamond") + " [Diamonds] | `32.5%` Chance | `x500`\n" +
                         EmoteUtil.getEmoteMention("Legend_Gear") + " [Legend Gear] | `2.5%` Chance | `x1`\n" +
                         EmoteUtil.getEmoteMention("Dragon_Steel_Key") + " [Dragon Steel Key] | `8%` Chance | `x1`\n" +
                         EmoteUtil.getEmoteMention("Titan_Alloy_Key") + " [Titan Alloy Key] | `2%` Chance | `x1`\n" +
                         EmoteUtil.getEmoteMention("Golden_Tavern_Token") + " [Golden Tavern Token] | `10%` Chance | `x2`"));
         return;
        }
        if (arg.equalsIgnoreCase("tavern")) {
            e.reply(MessageUtil.info("Tavern",
                    EmoteUtil.getEmoteMention("Steel_Key") + " [Steel Key] | `x1` | `3%` Chance\n" +
                            EmoteUtil.getEmoteMention("Dragon_Steel_Key") + " [Dragon Steel Key] | `x1` | `1.2%` Chance\n" +
                            EmoteUtil.getEmoteMention("Titan_Alloy_Key") + " [Titan Alloy Key] | `x1` | `0.3%` Chance\n" +
                            EmoteUtil.getEmoteMention("Alloy_Ingot") + " [Alloy Ingot] | `x10` | `6%` Chance\n" +
                            EmoteUtil.getDiamond() + " [Diamonds] | `x350` | `16%` Chance\n" +
                            EmoteUtil.getCoin() + " [Medallions] | `2%` | `44%` Chance\n" +
                            EmoteUtil.getEmoteMention("Scroll") + " [Scroll] | `x1` | `18%` Chance\n" +
                            EmoteUtil.getEmoteMention("Rare_Gear") + " [Rare Gear] | `x1` | `2.5%` Chance\n" +
                            EmoteUtil.getEmoteMention("Tavern_Token") + " [Tavern Token] | `x2` | `10%` Chance"));
            return;
        }
        if (arg.equalsIgnoreCase("armor")) {
            StringBuilder armor1 = new StringBuilder();
            for (ItemType it : ItemType.values()) {
                String itn = it.name().toLowerCase().replaceAll("_", " ");
                itn = itn.substring(0, 1).toUpperCase() + itn.substring(1).toLowerCase();
                Helmet armor = Helmet.getHelmet("common " + itn + " helmet");
                if (armor == null) continue;
                armor1.append(" ").append(EmoteUtil.getEmoteMention(itn+"_chestplate")).append(" ").append(itn).append(" - Protection: `").append(armor.getProtectionFrom()).append("-")
                        .append(armor.getProtectionTo()).append("`").append(". Need to be level `")
                        .append(it.getLevelRequirement()).append("` to equip.\n");
            }
            e.reply(MessageUtil.info("Armor", armor1.toString()));
            return;
        }
        if (arg.equalsIgnoreCase("chest")) {
            e.reply(MessageUtil.info("Chest",
                    EmoteUtil.getEmoteMention("Wood_Chest") + " [Wood Chest] | Level 1 Required\n" + EmoteUtil.getCoin() + "(`5-8`), Wood Gear (`65%` Chance), Copper Gear (`30%` Chance), or Reinforced Gear (`5%` Chance).\n\n" +
                    EmoteUtil.getEmoteMention("Metal_Chest") + " [Metal Chest] | Level 25 Required\n" + EmoteUtil.getCoin() + "(`22-36`), Copper Gear (`65%` Chance), Reinforced Gear (`30%` Chance), or Titanium Gear (`5%` Chance).\n\n" +
                    EmoteUtil.getEmoteMention("Titanium_Chest") + " [Titanium Chest] | Level 40 Required\n" + EmoteUtil.getCoin() + "(`85-115`), Reinforced Gear (`65%` Chance), Titanium Gear (`30%` Chance), or Iron Gear (`5%` Chance).\n\n" +
                    EmoteUtil.getEmoteMention("Steel_Chest") + " [Steel Chest] | Level 60 Required\n" + EmoteUtil.getCoin() + "(`225-335`), Titanium Gear (`65%` Chance), Iron Gear (`30%` Chance), or Steel Gear (`5%` Chance).\n\n" +
                    EmoteUtil.getEmoteMention("Dragon_Steel_Chest") + " [Dragon Steel Chest] | Level 75 Required\n" + EmoteUtil.getCoin() + "(`420-640`), Iron (`50%` Chance), Steel (`35%` Chance), Carbon Steel Gear (`10%` Chance), or Dragon Steel Gear (`5%` Chance).\n\n" +
                    EmoteUtil.getEmoteMention("Titan_Alloy_Chest") + " [Titan Alloy Chest] | Level 90 Required\n" + EmoteUtil.getCoin() + "(`1200-1750`), Steel Gear (`50%` Chance), Carbon Steel Gear (`37%` Chance), Dragon Steel Gear (`12%` Chance) or Titan Alloy Gear (`2%` Chance)"));
            return;
        }
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (arg.equalsIgnoreCase("petstats")) {
            PetItem petObj = null;
            String petName = "";
            if (args.length < 2) {
                e.reply(MessageUtil.info("Usage", "Please use `" + userPrefix + "info petstats <petName | ID>`\n\n" +
                     "[`1`] " + EmoteUtil.getEmoteMention("Junk_Bee") + " Junk Bee\n" +
                     "[`2`] " + EmoteUtil.getEmoteMention("Junk_Mongolian_Horse") + " Junk Mongolian Horse\n" +
                     "[`3`] " + EmoteUtil.getEmoteMention("Common_Mongolian_Horse") + " Common Mongolian Horse\n" +
                     "[`4`] " + EmoteUtil.getEmoteMention("Common_Andalusian_Horse") + " Common Andalusian Horse\n" +
                     "[`5`] " + EmoteUtil.getEmoteMention("Uncommon_Mongolian_Horse") + " Uncommon Mongolian Horse\n" +
                     "[`6`] " + EmoteUtil.getEmoteMention("Uncommon_Andalusian_Horse") + " Uncommon Andalusian Horse\n" +
                     "[`7`] " + EmoteUtil.getEmoteMention("Uncommon_Shire_Horse") + " Uncommon Shire Horse\n" +
                     "[`8`] " + EmoteUtil.getEmoteMention("Rare_Andalusian_Horse") + " Rare Andalusian Horse\n" +
                     "[`9`] " + EmoteUtil.getEmoteMention("Rare_Shire_Horse") + " Rare Shire Horse\n" +
                     "[`10`] " + EmoteUtil.getEmoteMention("Rare_Betta_Fish") + " Rare Betta Fish\n" +
                     "[`11`] " + EmoteUtil.getEmoteMention("Rare_Wolf") + " Rare Wolf\n" +
                     "[`12`] " + EmoteUtil.getEmoteMention("Epic_Shire_Horse") + " Epic Shire Horse\n" +
                     "[`13`] " + EmoteUtil.getEmoteMention("Epic_Wolf") + " Epic Wolf\n" +
                     "[`14`] " + EmoteUtil.getEmoteMention("Epic_Plague_Bearer") + " Epic Plague Bearer\n" +
                     "[`15`] " + EmoteUtil.getEmoteMention("Legend_Alpha_Predator") + " Legend Alpha Predator\n" +
                     "[`16`] " + EmoteUtil.getEmoteMention("Mythic_Snowman") + " Mythic Snowman\n" +
                     "[`17`] " + EmoteUtil.getEmoteMention("Mythic_Reaper") + " Mythic Reaper\n" +
                     "[`18`] " + EmoteUtil.getEmoteMention("Ancient_Kraken") + " Ancient Kraken\n"));
                return;
            }
            String type = args[1].toLowerCase();
            int intValue = 0;
            try {
                intValue = Integer.parseInt(type);
                if (intValue < 1 || intValue > 18) {
                    e.reply(MessageUtil.err(Language.getLocalized(uid, "error", "Error"),
                            Language.getLocalized(uid, "unknown_object", "Unknown Pet, Number must be between 1, and 18.")));
                    return;
                }
                petName = getPetName(intValue);
                petObj = PetItem.getPetItem(petName);
            } catch (NumberFormatException ex) {
                type += " " + args[2].toLowerCase();
                if (args.length == 4) {
                    type += " " + args[3].toLowerCase();
                }
                System.out.println(type);
                petName = type;
                petObj = PetItem.getPetItem(type);
            }
            if (petObj == null) {
                e.reply(MessageUtil.err(Language.getLocalized(uid, "error", "Error"),
                        Language.getLocalized(uid, "unknown_object", "Unknown Pet, be sure you spelt it correctly.")));
                return;
            }
            String petSuburl = "https://thewithering.com/";
            String[] petColor = petName.replaceAll("_", " ").split(" ");
            String petRarityColor = petColor[0];
            int petLevel = 1;
            int petStars = 0;
            StringBuilder petStarDisplay = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                petStarDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
            }
            petSuburl += petName.replaceAll(" ", "_").toUpperCase() + ".png";
            int petPower = Power.getFullPetPower(Player.getPlayer(uid), petObj, petLevel, petStars);
            String obtainable = getPetObtainable(petName.toLowerCase(), userPrefix);
            String obtainableTitle = "";
            if (obtainable.contains(userPrefix) || petName.equalsIgnoreCase("epic plague bearer")) obtainableTitle = "Obtainable";
            else obtainableTitle = "Unobtainable";
            String petResponse = Language.getLocalized(uid, "info_attributes_pet", "LVL {pet_level}" + "\n\n" +
                    "HP | DMG\n" +
                    "{base_HP} | {base_DMG}\n" +
                    "{pet_star_display}\n" +
                    "{divider}\n" +
                    "{power_rating} Power\n" +
                    "HP per lvl: {pet_per_lvl_HP}\n" +
                    "DMG per lvl: {pet_per_lvl_DMG}\n\n" +
                            "[{obtainable_title}]\n" +
                            "{obtainable}")
                    .replace("{pet_level}", "**" + petLevel + "**")
                    .replace("{base_HP}", "**" + petObj.getStarterHP() + "**")
                    .replace("{base_DMG}", "**" + petObj.getStarterDMG() + "**")
                    .replace("{pet_star_display}", petStarDisplay.toString())
                    .replace("{power_rating}", "**" + petPower + "**")
                    .replace("{pet_per_lvl_HP}", "" + petObj.getLevelHP())
                    .replace("{pet_per_lvl_DMG}", "" + petObj.getLevelDMG())
                    .replace("{obtainable}", obtainable)
                    .replace("{obtainable_title}", obtainableTitle)
                    .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                            + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                            + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                            + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                            + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            EmbedBuilder ebPet = new EmbedBuilder();
            ebPet.setTitle(Language.getLocalized(uid, "info_attributes_pet_title", capitalizeFully(petName)));
            ebPet.setDescription(petResponse);
            ebPet.setThumbnail(petSuburl);
            ebPet.setColor(petRarityColor(PetRarity.valueOf(petRarityColor.toUpperCase())));
            ebPet.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(ebPet.build());
            return;
        }

        if (arg.equalsIgnoreCase("rank")) {
            if (Player.getPlayer(uid).getGender() == Gender.SIR) {
                StringBuilder rankss = new StringBuilder();
                TreeMap<Integer, Rank> ranks = new TreeMap<>(Rank.getRanks());
                for (int level : ranks.keySet()) {
                    Rank rank = ranks.get(level);
                    rankss.append(" **").append(rank.getsirName()).append("** - Level required `").append(level).append("`\n");
                }
                e.reply(MessageUtil.info("Level Requirement", rankss.toString()));
                return;
            }
            if (Player.getPlayer(uid).getGender() == Gender.MADAM) {
                StringBuilder rankss = new StringBuilder();
                TreeMap<Integer, Rank> ranks = new TreeMap<>(Rank.getRanks());
                for (int level : ranks.keySet()) {
                    Rank rank = ranks.get(level);
                    rankss.append(" **").append(rank.getmadamName()).append("** - Level required `").append(level).append("`\n");
                }
                e.reply(MessageUtil.info("Level Requirement", rankss.toString()));
                return;
            }
            if (Player.getPlayer(uid).getGender() == Gender.NEUTRAL) {
                StringBuilder rankss = new StringBuilder();
                TreeMap<Integer, Rank> ranks = new TreeMap<>(Rank.getRanks());
                for (int level : ranks.keySet()) {
                    Rank rank = ranks.get(level);
                    rankss.append(" **").append(rank.getneutralName()).append("** - Level required `").append(level).append("`\n");
                }
                e.reply(MessageUtil.info("Level Requirement", rankss.toString()));
                return;
            }
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    public String getPetName(int id) {
        switch (id) {
            case 1:
                return "Junk Bee";
            case 2:
                return "Junk Mongolian Horse";
            case 3:
                return "Common Mongolian Horse";
            case 4:
                return "Common Andalusian Horse";
            case 5:
                return "Uncommon Mongolian Horse";
            case 6:
                return "Uncommon Andalusian Horse";
            case 7:
                return "Uncommon Shire Horse";
            case 8:
                return "Rare Andalusian Horse";
            case 9:
                return "Rare Shire Horse";
            case 10:
                return "Rare Betta Fish";
            case 11:
                return "Rare Wolf";
            case 12:
                return "Epic Shire Horse";
            case 13:
                return "Epic Wolf";
            case 14:
                return "Epic Plague Bearer";
            case 15:
                return "Legend Alpha Predator";
            case 16:
                return "Mythic Snowman";
            case 17:
                return "Mythic Reaper";
            case 18:
                return "Ancient Kraken";
        }
        return null;
    }

    public String getPetObtainable(String name, String userPrefix) {
        switch (name) {
            case "junk bee":
                return "`" + userPrefix + "tutorial` [100%] | Requirement: Complete Tutorial\n";
            case "junk mongolian horse":
                return "`" + userPrefix + "vote` [10%] | Requirement: Level 1 - 19\n";
            case "common mongolian horse":
                return "`" + userPrefix + "vote` [5%] | Requirement: Level 1 - 19\n";
            case "common andalusian horse":
                return "`" + userPrefix + "vote` [10%] | Requirement: Level 20 - 50\n";
            case "uncommon mongolian horse":
                return  "`" + userPrefix + "vote` [3%] | Requirement: Level 1 - 19\n";
            case "uncommon andalusian horse":
                return "`" + userPrefix + "vote` [5%] | Requirement: Level 20 - 50\n";
            case "uncommon shire horse":
                return "`" + userPrefix + "vote` [10%] | Requirement: Level 50+\n";
            case "rare andalusian horse":
                return  "`" + userPrefix + "vote` [3%] | Requirement: Level 20 - 50\n";
            case "rare shire horse":
                return "`" + userPrefix + "vote` [5%] | Requirement: Level 50+\n";
            case "rare betta fish":
                return "Requirement: Being level 115+ during BETA\n";
            case "rare wolf":
                return "`" + userPrefix + "random | arena` [0.1%]" + " | Requirement: 250+ Kills\n";
            case "epic shire horse":
                return  "`" + userPrefix + "vote` [3%] | Requirement: Level 50+\n";
            case "epic wolf":
                return "`" + userPrefix + "random | arena` [0.02%]" + " | Requirement: 250+ Kills\n";
            case "epic plague bearer":
                return "Reporting bugs in the Support Server" + " | Requirement: None\n";
            case "legend alpha predator":
                return "Requirement: Being an ALPHA player\n";
            case "mythic snowman":
                return " `" + userPrefix + "holiday shop`" + " | Requirement: 1200 Candy Cane\n";
            case "mythic reaper":
                return " `" + userPrefix + "holiday shop`" + " | Requirement: 150 Purple Candy\n";
            case "ancient kraken":
                return "Requirement: Top 3 most helpful in testing Update 1.0 (Full Release)\n";
        }
        return null;
    }

    public Color petRarityColor(PetRarity rarity) {
        switch (rarity) {
            case JUNK:
                return new Color(108, 100, 100);
            case COMMON:
                return new Color(255, 255, 255);
            case UNCOMMON:
                return new Color(30, 221, 31);
            case RARE:
                return new Color(30, 0, 250);
            case EPIC:
                return new Color(212, 0, 255);
            case LEGEND:
                return new Color(255, 136, 0);
            case MYTHIC:
                return new Color(255, 0, 18);
            case ANCIENT:
                return new Color(21, 242, 250);
            case ARTIFACT:
                return new Color(255, 0, 176);
        }
        return null;
    }

    private static String capitalizeFully(String string) {
        String capitalized = "";
        for(String word : string.split(" ")) {
            capitalized += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        if(capitalized.endsWith(" ")) capitalized = capitalized.substring(0, capitalized.length()-1);
        return capitalized;
    }
}