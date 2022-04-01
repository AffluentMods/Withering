package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.*;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.enums.ArmorType;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.event.ForgeEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.itemUtil.ItemLockingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.settingsUtil.ConfirmUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ForgeCommand extends BotCommand {

    public ForgeCommand() {
        this.name = "forge";
        this.cooldown = 1.5;
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
            eb.setColor(new Color(15, 141, 255));
            eb.setTitle("Forge");
            eb.setDescription(Language.getLocalized(uid, "usage", "Please use {command_usage}.\n" +
                            "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                            "You currently have {amount} Alloy Ingots.\n\n" +
                            "Use `" + userPrefix + "help forge` for more information on usage and the forge")
                    .replace("{command_usage}", "`" + userPrefix + "forge [smith | dissolve] <itemID>`")
                    .replace("{amount}", "`" + IngotUtil.getIngots(uid) + "` " + EmoteUtil.getEmoteMention("Alloy_Ingot")));
            e.getTextChannel().sendMessage(eb.build()).queue();
            return;
        }
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
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
        if (args[0].equalsIgnoreCase("smith")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                        Language.getLocalized(uid, "usage", "Please use {command_usage}.").replace("{command_usage}",
                                "`" + userPrefix + "forge smith <wither | staff> <item Type | Material> [ID's]`\n" +
                                        "Use `" + userPrefix + "forge smith wither`, and\n" +
                                        "`" + userPrefix + "forge smith staff` for additional information.")));
                return;
            }
            if (args[1].equalsIgnoreCase("wither")) {
                if (args.length < 3) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                            Language.getLocalized(uid, "forge_smith_wither_usage",
                                            "Please use {command_usage}.\n" +
                                                    "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                                    "Example `" + userPrefix + "forge smith wither chestplate 1 2 3`\n" +
                                                    "This will destroy the items 1, 2, and 3 and make a `Junk Wither Chestplate`\n" +
                                                    "Items have to be Ancient Titan Alloy.")
                                    .replace("{command_usage}", "`" + userPrefix + "forge smith wither <Item Type> <ID 1> <ID 2> <ID 3>`")));
                    return;
                }
                String type = args[2].toLowerCase();
                ArmorIronUser armorUser = p.getArmorIronUser();
                Armor armor = null;
                String armorType = "";

                String armorIDa1 = args[3];
                String armorIDa2 = args[4];
                String armorIDa3 = args[5];
                InventoryUser inv = p.getInventoryUser();
                int armorID1;
                int armorID2;
                int armorID3;
                try {
                    armorID1 = Integer.parseInt(armorIDa1);
                    armorID2 = Integer.parseInt(armorIDa2);
                    armorID3 = Integer.parseInt(armorIDa3);
                } catch (NumberFormatException ex) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                                    .replace("{argument}", "`<Item ID>`")));
                    return;
                }
                Item item1 = InventoryUser.getItemByID(armorID1);
                Item item2 = InventoryUser.getItemByID(armorID2);
                Item item3 = InventoryUser.getItemByID(armorID3);

                if (item1 == null || !item1.getOwner().equals(uid)) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                                    .replace("{item}", item1 == null ? "this item" : "`" + item1.getName() + "`")));
                    return;
                }
                if (item2 == null || !item2.getOwner().equals(uid)) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                                    .replace("{item}", item2 == null ? "this item" : "`" + item2.getName() + "`")));
                    return;
                }
                if (item3 == null || !item3.getOwner().equals(uid)) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                                    .replace("{item}", item3 == null ? "this item" : "`" + item3.getName() + "`")));
                    return;
                }
                if (item1.getRarity() != Rarity.ANCIENT || item1.getType() != ItemType.TITAN_ALLOY || item1.getRarity() != Rarity.ARTIFACT) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "not_ancient", "The [`{id}`] {item} has to be an Ancient/Artifact Titan Alloy in order to create a Wither Item!")
                                    .replace("{item}", item1.getName())
                                    .replace("{id}", "" + item1.getID())));
                    return;
                }
                if (item2.getRarity() != Rarity.ANCIENT || item2.getType() != ItemType.TITAN_ALLOY || item2.getRarity() != Rarity.ARTIFACT) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "not_ancient", "The [`{id}`] {item} has to be an Ancient/Artifact Titan Alloy in order to create a Wither Item!")
                                    .replace("{item}", item2.getName())
                                    .replace("{id}", "" + item2.getID())));
                    return;
                }
                if (item3.getRarity() != Rarity.ANCIENT || item3.getType() != ItemType.TITAN_ALLOY || item3.getRarity() != Rarity.ARTIFACT) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "not_ancient", "The [`{id}`] {item} has to be an Ancient/Artifact Titan Alloy in order to create a Wither Item!")
                                    .replace("{item}", item3.getName())
                                    .replace("{id}", "" + item3.getID())));
                    return;
                }

                String newArmor = "";
                boolean merged = false;
                if (type.equalsIgnoreCase("helmet")) {
                    newArmor = "junk wither helmet";
                    Helmet helmet = Helmet.getHelmet(newArmor);
                    if (helmet != null) inv.addItem(helmet.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("chestplate")) {
                    newArmor = "junk wither chestplate";
                    Chestplate chestplate = Chestplate.getChestplate(newArmor);
                    if (chestplate != null) inv.addItem(chestplate.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("gloves")) {
                    newArmor = "junk wither gloves";
                    Gloves gloves = Gloves.getGloves(newArmor);
                    if (gloves != null) inv.addItem(gloves.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("trousers")) {
                    newArmor = "junk wither trousers";
                    Trousers trousers = Trousers.getTrousers(newArmor);
                    if (trousers != null) inv.addItem(trousers.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("boots")) {
                    newArmor = "junk wither boots";
                    Boots boots = Boots.getBoots(newArmor);
                    if (boots != null) inv.addItem(boots.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("sword")) {
                    newArmor = "junk wither sword";
                    Weapon sword = Weapon.getWeapon(newArmor);
                    if (sword != null) inv.addItem(sword.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("bow")) {
                    newArmor = "junk wither bow";
                    Weapon bow = Weapon.getWeapon(newArmor);
                    if (bow != null) inv.addItem(bow.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("arrow") || type.equalsIgnoreCase("arrows")) {
                    newArmor = "junk wither arrow";
                    Arrow arrow = Arrow.getArrow(newArmor);
                    if (arrow != null) inv.addItem(arrow.getName().toLowerCase(), 1);
                    merged = true;
                }
                if (type.equalsIgnoreCase("shield")) {
                    newArmor = "junk wither shield";
                    Shield shield = Shield.getShield(newArmor);
                    if (shield != null) inv.addItem(shield.getName().toLowerCase(), 1);
                    merged = true;
                }

                if (merged) {
                    inv.fullRemoveItem(item1.getID());
                    inv.fullRemoveItem(item2.getID());
                    inv.fullRemoveItem(item3.getID());

                    e.reply(MessageUtil.success(Language.getLocalized(uid, "forged_plain", "Forged"),
                            Language.getLocalized(uid, "forged_wither",
                                            "Successfully Forged " + EmoteUtil.getEmoteMention(newArmor) + "{forged_wither}!")
                                    .replace("{forged_wither}", newArmor)));
                    return;
                }
                e.reply(MessageUtil
                        .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_argument", "Invalid argument")));
            }
            if (args[1].equalsIgnoreCase("staff")) {
                if (args.length < 3) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                            Language.getLocalized(uid, "forge_smith_staff_usage",
                                            "Please use {command_usage}.\n" +
                                                    "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                                    "You currently have {orbs} Orbs.")
                                    .replace("{command_usage}", "`" + userPrefix + "forge smith staff <material>`")
                                    .replace("{orbs}", "`" + OrbUtil.getOrbs(uid) + "` " + EmoteUtil.getEmoteMention("Orb"))));
                    return;
                }
                int orbs = OrbUtil.getOrbs(uid);
                String material = args[2].toLowerCase();
                String newStaff = "";
                ItemType itemType = null;
                InventoryUser inv = p.getInventoryUser();
                boolean crafted = false;
                if (material.equalsIgnoreCase("wood")) {
                    itemType = ItemType.WOOD;
                }
                if (material.equalsIgnoreCase("copper")) {
                    itemType = ItemType.COPPER;
                }
                if (material.equalsIgnoreCase("reinforced")) {
                    itemType = ItemType.REINFORCED;
                }
                if (material.equalsIgnoreCase("titanium")) {
                    itemType = ItemType.TITANIUM;
                }
                if (material.equalsIgnoreCase("iron")) {
                    itemType = ItemType.IRON;
                }
                if (material.equalsIgnoreCase("steel")) {
                    itemType = ItemType.STEEL;
                }
                if (material.equalsIgnoreCase("carbon steel") || material.equalsIgnoreCase("carbon_steel")) {
                    itemType = ItemType.CARBON_STEEL;
                }
                if (material.equalsIgnoreCase("dragon steel") || material.equalsIgnoreCase("dragon_steel")) {
                    itemType = ItemType.DRAGON_STEEL;
                }
                if (material.equalsIgnoreCase("titan alloy") || material.equalsIgnoreCase("titan_alloy")) {
                    itemType = ItemType.TITAN_ALLOY;
                }
                if (material.equalsIgnoreCase("wither")) {
                    itemType = ItemType.WITHER;
                }
                if (itemType == null) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                            Language.getLocalized(uid, "forge_smith_staff_usage",
                                            "Please use {command_usage}.\n" +
                                                    "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                                    "You currently have {orbs} Orbs.")
                                    .replace("{command_usage}", "`" + userPrefix + "forge smith staff <material>`")
                                    .replace("{orbs}", "`" + OrbUtil.getOrbs(uid) + "` " + EmoteUtil.getEmoteMention("Orb"))));
                    return;
                }
                if (cost) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "staff_cost",
                                            "In order to create a {emote} `junk {material} staff`\n" +
                                                    "You will need " + EmoteUtil.getEmoteMention("orb") + "{amount} orbs.")
                                    .replace("{emote}", EmoteUtil.getEmoteMention("junk " + itemType.toString().toLowerCase() + " staff"))
                                    .replace("{material}", "" + itemType.toString().toLowerCase())
                                    .replace("{amount}", "`" + (int) (getOrbCost(itemType)) + "`")));
                    return;
                }
                if (orbs <= 0) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_orbs",
                            "Majesty, you seem to have ran out. \nYou need orbs " + EmoteUtil.getEmoteMention("Orb") + " to use this.")));
                    return;
                }
                if (orbs < (int) (getOrbCost(itemType))) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_orbs",
                            "Majesty, you seem to have ran out. \nYou need `"+ (int) (getOrbCost(itemType)) + "` orbs " + EmoteUtil.getEmoteMention("Orb") + " to use this.")));
                    return;
                }
                String finalStaff;
                newStaff = "junk " + itemType + " staff";
                finalStaff = newStaff.replaceAll("_", " ");
                Weapon staff = Weapon.getWeapon(finalStaff);
                if (staff != null) inv.addItem(staff.getName().toLowerCase(), 1);
                OrbUtil.setOrbs(uid, (int) (orbs - getOrbCost(itemType)));
                e.reply(MessageUtil.success(Language.getLocalized(uid, "forged_plain", "Forged"),
                        Language.getLocalized(uid, "forged_staff",
                                        "Successfully Forged " + EmoteUtil.getEmoteMention(newStaff) + " {forged_staff}!")
                                .replace("{forged_staff}", finalStaff.toLowerCase())));
                return;
            }
        }
        if (args[0].equalsIgnoreCase("dissolve")) {
            if (args.length < 2) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                        Language.getLocalized(uid, "usage", "Please use {command_usage}.").replace("{command_usage}",
                                "`" + userPrefix + "forge dissolve <Item ID | Item | Material | Rarity>`")));
                return;
            }
            if (!(Player.playerExists(uid))) {
                e.reply(Constants.PROFILE_404(uid));
                return;
            }
            boolean confirm = false;
            String confirmation = ConfirmUtil.getConfirmUtil(uid).getConfirmSetting();
            if (confirmation.equalsIgnoreCase("disabled")) {
                confirm = true;
            }
            String itemName = "";
            String lockedItem = "";
            for (int i = 1; i < args.length; i++) {
                String arg = args[i];
                if (i == (args.length - 1)) {
                    if (arg.equalsIgnoreCase("confirm")) {
                        confirm = true;
                        break;
                    }
                }
                // DO NOT CONVERT TO STRINGBUILDER
                itemName += arg + " ";
            }
            int orbAmount = 0;
            if (itemName.endsWith(" ")) itemName = itemName.substring(0, itemName.length() - 1);
            List<Item> items = new ArrayList<>();
            try {
                long ID = Long.parseLong(itemName);
                Item item = InventoryUser.getItemByID(ID);
                if (item == null || !item.getOwner().equalsIgnoreCase(uid)) {
                    e.reply(MessageUtil
                            .err(Constants.ERROR(uid), Language.getLocalized(uid, "invalid_item", "Invalid item.")));
                    return;
                }
                orbAmount += getOrbAmount(item.getType(), item.getRarity());
                if (ItemLockingUtil.getItemLockValue(uid, item.getID()) == 0) {
                    items.add(item);
                }
                if (ItemLockingUtil.getItemLockValue(uid, item.getID()) == 1) {
                    lockedItem = "\nOne or more of your item(s) were locked and have not been dissolved.";
                }
            } catch (NumberFormatException ex) {
                final String format = "\n" +
                        "- ItemID | ex. 123\n" +
                        "- rarity + geartype | ex. common helmet\n" +
                        "- material + geartype | ex. wood sword\n" +
                        "- rarity + material + geartype | ex. common wood sword\n" +
                        "- rarity + material | ex. common carbon_steel\n" +
                        "- rarity | ex. " + "common\n" +
                        "- material | ex. " + "dragon_steel\n" +
                        "- all";
                if (itemName.split(" ").length > 1) {
                    boolean isName = validate(itemName, 4);
                    if (isName) {
                        items.addAll(p.getInventoryUser().getItemsByName(itemName));
                        orbAmount += p.getInventoryUser().getItemsByNameOrb(itemName);
                    } else {
                        boolean isRarityMaterial = validate(itemName, 10);
                        if (isRarityMaterial) {
                            items.addAll(p.getInventoryUser().getItemsByRarityMaterial(itemName));
                            orbAmount += p.getInventoryUser().getItemsByRarityMaterialOrb(itemName);
                        } else {
                            boolean isRarityGear = validate(itemName, 1);
                            if (isRarityGear) {
                                items.addAll(p.getInventoryUser().getItemsByRarityGear(itemName));
                                orbAmount += p.getInventoryUser().getItemsByRarityGearOrb(itemName);
                            } else {
                                boolean isMaterialGear = validate(itemName, 2);
                                if (isMaterialGear) {
                                    items.addAll(p.getInventoryUser().getItemsByMaterialGear(itemName));
                                    orbAmount += p.getInventoryUser().getItemsByMaterialGearOrb(itemName);
                                    } else {
                                        e.reply(MessageUtil.err(Constants.ERROR(uid),
                                                Language.getLocalized(uid, "invalid_format",
                                                        "Invalid format.\nFormat(s): {format}").replace("{format}", format)));
                                        return;
                                }
                            }
                        }
                    }
                } else {
                    boolean isRarity = validate(itemName, 11);
                    if (isRarity) {
                        items.addAll(p.getInventoryUser().getItemsByRarity(itemName));
                        orbAmount += p.getInventoryUser().getItemsByRarityOrb(itemName);
                    } else {
                        boolean isMaterial = validate(itemName, 6);
                        if (isMaterial) {
                            items.addAll(p.getInventoryUser().getItemsByMaterial(itemName));
                            orbAmount += p.getInventoryUser().getItemsByMaterialOrb(itemName);
                        } else {
                            if (itemName.equalsIgnoreCase("all")) {
                                items.addAll(p.getInventoryUser().getItemsAll());
                                orbAmount += p.getInventoryUser().getItemsAllOrb();
                            } else {
                                e.reply(MessageUtil.err(Constants.ERROR(uid),
                                        Language.getLocalized(uid, "invalid_format", "Invalid format.\nFormat(s): {format}")
                                                .replace("{format}", format)));
                                return;
                            }
                        }
                    }
                }
            }
            if (items.size() == 0) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "dissolve_no_items",
                        "No items could be found matching this item name format.\n" +
                                "Or the item(s) you requested were locked and could not be dissolved.")));
                return;
            }

            boolean multiple = items.size() > 1;
            if (!confirm) {
                String dissolveMessage = multiple ? Language.getLocalized(uid, "confirm_dissolve_many_items",
                        "Are you sure you wish to dissolve {item_count} items from your inventory? This cannot be undone\n" +
                                "You will obtain {orb_amount} Orbs in the process." +
                                ".\nUse {command_usage} to confirm.") : Language.getLocalized(uid, "confirm_dissolve_item",
                        "Are you sure you wish to dissolve the {item} from your inventory? This cannot be undone\n" +
                                "You will obtain {orb_amount} Orbs in the process." +
                                ".\nUse {command_usage} to confirm.");
                e.reply(MessageUtil.info(Language.getLocalized(uid, "confirm_plain", "Confirm"),
                        dissolveMessage.replace("{item}", "`" + items.get(0).getName() + "`")
                                .replace("{item_count}", "`" + items.size() + "`")
                                .replace("{orb_amount}", "" + EmoteUtil.getEmoteMention("Orb") + " " + orbAmount)
                                .replace("{command_usage}", "`" + userPrefix + "forge dissolve " + itemName + " confirm`")));
                return;
            }
            final String dissolvedItemName;
            InventoryUser inv = p.getInventoryUser();
            if (items.size() == 1) dissolvedItemName = items.get(0).getName();
            else dissolvedItemName = itemName;
            for (Item item : items) {
                inv.fullRemoveItem(item.getID());
            }
            if (multiple) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "item_dissolved_title", "Items Dissolved"),
                        Language.getLocalized(uid, "multiple_item_dissolved_message",
                                        "Successfully dissolved {item_count} items from your inventory for " +
                                                "{orb_amount} Orbs." + lockedItem)
                                .replace("{orb_amount}", EmoteUtil.getEmoteMention("Orb") + " " + orbAmount)
                                .replace("{item_count}", "`" + items.size() + "`")));
                OrbUtil.addOrbs(uid, orbAmount);
            } else {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "item_dissolved_title", "Item Dissolved"),
                        Language.getLocalized(uid, "item_dissolved_message",
                                        "Successfully dissolved the {item} from your inventory for " +
                                                "{orb_amount} Orbs." + lockedItem)
                                .replace("{orb_amount}", EmoteUtil.getEmoteMention("Orb") + " " + orbAmount)
                                .replace("{item}", "`" + capitalizeFully(dissolvedItemName) + "`")));
                OrbUtil.addOrbs(uid, 2);
            }
            return;
        }

        long itemID = 0;
        //boolean smeltingMess = new Random().nextInt(101) < 50;
        try {
            itemID = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                            .replace("{argument}", "`<Item ID>`")));
            return;
        }
        long ingots = IngotUtil.getIngots(uid);
        if (ingots <= 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need " + EmoteUtil.getEmoteMention("Alloy_Ingot") + " ingots to use this.")));
            return;
        }
        Item item = InventoryUser.getItemByID(itemID);
        if (item == null || !item.getOwner().equals(uid)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                            .replace("{item}", item == null ? "this item" : "`" + item.getName() + "`")));
            return;
        }
        if (cost) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                    Language.getLocalized(uid, "ingot_cost",
                                    "In order to upgrade the rarity of {item}\n" +
                                            "You will need " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "{amount} Alloy Ingots.")
                            .replace("{item}", item.getEmote() + " `" + item.getName() + "`")
                            .replace("{amount}", "`" + (int) (IngotUtil.getIngotCost(item.getRarity()) * getIngotCostMultiplier(item.getType())) + "`")));
            return;
        }
        int ingotCost = 0;
        if (item.getRarity() == Rarity.ANCIENT || item.getRarity() == Rarity.ARTIFACT) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(15, 141, 255));
            eb.setTitle("Max Rarity");
            eb.setDescription(Language.getLocalized(uid, "max_rarity",
                            "Majesty, I believe your {item} is of highest quality.")
                    .replace("{item}", item.getEmote() + " " + item.getName()));
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
            return;
        }
        ingotCost = (int) (IngotUtil.getIngotCost(item.getRarity()) * getIngotCostMultiplier(item.getType()));
        if (IngotUtil.getIngots(uid) < ingotCost) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(255, 58, 35));
            eb.setTitle("Error");
            eb.setDescription(Language.getLocalized(uid, "missing_needed_items_ingots",
                            "*checks pockets* \n" + "empty they are, cant afford.\n\n" +
                                    " You need " + EmoteUtil.getEmoteMention("Alloy_Ingot") + " {cost} Ingots to Forge this item.")
                    .replace("{cost}", "`" + ingotCost + "`"));
            e.reply(eb.build());
            return;
        }

        boolean confirm = false;
        final String oldItem = item.getName();
        final String oldItemEmote = item.getEmote();
        Rarity better = item.getRarity().better();
        if (better != null) item.updateRarity(better);
        final String newItem = item.getName();
        String confirmation = ConfirmUtil.getConfirmUtil(uid).getConfirmSetting();

        int starCount = ItemStarringUtil.getItemStar(uid, itemID);

        if (confirmation.equalsIgnoreCase("disabled")) {
            confirm = true;
        }
        if (!confirm) {
            confirm = true;
            if (args.length < 2) {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "forge_item_title", "Forging"),
                        Language.getLocalized(uid, "forge_item_success", "Confirm to forge your {old_item} to {new_item}! This will cost {cost} Alloy Ingots to forge.\n" +
                                        "Use `" + userPrefix + "forge " + itemID + " confirm`")
                                .replace("{old_item}", oldItemEmote + "`" + oldItem + "`")
                                .replace("{new_item}", item.getEmote() + "`" + newItem + "`")
                                .replace("{cost}", ingotCost + EmoteUtil.getEmoteMention("Alloy_Ingot"))));
                Rarity worse = item.getRarity().worse();
                if (worse != null) item.updateRarity(worse);
                return;
            }
        }

        if (starCount == 7 && item.getRarity() == Rarity.ANCIENT) {
            Rarity better2 = item.getRarity().betterOther();
            if (better2 != null) item.updateRarity(better2);
            final String newItem2 = item.getName();
            IngotUtil.setIngots(uid, ingots - ingotCost);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "forge_item_title", "Forging"),
                    Language.getLocalized(uid, "forge_item_success", "Successfully forged the {old_item} to {new_item}!\n" +
                                    "You used {cost} Alloy Ingots with a remaining amount of {amount_left}\n" +
                                    "Your item has 7 Stars, it has been upgraded to Artifact Rarity!")
                            .replace("{old_item}", oldItemEmote + "`" + oldItem + "`")
                            .replace("{new_item}", item.getEmote() + "`" + newItem2 + "`")
                            .replace("{cost}", "`" + ingotCost + "` " + EmoteUtil.getEmoteMention("Alloy_Ingot"))
                            .replace("{amount_left}", "`" + IngotUtil.getIngots(uid) + "` " + EmoteUtil.getEmoteMention("Alloy_Ingot"))));
            InventoryUser inv = InventoryUser.getInventoryUser(uid);
            inv.removeItem(itemID);
            inv.addItem(itemID, newItem2);
            return;
        }

            IngotUtil.setIngots(uid, ingots - ingotCost);
            ForgeEvent forgeEvent = new ForgeEvent(p, ingotCost);
            EventManager.callEvent(forgeEvent);
            InventoryUser inv = InventoryUser.getInventoryUser(uid);
            inv.removeItem(itemID);
            inv.addItem(itemID, newItem);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "forge_item_title", "Forging"),
                    Language.getLocalized(uid, "forge_item_success", "Successfully forged the {old_item} to {new_item}!\n" +
                                    "You used {cost} Alloy Ingots with a remaining amount of {amount_left}")
                            .replace("{old_item}", oldItemEmote + "`" + oldItem + "`")
                            .replace("{new_item}", item.getEmote() + "`" + newItem + "`")
                            .replace("{cost}", "`" + ingotCost + "` " + EmoteUtil.getEmoteMention("Alloy_Ingot"))
                            .replace("{amount_left}", "`" + IngotUtil.getIngots(uid) + "` " + EmoteUtil.getEmoteMention("Alloy_Ingot"))));
    }

    private boolean validate(String name, int validateType) {
        boolean a = false;
        String[] split = name.split(" ");
        if (validateType == 10) {
            String rarity = split[0];
            String itemType = name.substring(rarity.length() + 1);
            boolean b = Rarity.isRarity(rarity);
            boolean c = ItemType.isItemType(itemType);
            a = b && c;
        }
        if (validateType == 11) {
            a = Rarity.isRarity(name);
        }
        if (validateType == 1) {
            String gear = split[split.length - 1];
            String rarity = split[0];
            for (Rarity b : Rarity.values()) {
                if (b.name().replaceAll("_", " ").equalsIgnoreCase(rarity) && validate(gear, 3)) {
                    a = true;
                    break;
                }
            }
        }
        if (validateType == 2) {
            String gear = split[split.length - 1];
            String material = name.substring(0, (name.length() - gear.length() - 1));
            for (ItemType b : ItemType.values()) {
                if (b.name().replaceAll("_", " ").equalsIgnoreCase(material) && validate(gear, 3)) {
                    a = true;
                    break;
                }
            }
        }
        if (validateType == 3) {
            a = ArmorType.isArmorType(name);
        }
        if (validateType == 4) {
            String[] fullName = name.split(" ");
            String rarity = "";
            String itemType = "";
            String gear = "";
            for (int i = 0; i < fullName.length; i++) {
                if (i == 0) rarity = fullName[i];
                else if (i == (fullName.length - 1)) gear = fullName[i];
                else itemType += fullName[i] + " ";
            }
            if (itemType.endsWith(" ")) itemType = itemType.substring(0, itemType.length() - 1);
            a = validate(gear, 3) && validate(rarity, 5) && validate(itemType, 6);
        }
        if (validateType == 5) {
            a = Rarity.isRarity(name);
        }
        if (validateType == 6) {
            a = ItemType.isItemType(name);
        }
        return a;
    }

    private double getOrbCost(ItemType itemType) {
        return switch (itemType) {
            case COPPER -> 39;
            case REINFORCED -> 85;
            case TITANIUM -> 178;
            case IRON -> 298;
            case STEEL -> 536;
            case CARBON_STEEL -> 894;
            case DRAGON_STEEL -> 1489;
            case TITAN_ALLOY -> 2793;
            case WITHER -> 12500;
            default -> 22;
        };
    }

    public static double getOrbAmount(ItemType itemType, Rarity rarity) {
        return switch (rarity) {
            case JUNK, COMMON, UNCOMMON, RARE -> 1 + getOrbAmountIT(itemType);
            case EPIC -> 2 + getOrbAmountIT(itemType);
            case LEGEND, MYTHIC -> 3 + getOrbAmountIT(itemType);
            case ANCIENT -> 5 + getOrbAmountIT(itemType);
            case ARTIFACT -> 10 + getOrbAmountIT(itemType);
        };
    }

    private static double getOrbAmountIT(ItemType itemType) {
        return switch (itemType) {
            case IRON, STEEL, CARBON_STEEL -> 2;
            case DRAGON_STEEL, TITAN_ALLOY -> 4;
            case WITHER -> 8;
            default -> 1;
        };
    }

    private double getIngotCostMultiplier(ItemType itemType) {
        return switch (itemType) {
            case COPPER -> 5.32;
            case REINFORCED -> 6.78;
            case TITANIUM -> 9.98;
            case IRON -> 11.22;
            case STEEL -> 13.85;
            case CARBON_STEEL -> 18.33;
            case DRAGON_STEEL -> 24.5;
            case TITAN_ALLOY -> 27.35;
            case WITHER -> 35;
            default -> 1;
        };
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
