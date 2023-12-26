package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageHandler implements Listener {

    static SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (settlementManager.getSettlement(p) != null) {
            Settlement settlement = settlementManager.getSettlement(p);
            e.setFormat(ChatColor.GRAY + "[" + settlement.getName() + ChatColor.GRAY + "] " + ChatColor.GOLD + p.getName() + ChatColor.RESET + ": " + e.getMessage());
        }
        if (settlementManager.getSettlement(p) == null) {
            e.setFormat(ChatColor.GRAY + p.getName() + ChatColor.RESET + ": " + e.getMessage());
        }
    }
}