package atlas.atlas.Players;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AtlasPlayer {
    private UUID uuid;
    public AtlasPlayer(UUID uuid) {
        this.uuid = uuid;
    }
    public UUID getUUID() {
        return uuid;
    }
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
    public double getGold() {
        double gold = 0;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer.isOnline()) {
            Player player = Bukkit.getPlayer(uuid);
            Inventory inventory = player.getInventory();
            for (int j = 0; j < inventory.getSize(); j++) {
                if (inventory.getItem(j) == null) {
                    continue;
                }
                ItemStack itemStack = inventory.getItem(j);
                if (itemStack.getType().equals(Material.GOLD_BLOCK)) {
                    gold += itemStack.getAmount() * 0.81;
                }
                else if (itemStack.getType().equals(Material.GOLD_INGOT)) {
                    gold += itemStack.getAmount() * 0.09;
                }
                else if (itemStack.getType().equals(Material.GOLD_NUGGET)) {
                    gold += itemStack.getAmount() * 0.01;
                }
            }
        }
        return gold;
    }
}
