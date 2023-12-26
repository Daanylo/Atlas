package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SettlementDisbandCommand extends SubCommand {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cOnly the leader can disband the settlement.");
            return;
        }
        settlementManager.removeSettlement(settlementManager.getSettlementID(settlement));
        p.sendMessage(settlement.getName() + " §awas successfully disbanded.");
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
