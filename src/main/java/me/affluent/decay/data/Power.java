package me.affluent.decay.data;

import me.affluent.decay.Withering;
import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.entity.PetUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.ItemType;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.pets.Pets;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.util.PetStarringUtil;
import me.affluent.decay.util.PetUtil;
import me.affluent.decay.util.settingsUtil.EquippedDragonUtil;
import me.affluent.decay.util.settingsUtil.EquippedIronUtil;
import me.affluent.decay.util.settingsUtil.EquippedWitherUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.weapon.*;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonWeapon;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherWeapon;

public class Power {

    public static int getFullPower(Player p) {
        boolean ironHelmetEquipped = false;
        boolean ironChestplateEquipped = false;
        boolean ironGlovesEquipped = false;
        boolean ironTrousersEquipped = false;
        boolean ironBootsEquipped = false;
        boolean ironSwordEquipped = false;
        boolean ironShieldEquipped = false;
        boolean dragonHelmetEquipped = false;
        boolean dragonChestplateEquipped = false;
        boolean dragonGlovesEquipped = false;
        boolean dragonTrousersEquipped = false;
        boolean dragonBootsEquipped = false;
        boolean dragonSwordEquipped = false;
        boolean dragonShieldEquipped = false;
        boolean witherHelmetEquipped = false;
        boolean witherChestplateEquipped = false;
        boolean witherGlovesEquipped = false;
        boolean witherTrousersEquipped = false;
        boolean witherBootsEquipped = false;
        boolean witherSwordEquipped = false;
        boolean witherShieldEquipped = false;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronHelmet().equalsIgnoreCase("equipped")) ironHelmetEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronChestplate().equalsIgnoreCase("equipped")) ironChestplateEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronGloves().equalsIgnoreCase("equipped")) ironGlovesEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronTrousers().equalsIgnoreCase("equipped")) ironTrousersEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronBoots().equalsIgnoreCase("equipped")) ironBootsEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronSword().equalsIgnoreCase("equipped")) ironSwordEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(p.getUserId()).getIronShield().equalsIgnoreCase("equipped")) ironShieldEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonHelmet().equalsIgnoreCase("equipped")) dragonHelmetEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonChestplate().equalsIgnoreCase("equipped")) dragonChestplateEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonGloves().equalsIgnoreCase("equipped")) dragonGlovesEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonTrousers().equalsIgnoreCase("equipped")) dragonTrousersEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonBoots().equalsIgnoreCase("equipped")) dragonBootsEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonSword().equalsIgnoreCase("equipped")) dragonSwordEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(p.getUserId()).getDragonShield().equalsIgnoreCase("equipped")) dragonShieldEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherHelmet().equalsIgnoreCase("equipped")) witherHelmetEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherChestplate().equalsIgnoreCase("equipped")) witherChestplateEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherGloves().equalsIgnoreCase("equipped")) witherGlovesEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherTrousers().equalsIgnoreCase("equipped")) witherTrousersEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherBoots().equalsIgnoreCase("equipped")) witherBootsEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherSword().equalsIgnoreCase("equipped")) witherSwordEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(p.getUserId()).getWitherShield().equalsIgnoreCase("equipped")) witherShieldEquipped = true;
        int power = 0;
        for (Armor armor : p.getArmorUser().getAllArmor()) {
            power += getPower(armor.getRarity()) + getPower(armor.getItemType());
        }
        IronHelmet hIron = p.getArmorIronUser().getIronHelmet();
        IronChestplate cIron = p.getArmorIronUser().getIronChestplate();
        IronGloves gIron = p.getArmorIronUser().getIronGloves();
        IronTrousers tIron = p.getArmorIronUser().getIronTrousers();
        IronBoots bIron = p.getArmorIronUser().getIronBoots();
        DragonHelmet hDragon = p.getArmorDragonUser().getDragonHelmet();
        DragonChestplate cDragon = p.getArmorDragonUser().getDragonChestplate();
        DragonGloves gDragon = p.getArmorDragonUser().getDragonGloves();
        DragonTrousers tDragon = p.getArmorDragonUser().getDragonTrousers();
        DragonBoots bDragon = p.getArmorDragonUser().getDragonBoots();
        WitherHelmet hWither = p.getArmorWitherUser().getWitherHelmet();
        WitherChestplate cWither = p.getArmorWitherUser().getWitherChestplate();
        WitherGloves gWither = p.getArmorWitherUser().getWitherGloves();
        WitherTrousers tWither = p.getArmorWitherUser().getWitherTrousers();
        WitherBoots bWither = p.getArmorWitherUser().getWitherBoots();
        if (ironHelmetEquipped) {
            if (hIron != null) {
                power += getPower(hIron.getRarity()) + getPower(hIron.getItemType());
            }
        }
        if (ironChestplateEquipped) {
            if (cIron != null) {
                power += getPower(cIron.getRarity()) + getPower(cIron.getItemType());
            }
        }
        if (ironGlovesEquipped) {
            if (gIron != null) {
                power += getPower(gIron.getRarity()) + getPower(gIron.getItemType());
            }
        }
        if (ironTrousersEquipped) {
            if (tIron != null) {
                power += getPower(tIron.getRarity()) + getPower(tIron.getItemType());
            }
        }
        if (ironBootsEquipped) {
            if (bIron != null) {
                power += getPower(bIron.getRarity()) + getPower(bIron.getItemType());
            }
        }
        if (dragonHelmetEquipped) {
            if (hDragon != null) {
                power += getPower(hDragon.getRarity()) + getPower(hDragon.getItemType());
            }
        }
        if (dragonChestplateEquipped) {
            if (cDragon != null) {
                power += getPower(cDragon.getRarity()) + getPower(cDragon.getItemType());
            }
        }
        if (dragonGlovesEquipped) {
            if (gDragon != null) {
                power += getPower(gDragon.getRarity()) + getPower(gDragon.getItemType());
            }
        }
        if (dragonTrousersEquipped) {
            if (tDragon != null) {
                power += getPower(tDragon.getRarity()) + getPower(tDragon.getItemType());
            }
        }
        if (dragonBootsEquipped) {
            if (bDragon != null) {
                power += getPower(bDragon.getRarity()) + getPower(bDragon.getItemType());
            }
        }
        if (witherHelmetEquipped) {
            if (hWither != null) {
                power += getPower(hWither.getRarity()) + getPower(hWither.getItemType());
            }
        }
        if (witherChestplateEquipped) {
            if (cWither != null) {
                power += getPower(cWither.getRarity()) + getPower(cWither.getItemType());
            }
        }
        if (witherGlovesEquipped) {
            if (gWither != null) {
                power += getPower(gWither.getRarity()) + getPower(gWither.getItemType());
            }
        }
        if (witherTrousersEquipped) {
            if (tWither != null) {
                power += getPower(tWither.getRarity()) + getPower(tWither.getItemType());
            }
        }
        if (witherBootsEquipped) {
            if (bWither != null) {
                power += getPower(bWither.getRarity()) + getPower(bWither.getItemType());
            }
        }
        Weapon w = p.getArmorUser().getWeapon();
        IronWeapon wIron = p.getArmorIronUser().getIronWeapon();
        DragonWeapon wDragon = p.getArmorDragonUser().getDragonWeapon();
        WitherWeapon wWither = p.getArmorWitherUser().getWitherWeapon();
        IronShield sIron = p.getArmorIronUser().getIronShield();
        DragonShield sDragon = p.getArmorDragonUser().getDragonShield();
        WitherShield sWither = p.getArmorWitherUser().getWitherShield();
        PetUser petUser = p.getPetUser();
        PetItem petObj = p.getPetUser().getPet();
        if (petObj != null) {
            long petID = Long.parseLong(petUser.getPetID());
            Pets petItem = PetUtil.getEQPetByID(petID);
            int petStars = PetStarringUtil.getPetStar(p.getUserId(), petID);
            int petLevel = p.getPetExpUser().getPetLevel();
            power += getFullPetPower(p, petObj, petLevel, petStars);
        }
        if (w != null) {
            power += getPower(w.getRarity()) + getPower(w.getItemType()) + getPowerWeapon(w);
            if (w instanceof Bow) {
                if (p.getArmorUser().getArrow() != null) power += getPower(p.getArmorUser().getArrow());
            }
            if (w instanceof Sword) {
                if (p.getArmorUser().getShield() != null) power += getPower(p.getArmorUser().getShield());
            }
            if (w instanceof Staff) {
                int value = getPowerWeapon(p.getArmorUser().getWeapon());
                power += (value * 0.37);
            }
        }
        if (ironSwordEquipped) {
            if (wIron != null) {
                power += getPower(wIron.getRarity()) + getPower(wIron.getItemType()) + getPowerIronWeapon(wIron);
            }
        }
        if (ironShieldEquipped) {
            if (sIron != null) {
                power += getPower(sIron.getRarity()) + getPower(sIron.getItemType());
            }
        }
        if (dragonSwordEquipped) {
            if (wDragon != null) {
                power += getPower(wDragon.getRarity()) + getPower(wDragon.getItemType()) + getPowerDragonWeapon(wDragon);
            }
        }
        if (dragonShieldEquipped) {
            if (sDragon != null) {
                power += getPower(sDragon.getRarity()) + getPower(sDragon.getItemType());
            }
        }
        if (witherSwordEquipped) {
            if (wWither != null) {
                power += getPower(wWither.getRarity()) + getPower(wWither.getItemType()) + getPowerWitherWeapon(wWither);
            }
        }
        if (witherShieldEquipped) {
            if (sWither != null) {
                power += getPower(sWither.getRarity()) + getPower(sWither.getItemType());
            }
        }
        power += (p.getHealthUser().getMaxHealth() * 8);
        Withering.getBot().getDatabase().update("DELETE FROM power WHERE userId=?;", p.getUserId());
        Withering.getBot().getDatabase().update("INSERT INTO power VALUES (?, ?);", p.getUserId(), power);
        return power;
    }
    
    public static int getIronPower(Player p) {
        int power = 0;
        for (Armor armor : p.getArmorIronUser().getAllArmor()) {
            power += getPower(armor.getRarity()) + getPower(armor.getItemType());
        }

        IronWeapon wIron = p.getArmorIronUser().getIronWeapon();
        IronShield sIron = p.getArmorIronUser().getIronShield();

        if (wIron != null) {
            power += getPower(wIron.getRarity()) + getPower(wIron.getItemType()) + getPowerIronWeapon(wIron);
        }
        if (sIron != null) {
            power += getPower(sIron.getRarity()) + getPower(sIron.getItemType());
        }
        return power;
    }

    public static int getDragonPower(Player p) {
        int power = 0;
        for (Armor armor : p.getArmorDragonUser().getAllArmor()) {
            power += getPower(armor.getRarity()) + getPower(armor.getItemType());
        }

        DragonWeapon wDragon = p.getArmorDragonUser().getDragonWeapon();
        DragonShield sDragon = p.getArmorDragonUser().getDragonShield();

        if (wDragon != null) {
            power += getPower(wDragon.getRarity()) + getPower(wDragon.getItemType()) + getPowerDragonWeapon(wDragon);
        }
        if (sDragon != null) {
            power += getPower(sDragon.getRarity()) + getPower(sDragon.getItemType());
        }
        return power;
    }

    public static int getWitherPower(Player p) {
        int power = 0;
        for (Armor armor : p.getArmorWitherUser().getAllArmor()) {
            power += getPower(armor.getRarity()) + getPower(armor.getItemType());
        }

        WitherWeapon wWither = p.getArmorWitherUser().getWitherWeapon();
        WitherShield sWither = p.getArmorWitherUser().getWitherShield();

        if (wWither != null) {
            power += getPower(wWither.getRarity()) + getPower(wWither.getItemType()) + getPowerWitherWeapon(wWither);
        }
        if (sWither != null) {
            power += getPower(sWither.getRarity()) + getPower(sWither.getItemType());
        }
        return power;
    }

    public static int getPower(ItemType itemType) {
        switch (itemType) {
            case WOOD:
                return 66;
            case COPPER:
                return 165;
            case REINFORCED:
                return 352;
            case TITANIUM:
                return 627;
            case IRON:
                return 1023;
            case STEEL:
                return 1551;
            case CARBON_STEEL:
                return 2365;
            case DRAGON_STEEL:
                return 3630;
            case TITAN_ALLOY:
                return 4675;
            case WITHER:
                return 9632;
        }
        return 0;
    }

    public static int getFullPetPower(Player p, PetItem petItem, int level, int stars) {
        int petPower;
        int totalHP = 0;
        int totalDMG = 0;
        double levelDMG = petItem.getLevelDMG();
        double levelHP = petItem.getLevelHP();
        int starterHP = petItem.getStarterHP();
        int starterDMG = petItem.getStarterDMG();

        totalDMG += (levelDMG * level);
        totalDMG += starterDMG;
        totalHP += (levelHP * level);
        totalHP += starterHP;
        if (stars > 0) {
            totalHP *= ((double) (stars * 5) / 100) + 1;
            totalDMG *= ((double) (stars * 5) / 100) + 1;
        }
        petPower = ((totalDMG + totalHP) * 27);
        return petPower;
    }

    public static int getPower(Rarity rarity) {
        return rarity.getPower() + getBonusPowers(rarity);
    }

    public static int getPowerWeapon(Weapon weapon) {
        if (weapon.getName().toLowerCase().endsWith(" staff")) {
            return (int) (weapon.getDamageMiddle() * 37.8);
        }
        return weapon.getDamageMiddle() * 10;
    }

    public static int getPowerIronWeapon(IronWeapon weapon) {
        return weapon.getDamageMiddle() * 10;
    }
    
    public static int getPowerDragonWeapon(DragonWeapon weapon) {
        return weapon.getDamageMiddle() * 10;
    }
    
    public static int getPowerWitherWeapon(WitherWeapon weapon) {
        return weapon.getDamageMiddle() * 10;
    }

    public static int getPower(Arrow arrow) {
        return getPower(arrow.getRarity()) + getPower(arrow.getItemType());
    }

    public static int getPower (Shield shield) {
        return getPower(shield.getRarity()) + getPower(shield.getItemType());
    }

    public static int getBonusPowers(Rarity rarity) {
        int bonus = 0;
        RarityClass rarityClass = Rarities.getRarityClass(rarity);
        for (Attribute attribute : rarityClass.getAttributes()) {
            int dodge = FormatUtil
                    .getMiddle(attribute.getDodgeChance().getValue1(), attribute.getDodgeChance().getValue2());
            int damage = FormatUtil.getMiddle(attribute.getDamage().getValue1(), attribute.getDamage().getValue2());
            int goldcoins =
                    FormatUtil.getMiddle(attribute.getGoldCoins().getValue1(), attribute.getGoldCoins().getValue2());
            int protection =
                    FormatUtil.getMiddle(attribute.getProtection().getValue1(), attribute.getProtection().getValue2());
            int exp = FormatUtil.getMiddle(attribute.getExp().getValue1(), attribute.getExp().getValue2());
            if (dodge > 0) bonus += dodge * 20;
            if (damage > 0) bonus += damage * 20;
            if (protection > 0) bonus += protection * 10;
        }
        return bonus;
    }
}