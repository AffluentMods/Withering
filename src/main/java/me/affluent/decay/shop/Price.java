package me.affluent.decay.shop;

import java.util.HashMap;

public class Price {

    private final long goldcoins;
    private final HashMap<String, Long> items = new HashMap<>();
    private final PriceType priceType;

    public Price(long goldcoins) {
        this.goldcoins = goldcoins;
        priceType = PriceType.MONEY;
    }

    public Price(String item, long amount) {
        this.goldcoins = -1;
        items.put(item, amount);
        priceType = PriceType.ITEMS;
    }

    public Price(HashMap<String, Long> items) {
        this.goldcoins = -1;
        this.items.putAll(items);
        priceType = PriceType.ITEMS;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public long getGoldCoins() {
        return goldcoins;
    }

    public HashMap<String, Long> getItems() {
        return items;
    }

    public HashMap<String, Long> getItems(int multiplyBy) {
        HashMap<String, Long> items = new HashMap<>(this.items);
        HashMap<String, Long> multipliedItems = new HashMap<>();
        for (String item : items.keySet()) {
            long amount = items.get(item);
            multipliedItems.put(item, amount * multiplyBy);
        }
        return multipliedItems;
    }

    public enum PriceType {
        MONEY,
        ITEMS
    }
}