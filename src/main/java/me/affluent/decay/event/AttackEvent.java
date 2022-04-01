package me.affluent.decay.event;

import me.affluent.decay.entity.Player;

public class AttackEvent extends Event {

    private final Player attacker;
    private final Player defender;
    private final Player turn;
    private int atthealth;
    private int defhealth;
    private int damage;
    private int base_damage;

    public AttackEvent(Player attacker, Player defender, int attacker_health, int defender_health, Player turn,
                       int base_damage, int damage) {
        this.attacker = attacker;
        this.defender = defender;
        this.atthealth = attacker_health;
        this.defhealth = defender_health;
        this.turn = turn;
        this.base_damage = base_damage;
        this.damage = damage;
    }

    public int getBaseDamage() {
        return base_damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public int getAtthealth() {
        return atthealth;
    }

    public int getDefhealth() {
        return defhealth;
    }

    public void setAtthealth(int atthealth) {
        this.atthealth = atthealth;
    }

    public void setDefhealth(int defhealth) {
        this.defhealth = defhealth;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getDefender() {
        return defender;
    }

    public Player getTurn() {
        return turn;
    }
}