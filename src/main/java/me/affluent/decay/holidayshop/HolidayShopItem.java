package me.affluent.decay.holidayshop;

import me.affluent.decay.shop.Price;
import me.affluent.decay.util.system.EmoteUtil;

public class HolidayShopItem {

    private final String displayName;
    private final String itemName;
    private final long amount;
    private final HolidayPrice price;

    public HolidayShopItem(String displayName, String itemName, long amount, HolidayPrice price) {
        this.displayName = displayName;
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }

    public String getHolidayDisplayName(boolean checkForApply) {
        String toDisplay = displayName;
        if (checkForApply) {
            if (itemName.startsWith("")) {
                if (itemName.startsWith("heal|")) {
                    toDisplay =
                            displayName + " (" + itemName.split("\\|")[1] + " " + EmoteUtil.getEmoteMention("HP") +
                                    ")";
                }
            }
        }
        return toDisplay;
    }

    public String getHolidayItemName(boolean checkForApply) {
        if (checkForApply && itemName.startsWith("")) return displayName.toLowerCase();
        return itemName;
    }

    public long getHolidayAmount() {
        return amount;
    }

    public HolidayPrice getHolidayPrice() {
        return price;
    }

}
