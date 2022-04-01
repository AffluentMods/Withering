package me.affluent.decay.rarity;

import me.affluent.decay.enums.Rarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Rarities {

    private static final HashMap<Rarity, RarityClass> rarities = new HashMap<>();

    public static void load() {
        List<RarityClass> toLoad =
                Arrays.asList(new JunkRarity(), new CommonRarity(), new UncommonRarity(), new RareRarity(), new EpicRarity(),
                        new LegendRarity(), new MythicRarity(), new AncientRarity(), new ArtifactRarity());
        for (RarityClass rarityClass : toLoad) {
            rarities.put(rarityClass.getRarity(), rarityClass);
        }
    }

    public static RarityClass getRarityClass(Rarity rarity) {
        return rarities.get(rarity);
    }
}