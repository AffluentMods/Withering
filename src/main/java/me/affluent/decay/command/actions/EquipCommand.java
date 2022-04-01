package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.*;
import me.affluent.decay.data.Power;
import me.affluent.decay.entity.*;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.event.ConquestWinEvent;
import me.affluent.decay.event.EquipEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.util.settingsUtil.EquippedDragonUtil;
import me.affluent.decay.util.settingsUtil.EquippedIronUtil;
import me.affluent.decay.util.settingsUtil.EquippedWitherUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.*;
import net.dv8tion.jda.api.entities.User;

public class EquipCommand extends BotCommand {

    public EquipCommand() {
        this.name = "equip";
        this.aliases = new String[]{"eq"};
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        Player p = Player.getPlayer(uid);
        String[] args = e.getArgs();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "equip <Item ID>`\n" +
                                    "Use `" + userPrefix + "equip pet <Item ID>` for pets.\n" +
                                    "Use `" + userPrefix + "equip inventoryiron | inventorydragon` to equip other inventories")));
            return;
        }
        String petID = "";
        String armorID = "";
        if (args.length < 2) {
            if (args[0].equalsIgnoreCase("inventoryiron") || (args[0].equalsIgnoreCase("inviron") ||
                    (args[0].equalsIgnoreCase("pfliron") || args[0].equalsIgnoreCase("profileiron") || args[0].equalsIgnoreCase("invi")))) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_inventory_equipped", "Equipped"),
                        Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Inventory`!")));
                EquippedIronUtil.getEquippedIronUtil(uid).setIronHelmet("equipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronChestplate("equipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronTrousers("equipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronGloves("equipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronBoots("equipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronShield("equipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronSword("equipped");
                return;
            }
            if (args[0].equalsIgnoreCase("inventorydragon") || (args[0].equalsIgnoreCase("invdragon") ||
                    (args[0].equalsIgnoreCase("pfldragon") || args[0].equalsIgnoreCase("profiledragon") || args[0].equalsIgnoreCase("invd")))) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_inventory_equipped", "Equipped"),
                        Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Inventory`!")));
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonHelmet("equipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonChestplate("equipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonTrousers("equipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonGloves("equipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonBoots("equipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonShield("equipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonSword("equipped");
                return;
            }
            if (args[0].equalsIgnoreCase("inventorywither") || (args[0].equalsIgnoreCase("invwither") ||
                    (args[0].equalsIgnoreCase("pflwither") || args[0].equalsIgnoreCase("profilewither") || args[0].equalsIgnoreCase("invw")))) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_inventory_equipped", "Equipped"),
                        Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Inventory`!")));
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherHelmet("equipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherChestplate("equipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherTrousers("equipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherGloves("equipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherBoots("equipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherShield("equipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherSword("equipped");
                return;
            }
        } else {
            if (args[0].equalsIgnoreCase("inventoryiron") || (args[0].equalsIgnoreCase("inviron") ||
                    (args[0].equalsIgnoreCase("pfliron") || args[0].equalsIgnoreCase("profileiron") || args[0].equalsIgnoreCase("invi")))) {
                if (args[1].equalsIgnoreCase("helmet")) {
                    if (p.getArmorIronUser().getIronHelmet() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi helmet` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronHelmet().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_helmet", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Helmet` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_helmet_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Helmet`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronHelmet("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    if (p.getArmorIronUser().getIronChestplate() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi chestplate` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronChestplate().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_chestplate", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Chestplate` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_chestplate_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Chestplate`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronChestplate("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("gloves")) {
                    if (p.getArmorIronUser().getIronGloves() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi gloves` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronGloves().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_gloves", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Gloves` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_gloves_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Gloves`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronGloves("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("trousers")) {
                    if (p.getArmorIronUser().getIronTrousers() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi trousers` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronTrousers().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_trousers", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Trousers` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_trousers_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Trousers`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronTrousers("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    if (p.getArmorIronUser().getIronBoots() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi boots` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronBoots().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_boots", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Boots` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_boots_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Boots`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronBoots("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    if (p.getArmorIronUser().getIronWeapon() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi sword` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronSword().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_sword", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Sword` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_sword_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Sword`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronSword("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("shield")) {
                    if (p.getArmorIronUser().getIronShield() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi shield` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronShield().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_shield", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Iron Shield` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_shield_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Iron Shield`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronShield("equipped");
                    }
                    return;
                }
            }

            if (args[0].equalsIgnoreCase("inventorydragon") || (args[0].equalsIgnoreCase("invdragon") ||
                    (args[0].equalsIgnoreCase("pfldragon") || args[0].equalsIgnoreCase("profiledragon") || args[0].equalsIgnoreCase("invd")))) {
                if (args[1].equalsIgnoreCase("helmet")) {
                    if (p.getArmorDragonUser().getDragonHelmet() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd helmet` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonHelmet().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_helmet", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Helmet` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_helmet_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Helmet`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonHelmet("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    if (p.getArmorDragonUser().getDragonChestplate() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd chestplate` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonChestplate().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_chestplate", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Chestplate` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_chestplate_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Chestplate`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonChestplate("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("gloves")) {
                    if (p.getArmorDragonUser().getDragonGloves() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd gloves` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonGloves().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_gloves", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Gloves` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_gloves_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Gloves`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonGloves("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("trousers")) {
                    if (p.getArmorDragonUser().getDragonTrousers() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd trousers` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonTrousers().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_trousers", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Trousers` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_trousers_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Trousers`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonTrousers("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    if (p.getArmorDragonUser().getDragonBoots() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd boots` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonBoots().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_boots", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Boots` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_boots_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Boots`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonBoots("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    if (p.getArmorDragonUser().getDragonWeapon() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd sword` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonSword().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_sword", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Sword` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_sword_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Sword`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonSword("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("shield")) {
                    if (p.getArmorDragonUser().getDragonShield() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd shield` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonShield().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_shield", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Dragon Shield` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_shield_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Dragon Shield`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonShield("equipped");
                    }
                    return;
                }
            }

            if (args[0].equalsIgnoreCase("inventorywither") || (args[0].equalsIgnoreCase("invwither") ||
                    (args[0].equalsIgnoreCase("pflwither") || args[0].equalsIgnoreCase("profilewither") || args[0].equalsIgnoreCase("invw")))) {
                if (args[1].equalsIgnoreCase("helmet")) {
                    if (p.getArmorWitherUser().getWitherHelmet() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw helmet` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherHelmet().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_helmet", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Helmet` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_helmet_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Helmet`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherHelmet("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    if (p.getArmorWitherUser().getWitherChestplate() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw chestplate` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherChestplate().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_chestplate", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Chestplate` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_chestplate_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Chestplate`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherChestplate("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("gloves")) {
                    if (p.getArmorWitherUser().getWitherGloves() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw gloves` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherGloves().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_gloves", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Gloves` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_gloves_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Gloves`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherGloves("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("trousers")) {
                    if (p.getArmorWitherUser().getWitherTrousers() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw trousers` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherTrousers().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_trousers", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Trousers` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_trousers_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Trousers`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherTrousers("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    if (p.getArmorWitherUser().getWitherBoots() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw boots` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherBoots().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_boots", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Boots` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_boots_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Boots`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherBoots("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    if (p.getArmorWitherUser().getWitherWeapon() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw sword` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherSword().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_sword", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Sword` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_sword_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Sword`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherSword("equipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("shield")) {
                    if (p.getArmorWitherUser().getWitherShield() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw shield` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherShield().equalsIgnoreCase("equipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_shield", "Equipped"),
                                Language.getLocalized(uid, "already_equipped", "The `Wither Shield` is already equipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_shield_equipped", "Equipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully equipped `Wither Shield`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherShield("equipped");
                    }
                    return;
                }
            }
        }
       if (args[0].equalsIgnoreCase("pet")) {
           petID = args[1];
        } else {
            armorID = args[0];
        }
        InventoryUser inv = p.getInventoryUser();

       int armorID1 = 0;
       if (!args[0].equalsIgnoreCase("pet")) {
           try {
               armorID1 = Integer.parseInt(armorID);
           } catch (NumberFormatException ex) {
               e.reply(MessageUtil.err(Constants.ERROR(uid),
                       Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                               .replace("{argument}", "`<Item ID>`")));
               return;
           }
       }

        int petID1 = 0;
        if (args[0].equalsIgnoreCase("pet")) {
            try {
                petID1 = Integer.parseInt(petID);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                                .replace("{argument}", "`<Item ID>`")));
                return;
            }

            PetUtil petInv = p.getPetUtil();
            Pets pets = PetUtil.getPetByID(petID1);

            if (pets == null || !pets.getPetOwner().equals(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_inventory", "You don't have {pet}!")
                                .replace("{pet}", pets == null ? "this pet" : "`" + pets.getPetName().replace("_", "") + "`")));
                return;
            }

            String petName = pets.getPetName().toLowerCase().replace("_", " ");
            //WordUtils.capitalizeFully(petName);
            PetUser petUser = PetUser.getPetUser(uid);
            PetItem petItem = PetItem.getPetItem(petName);
            PetItem petItemNow = petUser.getPet();
            String petItemNowID = petUser.getPetID();
            boolean equipped = false;
            if (petItem != null) {
                if (petItemNow != null) petInv.addPetOld(Long.parseLong(petItemNowID), petItemNow.getName());
                petUser.updatePet(Long.valueOf(petID), petItem);
                equipped = true;
            }

            if (equipped) {
                petInv.removePet(pets.getPetID());
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "equipped_plain", "Equipped"),
                            Language.getLocalized(uid, "equipped_pet",
                                            "Successfully equipped {equipped_pet}!")
                                    .replace("{equipped_pet}", petName.replace("_", " ").toLowerCase() + "" + EmoteUtil.getEmoteMention(petName))));
                return;
            }
            e.reply(MessageUtil
                    .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
            return;
        }

        Item item = InventoryUser.getItemByID(armorID1);

        if (item == null || !item.getOwner().equals(uid)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                            .replace("{item}", item == null ? "this item" : "`" + item.getName() + "`")));
            return;
        }

        String armor = item.getName();
        ItemType it = item.getType();
        int level = p.getExpUser().getLevel();
        if (it != null) {
            if (it.getLevelRequirement() > level) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "equip_req_higher_level",
                        "You are not worthy of that item. You need to be at least level {level} to equip {type} items!")
                        .replace("{level}", "`" + it.getLevelRequirement() + "`")
                        .replace("{type}", it.name().toLowerCase())));
                return;
            }
        }
        Rarity rarity = item.getRarity();
        if (rarity == null) {
            e.reply(MessageUtil.err("Error", "Odd, this hath no rarity. Please contact support."));
            return;
        }
        boolean equipped = false;
        ArmorUser au = p.getArmorUser();
        Helmet helmet = Helmet.getHelmet(armor);
        Helmet helmetNow = au.getHelmet();
        String helmetNowID = au.getHelmetID();
        if (helmet != null) {
            if (helmetNow != null) inv.addItem(Long.parseLong(helmetNowID), helmetNow.getName().toLowerCase());
            au.updateHelmet(item.getID(), helmet);
            equipped = true;
        }
        Chestplate chestplate = Chestplate.getChestplate(armor);
        Chestplate chestplateNow = au.getChestplate();
        String chestplateNowID = au.getChestplateID();
        if (chestplate != null) {
            if (chestplateNow != null) inv.addItem(Long.parseLong(chestplateNowID), chestplateNow.getName().toLowerCase());
            au.updateChestplate(item.getID(), chestplate);
            equipped = true;
        }
        Gloves gloves = Gloves.getGloves(armor);
        Gloves glovesNow = au.getGloves();
        String glovesNowID = au.getGlovesID();
        if (gloves != null) {
            if (glovesNow != null) inv.addItem(Long.parseLong(glovesNowID), glovesNow.getName().toLowerCase());
            au.updateGloves(item.getID(), gloves);
            equipped = true;
        }
        Trousers trousers = Trousers.getTrousers(armor);
        Trousers trousersNow = au.getTrousers();
        String trousersNowID = au.getTrousersID();
        if (trousers != null) {
            if (trousersNow != null) inv.addItem(Long.parseLong(trousersNowID), trousersNow.getName().toLowerCase());
            au.updateTrousers(item.getID(), trousers);
            equipped = true;
        }
        Boots boots = Boots.getBoots(armor);
        Boots bootsNow = au.getBoots();
        String bootsNowID = au.getBootsID();
        if (boots != null) {
            if (bootsNow != null) inv.addItem(Long.parseLong(bootsNowID), bootsNow.getName().toLowerCase());
            au.updateBoots(item.getID(), boots);
            equipped = true;
        }

        Weapon weapon = Weapon.getWeapon(armor);
        Weapon weaponNow = au.getWeapon();
        String weaponNowID = au.getWeaponID();
        if (weapon != null) {
            if (weapon instanceof Bow) {
                if (!inv.hasArrows(level)) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_arrows",
                            "You can't equip a bow!\nYou don't have any arrows (or you can not equip them)!")));
                    return;
                }
                if (weaponNow != null) inv.addItem(Long.parseLong(weaponNowID), weaponNow.getName().toLowerCase());
                au.updateWeapon(item.getID(), weapon);
                Arrow arrowNow = au.getArrow();
                String arrowNowID = au.getArrowID();
                if (arrowNow != null) inv.addItem(Long.parseLong(arrowNowID), arrowNow.getName().toLowerCase());
                au.updateArrow(0L, null);
                Shield shieldNow = au.getShield();
                String shieldNowID = au.getShieldID();
                if (shieldNow != null) inv.addItem(Long.parseLong(shieldNowID), shieldNow.getName().toLowerCase());
                au.updateShield(0L, null);
                equipped = true;
                updateArrows(inv, au, level);

            } else if (weapon instanceof Sword) {
                if (weaponNow != null) inv.addItem(Long.parseLong(weaponNowID), weaponNow.getName().toLowerCase());
                au.updateWeapon(item.getID(), weapon);
                Shield shieldNow = au.getShield();
                String shieldNowID = au.getShieldID();
                if (shieldNow != null) inv.addItem(Long.parseLong(shieldNowID), shieldNow.getName().toLowerCase());
                au.updateShield(0L, null);
                Arrow arrowNow = au.getArrow();
                String arrowNowID = au.getArrowID();
                if (arrowNow != null) inv.addItem(Long.parseLong(arrowNowID), arrowNow.getName().toLowerCase());
                au.updateArrow(0L, null);
                equipped = true;
                updateShield(inv, au, level);

            } else if (weapon instanceof Staff) {
                if (weaponNow != null) inv.addItem(Long.parseLong(weaponNowID), weaponNow.getName().toLowerCase());
                au.updateWeapon(item.getID(), weapon);
                Shield shieldNow = au.getShield();
                String shieldNowID = au.getShieldID();
                if (shieldNow != null) inv.addItem(Long.parseLong(shieldNowID), shieldNow.getName().toLowerCase());
                au.updateShield(0L, null);
                Arrow arrowNow = au.getArrow();
                String arrowNowID = au.getArrowID();
                if (arrowNow != null) inv.addItem(Long.parseLong(arrowNowID), arrowNow.getName().toLowerCase());
                au.updateArrow(0L, null);
                equipped = true;
            }
        }
        Arrow arrow = Arrow.getArrow(armor);
        Arrow arrowNow = au.getArrow();
        String arrowNowID = au.getArrowID();
        if (arrow != null) {
            if (weaponNow instanceof Bow) {
                if (arrowNow != null) inv.addItem(Long.parseLong(arrowNowID), arrowNow.getName().toLowerCase());
                au.updateArrow(item.getID(), arrow);
                equipped = true;
            } else {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_bow_equipped",
                        "You can't equip {arrow_item}, because you don't have a bow equipped!")
                        .replace("{arrow_item}", arrow.getDisplay())));
                return;
            }
        }
        Shield shield = Shield.getShield(armor);
        Shield shieldNow = au.getShield();
        String shieldNowID = au.getShieldID();
        if (shield != null) {
            if (weaponNow instanceof Sword) {
                if (shieldNow != null) inv.addItem(Long.parseLong(shieldNowID), shieldNow.getName().toLowerCase());
                au.updateShield(item.getID(), shield);
                equipped = true;
            } else {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_sword_equipped",
                        "You can't equip {shield_item}, because you don't have a sword equipped!")
                        .replace("{shield_item}", shield.getDisplay())));
                return;
            }
        }
        if (equipped) {
            inv.removeItem(item.getID());
            EquipEvent equipEvent = new EquipEvent(p);
            EventManager.callEvent(equipEvent);
            if (weapon instanceof Bow) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "equipped_plain", "Equipped"),
                        Language.getLocalized(uid, "equipped_bow",
                                "Successfully equipped {equipped_bow} and automatically equipped {equipped_arrow}!")
                                .replace("{equipped_bow}", weapon.getDisplay())
                                .replace("{equipped_arrow}", au.getArrow().getDisplay())));
            } else if (weapon instanceof Sword) {
                if (au.getShield() != null) {
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "equipped_plain", "Equipped"),
                            Language.getLocalized(uid, "equipped_sword",
                                            "Successfully equipped {equipped_sword} and automatically equipped {equipped_shield}!")
                                    .replace("{equipped_sword}", weapon.getDisplay())
                                    .replace("{equipped_shield}", au.getShield().getDisplay())));
                } else {
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "equipped_plain", "Equipped"),
                            Language.getLocalized(uid, "equipped_sword",
                                            "Successfully equipped {equipped_sword}!")
                                    .replace("{equipped_sword}", weapon.getDisplay())));
                }
            } else {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "equipped_plain", "Equipped"),
                        Language.getLocalized(uid, "equipped", "Successfully equipped " + item.getEmote() + " {equipped_armor}!")
                                .replace("{equipped_armor}", item.getName())));
            }
            return;
        }
        e.reply(MessageUtil
                .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
    }

    private void updateArrows(InventoryUser inv, ArmorUser au, int level) {
        Arrow arrow = null;
        long itemID = -1L;
        for (long ID : inv.getItems().keySet()) {
            String itemName = inv.getItems().get(ID);
            if (itemName.toLowerCase().endsWith("arrow")) {
                Arrow newArrow = Arrow.getArrow(itemName.toLowerCase());
                if (newArrow == null) continue;
                ItemType it = newArrow.getItemType();
                if (it.getLevelRequirement() <= level) {
                    if (arrow != null) {
                        if (Power.getPower(newArrow) > Power.getPower(arrow)) {
                            //if (A.getFullDamage(newArrow) > A.getFullDamage(arrow)) {
                            arrow = newArrow;
                            itemID = ID;
                        }
                    } else {
                        arrow = newArrow;
                        itemID = ID;
                    }
                }
            }
        }
        if (arrow != null && itemID != -1L) {
            inv.removeItem(itemID);
            au.updateArrow(itemID, arrow);
        }
    }
    private void updateShield(InventoryUser inv, ArmorUser au, int level) {
        Shield shield = null;
        long itemId = -1L;
        for (long ID : inv.getItems().keySet()) {
            String itemName = inv.getItems().get(ID);
            if (itemName.toLowerCase().endsWith("shield")) {
                Shield newShield = Shield.getShield(itemName.toLowerCase());
                if (newShield == null) continue;
                ItemType it = newShield.getItemType();
                if (it.getLevelRequirement() <= level) {
                    if (shield != null) {
                        if (Power.getPower(newShield) > Power.getPower(shield)) {
                            shield = newShield;
                            itemId = ID;
                        }
                    } else {
                        shield = newShield;
                        itemId = ID;
                    }
                }
            }
        }
        if (shield != null && itemId != -1L) {
            inv.removeItem(itemId);
            au.updateShield(itemId, shield);
        }
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