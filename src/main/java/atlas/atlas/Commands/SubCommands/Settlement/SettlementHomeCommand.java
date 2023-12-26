package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SettlementHomeCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a settlement.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        if (settlement.getHome() == null) {
            p.sendMessage("§cYour settlement has not set a home yet.");
            return;
        }
        if (!settlementManager.teleportQueue.containsKey(p.getUniqueId())) {
            HashMap<UUID, Integer> queue = settlementManager.teleportQueue;
            queue.put(p.getUniqueId(), 100);
            settlementManager.teleportCancelled.put(p.getUniqueId(), false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int timeLeft = queue.get(p.getUniqueId());
                    queue.put(p.getUniqueId(), timeLeft - 20);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You will be teleported in §e" + timeLeft / 20 + " §rseconds. Don't move!"));
                    if (timeLeft <= 0) {
                        this.cancel();
                        queue.remove(p.getUniqueId());
                        p.teleport(settlement.getHome());
                        p.sendMessage("§aYou have been teleported to the settlement home.");
                    }
                    if (settlementManager.teleportCancelled.get(p.getUniqueId())) {
                        this.cancel();
                        queue.remove(p.getUniqueId());
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cTeleport has been cancelled because you moved."));
                    }
                }
            }.runTaskTimer(Atlas.getInstance(), 0L, 20L);
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
