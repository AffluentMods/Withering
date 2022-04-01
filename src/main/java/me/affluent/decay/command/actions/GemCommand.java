package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.GemUtil;
import me.affluent.decay.util.IngotUtil;
import me.affluent.decay.util.system.EmoteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class GemCommand extends BotCommand {

    public GemCommand() {
        this.name = "gem";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        Player p = Player.getPlayer(uid);

        if (args.length < 1) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(15, 141, 255));
            eb.setTitle("Gem");
            eb.setDescription(Language.getLocalized(uid, "usage", "Please use {command_usage}.\n" +
                            "`<>` - Required Parameter\n`[]` - Optional Parameter\n\n" +
                            "You currently have {amount} Gem Dust.")
                    .replace("{command_usage}", "`" + userPrefix + "gem`")
                    .replace("{amount}", "`" + GemUtil.getGemDust(uid) + "` " + EmoteUtil.getEmoteMention("Gem_Dust")));
            e.getTextChannel().sendMessage(eb.build()).queue();
            return;
        }

        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
    }
}
