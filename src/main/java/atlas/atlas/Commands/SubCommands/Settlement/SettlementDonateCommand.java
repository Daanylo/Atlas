package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class SettlementDonateCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (args.length < 2) {
            if (p.getItemInHand().getType().equals(Material.GOLD_BLOCK)) {
                ItemStack itemStack = p.getItemInHand();
                settlement.setReserves(settlement.getReserves() + itemStack.getAmount() * 0.81);
                p.sendMessage("§aSuccessfully donated §6" + decimalFormat.format(itemStack.getAmount() * 0.81) + "g §ato §r" + settlement.getName() + "§a.");
                itemStack.setAmount(0);
                return;
            }
            if (p.getItemInHand().getType().equals(Material.GOLD_INGOT)) {
                ItemStack itemStack = p.getItemInHand();
                settlement.setReserves(settlement.getReserves() + itemStack.getAmount() * 0.09);
                p.sendMessage("§aSuccessfully donated §6" + decimalFormat.format(itemStack.getAmount() * 0.09) + "g §ato §r" + settlement.getName() + "§a.");
                itemStack.setAmount(0);
                return;
            }
            if (p.getItemInHand().getType().equals(Material.GOLD_NUGGET)) {
                ItemStack itemStack = p.getItemInHand();
                settlement.setReserves(settlement.getReserves() + itemStack.getAmount() * 0.01);
                p.sendMessage("§aSuccessfully donated §6" + decimalFormat.format(itemStack.getAmount() * 0.01) + "g §ato §r" + settlement.getName() + "§a.");
                itemStack.setAmount(0);
                return;
            }
            if (p.getItemInHand().getType().equals(Material.AIR)) {
                p.sendMessage("§cPlease hold §6Gold §cin your main hand or use §7§o/settlement donate all§c.");
            }
            return;
        }
        if (args[1].equalsIgnoreCase("all")) {
            Inventory inventory = p.getInventory();
            double amount = 0;
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    continue;
                }
                if (inventory.getItem(i).getType().equals(Material.GOLD_BLOCK)) {
                    ItemStack itemStack = inventory.getItem(i);
                    amount += itemStack.getAmount() * 0.81;
                    itemStack.setAmount(0);
                    continue;
                }
                if (inventory.getItem(i).getType().equals(Material.GOLD_INGOT)) {
                    ItemStack itemStack = inventory.getItem(i);
                    amount += itemStack.getAmount() * 0.09;
                    itemStack.setAmount(0);
                    continue;
                }
                if (inventory.getItem(i).getType().equals(Material.GOLD_NUGGET)) {
                    ItemStack itemStack = inventory.getItem(i);
                    amount += itemStack.getAmount() * 0.01;
                    itemStack.setAmount(0);
                }
            }
            if (amount == 0) {
                p.sendMessage("§cYou dont have any §6Gold§c.");
                return;
            }
            settlement.setReserves(settlement.getReserves() + amount);
            p.sendMessage("§aSuccessfully donated §6" + decimalFormat.format(amount) + "g §ato §r" + settlement.getName() + "§a.");
        }
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
