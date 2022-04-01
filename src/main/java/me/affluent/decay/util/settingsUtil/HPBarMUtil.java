package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.util.system.EmoteUtil;

import java.util.ArrayList;
import java.util.List;

public class HPBarMUtil {

    public static String getMobileBar(int hp, int maxhp, int xp, double maxxp) {
        int columns = 4;
        return progressPercentage(hp, maxhp, xp, Double.valueOf(maxxp).intValue(), columns);
    }

    private static String progressPercentage(int doneTop, int totalTop, int doneBot, int totalBot, int size) {
        if (doneTop > totalTop) doneTop = totalTop;
        if (doneBot > totalBot) doneBot = totalBot;
        List<String> hpBar = new ArrayList<>();
        List<String> xpBar = new ArrayList<>();
        // Process Top Bar
        double doneLengthTop = getDoneLength(doneTop, totalTop, size);
        for (int i = 0; i < size; i++) {
            String rh = String.valueOf(i + 1);
            String emoteFull = rh + "m_fhp_exp";
            String emoteHalf = rh + "m_hhp_exp";
            String emoteEmpty = rh + "m_ehp_exp";
            if ((((double) i) + 0.5) == round(doneLengthTop, 1)) hpBar.add(emoteHalf);
            else if (i < doneLengthTop) hpBar.add(emoteFull);
            else hpBar.add(emoteEmpty);
        }
        // Process Bottom Bar
        double doneLengthBot = getDoneLength(doneBot, totalBot, size);
        for (int i = 0; i < size; i++) {
            String rh = String.valueOf(i + 1);
            String hpPart = hpBar.get(i).substring(3, 6);
            String emoteFull = rh + "m_" + hpPart + "_fxp";
            String emoteHalf = rh + "m_" + hpPart + "_hxp";
            String emoteEmpty = rh + "m_" + hpPart + "_exp";
            if ((((double) i) + 0.5) == round(doneLengthBot, 1)) xpBar.add(emoteHalf);
            else if (i < doneLengthBot) xpBar.add(emoteFull);
            else xpBar.add(emoteEmpty);
        }
        StringBuilder bar = new StringBuilder();
        for (String xpBarPart : xpBar) bar.append(EmoteUtil.getEmoteMention(xpBarPart));
        return bar.toString();
    }

    private static double getDoneLength(int done, int total, int size) {
        int donePercents = (100 * done) / total;
        return (double) size * (double) donePercents / 100.0;
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}

