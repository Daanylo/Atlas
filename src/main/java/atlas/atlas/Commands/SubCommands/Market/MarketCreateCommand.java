package atlas.atlas.Commands.SubCommands.Market;

import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Managers.MarketManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Atlas;
import atlas.atlas.Markets.Market;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MarketCreateCommand extends SubCommand {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    MarketManager marketManager = Atlas.getInstance().getMarketManager();
    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou can only create markets if you're a member of a settlement.");
            return;
        }
        if (marketManager.getCreatingMarket().containsKey(p.getUniqueId())) {
            return;
        }
        marketManager.getCreatingMarket().put(p.getUniqueId(), new Market(p.getUniqueId(), null, null, null, 0));
        p.sendMessage("§eYou can now create a market by left-clicking a chest.");
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
