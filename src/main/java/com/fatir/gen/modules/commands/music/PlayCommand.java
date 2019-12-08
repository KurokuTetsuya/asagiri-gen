package com.fatir.gen.modules.commands.music;

import com.fatir.gen.Asagiri;
import com.fatir.gen.audio.AudioHandler;
import com.fatir.gen.audio.QueuedTrack;
import com.fatir.gen.modules.categories.MusicCategory;
import com.fatir.gen.utils.FormatUtil;
import com.fatir.gen.utils.OtherUtil;
import com.fatir.gen.utils.SenderUtil;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.concurrent.TimeUnit;

public class PlayCommand extends MusicCategory {
    private static final String LOAD = "\uD83D\uDCE5";
    private static final String CANCEL = "\uD83D\uDEAB";
    private static String input;

    public PlayCommand(Asagiri asagiri) {
        super(asagiri);
        this.name = "play";
        this.help = "Play music";
        this.botPermissions = new Permission[]{Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty() && event.getMessage().getAttachments().isEmpty()) {
            AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
            if (handler.getPlayer().getPlayingTrack() != null && handler.getPlayer().isPaused()) {
                handler.getPlayer().setPaused(false);
                SenderUtil.reply(
                        event,
                        asagiri.getConfig().getMusic()
                                + " Resumed **"
                                + handler.getPlayer().getPlayingTrack().getInfo().title
                                + "**.");
                return;
            }
            event.reply("Please provide a title");
            return;
        }

        String arg = event.getArgs().startsWith("<") && event.getArgs().endsWith(">")
                ? event.getArgs().substring(1, event.getArgs().length() - 1)
                : event.getArgs().isEmpty() ? event.getMessage().getAttachments().get(0).getUrl() : event.getArgs();
        event
                .getChannel()
                .sendMessage(" Loading... `[" + arg + "]`")
                .queue(
                        m ->
                                asagiri
                                        .getPlayerManager()
                                        .loadItemOrdered(event.getGuild(), arg, new ResultHandler(m, event, false)));
    }

        private class ResultHandler implements AudioLoadResultHandler {
            private final Message m;
            private final CommandEvent event;
            private final boolean ytsearch;

            private ResultHandler(Message m, CommandEvent event, boolean ytsearch) {
                this.m = m;
                this.event = event;
                this.ytsearch = ytsearch;
            }

