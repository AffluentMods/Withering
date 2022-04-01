package me.affluent.decay.entity;

import me.affluent.decay.Withering;
import me.affluent.decay.command.actions.ForgeCommand;
import me.affluent.decay.enums.ArmorType;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.item.Item;
import me.affluent.decay.util.A;
import me.affluent.decay.util.DiamondsUtil;
import me.affluent.decay.util.KeysUtil;
import me.affluent.decay.util.itemUtil.ItemLockingUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.weapon.Arrow;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class InventoryUser {

    private static HashMap<String, InventoryUser> cache = new HashMap<>();

    private final String userId;
    private Map<Long, String> items;

    public static void clearCache() {
        cache.clear();
    }

    private InventoryUser(String userId) {
        this.userId = userId;
        load();
        cache();
    }

    public static Item getItemByID(long id) {
        try (ResultSet rs = Withering.getBot().getDatabase().query("SELECT * FROM inventory WHERE ID=?", id)) {
            if (rs.next()) return new Item(id, rs.getString("userId"), rs.getString("itemName"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void load() {
        items = new HashMap<>();
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM inventory WHERE userId=? ORDER BY ID;", userId)) {
            while (rs.next()) {
                String itemName = rs.getString("itemName").toLowerCase();
                if (itemName.equalsIgnoreCase("diamond") || itemName.toLowerCase().endsWith(" key")) continue;
                items.put(rs.getLong("ID"), itemName);
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

    public Map<Long, String> getItems() {
        return items;
    }

    public List<Long> getItemsSorted(ItemType it) {
        Map<Long, String> items = this.items;
        HashMap<ItemType, Integer> sortIDs = new HashMap<>();
        int lastID = 1;
        for (ItemType v : ItemType.values()) {
            if (v == it) sortIDs.put(v, 1000);
            else {
                sortIDs.put(v, lastID);
                lastID++;
            }
        }
        List<Long> sorted = new ArrayList<>();
        List<Long> addLater = new ArrayList<>();
        int priority = sortIDs.get(it);
        for (long ID : items.keySet()) {
            Item item = getItemByID(ID);
            if (item == null) {
                continue;
            }
            ItemType itt = item.getType();
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

    public List<Long> getItemsSorted(Rarity rarity) {
        Map<Long, String> items = this.items;
        HashMap<Rarity, Integer> sortIDs = new HashMap<>();
        int lastID = 1;
        for (Rarity v : Rarity.values()) {
            if (v == rarity) sortIDs.put(v, 1000);
            else {
                sortIDs.put(v, lastID);
                lastID++;
            }
        }
        List<Long> sorted = new ArrayList<>();
        List<Long> addLater = new ArrayList<>();
        int priority = sortIDs.get(rarity);
        for (long ID : items.keySet()) {
            Item item = getItemByID(ID);
            if (item == null) {
                continue;
            }
            Rarity itt = item.getRarity();
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

    public List<Long> getItemsSorted(ArmorType at) {
        Map<Long, String> items = this.items;
        HashMap<ArmorType, Integer> sortIDs = new HashMap<>();
        int lastID = 1;
        for (ArmorType v : ArmorType.values()) {
            if (v == at) sortIDs.put(v, 1000);
            else {
                sortIDs.put(v, lastID);
                lastID++;
            }
        }
        List<Long> sorted = new ArrayList<>();
        List<Long> addLater = new ArrayList<>();
        int priority = sortIDs.get(at);
        for (long ID : items.keySet()) {
            Item item = getItemByID(ID);
            if (item == null) {
                continue;
            }
            ArmorType itt = item.getGear();
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

    public List<Item> getItemsByName(String name) {
        List<Item> a = new ArrayList<>();
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String item = items.get(ID);
            if (item.replaceAll("_", " ").equalsIgnoreCase(name)) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a.add(new Item(ID, userId, item));
            }
        }
        return a;
    }

    public int getItemsByNameOrb(String name) {
        int a = 0;
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String item = items.get(ID);
            if (item.replaceAll("_", " ").equalsIgnoreCase(name)) {
                Item obj = new Item(ID, userId, item);
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(obj.getType(), obj.getRarity());
            }
        }
        return a;
    }

    public List<Item> getItemsAll() {
        List<Item> a = new ArrayList<>();
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID);
            Item item = new Item(ID, userId, itemName);
            if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a.add(item);
        }
        return a;
    }

    public int getItemsAllOrb() {
        int a = 0;
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID);
            Item item = new Item(ID, userId, itemName);
            if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(item.getType(), item.getRarity());
        }
        return a;
    }

    public List<Item> getItemsByRarity(String name) {
        List<Item> a = new ArrayList<>();
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID);
            Item item = new Item(ID, userId, itemName);
            if (item.getRarity().name().equalsIgnoreCase(Rarity.getRarity(name).name())) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) {
                    a.add(item);
                }
            }
        }
        return a;
    }

    public int getItemsByRarityOrb(String name) {
        int a = 0;
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID);
            Item item = new Item(ID, userId, itemName);
            if (item.getRarity().name().equalsIgnoreCase(Rarity.getRarity(name).name())) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(item.getType(), item.getRarity());
            }
        }
        return a;
    }

    public List<Item> getItemsByMaterial(String name) {
        List<Item> a = new ArrayList<>();
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID);
            Item item = new Item(ID, userId, itemName);
            if (item.getType().name().equalsIgnoreCase(ItemType.getItemType(name).name())) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a.add(item);
            }
        }
        return a;
    }

    public int getItemsByMaterialOrb(String name) {
        int a = 0;
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID);
            Item item = new Item(ID, userId, itemName);
            if (item.getType().name().equalsIgnoreCase(ItemType.getItemType(name).name())) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(item.getType(), item.getRarity());
            }
        }
        return a;
    }

    public List<Item> getItemsByRarityMaterial(String name) {
        List<Item> a = new ArrayList<>();
        String[] split = name.split(" ");
        String rarity = split[0];
        String itemType = name.substring(rarity.length() + 1);
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID).toLowerCase();
            Item item = new Item(ID, userId, itemName);
            if (item.getRarity() == Rarity.getRarity(rarity) &&
                item.getType().name().replaceAll("_", " ").equalsIgnoreCase(itemType.replaceAll("_", " "))) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a.add(item);
            }
        }
        return a;
    }

    public int getItemsByRarityMaterialOrb(String name) {
        int a = 0;
        String[] split = name.split(" ");
        String rarity = split[0];
        String itemType = name.substring(rarity.length() + 1);
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID).toLowerCase();
            Item item = new Item(ID, userId, itemName);
            if (item.getRarity() == Rarity.getRarity(rarity) &&
                    item.getType().name().replaceAll("_", " ").equalsIgnoreCase(itemType.replaceAll("_", " "))) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(item.getType(), item.getRarity());
            }
        }
        return a;
    }

    public List<Item> getItemsByMaterialGear(String name) {
        List<Item> a = new ArrayList<>();
        String[] split = name.split(" ");
        String gear = split[split.length - 1];
        String itemType = name.substring(0, (name.length() - gear.length() - 1));
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID).toLowerCase();
            Item item = new Item(ID, userId, itemName);
            if (item.getType().name().replaceAll("_", " ").equalsIgnoreCase(itemType) &&
                item.getGear().name().replaceAll("_", " ").equalsIgnoreCase(gear)) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a.add(item);
            }
        }
        return a;
    }

    public int getItemsByMaterialGearOrb(String name) {
        int a = 0;
        String[] split = name.split(" ");
        String gear = split[split.length - 1];
        String itemType = name.substring(0, (name.length() - gear.length() - 1));
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID).toLowerCase();
            Item item = new Item(ID, userId, itemName);
            if (item.getType().name().replaceAll("_", " ").equalsIgnoreCase(itemType) &&
                    item.getGear().name().replaceAll("_", " ").equalsIgnoreCase(gear)) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(item.getType(), item.getRarity());
            }
        }
        return a;
    }

    public List<Item> getItemsByRarityGear(String name) {
        List<Item> a = new ArrayList<>();
        String rarity = name.split(" ")[0];
        String gear = name.split(" ")[1];
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID).toLowerCase();
            Item item = new Item(ID, userId, itemName);
            if (item.getRarity().name().replaceAll("_", " ").equalsIgnoreCase(rarity) &&
                item.getGear().name().replaceAll("_", " ").equalsIgnoreCase(gear)) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a.add(item);
            }
        }
        return a;
    }

    public int getItemsByRarityGearOrb(String name) {
        int a = 0;
        String rarity = name.split(" ")[0];
        String gear = name.split(" ")[1];
        Map<Long, String> items = getItems();
        for (long ID : items.keySet()) {
            String itemName = items.get(ID).toLowerCase();
            Item item = new Item(ID, userId, itemName);
            if (item.getRarity().name().replaceAll("_", " ").equalsIgnoreCase(rarity) &&
                    item.getGear().name().replaceAll("_", " ").equalsIgnoreCase(gear)) {
                if (ItemLockingUtil.getItemLockValue(userId, ID) == 0) a += ForgeCommand.getOrbAmount(item.getType(), item.getRarity());
            }
        }
        return a;
    }

    public void getItemLevel(long ID) {
    }

    public void addItem(long ID, String itemName) {
        items.put(ID, itemName);
        cache();
        Withering.getBot().getDatabase()
                .update("INSERT INTO inventory (ID, userId, itemName) VALUES (" + ID + ", '" + userId + "', '" +
                        itemName + "');");
        Withering.getBot().getDatabase()
                .update("INSERT INTO itemStats (ID, userId, itemLevel, itemStars, gemName, gemLevel, lockValue) VALUES " +
                        "(" + ID + ", '" + userId + "', " + 1 + ", " + 0 + ", '" + "none" + "', " + 0 + ", " + 0 + ");");
    }

    public void addItem(String itemName, long amount) {
        if (itemName.endsWith(" key")) {
            KeysUtil.setKeys(userId, itemName, KeysUtil.getKeys(userId, itemName) + amount);
            return;
        }

        if (itemName.equalsIgnoreCase("diamond")) {
            DiamondsUtil.setDiamonds(userId, DiamondsUtil.getDiamonds(userId) + amount);
            return;
        }
        cache();
        try (PreparedStatement ps = Withering.getBot().getDatabase().getCon()
                .prepareStatement("INSERT INTO inventory (userId, itemName) VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, userId);
            ps.setObject(2, itemName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getInt(1);
                items.put(id, itemName);
                cache();
                Withering.getBot().getDatabase()
                        .update("INSERT INTO itemStats (ID, userId, itemLevel, itemStars, gemName, gemLevel, lockValue) VALUES " +
                                "(" + id + ", '" + userId + "', " + 1 + ", " + 0 + ", '" + "none" + "', " + 0 + ", " + 0 + ");");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public long addItemReturn(String itemName, long amount) {
        long id = 0;
        try (PreparedStatement ps = Withering.getBot().getDatabase().getCon()
                .prepareStatement("INSERT INTO inventory (userId, itemName) VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, userId);
            ps.setObject(2, itemName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
                items.put(id, itemName);
                cache();
                Withering.getBot().getDatabase()
                        .update("INSERT INTO itemStats (ID, userId, itemLevel, itemStars, gemName, gemLevel, lockValue) VALUES " +
                                "(" + id + ", '" + userId + "', " + 1 + ", " + 0 + ", '" + "none" + "', " + 0 + ", " + 0 + ");");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public void removeItem(String itemName, long amount) {
        if (itemName.endsWith(" key")) {
            KeysUtil.setKeys(userId, itemName, KeysUtil.getKeys(userId, itemName) - amount);
            return;
        }
        if (itemName.equalsIgnoreCase("diamond")) {
            DiamondsUtil.setDiamonds(userId, DiamondsUtil.getDiamonds(userId) - amount);
            return;
        }
        removeItem(getItemID(itemName));
    }

    public void removeItem(String itemName) {
        removeItem(itemName, 1);
    }

    public void removeItem(final long ID) {
        items.remove(ID);
        cache();
        Withering.getBot().getDatabase().update("DELETE FROM inventory WHERE ID=? AND userId=?;", ID, userId);
    }

    public void fullRemoveItem(final long ID) {
        removeItem(ID);
        cache();
        Withering.getBot().getDatabase().update("DELETE FROM itemStats WHERE ID=? AND userId=?;", ID, userId);
    }

    public int getItemID(String item) {
        int itemID = -1;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT ID FROM inventory WHERE itemName=? AND userId=?;", item, userId)) {
            if (rs.next()) itemID = rs.getInt("ID");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return itemID;
    }

    public long hasItem(String itemName) {
        if (itemName.endsWith(" key")) {
            return KeysUtil.getKeys(userId, itemName);
        }
        if (itemName.equalsIgnoreCase("diamond")) {
            return DiamondsUtil.getDiamonds(userId);
        }
        long has = 0;
        for (String items : items.values()) {
            if (items.equalsIgnoreCase(itemName)) has++;
        }
        return has;
    }

    public boolean containsItem(String itemName) {
        return hasItem(itemName) > 0;
    }

    public int getSize() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT COUNT(*) FROM inventory WHERE userId=?;", userId)) {
            if (rs.next()) return rs.getInt("COUNT(*)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return items.size();
    }

    public static InventoryUser getInventoryUser(String userId) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        return new InventoryUser(userId);
    }

    public boolean hasArrows(int level) {
        boolean hasArrows = false;
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT * FROM inventory WHERE itemName LIKE '%arrow' AND userId=?;", userId)) {
            while (rs.next()) {
                Arrow arrow = Arrow.getArrow(rs.getString("itemName"));
                if (arrow != null) {
                    ItemType it = arrow.getItemType();
                    if (it.getLevelRequirement() <= level) {
                        hasArrows = true;
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return hasArrows;
    }
}