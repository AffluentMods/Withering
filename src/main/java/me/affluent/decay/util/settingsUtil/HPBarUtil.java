package me.affluent.decay.util.settingsUtil;

import me.affluent.decay.util.system.EmoteUtil;

import java.util.ArrayList;
import java.util.List;

public class HPBarUtil {

    // topRow = true -> top row
    // topRow = false -> bottom row
    public static String getBar(int hp, int maxhp, int xp, double maxxp, boolean elixir) {
        int columns = 7;
        String topBar = progressPercentage(hp, maxhp, -1, -1, columns, elixir);
        String bottomBar = progressPercentage(hp, maxhp, xp, Double.valueOf(maxxp).intValue(), columns, elixir);
        return topBar + "\n" + bottomBar;
    }

    private static String progressPercentage(int doneTop, int totalTop, int doneBot, int totalBot, int size, boolean elixir) {
        boolean topRow = doneBot == -1 && totalBot == -1;
        if (doneTop > totalTop) doneTop = totalTop;
        if (doneBot > totalBot) doneBot = totalBot;
        List<String> hpBar = new ArrayList<>();
        List<String> xpBar = new ArrayList<>();
        // Process Top Bar
        double doneLengthTop = getDoneLength(doneTop, totalTop, size);
        for (int i = 0; i < size; i++) {
            String rh = String.valueOf(i + 2);
            String emoteFull = rh + "a_fhp_exp";
            String emoteHalf = rh + "a_hhp_exp";
            String emoteEmpty = rh + "a_ehp_exp";
            if ((((double) i) + 0.5) == round(doneLengthTop, 1)) hpBar.add(emoteHalf);
            else if (i < doneLengthTop) hpBar.add(emoteFull);
            else hpBar.add(emoteEmpty);
        }
        if (topRow) {
            StringBuilder bar = new StringBuilder(EmoteUtil.getEmoteMention("1a_ehp_exp"));
            for (String hpBarPart : hpBar) bar.append(EmoteUtil.getEmoteMention(hpBarPart));
            return bar.toString();
        }
        // Process Bottom Bar
        double doneLengthBot = getDoneLength(doneBot, totalBot, size);
        for (int i = 0; i < size; i++) {
            String rh = String.valueOf(i + 2);
            String hpPart = hpBar.get(i).substring(3, 6);
            String emoteFull = rh + (elixir ? ("b_" + hpPart + "_fexp") : ("b_" + hpPart + "_fxp"));
            String emoteHalf = rh + (elixir ? ("b_" + hpPart + "_hexp") : ("b_" + hpPart + "_hxp"));
            String emoteEmpty = rh + (elixir ? ("b_" + hpPart + "_eexp") : ("b_" + hpPart + "_exp"));
            if ((((double) i) + 0.5) == round(doneLengthBot, 1)) xpBar.add(emoteHalf);
            else if (i < doneLengthBot) xpBar.add(emoteFull);
            else xpBar.add(emoteEmpty);
        }
        StringBuilder bar = new StringBuilder(EmoteUtil.getEmoteMention("1b_ehp_exp"));
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
