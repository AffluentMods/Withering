package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.api.XPApi;
import me.affluent.decay.armor.*;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.*;
import me.affluent.decay.entity.otherInventory.ArmorDragonUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.entity.otherInventory.ArmorWitherUser;
import me.affluent.decay.enums.ArmorType;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.pets.PetExpUser;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemLockingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.settingsUtil.*;
import me.affluent.decay.util.system.*;
import me.affluent.decay.vote.VoteClient;
import me.affluent.decay.vote.VoteSystem;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonWeapon;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ProfileCommand extends BotCommand {

    public ProfileCommand() {
        this.name = "profile";
        this.aliases = new String[]{"pfl", "inventory", "inv"};
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        ItemType sortIT = null;
        Rarity sortR = null;
        ArmorType sortAT = null;
        int page = 1;
        String[] args = e.getArgs();
        boolean isMentioned = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String previousArg = i > 0 ? args[i - 1] : null;
                String arg = args[i];
                if (i == (args.length - 1)) {
                    try {
                        page = Integer.parseInt(arg);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (arg.length() == 18) {
                    try {
                        long targetId = Long.parseLong(arg);
                        uid = String.valueOf(targetId);
                        isMentioned = true;
                    } catch (NumberFormatException ignore) {

                    }
                }
                String farg = previousArg != null ? previousArg + " " + arg : arg;
                if (ItemType.isItemType(farg)) {
                    sortIT = ItemType.getItemType(farg);
                }
                if (Rarity.isRarity(farg)) {
                    sortR = Rarity.getRarity(farg);
                }
                if (ArmorType.isArmorType(farg)) {
                    sortAT = ArmorType.getArmorType(farg);
                }
            }
        }
        User mentioned = MentionUtil.getUser(e.getMessage());
        if (mentioned != null) {
            isMentioned = true;
            uid = mentioned.getId();
        }
        if (!Player.playerExists(uid) && isMentioned) {
            String msg1 = Language.getLocalized(uid, "target_not_found", "Preposterous! This particular person does not seem to exist");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Player p = Player.getPlayer(uid);
        MessageEmbed profile = getProfile(p);
        MessageEmbed inventory = getInventory(p, page, sortIT, sortR, sortAT);
        e.reply(profile);
        e.reply(inventory);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(Language.getLocalized(uid, "announcement", "Important Announcement"));
        eb.setColor(new Color(19, 255, 58));
        eb.setDescription(Language.getLocalized(uid, "announcement", "Effective **April 14th, 2022** Withering will be shutting down.\n\n" +
                "To view the full announcement please join the official server https://discord.gg/jFk8qNe9yJ.\n" +
                "Source code of many bot(s) along with lots of help/information will be provided in the server.\n\n" +
                "I apologize for the lack of updates and sudden shut down notification, but I no longer have the time. I truly love this community and really really wish I could " +
                "continue development on my bot(s), but I try to make time and it never works out. I hope someone with some programming knowledge takes over and continues what could have been. Love all of you!"
        ));
        e.getTextChannel().sendMessage(eb.build()).queue();
    }

    private MessageEmbed getInventory(Player p, int page, ItemType sortItemType, Rarity sortRarity,
                                      ArmorType sortArmorType) {
        String uid = p.getUserId();
        EmbedBuilder eb = new EmbedBuilder();
        InventoryUser inv = p.getInventoryUser();
        TreeMap<Long, String> items = new TreeMap<>(inv.getItems());
        List<Long> sortedItems = sortItemType != null ? inv.getItemsSorted(sortItemType) : (
                sortRarity != null ? inv.getItemsSorted(sortRarity) : (
                        sortArmorType != null ? inv.getItemsSorted(sortArmorType) : null));
        ArrayList<String> itemIDStrings = new ArrayList<>();
        if (sortedItems != null) {
            for (long itemID : sortedItems) {
                itemIDStrings.add(String.valueOf(itemID));
            }
        } else {
            for (long itemID : items.descendingKeySet())
                itemIDStrings.add(String.valueOf(itemID));
        }
        StringBuilder description = new StringBuilder();
        String response = ResponseUtil.getResponseUtil(uid).getResponse();
        if (response.equalsIgnoreCase("pc")) {
            description.append(EmoteUtil.getCoin()).append(" ").append(p.getEcoUser().getBalanceAbr());
        }
        if (response.equalsIgnoreCase("mobile")) {
            description.append(EmoteUtil.getCoin()).append(" ").append(p.getEcoUser().getBalanceCom());
        }

        if (response.equalsIgnoreCase("pc")) {
            description.append(Constants.TAB).append(Constants.TAB)
                    .append(EmoteUtil.getDiamond()).append(" ").append(FormatUtil.formatAbbreviated(DiamondsUtil.getDiamonds(uid)));
        }
        if (response.equalsIgnoreCase("mobile")) {
            description.append(Constants.TAB).append(Constants.TAB)
                    .append(EmoteUtil.getDiamond()).append(" ").append(FormatUtil.formatCommas(DiamondsUtil.getDiamonds(uid)));
        }

        if (p.getExpUser().isMaxed()) {
            if (response.equalsIgnoreCase("pc")) {
                description.append(Constants.TAB).append(Constants.TAB).append(EmoteUtil.getEmoteMention("elixir"))
                        .append(" ").append(FormatUtil.formatAbbreviated(ElixirUtil.getElixir(uid)));
            }
            if (response.equalsIgnoreCase("mobile")) {
                description.append(Constants.TAB).append(Constants.TAB).append(EmoteUtil.getEmoteMention("elixir"))
                        .append(" ").append(FormatUtil.formatCommas(ElixirUtil.getElixir(uid)));
            }
        }
        description.append("\n");
        if (page < 1) page = 1;
        int offset = page * 15 - 15;
        int end = offset + 15;
        if (end > items.size()) end = items.size();
        for (int i = offset; i < end; i++) {
            long itemID = Long.parseLong(itemIDStrings.get(i));
            String item = items.get(itemID);
            String lockedEmote = "";
            if (ItemLockingUtil.getItemLockValue(p.getUserId(), itemID) == 1) {
                lockedEmote = EmoteUtil.getEmoteMention("Locked");
            }
            Item itemObj = InventoryUser.getItemByID(itemID);
            int itemLevel = ItemLevelingUtil.getItemLevel(p.getUserId(), itemID);
            int itemStars = ItemStarringUtil.getItemStar(p.getUserId(), itemID);
            if (itemObj == null) description.append("[`").append(itemID).append("`] null\n");
            else {
                String display = itemObj.getRarity().name() + " " + itemObj.getType().name() + " " + itemObj.getGear().name();
                description.append(lockedEmote).append(" [`").append(itemID).append("`] ").append(EmoteUtil.getEmoteMention(display))
                        .append(" ").append(capitalizeFully(item)).append(" ").append(EmoteUtil.getEmoteMention("S_")).append(" ").append(itemStars)
                        .append(" Lvl ").append(itemLevel).append("\n");
            }
        }
        String prefix = PrefixUser.getPrefixUser(uid).getPrefix();
        long maxPage = getMaxPage(inv.getSize());
        eb.setTitle(
                Language.getLocalized(uid, "inventory", "Inventory") + " " + Language.getLocalized(uid, "of", "of") +
                " " + p.getUser().getAsTag() + " [" + page + "/" + maxPage + "]");
        eb.setDescription(description.toString());
        if (DailyRewardSystem.isDailyAvailable(uid))
            eb.setFooter("Your daily reward is available!\nUse " + prefix + "daily to redeem.",
                    "https://i.imgur.com/RbHmy82.png");
        else eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
        return eb.build();
    }

    private MessageEmbed getProfile(Player p) {
        String uid = p.getUserId();
        EmbedBuilder eb = new EmbedBuilder();
        int level = p.getExpUser().getLevel();
        int elixirLevel = p.getExpUser().getElixirLevel();
        boolean xpMaxed = p.getExpUser().isMaxed();
        boolean elixirXPMaxed = p.getExpUser().isElixirMaxed();
        eb.setTitle(
                Language.getLocalized(uid, "profile", "Profile") + " " + Language.getLocalized(uid, "of", "of") + " " +
                p.getUser().getAsTag());
        StringBuilder badgesString = new StringBuilder();
        for (String badge : p.getBadgeUser().getBadgeList())
            badgesString.append(EmoteUtil.getEmoteMention(badge + "_badge")).append(" ");
        if (badgesString.toString().length() > 0) {
            badgesString.append("\n");
            eb.appendDescription(badgesString.toString());
        }
        String bar = BarUtil.getBarUtil(uid).getBarSetting();
        String response = ResponseUtil.getResponseUtil(uid).getResponse();
        if (bar.equalsIgnoreCase("enabled")) {
            if (response.equalsIgnoreCase("pc")) {
                if (xpMaxed) {
                    eb.appendDescription(HPBarUtil.getBar(p.getHealthUser().getHealth(), p.getHealthUser().getMaxHealth(), Integer.parseInt(p.getExpUser().getElixirExperience().toString()), XPApi.getNeededElixirXP(p.getExpUser().getElixirLevel()), true));
                } else {
                    eb.appendDescription(HPBarUtil.getBar(p.getHealthUser().getHealth(), p.getHealthUser().getMaxHealth(), Integer.parseInt(p.getExpUser().getExperience().toString()), XPApi.getNeededXP(p.getExpUser().getLevel()), false));
                }
            }
            if (response.equalsIgnoreCase("mobile")) {
                eb.appendDescription(HPBarMUtil.getMobileBar(p.getHealthUser().getHealth(), p.getHealthUser().getMaxHealth(), Integer.parseInt(p.getExpUser().getExperience().toString()), XPApi.getNeededXP(p.getExpUser().getLevel())));
            }
            eb.appendDescription("\n");
        }
        //eb.setDescription("**Clan:** ");
        PetItem petItem = p.getPetUser().getPet();
        String petID = p.getPetUser().getPetID();
        String petDisplay = EmoteUtil.getEmoteMention("Null_Pet") + " None";
        int petLevel = 0;
        int petStars = 0;
        if (petItem != null) {
            petStars = PetStarringUtil.getPetStar(p.getUserId(), Long.parseLong(petID));
            petLevel = PetExpUser.getPetExpUser(uid).getPetLevel(uid, Integer.parseInt(petID));
            petDisplay = petItem.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") + " " + petStars + " Lvl " + petLevel;
        }

        Rank rank = p.getRank();
        String rankName = rank == null ? "-" : p.getRankDisplay();
        int power = Power.getFullPower(p);
        eb.appendDescription("**Power:** " + power);
        eb.appendDescription("\n**Rank:** " + rankName);
        if (p.getHealthUser().getHealth() > p.getHealthUser().getMaxHealth()) p.getHealthUser().setHealth(p.getHealthUser().getMaxHealth());
        if (bar.equalsIgnoreCase("disabled")) {
            eb.appendDescription(
                    "\n**Health:** " + p.getHealthUser().getHealth() + "/" + p.getHealthUser().getMaxHealth() + " " + EmoteUtil.getEmoteMention("HP"));
        }

        if (xpMaxed) eb.appendDescription("\n**Level:** " + level + " | **Elixir Level:** " + elixirLevel);
        else eb.appendDescription("\n**Level:** " + level);
        if (bar.equalsIgnoreCase("disabled")) {
            if (xpMaxed) {
                if (response.equalsIgnoreCase("mobile")) {
                    if (!elixirXPMaxed) {
                        String maxELXP = FormatUtil.formatCommas((long) XPApi.getNeededElixirXP(elixirLevel));
                        eb.appendDescription("\n**Elixir-EXP:** " +
                                FormatUtil.formatCommas(p.getExpUser().getElixirExperience().toString()) + "/" +
                                maxELXP + " " + EmoteUtil.getEmoteMention("Elixir_XP_Orb"));
                    }
                }
                if (response.equalsIgnoreCase("pc")) {
                    String maxELXP = FormatUtil.formatAbbreviated((long) XPApi.getNeededElixirXP(elixirLevel));
                    eb.appendDescription("\n**Elixir-EXP:** " +
                            FormatUtil.formatAbbreviated(p.getExpUser().getElixirExperience().toString()) + "/" +
                            maxELXP + " " + EmoteUtil.getEmoteMention("Elixir_XP_Orb"));
                }
            } else {
                if (response.equalsIgnoreCase("mobile")) {
                    String maxXP = FormatUtil.formatCommas((long) XPApi.getNeededXP(level));
                    eb.appendDescription(
                            "\n**Experience:** " + FormatUtil.formatCommas(p.getExpUser().getExperience().toString()) +
                                    "/" + maxXP + " " + EmoteUtil.getEmoteMention("XP_Orb"));
                }
                if (response.equalsIgnoreCase("pc")) {
                    String maxXP = FormatUtil.formatAbbreviated((long) XPApi.getNeededXP(level));
                    eb.appendDescription(
                            "\n**Experience:** " + FormatUtil.formatAbbreviated(p.getExpUser().getExperience().toString()) +
                                    "/" + maxXP + " " + EmoteUtil.getEmoteMention("XP_Orb"));
                }
            }
        }
        Helmet helmet = p.getArmorUser().getHelmet();
        String helmetID = p.getArmorUser().getHelmetID();
        int helmetLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(helmetID));
        int helmetStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(helmetID));
        String helmetDisplay = EmoteUtil.getEmoteMention("Null_Helmet") + " None";
        if (helmet != null) helmetDisplay = helmet.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") +
                " " + helmetStars + " Lvl " + helmetLevel;
        
        Chestplate chestplate = p.getArmorUser().getChestplate();
        String chestplateID = p.getArmorUser().getChestplateID();
        int chestplateLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(chestplateID));
        int chestplateStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(chestplateID));
        String chestplateDisplay = EmoteUtil.getEmoteMention("Null_Chestplate") + " None";
        if (chestplate != null) chestplateDisplay = chestplate.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") +
                " " + chestplateStars + " Lvl " + chestplateLevel;
        
        Gloves gloves = p.getArmorUser().getGloves();
        String glovesID = p.getArmorUser().getGlovesID();
        int glovesLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(glovesID));
        int glovesStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(glovesID));
        String glovesDisplay = EmoteUtil.getEmoteMention("Null_Gloves") + " None";
        if (gloves != null) glovesDisplay = gloves.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") +
                " " + glovesStars + " Lvl " + glovesLevel;
        
        Trousers trousers = p.getArmorUser().getTrousers();
        String trousersID = p.getArmorUser().getTrousersID();
        int trousersLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(trousersID));
        int trousersStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(trousersID));
        String trousersDisplay = EmoteUtil.getEmoteMention("Null_Trousers") + " None";
        if (trousers != null) trousersDisplay = trousers.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") +
                " " + trousersStars + " Lvl " + trousersLevel;
        
        Boots boots = p.getArmorUser().getBoots();
        String bootsID = p.getArmorUser().getBootsID();
        int bootsLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(bootsID));
        int bootsStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(bootsID));
        String bootsDisplay = EmoteUtil.getEmoteMention("Null_Boots") + " None";
        if (boots != null) bootsDisplay = boots.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") +
                " " + bootsStars + " Lvl " + bootsLevel;
        
        
        eb.appendDescription("\n" + helmetDisplay);
        eb.appendDescription("\n" + chestplateDisplay);
        eb.appendDescription("\n" + glovesDisplay);
        eb.appendDescription("\n" + trousersDisplay);
        eb.appendDescription("\n" + bootsDisplay);
        /*
        Artifact activeArtifact = ArtifactUtil.getActiveArtifact(uid);
        String artifactDisplay = activeArtifact != null ? activeArtifact.getName() : "None";
        eb.appendDescription("\n**Artifact:** " + artifactDisplay);
        */
        Weapon weapon = p.getArmorUser().getWeapon();
        String weaponID = p.getArmorUser().getWeaponID();

        IronWeapon ironWeapon = p.getArmorIronUser().getIronWeapon();
        DragonWeapon dragonWeapon = p.getArmorDragonUser().getDragonWeapon();
        WitherWeapon witherWeapon = p.getArmorWitherUser().getWitherWeapon();

        Arrow arrow = p.getArmorUser().getArrow();
        Shield shield = p.getArmorUser().getShield();
        String arrowID = p.getArmorUser().getArrowID();
        String shieldID = p.getArmorUser().getShieldID();

        IronShield ironShield = p.getArmorIronUser().getIronShield();
        DragonShield dragonShield = p.getArmorDragonUser().getDragonShield();
        WitherShield witherShield = p.getArmorWitherUser().getWitherShield();

        IronHelmet ironHelmet = p.getArmorIronUser().getIronHelmet();
        IronChestplate ironChestplate = p.getArmorIronUser().getIronChestplate();
        IronGloves ironGloves = p.getArmorIronUser().getIronGloves();
        IronTrousers ironTrousers = p.getArmorIronUser().getIronTrousers();
        IronBoots ironBoots = p.getArmorIronUser().getIronBoots();

        DragonHelmet dragonHelmet = p.getArmorDragonUser().getDragonHelmet();
        DragonChestplate dragonChestplate = p.getArmorDragonUser().getDragonChestplate();
        DragonGloves dragonGloves = p.getArmorDragonUser().getDragonGloves();
        DragonTrousers dragonTrousers = p.getArmorDragonUser().getDragonTrousers();
        DragonBoots dragonBoots = p.getArmorDragonUser().getDragonBoots();

        WitherHelmet witherHelmet = p.getArmorWitherUser().getWitherHelmet();
        WitherChestplate witherChestplate = p.getArmorWitherUser().getWitherChestplate();
        WitherGloves witherGloves = p.getArmorWitherUser().getWitherGloves();
        WitherTrousers witherTrousers = p.getArmorWitherUser().getWitherTrousers();
        WitherBoots witherBoots = p.getArmorWitherUser().getWitherBoots();

        int weaponLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(weaponID));
        int weaponStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(weaponID));
        int arrowLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(arrowID));
        int arrowStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(arrowID));
        int shieldLevel = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(shieldID));
        int shieldStars = ItemStarringUtil.getItemStar(uid, Long.parseLong(shieldID));
        String weaponDisplay = EmoteUtil.getEmoteMention("Null_Weapon") + "None";
        if (weapon != null) weaponDisplay = weapon.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") + " " + weaponStars + " Lvl " + weaponLevel;
        String arrowDisplay = null;
        String shieldDisplay = null;
        if (arrow != null) arrowDisplay = arrow.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") + " " + arrowStars + " Lvl " + arrowLevel;
        if (shield != null) shieldDisplay = shield.getDisplay() + " " + EmoteUtil.getEmoteMention("S_") + " " + shieldStars + " Lvl " + shieldLevel;
        eb.appendDescription("\n" + weaponDisplay);
        if (shieldDisplay != null) eb.appendDescription("\n" + shieldDisplay);
        if (arrowDisplay != null) eb.appendDescription("\n" + arrowDisplay);
        eb.appendDescription("\n\n" + petDisplay);
        
        ArmorUser au = p.getArmorUser();
        ArmorIronUser aiu = p.getArmorIronUser();
        ArmorDragonUser adu = p.getArmorDragonUser();
        ArmorWitherUser awu = p.getArmorWitherUser();
        int wd1 = 0;
        int wd2 = 0;
        int wad = 0;
        int wap = 0;
        if (weapon != null) {
            wd1 = weapon.getDamageFrom();
            wd2 = weapon.getDamageTo();
            wad = weapon.getRarityDamage();
            wap = weapon.getRarityProtection();
        }
        boolean ironHelmetEquipped = false;
        boolean ironChestplateEquipped = false;
        boolean ironGlovesEquipped = false;
        boolean ironTrousersEquipped = false;
        boolean ironBootsEquipped = false;
        boolean ironSwordEquipped = false;
        boolean ironShieldEquipped = false;
        boolean dragonHelmetEquipped = false;
        boolean dragonChestplateEquipped = false;
        boolean dragonGlovesEquipped = false;
        boolean dragonTrousersEquipped = false;
        boolean dragonBootsEquipped = false;
        boolean dragonSwordEquipped = false;
        boolean dragonShieldEquipped = false;
        boolean witherHelmetEquipped = false;
        boolean witherChestplateEquipped = false;
        boolean witherGlovesEquipped = false;
        boolean witherTrousersEquipped = false;
        boolean witherBootsEquipped = false;
        boolean witherSwordEquipped = false;
        boolean witherShieldEquipped = false;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronHelmet().equalsIgnoreCase("equipped")) ironHelmetEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronChestplate().equalsIgnoreCase("equipped")) ironChestplateEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronGloves().equalsIgnoreCase("equipped")) ironGlovesEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronTrousers().equalsIgnoreCase("equipped")) ironTrousersEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronBoots().equalsIgnoreCase("equipped")) ironBootsEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronSword().equalsIgnoreCase("equipped")) ironSwordEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(uid).getIronShield().equalsIgnoreCase("equipped")) ironShieldEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonHelmet().equalsIgnoreCase("equipped")) dragonHelmetEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonChestplate().equalsIgnoreCase("equipped")) dragonChestplateEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonGloves().equalsIgnoreCase("equipped")) dragonGlovesEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonTrousers().equalsIgnoreCase("equipped")) dragonTrousersEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonBoots().equalsIgnoreCase("equipped")) dragonBootsEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonSword().equalsIgnoreCase("equipped")) dragonSwordEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonShield().equalsIgnoreCase("equipped")) dragonShieldEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherHelmet().equalsIgnoreCase("equipped")) witherHelmetEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherChestplate().equalsIgnoreCase("equipped")) witherChestplateEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherGloves().equalsIgnoreCase("equipped")) witherGlovesEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherTrousers().equalsIgnoreCase("equipped")) witherTrousersEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherBoots().equalsIgnoreCase("equipped")) witherBootsEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherSword().equalsIgnoreCase("equipped")) witherSwordEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherShield().equalsIgnoreCase("equipped")) witherShieldEquipped = true;
        int wd1iron = 0;
        int wd2iron = 0;
        int wadiron = 0;
        int wapiron = 0;
        if (ironWeapon != null && ironSwordEquipped) {
            wd1iron = ironWeapon.getDamageFrom();
            wd2iron = ironWeapon.getDamageTo();
            wadiron = ironWeapon.getRarityDamage();
            wapiron = ironWeapon.getRarityProtection();
        }
        int wd1dragon = 0;
        int wd2dragon = 0;
        int waddragon = 0;
        int wapdragon = 0;
        if (dragonWeapon != null && dragonSwordEquipped) {
            wd1dragon = dragonWeapon.getDamageFrom();
            wd2dragon = dragonWeapon.getDamageTo();
            waddragon = dragonWeapon.getRarityDamage();
            wapdragon = dragonWeapon.getRarityProtection();
        }
        int wd1wither = 0;
        int wd2wither = 0;
        int wadwither = 0;
        int wapwither = 0;
        if (witherWeapon != null && witherSwordEquipped) {
            wd1wither = witherWeapon.getDamageFrom();
            wd2wither = witherWeapon.getDamageTo();
            wadwither = witherWeapon.getRarityDamage();
            wapwither = witherWeapon.getRarityProtection();
        }
        
        int ad = 0;
        if (arrow != null) {
            ad = arrow.getDamage();
            wad += arrow.getRarityDamage();
        }
        int sp = 0;
        if (shield != null) {
            sp = shield.getProtection();
            wap = shield.getRarityProtection();
        }
        int spiron = 0;
        if (ironShield != null && ironShieldEquipped) {
            spiron = ironShield.getProtection();
            wapiron = ironShield.getRarityProtection();
        }
        int spdragon = 0;
        if (dragonShield != null && dragonShieldEquipped) {
            spdragon = dragonShield.getProtection();
            wapdragon = dragonShield.getRarityProtection();
        }
        int spwither = 0;
        if (witherShield != null && witherShieldEquipped) {
            spwither = witherShield.getProtection();
            wapwither = witherShield.getRarityProtection();
        }

        List<Armor> armors = au.getAllArmor();
        int ap1 = 0;
        int ap2 = 0;
        //
        int aad1 = 0; // Attribute Dodge
        int aad2 = 0;
        int aap1 = 0; // Attribute Protection
        int aap2 = 0;
        int aagc1 = 0; // Attribute Gold Coins
        int aagc2 = 0;
        int aaxp1 = 0; // Attribute Exp Gain
        int aaxp2 = 0;
        for (Armor armor : armors) {
            ap1 += armor.getProtectionFrom();
            ap2 += armor.getProtectionTo();
            RarityClass rc = Rarities.getRarityClass(armor.getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    int dodge1 = attribute.getDodgeChance().getValue1();
                    int dodge2 = attribute.getDodgeChance().getValue2();
                    int p1 = attribute.getProtection().getValue1();
                    int p2 = attribute.getProtection().getValue2();
                    int gc1 = attribute.getGoldCoins().getValue1();
                    int gc2 = attribute.getGoldCoins().getValue2();
                    int xp1 = attribute.getExp().getValue1();
                    int xp2 = attribute.getExp().getValue2();
                    if (dodge1 > 0) aad1 += dodge1;
                    if (dodge2 > 0) aad2 += dodge2;
                    if (p1 > 0) aap1 += p1;
                    if (p2 > 0) aap2 += p2;
                    if (gc1 > 0) aagc1 += gc1;
                    if (gc2 > 0) aagc2 += gc2;
                    if (xp1 > 0) aaxp1 += xp1;
                    if (xp2 > 0) aaxp2 += xp2;
                }
            }
        }

        int totalDmg = FormatUtil.getBetween(wd1, wd2) + wad + ad;
        int totalProtection = FormatUtil.getBetween(ap1 + aap1, ap2 + aap2) + wap + sp +
                wapiron + spiron + wapdragon + spdragon + wapwither + spwither;
        int totalDodge = FormatUtil.getBetween(aad1, aad2);
        int totalXP = FormatUtil.getBetween(aaxp1, aaxp2) + 100;
        int totalGold = FormatUtil.getBetween(aagc1, aagc2) + 100;
        if (FireworkSystem.hasFirework(uid)) {
            totalGold += 100;
            totalXP += 100;
        }
        if (VoteSystem.hasVoted(uid)) {
            totalXP += VoteClient.expBoost;
        }
        totalDmg += FormatUtil.getBetween(wd1iron, wd2iron) + wadiron;
        totalDmg += FormatUtil.getBetween(wd1dragon, wd2dragon) + waddragon;
        totalDmg += FormatUtil.getBetween(wd1wither, wd2wither) + wadwither;
        
        if (ironHelmetEquipped && ironHelmet != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(aiu.getIronHelmet().getProtectionFrom() + p1iron, aiu.getIronHelmet().getProtectionTo() + p2iron);
        }

        if (ironChestplateEquipped && ironChestplate != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(aiu.getIronChestplate().getProtectionFrom() + p1iron, aiu.getIronChestplate().getProtectionTo() + p2iron);
        }

        if (ironGlovesEquipped && ironGloves != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(aiu.getIronGloves().getProtectionFrom() + p1iron, aiu.getIronGloves().getProtectionTo() + p2iron);
        }

        if (ironTrousersEquipped && ironTrousers != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(aiu.getIronTrousers().getProtectionFrom() + p1iron, aiu.getIronTrousers().getProtectionTo() + p2iron);
        }

        if (ironBootsEquipped && ironBoots != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(aiu.getIronBoots().getProtectionFrom() + p1iron, aiu.getIronBoots().getProtectionTo() + p2iron);
        }

        if (dragonHelmetEquipped && dragonHelmet != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(adu.getDragonHelmet().getProtectionFrom() + p1dragon, adu.getDragonHelmet().getProtectionTo() + p2dragon);
        }

        if (dragonChestplateEquipped && dragonChestplate != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(adu.getDragonChestplate().getProtectionFrom() + p1dragon, adu.getDragonChestplate().getProtectionTo() + p2dragon);
        }

        if (dragonGlovesEquipped && dragonGloves != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(adu.getDragonGloves().getProtectionFrom() + p1dragon, adu.getDragonGloves().getProtectionTo() + p2dragon);
        }

        if (dragonTrousersEquipped && dragonTrousers != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(adu.getDragonTrousers().getProtectionFrom() + p1dragon, adu.getDragonTrousers().getProtectionTo() + p2dragon);
        }

        if (dragonBootsEquipped && dragonBoots != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(adu.getDragonBoots().getProtectionFrom() + p1dragon, adu.getDragonBoots().getProtectionTo() + p2dragon);
        }

        if (witherHelmetEquipped && witherHelmet != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(awu.getWitherHelmet().getProtectionFrom() + p1wither, awu.getWitherHelmet().getProtectionTo() + p2wither);
        }

        if (witherChestplateEquipped && witherChestplate != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(awu.getWitherChestplate().getProtectionFrom() + p1wither, awu.getWitherChestplate().getProtectionTo() + p2wither);
        }

        if (witherGlovesEquipped && witherGloves != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(awu.getWitherGloves().getProtectionFrom() + p1wither, awu.getWitherGloves().getProtectionTo() + p2wither);
        }

        if (witherTrousersEquipped && witherTrousers != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(awu.getWitherTrousers().getProtectionFrom() + p1wither, awu.getWitherTrousers().getProtectionTo() + p2wither);
        }

        if (witherBootsEquipped && witherBoots != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(awu.getWitherBoots().getProtectionFrom() + p1wither, awu.getWitherBoots().getProtectionTo() + p2wither);
        }
        
        if (petItem != null) {
            int petHealth = 0;
            petHealth += (petItem.getLevelHP() * petLevel);
            petHealth += petItem.getStarterHP();
            int petDamage = 0;
            petDamage += (petItem.getLevelDMG() * petLevel);
            petDamage += petItem.getStarterDMG();
            if (petStars > 0) {
                petHealth *= ((double) (petStars * 5) / 100) + 1;
                petDamage *= ((double) (petStars * 5) / 100) + 1;
            }
            if (petLevel == petItem.getMaxLevel()) {
                petDamage *= 1.15;
                petHealth *= 1.15;
            }
            totalDmg += petDamage;
            totalProtection += petHealth;
        }

        eb.appendDescription(
                "\n\n" + totalProtection + " " + EmoteUtil.getEmoteMention("Protection") +
                        Constants.TAB + Constants.TAB + Constants.TAB + Constants.TAB + totalDmg + " " + EmoteUtil.getEmoteMention("Damage") +
                        Constants.TAB + Constants.TAB + Constants.TAB + Constants.TAB + p.getHealthUser().getMaxHealth() +
                        " " + EmoteUtil.getEmoteMention("HP") +

                        "\n\n" + "Dodge: " + totalDodge + "%" +
                        Constants.TAB + Constants.TAB + "Gold: " + totalGold + "%" +
                        Constants.TAB + Constants.TAB + "Exp: " + totalXP + "%");
        return eb.build();
    }

    private int getMaxPage(int size) {
        double s = size / 15.0;
        int maxPage = new Double("" + s).intValue();
        if (size % 15 != 0) maxPage++;
        if (maxPage < 1) maxPage = 1;
        return maxPage;
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