package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.Event;
import me.affluent.decay.language.Language;
import me.affluent.decay.specialevent.SpecialEvent;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.InGameTime;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EventCommand extends BotCommand {

    public EventCommand() {
        this.name = "event";
        this.cooldown = 1.0;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        boolean isEventActive = SpecialEvent.isEventActive();
        if (args.length < 1) {
            int points = SpecialEvent.getCurrentEvent().getPoints(uid);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(15, 141, 255));
            eb.setTitle(Language.getLocalized(uid, "event_title", "Usage"));
            eb.setDescription(Language.getLocalized(uid, "event_description",
                            "Please use {command_usage}.")
                    .replace("{command_usage}", "`" + userPrefix + "event <info | list>`"));
            eb.setFooter("You currently have " + points + " points");
            e.reply(eb.build());
            return;
        }
        String events = "";
        for (SpecialEvent specialEvent1 : SpecialEvent.getSpecialEvents()) {
            events += "- **" + specialEvent1.getName() + "** - " + specialEvent1.getShortDescription() + "\n";
        }
        if (args[0].equalsIgnoreCase("list")) {
            e.reply(MessageUtil.info(Language.getLocalized(uid, "event_list_title", "Events"),
                    Language.getLocalized(uid, "event_info", events)));
            return;
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (!isEventActive) {
                e.reply(MessageUtil.err(Language.getLocalized(uid, "event_plain", "Event"),
                        Language.getLocalized(uid, "event_not_active", "There is no active event. Check back later!")));
                return;
            }
            SpecialEvent specialEvent = SpecialEvent.getCurrentEvent();
            String timeLeftDisplay = SpecialEvent.getTimeLeftDisplay(uid) + " left";
            String description = "Use {top_event_command} to view the leaderboard.\n\n " +
                    "{event_description}\n\n__Rewards:__\n{rewards}";
            description = description
                    .replace("{top_event_command}", "`" + userPrefix + "top event`")
                    .replace("{event_description}", specialEvent.getDescription())
                    .replace("{rewards}", specialEvent.getRewardsDisplay());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(15, 141, 255));
            eb.setTitle(
                    Language.getLocalized(uid, "event_title", "{event} Event").replace("{event}", specialEvent.getName()));
            eb.setDescription(Language.getLocalized(uid, "event_description",
                    description.replace("w.", userPrefix)));
            eb.setFooter(timeLeftDisplay, "https://i.imgur.com/RbHmy82.png");
            e.reply(eb.build());
        }
    }
}