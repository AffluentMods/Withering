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

public class ChapterTwelve extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterTwelve() {
        super(12);
        List<Attribute> l1 = l(A.dc(358), A.ha(358), A.da(358), A.pa(1414));
        List<Attribute> l2 = l(A.dc(450), A.ha(450), A.da(450), A.pa(1765));
        List<Attribute> l3 = l(A.dc(540), A.ha(540), A.da(540), A.pa(2200));
        List<Attribute> l4 = l(A.dc(700), A.ha(700), A.da(700), A.pa(3000));
        // Quest 1
        Quest q1 = new Quest(1, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 575), l1),
                q(A.c("Ancient Titan Alloy Chestplate", 575), l1), q(A.g("Ancient Titan Alloy Gloves", 575), l1),
                q(A.t("Ancient Titan Alloy Trousers", 575), l1), q(A.b("Ancient Titan Alloy Boots", 575), l1)),
                w(A.s("Ancient Titan Alloy Sword", 400), 1.5));
        // Quest 2
        Quest q2 = new Quest(2, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 615), l1),
                q(A.c("Ancient Titan Alloy Chestplate", 615), l1), q(A.g("Ancient Titan Alloy Gloves", 615), l2),
                q(A.t("Ancient Titan Alloy Trousers", 615), l1), q(A.b("Ancient Titan Alloy Boots", 615), l1)),
                w(A.s("Ancient Titan Alloy Sword", 432), 1.55));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 660), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 660), l1), q(A.g("Ancient Titan Alloy Gloves", 660), l2),
                q(A.t("Ancient Titan Alloy Trousers", 660), l1), q(A.b("Ancient Titan Alloy Boots", 660), l2)),
                w(A.s("Ancient Titan Alloy Sword", 432), 1.6));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 715), l2),
                q(A.c("Ancient Titan Alloy Chestplate", 715), l2), q(A.g("Ancient Titan Alloy Gloves", 715), l2),
                q(A.t("Ancient Titan Alloy Trousers", 715), l2), q(A.b("Ancient Titan Alloy Boots", 715), l1)),
                w(A.s("Ancient Titan Alloy Sword", 475), 1.65));
        // Quest 5
        Quest q5 = new Quest(5, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 775), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 775), l2), q(A.g("Ancient Titan Alloy Gloves", 775), l2),
                q(A.t("Ancient Titan Alloy Trousers", 775), l2), q(A.b("Ancient Titan Alloy Boots", 775), l2)),
                w(A.s("Ancient Titan Alloy Sword", 475), 1.7));
        // Quest 6
        Quest q6 = new Quest(6, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 840), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 840), l2), q(A.g("Ancient Titan Alloy Gloves", 840), l3),
                q(A.t("Ancient Titan Alloy Trousers", 840), l2), q(A.b("Ancient Titan Alloy Boots", 840), l2)),
                w(A.s("Ancient Titan Alloy Sword", 515), 1.75));
        // Quest 7
        Quest q7 = new Quest(7, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 920), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 920), l3), q(A.g("Ancient Titan Alloy Gloves", 920), l3),
                q(A.t("Ancient Titan Alloy Trousers", 920), l3), q(A.b("Ancient Titan Alloy Boots", 920), l2)),
                w(A.s("Ancient Titan Alloy Sword", 515), 1.8));
        // Quest 8
        Quest q8 = new Quest(8, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 1000), l3),
                q(A.c("Ancient Titan Alloy Chestplate", 1000), l3), q(A.g("Ancient Titan Alloy Gloves", 1000), l3),
                q(A.t("Ancient Titan Alloy Trousers", 1000), l3), q(A.b("Ancient Titan Alloy Boots", 1000), l3)),
                w(A.s("Ancient Titan Alloy Sword", 570), 1.85));
        // Quest 9
        Quest q9 = new Quest(9, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 1100), l4),
                q(A.c("Ancient Titan Alloy Chestplate", 1100), l3), q(A.g("Ancient Titan Alloy Gloves", 1100), l4),
                q(A.t("Ancient Titan Alloy Trousers", 1100), l3), q(A.b("Ancient Titan Alloy Boots", 1100), l3)),
                w(A.s("Ancient Titan Alloy Sword", 570), 1.9));
        // Quest 10
        Quest q10 = new Quest(10, Arrays.asList(q(A.h("Ancient Titan Alloy Helmet", 1250), l4),
                q(A.c("Ancient Titan Alloy Chestplate", 1250), l4), q(A.g("Ancient Titan Alloy Gloves", 1250), l4),
                q(A.t("Ancient Titan Alloy Trousers", 1250), l4), q(A.b("Ancient Titan Alloy Boots", 1250), l4)),
                w(A.s("Ancient Titan Alloy Sword", 650), 2));
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
        if (questNumber == 1) return 600;
        if (questNumber == 2) return 605;
        if (questNumber == 3) return 610;
        if (questNumber == 4) return 615;
        if (questNumber == 5) return 620;
        if (questNumber == 6) return 625;
        if (questNumber == 7) return 630;
        if (questNumber == 8) return 635;
        if (questNumber == 9) return 640;
        if (questNumber == 10) return 645;
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