package me.affluent.decay.command.quest;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.QuestUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class JournalEntryCommand extends BotCommand {

    private static final HashMap<Integer, String> journalEntries = new HashMap<>();
    private static final HashMap<Integer, String> chapterTitles = new HashMap<>();

    public JournalEntryCommand() {
        this.name = "journal-entry";
        this.aliases = new String[]{"je", "journalentry"};
        this.cooldown = 1.5;
        chapterTitles.put(1, "'Withering'");
        chapterTitles.put(2, "Death is here");
        chapterTitles.put(3, "Safe?");
        chapterTitles.put(4, "Home Sweet Home");
        chapterTitles.put(5, "Ravagers");
        chapterTitles.put(6, "Gold Coins");
        chapterTitles.put(7, "Newbie");
        chapterTitles.put(8, "Blood Shed");
        chapterTitles.put(9, "Huntin' Merchant");
        chapterTitles.put(10, "Gods will be Gods");
        chapterTitles.put(11, "Quiet out here");
        chapterTitles.put(12, "Broken Promises");
        journalEntries.put(1,
                "You might be thinking: What is Withering?\nWell, let me tell you. Withering is neither a place nor a" +
                " person- it's a disease. The world is filled with chaos and destruction.\nThe only thing left are " +
                "murderers looking to survive.\nWithering started 35 years ago in 1568. It rotted everything slowly," +
                " but indefinitely. We lost Structures, loved ones, and civilizations to this disease and theres no " +
                "stopping it. The only thing we can do is survive.");
        journalEntries.put(2,
                "So many have lost their lives. I gradually got used to the smell of rotting corpses.\nKingdoms " +
                "are falling and fading away. New kingdoms are starting to arise, or should I say clans. Some " +
                "are good, attempting to restore chaos.\nHowever, theres the select few who kill for sport, and steal" +
                ". Thankfully theres no witches casting their wicked magic causing blazes. The smell of rotting corpses is already " +
                "bad enough.\nTheres no need for the smell of melting flesh.");
        journalEntries.put(3,
                "Nowhere is safe any more.\nI search and search endlessly, and find either dead corpses or " +
                "other survivors. I don't trust them, I can't... not in this world. I wander around the woods, and " +
                "stick to trails.\nI try to stay away from main roads 'cause many bad clans follow those trails. " +
                "However, I like to stay in eyeshot of them.\nTheres a chance of a merchant to be using them.\nI " +
                "only have some gold coins left, but hopefully that's enough to buy some bread.");
        journalEntries.put(4,
                "I found an old military outpost. Its abandoned and a perfect shelter. It's small, but as long " +
                "as I'm safe, I don't care.\nI should probably use the rest of my gold coins to buy gear. Maybe " +
                "I should just say screw it. Its my life or theirs, and I choose mine.\nI'll go looking for a clan " +
                "soon. Good or bad is fine by me as long as Im fed, safe and well protected.\nI used to be a soldier" +
                " in the Silver Wolves Army.\nWe were some of the best soldiers, but first to fall to this...this plague.");
        journalEntries.put(5,
                "I stumbled upon some survivors. They call themselves the Ravagers.\nSure it sounds vicious on the " +
                "outside, but they are quite nice. Turns out they're not from around here. They came here as " +
                "messengers from stronger kingdoms, and ended up as a small group of survivors. So, as long as they " +
                "keep cooking me food I'll go ahead and protect them.\nWho knows? Maybe Ill just lay low here, " +
                "'protect them', then just kill them. Perhaps I could sell their meat for some gold coins. Could " +
                "just say its deer meat.\nLater on, I will get myself some better gear. I currently only have some " +
                "basic wood gear. Don't get me wrong, it's better than nothing. But, with all these psychos running " +
                "around, a single strike of a strong iron sword and there goes my life.\nI must sleep now. I shall " +
                "write more tomorrow.");
        journalEntries.put(6,
                "You read the title right: Gold Coins.\nI killed them. I killed all four of them, then sold their " +
                "insides for gold coins ha ha.\nAll these fools out here cant mess with me now. I have a reinforced " +
                "sword and full titanium armor. I can take on a whole army. I feel like the new god, as the old " +
                "one's abandoned us. However, Im not a good hunter or cook so, time to find a new clan.\nPerhaps " +
                "one with more flesh would be a good idea.");
        journalEntries.put(7, "These last few days, I was creeping around the kingdom. I found some suitable clans, " +
                              "and some blood thirsty clans.\nI later on decided to join the 'Blood Ravens'. There " +
                              "are so many of them, some are quite weak while others are even stronger than me. From" +
                              " what I have seen this clan is by far the best out here.\nThey have a ranking setup, " +
                              "and lots of members. The leader is ruthless, like a King. He rules with an iron fist.");
        journalEntries.put(8,
                "Theres a bunch of talk out there about clans rising up. Apparently my clan steals from the " +
                "others and kills everyone.\n\n War.\n We went to war with 2 other clans.\nThey killed our leader, and " +
                "others of high rank. It was all for a loyal cause. We broke apart both clans. Not only that, but we" +
                " also raided their camp grounds for some much needed food.\nOh, they chose me to be the new Baron. " +
                "Which is the second highest ranking in this clan. I get to control my own army. I also offer " +
                "suggestions with two other Barons to the King for military actions. " +
                "You might be wondering \"Well, who's the new leader\"?\nOnly 1 Baron survived the war. They " +
                "were the people's choice.");
        journalEntries.put(9,
                "It's been a week since the war.\nWe're recovering at a steady pace. Later on today i'll" +
                " be leading a hunting party. We did not really do much during this week. It has been " +
                "quite quiet since the war. I am back from the hunting party. We did not find much. " +
                "Just a couple rabbits, and a deer. This is not enough for our clan. Thankfully when " +
                "my army was hunting, I found a merchant.\nAt first I just bought myself some extra " +
                "arrows, and I also bought a ceramic steel brick to sharpen my sword. This merchant " +
                "had really good stuff, but I didn't bring much money. This would normally be a " +
                "problem, yet I found a solution.\nI told the merchant to drop his supplies off at the " +
                "clan or die. They did as they were told and I killed him anyways. Free labor " +
                "and free stuff, today was a win/win.\nWhat a great day.");
        journalEntries.put(10,
                "Our leader has been dethroned.\nHe was useless and tried enforcing stupid rules. I was chosen " +
                "as the new leader. I have made quite a name for myself.\nI went from a soldier, to a Baron, to a " +
                "leader in only a month. There has been talk all the clans will rise up and kill us. We can't...I " +
                "can NOT let that happen. If they all come together we are screwed. Our army is still crippled from " +
                "the war. Well, even if that war never happened we would still have no chance. I will attempt to " +
                "bring peace to the clans.\nThe other leaders came here to speak with me. They wanted ME to " +
                "surrender. I called them a bunch of churls and cut off their heads. When the clans came together to" +
                " hear news from all the leaders, they only saw me. I can tell they were confused. I decided to make" +
                " it clear by throwing the heads of the ex-leaders into the crowd. I told them to kneel to the new " +
                "king or die.\nMost kneeled instantly but, others not so fast. I respect that, but I can't have " +
                "dissident soldiers.\nI commanded my army to start killing them 1 by 1 until everyone was kneeling. " +
                "Only 10 had to parish");
        journalEntries.put(11,
                "It's peaceful, quiet. Too Quiet. Our- no, MY clan has evolved. Nobody can or will stop us, ever." +
                "Sorry, I don't know what to write this entry, there's not much to say this time." +
                "I promise I shall write more next time.");
        journalEntries.put(12,
                "Betrayed I was. My own second-hand decided to challenge me trial by combat. I trusted him with my life, now I must put an end to his." +
                " We are meant to fight at dusk little does he know, i'm going to stab them in their sleep! That will show them, that will show them all; " +
                        "never to mess with their rightful ruler!\n\n" +
                "Wounded, I am. They put up a good fight I'll give him that. But, they were weak; a coward. I had an eye open the entire time, knowing they were " +
                        "going to try and kill in my sleep. They never deserved the title of King, they ruled with fear; a disease fueled tactic of ruling. I" +
                        "shall rule with a kind iron fist, one built on love, and strength. Our king is dead, but a new one has begun.");
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "journal-entry <# (chapter)>`")));
            return;
        }
        int chapterNumber = QuestUtil.getCurrentChapter(uid);
        int journalChapter;
        try {
            journalChapter = Integer.parseInt(args[0]);
            if (journalChapter < 1 || journalChapter > 12) {
                return;
            }
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                            .replace("{argument}", "`<Chapter #>`")));
            return;
        }
        if (journalChapter > chapterNumber) {
            String msg1 = Language.getLocalized(uid, "chapter_locked", "This chapter is locked!");
            String msg = Language.getLocalized(uid, "argument_between",
                    "The argument {argument} has to be between {min} and {max}!").replace("{argument}", "<amount>")
                    .replace("{min}", "1").replace("{max}", String.valueOf(chapterNumber));
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1 + "\n" + msg));
            return;
        }
        AtomicBoolean fail = new AtomicBoolean(false);
        u.openPrivateChannel().queue(pc -> pc
                        .sendMessage(MessageUtil.info(chapterTitles.get(journalChapter),
                                journalEntries.get(journalChapter)))
                        .queue(success -> e
                                .reply(Language.getLocalized(uid, "dm_success",
                                        "{user_mention}, please check your " + "DM's!")
                                        .replace("{user_mention}", u.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">")), failure -> fail.set(true))),
                failure -> fail.set(true));
        if (fail.get()) {
            e.reply(Language.getLocalized(uid, "dm_failure",
                    "{user_mention}, I couldn't send you a DM! Please check your settings and try again.")
                    .replace("{user_mention}", u.getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">")));
        }
    }
}