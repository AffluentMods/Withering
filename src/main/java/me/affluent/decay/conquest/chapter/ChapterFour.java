package me.affluent.decay.conquest.chapter;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.conquest.Quest;
import me.affluent.decay.conquest.QuestArmor;
import me.affluent.decay.conquest.QuestWeapon;
import me.affluent.decay.util.A;
import me.affluent.decay.weapon.Weapon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChapterFour extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterFour() {
        super(4);
        // Quest 1
        Quest q1 = new Quest(1,
                Arrays.asList(q(A.h("Mythic Iron Helmet", 58), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Iron Chestplate", 57), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Titanium Gloves", 57), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Titanium Trousers", 58), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Titanium Boots", 58), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Iron Sword", 63), 0.59));
        // Quest 2
        Quest q2 = new Quest(2,
                Arrays.asList(q(A.h("Mythic Iron Helmet", 60), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Iron Chestplate", 60), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Iron Gloves", 60), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Iron Trousers", 60), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Titanium Boots", 62), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Iron Sword", 67), 0.59));
        // Quest 3
        Quest q3 = new Quest(3,
                Arrays.asList(q(A.h("Mythic Iron Helmet", 65), l(A.dc(12), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Iron Chestplate", 65), l(A.dc(12), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Iron Gloves", 64), l(A.dc(12), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Iron Trousers", 63), l(A.dc(12), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Iron Boots", 63), l(A.dc(12), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Iron Sword", 69), 0.59));
        // Quest 4
        Quest q4 = new Quest(4,
                Arrays.asList(q(A.h("Mythic Iron Helmet", 67), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Iron Chestplate", 68), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Iron Gloves", 68), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Iron Trousers", 69), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Iron Boots", 67), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Iron Sword", 73), 0.59));
        // Quest 5
        Quest q5 = new Quest(5,
                Arrays.asList(q(A.h("Mythic Steel Helmet", 74), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Iron Chestplate", 73), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Iron Gloves", 73), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Iron Trousers", 72), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Iron Boots", 74), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Iron Sword", 78), 0.59));
        // Quest 6
        Quest q6 = new Quest(6,
                Arrays.asList(q(A.h("Mythic Steel Helmet", 74), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Steel Chestplate", 74), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Iron Gloves", 78), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Iron Trousers", 77), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Iron Boots", 77), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Iron Sword", 82), 0.59));
        // Quest 7
        Quest q7 = new Quest(7,
                Arrays.asList(q(A.h("Mythic Steel Helmet", 80), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Steel Chestplate", 80), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Steel Gloves", 80), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Iron Trousers", 81), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Iron Boots", 79), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Iron Sword", 86), 0.59));
        // Quest 8
        Quest q8 = new Quest(8,
                Arrays.asList(q(A.h("Mythic Steel Helmet", 86), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Steel Chestplate", 85), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Steel Gloves", 86), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Steel Trousers", 85), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Iron Boots", 84), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Iron Sword", 90), 0.59));
        // Quest 9
        Quest q9 = new Quest(9,
                Arrays.asList(q(A.h("Mythic Steel Helmet", 90), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Steel Chestplate", 90), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Steel Gloves", 90), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Steel Trousers", 89), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Steel Boots", 91), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Iron Sword", 95), 0.59));
        // Quest 10
        Quest q10 = new Quest(10,
                Arrays.asList(q(A.h("Mythic Steel Helmet", 96), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Steel Chestplate", 98), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Steel Gloves", 99), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Steel Trousers", 97), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Steel Boots", 99), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Steel Sword", 110), 0.59));
        //
        this.questList = new HashMap<>();
        List<Quest> questList1 = Arrays.asList(q1, q2, q3, q4, q5, q6, q7, q8, q9, q10);
        for (Quest q : questList1) {
            int qn = q.getQuestNumber();
            this.questList.put(qn, q);
        }
    }

    @Override
    public HashMap<Integer, Quest> getQuestList() {
        return questList;
    }

    @Override
    public Quest getQuest(int questNumber) {
        return questList.get(questNumber);
    }

    @Override
    public int getDiamondsReward(int questNumber) {
        if (questNumber == 1) return 200;
        if (questNumber == 2) return 205;
        if (questNumber == 3) return 210;
        if (questNumber == 4) return 215;
        if (questNumber == 5) return 220;
        if (questNumber == 6) return 225;
        if (questNumber == 7) return 230;
        if (questNumber == 8) return 235;
        if (questNumber == 9) return 240;
        if (questNumber == 10) return 245;
        return 0;
    }

    private List<Attribute> l(Attribute... aa) {
        return Arrays.asList(aa);
    }

    private QuestWeapon w(Weapon weapon, double damageMultiplier) {
        return new QuestWeapon(weapon, damageMultiplier);
    }

    private QuestArmor q(Armor armor, List<Attribute> attributes) {
        return new QuestArmor(armor, attributes);
    }
}