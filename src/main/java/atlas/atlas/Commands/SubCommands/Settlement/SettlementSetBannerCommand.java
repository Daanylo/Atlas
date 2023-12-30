package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettlementSetBannerCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cYou are not allowed to change the settlement banner.");
            return;
        }
        if (!(p.getItemInHand().getType().name().contains("BANNER"))) {
            p.sendMessage("§cPlease hold the banner in your main hand.");
            return;
        }
        ItemStack banner = new ItemStack(p.getItemInHand());
        ItemMeta meta = banner.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(meta);
        banner.setAmount(1);
        settlement.setBanner(banner);
        p.sendMessage("§aSuccessfully changed the settlement banner.");
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
