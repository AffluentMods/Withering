package me.affluent.decay.shop;

import me.affluent.decay.util.system.EmoteUtil;

public class ShopItem {

    private final String displayName;
    private final String itemName;
    private final long amount;
    private final Price price;

    public ShopItem(String displayName, String itemName, long amount, Price price) {
        this.displayName = displayName;
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }

    public String getDisplayName(boolean checkForApply) {
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

    public String getItemName(boolean checkForApply) {
        if (checkForApply && itemName.startsWith("")) return displayName.toLowerCase();
        return itemName;
    }

    public long getAmount() {
        return amount;
    }

    public Price getPrice() {
        return price;
    }
}