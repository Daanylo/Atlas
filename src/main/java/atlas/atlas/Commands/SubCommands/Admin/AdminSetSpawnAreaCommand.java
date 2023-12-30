package atlas.atlas.Commands.SubCommands.Admin;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SelectionManager;
import atlas.atlas.Managers.SpawnManager;
import atlas.atlas.Regions.Selection;
import org.bukkit.entity.Player;

public class AdminSetSpawnAreaCommand extends SubCommand {

    SelectionManager selectionManager = Atlas.getInstance().getSelectionManager();
    SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();
    @Override
    public void execute(Player p, String[] args) {
        if (!selectionManager.selections.containsKey(p.getUniqueId())) {
            p.sendMessage("§cYou have not made a selection.");
            return;
        }
        Selection selection = selectionManager.selections.get(p.getUniqueId());
        if (selectionManager.selectionOverlaps(selection)) {
            p.sendMessage("§cThat selection overlaps a settlement.");
            return;
        }
        spawnManager.setSpawnArea(new Selection(selection.getxA(), selection.getzA(), selection.getxB(), selection.getzB()));
        selectionManager.selections.remove(p.getUniqueId());
        p.sendMessage("§aSpawn area set successfully");
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
