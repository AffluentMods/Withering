package me.affluent.decay.holidayshop;

import me.affluent.decay.shop.Price;

import java.util.HashMap;

public class HolidayPrice {

    private final long goldcoins;
    private final HashMap<String, Long> items = new HashMap<>();
    private final HolidayPriceType priceType;

    public HolidayPrice(long goldcoins) {
        this.goldcoins = goldcoins;
        priceType = HolidayPriceType.MONEY;
    }

    public HolidayPrice(String item, long amount) {
        this.goldcoins = -1;
        items.put(item, amount);
        priceType = HolidayPriceType.ITEMS;
    }

    public HolidayPrice(HashMap<String, Long> items) {
        this.goldcoins = -1;
        this.items.putAll(items);
        priceType = HolidayPriceType.ITEMS;
    }

    public HolidayPriceType getHolidayPriceType() {
        return priceType;
    }

    public long getHolidayGoldCoins() {
        return goldcoins;
    }

    public HashMap<String, Long> getHolidayItems() {
        return items;
    }

    public HashMap<String, Long> getHolidayItems(int multiplyBy) {
        HashMap<String, Long> items = new HashMap<>(this.items);
        HashMap<String, Long> multipliedItems = new HashMap<>();
        for (String item : items.keySet()) {
            long amount = items.get(item);
            multipliedItems.put(item, amount * multiplyBy);
        }
        return multipliedItems;
    }

    public enum HolidayPriceType {
        MONEY,
        ITEMS
    }
}
