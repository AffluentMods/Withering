package me.affluent.decay.specialevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.ItemLevelUpEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class GoldenHandSpecialEvent extends SpecialEvent {

    public GoldenHandSpecialEvent() {
        super("Golden Hand", "Level up items to receive points. You must level up your within 1 of your strongest material or better.\n" +
                "For example, if you can equip up to steel, you must level up at least iron\n" + Constants.TAB + "- `" + Constants.PREFIX +
                "level <itemId> [amount]`.\n\n Points\n" + Constants.TAB + "- Leveling: 1 Point", "Level up items to gain points", "" +
                "30 Points - 350 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "50 Points - 2 Scrolls " + EmoteUtil.getEmoteMention("Scroll") + "\n" +
                "80 Points - 3 Iron Scrolls " + EmoteUtil.getEmoteMention("Iron Scroll") + "\n" +
                "120 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "140 Points - 1 Dragon Steel Scroll " + EmoteUtil.getEmoteMention("Dragon Steel Scroll") + "\n" +
                "200 Points - 12.5% of current gold " + EmoteUtil.getCoin());
    }

    @Override
    public void onItemLevelUpEvent(ItemLevelUpEvent event) {
        if (!isActive(this)) return;
        ItemType itemType = event.getItemType();
        Player p = event.getLeveler();
        int value = 0;
        int value2 = 0;
        ItemType it = Rank.getHighestMaterial(p.getExpUser().getLevel());
        switch (it) {
            case WOOD:
                value = 1;
                break;
            case COPPER:
                value = 2;
                break;
            case REINFORCED:
                value = 3;
                break;
            case TITANIUM:
                value = 4;
                break;
            case IRON:
                value = 5;
                break;
            case STEEL:
                value = 6;
                break;
            case CARBON_STEEL:
                value = 7;
                break;
            case DRAGON_STEEL:
                value = 8;
                break;
            case TITAN_ALLOY:
                value = 9;
                break;
            case WITHER:
                value = 10;
                break;
        }
        switch (itemType) {
            case WOOD:
                value2 = 1;
                break;
            case COPPER:
                value2 = 2;
                break;
            case REINFORCED:
                value2 = 3;
                break;
            case TITANIUM:
                value2 = 4;
                break;
            case IRON:
                value2 = 5;
                break;
            case STEEL:
                value2 = 6;
                break;
            case CARBON_STEEL:
                value2 = 7;
                break;
            case DRAGON_STEEL:
                value2 = 8;
                break;
            case TITAN_ALLOY:
                value2 = 9;
                break;
            case WITHER:
                value2 = 10;
                break;
        }
        if (value == value2 || value2 + 1 == value || value2 > value) {
            String uid = p.getUserId();
            int points = getPoints(uid);
            points += event.getItemLevelUpAmount();
            setData(uid, points, 0);
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
                        new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.125)).toBigInteger();
                add = add.add(pg);
                BigInteger fullAmountToAdd = BigInteger.ZERO;
                int diamonds = 0;
                int scrolls = 0;
                int ironScrolls = 0;
                int dragonScrolls = 0;
                int witherScroll = 0;
                final int maxPoints = 200;
                //
                if (points >= 30) diamonds += 350;
                if (points >= 50) scrolls += 2;
                if (points >= 80) ironScrolls += 3;
                if (points >= 120) diamonds += 500;
                if (points >= 140) dragonScrolls += 1;
                if (points >= maxPoints) fullAmountToAdd = fullAmountToAdd.add(add);
                //
                if (points >= maxPoints + 30) diamonds += 350;
                if (points >= maxPoints + 50) scrolls += 2;
                if (points >= maxPoints + 80) ironScrolls += 3;
                if (points >= maxPoints + 120) diamonds += 500;
                if (points >= maxPoints + 140) dragonScrolls += 1;
                if (points >= maxPoints + maxPoints) fullAmountToAdd = fullAmountToAdd.add(add);
                //
                if (points >= maxPoints + maxPoints + 30) diamonds += 350;
                if (points >= maxPoints + maxPoints + 50) scrolls += 2;
                if (points >= maxPoints + maxPoints + 80) ironScrolls += 3;
                if (points >= maxPoints + maxPoints + 120) diamonds += 500;
                if (points >= maxPoints + maxPoints + 140) dragonScrolls += 1;
                if (points >= maxPoints + maxPoints + maxPoints) {
                    fullAmountToAdd = fullAmountToAdd.add(add);
                    witherScroll += 1;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 30) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 30 && (points > 0)) {
                    specialEvent.append("Sadly you didn't obtain any rewards.\n");
                }
                if (fullAmountToAdd.compareTo(BigInteger.ZERO) > 0) {
                    if (fullAmountToAdd.compareTo(BigInteger.valueOf(500000)) > 0) {
                        int finalMoneyReward = 500000;
                        p.getEcoUser().addBalance(finalMoneyReward);
                        specialEvent.append(EmoteUtil.getCoin()).append(" `x").append(finalMoneyReward).append("` Gold Coins\n");
                    } else {
                        p.getEcoUser().addBalance(fullAmountToAdd);
                        specialEvent.append(EmoteUtil.getCoin()).append(" `x").append(fullAmountToAdd).append("` Gold Coins\n");
                    }
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
                if (witherScroll > 0) {
                    WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                }
                if (points < maxPoints && (points >= 30)) {
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
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Golden-Hand Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
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
