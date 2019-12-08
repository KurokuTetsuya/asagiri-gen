package com.fatir.gen;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class Config {
    public static final String TOKEN = "YoUr ToKeN gOeS HeRe";
    public static final boolean NPI_IMAGES = false;
    public static final String PLAY_EMOJI = "\u25B6";
    public static final String PAUSE_EMOJI = "\u23F8";
    public static final String STOP_EMOJI = "\u23F9";
    public static final String ERROR_EMOJI = "\uD83D\uDEAB";
    public static final String SUCCESS_EMOJI = "\uD83C\uDFB6";
    public static final String WARNING_EMOJI = "\uD83D\uDCA1";
    public static final String MUSIC_EMOJI = "\uD83C\uDFB5";
    public static final boolean STAY_IN_VC = false;
    public static final String ownerID = "327586923252285440";
    public static final String[] coOwnersID = {};
    public static final String PREFIX = "a.";
    public static final int MAX_TIME = 0;

    public boolean isTooLong(AudioTrack track) {
        if (getMaxTime() <= 0) return false;
        return Math.round(track.getDuration() / 1000.0) > getMaxTime();
    }

    public int getMaxTime() { return MAX_TIME * 1000; }
    public String getOwnerID() { return ownerID; }
    public String[] getCoOwnersID() { return coOwnersID; }
    public String getPrefix() { return PREFIX; }
    public boolean useNPImages() {
        return NPI_IMAGES;
    }
    public String getSuccess() { return SUCCESS_EMOJI; }
    public String getWarning() { return WARNING_EMOJI; }
    public String getMusic() { return MUSIC_EMOJI; }
    public String getError() { return ERROR_EMOJI; }
    public boolean getStay() {
        return STAY_IN_VC;
    }
}
