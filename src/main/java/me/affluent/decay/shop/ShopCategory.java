package me.affluent.decay.shop;

import java.util.List;

public class ShopCategory {

    private final String title;
    private final List<ShopItem> shopItems;

    public ShopCategory(String title, List<ShopItem> shopItems) {
        this.title = title;
        this.shopItems = shopItems;
    }

    public String getTitle() {
        return title;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }
}