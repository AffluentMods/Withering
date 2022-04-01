package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.language.Language;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.IronScrollsUtil;
import me.affluent.decay.util.MentionUtil;
import me.affluent.decay.util.IronScrollsUtil;
import me.affluent.decay.util.settingsUtil.EquippedIronUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class ProfileIronCommand extends BotCommand {

    public ProfileIronCommand() {
        this.name = "profileiron";
        this.aliases = new String[]{"pfliron", "inventoryiron", "inviron", "invi"};
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
        String[] args = e.getArgs();
        boolean isMentioned = false;
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.length() == 18) {
                    try {
                        long targetId = Long.parseLong(arg);
                        uid = String.valueOf(targetId);
                        isMentioned = true;
                    } catch (NumberFormatException ignore) {

                    }
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
        if (p.getExpUser().getLevel() < 35) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "low_level",
                    "Majesty, you seem to be inexperienced. \nPerhaps reach level `35` first.")));
            return;
        }
        if (args.length < 1) {
            MessageEmbed profile = getProfile(p);
            e.reply(profile);
            return;
        }
        String type = args[0].toLowerCase();
        ArmorIronUser armorUser = p.getArmorIronUser();
        int purchaseCost = 0;
        Armor armor = null;
        IronWeapon weapon = null;
        IronShield shield = null;
        String armorType = "";
        if (type.equalsIgnoreCase("helmet")) {
            armor = armorUser.getIronHelmet();
            armorType = "Helmet";
            purchaseCost = 5;
        }
        if (type.equalsIgnoreCase("chestplate")) {
            armor = armorUser.getIronChestplate();
            armorType = "Chestplate";
            purchaseCost = 10;
        }
        if (type.equalsIgnoreCase("gloves")) {
            armor = armorUser.getIronGloves();
            armorType = "Gloves";
            purchaseCost = 15;
        }
        if (type.equalsIgnoreCase("trousers")) {
            armor = armorUser.getIronTrousers();
            armorType = "Trousers";
            purchaseCost = 25;
        }
        if (type.equalsIgnoreCase("boots")) {
            armor = armorUser.getIronBoots();
            armorType = "Boots";
            purchaseCost = 30;
        }
        int scrolls = IronScrollsUtil.getIronScrolls(uid);
        if (scrolls <= 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
            return;
        }
        if (scrolls < purchaseCost) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need `" + purchaseCost +"` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
            return;
        }

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
        boolean upgraded = false;
        boolean purchased = false;
        if (type.equalsIgnoreCase("sword") || type.equalsIgnoreCase("weapon")) {
            weapon = armorUser.getIronWeapon();
            if (weapon != null) {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + (int) (20 * getRarityMultiplier(weapon.getRarity())) + "` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                if (weapon.getRarity() == Rarity.ARTIFACT) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                            Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                    .replace("{item}", weapon.getDisplay())));
                    return;
                }
                if (scrolls < (int) (20 * getRarityMultiplier(weapon.getRarity()))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + (int) (20 * getRarityMultiplier(weapon.getRarity())) +"` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                Rarity better = weapon.getRarity().betterOther();
                String newWeapon = better + " iron sword";
                IronWeapon ironSword = IronWeapon.getWeapon(newWeapon);
                if (ironSword != null) armorUser.updateIronWeapon(ironSword);
                weapon = armorUser.getIronWeapon();
                IronScrollsUtil.setIronScrolls(uid, scrolls - (int) (20 * getRarityMultiplier(weapon.getRarity())));
                scrolls = IronScrollsUtil.getIronScrolls(uid);
                upgraded = true;
            } else {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + 20 + "` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                if (scrolls < 20) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + 20 +"` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                IronScrollsUtil.setIronScrolls(uid, scrolls - 20);
                scrolls = IronScrollsUtil.getIronScrolls(uid);
                String brandNewWeapon = "junk iron sword";
                IronWeapon ironWeapon = IronWeapon.getWeapon(brandNewWeapon);
                if (ironWeapon != null) armorUser.updateIronWeapon(ironWeapon);
                weapon = armorUser.getIronWeapon();
                purchased = true;
            }
        }

        if (type.equalsIgnoreCase("shield")) {
            shield = armorUser.getIronShield();
            if (shield != null) {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + (int) (35 * getRarityMultiplier(shield.getRarity())) + "` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                if (shield.getRarity() == Rarity.ARTIFACT) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                            Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                    .replace("{item}", shield.getDisplay())));
                    return;
                }
                if (scrolls < (int) (35 * getRarityMultiplier(shield.getRarity()))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + (int) (35 * getRarityMultiplier(shield.getRarity())) +"` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                Rarity better = shield.getRarity().betterOther();
                String newShield = better + " iron shield";
                IronShield ironShield = IronShield.getIronShield(newShield);
                armorUser.updateIronShield(ironShield);
                shield = armorUser.getIronShield();
                IronScrollsUtil.setIronScrolls(uid, scrolls - (int) (35 * getRarityMultiplier(shield.getRarity())));
                scrolls = IronScrollsUtil.getIronScrolls(uid);
                upgraded = true;
            } else {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + 35 + "` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                if (scrolls < 35) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + 35 +"` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                    return;
                }
                IronScrollsUtil.setIronScrolls(uid, scrolls - 35);
                scrolls = IronScrollsUtil.getIronScrolls(uid);
                String brandNewShield = "junk iron shield";
                IronShield ironShield = IronShield.getIronShield(brandNewShield);
                armorUser.updateIronShield(ironShield);
                shield = armorUser.getIronShield();
                purchased = true;
            }
        }

        if (armor != null) {
            if (cost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                        "You need `" + (int) (purchaseCost * getRarityMultiplier(armor.getRarity())) + "` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scrolls") + " to use this.")));
                return;
            }
            if (armor.getRarity() == Rarity.ARTIFACT) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                        Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                .replace("{item}", armor.getDisplay())));
                return;
            }
            IronScrollsUtil.setIronScrolls(uid, scrolls - (int) (purchaseCost * getRarityMultiplier(armor.getRarity())));
            Rarity better = armor.getRarity().betterOther();
            String newArmor = better + " iron " + armorType;
            if (armorType.equalsIgnoreCase("helmet")) {
                IronHelmet ironHelmet = IronHelmet.getIronHelmet(newArmor);
                armorUser.updateIronHelmet(ironHelmet);
                armor = armorUser.getIronHelmet();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("chestplate")) {
                IronChestplate ironChestplate = IronChestplate.getIronChestplate(newArmor);
                armorUser.updateIronChestplate(ironChestplate);
                armor = armorUser.getIronChestplate();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("trousers")) {
                IronTrousers ironTrousers = IronTrousers.getIronTrousers(newArmor);
                armorUser.updateIronTrousers(ironTrousers);
                armor = armorUser.getIronTrousers();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("boots")) {
                IronBoots ironBoots = IronBoots.getIronBoots(newArmor);
                armorUser.updateIronBoots(ironBoots);
                armor = armorUser.getIronBoots();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("gloves")) {
                IronGloves ironGloves = IronGloves.getIronGloves(newArmor);
                armorUser.updateIronGloves(ironGloves);
                armor = armorUser.getIronGloves();
                armorType = "armor";
                upgraded = true;
            }
        } else {
            if (cost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                        "You need `" + purchaseCost + "` Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + " to use this.")));
                return;
            }
            IronScrollsUtil.setIronScrolls(uid, scrolls - purchaseCost);
            String brandNewArmor = "Junk Iron " + armorType;
            if (armorType.equalsIgnoreCase("helmet")) {
                IronHelmet ironHelmet = IronHelmet.getIronHelmet(brandNewArmor);
                armorUser.updateIronHelmet(ironHelmet);
                armor = armorUser.getIronHelmet();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("chestplate")) {
                IronChestplate ironChestplate = IronChestplate.getIronChestplate(brandNewArmor);
                armorUser.updateIronChestplate(ironChestplate);
                armor = armorUser.getIronChestplate();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("trousers")) {
                IronTrousers ironTrousers = IronTrousers.getIronTrousers(brandNewArmor);
                armorUser.updateIronTrousers(ironTrousers);
                armor = armorUser.getIronTrousers();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("boots")) {
                IronBoots ironBoots = IronBoots.getIronBoots(brandNewArmor);
                armorUser.updateIronBoots(ironBoots);
                armor = armorUser.getIronBoots();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("gloves")) {
                IronGloves ironGloves = IronGloves.getIronGloves(brandNewArmor);
                armorUser.updateIronGloves(ironGloves);
                armor = armorUser.getIronGloves();
                armorType = "armor";
                purchased = true;
            }
        }

        if (upgraded) {
            if (type.equalsIgnoreCase("sword")) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "upgraded_plain", "Upgraded"),
                        Language.getLocalized(uid, "upgraded_sword",
                                        "Succesfully upgraded to {upgraded_sword}!")
                                .replace("{upgraded_sword}", weapon.getDisplay())));
            }
            if (type.equalsIgnoreCase("shield")) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "upgraded_plain", "Upgraded"),
                        Language.getLocalized(uid, "upgraded_shield",
                                        "Succesfully upgraded to {upgraded_shield}!")
                                .replace("{upgraded_shield}", shield.getDisplay())));
            }
            if (armorType.equalsIgnoreCase("armor")) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "upgraded_plain", "Upgraded"),
                        Language.getLocalized(uid, "upgraded_armor", "Successfully upgraded to {upgraded_armor}!")
                                .replace("{upgraded_armor}", armor.getDisplay())));
            }
            return;
        }
        if (purchased) {
            if (type.equalsIgnoreCase("sword")) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "purchased_plain", "Purchased"),
                        Language.getLocalized(uid, "purchased_sword",
                                        "Succesfully purchased {purchased_sword}!")
                                .replace("{purchased_sword}", weapon.getDisplay())));
            }
            if (type.equalsIgnoreCase("shield")) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "purchased_plain", "Purchased"),
                        Language.getLocalized(uid, "purchased_shield",
                                        "Succesfully purchased {purchased_shield}!")
                                .replace("{purchased_shield}", shield.getDisplay())));
            }
            if (armorType.equalsIgnoreCase("armor")) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "purchased_plain", "Purchased"),
                        Language.getLocalized(uid, "purchased_armor", "Successfully purchased {purchased_armor}!")
                                .replace("{purchased_armor}", armor.getDisplay())));
            }
            return;
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    private MessageEmbed getProfile(Player p) {
        String uid = p.getUserId();
        EmbedBuilder eb = new EmbedBuilder();

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
        
        boolean ironUnequipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronBoots().equalsIgnoreCase("unequipped") &&
                EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronHelmet().equalsIgnoreCase("unequipped") &&
                EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronGloves().equalsIgnoreCase("unequipped") &&
                EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronTrousers().equalsIgnoreCase("unequipped") &&
                EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronChestplate().equalsIgnoreCase("unequipped") &&
                EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronShield().equalsIgnoreCase("unequipped") &&
                EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronSword().equalsIgnoreCase("unequipped");
        int power = Power.getIronPower(p);

        boolean helmetEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronHelmet().equalsIgnoreCase("equipped");
        String helmetEquip = "";
        if (!helmetEquipped) {
            helmetEquip = " - **Unequipped**";
        }
        IronHelmet helmet = p.getArmorIronUser().getIronHelmet();
        String helmetDisplay = EmoteUtil.getEmoteMention("Null_Helmet") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `5` to unlock";
        if (helmet != null) {
            helmetDisplay = helmet.getDisplay() + helmetEquip;
            if (!helmetEquipped) power -= Power.getPower(helmet.getRarity()) + Power.getPower(helmet.getItemType());
        }

        boolean chestplateEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronChestplate().equalsIgnoreCase("equipped");
        String chestplateEquip = "";
        if (!chestplateEquipped) {
            chestplateEquip = " - **Unequipped**";
        }
        IronChestplate chestplate = p.getArmorIronUser().getIronChestplate();
        String chestplateDisplay = EmoteUtil.getEmoteMention("Null_Chestplate") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `10` to unlock";
        if (chestplate != null) {
            chestplateDisplay = chestplate.getDisplay() + chestplateEquip;
            if (!chestplateEquipped) power -= Power.getPower(chestplate.getRarity()) + Power.getPower(chestplate.getItemType());
        }

        boolean glovesEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronGloves().equalsIgnoreCase("equipped");
        String glovesEquip = "";
        if (!glovesEquipped) {
            glovesEquip = " - **Unequipped**";
        }
        IronGloves gloves = p.getArmorIronUser().getIronGloves();
        String glovesDisplay = EmoteUtil.getEmoteMention("Null_Gloves") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `15` to unlock";
        if (gloves != null) {
            glovesDisplay = gloves.getDisplay() + glovesEquip;
            if (!glovesEquipped) power -= Power.getPower(gloves.getRarity()) + Power.getPower(gloves.getItemType());
        }

        boolean trousersEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronTrousers().equalsIgnoreCase("equipped");
        String trousersEquip = "";
        if (!trousersEquipped) {
            trousersEquip = " - **Unequipped**";
        }
        IronTrousers trousers = p.getArmorIronUser().getIronTrousers();
        String trousersDisplay = EmoteUtil.getEmoteMention("Null_Trousers") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `25` to unlock";
        if (trousers != null) {
            trousersDisplay = trousers.getDisplay() + trousersEquip;
            if (!trousersEquipped) power -= Power.getPower(trousers.getRarity()) + Power.getPower(trousers.getItemType());
        }

        boolean bootsEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronBoots().equalsIgnoreCase("equipped");
        String bootsEquip = "";
        if (!bootsEquipped) {
            bootsEquip = " - **Unequipped**";
        }
        IronBoots boots = p.getArmorIronUser().getIronBoots();
        String bootsDisplay = EmoteUtil.getEmoteMention("Null_Boots") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `30` to unlock";
        if (boots != null) {
            bootsDisplay = boots.getDisplay() + bootsEquip;
            if (!bootsEquipped) power -= Power.getPower(boots.getRarity()) + Power.getPower(boots.getItemType());
        }

        IronWeapon weapon = p.getArmorIronUser().getIronWeapon();
        IronShield shield = p.getArmorIronUser().getIronShield();

        if (helmet == null && chestplate == null && gloves == null && trousers == null && boots == null && weapon == null && shield == null) {
            eb.appendDescription("\n" + "Use `" + PrefixUser.getPrefixUser(uid).getPrefix() + "invd [Helmet | Chestplate | Trousers |\n" +
                    "Gloves | Boots | Weapon | Shield] [Cost]`");
        }

        boolean swordEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronSword().equalsIgnoreCase("equipped");
        String swordEquip = "";
        if (!swordEquipped) {
            swordEquip = " - **Unequipped**";
        }
        String weaponDisplay = EmoteUtil.getEmoteMention("Null_Weapon") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `20` to unlock";
        if (weapon != null) {
            weaponDisplay = weapon.getDisplay() + swordEquip;
            if (!swordEquipped) power -= Power.getPower(weapon.getRarity()) + Power.getPower(weapon.getItemType()) + Power.getPowerIronWeapon(weapon);
        }

        boolean shieldEquipped = EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronShield().equalsIgnoreCase("equipped");
        String shieldEquip = "";
        if (!shieldEquipped) {
            shieldEquip = " - **Unequipped**";
        }
        String shieldDisplay = EmoteUtil.getEmoteMention("Null_Shield") + " Costs " + EmoteUtil.getEmoteMention("Iron_Scroll") + " `35` to unlock";
        if (shield != null) {
            shieldDisplay = shield.getDisplay() + shieldEquip;
            if (!shieldEquipped) power -= Power.getPower(shield.getRarity()) + Power.getPower(shield.getItemType());
        }
        
        if (ironUnequipped) {
            eb.appendDescription("**Iron Power:** ~~" + power + "~~ **Unequipped**");
        } else {
            eb.appendDescription("**Iron Power:** " + power);
        }

        eb.appendDescription("\n" + helmetDisplay);
        eb.appendDescription("\n" + chestplateDisplay);
        eb.appendDescription("\n" + glovesDisplay);
        eb.appendDescription("\n" + trousersDisplay);
        eb.appendDescription("\n" + bootsDisplay);
        eb.appendDescription("\n" + weaponDisplay);
        eb.appendDescription("\n" + shieldDisplay);

        ArmorIronUser au = p.getArmorIronUser();
        int wd1 = 0;
        int wd2 = 0;
        int wad = 0;
        int wap = 0;
        if (weapon != null && swordEquipped) {
            wd1 = weapon.getDamageFrom();
            wd2 = weapon.getDamageTo();
            wad = weapon.getRarityDamage();
            wap = weapon.getRarityProtection();
        }
        int sp = 0;
        if (shield != null && shieldEquipped) {
            sp = shield.getProtection();
            wap = shield.getRarityProtection();
        }

        int totalDmg = FormatUtil.getBetween(wd1, wd2) + wad;
        int totalProtection = wap + sp;
        int scrolls = IronScrollsUtil.getIronScrolls(uid);

        if (helmetEquipped && helmet != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(au.getIronHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getIronHelmet().getProtectionFrom() + p1iron, au.getIronHelmet().getProtectionTo() + p2iron);
        }

        if (chestplateEquipped && chestplate != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(au.getIronChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getIronChestplate().getProtectionFrom() + p1iron, au.getIronChestplate().getProtectionTo() + p2iron);
        }

        if (glovesEquipped && gloves != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(au.getIronGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getIronGloves().getProtectionFrom() + p1iron, au.getIronGloves().getProtectionTo() + p2iron);
        }

        if (trousersEquipped && trousers != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(au.getIronTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getIronTrousers().getProtectionFrom() + p1iron, au.getIronTrousers().getProtectionTo() + p2iron);
        }

        if (bootsEquipped && boots != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(au.getIronBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getIronBoots().getProtectionFrom() + p1iron, au.getIronBoots().getProtectionTo() + p2iron);
        }

        eb.appendDescription("\n\n" + totalProtection + " " + EmoteUtil.getEmoteMention("Protection") + Constants.TAB + Constants.TAB +
                totalDmg + " " + EmoteUtil.getEmoteMention("Damage") + Constants.TAB + Constants.TAB + scrolls + " " + EmoteUtil.getEmoteMention("Iron_Scroll"));
        return eb.build();
    }

    private double getRarityMultiplier(Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return 1.23;
            case UNCOMMON:
                return 1.51;
            case RARE:
                return 1.86;
            case EPIC:
                return 2.29;
            case LEGEND:
                return 2.82;
            case MYTHIC:
                return 3.45;
            case ANCIENT:
                return 4.24;
            case ARTIFACT:
                return 5.22;
            default:
                return 1;
        }
    }
}
