package me.affluent.decay.vote;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.holidayevent.HolidayEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.ZoneId;
import java.util.*;

public class VoteClient {

    public static boolean lastWeekend = false;
    private static final List<String> voteQueue = new ArrayList<>();

    VoteClient(final Socket socket, final InputStream in, final long id) {
        final DataInputStream dis = new DataInputStream(in);
        try {
            boolean authorized = false;
            while (socket != null && !socket.isClosed()) {
                @SuppressWarnings("deprecation") final String line = dis.readLine();
                if (line == null) continue;
                if (line.startsWith("Authorization: ")) {
                    if (line.substring("Authorization: ".length()).equals(Constants.dbl_vote_auth)) {
                        authorized = true;
                    }
                }
                if (line.startsWith("{")) {
                    final JSONObject json = new JSONObject(line);
                    final String uid = json.getString("user");
                    final boolean weekend = json.getBoolean("isWeekend");
                    lastWeekend = weekend;
                    vote(uid, weekend, authorized);
                    //
                    dis.close();
                    socket.close();
                    VoteServer.close(id);
                    break;
                }
            }
        } catch (SocketException | SocketTimeoutException ignored) {
            VoteServer.close(id);
        } catch (Exception ex) {
            VoteServer.close(id);
            ex.printStackTrace();
        }
    }

    public static void vote(final String uid, final boolean authorized) {
        vote(uid, isWeekend(), authorized);
    }

