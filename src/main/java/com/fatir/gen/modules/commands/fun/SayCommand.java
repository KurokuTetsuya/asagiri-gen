package com.fatir.gen.modules.commands.fun;

import com.fatir.gen.Asagiri;
import com.fatir.gen.modules.categories.FunCategory;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class SayCommand extends FunCategory {
    public SayCommand(Asagiri asagiri) {
        super(asagiri);
        this.name = "say";
        this.aliases = new String[]{"echo"};
        this.help = "i'll say what are you said";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply("what should i say?");
            return;
        }
        event.reply(event.getArgs());
        try {
            event.getMessage().delete().queue();
        } catch (InsufficientPermissionException ignored) {

        }
    }
}