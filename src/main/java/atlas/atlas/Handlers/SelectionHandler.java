package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Regions.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;

public class SelectionHandler implements Listener {

    HashMap<UUID, Selection> selections = Atlas.getInstance().getSelectionManager().selections;

    @EventHandler
    public void settlementSelect(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if ((e.getHand() == EquipmentSlot.HAND) && player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§eClaim Wand")) {
            if (!(e.getPlayer().getWorld().getEnvironment() == World.Environment.NORMAL)) {
                player.sendMessage("§cYou can only claim a settlement in the overworld.");
                return;
            }
            selections.putIfAbsent(player.getUniqueId(), new Selection(0, 0, 0, 0));
            Selection selection = selections.get(player.getUniqueId());
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location a = e.getClickedBlock().getLocation();
                try {
                    selection.setxA(a.getBlockX());
                    selection.setzA(a.getBlockZ());
                    if (selection.getxB() == 0 & selection.getzB() == 0) {
                        player.sendMessage("[Point A] X:" + a.getBlockX() + " Z:" + a.getBlockZ());
                    } else {
                        player.sendMessage("[Point A] X:" + a.getBlockX() + " Z:" + a.getBlockZ() + " (" + selection.getArea() + ")");
                    }
                } catch (Exception exception) {
                    player.sendMessage("An error occured. Please relog.");
                }
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Location b = e.getClickedBlock().getLocation();
                try {
                    selection.setxB(b.getBlockX());
                    selection.setzB(b.getBlockZ());
                    if (selection.getxA() == 0 & selection.getzA() == 0) {
                        player.sendMessage("[Point B] X:" + b.getBlockX() + " Z:" + b.getBlockZ());
                    } else {
                        player.sendMessage("[Point B] X:" + b.getBlockX() + " Z:" + b.getBlockZ() + " (" + selection.getArea() + ")");
                    }
                } catch (Exception exception) {
                    player.sendMessage("An error occured. Please relog.");
                }
            }
        }
    }
}
