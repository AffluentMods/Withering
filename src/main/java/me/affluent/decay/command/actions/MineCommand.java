package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.event.GiftingEvent;
import me.affluent.decay.event.MiningEvent;
import me.affluent.decay.language.Language;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.skill.Skill;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.IngotUtil;
import me.affluent.decay.util.SkillUtil;
import me.affluent.decay.util.system.CooldownUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MineCommand extends BotCommand {

    public MineCommand() {
        this.name = "mine";
        this.aliases = new String[]{"m"};
        this.cooldown = 6;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        long mineCooldown = CooldownUtil.getCooldown(uid, "mine_cooldown");
        if (mineCooldown > 0) {
            long now = System.currentTimeMillis();
            long cdDiff = mineCooldown - now;
            if (cdDiff > 0) {
                String cooldownString = CooldownUtil.format(cdDiff, uid);
                String msg1 = Constants.COOLDOWN(uid).replace("{cooldown}", cooldownString);
                e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
                return;
            }
        }
        int randomIngotAmount = new Random().nextInt(1001);
        int randomIngotMinusAmount = new Random().nextInt(10);
        long ingotAmount = 0;
        double amount = 0;
        int asl = SkillUtil.getLevel(uid, 5);
        if (asl > 0) {
            Skill dwarfSkill = Skill.getSkill(5);
            amount = (double) dwarfSkill.getValue(asl);
            amount = (amount / 100) + 1;
        }
        if (randomIngotAmount <= 150) ingotAmount = (33 - randomIngotMinusAmount);
        if (randomIngotAmount <= 520 && (randomIngotAmount > 151)) ingotAmount = (23 - randomIngotMinusAmount);
        if (randomIngotAmount <= 840 && (randomIngotAmount > 521)) ingotAmount = (13 - randomIngotMinusAmount);
        if (amount > 0) ingotAmount *= amount;
            e.reply(MessageUtil.success(Language.getLocalized(uid, "mine", "Mining"),
                    Language.getLocalized(uid, "usage", "You have begun an adventure to the caves.\n" +
                            "Oh, what amazing ore await you this time? Perhaps something... else awaits.")));
            Timer timer = new Timer();
            long finalIngotAmount = ingotAmount;
            MiningEvent miningEvent = new MiningEvent(p, (int) finalIngotAmount);
            EventManager.callEvent(miningEvent);
            timer.schedule(new TimerTask() {
                public void run() {
                    IngotUtil.addIngots(uid, finalIngotAmount);
                    if (finalIngotAmount == 0) {
                        e.reply(MessageUtil.info(Language.getLocalized(uid, "mine", "Empty"),
                                Language.getLocalized(uid, "usage", "Cave seems empty. I'm free to try another location in about 5 minutes.")));
                        CooldownUtil.addCooldown(uid, "mine_cooldown", System.currentTimeMillis() + (300 * 1000), true);
                    }
                    if (finalIngotAmount >= 2) {
                        e.reply(MessageUtil.success(Language.getLocalized(uid, "mine", "Rewards"),
                                Language.getLocalized(uid, "usage", "W-what was that! No, I ain't going back down there. Not for at least 45 minutes!\n" +
                                                "I was able to grab {amount} Alloy Ingot's however")
                                        .replace("{amount}", finalIngotAmount + " " + EmoteUtil.getEmoteMention("Alloy_Ingot"))));
                        CooldownUtil.addCooldown(uid, "mine_cooldown", System.currentTimeMillis() + (2700 * 1000), true);
                        }
                }
            }, 5000L );
    }
}