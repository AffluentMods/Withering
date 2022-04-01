package me.affluent.decay;

import me.affluent.decay.database.Database;
import me.affluent.decay.listener.BotInviteListener;
import me.affluent.decay.superclass.CommandHandler;
import me.affluent.decay.superclass.ShardTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.annotation.Nonnull;
import java.util.*;

public class Bot extends ListenerAdapter {

    private final Database database;
    private ShardManager shardManager;
    private String token;
    private int shards;

    Bot(boolean test) {
        this.database = new Database();
        this.token =
                test ? "ODg3ODUwNjU1ODYyMTEyMjY4.YUKJWw.hO38lY_s2PoBqq8s96fbW0GVyiU" : "ODg3ODUwNjU1ODYyMTEyMjY4" +
                        ".YUKJWw" +
                        ".hO38lY_s2PoBqq8s96fbW0GVyiU";
        this.shards = test ? 1 : 2;
    }

    public List<JDA> getShards() {
        return shardManager.getShards();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Database getDatabase() {
        return database;
    }

    public void start() {
        try {
            if (database.getCon() == null) {
                System.out.println("[FATAL] Database could not connect! Stopping...");
                System.exit(0);
                return;
            }
            Collection<GatewayIntent> intents = Arrays
                    .asList(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES);
            this.shardManager = DefaultShardManagerBuilder.createDefault(token, intents).disableCache(CacheFlag.VOICE_STATE,
                            CacheFlag.MEMBER_OVERRIDES).setMemberCachePolicy(MemberCachePolicy.ALL).setShardsTotal(shards).setAutoReconnect(true)
                    .setActivity(Activity.watching("loading process")).setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .addEventListeners(this, new CommandHandler(), new BotInviteListener())
                    .build();
            addDoneTask(Withering::updateStatus);
            addDoneTask(() -> {
                this.shardManager.addEventListener();
                Withering.fullyLoaded = true;
            });
            addDoneTask(() -> new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Withering.dblapi.setStats(Withering.getTotalGuilds());
                }
            }, 60 * 1000, 10 * 60 * 1000));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<Runnable> doneTasks = new ArrayList<>();

    void addDoneTask(Runnable runnable) {
        doneTasks.add(runnable);
    }

    public void runShardTask(ShardTask shardTask) {
        for (JDA shard : getShards()) shardTask.run(shard);
    }

    private void loadBot() {
        for (Runnable r : doneTasks) r.run();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        readyShard(event.getJDA());
    }

    private int loadedShards = 0;

    private void readyShard(JDA jda) {
        if (Withering.fullyLoaded) return;
        loadedShards++;
        if (loadedShards == shards) {
            System.out.println("[INTERN INFO] All shards loaded. Loading bot instance...");
            loadBot();
        }
    }
}