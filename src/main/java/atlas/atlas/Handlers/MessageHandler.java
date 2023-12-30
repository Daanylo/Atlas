package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.MessageManager;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageHandler implements Listener {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    MessageManager messageManager = Atlas.getInstance().getMessageManager();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (settlementManager.getSettlement(p) != null) {
            Settlement settlement = settlementManager.getSettlement(p);
            e.setFormat("§f[" + settlement.getName() + "§r] " + p.getName() + ": " + e.getMessage());
        }
        if (settlementManager.getSettlement(p) == null) {
            e.setFormat(p.getName() + ": " + e.getMessage());
        }
        if (messageManager.currentQuestion != null) {
            if (messageManager.isCorrectAnswer(e.getMessage())) {
                Bukkit.broadcastMessage("§d§lCONGRATULATIONS §6§l" + p.getName() + "§d§l!!! YOU WON NOTHING!!!");
                Bukkit.broadcastMessage("§eThe correct answer was \"" + messageManager.getCurrentAnswers().get(0) + "\"");
                e.setCancelled(true);
                messageManager.setCurrentQuestion(null);
            }
        }
    }
}
