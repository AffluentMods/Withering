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

public class ChapterOne extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterOne() {
        super(1);
        // Quest 1
        Quest q1 = new Quest(1, A.s("Common Wood Sword", 2));
        // Quest 2
        Quest q2 = new Quest(2, Collections.singletonList(A.h("Common Wood Helmet", 3)), A.s("Common Wood Sword", 3));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(A.h("Common Wood Helmet", 4), A.c("Common Wood Chestplate", 4)),
                A.s("Common Wood Sword", 3));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(A.c("Common Wood Helmet", 4), A.c("Common Wood Chestplate", 4),
                A.g("Common Wood Gloves", 4)), A.s("Common Wood Sword", 3));
        // Quest 5
        Quest q5 = new Quest(5, Arrays.asList(A.h("Common Wood Helmet", 4), A.c("Common Wood Chestplate", 4),
                A.g("Common Wood Gloves", 4), A.t("Common Wood Trousers", 4)), A.s("Common Wood Sword", 4));
        // Quest 6
        Quest q6 = new Quest(6, Arrays.asList(A.h("Common Wood Helmet", 4), A.c("Common Wood Chestplate", 4),
                A.g("Common Wood Gloves", 3), A.t("Common Wood Trousers", 4), A.b("Common Wood Boots", 4)),
                A.s("Common Wood Sword", 5));
        // Quest 7
        Quest q7 = new Quest(7, Arrays.asList(q(A.h("Uncommon Wood Helmet", 4), l(A.dc(2))),
                q(A.c("Uncommon Wood Chestplate", 4), l(A.dc(2))), q(A.g("Uncommon Wood Gloves", 4), l(A.dc(2))),
                q(A.t("Uncommon Wood Trousers", 4), l(A.dc(1))), q(A.b("Uncommon Wood Boots", 4), l(A.dc(1)))),
                w(A.s("Uncommon Wood Sword", 5), 0.05));
        // Quest 8
        Quest q8 = new Quest(8, Arrays.asList(q(A.h("Rare Wood Helmet", 5), l(A.dc(2), A.ha(2))),
                q(A.c("Rare Wood Chestplate", 5), l(A.dc(2), A.ha(2))), q(A.g("Uncommon Wood Gloves", 5), l(A.dc(2))),
                q(A.t("Uncommon Wood Trousers", 5), l(A.dc(2))), q(A.b("Uncommon Wood Boots", 5), l(A.dc(2)))),
                w(A.s("Uncommon Wood Sword", 5), 0.05));
        // Quest 9
        Quest q9 = new Quest(9, Arrays.asList(q(A.h("Rare Wood Helmet", 6), l(A.dc(2), A.ha(3))),
                q(A.c("Rare Wood Chestplate", 6), l(A.dc(2), A.ha(3))),
                q(A.g("Rare Wood Gloves", 6), l(A.dc(2), A.ha(2))),
                q(A.t("Rare Wood Trousers", 6), l(A.dc(2), A.ha(2))), q(A.b("Uncommon Wood Boots", 6), l(A.dc(2)))),
                w(A.s("Rare Wood Sword", 6), 0.08));
        // Quest 10
        Quest q10 = new Quest(10, Arrays.asList(q(A.h("Rare Copper Helmet", 8), l(A.dc(7), A.ha(4))),
                q(A.c("Rare Wood Chestplate", 7), l(A.dc(2), A.ha(4))),
                q(A.g("Rare Wood Gloves", 7), l(A.dc(2), A.ha(4))),
                q(A.t("Rare Wood Trousers", 7), l(A.dc(2), A.ha(3))),
                q(A.b("Rare Wood Boots", 7), l(A.dc(2), A.ha(3)))), w(A.s("Rare Wood Sword", 7), 0.08));
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
        if (questNumber == 1) return 50;
        if (questNumber == 2) return 55;
        if (questNumber == 3) return 60;
        if (questNumber == 4) return 65;
        if (questNumber == 5) return 70;
        if (questNumber == 6) return 75;
        if (questNumber == 7) return 80;
        if (questNumber == 8) return 85;
        if (questNumber == 9) return 90;
        if (questNumber == 10) return 95;
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