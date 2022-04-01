package me.affluent.decay.pvp;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.entity.Player;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.event.AttackEvent;
import me.affluent.decay.event.FightEndEvent;
import me.affluent.decay.manager.EventManager;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.skill.Skill;
import me.affluent.decay.util.BoostUtil;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.SkillUtil;
import me.affluent.decay.util.settingsUtil.EquippedDragonUtil;
import me.affluent.decay.util.settingsUtil.EquippedIronUtil;
import me.affluent.decay.util.settingsUtil.EquippedWitherUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.weapon.*;

import java.util.List;
import java.util.Random;

public class Fight {

    private final Player attacker;
    private final Player defender;
    private final Weapon attacker_weapon;
    private final Weapon defender_weapon;
    private int attackerGoldCoinsBonus = 0;
    private int attackerExpBonus = 0;
    private int defenderGoldCoinsBonus = 0;
    private int defenderExpBonus = 0;
    private int attackerDamageDealt = 0;
    private int defenderDamageDealt = 0;
    private int attackerHealthLeft = 0;
    private int defenderHealthLeft = 0;

    public Fight(Player attacker, Player defender, Weapon attacker_weapon, Weapon defender_weapon) {
        this.attacker = attacker;
        this.defender = defender;
        this.attacker_weapon = attacker_weapon;
        this.defender_weapon = defender_weapon;
    }

    public int getAttackerDamageDealt() {
        return attackerDamageDealt;
    }

    public int getDefenderDamageDealt() {
        return defenderDamageDealt;
    }

    public int getAttackerHealthLeft() {
        return attackerHealthLeft;
    }

    public int getDefenderHealthLeft() {
        return defenderHealthLeft;
    }

