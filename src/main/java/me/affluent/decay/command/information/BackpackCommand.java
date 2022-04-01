package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.settingsUtil.ResponseUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.math.BigInteger;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class BackpackCommand extends BotCommand {

    public BackpackCommand() {
        this.name = "backpack";
        this.aliases = new String[]{"bp"};
        this.cooldown = 1.5;
    }

    private static int totalItems;

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        String amountPresent = String.valueOf(PresentsUtil.getHolidayPresents(uid));
        String amountFirework = String.valueOf( FireworksUtil.getHolidayFireworks(uid));
        String amountPurpleCandy = String.valueOf( PurpleCandyUtil.getHolidayPurple(uid));
        String amountCandyCane = String.valueOf( CandyCaneUtil.getHolidayCandyCane(uid));
        String amountCandyCorn = String.valueOf( CandyCornUtil.getHolidayCorn(uid));
        String amountDiamonds = String.valueOf( DiamondsUtil.getDiamonds(uid));
        String amountDiamondsA = String.valueOf( FormatUtil.formatAbbreviated(DiamondsUtil.getDiamonds(uid)));
        String amountDiamondsC = FormatUtil.formatCommas(DiamondsUtil.getDiamonds(uid));
        String amountElixir = String.valueOf( ElixirUtil.getElixir(uid));
        String amountElixirA = String.valueOf( FormatUtil.formatAbbreviated(ElixirUtil.getElixir(uid)));
        String amountElixirC = FormatUtil.formatCommas(ElixirUtil.getElixir(uid));
        String amountAlloyIngot = String.valueOf( IngotUtil.getIngots(uid));
        String amountOrb = String.valueOf( OrbUtil.getOrbs(uid));
        String amountScroll = String.valueOf( ScrollsUtil.getScrolls(uid));
        String amountIronScroll = String.valueOf( IronScrollsUtil.getIronScrolls(uid));
        String amountDragonScroll = String.valueOf( DragonScrollsUtil.getDragonScrolls(uid));
        String amountWitherScroll = String.valueOf( WitherScrollsUtil.getWitherScrolls(uid));
        String amountWoodKey = String.valueOf( KeysUtil.getKeys(uid, "wood key"));
        String amountMetalKey = String.valueOf( KeysUtil.getKeys(uid, "metal key"));
        String amountTitaniumKey = String.valueOf( KeysUtil.getKeys(uid, "titanium key"));
        String amountSteelKey = String.valueOf( KeysUtil.getKeys(uid, "steel key"));
        String amountDragonKey = String.valueOf( KeysUtil.getKeys(uid, "dragon steel key"));
        String amountTitanKey = String.valueOf( KeysUtil.getKeys(uid, "titan alloy key"));
        String amountTavernToken = String.valueOf( TokensUtil.getTokens(uid));
        String amountGoldenTavernToken = String.valueOf( GoldenTokensUtil.getGoldenTokens(uid));
        String amountCoin = String.valueOf(p.getEcoUser().getBalance());
        String amountCoinA = String.valueOf(p.getEcoUser().getBalanceAbr());
        String amountCoinC = String.valueOf(p.getEcoUser().getBalanceCom());
        String response = ResponseUtil.getResponseUtil(uid).getResponse();
        StringBuilder backpack = new StringBuilder();
        totalItems = 0;
        if (amountCoin.compareTo(String.valueOf(BigInteger.ZERO)) > 0) {
            totalItems++;
            if (response.equalsIgnoreCase("pc")) {
                backpack.append(EmoteUtil.getCoin()).append(" ").append(amountCoinA).append(addItem(totalItems, amountCoinA.length()));
            }
            if (response.equalsIgnoreCase("mobile")) {
                backpack.append(EmoteUtil.getCoin()).append(" ").append(amountCoinC).append(addItem(totalItems, amountCoinC.length()));
            }
        }
        if (Integer.parseInt(amountDiamonds) > 0) {
            totalItems++;
            if (response.equalsIgnoreCase("pc")) {
                backpack.append(EmoteUtil.getDiamond()).append(" ").append(amountDiamondsA).append(addItem(totalItems, amountDiamondsA.length()));
            }
            if (response.equalsIgnoreCase("mobile")) {
                backpack.append(EmoteUtil.getDiamond()).append(" ").append(amountDiamondsC).append(addItem(totalItems, amountDiamondsC.length()));
            }
        }
        if (Integer.parseInt(amountElixir) > 0) {
            totalItems++;
            if (response.equalsIgnoreCase("pc")) {
                backpack.append(EmoteUtil.getEmoteMention("Elixir")).append(" ").append(amountElixirA).append(addItem(totalItems, amountElixirA.length()));
            }
            if (response.equalsIgnoreCase("mobile")) {
                backpack.append(EmoteUtil.getEmoteMention("Elixir")).append(" ").append(amountElixirC).append(addItem(totalItems, amountElixirC.length()));
            }
        }
        if (Integer.parseInt(amountAlloyIngot) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Alloy_Ingot")).append(" ").append(amountAlloyIngot).append(addItem(totalItems, amountAlloyIngot.length()));
        }
        if (Integer.parseInt(amountOrb) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Orb")).append(" ").append(amountOrb).append(addItem(totalItems, amountOrb.length()));
        }
        if (Integer.parseInt(amountScroll) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Scroll")).append(" ").append(amountScroll).append(addItem(totalItems, amountScroll.length()));
        }
        if (Integer.parseInt(amountIronScroll) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Iron_Scroll")).append(" ").append(amountIronScroll).append(addItem(totalItems, amountIronScroll.length()));
        }
        if (Integer.parseInt(amountDragonScroll) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Dragon_Steel_Scroll")).append(" ").append(amountDragonScroll).append(addItem(totalItems, amountDragonScroll.length()));
        }
        if (Integer.parseInt(amountWitherScroll) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Wither_Scroll")).append(" ").append(amountDragonScroll).append(addItem(totalItems, amountWitherScroll.length()));
        }
        if (Integer.parseInt(amountTavernToken) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Tavern_Token")).append(" ").append(amountTavernToken).append(addItem(totalItems, amountTavernToken.length()));
        }
        if (Integer.parseInt(amountGoldenTavernToken) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Golden_Tavern_Token")).append(" ").append(amountGoldenTavernToken).append(addItem(totalItems, amountGoldenTavernToken.length()));
        }
        if (Integer.parseInt(amountWoodKey) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Wood_Key")).append(" ").append(amountWoodKey).append(addItem(totalItems, amountWoodKey.length()));
        }
        if (Integer.parseInt(amountMetalKey) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Metal_Key")).append(" ").append(amountMetalKey).append(addItem(totalItems, amountMetalKey.length()));
        }
        if (Integer.parseInt(amountTitaniumKey) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Titanium_Key")).append(" ").append(amountTitaniumKey).append(addItem(totalItems, amountTitaniumKey.length()));
        }
        if (Integer.parseInt(amountSteelKey) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Steel_Key")).append(" ").append(amountSteelKey).append(addItem(totalItems, amountSteelKey.length()));
        }
        if (Integer.parseInt(amountDragonKey) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Dragon_Steel_Key")).append(" ").append(amountDragonKey).append(addItem(totalItems, amountDragonKey.length()));
        }
        if (Integer.parseInt(amountTitanKey) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Titan_Alloy_Key")).append(" ").append(amountTitanKey).append(addItem(totalItems, amountTitanKey.length()));
        }
        if (Integer.parseInt(amountPresent) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Holiday_Present")).append(" ").append(amountPresent).append(addItem(totalItems, amountPresent.length()));
        }
        if (Integer.parseInt(amountFirework) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Holiday_Firework")).append(" ").append(amountFirework).append(addItem(totalItems, amountFirework.length()));
        }
        if (Integer.parseInt(amountPurpleCandy) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Holiday_Purple_Candy")).append(" ").append(amountPurpleCandy).append(addItem(totalItems, amountPurpleCandy.length()));
        }
        if (Integer.parseInt(amountCandyCane) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Holiday_Candy_Cane")).append(" ").append(amountCandyCane).append(addItem(totalItems, amountCandyCane.length()));
        }
        if (Integer.parseInt(amountCandyCorn) > 0) {
            totalItems++;
            backpack.append(EmoteUtil.getEmoteMention("Holiday_Candy_Corn")).append(" ").append(amountCandyCorn).append(addItem(totalItems, amountCandyCorn.length()));
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(Language.getLocalized(uid, "backpack", "Backpack") + " " + Language.getLocalized(uid, "of", "of") + " " +
                p.getUser().getAsTag());
        eb.setDescription(backpack);
        e.getTextChannel().sendMessage(eb.build()).queue();
    }
    
    public String addItem(int amount, int length) {
        StringBuilder spacing = new StringBuilder();
        if (amount >= 3) {
            totalItems -= 3;
            return "\n";
        }
        else {
            while (length < 17) {
                //System.out.println(length + " > length");
                spacing.append("\u2004");
                length++;
            }
            //System.out.println(spacing + " spacing");
            return spacing.toString();
        }
    }
}
