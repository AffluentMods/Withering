package me.affluent.decay.item;

import me.affluent.decay.Withering;
import me.affluent.decay.enums.ArmorType;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.system.EmoteUtil;

public class Item {

    private final long ID;
    private String owner;
    private String name;

    public Item(final long ID, String owner, String name) {
        this.name = name;
        this.ID = ID;
        this.owner = owner;
    }

    public ArmorType getGear() {
        String[] split = this.name.split(" ");
        String gear = split[split.length - 1];
        for (ArmorType at : ArmorType.values()) {
            if (at.name().replaceAll("_", " ").equalsIgnoreCase(gear)) return at;
        }
        return null;
    }

    public Rarity getRarity() {
        String[] split = this.name.split(" ");
        String gear = split[split.length - 1];
        for (Rarity rarity : Rarity.values())
            if (this.name.split(" ")[0].equalsIgnoreCase(rarity.name())) return rarity;
        return null;
    }

    public ItemType getType() {
        String[] split = name.split(" ");
        String itemTypeName = split[1];
        if (split.length > 3) itemTypeName += " " + split[2];
        for (ItemType itemType : ItemType.values()) {
            if (itemType.name().replace("_", " ").equalsIgnoreCase(itemTypeName)) return itemType;
        }
        return null;
    }

    public void updateOwner(String newOwner) {
        this.owner = newOwner;
        Withering.getBot().getDatabase().update("UPDATE inventory SET userId=? WHERE ID=?;", newOwner, ID);
        Withering.getBot().getDatabase().update("UPDATE itemStats SET userId=? WHERE ID=?;", newOwner, ID);
    }

    public void updateGear(ArmorType newGear) {
        this.name = getRarity().name().toLowerCase().replace("_", " ") + " " +
                    getType().name().toLowerCase().replace("_", " ") + " " +
                    newGear.name().toLowerCase().replace("_", " ");
        Withering.getBot().getDatabase().update("UPDATE inventory SET itemName=? WHERE ID=?;", this.name, ID);
    }

    public void updateRarity(Rarity newRarity) {
        this.name = newRarity.name().toLowerCase().replace("_", " ") + " " +
                    getType().name().toLowerCase().replace("_", " ") + " " +
                    getGear().name().toLowerCase().replace("_", " ");
        Withering.getBot().getDatabase().update("UPDATE inventory SET itemName=? WHERE ID=?;", this.name, ID);
    }

    public String getOwner() {
        return owner;
    }

    public int getSpecificLevel(String userId, int ID) {
        return ItemLevelingUtil.getItemLevel(userId, ID);
    }

    public int getSpecificStars(String userId, int ID) {
        return ItemStarringUtil.getItemStar(userId, ID);
    }

    public int getLevel() {
        return ItemLevelingUtil.getItemLevel(owner, ID);
    }

    public int getStars() {
        return ItemStarringUtil.getItemStar(owner, ID);
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmote() {
        return EmoteUtil.getEmoteMention(getRarity().name().toLowerCase() + " " + getType().name().toLowerCase() + " " + getGear().name().toLowerCase());
    }
}