    /**
     * @return Player instance of fight's winner
     */
    public Player doFight(boolean practice, boolean randomFight) {
        int attacker_max_health = attacker.getHealthUser().getMaxHealth();
        int defender_max_health = defender.getHealthUser().getMaxHealth();
        int attacker_health = attacker.getHealthUser().getHealth();
        int defender_health = defender.getHealthUser().getHealth();
        boolean aironHelmetEquipped = false;
        boolean aironChestplateEquipped = false;
        boolean aironGlovesEquipped = false;
        boolean aironTrousersEquipped = false;
        boolean aironBootsEquipped = false;
        boolean aironSwordEquipped = false;
        boolean aironShieldEquipped = false;
        boolean adragonHelmetEquipped = false;
        boolean adragonChestplateEquipped = false;
        boolean adragonGlovesEquipped = false;
        boolean adragonTrousersEquipped = false;
        boolean adragonBootsEquipped = false;
        boolean adragonSwordEquipped = false;
        boolean adragonShieldEquipped = false;
        boolean awitherHelmetEquipped = false;
        boolean awitherChestplateEquipped = false;
        boolean awitherGlovesEquipped = false;
        boolean awitherTrousersEquipped = false;
        boolean awitherBootsEquipped = false;
        boolean awitherSwordEquipped = false;
        boolean awitherShieldEquipped = false;
        boolean dironHelmetEquipped = false;
        boolean dironChestplateEquipped = false;
        boolean dironGlovesEquipped = false;
        boolean dironTrousersEquipped = false;
        boolean dironBootsEquipped = false;
        boolean dironSwordEquipped = false;
        boolean dironShieldEquipped = false;
        boolean ddragonHelmetEquipped = false;
        boolean ddragonChestplateEquipped = false;
        boolean ddragonGlovesEquipped = false;
        boolean ddragonTrousersEquipped = false;
        boolean ddragonBootsEquipped = false;
        boolean ddragonSwordEquipped = false;
        boolean ddragonShieldEquipped = false;
        boolean dwitherHelmetEquipped = false;
        boolean dwitherChestplateEquipped = false;
        boolean dwitherGlovesEquipped = false;
        boolean dwitherTrousersEquipped = false;
        boolean dwitherBootsEquipped = false;
        boolean dwitherSwordEquipped = false;
        boolean dwitherShieldEquipped = false;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronHelmet().equalsIgnoreCase("equipped")) aironHelmetEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronChestplate().equalsIgnoreCase("equipped")) aironChestplateEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronGloves().equalsIgnoreCase("equipped")) aironGlovesEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronTrousers().equalsIgnoreCase("equipped")) aironTrousersEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronBoots().equalsIgnoreCase("equipped")) aironBootsEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronSword().equalsIgnoreCase("equipped")) aironSwordEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronShield().equalsIgnoreCase("equipped")) aironShieldEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonHelmet().equalsIgnoreCase("equipped")) adragonHelmetEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonChestplate().equalsIgnoreCase("equipped")) adragonChestplateEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonGloves().equalsIgnoreCase("equipped")) adragonGlovesEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonTrousers().equalsIgnoreCase("equipped")) adragonTrousersEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonBoots().equalsIgnoreCase("equipped")) adragonBootsEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonSword().equalsIgnoreCase("equipped")) adragonSwordEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonShield().equalsIgnoreCase("equipped")) adragonShieldEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherHelmet().equalsIgnoreCase("equipped")) awitherHelmetEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherChestplate().equalsIgnoreCase("equipped")) awitherChestplateEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherGloves().equalsIgnoreCase("equipped")) awitherGlovesEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherTrousers().equalsIgnoreCase("equipped")) awitherTrousersEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherBoots().equalsIgnoreCase("equipped")) awitherBootsEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherSword().equalsIgnoreCase("equipped")) awitherSwordEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherShield().equalsIgnoreCase("equipped")) awitherShieldEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronHelmet().equalsIgnoreCase("equipped")) dironHelmetEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronChestplate().equalsIgnoreCase("equipped")) dironChestplateEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronGloves().equalsIgnoreCase("equipped")) dironGlovesEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronTrousers().equalsIgnoreCase("equipped")) dironTrousersEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronBoots().equalsIgnoreCase("equipped")) dironBootsEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronSword().equalsIgnoreCase("equipped")) dironSwordEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(defender.getUserId()).getIronShield().equalsIgnoreCase("equipped")) dironShieldEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonHelmet().equalsIgnoreCase("equipped")) ddragonHelmetEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonChestplate().equalsIgnoreCase("equipped")) ddragonChestplateEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonGloves().equalsIgnoreCase("equipped")) ddragonGlovesEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonTrousers().equalsIgnoreCase("equipped")) ddragonTrousersEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonBoots().equalsIgnoreCase("equipped")) ddragonBootsEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonSword().equalsIgnoreCase("equipped")) ddragonSwordEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(defender.getUserId()).getDragonShield().equalsIgnoreCase("equipped")) ddragonShieldEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherHelmet().equalsIgnoreCase("equipped")) dwitherHelmetEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherChestplate().equalsIgnoreCase("equipped")) dwitherChestplateEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherGloves().equalsIgnoreCase("equipped")) dwitherGlovesEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherTrousers().equalsIgnoreCase("equipped")) dwitherTrousersEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherBoots().equalsIgnoreCase("equipped")) dwitherBootsEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherSword().equalsIgnoreCase("equipped")) dwitherSwordEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(defender.getUserId()).getWitherShield().equalsIgnoreCase("equipped")) dwitherShieldEquipped = true;

        int attacker_protection = attacker.getArmorUser().getAllProtection();
        if (attacker.getArmorIronUser().getIronShield() != null && aironShieldEquipped)
            attacker_protection += attacker.getArmorIronUser().getIronShield().getProtection();
        if (attacker.getArmorDragonUser().getDragonShield() != null && adragonShieldEquipped)
            attacker_protection += attacker.getArmorDragonUser().getDragonShield().getProtection();
        if (attacker.getArmorWitherUser().getWitherShield() != null && awitherShieldEquipped)
            attacker_protection += attacker.getArmorWitherUser().getWitherShield().getProtection();
        if (attacker.getPetUser().getPetID() != null)
            attacker_protection += attacker.getPetUser().getFullPetHealth();

        int defender_protection = defender.getArmorUser().getAllProtection();
        if (defender.getArmorIronUser().getIronShield() != null && dironShieldEquipped)
            defender_protection += defender.getArmorIronUser().getIronShield().getProtection();
        if (defender.getArmorDragonUser().getDragonShield() != null && ddragonShieldEquipped)
            defender_protection += defender.getArmorDragonUser().getDragonShield().getProtection();
        if (defender.getArmorWitherUser().getWitherShield() != null && dwitherShieldEquipped)
            defender_protection += defender.getArmorWitherUser().getWitherShield().getProtection();
        if (defender.getPetUser().getPetID() != null)
            defender_protection += defender.getPetUser().getFullPetHealth();

        int attacker_base_damage = attacker.getArmorUser().getWeapon().getDamageMiddle();
        String attacker_weaponID = attacker.getArmorUser().getWeaponID();
        int attacker_weapon_level = ItemLevelingUtil.getItemLevel(attacker.getUserId(), Long.parseLong(attacker_weaponID));
        int attacker_weapon_stars = ItemStarringUtil.getItemStar(attacker.getUserId(), Long.parseLong(attacker_weaponID));
        attacker_base_damage += getLevelProtection(attacker_weapon.getDamageBetween(), attacker_weapon_level);
        attacker_base_damage += getStarsProtection(attacker_weapon.getDamageBetween(), attacker_weapon_stars);


        int defender_base_damage = defender.getArmorUser().getWeapon().getDamageMiddle();
        String defender_weaponID = defender.getArmorUser().getWeaponID();
        int defender_weapon_level = ItemLevelingUtil.getItemLevel(defender.getUserId(), Long.parseLong(defender_weaponID));
        int defender_weapon_stars = ItemStarringUtil.getItemStar(defender.getUserId(), Long.parseLong(defender_weaponID));
        defender_base_damage += getLevelProtection(defender_weapon.getDamageBetween(), defender_weapon_level);
        defender_base_damage += getStarsProtection(defender_weapon.getDamageBetween(), defender_weapon_stars);

        int attacker_damage = attacker_base_damage;
            if (attacker.getArmorIronUser().getIronWeapon() != null && aironSwordEquipped)
                attacker_damage += attacker.getArmorIronUser().getIronWeapon().getDamageBetween();
            if (attacker.getArmorDragonUser().getDragonWeapon() != null && adragonSwordEquipped)
                attacker_damage += attacker.getArmorDragonUser().getDragonWeapon().getDamageBetween();
            if (attacker.getArmorWitherUser().getWitherWeapon() != null && awitherSwordEquipped)
                attacker_damage += attacker.getArmorWitherUser().getWitherWeapon().getDamageBetween();
            if (attacker.getPetUser().getPetID() != null)
                attacker_damage += attacker.getPetUser().getFullPetDamage();

        int defender_damage = defender_base_damage;
            if (defender.getArmorIronUser().getIronWeapon() != null && dironSwordEquipped)
                defender_damage += defender.getArmorIronUser().getIronWeapon().getDamageBetween();
            if (defender.getArmorDragonUser().getDragonWeapon() != null && ddragonSwordEquipped)
                defender_damage += defender.getArmorDragonUser().getDragonWeapon().getDamageBetween();
            if (defender.getArmorWitherUser().getWitherWeapon() != null && dwitherSwordEquipped)
                defender_damage += defender.getArmorWitherUser().getWitherWeapon().getDamageBetween();
            if (defender.getPetUser().getPetID() != null)
                defender_damage += defender.getPetUser().getFullPetDamage();
        if (attacker_weapon instanceof Bow) {
            Arrow attacker_arrow = attacker.getArmorUser().getArrow();
            if (attacker_arrow != null) {
                String attacker_arrowID = attacker.getArmorUser().getArrowID();
                int level = ItemLevelingUtil.getItemLevel(attacker.getUserId(), Long.parseLong(attacker_arrowID));
                int stars = ItemStarringUtil.getItemStar(attacker.getUserId(), Long.parseLong(attacker_arrowID));
                attacker_damage += attacker_arrow.getDamage();
                attacker_damage += getLevelProtection(attacker_arrow.getDamage(), level);
                attacker_damage += getStarsProtection(attacker_arrow.getDamage(), stars);
            }
        }
        if (defender_weapon instanceof Bow) {
            Arrow defender_arrow = defender.getArmorUser().getArrow();
            if (defender_arrow != null) {
                String defender_arrowID = defender.getArmorUser().getArrowID();
                int level = ItemLevelingUtil.getItemLevel(defender.getUserId(), Long.parseLong(defender_arrowID));
                int stars = ItemStarringUtil.getItemStar(defender.getUserId(), Long.parseLong(defender_arrowID));
                defender_damage += defender_arrow.getDamage();
                defender_damage += getLevelProtection(defender_arrow.getDamage(), level);
                defender_damage += getStarsProtection(defender_arrow.getDamage(), stars);
            }
        }
        if (attacker_weapon instanceof Sword) {
            Shield attacker_shield = attacker.getArmorUser().getShield();
            if (attacker_shield != null) {
                String attacker_shieldID = attacker.getArmorUser().getShieldID();
                int level = ItemLevelingUtil.getItemLevel(attacker.getUserId(), Long.parseLong(attacker_shieldID));
                int stars = ItemStarringUtil.getItemStar(attacker.getUserId(), Long.parseLong(attacker_shieldID));
                attacker_protection += attacker_shield.getProtection();
                attacker_protection += getLevelProtection(attacker_shield.getProtection(), level);
                attacker_protection += getStarsProtection(attacker_shield.getProtection(), stars);
            }
        }
        if (defender_weapon instanceof Sword) {
            Shield defender_shield = defender.getArmorUser().getShield();
            if (defender_shield != null) {
                String defender_shieldID = defender.getArmorUser().getShieldID();
                int level = ItemLevelingUtil.getItemLevel(defender.getUserId(), Long.parseLong(defender_shieldID));
                int stars = ItemStarringUtil.getItemStar(defender.getUserId(), Long.parseLong(defender_shieldID));
                defender_protection += defender_shield.getProtection();
                defender_protection += getLevelProtection(defender_shield.getProtection(), level);
                defender_protection += getStarsProtection(defender_shield.getProtection(), stars);
            }
        }
        int turn = 0;
        List<Armor> attackerArmor = attacker.getArmorUser().getAllArmor();
        int attackerFullDodge = 0;
        for (Armor armor : attackerArmor) {
            RarityClass rarityClass = Rarities.getRarityClass(armor.getRarity());
            if (rarityClass == null) {
                System.out.println("[INTERN ERR] Armor rarity is null: " + armor.getName());
                continue;
            }
            for (Attribute attribute : rarityClass.getAttributes()) {
                int dodge = FormatUtil
                        .getBetween(attribute.getDodgeChance().getValue1(), attribute.getDodgeChance().getValue2());
                int health =
                        FormatUtil.getBetween(attribute.getHealth().getValue1(), attribute.getHealth().getValue2());
                int goldcoins = FormatUtil
                        .getBetween(attribute.getGoldCoins().getValue1(), attribute.getGoldCoins().getValue2());
                int protection = FormatUtil
                        .getBetween(attribute.getProtection().getValue1(), attribute.getProtection().getValue2());
                int exp = FormatUtil.getBetween(attribute.getExp().getValue1(), attribute.getExp().getValue2());
                if (dodge > 0) attackerFullDodge += dodge;
                if (health > 0) attacker_health += health;
                if (goldcoins > 0) attackerGoldCoinsBonus += goldcoins;
                if (protection > 0) attacker_protection += protection;
                if (exp > 0) attackerExpBonus += exp;
            }
        }

        IronHelmet aironHelmet = attacker.getArmorIronUser().getIronHelmet();
        IronChestplate aironChestplate = attacker.getArmorIronUser().getIronChestplate();
        IronGloves aironGloves = attacker.getArmorIronUser().getIronGloves();
        IronTrousers aironTrousers = attacker.getArmorIronUser().getIronTrousers();
        IronBoots aironBoots = attacker.getArmorIronUser().getIronBoots();

        DragonHelmet adragonHelmet = attacker.getArmorDragonUser().getDragonHelmet();
        DragonChestplate adragonChestplate = attacker.getArmorDragonUser().getDragonChestplate();
        DragonGloves adragonGloves = attacker.getArmorDragonUser().getDragonGloves();
        DragonTrousers adragonTrousers = attacker.getArmorDragonUser().getDragonTrousers();
        DragonBoots adragonBoots = attacker.getArmorDragonUser().getDragonBoots();

        WitherHelmet awitherHelmet = attacker.getArmorWitherUser().getWitherHelmet();
        WitherChestplate awitherChestplate = attacker.getArmorWitherUser().getWitherChestplate();
        WitherGloves awitherGloves = attacker.getArmorWitherUser().getWitherGloves();
        WitherTrousers awitherTrousers = attacker.getArmorWitherUser().getWitherTrousers();
        WitherBoots awitherBoots = attacker.getArmorWitherUser().getWitherBoots();

        IronHelmet dironHelmet = defender.getArmorIronUser().getIronHelmet();
        IronChestplate dironChestplate = defender.getArmorIronUser().getIronChestplate();
        IronGloves dironGloves = defender.getArmorIronUser().getIronGloves();
        IronTrousers dironTrousers = defender.getArmorIronUser().getIronTrousers();
        IronBoots dironBoots = defender.getArmorIronUser().getIronBoots();

        DragonHelmet ddragonHelmet = defender.getArmorDragonUser().getDragonHelmet();
        DragonChestplate ddragonChestplate = defender.getArmorDragonUser().getDragonChestplate();
        DragonGloves ddragonGloves = defender.getArmorDragonUser().getDragonGloves();
        DragonTrousers ddragonTrousers = defender.getArmorDragonUser().getDragonTrousers();
        DragonBoots ddragonBoots = defender.getArmorDragonUser().getDragonBoots();

        WitherHelmet dwitherHelmet = defender.getArmorWitherUser().getWitherHelmet();
        WitherChestplate dwitherChestplate = defender.getArmorWitherUser().getWitherChestplate();
        WitherGloves dwitherGloves = defender.getArmorWitherUser().getWitherGloves();
        WitherTrousers dwitherTrousers = defender.getArmorWitherUser().getWitherTrousers();
        WitherBoots dwitherBoots = defender.getArmorWitherUser().getWitherBoots();
        
        if (aironHelmetEquipped && aironHelmet != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorIronUser().getIronHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorIronUser().getIronHelmet().getProtectionFrom() + p1iron, attacker.getArmorIronUser().getIronHelmet().getProtectionTo() + p2iron);
        }

        if (aironChestplateEquipped && aironChestplate != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorIronUser().getIronChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorIronUser().getIronChestplate().getProtectionFrom() + p1iron, attacker.getArmorIronUser().getIronChestplate().getProtectionTo() + p2iron);
        }

        if (aironGlovesEquipped && aironGloves != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorIronUser().getIronGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorIronUser().getIronGloves().getProtectionFrom() + p1iron, attacker.getArmorIronUser().getIronGloves().getProtectionTo() + p2iron);
        }

        if (aironTrousersEquipped &&aironTrousers != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorIronUser().getIronTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorIronUser().getIronTrousers().getProtectionFrom() + p1iron, attacker.getArmorIronUser().getIronTrousers().getProtectionTo() + p2iron);
        }

        if (aironBootsEquipped && aironBoots != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorIronUser().getIronBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorIronUser().getIronBoots().getProtectionFrom() + p1iron, attacker.getArmorIronUser().getIronBoots().getProtectionTo() + p2iron);
        }

        if (adragonHelmetEquipped && adragonHelmet != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorDragonUser().getDragonHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorDragonUser().getDragonHelmet().getProtectionFrom() + p1dragon, attacker.getArmorDragonUser().getDragonHelmet().getProtectionTo() + p2dragon);
        }

        if (adragonChestplateEquipped && adragonChestplate != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorDragonUser().getDragonChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorDragonUser().getDragonChestplate().getProtectionFrom() + p1dragon, attacker.getArmorDragonUser().getDragonChestplate().getProtectionTo() + p2dragon);
        }

        if (adragonGlovesEquipped && adragonGloves != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorDragonUser().getDragonGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorDragonUser().getDragonGloves().getProtectionFrom() + p1dragon, attacker.getArmorDragonUser().getDragonGloves().getProtectionTo() + p2dragon);
        }

        if (adragonTrousersEquipped && adragonTrousers != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorDragonUser().getDragonTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorDragonUser().getDragonTrousers().getProtectionFrom() + p1dragon, attacker.getArmorDragonUser().getDragonTrousers().getProtectionTo() + p2dragon);
        }

        if (adragonBootsEquipped && adragonBoots != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorDragonUser().getDragonBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorDragonUser().getDragonBoots().getProtectionFrom() + p1dragon, attacker.getArmorDragonUser().getDragonBoots().getProtectionTo() + p2dragon);
        }

        if (awitherHelmetEquipped && awitherHelmet != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorWitherUser().getWitherHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorWitherUser().getWitherHelmet().getProtectionFrom() + p1wither, attacker.getArmorWitherUser().getWitherHelmet().getProtectionTo() + p2wither);
        }

        if (awitherChestplateEquipped && awitherChestplate != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorWitherUser().getWitherChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorWitherUser().getWitherChestplate().getProtectionFrom() + p1wither, attacker.getArmorWitherUser().getWitherChestplate().getProtectionTo() + p2wither);
        }

        if (awitherGlovesEquipped && awitherGloves != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorWitherUser().getWitherGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorWitherUser().getWitherGloves().getProtectionFrom() + p1wither, attacker.getArmorWitherUser().getWitherGloves().getProtectionTo() + p2wither);
        }

        if (awitherTrousersEquipped && awitherTrousers != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorWitherUser().getWitherTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorWitherUser().getWitherTrousers().getProtectionFrom() + p1wither, attacker.getArmorWitherUser().getWitherTrousers().getProtectionTo() + p2wither);
        }

        if (awitherBootsEquipped && awitherBoots != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(attacker.getArmorWitherUser().getWitherBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(attacker.getArmorWitherUser().getWitherBoots().getProtectionFrom() + p1wither, attacker.getArmorWitherUser().getWitherBoots().getProtectionTo() + p2wither);
        }


        int defenderFullDodge = 0;
        List<Armor> defenderArmor = defender.getArmorUser().getAllArmor();
        for (Armor armor : defenderArmor) {
            RarityClass rarityClass = Rarities.getRarityClass(armor.getRarity());
            for (Attribute attribute : rarityClass.getAttributes()) {
                int dodge = FormatUtil
                        .getBetween(attribute.getDodgeChance().getValue1(), attribute.getDodgeChance().getValue2());
                int health =
                        FormatUtil.getBetween(attribute.getHealth().getValue1(), attribute.getHealth().getValue2());
                int goldcoins = FormatUtil
                        .getBetween(attribute.getGoldCoins().getValue1(), attribute.getGoldCoins().getValue2());
                int protection = FormatUtil
                        .getBetween(attribute.getProtection().getValue1(), attribute.getProtection().getValue2());
                int exp = FormatUtil.getBetween(attribute.getExp().getValue1(), attribute.getExp().getValue2());
                if (dodge > 0) defenderFullDodge += dodge;
                if (health > 0) defender_health += health;
                if (goldcoins > 0) defenderGoldCoinsBonus += goldcoins;
                if (protection > 0) defender_protection += protection;
                if (exp > 0) defenderExpBonus += exp;
            }
        }

        if (dironHelmetEquipped && dironHelmet != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorIronUser().getIronHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorIronUser().getIronHelmet().getProtectionFrom() + p1iron, defender.getArmorIronUser().getIronHelmet().getProtectionTo() + p2iron);
        }

        if (dironChestplateEquipped && dironChestplate != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorIronUser().getIronChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorIronUser().getIronChestplate().getProtectionFrom() + p1iron, defender.getArmorIronUser().getIronChestplate().getProtectionTo() + p2iron);
        }

        if (dironGlovesEquipped && dironGloves != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorIronUser().getIronGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorIronUser().getIronGloves().getProtectionFrom() + p1iron, defender.getArmorIronUser().getIronGloves().getProtectionTo() + p2iron);
        }

        if (dironTrousersEquipped && dironTrousers != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorIronUser().getIronTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorIronUser().getIronTrousers().getProtectionFrom() + p1iron, defender.getArmorIronUser().getIronTrousers().getProtectionTo() + p2iron);
        }

        if (dironBootsEquipped && dironBoots != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorIronUser().getIronBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorIronUser().getIronBoots().getProtectionFrom() + p1iron, defender.getArmorIronUser().getIronBoots().getProtectionTo() + p2iron);
        }

        if (ddragonHelmetEquipped && ddragonHelmet != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorDragonUser().getDragonHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorDragonUser().getDragonHelmet().getProtectionFrom() + p1dragon, defender.getArmorDragonUser().getDragonHelmet().getProtectionTo() + p2dragon);
        }

        if (ddragonChestplateEquipped && ddragonChestplate != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorDragonUser().getDragonChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorDragonUser().getDragonChestplate().getProtectionFrom() + p1dragon, defender.getArmorDragonUser().getDragonChestplate().getProtectionTo() + p2dragon);
        }

        if (ddragonGlovesEquipped && ddragonGloves != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorDragonUser().getDragonGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorDragonUser().getDragonGloves().getProtectionFrom() + p1dragon, defender.getArmorDragonUser().getDragonGloves().getProtectionTo() + p2dragon);
        }

        if (ddragonTrousersEquipped && ddragonTrousers != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorDragonUser().getDragonTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorDragonUser().getDragonTrousers().getProtectionFrom() + p1dragon, defender.getArmorDragonUser().getDragonTrousers().getProtectionTo() + p2dragon);
        }

        if (ddragonBootsEquipped && ddragonBoots != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorDragonUser().getDragonBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorDragonUser().getDragonBoots().getProtectionFrom() + p1dragon, defender.getArmorDragonUser().getDragonBoots().getProtectionTo() + p2dragon);
        }

        if (dwitherHelmetEquipped && dwitherHelmet != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorWitherUser().getWitherHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorWitherUser().getWitherHelmet().getProtectionFrom() + p1wither, defender.getArmorWitherUser().getWitherHelmet().getProtectionTo() + p2wither);
        }

        if (dwitherChestplateEquipped && dwitherChestplate != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorWitherUser().getWitherChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorWitherUser().getWitherChestplate().getProtectionFrom() + p1wither, defender.getArmorWitherUser().getWitherChestplate().getProtectionTo() + p2wither);
        }

        if (dwitherGlovesEquipped && dwitherGloves != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorWitherUser().getWitherGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorWitherUser().getWitherGloves().getProtectionFrom() + p1wither, defender.getArmorWitherUser().getWitherGloves().getProtectionTo() + p2wither);
        }

        if (dwitherTrousersEquipped && dwitherTrousers != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorWitherUser().getWitherTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorWitherUser().getWitherTrousers().getProtectionFrom() + p1wither, defender.getArmorWitherUser().getWitherTrousers().getProtectionTo() + p2wither);
        }

        if (dwitherBootsEquipped && dwitherBoots != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(defender.getArmorWitherUser().getWitherBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            defender_protection += FormatUtil.getBetween(defender.getArmorWitherUser().getWitherBoots().getProtectionFrom() + p1wither, defender.getArmorWitherUser().getWitherBoots().getProtectionTo() + p2wither);
        }

        int attacker_extraDamage =
                round(attacker_damage, getRarityDamage(attacker_weapon.getRarity()));
            if (attacker.getArmorIronUser().getIronWeapon() != null && aironSwordEquipped)
                attacker_extraDamage += round(attacker_damage, getRarityDamage(attacker.getArmorIronUser().getIronWeapon().getRarity()));
            if (attacker.getArmorDragonUser().getDragonWeapon() != null && adragonSwordEquipped)
                attacker_extraDamage += round(attacker_damage, getRarityDamage(attacker.getArmorDragonUser().getDragonWeapon().getRarity()));
            if (attacker.getArmorWitherUser().getWitherWeapon() != null && awitherSwordEquipped)
                attacker_extraDamage += round(attacker_damage, getRarityDamage(attacker.getArmorWitherUser().getWitherWeapon().getRarity()));
            
        int defender_extraDamage =
                round(defender_damage, getRarityDamage(defender_weapon.getRarity()));
            if (defender.getArmorIronUser().getIronWeapon() != null && dironSwordEquipped)
                defender_extraDamage += round(defender_damage, getRarityDamage(defender.getArmorIronUser().getIronWeapon().getRarity()));
            if (defender.getArmorDragonUser().getDragonWeapon() != null && ddragonSwordEquipped)
                defender_extraDamage += round(defender_damage, getRarityDamage(defender.getArmorDragonUser().getDragonWeapon().getRarity()));
            if (defender.getArmorWitherUser().getWitherWeapon() != null && dwitherSwordEquipped)
                defender_extraDamage += round(defender_damage, getRarityDamage(defender.getArmorWitherUser().getWitherWeapon().getRarity()));

        if (attacker_extraDamage < 1) attacker_extraDamage = 1;
        if (defender_extraDamage < 1) defender_extraDamage = 1;
        attacker_damage += attacker_extraDamage;
        defender_damage += defender_extraDamage;
        if (BoostUtil.hasBoost(attacker.getUserId(), "damage_boost")) {
            BoostUtil.Boost boost = BoostUtil.getBoost(attacker.getUserId(), "damage_boost");
            int boostPerc = Integer.parseInt(boost.getValue());
            attacker_damage += (attacker_damage * (boostPerc / 100.0));
        }
        if (BoostUtil.hasBoost(attacker.getUserId(), "protection_boost")) {
            BoostUtil.Boost boost = BoostUtil.getBoost(attacker.getUserId(), "protection_boost");
            int boostPerc = Integer.parseInt(boost.getValue());
            attacker_protection += (attacker_protection * (boostPerc / 100.0));
        }
        //
        if (BoostUtil.hasBoost(defender.getUserId(), "damage_boost")) {
            BoostUtil.Boost boost = BoostUtil.getBoost(defender.getUserId(), "damage_boost");
            int boostPerc = Integer.parseInt(boost.getValue());
            defender_damage += (defender_damage * (boostPerc / 100.0));
        }
        if (BoostUtil.hasBoost(defender.getUserId(), "protection_boost")) {
            BoostUtil.Boost boost = BoostUtil.getBoost(defender.getUserId(), "protection_boost");
            int boostPerc = Integer.parseInt(boost.getValue());
            defender_protection += (defender_protection * (boostPerc / 100.0));
        }
        AttackRate attacker_ar = attacker_weapon.getAttackRate();
        int attacker_mc = attacker_ar.getChanceToMiss();
        AttackRate defender_ar = defender_weapon.getAttackRate();
        int defender_mc = defender_ar.getChanceToMiss();
        if (BoostUtil.hasBoost(attacker.getUserId(), "accuracy_boost")) {
            BoostUtil.Boost boost = BoostUtil.getBoost(attacker.getUserId(), "accuracy_boost");
            double boostVal = Integer.parseInt(boost.getValue()) / 100.0;
            attacker_mc = Double.valueOf(attacker_mc * boostVal).intValue();
        }
        if (BoostUtil.hasBoost(defender.getUserId(), "accuracy_boost")) {
            BoostUtil.Boost boost = BoostUtil.getBoost(defender.getUserId(), "accuracy_boost");
            double boostVal = Integer.parseInt(boost.getValue()) / 100.0;
            defender_mc = Double.valueOf(defender_mc * boostVal).intValue();
        }
        final Random random = new Random();
        boolean firstTurnAttacker = true;
        boolean firstTurnDefender = true;
        while (attacker_health > 0 && defender_health > 0) {
            AttackEvent attackEvent = new AttackEvent(attacker, defender, attacker_health, defender_health,
                    turn == 0 ? defender : attacker, turn == 0 ? defender_damage : attacker_damage,
                    turn == 0 ? defender_damage : attacker_damage);
            EventManager.callEvent(attackEvent);
            attacker_health = attackEvent.getAtthealth();
            defender_health = attackEvent.getDefhealth();
            int dodgeRnd = random.nextInt(100) + 1;
            int nowDodge = turn == 1 ? defenderFullDodge : attackerFullDodge;
            int damageForThisRound = attackEvent.getDamage();
            if (turn == 1 && firstTurnAttacker) {
                nowDodge += getStealth(attacker.getUserId());
                boolean doSkillAction = isAssassin(attacker.getUserId());
                if (doSkillAction) {
                    damageForThisRound = defender_health;
                    nowDodge = 0;
                }
            }
            if (turn == 0 && firstTurnDefender) {
                nowDodge += getStealth(defender.getUserId());
                boolean doSkillAction = isAssassin(defender.getUserId());
                if (doSkillAction) {
                    damageForThisRound = defender_health;
                    nowDodge = 0;
                }
            }
            if (dodgeRnd <= nowDodge) damageForThisRound = 0;
            if (random.nextInt(101) + 1 <= (turn == 1 ? attacker_mc : defender_mc)) damageForThisRound = 0;
            if (damageForThisRound > 0) {
                if (turn == 1) {
                    attackerDamageDealt += damageForThisRound;
                    if (defender_protection > 0) {
                        if (defender_protection < damageForThisRound) {
                            int toDamageHealth = damageForThisRound - defender_protection;
                            defender_protection = 0;
                            defender_health -= toDamageHealth;
                        } else {
                            defender_protection -= damageForThisRound;
                        }
                    } else {
                        defender_health -= damageForThisRound;
                        if (defender_health < 0) defender_health = 0;
                    }
                    if (firstTurnAttacker) firstTurnAttacker = false;
                } else {
                    defenderDamageDealt += damageForThisRound;
                    if (attacker_protection > 0) {
                        if (attacker_protection < damageForThisRound) {
                            int toDamageHealth = damageForThisRound - attacker_protection;
                            attacker_protection = 0;
                            attacker_health -= toDamageHealth;
                        } else {
                            attacker_protection -= damageForThisRound;
                        }
                    } else {
                        attacker_health -= damageForThisRound;
                        if (attacker_health < 0) attacker_health = 0;
                    }
                    if (firstTurnDefender) firstTurnDefender = false;
                }
                turn = turn == 0 ? 1 : 0;
            }
        }
        if (attacker_health < 0) attacker_health = 0;
        if (defender_health < 0) defender_health = 0;
        defenderHealthLeft = defender_health;
        attackerHealthLeft = attacker_health;
        Player winner = attacker_health == 0 ? defender : attacker;
        if (attacker_health > attacker_max_health) attacker_health = attacker_max_health;
        if (defender_health > defender_max_health) defender_health = defender_max_health;
        if (!practice) defender.getHealthUser().setHealth(defender_health);
        if (!practice) attacker.getHealthUser().setHealth(attacker_health);
        FightEndEvent fightEndEvent = new FightEndEvent(attacker, defender, winner, practice, randomFight);
        EventManager.callEvent(fightEndEvent);
        return winner;
    }

    private static int getStealth(String userId) {
        int ssl = SkillUtil.getLevel(userId, 3);
        if (ssl > 0) {
            Skill stealthSkill = Skill.getSkill(3);
            double chance = (double) stealthSkill.getValue(ssl);
            return ((int) (chance / 100.0));
        }
        return 0;
    }

    private static boolean isAssassin(String userId) {
        int asl = SkillUtil.getLevel(userId, 4);
        if (asl > 0) {
            Skill assassinSkill = Skill.getSkill(4);
            double chance = (double) assassinSkill.getValue(asl);
            return new Random().nextInt(10000) < ((int) (chance / 100.0));
        }
        return false;
    }

    public int getAttackerExpBonus() {
        return attackerExpBonus;
    }

    public int getAttackerGoldCoinsBonus() {
        return attackerGoldCoinsBonus;
    }

    public int getDefenderExpBonus() {
        return defenderExpBonus;
    }

    public int getDefenderGoldCoinsBonus() {
        return defenderGoldCoinsBonus;
    }

    public static int round(int value, int multiplier) {
        double newValue = value * (multiplier / 100.0);
        String newValueString = String.valueOf(newValue);
        int beforePoint = Integer.parseInt(newValueString.substring(0, newValueString.indexOf(".")));
        String afterPointString = newValueString.substring(newValueString.indexOf(".") + 1);
        if (afterPointString.length() > 9) afterPointString = afterPointString.substring(0, 9);
        int afterPoint = Integer.parseInt(afterPointString);
        int rounded = beforePoint;
        if (afterPoint > 9) {
            if (afterPoint >= 85) rounded = beforePoint + 1;
        } else {
            if (afterPoint > 8) rounded = beforePoint + 1;
        }
        if (rounded < 1) rounded = 1;
        return rounded;
    }

    public static int getRarityDamage(Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return 10;
            case UNCOMMON:
                return 17;
            case RARE:
                return 27;
            case EPIC:
                return 41;
            case LEGEND:
                return 59;
            case MYTHIC:
                return 80;
            case ANCIENT:
                return 103;
            case ARTIFACT:
                return 130;
            default:
                return 0;
        }
    }

    public static int getRarityProtection(Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return 10;
            case UNCOMMON:
                return 17;
            case RARE:
                return 27;
            case EPIC:
                return 41;
            case LEGEND:
                return 59;
            case MYTHIC:
                return 80;
            case ANCIENT:
                return 103;
            case ARTIFACT:
                return 130;
            default:
                return 0;
        }
    }
    public static long getLevelProtection(int baseProtection, int level) {
        int finalProtection = 0;
        int protection = baseProtection;
        for (int i = 1; i < level + 1; i++) {
            protection *= (1.00725);
        }
        finalProtection = (protection - baseProtection);
        return finalProtection;
    }

    public static long getStarsProtection(int baseProtection, int stars) {
        int finalProtection = 0;
        int protection = baseProtection;
        for (int i = 1; i < stars + 1; i++) {
            protection *= (1.045);
        }
        finalProtection = (protection - baseProtection);
        return finalProtection;
    }
}