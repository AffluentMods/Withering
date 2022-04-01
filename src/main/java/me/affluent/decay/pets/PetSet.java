package me.affluent.decay.pets;

import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.enums.Rarity;

import java.util.Arrays;
import java.util.List;

public class PetSet {

    private final String baseName;
    private PetItem petItem;

    public PetSet(String baseName, int starterHP, int starterDMG, double perLevelHP, double perLevelDMG, int maxLevel) {
        String b = baseName.substring(0, 1).toUpperCase() + baseName.substring(1);
        this.baseName = b;
        List<PetRarity> rarities = Arrays.asList(PetRarity.ARTIFACT, PetRarity.ANCIENT, PetRarity.MYTHIC, PetRarity.LEGEND, PetRarity.EPIC, PetRarity.RARE,
                PetRarity.UNCOMMON, PetRarity.COMMON, PetRarity.JUNK);
        if (starterHP != -1 && perLevelHP != -1 && starterDMG != -1 && perLevelDMG != -1 && maxLevel != -1) {
            for (PetRarity rarity : rarities) {
                String a = rarity.name().toLowerCase();
                a = a.substring(0, 1).toUpperCase() + a.substring(1);
                this.petItem = new PetItem(b, starterHP, starterDMG, perLevelHP, perLevelDMG, maxLevel, rarity);
            }
        } else {
            this.petItem = null;
        }
    }
    
    public String getBaseName() {
        return baseName;
    }
    
    public PetItem getPetItem() {
        return petItem;
    }
}
