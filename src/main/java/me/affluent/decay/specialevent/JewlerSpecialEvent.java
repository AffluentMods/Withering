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

public class JewlerSpecialEvent extends SpecialEvent {

    public JewlerSpecialEvent() {
        super("Jewler", "Merge gems to obtain points.\n" + Constants.TAB + "- `" + Constants.PREFIX +
                "gem merge <tier>`.\n\n Points\n"
                + Constants.TAB + "- Tier 1: 10 Points\n"
                + Constants.TAB + "- Tier 2: 30 Points\n"
                + Constants.TAB + "- Tier 3: 120 Points\n"
                + Constants.TAB + "- Tier 4: 480 Points\n"
                + Constants.TAB + "- Tier 5: 1920 Points\n",
                "Merge gems to obtain points",
                "" +
                "75 Points - 2.5% of current gold " + EmoteUtil.getCoin() + "\n" +
                "200 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "500 Points - Random Tier 2 Gem " + EmoteUtil.getEmoteMention("Random Gem") + "\n" +
                "1250 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "1920 Points - Random Tier 3 Gem " + EmoteUtil.getEmoteMention("Random Gem"));
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
                int randomTier2 = 0;
                int randomTier3 = 0;
                int witherScroll = 0;
                final int maxPoints = 1920;
                //
                if (points >= 75) fullAmountToAdd = fullAmountToAdd.add(add);
                if (points >= 200) diamonds += 500;
                if (points >= 500) randomTier2 += 1;
                if (points >= 1250) diamonds += 500;
                if (points >= maxPoints) randomTier3 += 1;
                //
                if (points >= maxPoints + 75) fullAmountToAdd = fullAmountToAdd.add(add);
                if (points >= maxPoints + 200) diamonds += 500;
                if (points >= maxPoints + 500) randomTier2 += 1;
                if (points >= maxPoints + 1250) diamonds += 500;
                if (points >= maxPoints + maxPoints) randomTier3 += 1;
                //
                if (points >= maxPoints + maxPoints + 75) fullAmountToAdd = fullAmountToAdd.add(add);
                if (points >= maxPoints + maxPoints + 200) diamonds += 500;
                if (points >= maxPoints + maxPoints + 500) randomTier2 += 1;
                if (points >= maxPoints + maxPoints + 1250) diamonds += 500;
                if (points >= maxPoints + maxPoints + maxPoints) {
                    randomTier3 += 1;
                    witherScroll += 1;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 75) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 75 && (points > 0)) {
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
                if (randomTier2 > 0) {
                    specialEvent.append(EmoteUtil.getEmoteMention("Random Gem")).append(" `x").append(randomTier2).append("` Random Tier 2\n");
                }
                if (randomTier3 > 0) {
                    specialEvent.append(EmoteUtil.getEmoteMention("Random Gem")).append(" `x").append(randomTier3).append("` Random Tier 3\n");
                }
                if (witherScroll > 0) {
                    WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                }
                if (points < 1920 && (points >= 75)) {
                    specialEvent.append("\n*[0/3] Completions*");
                }
                if (points >= 1920 && (points < 3839 )) {
                    specialEvent.append("\n*[1/3] Completions*");
                }
                if (points >= 3840 && (points < 5759 )) {
                    specialEvent.append("\n*[2/3] Completions*");
                }
                if (points >= 5760) {
                    specialEvent.append("\n*[3/3] Completions*");
                }
                final User u = Withering.getBot().getShardManager().retrieveUserById(userId).complete();
                if (u != null) {
                    try {
                        final AtomicBoolean sent = new AtomicBoolean(false);
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Jewler Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
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
