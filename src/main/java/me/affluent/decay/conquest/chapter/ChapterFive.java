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

public class ChapterFive extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterFive() {
        super(5);
        // Quest 1
        Quest q1 = new Quest(1,
                Arrays.asList(q(A.h("Mythic Carbon Steel Helmet", 105), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Steel Chestplate", 105), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Steel Gloves", 105), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Steel Trousers", 105), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Steel Boots", 105), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Carbon Steel Sword", 110), 0.59));
        // Quest 2
        Quest q2 = new Quest(2,
                Arrays.asList(q(A.h("Mythic Carbon Steel Helmet", 115), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Carbon Steel Chestplate", 115), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Carbon Steel Gloves", 115), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Steel Trousers", 115), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Steel Boots", 115), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Carbon Steel Sword", 116), 0.59));
        // Quest 3
        Quest q3 = new Quest(3,
                Arrays.asList(q(A.h("Mythic Carbon Steel Helmet", 125), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Carbon Steel Chestplate", 125), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Carbon Steel Gloves", 125), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Carbon Steel Trousers", 125), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Carbon Steel Boots", 125), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Carbon Steel Sword", 123), 0.59));
        // Quest 4
        Quest q4 = new Quest(4,
                Arrays.asList(q(A.h("Mythic Carbon Steel Helmet", 136), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Carbon Steel Chestplate", 136), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Carbon Steel Gloves", 136), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Carbon Steel Trousers", 136), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Carbon Steel Boots", 136), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Carbon Steel Sword", 130), 0.59));
        // Quest 5
        Quest q5 = new Quest(5,
                Arrays.asList(q(A.h("Mythic Carbon Steel Helmet", 145), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Carbon Steel Chestplate", 145), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Carbon Steel Gloves", 145), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Carbon Steel Trousers", 145), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Carbon Steel Boots", 145), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Carbon Steel Sword", 138), 0.59));
        // Quest 6
        Quest q6 = new Quest(6,
                Arrays.asList(q(A.h("Mythic Dragon Steel Helmet", 157), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Carbon Steel Chestplate", 157), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Carbon Steel Gloves", 157), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Carbon Steel Trousers", 157), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Carbon Steel Boots", 157), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Carbon Steel Sword", 146), 0.59));
        // Quest 7
        Quest q7 = new Quest(7,
                Arrays.asList(q(A.h("Mythic Dragon Steel Helmet", 167), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Dragon Steel Chestplate", 167), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Carbon Steel Gloves", 167), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Carbon Steel Trousers", 167), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Carbon Steel Boots", 167), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Carbon Steel Sword", 155), 0.59));
        // Quest 8
        Quest q8 = new Quest(8,
                Arrays.asList(q(A.h("Mythic Dragon Steel Helmet", 175), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Dragon Steel Chestplate", 175), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Dragon Steel Gloves", 175), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Carbon Steel Trousers", 115), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Carbon Steel Boots", 115), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Carbon Steel Sword", 167), 0.59));
        // Quest 9
        Quest q9 = new Quest(9,
                Arrays.asList(q(A.h("Mythic Dragon Steel Helmet", 190), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Dragon Steel Chestplate", 190), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Dragon Steel Gloves", 190), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Dragon Steel Trousers", 190), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Carbon Steel Boots", 190), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Carbon Steel Sword", 180), 0.59));
        // Quest 10
        Quest q10 = new Quest(10,
                Arrays.asList(q(A.h("Mythic Dragon Steel Helmet", 200), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.c("Mythic Dragon Steel Chestplate", 200), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.g("Mythic Dragon Steel Gloves", 200), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.t("Mythic Dragon Steel Trousers", 200), l(A.dc(12), A.ha(18), A.da(14), A.pa(30))),
                        q(A.b("Mythic Dragon Steel Boots", 200), l(A.dc(12), A.ha(18), A.da(14), A.pa(30)))),
                w(A.s("Mythic Carbon Steel Sword", 200), 0.59));
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
        if (questNumber == 1) return 250;
        if (questNumber == 2) return 255;
        if (questNumber == 3) return 260;
        if (questNumber == 4) return 265;
        if (questNumber == 5) return 270;
        if (questNumber == 6) return 275;
        if (questNumber == 7) return 280;
        if (questNumber == 8) return 285;
        if (questNumber == 9) return 290;
        if (questNumber == 10) return 295;
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