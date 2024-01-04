package atlas.atlas.TabCompleters;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SettlementCommandHandler;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SettlementTabCompleter implements TabCompleter {

    SettlementCommandHandler settlementCommandHandler = new SettlementCommandHandler();
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String [] args) {
        if (command.getName().equals("settlement") && args.length == 1) {
            return new ArrayList<>(settlementCommandHandler.commands.keySet());
        }
        if (args[0].equalsIgnoreCase("info")) {
            ArrayList<String> settlementNames = new ArrayList<>();
            for (String settlementID : settlementManager.getSettlements().keySet()) {
                Settlement settlement = settlementManager.getSettlements().get(settlementID);
                settlementNames.add(settlement.getRawName());
            }
            return settlementNames;
        }
        return null;
    }
}
