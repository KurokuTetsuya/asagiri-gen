package com.fatir.gen;

import com.fatir.gen.audio.NowplayingHandler;
import com.fatir.gen.audio.PlayerManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import net.dv8tion.jda.api.JDA;

import java.time.OffsetDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Asagiri {
    private final Config config;
    private JDA jda;
    private final ScheduledExecutorService threadpool;
    private final EventWaiter waiter;
    private final OffsetDateTime readyAt = OffsetDateTime.now();
    private final PlayerManager players;
    private final NowplayingHandler nowplaying;

    public Asagiri(EventWaiter waiter, Config configuration, JDA jda) {
        this.jda = jda;
        this.config = configuration;
        this.waiter = waiter;
        this.threadpool = Executors.newSingleThreadScheduledExecutor();
        this.players = new PlayerManager(this);
        this.players.init();
        this.nowplaying = new NowplayingHandler(this);
        this.nowplaying.init();
        players.setFrameBufferDuration(300);
        players.getConfiguration().setFilterHotSwapEnabled(true);
        players.getConfiguration().setOpusEncodingQuality(10);
        players.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
    }

    public PlayerManager getPlayerManager() {
        return players;
    }
    public EventWaiter getWaiter() {
        return waiter;
    }
    public ScheduledExecutorService getThreadpool() {
        return threadpool;
    }
    public JDA getJDA() { return jda; }
    public Config getConfig() {
        return config;
    }
}
