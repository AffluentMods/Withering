package me.affluent.decay.command.actions;

import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class DeleteCommand extends BotCommand {

    public DeleteCommand() {
        this.name = "delete";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        e.reply(MessageUtil.info(Language.getLocalized(uid, "info_plain", "Delete Removed"),
                Language.getLocalized(uid, "usage", "`" + userPrefix + "delete` command has been removed.\n" +
                        "If you wish to delete items, refer to the `" + userPrefix + "forge dissolve` command.\n" +
                        "`" + userPrefix + "help forge`")));
        return;
    }
}