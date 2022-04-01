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

public class AttributesCommand extends BotCommand {

    public AttributesCommand() {
        this.name = "attributes";
        this.aliases = new String[]{"att", "attribute"};
        this.cooldown = 0.75;
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
                            .replace("{command_usage}", "`" + userPrefix + "attributes <equipped gear | item ID>`\n" +
                                    "Use `" + userPrefix + "attributes pet [Pet ID]` for pets.")));
            return;
        }
        Player p = Player.getPlayer(u.getId());
        PetUser petUser = p.getPetUser();
        ArmorUser armorUser = p.getArmorUser();
        String type = args[0].toLowerCase();
        if (args[0].equalsIgnoreCase("pet") && args.length != 1) {
            type = args[1].toLowerCase();
        }
        Armor armor = null;
        Weapon weapon = null;
        Arrow arrow = null;
        Shield shield = null;
        PetItem petObj = null;
        Pets petItem = null;
        String petName = null;
        long petID = 0;
        long itemID = 0;
        if (args[0].equalsIgnoreCase("pet") && args.length == 1) {
            petObj = petUser.getPet();
            petID = Long.parseLong(petUser.getPetID());
            petItem = PetUtil.getEQPetByID(petID);
        }
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
        if (type.equalsIgnoreCase("sword") || type.equalsIgnoreCase("bow") || type.equalsIgnoreCase("staff") ||type.equalsIgnoreCase("weapon")) {
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

        if (itemID == 0 && !args[0].equalsIgnoreCase("pet")) {
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
            } catch (NumberFormatException ignored) {
            }
        }

        if (petID == 0 && args[0].equalsIgnoreCase("pet")) {
            try {
                petID = Long.parseLong(type);
                petItem = PetUtil.getPetByID(petID);
                if (petItem != null) {
                    String pi = petItem.getPetName().toLowerCase().replace("_", " ");
                    petObj = PetItem.getPetItem(pi);
                }
            } catch (NumberFormatException ignored) {
            }
        }

            String petResponse = null;
            String petRarityColor = "";
            String petSuburl = "https://thewithering.com/";
            if (args[0].equalsIgnoreCase("pet")) {
                if (petObj != null) {
                    petName = petItem.getPetName().toLowerCase().replace("_", " ");
                    String[] petColor = petItem.getPetName().replaceAll("_", " ").split(" ");
                    petRarityColor = petColor[0];
                    int petStars = PetStarringUtil.getPetStar(p.getUserId(), petID);
                    int petLevel = petItem.getPetLevel(uid, String.valueOf(petID));
                    BigInteger petExp = petItem.getPetExp(uid, String.valueOf(petID));
                    double maxPetExp = PetXPApi.getNeededPetXP(petObj, petLevel);
                    boolean xpMaxed = p.getPetExpUser().isPetMaxed(petObj, uid, Math.toIntExact(petID));
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
                    petHealth += (petObj.getLevelHP() * petLevel);
                    petHealth += petObj.getStarterHP();
                    int petDamage = 0;
                    petDamage += (petObj.getLevelDMG() * petLevel);
                    petDamage += petObj.getStarterDMG();
                    if (petStars > 0) {
                        petHealth *= ((double) (petStars * 5) / 100) + 1;
                        petDamage *= ((double) (petStars * 5) / 100) + 1;
                    }
                    if (petLevel == petObj.getMaxLevel()) {
                        petDamage *= 1.15;
                        petHealth *= 1.15;
                    }
                    petSuburl += petName.replaceAll(" ", "_").toUpperCase() + ".png";
                    int petPower = Power.getFullPetPower(p, petObj, petLevel, petStars);
                    petResponse = Language.getLocalized(uid, "cmd_attributes_pet",
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
                            .replace("{base_HP}", "" + petObj.getStarterHP())
                            .replace("{pet_per_lvl_HP}", "" + petObj.getLevelHP())
                            .replace("{pet_per_lvl_DMG}", "" + petObj.getLevelDMG())
                            .replace("{base_DMG}", "" + petObj.getStarterDMG())
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
                if (petResponse == null) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "cmd_attributes_invalid",
                            "You don't have an equipped pet or that pet doesn't exist.")));
                    return;
                }

                EmbedBuilder ebPet = new EmbedBuilder();
                ebPet.setTitle(Language.getLocalized(uid, "cmd_attributes_pet_title", capitalizeFully(petName)));
                ebPet.setDescription(petResponse);
                ebPet.setThumbnail(petSuburl);
                ebPet.setColor(petRarityColor(PetRarity.valueOf(petRarityColor.toUpperCase())));
                ebPet.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
                e.reply(ebPet.build());
                return;
            }

            Rarity rarityColor = null;
            String response = null;
            String suburl = "https://thewithering.com/";
            String itemName = "";
            int level = ItemLevelingUtil.getItemLevel(uid, itemID);
            int stars = ItemStarringUtil.getItemStar(uid, itemID);
            int ad1 = 0; // Attribute Dodge
            int ad2 = 0;
            int ap1 = 0; // Attribute Protection
            int ap2 = 0;
            int agc = 0; // Attribute Gold Coins
            int axp = 0; // Attribute Exp Gain
            StringBuilder starDisplay = new StringBuilder();
            if (stars > 0) {
                for (int i = 0; i < stars; i++) {
                    starDisplay.append(EmoteUtil.getEmoteMention("S_")).append(" ");
                }
                if (stars < 7) {
                    for (int s = stars; s < 7; s++) {
                        starDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                    }
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    starDisplay.append(EmoteUtil.getEmoteMention("E_S")).append(" ");
                }
            }
            if (armor != null) {
                String suburl2 = armor.getName() + ".png";
                suburl += capitalizeFully(suburl2).replaceAll(" ", "_");
                String geminfo = ""; //TODO gem info
                int power = Power.getPower(armor.getRarity()) + Power.getPower(armor.getItemType());
                ;
                RarityClass rc = Rarities.getRarityClass(armor.getRarity());
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
                if (armor.getRarity() == JUNK) {
                    goldCoin = "";
                    exp = "";
                    protection = "";
                    dodge = "";
                }
                if (armor.getRarity() == COMMON) {
                    goldCoin = "";
                    exp = "";
                }
                if (armor.getRarity() == UNCOMMON) {
                    exp = "";
                }
                itemName = armor.getName();
                rarityColor = armor.getRarity();
                int armor1 = (int) ((getLevelProtection(armor.getProtectionFrom(), level) + getStarsProtection(armor.getProtectionFrom(), stars)) + armor.getProtectionFrom());
                int armor2 = (int) ((getLevelProtection(armor.getProtectionTo(), level) + getStarsProtection(armor.getProtectionTo(), stars)) + armor.getProtectionTo());
                response = Language.getLocalized(uid, "cmd_attributes_armor_bonus",
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
                        .replace("{star_display}", starDisplay.toString())
                        .replace("{base_amount}", "**" + armor1 + " - " + armor2 + "**")
                        .replace("{item_level}", "**" + level + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (arrow != null) {
                String suburl2 = arrow.getName() + ".png";
                suburl += capitalizeFully(suburl2).replaceAll(" ", "_");
                int power = Power.getPower(arrow);
                String geminfo = "";
                int bonusDamage = Fight.round(arrow.getDamage(), Fight.getRarityDamage(arrow.getRarity()));
                String bonuses = "• Damage: `+" + bonusDamage + "`\n";
                if (arrow.getRarity() == Rarity.JUNK) bonuses = "";
                itemName = arrow.getName();
                rarityColor = arrow.getRarity();
                int dmg = (int) ((getLevelProtection(arrow.getDamage(), level) + getStarsProtection(arrow.getDamage(), stars)) + arrow.getDamage());
                response = Language.getLocalized(uid, "cmd_attributes_arrow_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "DMG\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay.toString())
                        .replace("{base_amount}", "**" + dmg + "**")
                        .replace("{item_level}", "**" + level + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (shield != null) {
                int power = Power.getPower(shield);
                String suburl2 = shield.getName() + ".png";
                    suburl += capitalizeFully(suburl2).replaceAll(" ", "_");
                String geminfo = "";
                int bonusProtection = Fight.round(shield.getProtection(), Fight.getRarityProtection(shield.getRarity()));
                String bonuses = "• Protection: `+" + bonusProtection + "`\n";
                if (shield.getRarity() == Rarity.JUNK) bonuses = "";
                int prot = (int) (getLevelProtection(shield.getProtection(), level) + getStarsProtection(shield.getProtection(), stars)) + shield.getProtection();
                itemName = shield.getName();
                rarityColor = shield.getRarity();
                response = Language.getLocalized(uid, "cmd_attributes_shield_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "PROT\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay.toString())
                        .replace("{base_amount}", "**" + prot + "**")
                        .replace("{item_level}", "**" + level + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));

            } else if (weapon != null) {
                String suburl2 = weapon.getName() + ".png";
                suburl += capitalizeFully(suburl2).replaceAll(" ", "_");
                int power = Power.getPower(weapon.getRarity()) + Power.getPower(weapon.getItemType()) + Power.getPowerWeapon(weapon);
                String geminfo = "";
                int bonusDamage = Fight.round(weapon.getDamageMiddle(),
                        Fight.getRarityDamage(weapon.getRarity()));
                String bonuses = "• Damage: `+" + bonusDamage + "`\n";
                if (weapon.getRarity() == Rarity.JUNK) bonuses = "";
                itemName = weapon.getName();
                rarityColor = weapon.getRarity();
                int dmg1 = (int) ((getLevelProtection(weapon.getDamageFrom(), level) + getStarsProtection(weapon.getDamageFrom(), stars)) + weapon.getDamageFrom());
                int dmg2 = (int) ((getLevelProtection(weapon.getDamageTo(), level) + getStarsProtection(weapon.getDamageTo(), stars)) + weapon.getDamageTo());
                response = Language.getLocalized(uid, "cmd_attributes_weapon_bonus",
                                "LVL {item_level}" + "\n\n" +
                                        "DMG\n" +
                                        "{base_amount}\n" +
                                        "{star_display}\n" +
                                        "{divider}\n" +
                                        "{power_rating} Power\n" +
                                        bonuses +
                                        "\n[ID: " + itemID + "]")
                        .replace("{star_display}", starDisplay.toString())
                        .replace("{base_amount}", "**" + dmg1 + " - " + dmg2 + "**")
                        .replace("{item_level}", "**" + level + "**")
                        .replace("{power_rating}", "**" + power + "**")
                        .replace("{divider}", EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider")
                                + EmoteUtil.getEmoteMention("att_divider") + EmoteUtil.getEmoteMention("att_divider"));
            }

            if (response == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "cmd_attributes_invalid",
                        "You don't have any equipped armor of that type or that type doesn't exist.")));
                return;
            }
            EmbedBuilder eb = new EmbedBuilder();
            if (ItemLockingUtil.getItemLockValue(uid, itemID) == 1) {
                eb.setAuthor(Language.getLocalized(uid, "cmd_attributes_title", capitalizeFully(itemName)), null, "https://thewithering.com/Locked.png");
            } else {
                eb.setTitle(Language.getLocalized(uid, "cmd_attributes_title", capitalizeFully(itemName)));
            }
            eb.setDescription(response);
            eb.setThumbnail(suburl);
            eb.setColor(rarityColor(rarityColor));
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
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