package me.affluent.decay.specialevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.chest.Chest;
import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.event.ChestOpenEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.util.DiamondsUtil;
import me.affluent.decay.util.KeysUtil;
import me.affluent.decay.util.ScrollsUtil;
import me.affluent.decay.util.WitherScrollsUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.RandomUtil;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChestBreacherSpecialEvent extends SpecialEvent {

    public ChestBreacherSpecialEvent() {
        super("Chest Breacher", "Each chest you open gives you points.\n" + Constants.TAB + "- `" + Constants.PREFIX +
                                "open <key>`.\n\n Points\n" + Constants.TAB + "- Wood Chest: 1 Point\n" + Constants.TAB +
                                "- Metal Chest: 2 Points\n" + Constants.TAB + "- Titanium Chest: 10 Points\n" +
                                Constants.TAB + "- Steel Chest: 30 Points\n" + Constants.TAB +
                                "- Dragon-Steel Chest: 75 Points\n" + Constants.TAB + "- Titan-Alloy Chest: 175 Points", "Open chests to gain points",
                "" +
                        "40 Points - 350 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                        "140 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                        "240 Points - 500 Diamonds " + EmoteUtil.getDiamond() + "\n" +
                        "400 Points - 1 Dragon Steel Key " + EmoteUtil.getEmoteMention("Dragon Steel Key") + "\n" +
                        "625 Points - 1 Random Epic " + EmoteUtil.getEmoteMention("Epic Gear") + "\n" +
                        "800 Points - 1 Dragon Steel Key " + EmoteUtil.getEmoteMention("Dragon Steel Key") + "\n" +
                        "1200 Points - 1 Titan Alloy Key " + EmoteUtil.getEmoteMention("Titan Alloy Key"));
    }

    @Override
    public void onChestOpenEvent(ChestOpenEvent event) {
        if (!isActive(this)) return;
        Player p = event.getOpener();
        Chest chest = event.getChest();
        String uid = p.getUserId();
        int points = getPoints(uid);
        int pointsToAdd = 0;
        if (chest.getItemType() == ItemType.WOOD) pointsToAdd += 1;
        if (chest.getItemType() == ItemType.METAL) pointsToAdd += 2;
        if (chest.getItemType() == ItemType.TITANIUM) pointsToAdd += 10;
        if (chest.getItemType() == ItemType.STEEL) pointsToAdd += 30;
        if (chest.getItemType() == ItemType.DRAGON_STEEL) pointsToAdd += 75;
        if (chest.getItemType() == ItemType.TITAN_ALLOY) pointsToAdd += 175;
        points += (pointsToAdd * event.getOpenAmount());
        setData(uid, points, 0);
    }

    @Override
    public void giveRewards() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT userId, points FROM specialevent;")) {
            while (rs.next()) {
                String userId = rs.getString("userId");
                int points = rs.getInt("points");
                Player p = Player.getPlayer(userId);
                int dragonsteelkeys = 0;
                int titanalloykeys = 0;
                int diamonds = 0;
                int witherScroll = 0;
                int epicItem = 0;
                final int maxPoints = 1200;
                //
                if (points >= 40) diamonds += 350;
                if (points >= 140) diamonds += 500;
                if (points >= 240) diamonds += 500;
                if (points >= 400) dragonsteelkeys += 1;
                if (points >= 625) epicItem += 1;
                if (points >= 800) dragonsteelkeys += 1;
                if (points >= maxPoints) titanalloykeys += 1;
                //
                if (points >= maxPoints + 40) diamonds += 350;
                if (points >= maxPoints + 140) diamonds += 500;
                if (points >= maxPoints + 240) diamonds += 500;
                if (points >= maxPoints + 400) dragonsteelkeys += 1;
                if (points >= maxPoints + 625) epicItem += 1;
                if (points >= maxPoints + 800) dragonsteelkeys += 1;
                if (points >= maxPoints + maxPoints) titanalloykeys += 1;
                //
                if (points >= maxPoints + maxPoints + 40) diamonds += 350;
                if (points >= maxPoints + maxPoints + 140) diamonds += 500;
                if (points >= maxPoints + maxPoints + 240) diamonds += 500;
                if (points >= maxPoints + maxPoints + 400) dragonsteelkeys += 1;
                if (points >= maxPoints + maxPoints + 625) epicItem += 1;
                if (points >= maxPoints + maxPoints + 800) dragonsteelkeys += 1;
                if (points >= maxPoints + maxPoints + maxPoints) {
                    titanalloykeys += 1;
                    witherScroll += 1;
                }
                //
                StringBuilder specialEvent = new StringBuilder();
                if (points >= 40) {
                    specialEvent.append("__Here are your rewards:__\n");
                }
                if (points < 40 && (points > 0)) {
                    specialEvent.append("Sadly you didn't obtain any rewards.\n");
                }
                    if (diamonds > 0) {
                        DiamondsUtil.addDiamonds(userId, diamonds);
                        specialEvent.append(EmoteUtil.getDiamond()).append(" `x").append(diamonds).append("` Diamonds\n");
                    }
                    if (epicItem > 0) {
                        while (epicItem > 0) {
                            ItemType it = Rank.getHighestMaterial(p.getExpUser().getLevel());
                            if (it == ItemType.DRAGON_STEEL) it = ItemType.CARBON_STEEL;
                            else if (it == ItemType.TITAN_ALLOY || it == ItemType.WITHER) it = ItemType.DRAGON_STEEL;
                            String theArmor = RandomUtil.getRandomArmor(it);
                            String item = "epic " + theArmor;
                            p.getInventoryUser().addItem(item, 1);
                            epicItem--;
                        }
                        specialEvent.append(EmoteUtil.getEmoteMention("epic gear")).append(" `x").append(epicItem).append("` Epic Items\n");
                    }
                    if (dragonsteelkeys > 0) {
                        KeysUtil.addKeys(userId, "dragon steel key", dragonsteelkeys);
                        specialEvent.append(EmoteUtil.getEmoteMention("Dragon_Steel_Key")).append(" `x").append(dragonsteelkeys).append("` Dragon Steel Keys\n");
                    }
                    if (titanalloykeys > 0) {
                        KeysUtil.addKeys(userId, "titan alloy key", titanalloykeys);
                        specialEvent.append(EmoteUtil.getEmoteMention("Titan_Alloy_Key")).append(" `x").append(titanalloykeys).append("` Titan Alloy Keys");
                    }
                    if (witherScroll > 0) {
                        WitherScrollsUtil.addWitherScrolls(userId, witherScroll);
                        specialEvent.append(EmoteUtil.getEmoteMention("Wither Scroll")).append(" `x").append(witherScroll).append("` Wither Scroll");
                    }
                    if (points < maxPoints && (points >= 40)) {
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
                        u.openPrivateChannel().queue(pc -> pc.sendMessage(MessageUtil.info("**Chest-Breacher Ended**", "" + specialEvent)).queue(pcc -> sent.set(true), f -> {
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