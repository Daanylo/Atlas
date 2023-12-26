package atlas.atlas.Commands;

import atlas.atlas.Commands.SubCommands.Market.MarketCreateCommand;
import atlas.atlas.Commands.SubCommands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MarketCommandHandler implements CommandExecutor {

    public HashMap<String, SubCommand> commands;

    public MarketCommandHandler() {
        this.commands = new HashMap<String, SubCommand>();
        registerCommands();
    }

    public void registerCommands() {
        commands.put("create", new MarketCreateCommand());
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if (strings.length == 0) {
            p.sendMessage("Please use any of the subcommands.");
        }
        if (strings.length > 0) {
            for (String str : commands.keySet())
                if (str.equalsIgnoreCase(strings[0])) {
                    commands.get(str).execute(p, strings);
                }
        }
        return true;
    }
}
