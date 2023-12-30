package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CompassHandler implements Listener {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    public HashMap<UUID, Settlement> tracking;
    @EventHandler
    public void onCompassUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction().isRightClick() && e.getMaterial() == Material.COMPASS) {
            openCompassMenu(p);
        }
    }

    @EventHandler
    public void onCompassClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Tracker Menu")) {
            event.setCancelled(true);
            if (tracking == null) {
                tracking = new HashMap<>();
            }
            if (event.getCurrentItem() != null) {
                for (String settlementID : settlementManager.getSettlements().keySet()) {
                    Settlement settlement = settlementManager.getSettlements().get(settlementID);
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(settlement.getName())) {
                        tracking.putIfAbsent(player.getUniqueId(), null);
                        if (tracking.get(player.getUniqueId()) != null && tracking.get(player.getUniqueId()).equals(settlement)) {
                            player.sendMessage("§cAlready tracking §r" + settlement.getName() + "§c.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                            return;
                        }
                        showDistance(player, settlement);
                    }
                }
            }
        }
    }

    public void openCompassMenu(Player p) {
        Inventory compassMenu = Bukkit.createInventory(null, 27, "Tracker Menu");

        int i = 0;
        for (String settlementID : settlementManager.getSettlements().keySet()) {
            Settlement settlement = settlementManager.getSettlements().get(settlementID);
            ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§r§aClick to track");
            meta.setLore(lore);
            meta.setDisplayName(settlement.getName());
            itemStack.setItemMeta(meta);
            compassMenu.setItem(i, itemStack);
            if (i < compassMenu.getSize()) {
                i++;
            }
        }
        p.openInventory(compassMenu);
    }

    public void showDistance(Player player, Settlement settlement) {
        player.setCompassTarget(settlement.getArea().findNearestLocation(player.getLocation()));
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        player.sendMessage("§eCurrently tracking §r" + settlement.getName() + "§e.");
        tracking.put(player.getUniqueId(), settlement);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getItemInHand().getType().equals(Material.COMPASS)) {
                    int playerX = (int) player.getLocation().getX();
                    int playerZ = (int) player.getLocation().getZ();
                    int targetX = (int) player.getCompassTarget().getX();
                    int targetZ = (int) player.getCompassTarget().getZ();
                    int x = Math.abs(playerX - targetX);
                    int z = Math.abs(playerZ - targetZ);
                    int distance = (int) Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Distance: " + distance + " blocks"));
                }
                if (settlement != tracking.get(player.getUniqueId())) {
                    this.cancel();
                }
                if (settlement.getArea().isWithin(player.getLocation())) {
                    this.cancel();
                    tracking.put(player.getUniqueId(), null);
                }
            }
        }.runTaskTimer(Atlas.getPlugin(Atlas.class), 0, 20);
    }
}
