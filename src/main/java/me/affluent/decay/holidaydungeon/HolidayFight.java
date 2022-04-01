package me.affluent.decay.holidaydungeon;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.armor.dragon.*;
import me.affluent.decay.armor.iron.*;
import me.affluent.decay.armor.wither.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.otherInventory.ArmorDragonUser;
import me.affluent.decay.entity.otherInventory.ArmorIronUser;
import me.affluent.decay.entity.otherInventory.ArmorWitherUser;
import me.affluent.decay.enums.Rarity;
import me.affluent.decay.pvp.AttackRate;
import me.affluent.decay.pvp.Fight;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.rarity.RarityClass;
import me.affluent.decay.util.itemUtil.ItemLevelingUtil;
import me.affluent.decay.util.itemUtil.ItemStarringUtil;
import me.affluent.decay.util.settingsUtil.EquippedDragonUtil;
import me.affluent.decay.util.settingsUtil.EquippedIronUtil;
import me.affluent.decay.util.settingsUtil.EquippedWitherUtil;
import me.affluent.decay.util.system.FormatUtil;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;
import me.affluent.decay.weapon.dragon.DragonShield;
import me.affluent.decay.weapon.dragon.DragonWeapon;
import me.affluent.decay.weapon.iron.IronShield;
import me.affluent.decay.weapon.iron.IronWeapon;
import me.affluent.decay.weapon.wither.WitherShield;
import me.affluent.decay.weapon.wither.WitherWeapon;

import java.util.List;
import java.util.Random;

public class HolidayFight {

    private final Player attacker;
    private final int stageNumber;

    private int attacker_health = 0;
    private int enemy_health = 0;
    private int player_damage = 0;
    private int enemy_damage = 0;

    public HolidayFight(Player attacker, int stageNumber) {
        this.attacker = attacker;
        this.stageNumber = stageNumber;
    }

    public int getPlayerDamage() {
        return player_damage;
    }

    public int getPlayerHealth() {
        return attacker_health;
    }

    public int getEnemyHealth() {
        return enemy_health;
    }

    public int getEnemyDamage() {
        return enemy_damage;
    }

