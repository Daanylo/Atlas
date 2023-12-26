package atlas.atlas.Commands.SubCommands.Admin;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.AtlasPlayerManager;
import atlas.atlas.Players.AtlasPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class AdminGoldCommand extends SubCommand {
    AtlasPlayerManager atlasPlayerManager = Atlas.getInstance().getAtlasPlayerManager();
    @Override
    public void execute(Player p, String[] args) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (args[1].equals("set")) {
            if (Bukkit.getPlayerUniqueId(args[2]) != null) {
                AtlasPlayer atlasPlayer = atlasPlayerManager.getAtlasPlayer(Bukkit.getPlayerUniqueId(args[2]));
                atlasPlayer.setGold(Double.parseDouble(args[3]));
                p.sendMessage("§aSuccessfully set §f" + Bukkit.getOfflinePlayer(atlasPlayer.getUUID()).getName() + "§a's §6Gold §ato §6" + decimalFormat.format(atlasPlayer.getGold()) + "g§a.");
            }
        }
        if (args[1].equals("add")) {
            if (Bukkit.getPlayerUniqueId(args[2]) != null) {
                AtlasPlayer atlasPlayer = atlasPlayerManager.getAtlasPlayer(Bukkit.getPlayerUniqueId(args[2]));
                double amount = Double.parseDouble(args[3]);
                atlasPlayer.setGold(atlasPlayer.getGold() + amount);
                p.sendMessage("§aSuccessfully added §6" + decimalFormat.format(amount) + "g Gold§a to §f" + Bukkit.getOfflinePlayer(atlasPlayer.getUUID()).getName());
            }
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
