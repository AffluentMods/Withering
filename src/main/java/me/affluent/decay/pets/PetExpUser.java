package me.affluent.decay.pets;

import me.affluent.decay.Withering;
import me.affluent.decay.api.PetXPApi;
import me.affluent.decay.api.XPApi;
import me.affluent.decay.entity.ExpUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.event.PetLevelUpEvent;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.util.system.StatsUtil;
import net.dv8tion.jda.api.entities.TextChannel;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PetExpUser {

    private static final HashMap<String, PetExpUser> cache = new HashMap<>();

    private final String userId;
    private int petLevel;
    int maxLevel;
    private int petID;
    private int petExp;

    public static void clearCache() {
        cache.clear();
    }

    private PetExpUser(String userId) {
        this.userId = userId;
        load();
        cache.put(userId, this);
    }

    private void load() {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM equippedPet WHERE userId=?;", userId)) {
            if (rs.next()) {
                petID = rs.getInt("petID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM petexp WHERE userId=? AND ID=?;", userId, petID)) {
            if (rs.next()) {
                petLevel = rs.getInt("petLevel");
                petExp = rs.getInt("petExperience");
            }/* else {
                petLevel = 1;
                petExp = BigInteger.ZERO;
                Withering.getBot().getDatabase().update("INSERT INTO petexp VALUES (?, ?, ?, ?);", petID, userId, petLevel, petExp);
            }*/
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getPetLevel(String userId, int ID) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT petLevel FROM petexp WHERE userId=? AND ID=?", userId, ID)) {
            if (rs.next()) {
                petLevel = rs.getInt("petLevel");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return petLevel;
    }

    public int getPetLevel() {
        return petLevel;
    }

    public int getPetExp() {
        return petExp;
    }

    public boolean isPetMaxed(PetItem pet, String userId, int ID) {
        return getPetLevel(userId, ID) >= pet.getMaxLevel();
    }

    public void addPetExp(long amount, String userId) {
        Pets pets = PetUtil.getEquippedPet(userId);
        PetItem petItem = null;
        if (pets != null) {
            petID = (int) pets.getPetID();
            petItem = PetItem.getPetItem(pets.getPetName());
        }
        if (petItem != null) {
            if (isPetMaxed(petItem, userId, petID)) {
                return;
            }
            petExp += amount;
            setPetExp(petExp, petID);
            while (PetXPApi.canPetLevelUp(petItem, petExp, petLevel)) {
                double neededPetExp = PetXPApi.getNeededPetXP(petItem, petLevel);
                int petExpNow = this.petExp;
                setPetExp((int) (petExpNow - neededPetExp), petID);
                final int newPetLevel = petLevel + 1;
                setPetLevel(newPetLevel, petID);
                Player p = Player.getPlayer(userId);
                int pet_levels = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "total_pet_levels", "0"));
                pet_levels += 1;
                StatsUtil.setStat(p.getUserId(), "total_pet_levels", String.valueOf(pet_levels));
                int pet_level = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "highest_pet_level", "0"));
                if (newPetLevel > pet_level) {
                    pet_level = newPetLevel;
                }
                StatsUtil.setStat(p.getUserId(), "highest_pet_level", String.valueOf(pet_level));
                PetLevelUpEvent petLevelUpEvent = new PetLevelUpEvent(p, getPetLevel());
                EventManager.callEvent(petLevelUpEvent);
            }
        }
        cache();
    }

    /*public void addPetExp(PetItem pet, long amount, int ID) {
        BigInteger currentXP = BigInteger.ZERO;
        Pets pets = PetUtil.getPetByID(petID);
        if (pets != null) {
            PetItem petItem = PetItem.getPetItem(pets.getPetName());
            if (petItem != null) {
                maxLevel = petItem.getMaxLevel();
            }
        }
        if (isPetMaxed(pet, userId, petID)) {
            return;
        }
        setPetExp(this.petExp.add(BigInteger.valueOf(amount)), ID);
        while (PetXPApi.canPetLevelUp(pet, this.petExp.doubleValue(), petLevel)) {
            double neededPetExp = PetXPApi.getNeededPetXP(pet, petLevel);
            double petExpNow = this.petExp.doubleValue();
            setPetExp(BigInteger.valueOf(new Double(petExpNow - neededPetExp).longValue()), ID);
            final int newPetLevel = petLevel + 1;
            setPetLevel(newPetLevel, ID);
            Player p = Player.getPlayer(userId);
            int pet_levels = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "total_pet_levels", "0"));
            pet_levels += 1;
            StatsUtil.setStat(p.getUserId(), "total_pet_levels", String.valueOf(pet_levels));
            int pet_level = Integer.parseInt(StatsUtil.getStat(p.getUserId(), "highest_pet_level", "0"));
            if (newPetLevel > pet_level) {
                pet_level = newPetLevel;
            }
            StatsUtil.setStat(p.getUserId(), "highest_pet_level", String.valueOf(pet_level));
            PetLevelUpEvent petLevelUpEvent = new PetLevelUpEvent(p, getPetLevel());
            EventManager.callEvent(petLevelUpEvent);
        }
        cache();
    }*/

    public void setPetLevel(int petLevel, int ID) {
        this.petLevel = petLevel;
        cache();
        Withering.getBot().getDatabase().update("UPDATE petexp SET petLevel=? WHERE userId=? AND ID=?;", petLevel, userId, ID);
    }

    public void setPetExp(int petExp, int ID) {
        this.petExp = petExp;
        cache();
        Withering.getBot().getDatabase().update("UPDATE petexp SET petExperience=? WHERE userId=? AND ID=?;", petExp, userId, ID);
    }

    private void cache() {
        cache.put(userId, this);
    }

    public static PetExpUser getPetExpUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new PetExpUser(userId);
    }
}


