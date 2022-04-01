package me.affluent.decay.conquest;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Bow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Sword;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    private final int questNumber;
    private final List<QuestArmor> armor;
    private final QuestWeapon weapon;
    private final Arrow arrow;
    private final Shield shield;

    public int getQuestNumber() {
        return questNumber;
    }

    public Quest(int questNumber, Sword enemyWeapon) {
        this.questNumber = questNumber;
        this.armor = new ArrayList<>();
        this.weapon = new QuestWeapon(enemyWeapon);
        this.arrow = null;
        this.shield = null;
    }

    public Quest(int qN, List<Armor> enemyArmor, Sword enemyWeapon) {
        this.questNumber = qN;
        this.armor = new ArrayList<>();
        for (Armor armor : enemyArmor) this.armor.add(new QuestArmor(armor, new ArrayList<>()));
        this.weapon = new QuestWeapon(enemyWeapon);
        this.arrow = null;
        this.shield = null;
    }

    public Quest(int questNumber, List<Armor> enemyArmor, Bow enemyWeapon, Arrow bowArrow, Shield swordShield) {
        this.questNumber = questNumber;
        this.armor = new ArrayList<>();
        for (Armor armor : enemyArmor) this.armor.add(new QuestArmor(armor, new ArrayList<>()));
        this.weapon = new QuestWeapon(enemyWeapon);
        this.arrow = bowArrow;
        this.shield = swordShield;
    }

    public Quest(int qN, List<QuestArmor> enemyArmor, QuestWeapon enemyWeapon) {
        this(qN, enemyArmor, enemyWeapon, null, null);
    }

    public Quest(int questNumber, List<QuestArmor> enemyArmor, QuestWeapon enemyWeapon, Arrow bowArrow, Shield swordShield) {
        this.questNumber = questNumber;
        this.armor = enemyArmor;
        this.weapon = enemyWeapon;
        this.arrow = bowArrow;
        this.shield = swordShield;
    }

    public List<QuestArmor> getArmor() {
        return armor;
    }

    public QuestWeapon getWeapon() {
        return weapon;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public Shield getShield() {
        return shield;
    }
}