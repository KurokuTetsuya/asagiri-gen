package com.fatir.gen;

import com.fatir.gen.handler.CommandHandler;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;

public class Main {
    private static Asagiri asagiri;
    private static Config config;
    public static EventWaiter waiter;

    public static void main(String[] args) throws LoginException, InterruptedException {
        waiter = new EventWaiter(Executors.newSingleThreadScheduledExecutor(), false);
        config = new Config();
        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(Config.TOKEN)
                .build().awaitReady();
        Asagiri asagiri = new Asagiri(waiter, config, jda);
        new CommandHandler(jda, asagiri);
    }
}
