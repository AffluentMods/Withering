package me.affluent.decay.api;

public class XPApi {

    public static boolean canLevelUp(double xp, int level) {
        if (level >= 150) return false;
        return xp >= getNeededXP(level);
    }

    /* Usual EXP Formula */
    public static double getNeededXP(int level) {
        double neededXP = 0;
        if (level >= 150) return -1;
        for (int i = 1; i < level + 1; i++) {
            neededXP += (i * 115);
        }
        return neededXP;
    }

    public static boolean canLevelUpElixir(double elxp, int el) {
        if (el >= 999) return false;
        return elxp >= getNeededElixirXP(el);
    }

    /* Elixir EXP Formula */
    public static double getNeededElixirXP(int el) {
        double neededXP = 135;
        if (el >= 999) return -1;
        for (int i = 1; i < el + 1; i++) {
            if (el < 300) neededXP += (i * 7);
            if (el >= 300 && el < 600) neededXP += (i * 6);
            if (el >= 600) neededXP += (i * 5);
        }
        return neededXP;
    }
}