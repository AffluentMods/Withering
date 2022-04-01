package me.affluent.decay.command.information;

import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.EmoteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class RulesCommand extends BotCommand {

    public RulesCommand() {
        this.name = "rules";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Language.getLocalized(uid, "rules_plain", "Rules & Information"));
            eb.setThumbnail("https://i.imgur.com/RbHmy82.png");
            eb.setDescription(Language.getLocalized(uid, "rules",
                    "`1.` All users must abide by Discord's TOS, and Community Guidelines.\n" +
                            "`2.` More specifically, no Macros, Bots, Scripts, or any other automated software.\n" +
                            "`3.` Trading has freedom to trade with any player as much as you want. Using alts to 'boost' your main account is not allowed.\n" +
                            "• All trades are logged, anyone found abusing the trading system may receive a perm ban from trading, or even Withering.\n" +
                            "• Alts on the other hand, are indeed allowed in Withering. As long as they are not made specifically for the purpose of boosting.\n\n" +
                            "Join the [Official Server](https://discord.gg/withering) if you ever have any questions."
            ));
            eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
            e.getTextChannel().sendMessage(eb.build()).queue();
        }
    }
}
