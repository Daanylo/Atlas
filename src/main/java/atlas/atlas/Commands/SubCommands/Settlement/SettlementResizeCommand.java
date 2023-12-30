package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SelectionManager;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Selection;
import atlas.atlas.Regions.Settlement;
import org.bukkit.entity.Player;

public class SettlementResizeCommand extends SubCommand {
    SelectionManager selectionManager = Atlas.getInstance().getSelectionManager();
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cYou are not allowed to change the settlement area.");
            return;
        }
        if (selectionManager.getSelections().get(p.getUniqueId()) == null) {
            p.sendMessage("§cYou have to make a selection first.");
            return;
        }
        Selection selection = selectionManager.getSelections().get(p.getUniqueId());

        if (selectionManager.selectionOverlapsSpawn(selection)) {
            p.sendMessage("§cThat selection overlaps spawn.");
            return;
        }
        if (selectionManager.selectionOverlaps(selection) && selectionManager.selectionOverlapsSettlement(selection) != settlement) {
            p.sendMessage("§cThat selection overlaps another settlement.");
            return;
        }
        if (selection.getArea() > 10000) {
            p.sendMessage("§cThe selected area is too big. Please select an area of §e10000 §cblocks or less.");
            return;
        }
        if (selection.getArea() < 100) {
            p.sendMessage("§cThe selected area is too small. Please select an area of §e100 §cblocks or more.");
            return;
        }
        settlement.setArea(new Selection(selection.getxA(), selection.getzA(), selection.getxB(), selection.getzB()));
        p.sendMessage("§aSuccesfully updated the settlement area.");
        if (settlement.getHome() != null && !selection.isWithin(settlement.getHome())) {
            p.sendMessage("§eThe settlement home was removed because it's outside the settlement area.");
            settlement.setHome(null);
        }
        selectionManager.selections.remove(p.getUniqueId());
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
