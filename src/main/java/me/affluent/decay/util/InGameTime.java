package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.holidayevent.HolidayEvent;
import me.affluent.decay.specialevent.SpecialEvent;
import me.affluent.decay.util.system.CaravanUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class InGameTime implements Runnable {

    private static int gameYears;
    private static int gameMonths;
    private static int gameDays;
    private static int gameHours;
    private static int gameMinutes;
    private static int daysPerMonth;
    private static String event1;
    private static String event2;

    public static void load() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM gameTime;")) {
            if (rs.next()) {
                gameMinutes = rs.getInt("minute");
                gameHours = rs.getInt("hour");
                gameDays = rs.getInt("day");
                gameMonths = rs.getInt("month");
                gameYears = rs.getInt("year");
            } else {
                gameMinutes = 0;
                gameHours = 0;
                gameDays = 1;
                gameMonths = 1;
                gameYears = 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void setTime() {
        Withering.getBot().getDatabase().update("DELETE FROM gameTime;");
        Withering.getBot().getDatabase().update("INSERT INTO gameTime VALUES (?, ?, ?, ?, ?);", gameMinutes, gameHours, gameDays, gameMonths, gameYears);
    }

    public static void setTime(int minute, int hour, int day, int month, int year) {
        gameMinutes = minute;
        gameHours = hour;
        gameDays = day;
        gameMonths = month;
        gameYears = year;
    }

    public static void updateTime() {
        ScheduledExecutorService scheduler
                = Executors.newSingleThreadScheduledExecutor();

        Runnable task = new InGameTime() {
            @Override
            public void run() {
                gameMinutes += 15;
                gameHours = (int) getCurrentHours();
                gameDays = (int) getCurrentDays();
                gameMonths = (int) getCurrentMonths();
                gameYears = (int) getCurrentYear();
                setTime();
                if (SpecialEvent.isEventActive()) {
                    if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("striker") && gameMonths != 1) {
                        if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("striker") && gameMonths != 2)
                            SpecialEvent.getCurrentEvent().end();
                    }
                    if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("gambler") && gameMonths != 3) {
                        if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("gambler") && gameMonths != 4)
                            SpecialEvent.getCurrentEvent().end();
                    }
                    if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("chest breacher") && gameMonths != 5) {
                        if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("chest breacher") && gameMonths != 6)
                            SpecialEvent.getCurrentEvent().end();
                    }
                    if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("golden hand") && gameMonths != 7) {
                        if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("golden hand") && gameMonths != 8)
                            SpecialEvent.getCurrentEvent().end();
                    }
                    if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("golden gambler") && gameMonths != 9) {
                        if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("golden gambler") && gameMonths != 10)
                            SpecialEvent.getCurrentEvent().end();
                    }
                    if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("dwarven miner") && gameMonths != 11) {
                        if (SpecialEvent.getCurrentEvent().getName().equalsIgnoreCase("dwarven miner") && gameMonths != 12)
                            SpecialEvent.getCurrentEvent().end();
                    }
                }
                if (!SpecialEvent.isEventActive()) {
                    String nextEvent = getNextEventTemp();
                    SpecialEvent specialEvent = SpecialEvent.getSpecialEvent(nextEvent);
                    if (specialEvent != null) specialEvent.start();
                    else System.out.println("[ERROR] Unknown Special Event");
                }
                if (HolidayEvent.isEventActive()) {
                    if (HolidayEvent.getCurrentEvent().getName().equalsIgnoreCase("christmas") && gameMonths != 12)
                        HolidayEvent.getCurrentEvent().end();
                    if (HolidayEvent.getCurrentEvent().getName().equalsIgnoreCase("new years") && gameMonths != 1)
                        HolidayEvent.getCurrentEvent().end();
                    //if (HolidayEvent.getCurrentEvent().getName().equalsIgnoreCase("anniversary") && gameMonths != 9)
                    //HolidayEvent.getCurrentEvent().end();
                    if (HolidayEvent.getCurrentEvent().getName().equalsIgnoreCase("halloween") && gameMonths != 10)
                        HolidayEvent.getCurrentEvent().end();
                }
                if (!HolidayEvent.isEventActive()) {
                    String holidayEventName = "";
                    if (gameMonths == 1) holidayEventName = "new years";
                    //if (gameMonths == 9) holidayEventName = "anniversary";
                    if (gameMonths == 10) holidayEventName = "halloween";
                    if (gameMonths == 12) holidayEventName = "christmas";
                    if (!holidayEventName.equalsIgnoreCase("")) {
                        HolidayEvent holidayEvent = HolidayEvent.getHolidayEvent(holidayEventName);
                        if (holidayEvent != null) holidayEvent.start();
                        else System.out.println("[ERROR] Unknown Holiday Event");
                    }
                }
            }
        };
        int initialDelay = 72;
        int periodicDelay = 72;
        scheduler.scheduleAtFixedRate(task, initialDelay, periodicDelay,
                TimeUnit.SECONDS);
    }

    public String getNextEvent1() {
        switch (gameMonths) {
            case 1:
                event1 = "pet_leveling";
                break;
            case 3:
                event1 = "gambler";
                break;
            case 5:
                event1 = "chest_breacher";
                break;
            case 7:
                event1 = "item_starring";
                break;
            case 9:
                event1 = "gambling";
                break;
            case 11:
                event1 = "dungeon";
                break;
        }
        return event1;
    }

    public String getNextEvent2() {
        switch (gameMonths) {
            case 1:
                event2 = "striker";
                break;
            case 3:
                event2 = "golden_gambler";
                break;
            case 5:
                event2 = "jeweler";
                break;
            case 7:
                event2 = "golden_hand";
                break;
            case 9:
                event2 = "mining_event";
                break;
            case 11:
                event2 = "boss_event";
                break;
        }
        return event2;
    }

    public String getNextEventTemp() {
        switch (gameMonths) {
            case 1:
            case 2:
                event1 = "striker";
                break;
            case 3:
            case 4:
                event1 = "gambler";
                break;
            case 5:
            case 6:
                event1 = "chest breacher";
                break;
            case 7:
            case 8:
                event1 = "golden hand";
                break;
            case 9:
            case 10:
                event1 = "golden gambler";
                break;
            case 11:
            case 12:
                event1 = "dwarven miner";
                break;
        }
        return event1;
    }

    public static long getSpecialEventEnd() {
        long days1;
        long days2;
        if (gameMonths % 2 == 0) {
            days1 = daysPerMonthSpecific(gameMonths);
            days2 = 0;
        } else {
            days1 = daysPerMonthSpecific(gameMonths);
            days2 = daysPerMonthSpecific(gameMonths + 1);
        }
        long totalDays = (days1 + days2);
        return (totalDays * 120L); // real time amount of minutes (4 minutes/hour)
    }

    public static long getHolidayEventEnd() {
        long days = daysPerMonthSpecific(gameMonths);
        return (days * 120L); // real time amount of minutes (4 minutes/hour)
    }

    public long daysPerMonth() {
        switch (gameMonths) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysPerMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysPerMonth = 30;
                break;
            case 2:
                if (((gameYears % 4 == 0) && !((gameYears % 100) == 0)) || (gameYears % 400 == 0))
                    daysPerMonth = 29;
                else
                    daysPerMonth = 28;
        }
        return daysPerMonth;
    }

    public static long daysPerMonthSpecific(int month) {
        int days = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (((gameYears % 4 == 0) && !((gameYears % 100) == 0)) || (gameYears % 400 == 0))
                    days = 29;
                else
                    days = 28;
        }
        return days;
    }

    public static String getCurrentDate() {
        String year;
        year = "Year " + gameYears;
        String month = monthString(gameMonths);
        String daySuffix = daySuffix(gameDays);
        String day = gameDays + daySuffix;
        return day + " of " + month + " " + year;
    }

    public static String getCurrentTime() {
        String ampm = "";
        /*if (gameHours >= 12) {
            ampm = "PM";
        } else {
            ampm = "AM";
        } Currently only military time, add a setting to switch between military and standard time*/
        String minute;
        if (gameMinutes >= 10) {
            minute = String.valueOf(gameMinutes);
        } else {
            minute = "0" + gameMinutes;
        }

        String hour;
        if (gameHours >= 10) {
            hour = String.valueOf(gameHours);
        } else {
            hour = "0" + gameHours;
        }
        return hour + ":" + minute + " " + ampm;
    }

    public static String daySuffix(int gameDays) {
        int ten = gameDays % 10;
        int hundred = gameDays % 100;
        if (hundred - ten == 10) {
            return "th";
        }
        switch (ten) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String monthString(int gameMonths) {
        switch (gameMonths) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return null;
    }

    public long getCurrentHours() {
        while (gameMinutes >= 60) {
            gameMinutes -= 60;
            gameHours++;
            //CaravanUtil.updateRewards();
        }
        return gameHours;
    }

    public long getCurrentDays() {
        while (gameHours >= 24) {
            gameHours -= 24;
            gameDays++;
        }
        return gameDays;
    }

    public long getCurrentMonths() {
        if (gameDays > daysPerMonth()) {
            gameDays = 1;
            gameMonths++;
        }
        return gameMonths;
    }

    public long getCurrentYear() {
        while (gameMonths >= 13) {
            gameMonths -= 12;
            gameYears++;
        }
        return gameYears;
    }

    public static int getCurrentMonth() {
        return gameMonths;
    }
}