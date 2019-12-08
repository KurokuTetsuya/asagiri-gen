package com.fatir.gen.handler;

import com.fatir.gen.Asagiri;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class CommandHandler {
    public static CommandClientBuilder cmdClient;
    public static EventWaiter waiter;
    public static ArrayList<Category> categories;

    public CommandHandler(JDA client, Asagiri asagiri) {
        categories = new ArrayList<>();
        waiter = new EventWaiter();
        cmdClient = new CommandClientBuilder()
                .setOwnerId(asagiri.getConfig().getOwnerID())
                .setCoOwnerIds(asagiri.getConfig().getCoOwnersID())
                .setPrefix(asagiri.getConfig().getPrefix())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("In Development!"))
                .useHelpBuilder(false);

        new Reflections("com.fatir.gen.modules.categories")
                .getSubTypesOf(Category.class)
                .forEach(c -> {
                    try {
                        categories.add(c.getConstructor(asagiri.getClass()).newInstance(asagiri));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        client.addEventListener(waiter);
        client.addEventListener(cmdClient.build());
    }
}