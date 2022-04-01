package me.affluent.decay.api;

import me.affluent.decay.pets.PetItem;

public class PetXPApi {

    public static boolean canPetLevelUp(PetItem pet, double petXp, int petLevel) {
        if (petLevel >= pet.getMaxLevel()) return false;
        return petXp >= getNeededPetXP(pet, petLevel);
    }

    public static double getNeededPetXP(PetItem pet, int petLevel) {
        double neededPetXP = 0;
        if (petLevel >= pet.getMaxLevel()) return -1;
        for (int i = 1; i < petLevel + 1; i++) {
            neededPetXP += (i * 225);
        }
        return neededPetXP;
    }
}
