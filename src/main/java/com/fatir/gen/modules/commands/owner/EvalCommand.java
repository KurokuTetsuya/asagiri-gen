package com.fatir.gen.modules.commands.owner;

import com.fatir.gen.Asagiri;
import com.fatir.gen.modules.categories.OwnerCategory;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalCommand extends OwnerCategory {
    public EvalCommand(Asagiri asagiri) {
        super(asagiri);
        this.name = "eval";
        this.aliases = new String[]{"evaluate", "ev", "e"};
        this.help = "evaluating";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply("what do you want?");
            return;
        }
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.put("event", event);
            engine.put("client", event.getJDA());
            engine.put("cmdClient", commandClient);
            engine.put("asagiri", asagiri);
            engine.put("command", this);
            engine.put("message", event.getMessage());
            Object evaled = engine.eval(event.getArgs());
            event.reply("<:yes:615730280619048970> **Output**\n```java\n" + evaled + "\n```");
        } catch (Exception e) {
            event.reply("<:no:615730302936940554> **Error**\n```java\n" + e.getMessage() + "\n```");
        }
    }
}