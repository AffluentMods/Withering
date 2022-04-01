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

public class ChapterNine extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterNine() {
        super(9);
        List<Attribute> l1 = l(A.dc(118), A.ha(118), A.da(118), A.pa(360));
        List<Attribute> l2 = l(A.dc(148), A.ha(148), A.da(148), A.pa(450));
        List<Attribute> l3 = l(A.dc(178), A.ha(178), A.da(178), A.pa(540));
        // Quest 1
        Quest q1 = new Quest(1, Arrays.asList(q(A.h("Mythic Dragon Steel Helmet", 200), l1),
                q(A.c("Mythic Dragon Steel Chestplate", 200), l1), q(A.g("Mythic Dragon Steel Gloves", 200), l1),
                q(A.t("Mythic Dragon Steel Trousers", 200), l1), q(A.b("Mythic Dragon Steel Boots", 200), l1)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.59));
        // Quest 2
        Quest q2 = new Quest(2, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l2),
                q(A.c("Mythic Dragon Steel Chestplate", 200), l1), q(A.g("Mythic Dragon Steel Gloves", 200), l1),
                q(A.t("Mythic Dragon Steel Trousers", 200), l1), q(A.b("Mythic Dragon Steel Boots", 200), l1)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.59));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l2),
                q(A.c("Mythic Dragon Steel Chestplate", 200), l1), q(A.g("Mythic Dragon Steel Gloves", 200), l2),
                q(A.t("Mythic Dragon Steel Trousers", 200), l1), q(A.b("Mythic Dragon Steel Boots", 200), l1)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.59));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l2),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l2), q(A.g("Mythic Dragon Steel Gloves", 200), l2),
                q(A.t("Mythic Dragon Steel Trousers", 200), l1), q(A.b("Mythic Dragon Steel Boots", 200), l1)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.59));
        // Quest 5
        Quest q5 = new Quest(5, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l2),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l2), q(A.g("Mythic Dragon Steel Gloves", 200), l2),
                q(A.t("Mythic Dragon Steel Trousers", 200), l1), q(A.b("Mythic Dragon Steel Boots", 200), l2)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.59));
        // Quest 6
        Quest q6 = new Quest(6, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l2),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l2), q(A.g("Mythic Dragon Steel Gloves", 200), l2),
                q(A.t("Mythic Dragon Steel Trousers", 200), l2), q(A.b("Mythic Dragon Steel Boots", 200), l2)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.59));
        // Quest 7
        Quest q7 = new Quest(7, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l2),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l2), q(A.g("Mythic Dragon Steel Gloves", 200), l2),
                q(A.t("Mythic Dragon Steel Trousers", 200), l2), q(A.b("Mythic Dragon Steel Boots", 200), l2)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.65));
        // Quest 8
        Quest q8 = new Quest(8, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l3),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l2), q(A.g("Ancient Dragon Steel Gloves", 200), l2),
                q(A.t("Mythic Dragon Steel Trousers", 200), l2), q(A.b("Mythic Dragon Steel Boots", 200), l2)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.65));
        // Quest 9
        Quest q9 = new Quest(9, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l3),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l2), q(A.g("Ancient Dragon Steel Gloves", 200), l3),
                q(A.t("Mythic Dragon Steel Trousers", 200), l2), q(A.b("Mythic Dragon Steel Boots", 200), l2)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.65));
        // Quest 10
        Quest q10 = new Quest(10, Arrays.asList(q(A.h("Ancient Dragon Steel Helmet", 200), l3),
                q(A.c("Ancient Dragon Steel Chestplate", 200), l3), q(A.g("Ancient Dragon Steel Gloves", 200), l3),
                q(A.t("Ancient Dragon Steel Trousers", 200), l3), q(A.b("Mythic Dragon Steel Boots", 200), l3)),
                w(A.s("Mythic Dragon Steel Sword", 200), 0.65));
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
        if (questNumber == 1) return 450;
        if (questNumber == 2) return 455;
        if (questNumber == 3) return 460;
        if (questNumber == 4) return 465;
        if (questNumber == 5) return 470;
        if (questNumber == 6) return 475;
        if (questNumber == 7) return 480;
        if (questNumber == 8) return 485;
        if (questNumber == 9) return 490;
        if (questNumber == 10) return 495;
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