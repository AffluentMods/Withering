package me.affluent.decay.specialevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.event.TavernSpinEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamblerSpecialEvent extends SpecialEvent {

    public GamblerSpecialEvent() {
        super("Gambler", "Each Tavern Token used rewards points.\n" + Constants.TAB + "- `" + Constants.PREFIX +
                "tavern spin`.\n\n Points\n" + Constants.TAB + "- Spinning: 1 Point", "Spin Tavern Tokens at the Tavern to obtain points", "" +
                "10 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "27 Points - 5 Golden Tavern Token " + EmoteUtil.getEmoteMention("Golden Tavern Token") + "\n" +
                "35 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "52 Points - 10 Golden Tavern Token " + EmoteUtil.getEmoteMention("Golden Tavern Token") + "\n" +
                "70 Points - 750 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "105 Points - 10 Golden Tavern Token " + EmoteUtil.getEmoteMention("Golden Tavern Token"));
    }

    @Override
    public void onTavernSpinEvent(TavernSpinEvent event) {
        if (!isActive(this)) return;
        Player p = event.getSpinner();
        String uid = p.getUserId();
        int points = getPoints(uid);
        points += event.getSpinAmount();
        setData(uid, points, 0);
    }

    @Override
    public void giveRewards() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId, points FROM specialevent;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                int points = rs.getInt("points");
                Player p = Player.getPlayer(userId);
                int diamonds = 0;
                int goldenToken = 0;
                int witherScroll = 0;
                final int maxPoints = 105;
                //
                if (points >= 10) diamonds += 500;
                if (points >= 27) goldenToken += 5;
                if (points >= 35) diamonds += 500;
                if (points >= 52) goldenToken += 10;
                if (points >= 70) diamonds += 750;
                if (points >= maxPoints) goldenToken += 10;
                //
                if (points >= maxPoints + 10) diamonds += 500;
                if (points >= maxPoints + 27) goldenToken += 5;
                if (points >= maxPoints + 35) diamonds += 500;
                if (points >= maxPoints + 52) goldenToken += 10;
                if (points >= maxPoints + 70) diamonds += 750;
                if (points >= maxPoints + maxPoints) goldenToken += 10;
                //
                if (points >= maxPoints + maxPoints + 10) diamonds += 500;
                if (points >= maxPoints + maxPoints + 27) goldenToken += 5;
                if (points >= maxPoints + maxPoints + 35) diamonds += 500;
                if (points >= maxPoints + maxPoints + 52) goldenToken += 10;
                if (points >= maxPoints + maxPoints + 70) diamonds += 750;
                if (points >= maxPoints + maxPoints + maxPoints) {
                    goldenToken += 10;
                    witherScroll += 1;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 10) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 10 && (points > 0)) {
                    specialEvent.append("Sadly you didn't obtain any rewards.\n");
                }
                if (diamonds > 0) {
                    DiamondsUtil.addDiamonds(userId, diamonds);
                    specialEvent.append(EmoteUtil.getDiamond()).append(" `x").append(diamonds).append("` Diamonds\n");
                }
                if (goldenToken > 0) {
                    GoldenTokensUtil.addGoldenTokens(userId, goldenToken);
                    specialEvent.append(EmoteUtil.getEmoteMention("Golden Tavern Token")).append(" `x").append(goldenToken).append("` Golden Tokens\n");
                }
                if (witherScroll > 0) {
                    WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                }
                if (points < maxPoints && (points >= 10)) {
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
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Gambler Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
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
