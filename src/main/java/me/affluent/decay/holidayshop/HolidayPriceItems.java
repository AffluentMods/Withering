package me.affluent.decay.holidayshop;

import java.util.HashMap;

public class HolidayPriceItems extends HolidayPrice {

    public HolidayPriceItems(String item, long amount) {
        super(item, amount);
    }

    public HolidayPriceItems(HashMap<String, Long> items) {
        super(items);
    }

    @Override
    public HashMap<String, Long> getHolidayItems() {
        return super.getHolidayItems();
    }
}
