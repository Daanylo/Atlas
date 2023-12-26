package atlas.atlas.TabCompleters;

import atlas.atlas.Commands.SettlementCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SettlementTabCompleter implements TabCompleter {

    SettlementCommandHandler settlementCommandHandler = new SettlementCommandHandler();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String [] args) {
        if (command.getName().equals("settlement") && args.length == 1) {
            return new ArrayList<>(settlementCommandHandler.commands.keySet());
        }
        return null;
    }
}
