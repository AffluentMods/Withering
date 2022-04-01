package me.affluent.decay.shop;

import java.util.HashMap;

public class PriceItems extends Price {

    public PriceItems(String item, long amount) {
        super(item, amount);
    }

    public PriceItems(HashMap<String, Long> items) {
        super(items);
    }

    @Override
    public HashMap<String, Long> getItems() {
        return super.getItems();
    }
}