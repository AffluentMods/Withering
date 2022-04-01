package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ArmorType;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.event.Event;
import me.affluent.decay.event.ScrollEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.ScrollsUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ScrollCommand extends BotCommand {

    public ScrollCommand() {
        this.name = "scroll";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        String[] args = e.getArgs();
        if (args.length < 1) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "scrolls_usage",
                            "Please use {command_usage}.\n" +
                                    "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                    "You currently have {scrolls} scrolls.")
                            .replace("{command_usage}", "`" + userPrefix + "scroll <Item ID>`")
                            .replace("{scrolls}", "`" + ScrollsUtil.getScrolls(uid) + "` " + EmoteUtil.getEmoteMention("Scroll"))));
            return;
        }
        long itemID;
        try {
            itemID = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                            .replace("{argument}", "`<Item ID>`")));
            return;
        }
        int scrolls = ScrollsUtil.getScrolls(uid);
        if (scrolls <= 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_scrolls",
                    "Majesty, you seem to have ran out. \nYou need scrolls " + EmoteUtil.getEmoteMention("Scroll") + " to use this.")));
            return;
        }
        Item item = InventoryUser.getItemByID(itemID);
        if (item == null || !item.getOwner().equals(uid)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                            .replace("{item}", item == null ? "this item" : "`" + item.getName() + "`")));
            return;
        }
        ScrollsUtil.setScrolls(uid, scrolls - 1);
        ArmorType at_now = item.getGear();
        ItemType it_now = item.getType();
        if (at_now.equals(ArmorType.STAFF) && it_now.equals(ItemType.TITAN_ALLOY)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "no_scrolling_staff", "You cannot scroll Titan Alloy Staffs")));
            return;
        }
        if (at_now.equals(ArmorType.STAFF) && it_now.equals(ItemType.WITHER)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "no_scrolling_staff", "You cannot scroll Wither Staffs")));
            return;
        }
        List<ArmorType> ats = new ArrayList<>(Arrays.asList(ArmorType.values()));
        ats.remove(ArmorType.STAFF);
        ats.remove(at_now);
        ArmorType at_new = ats.get(new Random().nextInt(ats.size()));
        final String oldItem = item.getName();
        final String oldItemEmote = item.getEmote();
        item.updateGear(at_new);

        boolean upgradeRarity = new Random().nextInt(101) < 2;
        Rarity better = item.getRarity().better();
        if (upgradeRarity && better != null) item.updateRarity(better);
        final String newItem = item.getName();
        InventoryUser inv = InventoryUser.getInventoryUser(uid);
        inv.removeItem(itemID);
        inv.addItem(itemID, newItem);
        ScrollEvent scrollEvent = new ScrollEvent(Player.getPlayer(uid));
        EventManager.callEvent(scrollEvent);
        e.reply(MessageUtil.success(Language.getLocalized(uid, "scroll_title", "Reroll"),
                Language.getLocalized(uid, "scroll_success", "Successfully scrolled the {old_item} to {new_item}!")
                        .replace("{old_item}", oldItemEmote + "`" + oldItem + "`")
                        .replace("{new_item}", item.getEmote() + "`" + newItem + "`")));
    }
}