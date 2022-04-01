package me.affluent.decay.util.system;

import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static final List<String> possibleTypes =
            Arrays.asList("helmet", "chestplate", "gloves", "trousers", "boots", "sword", "bow", "arrow", "shield");
    private static final int perc = possibleTypes.size();

    public static ItemType getRandomItemType(HashMap<ItemType, Integer> possibleChances) {
        ItemType it = null;
        int perc = 1;
        for (int i : possibleChances.values()) perc += i;
        for (ItemType itemType : possibleChances.keySet()) {
            int rnd = new Random().nextInt(perc);
            int chance = possibleChances.get(itemType);
            if (rnd <= chance) {
                it = itemType;
                break;
            }
            perc -= chance;
        }
        return it;
    }

    public static ItemType getRandomItemType(ItemType... possibles) {
        int perc = possibles.length;
        return possibles[new Random().nextInt(perc)];
    }

    public static String getRandomArmor(ItemType armorSet) {
        int pick = new Random().nextInt(perc);
        return armorSet.name().toLowerCase().replaceAll("_", " ") + " " + possibleTypes.get(pick);
    }

    public static Rarity getRandomRarity() {
        HashMap<Rarity, Integer> possibleChances = new HashMap<>();
        possibleChances.put(Rarity.JUNK, 39200);
        /*possibleChances.put(Rarity.COMMON, 29000);
        possibleChances.put(Rarity.UNCOMMON, 20000);
        possibleChances.put(Rarity.RARE, 8000);
        possibleChances.put(Rarity.EPIC, 3000);
        possibleChances.put(Rarity.LEGEND, 650);
        possibleChances.put(Rarity.MYTHIC, 149);
        possibleChances.put(Rarity.ANCIENT, 1);*/
        Rarity it = null;
        int perc = 1;
        for (int i : possibleChances.values()) perc += i;
        for (Rarity itemType : possibleChances.keySet()) {
            int rnd = new Random().nextInt(perc);
            int chance = possibleChances.get(itemType);
            if (rnd <= chance) {
                it = itemType;
                break;
            }
            perc -= chance;
        }
        return it;
    }
}