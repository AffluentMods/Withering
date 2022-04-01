package me.affluent.decay.util;

import me.affluent.decay.entity.Player;

public class ShopUtil {

    public static boolean applyItem(Player p, String itemData, int amount) {
        String[] splitItem = itemData.split("\\|").clone();
        String itemName = splitItem[0];
        String itemValue = splitItem[1];
        if (splitItem.length > 2) {
            String itemMeta = splitItem[2];
            String itemValueDisplay = splitItem[3];
        }
        return false;
    }
}