package me.affluent.decay.holidayevent;

import me.affluent.decay.util.system.EmoteUtil;

public class NewYearsHolidayEvent extends HolidayEvent {

    public NewYearsHolidayEvent() {
        super("New Years", "The new year begins, this calls for a celebration!\n" +
                "During New Years, while voting you will be obtaining fireworks that shall be used for the celebrations!",
                EmoteUtil.getEmoteMention("Holiday_Firework") + " Fireworks can be launched whenever you want in order to gain double experience and coins for 20 minutes.");
    }

    @Override
    public int getPlayerStages(String userId) {
        return 0;
    }
}
