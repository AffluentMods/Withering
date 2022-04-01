package me.affluent.decay.util.system;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class FormatUtil {

    public static int round(double toRound) {
        return new BigDecimal(String.valueOf(toRound)).round(new MathContext(1)).intValue();
    }

    public static int getMiddle(int a, int b) {
        return (a + b) / 2;
    }

    public static int getBetween(int a, int b) {
        if (a == b) return a;
        return a + ((new Random().nextInt(b - a + 1)));
    }

    public static String formatAbbreviated(long amount) {
        return formatAbbreviated(String.valueOf(amount));
    }

    public static String formatAbbreviated(String amount) {
        String amountStr = String.valueOf(amount);
        String amountString = String.valueOf(amount);
        if (amountStr.length() >= 67) {
            int sub = amountStr.length() - 66;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "c";
        } else if (amountStr.length() >= 64) {
            int sub = amountStr.length() - 63;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "v";
        } else if (amountStr.length() >= 61) {
            int sub = amountStr.length() - 60;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "N";
        } else if (amountStr.length() >= 58) {
            int sub = amountStr.length() - 57;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "O";
        } else if (amountStr.length() >= 55) {
            int sub = amountStr.length() - 54;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "St";
        } else if (amountStr.length() >= 52) {
            int sub = amountStr.length() - 51;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "Sd";
        } else if (amountStr.length() >= 49) {
            int sub = amountStr.length() - 48;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "Qd";
        } else if (amountStr.length() >= 46) {
            int sub = amountStr.length() - 45;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "Qt";
        } else if (amountStr.length() >= 43) {
            int sub = amountStr.length() - 42;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "T";
        } else if (amountStr.length() >= 40) {
            int sub = amountStr.length() - 39;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "D";
        } else if (amountStr.length() >= 37) {
            int sub = amountStr.length() - 36;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "U";
        } else if (amountStr.length() >= 34) {
            int sub = amountStr.length() - 33;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "d";
        } else if (amountStr.length() >= 31) {
            int sub = amountStr.length() - 30;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "n";
        } else if (amountStr.length() >= 28) {
            int sub = amountStr.length() - 27;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "o";
        } else if (amountStr.length() >= 25) {
            int sub = amountStr.length() - 24;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "S";
        } else if (amountStr.length() >= 22) {
            int sub = amountStr.length() - 21;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "s";
        } else if (amountStr.length() >= 19) {
            int sub = amountStr.length() - 18;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "Q";
        } else if (amountStr.length() >= 16) {
            int sub = amountStr.length() - 15;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "q";
        } else if (amountStr.length() >= 13) {
            int sub = amountStr.length() - 12;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "t";
        } else if (amountStr.length() >= 10) {
            int sub = amountStr.length() - 9;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "B";
        } else if (amountStr.length() >= 7) {
            int sub = amountStr.length() - 6;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "M";
        } else if (amountStr.length() >= 4) {
            int sub = amountStr.length() - 3;
            amountString = amountStr.substring(0, sub) + "." + amountStr.substring(sub).substring(0, 1) + "K";
        }
        return amountString;
    }

    public static String formatCommas(String numberString) {
        if (numberString.contains(".")) numberString = numberString.substring(0, numberString.indexOf("."));
        return formatCommas(Long.parseLong(numberString));
    }

    public static String formatCommas(long number) {
        return new DecimalFormat("#,###").format(number);
    }

    public static Date fromTime(long millis) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.setTime(new Date(millis));
        return c.getTime();
    }

    public static Date fromString(String dateString) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        try {
            return format.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Date fromStringNoTime(String dateString) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return format.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static Date getBefore12Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 12);
        return c.getTime();
    }

    static Date getBefore24Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
        return c.getTime();
    }

    public static Date get15Minutes() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.add(Calendar.MINUTE, 15);
        return c.getTime();
    }

    public static Date get24Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date get48Hours() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));
        c.add(Calendar.DAY_OF_YEAR, 2);
        return c.getTime();
    }

    public static Date getNow() {
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin"))).getTime();
    }

    public static String fromDate(Date date) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        return format.format(date);
    }

    public static String fromDateNoTime(Date date) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(date);
    }

    public static String getRomicNumber(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
        return "";
    }

    public static String formatItemListOneLine(HashMap<String, Long> items, boolean ironman, boolean emotes) {
        StringBuilder string = new StringBuilder();
        for (String item : items.keySet()) {
            long amount = items.get(item);
            if (ironman) amount *= 1.35;
            if (emotes) string.append(me.affluent.decay.util.system.EmoteUtil.getEmoteMention(item)).append(" `x").append(FormatUtil.formatCommas(amount)).append("`, ");
        }
        if (string.toString().endsWith(", ")) string = new StringBuilder(string.substring(0, string.length() - 2));
        return string.toString();
    }
}