package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.api.PetXPApi;
import me.affluent.decay.armor.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.*;
import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.pvp.Fight;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemLockingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigInteger;
import java.util.List;

import static me.affluent.decay.enums.Rarity.*;

public class CompareCommand extends BotCommand {

    public CompareCommand() {
        this.name = "compare";
        this.aliases = new String[]{"com"};
        this.cooldown = 1.0;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        if (!(Player.playerExists(u.getId()))) {
            e.reply(Constants.PROFILE_404(u.getId()));
            return;
        }
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "compare <item ID>`\n" +
                                    "Use `" + userPrefix + "compare pet <petId>` for pets.")));
            return;
        }
        Player p = Player.getPlayer(u.getId());
        PetUser petUser = p.getPetUser();
        ArmorUser armorUser = p.getArmorUser();
        String type1 = args[0].toLowerCase();
        if (args[0].equalsIgnoreCase("pet") && args.length != 1) {
            type1 = args[1].toLowerCase();
        }
        Armor armor1 = null;
        Armor armor2 = null;
        Weapon weapon1 = null;
        Weapon weapon2 = null;
        Arrow arrow1 = null;
        Arrow arrow2 = null;
        Shield shield1 = null;
        Shield shield2 = null;
        PetItem petObj1 = null;
        PetItem petObj2 = null;
        Pets petItem1 = null;
        Pets petItem2 = null;
        long itemID1 = 0;
        String itemID = "";
        long petID1 = 0;
        String petID = "";
        if (!args[0].equalsIgnoreCase("pet")) {
            try {
                itemID1 = Long.parseLong(type1);
                Item item1 = InventoryUser.getItemByID(itemID1);
                if (item1 != null) {
                    String in = item1.getName().toLowerCase();
                    if (in.endsWith(" helmet")) {
                        armor2 = Helmet.getHelmet(in);
                        armor1 = armorUser.getHelmet();
                        itemID = armorUser.getHelmetID();
                    }
                    if (in.endsWith(" chestplate")) {
                        armor2 = Chestplate.getChestplate(in);
                        armor1 = armorUser.getChestplate();
                        itemID = armorUser.getChestplateID();
                    }
                    if (in.endsWith(" gloves")) {
                        armor2 = Gloves.getGloves(in);
                        armor1 = armorUser.getGloves();
                        itemID = armorUser.getGlovesID();
                    }
                    if (in.endsWith(" trousers")) {
                        armor2 = Trousers.getTrousers(in);
                        armor1 = armorUser.getTrousers();
                        itemID = armorUser.getTrousersID();
                    }
                    if (in.endsWith(" boots")) {
                        armor2 = Boots.getBoots(in);
                        armor1 = armorUser.getBoots();
                        itemID = armorUser.getBootsID();
                    }
                    if (in.endsWith(" sword") || in.endsWith(" bow") || in.endsWith(" staff")) {
                        weapon2 = Weapon.getWeapon(in);
                        weapon1 = armorUser.getWeapon();
                        itemID = armorUser.getWeaponID();
                    }
                    if (in.endsWith(" arrow")) {
                        arrow2 = Arrow.getArrow(in);
                        arrow1 = armorUser.getArrow();
                        itemID = armorUser.getArrowID();
                    }
                    if (in.endsWith(" shield")) {
                        shield2 = Shield.getShield(in);
                        shield1 = armorUser.getShield();
                        itemID = armorUser.getShieldID();
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        if (args[0].equalsIgnoreCase("pet")) {
            try {
                petID1 = Long.parseLong(type1);
                petItem2 = PetUtil.getPetByID(Long.parseLong(type1));
                if (petItem2 != null) {
                    String pi = petItem2.getPetName().toLowerCase().replace("_", " ");
                    petObj2 = PetItem.getPetItem(pi);
                    petObj1 = petUser.getPet();
                    petID = petUser.getPetID();
                }
            } catch (NumberFormatException ignored) {
            }
        }

        String petResponse1 = null;
        String petResponse2 = null;
        String petRarityColor1 = "";
        String petRarityColor2 = "";
        String petSuburl1 = "https://thewithering.com/";
        String petSuburl2 = "https://thewithering.com/";
        String petName1 = "";
        String petName2 = "";
        if (args[0].equalsIgnoreCase("pet")) {
            if (petObj1 != null) {
                petItem1 = PetUtil.getEQPetByID(Long.parseLong(petID));
                petName1 = petItem1.getPetName().toLowerCase().replace("_", " ");
                String[] petColor = petItem1.getPetName().replaceAll("_", " ").split(" ");
                petRarityColor1 = petColor[0];
                int petStars = PetStarringUtil.getPetStar(p.getUserId(), Long.parseLong(petID));
                int petLevel = petItem1.getPetLevel(uid, petID);
                BigInteger petExp = petItem1.getPetExp(uid, petID);
                double maxPetExp = PetXPApi.getNeededPetXP(petObj1, petLevel);
                boolean xpMaxed = p.getPetExpUser().isPetMaxed(petObj1, uid, Math.toIntExact(Long.parseLong(petID)));
                String exp = "";
                if (xpMaxed) {
                    exp = "**Maxed**";
                } else {
                    exp = "EXP **" + FormatUtil.formatCommas(String.valueOf(petExp)) + "/ " + FormatUtil.formatCommas(String.valueOf(maxPetExp)) + "**";
                }
                StringBuilder petStarDisplay = new StringBuilder();
                if (petStars > 0) {
                    for (int i = 0; i < petStars; i++) {
                        petStarDisplay.append(EmoteUtil.getEmoteMention("S_")).append(" ");
                    }
                    if (petStars < 10) {
                        for (int s = petStars; s < 10; s++) {
                            petStarDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        petStarDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                    }
                }
                int petHealth = 0;
                petHealth += (petObj1.getLevelHP() * petLevel);
                petHealth += petObj1.getStarterHP();
                int petDamage = 0;
                petDamage += (petObj1.getLevelDMG() * petLevel);
                petDamage += petObj1.getStarterDMG();
                if (petStars > 0) {
                    petHealth *= ((double) (petStars * 5) / 100) + 1;
                    petDamage *= ((double) (petStars * 5) / 100) + 1;
                }
                if (petLevel == petObj1.getMaxLevel()) {
                    petDamage *= 1.15;
                    petHealth *= 1.15;
                }
                petSuburl1 += petName1.replaceAll(" ", "_").toUpperCase() + ".png";
                int petPower = Power.getFullPetPower(p, petObj1, petLevel, petStars);
                petResponse1 = Language.getLocalized(uid, "cmd_attributes_pet",
                                "LVL {pet_level} | {exp}" + "\n\n" +
                                        "HP | DMG\n" +
                                        "{pet_final_HP} | {pet_final_DMG}\n" +
                                        "{pet_star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        "Base HP: {base_HP}\n" +
                                        "Base DMG: {base_DMG}\n" +
                                        "HP per lvl: {pet_per_lvl_HP}\n" +
                                        "DMG per lvl: {pet_per_lvl_DMG}\n" +
                                        "\n[ID: " + petID + "]")
                        .replace("{exp}", "" + exp)
                        .replace("{pet_level}", "**" + petLevel + "**")
                        .replace("{base_HP}", "" + petObj1.getStarterHP())
                        .replace("{pet_per_lvl_HP}", "" + petObj1.getLevelHP())
                        .replace("{pet_per_lvl_DMG}", "" + petObj1.getLevelDMG())
                        .replace("{base_DMG}", "" + petObj1.getStarterDMG())
                        .replace("{pet_star_display}", petStarDisplay.toString())
                        .replace("{power_rating}", "**" + petPower + "**")
                        .replace("{pet_final_HP}", "**" + petHealth + "**")
                        .replace("{pet_final_DMG}", "**" + petDamage + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));
            }
            if (petObj2 != null) {
                petName2 = petItem2.getPetName().toLowerCase().replace("_", " ");
                String[] petColor = petItem2.getPetName().replaceAll("_", " ").split(" ");
                petRarityColor2 = petColor[0];
                int petStars = PetStarringUtil.getPetStar(p.getUserId(), petID1);
                int petLevel = petItem2.getPetLevel(uid, String.valueOf(petID1));
                BigInteger petExp = petItem2.getPetExp(uid, String.valueOf(petID1));
                double maxPetExp = PetXPApi.getNeededPetXP(petObj2, petLevel);
                boolean xpMaxed = p.getPetExpUser().isPetMaxed(petObj2, uid, Math.toIntExact(petID1));
                String exp = "";
                if (xpMaxed) {
                    exp = "**Maxed**";
                } else {
                    exp = "EXP **" + FormatUtil.formatCommas(String.valueOf(petExp)) + "/ " + FormatUtil.formatCommas(String.valueOf(maxPetExp)) + "**";
                }
                StringBuilder petStarDisplay = new StringBuilder();
                if (petStars > 0) {
                    for (int i = 0; i < petStars; i++) {
                        petStarDisplay.append(EmoteUtil.getEmoteMention("S_")).append(" ");
                    }
                    if (petStars < 10) {
                        for (int s = petStars; s < 10; s++) {
                            petStarDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        petStarDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                    }
                }
                int petHealth = 0;
                petHealth += (petObj2.getLevelHP() * petLevel);
                petHealth += petObj2.getStarterHP();
                int petDamage = 0;
                petDamage += (petObj2.getLevelDMG() * petLevel);
                petDamage += petObj2.getStarterDMG();
                if (petStars > 0) {
                    petHealth *= ((double) (petStars * 5) / 100) + 1;
                    petDamage *= ((double) (petStars * 5) / 100) + 1;
                }
                if (petLevel == petObj2.getMaxLevel()) {
                    petDamage *= 1.15;
                    petHealth *= 1.15;
                }
                petSuburl2 += petName2.replaceAll(" ", "_").toUpperCase() + ".png";
                int petPower = Power.getFullPetPower(p, petObj2, petLevel, petStars);
                petResponse2 = Language.getLocalized(uid, "cmd_attributes_pet",
                                "LVL {pet_level} | {exp}" + "\n\n" +
                                        "HP | DMG\n" +
                                        "{pet_final_HP} | {pet_final_DMG}\n" +
                                        "{pet_star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        "Base HP: {base_HP}\n" +
                                        "Base DMG: {base_DMG}\n" +
                                        "HP per lvl: {pet_per_lvl_HP}\n" +
                                        "DMG per lvl: {pet_per_lvl_DMG}\n" +
                                        "\n[ID: " + petID1 + "]")
                        .replace("{exp}", "" + exp)
                        .replace("{pet_level}", "**" + petLevel + "**")
                        .replace("{base_HP}", "" + petObj2.getStarterHP())
                        .replace("{pet_per_lvl_HP}", "" + petObj2.getLevelHP())
                        .replace("{pet_per_lvl_DMG}", "" + petObj2.getLevelDMG())
                        .replace("{base_DMG}", "" + petObj2.getStarterDMG())
                        .replace("{pet_star_display}", petStarDisplay.toString())
                        .replace("{power_rating}", "**" + petPower + "**")
                        .replace("{pet_final_HP}", "**" + petHealth + "**")
                        .replace("{pet_final_DMG}", "**" + petDamage + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));
            }

            if (petResponse2 == null || petResponse1 == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "cmd_attributes_invalid",
                        "You don't have an equipped pet or that pet doesn't exist.")));
                return;
            }

            EmbedBuilder ebPet2 = new EmbedBuilder();
            ebPet2.setTitle(Language.getLocalized(uid, "cmd_attributes_pet_title", capitalizeFully(petName2)));
            ebPet2.setDescription(petResponse2);
            ebPet2.setThumbnail(petSuburl2);
            ebPet2.setColor(petRarityColor(PetRarity.valueOf(petRarityColor2.toUpperCase())));
            ebPet2.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(ebPet2.build());

            EmbedBuilder ebPet1 = new EmbedBuilder();
            ebPet1.setTitle(Language.getLocalized(uid, "cmd_attributes_pet_title", capitalizeFully(petName1)));
            ebPet1.setDescription(petResponse1);
            ebPet1.setThumbnail(petSuburl1);
            ebPet1.setColor(petRarityColor(PetRarity.valueOf(petRarityColor1.toUpperCase())));
            ebPet1.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(ebPet1.build());
            return;
        }

        Rarity rarityColor1 = null;
        Rarity rarityColor2 = null;
        String response1 = null;
        String response2 = null;
        String suburl1 = "https://thewithering.com/";
        String suburl2 = "https://thewithering.com/";
        String itemName1 = "";
        String itemName2 = "";
        int level2 = ItemLevelingUtil.getItemLevel(uid, itemID1);
        int stars2 = ItemStarringUtil.getItemStar(uid, itemID1);
        int level1 = ItemLevelingUtil.getItemLevel(uid, Long.parseLong(itemID));
        int stars1 = ItemStarringUtil.getItemStar(uid, Long.parseLong(itemID));
        int ad1 = 0; // Attribute Dodge
        int ad2 = 0;
        int ap1 = 0; // Attribute Protection
        int ap2 = 0;
        int agc = 0; // Attribute Gold Coins
        int axp = 0; // Attribute Exp Gain

        StringBuilder starDisplay1 = new StringBuilder();
        if (stars1 > 0) {
            for (int i = 0; i < stars1; i++) {
                starDisplay1.append(EmoteUtil.getEmoteMention("S_")).append(" ");
            }
            if (stars1 < 7) {
                for (int s = stars1; s < 7; s++) {
                    starDisplay1.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                starDisplay1.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
            }
        }
        StringBuilder starDisplay2 = new StringBuilder();
        if (stars2 > 0) {
            for (int i = 0; i < stars2; i++) {
                starDisplay2.append(EmoteUtil.getEmoteMention("S_")).append(" ");
            }
            if (stars2 < 7) {
                for (int s = stars2; s < 7; s++) {
                    starDisplay2.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                starDisplay2.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
            }
        }


        if (!args[0].equalsIgnoreCase("pet")) {
            if (armor1 != null) {
                String suburl11 = armor1.getName() + ".png";
                suburl1 += capitalizeFully(suburl11).replaceAll(" ", "_");
                String geminfo = ""; //TODO gem info
                int power = Power.getPower(armor1.getRarity()) + Power.getPower(armor1.getItemType());
                ;
                RarityClass rc = Rarities.getRarityClass(armor1.getRarity());
                if (rc != null) {
                    List<Attribute> attributeList = rc.getAttributes();
                    for (Attribute attribute : attributeList) {
                        int dodge1 = attribute.getDodgeChance().getValue1();
                        int dodge2 = attribute.getDodgeChance().getValue2();
                        int p1 = attribute.getProtection().getValue1();
                        int p2 = attribute.getProtection().getValue2();
                        int gc = attribute.getGoldCoins().getValue1();
                        int xp = attribute.getExp().getValue1();
                        if (dodge1 > 0) ad1 += dodge1;
                        if (dodge2 > 0) ad2 += dodge2;
                        if (p1 > 0) ap1 += p1;
                        if (p2 > 0) ap2 += p2;
                        if (gc > 0) agc += gc;
                        if (xp > 0) axp += xp;
                    }
                }
                String goldCoin = "• Gold Rate `+" + agc + "%`\n";
                String exp = "• Exp Rate `+" + axp + "%`\n";
                String protection = "• Protection `+" + ap1 + " - " + ap2 + "`\n";
                String dodge = "• Dodge `+" + ad1 + " - " + ad2 + "`\n";
                if (armor1.getRarity() == JUNK) {
                    goldCoin = "";
                    exp = "";
                    protection = "";
                    dodge = "";
                }
                if (armor1.getRarity() == COMMON) {
                    goldCoin = "";
                    exp = "";
                }
                if (armor1.getRarity() == UNCOMMON) {
                    exp = "";
                }
                itemName1 = armor1.getName();
                rarityColor1 = armor1.getRarity();
                response1 = Language.getLocalized(uid, "cmd_attributes_armor_bonus1",
                                "LVL {item_level}" + "\n\n" +
                                        "PROT\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        protection +
                                        dodge +
                                        goldCoin +
                                        exp +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay1.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(armor1.getProtectionFrom(), level1) + getStarsProtection(armor1.getProtectionFrom(), stars1) + armor1.getProtectionFrom())
                                + " - " + (int) (getLevelProtection(armor1.getProtectionTo(), level1) + getStarsProtection(armor1.getProtectionTo(), stars1) + armor1.getProtectionTo()) + "**")
                        .replace("{item_level}", "**" + level1 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (arrow1 != null) {
                String suburl11 = arrow1.getName() + ".png";
                suburl1 += capitalizeFully(suburl11).replaceAll(" ", "_");
                int power = Power.getPower(arrow1);
                String geminfo = "";
                int bonusDamage = Fight.round(arrow1.getDamage(), Fight.getRarityDamage(arrow1.getRarity()));
                String bonuses = "• Damage: `+" + bonusDamage + "`\n";
                if (arrow1.getRarity() == Rarity.JUNK) bonuses = "";
                itemName1 = arrow1.getName();
                rarityColor1 = arrow1.getRarity();
                response1 = Language.getLocalized(uid, "cmd_attributes_arrow_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "DMG\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay1.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(arrow1.getDamage(), level1) + getStarsProtection(arrow1.getDamage(), stars1) + arrow1.getDamage()) + "**")
                        .replace("{item_level}", "**" + level1 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (shield1 != null) {
                int power = Power.getPower(shield1);
                String suburl11 = shield1.getName() + ".png";
                suburl1 += capitalizeFully(suburl11).replaceAll(" ", "_");
                String geminfo = "";
                int bonusProtection = Fight.round(shield1.getProtection(), Fight.getRarityProtection(shield1.getRarity()));
                String bonuses = "• Protection: `+" + bonusProtection + "`\n";
                if (shield1.getRarity() == Rarity.JUNK) bonuses = "";

                itemName1 = shield1.getName();
                rarityColor1 = shield1.getRarity();
                response1 = Language.getLocalized(uid, "cmd_attributes_shield_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "PROT\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay1.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(shield1.getProtection(), level1) + getStarsProtection(shield1.getProtection(), stars1) + shield1.getProtection()) + "**")
                        .replace("{item_level}", "**" + level1 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (weapon1 != null) {
                String suburl11 = weapon1.getName() + ".png";
                suburl1 += capitalizeFully(suburl11).replaceAll(" ", "_");
                int power = Power.getPower(weapon1.getRarity()) + Power.getPower(weapon1.getItemType()) + Power.getPowerWeapon(weapon1);
                ;
                String geminfo = "";
                int bonusDamage = Fight.round(weapon1.getDamageMiddle(),
                        Fight.getRarityDamage(weapon1.getRarity()));
                String bonuses = "• Damage: `+" + bonusDamage + "`\n";
                if (weapon1.getRarity() == Rarity.JUNK) bonuses = "";
                itemName1 = weapon1.getName();
                rarityColor1 = weapon1.getRarity();
                response1 = Language.getLocalized(uid, "cmd_attributes_weapon_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "DMG\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay1.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(weapon1.getDamageFrom(), level1) + getStarsProtection(weapon1.getDamageFrom(), stars1) + weapon1.getDamageFrom())
                                + " - " + (int) (getLevelProtection(weapon1.getDamageTo(), level1) + getStarsProtection(weapon1.getDamageTo(), stars1) + weapon2.getDamageTo()) + "**")
                        .replace("{item_level}", "**" + level1 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));
            }


            if (armor2 != null) {
                String suburl22 = armor2.getName() + ".png";
                suburl2 += capitalizeFully(suburl22).replaceAll(" ", "_");
                String geminfo = ""; //TODO gem info
                int power = Power.getPower(armor2.getRarity()) + Power.getPower(armor2.getItemType());
                ;
                RarityClass rc = Rarities.getRarityClass(armor2.getRarity());
                if (rc != null) {
                    List<Attribute> attributeList = rc.getAttributes();
                    for (Attribute attribute : attributeList) {
                        int dodge1 = attribute.getDodgeChance().getValue1();
                        int dodge2 = attribute.getDodgeChance().getValue2();
                        int p1 = attribute.getProtection().getValue1();
                        int p2 = attribute.getProtection().getValue2();
                        int gc = attribute.getGoldCoins().getValue1();
                        int xp = attribute.getExp().getValue1();
                        if (dodge1 > 0) ad1 += dodge1;
                        if (dodge2 > 0) ad2 += dodge2;
                        if (p1 > 0) ap1 += p1;
                        if (p2 > 0) ap2 += p2;
                        if (gc > 0) agc += gc;
                        if (xp > 0) axp += xp;
                    }
                }
                String goldCoin = "• Gold Rate `+" + agc + "%`\n";
                String exp = "• Exp Rate `+" + axp + "%`\n";
                String protection = "• Protection `+" + ap1 + " - " + ap2 + "`\n";
                String dodge = "• Dodge `+" + ad1 + " - " + ad2 + "`\n";
                if (armor2.getRarity() == JUNK) {
                    goldCoin = "";
                    exp = "";
                    protection = "";
                    dodge = "";
                }
                if (armor2.getRarity() == COMMON) {
                    goldCoin = "";
                    exp = "";
                }
                if (armor2.getRarity() == UNCOMMON) {
                    exp = "";
                }
                itemName2 = armor2.getName();
                rarityColor2 = armor2.getRarity();
                response2 = Language.getLocalized(uid, "cmd_attributes_armor_bonus2",
                                "LVL {item_level}" + "\n\n" +
                                        "PROT\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        protection +
                                        dodge +
                                        goldCoin +
                                        exp +
                                        "\n[ID: " + itemID1 + "]")
                        .replace("{star_display}", starDisplay2.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(armor2.getProtectionFrom(), level2) + getStarsProtection(armor2.getProtectionFrom(), stars2) + armor2.getProtectionFrom())
                                + " - " + (int) (getLevelProtection(armor2.getProtectionTo(), level2) + getStarsProtection(armor2.getProtectionTo(), stars2) + armor2.getProtectionTo()) + "**")
                        .replace("{item_level}", "**" + level2 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (arrow2 != null) {
                String suburl22 = arrow2.getName() + ".png";
                suburl2 += capitalizeFully(suburl22).replaceAll(" ", "_");
                int power = Power.getPower(arrow2);
                String geminfo = "";
                int bonusDamage = Fight.round(arrow2.getDamage(), Fight.getRarityDamage(arrow2.getRarity()));
                String bonuses = "• Damage: `+" + bonusDamage + "`\n";
                if (arrow2.getRarity() == Rarity.JUNK) bonuses = "";
                itemName2 = arrow2.getName();
                rarityColor2 = arrow2.getRarity();
                response2 = Language.getLocalized(uid, "cmd_attributes_arrow_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "DMG\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID1 + "]")
                        .replace("{star_display}", starDisplay2.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(arrow2.getDamage(), level2) + getStarsProtection(arrow2.getDamage(), stars2) + arrow2.getDamage()) + "**")
                        .replace("{item_level}", "**" + level2 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (shield2 != null) {
                int power = Power.getPower(shield2);
                String suburl22 = shield2.getName() + ".png";
                suburl2 += capitalizeFully(suburl22).replaceAll(" ", "_");
                String geminfo = "";
                int bonusProtection = Fight.round(shield2.getProtection(), Fight.getRarityProtection(shield2.getRarity()));
                String bonuses = "• Protection: `+" + bonusProtection + "`\n";
                if (shield2.getRarity() == Rarity.JUNK) bonuses = "";

                itemName2 = shield2.getName();
                rarityColor2 = shield2.getRarity();
                response2 = Language.getLocalized(uid, "cmd_attributes_shield_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "PROT\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID1 + "]")
                        .replace("{star_display}", starDisplay2.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(shield2.getProtection(), level2) + getStarsProtection(shield2.getProtection(), stars2) + shield2.getProtection()) + "**")
                        .replace("{item_level}", "**" + level2 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (weapon2 != null) {
                String suburl22 = weapon2.getName() + ".png";
                suburl2 += capitalizeFully(suburl22).replaceAll(" ", "_");
                int power = Power.getPower(weapon2.getRarity()) + Power.getPower(weapon2.getItemType()) + Power.getPowerWeapon(weapon2);
                ;
                String geminfo = "";
                int bonusDamage = Fight.round(weapon2.getDamageMiddle(),
                        Fight.getRarityDamage(weapon2.getRarity()));
                String bonuses = "• Damage: `+" + bonusDamage + "`\n";
                if (weapon2.getRarity() == Rarity.JUNK) bonuses = "";
                itemName2 = weapon2.getName();
                rarityColor2 = weapon2.getRarity();
                response2 = Language.getLocalized(uid, "cmd_attributes_weapon_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "DMG\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID1 + "]")
                        .replace("{star_display}", starDisplay2.toString())
                        .replace("{base_amount}", "**" + (int) (getLevelProtection(weapon2.getDamageFrom(), level2) + getStarsProtection(weapon2.getDamageFrom(), stars2) + weapon2.getDamageFrom())
                                + " - " + (int) (getLevelProtection(weapon2.getDamageTo(), level2) + getStarsProtection(weapon2.getDamageTo(), stars2) + weapon2.getDamageTo()) + "**")
                        .replace("{item_level}", "**" + level2 + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));
            }

            if (response2 == null || response1 == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "cmd_attributes_invalid",
                        "You don't have any equipped armor of that type or that type doesn't exist.")));
                return;
            }

            EmbedBuilder eb2 = new EmbedBuilder();
            if (ItemLockingUtil.getItemLockValue(uid, Long.parseLong(String.valueOf(itemID1))) == 1) {
                eb2.setAuthor(Language.getLocalized(uid, "cmd_attributes_title", capitalizeFully(itemName2)), null, "https://thewithering.com/Locked.png");
            } else {
                eb2.setTitle(Language.getLocalized(uid, "cmd_attributes_title", capitalizeFully(itemName2)));
            }
            eb2.setDescription(response2);
            eb2.setThumbnail(suburl2);
            eb2.setColor(rarityColor(rarityColor2));
            eb2.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb2.build());

            EmbedBuilder eb = new EmbedBuilder();
            if (ItemLockingUtil.getItemLockValue(uid, Long.parseLong(itemID)) == 1) {
                eb.setAuthor(Language.getLocalized(uid, "cmd_attributes_title", capitalizeFully(itemName1)), null, "https://thewithering.com/Locked.png");
            } else {
                eb.setTitle(Language.getLocalized(uid, "cmd_attributes_title", capitalizeFully(itemName1)));
            }
            eb.setDescription(response1);
            eb.setThumbnail(suburl1);
            eb.setColor(rarityColor(rarityColor1));
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            return;
        }

        e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "cmd_attributes_invalid",
                "This item is invalid, or invalid usage.")));
    }

    public static long getLevelProtection(int baseProtection, int level) {
        int finalProtection = 0;
        int protection = baseProtection;
        for (int i = 1; i < level + 1; i++) {
            protection *= (1.00725);
        }
        finalProtection = (protection - baseProtection);
        return finalProtection;
    }

    public static long getStarsProtection(int baseProtection, int stars) {
        int finalProtection = 0;
        int protection = baseProtection;
        for (int i = 1; i < stars + 1; i++) {
            protection *= (1.045);
        }
        finalProtection = (protection - baseProtection);
        return finalProtection;
    }

    public Color rarityColor(Rarity rarity) {
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