package me.affluent.decay.holidayevent;

import me.affluent.decay.Constants;
import me.affluent.decay.entity.Player;
import me.affluent.decay.event.HolidayWinEvent;
import me.affluent.decay.util.system.EmoteUtil;

public class HalloweenHolidayEvent extends HolidayEvent {

    public HalloweenHolidayEvent() {
        super("Halloween", "Boo! The spirits are wondering about. Catch these spirits for a treat.\n" +
                "Candy can be spent in the `" + Constants.PREFIX + "holiday store` on unique items and rewards.",
                "• Stage 1 - 50;\n" +
                Constants.TAB + "- `1` " + EmoteUtil.getEmoteMention("Holiday_Candy_Corn") + "\n" +
                "• Stage 51 - 150;\n" +
                Constants.TAB + "- `2` " + EmoteUtil.getEmoteMention("Holiday_Candy_Corn") + "\n" +
                "• Stage 151 - 300;\n" +
                Constants.TAB + "- `3` " + EmoteUtil.getEmoteMention("Holiday_Candy_Corn") + "\n" +
                "• Stage 301+;\n" +
                Constants.TAB + "- `5` " + EmoteUtil.getEmoteMention("Holiday_Candy_Corn") + "\n" +
                "Rare chance of Purple Candy " + EmoteUtil.getEmoteMention("Holiday_Purple_Candy") + " dropping");
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
