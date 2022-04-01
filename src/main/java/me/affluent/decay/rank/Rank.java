package me.affluent.decay.rank;

import me.affluent.decay.enums.ItemType;
import me.affluent.decay.shop.PriceMoney;

import java.util.*;

public abstract class Rank {

    private final int id;
    private final String sirName;
    private final String madamName;
    private final String neutralName;
    private final int requiredLevel;
    private final int maxLevel;
    private final int health;
    private static final HashMap<Integer, Rank> ranks = new HashMap<>();

    Rank(int id, String sir_name, String madam_name, String neutral_name, int levelRequired, int maxLevel, int health) {
        this.id = id;
        this.sirName = sir_name;
        this.madamName = madam_name;
        this.neutralName = neutral_name;
        this.requiredLevel = levelRequired;
        this.maxLevel = maxLevel;
        this.health = health;
    }

    public static HashMap<Integer, Rank> getRanks() {
        return ranks;
    }

    public static void loadRanks() {
        List<Rank> ranks1 =
                Arrays.asList(new SquireEsquire(), new KnightDame(), new BaronBaroness(), new EarlCountess(),
                        new DukeDuchess(), new PrincePrincess(), new KingQueen(), new EmperorEmpress());
        for (Rank rank : ranks1) {
            ranks.put(rank.getRequiredLevel(), rank);
        }
    }
    public int getId() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public String getmadamName() {
        return madamName;
    }

    public String getsirName() {
        return sirName;
    }

    public String getneutralName() {
        return neutralName;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static Rank getRank(String name) {
        for (Rank r : ranks.values()) {
            if (r.getsirName().equalsIgnoreCase(name)) return r;
            if (r.getmadamName().equalsIgnoreCase(name)) return r;
            if (r.getneutralName().equalsIgnoreCase(name)) return r;
        }
        return null;
    }

    public static Rank getRank(int level) {
        Rank rank = null;
        int lastLQ = 0;
        for (int lq : ranks.keySet()) {
            Rank r = ranks.get(lq);
            if (r.getRequiredLevel() <= level && r.getRequiredLevel() > lastLQ) {
                rank = r;
                lastLQ = lq;
            }
        }
        return rank;
    }

    public static ItemType getHighestMaterial(int level) {
        List<ItemType> itemTypes = new ArrayList<>(Arrays.asList(ItemType.values()));
        ItemType highest = ItemType.WOOD;
        TreeMap<Integer, ItemType> its = new TreeMap<>();
        for (ItemType it : itemTypes) its.put(it.getLevelRequirement(), it);
        for (int reqLevel : its.descendingKeySet()) {
            if (reqLevel <= level) {
                highest = its.get(reqLevel);
                break;
            }
        }
        return highest;
    }
}