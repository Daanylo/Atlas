package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PvpHandler implements Listener {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player & e.getEntity() instanceof Player) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                Player shooter = (Player) ((Projectile) e.getDamager()).getShooter();
                if (e.getEntity() instanceof Player) {
                    e.setCancelled(true);
                    return;
                }
                Location location = e.getEntity().getLocation();
                if (settlementManager.getSettlement(location) != null) {
                    Settlement settlement = settlementManager.getSettlement(location);
                    if (!settlement.getMembers().contains(shooter.getUniqueId())) {
                        e.setCancelled(true);
                        return;
                    }
                }
                if (e.getEntity() instanceof Wolf) {
                    Wolf wolf = (Wolf) e.getEntity();
                    if (wolf.isTamed()) {
                        if (wolf.getOwner().getUniqueId() != shooter.getUniqueId()) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            Location location = e.getEntity().getLocation();
            if (settlementManager.getSettlement(location) != null) {
                Settlement settlement = settlementManager.getSettlement(location);
                if (!settlement.getMembers().contains(player.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
            if (e.getEntity() instanceof Wolf) {
                Wolf wolf = (Wolf) e.getEntity();
                if (wolf.isTamed()) {
                    if (wolf.getOwner().getUniqueId() != player.getUniqueId()) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
        if (e.getEntity() instanceof Villager && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("§eSell NPC")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Villager && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("§eSell NPC")) {
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

}
