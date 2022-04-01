package me.affluent.decay.trade;

import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import me.affluent.decay.item.Item;
import me.affluent.decay.pets.PetSet;
import me.affluent.decay.pets.Pets;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;

public class Trade {

    private final String p1;
    private final String p2;
    private final HashMap<Long, Item> p1items = new HashMap<>();
    private final HashMap<Long, Item> p2items = new HashMap<>();
    private final HashMap<Long, Pets> p1petitems = new HashMap<>();
    private final HashMap<Long, Pets> p2petitems = new HashMap<>();
    private final long tc;
    private boolean locked;
    private String lockedBy;
    public long m1;
    public long m2;
    public long tl1;
    public long tl2;

    public void clear() {
        m1 = 0;
        m2 = 0;
        lockedBy = null;
        locked = false;
    }

    public Trade(String requester, String receiver, TextChannel textChannel) {
        this.p1 = requester;
        this.p2 = receiver;
        tc = textChannel.getIdLong();
        m1 = 0;
        m2 = 0;
        locked = false;
        lockedBy = null;
    }

    public Player getPlayer1() {
        return Player.getPlayer(p1);
    }

    public Player getPlayer2() {
        return Player.getPlayer(p2);
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public void addP1Item(Item item) {
        if (item == null) return;
        long ID = item.getID();
        p1items.put(ID, item);
    }

    public void addP2Item(Item item) {
        if (item == null) return;
        long ID = item.getID();
        p2items.put(ID, item);
    }

    public void removeP1Item(Item item) {
        if (item == null) return;
        long ID = item.getID();
        p1items.remove(ID);
    }

    public void removeP2Item(Item item) {
        if (item == null) return;
        long ID = item.getID();
        p2petitems.remove(ID);
    }

    public void addP1PetItem(Pets petItem) {
        if (petItem == null) return;
        long ID = petItem.getPetID();
        p1petitems.put(ID, petItem);
    }

    public void addP2PetItem(Pets petItem) {
        if (petItem == null) return;
        long ID = petItem.getPetID();
        p2petitems.put(ID, petItem);
    }

    public void removeP1PetItem(Pets petItem) {
        if (petItem == null) return;
        long ID = petItem.getPetID();
        p1petitems.remove(ID);
    }

    public void removeP2PetItem(Pets petItem) {
        if (petItem == null) return;
        long ID = petItem.getPetID();
        p2petitems.remove(ID);
    }

    public void addP1Money(long amount) {
        m1 += amount;
    }

    public void addP2Money(long amount) {
        m2 += amount;
    }

    public void removeP1Money(long amount) {
        m1 -= amount;
        if (m1 < 0) m1 = 0;
    }

    public void removeP2Money(long amount) {
        m2 -= amount;
        if (m2 < 0) m2 = 0;
    }

    public long getMoney(String uid) {
        if (uid.equalsIgnoreCase(p1)) return m1;
        else if (uid.equalsIgnoreCase(p2)) return m2;
        return -1L;
    }

    public long getMoney1() {
        return m1;
    }

    public long getMoney2() {
        return m2;
    }

    public HashMap<Long, Item> getItems(String uid) {
        if (uid.equalsIgnoreCase(p1)) return p1items;
        else if (uid.equalsIgnoreCase(p2)) return p2items;
        return new HashMap<>();
    }

    public HashMap<Long, Pets> getPetItems(String uid) {
        if (uid.equalsIgnoreCase(p1)) return p1petitems;
        else if (uid.equalsIgnoreCase(p2)) return p2petitems;
        return new HashMap<>();
    }

    public HashMap<Long, Item> getItemsP1() {
        return p1items;
    }

    public HashMap<Long, Item> getItemsP2() {
        return p2items;
    }

    public HashMap<Long, Pets> getPetItemsP1() {
        return p1petitems;
    }

    public HashMap<Long, Pets> getPetItemsP2() {
        return p2petitems;
    }

    public void lock(String id) {
        lockedBy = id;
        locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void finishTrade() {
        TradeAPI.cancelTradeTimer(p1);
        TradeAPI.cancelTradeTimer(p2);
        TradeAPI.cancelTimeoutTimer(p1);
        TradeAPI.cancelTimeoutTimer(p2);
        HashMap<Long, Item> i1 = p1items;
        HashMap<Long, Item> i2 = p2items;
        HashMap<Long, Pets> pi1 = p1petitems;
        HashMap<Long, Pets> pi2 = p2petitems;
        Player p1 = getPlayer1();
        Player p2 = getPlayer2();
        if (m2 > 0) p1.getEcoUser().addBalance((long) (m2 / 1.053));
        if (m1 > 0) p2.getEcoUser().addBalance((long) (m1 / 1.053));
        tl1 = m1;
        tl2 = m2;
        for (long itemID : i1.keySet()) {
            Item item = i1.get(itemID);
            item.updateOwner(p2.getUserId());
            p2.getInventoryUser().addItem(itemID, item.getName());
        }
        for (long itemID : i2.keySet()) {
            Item item = i2.get(itemID);
            item.updateOwner(p1.getUserId());
            p1.getInventoryUser().addItem(itemID, item.getName());
        }
        for (long petID : pi1.keySet()) {
            Pets petItem = pi1.get(petID);
            petItem.updatePetOwner(p2.getUserId());
            p2.getPetUtil().addPetOld(petID, petItem.getPetName());
        }
        for (long petID : pi2.keySet()) {
            Pets petItem = pi2.get(petID);
            petItem.updatePetOwner(p1.getUserId());
            p1.getPetUtil().addPetOld(petID, petItem.getPetName());
        }
        TradeAPI.setTrade(this.p1, null);
        TradeAPI.setTrade(this.p2, null);
        clear();
        TradeAPI.logTrade(this);
    }

    public TextChannel getTextChannel() {
        return Withering.getBot().getShardManager().getTextChannelById(tc);
    }
}