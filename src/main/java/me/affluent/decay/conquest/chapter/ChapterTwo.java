package me.affluent.decay.conquest.chapter;

import me.affluent.decay.armor.Armor;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.conquest.Quest;
import me.affluent.decay.conquest.QuestArmor;
import me.affluent.decay.conquest.QuestWeapon;
import me.affluent.decay.util.A;
import me.affluent.decay.weapon.Weapon;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ChapterTwo extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterTwo() {
        super(2);
        // Quest 1
        Quest q1 = new Quest(1, Arrays.asList(q(A.h("Rare Copper Helmet", 9), l(A.dc(2), A.ha(4))),
                q(A.c("Rare Copper Chestplate", 8), l(A.dc(2), A.ha(4))),
                q(A.g("Rare Copper Gloves", 7), l(A.dc(2), A.ha(4))),
                q(A.t("Rare Wood Trousers", 7), l(A.dc(2), A.ha(4))),
                q(A.b("Rare Wood Boots", 7), l(A.dc(2), A.ha(4)))), w(A.s("Rare Wood Sword", 7), 0.08));
        // Quest 2
        Quest q2 = new Quest(2, Arrays.asList(q(A.h("Rare Copper Helmet", 9), l(A.dc(2), A.ha(4))),
                q(A.c("Rare Copper Chestplate", 9), l(A.dc(2), A.ha(4))),
                q(A.g("Rare Copper Gloves", 8), l(A.dc(2), A.ha(4))),
                q(A.t("Rare Copper Trousers", 8), l(A.dc(2), A.ha(4))),
                q(A.b("Rare Copper Boots", 8), l(A.dc(2), A.ha(4)))), w(A.s("Rare Copper Sword", 8), 0.08));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(q(A.h("Rare Copper Helmet", 9), l(A.dc(3), A.ha(4))),
                q(A.c("Rare Copper Chestplate", 9), l(A.dc(3), A.ha(4))),
                q(A.g("Rare Copper Gloves", 9), l(A.dc(3), A.ha(4))),
                q(A.t("Rare Copper Trousers", 9), l(A.dc(3), A.ha(4))),
                q(A.b("Rare Copper Boots", 9), l(A.dc(3), A.ha(4)))), w(A.s("Rare Copper Sword", 9), 0.08));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(q(A.h("Epic Copper Helmet", 9), l(A.dc(3), A.ha(7), A.da(6))),
                q(A.c("Epic Copper Chestplate", 9), l(A.dc(3), A.ha(6), A.da(6))),
                q(A.g("Epic Copper Gloves", 9), l(A.dc(3), A.ha(5), A.da(5))),
                q(A.t("Epic Copper Trousers", 9), l(A.dc(3), A.ha(5), A.da(4))),
                q(A.b("Rare Copper Boots", 9), l(A.dc(3), A.ha(4)))), w(A.s("Rare Copper Sword", 10), 0.08));
        // Quest 5
        Quest q5 = new Quest(5, Arrays.asList(q(A.h("Epic Copper Helmet", 11), l(A.dc(4), A.ha(8), A.da(6))),
                q(A.c("Epic Copper Chestplate", 10), l(A.dc(4), A.ha(7), A.da(6))),
                q(A.g("Epic Copper Gloves", 10), l(A.dc(3), A.ha(7), A.da(6))),
                q(A.t("Epic Copper Trousers", 11), l(A.dc(3), A.ha(6), A.da(6))),
                q(A.b("Epic Copper Boots", 11), l(A.dc(3), A.ha(6), A.da(6)))), w(A.s("Rare Copper Sword", 11), 0.14));
        // Quest 6
        Quest q6 = new Quest(6, Arrays.asList(q(A.h("Epic Copper Helmet", 12), l(A.dc(5), A.ha(8), A.da(6))),
                q(A.c("Epic Copper Chestplate", 11), l(A.dc(5), A.ha(8), A.da(6))),
                q(A.g("Epic Copper Gloves", 11), l(A.dc(4), A.ha(8), A.da(6))),
                q(A.t("Epic Copper Trousers", 11), l(A.dc(4), A.ha(8), A.da(6))),
                q(A.b("Epic Copper Boots", 11), l(A.dc(3), A.ha(8), A.da(6)))),
                w(A.s("Legend Copper Sword", 11), 0.23));
        // Quest 7
        Quest q7 = new Quest(7, Arrays.asList(q(A.h("Legend Copper Helmet", 13), l(A.dc(7), A.ha(11), A.da(6))),
                q(A.c("Legend Copper Chestplate", 13), l(A.dc(6), A.ha(10), A.da(8))),
                q(A.g("Legend Copper Gloves", 13), l(A.dc(6), A.ha(10), A.da(7))),
                q(A.t("Legend Copper Trousers", 12), l(A.dc(5), A.ha(9), A.da(7))),
                q(A.b("Legend Copper Boots", 13), l(A.dc(5), A.ha(8), A.da(6)))),
                w(A.s("Legend Copper Sword", 13), 0.23));
        // Quest 8
        Quest q8 = new Quest(8, Arrays.asList(q(A.h("Legend Copper Helmet", 15), l(A.dc(7), A.ha(12), A.da(6))),
                q(A.c("Legend Copper Chestplate", 14), l(A.dc(6), A.ha(12), A.da(9))),
                q(A.g("Legend Copper Gloves", 14), l(A.dc(6), A.ha(12), A.da(9))),
                q(A.t("Legend Copper Trousers", 16), l(A.dc(5), A.ha(12), A.da(9))),
                q(A.b("Legend Copper Boots", 16), l(A.dc(5), A.ha(12), A.da(9)))),
                w(A.s("Legend Copper Sword", 15), 0.23));
        // Quest 9
        Quest q9 = new Quest(9, Arrays.asList(q(A.h("Legend Copper Helmet", 17), l(A.dc(7), A.ha(12), A.da(6))),
                q(A.c("Legend Copper Chestplate", 18), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.g("Legend Copper Gloves", 18), l(A.dc(6), A.ha(12), A.da(9))),
                q(A.t("Legend Copper Trousers", 17), l(A.dc(6), A.ha(12), A.da(9))),
                q(A.b("Legend Copper Boots", 17), l(A.dc(6), A.ha(12), A.da(9)))),
                w(A.s("Legend Reinforced Sword", 17), 0.23));
        // Quest 10
        Quest q10 = new Quest(10, Arrays.asList(q(A.h("Legend Reinforced Helmet", 22), l(A.dc(7), A.ha(12), A.da(8))),
                q(A.c("Legend Reinforced Chestplate", 22), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.g("Legend Reinforced Gloves", 23), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.t("Legend Copper Trousers", 22), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.b("Legend Copper Boots", 23), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Reinforced Sword", 25), 0.23));
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
        if (questNumber == 1) return 100;
        if (questNumber == 2) return 105;
        if (questNumber == 3) return 110;
        if (questNumber == 4) return 115;
        if (questNumber == 5) return 120;
        if (questNumber == 6) return 125;
        if (questNumber == 7) return 130;
        if (questNumber == 8) return 135;
        if (questNumber == 9) return 140;
        if (questNumber == 10) return 145;
        return 0;
    }

    private List<Attribute> l(Attribute... aa) {
        return Arrays.asList(aa);
    }

    private List<Attribute> l(Attribute a) {
        return Collections.singletonList(a);
    }

    private QuestWeapon w(Weapon weapon, double damageMultiplier) {
        return new QuestWeapon(weapon, damageMultiplier);
    }

    private QuestArmor q(Armor armor, List<Attribute> attributes) {
        return new QuestArmor(armor, attributes);
    }
}