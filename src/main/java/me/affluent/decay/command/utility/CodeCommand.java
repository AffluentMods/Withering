package me.affluent.decay.command.utility;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CodeCommand extends BotCommand {

    public CodeCommand() {
        this.name = "code";
        this.aliases = new String[]{"redeem"};
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p = Player.getPlayer(uid);
        boolean isCodeRedeemed = false;
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info(Language.getLocalized(uid, "usage_plain", "Usage"),
                    Language.getLocalized(uid, "code_usage",
                            "Please use {command_usage}.\n\n"
                                    .replace("{command_usage}", "`" + userPrefix + "code <code>`\n" +
                                            "Example: `" + userPrefix + "code 123`"))));
            return;
        }
        String redeemed = args[0];

        if (redeemed.equalsIgnoreCase("valentines2022")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM codes WHERE userId=? AND code=?;", uid, redeemed)) {
                if (rs.next()) isCodeRedeemed = true;
                if (!isCodeRedeemed) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(Language.getLocalized(uid, "code_plain", "Redeemed Code"));
                    eb.setColor(new Color(19, 255, 58));
                    eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
                    BigInteger add = BigInteger.valueOf(1000);
                    BigInteger pg =
                            new BigDecimal(p.getEcoUser().getBalance()).multiply(BigDecimal.valueOf(0.2)).toBigInteger();
                    add = add.add(pg);
                    BigInteger fullAmountToAdd = add;
                    String amount = "0";
                    if (fullAmountToAdd.compareTo(BigInteger.ZERO) > 0) {
                        if (fullAmountToAdd.compareTo(BigInteger.valueOf(500000)) > 0) {
                            int finalMoneyReward = 500000;
                            amount = "500,000";
                            p.getEcoUser().addBalance(finalMoneyReward);
                        } else {
                            amount = String.valueOf(fullAmountToAdd);
                            p.getEcoUser().addBalance(fullAmountToAdd);
                        }
                    }
                    eb.setDescription(Language.getLocalized(uid, "code",
                            EmoteUtil.getCoin() + " Gold Coin `x" + amount + "`\n" +
                                    EmoteUtil.getDiamond() + " Diamonds `x3500`\n" +
                                    EmoteUtil.getEmoteMention("Golden_Tavern_Token") + " Golden Tavern Token `x20`\n" +
                                    EmoteUtil.getEmoteMention("Tavern_Token") + " Tavern Token `x50`\n"
                    ));
                    e.getTextChannel().sendMessage(eb.build()).queue();
                    GoldenTokensUtil.addGoldenTokens(uid, 20);
                    TokensUtil.addTokens(uid, 50);
                    DiamondsUtil.addDiamonds(uid, 3500);
                    Withering.getBot().getDatabase().update("INSERT INTO codes VALUES (?, ?);", uid, args[0]);
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (redeemed.equalsIgnoreCase("123")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM codes WHERE userId=? AND code=?;", uid, redeemed)) {
                if (rs.next()) isCodeRedeemed = true;
                if (!isCodeRedeemed) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(Language.getLocalized(uid, "code_plain", "Redeemed Code"));
                    eb.setColor(new Color(19, 255, 58));
                    eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
                    eb.setDescription(Language.getLocalized(uid, "code",
                            EmoteUtil.getCoin() + " Gold Coin `x123`\n" +
                            EmoteUtil.getDiamond() + " Diamonds `x123`"
                    ));
                    e.getTextChannel().sendMessage(eb.build()).queue();
                    DiamondsUtil.addDiamonds(uid, 123);
                    p.getEcoUser().addBalance(123);
                    Withering.getBot().getDatabase().update("INSERT INTO codes VALUES (?, ?);", uid, args[0]);
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (redeemed.equalsIgnoreCase("buggy")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM codes WHERE userId=? AND code=?;", uid, redeemed)) {
                if (rs.next()) isCodeRedeemed = true;
                if (!isCodeRedeemed) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(Language.getLocalized(uid, "code_plain", "Redeemed Code"));
                    eb.setColor(new Color(19, 255, 58));
                    eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
                    eb.setDescription(Language.getLocalized(uid, "code",
                            EmoteUtil.getCoin() + " Gold Coin `x6666`\n" +
                                    EmoteUtil.getDiamond() + " Diamonds `x666`\n" +
                                    "For now on, whoever reports bugs will be given an Epic Pet for free, the Bug Breaker" +
                                    "Who abuses bugs, will have the abused items reset, with potential banning."
                    ));
                    e.getTextChannel().sendMessage(eb.build()).queue();
                    DiamondsUtil.addDiamonds(uid, 666);
                    p.getEcoUser().addBalance(6666);
                    Withering.getBot().getDatabase().update("INSERT INTO codes VALUES (?, ?);", uid, args[0]);
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (redeemed.equalsIgnoreCase("NewWitheringArt")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM codes WHERE userId=? AND code=?;", uid, redeemed)) {
                if (rs.next()) isCodeRedeemed = true;
                if (!isCodeRedeemed) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(Language.getLocalized(uid, "code_plain", "Redeemed Code"));
                    eb.setColor(new Color(19, 255, 58));
                    eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
                    eb.setDescription(Language.getLocalized(uid, "code",
                            EmoteUtil.getCoin() + " Gold Coin `2,500`\n" +
                                    EmoteUtil.getDiamond() + " Diamonds `500`\n" +
                                    EmoteUtil.getEmoteMention("Alloy_Ingot") + " Alloy Ingot `50`\n" +
                                    EmoteUtil.getEmoteMention("Iron_Scroll") + " Icon Scroll `3`\n" +
                                    EmoteUtil.getEmoteMention("Dragon_Steel_Scroll") + " Dragon Steel Scroll `1`\n"
                    ));
                    e.getTextChannel().sendMessage(eb.build()).queue();
                    DiamondsUtil.addDiamonds(uid, 500);
                    p.getEcoUser().addBalance(2500);
                    IngotUtil.addIngots(uid, 50);
                    IronScrollsUtil.addIronScrolls(uid, 3);
                    DragonScrollsUtil.addDragonScrolls(uid, 1);
                    Withering.getBot().getDatabase().update("INSERT INTO codes VALUES (?, ?);", uid, args[0]);
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (redeemed.equalsIgnoreCase("Christmas2021")) {
            try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM codes WHERE userId=? AND code=?;", uid, redeemed)) {
                if (rs.next()) isCodeRedeemed = true;
                if (!isCodeRedeemed) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(Language.getLocalized(uid, "code_plain", "Merry Christmas"));
                    eb.setColor(new Color(19, 255, 58));
                    eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
                    eb.setDescription(Language.getLocalized(uid, "code",
                            EmoteUtil.getCoin() + " Gold Coin `2021`\n" +
                                    EmoteUtil.getDiamond() + " Diamonds `2021`\n" +
                                    EmoteUtil.getEmoteMention("Dragon Steel Scroll") + "Dragon Steel Scrolls `2`\n" +
                                    EmoteUtil.getEmoteMention("Orb") + "Orbs `125`\n" +
                                    EmoteUtil.getEmoteMention("Alloy_Ingot") + "Alloy Ingots `221\n`" +
                                    EmoteUtil.getEmoteMention("Titan Alloy Key") + "Titan Alloy Key `1`"
                    ));
                    e.getTextChannel().sendMessage(eb.build()).queue();
                    DiamondsUtil.addDiamonds(uid, 2021);
                    p.getEcoUser().addBalance(2021);
                    DragonScrollsUtil.addDragonScrolls(uid, 2);
                    OrbUtil.addOrbs(uid, 125);
                    IngotUtil.addIngots(uid, 221);
                    KeysUtil.addKeys(uid, "titan alloy key", 1);
                    Withering.getBot().getDatabase().update("INSERT INTO codes VALUES (?, ?);", uid, args[0]);
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (isCodeRedeemed) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "redeemed_code_plain", "Redeemed Code"));
            eb.setColor(new Color(250, 58, 35));
            eb.setDescription(Language.getLocalized(uid, "code",
            "You already redeemed `" + redeemed + "`"));
            e.getTextChannel().sendMessage(eb.build()).queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(Language.getLocalized(uid, "invalid_code_plain", "Invalid Code"));
        eb.setColor(new Color(250, 58, 35));
        eb.setDescription(Language.getLocalized(uid, "invalid",
                "`" + redeemed + "` is not a valid code."));
        e.getTextChannel().sendMessage(eb.build()).queue();
    }
}
