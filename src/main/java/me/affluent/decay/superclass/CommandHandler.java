package me.affluent.decay.superclass;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.command.actions.*;
import me.affluent.decay.command.admin.AdminCommand;
import me.affluent.decay.command.admin.DevCommand;
import me.affluent.decay.command.information.*;
import me.affluent.decay.command.other.GiveAllCommand;
import me.affluent.decay.command.other.VerifyCommand;
import me.affluent.decay.command.quest.JournalEntryCommand;
import me.affluent.decay.command.quest.ConquestCommand;
import me.affluent.decay.command.shop.ElixirCommand;
import me.affluent.decay.command.shop.MerchantCommand;
import me.affluent.decay.command.utility.*;
import me.affluent.decay.entity.Player;
import me.affluent.decay.entity.PrefixUser;
import me.affluent.decay.manager.BanManager;
import me.affluent.decay.util.CommandEvent;
import me.affluent.decay.util.system.CooldownUtil;
import me.affluent.decay.util.system.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandHandler extends ListenerAdapter {

    private static Map<String, BotCommand> commands = new TreeMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(300);
    private static String selfPrefix = null;

    public static void load() {
        /* REGISTER COMMANDS */
        List<BotCommand> allCommands =
                Arrays.asList(new StartCommand(), new ProfileCommand(), new AdminCommand(), new DevCommand(),
                        new ArenaCommand(), new VoteCommand(), new CooldownsCommand(), new AttributesCommand(),
                        new OpenCommand(), new KeysCommand(), new InfoCommand(), new EquipCommand(),
                        new UnequipCommand(), new StatsCommand(), new MerchantCommand(), new PrefixCommand(),
                        new TopCommand(), new VerifyCommand(), new PracticeCommand(), new JournalEntryCommand(),
                        new ConquestCommand(), new RandomCommand(), new HelpCommand(), new InviteCommand(),
                        new DeleteCommand(), new TradeCommand(), new PingCommand(), new ScrollCommand(),
                        new DailyCommand(), new ElixirCommand(), new EventCommand(), new TavernCommand(),
                        new HealCommand(), new BenefitsCommand(), new DonateCommand(),
                        new CompareCommand(), new TutorialCommand(), new CodeCommand(), new AchievementsCommand(),
                        new SettingsCommand(), new RulesCommand(), new MineCommand(), new ForgeCommand(),
                        new ProfileIronCommand(), new ProfileDragonCommand(), new ProfileWitherCommand(),
                        new ItemLevelingCommand(), new ItemStarringCommand(), new GoldenTavernCommand(),
                        new GemCommand(), new PetInventoryCommand(), new LockCommand(),
                        new UnlockCommand(), new CalendarCommand(), new HolidayCommand(), new GiftCommand(),
                        new FireworkCommand(), new BackpackCommand()
                );
        for (BotCommand a : allCommands) {
            commands.put(a.name, a);
            String[] aliases = a.aliases;
            if (aliases.length > 0) {
                for (String alias : aliases) commands.put(alias, a);
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();
        // blacklisted words filter
        //if (msg.contains())
        // command and user checks
        if (!Withering.fullyLoaded || Withering.shuttingdown) return;
        String cmdprefix = Constants.PREFIX;
        if (!Withering.test) {
            if (Player.playerExists(e.getAuthor().getId()))
                cmdprefix = PrefixUser.getPrefixUser(e.getAuthor().getId()).getPrefix();
        }
        User user = e.getAuthor();
        if (user.isBot()) return;
        if (selfPrefix == null) selfPrefix = "<@!" + e.getJDA().getSelfUser().getId() + ">";
        if (msg.equals(selfPrefix) || msg.equals(selfPrefix + " ")) msg = cmdprefix + "help";
        if (msg.startsWith(selfPrefix + " ")) msg = cmdprefix + msg.substring(selfPrefix.length() + 1); // +1 because of the length of " "
        if (msg.startsWith(selfPrefix)) msg = cmdprefix + msg.substring(selfPrefix.length());
        boolean isCommand = msg.startsWith(cmdprefix) && msg.length() > cmdprefix.length();
        if (!isCommand) return;
        boolean isBot = e.getAuthor().isBot() && !e.getAuthor().getId().equals(e.getJDA().getSelfUser().getId());
        if (isBot) return;

        // command initialization
        String cmd = e.getMessage().getContentRaw().split(" ")[0].substring(cmdprefix.length());
        if (!(commands.containsKey(cmd.toLowerCase()))) return;
        BotCommand bc = commands.get(cmd.toLowerCase());
        // botcommmand checks
        String uid = user.getId();
        if (BanManager.isBanned(uid)) {
            if (!bc.isRunnableAsBanned()) return;
        }
        boolean isAuthorOwner =
                uid.equalsIgnoreCase("439548972181487616") || uid.equalsIgnoreCase("429307019229397002") || uid.equalsIgnoreCase("335051227324743682");
        if (bc.ownerCommand) {
            if (!isAuthorOwner) return;
        }
        if (bc.cooldown > 0) {
            long now = System.currentTimeMillis();
            if (CooldownUtil.hasCooldown(uid, "cmd_cd_" + bc.name.toLowerCase())) {
                long cd = CooldownUtil.getCooldown(uid, "cmd_cd_" + bc.name.toLowerCase());
                if (cd > now) {
                    long diff = cd - now;
                    e.getChannel().sendMessage(MessageUtil.err("Error",
                            "Calm down there warrior, sit by the fire, and sharpen your blade while you\n wait** " + CooldownUtil.format(diff, uid) + "**"))
                            .queue();
                    return;
                } else {
                    CooldownUtil.removeCooldown(uid, "cmd_cd_" + bc.name.toLowerCase());
                }
            } else {
                double cd1 = bc.getCooldown();
                cd1 *= 1000;
                long cd3 = new Double(cd1).longValue();
                CooldownUtil.addCooldown(uid, "cmd_cd_" + bc.name.toLowerCase(), (now + cd3), false);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        CooldownUtil.removeCooldown(uid, "cmd_cd_" + bc.name.toLowerCase());
                    }
                }, cd3);
            }
        }
        // command build
        Runnable runnable = () -> {
            try {
                boolean hasAllPermissions = true;
                List<Permission> needed =
                        Arrays.asList(Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
                                Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS);
                TextChannel tc = e.getChannel();
                Member m = tc.getGuild().getSelfMember();
                List<Permission> allowed = new ArrayList<>(m.getPermissions(e.getChannel()));
                if (!allowed.contains(Permission.ADMINISTRATOR)) {
                    for (Permission need : needed)
                        if (!allowed.contains(need)) {
                            hasAllPermissions = false;
                            break;
                        }
                }
                if (hasAllPermissions) {
                    if (Withering.maintenance && !isAuthorOwner) {
                        e.getChannel().sendMessage(
                                "**Withering** is in __**maintenance**__:\n\n" + Withering.maintenance_message).queue();
                        return;
                    }
                    bc.run(new CommandEvent(e));
                } else {
                    e.getAuthor().openPrivateChannel().queue(pc -> {
                        pc.sendMessage(
                                "I can't chat in this text channel!\nIf you think this is an error, please tell " +
                                        "an administrator to update my permissions!").queue();
                        final StringBuilder needPerms = new StringBuilder();
                        needed.forEach(permission -> needPerms.append(permission.getName()).append(", "));
                        String np = needPerms.toString();
                        if (np.endsWith(", ")) np = np.substring(0, np.length() - 2);
                        pc.sendMessage("I need: " + np).queue();
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        executorService.submit(runnable);
    }
}