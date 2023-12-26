package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SettlementSetLeaderCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cYou are not allowed to change the settlement leader.");
            return;
        }
        if (args.length < 2) {
            p.sendMessage("§cPlease use §7§o/settlement setleader (name) §c.");
            return;
        }
        OfflinePlayer newLeader = Bukkit.getOfflinePlayer(args[1]);
        if (settlementManager.getSettlement(newLeader) == null || settlementManager.getSettlement(newLeader) != settlement) {
            p.sendMessage("§cThat player is not a member of your settlement.");
            return;
        }
        if (newLeader == p) {
            p.sendMessage("§cYou are already the leader of your settlement.");
            return;
        }
        settlement.setLeader(newLeader.getUniqueId());
        p.sendMessage("§aYou have set §r" + newLeader.getName() + " §aas the new leader of your settlement.");
        if (newLeader.isOnline()) {
            ((Player) newLeader).sendMessage("§eYou are the new leader of " + settlement.getName() + "§e.");
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
