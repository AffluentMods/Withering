package me.affluent.decay.conquest.chapter;

import me.affluent.decay.Withering;
import me.affluent.decay.armor.*;
import me.affluent.decay.attribute.Attribute;
import me.affluent.decay.conquest.Quest;
import me.affluent.decay.conquest.QuestArmor;
import me.affluent.decay.conquest.QuestWeapon;
import me.affluent.decay.weapon.Arrow;
import me.affluent.decay.weapon.Shield;
import me.affluent.decay.weapon.Weapon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Chapter {

    private static final HashMap<Integer, Chapter> chapters = new HashMap<>();

    private final int chapterNumber;

    Chapter(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public abstract int getDiamondsReward(int questNumber);

    public abstract HashMap<Integer, Quest> getQuestList();

    public abstract Quest getQuest(int questNumber);

    public int getChapterNumber() {
        return chapterNumber;
    }

    public static int getChapterCount() {
        return chapters.size();
    }

    public static void loadChapters() {
        List<Chapter> dynamicChapters = new ArrayList<>();
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS sys_chapters (ID INT AUTO_INCREMENT PRIMARY KEY, chapter INT NOT " +
                        "NULL);");
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS sys_chapters_quests (chapter INT NOT NULL, quest INT NOT NULL, " +
                        "diamondsReward INT NOT NULL, helmet VARCHAR(96), chestplate VARCHAR(96), gloves VARCHAR(96)," +
                        " trousers VARCHAR(96), boots VARCHAR(96), weapon VARCHAR(96), arrow VARCHAR(96), shield VARCHAR(96));");
        Withering.getBot().getDatabase()
                .update("CREATE TABLE IF NOT EXISTS sys_chapters_quests_attrs (chapter INT NOT NULL, quest INT NOT " +
                        "NULL, helmet VARCHAR(512), chestplate VARCHAR(512), gloves VARCHAR(512), trousers VARCHAR" +
                        "(512), boots VARCHAR(512), weapon VARCHAR(16));");
        try (ResultSet rs1 = Withering.getBot().getDatabase().query("SELECT * FROM sys_chapters;")) {
            while (rs1.next()) {
                final int chapter = rs1.getInt("chapter");
                final HashMap<Integer, Integer> diamondsRewards = new HashMap<>();
                final HashMap<Integer, Quest> questList = new HashMap<>();
                try (ResultSet rs2 = Withering.getBot().getDatabase()
                        .query("SELECT * FROM sys_chapters_quests WHERE chapter=?;", chapter)) {
                    while (rs2.next()) {
                        int quest = rs2.getInt("quest");
                        diamondsRewards.put(quest, rs2.getInt("diamondsRewards"));
                        List<Attribute> qa1a = new ArrayList<>();
                        List<Attribute> qa2a = new ArrayList<>();
                        List<Attribute> qa3a = new ArrayList<>();
                        List<Attribute> qa4a = new ArrayList<>();
                        List<Attribute> qa5a = new ArrayList<>();
                        double qwdm = 0.0;
                        try (ResultSet rs3 = Withering.getBot().getDatabase()
                                .query("SELECT * FROM sys_chapters_quests_attrs WHERE chapter=? AND quest=?;", chapter,
                                        quest)) {
                            while (rs3.next()) {
                                String[] qa1_attr_data = rs3.getString("helmet").split(";");
                                String qa1_attr_name = qa1_attr_data[0];
                                String qa1_attr_value = qa1_attr_data[1];
                                Attribute qa1_a = Attribute.getAttribute(qa1_attr_name, qa1_attr_value);
                                String[] qa2_attr_data = rs3.getString("chestplate").split(";");
                                String qa2_attr_name = qa2_attr_data[0];
                                String qa2_attr_value = qa2_attr_data[1];
                                Attribute qa2_a = Attribute.getAttribute(qa2_attr_name, qa2_attr_value);
                                String[] qa3_attr_data = rs3.getString("gloves").split(";");
                                String qa3_attr_name = qa3_attr_data[0];
                                String qa3_attr_value = qa3_attr_data[1];
                                Attribute qa3_a = Attribute.getAttribute(qa3_attr_name, qa3_attr_value);
                                String[] qa4_attr_data = rs3.getString("trousers").split(";");
                                String qa4_attr_name = qa4_attr_data[0];
                                String qa4_attr_value = qa4_attr_data[1];
                                Attribute qa4_a = Attribute.getAttribute(qa4_attr_name, qa4_attr_value);
                                String[] qa5_attr_data = rs3.getString("boots").split(";");
                                String qa5_attr_name = qa5_attr_data[0];
                                String qa5_attr_value = qa5_attr_data[1];
                                Attribute qa5_a = Attribute.getAttribute(qa5_attr_name, qa5_attr_value);
                                qa1a.add(qa1_a);
                                qa2a.add(qa2_a);
                                qa3a.add(qa3_a);
                                qa4a.add(qa4_a);
                                qa5a.add(qa5_a);
                                double addqwdm = Double.parseDouble(rs3.getString("weapon"));
                                if (addqwdm > 0) qwdm += addqwdm;
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        QuestArmor qa1 = new QuestArmor(Helmet.getHelmet(rs2.getString("helmet")), qa1a);
                        QuestArmor qa2 = new QuestArmor(Chestplate.getChestplate(rs2.getString("chestplate")), qa2a);
                        QuestArmor qa3 = new QuestArmor(Gloves.getGloves(rs2.getString("gloves")), qa3a);
                        QuestArmor qa4 = new QuestArmor(Trousers.getTrousers(rs2.getString("trousers")), qa4a);
                        QuestArmor qa5 = new QuestArmor(Boots.getBoots(rs2.getString("boots")), qa5a);
                        QuestWeapon qw = new QuestWeapon(Weapon.getWeapon(rs2.getString("weapon")), qwdm);
                        Arrow arrow = Arrow.getArrow(rs2.getString("arrow"));
                        Shield shield = Shield.getShield(rs2.getString("shield"));
                        Quest q = new Quest(quest, new ArrayList<>(Arrays.asList(qa1, qa2, qa3, qa4, qa5)), qw, arrow, shield);
                        questList.put(quest, q);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                Chapter newChapter = new Chapter(chapter) {
                    final HashMap<Integer, Integer> dr = diamondsRewards;
                    final HashMap<Integer, Quest> ql = questList;

                    @Override
                    public int getDiamondsReward(int questNumber) {
                        return dr.get(questNumber);
                    }

                    @Override
                    public HashMap<Integer, Quest> getQuestList() {
                        return ql;
                    }

                    @Override
                    public Quest getQuest(int questNumber) {
                        return ql.get(questNumber);
                    }
                };
                dynamicChapters.add(newChapter);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        List<Chapter> chapterList =
                Arrays.asList(new ChapterOne(), new ChapterTwo(), new ChapterThree(), new ChapterFour(), new ChapterFive(),
                        new ChapterSix(), new ChapterSeven(), new ChapterEight(), new ChapterNine(), new ChapterTen(), new ChapterEleven(), new ChapterTwelve());
        chapterList.addAll(dynamicChapters);
        for (Chapter chapter : chapterList) {
            int cn = chapter.getChapterNumber();
            chapters.put(cn, chapter);
        }
    }

    public static Chapter getChapter(int chapterNumber) {
        return chapters.get(chapterNumber);
    }

    public static boolean chapterExists(int chapterNumber) {
        return chapters.containsKey(chapterNumber);
    }
}