    public static void vote(final String uid, final boolean weekend, final boolean authorized) {
        if (!authorized) {
            return;
        }
        final User u = Withering.getBot().getShardManager().retrieveUserById(uid).complete();
        if (u != null) {
            if (voteQueue.contains(uid)) return;
            if (!VoteSystem.hasVoted(uid)) {
                voteQueue.add(uid);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        voteQueue.remove(uid);
                    }
                }, 7544);
                if (!Player.playerExists(uid)) {
                    return;
                }
                final Player p = Player.getPlayer(uid);
                InventoryUser inv = p.getInventoryUser();
                long now = System.currentTimeMillis();
                final long diff = 43200000L;
                long until = now + diff;
                String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
                boolean godlyOccurence = new Random().nextInt(100) <= 1;
                boolean junkMongolianHorse = new Random().nextInt(100) <= 10;
                boolean commonMongolianHorse = new Random().nextInt(100) <= 5;
                boolean uncommonMongolianHorse = new Random().nextInt(100) <= 3;
                boolean commonAndalusianHorse = new Random().nextInt(200) <= 10;
                boolean uncommonAndalusianHorse = new Random().nextInt(200) <= 5;
                boolean rareAndalusianHorse = new Random().nextInt(200) <= 3;
                boolean uncommonShireHorse = new Random().nextInt(300) <= 10;
                boolean rareShireHorse = new Random().nextInt(300) <= 5;
                boolean epicShireHorse = new Random().nextInt(300) <= 3;
                int level = p.getExpUser().getLevel();
                boolean horse1 = junkMongolianHorse || commonMongolianHorse || uncommonMongolianHorse;
                boolean horse2 = commonAndalusianHorse || uncommonAndalusianHorse || rareAndalusianHorse;
                boolean horse3 = uncommonShireHorse || rareShireHorse || epicShireHorse;
                String earnedPet = "";
                if (level >= 1 && level < 20 && horse1) {
                    if (junkMongolianHorse) {
                        p.getPetUtil().addPet("junk mongolian horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Junk_Mongolian_Horse") + " Junk Mongolian Horse";
                    }
                    if (commonMongolianHorse && earnedPet.equalsIgnoreCase("")) {
                        p.getPetUtil().addPet("common mongolian horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Common_Mongolian_Horse") + " Common Mongolian Horse";
                    }
                    if (uncommonMongolianHorse && earnedPet.equalsIgnoreCase("")) {
                        p.getPetUtil().addPet("uncommon mongolian horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Uncommon_Mongolian_Horse") + " Uncommon Mongolian Horse";
                    }
                }
                if (level >= 20 && level < 50 && horse2) {
                    if (commonAndalusianHorse) {
                        p.getPetUtil().addPet("common andalusian horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Common_Andalusian_Horse") + " Common Andalusian Horse";
                    }
                    if (uncommonAndalusianHorse && earnedPet.equalsIgnoreCase("")) {
                        p.getPetUtil().addPet("uncommon andalusian horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Uncommon_Andalusian_Horse") + " Uncommon Andalusian Horse";
                    }
                    if (rareAndalusianHorse && earnedPet.equalsIgnoreCase("")) {
                        p.getPetUtil().addPet("rare andalusian horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Rare_Andalusian_Horse") + " Rare Andalusian Horse";
                    }
                }
                if (level >= 50 && horse3) {
                    if (uncommonShireHorse) {
                        p.getPetUtil().addPet("uncommon shire horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Uncommon_Shire_Horse") + " Uncommon Shire Horse";
                    }
                    if (rareShireHorse && earnedPet.equalsIgnoreCase("")) {
                        p.getPetUtil().addPet("rare shire horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Rare_Shire_Horse") + " Rare Shire Horse";
                    }
                    if (epicShireHorse && earnedPet.equalsIgnoreCase("")) {
                        p.getPetUtil().addPet("epic shire horse", 1);
                        earnedPet += "\n`1x` " + EmoteUtil.getEmoteMention("Epic_Shire_Horse") + " Epic Shire Horse";
                    }
                }
                boolean isEventActive = HolidayEvent.isEventActive();
                int fireworks = 0;
                if (isEventActive) {
                    HolidayEvent holidayEvent = HolidayEvent.getCurrentEvent();
                    if (holidayEvent.getName().equalsIgnoreCase("new years")) {
                        fireworks = 1;
                    }
                }
                int tokens = 0;
                int goldenTokens = 0;
                if (godlyOccurence) {
                    tokens = (getTokenReward(weekend) + (DonatorUtil.isBoosting(uid) ? 1 : 0)) * 2;
                    goldenTokens = (getGoldenTokenReward(weekend)) * 2;
                } else {
                    tokens = getTokenReward(weekend) + (DonatorUtil.isBoosting(uid) ? 1 : 0);
                    goldenTokens = (getGoldenTokenReward(weekend));
                }
                String goldToken = "";
                if (goldenTokens >= 1) {
                    goldToken = "\n`" + goldenTokens + "x` " + EmoteUtil.getEmoteMention("Golden_Tavern_Token");
                }
                String fireworkString = "";
                if (fireworks >= 1) {
                    fireworkString = "\n`" + fireworks + "x` " + EmoteUtil.getEmoteMention("Holiday_Firework");
                }
                FireworksUtil.addHolidayFireworks(uid, fireworks);
                TokensUtil.addTokens(uid, tokens);
                GoldenTokensUtil.addGoldenTokens(uid, goldenTokens);
                final String tokenEmote = EmoteUtil.getEmoteMention("Tavern_Token");
                final String finalRewardsDisplay = /*rewardsDisplay.toString()*/
                        "`" + tokens + "x` " + tokenEmote + goldToken + fireworkString + earnedPet + "\n`" + userPrefix +
                        "daily` - Can be used every `24` hours";
                VoteSystem.addVote(u.getId(), until);
                String weekendVote = Language.getLocalized(u.getId(), "weekend_vote", "Weekend Vote");
                String vote = Language.getLocalized(u.getId(), "vote", "Vote");
                u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.success(weekend ? weekendVote : vote,
                        Language.getLocalized(u.getId(), "vote_received_message",
                                "Thank you for upvoting Withering!\n\n__Here are your rewards:__\n{rewards}")
                                .replace("{rewards}", finalRewardsDisplay))).queue());
            }
        }
    }

    public static int getTokenReward(boolean weekend) {
        return weekend ? 12 : 7;
    }

    public static int getGoldenTokenReward(boolean weekend) {
        return weekend ? 1 : 0;
    }

    public static final int expBoost = 20;

    public static HashMap<String, Long> getVoteRewards(int level) {
        HashMap<String, Long> voteRewards = new HashMap<>();
        voteRewards.put("exp-boost", (long) expBoost);
        return voteRewards;
    }

    public static boolean isWeekend() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        int dow = c.get(Calendar.DAY_OF_WEEK);
        return dow == Calendar.SATURDAY || dow == Calendar.SUNDAY;
    }
}