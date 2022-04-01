package me.affluent.decay.gems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Gem {

    private static final HashMap<String, Gem> gems = new HashMap<>();

    private final String name;
    private final double v1;
    private final double v2;
    private final double v3;
    private final double v4;
    private final double v5;

    public Gem(String name, double val1, double val2, double val3, double val4, double val5) {
        this.name = name;
        this.v1 = val1;
        this.v2 = val2;
        this.v3 = val3;
        this.v4 = val4;
        this.v5 = val5;
        gems.put(this.name.replaceAll(" ", "_").toLowerCase(), this);
    }

    public String getName() {
        return name;
    }

    public double getV1() {
        return v1;
    }

    public double getV2() {
        return v2;
    }

    public double getV3() {
        return v3;
    }

    public double getV4() {
        return v4;
    }

    public double getV5() {
        return v5;
    }

    public static Gem getGem(String name) {
        return gems.get(name.replaceAll(" ", "_").toLowerCase());
    }

    public static List<Gem> getGems() {
        return new ArrayList<>(gems.values());
    }
}