package me.affluent.decay.util.system;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageUtil {

    public static MessageEmbed info(String title, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(new Color(15, 141, 255));
        return eb.build();
    }

    public static MessageEmbed success(String title, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(new Color(19, 255, 58));
        return eb.build();
    }

    public static MessageEmbed err(String title, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(new Color(250, 58, 35));
        return eb.build();
    }

    public static boolean sendMessage(User u, String msg1, boolean presuffix) {
        if (presuffix) msg1 = "You received a message from an **Admin** of **Withering**:\n\n`" + msg1 +
                              "`\n\nTo respond, please visit the official discord server of Withering.\nClick the " +
                              "Discord-Invite link at `w.invite` to join.";
        final String msg = msg1;
        final AtomicBoolean sent = new AtomicBoolean(false);
        u.openPrivateChannel().queue(s -> s.sendMessage(msg).queue(ss -> sent.set(true), f -> {
            sent.set(false);
        }));
        return true;
    }
}