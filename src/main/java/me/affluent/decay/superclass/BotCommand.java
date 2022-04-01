package me.affluent.decay.superclass;

import me.affluent.decay.util.CommandEvent;

public abstract class BotCommand extends Descriptable {

    protected abstract void execute(CommandEvent e);

    protected boolean runnableAsBanned = false;

    protected String name = "null";

    protected String help = "No help available";

    protected boolean ownerCommand = false;

    protected double cooldown = 0;

    protected String[] aliases = new String[0];

    protected boolean hidden = false;

    final void run(CommandEvent event) {
        execute(event);
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public double getCooldown() {
        return cooldown;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isOwnerCommand() {
        return ownerCommand;
    }

    boolean isRunnableAsBanned() {
        return runnableAsBanned;
    }
}