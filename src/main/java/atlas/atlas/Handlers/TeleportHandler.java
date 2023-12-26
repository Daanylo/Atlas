package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Managers.SpawnManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportHandler implements Listener {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (settlementManager.teleportQueue.containsKey(e.getPlayer().getUniqueId())) {
            if (!settlementManager.teleportCancelled.get(e.getPlayer().getUniqueId())) {
                if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
                    Player p = e.getPlayer();
                    settlementManager.teleportCancelled.put(p.getUniqueId(), true);
                }
            }
        }
    }

    @EventHandler
    public void netherTeleportEvent(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            Player player = event.getPlayer();
            if (spawnManager.getSpawnArea().isWithin(event.getTo())) {
                event.setCancelled(true);
                player.sendMessage("§cYou can't teleport into the spawn area.");
                return;
            }
            if (settlementManager.getSettlement(event.getTo()) != null && settlementManager.getSettlement(event.getTo()) != settlementManager.getSettlement(player)) {
                event.setCancelled(true);
                player.sendMessage("§cYou can't teleport into another settlement.");
            }
        }
    }

}
