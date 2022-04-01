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

public class ChapterTen extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterTen() {
        super(10);
        List<Attribute> l1 = l(A.dc(178), A.ha(178), A.da(178), A.pa(540));
        List<Attribute> l2 = l(A.dc(193), A.ha(193), A.da(193), A.pa(675));
        List<Attribute> l3 = l(A.dc(238), A.ha(238), A.da(238), A.pa(810));
        // Quest 1
        Quest q1 = new Quest(1, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 210), l1),
                q(A.c("Ancient Titan Alloy Chestplate", 210), l1), q(A.g("Ancient Titan Alloy Gloves", 210), l1),
                q(A.t("Ancient Titan Alloy Trousers", 210), l1), q(A.b("Ancient Titan Alloy Boots", 210), l1)),
                w(A.s("Ancient Titan Alloy Sword", 110), 0.70));
        // Quest 2
        Quest q2 = new Quest(2, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 215), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 215), l1), q(A.g("Ancient Titan Alloy Gloves", 215), l1),
                q(A.t("Ancient Titan Alloy Trousers", 215), l1), q(A.b("Ancient Titan Alloy Boots", 215), l1)),
                w(A.s("Ancient Titan Alloy Sword", 120), 0.70));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 220), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 220), l1), q(A.g("Ancient Titan Alloy Gloves", 220), l2),
                q(A.t("Ancient Titan Alloy Trousers", 220), l1), q(A.b("Ancient Titan Alloy Boots", 220), l1)),
                w(A.s("Ancient Titan Alloy Sword", 130), 0.70));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 225), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 225), l2), q(A.g("Ancient Titan Alloy Gloves", 225), l2),
                q(A.t("Ancient Titan Alloy Trousers", 225), l1), q(A.b("Ancient Titan Alloy Boots", 225), l1)),
                w(A.s("Ancient Titan Alloy Sword", 140), 0.70));
        // Quest 5
        Quest q5 = new Quest(5, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 230), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 230), l2), q(A.g("Ancient Titan Alloy Gloves", 230), l2),
                q(A.t("Ancient Titan Alloy Trousers", 230), l2), q(A.b("Ancient Titan Alloy Boots", 230), l1)),
                w(A.s("Ancient Titan Alloy Sword", 150), 0.80));
        // Quest 6
        Quest q6 = new Quest(6, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 235), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 235), l2), q(A.g("Ancient Titan Alloy Gloves", 235), l2),
                q(A.t("Ancient Titan Alloy Trousers", 235), l2), q(A.b("Ancient Titan Alloy Boots", 235), l2)),
                w(A.s("Ancient Titan Alloy Sword", 160), 0.80));
        // Quest 7
        Quest q7 = new Quest(7, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 240), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 240), l2), q(A.g("Ancient Titan Alloy Gloves", 240), l2),
                q(A.t("Ancient Titan Alloy Trousers", 240), l2), q(A.b("Ancient Titan Alloy Boots", 240), l2)),
                w(A.s("Ancient Titan Alloy Sword", 170), 0.80));
        // Quest 8
        Quest q8 = new Quest(8, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 245), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 245), l2), q(A.g("Ancient Titan Alloy Gloves", 245), l3),
                q(A.t("Ancient Titan Alloy Trousers", 245), l2), q(A.b("Ancient Titan Alloy Boots", 245), l2)),
                w(A.s("Ancient Titan Alloy Sword", 180), 0.80));
        // Quest 9
        Quest q9 = new Quest(9, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 250), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 250), l3), q(A.g("Ancient Titan Alloy Gloves", 250), l3),
                q(A.t("Ancient Titan Alloy Trousers", 250), l2), q(A.b("Ancient Titan Alloy Boots", 250), l2)),
                w(A.s("Ancient Titan Alloy Sword", 190), 0.80));
        // Quest 10
        Quest q10 = new Quest(10, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 300), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 300), l3), q(A.g("Ancient Titan Alloy Gloves", 300), l3),
                q(A.t("Ancient Titan Alloy Trousers", 300), l3), q(A.b("Ancient Titan Alloy Boots", 300), l3)),
                w(A.s("Ancient Titan Alloy Sword", 200), 1.0));
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
        if (questNumber == 1) return 500;
        if (questNumber == 2) return 505;
        if (questNumber == 3) return 510;
        if (questNumber == 4) return 515;
        if (questNumber == 5) return 520;
        if (questNumber == 6) return 525;
        if (questNumber == 7) return 530;
        if (questNumber == 8) return 535;
        if (questNumber == 9) return 540;
        if (questNumber == 10) return 545;
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