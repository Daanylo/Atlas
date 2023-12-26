package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import org.bukkit.entity.Player;

public class SettlementSetHomeCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cYou are not allowed to change the settlement home.");
            return;
        }
        if (settlementManager.getSettlement(p.getLocation()) == null || settlementManager.getSettlement(p.getLocation()) != settlement) {
            p.sendMessage("§cYou have to be inside your settlement to set the settlement home.");
        } else {
            settlement.setHome(p.getLocation());
            p.sendMessage("§aThe new settlement home has been set.");
        }
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
