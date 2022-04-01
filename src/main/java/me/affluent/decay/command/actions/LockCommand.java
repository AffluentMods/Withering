package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.*;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.itemUtil.ItemLockingUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.entities.User;

public class LockCommand extends BotCommand {

    public LockCommand() {
        this.name = "lock";
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String[] args = e.getArgs();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "lock_usage",
                                    "Please use {command_usage}.\n" +
                                            "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                            "Locking items will prevent them from being dissolved.")
                            .replace("{command_usage}", "`" + userPrefix + "lock <Equipped Gear | ItemID>`")));
            return;
        }
        ArmorUser armorUser = p.getArmorUser();
        Armor armor = null;
        Weapon weapon = null;
        Arrow arrow = null;
        Shield shield = null;
        String type = args[0].toLowerCase();
        long itemID = 0;
        if (type.equalsIgnoreCase("helmet")) itemID = Long.parseLong(armorUser.getHelmetID());
        if (type.equalsIgnoreCase("chestplate")) itemID = Long.parseLong(armorUser.getChestplateID());
        if (type.equalsIgnoreCase("gloves")) itemID = Long.parseLong(armorUser.getGlovesID());
        if (type.equalsIgnoreCase("trousers")) itemID = Long.parseLong(armorUser.getTrousersID());
        if (type.equalsIgnoreCase("boots")) itemID = Long.parseLong(armorUser.getBootsID());
        if (type.equalsIgnoreCase("sword") || type.equalsIgnoreCase("bow") ||
                type.equalsIgnoreCase("staff") || type.equalsIgnoreCase("weapon")) itemID = Long.parseLong(armorUser.getWeaponID());
        if (type.equalsIgnoreCase("arrow") || type.equalsIgnoreCase("arrows"))itemID = Long.parseLong(armorUser.getArrowID());
        if (type.equalsIgnoreCase("shield")) itemID = Long.parseLong(armorUser.getShieldID());
        if (itemID == 0) {
            try {
                itemID = Long.parseLong(type);
            } catch (NumberFormatException ex) {
            }
        }
        Item item = InventoryUser.getItemByID(itemID);
        if (item == null || !item.getOwner().equals(uid)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                            .replace("{item}", item == null ? "this item" : "`" + item.getName() + "`")));
            return;
        }
        if (ItemLockingUtil.getItemLockValue(uid, itemID) == 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Locked"),
                    Language.getLocalized(uid, "locked_confirm",
                                    "You have already locked your item `[{id}]`")
                            .replace("{id}", "" + itemID)));
            return;
        }
        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Locked"),
                Language.getLocalized(uid, "lock_confirm",
                                "You have successfully locked your item `[{id}]`")
                        .replace("{id}", "" + itemID)));
            ItemLockingUtil.setItemLockValue(uid, 1, itemID);
    }
}
