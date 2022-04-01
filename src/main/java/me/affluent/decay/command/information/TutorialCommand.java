package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.*;
import me.affluent.decay.language.Language;
import me.affluent.decay.listener.EventListener;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.TutorialUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TutorialCommand extends BotCommand implements EventListener {

    public TutorialCommand() {
        this.name = "tutorial";
        this.aliases = new String[]{"tut"};
        this.cooldown = 1.5;
    }

    private static final List<String> tutorialAttack = new ArrayList<>();

    private static final List<String> tutorialChest = new ArrayList<>();

    private static final List<String> tutorialEquip = new ArrayList<>();

    private static final List<String> tutorialUnequip = new ArrayList<>();

    private static final List<String> tutorialTavern = new ArrayList<>();

    private static final List<String> tutorialScroll = new ArrayList<>();

    private static final List<String> tutorialLevel = new ArrayList<>();

    private static final List<String> tutorialStar = new ArrayList<>();

    private static final List<String> tutorialForge = new ArrayList<>();

    private static TextChannel textChannel;
    private static String itemFinalName;
    private static long itemID;

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        Player p = Player.getPlayer(uid);
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        if (args.length < 1) {
            tutorialAttack.add(uid);
            if (!TutorialUtil.getInitialReward(uid).equalsIgnoreCase("claimed")) {
                ScrollsUtil.addScrolls(uid, 1);
                KeysUtil.addKeys(uid, "Wood Key", 1);
                p.getEcoUser().addBalance(250);
                DiamondsUtil.addDiamonds(uid, 100);
                IngotUtil.addIngots(uid, 2);
                TokensUtil.addTokens(uid, 1);
                TutorialUtil.setInitialReward(uid, "claimed");
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            if (!TutorialUtil.getFinalReward(uid).equalsIgnoreCase("claimed")) {
                eb.setDescription(Language.getLocalized(uid, "tutorial",
                        "Welcome to Withering's Tutorial.\n " +
                                "Upon completing the tutorial you will be rewarded some amazing items to get you started.\n" +
                                "To begin the tutorial, use `" + userPrefix + "random` to attack a random player."));
            } else {
                eb.setDescription(Language.getLocalized(uid, "tutorial",
                        "Welcome to Withering's Tutorial.\n " +
                                "Normally you would receive rewards upon completion, however you already finished the tutorial.\n" +
                                "To begin the tutorial, use `" + userPrefix + "random` to attack a random player."));
            }
            eb.setFooter("For further information use: " + userPrefix + "help random");
            textChannel = e.getTextChannel();
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onFightEndEvent(FightEndEvent event) {
        String uid = event.getAttacker().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialAttack.contains(uid)) {
            tutorialAttack.remove(uid);
            tutorialChest.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Great!\n" +
                    "Perhaps you should try opening some chests to get better gear.\n" +
                    "Use `" + userPrefix + "open wood` to open a wood chest!"));
            eb.setFooter("For further information use: " + userPrefix + "help open");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onChestOpenEvent(ChestOpenEvent event) {
        String uid = event.getOpener().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        int upgradeRarity = new Random().nextInt(5);
        String itemName = "";
        long id = 0;
        if (tutorialChest.contains(uid)) {
            tutorialChest.remove(uid);
            tutorialEquip.add(uid);
            switch (upgradeRarity) {
                case 0:
                    id = p.getInventoryUser().addItemReturn("junk wood helmet", 1);
                    itemName = "Helmet";
                    break;
                case 1:
                    id = p.getInventoryUser().addItemReturn("junk wood chestplate", 1);
                    itemName = "Chestplate";
                    break;
                case 2:
                    id = p.getInventoryUser().addItemReturn("junk wood gloves", 1);
                    itemName = "Gloves";
                    break;
                case 3:
                    id = p.getInventoryUser().addItemReturn("junk wood trousers", 1);
                    itemName = "Trousers";
                    break;
                case 4:
                    id = p.getInventoryUser().addItemReturn("junk wood boots", 1);
                    itemName = "Boots";
                    break;
            }
            itemFinalName = itemName;
            itemID = id;
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Great!\n" +
                    "Now that you have some new items, why don't you go ahead and equip them.\n" +
                    "Use `" + userPrefix + "inv`, You should see this;\n" +
                    "[{id}] {emote} {name} " + EmoteUtil.getEmoteMention("S_") + " 0 Lvl 1\n\n" +
                            "If you look on the left, there's an ID, this is what you use to equip items. So go ahead and use,\n" +
                            "`" + userPrefix + "equip " + id + "` to equip your Junk Wood " + itemName)
                    .replace("{id}", "`" + id + "`")
                    .replace("{emote}", EmoteUtil.getEmoteMention("Junk Wood " + itemName))
                    .replace("{name}", "Junk Wood " + itemName));
            eb.setFooter("For further information use: " + userPrefix + "help equip");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onEquipEvent(EquipEvent event) {
        String uid = event.getEquipper().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialEquip.contains(uid)) {
            tutorialEquip.remove(uid);
            tutorialUnequip.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Nice job!\n" +
                            "Now that you know how to equip your items, let's go ahead and learn how to unequip them.\n" +
                    "Nothing quite like the shock of needing the restroom with your trousers stuck on.\n" +
                    "Use `" + userPrefix + "unequip " + itemFinalName.toLowerCase() + "`"));
            eb.setFooter("For further information use: " + userPrefix + "help unequip");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onUnequipEvent(UnequipEvent event) {
        String uid = event.getUnequipper().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialUnequip.contains(uid)) {
            tutorialUnequip.remove(uid);
            tutorialLevel.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Easy!\n" +
                    "Now that you know how to unequip your items, let's learn how to make them far more powerful!\n" +
                    "Use `" + userPrefix + "level " + itemID + "`\n" +
                    "You can also use `" + userPrefix + "level " + itemID + " 10` to level it 10 times.\n" +
                    "It's not recommend to spend too much " + EmoteUtil.getCoin() + " on leveling items, especially on wood."));
            eb.setFooter("For further information use: " + userPrefix + "help level");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onItemLevelUpEvent(ItemLevelUpEvent event) {
        String uid = event.getLeveler().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialLevel.contains(uid)) {
            tutorialLevel.remove(uid);
            tutorialStar.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Getting There!\n" +
                    "Wood items are not the best to spend resources on for upgrading, however it is the best you can currently equip.\n" +
                    "Let's star this item by using\n`" +
                    userPrefix + "star " + itemID + "`\n" +
                    "Just like leveling, you can also use an amount at the end of the command.\n" +
                    "You can also star pets in a similar fashion using `" + userPrefix + "star pet <petId> [amount]`"));
            eb.setFooter("For further information use: " + userPrefix + "help star");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onItemStarEvent(ItemStarEvent event) {
        String uid = event.getStarrer().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialStar.contains(uid)) {
            tutorialStar.remove(uid);
            tutorialForge.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Almost There!\n" +
                    "Now lets forge this item into a whole other rarity. " +
                    "Rarities are good for any material as the material doesn't effect the outcome of the effect, while starring and leveling does.\n" +
                    "Use `" + userPrefix + "forge " + itemID + " confirm`\n" +
                    "If you don't like using `confirm` a lot, go ahead and change it in the `" + userPrefix + "settings`\n" +
                    "Use `" + userPrefix + "settings confirm disable`"));
            eb.setFooter("For further information use: " + userPrefix + "help forge");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onForgeEvent(ForgeEvent event) {
        String uid = event.getForger().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialForge.contains(uid)) {
            tutorialForge.remove(uid);
            tutorialScroll.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Perfect!\n" +
                    "Now that's quite a fine piece you got there. Let's try scrolling this item.\n" +
                    "Scrolling items changes the item type (Helmet, Chestplate, Sword, Shield, etc) to a different item type. \n" +
                    "It also has a small chance to increase the rarity, let's try it!\n" +
                    "Use `" + userPrefix + "scroll " + itemID + "`"));
            eb.setFooter("For further information use: " + userPrefix + "help scroll");
            textChannel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onScrollEvent(ScrollEvent event) {
        String uid = event.getScroller().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialScroll.contains(uid)) {
            tutorialScroll.remove(uid);
            tutorialTavern.add(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "tutorial", "Astonishing!\n" +
                    "What an astoundingly beautiful piece. With this knowledge you surely will never fail.\n" +
                    "Go ahead and re-equip that item\n" +
                    "Use `" + userPrefix + "equip " + itemID + "`\n" +
                    "Now let's go to the tavern and test our luck!\n" +
                    "Are you feeling lucky? I'm feeling lucky, use `" + userPrefix + "tavern spin`\n" +
                    "You can also use `" + userPrefix + "tavern spin 100` to spin 100 times, which is the maximum amount."));
            eb.setFooter("For further information use: " + userPrefix + "help tavern");
            textChannel.sendMessage(eb.build()).queue();
        }

    }

    @Override
    public void onTavernSpinEvent(TavernSpinEvent event) {
        String uid = event.getSpinner().getUserId();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (tutorialTavern.contains(uid)) {
            tutorialTavern.remove(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "tutorial_plain", "Tutorial"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            if (!TutorialUtil.getFinalReward(uid).equalsIgnoreCase("claimed")) {
                eb.setDescription(Language.getLocalized(uid, "tutorial", "Completed!\n" +
                        "You have completed the tutorial!\n" +
                        "Kindly take your much deserved rewards;\n"
                        + EmoteUtil.getEmoteMention("Junk_Bee") + " Junk Bee pet `x1`\n"
                        + EmoteUtil.getCoin() + " Gold Coin `x250`\n"
                        + EmoteUtil.getDiamond() + " Diamonds `x100`\n"
                        + EmoteUtil.getEmoteMention("Tavern Token") + " Tavern Token `x5`\n "
                        + EmoteUtil.getEmoteMention("Golden Tavern Token") + " Golden Tavern Token `x1`\n "
                        + EmoteUtil.getEmoteMention("Alloy_Ingot") + " Alloy Ingot `x5`\n"
                        + EmoteUtil.getEmoteMention("Wood Key") + " Wood Key `x2`\n "
                        + EmoteUtil.getEmoteMention("Scroll") + " Scrolls `x1`"));
            } else {
                eb.setDescription(Language.getLocalized(uid, "tutorial", "Completed!\n" +
                        "You have already completed the tutorial!"));
            }
            if (!TutorialUtil.getFinalReward(uid).equalsIgnoreCase("claimed")) {
                PetUtil.getPetUtil(uid).addPet("JUNK_BEE", 1);
                p.getEcoUser().addBalance(250);
                KeysUtil.addKeys(uid, "Wood Key", 2);
                IngotUtil.addIngots(uid, 5);
                TokensUtil.addTokens(uid, 5);
                GoldenTokensUtil.addGoldenTokens(uid, 1);
                ScrollsUtil.addScrolls(uid, 2);
                DiamondsUtil.addDiamonds(uid, 100);
                TutorialUtil.setFinalReward(uid, "claimed");
            }
            eb.setFooter("For further information use: " + userPrefix + "help");
            textChannel.sendMessage(eb.build()).queue();
        }
    }
}


