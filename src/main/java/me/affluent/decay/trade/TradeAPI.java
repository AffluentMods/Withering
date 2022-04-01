package me.affluent.decay.trade;

import me.affluent.decay.Withering;
import me.affluent.decay.command.utility.TradeCommand;
import me.affluent.decay.item.Item;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TradeAPI {

    private static HashMap<String, TimerTask> timeoutTimers = new HashMap<>();
    private static HashMap<String, TimerTask> tradeTimers = new HashMap<>();
    private static HashMap<String, Trade> trades = new HashMap<>();
    private static HashMap<String, String> requests = new HashMap<>();

    public static boolean isInTrade(String uid) {
        return trades.containsKey(uid);
    }

    public static boolean hasTradeRequest(String uid) {
        return requests.containsKey(uid);
    }

    public static void setTrade(String uid, Trade trade) {
        trades.remove(uid);
        if (trade != null) trades.put(uid, trade);
    }

    public static void setRequests(String uid, String from) {
        requests.remove(uid);
        if (from != null) requests.put(uid, from);
    }

    public static void runTimeoutTimer(String uid) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (timeoutTimers.containsValue(this)) {
                    setRequests(uid, null);
                }
                timeoutTimers.remove(uid);
            }
        };
        new Timer().schedule(timerTask, 300000);
        timeoutTimers.put(uid, timerTask);
    }

    public static void cancelTimeoutTimer(String uid) {
        if (timeoutTimers.containsKey(uid)) {
            timeoutTimers.get(uid).cancel();
            timeoutTimers.remove(uid);
        }
    }

    public static void runTradeTimer(String uid) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (tradeTimers.containsValue(this)) {
                    if (isInTrade(uid)) {
                        TradeCommand.cancelTrade(getTrade(uid));
                        setTrade(uid, null);
                    }
                }
                tradeTimers.remove(uid);
            }
        };
        new Timer().schedule(timerTask, 600000);
        tradeTimers.put(uid, timerTask);
    }

    public static void cancelTradeTimer(String uid) {
        if (tradeTimers.containsKey(uid)) {
            tradeTimers.get(uid).cancel();
            tradeTimers.remove(uid);
        }
    }

    public static String getRequestFrom(String uid) {
        return requests.get(uid);
    }

    public static Trade getTrade(String uid) {
        return trades.get(uid);
    }

    private static final long tradeLogChannelID = 889385920518819860L;

    public static void logTrade(Trade trade) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Trade Completed");
        StringBuilder t1Items = new StringBuilder();
        HashMap<Long, Item> t1ItemsMap = trade.getItemsP1();
        HashMap<Long, Pets> t1PetItemsMap = trade.getPetItemsP1();
        User t1 = trade.getPlayer1().getUser();
        for (long tpetsID : t1PetItemsMap.keySet()) {
            Pets petItem = t1PetItemsMap.get(tpetsID);
            String tPetItem = petItem.getPetName();
            t1Items.append("[`").append(tpetsID).append("`] ").append(petItem.getPetEmote()).append(" ")
                    .append(tPetItem.replace("_", " ").toLowerCase()).append(" ").append(petItem.getPetStars(t1.getId(), String.valueOf(tpetsID))).append(" ")
                    .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(petItem.getPetLevel(t1.getId(), String.valueOf(tpetsID))).append("\n");
        }
        for (long tID : t1ItemsMap.keySet()) {
            Item item = t1ItemsMap.get(tID);
            String tItem = item.getName();
            t1Items.append("[`").append(tID).append("`] ").append(item.getEmote()).append(" ")
                    .append(tItem).append(" ").append(item.getSpecificStars(t1.getId(), Math.toIntExact(tID))).append(" ")
                    .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(item.getSpecificLevel(t1.getId(), Math.toIntExact(tID))).append("\n");
        }
        if (t1Items.toString().endsWith(" | ")) t1Items = new StringBuilder(t1Items.substring(0, t1Items.length() - 3));
        if (trade.tl1 > 0) {
            eb.addField("Trader", t1.getAsTag() + " - `" + t1.getId() + "`\n" + EmoteUtil.getCoin() + " `x" +
                    FormatUtil.formatCommas(trade.tl1) + "`\n" + t1Items, false);
        }
        if (trade.tl1 == 0) {
            eb.addField("Trader", t1.getAsTag() + " - `" + t1.getId() + "`\n" + t1Items, false);
        }
        //
        StringBuilder t2Items = new StringBuilder();
        HashMap<Long, Item> t2ItemsMap = trade.getItemsP2();
        HashMap<Long, Pets> t2PetItemsMap = trade.getPetItemsP2();
        User t2 = trade.getPlayer2().getUser();
        for (long tpetsID : t2PetItemsMap.keySet()) {
            Pets petItem = t2PetItemsMap.get(tpetsID);
            String tPetItem = petItem.getPetName();
            t2Items.append("[`").append(tpetsID).append("`] ").append(petItem.getPetEmote()).append(" ")
                    .append(tPetItem.replace("_", " ").toLowerCase()).append(" ").append(petItem.getPetStars(t2.getId(), String.valueOf(tpetsID))).append(" ")
                    .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(petItem.getPetLevel(t2.getId(), String.valueOf(tpetsID))).append("\n");
        }
        for (long tID : t2ItemsMap.keySet()) {
            Item item = t2ItemsMap.get(tID);
            String tItem = item.getName();
            t2Items.append("[`").append(tID).append("`] ").append(item.getEmote()).append(" ")
                    .append(tItem).append(" ").append(item.getSpecificStars(t2.getId(), Math.toIntExact(tID))).append(" ")
                    .append(EmoteUtil.getEmoteMention("S_")).append(" Lvl ").append(item.getSpecificLevel(t2.getId(), Math.toIntExact(tID))).append("\n");
        }
        if (t2Items.toString().endsWith(" | ")) t2Items = new StringBuilder(t2Items.substring(0, t2Items.length() - 3));
        if (trade.tl2 > 0) {
            eb.addField("Traded with", t2.getAsTag() + " - `" + t2.getId() + "`\n" + EmoteUtil.getCoin() + " `x" +
                    FormatUtil.formatCommas(trade.tl2) + "`\n" + t2Items, false);
        }
        if (trade.tl2 == 0) {
            eb.addField("Traded with", t2.getAsTag() + " - `" + t2.getId() + "`\n" + t2Items, false);
        }
        //
        TextChannel tradeLog = Withering.getBot().getShardManager().getTextChannelById(tradeLogChannelID);
        if (tradeLog != null) tradeLog.sendMessage(eb.build()).queue();

    }
}