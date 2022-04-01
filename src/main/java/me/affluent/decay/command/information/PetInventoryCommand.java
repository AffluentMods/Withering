package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.enums.*;
import me.affluent.decay.language.Language;
import me.affluent.decay.pets.PetExpUser;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PetInventoryCommand extends BotCommand {

    public PetInventoryCommand() {
        this.name = "pets";
        this.aliases = new String[]{"pet"};
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
        PetRarity sortR = null;
        int page = 1;
        String[] args = e.getArgs();
        boolean isMentioned = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String previousArg = i > 0 ? args[i - 1] : null;
                String arg = args[i];
                if (i == (args.length - 1)) {
                    try {
                        page = Integer.parseInt(arg);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (arg.length() == 18) {
                    try {
                        long targetId = Long.parseLong(arg);
                        uid = String.valueOf(targetId);
                        isMentioned = true;
                    } catch (NumberFormatException ignore) {
                    }
                }
                String farg = previousArg != null ? previousArg + " " + arg : arg;
                if (PetRarity.isPetRarity(farg)) {
                    sortR = PetRarity.getPetRarity(farg);
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
        MessageEmbed inventory = getInventory(p, page, sortR);
        e.reply(inventory);
    }


    private MessageEmbed getInventory(Player p, int page, PetRarity sortRarity) {
        String uid = p.getUserId();
        EmbedBuilder eb = new EmbedBuilder();
        PetUtil inv = p.getPetUtil();
        TreeMap<Long, String> pets = new TreeMap<>(inv.getPets());
        List<Long> sortedPets = sortRarity != null ? inv.getPetsSorted(sortRarity) : null;
        ArrayList<String> petIDStrings = new ArrayList<>();
        if (sortedPets != null) {
            for (long petID : sortedPets) {
                petIDStrings.add(String.valueOf(petID));
            }
        } else {
            for (long petID : pets.descendingKeySet())
                petIDStrings.add(String.valueOf(petID));
        }
        StringBuilder description = new StringBuilder();
        if (page < 1) page = 1;
        int offset = page * 15 - 15;
        int end = offset + 15;
        if (end > pets.size()) end = pets.size();
        for (int i = offset; i < end; i++) {
            long petID = Long.parseLong(petIDStrings.get(i));
            String pet = pets.get(petID);
            Pets petObj = PetUtil.getPetByID(petID);
            int petStars = PetStarringUtil.getPetStar(p.getUserId(), petID);
            int petLevel = PetExpUser.getPetExpUser(uid).getPetLevel(p.getUserId(), Math.toIntExact(petID));
            if (petObj == null) description.append("[").append(petID).append("`] null\n");
            else {
                String display = petObj.getPetName();
                description.append("[`").append(petID).append("`] ").append(EmoteUtil.getEmoteMention(display))
                        .append(" ").append(pet.replace("_", " ").toLowerCase()).append(" ").append(EmoteUtil.getEmoteMention("S_")).append(" ").append(petStars)
                        .append(" Lvl ").append(petLevel).append("\n");
            }
        }
        String prefix = PrefixUser.getPrefixUser(uid).getPrefix();
        long maxPage = getMaxPage(inv.getPetSize());
        eb.setTitle(
                Language.getLocalized(uid, "pets", "Pets") + " " + Language.getLocalized(uid, "of", "of") +
                        " " + p.getUser().getAsTag() + " [" + page + "/" + maxPage + "]");
        eb.setDescription(description.toString());
        eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
        return eb.build();
    }

    private int getMaxPage(int size) {
        double s = size / 15.0;
        int maxPage = new Double("" + s).intValue();
        if (size % 15 != 0) maxPage++;
        if (maxPage < 1) maxPage = 1;
        return maxPage;
    }
}
