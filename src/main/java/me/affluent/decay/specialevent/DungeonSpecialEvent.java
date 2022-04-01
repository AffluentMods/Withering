package me.affluent.decay.specialevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.language.Language;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DungeonSpecialEvent extends SpecialEvent {

    public DungeonSpecialEvent() {
        super("Dungeon", "Complete dungeons to go up the leaderboard.\n" + Constants.TAB + "- `" + Constants.PREFIX +
                "dungeon run`.", "Complete dungeons to go up the leaderboard", "" +
                "1st: - 15% " + EmoteUtil.getCoin() + " | " + "5,000 " + EmoteUtil.getDiamond() + " | " + "2 " + EmoteUtil.getEmoteMention("Titan Alloy Key")
                + " | " + "5 " + EmoteUtil.getEmoteMention("Dragon Steel Key") + " | " + "3 " + EmoteUtil.getEmoteMention("Dragon Steel Scroll")
                + " | " + "15 " + EmoteUtil.getEmoteMention("Iron Scroll") + " | " + "20 " + EmoteUtil.getEmoteMention("Scroll") + "\n\n" +

                "2nd - 5th: 10% " + EmoteUtil.getCoin() + " | " + "3,750 " + EmoteUtil.getDiamond() + " | " + "1 " + EmoteUtil.getEmoteMention("Titan Alloy Key")
                + " | " + "2 " + EmoteUtil.getEmoteMention("Dragon Steel Key") + " | " + "1 " + EmoteUtil.getEmoteMention("Dragon Steel Scroll")
                + " | " + "8 " + EmoteUtil.getEmoteMention("Iron Scroll") + " | " + "8 " + EmoteUtil.getEmoteMention("Scroll") + "\n\n" +

                "6th - 20th: - 7.5% " + EmoteUtil.getCoin() + " | " + "2,000 " + EmoteUtil.getDiamond() + " | " + "1 " + EmoteUtil.getEmoteMention("Dragon Steel Key")
                + " | " + "5 " + EmoteUtil.getEmoteMention("Iron Scroll") + " | " + "3 " + EmoteUtil.getEmoteMention("Scroll") + "\n\n" +

                "21st - 50th: - 4% " + EmoteUtil.getCoin() + " | " + "1,500 " + EmoteUtil.getDiamond() + " | " + "2 " + EmoteUtil.getEmoteMention("Iron Scroll")
                + " | " + "3 " + EmoteUtil.getEmoteMention("Scroll") + "\n\n" +

                "51st - 100th: - 2.5% " + EmoteUtil.getCoin() + " | " + "500 " + EmoteUtil.getDiamond() + " | " + "1 " + EmoteUtil.getEmoteMention("Iron Scroll")
                + " | " + "1 " + EmoteUtil.getEmoteMention("Scroll") + "\n\n" +

                "101st +: - 1% " + EmoteUtil.getCoin() + " | " + "350 " + EmoteUtil.getDiamond());
    }

    @Override
    public void giveRewards() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId, points FROM specialevent;")) {
            while (rs.next()) {
                try (ResultSet resultSet = Withering.getBot().getDatabase().query("SELECT * FROM specialevent ORDER BY points DESC LIMIT 100;")) {
                    while (resultSet.next()) {

                    }
                }
                String userId = rs.getString("userId");
                int points = rs.getInt("points");
                int place = 0;
                int totalPlayers = 0;
                Player p = Player.getPlayer(userId);
                BigInteger add1 = BigInteger.valueOf(1000);
                BigInteger add2 = BigInteger.valueOf(1000);
                BigInteger add3 = BigInteger.valueOf(1000);
                BigInteger add4 = BigInteger.valueOf(1000);
                BigInteger add5 = BigInteger.valueOf(1000);
                BigInteger add6 = BigInteger.valueOf(1000);
                BigInteger pg1 =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.15)).toBigInteger();
                BigInteger pg2 =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.10)).toBigInteger();
                BigInteger pg3 =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.075)).toBigInteger();
                BigInteger pg4 =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.04)).toBigInteger();
                BigInteger pg5 =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.025)).toBigInteger();
                BigInteger pg6 =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.01)).toBigInteger();
                add1 = add1.add(pg1);
                add2 = add2.add(pg2);
                add3 = add3.add(pg3);
                add4 = add4.add(pg4);
                add5 = add5.add(pg5);
                add6 = add6.add(pg6);
                BigInteger fullAmountToAdd = BigInteger.ZERO;
                int diamonds = 0;
                int scrolls = 0;
                int ironScrolls = 0;
                int dragonScrolls = 0;
                int titanAlloyKeys = 0;
                int dragonSteelKeys = 0;
                int witherScroll = 0;
                //
                if (place == 1) {
                    fullAmountToAdd = fullAmountToAdd.add(add1);
                    diamonds += 5000;
                    titanAlloyKeys += 2;
                    dragonSteelKeys += 5;
                    dragonScrolls += 3;
                    ironScrolls += 15;
                    scrolls += 20;
                    witherScroll += 1;
                }
                if (place > 1 || place < 6) {
                    fullAmountToAdd = fullAmountToAdd.add(add2);
                    diamonds += 3750;
                    titanAlloyKeys += 1;
                    dragonSteelKeys += 2;
                    dragonScrolls += 1;
                    ironScrolls += 8;
                    scrolls += 8;
                }
                if (place > 5 || place < 21) {
                    fullAmountToAdd = fullAmountToAdd.add(add3);
                    diamonds += 2500;
                    dragonSteelKeys += 1;
                    ironScrolls += 5;
                    scrolls += 3;
                }
                if (place > 20 || place < 51) {
                    fullAmountToAdd = fullAmountToAdd.add(add4);
                    diamonds += 1500;
                    ironScrolls += 2;
                    scrolls += 3;
                }
                if (place > 50 || place < 101) {
                    fullAmountToAdd = fullAmountToAdd.add(add5);
                    diamonds += 500;
                    ironScrolls += 1;
                    scrolls += 1;
                }
                if (place > 100) {
                    fullAmountToAdd = fullAmountToAdd.add(add6);
                    diamonds += 350;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 1) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 1) {
                    specialEvent.append("Sadly you didn't obtain any rewards.\n");
                }
                if (fullAmountToAdd.compareTo(BigInteger.ZERO) > 0)
                    if (fullAmountToAdd.compareTo(BigInteger.valueOf(250000)) > 0) {
                        int finalMoneyReward = 250000;
                        p.getEcoUser().addBalance(finalMoneyReward);
                        specialEvent.append(EmoteUtil.getCoin()).append(" `x").append(finalMoneyReward).append("` Gold Coins\n");
                    } else {
                        p.getEcoUser().addBalance(fullAmountToAdd);
                        specialEvent.append(EmoteUtil.getCoin()).append(" `x").append(fullAmountToAdd).append("` Gold Coins\n");
                    }
                if (diamonds > 0) {
                    DiamondsUtil.addDiamonds(userId, diamonds);
                    specialEvent.append(EmoteUtil.getDiamond()).append(" `x").append(diamonds).append("` Diamonds\n");
                }
                if (scrolls > 0) {
                    ScrollsUtil.addScrolls(userId, scrolls);
                    specialEvent.append(EmoteUtil.getEmoteMention("Scroll")).append(" `x").append(scrolls).append("` Scrolls\n");
                }
                if (ironScrolls > 0) {
                    IronScrollsUtil.addIronScrolls(userId, ironScrolls);
                    specialEvent.append(EmoteUtil.getEmoteMention("Iron Scroll")).append(" `x").append(ironScrolls).append("` Iron Scrolls\n");
                }
                if (dragonScrolls > 0) {
                    DragonScrollsUtil.addDragonScrolls(userId, dragonScrolls);
                    specialEvent.append(EmoteUtil.getEmoteMention("Dragon Steel Scroll")).append(" `x").append(dragonScrolls).append("` Dragon Steel Scrolls\n");
                }
                if (titanAlloyKeys > 0) {
                    KeysUtil.addKeys(userId, "titan alloy key", titanAlloyKeys);
                    specialEvent.append(EmoteUtil.getEmoteMention("Titan_Alloy_Key")).append(" `x").append(titanAlloyKeys).append("` Titan Alloy Keys");
                }
                if (dragonSteelKeys > 0) {
                    KeysUtil.addKeys(userId, "dragon steel key", dragonSteelKeys);
                    specialEvent.append(EmoteUtil.getEmoteMention("Dragon_Steel_Key")).append(" `x").append(dragonSteelKeys).append("` Dragon Steel Keys\n");
                }
                if (witherScroll > 0) {
                    WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                }
                if (points >= 1) {
                    specialEvent.append("*Placed [").append(place).append(" / ").append(totalPlayers).append("]");
                }
                final User u = Withering.getBot().getShardManager().retrieveUserById(userId).complete();
                if (u != null) {
                    try {
                        final AtomicBoolean sent = new AtomicBoolean(false);
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Dungeon Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
                            if (sent.get()) {
                                specialEvent.append(Language.getLocalized(userId, "dm_failure",
                                                "{user_mention}, I couldn't send you a DM! Please check your settings and try again.")
                                        .replace("{user_mention}", p.getUser().getAsMention()));
                            }
                        }));
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getPlayerPoint(String userId) {
        return getPoints(userId);
    }
}
