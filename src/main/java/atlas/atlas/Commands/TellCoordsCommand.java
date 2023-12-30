package atlas.atlas.Commands;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class TellCoordsCommand implements CommandExecutor {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        String settlementName = "";
        int x = (int) p.getLocation().getX();
        int y = (int) p.getLocation().getY();
        int z = (int) p.getLocation().getZ();
        if (settlementManager.getSettlement(p) != null) {
            settlementName = settlementManager.getSettlement(p).getName();
        }
        Bukkit.getServer().broadcastMessage("§f[§r" + settlementName + "§f]§r " + p.getName() + ": X: " + x + " Y: " + y + " Z: " + z);
        return true;
    }
}
