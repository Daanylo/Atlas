package atlas.atlas.Commands;

import atlas.atlas.Commands.SubCommands.Admin.AdminGoldCommand;
import atlas.atlas.Commands.SubCommands.Admin.AdminNPCCommand;
import atlas.atlas.Commands.SubCommands.Admin.AdminSetSpawnAreaCommand;
import atlas.atlas.Commands.SubCommands.Market.MarketCreateCommand;
import atlas.atlas.Commands.SubCommands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AdminCommandHandler implements CommandExecutor {
    public HashMap<String, SubCommand> commands;

    public AdminCommandHandler() {
        this.commands = new HashMap<String, SubCommand>();
        registerCommands();
    }

    public void registerCommands() {
        commands.put("gold", new AdminGoldCommand());
        commands.put("npc", new AdminNPCCommand());
        commands.put("setspawnarea", new AdminSetSpawnAreaCommand());
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if (!p.isOp()) {
            p.sendMessage("§cYou are not allowed to use that command.");
            return true;
        }
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
