package me.affluent.decay;

import me.affluent.decay.database.Database;
import me.affluent.decay.entity.Player;
import me.affluent.decay.manager.BanManager;
import me.affluent.decay.patreon.PatreonServer;
import me.affluent.decay.rank.Rank;
import me.affluent.decay.superclass.CommandHandler;
import me.affluent.decay.util.InGameTime;
import me.affluent.decay.util.system.CooldownUtil;
import me.affluent.decay.util.system.LoadUtil;
import me.affluent.decay.vote.VoteServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.*;

public class Withering {

    public static boolean maintenance = false;
    public static String maintenance_message = "The bot is currently in maintenance.";
    public static boolean test = false;
    public static Timer mainTimer;
    public static DiscordBotListAPI dblapi = null;
    private static long started;
    private static long loaded;
    public static long restartAt;
    public static boolean shuttingdown = false;
    public static boolean fullyLoaded = false;

    public static long getLoaded() {
        return loaded;
    }

    public static long getStarted() {
        return started;
    }

    private static Bot bot;

    public static void main(String[] args) {
        if (args.length > 0) test = args[0].equals("devtest");
        started = System.currentTimeMillis();
        restartAt = started + (24 * 60 * 60 * 1000);
        start(test);
    }

    public static int getTotalGuilds() {
        int tg = 0;
        for (JDA jda : Withering.getBot().getShards()) tg += jda.getGuilds().size();
        return tg;
    }

    public static int getTotalUsers() {
        int tu = 0;
        for (JDA jda : Withering.getBot().getShards()) tu += jda.getUsers().size();
        return tu;
    }

    private static void start(boolean test) {
        try {
            mainTimer = new Timer(true);
            bot = new Bot(test);
            bot.start();
            dblapi = new DiscordBotListAPI.Builder().botId(Constants.dbl_botid).token(Constants.dbl_token).build();
            registerListener();
            bot.addDoneTask(() -> new Timer().schedule(new TimerTask() {
                int lastShardRestarted = -1;

                @Override
                public void run() {
                    if (shuttingdown) {
                        this.cancel();
                        return;
                    }
                    long now = System.currentTimeMillis();
                    if (now > restartAt) {
                        doStop();
                        return;
                    }
                    lastShardRestarted++;
                    if (lastShardRestarted >= bot.getShardManager().getShardsTotal()) lastShardRestarted = 0;
                    bot.getShardManager().restart(lastShardRestarted);
                    System.out.println("Restarting shard #" + lastShardRestarted);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Withering.updateStatus(
                                        Objects.requireNonNull(bot.getShardManager().getShardById(lastShardRestarted))
                                                .awaitReady());
                            } catch (InterruptedException e) {
                                System.out.println(
                                        "Error restarting shard " + lastShardRestarted + ": " + e.getMessage());
                            }
                        }
                    }, 15000);
                }
            }, 5 * 60 * 1000, 5 * 60 * 1000));
            Runnable doneTask = () -> {
                LoadUtil.load();
                CooldownUtil.loadCooldowns();
                runTimers();
                CommandHandler.load();
                loaded = System.currentTimeMillis();
            };
            bot.addDoneTask(doneTask);
            Scanner scanner = new Scanner(System.in);
            Thread thread = new Thread(() -> {
                while (scanner.hasNextLine()) {
                    String command = scanner.nextLine();
                    if (command.equalsIgnoreCase("stop")) {
                        doStop();
                        return;
                    }
                }
            });
            thread.setName("Console Command Handler");
            thread.setDaemon(true);
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void doStop() {
        if (shuttingdown) return;
        shuttingdown = true;
        System.out.println("[INTERN STOP] Cancelling timer...");
        mainTimer.cancel();
        System.out.println("[INTERN STOP] Stopping VoteServer...");
        VoteServer.stop();
        System.out.println("[INTERN STOP] Stopping PatreonServer...");
        PatreonServer.stop();
        System.out.println("[INTERN STOP] Shutting down shard manager...");
        getBot().getShardManager().shutdown();
        System.out.println("[INTERN STOP] Disconnecting database...");
        getBot().getDatabase().disconnect();
        System.exit(0);
    }

    private static void registerListener() {
    }

    private static void runTimers() {
        BanManager.checkBans();
        InGameTime.load();
        InGameTime.updateTime();
        System.out.println("[INTERN INFO] Started Game Time");
    }

    public static Bot getBot() {
        return bot;
    }

    public static TextChannel getBotLog() {
        //This would be considered the Server log
        return getBot().getShardManager().getTextChannelById("889385842446041138");
    }

    public static TextChannel getModLog() {
        //This would be considered the bot log
        return getBot().getShardManager().getTextChannelById("889384767601115156");
    }

    public static Guild getHub() {
        return getBot().getShardManager().getGuildById(Constants.main_guild);
    }

    public static void updateStatus(JDA jda) {
        String prefix = Constants.PREFIX;
        getBot().getShardManager().setPresence(OnlineStatus.ONLINE, Activity.watching(
                prefix + "help | " + getTotalGuilds() + " servers [#" + (jda.getShardInfo().getShardId() + 1) + "]"));
    }

    public static void updateStatus() {
        Withering.getBot().runShardTask(Withering::updateStatus);
    }

    public static void updateRole(String userId, String roleName) {
        Guild hub = getHub();
        if (hub.isMember(Objects.requireNonNull(Player.getUser(userId)))) {
            Member m = hub.getMemberById(userId);
            List<Role> toRemove = new ArrayList<>();

            for (Role role : m.getRoles()) if (Rank.getRank(role.getName()) != null) toRemove.add(role);
            List<Role> roles = hub.getRolesByName(roleName, true);
            if (roles.size() > 0) {
                Role role = roles.get(0);
                hub.modifyMemberRoles(m, Collections.singletonList(role), toRemove).queue();
            }
        }
    }
}
