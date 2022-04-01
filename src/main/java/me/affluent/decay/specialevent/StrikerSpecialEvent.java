package me.affluent.decay.specialevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.event.FightEndEvent;
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

public class StrikerSpecialEvent extends SpecialEvent {

    public StrikerSpecialEvent() {
        super("Striker", "Kill people to gain points\n" + Constants.TAB + "- Use the `" + Constants.PREFIX +
                         "random` pvp command.\n\n Points:\n" + Constants.TAB + "- Win an attack: 3 Points\n" + Constants.TAB + "- Win a defense: 1 Point", "Kill people to gain Points",
                "" +
                        "25 Score - 350 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                        "50 Score - 3 Iron Scroll " + EmoteUtil.getEmoteMention("Iron Scroll") + "\n" +
                        "100 Score - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                        "150 Score - 1 Dragon Steel Scroll " + EmoteUtil.getEmoteMention("Dragon Steel Scroll") + "\n" +
                        "250 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                        "350 Points - 1 Titan Alloy Key " + EmoteUtil.getEmoteMention("Titan Alloy Key"));
    }

    @Override
    public void onFightEndEvent(FightEndEvent event) {
        if (!isActive(this)) return;
        if (!event.isRandom() || event.isPractice()) return;
        Player w = event.getWinner();
        Player a = event.getAttacker();
        Player d = event.getDefender();
        String aid = a.getUserId();
        String did = d.getUserId();
        int wPoints;
        String wid;
        if (w.getUserId().equalsIgnoreCase(aid)) {
            wPoints = getPoints(aid);
            wid = aid;
            wPoints += 3;
            setData(wid, wPoints, 0);
        }
        if (w.getUserId().equalsIgnoreCase(did)) {
            wPoints = getPoints(did);
            wid = did;
            wPoints++;
            setData(wid, wPoints, 0);
        }
    }

    @Override
    public void giveRewards() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId, points FROM specialevent;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                int points = rs.getInt("points");
                Player p = Player.getPlayer(userId);
                BigInteger add = BigInteger.valueOf(1000);
                BigInteger pg =
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.025)).toBigInteger();
                add = add.add(pg);
                BigInteger fullAmountToAdd = BigInteger.ZERO;
                int diamonds = 0;
                int ironScroll = 0;
                int dragonScroll = 0;
                int titanAlloyKey = 0;
                int witherScroll = 0;
                final int maxPoints = 350;
                //
                if (points >= 25) diamonds += 350;
                if (points >= 50) ironScroll += 3;
                if (points >= 100) diamonds += 500;
                if (points >= 150) dragonScroll += 3;
                if (points >= 250) diamonds += 500;
                if (points >= maxPoints) titanAlloyKey += 1;
                //
                if (points >= maxPoints + 25) diamonds += 350;
                if (points >= maxPoints + 50) ironScroll += 3;
                if (points >= maxPoints + 100) diamonds += 500;
                if (points >= maxPoints + 150) dragonScroll += 3;
                if (points >= maxPoints + 250) diamonds += 500;
                if (points >= maxPoints + maxPoints) titanAlloyKey += 1;
                //
                if (points >= maxPoints + maxPoints + 25) diamonds += 350;
                if (points >= maxPoints + maxPoints + 50) ironScroll += 3;
                if (points >= maxPoints + maxPoints + 100) diamonds += 500;
                if (points >= maxPoints + maxPoints + 150) dragonScroll += 3;
                if (points >= maxPoints + maxPoints + 250) diamonds += 500;
                if (points >= maxPoints + maxPoints + maxPoints) {
                    titanAlloyKey += 1;
                    witherScroll += 1;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 25) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 25 && (points > 0)) {
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
                if (ironScroll > 0) {
                    IronScrollsUtil.addIronScrolls(userId, ironScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Iron Scroll")).append(" `x").append(ironScroll).append("` Iron Scrolls\n");
                }
                if (dragonScroll > 0) {
                    DragonScrollsUtil.addDragonScrolls(userId, dragonScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Dragon_Steel_Scroll")).append(" `x").append(dragonScroll).append("` Dragon Scrolls\n");
                }
                if (diamonds > 0) {
                    DiamondsUtil.addDiamonds(userId, diamonds);
                    specialEvent.append(EmoteUtil.getDiamond()).append(" `x").append(diamonds).append("` Diamonds\n");
                }
                if (titanAlloyKey > 0) {
                    KeysUtil.addKeys(userId, "titan alloy key", titanAlloyKey);
                    specialEvent.append(EmoteUtil.getEmoteMention("Titan_Alloy_Key")).append(" `x").append(titanAlloyKey).append("` Titan Alloy Keys");
                }
                if (witherScroll > 0) {
                    WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                }
                if (points < maxPoints && (points >= 25)) {
                    specialEvent.append("\n*[0/3] Completions*");
                }
                if (points >= maxPoints && (points < maxPoints + maxPoints - 1 )) {
                    specialEvent.append("\n*[1/3] Completions*");
                }
                if (points >= maxPoints + maxPoints && (points < maxPoints + maxPoints + maxPoints - 1 )) {
                    specialEvent.append("\n*[2/3] Completions*");
                }
                if (points >= maxPoints + maxPoints + maxPoints) {
                    specialEvent.append("\n*[3/3] Completions*");
                }
                final User u = Withering.getBot().getShardManager().retrieveUserById(userId).complete();
                if (u != null) {
                    try {
                        final AtomicBoolean sent = new AtomicBoolean(false);
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Striker Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
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