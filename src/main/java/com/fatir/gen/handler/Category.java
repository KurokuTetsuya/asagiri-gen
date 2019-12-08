package com.fatir.gen.handler;

import com.fatir.gen.Asagiri;
import com.fatir.gen.modules.categories.MusicCategory;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import java.util.ArrayList;

import static com.fatir.gen.handler.CommandHandler.cmdClient;


public abstract class Category extends Command {
    public final Asagiri asagiri;
    public final CommandClient commandClient;
    public final ArrayList<com.fatir.gen.handler.Category> commands;

    public Category(Asagiri asagiri) {
        this.commandClient = cmdClient.build();
        this.asagiri = asagiri;
        this.commands = new ArrayList<>();
    }
}
