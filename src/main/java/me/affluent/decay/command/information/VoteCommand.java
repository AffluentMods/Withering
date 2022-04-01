package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.InventoryUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.DonatorUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.vote.VoteClient;
import me.affluent.decay.vote.VoteSystem;
import net.dv8tion.jda.api.entities.User;

import java.util.Random;

public class VoteCommand extends BotCommand {

    public static String voteLink = "https://top.gg/bot/887850655862112268/vote";

    public VoteCommand() {
        this.name = "vote";
        this.aliases = new String[]{"v"};
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (e.getArgs().length > 0 && e.getArgs()[0].equalsIgnoreCase("check")) {
            if (VoteSystem.hasVoted(uid)) {
                String active_vote = Language.getLocalized(uid, "active_vote", "Active Vote");
                String active_vote_msg =
                        Language.getLocalized(uid, "active_vote_message", "You already have an active vote boost!");
                e.reply(MessageUtil.err(active_vote, active_vote_msg));
                return;
            }
            if (Withering.dblapi != null) {
                Withering.dblapi.hasVoted(uid).whenCompleteAsync((voted, error) -> {
                    if (error != null) {
                        e.reply(MessageUtil.err(Constants.ERROR(uid),
                                Language.getLocalized(uid, "data_retrieve_fail", "Could not retrieve data")));
                        return;
                    }
                    if (voted) {
                        VoteClient.vote(uid,true);
                    } else {
                        String vote = Language.getLocalized(uid, "vote", "Vote");
                        e.reply(MessageUtil.err(vote, Language.getLocalized(uid, "no_vote", "You didn't vote yet!")));
                    }
                });
            }
            return;
        }
        if (VoteSystem.hasVoted(uid)) {
            String active_vote = Language.getLocalized(uid, "active_vote", "Active Vote");
            String active_vote_msg =
                    Language.getLocalized(uid, "active_vote_message", "You already have an active vote boost!");
            e.reply(MessageUtil.err(active_vote, active_vote_msg));
        }
        Player p = Player.getPlayer(uid);
        InventoryUser inv = p.getInventoryUser();
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        int tokens = VoteClient.getTokenReward(VoteClient.lastWeekend) + (DonatorUtil.isBoosting(uid) ? 1 : 0);
        final String tokenEmote = EmoteUtil.getEmoteMention("Tavern_Token");
        e.reply(MessageUtil.info(Language.getLocalized(uid, "vote", "Vote"), Language.getLocalized(uid, "vote_message",
                " You can vote for Withering [here]({vote_link})" +
                "\nYou didn't receive your rewards? Type {vote_check_command} to get them!\n\nVote rewards:\n{rewards}")
                .replace("{vote_link}", voteLink).replace("{vote_check_command}", "`" + userPrefix + "vote check`")
                .replace("{rewards}", "`" + tokens + "x` " + tokenEmote + "\n\n`" + userPrefix + "daily`")));
    }
}