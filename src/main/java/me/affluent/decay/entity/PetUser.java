package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.pets.PetExpUser;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.util.PetStarringUtil;
import me.affluent.decay.util.PetUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PetUser {

    public static HashMap<String, PetUser> cache = new HashMap<>();

    private final String userId;
    private long petID;
    private PetItem pet;

    public static void clearCache() {
        cache.clear();
    }

    private PetUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM equippedPet WHERE userId=?", userId)) {
            if (rs.next()) {
                this.petID = rs.getInt("petID");
                this.pet = PetItem.getPetItem(rs.getString("pet"));
            } else {
                this.pet = null;
                this.petID = -1;

                Withering.getBot().getDatabase()
                        .update("INSERT INTO equippedPet VALUES (?, ?, ?);", userId, "", -1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getPetID() {
        return String.valueOf(petID);
    }

    public PetItem getPet() {
        return pet;
    }

    public int getFullPetDamage() {
        int damage = 0;
        if (pet != null) {
            String id = getPetID();
            int stars = PetStarringUtil.getPetStar(userId, petID);
            int level = PetExpUser.getPetExpUser(userId).getPetLevel(userId, Math.toIntExact(petID));
            double petLevelDMG = pet.getLevelDMG();
            int petStartingDMG = pet.getStarterDMG();
            damage += (petLevelDMG * level);
            damage += petStartingDMG;
            if (stars > 0) {
                damage *= ((double) (stars * 5) / 100);
            }
            if (level == pet.getMaxLevel()) {
                damage *= 1.15;
            }
        }
        return damage;
    }

    public int getFullPetHealth() {
        int health = 0;
        if (pet != null) {
            String id = getPetID();
            int stars = PetStarringUtil.getPetStar(userId, petID);
            int level = PetExpUser.getPetExpUser(userId).getPetLevel(userId, Math.toIntExact(petID));
            double petLevelHP = pet.getLevelHP();
            int petStartingHP = pet.getStarterHP();
            health += (petLevelHP * level);
            health += petStartingHP;
            if (stars > 0) {
                health *= ((double) (stars * 5) / 100);
                }
            if (level == pet.getMaxLevel()) {
                health *= 1.15;
            }
        }
        return health;
    }

    public void updatePet(Long ID, PetItem pets) {
        this.pet = pets;
        this.petID = ID;
        String pet = pets == null ? "" : pets.getName();
        Withering.getBot().getDatabase().update("UPDATE equippedPet SET pet=?, petID=? WHERE userId=?;", pet, ID, userId);
        cache();
    }

    public String getUserId() {
        return userId;
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static PetUser getPetUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new PetUser(userId);
    }
}
