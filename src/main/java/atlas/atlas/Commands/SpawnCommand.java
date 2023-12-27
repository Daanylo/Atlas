package atlas.atlas.Commands;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Managers.SpawnManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class SpawnCommand implements CommandExecutor {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();
    SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if (spawnManager.getSpawn() == null) {
            p.sendMessage("§cThe spawn has not been set.");
            return true;
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
                        p.teleport(spawnManager.getSpawn());
                        p.sendMessage("§aYou have been teleported to the spawn.");
                    }
                    if (settlementManager.teleportCancelled.get(p.getUniqueId())) {
                        this.cancel();
                        queue.remove(p.getUniqueId());
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cTeleport has been cancelled because you moved."));
                    }
                }
            }.runTaskTimer(Atlas.getInstance(), 0L, 20L);
        }
        return true;
    }
}
