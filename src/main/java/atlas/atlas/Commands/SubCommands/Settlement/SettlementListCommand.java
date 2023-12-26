package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import atlas.atlas.Commands.SubCommands.SubCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SettlementListCommand extends SubCommand {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        int maxPages = (int) Math.ceil(settlementManager.getSettlements().keySet().size()/10f);
        ArrayList<Settlement> settlementList = new ArrayList<>();
        for (String settlementID : settlementManager.getSettlements().keySet()) {
            Settlement settlement = settlementManager.getSettlements().get(settlementID);
            settlementList.add(settlement);
        }
        int pageNumber = 1;
        if (args.length > 1 && args[1].matches("1234567890") && !(Integer.parseInt(args[1]) <= 1)) {
            pageNumber = Integer.parseInt(args[1]);
        }
        if (pageNumber > maxPages) {
            pageNumber = maxPages;
        }
        p.sendMessage("§e§lSettlement list: §7("+ pageNumber + "/" + maxPages + ")");
        for (int i = (10 * pageNumber - 10); i < (10 * pageNumber); i++) {
            if (settlementList.size() > i) {
                Settlement settlement = settlementList.get(i);
                TextComponent settlementInfo = new TextComponent(i + 1 + ") " + settlement.getName());
                settlementInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Bukkit.getOfflinePlayer(settlement.getLeader()).getName()).create()));
                settlementInfo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement info " + settlement.getRawName()));
                p.spigot().sendMessage(settlementInfo);
            }
        }
        TextComponent next = null;
        TextComponent previous = null;
        TextComponent spaces = new TextComponent("         ");
        if (pageNumber < maxPages) {
            next = new TextComponent("§7Next");
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement list " + (pageNumber + 1)));
        }
        if (pageNumber > 1) {
            previous = new TextComponent("§Previous");
            previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement list " + (pageNumber - 1)));
        }
        if (next == null && previous == null) {
            return;
        }
        if (next != null && previous == null) {
            p.spigot().sendMessage(next);
            return;
        }
        if (previous != null && next == null) {
            p.spigot().sendMessage(previous);
            return;
        }
        p.spigot().sendMessage(previous, spaces, next);
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
