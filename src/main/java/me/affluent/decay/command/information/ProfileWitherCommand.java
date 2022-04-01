package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.entity.otherInventory.ArmorWitherUser;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.language.Language;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.WitherScrollsUtil;
import me.affluent.decay.util.MentionUtil;
import me.affluent.decay.util.settingsUtil.EquippedWitherUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class ProfileWitherCommand extends BotCommand {

    public ProfileWitherCommand() {
        this.name = "profilewither";
        this.aliases = new String[]{"pflwither", "inventorywither", "invwither", "invw"};
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
        if (p.getExpUser().getLevel() < 150) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "low_level",
                    "Majesty, you seem to be inexperienced. \nPerhaps reach level `150` first.")));
            return;
        }
        if (args.length < 1) {
            MessageEmbed profile = getProfile(p);
            e.reply(profile);
            return;
        }
        String type = args[0].toLowerCase();
        ArmorWitherUser armorUser = p.getArmorWitherUser();
        int purchaseCost = 0;
        Armor armor = null;
        WitherWeapon weapon = null;
        WitherShield shield = null;
        String armorType = "";
        if (type.equalsIgnoreCase("helmet")) {
            armor = armorUser.getWitherHelmet();
            armorType = "Helmet";
            purchaseCost = 1;
        }
        if (type.equalsIgnoreCase("chestplate")) {
            armor = armorUser.getWitherChestplate();
            armorType = "Chestplate";
            purchaseCost = 2;
        }
        if (type.equalsIgnoreCase("gloves")) {
            armor = armorUser.getWitherGloves();
            armorType = "Gloves";
            purchaseCost = 3;
        }
        if (type.equalsIgnoreCase("trousers")) {
            armor = armorUser.getWitherTrousers();
            armorType = "Trousers";
            purchaseCost = 5;
        }
        if (type.equalsIgnoreCase("boots")) {
            armor = armorUser.getWitherBoots();
            armorType = "Boots";
            purchaseCost = 6;
        }
        int scrolls = WitherScrollsUtil.getWitherScrolls(uid);
        if (scrolls <= 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
            return;
        }
        if (scrolls < purchaseCost) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need `" + purchaseCost +"` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
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
            weapon = armorUser.getWitherWeapon();
            if (weapon != null) {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + (int) (4 * getRarityMultiplier(weapon.getRarity())) + "` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                if (weapon.getRarity() == Rarity.ARTIFACT) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                            Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                    .replace("{item}", weapon.getDisplay())));
                    return;
                }
                if (scrolls < (int) (4 * getRarityMultiplier(weapon.getRarity()))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + (int) (4 * getRarityMultiplier(weapon.getRarity())) +"` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                Rarity better = weapon.getRarity().betterOther();
                String newWeapon = better + " wither sword";
                WitherWeapon witherSword = WitherWeapon.getWeapon(newWeapon);
                if (witherSword != null) armorUser.updateWitherWeapon(witherSword);
                weapon = armorUser.getWitherWeapon();
                WitherScrollsUtil.setWitherScrolls(uid, scrolls - (int) (4 * getRarityMultiplier(weapon.getRarity())));
                scrolls = WitherScrollsUtil.getWitherScrolls(uid);
                upgraded = true;
            } else {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + 4 + "` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                if (scrolls < 4) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + 4 +"` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                WitherScrollsUtil.setWitherScrolls(uid, scrolls - 4);
                scrolls = WitherScrollsUtil.getWitherScrolls(uid);
                String brandNewWeapon = "junk wither sword";
                WitherWeapon witherWeapon = WitherWeapon.getWeapon(brandNewWeapon);
                if (witherWeapon != null) armorUser.updateWitherWeapon(witherWeapon);
                weapon = armorUser.getWitherWeapon();
                purchased = true;
            }
        }

        if (type.equalsIgnoreCase("shield")) {
            shield = armorUser.getWitherShield();
            if (shield != null) {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + (int) (7 * getRarityMultiplier(shield.getRarity())) + "` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                if (shield.getRarity() == Rarity.ARTIFACT) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                            Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                    .replace("{item}", shield.getDisplay())));
                    return;
                }
                if (scrolls < (int) (7 * getRarityMultiplier(shield.getRarity()))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + (int) (7 * getRarityMultiplier(shield.getRarity())) +"` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                Rarity better = shield.getRarity().betterOther();
                String newShield = better + " wither shield";
                WitherShield witherShield = WitherShield.getWitherShield(newShield);
                armorUser.updateWitherShield(witherShield);
                shield = armorUser.getWitherShield();
                WitherScrollsUtil.setWitherScrolls(uid, scrolls - (int) (7 * getRarityMultiplier(shield.getRarity())));
                scrolls = WitherScrollsUtil.getWitherScrolls(uid);
                upgraded = true;
            } else {
                if (cost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "You need `" + 7 + "` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                if (scrolls < 7) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                            "Majesty, you seem to have ran out. \nYou need `" + 7 +"` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                    return;
                }
                WitherScrollsUtil.setWitherScrolls(uid, scrolls - 7);
                scrolls = WitherScrollsUtil.getWitherScrolls(uid);
                String brandNewShield = "junk wither shield";
                WitherShield witherShield = WitherShield.getWitherShield(brandNewShield);
                armorUser.updateWitherShield(witherShield);
                shield = armorUser.getWitherShield();
                purchased = true;
            }
        }

        if (armor != null) {
            if (cost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                        "You need `" + (int) (purchaseCost * getRarityMultiplier(armor.getRarity())) + "` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scrolls") + " to use this.")));
                return;
            }
            if (armor.getRarity() == Rarity.ARTIFACT) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "maxed", "Max Rarity"),
                        Language.getLocalized(uid, "usage", "Majesty, I believe your {item} is of highest quality.")
                                .replace("{item}", armor.getDisplay())));
                return;
            }
            WitherScrollsUtil.setWitherScrolls(uid, scrolls - (int) (purchaseCost * getRarityMultiplier(armor.getRarity())));
            Rarity better = armor.getRarity().betterOther();
            String newArmor = better + " wither " + armorType;
            if (armorType.equalsIgnoreCase("helmet")) {
                WitherHelmet witherHelmet = WitherHelmet.getWitherHelmet(newArmor);
                armorUser.updateWitherHelmet(witherHelmet);
                armor = armorUser.getWitherHelmet();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("chestplate")) {
                WitherChestplate witherChestplate = WitherChestplate.getWitherChestplate(newArmor);
                armorUser.updateWitherChestplate(witherChestplate);
                armor = armorUser.getWitherChestplate();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("trousers")) {
                WitherTrousers witherTrousers = WitherTrousers.getWitherTrousers(newArmor);
                armorUser.updateWitherTrousers(witherTrousers);
                armor = armorUser.getWitherTrousers();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("boots")) {
                WitherBoots witherBoots = WitherBoots.getWitherBoots(newArmor);
                armorUser.updateWitherBoots(witherBoots);
                armor = armorUser.getWitherBoots();
                armorType = "armor";
                upgraded = true;
            }
            if (armorType.equalsIgnoreCase("gloves")) {
                WitherGloves witherGloves = WitherGloves.getWitherGloves(newArmor);
                armorUser.updateWitherGloves(witherGloves);
                armor = armorUser.getWitherGloves();
                armorType = "armor";
                upgraded = true;
            }
        } else {
            if (cost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                        "You need `" + purchaseCost + "` Wither Scrolls " + EmoteUtil.getEmoteMention("Wither_Scroll") + " to use this.")));
                return;
            }
            WitherScrollsUtil.setWitherScrolls(uid, scrolls - purchaseCost);
            String brandNewArmor = "Junk Wither " + armorType;
            if (armorType.equalsIgnoreCase("helmet")) {
                WitherHelmet witherHelmet = WitherHelmet.getWitherHelmet(brandNewArmor);
                armorUser.updateWitherHelmet(witherHelmet);
                armor = armorUser.getWitherHelmet();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("chestplate")) {
                WitherChestplate witherChestplate = WitherChestplate.getWitherChestplate(brandNewArmor);
                armorUser.updateWitherChestplate(witherChestplate);
                armor = armorUser.getWitherChestplate();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("trousers")) {
                WitherTrousers witherTrousers = WitherTrousers.getWitherTrousers(brandNewArmor);
                armorUser.updateWitherTrousers(witherTrousers);
                armor = armorUser.getWitherTrousers();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("boots")) {
                WitherBoots witherBoots = WitherBoots.getWitherBoots(brandNewArmor);
                armorUser.updateWitherBoots(witherBoots);
                armor = armorUser.getWitherBoots();
                armorType = "armor";
                purchased = true;
            }
            if (armorType.equalsIgnoreCase("gloves")) {
                WitherGloves witherGloves = WitherGloves.getWitherGloves(brandNewArmor);
                armorUser.updateWitherGloves(witherGloves);
                armor = armorUser.getWitherGloves();
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
        boolean witherUnequipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherBoots().equalsIgnoreCase("unequipped") &&
                EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherHelmet().equalsIgnoreCase("unequipped") &&
                EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherGloves().equalsIgnoreCase("unequipped") &&
                EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherTrousers().equalsIgnoreCase("unequipped") &&
                EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherChestplate().equalsIgnoreCase("unequipped") &&
                EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherShield().equalsIgnoreCase("unequipped") &&
                EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherSword().equalsIgnoreCase("unequipped");
        int power = Power.getWitherPower(p);

        boolean helmetEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherHelmet().equalsIgnoreCase("equipped");
        String helmetEquip = "";
        if (!helmetEquipped) {
            helmetEquip = " - **Unequipped**";
        }
        WitherHelmet helmet = p.getArmorWitherUser().getWitherHelmet();
        String helmetDisplay = EmoteUtil.getEmoteMention("Null_Helmet") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `1` to unlock";
        if (helmet != null) {
            helmetDisplay = helmet.getDisplay() + helmetEquip;
            if (!helmetEquipped) power -= Power.getPower(helmet.getRarity()) + Power.getPower(helmet.getItemType());
        }

        boolean chestplateEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherChestplate().equalsIgnoreCase("equipped");
        String chestplateEquip = "";
        if (!chestplateEquipped) {
            chestplateEquip = " - **Unequipped**";
        }
        WitherChestplate chestplate = p.getArmorWitherUser().getWitherChestplate();
        String chestplateDisplay = EmoteUtil.getEmoteMention("Null_Chestplate") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `2` to unlock";
        if (chestplate != null) {
            chestplateDisplay = chestplate.getDisplay() + chestplateEquip;
            if (!chestplateEquipped) power -= Power.getPower(chestplate.getRarity()) + Power.getPower(chestplate.getItemType());
        }

        boolean glovesEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherGloves().equalsIgnoreCase("equipped");
        String glovesEquip = "";
        if (!glovesEquipped) {
            glovesEquip = " - **Unequipped**";
        }
        WitherGloves gloves = p.getArmorWitherUser().getWitherGloves();
        String glovesDisplay = EmoteUtil.getEmoteMention("Null_Gloves") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `3` to unlock";
        if (gloves != null) {
            glovesDisplay = gloves.getDisplay() + glovesEquip;
            if (!glovesEquipped) power -= Power.getPower(gloves.getRarity()) + Power.getPower(gloves.getItemType());
        }

        boolean trousersEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherTrousers().equalsIgnoreCase("equipped");
        String trousersEquip = "";
        if (!trousersEquipped) {
            trousersEquip = " - **Unequipped**";
        }
        WitherTrousers trousers = p.getArmorWitherUser().getWitherTrousers();
        String trousersDisplay = EmoteUtil.getEmoteMention("Null_Trousers") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `5` to unlock";
        if (trousers != null) {
            trousersDisplay = trousers.getDisplay() + trousersEquip;
            if (!trousersEquipped) power -= Power.getPower(trousers.getRarity()) + Power.getPower(trousers.getItemType());
        }

        boolean bootsEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherBoots().equalsIgnoreCase("equipped");
        String bootsEquip = "";
        if (!bootsEquipped) {
            bootsEquip = " - **Unequipped**";
        }
        WitherBoots boots = p.getArmorWitherUser().getWitherBoots();
        String bootsDisplay = EmoteUtil.getEmoteMention("Null_Boots") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `6` to unlock";
        if (boots != null) {
            bootsDisplay = boots.getDisplay() + bootsEquip;
            if (!bootsEquipped) power -= Power.getPower(boots.getRarity()) + Power.getPower(boots.getItemType());
        }

        WitherWeapon weapon = p.getArmorWitherUser().getWitherWeapon();
        WitherShield shield = p.getArmorWitherUser().getWitherShield();

        if (helmet == null && chestplate == null && gloves == null && trousers == null && boots == null && weapon == null && shield == null) {
            eb.appendDescription("\n" + "Use `" + PrefixUser.getPrefixUser(uid).getPrefix() + "invd [Helmet | Chestplate | Trousers |\n" +
                    "Gloves | Boots | Weapon | Shield] [Cost]`");
        }

        boolean swordEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherSword().equalsIgnoreCase("equipped");
        String swordEquip = "";
        if (!swordEquipped) {
            swordEquip = " - **Unequipped**";
        }
        String weaponDisplay = EmoteUtil.getEmoteMention("Null_Weapon") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `4` to unlock";
        if (weapon != null) {
            weaponDisplay = weapon.getDisplay() + swordEquip;
            if (!swordEquipped) power -= Power.getPower(weapon.getRarity()) + Power.getPower(weapon.getItemType()) + Power.getPowerWitherWeapon(weapon);
        }

        boolean shieldEquipped = EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherShield().equalsIgnoreCase("equipped");
        String shieldEquip = "";
        if (!shieldEquipped) {
            shieldEquip = " - **Unequipped**";
        }
        String shieldDisplay = EmoteUtil.getEmoteMention("Null_Shield") + " Costs " + EmoteUtil.getEmoteMention("Wither_Scroll") + " `7` to unlock";
        if (shield != null) {
            shieldDisplay = shield.getDisplay() + shieldEquip;
            if (!shieldEquipped) power -= Power.getPower(shield.getRarity()) + Power.getPower(shield.getItemType());
        }

        if (witherUnequipped) {
            eb.appendDescription("**Wither Power:** ~~" + power + "~~ **Unequipped**");
        } else {
            eb.appendDescription("**Wither Power:** " + power);
        }

        eb.appendDescription("\n" + helmetDisplay);
        eb.appendDescription("\n" + chestplateDisplay);
        eb.appendDescription("\n" + glovesDisplay);
        eb.appendDescription("\n" + trousersDisplay);
        eb.appendDescription("\n" + bootsDisplay);
        eb.appendDescription("\n" + weaponDisplay);
        eb.appendDescription("\n" + shieldDisplay);

        ArmorWitherUser au = p.getArmorWitherUser();
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
        int scrolls = WitherScrollsUtil.getWitherScrolls(uid);

        if (helmetEquipped && helmet != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(au.getWitherHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getWitherHelmet().getProtectionFrom() + p1wither, au.getWitherHelmet().getProtectionTo() + p2wither);
        }

        if (chestplateEquipped && chestplate != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(au.getWitherChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getWitherChestplate().getProtectionFrom() + p1wither, au.getWitherChestplate().getProtectionTo() + p2wither);
        }

        if (glovesEquipped && gloves != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(au.getWitherGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getWitherGloves().getProtectionFrom() + p1wither, au.getWitherGloves().getProtectionTo() + p2wither);
        }

        if (trousersEquipped && trousers != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(au.getWitherTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getWitherTrousers().getProtectionFrom() + p1wither, au.getWitherTrousers().getProtectionTo() + p2wither);
        }

        if (bootsEquipped && boots != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(au.getWitherBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            totalProtection += FormatUtil.getBetween(au.getWitherBoots().getProtectionFrom() + p1wither, au.getWitherBoots().getProtectionTo() + p2wither);
        }

        eb.appendDescription("\n\n" + totalProtection + " " + EmoteUtil.getEmoteMention("Protection") + Constants.TAB + Constants.TAB +
                totalDmg + " " + EmoteUtil.getEmoteMention("Damage") + Constants.TAB + Constants.TAB + scrolls + " " + EmoteUtil.getEmoteMention("Wither_Scroll"));
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
