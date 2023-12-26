package atlas.atlas.Utils;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.MarketManager;
import atlas.atlas.Markets.Market;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class MarketUtil {

    static MarketManager marketManager = Atlas.getInstance().getMarketManager();

    public static void createMarkets() {
        new File(Atlas.getInstance().getDataFolder() + "/data").mkdir();
        File marketsFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "markets.yml");
        FileConfiguration fc = new YamlConfiguration();

        if (!marketsFile.exists()) {
            try {
                marketsFile.createNewFile();
                fc.save(marketsFile);
                Bukkit.getLogger().info("markets.yml created.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void saveMarkets() {
        File marketsFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "markets.yml");
        FileConfiguration fc = new YamlConfiguration();
        HashMap<UUID, Market> markets = marketManager.getMarkets();
        if (markets != null) {
            for (UUID marketUUID : markets.keySet()) {
                Market market = markets.get(marketUUID);
                UUID ownerUUID = market.getOwner();
                Location location = market.getChest().getLocation();
                ItemStack sellItem = market.getSellItem();
                double price = market.getPrice();
                fc.set(marketUUID + ".owner", ownerUUID.toString());
                fc.set(marketUUID + ".location", location);
                fc.set(marketUUID + ".item", sellItem);
                fc.set(marketUUID + ".price", price);
                for (ArmorStand armorStand : market.getHolograms()) {
                    armorStand.remove();
                }
                for (Entity entity : location.getWorld().getEntities()) {
                    if (entity instanceof ArmorStand) {
                        ArmorStand armorStand = (ArmorStand) entity;
                        if (armorStand.getCustomName() != null) {
                            armorStand.remove();
                        }
                    }
                }
            }
            try {
                fc.save(marketsFile);
                Bukkit.getLogger().info("Saving markets...");
                markets.clear();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    public static void loadMarkets() {
        File marketsFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "markets.yml");
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(marketsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<UUID, Market> markets = new HashMap<>();
        if (!(fc.getKeys(false).size() == 0)) {
            Set<String> marketUUIDs = fc.getKeys(false);
            for (String marketUUID : marketUUIDs) {
                UUID owner = UUID.fromString(fc.getString(marketUUID + ".owner"));
                Location location = (Location) fc.get(marketUUID + ".location");
                if (!location.getWorld().getBlockAt(location).getType().equals(Material.CHEST)) {
                    return;
                }
                Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
                ItemStack sellItem = (ItemStack) fc.get(marketUUID + ".item");
                double price = fc.getDouble(marketUUID + ".price");
                Market market = new Market(owner, chest, new ArrayList<>(), sellItem, price);
                markets.put(UUID.fromString(marketUUID), market);
            }
        }
        marketManager.setMarkets(markets);
        marketManager.updateMarkets();
        Bukkit.getLogger().info("Loading markets...");
    }
}
