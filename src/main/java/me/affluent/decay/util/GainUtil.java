package me.affluent.decay.util;

import me.affluent.decay.entity.Player;
import me.affluent.decay.util.system.FormatUtil;
import net.dv8tion.jda.internal.utils.Checks;

public class GainUtil {

    public static int getHealingCost(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return 1;
        if (level <= 34) return 2;
        if (level <= 49) return 4;
        if (level <= 74) return 6;
        if (level <= 99) return 9;
        if (level <= 149) return 12;
        return 16;
    }

    public static int getExpGainAttackWin(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return 450;
        if (level <= 19) return 2100;
        if (level <= 34) return 7500;
        if (level <= 49) return 11000;
        if (level <= 74) return 19000;
        if (level <= 100) return 30000;
        return 45000;
    }

    public static int getExpGainAttackLose(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return 150;
        if (level <= 19) return 650;
        if (level <= 34) return 3000;
        if (level <= 49) return 5000;
        if (level <= 74) return 8500;
        if (level <= 100) return 13500;
        return 20000;
    }

    public static int getMedallionGainAttackWin(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return FormatUtil.getBetween(8, 15);
        if (level <= 19) return FormatUtil.getBetween(30, 45);
        if (level <= 34) return FormatUtil.getBetween(90, 135);
        if (level <= 49) return FormatUtil.getBetween(270, 405);
        if (level <= 74) return FormatUtil.getBetween(510, 775);
        if (level <= 100) return FormatUtil.getBetween(1550, 1890);
        return FormatUtil.getBetween(3200, 4500);
    }

    public static int getMedallionGainAttackLose(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return FormatUtil.getBetween(5, 9);
        if (level <= 19) return FormatUtil.getBetween(18, 27);
        if (level <= 34) return FormatUtil.getBetween(54, 81);
        if (level <= 49) return FormatUtil.getBetween(162, 243);
        if (level <= 74) return FormatUtil.getBetween(350, 550);
        if (level <= 100) return FormatUtil.getBetween(850, 1250);
        return FormatUtil.getBetween(1750, 2500);
    }

    //

    public static int getExpGainDefendWin(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return 35;
        if (level <= 19) return 160;
        if (level <= 34) return 565;
        if (level <= 49) return 825;
        if (level <= 74) return 1800;
        if(level <= 100) return 3750;
        return 6000;
    }

    public static int getExpGainDefendLose(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return 18;
        if (level <= 19) return 80;
        if (level <= 34) return 280;
        if (level <= 49) return 410;
        if (level <= 74) return 900;
        if (level <= 100) return 1650;
        return 2625;
    }

    public static int getMedallionGainDefendWin(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return FormatUtil.getBetween(2, 4);
        if (level <= 19) return FormatUtil.getBetween(8, 12);
        if (level <= 34) return FormatUtil.getBetween(22, 30);
        if (level <= 49) return FormatUtil.getBetween(60, 92);
        if (level <= 74) return FormatUtil.getBetween(135, 175);
        if (level <= 100) return FormatUtil.getBetween(275, 375);
        return FormatUtil.getBetween(635, 875);
    }

    public static int getMedallionGainDefendLose(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return FormatUtil.getBetween(1, 3);
        if (level <= 19) return FormatUtil.getBetween(3, 9);
        if (level <= 34) return FormatUtil.getBetween(7, 22);
        if (level <= 49) return FormatUtil.getBetween(25, 42);
        if (level <= 74) return FormatUtil.getBetween(55, 74);
        if (level <= 100) return FormatUtil.getBetween(120, 160);
        return FormatUtil.getBetween(285, 435);
    }

    public static int getDiamondsGainPvP(Player p, int level) {
        //if (p.getExpUser().getLevel() < 1) p.getExpUser().setLevel(1);
        Checks.check(level > 0, "level must be bigger than 0");
        Checks.check(level < 152, "level must be smaller than 152");
        if (level <= 9) return 1;
        if (level <= 19) return 2;
        if (level <= 34) return 5;
        if (level <= 49) return 15;
        if (level <= 74) return 35;
        if (level <= 100) return 50;
        return 75;
    }
}