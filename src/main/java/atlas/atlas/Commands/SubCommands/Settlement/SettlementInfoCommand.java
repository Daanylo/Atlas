package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Markets.Market;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SettlementInfoCommand extends SubCommand {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage("§cPlease use §7§o/settlement info (name)§c.");
            return;
        }
        for (String settlementID : settlementManager.getSettlements().keySet()) {
            Settlement settlement = settlementManager.getSettlements().get(settlementID);
            if (settlement.getRawName().equalsIgnoreCase(args[1])) {
                openSettlementInfo(settlement, p);
                return;
            }
        }
        p.sendMessage("§cThat settlement was not found.");
    }

    public void openSettlementInfo(Settlement settlement, Player player) {
        Inventory settlementInfoMenu = Bukkit.createInventory(null, 45, "Settlement Info");

        ItemStack info = new ItemStack(settlement.getBanner());
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        infoMeta.setDisplayName("§r" + settlement.getName());
        List<String> lore = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        lore.add("§fLeader: " + Bukkit.getOfflinePlayer(settlement.getLeader()).getName());
        lore.add("§fReserves: §6" + decimalFormat.format(settlement.getReserves()) + "g");
        lore.add("§fLevel: §b" + settlement.getLevel());
        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        int i = 0;
        for (UUID memberUUID : settlement.getMembers()) {
            OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setDisplayName("§r" + member.getName());
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(memberUUID));
            skull.setItemMeta(skullMeta);
            if (memberUUID.equals(settlement.getLeader())) {
                settlementInfoMenu.setItem(31, skull);
            } else {
                settlementInfoMenu.setItem(36 + i, skull);
                i++;
            }
        }

        settlementInfoMenu.setItem(13, info);
        player.openInventory(settlementInfoMenu);
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
