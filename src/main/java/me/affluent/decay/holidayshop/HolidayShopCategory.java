package me.affluent.decay.holidayshop;

import me.affluent.decay.shop.ShopItem;

import java.util.List;

public class HolidayShopCategory {

    private final String title;
    private final List<HolidayShopItem> shopItems;

    public HolidayShopCategory(String title, List<HolidayShopItem> shopItems) {
        this.title = title;
        this.shopItems = shopItems;
    }

    public String getHolidayTitle() {
        return title;
    }

    public List<HolidayShopItem> getHolidayShopItems() {
        return shopItems;
    }
}
