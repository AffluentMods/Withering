package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.event.ItemStarEvent;
import me.affluent.decay.event.PetStarEvent;
import me.affluent.decay.item.Item;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class ItemStarringCommand extends BotCommand {

    public ItemStarringCommand() {
        this.name = "star";
        this.aliases = new String[]{"tier"};
        this.cooldown = 1.5;
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
                    Language.getLocalized(uid, "star_usage",
                                    "Please use {command_usage}.\n" +
                                            "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                                            "You currently have {diamond} Diamonds left.")
                            .replace("{command_usage}", "`" + userPrefix + "star <Item ID> [amount] [cost]`\n" +
                                    "or `" + userPrefix + "star pet <Pet ID> [amount] [cost]` for pets")
                            .replace("{diamond}", EmoteUtil.getDiamond() + " `" + DiamondsUtil.getDiamonds(uid) + "`")));
            return;
        }

        long balance = DiamondsUtil.getDiamonds(uid);

        if (args[0].equalsIgnoreCase("pet")) {
            long petID;
            try {
                petID = Long.parseLong(args[1]);
            } catch (NumberFormatException ex) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                                .replace("{argument}", "`<Item ID>`")));
                return;
            }
            balance = DiamondsUtil.getDiamonds(uid);
            if (balance <= 0) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_diamonds",
                        "Majesty, you seem to have ran out. \nYou need Diamonds " + EmoteUtil.getDiamond() + " to use this.")));
                return;
            }
            Pets pet = PetUtil.getPetByID(petID);
            if (pet == null || !pet.getPetOwner().equals(uid)) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_in_inventory", "You don't have {pet}!")
                                .replace("{pet}", pet == null ? "this pet" : "`" + pet.getPetName() + "`")));
                return;
            }
            int currentPetStars = PetStarringUtil.getPetStar(uid, petID);
            int petStarAmount;
            if (args.length > 2) {
                try {
                    petStarAmount = Integer.parseInt(args[2]);
                    if (petStarAmount < 1 || petStarAmount > 10) {
                        e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "argument_between",
                                        "The argument {argument} has to be between {min} and {max}!")
                                .replace("{argument}", "`<star amount>`").replace("{min}", "`1`").replace("{max}", "`10`")));
                        return;
                    }
                } catch (NumberFormatException ex) {
                    petStarAmount = 1;
                }
            } else {
                petStarAmount = 1;
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
            int basePetCost = getNeededPetStarCost(currentPetStars, currentPetStars + petStarAmount);
            if (!cost) {
                if (balance < basePetCost) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_diamonds",
                            "Majesty, you seem to not have enough. \nYou need `x" + basePetCost + "` Diamonds " + EmoteUtil.getDiamond() + " to use this.")));
                    return;
                }
            }
            int totalPetStars = currentPetStars + petStarAmount;
            int possiblePetStars = 10 - currentPetStars;
            if (currentPetStars >= 10) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "maxed_stars", "The {pet} is currently " + EmoteUtil.getEmoteMention("S_") + " {star_count} starred.\n" +
                                        "This pet is maxed and can no longer be starred.")
                                .replace("{pet}", pet.getPetName() + " " + pet.getPetEmote())
                                .replace("{star_count}", "`" + currentPetStars + "`")));
                return;
            }
            if (totalPetStars > 10) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "maxed_stars", "The {pet} is currently " + EmoteUtil.getEmoteMention("S_") + " {star_count} starred.\n" +
                                        "You can only star it {amount} more times.")
                                .replace("{pet}", pet.getPetName() + " " + pet.getPetEmote())
                                .replace("{star_count}", "`" + currentPetStars + "`")
                                .replace("{amount}", "`" + possiblePetStars + "`")));
                return;
            }
            if (cost) {
                if (currentPetStars == 9) {
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "star_cost",
                                            "Your {pet} is currently {star_count} starred\n" +
                                                    "This will have a cost of {amount} to max out.")
                                    .replace("{amount}", "`x" + getNeededPetStarCost(currentPetStars, 10) + "` " + EmoteUtil.getDiamond())
                                    .replace("{pet}", pet.getPetName() + " " + pet.getPetEmote())
                                    .replace("{star_count}", "`" + currentPetStars + "`")));
                    return;
                }
                if (petStarAmount <= 1) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                                Language.getLocalized(uid, "star_cost",
                                                "Your {pet} is currently {star_count} starred\n" +
                                                        "This will have a cost of {amount} to star once,\n" +
                                                        "with a total cost of {max_amount} to `10` star.")
                                        .replace("{max_amount}", "`x" + getNeededPetStarCost(currentPetStars, 10) + "` " + EmoteUtil.getDiamond())
                                        .replace("{amount}", "`x" + getNeededPetStarCost(currentPetStars, currentPetStars + 1) + "` " + EmoteUtil.getDiamond())
                                        .replace("{pet}", pet.getPetName() + " " + pet.getPetEmote())
                                        .replace("{star_count}", "`" + currentPetStars + "`")));
                        return;
                } else {
                    if (currentPetStars + petStarAmount > 10) {
                        e.reply(MessageUtil.err(Constants.ERROR(uid),
                                Language.getLocalized(uid, "maxed_stars", "The {pet} is currently " + EmoteUtil.getEmoteMention("S_") + " {star_count} starred.\n" +
                                                "You can only star it {amount} more times.")
                                        .replace("{pet}", pet.getPetName() + " " + pet.getPetEmote())
                                        .replace("{star_count}", "`" + currentPetStars + "`")
                                        .replace("{amount}", "`" + possiblePetStars + "`")));
                        return;
                    }
                    e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                            Language.getLocalized(uid, "star_cost",
                                            "Your {pet} is currently {star_count} starred\n" +
                                                    "This will have a cost of {amount} to star {star_amount} times,\n" +
                                                    "with a total cost of {max_amount} to `10` star.")
                                    .replace("{star_amount}", "`" + petStarAmount + "`")
                                    .replace("{max_amount}", "`x" + getNeededPetStarCost(currentPetStars, 10) + "` " + EmoteUtil.getDiamond())
                                    .replace("{amount}", "`x" + getNeededPetStarCost(currentPetStars, currentPetStars + petStarAmount) + "` " + EmoteUtil.getDiamond())
                                    .replace("{pet}", pet.getPetName() + " " + pet.getPetEmote())
                                    .replace("{star_count}", "`" + currentPetStars + "`")));
                    return;
                }
            }
            DiamondsUtil.setDiamonds(uid, (long) (balance - basePetCost));
            PetStarringUtil.setPetStar(uid, totalPetStars, petID);
            StringBuilder starDisplay = new StringBuilder();
            for (int i = 0; i < totalPetStars; i++) {
                starDisplay.append(EmoteUtil.getEmoteMention("S_")).append(" ");
            }
            PetStarEvent petStarEvent = new PetStarEvent(p, petStarAmount);
            EventManager.callEvent(petStarEvent);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "star_title", "Pet Starred"),
                    Language.getLocalized(uid, "pet_star_success", "Successfully starred {pet} to {star_count}!")
                            .replace("{pet}", pet.getPetEmote() + " " + pet.getPetName().toLowerCase().replace("_", " "))
                            .replace("{star_count}", starDisplay.toString())));
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

        if (balance <= 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_diamonds",
                    "Majesty, you seem to have ran out. \nYou need Diamonds " + EmoteUtil.getDiamond() + " to use this.")));
            return;
        }
        Item item = InventoryUser.getItemByID(itemID);
        if (item == null || !item.getOwner().equals(uid)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "not_in_inventory", "You don't have {item}!")
                            .replace("{item}", item == null ? "this item" : "`" + item.getName() + "`")));
            return;
        }
        int currentStars = ItemStarringUtil.getItemStar(uid, itemID);
        int starAmount;
        if (args.length > 1) {
            try {
                starAmount = Integer.parseInt(args[1]);
                if (starAmount < 1 || starAmount > 7) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "argument_between",
                                    "The argument {argument} has to be between {min} and {max}!")
                            .replace("{argument}", "`<star amount>`")
                            .replace("{min}", "`1`")
                            .replace("{max}", "`7`")));
                    return;
                }
            } catch (NumberFormatException ex) {
                starAmount = 1;
            }
        } else {
            starAmount = 1;
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
        int baseCost = getNeededStarCost(currentStars, currentStars + starAmount);
        int totalCost = (int) (baseCost * getStarMultiplier(item.getType()));
        if (!cost) {
            if (balance < baseCost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_diamonds",
                        "Majesty, you seem to not have enough. \nYou need `x" + totalCost + "` Diamonds " + EmoteUtil.getDiamond() + " to use this.")));
                return;
            }
        }
        if (!cost) {
            if (balance < totalCost) {
                e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "no_diamonds",
                        "Majesty, you seem to not have enough. \nYou need `x" + totalCost + "` Diamonds " + EmoteUtil.getDiamond() + " to use this.")));
                return;
            }
        }
        int totalStars = currentStars + starAmount;
        int possibleStars = 7 - currentStars;
        if (currentStars >= 7) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "maxed_stars", "Your {item} is currently {star_count}.\n" +
                                    "This item is maxed and can no longer be starred.")
                            .replace("{item}", item.getName() + " " + item.getEmote())
                            .replace("{star_count}", "`" + currentStars + "` " + EmoteUtil.getEmoteMention("S_"))));
            return;
        }
        if (totalStars > 7) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "maxed_stars", "Your {item} is currently {star_count} starred.\n" +
                                    "You can only star it {amount} more times.")
                            .replace("{item}", item.getName() + " " + item.getEmote())
                            .replace("{star_count}", "`" + currentStars + "`")
                            .replace("{amount}", "`" + possibleStars + "` " + EmoteUtil.getEmoteMention("S_"))));
            return;
        }
        if (cost) {
            if (currentStars == 6) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                        Language.getLocalized(uid, "star_cost",
                                        "Your {item} is currently {star_count} starred\n" +
                                                "This will have a cost of {amount} to max out.")
                                .replace("{amount}", "`x" + getNeededPetStarCost(currentStars, 7) + "` " + EmoteUtil.getDiamond())
                                .replace("{item}", item.getName() + " " + item.getEmote())
                                .replace("{star_count}", "`" + currentStars + "`")));
                return;
            }
            if (starAmount <= 1) {
                e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                        Language.getLocalized(uid, "star_cost",
                                        "Your {item} is currently {star_count} starred\n" +
                                                "This will have a cost of {amount} to star once,\n" +
                                                "with a total cost of {max_amount} to `7` star.")
                                .replace("{max_amount}", "`x" + getNeededPetStarCost(currentStars, 7) + "` " + EmoteUtil.getDiamond())
                                .replace("{amount}", "`x" + getNeededPetStarCost(currentStars, currentStars + 1) + "` " + EmoteUtil.getDiamond())
                                .replace("{item}", item.getName() + " " + item.getEmote())
                                .replace("{star_count}", "`" + currentStars + "`")));
                return;
            } else {
                if (currentStars + starAmount > 7) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid),
                            Language.getLocalized(uid, "maxed_stars", "The {item} is currently {star_count} starred.\n" +
                                            "You can only star it {amount} more times.")
                                    .replace("{item}", item.getName() + " " + item.getEmote())
                                    .replace("{star_count}", "`" + currentStars + "`")
                                    .replace("{amount}", "`" + possibleStars + "`")));
                    return;
                }
                e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Cost"),
                        Language.getLocalized(uid, "star_cost",
                                        "Your {item} is currently {star_count} starred\n" +
                                                "This will have a cost of {amount} to star {star_amount} times,\n" +
                                                "with a total cost of {max_amount} to `7` star.")
                                .replace("{star_amount}", "`" + starAmount + "`")
                                .replace("{max_amount}", "`x" + getNeededPetStarCost(currentStars, 7) + "` " + EmoteUtil.getDiamond())
                                .replace("{amount}", "`x" + getNeededPetStarCost(currentStars, currentStars + starAmount) + "` " + EmoteUtil.getDiamond())
                                .replace("{item}", item.getName() + " " + item.getEmote())
                                .replace("{star_count}", "`" + currentStars + "`")));
                return;
            }
        }
        DiamondsUtil.setDiamonds(uid, (long) (balance - totalCost));
        ItemStarringUtil.setItemStar(uid, totalStars, itemID);
        final String newItem = item.getName();
        InventoryUser inv = InventoryUser.getInventoryUser(uid);
        inv.removeItem(itemID);
        inv.addItem(itemID, newItem);
        StringBuilder starDisplay = new StringBuilder();
        for (int i = 0; i < totalStars; i++) {
            starDisplay.append(EmoteUtil.getEmoteMention("S_")).append(" ");
        }
        if (totalStars == 7 && item.getRarity() == Rarity.ANCIENT) {
            Rarity better = item.getRarity().betterOther();
            if (better != null) item.updateRarity(better);
            final String newItem2 = item.getName();
            inv.removeItem(itemID);
            inv.addItem(itemID, newItem2);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "fully_maxed", "Item Starred"),
                    Language.getLocalized(uid, "star_success", "Successfully starred {item} to {star_count}!\n" +
                                    "Your item was of Ancient Quality. Your item is now an Artifact!")
                            .replace("{item}", item.getEmote() + " " + item.getName())
                            .replace("{star_count}", starDisplay.toString())));
            return;
        }
        ItemStarEvent itemStarEvent = new ItemStarEvent(p, starAmount);
        EventManager.callEvent(itemStarEvent);
        e.reply(MessageUtil.success(Language.getLocalized(uid, "star_title", "Item Starred"),
                Language.getLocalized(uid, "star_success", "Successfully starred {item} to {star_count}!")
                        .replace("{item}", item.getEmote() + " " + item.getName())
                        .replace("{star_count}", starDisplay.toString())));
    }


    private double getStarMultiplier(ItemType itemType) {
        switch (itemType) {
            case COPPER:
                return 1.5;
            case REINFORCED:
                return 2;
            case TITANIUM:
                return 2.5;
            case IRON:
                return 3;
            case STEEL:
                return 3.5;
            case CARBON_STEEL:
                return 4;
            case DRAGON_STEEL:
                return 4.5;
            case TITAN_ALLOY:
                return 5;
            case WITHER:
                return 7.5;
            default:
                return 1;
        }
    }

    public static int getNeededStarCost(int currentStars, int stars) {
        int neededCost = 0;
        for (int i = currentStars + 1; i <= stars; i++) {
            neededCost += (100 * i);
            if (i == 7) {
                neededCost += 300;
            }
        }
        return neededCost;
    }

    public static int getNeededPetStarCost(int currentStars, int stars) {
        int neededCost = 0;
        for (int i = currentStars + 1; i <= stars; i++) {
            neededCost += (150 * i);
            if (i == 10) {
                neededCost += 600;
            }
        }
        return neededCost;
    }
}
