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

public class ChapterThree extends Chapter {

    private final HashMap<Integer, Quest> questList;

    public ChapterThree() {
        super(3);
        // Quest 1
        Quest q1 = new Quest(1, Arrays.asList(q(A.h("Legend Reinforced Helmet", 25), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.c("Legend Reinforced Chestplate", 25), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.g("Legend Reinforced Gloves", 27), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.t("Legend Reinforced Trousers", 26), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.b("Legend Reinforced Boots", 27), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Reinforced Sword", 27), 0.41));
        // Quest 2
        Quest q2 = new Quest(2, Arrays.asList(q(A.h("Legend Titanium Helmet", 31), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.c("Legend Titanium Chestplate", 31), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.g("Legend Titanium Gloves", 31), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.t("Legend Reinforced Trousers", 31), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.b("Legend Reinforced Boots", 31), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Reinforced Sword", 30), 0.41));
        // Quest 3
        Quest q3 = new Quest(3, Arrays.asList(q(A.h("Legend Titanium Helmet", 33), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.c("Legend Titanium Chestplate", 33), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.g("Legend Titanium Gloves", 33), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.t("Legend Reinforced Trousers", 33), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.b("Legend Reinforced Boots", 34), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Titanium Sword", 32), 0.41));
        // Quest 4
        Quest q4 = new Quest(4, Arrays.asList(q(A.h("Legend Titanium Helmet", 36), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.c("Legend Titanium Chestplate", 36), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.g("Legend Titanium Gloves", 36), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.t("Legend Titanium Trousers", 36), l(A.dc(7), A.ha(12), A.da(9))),
                q(A.b("Legend Titanium Boots", 36), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Titanium Sword", 36), 0.41));
        // Quest 5
        Quest q5 = new Quest(5,
                Arrays.asList(q(A.h("Mythic Titanium Helmet", 38), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Legend Titanium Chestplate", 38), l(A.dc(7), A.ha(12), A.da(9))),
                        q(A.g("Legend Titanium Gloves", 38), l(A.dc(7), A.ha(12), A.da(9))),
                        q(A.t("Legend Titanium Trousers", 38), l(A.dc(7), A.ha(12), A.da(9))),
                        q(A.b("Legend Titanium Boots", 38), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Titanium Sword", 40), 0.41));
        // Quest 6
        Quest q6 = new Quest(6,
                Arrays.asList(q(A.h("Mythic Titanium Helmet", 42), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Titanium Chestplate", 42), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Legend Titanium Gloves", 42), l(A.dc(7), A.ha(12), A.da(9))),
                        q(A.t("Legend Titanium Trousers", 41), l(A.dc(7), A.ha(12), A.da(9))),
                        q(A.b("Legend Titanium Boots", 42), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Titanium Sword", 44), 0.41));
        // Quest 7
        Quest q7 = new Quest(7,
                Arrays.asList(q(A.h("Mythic Titanium Helmet", 44), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Titanium Chestplate", 43), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Titanium Gloves", 46), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Legend Titanium Trousers", 45), l(A.dc(7), A.ha(12), A.da(9))),
                        q(A.b("Legend Titanium Boots", 45), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Titanium Sword", 48), 0.41));
        // Quest 8
        Quest q8 = new Quest(8,
                Arrays.asList(q(A.h("Mythic Titanium Helmet", 47), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Titanium Chestplate", 47), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Titanium Gloves", 47), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Titanium Trousers", 48), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Legend Titanium Boots", 48), l(A.dc(7), A.ha(12), A.da(9)))),
                w(A.s("Legend Titanium Sword", 49), 0.41));
        // Quest 9
        Quest q9 = new Quest(9,
                Arrays.asList(q(A.h("Mythic Titanium Helmet", 50), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Titanium Chestplate", 49), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Titanium Gloves", 49), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Titanium Trousers", 50), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Titanium Boots", 51), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Legend Titanium Sword", 54), 0.41));
        // Quest 10
        Quest q10 = new Quest(10,
                Arrays.asList(q(A.h("Mythic Iron Helmet", 55), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.c("Mythic Titanium Chestplate", 54), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.g("Mythic Titanium Gloves", 55), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.t("Mythic Titanium Trousers", 54), l(A.dc(10), A.ha(16), A.da(12), A.pa(25))),
                        q(A.b("Mythic Titanium Boots", 56), l(A.dc(10), A.ha(16), A.da(12), A.pa(25)))),
                w(A.s("Mythic Titanium Sword", 60), 0.59));
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
        if (questNumber == 1) return 150;
        if (questNumber == 2) return 155;
        if (questNumber == 3) return 160;
        if (questNumber == 4) return 165;
        if (questNumber == 5) return 170;
        if (questNumber == 6) return 175;
        if (questNumber == 7) return 180;
        if (questNumber == 8) return 185;
        if (questNumber == 9) return 190;
        if (questNumber == 10) return 195;
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