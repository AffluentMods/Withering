package me.affluent.decay.chest;

import me.affluent.decay.chest.rewards.*;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.util.system.EmoteUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.util.system.RandomUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Chest {

    private static final HashMap<ItemType, Chest> chests = new HashMap<>();
    private HashMap<ItemType, Integer> rewards = new HashMap<>();
    private int medallion;
    private ItemType itemType;
    private final String name;
    private int levelReq;

    public String getName() {
        return name;
    }

    public Chest(ItemType itemType, String name, int levelReq) {
        this.name = name;
        this.itemType = itemType;
        this.levelReq = levelReq;
    }

    public static Chest getChest(ItemType itemType) {
        return chests.get(itemType);
    }

    public static void load() {
        List<Chest> allChests = Arrays.asList(new ChestRewardWood(), new ChestRewardMetal(), new ChestRewardTitanium(),
                new ChestRewardSteel(), new ChestRewardDragonSteel(), new ChestRewardTitanAlloy());
        for (Chest chest : allChests) {
            chests.put(chest.getItemType(), chest);
        }
    }

    public String getEmote() {
        return EmoteUtil.getEmoteMention(getName());
    }

    public int getLevelReq() {
        return levelReq;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setReward(ItemType item, int chanceOf100) {
        rewards.put(item, chanceOf100);
    }

    public void setMedallion(int from, int to) {
        medallion = FormatUtil.getBetween(from, to);
    }

    public HashMap<String, Integer> getRewards() {
            HashMap<String, Integer> rewards = new HashMap<>();
            String random1 = RandomUtil.getRandomArmor(RandomUtil.getRandomItemType(this.rewards));
            String random2 = RandomUtil.getRandomArmor(RandomUtil.getRandomItemType(this.rewards));
            rewards.put(random1, 1);
            rewards.put(random2, rewards.getOrDefault(random2, 0) + 1);
            boolean godlyOccurence = new Random().nextInt(100) <= 1;
            if  (godlyOccurence) {
                String random3 = RandomUtil.getRandomArmor(RandomUtil.getRandomItemType(this.rewards));
                String random4 = RandomUtil.getRandomArmor(RandomUtil.getRandomItemType(this.rewards));
                rewards.put(random3, rewards.getOrDefault(random3, 0) + 1);
                rewards.put(random4, rewards.getOrDefault(random4, 0) + 1);
            }
            rewards = updateRarityRewards(rewards);
            return rewards;
        }

    private HashMap<String, Integer> updateRarityRewards(HashMap<String, Integer> rewards) {
        HashMap<String, Integer> newRewards = new HashMap<>();
        for (String in : rewards.keySet()) {
            int ia = rewards.get(in);
            String randomRarity = RandomUtil.getRandomRarity().name().toLowerCase();
            newRewards.put(randomRarity + " " + in, ia);
        }
        return newRewards;
    }

    public int getMedallion() {
        return medallion;
    }

}