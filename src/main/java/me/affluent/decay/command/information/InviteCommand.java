package me.affluent.decay.command.information;

import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class InviteCommand extends BotCommand {

    public InviteCommand() {
        this.name = "invite";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(Language.getLocalized(uid, "invite_server_title", "Support Server"), "[Link](https://discord.gg/withering)",
                false);
        eb.addField(Language.getLocalized(uid, "invite_bot_title", "Invite Withering"),
                "[Link](https://discord.com/api/oauth2/authorize?client_id=887850655862112268&permissions=379904&scope=bot)",
                false);
        eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
        e.reply(eb.build());
    }
}