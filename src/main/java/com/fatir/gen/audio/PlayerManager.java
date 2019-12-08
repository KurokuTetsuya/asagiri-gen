package com.fatir.gen.audio;

import com.fatir.gen.Asagiri;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;

public class PlayerManager extends DefaultAudioPlayerManager {
    private final Asagiri asagiri;

    public PlayerManager(Asagiri asagiri) {
        this.asagiri = asagiri;
    }

    public void init() {
        AudioSourceManagers.registerRemoteSources(this);
        AudioSourceManagers.registerLocalSource(this);
        source(YoutubeAudioSourceManager.class).setPlaylistPageCount(10);
    }

    public Asagiri getBot() {
        return asagiri;
    }

    public boolean hasHandler(Guild guild) {
        return guild.getAudioManager().getSendingHandler() != null;
    }

    public AudioHandler setUpHandler(Guild guild) {
        AudioHandler handler;
        if (guild.getAudioManager().getSendingHandler() == null) {
            AudioPlayer player = createPlayer();
            player.setVolume(100);
            handler = new AudioHandler(this, guild, player);
            player.addListener(handler);
            guild.getAudioManager().setSendingHandler(handler);
        } else handler = (AudioHandler) guild.getAudioManager().getSendingHandler();
        return handler;
    }
}