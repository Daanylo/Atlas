package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.AtlasPlayerManager;
import atlas.atlas.Managers.NameTag;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Players.AtlasPlayer;
import atlas.atlas.Regions.Settlement;
import atlas.atlas.Utils.MarketUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveHandler implements Listener {
    AtlasPlayerManager atlasPlayerManager = Atlas.getInstance().getAtlasPlayerManager();
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.joinMessage(Component.text("§a + §f" + p.getName() +  " joined."));
        if (settlementManager.getSettlement(p) != null) {
            e.joinMessage(Component.text("§a + §f[" + settlementManager.getSettlement(p).getName() + "§r] " + p.getName() +  " joined."));
        }
        Atlas.getInstance().getScoreboardManager().setupScoreboard(p);
        if (atlasPlayerManager.getAtlasPlayer(p.getUniqueId()) == null) {
            atlasPlayerManager.getAtlasPlayers().add(new AtlasPlayer(p.getUniqueId(), 0));
        }
        if (Bukkit.getOnlinePlayers().size() == 1) {
            MarketUtil.loadMarkets();
        }
        NameTag nametag = new NameTag(p);
        nametag.nameTagUpdater(p);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.quitMessage(Component.text("§c - §f" + p.getName() +  " left."));
        if (settlementManager.getSettlement(p) != null) {
            e.quitMessage(Component.text("§c - §f[" + settlementManager.getSettlement(p).getName() + "§r] " + p.getName() +  " left."));
        }
        if (Bukkit.getOnlinePlayers().size() == 1) {
            MarketUtil.saveMarkets();
        }
    }
}
