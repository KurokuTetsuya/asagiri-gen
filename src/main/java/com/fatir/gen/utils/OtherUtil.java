package com.fatir.gen.utils;

import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class OtherUtil {
    public static void deleteMessageAfter(Message message, long delay) {
        message.delete().queueAfter(delay, TimeUnit.MILLISECONDS);
    }
}
