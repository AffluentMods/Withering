package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.settingsUtil.ConfirmUtil;
import me.affluent.decay.util.GainUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.math.BigInteger;

public class HealCommand extends BotCommand {

    public HealCommand() {
        this.name = "heal";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        Player p = Player.getPlayer(uid);
        int currentHealth = p.getHealthUser().getHealth();
        int maxHealth = p.getHealthUser().getMaxHealth();
        int amount = 0;
        BigInteger balanceAmount = p.getEcoUser().getBalance().divide(BigInteger.valueOf((long) 87.76333));
        if (maxHealth - currentHealth > 0) {
            amount = (int) ((int) ((((maxHealth - currentHealth) * (GainUtil.getHealingCost(p, p.getExpUser().getLevel()))) + (balanceAmount.longValue()))) / 2.137);
        }
        String confirmation = ConfirmUtil.getConfirmUtil(uid).getConfirmSetting();
        if (confirmation.equalsIgnoreCase("disabled")) {
            if (args.length < 1) {
                if (p.getEcoUser().getBalance().compareTo(new BigInteger(String.valueOf(amount))) < 0) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(new Color(255, 58, 35));
                    eb.setTitle("Error");
                    eb.setDescription(Language.getLocalized(uid, "missing_needed_items_ingots",
                                    "*checks pockets* \n" + "empty they are, cant afford.\n\n" +
                                            " You need " + EmoteUtil.getCoin() + " {cost} coins to heal.")
                            .replace("{cost}", "`" + amount + "`"));
                    e.reply(eb.build());
                    return;
                } else {
                    e.reply(MessageUtil.success(Language.getLocalized(uid, "healed", "Fully Healed"),
                            Language.getLocalized(uid, "usage", "You have invoked the energy of " + EmoteUtil.getCoin() + " {amount}. Successfully healed!")
                                    .replace("{amount}", "`" + amount + "`")));
                    p.getHealthUser().setHealth(maxHealth);
                    p.getEcoUser().removeBalance(amount);
                    return;
                }
            }
        }
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            e.reply(MessageUtil.info(Language.getLocalized(uid, "heal", "Healing"),
                    Language.getLocalized(uid, "usage", "This will cost " + EmoteUtil.getCoin() + " {amount}. If you wish to heal, use `" + userPrefix + "{usage}`")
                            .replace("{usage}", "heal confirm")
                            .replace("{amount}", "`" + amount + "`")));
            return;
        }
        if (!(Player.playerExists(uid))) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        if (args[0].equalsIgnoreCase("confirm")) {
            if (p.getEcoUser().getBalance().compareTo(new BigInteger(String.valueOf(amount))) < 0) {
                e.reply(Constants.CANT_AFFORD(uid));
            } else {
                e.reply(MessageUtil.success(Language.getLocalized(uid, "healed", "Fully Healed"),
                        Language.getLocalized(uid, "usage", "You have invoked the energy of " + EmoteUtil.getCoin() + " {amount}. Successfully healed!")
                                .replace("{amount}", "`" + amount + "`")));
                p.getHealthUser().setHealth(maxHealth);
                p.getEcoUser().removeBalance(amount);
            }
        }
    }
}
