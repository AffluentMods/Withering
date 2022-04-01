package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.captcha.CaptchaAPI;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.pvp.Fight;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.MentionUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.entities.User;

public class PracticeCommand extends BotCommand {

    public PracticeCommand() {
        this.name = "practice";
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        if (e.getArgs().length == 0) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "practice <@user | user#tag>`")));
            return;
        }
        if (CaptchaAPI.isPending(uid)) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "verify_pending",
                    "**Halt There**, verify your intelligence or thee shall be decapitated.\nIf you lost the verify, then use `" +
                    userPrefix + "verify resend`")));
            return;
        }
        User target = MentionUtil.getUser(e.getMessage());
        if (target == null || !Player.playerExists(target.getId())) {
            String msg1 = Language.getLocalized(uid, "target_not_found", "Preposterous! This particular person does not seem to exist");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        if (target.getId().equalsIgnoreCase(uid)) {
            String msg1 = Language.getLocalized(uid, "no_self_harm", "You foolish scoundrel, you grab with the handle, not the pointy side!");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (p.getHealthUser().getHealth() <= 0) {
            String msg1 = Language.getLocalized(uid, "self_dead", "M'lord, you seem to have fallen. Might I suggest you `" + userPrefix + "heal`");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Weapon pw = p.getArmorUser().getWeapon();
        if (pw == null) {
            String msg1 = Language.getLocalized(uid, "no_weapon", "Did you just attempt to fight them with no weapon equipped? You're mad!");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Player t = Player.getPlayer(target.getId());
        if (t.getHealthUser().getHealth() <= 0) {
            String msg1 = Language.getLocalized(uid, "target_dead", "Scurry along now, they're already dead. No need to fight a corpse.");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Weapon tw = t.getArmorUser().getWeapon();
        if (tw == null) {
            String msg1 = Language.getLocalized(uid, "target_no_weapon", "Thee enemy hath no weapon! Quick, grab your blade and slice em!");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Fight fight = new Fight(p, t, pw, tw);
        Player winner = fight.doFight(true, false);
        boolean tWin = winner.getUserId().equals(target.getId());
        String title1 = Language.getLocalized(uid, "practice_ended", "Practice PvP Ended");
        int defender_health_left = fight.getDefenderHealthLeft();
        int attacker_health_left = fight.getAttackerHealthLeft();
        int defender_damage_dealt = fight.getDefenderDamageDealt();
        int attacker_damage_dealt = fight.getAttackerDamageDealt();
        if (tWin) {
            String message1 = Language.getLocalized(uid, "practice_result_lose",
                    "{@attacker} Died against {@defender}.\n\n" +

                            "{attacker_tag}\n" +
                            "• Dealt {attacker_damage} Damage {attacker_emote}\n" +
                            "• {attacker_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +

                            "{defender_tag}\n" +
                            "• Dealt {defender_damage} Damage {defender_emote}\n" +
                            "• {defender_health} Remaining HP " + EmoteUtil.getEmoteMention("HP"))
                    .replace("{attacker_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(p.getUserId()).getWeapon().getName()))
                    .replace("{defender_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(t.getUserId()).getWeapon().getName()))
                    .replace("{@attacker}", p.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                    .replace("{@defender}", t.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                    .replace("{attacker_tag}", "`" + p.getUser().getAsTag() + "`")
                    .replace("{defender_tag}", "`" + t.getUser().getAsTag() + "`")
                    .replace("{defender_damage}", "`" + defender_damage_dealt + "`")
                    .replace("{attacker_damage}", "`" + attacker_damage_dealt + "`")
                    .replace("attacker_health", "`" + attacker_health_left + "`")
                    .replace("{defender_health}", "`" + defender_health_left + "`");
            e.reply(MessageUtil.err(title1, message1));
        } else {
            String message1 = Language.getLocalized(uid, "practice_result_win",
                            "{@attacker} Killed {@defender}.\n\n" +

                                    "{attacker_tag}\n" +
                                    "• Dealt {attacker_damage} Damage {attacker_emote}\n" +
                                    "• {attacker_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +

                                    "{defender_tag}\n" +
                                    "• Dealt {defender_damage} Damage {defender_emote}\n" +
                                    "• {defender_health} Remaining HP " + EmoteUtil.getEmoteMention("HP"))
                    .replace("{attacker_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(p.getUserId()).getWeapon().getName()))
                    .replace("{defender_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(t.getUserId()).getWeapon().getName()))
                    .replace("{@attacker}", p.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                    .replace("{@defender}", t.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                    .replace("{attacker_tag}", "`" + p.getUser().getAsTag() + "`")
                    .replace("{defender_tag}", "`" + t.getUser().getAsTag() + "`")
                    .replace("{defender_damage}", "`" + defender_damage_dealt + "`")
                    .replace("{attacker_damage}", "`" + attacker_damage_dealt + "`")
                    .replace("{attacker_health}", "`" + attacker_health_left + "`")
                    .replace("{defender_health}", "`" + defender_health_left + "`");
            e.reply(MessageUtil.success(title1, message1));
        }
    }
}