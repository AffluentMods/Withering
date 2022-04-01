package me.affluent.decay.command.other;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.ExpUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigInteger;

public class GiveAllCommand extends BotCommand {

    public GiveAllCommand() {
        this.name = "giveall";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p = Player.getPlayer(uid);
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(Language.getLocalized(uid, "giveall_plain", "Everything Added"));
        eb.setColor(new Color(19, 255, 58));
        eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
        eb.setDescription(Language.getLocalized(uid, "giveall",
        "Set;\n" +
                "Level: 151\n" +
                "Elixir Level: 999\n" +
                "Elixir: 1,000,000\n" +
                "Diamonds: 1,000,000\n" +
                "Gold: 1,000,000\n" +
                "Scrolls: 1,000,000\n" +
                "Iron Scrolls: 1,000,000\n" +
                "Dragon Scrolls: 1,000,000\n" +
                "Orbs: 1,000,000\n" +
                "Tokens: 1,000,000\n" +
                "Golden Tokens: 1,000,000\n" +
                "Alloy Ingots: 1,000,000\n" +
                "`All` Keys: 1,000,000\n" +
                "Betta Fish Pet [Rare]: 1"
        ));
        e.getTextChannel().sendMessage(eb.build()).queue();
        ExpUser.getExpUser(uid).setLevel(151);
        ExpUser.getExpUser(uid).setElixirLevel(999);
        ElixirUtil.setElixir(uid, 1000000);
        DiamondsUtil.setDiamonds(uid, 1000000);
        ScrollsUtil.setScrolls(uid, 1000000);
        IronScrollsUtil.setIronScrolls(uid, 1000000);
        DragonScrollsUtil.setDragonScrolls(uid, 1000000);
        WitherScrollsUtil.setWitherScrolls(uid, 1000000);
        OrbUtil.setOrbs(uid, 1000000);
        TokensUtil.setTokens(uid, 1000000);
        GoldenTokensUtil.setGoldenTokens(uid, 1000000);
        IngotUtil.setIngots(uid, 1000000);
        KeysUtil.setKeys(uid, "Wood Key", 1000000);
        KeysUtil.setKeys(uid, "Metal Key", 1000000);
        KeysUtil.setKeys(uid, "Titanium key", 1000000);
        KeysUtil.setKeys(uid, "Steel Key", 1000000);
        KeysUtil.setKeys(uid, "Dragon Steel Key", 1000000);
        KeysUtil.setKeys(uid, "Titan Alloy Key", 1000000);
        PetUtil.getPetUtil(uid).addPet("RARE_BETTA_FISH", 1);
        p.getEcoUser().setBalance(new BigInteger("1000000"));
    }
}
