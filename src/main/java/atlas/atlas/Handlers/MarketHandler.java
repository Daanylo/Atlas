package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.AtlasPlayerManager;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Managers.MarketManager;
import atlas.atlas.Markets.Market;
import atlas.atlas.Players.AtlasPlayer;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class MarketHandler implements Listener {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    MarketManager marketManager = Atlas.getInstance().getMarketManager();
    AtlasPlayerManager atlasPlayerManager = Atlas.getInstance().getAtlasPlayerManager();
    @EventHandler
    public void onMarketCreate(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            return;
        }
        if (marketIsValid(event)) {
            Player player = event.getPlayer();
            Chest chest = (Chest) event.getClickedBlock().getState();
            Market market = marketManager.getCreatingMarket().get(player.getUniqueId());
            market.setChest(chest);
            openMarketCreateMenu(player);
        }
    }
    @EventHandler
    public void onMarketInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (!(event.getClickedBlock().getState() instanceof Chest)) {
            return;
        }
        if (marketManager.getMarket(event.getClickedBlock().getLocation()) == null) {
            return;
        }
        Player p = event.getPlayer();
        Market market = marketManager.getMarket(event.getClickedBlock().getLocation());
        if (market.getOwner().equals(p.getUniqueId())) {
            openMarketEditMenu(p, market);
        }
        if (!market.getOwner().equals(p.getUniqueId())) {
            openMarketBuyMenu(p, market);
            marketManager.getBuyingMarket().put(p.getUniqueId(), market);
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onMarketClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Market market = null;
        switch (event.getView().getTitle()) {
            case "Create Market":
                event.setCancelled(true);
                if (marketManager.getCreatingMarket().containsKey(player.getUniqueId())) {
                    market = marketManager.getCreatingMarket().get(player.getUniqueId());
                }
                if (!marketManager.getNewPrice().containsKey(market)) {
                    marketManager.setNewPrice(market, market.getPrice());
                }
                double newPrice = marketManager.getNewPrice().get(market);
                if (event.getClickedInventory() == player.getInventory()) {
                    if (event.getCurrentItem() != null) {
                        ItemStack sellItem = new ItemStack(event.getCurrentItem());
                        sellItem.setAmount(1);
                        event.getView().setItem(13, sellItem);
                        market.setSellItem(sellItem);
                    }
                }
                switch (event.getSlot()) {
                    case 10:
                        marketManager.setNewPrice(market, (Math.max(0.01, newPrice - 1)));
                        break;
                    case 11:
                        marketManager.setNewPrice(market, (Math.max(0.01, newPrice - 0.10)));
                        break;
                    case 12:
                        marketManager.setNewPrice(market, (Math.max(0.01, newPrice - 0.01)));
                        break;
                    case 14:
                        marketManager.setNewPrice(market, (Math.min(100, newPrice + 0.01)));
                        break;
                    case 15:
                        if (marketManager.getNewPrice().get(market) == 0.01) {
                            marketManager.setNewPrice(market, (0.10));
                        } else {
                            marketManager.setNewPrice(market, (Math.min(100, newPrice + 0.10)));
                        }
                        break;
                    case 16:
                        if (marketManager.getNewPrice().get(market) == 0.01) {
                            marketManager.setNewPrice(market, (1.00));
                        } else {
                            marketManager.setNewPrice(market, (Math.min(100, newPrice + 1)));
                        }
                        break;
                    case 18:
                        event.getInventory().close();
                        break;
                    case 26:
                        ItemStack sellItem = market.getSellItem();
                        if (sellItem == null || sellItem.getType() == Material.AIR) {
                            player.sendMessage("§cSelect an item in your inventory to sell.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                            return;
                        }
                        if (newPrice < 0.01) {
                            player.sendMessage( "§cThe price has to be at least §60,01g§c.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                            return;
                        }
                        market.setPrice(newPrice);
                        marketManager.createMarket(market, player);
                        event.getInventory().close();
                        break;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                List<String> priceLore = new ArrayList<>();
                priceLore.add("§fPrice: §6" + decimalFormat.format(marketManager.getNewPrice().get(market)) + "g");
                for (int i = 10; i < 17; i++) {
                    if (event.getInventory().getItem(i) != null) {
                        event.getInventory().getItem(i).setLore(priceLore);
                    }
                }
                break;

            case "Edit Market":
                event.setCancelled(true);
                if (marketManager.getEditingMarket().containsKey(player.getUniqueId())) {
                    market = marketManager.getEditingMarket().get(player.getUniqueId());
                }
                if (!marketManager.getNewPrice().containsKey(market)) {
                    marketManager.setNewPrice(market, market.getPrice());
                }
                newPrice = marketManager.getNewPrice().get(market);
                if (event.getClickedInventory() == player.getInventory()) {
                    return;
                }
                switch (event.getSlot()) {
                    case 10:
                        marketManager.setNewPrice(market, (Math.max(0.01, newPrice - 1)));
                        break;
                    case 11:
                        marketManager.setNewPrice(market, (Math.max(0.01, newPrice - 0.10)));
                        break;
                    case 12:
                        marketManager.setNewPrice(market, (Math.max(0.01, newPrice - 0.01)));
                        break;
                    case 14:
                        marketManager.setNewPrice(market, (Math.min(100, newPrice + 0.01)));
                        break;
                    case 15:
                        if (marketManager.getNewPrice().get(market) == 0.01) {
                            marketManager.setNewPrice(market, (0.10));
                        } else {
                            marketManager.setNewPrice(market, (Math.min(100, newPrice + 0.10)));
                        }
                        break;
                    case 16:
                        if (marketManager.getNewPrice().get(market) == 0.01) {
                            marketManager.setNewPrice(market, (1.00));
                        } else {
                            marketManager.setNewPrice(market, (Math.min(100, newPrice + 1)));
                        }
                        break;
                    case 18:
                        event.getInventory().close();
                        break;
                    case 22:
                        marketManager.removeMarket(market);
                        event.getInventory().close();
                        marketManager.getEditingMarket().remove(player.getUniqueId());
                        player.sendMessage("§aMarket successfully removed.");
                        break;
                    case 26:
                        event.getInventory().close();
                        market.setPrice(newPrice);
                        marketManager.getNewPrice().remove(market);
                        marketManager.getEditingMarket().remove(player.getUniqueId());
                        player.sendMessage("§aMarket updated successfully.");
                        break;
                }
                if (marketManager.getNewPrice().get(market) != null) {
                    decimalFormat = new DecimalFormat("0.00");
                    priceLore = new ArrayList<>();
                    priceLore.add("§fPrice: §6" + decimalFormat.format(marketManager.getNewPrice().get(market)) + "g");
                    for (int i = 10; i < 17; i++) {
                        event.getInventory().getItem(i).setLore(priceLore);
                    }
                }
                break;
            case "Market":
                if (marketManager.getBuyingMarket().containsKey(player.getUniqueId())) {
                    market = marketManager.getBuyingMarket().get(player.getUniqueId());
                }
                event.setCancelled(true);
                ItemStack buyItem = event.getInventory().getItem(13);
                if (event.getClickedInventory() == player.getInventory()) {
                    return;
                }
                int stock = market.getStock();
                if (stock == 0) {
                    stock = 1;
                }
                switch (event.getSlot()) {
                    case 10:
                        buyItem.setAmount(1);
                        break;
                    case 11:
                        buyItem.setAmount(Math.min(stock, (Math.max(buyItem.getAmount() - 16, 1))));
                        break;
                    case 12:
                        buyItem.setAmount(Math.min(stock, (Math.max(buyItem.getAmount() - 1, 1))));
                        break;
                    case 14:
                        buyItem.setAmount(Math.min(stock, (Math.min(buyItem.getAmount() + 1, buyItem.getMaxStackSize()))));
                        break;
                    case 15:
                        if (buyItem.getAmount() == 1) {
                            buyItem.setAmount(Math.min(stock, (Math.min(16, buyItem.getMaxStackSize()))));
                        } else {
                            buyItem.setAmount(Math.min(stock, (Math.min(buyItem.getAmount() + 16, buyItem.getMaxStackSize()))));
                        }
                        break;
                    case 16:
                        buyItem.setAmount(Math.min(stock, (buyItem.getMaxStackSize())));
                        break;
                    case 26:
                        buyItem(player, market, buyItem.getAmount());
                        int stock1 = market.getStock();
                        if (stock1 == 0) {
                            stock1 = 1;
                        }
                        buyItem.setAmount(Math.min(stock1, (buyItem.getAmount())));
                        break;
                }
                break;
            case "Settlement Info":
                event.setCancelled(true);
                break;
        }
    }
    @EventHandler
    public void onMarketClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (title.equals("Create Market")) {
            marketManager.getCreatingMarket().remove(event.getPlayer().getUniqueId());
        }
        if (title.equals("Edit Market")) {
            marketManager.getNewPrice().remove(marketManager.getEditingMarket().get(event.getPlayer().getUniqueId()));
            marketManager.getEditingMarket().remove(event.getPlayer().getUniqueId());
        }
        if (title.equals("Market")) {
            marketManager.getBuyingMarket().remove(event.getPlayer().getUniqueId());
        }
    }
    @EventHandler
    public void onSellNPCInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager && event.getRightClicked().getCustomName() != null && event.getRightClicked().getCustomName().equals("§eSell NPC")) {
            Player player = event.getPlayer();
            AtlasPlayer atlasPlayer = atlasPlayerManager.getAtlasPlayer(player.getUniqueId());
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double amountToAdd;
            switch (itemInHand.getType()) {
                case GOLD_NUGGET:
                    amountToAdd = itemInHand.getAmount() * 0.01;
//                    atlasPlayer.setGold(atlasPlayer.getGold() + amountToAdd);
                    player.sendMessage("§aSuccessfully sold §f" + itemInHand.getAmount() + " §6Gold Nuggets §afor §6" + decimalFormat.format(amountToAdd) + "g§a.");
                    itemInHand.setAmount(0);
                    break;
                case GOLD_INGOT:
                    amountToAdd = itemInHand.getAmount() * 0.09;
//                    atlasPlayer.setGold(atlasPlayer.getGold() + amountToAdd);
                    player.sendMessage("§aSuccessfully sold §f" + itemInHand.getAmount() + " §6Gold Ingots §afor §6" + decimalFormat.format(amountToAdd) + "g§a.");
                    itemInHand.setAmount(0);
                    break;
                case GOLD_BLOCK:
                    amountToAdd = itemInHand.getAmount() * 0.81;
//                    atlasPlayer.setGold(atlasPlayer.getGold() + amountToAdd);
                    player.sendMessage("§aSuccessfully sold §f" + itemInHand.getAmount() + " §6Gold Blocks §afor §6" + decimalFormat.format(amountToAdd) + "g§a.");
                    itemInHand.setAmount(0);
                    break;
                default:
                    player.sendMessage("§cThis villager only takes §6Gold§c.");
                    break;
                }
            }
    }
    public boolean marketIsValid(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return false;
        }
        if (!marketManager.getCreatingMarket().containsKey(event.getPlayer().getUniqueId())) {
            return false;
        }
        Player player = event.getPlayer();
        if (settlementManager.getSettlement(event.getClickedBlock().getLocation()) != settlementManager.getSettlement(player)) {
            player.sendMessage("§cYou can only create a market inside your own settlement.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            marketManager.getCreatingMarket().remove(player.getUniqueId());
            return false;
        }
        if (!event.getClickedBlock().getType().equals(Material.CHEST)) {
            player.sendMessage("§cYou have to click on a chest to create a market.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            marketManager.getCreatingMarket().remove(player.getUniqueId());
            return false;
        }
        if (marketManager.getMarket(event.getClickedBlock().getLocation()) != null) {
            player.sendMessage("§cThat market already exists.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            marketManager.getCreatingMarket().remove(player.getUniqueId());
            return false;
        }
        return true;
    }
    public void buyItem(Player player, Market market, int amount) {
        AtlasPlayer atlasPlayer = atlasPlayerManager.getAtlasPlayer(player.getUniqueId());
        if (amount * market.getPrice() > atlasPlayer.getGold()) {
            player.sendMessage("§cYou dont have enough §6Gold§c.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            return;
        } else if (market.getStock() < amount) {
            player.sendMessage("§cMarket is out of stock.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            return;
        }
        Inventory chestInventory = market.getChest().getBlockInventory();
        ItemStack newItem = new ItemStack(market.getSellItem());
        newItem.setAmount(amount);
        Inventory inventory = player.getInventory();

        double remainingCost = market.getPrice() * amount;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    continue;
                }
                if (j == 0) {
                    if (inventory.getItem(i).getType().equals(Material.GOLD_BLOCK)) {
                        ItemStack itemStack = inventory.getItem(i);
                        for (int k = itemStack.getAmount(); k > 0 && remainingCost >= 0.81; k--) {
                            itemStack.setAmount(k - 1);
                            remainingCost -= 0.81;
                        }
                    }
                }
                if (j == 1) {
                    if (inventory.getItem(i).getType().equals(Material.GOLD_INGOT)) {
                        ItemStack itemStack = inventory.getItem(i);
                        for (int k = itemStack.getAmount(); k > 0 && remainingCost >= 0.09; k--) {
                            itemStack.setAmount(k - 1);
                            remainingCost -= 0.09;
                        }
                    }
                }
                if (j == 2) {
                    if (inventory.getItem(i).getType().equals(Material.GOLD_NUGGET)) {
                        ItemStack itemStack = inventory.getItem(i);
                        for (int k = itemStack.getAmount(); k > 0 && remainingCost >= 0.01; k--) {
                            itemStack.setAmount(k - 1);
                            remainingCost -= 0.01;
                        }
                    }
                }
            }
        }
        int owedIngots = 0;
        int owedNuggets = 0;
        if (remainingCost > 0) {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (remainingCost <= 0) {
                        break;
                    }
                    if (inventory.getItem(i) == null) {
                        continue;
                    }
                    if (j == 0) {
                        if (inventory.getItem(i).getType().equals(Material.GOLD_INGOT)) {
                            ItemStack itemStack = inventory.getItem(i);
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            owedNuggets += (int) (9 - (remainingCost * 100));
                            remainingCost -= 0.09;
                        }
                    }
                    if (j == 1) {
                        if (inventory.getItem(i).getType().equals(Material.GOLD_BLOCK)) {
                            ItemStack itemStack = inventory.getItem(i);
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            owedNuggets += (int) ( 81 - (remainingCost * 100));
                            remainingCost -= 0.81;
                        }
                    }
                }
            }
        }
        if (owedNuggets > 8) {
            owedIngots = (int) Math.floor((double) owedNuggets / 9);
            owedNuggets -= owedIngots * 9;
        }

        int remaining = amount;

        for (int i = 0; i < chestInventory.getSize(); i++) {
            if (chestInventory.getItem(i) != null) {
                ItemStack itemStack = chestInventory.getItem(i);
                if (itemStack.isSimilar(market.getSellItem())) {
                    if (remaining >= itemStack.getAmount()) {
                        remaining -= itemStack.getAmount();
                        chestInventory.setItem(i, new ItemStack(Material.AIR));
                    }
                    if (remaining < itemStack.getAmount()) {
                        itemStack.setAmount(itemStack.getAmount() - remaining);
                        remaining = 0;
                    }
                    if (remaining == 0) {
                        break;
                    }
                }
            }
        }
        ItemStack owedIngotsItem = new ItemStack(Material.GOLD_INGOT);
        ItemStack owedNuggetsItem = new ItemStack(Material.GOLD_NUGGET);
        owedIngotsItem.setAmount(owedIngots);
        owedNuggetsItem.setAmount(owedNuggets);

        if (player.getInventory().firstEmpty() == -1) {
            player.getLocation().getWorld().dropItem(player.getLocation(), newItem);
        } else {
            player.getInventory().addItem(newItem);
        }
        if (player.getInventory().firstEmpty() == -1) {
            player.getLocation().getWorld().dropItem(player.getLocation(), owedIngotsItem);
        } else {
            player.getInventory().addItem(owedIngotsItem);
        }
        if (player.getInventory().firstEmpty() == -1) {
            player.getLocation().getWorld().dropItem(player.getLocation(), owedNuggetsItem);
        } else {
            player.getInventory().addItem(owedNuggetsItem);
        }


        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        player.sendMessage("§aSuccessfully bought §f" + amount + " " + newItem.getType().name() + " §afor §6" + decimalFormat.format(amount * market.getPrice()) + "g");
    }
    public void openMarketCreateMenu(Player player) {
        Inventory marketCreateMenu = Bukkit.createInventory(null, 27, "Create Market");

        ItemStack cancelButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelButtonMeta = cancelButton.getItemMeta();
        cancelButtonMeta.setDisplayName(ChatColor.RED + "Cancel");
        cancelButton.setItemMeta(cancelButtonMeta);
        marketCreateMenu.setItem(18, cancelButton);

        ItemStack min64 = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta min64Meta = min64.getItemMeta();
        min64Meta.setDisplayName(ChatColor.RED + "-1.00g");
        min64.setItemMeta(min64Meta);
        marketCreateMenu.setItem(10, min64);

        ItemStack min16 = new ItemStack(Material.GOLD_INGOT);
        ItemMeta min16Meta = min16.getItemMeta();
        min16Meta.setDisplayName(ChatColor.RED + "-0.10g");
        min16.setItemMeta(min16Meta);
        marketCreateMenu.setItem(11, min16);

        ItemStack min1 = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta min1Meta = min1.getItemMeta();
        min1Meta.setDisplayName(ChatColor.RED + "-0.01g");
        min1.setItemMeta(min1Meta);
        marketCreateMenu.setItem(12, min1);

        ItemStack plus64 = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta plus64Meta = plus64.getItemMeta();
        plus64Meta.setDisplayName(ChatColor.GREEN + "+1.00g");
        plus64.setItemMeta(plus64Meta);
        marketCreateMenu.setItem(16, plus64);

        ItemStack plus16 = new ItemStack(Material.GOLD_INGOT);
        ItemMeta plus16Meta = plus16.getItemMeta();
        plus16Meta.setDisplayName(ChatColor.GREEN + "+0.10g");
        plus16.setItemMeta(plus16Meta);
        marketCreateMenu.setItem(15, plus16);

        ItemStack plus1 = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta plus1Meta = plus1.getItemMeta();
        plus1Meta.setDisplayName(ChatColor.GREEN + "+0.01g");
        plus1.setItemMeta(plus1Meta);
        marketCreateMenu.setItem(14, plus1);

        ItemStack acceptButton = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta acceptButtonMeta = acceptButton.getItemMeta();
        acceptButtonMeta.setDisplayName(ChatColor.GREEN + "Accept");
        acceptButton.setItemMeta(acceptButtonMeta);
        marketCreateMenu.setItem(26, acceptButton);

        player.openInventory(marketCreateMenu);
    }
    public void openMarketEditMenu(Player p, Market market) {
        Inventory marketEditMenu = Bukkit.createInventory(null, 27, "Edit Market");

        ItemStack cancelButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelButtonMeta = cancelButton.getItemMeta();
        cancelButtonMeta.setDisplayName(ChatColor.RED + "Cancel");
        cancelButton.setItemMeta(cancelButtonMeta);
        marketEditMenu.setItem(18, cancelButton);

        ItemStack deleteButton = new ItemStack(Material.CAULDRON);
        ItemMeta deleteButtonMeta = deleteButton.getItemMeta();
        deleteButtonMeta.setDisplayName(ChatColor.RED + "Delete Market");
        deleteButton.setItemMeta(deleteButtonMeta);
        marketEditMenu.setItem(22, deleteButton);

        ItemStack acceptButton = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta acceptButtonMeta = acceptButton.getItemMeta();
        acceptButtonMeta.setDisplayName(ChatColor.GREEN + "Accept");
        acceptButton.setItemMeta(acceptButtonMeta);
        marketEditMenu.setItem(26, acceptButton);

        ItemStack min64 = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta min64Meta = min64.getItemMeta();
        min64Meta.setDisplayName(ChatColor.RED + "-1.00g");
        min64.setItemMeta(min64Meta);
        marketEditMenu.setItem(10, min64);

        ItemStack min16 = new ItemStack(Material.GOLD_INGOT);
        ItemMeta min16Meta = min16.getItemMeta();
        min16Meta.setDisplayName(ChatColor.RED + "-0.10g");
        min16.setItemMeta(min16Meta);
        marketEditMenu.setItem(11, min16);

        ItemStack min1 = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta min1Meta = min1.getItemMeta();
        min1Meta.setDisplayName(ChatColor.RED + "-0.01g");
        min1.setItemMeta(min1Meta);
        marketEditMenu.setItem(12, min1);

        ItemStack plus64 = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta plus64Meta = plus64.getItemMeta();
        plus64Meta.setDisplayName(ChatColor.GREEN + "+1.00g");
        plus64.setItemMeta(plus64Meta);
        marketEditMenu.setItem(16, plus64);

        ItemStack plus16 = new ItemStack(Material.GOLD_INGOT);
        ItemMeta plus16Meta = plus16.getItemMeta();
        plus16Meta.setDisplayName(ChatColor.GREEN + "+0.10g");
        plus16.setItemMeta(plus16Meta);
        marketEditMenu.setItem(15, plus16);

        ItemStack plus1 = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta plus1Meta = plus1.getItemMeta();
        plus1Meta.setDisplayName(ChatColor.GREEN + "+0.01g");
        plus1.setItemMeta(plus1Meta);
        marketEditMenu.setItem(14, plus1);

        ItemStack sellItem = new ItemStack(market.getSellItem());
        marketEditMenu.setItem(13, sellItem);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        List<String> priceLore = new ArrayList<>();
        priceLore.add("§fPrice: §6" + decimalFormat.format(market.getPrice()) + "g");
        for (int i = 10; i < 17; i++) {
            marketEditMenu.getItem(i).setLore(priceLore);
        }

        p.openInventory(marketEditMenu);
        marketManager.getEditingMarket().put(p.getUniqueId(), market);
    }
    public void openMarketBuyMenu(Player p, Market market) {
        Inventory marketBuyMenu = Bukkit.createInventory(null, 27, "Market");
        AtlasPlayer atlasPlayer = atlasPlayerManager.getAtlasPlayer(p.getUniqueId());

        ItemStack min64 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        min64.setAmount(64);
        ItemMeta min64Meta = min64.getItemMeta();
        min64Meta.setDisplayName(ChatColor.RED + "-64");
        min64.setItemMeta(min64Meta);
        marketBuyMenu.setItem(10, min64);

        ItemStack min16 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        min16.setAmount(16);
        ItemMeta min16Meta = min16.getItemMeta();
        min16Meta.setDisplayName(ChatColor.RED + "-16");
        min16.setItemMeta(min16Meta);
        marketBuyMenu.setItem(11, min16);

        ItemStack min1 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        min1.setAmount(1);
        ItemMeta min1Meta = min1.getItemMeta();
        min1Meta.setDisplayName(ChatColor.RED + "-1");
        min1.setItemMeta(min1Meta);
        marketBuyMenu.setItem(12, min1);

        ItemStack plus64 = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        plus64.setAmount(64);
        ItemMeta plus64Meta = plus64.getItemMeta();
        plus64Meta.setDisplayName(ChatColor.GREEN + "+64");
        plus64.setItemMeta(plus64Meta);
        marketBuyMenu.setItem(16, plus64);

        ItemStack plus16 = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        plus16.setAmount(16);
        ItemMeta plus16Meta = plus16.getItemMeta();
        plus16Meta.setDisplayName(ChatColor.GREEN + "+16");
        plus16.setItemMeta(plus16Meta);
        marketBuyMenu.setItem(15, plus16);

        ItemStack plus1 = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        plus1.setAmount(1);
        ItemMeta plus1Meta = plus1.getItemMeta();
        plus1Meta.setDisplayName(ChatColor.GREEN + "+1");
        plus1.setItemMeta(plus1Meta);
        marketBuyMenu.setItem(14, plus1);

        ItemStack marketInfo = new ItemStack(Material.ENDER_EYE);
        ItemMeta marketInfoMeta = marketInfo.getItemMeta();
        List<String> marketInfoLore = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        marketInfoLore.add(ChatColor.WHITE + "Owner: " + Bukkit.getOfflinePlayer(market.getOwner()).getName());
        marketInfoLore.add(ChatColor.WHITE + "Sold Item: " + market.getSellItem().getType().name());
        marketInfoLore.add(ChatColor.WHITE + "Price: " + ChatColor.GOLD + decimalFormat.format(market.getPrice()) + "g " + ChatColor.WHITE + "a piece");
        marketInfoLore.add(ChatColor.WHITE + "Stock: " + market.getStock());

        ItemStack buyButton = new ItemStack(Material.GOLD_INGOT);
        ItemMeta buyButtonMeta = buyButton.getItemMeta();
        List<String> buyButtonLore = new ArrayList<>();
        buyButtonLore.add(ChatColor.WHITE + "Cost: " + ChatColor.GOLD + market.getPrice() + "g");

        
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack buyItem = marketBuyMenu.getItem(13);

                marketInfoMeta.setDisplayName(ChatColor.RESET + "Market Info");

                marketInfoLore.set(0, "§fOwner: " + Bukkit.getOfflinePlayer(market.getOwner()).getName());
                marketInfoLore.set(1, "§fSold Item: " + market.getSellItem().getType().name());
                marketInfoLore.set(2, "§fPrice: §6" + ChatColor.GOLD + decimalFormat.format(market.getPrice()) + "g " + "§fa piece");
                marketInfoLore.set(3, "§fStock: " + market.getStock());
                marketInfoMeta.setLore(marketInfoLore);
                marketInfo.setItemMeta(marketInfoMeta);
                marketBuyMenu.setItem(18, marketInfo);

                if (market.getStock() == 0) {
                    buyButtonMeta.setDisplayName("§cOUT OF STOCK");
                }
                else if ((buyItem.getAmount() * market.getPrice()) > atlasPlayer.getGold()) {
                    buyButtonMeta.setDisplayName("§cNot enough gold");
                } else {
                    buyButtonMeta.setDisplayName("§6Buy Item(s)");
                }
                buyButtonLore.set(0, "§fCost: §6" + decimalFormat.format(market.getPrice() * buyItem.getAmount()) + "g");
                buyButtonMeta.setLore(buyButtonLore);
                buyButton.setItemMeta(buyButtonMeta);
                marketBuyMenu.setItem(26, buyButton);

            }
        }.runTaskTimer(Atlas.getPlugin(Atlas.class), 0, 10);

        marketBuyMenu.setItem(13, market.getSellItem());

        p.openInventory(marketBuyMenu);
    }
}
