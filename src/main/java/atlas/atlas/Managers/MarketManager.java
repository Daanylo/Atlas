package atlas.atlas.Managers;

import atlas.atlas.Atlas;
import atlas.atlas.Markets.Market;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MarketManager {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    public HashMap<UUID, Market> markets;
    public HashMap<UUID, Market> creatingMarket;
    public HashMap<UUID, Market> editingMarket;
    public HashMap<UUID, Market> buyingMarket;
    public HashMap<Market, Double> newPrice;
    public MarketManager() {
        markets = new HashMap<>();
        creatingMarket = new HashMap<>();
        editingMarket = new HashMap<>();
        buyingMarket = new HashMap<>();
        newPrice = new HashMap<>();
    }
    public ArrayList<Market> getPlayerMarkets(Player owner) {
        ArrayList<Market> marketArrayList = new ArrayList<>();
        for (UUID marketUUID : markets.keySet()) {
            Market market = markets.get(marketUUID);
            if (market.getOwner().equals(owner.getUniqueId())) {
                marketArrayList.add(market);
            }
        }
        return marketArrayList;
    }
    public Market getMarket(Location location) {
        for (UUID marketUUID : markets.keySet()) {
            Market market = markets.get(marketUUID);
            if (market.getChest().getLocation().equals(location)) {
                return market;
            }
        }
        return null;
    }
    public void removeMarket(Market market) {
        UUID target = null;
        for (UUID marketUUID : markets.keySet()) {
            if (market.equals(markets.get(marketUUID))) {
                target = marketUUID;
                for (ArmorStand armorStand : market.getHolograms()) {
                    armorStand.remove();
                }
            }
        }
        markets.remove(target);
    }
    public HashMap<UUID, Market> getMarkets() {
        return markets;
    }
    public void setMarkets(HashMap<UUID, Market> markets) {
        this.markets = markets;
    }
    public HashMap<Market, Double> getNewPrice() {
        return newPrice;
    }
    public void setNewPrice(Market market, Double newPrice) {
        this.newPrice.put(market, newPrice);
    }
    public void createMarket(Market market, Player player) {
        market.setHolograms(new ArrayList<>());
        markets.put(UUID.randomUUID(), market);
        updateMarkets();
        player.sendMessage("§aMarket successfully created!");
        creatingMarket.remove(player.getUniqueId());
    }

    public HashMap<UUID, Market> getCreatingMarket() {
        return creatingMarket;
    }

    public HashMap<UUID, Market> getEditingMarket() {
        return editingMarket;
    }

    public HashMap<UUID, Market> getBuyingMarket() {
        return buyingMarket;
    }

    public void updateMarkets() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    return;
                }
                for (UUID marketUUID : markets.keySet()) {
                    Market market = markets.get(marketUUID);
                    if (!(market.getChest().getWorld().getBlockAt(market.getChest().getLocation()).getState() instanceof Chest)) {
                        removeMarket(market);
                        return;
                    }
                    if (settlementManager.getSettlement(market.getChest().getLocation()) == null || !settlementManager.getSettlement(market.getChest().getLocation()).getMembers().contains(market.getOwner())) {
                        removeMarket(market);
                        return;
                    }
                    String[] lines = {
                            "Stock: " + market.getStock(),
                            "Cost: §6" + decimalFormat.format(market.getPrice()) + "g",
                            "Sold item: §a" + market.getSellItem().getType().name(),
                            "§e§nMarket"
                    };
                    if (market.getStock() == 0) {
                        lines[0] = "Stock: §cOUT OF STOCK";
                    }
                    if (market.getHolograms() == null || market.getHolograms().size() < 1) {
                        ArrayList<ArmorStand> holograms = new ArrayList<>();
                        for (int i = 0; i < lines.length; i++) {
                            Location lineLocation = market.getChest().getLocation().clone().add(0.5, 1 + (i * 0.25), 0.5);
                            ArmorStand armorStand = (ArmorStand) market.getChest().getLocation().getWorld().spawnEntity(lineLocation, EntityType.ARMOR_STAND);
                            armorStand.setCustomNameVisible(true);
                            armorStand.setGravity(false);
                            armorStand.setCustomName(lines[i]);
                            armorStand.setVisible(false);
                            armorStand.setMarker(true);
                            holograms.add(armorStand);
                        }
                        market.setHolograms(holograms);
                    }
                    if (market.getHolograms().size() > lines.length) {
                        for (ArmorStand armorStand : market.getHolograms()) {
                            armorStand.remove();
                        }
                        market.getHolograms().clear();
                    }
                    if (market.getHolograms().size() == lines.length) {
                        for (int i = 0; i < market.getHolograms().size(); i++) {
                            market.getHolograms().get(i).setCustomName(lines[i]);
                        }
                    }
                }
            }
        }.runTaskTimer(Atlas.getPlugin(Atlas.class), 0,10);
    }
}
