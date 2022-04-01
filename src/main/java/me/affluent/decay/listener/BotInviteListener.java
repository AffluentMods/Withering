package me.affluent.decay.listener;

import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class BotInviteListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        TextChannel botlogChannel = Withering.getBotLog();
        Guild g = e.getGuild();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Joined Guild!", null, g.getIconUrl());
        eb.setColor(new Color(19, 255, 58));
        eb.appendDescription("ID: `{" + g.getId() + "}`\n" +
                "Server: **" + g.getName() + "**\n" +
                "This server has **" + g.getMemberCount() + "** members.\n" +
                "I am in **" + Withering.getTotalGuilds() + " guilds** now!");
        eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
        botlogChannel.sendMessage(eb.build()).queue();
        Withering.updateStatus();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        TextChannel botlogChannel = Withering.getBotLog();
        Guild g = e.getGuild();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Left Guild!", null, g.getIconUrl());
        eb.setColor(new Color(250, 58, 35));
        eb.appendDescription("ID: `{" + g.getId() + "}`\n" +
                "Server: **" + g.getName() + "**\n" +
                "This server has **" + g.getMemberCount() + "** members.\n" +
                "I am in **" + Withering.getTotalGuilds() + " guilds** now!");
        eb.setFooter("Withering RPG", "https://i.imgur.com/RbHmy82.png");
        botlogChannel.sendMessage(eb.build()).queue();
        Withering.updateStatus();
    }
}