package me.affluent.decay.specialevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.MiningEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.RandomUtil;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DwarvenMinerSpecialEvent extends SpecialEvent {

    public DwarvenMinerSpecialEvent() {
        super("Dwarven Miner", "Mine away in the caverns to earn points.\n" +
                Constants.TAB + "- `" + Constants.PREFIX + "mine`.\n\n" +
                "Points\n" +
                Constants.TAB + "- Mining: 1 Point", "Mine Alloy Ingots to gain points", "" +
                "2 Points - 350 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "4 Points - 1 Scroll " + EmoteUtil.getEmoteMention("Scroll") + "\n" +
                "8 Points - 2 Iron Scrolls " + EmoteUtil.getEmoteMention("Iron_Scroll") + "\n" +
                "12 Points - 550 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                "17 Points - 1 Random Rare " + EmoteUtil.getEmoteMention("Rare Gear") + "\n" +
                "22 Points - 1 Dragon Scroll " + EmoteUtil.getEmoteMention("Dragon Steel Scroll"));
    }

    @Override
    public void onMiningEvent(MiningEvent event) {
        if (!isActive(this)) return;
        Player p = event.getMiner();
        String uid = p.getUserId();
        int points = getPoints(uid);
        points++;
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
                int scroll = 0;
                int ironScroll = 0;
                int rareItem = 0;
                int dragonScroll = 0;
                int witherScroll = 0;
                final int maxPoints = 22;
                //
                if (points >= 2) diamonds += 350;
                if (points >= 4) scroll += 1;
                if (points >= 8) ironScroll += 1;
                if (points >= 12) diamonds += 550;
                if (points >= 17) rareItem += 1;
                if (points >= maxPoints) dragonScroll += 1;
                //
                if (points >= maxPoints + 2) diamonds += 350;
                if (points >= maxPoints + 4) scroll += 1;
                if (points >= maxPoints + 8) ironScroll += 1;
                if (points >= maxPoints + 12) diamonds += 550;
                if (points >= maxPoints + 17) rareItem += 1;
                if (points >= maxPoints + maxPoints) dragonScroll += 1;
                //
                if (points >= maxPoints + maxPoints + 2) diamonds += 350;
                if (points >= maxPoints + maxPoints + 4) scroll += 1;
                if (points >= maxPoints + maxPoints + 8) ironScroll += 1;
                if (points >= maxPoints + maxPoints + 12) diamonds += 550;
                if (points >= maxPoints + maxPoints + 17) rareItem += 1;
                if (points >= maxPoints + maxPoints + maxPoints) {
                    dragonScroll += 1;
                    witherScroll += 1;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 2) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 2 && (points > 0)) {
                    specialEvent.append("Sadly you didn't obtain any rewards.\n");
                }
                if (diamonds > 0) {
                    DiamondsUtil.addDiamonds(userId, diamonds);
                    specialEvent.append(EmoteUtil.getDiamond()).append(" `x").append(diamonds).append("` Diamonds\n");
                }
                if (rareItem > 0) {
                    while (rareItem > 0) {
                        ItemType it = Rank.getHighestMaterial(p.getExpUser().getLevel());
                        if (it == ItemType.TITAN_ALLOY || it == ItemType.WITHER) it = ItemType.DRAGON_STEEL;
                        String theArmor = RandomUtil.getRandomArmor(it);
                        String item = "rare " + theArmor;
                        p.getInventoryUser().addItem(item, 1);
                        rareItem--;
                    }
                    specialEvent.append(EmoteUtil.getEmoteMention("epic gear")).append(" `x").append(rareItem).append("` Rare Items\n");
                }
                if (witherScroll > 0) {
                    WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                    specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                }
                if (ironScroll > 0) {
                    IronScrollsUtil.addIronScrolls(userId, ironScroll);
                }
                if (dragonScroll > 0) {
                    DragonScrollsUtil.addDragonScrolls(userId, dragonScroll);
                }
                if (scroll > 0) {
                    ScrollsUtil.addScrolls(userId, scroll);
                }
                if (points < maxPoints && (points >= 2)) {
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
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Dwarven-Miner Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
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