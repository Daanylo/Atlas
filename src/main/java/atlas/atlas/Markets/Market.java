package atlas.atlas.Markets;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Market {
    private UUID ownerUUID;
    private Chest chest;
    private ArrayList<ArmorStand> hologram;
    private ItemStack sellItem;
    private double price;
    public Market(UUID ownerUUID, Chest chest, ArrayList<ArmorStand> hologram, ItemStack sellItem, double price) {
        this.ownerUUID = ownerUUID;
        this.chest = chest;
        this.hologram = hologram;
        this.sellItem = sellItem;
        this.price = price;
    }
    public UUID getOwner() {
        return ownerUUID;
    }
    public void setOwner(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
    public Chest getChest() {
        return chest;
    }
    public void setChest(Chest chest) {
        this.chest = chest;
    }
    public ArrayList<ArmorStand> getHolograms() {
        return hologram;
    }
    public void setHolograms(ArrayList<ArmorStand> hologram) {
        this.hologram = hologram;
    }
    public ItemStack getSellItem() {
        return sellItem;
    }
    public void setSellItem(ItemStack sellItem) {
        this.sellItem = sellItem;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStock() {
        int count = 0;
        Inventory chestInventory = getChest().getBlockInventory();
        for (ItemStack itemStack : chestInventory.getContents()) {
            if (itemStack != null && itemStack.isSimilar(getSellItem())) {
                count += itemStack.getAmount();
            }
        }
        return count;
    }
}
