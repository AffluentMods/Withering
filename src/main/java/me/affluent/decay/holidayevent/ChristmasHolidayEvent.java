package me.affluent.decay.holidayevent;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.event.Event;
import me.affluent.decay.event.HolidayWinEvent;
import me.affluent.decay.util.system.EmoteUtil;

import java.sql.ResultSet;

public class ChristmasHolidayEvent extends HolidayEvent {

    public ChristmasHolidayEvent() {
        super("Christmas", "The Grinch is trying to steal Christmas! Deal with him quickly to obtain your much deserved presents.\n" +
                "Open presents to obtain some loot! Spend your Candy Canes in the `" + Constants.PREFIX + "holiday store`", "• Stage 1 - 50;\n" +
                Constants.TAB + "- `1` " + EmoteUtil.getEmoteMention("Holiday_Candy_Cane") + "\n" +
                "• Stage 51 - 150;\n" +
                Constants.TAB + "- `2` " + EmoteUtil.getEmoteMention("Holiday_Candy_Cane") + "\n" +
                "• Stage 151 - 300;\n" +
                Constants.TAB + "- `3` " + EmoteUtil.getEmoteMention("Holiday_Candy_Cane") + "\n" +
                "• Stage 301+;\n" +
                Constants.TAB + "- `5` " + EmoteUtil.getEmoteMention("Holiday_Candy_Cane"));
    }

    @Override
    public void onHolidayWinEvent(HolidayWinEvent e) {
        if (!isActive(this)) return;
        Player w = e.getWinner();
        String id = w.getUserId();
        setData(id, e.getStageNumber());
    }

    @Override
    public int getPlayerStages(String userId) {
        return getStage(userId);
    }
}