    public boolean doFight() {
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
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronHelmet().equalsIgnoreCase("equipped")) ironHelmetEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronChestplate().equalsIgnoreCase("equipped")) ironChestplateEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronGloves().equalsIgnoreCase("equipped")) ironGlovesEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronTrousers().equalsIgnoreCase("equipped")) ironTrousersEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronBoots().equalsIgnoreCase("equipped")) ironBootsEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronSword().equalsIgnoreCase("equipped")) ironSwordEquipped = true;
        if (EquippedIronUtil.getEquippedIronUtil(attacker.getUserId()).getIronShield().equalsIgnoreCase("equipped")) ironShieldEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonHelmet().equalsIgnoreCase("equipped")) dragonHelmetEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonChestplate().equalsIgnoreCase("equipped")) dragonChestplateEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonGloves().equalsIgnoreCase("equipped")) dragonGlovesEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonTrousers().equalsIgnoreCase("equipped")) dragonTrousersEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonBoots().equalsIgnoreCase("equipped")) dragonBootsEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonSword().equalsIgnoreCase("equipped")) dragonSwordEquipped = true;
        if (EquippedDragonUtil.getEquippedDragonUtil(attacker.getUserId()).getDragonShield().equalsIgnoreCase("equipped")) dragonShieldEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherHelmet().equalsIgnoreCase("equipped")) witherHelmetEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherChestplate().equalsIgnoreCase("equipped")) witherChestplateEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherGloves().equalsIgnoreCase("equipped")) witherGlovesEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherTrousers().equalsIgnoreCase("equipped")) witherTrousersEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherBoots().equalsIgnoreCase("equipped")) witherBootsEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherSword().equalsIgnoreCase("equipped")) witherSwordEquipped = true;
        if (EquippedWitherUtil.getEquippedWitherUtil(attacker.getUserId()).getWitherShield().equalsIgnoreCase("equipped")) witherShieldEquipped = true;
        Weapon attacker_weapon = attacker.getArmorUser().getWeapon();
        String attacker_weaponID = attacker.getArmorUser().getWeaponID();
        Arrow arrow = attacker.getArmorUser().getArrow();
        String attacker_arrowID = attacker.getArmorUser().getArrowID();
        IronWeapon attacker_ironWeapon = attacker.getArmorIronUser().getIronWeapon();
        DragonWeapon attacker_dragonWeapon = attacker.getArmorDragonUser().getDragonWeapon();
        WitherWeapon attacker_witherWeapon = attacker.getArmorWitherUser().getWitherWeapon();
        Shield shield = attacker.getArmorUser().getShield();
        String attacker_shieldID = attacker.getArmorUser().getShieldID();
        IronShield ironShield = attacker.getArmorIronUser().getIronShield();
        WitherShield witherShield = attacker.getArmorWitherUser().getWitherShield();
        DragonShield dragonShield = attacker.getArmorDragonUser().getDragonShield();
        
        IronHelmet ironHelmet = attacker.getArmorIronUser().getIronHelmet();
        IronChestplate ironChestplate = attacker.getArmorIronUser().getIronChestplate();
        IronGloves ironGloves = attacker.getArmorIronUser().getIronGloves();
        IronTrousers ironTrousers = attacker.getArmorIronUser().getIronTrousers();
        IronBoots ironBoots = attacker.getArmorIronUser().getIronBoots();

        DragonHelmet dragonHelmet = attacker.getArmorDragonUser().getDragonHelmet();
        DragonChestplate dragonChestplate = attacker.getArmorDragonUser().getDragonChestplate();
        DragonGloves dragonGloves = attacker.getArmorDragonUser().getDragonGloves();
        DragonTrousers dragonTrousers = attacker.getArmorDragonUser().getDragonTrousers();
        DragonBoots dragonBoots = attacker.getArmorDragonUser().getDragonBoots();

        WitherHelmet witherHelmet = attacker.getArmorWitherUser().getWitherHelmet();
        WitherChestplate witherChestplate = attacker.getArmorWitherUser().getWitherChestplate();
        WitherGloves witherGloves = attacker.getArmorWitherUser().getWitherGloves();
        WitherTrousers witherTrousers = attacker.getArmorWitherUser().getWitherTrousers();
        WitherBoots witherBoots = attacker.getArmorWitherUser().getWitherBoots();

        int wap = 0;
        int wapiron = 0;
        int wapdragon = 0;
        int wapwither = 0;
        int arrowDmg = 0;

        int sp = 0;
        if (shield != null) {
            int level = ItemLevelingUtil.getItemLevel(attacker.getUserId(), Long.parseLong(attacker_shieldID));
            int stars = ItemStarringUtil.getItemStar(attacker.getUserId(), Long.parseLong(attacker_shieldID));
            sp = shield.getProtection();
            sp += getLevelProtection(shield.getProtection(), level);
            sp += getStarsProtection(shield.getProtection(), stars);
            wap = shield.getRarityProtection();
        }
        int spiron = 0;
        if (ironShield != null && ironShieldEquipped) {
            spiron = ironShield.getProtection();
            wapiron = ironShield.getRarityProtection();
        }
        int spdragon = 0;
        if (dragonShield != null && dragonShieldEquipped) {
            spdragon = dragonShield.getProtection();
            wapdragon = dragonShield.getRarityProtection();
        }
        int spwither = 0;
        if (witherShield != null && witherShieldEquipped) {
            spwither = witherShield.getProtection();
            wapwither = witherShield.getRarityProtection();
        }
        if (arrow != null) {
            int level = ItemLevelingUtil.getItemLevel(attacker.getUserId(), Long.parseLong(attacker_arrowID));
            int stars = ItemStarringUtil.getItemStar(attacker.getUserId(), Long.parseLong(attacker_arrowID));
            arrowDmg = arrow.getDamage();
            arrowDmg += getLevelProtection(arrow.getDamage(), level);
            arrowDmg += getStarsProtection(arrow.getDamage(), stars);
        }

        attacker_health = attacker.getHealthUser().getHealth();
        int attacker_protection = attacker.getArmorUser().getAllProtection() +
                sp + wap +
                spiron + wapiron +
                spdragon + wapdragon +
                spwither + wapwither;
        if (attacker.getPetUser().getPetID() != null)
            attacker_protection += attacker.getPetUser().getFullPetHealth();

        int attacker_damage = ArmorUser.getArmorUser(attacker.getUserId()).getWeapon().getDamageBetween() + arrowDmg;
        int attacker_weapon_level = ItemLevelingUtil.getItemLevel(attacker.getUserId(), Long.parseLong(attacker_weaponID));
        int attacker_weapon_stars = ItemStarringUtil.getItemStar(attacker.getUserId(), Long.parseLong(attacker_weaponID));
        attacker_damage += getLevelProtection(attacker_weapon.getDamageBetween(), attacker_weapon_level);
        attacker_damage += getStarsProtection(attacker_weapon.getDamageBetween(), attacker_weapon_stars);
        if (attacker.getArmorIronUser().getIronWeapon() != null && ironSwordEquipped)
            attacker_damage += attacker.getArmorIronUser().getIronWeapon().getDamageBetween();
        if (attacker.getArmorDragonUser().getDragonWeapon() != null && dragonSwordEquipped)
            attacker_damage += attacker.getArmorDragonUser().getDragonWeapon().getDamageBetween();
        if (attacker.getArmorWitherUser().getWitherWeapon() != null && witherSwordEquipped)
            attacker_damage += attacker.getArmorWitherUser().getWitherWeapon().getDamageBetween();
        if (attacker.getPetUser().getPetID() != null)
            attacker_damage += attacker.getPetUser().getFullPetDamage();

        //
        enemy_health = 50;
        enemy_damage = 30;
        int stage = stageNumber;
        enemy_damage += ((117 * stage) / 100);
        enemy_health += ((117 * stage) / 100);
        //
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
                int damage =
                        FormatUtil.getBetween(attribute.getDamage().getValue1(), attribute.getDamage().getValue2());
                int protection = FormatUtil
                        .getBetween(attribute.getProtection().getValue1(), attribute.getProtection().getValue2());
                if (dodge > 0) attackerFullDodge += dodge;
                if (health > 0) attacker_health += health;
                if (damage > 0) attacker_damage += damage;
                if (protection > 0) attacker_protection += protection;
            }
        }

        ArmorIronUser aiu = attacker.getArmorIronUser();
        ArmorDragonUser adu = attacker.getArmorDragonUser();
        ArmorWitherUser awu = attacker.getArmorWitherUser();

        if (ironHelmetEquipped && ironHelmet != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(aiu.getIronHelmet().getProtectionFrom() + p1iron, aiu.getIronHelmet().getProtectionTo() + p2iron);
        }

        if (ironChestplateEquipped && ironChestplate != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(aiu.getIronChestplate().getProtectionFrom() + p1iron, aiu.getIronChestplate().getProtectionTo() + p2iron);
        }

        if (ironGlovesEquipped && ironGloves != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(aiu.getIronGloves().getProtectionFrom() + p1iron, aiu.getIronGloves().getProtectionTo() + p2iron);
        }

        if (ironTrousersEquipped && ironTrousers != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(aiu.getIronTrousers().getProtectionFrom() + p1iron, aiu.getIronTrousers().getProtectionTo() + p2iron);
        }

        if (ironBootsEquipped && ironBoots != null) {
            int p1iron = 0;
            int p2iron = 0;
            RarityClass rc = Rarities.getRarityClass(aiu.getIronBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1iron = attribute.getProtection().getValue1();
                    p2iron = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(aiu.getIronBoots().getProtectionFrom() + p1iron, aiu.getIronBoots().getProtectionTo() + p2iron);
        }

        if (dragonHelmetEquipped && dragonHelmet != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(adu.getDragonHelmet().getProtectionFrom() + p1dragon, adu.getDragonHelmet().getProtectionTo() + p2dragon);
        }

        if (dragonChestplateEquipped && dragonChestplate != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(adu.getDragonChestplate().getProtectionFrom() + p1dragon, adu.getDragonChestplate().getProtectionTo() + p2dragon);
        }

        if (dragonGlovesEquipped && dragonGloves != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(adu.getDragonGloves().getProtectionFrom() + p1dragon, adu.getDragonGloves().getProtectionTo() + p2dragon);
        }

        if (dragonTrousersEquipped && dragonTrousers != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(adu.getDragonTrousers().getProtectionFrom() + p1dragon, adu.getDragonTrousers().getProtectionTo() + p2dragon);
        }

        if (dragonBootsEquipped && dragonBoots != null) {
            int p1dragon = 0;
            int p2dragon = 0;
            RarityClass rc = Rarities.getRarityClass(adu.getDragonBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1dragon = attribute.getProtection().getValue1();
                    p2dragon = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(adu.getDragonBoots().getProtectionFrom() + p1dragon, adu.getDragonBoots().getProtectionTo() + p2dragon);
        }

        if (witherHelmetEquipped && witherHelmet != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherHelmet().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(awu.getWitherHelmet().getProtectionFrom() + p1wither, awu.getWitherHelmet().getProtectionTo() + p2wither);
        }

        if (witherChestplateEquipped && witherChestplate != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherChestplate().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(awu.getWitherChestplate().getProtectionFrom() + p1wither, awu.getWitherChestplate().getProtectionTo() + p2wither);
        }

        if (witherGlovesEquipped && witherGloves != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherGloves().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(awu.getWitherGloves().getProtectionFrom() + p1wither, awu.getWitherGloves().getProtectionTo() + p2wither);
        }

        if (witherTrousersEquipped && witherTrousers != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherTrousers().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(awu.getWitherTrousers().getProtectionFrom() + p1wither, awu.getWitherTrousers().getProtectionTo() + p2wither);
        }

        if (witherBootsEquipped && witherBoots != null) {
            int p1wither = 0;
            int p2wither = 0;
            RarityClass rc = Rarities.getRarityClass(awu.getWitherBoots().getRarity());
            if (rc != null) {
                List<Attribute> attributeList = rc.getAttributes();
                for (Attribute attribute : attributeList) {
                    p1wither = attribute.getProtection().getValue1();
                    p2wither = attribute.getProtection().getValue2();
                }
            }
            attacker_protection += FormatUtil.getBetween(awu.getWitherBoots().getProtectionFrom() + p1wither, awu.getWitherBoots().getProtectionTo() + p2wither);
        }

        attacker_damage += round(attacker_damage, getRarityDamage(attacker_weapon.getRarity()));
        if (attacker_ironWeapon != null) {
            attacker_damage += round(attacker_damage, getRarityDamage(attacker_ironWeapon.getRarity()));
        }
        if (attacker_dragonWeapon != null) {
            attacker_damage += round(attacker_damage, getRarityDamage(attacker_dragonWeapon.getRarity()));
        }
        if (attacker_witherWeapon != null) {
            attacker_damage += round(attacker_damage, getRarityDamage(attacker_witherWeapon.getRarity()));
        }
        AttackRate attacker_ar = attacker_weapon.getAttackRate();
        int attacker_mc = attacker_ar.getChanceToMiss();
        AttackRate enemy_ar = AttackRate.DECENT;
        int enemy_mc = enemy_ar.getChanceToMiss();
        int enemyFullDodge = 22;
        int enemy_protection = (enemy_health / 2);

        for (Attribute attribute : Rarities.getRarityClass(attacker_weapon.getRarity()).getAttributes()) {
            int damage = FormatUtil.getBetween(attribute.getDamage().getValue1(), attribute.getDamage().getValue2());
            if (damage > 0) attacker_damage += damage;
        }
        int turn = 0;
        final Random random = new Random();
        while (attacker_health > 0 && enemy_health > 0) {
            int damageForThisRound = turn == 1 ? attacker_damage : enemy_damage;
            int dodgeRnd = random.nextInt(100) + 1;
            int nowDodge = turn == 1 ? enemyFullDodge : attackerFullDodge;
            if (dodgeRnd <= nowDodge) damageForThisRound = 0;
            if (random.nextInt(101) + 1 <= (turn == 1 ? attacker_mc : enemy_mc)) damageForThisRound = 0;
            if (damageForThisRound > 0) {
                if (turn == 1) {
                    if (enemy_protection > 0) {
                        if (enemy_protection < damageForThisRound) {
                            int toDamageHealth = damageForThisRound - enemy_protection;
                            enemy_protection = 0;
                            enemy_health -= toDamageHealth;
                            player_damage += toDamageHealth;
                        } else {
                            enemy_protection -= damageForThisRound;
                            player_damage += damageForThisRound;
                        }
                    } else {
                        if (damageForThisRound > enemy_health) damageForThisRound = enemy_health;
                        enemy_health -= damageForThisRound;
                        if (enemy_health < 0) enemy_health = 0;
                        player_damage += damageForThisRound;
                    }
                } else {
                    if (attacker_protection > 0) {
                        if (attacker_protection < damageForThisRound) {
                            int toDamageHealth = damageForThisRound - attacker_protection;
                            attacker_protection = 0;
                            attacker_health -= toDamageHealth;
                            enemy_damage += toDamageHealth;
                        } else {
                            attacker_protection -= damageForThisRound;
                            enemy_damage += damageForThisRound;
                        }
                    } else {
                        if (damageForThisRound > attacker_health) damageForThisRound = attacker_health;
                        attacker_health -= damageForThisRound;
                        if (attacker_health < 0) attacker_health = 0;
                        enemy_damage += damageForThisRound;
                    }
                }
                turn = turn == 0 ? 1 : 0;
            }
        }
        if (attacker_health < 0) attacker_health = 0;
        if (enemy_health < 0) enemy_health = 0;
        boolean win = enemy_health <= 0;
        if (attacker_health > attacker.getHealthUser().getMaxHealth()) attacker_health = attacker.getHealthUser().getMaxHealth();
        if (attacker_health < 0) attacker.getHealthUser().setHealth(0);
        attacker.getHealthUser().setHealth(attacker_health);
        return win;
    }
    private static int round(int damage, int multiplier) {
        return Fight.round(damage, multiplier);
    }

    private int getRarityDamage(Rarity rarity) {
        switch (rarity) {
            case UNCOMMON:
                return 5;
            case RARE:
                return 8;
            case EPIC:
                return 14;
            case LEGEND:
                return 23;
            case MYTHIC:
                return 35;
            case ANCIENT:
                return 50;
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
