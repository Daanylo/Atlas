package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SettlementKickCommand extends SubCommand {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a region.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p)) {
            p.sendMessage("§cYou are not allowed to kick members from your settlement.");
            return;
        }
        if (args.length < 2) {
            p.sendMessage("§cPlease use " + ChatColor.GRAY + ChatColor.ITALIC + "/settlement kick (name) " + ChatColor.RESET + ".");
            return;
        }
        OfflinePlayer kickedPlayer = Bukkit.getPlayer(args[1]);
        if (settlementManager.getSettlement(kickedPlayer) == null || settlementManager.getSettlement(kickedPlayer) != settlement) {
            p.sendMessage("§cThat player is not a member of your settlement.");
            return;
        }
        if (kickedPlayer == p) {
            p.sendMessage("§cYou can't kick yourself from your region.");
            return;
        }
        settlement.removeMember(kickedPlayer.getUniqueId());
        p.sendMessage("§aYou have kicked §r" + kickedPlayer.getName() + " §afrom your settlement.");
        if (kickedPlayer.isOnline()) {
            ((Player) kickedPlayer).sendMessage("§eYou have been kicked from " + settlement.getName() + "§e.");
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
