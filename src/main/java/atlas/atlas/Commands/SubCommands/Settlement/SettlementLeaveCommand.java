package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SettlementLeaveCommand extends SubCommand {

    private final SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a region.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (Objects.equals(settlement.getLeader(), p.getUniqueId())) {
            p.sendMessage("§cYou have to appoint a new leader before you leave.");
            return;
        }
        settlement.getMembers().remove(p.getUniqueId());
        p.sendMessage("§aYou have left " + settlement.getName());
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
