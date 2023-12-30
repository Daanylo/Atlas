package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.MarketManager;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Managers.SpawnManager;
import atlas.atlas.Regions.Settlement;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class SettlementHandler implements Listener {
    private final SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    private final SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();
    private final MarketManager marketManager = Atlas.getInstance().getMarketManager();
    private final HashMap<UUID, Long> messageCooldown = new HashMap<>();
    private final HashMap<UUID, HashMap<String, Boolean>> enteredSettlements = new HashMap<>();
    private final HashMap<UUID, Boolean> enteredSpawn = new HashMap<>();
    int cooldownTime = 3;
    private boolean getMessageCooldown(Player p) {
        if (!messageCooldown.containsKey(p.getUniqueId())) {
            messageCooldown.putIfAbsent(p.getUniqueId(), System.currentTimeMillis());
            return false;
        }
        long secondsLeft = (((messageCooldown.get(p.getUniqueId())) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
        if (secondsLeft <= 0) {
            messageCooldown.put(p.getUniqueId(), System.currentTimeMillis());
            return false;
        }
        return true;
    }
    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (b != null) {
            if (settlementManager.getSettlement(b.getLocation()) != null) {
                Settlement settlement = settlementManager.getSettlement(b.getLocation());
                if (settlementManager.getSettlement(p) != settlement) {
                    e.setCancelled(true);
                    if (b.getState() instanceof Chest && marketManager.getMarket(b.getLocation()) != null) {
                        return;
                    }
                    if (!getMessageCooldown(p)) {
                        p.sendMessage("§cYou can't build on other peoples property.");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Location loc = e.getLocation();
        if (settlementManager.getSettlement(loc) != null) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Location loc = p.getLocation();
        if (!loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
            enteredSpawn.putIfAbsent(uuid, false);
            enteredSettlements.putIfAbsent(uuid, new HashMap<>());
            if (spawnManager.getSpawnArea() != null && spawnManager.getSpawnArea().isWithin(loc) && !enteredSpawn.get(uuid)) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eEntered Spawn"));
                enteredSpawn.put(uuid, true);
            }
            if (spawnManager.getSpawnArea() != null && !spawnManager.getSpawnArea().isWithin(loc) && enteredSpawn.get(uuid)) {
                enteredSpawn.put(uuid, false);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eLeft Spawn"));
            }
            for (String settlementID : settlementManager.getSettlements().keySet()) {
                Settlement settlement = settlementManager.getSettlements().get(settlementID);
                if (settlementManager.getSettlement(loc) == settlement) {
                    if (!enteredSettlements.get(uuid).containsKey(settlementID) || !enteredSettlements.get(uuid).get(settlementID)) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Entered " + settlement.getName()));
                        enteredSettlements.get(uuid).put(settlementID, true);
                    }
                } else {
                    if (enteredSettlements.get(uuid).containsKey(settlementID) && enteredSettlements.get(uuid).get(settlementID)) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Left " + settlement.getName()));
                        enteredSettlements.get(uuid).put(settlementID, false);
                    }
                }
            }
        }
    }
}
