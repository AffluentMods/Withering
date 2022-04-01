package me.affluent.decay.command.actions;

import me.affluent.decay.Constants;
import me.affluent.decay.captcha.CaptchaAPI;
import me.affluent.decay.entity.ArmorUser;
import me.affluent.decay.entity.PetUser;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.language.Language;
import me.affluent.decay.pets.PetItem;
import me.affluent.decay.pvp.Fight;
import me.affluent.decay.superclass.BotCommand;
import me.affluent.decay.util.*;
import me.affluent.decay.util.settingsUtil.ResponseUtil;
import me.affluent.decay.util.system.*;
import me.affluent.decay.weapon.Weapon;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

public class ArenaCommand extends BotCommand {

    public ArenaCommand() {
        this.name = "arena";
        this.aliases = new String[]{"att", "pvp", "fight"};
        this.cooldown = 2;
    }

    @Override
    protected void execute(CommandEvent e) {
        User u = e.getAuthor();
        String uid = u.getId();
        if (!Player.playerExists(uid)) {
            e.reply(Constants.PROFILE_404(uid));
            return;
        }
        Player p = Player.getPlayer(uid);
        String userPrefix = PrefixUser.getPrefixUser(uid).getPrefix();
        String response = ResponseUtil.getResponseUtil(uid).getResponse();
        if (e.getArgs().length == 0) {
            e.reply(MessageUtil.err(Constants.ERROR(uid),
                    Language.getLocalized(uid, "usage", "Please use {command_usage}.")
                            .replace("{command_usage}", "`" + userPrefix + "arena <@user | user#tag>`")));
            return;
        }
        if (CaptchaAPI.isPending(uid)) {
            e.reply(MessageUtil.err(Constants.ERROR(uid), Language.getLocalized(uid, "verify_pending",
                            "my good {gender}, do note, there is a letter for you to read. \nIf you lost the letter, then use `" +
                                    userPrefix + "verify resend`")
                    .replace("{gender}", "" + p.getGender())));
            return;
        }
        User target = MentionUtil.getUser(e.getMessage());
        if (target == null || !Player.playerExists(target.getId())) {
            String msg1 = Language.getLocalized(uid, "target_not_found", "Preposterous! This particular person does not seem to exist!");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        if (target.getId().equalsIgnoreCase(uid)) {
            String msg1 = Language.getLocalized(uid, "no_self_harm", "You foolish scoundrel, you grab with the handle, not the pointy side!");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        long pvpCooldown = CooldownUtil.getCooldown(uid, "pvp_cooldown");
        if (pvpCooldown > 0) {
            long now = System.currentTimeMillis();
            long cdDiff = pvpCooldown - now;
            if (cdDiff > 0) {
                String cooldownString = CooldownUtil.format(cdDiff, uid);
                String msg1 = Constants.COOLDOWN(uid).replace("{cooldown}", cooldownString);
                e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
                return;
            }
        }
        if (p.getHealthUser().getHealth() <= 0) {
            String msg1 = Language.getLocalized(uid, "self_dead", "M'lord, you seem to have fallen. Might I suggest you `" + userPrefix + "heal`");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Weapon pw = p.getArmorUser().getWeapon();
        if (pw == null) {
            String msg1 = Language.getLocalized(uid, "no_weapon", "I thought I told you to grab a weapon.");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        Player t = Player.getPlayer(target.getId());
        if (t.getRank().getId() != p.getRank().getId()) {
            String msg1 = Language.getLocalized(uid, "not_same_rank",
                            "You don't have the same rank as this user!\nYour rank: {self_rank}\n{user_tag}'s rank: " +
                                    "{user_rank}").replace("{self_rank}", p.getRankDisplay())
                    .replace("{user_tag}", t.getUser().getAsTag()).replace("{user_rank}", t.getRankDisplay());
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        if (t.getHealthUser().getHealth() <= 0) {
            String msg1 = Language.getLocalized(uid, "target_dead", "Scurry along now, they're already dead. No need to fight a corpse.")
                    .replace("{gender}", "" + p.getGender());
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        if (CooldownUtil.hasCooldown(target.getId(), "pvp_barrier")) {
            long till = CooldownUtil.getCooldown(target.getId(), "pvp_barrier");
            long now = System.currentTimeMillis();
            if (till > now) {
                String msg1 = Language.getLocalized(uid, "target_has_barrier", "Can't attack `" + "{target}" + "`, they are hiding behind a barrier.")
                        .replace("{target}", t.getUser().getAsTag());
                e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
                return;
            } else {
                CooldownUtil.removeCooldown(target.getId(), "pvp_barrier");
            }
        }
        Weapon tw = t.getArmorUser().getWeapon();
        if (tw == null) {
            String msg1 = Language.getLocalized(uid, "target_no_weapon", "Aha, that fool! They don't have a weapon, quick kill them!");
            e.reply(MessageUtil.err(Constants.ERROR(uid), msg1));
            return;
        }
        if (CooldownUtil.hasCooldown(uid, "pvp_barrier")) CooldownUtil.removeCooldown(uid, "pvp_barrier");
        Fight fight = new Fight(p, t, pw, tw);
        Player winner = fight.doFight(false, false);
        String attackerPetEquipped = PetUser.getPetUser(p.getUserId()).getPetID();
        String defenderPetEquipped = PetUser.getPetUser(t.getUserId()).getPetID();
        PetItem petObj1 = null;
        PetUser petUser1 = p.getPetUser();
        PetItem petObj2 = null;
        PetUser petUser2 = p.getPetUser();
        boolean attackerHasPet = !Objects.equals(attackerPetEquipped, null);
        if (attackerHasPet) petObj1 = petUser1.getPet();
        boolean defenderHasPet = !Objects.equals(defenderPetEquipped, null);
        if (defenderHasPet) petObj2 = petUser2.getPet();
        int attackerGoldCoinsBonus = fight.getAttackerGoldCoinsBonus();
        int defenderGoldCoinsBonus = fight.getDefenderGoldCoinsBonus();
        int attackerExpBonus = fight.getAttackerExpBonus();
        int defenderExpBonus = fight.getDefenderExpBonus();
        int attacker_exp;
        int attacker_gold;
        String attacker_key = "";
        String attacker_diamonds = "";
        String attacker_ingots = "";
        String defender_ingots = "";
        String attacker_rare_wolf = "";
        String attacker_epic_wolf = "";
        int defender_exp;
        int defender_gold;
        boolean tWin = winner.getUserId().equals(target.getId());
        String title1 = Language.getLocalized(uid, "pvp_ended", "PvP Ended");
        boolean woodKeyP = new Random().nextInt(101) < (tWin ? 5 : 20);
        boolean metalKeyP = new Random().nextInt(101) < (tWin ? 1 : 5);
        final int defender_health_after = t.getHealthUser().getHealth();
        DiminishingUtil.Diminish diminish = DiminishingUtil.getDiminish(p.getUserId(), t.getUserId());
        double p_rewardsMultiplier = 1.00;
        if (diminish != null) {
            p_rewardsMultiplier -= diminish.getCount() * 0.10;
        }
        if (p_rewardsMultiplier < 0) p_rewardsMultiplier = 0;
        DiminishingUtil.Diminish t_diminish = DiminishingUtil.getDiminish(t.getUserId(), p.getUserId());
        double t_rewardsMultiplier = 1.00;
        if (t_diminish != null) {
            t_rewardsMultiplier -= t_diminish.getCount() * 0.10;
        }
        if (t_rewardsMultiplier < 0) t_rewardsMultiplier = 0;
        TextChannel tc = e.getTextChannel();
        if (tWin) {
            int tExpGain = GainUtil.getExpGainDefendWin(t, t.getExpUser().getLevel());
            int tPetExpGain = GainUtil.getExpGainDefendWin(t, t.getPetExpUser().getPetLevel());
            int pExpGain = GainUtil.getExpGainAttackLose(p, p.getExpUser().getLevel());
            int pPetExpGain = GainUtil.getExpGainAttackLose(p, p.getPetExpUser().getPetLevel());
            if (attackerExpBonus > 0) {
                pExpGain += pExpGain * (attackerExpBonus / 100.0);
                pPetExpGain += pPetExpGain * (attackerExpBonus / 100.0);
            }
            if (defenderExpBonus > 0) {
                tExpGain += tExpGain * (defenderExpBonus / 100.0);
                tPetExpGain += tPetExpGain * (defenderExpBonus / 100.0);
            }
            int tGoldCoinsGain = GainUtil.getMedallionGainDefendWin(t, t.getExpUser().getLevel());
            int pGoldCoinsGain = GainUtil.getMedallionGainAttackLose(p, p.getExpUser().getLevel());
            if (attackerGoldCoinsBonus > 0) pGoldCoinsGain += pGoldCoinsGain * (attackerGoldCoinsBonus / 100.0);
            if (defenderGoldCoinsBonus > 0) tGoldCoinsGain += tGoldCoinsGain * (defenderGoldCoinsBonus / 100.0);
            pExpGain *= p_rewardsMultiplier;
            pPetExpGain *= p_rewardsMultiplier;
            pGoldCoinsGain *= p_rewardsMultiplier;
            tExpGain *= t_rewardsMultiplier;
            tPetExpGain *= t_rewardsMultiplier;
            tGoldCoinsGain *= t_rewardsMultiplier;
            boolean godlyOccurenceWin = new Random().nextInt(100) <= 5;
            if (godlyOccurenceWin) {
                pExpGain *= 2;
                pPetExpGain *= 2;
                pGoldCoinsGain *= 2;
                tExpGain *= 2;
                tPetExpGain *= 2;
                tGoldCoinsGain *= 2;
            }
            //
            if (attackerHasPet) {
                p.getPetExpUser().addPetExp(pPetExpGain, p.getUserId());
            }
            if (defenderHasPet) {
                t.getPetExpUser().addPetExp(tPetExpGain, t.getUserId());
            }
            if (FireworkSystem.hasFirework(uid)) {
                pExpGain *= 2;
                pGoldCoinsGain *= 2;
            }
            if (FireworkSystem.hasFirework(t.getUserId())) {
                tExpGain *= 2;
                tGoldCoinsGain *= 2;
            }
            p.getExpUser().addExp(pExpGain, tc);
            t.getExpUser().addExp(tExpGain, tc);
            t.getEcoUser().addBalance(tGoldCoinsGain);
            p.getEcoUser().addBalance(pGoldCoinsGain);
            attacker_exp = pExpGain;
            attacker_gold = pGoldCoinsGain;
            defender_exp = tExpGain;
            defender_gold = tGoldCoinsGain;
            //
        } else {
            int pExpGain = GainUtil.getExpGainAttackWin(p, p.getExpUser().getLevel());
            int tPetExpGain = 1;
            int pPetExpGain = 1;
            if (defenderHasPet) {
                tPetExpGain = GainUtil.getExpGainAttackWin(t, t.getPetExpUser().getPetLevel());
            }
            int tExpGain = GainUtil.getExpGainDefendLose(t, t.getExpUser().getLevel());
            if (attackerHasPet) {
                pPetExpGain = GainUtil.getExpGainDefendLose(p, p.getPetExpUser().getPetLevel());
            }
            if (attackerExpBonus > 0) {
                pExpGain += pExpGain * (attackerExpBonus / 100.0);
                pPetExpGain += pPetExpGain * (attackerExpBonus / 100.0);
            }
            if (defenderExpBonus > 0) {
                tExpGain += tExpGain * (defenderExpBonus / 100.0);
                tPetExpGain += tPetExpGain * (defenderExpBonus / 100.0);
            }
            int pGoldCoinsGain = GainUtil.getMedallionGainAttackWin(p, p.getExpUser().getLevel());
            int tGoldCoinsGain = GainUtil.getMedallionGainDefendLose(t, t.getExpUser().getLevel());
            if (attackerGoldCoinsBonus > 0) pGoldCoinsGain += pGoldCoinsGain * (attackerGoldCoinsBonus / 100.0);
            if (defenderGoldCoinsBonus > 0) tGoldCoinsGain += tGoldCoinsGain * (defenderGoldCoinsBonus / 100.0);
            pExpGain *= p_rewardsMultiplier;
            pPetExpGain *= p_rewardsMultiplier;
            pGoldCoinsGain *= p_rewardsMultiplier;
            tExpGain *= t_rewardsMultiplier;
            tPetExpGain *= t_rewardsMultiplier;
            tGoldCoinsGain *= t_rewardsMultiplier;
            boolean godlyOccurenceLose = new Random().nextInt(100) <= 5;
            if (godlyOccurenceLose) {
                pExpGain *= 2;
                pPetExpGain *= 2;
                pGoldCoinsGain *= 2;
                tExpGain *= 2;
                tPetExpGain *= 2;
                tGoldCoinsGain *= 2;
            }
            //
            if (defenderHasPet) {
                t.getPetExpUser().addPetExp(tPetExpGain, t.getUserId());
            }
            if (attackerHasPet) {
                p.getPetExpUser().addPetExp(pPetExpGain, p.getUserId());
            }
            if (FireworkSystem.hasFirework(uid)) {
                pExpGain *= 2;
                pGoldCoinsGain *= 2;
            }
            if (FireworkSystem.hasFirework(t.getUserId())) {
                tExpGain *= 2;
                tGoldCoinsGain *= 2;
            }
            p.getExpUser().addExp(pExpGain, tc);
            t.getExpUser().addExp(tExpGain, tc);
            t.getEcoUser().addBalance(tGoldCoinsGain);
            p.getEcoUser().addBalance(pGoldCoinsGain);
            attacker_exp = pExpGain;
            attacker_gold = pGoldCoinsGain;
            defender_exp = tExpGain;
            defender_gold = tGoldCoinsGain;
        }
        if (metalKeyP) {
            p.getInventoryUser().addItem("metal key", 1);
            attacker_key = "• `1` Metal Key" + EmoteUtil.getEmoteMention("Metal_Key") + "\n";
        } else {
            if (woodKeyP) {
                p.getInventoryUser().addItem("wood key", 1);
                attacker_key = "• `1` Wood Key" + EmoteUtil.getEmoteMention("Wood_Key") + "\n";
            }
        }
        int attacker_kills = Integer.parseInt(StatsUtil.getStat(uid, "attack_kills", "0"));
        int defender_kills = Integer.parseInt(StatsUtil.getStat(uid, "defend_kills", "0"));
        int totalKills = defender_kills + attacker_kills;
        int attackerRandomIngot = new Random().nextInt(1001);
        int attackerRandomRareWolf = new Random().nextInt(1001);
        int attackerRandomEpicWolf = new Random().nextInt(20001);
        long defender_current_ingots = IngotUtil.getIngots(t.getUserId());
        if (totalKills >= 250) {
            if (attackerRandomRareWolf == 1) {
                PetUtil.getPetUtil(uid).addPet("RARE_WOLF", 1);
                attacker_rare_wolf = "• Found Rare Wolf pup " + EmoteUtil.getEmoteMention("Rare_Wolf") + "\n";
            }
            if (attackerRandomEpicWolf == 1) {
                PetUtil.getPetUtil(uid).addPet("EPIC_WOLF", 1);
                attacker_rare_wolf = "• Found Epic Wolf pup " + EmoteUtil.getEmoteMention("Epic_Wolf") + "\n";
            }
        }
        if (attackerRandomIngot <= 35 && (attackerRandomIngot > 15)) {
            if (defender_current_ingots > 0) {
                if (defender_current_ingots >= 7) {
                    IngotUtil.addIngots(p.getUserId(), 7);
                    IngotUtil.setIngots(t.getUserId(), defender_current_ingots - 7);
                    attacker_ingots = "• stole `7` Ingot " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                    defender_ingots = "• lost `7` Ingot " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                } else {
                    IngotUtil.addIngots(p.getUserId(), defender_current_ingots);
                    attacker_ingots = "• stole `" + defender_current_ingots + "` Ingot " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                    defender_ingots = "• lost `" + defender_current_ingots + "` Ingot " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                    IngotUtil.setIngots(t.getUserId(), 0);
                }
            }
        }
        if (attackerRandomIngot <= 15) {
            if (defender_current_ingots > 0) {
                if (defender_current_ingots >= 10) {
                    IngotUtil.addIngots(p.getUserId(), 10);
                    IngotUtil.setIngots(t.getUserId(), defender_current_ingots - 10);
                    attacker_ingots = "• stole `10` Ingots " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                    defender_ingots = "• lost `10` Ingots " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                } else {
                    IngotUtil.addIngots(p.getUserId(), defender_current_ingots);
                    attacker_ingots = "• stole `" + defender_current_ingots + "` Ingot " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                    defender_ingots = "• lost `" + defender_current_ingots + "` Ingot " + EmoteUtil.getEmoteMention("Alloy_Ingot") + "\n";
                    IngotUtil.setIngots(t.getUserId(), 0);
                }
            }
        }
        int attackerRandomDiamond = new Random().nextInt(101);
        if (attackerRandomDiamond <= 15) {
            int diamondGain = GainUtil.getDiamondsGainPvP(p, p.getExpUser().getLevel());
            p.getInventoryUser().addItem("diamond", diamondGain);
            if (diamondGain > 1) {
                attacker_diamonds = "• `" + FormatUtil.formatCommas(diamondGain) + "` Diamonds " + EmoteUtil.getDiamond() + "\n";
            } else {
                attacker_diamonds = "• `" + FormatUtil.formatCommas(diamondGain) + "` Diamond " + EmoteUtil.getDiamond() + "\n";
            }
        }
        int defender_health_left = fight.getDefenderHealthLeft();
        int attacker_health_left = fight.getAttackerHealthLeft();
        int defender_damage_dealt = fight.getDefenderDamageDealt();
        int attacker_damage_dealt = fight.getAttackerDamageDealt();
        updateStats(p.getUserId(), t.getUserId(), winner.getUserId());
        boolean attackerXPMaxed = p.getExpUser().isMaxed();
        String attackerXPEmote = "";
        if (attackerXPMaxed) {
            attackerXPEmote = EmoteUtil.getEmoteMention("Elixir_XP_Orb");
        } else {
            attackerXPEmote = EmoteUtil.getEmoteMention("XP_Orb");
        }
        boolean defenderXPMaxed = t.getExpUser().isMaxed();
        String defenderXPEmote = "";
        if (defenderXPMaxed) {
            defenderXPEmote = EmoteUtil.getEmoteMention("Elixir_XP_Orb");
        } else {
            defenderXPEmote = EmoteUtil.getEmoteMention("XP_Orb");
        }
        if (response.equalsIgnoreCase("pc")) {
            if (tWin) {
                CooldownUtil.addCooldown(p.getUserId(), "pvp_cooldown", System.currentTimeMillis() + (60 * 1000), true);
                int maxHealthP = p.getHealthUser().getMaxHealth(); // 10
                int recoveredHealth = FormatUtil.round(maxHealthP * 0.15); // 2
                int newHealthP = defender_health_after + recoveredHealth; // 9 + 2
                if (newHealthP > maxHealthP) { // 11 > 10
                    recoveredHealth = maxHealthP - defender_health_after; // 10-9
                    newHealthP = maxHealthP; // 1
                }
                String word = "";
                if (recoveredHealth >= 0) word = "recovered";
                if (recoveredHealth < 0) word = "lost";
                t.getHealthUser().setHealth(newHealthP);
                String message1 = Language.getLocalized(uid, "pvp_result_lose",
                                "{@attacker} Died against {@defender}.\n\n" +

                                        "`{attacker_tag}`\n" +
                                        "• Dealt {attacker_damage} Damage {attacker_emote}\n" +
                                        "• {attacker_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +
                                        "• `{attacker_exp}` XP {attacker_xp_emote} \n" +
                                        "• `{attacker_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{attacker_key}{attacker_diamonds}{attacker_ingots}{rare_wolf}{epic_wolf}\n\n" +

                                        "`{defender_tag}`\n" +
                                        "• Dealt {defender_damage} Damage {defender_emote}\n" +
                                        "• {defender_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +


                                        "• `{defender_exp}` XP {defender_xp_emote} \n" +
                                        "• `{defender_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{defender_ingots}\n" +
                                        "`{defender_tag}` {word} `{recovered_health}` health.")
                        .replace("{attacker_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(p.getUserId()).getWeapon().getName()))
                        .replace("{defender_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(t.getUserId()).getWeapon().getName()))
                        .replace("{@attacker}", p.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{@defender}", t.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{attacker_tag}", p.getUser().getAsTag())
                        .replace("{defender_tag}", t.getUser().getAsTag())
                        .replace("{defender_damage}", "`" + defender_damage_dealt + "`")
                        .replace("{attacker_damage}", "`" + attacker_damage_dealt + "`")
                        .replace("{defender_health}", "`" + defender_health_left + "` ")
                        .replace("{attacker_health}", "`" + attacker_health_left + "` ")
                        .replace("{attacker_exp}", "" + FormatUtil.formatCommas(attacker_exp))
                        .replace("{attacker_gold}", "" + FormatUtil.formatCommas(attacker_gold))
                        .replace("{attacker_key}", "" + attacker_key)
                        .replace("{attacker_diamonds}", "" + attacker_diamonds)
                        .replace("{attacker_ingots}", "" + attacker_ingots)
                        .replace("{defender_ingots}", "" + defender_ingots)
                        .replace("{rare_wolf}", "" + attacker_rare_wolf)
                        .replace("{epic_wolf}", "" + attacker_epic_wolf)
                        .replace("{defender_exp}", "" + FormatUtil.formatCommas(defender_exp))
                        .replace("{defender_gold}", "" + FormatUtil.formatCommas(defender_gold))
                        .replace("{recovered_health}", String.valueOf(recoveredHealth))
                        .replace("{attacker_xp_emote}", attackerXPEmote)
                        .replace("{defender_xp_emote}", defenderXPEmote)
                        .replace("{word}", word);
                e.reply(MessageUtil.err(title1, message1));
            } else {
                CooldownUtil.addCooldown(p.getUserId(), "pvp_cooldown", System.currentTimeMillis() + (60 * 1000), false);
                long barrierTime = ((2 * 60 * 60 * 1000) + (50 * 60 * 1000)) / 300; //normally 3
                barrierTime += barrierTime += new Random().nextInt(20 * 60 * 1000 + 1) / 5;
                CooldownUtil.addCooldown(t.getUserId(), "pvp_barrier", System.currentTimeMillis() + barrierTime, true);
                String message1 = Language.getLocalized(uid, "pvp_result_win",
                                "{@attacker} Killed {@defender}.\n\n" +

                                        "`{attacker_tag}`\n" +
                                        "• Dealt {attacker_damage} Damage {attacker_emote}\n" +
                                        "• {attacker_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +

                                        "• `{attacker_exp}` XP {attacker_xp_emote} \n" +
                                        "• `{attacker_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{attacker_key}{attacker_diamonds}{attacker_ingots}{rare_wolf}{epic_wolf}\n\n" +

                                        "`{defender_tag}`\n" +
                                        "• Dealt {defender_damage} Damage {defender_emote}\n" +
                                        "• {defender_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +


                                        "• `{defender_exp}` XP {defender_xp_emote} \n" +
                                        "• `{defender_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{defender_ingots}\n" +
                                        "`{defender_tag}` now has a barrier.")
                        .replace("{attacker_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(p.getUserId()).getWeapon().getName()))
                        .replace("{defender_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(t.getUserId()).getWeapon().getName()))
                        .replace("{@attacker}", p.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{@defender}", t.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{attacker_tag}", p.getUser().getAsTag())
                        .replace("{defender_tag}", t.getUser().getAsTag())
                        .replace("{defender_damage}", "`" + defender_damage_dealt + "`")
                        .replace("{attacker_damage}", "`" + attacker_damage_dealt + "`")
                        .replace("{defender_health}", "`" + defender_health_left + "` ")
                        .replace("{attacker_health}", "`" + attacker_health_left + "` ")
                        .replace("{attacker_exp}", "" + FormatUtil.formatCommas(attacker_exp))
                        .replace("{attacker_gold}", "" + FormatUtil.formatCommas(attacker_gold))
                        .replace("{attacker_key}", "" + attacker_key)
                        .replace("{attacker_diamonds}", "" + attacker_diamonds)
                        .replace("{attacker_ingots}", "" + attacker_ingots)
                        .replace("{defender_ingots}", "" + defender_ingots)
                        .replace("{rare_wolf}", "" + attacker_rare_wolf)
                        .replace("{epic_wolf}", "" + attacker_epic_wolf)
                        .replace("{defender_exp}", "" + FormatUtil.formatCommas(defender_exp))
                        .replace("{attacker_xp_emote}", attackerXPEmote)
                        .replace("{defender_xp_emote}", defenderXPEmote)
                        .replace("{defender_gold}", "" + FormatUtil.formatCommas(defender_gold));
                e.reply(MessageUtil.success(title1, message1));
            }
        } else {
            if (tWin) {
                CooldownUtil.addCooldown(p.getUserId(), "pvp_cooldown", System.currentTimeMillis() + (60 * 1000), true);
                int maxHealthP = p.getHealthUser().getMaxHealth(); // 10
                int recoveredHealth = FormatUtil.round(maxHealthP * 0.15); // 2
                int newHealthP = defender_health_after + recoveredHealth; // 9 + 2
                if (newHealthP > maxHealthP) { // 11 > 10
                    recoveredHealth = maxHealthP - defender_health_after; // 10-9
                    newHealthP = maxHealthP; // 1
                }
                String word = "";
                if (recoveredHealth >= 0) word = "recovered";
                if (recoveredHealth < 0) word = "lost";
                t.getHealthUser().setHealth(newHealthP);
                String message1 = Language.getLocalized(uid, "pvp_result_lose",
                                "{@attacker} Died against {@defender}.\n\n" +

                                        "`{attacker_tag}`\n" +
                                        "• Dealt {attacker_damage} Damage {attacker_emote}\n" +
                                        "• {attacker_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +
                                        "• `{attacker_exp}` XP {attacker_xp_emote} \n" +
                                        "• `{attacker_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{attacker_key}{attacker_diamonds}{attacker_ingots}{rare_wolf}{epic_wolf}\n\n" +

                                        "`{defender_tag}`\n" +
                                        "• Dealt {defender_damage} Damage {defender_emote}\n" +
                                        "• {defender_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +


                                        "• `{defender_exp}` XP {defender_xp_emote} \n" +
                                        "• `{defender_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{defender_ingots}\n" +
                                        "`{defender_tag}` {word} `{recovered_health}` health.")
                        .replace("{attacker_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(p.getUserId()).getWeapon().getName()))
                        .replace("{defender_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(t.getUserId()).getWeapon().getName()))
                        .replace("{@attacker}", p.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{@defender}", t.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{attacker_tag}", p.getUser().getAsTag())
                        .replace("{defender_tag}", t.getUser().getAsTag())
                        .replace("{defender_damage}", "`" + defender_damage_dealt + "`")
                        .replace("{attacker_damage}", "`" + attacker_damage_dealt + "`")
                        .replace("{defender_health}", "`" + defender_health_left + "` ")
                        .replace("{attacker_health}", "`" + attacker_health_left + "` ")
                        .replace("{attacker_exp}", "" + FormatUtil.formatCommas(attacker_exp))
                        .replace("{attacker_gold}", "" + FormatUtil.formatCommas(attacker_gold))
                        .replace("{attacker_key}", "" + attacker_key)
                        .replace("{attacker_diamonds}", "" + attacker_diamonds)
                        .replace("{attacker_ingots}", "" + attacker_ingots)
                        .replace("{defender_ingots}", "" + defender_ingots)
                        .replace("{rare_wolf}", "" + attacker_rare_wolf)
                        .replace("{epic_wolf}", "" + attacker_epic_wolf)
                        .replace("{defender_exp}", "" + FormatUtil.formatCommas(defender_exp))
                        .replace("{defender_gold}", "" + FormatUtil.formatCommas(defender_gold))
                        .replace("{recovered_health}", String.valueOf(recoveredHealth))
                        .replace("{attacker_xp_emote}", attackerXPEmote)
                        .replace("{defender_xp_emote}", defenderXPEmote)
                        .replace("{word}", word);
                e.reply(MessageUtil.err(title1, message1));
            } else {
                CooldownUtil.addCooldown(p.getUserId(), "pvp_cooldown", System.currentTimeMillis() + (60 * 1000), false);
                long barrierTime = ((2 * 60 * 60 * 1000) + (50 * 60 * 1000)) / 300; //normally 3
                barrierTime += barrierTime += new Random().nextInt(20 * 60 * 1000 + 1) / 5;
                CooldownUtil.addCooldown(t.getUserId(), "pvp_barrier", System.currentTimeMillis() + barrierTime, true);
                String message1 = Language.getLocalized(uid, "pvp_result_win",
                                "{@attacker} Killed {@defender}.\n\n" +

                                        "`{attacker_tag}`\n" +
                                        "• Dealt {attacker_damage} Damage {attacker_emote}\n" +
                                        "• {attacker_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +

                                        "• `{attacker_exp}` XP {attacker_xp_emote} \n" +
                                        "• `{attacker_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{attacker_key}{attacker_diamonds}{attacker_ingots}{rare_wolf}{epic_wolf}\n\n" +

                                        "`{defender_tag}`\n" +
                                        "• Dealt {defender_damage} Damage {defender_emote}\n" +
                                        "• {defender_health} Remaining HP " + EmoteUtil.getEmoteMention("HP") + "\n\n" +


                                        "• `{defender_exp}` XP {defender_xp_emote} \n" +
                                        "• `{defender_gold}` Gold Coins " + EmoteUtil.getCoin() + "\n" +
                                        "{defender_ingots}\n" +
                                        "`{defender_tag}` now has a barrier.")
                        .replace("{attacker_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(p.getUserId()).getWeapon().getName()))
                        .replace("{defender_emote}", EmoteUtil.getEmoteMention(ArmorUser.getArmorUser(t.getUserId()).getWeapon().getName()))
                        .replace("{@attacker}", p.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{@defender}", t.getUser().getAsMention().replace("`", "").replace("*", "").replace("||", "").replace("||", ">"))
                        .replace("{attacker_tag}", p.getUser().getAsTag())
                        .replace("{defender_tag}", t.getUser().getAsTag())
                        .replace("{defender_damage}", "`" + defender_damage_dealt + "`")
                        .replace("{attacker_damage}", "`" + attacker_damage_dealt + "`")
                        .replace("{defender_health}", "`" + defender_health_left + "` ")
                        .replace("{attacker_health}", "`" + attacker_health_left + "` ")
                        .replace("{attacker_exp}", "" + FormatUtil.formatCommas(attacker_exp))
                        .replace("{attacker_gold}", "" + FormatUtil.formatCommas(attacker_gold))
                        .replace("{attacker_key}", "" + attacker_key)
                        .replace("{attacker_diamonds}", "" + attacker_diamonds)
                        .replace("{attacker_ingots}", "" + attacker_ingots)
                        .replace("{defender_ingots}", "" + defender_ingots)
                        .replace("{rare_wolf}", "" + attacker_rare_wolf)
                        .replace("{epic_wolf}", "" + attacker_epic_wolf)
                        .replace("{defender_exp}", "" + FormatUtil.formatCommas(defender_exp))
                        .replace("{attacker_xp_emote}", attackerXPEmote)
                        .replace("{defender_xp_emote}", defenderXPEmote)
                        .replace("{defender_gold}", "" + FormatUtil.formatCommas(defender_gold));
                e.reply(MessageUtil.success(title1, message1));
            }
        }
            DiminishingUtil.pushDiminish(p.getUserId(), t.getUserId());
            CaptchaAPI.doCaptchaAction(e.getTextChannel(), p.getUserId(), p.getUser().getAsMention());
        }

    private void updateStats(String p, String t, String w) {
        if (w.equals(p)) {
            // Attacker wins
            int attacker_kills = Integer.parseInt(StatsUtil.getStat(p, "attack_kills", "0"));
            attacker_kills++;
            StatsUtil.setStat(p, "attack_kills", String.valueOf(attacker_kills));
            // Defender loses
            int defender_deaths = Integer.parseInt(StatsUtil.getStat(t, "defend_deaths", "0"));
            defender_deaths++;
            StatsUtil.setStat(t, "defend_deaths", String.valueOf(defender_deaths));
        } else {
            // Defender wins
            int defender_kills = Integer.parseInt(StatsUtil.getStat(t, "defend_kills", "0"));
            defender_kills++;
            StatsUtil.setStat(t, "defend_kills", String.valueOf(defender_kills));
            // Attacker loses
            int attacker_deaths = Integer.parseInt(StatsUtil.getStat(p, "attack_deaths", "0"));
            attacker_deaths++;
            StatsUtil.setStat(p, "attack_deaths", String.valueOf(attacker_deaths));
        }
        int pkills1 = Integer.parseInt(StatsUtil.getStat(p, "attack_kills", "0"));
        int pkills2 = Integer.parseInt(StatsUtil.getStat(p, "defend_kills", "0"));
        int ptotalKills = pkills1 + pkills2;
        int pdeaths1 = Integer.parseInt(StatsUtil.getStat(p, "attack_deaths", "0"));
        int pdeaths2 = Integer.parseInt(StatsUtil.getStat(p, "defend_deaths", "0"));
        int ptotalDeaths = pdeaths1 + pdeaths2;
        double pkdKills;
        double pkdDeaths;
        if (ptotalDeaths == 0) {
            pkdDeaths = 1.0;
        } else {
            pkdDeaths = ptotalDeaths;
        }
        if (ptotalKills == 0) {
            pkdKills = 1.0;
        } else {
            pkdKills = ptotalKills;
        }
        double pkd = round(pkdKills/pkdDeaths, 2);
        StatsUtil.setStat(p, "kd", String.valueOf(round(pkd, 2)));

        int tkills1 = Integer.parseInt(StatsUtil.getStat(t, "attack_kills", "0"));
        int tkills2 = Integer.parseInt(StatsUtil.getStat(t, "defend_kills", "0"));
        int ttotalKills = tkills1 + tkills2;
        int tdeaths1 = Integer.parseInt(StatsUtil.getStat(t, "attack_deaths", "0"));
        int tdeaths2 = Integer.parseInt(StatsUtil.getStat(t, "defend_deaths", "0"));
        int ttotalDeaths = tdeaths1 + tdeaths2;
        double tkdKills;
        double tkdDeaths;
        if (ttotalDeaths == 0) {
            tkdDeaths = 1.0;
        } else {
            tkdDeaths = ttotalDeaths;
        }
        if (ttotalKills == 0) {
            tkdKills = 1.0;
        } else {
            tkdKills = ttotalKills;
        }
        double tkd = round(tkdKills/tkdDeaths, 2);
        StatsUtil.setStat(t, "kd", String.valueOf(round(tkd, 2)));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}