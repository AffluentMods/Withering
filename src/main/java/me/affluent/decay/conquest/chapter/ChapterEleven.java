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

public class ChapterEleven extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterEleven() {
        super(11);
        List<Attribute> l1 = l(A.dc(238), A.ha(238), A.da(238), A.pa(1010));
        List<Attribute> l2 = l(A.dc(298), A.ha(298), A.da(298), A.pa(1212));
        List<Attribute> l3 = l(A.dc(358), A.ha(358), A.da(358), A.pa(1414));
        // Quest 1
        Quest q1 = new Quest(1, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 325), l1),
                q(A.c("Ancient Titan Alloy Chestplate", 325), l1), q(A.g("Ancient Titan Alloy Gloves", 325), l1),
                q(A.t("Ancient Titan Alloy Trousers", 325), l1), q(A.b("Ancient Titan Alloy Boots", 325), l1)),
                w(A.s("Ancient Titan Alloy Sword", 215), 1.0));
        // Quest 2
        Quest q2 = new Quest(2, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 340), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 340), l1), q(A.g("Ancient Titan Alloy Gloves", 340), l1),
                q(A.t("Ancient Titan Alloy Trousers", 340), l1), q(A.b("Ancient Titan Alloy Boots", 340), l1)),
                w(A.s("Ancient Titan Alloy Sword", 225), 1.05));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 360), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 360), l1), q(A.g("Ancient Titan Alloy Gloves", 360), l2),
                q(A.t("Ancient Titan Alloy Trousers", 360), l1), q(A.b("Ancient Titan Alloy Boots", 360), l1)),
                w(A.s("Ancient Titan Alloy Sword", 240), 1.1));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 380), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 380), l2), q(A.g("Ancient Titan Alloy Gloves", 380), l2),
                q(A.t("Ancient Titan Alloy Trousers", 380), l1), q(A.b("Ancient Titan Alloy Boots", 380), l1)),
                w(A.s("Ancient Titan Alloy Sword", 255), 1.15));
        // Quest 5
        Quest q5 = new Quest(5, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 405), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 405), l2), q(A.g("Ancient Titan Alloy Gloves", 405), l2),
                q(A.t("Ancient Titan Alloy Trousers", 405), l2), q(A.b("Ancient Titan Alloy Boots", 405), l1)),
                w(A.s("Ancient Titan Alloy Sword", 270), 1.2));
        // Quest 6
        Quest q6 = new Quest(6, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 430), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 430), l2), q(A.g("Ancient Titan Alloy Gloves", 430), l2),
                q(A.t("Ancient Titan Alloy Trousers", 430), l2), q(A.b("Ancient Titan Alloy Boots", 430), l2)),
                w(A.s("Ancient Titan Alloy Sword", 290), 1.25));
        // Quest 7
        Quest q7 = new Quest(7, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 460), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 460), l2), q(A.g("Ancient Titan Alloy Gloves", 460), l2),
                q(A.t("Ancient Titan Alloy Trousers", 460), l2), q(A.b("Ancient Titan Alloy Boots", 460), l2)),
                w(A.s("Ancient Titan Alloy Sword", 310), 1.3));
        // Quest 8
        Quest q8 = new Quest(8, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 490), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 490), l2), q(A.g("Ancient Titan Alloy Gloves", 490), l3),
                q(A.t("Ancient Titan Alloy Trousers", 490), l2), q(A.b("Ancient Titan Alloy Boots", 490), l2)),
                w(A.s("Ancient Titan Alloy Sword", 335), 1.35));
        // Quest 9
        Quest q9 = new Quest(9, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 520), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 520), l3), q(A.g("Ancient Titan Alloy Gloves", 520), l3),
                q(A.t("Ancient Titan Alloy Trousers", 520), l2), q(A.b("Ancient Titan Alloy Boots", 520), l2)),
                w(A.s("Ancient Titan Alloy Sword", 360), 1.4));
        // Quest 10
        Quest q10 = new Quest(10, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 550), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 550), l3), q(A.g("Ancient Titan Alloy Gloves", 550), l3),
                q(A.t("Ancient Titan Alloy Trousers", 550), l3), q(A.b("Ancient Titan Alloy Boots", 550), l3)),
                w(A.s("Ancient Titan Alloy Sword", 390), 1.45));
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
        if (questNumber == 1) return 550;
        if (questNumber == 2) return 555;
        if (questNumber == 3) return 560;
        if (questNumber == 4) return 565;
        if (questNumber == 5) return 570;
        if (questNumber == 6) return 575;
        if (questNumber == 7) return 580;
        if (questNumber == 8) return 585;
        if (questNumber == 9) return 590;
        if (questNumber == 10) return 595;
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