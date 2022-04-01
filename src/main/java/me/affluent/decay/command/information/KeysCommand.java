package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.KeysUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class KeysCommand extends BotCommand {

    public KeysCommand() {
        this.name = "keys";
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        Player p = Player.getPlayer(uid);
        HashMap<String, Integer> keys = KeysUtil.getAllKeys(uid);
        StringBuilder keysDisplay = new StringBuilder();
        for (String sortKey : sort) {
            if (keys.containsKey(sortKey.toLowerCase())) {
                keysDisplay.append(EmoteUtil.getEmoteMention(sortKey)).append(" ").append(sortKey)
                        .append(" `x").append(keys.get(sortKey.toLowerCase())).append("`\n");
            }
        }
        if (keysDisplay.toString().equalsIgnoreCase("")) keysDisplay = new StringBuilder("-");
        e.reply(MessageUtil.success(Language.getLocalized(uid, "keys_of", "Keys of {user_tag}")
                .replace("{user_tag}", p.getUser().getAsTag()), keysDisplay.toString()));
    }

    private static final List<String> sort =
            Arrays.asList("wood key", "metal key", "titanium key", "steel key", "dragon steel key", "titan alloy key");
}