package com.fatir.gen.utils;

import com.fatir.gen.Config;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SenderUtil {
    public static void sendDM(User user, String message) {
        try {
            user.openPrivateChannel().queue(pc -> pc.sendMessage(message).queue());
        } catch (Exception ignore) {
        }
    }

    public static void reply(CommandEvent event, String message) {
        try {
            event.getChannel().sendMessage(message).queue();
        } catch (Exception ignore) {
        }
    }

    public static void replySuccess(MessageReceivedEvent event, String message) {
        try {
            event.getChannel().sendMessage(Config.SUCCESS_EMOJI + " " + message).queue();
        } catch (Exception ignore) {
        }
    }

    public static void replyWarning(MessageReceivedEvent event, String message) {
        try {
            event.getChannel().sendMessage(Config.WARNING_EMOJI + " " + message).queue();
        } catch (Exception ignore) {
        }
    }

    public static void replyError(MessageReceivedEvent event, String message) {
        try {
            event.getChannel().sendMessage(Config.WARNING_EMOJI + " " + message).queue();
        } catch (Exception ignore) {
        }
    }
}