package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.chest.Chest;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.ChestOpenEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.KeysUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.StatsUtil;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class OpenCommand extends BotCommand {

    public OpenCommand() {
        this.name = "open";
        this.aliases = new String[]{"open"};
        this.cooldown = 1.325;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String[] args = e.getArgs();
        if (args.length == 0) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            String defaultMessage =
                    "Please use {command_usage}.\n`<>` - Required Parameter\n`[]` - Optional Parameter\n\n Looking for specifics? Use `" + userPrefix + "info chest`\nCheck your keys with `" + userPrefix + "keys`";
            String msg = Language.getLocalized(uid, "open_usage", defaultMessage)
                    .replace("{command_usage}", "`" + userPrefix + "open <chest type> [amount]`")
                    .replace("{chance}", "`12.5%`");
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"), msg));
            return;
        }
        Player p = Player.getPlayer(uid);
        InventoryUser inv = p.getInventoryUser();
        int invLimit = p.getInvLimit();
        if (inv.getSize() >= invLimit) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "inventory_full",
                            "Your inventory is full!\nYou can't go over the limit of {limit} items.")
                    .replace("{limit}", "`" + invLimit + "`")));
            return;
        }
        int openAmount = 1;
        StringBuilder arg = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String arg0 = args[i];
            if (i == (args.length - 1)) {
                try {
                    openAmount = Integer.parseInt(arg0);
                } catch (NumberFormatException ex) {
                    arg.append(arg0).append(" ");
                }
                continue;
            }
            arg.append(arg0).append(" ");
        }
        if (arg.toString().endsWith(" ")) arg = new StringBuilder(arg.substring(0, arg.length() - 1));
        if (openAmount < 1) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "negative_amount", "The argument {argument} must be a positive number!")
                            .replace("{argument}", "`<amount>`")));
            return;
        }
        if (openAmount > (invLimit / 10)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                   Language.getLocalized(uid, "over_limit", "You can only open up to {amount} chests at once!")
                           .replace("{amount}", "`" + invLimit / 10 + "`")));
            return;
        }
        ItemType itemType = null;
        for (ItemType it : ItemType.values()) {
            if (it.name().replaceAll("_", " ").equalsIgnoreCase(arg.toString())) {
                itemType = it;
                break;
            }
        }
        String chestTypes = "- Wood\n- Metal\n- Titanium\n- Steel\n- Dragon Steel\n-Titan Alloy";
        if (itemType == null) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "invalid_chest_type", "Invalid chest type.\n\n__Types:__\n{types}")
                            .replace("{types}", chestTypes)));
            return;
        }
        String type = itemType.name().toLowerCase().replaceAll("_", " ");
        if (type.endsWith(" key")) type = type.substring(0, type.length() - 4);
        long hasKeys = KeysUtil.getKeys(uid, type + " key");
        if (hasKeys < openAmount) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "not_enough_item",
                            "You don't have enough of {emote} {item}!\nYou currently have {emote} {amount} {item}.")
                    .replace("{item}", capitalizeFully(type) + " Key")
                    .replace("{emote}", EmoteUtil.getEmoteMention(capitalizeFully(type) + "_Key"))
                    .replace("{amount}", "`" + hasKeys + "x`")));
            return;
        }
        StringBuilder rw = new StringBuilder();
        HashMap<String, Integer> rewards = new HashMap<>();
        for (int a = 0; a < openAmount; a++) {
            HashMap<String, Integer> rewards0 = getChestRewards(itemType);
            if (rewards0 == null) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), "Error rewards is null"));
                return;
            }
            for (String ri : rewards0.keySet()) {
                int ra = rewards0.get(ri);
                rewards.put(ri, rewards.getOrDefault(ri, 0) + ra);
            }
        }
        for (String ri : rewards.keySet()) {
            int ra = rewards.get(ri);
            rw.append("- ");
            if (ri.equalsIgnoreCase("medallion")) rw.append(EmoteUtil.getCoin());
            else rw.append(new Item(-1, "", ri.replace("_", " ")).getEmote());
            rw.append(" ").append(capitalizeFully(ri)).append(" `x").append(FormatUtil.formatCommas(ra)).append("`\n");
        }
        if (p.getExpUser().getLevel() >= Chest.getChest(itemType).getLevelReq()) {
            int chests_opened = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "chests_opened", "0"));
            chests_opened += openAmount;
            StatsUtil.setStat(p.getUserId(), "chests_opened", String.valueOf(chests_opened));
            ChestOpenEvent chestOpenEvent = new ChestOpenEvent(p, itemType, rewards, openAmount);
            EventManager.callEvent(chestOpenEvent);
            KeysUtil.setKeys(uid, type + " key", hasKeys - openAmount);
            for (String ri : rewards.keySet()) {
                int ra = rewards.get(ri);
                if (ri.equalsIgnoreCase("medallion")) {
                    p.getEcoUser().addBalance(ra);
                    continue;
                }
                if (ra > 1) {
                    for (int i = 0; i < ra; i++) inv.addItem(ri, 1);
                } else inv.addItem(ri, ra);
            }
            e.reply(MessageUtil.success(Language.getLocalized(uid, "opened_chest", "Opened {open_amount} {chest_name} {emote}")
                            .replace("{emote}", Chest.getChest(itemType).getEmote())
                            .replace("{chest_name}", getChestName(itemType))
                            .replace("{open_amount}", openAmount + "x"),
                    Language.getLocalized(uid, "rewards", "Rewards") + ":\n\n" + rw));
       } else if (p.getExpUser().getLevel() < Chest.getChest(itemType).getLevelReq()) {
            int levelRequired = Chest.getChest(itemType).getLevelReq();
            e.reply(MessageUtil.success(Language.getLocalized(uid, "low_level", "Locked"),
                    Language.getLocalized(uid, "locked_chest", "This " + Chest.getChest(itemType).getEmote() + " {chest_name} appears to be locked.\n Perhaps you should hit level {level_req} before attempting to open this chest!")
                    .replace("{chest_name}", getChestName(itemType)).replace("{level_req}", levelRequired + "")));
        }
    }

    private static String getChestName(ItemType it) {
        Chest chest = Chest.getChest(it);
        return chest.getName();
    }
    private static HashMap<String, Integer> getChestRewards(ItemType it) {
            Chest chest = Chest.getChest(it);
            if (chest == null) return null;
            HashMap<String, Integer> rewards = chest.getRewards();
            rewards.put("medallion", chest.getMedallion());
            return rewards;
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