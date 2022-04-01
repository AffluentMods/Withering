package me.affluent.decay.pets;

import me.affluent.decay.Withering;
import me.affluent.decay.enums.PetRarity;
import me.affluent.decay.util.PetStarringUtil;
import me.affluent.decay.util.system.EmoteUtil;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pets {

    private final long ID;
    private String name;
    private String owner;

   public Pets(final long ID, String owner, String name) {
       this.name = name;
       this.ID = ID;
       this.owner = owner;
   }

   public void updatePetOwner(String newOwner) {
       this.owner = newOwner;
       Withering.getBot().getDatabase().update("UPDATE pets SET userId=? WHERE ID=?;", newOwner, ID);
       Withering.getBot().getDatabase().update("UPDATE petStats SET userId=? WHERE ID=?;", newOwner, ID);
       Withering.getBot().getDatabase().update("UPDATE petexp SET userId=? WHERE ID=?;", newOwner, ID);
   }

   public String getPetOwner() {
       return owner;
   }

   public int getPetLevel(String userId, String ID) {
       try (ResultSet rs = Withering.getBot().getDatabase()
               .query("SELECT petLevel FROM petexp WHERE userId=? AND ID=?;", userId, ID)) {
         if (rs.next()) return rs.getInt("petLevel");
   } catch (SQLException ex) {
        ex.printStackTrace();
        }
    return 1;
   }

    public int getPetLevelEmpty() {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT petLevel FROM petexp WHERE userId=? AND ID=?;", owner, ID)) {
            if (rs.next()) return rs.getInt("petLevel");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

   public BigInteger getPetExp(String userId, String ID) {
       try (ResultSet rs = Withering.getBot().getDatabase()
               .query("SELECT petExperience FROM petexp WHERE userId=? AND ID=?;", userId, ID)) {
           if (rs.next()) {
               String expString = rs.getString("petExperience");
               return new BigInteger(expString);
           }
       } catch (SQLException ex) {
           ex.printStackTrace();
       }
       return BigInteger.valueOf(0);
   }

    public PetRarity getRarity() {
        for (PetRarity rarity : PetRarity.values()) {
            if (this.name.split(" ")[0].equalsIgnoreCase(rarity.name())) return rarity;
        }
        return null;
    }

    public int getPetStars(String userId, String ID) {
        try (ResultSet rs = Withering.getBot().getDatabase()
                .query("SELECT petStars FROM petStats WHERE userId=? AND ID=?;", userId, ID)) {
            if (rs.next()) return rs.getInt("petStars");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public int getPetStarsEmpty() {
        return PetStarringUtil.getPetStar(owner, ID);
    }

   public long getPetID() {
       return ID;
   }

   public String getPetName() {
       return name;
   }

   public String getPetEmote() {
       return EmoteUtil.getEmoteMention(name.toLowerCase());
   }
}
