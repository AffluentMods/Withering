package me.affluent.decay.superclass;

import net.dv8tion.jda.api.JDA;

public interface ShardTask {
    void run(JDA shard);
}