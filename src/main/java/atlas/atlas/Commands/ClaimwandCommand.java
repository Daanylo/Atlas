package atlas.atlas.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClaimwandCommand implements CommandExecutor {

    public HashMap<String, Long> cooldowns = new HashMap<>();

    public static ItemStack getClaimwand() {
        ItemStack claimwand = new ItemStack(Material.STICK);
        ItemMeta meta = claimwand.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + ChatColor.YELLOW.toString() + "Claim Wand");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.RESET + ChatColor.GRAY.toString() + "Left click to select the first point.");
        list.add(ChatColor.RESET + ChatColor.GRAY.toString() + "Right click to select the second point.");
        list.add(ChatColor.RESET + ChatColor.GRAY.toString() + "Use /settlement claim (name).");
        meta.setLore(list);
        claimwand.setItemMeta(meta);
        return claimwand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        int cooldownTime = 3;

        if(cooldowns.containsKey(commandSender.getName())) {
            long secondsLeft = ((cooldowns.get(commandSender.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
            if(secondsLeft>0) {
                commandSender.sendMessage("You cant use that command for another "+ secondsLeft +" seconds.");
                return true;
            }
        }

        cooldowns.put(commandSender.getName(), System.currentTimeMillis());
        Player player = (Player) commandSender;

        if (player.getInventory().containsAtLeast(getClaimwand(), 1)) {

            commandSender.sendMessage("You already have a Claim Wand.");

        }

        else {
            player.getInventory().addItem(getClaimwand());
            commandSender.sendMessage("You have received a " + ChatColor.YELLOW + "Claim Wand");
            commandSender.sendMessage("Left click to select the first point.");
            commandSender.sendMessage("Right click to select the second point.");
            commandSender.sendMessage("To claim a settlement, use " + ChatColor.GRAY + ChatColor.ITALIC + "/settlement claim (name)" + ChatColor.RESET + ".");
        }
        return true;

    }
}
