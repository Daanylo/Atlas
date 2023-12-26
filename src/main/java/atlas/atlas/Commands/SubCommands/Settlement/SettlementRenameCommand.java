package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import atlas.atlas.Commands.SubCommands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SettlementRenameCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cYou are not allowed to change the settlement name.");
            return;
        }
        if (validateName(args[1], p)) {
            settlement.setName(ChatColor.translateAlternateColorCodes('&', args[1]));
            p.sendMessage("§aThe new settlement name has been set.");
        }
    }

    public boolean validateName(String name, Player p) {
        if (name == null) {
            p.sendMessage("§cPlease use §7§o/settlement rename (name)§c.");
            return false;
        }
        if (settlementManager.containsIllegalCharacters(name)) {
            p.sendMessage("§cThat name contains illegal characters. Only use letters, numbers and dashes.");
            return false;
        }
        if (name.length() > 16) {
            p.sendMessage("§cThat name is too long. Max length is 16.");
            return false;
        }
        if (name.length() < 3) {
            p.sendMessage("§cThat name is too short. Min length is 3.");
            return false;
        }
        String rawName = name.replaceAll("&.", "");
        if (!settlementManager.isAvailable(rawName)) {
            if (settlementManager.getSettlement(rawName).getMembers().contains(p.getUniqueId())) {
                return true;
            } else {
                p.sendMessage("§cThat name is already in use. Please choose a different name.");
                return false;
            }
        }
        return true;
    }






    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
