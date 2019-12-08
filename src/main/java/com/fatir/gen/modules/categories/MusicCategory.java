package com.fatir.gen.modules.categories;

import com.fatir.gen.Asagiri;
import com.fatir.gen.handler.Category;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import static com.fatir.gen.handler.CommandHandler.cmdClient;

public class MusicCategory extends Category {
    public MusicCategory(Asagiri asagiri) {
        super(asagiri);
        this.category = new Category("Music");
        this.cooldown = 5;

        new Reflections("com.fatir.gen.modules.commands")
                .getSubTypesOf(this.getClass())
                .forEach(c -> {
                    try {
                        MusicCategory cmd = c.getConstructor(asagiri.getClass()).newInstance(asagiri);
                        commands.add(cmd);
                        cmdClient.addCommand(cmd);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void execute(CommandEvent event) {}
}