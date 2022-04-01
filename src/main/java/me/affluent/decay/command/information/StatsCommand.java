package me.affluent.decay.command.information;

import me.affluent.decay.Constants;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.ArmorSet;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.language.Language;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.skill.Skill;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.MessageUtil;
import me.affluent.decay.util.system.StatsUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StatsCommand extends BotCommand {

    public StatsCommand() {
        this.name = "stats";
        this.aliases = new String[]{"statistic", "statistics"};
        this.cooldown = 0.75;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        Player p = Player.getPlayer(uid);
        ArmorUser au = p.getArmorUser();
        Weapon weapon = au.getWeapon();
        int wd1 = weapon.getDamageFrom();
        int wd2 = weapon.getDamageTo();
        int wad = weapon.getRarityDamage();
        int wap = weapon.getRarityProtection();
        Arrow arrow = au.getArrow();
        Shield shield = au.getShield();
        int ad = 0;
        int sp = 0;
        if (arrow != null) {
            ad = arrow.getDamage();
            wad += arrow.getRarityDamage();
        }
        if (shield != null) {
            sp = shield.getProtection();
            wap = shield.getRarityProtection();
        }
        //
       /* BerserkerSkill bs = (BerserkerSkill) Skill.getSkill(2);
        int bsl = SkillUtil.getLevel(uid, 2);
        double bv = bs.getValue(bsl) / 100.0;
        wd1 += wd1 * bv;
        wd2 += wd2 * bv;
        ad += ad * bv;
        wad += wad * bv; */
        //
        String stats = "";
        if (arrow != null) {
            stats += EmoteUtil.getEmoteMention(weapon.getName()) + " __Weapon__ " + EmoteUtil.getEmoteMention(arrow.getName()) + "\n" +
                    "`" + wd1 + "-" + wd2 + "`[+`" + ad + "`] Base Damage[Arrow Damage]\n\n" +
                    EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getRarity().name() + "_gear") + " Weapon Rarity Bonus " + EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getArrow().getRarity().name() + "_gear") + "\n" +
                    "`+" + wad + "` Damage\n\n";
        } else if (shield != null) {
            stats += EmoteUtil.getEmoteMention(weapon.getName()) + " __weapon__ " + EmoteUtil.getEmoteMention(shield.getName()) + "\n" +
                    "`" + wd1 + "-" + wd2 + "`[+`" + sp + "`] Base Damage[Shield Protection]\n\n" +
                    EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getRarity().name() + "_gear") + " Weapon Rarity Bonus " + EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getShield().getRarity().name() + "_gear") + "\n" +
                    "`+" + wad + "` Damage\n" +
                    "`+" + wap + "` Protection\n\n";
        } else {
            stats += EmoteUtil.getEmoteMention(weapon.getName()) + " __Weapon__ " + EmoteUtil.getEmoteMention(weapon.getName()) + "\n" +
                    "`" + wd1 + "-" + wd2 + "` Base Damage" + "\n\n" +
                    EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getRarity().name() + "_gear") + " Weapon Rarity Bonus " + EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getWeapon().getRarity().name() + "_gear") + "\n" +
                    "`+" + wad + "` Damage\n\n";
        }
        List<Armor> armors = au.getAllArmor();
        int ap1 = 0;
        int ap2 = 0;
        //
        int aad1 = 0; // Attribute Dodge
        int aad2 = 0;
        int aap1 = 0; // Attribute Protection
        int aap2 = 0;
        int aagc1 = 0; // Attribute Gold Coins
        int aagc2 = 0;
        int aaxp1 = 0; // Attribute Exp Gain
        int aaxp2 = 0;
        for (Armor armor : armors) {
            ap1 += armor.getProtectionFrom();
            ap2 += armor.getProtectionTo();
            RarityClass rc = Rarities.getRarityClass(armor.getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    int dodge1 = attribute.getDodgeChance().getValue1();
                    int dodge2 = attribute.getDodgeChance().getValue2();
                    int p1 = attribute.getProtection().getValue1();
                    int p2 = attribute.getProtection().getValue2();
                    int gc1 = attribute.getGoldCoins().getValue1();
                    int gc2 = attribute.getGoldCoins().getValue2();
                    int xp1 = attribute.getExp().getValue1();
                    int xp2 = attribute.getExp().getValue2();
                    if (dodge1 > 0) aad1 += dodge1;
                    if (dodge2 > 0) aad2 += dodge2;
                    if (p1 > 0) aap1 += p1;
                    if (p2 > 0) aap2 += p2;
                    if (gc1 > 0) aagc1 += gc1;
                    if (gc2 > 0) aagc2 += gc2;
                    if (xp1 > 0) aaxp1 += xp1;
                    if (xp2 > 0) aaxp2 += xp2;
                }
            }
        }
        int kills1 = Integer.parseInt(StatsUtil.getStat(uid, "attack_kills", "0"));
        int kills2 = Integer.parseInt(StatsUtil.getStat(uid, "defend_kills", "0"));
        int totalKills = kills1 + kills2;
        int deaths1 = Integer.parseInt(StatsUtil.getStat(uid, "attack_deaths", "0"));
        int deaths2 = Integer.parseInt(StatsUtil.getStat(uid, "defend_deaths", "0"));
        int totalDeaths = deaths1 + deaths2;
        double kdKills;
        double kdDeaths;
        if (totalDeaths == 0) {
            kdDeaths = 1.0;
        } else {
            kdDeaths = totalDeaths;
        }
        if (totalKills == 0) {
            kdKills = 1.0;
        } else {
            kdKills = totalKills;
        }
        String chestplate = "";
        if (ArmorUser.getArmorUser(uid).getChestplate() != null) {
            EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(uid).getChestplate().getRarity().name() + "_gear");
        }
        if (au.getChestplate() == null) {
            stats +=
                    " __Armor__ " + "\n" +
                            "`" + ap1 + "-" + ap2 + "` Base Protection\n\n" +
                            chestplate + " Armor Rarity Bonus " + chestplate + "\n" +
                            "`" + aap1 + "-" + aap2 + "` Protection\n" +
                            "`" + aad1 + "-" + aad2 + "%` Dodge Chance\n" +
                            "`" + aagc1 + "-" + aagc2 + "%` Gold Coins\n" +
                            "`" + aaxp1 + "-" + aaxp2 + "%` Exp Rate\n\n" +

                            " __Stats__ \n" +
                            "Kills: " + totalKills + " **|** " +
                            "Deaths: " + totalDeaths + " **|** " +
                            "KD Ratio: " + round(kdKills/kdDeaths, 2);
        } else {
            stats +=
                    EmoteUtil.getEmoteMention(au.getChestplate().getName()) + " __Armor__ " + EmoteUtil.getEmoteMention(au.getChestplate().getName()) + "\n" +
                            "`" + ap1 + "-" + ap2 + "` Base Protection\n\n" +
                            chestplate + " Armor Rarity Bonus " + chestplate + "\n" +
                            "`" + aap1 + "-" + aap2 + "` Protection\n" +
                            "`" + aad1 + "-" + aad2 + "%` Dodge Chance\n" +
                            "`" + aagc1 + "-" + aagc2 + "%` Gold Coins\n" +
                            "`" + aaxp1 + "-" + aaxp2 + "%` Exp Rate\n\n" +

                      " __Stats__ \n" +
                      "Kills: " + totalKills + " **|** " +
                      "Deaths: " + totalDeaths + " **|** " +
                      "KD Ratio: " + round(kdKills/kdDeaths, 2);
        }
        e.reply(MessageUtil.success(Language.getLocalized(uid, "stats", "Stats"), stats));
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}