package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.item.Item;
import me.affluent.decay.pets.Pets;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PetUtil {

    private static HashMap<String, PetUtil> cache = new HashMap<>();

    private final String userId;
    private Map<Long, String> pets;

    public static void clearCache() {
        cache.clear();
    }

    private PetUtil(String userId) {
        this.userId = userId;
        load();
        cache();
    }

    public static Pets getPetByID(long id) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM pets WHERE ID=?;", id)) {
            if (rs.next()) return new Pets(id, rs.getString("userId"), rs.getString("petName"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Pets getEQPetByID(long id) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM equippedPet WHERE petID=?;", id)) {
            if (rs.next()) return new Pets(id, rs.getString("userId"), rs.getString("pet"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Pets getEquippedPet(String userId) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM equippedPet WHERE userId=?;", userId)) {
            if (rs.next()) return new Pets(rs.getInt("petID"), userId, rs.getString("pet"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void load() {
        pets = new HashMap<>();
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM pets WHERE userId=? ORDER by ID;", userId)) {
         while (rs.next()) {
             String petName = rs.getString("petName").toLowerCase();
             pets.put(rs.getLong("ID"), petName);
             }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cache();
    }

    public String getUserId() {
        return userId;
    }

    private void cache() {
        cache.put(this.userId, this);
    }

    public Map<Long, String> getPets() {
        return pets;
    }

    public List<Long> getPetsSorted(PetRarity rarity) {
        Map<Long, String> pets = this.pets;
        HashMap<PetRarity, Integer> sortIDs = new HashMap<>();
        int lastID = 1;
        for (PetRarity v : PetRarity.values()) {
            if (v == rarity) sortIDs.put(v, 1000);
            else {
                sortIDs.put(v, lastID);
                lastID++;
            }
        }
        List<Long> sorted = new ArrayList<>();
        List<Long> addLater = new ArrayList<>();
        int priority = sortIDs.get(rarity);
        for (long ID : pets.keySet()) {
            Pets pets2 = getPetByID(ID);
            if (pets2 == null) {
                continue;
            }
            PetRarity itt = pets2.getRarity();
            int sort = sortIDs.get(itt);
            if (sort < priority) {
                addLater.add(ID);
            } else {
                sorted.add(ID);
            }
        }
        sorted.addAll(addLater);
        return sorted;
    }

    public List<Item> getPetsByName(String name) {
        List<Item> a = new ArrayList<>();
        Map<Long, String> pets = getPets();
        for (long ID : pets.keySet()) {
            String pet = pets.get(ID);
            if (pet.replaceAll("_", " ").equalsIgnoreCase(name)) a.add(new Item(ID, userId, pet));
        }
        return a;
    }

    public void addPet(long ID, String petName) {
        pets.put(ID, petName);
        cache();
        Withering.getBot().getDatabase()
                .update("INSERT INTO pets (ID, userId, petName) VALUES" +
                        " (" + ID + ", '" + userId + "', '" + petName + "');");
        Withering.getBot().getDatabase()
                .update("INSERT INTO petStats (ID, userId, petStars) VALUES" +
                        " (" + ID + ", '" + userId + "', " + 0 + ");");
        Withering.getBot().getDatabase()
                .update("INSERT INTO petexp (ID, userId, petLevel, petExperience) VALUES" +
                        " (" + ID + ", '" + userId + "', " + 1 + ", " + 0 + ");");
    }

    public void addPetOld(long ID, String petName) {
        pets.put(ID, petName);
        cache();
        Withering.getBot().getDatabase()
                .update("INSERT INTO pets (ID, userId, petName) VALUES" +
                        " (" + ID + ", '" + userId + "', '" + petName + "');");
    }

    public void addPet(String petName, long amount) {
        try (PreparedStatement ps = Withering.getBot().getDatabase().getCon()
                .prepareStatement("INSERT INTO pets (userId, petName) VALUES (?, ?);",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, userId);
            ps.setObject(2, petName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getInt(1);
                pets.put(id, petName);
                cache();
                Withering.getBot().getDatabase()
                        .update("INSERT INTO petStats (ID, userId, petStars) VALUES" +
                                " (" + id + ", '" + userId + "', " + 0 + ");");
                Withering.getBot().getDatabase()
                        .update("INSERT INTO petexp (ID, userId, petLevel, petExperience) VALUES" +
                                " (" + id + ", '" + userId + "', " + 1 + ", " + 0 + ");");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removePet(String petName, long amount) {
        removePet(getPetID(petName));
    }

    public void removePet(String petName) {
        removePet(petName, 1);
    }

    public void removePet(final long ID) {
        pets.remove(ID);
        cache();
        Withering.getBot().getDatabase().update("DELETE FROM pets WHERE ID=? AND userId=?;", ID, userId);
    }

    public int getPetID(String pet) {
        int petID = -1;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT ID FROM pets WHERE petName=? AND userId=?;", pet, userId)) {
            if (rs.next()) petID = rs.getInt("ID");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return petID;
    }

    public long hasPet(String petName) {
        long amount = 0;
        for (String pets : pets.values()) {
            if (pets.equalsIgnoreCase(petName)) amount++;
        }
        return amount;
    }

    public int getPetSize() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT COUNT(*) FROM pets WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("COUNT(*)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pets.size();
    }

    public static PetUtil getPetUtil(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new PetUtil(userId);
    }
}
