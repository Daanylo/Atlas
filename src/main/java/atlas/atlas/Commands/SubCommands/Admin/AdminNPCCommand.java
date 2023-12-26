package atlas.atlas.Commands.SubCommands.Admin;

import atlas.atlas.Commands.SubCommands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class AdminNPCCommand extends SubCommand {
    @Override
    public void execute(Player p, String[] args) {
        if (args[1].equals("sell")) {
            spawnSellNPC(p);
        }
        if (args[1].equals("remove")) {
            removeSellNPC();
        }
    }
    private void spawnSellNPC(Player player) {
        Villager sellNPC = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        sellNPC.setProfession(Villager.Profession.NITWIT);
        sellNPC.setCustomName("§eSell NPC");
        sellNPC.setCustomNameVisible(true);
        sellNPC.setInvulnerable(true);
        sellNPC.setCanPickupItems(false);
        sellNPC.setAI(false);
        sellNPC.setSilent(true);
    }

    private void removeSellNPC() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Villager && entity.getCustomName() != null && entity.getCustomName().equals("§eSell NPC")) {
                    entity.remove();
                    return;
                }
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
