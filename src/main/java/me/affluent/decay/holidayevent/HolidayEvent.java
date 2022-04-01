package me.affluent.decay.holidayevent;

import me.affluent.decay.Withering;
import me.affluent.decay.listener.EventListener;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.util.InGameTime;
import me.affluent.decay.util.system.CooldownUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class HolidayEvent implements EventListener {

    private static final HashMap<String, HolidayEvent> holidayEvents = new HashMap<>();

    private static HolidayEvent currentEvent = null;
    private static boolean eventActive = false;

    public static HolidayEvent getCurrentEvent() {
        return currentEvent;
    }

    public static boolean isEventActive() {
        return eventActive;
    }

    private final String name;
    private final String description;
    private final String rewardsDisplay;

    private static long end;

    public HolidayEvent(String name, String description, String rewardsDisplay) {
        this.name = name;
        this.description = description;
        this.rewardsDisplay = rewardsDisplay;
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

    public boolean isActive(HolidayEvent theEvent) {
        return eventActive && (theEvent.getName().equals(currentEvent.getName()));
    }

    private static final HashMap<String, Integer> cachePoints = new HashMap<>();
    private static Timer endTimer = null;

    private static void loadActiveEvent() {
        long now = System.currentTimeMillis();
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM holidayevent_data WHERE active=1;")) {
            if (rs.next()) {
                HolidayEvent holidayEvent = HolidayEvent.getHolidayEvent(rs.getString("eventName").toLowerCase());
                try {
                    long end = Long.parseLong(rs.getString("end"));
                    if (now < end) {
                        holidayEvent.setActiveEvent(end);
                        endTimer = new Timer();
                        endTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("[INTERN INFO] Timer Ended Holiday Event " + holidayEvent.getName());
                                holidayEvent.end();
                            }
                        }, (end - now));
                    } else {
                        holidayEvent.end();
                    }
                } catch (NumberFormatException ex) {
                    Withering.getBot().getDatabase().update("SELECT FROM holidayevent_data;");
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
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS holidayevent (userId VARCHAR(24) NOT NULL, stage INT NOT NULL);");
        Withering.getBot().getDatabase().update("DELETE FROM holidayevent;");
        long end = System.currentTimeMillis() + (InGameTime.getHolidayEventEnd() * 60 * 1000);
        System.out.println(end);
        setActiveEvent(end);
        System.out.println("[INTERN INFO] Holiday Event " + getName() + " started");
    }

    private void setActiveEvent(long end) {
        currentEvent = this;
        eventActive = true;
        HolidayEvent.end = end;
        Withering.getBot().getDatabase().update("DELETE FROM holidayevent_data;");
        Withering.getBot().getDatabase()
                .update("INSERT INTO holidayevent_data VALUES (?, ?, 1);", currentEvent.getName().toLowerCase(),
                        String.valueOf(end));
    }

    public void end() {
        if (endTimer != null) {
        System.out.println("[INTERN INFO] Holiday Event " + getName() + " ended");
        eventActive = false;
        endTimer.cancel();
        Withering.getBot().getDatabase()
                .update("UPDATE holidayevent_data SET active=0 WHERE eventName=?;", getName().toLowerCase());
        }
    }

    public abstract int getPlayerStages(String userId);

    public void setData(String userId, int stage) {
        cachePoints.put(userId, stage);
        Withering.getBot().getDatabase().update("DELETE FROM holidayevent WHERE userId=?;", userId);
        Withering.getBot().getDatabase()
                .update("INSERT INTO holidayevent VALUES (?, ?);", userId, stage);
    }

    public int getStage(String userId) {
        if (cachePoints.containsKey(userId)) return cachePoints.get(userId);
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT stage FROM holidayevent WHERE userId=?;", userId)) {
            if (rs.next()) {
                int stage = rs.getInt("stage");
                cachePoints.put(userId, stage);
                return stage;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static List<HolidayEvent> getHolidayEvents() {
        return new ArrayList<>(holidayEvents.values());
    }

    public static HolidayEvent getHolidayEvent(String name) {
        return holidayEvents.get(name.toLowerCase());
    }

    public static void load() {
        List<HolidayEvent> holidayEventList =
                Arrays.asList(new ChristmasHolidayEvent(), new HalloweenHolidayEvent(),
                new NewYearsHolidayEvent());
        for (HolidayEvent holidayEvent : holidayEventList) {
            EventManager.registerListener(holidayEvent);
            holidayEvents.put(holidayEvent.getName().toLowerCase(), holidayEvent);
            System.out.println("[INTERN INFO] Loaded Holiday Event: " + holidayEvent.getName());
        }

        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS holidayevent_data (eventName VARCHAR(32) NOT NULL, end VARCHAR" +
                        "(24) NOT NULL, active INT(2) NOT NULL);");
        loadActiveEvent();
    }
}
