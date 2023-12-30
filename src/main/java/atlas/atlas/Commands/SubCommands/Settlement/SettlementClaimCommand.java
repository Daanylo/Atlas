package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Managers.SelectionManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import atlas.atlas.Regions.Selection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SettlementClaimCommand extends SubCommand {

    SelectionManager selectionManager = Atlas.getInstance().getSelectionManager();
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            help(p);
            return;
        }
        if (!selectionManager.selections.containsKey(p.getUniqueId())) {
            p.sendMessage("§cPlease make a selection first. You can obtain a §eClaim Wand §cby using §7§o/claimwand§c.");
            return;
        }
        Selection selection = selectionManager.selections.get(p.getUniqueId());
        if (settlementManager.getSettlement(p) != null) {
            p.sendMessage("§cYou are already a member of a settlement");
            return;
        }
        if (validateSettlement(selection, args[1], p)) {
            String randomUUID = UUID.randomUUID().toString();
            ArrayList<UUID> members = new ArrayList<>();
            members.add(p.getUniqueId());
            settlementManager.addSettlement(randomUUID, new Settlement(ChatColor.translateAlternateColorCodes('&', args[1]), p.getUniqueId(), members, selection, null, 0, 0));
            p.sendMessage("§aYou have succesfully founded a new settlement!");
            selectionManager.selections.remove(p.getUniqueId());
        }
    }

    private boolean validateSettlement(Selection selection, String name, Player p) {
        if (selectionManager.selectionOverlapsSpawn(selection)) {
            p.sendMessage("§cThat selection overlaps spawn.");
            return false;
        }
        if (selectionManager.selectionOverlaps(selection)) {
            p.sendMessage("§cThat selection overlaps another settlement.");
            return false;
        }
        if (selection.getArea() > 10000) {
            p.sendMessage("§cThe selected area is too big. Please select an area smaller than §e16 §cblocks.");
            return false;
        }
        if (selection.getArea() < 100) {
            p.sendMessage("§cThe selected area is too small. Please select an area bigger than §e4 §cblocks.");
            return false;
        }
        if (settlementManager.containsIllegalCharacters(name)) {
            p.sendMessage("§cThat name contains illegal characters. Only use letters, numbers and dashes.");
            return false;
        }
        if (name.replaceAll("&.", "").length() > 16) {
            p.sendMessage("§cThat name is too long. Max length is §e16§c.");
            return false;
        }
        if (name.replaceAll("&.", "").length() < 3) {
            p.sendMessage("§cThat name is too short. Min length is §e3§c.");
            return false;
        }
        if (!settlementManager.isAvailable(name)) {
            p.sendMessage("§cThat name is already in use. Please choose a different name.");
            return false;
        }
        return true;
    }

    @Override
    public void help(Player p) {
        p.sendMessage("To found a settlement, please use §7§o/settlement claim (name)§r.");
    }

    @Override
    public String permission() {
        return null;
    }
}
