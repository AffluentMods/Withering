package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.entity.*;
import me.affluent.decay.event.Event;
import me.affluent.decay.event.UnequipEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.util.settingsUtil.EquippedDragonUtil;
import me.affluent.decay.util.settingsUtil.EquippedIronUtil;
import me.affluent.decay.util.settingsUtil.EquippedWitherUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class UnequipCommand extends BotCommand {

    public UnequipCommand() {
        this.name = "unequip";
        this.aliases = new String[]{"uneq", "unq", "ueq"};
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
        if (args.length == 0) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.\n" +
                                    "If you wish to unequip other inventories use `" + userPrefix + "unequip <inventoryiron | inventorydragon>`")
                            .replace("{command_usage}", "`" + userPrefix + "unequip <helmet | chestplate | gloves | trousers | boots | pet>`")));
            return;
        }
        InventoryUser inv = p.getInventoryUser();
        PetUtil petInv = p.getPetUtil();
        String armor = args[0].toLowerCase();
        ArmorUser au = p.getArmorUser();
        PetUser pu = p.getPetUser();
        Armor armorObj = null;
        PetItem petItem = null;
        String itemId = "";
        if (args.length < 2) {
            if (args[0].equalsIgnoreCase("inventoryiron") || (args[0].equalsIgnoreCase("inviron") ||
                    (args[0].equalsIgnoreCase("pfliron") || args[0].equalsIgnoreCase("profileiron") || args[0].equalsIgnoreCase("invi")))) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_inventory_unequipped", "Unequipped"),
                        Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Inventory`!")));
                EquippedIronUtil.getEquippedIronUtil(uid).setIronHelmet("unequipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronChestplate("unequipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronTrousers("unequipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronGloves("unequipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronBoots("unequipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronShield("unequipped");
                EquippedIronUtil.getEquippedIronUtil(uid).setIronSword("unequipped");
                return;
            }
            if (args[0].equalsIgnoreCase("inventorydragon") || (args[0].equalsIgnoreCase("invdragon") ||
                    (args[0].equalsIgnoreCase("pfldragon") || args[0].equalsIgnoreCase("profiledragon") || args[0].equalsIgnoreCase("invd")))) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_inventory_unequipped", "Unequipped"),
                        Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Inventory`!")));
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonHelmet("unequipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonChestplate("unequipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonTrousers("unequipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonGloves("unequipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonBoots("unequipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonShield("unequipped");
                EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonSword("unequipped");
                return;
            }
            if (args[0].equalsIgnoreCase("inventorywither") || (args[0].equalsIgnoreCase("invwither") ||
                    (args[0].equalsIgnoreCase("pflwither") || args[0].equalsIgnoreCase("profilewither") || args[0].equalsIgnoreCase("invw")))) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_inventory_unequipped", "Unequipped"),
                        Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Inventory`!")));
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherHelmet("unequipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherChestplate("unequipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherTrousers("unequipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherGloves("unequipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherBoots("unequipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherShield("unequipped");
                EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherSword("unequipped");
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
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronHelmet().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_helmet", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Helmet` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_helmet_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Helmet`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronHelmet("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    if (p.getArmorIronUser().getIronChestplate() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi chestplate` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronChestplate().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_chestplate", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Chestplate` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_chestplate_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Chestplate`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronChestplate("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("gloves")) {
                    if (p.getArmorIronUser().getIronGloves() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi gloves` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronGloves().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_gloves", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Gloves` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_gloves_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Gloves`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronGloves("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("trousers")) {
                    if (p.getArmorIronUser().getIronTrousers() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi trousers` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronTrousers().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_trousers", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Trousers` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_trousers_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Trousers`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronTrousers("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    if (p.getArmorIronUser().getIronBoots() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi boots` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronBoots().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_boots", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Boots` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_boots_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Boots`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronBoots("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    if (p.getArmorIronUser().getIronWeapon() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi sword` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronSword().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_sword", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Sword` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_sword_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Sword`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronSword("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("shield")) {
                    if (p.getArmorIronUser().getIronShield() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invi shield` to purchase this")));
                        return;
                    }
                    if (EquippedIronUtil.getEquippedIronUtil(uid).getIronShield().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_shield", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Iron Shield` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "iron_shield_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Iron Shield`!")));
                        EquippedIronUtil.getEquippedIronUtil(uid).setIronShield("unequipped");
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
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonHelmet().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_helmet", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Helmet` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_helmet_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Helmet`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonHelmet("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    if (p.getArmorDragonUser().getDragonChestplate() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd chestplate` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonChestplate().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_chestplate", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Chestplate` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_chestplate_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Chestplate`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonChestplate("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("gloves")) {
                    if (p.getArmorDragonUser().getDragonGloves() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd gloves` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonGloves().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_gloves", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Gloves` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_gloves_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Gloves`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonGloves("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("trousers")) {
                    if (p.getArmorDragonUser().getDragonTrousers() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd trousers` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonTrousers().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_trousers", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Trousers` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_trousers_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Trousers`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonTrousers("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    if (p.getArmorDragonUser().getDragonBoots() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd boots` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonBoots().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_boots", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Boots` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_boots_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Boots`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonBoots("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    if (p.getArmorDragonUser().getDragonWeapon() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd sword` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonSword().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_sword", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Sword` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_sword_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Sword`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonSword("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("shield")) {
                    if (p.getArmorDragonUser().getDragonShield() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invd shield` to purchase this")));
                        return;
                    }
                    if (EquippedDragonUtil.getEquippedDragonUtil(uid).getDragonShield().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_shield", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Dragon Shield` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "dragon_shield_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Dragon Shield`!")));
                        EquippedDragonUtil.getEquippedDragonUtil(uid).setDragonShield("unequipped");
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
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherHelmet().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_helmet", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Helmet` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_helmet_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Helmet`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherHelmet("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    if (p.getArmorWitherUser().getWitherChestplate() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw chestplate` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherChestplate().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_chestplate", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Chestplate` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_chestplate_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Chestplate`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherChestplate("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("gloves")) {
                    if (p.getArmorWitherUser().getWitherGloves() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw gloves` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherGloves().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_gloves", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Gloves` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_gloves_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Gloves`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherGloves("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("trousers")) {
                    if (p.getArmorWitherUser().getWitherTrousers() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw trousers` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherTrousers().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_trousers", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Trousers` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_trousers_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Trousers`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherTrousers("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    if (p.getArmorWitherUser().getWitherBoots() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw boots` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherBoots().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_boots", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Boots` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_boots_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Boots`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherBoots("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    if (p.getArmorWitherUser().getWitherWeapon() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw sword` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherSword().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_sword", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Sword` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_sword_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Sword`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherSword("unequipped");
                    }
                    return;
                }
                if (args[1].equalsIgnoreCase("shield")) {
                    if (p.getArmorWitherUser().getWitherShield() == null) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Error"),
                                Language.getLocalized(uid, "usage", "Use `" + userPrefix + "invw shield` to purchase this")));
                        return;
                    }
                    if (EquippedWitherUtil.getEquippedWitherUtil(uid).getWitherShield().equalsIgnoreCase("unequipped")) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_shield", "Unequipped"),
                                Language.getLocalized(uid, "already_unequipped", "The `Wither Shield` is already unequipped!")));
                    } else {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "wither_shield_unequipped", "Unequipped"),
                                Language.getLocalized(uid, "equip_successful", "Successfully unequipped `Wither Shield`!")));
                        EquippedWitherUtil.getEquippedWitherUtil(uid).setWitherShield("unequipped");
                    }
                    return;
                }
            }
        }
        if (armor.equalsIgnoreCase("pet")) {
                petItem = pu.getPet();
                itemId = pu.getPetID();
                pu.updatePet(-1L, null);
        } else {
            switch (armor) {
                case "helmet":
                    armorObj = au.getHelmet();
                    itemId = au.getHelmetID();
                    au.updateHelmet(-1L, null);
                    break;
                case "chestplate":
                    armorObj = au.getChestplate();
                    itemId = au.getChestplateID();
                    au.updateChestplate(-1L, null);
                    break;
                case "gloves":
                    armorObj = au.getGloves();
                    itemId = au.getGlovesID();
                    au.updateGloves(-1L, null);
                    break;
                case "trousers":
                    armorObj = au.getTrousers();
                    itemId = au.getTrousersID();
                    au.updateTrousers(-1L, null);
                    break;
                case "boots":
                    armorObj = au.getBoots();
                    itemId = au.getBootsID();
                    au.updateBoots(-1L, null);
                    break;
                case "sword":
                case "staff":
                case "bow":
                case "arrow":
                case "shield":
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "cant_unequip_weapon", "You can't unequip your weapon! You must defend yourself.")));
                    return;
                default:
                    itemId = "";
                    break;
            }
        }
        if (!armor.equalsIgnoreCase("pet")) {
            if (armorObj == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_armor_or_not_wearing",
                        "This armor doesn't exists or you aren't wearing it!")));
                return;
            }

            inv.addItem(Long.parseLong(itemId), armorObj.getName());
            UnequipEvent unequipEvent = new UnequipEvent(p);
            EventManager.callEvent(unequipEvent);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "armor", "Armor"),
                    Language.getLocalized(uid, "unequip_successful", "Successfully ununequipped your {armor}!")
                            .replace("{armor}", armorObj.getDisplay())));
        } else {
            if (petItem == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_pet_or_not_using",
                        "This pet doesn't exists or you aren't using it!")));
                return;
            }
            petInv.addPetOld(Long.parseLong(itemId), petItem.getName());
            e.reply(MessageUtil.success(Language.getLocalized(uid, "pet", "Pet"),
                    Language.getLocalized(uid, "unequip_successful", "Successfully ununequipped your {pet}!")
                            .replace("{pet}", petItem.getDisplay())));
        }
    }
}