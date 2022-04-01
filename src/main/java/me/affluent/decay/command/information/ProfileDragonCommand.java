package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.entity.otherInventory.ArmorDragonUser;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.language.Language;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.DragonScrollsUtil;
import me.affluent.decay.util.MentionUtil;
import me.affluent.decay.util.DragonScrollsUtil;
import me.affluent.decay.util.settingsUtil.EquippedDragonUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class ProfileDragonCommand extends BotCommand {

    public ProfileDragonCommand() {
        this.name = "profiledragon";
        this.aliases = new String[]{"pfldragon", "inventorydragon", "invdragon", "invd"};
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
        if (p.getExpUser().getLevel() < 75) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "low_level",
                    "Majesty, you seem to be inexperienced. \nPerhaps reach level `75` first.")));
            return;
        }
        if (args.length < 1) {
            MessageEmbed profile = getProfile(p);
            e.reply(profile);
            return;
        }
        String type = args[0].toLowerCase();
        ArmorDragonUser armorUser = p.getArmorDragonUser();
        int purchaseCost = 0;
        Armor armor = null;
        DragonWeapon weapon = null;
        DragonShield shield = null;
        String armorType = "";
        if (type.equalsIgnoreCase("helmet")) {
            armor = armorUser.getDragonHelmet();
            armorType = "Helmet";
            purchaseCost = 3;
        }
        if (type.equalsIgnoreCase("chestplate")) {
            armor = armorUser.getDragonChestplate();
            armorType = "Chestplate";
            purchaseCost = 6;
        }
        if (type.equalsIgnoreCase("gloves")) {
            armor = armorUser.getDragonGloves();
            armorType = "Gloves";
            purchaseCost = 9;
        }
        if (type.equalsIgnoreCase("trousers")) {
            armor = armorUser.getDragonTrousers();
            armorType = "Trousers";
            purchaseCost = 15;
        }
        if (type.equalsIgnoreCase("boots")) {
            armor = armorUser.getDragonBoots();
            armorType = "Boots";
            purchaseCost = 18;
        }
        int scrolls = DragonScrollsUtil.getDragonScrolls(uid);
        if (scrolls <= 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need Dragon Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
            return;
        }
        if (scrolls < purchaseCost) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need `" + purchaseCost +"` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
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
            weapon = armorUser.getDragonWeapon();
            if (weapon != null) {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + (int) (12 * getRarityMultiplier(weapon.getRarity())) + "` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                if (weapon.getRarity() == Rarity.ARTIFACT) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                            Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                    .replace("{item}", weapon.getDisplay())));
                    return;
                }
                if (scrolls < (int) (12 * getRarityMultiplier(weapon.getRarity()))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + (int) (12 * getRarityMultiplier(weapon.getRarity())) +"` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                Rarity better = weapon.getRarity().betterOther();
                String newWeapon = better + " dragon steel sword";
                DragonWeapon dragonSword = DragonWeapon.getWeapon(newWeapon);
                if (dragonSword != null) armorUser.updateDragonWeapon(dragonSword);
                weapon = armorUser.getDragonWeapon();
                DragonScrollsUtil.setDragonScrolls(uid, scrolls - (int) (12 * getRarityMultiplier(weapon.getRarity())));
                scrolls = DragonScrollsUtil.getDragonScrolls(uid);
                upgraded = true;
            } else {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + 12 + "` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                if (scrolls < 12) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + 12 +"` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                DragonScrollsUtil.setDragonScrolls(uid, scrolls - 12);
                scrolls = DragonScrollsUtil.getDragonScrolls(uid);
                String brandNewWeapon = "junk dragon steel sword";
                DragonWeapon dragonWeapon = DragonWeapon.getWeapon(brandNewWeapon);
                if (dragonWeapon != null) armorUser.updateDragonWeapon(dragonWeapon);
                weapon = armorUser.getDragonWeapon();
                purchased = true;
            }
        }

        if (type.equalsIgnoreCase("shield")) {
            shield = armorUser.getDragonShield();
            if (shield != null) {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + (int) (21 * getRarityMultiplier(shield.getRarity())) + "` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                if (shield.getRarity() == Rarity.ARTIFACT) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                            Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                    .replace("{item}", shield.getDisplay())));
                    return;
                }
                if (scrolls < (int) (21 * getRarityMultiplier(shield.getRarity()))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + (int) (21 * getRarityMultiplier(shield.getRarity())) +"` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                Rarity better = shield.getRarity().betterOther();
                String newShield = better + " dragon steel shield";
                DragonShield dragonShield = DragonShield.getDragonShield(newShield);
                armorUser.updateDragonShield(dragonShield);
                shield = armorUser.getDragonShield();
                DragonScrollsUtil.setDragonScrolls(uid, scrolls - (int) (21 * getRarityMultiplier(shield.getRarity())));
                scrolls = DragonScrollsUtil.getDragonScrolls(uid);
                upgraded = true;
            } else {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + 21 + "` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                if (scrolls < 21) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + 21 +"` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                    return;
                }
                DragonScrollsUtil.setDragonScrolls(uid, scrolls - 21);
                scrolls = DragonScrollsUtil.getDragonScrolls(uid);
                String brandNewShield = "junk dragon steel shield";
                DragonShield dragonShield = DragonShield.getDragonShield(brandNewShield);
                armorUser.updateDragonShield(dragonShield);
                shield = armorUser.getDragonShield();
                purchased = true;
            }
        }

        if (armor != null) {
            if (cost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                        "You need `" + (int) (purchaseCost * getRarityMultiplier(armor.getRarity())) + "` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scrolls") + " to use this.")));
                return;
            }
            if (armor.getRarity() == Rarity.ARTIFACT) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                        Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                .replace("{item}", armor.getDisplay())));
                return;
            }
            DragonScrollsUtil.setDragonScrolls(uid, scrolls - (int) (purchaseCost * getRarityMultiplier(armor.getRarity())));
            Rarity better = armor.getRarity().betterOther();
            String newArmor = better + " dragon steel " + armorType;
            if (armorType.equalsIgnoreCase("helmet")) {
                DragonHelmet dragonHelmet = DragonHelmet.getDragonHelmet(newArmor);
                armorUser.updateDragonHelmet(dragonHelmet);
                armor = armorUser.getDragonHelmet();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("chestplate")) {
                DragonChestplate dragonChestplate = DragonChestplate.getDragonChestplate(newArmor);
                armorUser.updateDragonChestplate(dragonChestplate);
                armor = armorUser.getDragonChestplate();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("trousers")) {
                DragonTrousers dragonTrousers = DragonTrousers.getDragonTrousers(newArmor);
                armorUser.updateDragonTrousers(dragonTrousers);
                armor = armorUser.getDragonTrousers();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("boots")) {
                DragonBoots dragonBoots = DragonBoots.getDragonBoots(newArmor);
                armorUser.updateDragonBoots(dragonBoots);
                armor = armorUser.getDragonBoots();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("gloves")) {
                DragonGloves dragonGloves = DragonGloves.getDragonGloves(newArmor);
                armorUser.updateDragonGloves(dragonGloves);
                armor = armorUser.getDragonGloves();
                armorType = "armor";
                upgraded = true;
            }
        } else {
            if (cost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                        "You need `" + purchaseCost + "` Dragon Steel Scrolls " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " to use this.")));
                return;
            }
            DragonScrollsUtil.setDragonScrolls(uid, scrolls - purchaseCost);
            String brandNewArmor = "Junk Dragon steel " + armorType;
            if (armorType.equalsIgnoreCase("helmet")) {
                DragonHelmet dragonHelmet = DragonHelmet.getDragonHelmet(brandNewArmor);
                armorUser.updateDragonHelmet(dragonHelmet);
                armor = armorUser.getDragonHelmet();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("chestplate")) {
                DragonChestplate dragonChestplate = DragonChestplate.getDragonChestplate(brandNewArmor);
                armorUser.updateDragonChestplate(dragonChestplate);
                armor = armorUser.getDragonChestplate();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("trousers")) {
                DragonTrousers dragonTrousers = DragonTrousers.getDragonTrousers(brandNewArmor);
                armorUser.updateDragonTrousers(dragonTrousers);
                armor = armorUser.getDragonTrousers();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("boots")) {
                DragonBoots dragonBoots = DragonBoots.getDragonBoots(brandNewArmor);
                armorUser.updateDragonBoots(dragonBoots);
                armor = armorUser.getDragonBoots();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("gloves")) {
                DragonGloves dragonGloves = DragonGloves.getDragonGloves(brandNewArmor);
                armorUser.updateDragonGloves(dragonGloves);
                armor = armorUser.getDragonGloves();
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

        boolean dragonUnequipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonBoots().equalsIgnoreCase("unequipped") &&
                EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonHelmet().equalsIgnoreCase("unequipped") &&
                EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonGloves().equalsIgnoreCase("unequipped") &&
                EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonTrousers().equalsIgnoreCase("unequipped") &&
                EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonChestplate().equalsIgnoreCase("unequipped") &&
                EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonShield().equalsIgnoreCase("unequipped") &&
                EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonSword().equalsIgnoreCase("unequipped");
        int power = Power.getDragonPower(p);
        
        boolean helmetEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonHelmet().equalsIgnoreCase("equipped");
        String helmetEquip = "";
        if (!helmetEquipped) {
            helmetEquip = " - **Unequipped**";
        }
        DragonHelmet helmet = p.getArmorDragonUser().getDragonHelmet();
        String helmetDisplay = EmoteUtil.getEmoteMention("Null_Helmet") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `3` to unlock";
        if (helmet != null) {
            helmetDisplay = helmet.getDisplay() + helmetEquip;
            if (!helmetEquipped) power -= Power.getPower(helmet.getRarity()) + Power.getPower(helmet.getItemType());
        }

        boolean chestplateEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonChestplate().equalsIgnoreCase("equipped");
        String chestplateEquip = "";
        if (!chestplateEquipped) {
            chestplateEquip = " - **Unequipped**";
        }
        DragonChestplate chestplate = p.getArmorDragonUser().getDragonChestplate();
        String chestplateDisplay = EmoteUtil.getEmoteMention("Null_Chestplate") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `6` to unlock";
        if (chestplate != null) {
            chestplateDisplay = chestplate.getDisplay() + chestplateEquip;
            if (!chestplateEquipped) power -= Power.getPower(chestplate.getRarity()) + Power.getPower(chestplate.getItemType());
        }

        boolean glovesEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonGloves().equalsIgnoreCase("equipped");
        String glovesEquip = "";
        if (!glovesEquipped) {
            glovesEquip = " - **Unequipped**";
        }
        DragonGloves gloves = p.getArmorDragonUser().getDragonGloves();
        String glovesDisplay = EmoteUtil.getEmoteMention("Null_Gloves") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `9` to unlock";
        if (gloves != null) {
            glovesDisplay = gloves.getDisplay() + glovesEquip;
            if (!glovesEquipped) power -= Power.getPower(gloves.getRarity()) + Power.getPower(gloves.getItemType());
        }

        boolean trousersEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonTrousers().equalsIgnoreCase("equipped");
        String trousersEquip = "";
        if (!trousersEquipped) {
            trousersEquip = " - **Unequipped**";
        }
        DragonTrousers trousers = p.getArmorDragonUser().getDragonTrousers();
        String trousersDisplay = EmoteUtil.getEmoteMention("Null_Trousers") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `15` to unlock";
        if (trousers != null) {
            trousersDisplay = trousers.getDisplay() + trousersEquip;
            if (!trousersEquipped) power -= Power.getPower(trousers.getRarity()) + Power.getPower(trousers.getItemType());
        }

        boolean bootsEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonBoots().equalsIgnoreCase("equipped");
        String bootsEquip = "";
        if (!bootsEquipped) {
            bootsEquip = " - **Unequipped**";
        }
        DragonBoots boots = p.getArmorDragonUser().getDragonBoots();
        String bootsDisplay = EmoteUtil.getEmoteMention("Null_Boots") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `18` to unlock";
        if (boots != null) {
            bootsDisplay = boots.getDisplay() + bootsEquip;
            if (!bootsEquipped) power -= Power.getPower(boots.getRarity()) + Power.getPower(boots.getItemType());
        }

        DragonWeapon weapon = p.getArmorDragonUser().getDragonWeapon();
        DragonShield shield = p.getArmorDragonUser().getDragonShield();

        if (helmet == null && chestplate == null && gloves == null && trousers == null && boots == null && weapon == null && shield == null) {
            eb.appendDescription("\n" + "Use `" + PrefixUser.getPrefixUser(uid).getPrefix() + "invd [Helmet | Chestplate | Trousers |\n" +
                    "Gloves | Boots | Weapon | Shield] [Cost]`");
        }

        boolean swordEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonSword().equalsIgnoreCase("equipped");
        String swordEquip = "";
        if (!swordEquipped) {
            swordEquip = " - **Unequipped**";
        }
        String weaponDisplay = EmoteUtil.getEmoteMention("Null_Weapon") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `12` to unlock";
        if (weapon != null) {
            weaponDisplay = weapon.getDisplay() + swordEquip;
            if (!swordEquipped) power -= Power.getPower(weapon.getRarity()) + Power.getPower(weapon.getItemType()) + Power.getPowerDragonWeapon(weapon);
        }

        boolean shieldEquipped = EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonShield().equalsIgnoreCase("equipped");
        String shieldEquip = "";
        if (!shieldEquipped) {
            shieldEquip = " - **Unequipped**";
        }
        String shieldDisplay = EmoteUtil.getEmoteMention("Null_Shield") + " Costs " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " `21` to unlock";
        if (shield != null) {
            shieldDisplay = shield.getDisplay() + shieldEquip;
            if (!shieldEquipped) power -= Power.getPower(shield.getRarity()) + Power.getPower(shield.getItemType());
        }
        
        if (dragonUnequipped) {
            eb.appendDescription("**Dragon Power:** ~~" + power + "~~ **Unequipped**");
        } else {
            eb.appendDescription("**Dragon Power:** " + power);
        }

        eb.appendDescription("\n" + helmetDisplay);
        eb.appendDescription("\n" + chestplateDisplay);
        eb.appendDescription("\n" + glovesDisplay);
        eb.appendDescription("\n" + trousersDisplay);
        eb.appendDescription("\n" + bootsDisplay);
        eb.appendDescription("\n" + weaponDisplay);
        eb.appendDescription("\n" + shieldDisplay);

        ArmorDragonUser au = p.getArmorDragonUser();
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
        int scrolls = DragonScrollsUtil.getDragonScrolls(uid);

        if (helmetEquipped && helmet != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(au.getDragonHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getDragonHelmet().getProtectionFrom() + p1dragon, au.getDragonHelmet().getProtectionTo() + p2dragon);
        }

        if (chestplateEquipped && chestplate != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(au.getDragonChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getDragonChestplate().getProtectionFrom() + p1dragon, au.getDragonChestplate().getProtectionTo() + p2dragon);
        }

        if (glovesEquipped && gloves != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(au.getDragonGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getDragonGloves().getProtectionFrom() + p1dragon, au.getDragonGloves().getProtectionTo() + p2dragon);
        }

        if (trousersEquipped && trousers != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(au.getDragonTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getDragonTrousers().getProtectionFrom() + p1dragon, au.getDragonTrousers().getProtectionTo() + p2dragon);
        }

        if (bootsEquipped && boots != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(au.getDragonBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getDragonBoots().getProtectionFrom() + p1dragon, au.getDragonBoots().getProtectionTo() + p2dragon);
        }

        eb.appendDescription("\n\n" + totalProtection + " " + EmoteUtil.getEmoteMention("Protection") + Constants.TAB + Constants.TAB +
                totalDmg + " " + EmoteUtil.getEmoteMention("Damage") + Constants.TAB + Constants.TAB + scrolls + " " + EmoteUtil.getEmoteMention("Dragon_Steel_Scroll"));
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