            private void loadSingle(AudioTrack track, AudioPlaylist playlist) {
                if (asagiri.getConfig().isTooLong(track)) {
                    m.editMessage(
                            FormatUtil.filterEveryone(
                                    asagiri.getConfig().getWarning()
                                            + " This track (**"
                                            + track.getInfo().title
                                            + "**) is longer than the allowed maximum: `"
                                            + FormatUtil.formatTime(track.getDuration())
                                            + "` > `"
                                            + FormatUtil.formatTime(asagiri.getConfig().getMaxTime())
                                            + "`"))
                            .queue();
                    return;
                }
                AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                handler.setAnnouncingChannel(event.getChannel().getIdLong());
                int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor())) + 1;
                String addMsg =
                        FormatUtil.filterEveryone(
                                asagiri.getConfig().getMusic()
                                        + " Added **"
                                        + track.getInfo().title
                                        + "** (`"
                                        + FormatUtil.formatTime(track.getDuration())
                                        + "`) "
                                        + (pos == 0 ? "" : " to the queue at position " + pos));
                if (playlist == null
                        || !event
                        .getGuild()
                        .getSelfMember()
                        .hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION))
                    m.editMessage(addMsg)
                            .queue(
                                    (m) -> {
                                        OtherUtil.deleteMessageAfter(m, track.getDuration());
                                    });
                else {
                    new ButtonMenu.Builder()
                            .setText(
                                    addMsg
                                            + "\n"
                                            + asagiri.getConfig().getWarning()
                                            + " This track has a playlist of **"
                                            + playlist.getTracks().size()
                                            + "** tracks attached. Select "
                                            + LOAD
                                            + " to load playlist.")
                            .setChoices(LOAD, CANCEL)
                            .setEventWaiter(asagiri.getWaiter())
                            .setTimeout(30, TimeUnit.SECONDS)
                            .setAction(
                                    re -> {
                                        if (re.getName().equals(LOAD))
                                            m.editMessage(
                                                    addMsg
                                                            + "\n"
                                                            + asagiri.getConfig().getSuccess()
                                                            + " Loaded **"
                                                            + loadPlaylist(playlist, track)
                                                            + "** additional tracks!")
                                                    .queue();
                                        else m.editMessage(addMsg).queue();
                                    })
                            .setFinalAction(
                                    m -> {
                                        try {
                                            m.clearReactions().queue();
                                        } catch (PermissionException ignore) {
                                        }
                                    })
                            .build()
                            .display(m);
                }
            }

            private int loadPlaylist(AudioPlaylist playlist, AudioTrack exclude) {
                int[] count = {0};
                playlist
                        .getTracks()
                        .stream()
                        .forEach(
                                (track) -> {
                                    if (!asagiri.getConfig().isTooLong(track) && !track.equals(exclude)) {
                                        AudioHandler handler =
                                                (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                                        handler.setAnnouncingChannel(event.getChannel().getIdLong());
                                        handler.addTrack(new QueuedTrack(track, event.getAuthor()));
                                        count[0]++;
                                    }
                                });
                return count[0];
            }

            @Override
            public void trackLoaded(AudioTrack track) {
                loadSingle(track, null);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getTracks().size() == 1 || playlist.isSearchResult()) {
                    AudioTrack single =
                            playlist.getSelectedTrack() == null
                                    ? playlist.getTracks().get(0)
                                    : playlist.getSelectedTrack();
                    loadSingle(single, null);
                } else if (playlist.getSelectedTrack() != null) {
                    AudioTrack single = playlist.getSelectedTrack();
                    loadSingle(single, playlist);
                } else {
                    int count = loadPlaylist(playlist, null);
                    if (count == 0) {
                        m.editMessage(
                                FormatUtil.filterEveryone(
                                        asagiri.getConfig().getWarning()
                                                + " All entries in this playlist "
                                                + (playlist.getName() == null ? "" : "(**" + playlist.getName() + "**) ")
                                                + "were longer than the allowed maximum (`"
                                                + asagiri.getConfig().getMaxTime()
                                                + "`)"))
                                .queue();
                    } else {
                        m.editMessage(
                                FormatUtil.filterEveryone(
                                        asagiri.getConfig().getSuccess()
                                                + " Found "
                                                + (playlist.getName() == null
                                                ? "a playlist"
                                                : "playlist **" + playlist.getName() + "**")
                                                + " with `"
                                                + playlist.getTracks().size()
                                                + "` entries; added to the queue!"
                                                + (count < playlist.getTracks().size()
                                                ? "\n"
                                                + asagiri.getConfig().getWarning()
                                                + " Tracks longer than the allowed maximum (`"
                                                + asagiri.getConfig().getMaxTime()
                                                + "`) have been omitted."
                                                : "")))
                                .queue();
                    }
                }
            }

            @Override
            public void noMatches() {
                if (ytsearch)
                    m.editMessage(
                            FormatUtil.filterEveryone(
                                    asagiri.getConfig().getWarning() + " No results found for `" + input + "`."))
                            .queue();
                else
                    asagiri
                            .getPlayerManager()
                            .loadItemOrdered(
                                    event.getGuild(), "ytsearch:" + input, new ResultHandler(m, event, true));
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                if (throwable.severity == FriendlyException.Severity.COMMON)
                    m.editMessage(asagiri.getConfig().getError() + " Error loading: " + throwable.getMessage())
                            .queue();
                else m.editMessage(asagiri.getConfig().getError() + " Error loading track.").queue();
            }
        }
    }