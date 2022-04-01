package me.affluent.decay.util.system;

import me.affluent.decay.Withering;
import me.affluent.decay.achievements.Achievement;
import me.affluent.decay.armor.ArmorSet;
import me.affluent.decay.armor.dragon.ArmorDragonSet;
import me.affluent.decay.armor.dragonset.DragonDragonSteelSet;
import me.affluent.decay.armor.iron.ArmorIronSet;
import me.affluent.decay.armor.ironset.IronIronSet;
import me.affluent.decay.armor.sets.*;
import me.affluent.decay.armor.wither.ArmorWitherSet;
import me.affluent.decay.armor.witherset.WitherWitherSet;
import me.affluent.decay.chest.Chest;
import me.affluent.decay.conquest.chapter.Chapter;
import me.affluent.decay.entity.HealthUser;
import me.affluent.decay.holidayevent.HolidayEvent;
import me.affluent.decay.pets.MassPets.*;
import me.affluent.decay.pets.PetSet;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.rarity.Rarities;
import me.affluent.decay.skill.Skill;
import me.affluent.decay.specialevent.SpecialEvent;
import me.affluent.decay.util.*;
import me.affluent.decay.vote.VoteSystem;

import java.util.*;

public class LoadUtil {

    public static void load() {
        List<ArmorSet> armorSets =
                Arrays.asList(new WoodSet(), new CopperSet(), new ReinforcedSet(), new TitaniumSet(), new IronSet(),
                        new SteelSet(), new CarbonSteelSet(), new DragonSteelSet(), new TitanAlloySet(), new WitherSet());
        List<PetSet> petSets =
                Arrays.asList(new RareBettaFish(), new LegendAlphaPredator(), new UncommonMongolianHorse(),
                new CommonMongolianHorse(), new JunkMongolianHorse(), new AncientKraken(), new RareWolf(), new EpicWolf(),
                new EpicPlagueBearer(), new CommonAndalusianHorse(), new UncommonAndalusianHorse(), new RareAndalusianHorse(),
                new UncommonShireHorse(), new RareShireHorse(), new EpicShireHorse(), new MythicReaper(), new MythicSnowman(),
                new JunkBee());
        for (PetSet petSet : petSets) {
            System.out.println("[INTERN INFO] Loaded Pet: " + petSet.getBaseName());
        }
        for (ArmorSet armorSet : armorSets) {
            System.out.println("[INTERN INFO] Loaded armor set: " + armorSet.getBaseName());
        }
        List<ArmorWitherSet> armorWitherSets =
                Collections.singletonList(new WitherWitherSet());
        for (ArmorWitherSet armorWitherSet : armorWitherSets) {
            System.out.println("[INTERN INFO] Loaded armor set: " + armorWitherSet.getBaseName() + " [2]");
        }

        List<ArmorDragonSet> armorDragonSets =
                Collections.singletonList(new DragonDragonSteelSet());
        for (ArmorDragonSet armorDragonSet : armorDragonSets) {
            System.out.println("[INTERN INFO] Loaded armor set: " + armorDragonSet.getBaseName() + " [2]");
        }

        List<ArmorIronSet> armorIronSets =
                Collections.singletonList(new IronIronSet());
        for (ArmorIronSet armorIronSet : armorIronSets) {
            System.out.println("[INTERN INFO] Loaded armor set: " + armorIronSet.getBaseName() + " [2]");
        }
        Rank.loadRanks();
        VoteSystem.load();
        FireworkSystem.load();
        DailyRewardSystem.load();
        Chest.load();
        Rarities.load();
        BoostUtil.load();
        //ArtifactUtil.load();
        ScrollsUtil.load();
        GoldenTokensUtil.load();
        TokensUtil.load();
        ElixirUtil.load();
        SpecialEvent.load();
        HolidayEvent.load();
        Skill.load();
        SkillUtil.load();
        TutorialUtil.load();
        //PatreonServer.start();
        Chapter.loadChapters();
        Timer timer = new Timer();
        Achievement.loadAchievements();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Withering.getBot().getDatabase()
                        .update("UPDATE health SET health=(health+3) WHERE health < maxhealth;");
                HealthUser.clearCache();
            }
        }, 0L, 90 * 1000L);
    }
}