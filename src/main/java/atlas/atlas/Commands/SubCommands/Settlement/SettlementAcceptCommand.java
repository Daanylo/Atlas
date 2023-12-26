package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.AtlasPlayerManager;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Players.AtlasPlayer;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SettlementAcceptCommand extends SubCommand {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    AtlasPlayerManager atlasPlayerManager = Atlas.getInstance().getAtlasPlayerManager();

    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage("§cPlease use §7§o/settlement accept (name)§c.");
            return;
        }
        if (settlementManager.getSettlement(args[1]) == null) {
            p.sendMessage("§cThat settlement does not exist.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(args[1]);
        String settlementID = settlementManager.getSettlementID(settlement);
        if (!settlementManager.invites.containsKey(settlementID)) {
            p.sendMessage("§cThat settlement has not invited you.");
            return;
        }
        if (!settlementManager.invites.get(settlementID).contains(p.getUniqueId())) {
            p.sendMessage("§cThat settlement has not invited you.");
            return;
        }
        if (settlement.getMembers().size() > 9) {
            p.sendMessage("§cThat settlement is full.");
            return;
        }
        settlement.getMembers().add(p.getUniqueId());
        settlementManager.invites.get(settlementID).remove(p.getUniqueId());
        p.sendMessage("§aYou have joined §r" + settlement.getName());
        for (UUID member : settlement.getMembers()) {
            if (member != p.getUniqueId()) {
                if (Bukkit.getOfflinePlayer(member).isOnline()) {
                    Bukkit.getPlayer(member).sendMessage(p.getName() + " §ehas joined your settlement.");
                }
            }
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
