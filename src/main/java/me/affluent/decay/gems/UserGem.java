package me.affluent.decay.gems;

public class UserGem {

    private final String owner;
    private final Gem gem;
    private final String itemId;
    private int level;

    public UserGem(String owner, String itemId, Gem gem, int level) {
        this.owner = owner;
        this.gem = gem;
        this.itemId = itemId;
        this.level = level;
    }

    public String getItemId() {
        return itemId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getOwner() {
        return owner;
    }

    public int getLevel() {
        return level;
    }

    public Gem getGem() {
        return gem;
    }
}