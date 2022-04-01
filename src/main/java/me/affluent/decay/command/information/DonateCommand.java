package me.affluent.decay.command.information;

import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class DonateCommand extends BotCommand {

    public DonateCommand() {
        this.name = "donate";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(Language.getLocalized(uid, "donate_link", "Donate Link"), "[Link](https://www.patreon.com/withering)",
                false);
        eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
        e.reply(eb.build());
    }
}