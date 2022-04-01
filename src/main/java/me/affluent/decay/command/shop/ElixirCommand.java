package me.affluent.decay.command.shop;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.skill.Skill;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.DiamondsUtil;
import me.affluent.decay.util.ElixirUtil;
import me.affluent.decay.util.SkillUtil;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ElixirCommand extends BotCommand {

    public ElixirCommand() {
        this.name = "elixir";
        this.cooldown = 1.5;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        String[] args = e.getArgs();
        if (args.length < 1) {
            String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
            final String TAB = Constants.TAB;
            final String EE = EmoteUtil.getEmoteMention("elixir");

            Skill js = Skill.getSkill(1);
            int jsl = SkillUtil.getLevel(uid, 1);
            String jsc = FormatUtil.formatCommas(js.getElixirCost(jsl + 1));
            String jv = js.getValue(jsl + 1) + "";
            String jvn = js.getValue(jsl) + "";
            if (jsl >= 50) jv = "Maxed: " + js.getValue(jsl);

            Skill bs = Skill.getSkill(2);
            int bsl = SkillUtil.getLevel(uid, 2);
            String bsc = FormatUtil.formatCommas(bs.getElixirCost(bsl + 1));
            String bv = bs.getValue(bsl + 1) + "";
            String bvn = bs.getValue(bsl) + "";
            if (bsl >= 50) bv = "Maxed: " + bs.getValue(bsl);

            Skill ss = Skill.getSkill(3);
            int ssl = SkillUtil.getLevel(uid, 3);
            String ssc = FormatUtil.formatCommas(ss.getElixirCost(ssl + 1));
            String sv = ss.getValue(ssl + 1) + "";
            String svn = ss.getValue(ssl) + "";
            if (ssl >= 50) sv = "Maxed: " + ss.getValue(ssl);

            Skill as = Skill.getSkill(4);
            int asl = SkillUtil.getLevel(uid, 4);
            String asc = FormatUtil.formatCommas(as.getElixirCost(asl + 1));
            String av = as.getValue(asl + 1) + "";
            String avn = as.getValue(asl) + "";
            if (asl >= 20) av = "Maxed: " + as.getValue(asl);

            Skill ds = Skill.getSkill(5);
            int dsl = SkillUtil.getLevel(uid, 5);
            String dsc = FormatUtil.formatCommas(ds.getElixirCost(dsl + 1));
            String dv = ds.getValue(dsl + 1) + "";
            String dvn = ds.getValue(dsl) + "";
            if (dsl >= 50) dv = "Maxed: " + ds.getValue(dsl);
            //
            String berserkerString1;
            String berserkerString2;
            double berserkerAmount1 = 35.0;
            double berserkerAmount2 = 35.0;
            if (bsl <= 49) {
                berserkerAmount1 = round(Double.parseDouble(bv), 2);
                berserkerAmount2 = round(Double.parseDouble(bvn), 2);
                berserkerString1 = String.valueOf(berserkerAmount1);
                berserkerString2 = String.valueOf(berserkerAmount2);
            } else {
                berserkerString1 = "Maxed: " + berserkerAmount1;
                berserkerString2 = "Maxed: " + berserkerAmount2;
            }

            String msg =
                    EmoteUtil.getEmoteMention("Jug_Skill") + " __Skill 1: Juggernaut__\n" +
                    "- Increases your `HP` by `" + jv + "`.\n" +
                            TAB + "- Current: `" + jvn + "`\n" +
                            TAB + "- Level: `" + jsl + "/50`\n" +
                            TAB + "- Costs: `" + jsc + "` " + EE + " to upgrade\n\n" +

                   EmoteUtil.getEmoteMention("Berserker_Skill") + " __Skill 2: Berserker__\n" +
                   "- Increases your `damage` by `" + berserkerString1 + "%`.\n" +
                            TAB + "- Current: `" + berserkerString2 + "`\n" +
                            TAB + "- Level: `" + bsl + "/50`\n" +
                            TAB + "- Costs `" + bsc + "` " + EE + " to upgrade\n\n" +


                   EmoteUtil.getEmoteMention("Stealth_Skill") + " __Skill 3: Stealth__\n" +
                   "- Increases your dodge chance by `" + sv + "%`.\n" +
                            TAB + "- Current: `" + svn + "`\n" +
                            TAB + "- Level: `" + ssl + "/50`\n" +
                            TAB + "- Costs: `" + ssc + "` " + EE + " to upgrade\n\n" +

                   EmoteUtil.getEmoteMention("Assassin_Skill") + " __Skill 4: Assassin__\n" +
                   "- Increases your chance of Assassination by `" + av + "%`\n" +
                            TAB + "- Current: `" + avn + "`\n" +
                            TAB + "- Level: `" + asl + "/20`\n" +
                            TAB + "- Costs: `" + asc + "` " + EE + " to upgrade\n\n" +

                   EmoteUtil.getEmoteMention("Dwarf_Skill") + " __Skill 5: Dwarf__\n" +
                   "- Increases the amount of ingots mined by `" + dv + "%`\n" +
                           TAB + "- Current: `" + dvn + "`\n" +
                           TAB + "- Level: `" + dsl + "/50`\n" +
                           TAB + "- Costs: `" + dsc + "` " + EE + " to upgrade";

            msg += "\n\nUse `" + userPrefix + "elixir <skill number> [amount]` to upgrade a skill.\n" +
                    "Use `" + userPrefix + "elixir reset` to reset all skills for " + EmoteUtil.getDiamond() + " `5,000` Diamonds";
            e.reply(MessageUtil.info(Language.getLocalized(uid, "skills_plain", "Skills"), msg));
            return;
        }
        String arg = args[0].toLowerCase();
        if (arg.equalsIgnoreCase("reset")) {
            long diamonds = DiamondsUtil.getDiamonds(uid);
            if (diamonds < 5000) {
                e.reply(MessageUtil.err(Constants.ERROR(uid),
                        Language.getLocalized(uid, "not_enough", "You need `5000` Diamonds to reset skills")));
                return;
            }
            int totalElixir = 0;
            for (int i = 1; i <= 5; i++) {
                int level = SkillUtil.getLevel(uid, i);
                Skill skill = Skill.getSkill(i);
                totalElixir += skill.getTotalElixirCost(0, level);
                SkillUtil.setLevel(uid, i, 0);
            }
            ElixirUtil.setElixir(uid, totalElixir);
            DiamondsUtil.setDiamonds(uid, diamonds - 5000);
            e.reply(MessageUtil.success(Language.getLocalized(uid, "reset", "Reset"),
                    Language.getLocalized(uid, "reset_successful",
                            "Successfully reset skills!")));
            return;
        }
        int skillID;
        try {
            skillID = Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "parameter_number_required", "The argument {argument} must be a number!")
                            .replace("{argument}", "`<skill number>`")));
            return;
        }
        int levelAmount;
        if (args.length > 1) {
            try {
                levelAmount = Integer.parseInt(args[1]);
                if (levelAmount < 1 || levelAmount > 50) {
                    e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "argument_between",
                                    "The argument {argument} has to be between {min} and {max}!")
                            .replace("{argument}", "`<level amount>`")
                            .replace("{min}", "`1`")
                            .replace("{max}", "`50`")));
                    return;
                }
            } catch (NumberFormatException ex) {
                levelAmount = 1;
            }
        } else {
            levelAmount = 1;
        }
        Skill skill = Skill.getSkill(skillID);
        if (skill == null) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "invalid_skill", "A skill with the ID {id} does not exist.")
                            .replace("{id}", "`" + skillID + "`")));
            return;
        }
        int sl = SkillUtil.getLevel(uid, skillID);
        int finalLevel = sl + levelAmount;
        if (skillID == 4 && finalLevel > 20) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "max_level", "The maximum level of this skill is 20.")));
            return;
        }
        if (finalLevel > 50) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "max_level_50", "The maximum level of this skill is 50.")));
            return;
        }
        int elixirCost = skill.getTotalElixirCost(sl, finalLevel);
        int elixir = ElixirUtil.getElixir(uid);
        if (elixir < elixirCost) {
            e.reply(Constants.CANT_AFFORD(uid));
            return;
        }
        skill.onBuy(uid);
        ElixirUtil.setElixir(uid, elixir - elixirCost);
        SkillUtil.setLevel(uid, skillID, finalLevel);
        e.reply(MessageUtil.success(Language.getLocalized(uid, "skills_plain", "Skills"),
                Language.getLocalized(uid, "skill_upgrade_successful",
                        "Successfully upgraded {skill_name} to Level {skill_level}!")
                        .replace("{skill_name}", skill.getName()).replace("{skill_level}", "`" + (finalLevel) + "`")));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}