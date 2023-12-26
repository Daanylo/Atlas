package atlas.atlas.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PvpHandler implements Listener {

    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player & e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
        if (e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Villager && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("§eSell NPC")) {
            event.setCancelled(true);
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
