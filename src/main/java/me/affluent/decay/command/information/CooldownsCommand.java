package me.affluent.decay.command.information;

import me.affluent.decay.command.utility.DailyCommand;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.BoostUtil;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.CooldownUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FireworkSystem;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.vote.VoteClient;
import me.affluent.decay.vote.VoteSystem;
import net.dv8tion.jda.api.entities.User;

public class CooldownsCommand extends BotCommand {

    public CooldownsCommand() {
        this.name = "cooldowns";
        this.aliases = new String[]{"cd", "cds", "timer", "timers"};
        this.cooldown = 0.75;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        Player p = Player.getPlayer(uid);
        StringBuilder boosts = new StringBuilder();
        long now = System.currentTimeMillis();
        if (VoteSystem.hasVoted(uid)) {
            long until = VoteSystem.getUntilVote(uid);
            long diff = until - now;
            String emote;
            if (p.getExpUser().isMaxed()) emote = "Elixir_XP_Orb";
            else emote = "XP_Orb";
            boosts.append("**").append(Language.getLocalized(uid, "vote", EmoteUtil.getEmoteMention(emote) + " Vote")).append(" EXP**: ").append("`")
                    .append(VoteClient.expBoost).append("%` - ").append(CooldownUtil.format(diff)).append("\n");
        }
        if (FireworkSystem.hasFirework(uid)) {
            long until = FireworkSystem.getFireworkTime(uid);
            long diff = until - now;
            boosts.append("**").append(Language.getLocalized(uid, "firework", EmoteUtil.getEmoteMention("Holiday_Firework") + " Firework")).append(" EXP**: `100%` - ")
                    .append(CooldownUtil.format(diff)).append("\n");
            boosts.append("**").append(Language.getLocalized(uid, "firework", EmoteUtil.getEmoteMention("Holiday_Firework") + " Firework")).append(" Coin**: `100%` - ")
                    .append(CooldownUtil.format(diff)).append("\n");
        }
        if (CooldownUtil.hasCooldown(uid, "pvp_barrier")) {
            long until = CooldownUtil.getCooldown(uid, "pvp_barrier");
            long diff = until - now;
            boosts.append("**").append(Language.getLocalized(uid, "barrier", EmoteUtil.getEmoteMention("Protection") + " barrier")).append("**: ")
                    .append(CooldownUtil.format(diff)).append("\n");
        }
        if (CooldownUtil.hasCooldown(uid, "pvp_cooldown")) {
            long until = CooldownUtil.getCooldown(uid, "pvp_cooldown");
            long diff = until - now;
            boosts.append("**").append(Language.getLocalized(uid, "pvpcd", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getName()) + " Arena Cooldown")).append("**: ")
                    .append(CooldownUtil.format(diff)).append("\n");
        }
        if (CooldownUtil.hasCooldown(uid, "conquest_cooldown")) {
            long until = CooldownUtil.getCooldown(uid, "conquest_cooldown");
            long diff = until - now;
            boosts.append("**").append(Language.getLocalized(uid, "conquestcd", EmoteUtil.getDiamond() + " Conquest Cooldown")).append("**: ")
                    .append(CooldownUtil.format(diff)).append("\n");
        }
        if (CooldownUtil.hasCooldown(uid, "mine_cooldown")) {
            long until = CooldownUtil.getCooldown(uid, "mine_cooldown");
            long diff = until - now;
            boosts.append("**").append(Language.getLocalized(uid, "mining", EmoteUtil.getEmoteMention("Alloy Ingot") + " Mine Cooldown")).append("**: ")
                    .append(CooldownUtil.format(diff)).append("\n");
        }
        for (BoostUtil.Boost boost : BoostUtil.getAllBoosts(uid)) {
            String boostName = boost.getName();
            String boostValue = boost.getValueDisplay();
            long boostUntil = boost.getEnd();
            long diff = boostUntil - now;
            if (diff < 0) {
                BoostUtil.removeBoost(uid, boostName);
                continue;
            }
            String boostDisplay = "**" +
                                  (boostName.substring(0, 1).toUpperCase() + boostName.substring(1).toLowerCase())
                                          .replaceAll("_", " ") + "**: " + boostValue + " (" +
                                  CooldownUtil.format(diff) + ")";
            boosts.append(boostDisplay).append("\n");
        }
        if (boosts.toString().equalsIgnoreCase(""))
            boosts = new StringBuilder(Language.getLocalized(uid, "no_boosts", "No active boosts."));
        e.reply(MessageUtil.success(Language.getLocalized(uid, "boosts", "Cooldowns"), boosts.toString()));
    }
}