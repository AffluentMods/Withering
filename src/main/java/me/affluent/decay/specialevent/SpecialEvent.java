package me.affluent.decay.specialevent;

import me.affluent.decay.Withering;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.listener.EventListener;
import me.affluent.decay.util.InGameTime;
import me.affluent.decay.util.system.CooldownUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class SpecialEvent implements EventListener {

    private static final HashMap<String, SpecialEvent> specialEvents = new HashMap<>();

    private static SpecialEvent currentEvent = null;
    private static boolean eventActive = false;

    public static SpecialEvent getCurrentEvent() {
        return currentEvent;
    }

    public static boolean isEventActive() {
        return eventActive;
    }

    private final String name;
    private final String description;
    private final String rewardsDisplay;
    private final String shortDescription;

    private static long end;

    public SpecialEvent(String name, String description, String shortDescription, String rewardsDisplay) {
        this.name = name;
        this.description = description;
        this.rewardsDisplay = rewardsDisplay;
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRewardsDisplay() {
        return rewardsDisplay;
    }

    public boolean isActive(SpecialEvent theEvent) {
        return eventActive && (theEvent.getName().equals(currentEvent.getName()));
    }

    private static final HashMap<String, Integer> cachePoints = new HashMap<>();
    private static final HashMap<String, Integer> cacheCurrentValue = new HashMap<>();
    private static Timer endTimer = null;

    private static void loadActiveEvent() {
        long now = System.currentTimeMillis();
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM specialevent_data WHERE active=1;")) {
            if (rs.next()) {
                SpecialEvent specialEvent = SpecialEvent.getSpecialEvent(rs.getString("eventName").toLowerCase());
                try {
                    long end = Long.parseLong(rs.getString("end"));
                    if (now < end) {
                        specialEvent.setActiveEvent(end);
                        endTimer = new Timer();
                        endTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("[INTERN INFO] Timer ended Special Event " + specialEvent.getName());
                                specialEvent.end();
                            }
                        }, (end - now));
                    } else {
                        specialEvent.end();
                    }
                } catch (NumberFormatException ex) {
                    Withering.getBot().getDatabase().update("DELETE FROM specialevent_data;");
                    loadActiveEvent();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String getTimeLeftDisplay(String uid) {
        long now = System.currentTimeMillis();
        long diff = end - now;
        return CooldownUtil.format(diff, uid);
    }

    public void start() {
        cachePoints.clear();
        cacheCurrentValue.clear();
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS specialevent (userId VARCHAR(24) NOT NULL, currentValue INT NOT " +
                        "NULL, points INT NOT NULL);");
        Withering.getBot().getDatabase().update("DELETE FROM specialevent;");
        long end = System.currentTimeMillis() + (InGameTime.getSpecialEventEnd() * 60 * 1000);
        setActiveEvent(end);
        System.out.println("[INTERN INFO] Event " + getName() + " started");
    }

    private void setActiveEvent(long end) {
        currentEvent = this;
        eventActive = true;
        SpecialEvent.end = end;
        Withering.getBot().getDatabase().update("DELETE FROM specialevent_data;");
        Withering.getBot().getDatabase()
                .update("INSERT INTO specialevent_data VALUES (?, ?, 1);", currentEvent.getName().toLowerCase(),
                        String.valueOf(end));
    }

    public void end() {
        System.out.println("[INTERN INFO] Event " + getName() + " ended");
        eventActive = false;
        endTimer.cancel();
        Withering.getBot().getDatabase()
                .update("UPDATE specialevent_data SET active=0 WHERE eventName=?;", getName().toLowerCase());
        giveRewards();
    }

    public abstract void giveRewards();

    public abstract int getPlayerPoint(String userId);

    public void setData(String userId, int points, int currentValue) {
        cachePoints.put(userId, points);
        cacheCurrentValue.put(userId, currentValue);
        Withering.getBot().getDatabase().update("DELETE FROM specialevent WHERE userId=?;", userId);
        Withering.getBot().getDatabase()
                .update("INSERT INTO specialevent VALUES (?, ?, ?);", userId, currentValue, points);
    }

    public int getPoints(String userId) {
        if (cachePoints.containsKey(userId)) return cachePoints.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT points, currentValue FROM specialevent WHERE userId=?;", userId)) {
            if (rs.next()) {
                int currentValue = rs.getInt("currentValue");
                int points = rs.getInt("points");
                cachePoints.put(userId, points);
                cacheCurrentValue.put(userId, currentValue);
                return points;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static List<SpecialEvent> getSpecialEvents() {
        return new ArrayList<>(specialEvents.values());
    }

    public static SpecialEvent getSpecialEvent(String name) {
        return specialEvents.get(name.toLowerCase());
    }

    public static void load() {
        List<SpecialEvent> specialEventList =
                Arrays.asList(new StrikerSpecialEvent(), new ChestBreacherSpecialEvent(), new DwarvenMinerSpecialEvent(),
                        new GamblerSpecialEvent(), new GoldenGamblerSpecialEvent(), new GoldenHandSpecialEvent());
        for (SpecialEvent specialEvent : specialEventList) {
            EventManager.registerListener(specialEvent);
            specialEvents.put(specialEvent.getName().toLowerCase(), specialEvent);
            System.out.println("[INTERN INFO] Loaded event: " + specialEvent.getName());
        }
        //
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS specialevent_data (eventName VARCHAR(32) NOT NULL, end VARCHAR" +
                        "(24) NOT NULL, active INT(2) NOT NULL);");
        loadActiveEvent();
    }
}