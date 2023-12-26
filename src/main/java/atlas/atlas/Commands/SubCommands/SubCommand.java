package atlas.atlas.Commands.SubCommands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract void execute(Player p, String[] args);

    public abstract void help(Player p);

    public abstract String permission();

}
