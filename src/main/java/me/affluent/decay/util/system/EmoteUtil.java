package me.affluent.decay.util.system;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EmoteUtil {

    private static final HashMap<String, String> cache = new HashMap<>();
    private static List<Guild> officialGuilds = null;

    private static void loadEmoteGuilds() {
        if (officialGuilds == null) {
            ShardManager shardManager = Withering.getBot().getShardManager();
            officialGuilds = Arrays.asList(shardManager.getGuildById("919975908843061258"), shardManager.getGuildById("919964467876483102"),
                    shardManager.getGuildById("919964530958807051"), shardManager.getGuildById("919964657660334181"), shardManager.getGuildById("919964960396824576"),
                    shardManager.getGuildById("919965264504827964"), shardManager.getGuildById("919965341260607598"), shardManager.getGuildById("919965388404555796"),
                    shardManager.getGuildById("919965445304487956"), shardManager.getGuildById("919965499591389214"), shardManager.getGuildById("919965703094820904"),
                    shardManager.getGuildById("919965769285120000"), shardManager.getGuildById("919965820925407313"), shardManager.getGuildById("919965878865494117"),
                    shardManager.getGuildById("919965921664172092"), shardManager.getGuildById("919978184949268500"), shardManager.getGuildById("919966097388740628"),
                    shardManager.getGuildById("919966152153759764"), shardManager.getGuildById("919966198563737630"), shardManager.getGuildById("919966252657680434"),
                    shardManager.getGuildById("894771076133978132"), shardManager.getGuildById("894771113077407804"), shardManager.getGuildById("894771150415093830"),
                    shardManager.getGuildById("894771185148129310"), shardManager.getGuildById("907027474926432286"), shardManager.getGuildById("907030461811925079"),
                    shardManager.getGuildById("919967022568337439"), shardManager.getGuildById("919967207709114379"), shardManager.getGuildById("919967097298243634"),
                    shardManager.getGuildById(Constants.main_guild));
        }
    }

    public static String getEmoteMention(String name, List<String> replaceAll, List<String> with) {
        loadEmoteGuilds();
        if (replaceAll != null && with != null && replaceAll.size() > 0 && with.size() > 0) {
            if (replaceAll.size() != with.size()) {
                System.out.println("[INTERN ERR] Error: replaceAll size != with size | getEmoteMention(...)");
                return "[ERR]";
            }
            for (int i = 0; i < replaceAll.size(); i++) {
                String arg = replaceAll.get(i);
                String argWith = with.get(i);
                name = name.replaceAll(arg, argWith);
            }
        }
        if (cache.containsKey(name)) return cache.get(name);
        Emote emote = null;
        for (Guild guild : officialGuilds) {
            if (guild == null) continue;
            List<Emote> emotes = guild.getEmotesByName(name, true);
            if (emotes.size() > 0) {
                emote = emotes.get(0);
                break;
            }
        }
        if (emote == null) {
            System.out.println(name + " emote is null");
            return Withering.test ? "[TE=N]" : "";
        }
        cache.put(name.replaceAll(" ", "_"), emote.getAsMention());
        return emote.getAsMention();
    }

    private static Emote getEmote(String name) {
        loadEmoteGuilds();
        for (Guild guild : officialGuilds) {
            if (guild == null) continue;
            List<Emote> emotes = guild.getEmotesByName(name, true);
            if (emotes.size() > 0) {
                return emotes.get(0);
            }
        }
        return null;
    }

    public static String getEmoteMentionOrDefaultAsString(String name, String defaultName) {
        Emote emote = getEmote(name);
        if (emote == null) return defaultName;
        return emote.getAsMention();
    }

    public static String getEmoteMention(String name) {
        return getEmoteMention(name, Arrays.asList(" ", "-"), Arrays.asList("_", "_"));
    }

    public static String getCoin() {
        return getEmoteMention("gold_coin");
    }
    public static String getDiamond() {
        return getEmoteMention("Diamond");
    }

    public static void clearCache() {
        cache.clear();
    }
}