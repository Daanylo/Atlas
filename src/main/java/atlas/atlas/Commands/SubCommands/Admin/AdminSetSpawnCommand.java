package atlas.atlas.Commands.SubCommands.Admin;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Managers.SpawnManager;
import org.bukkit.entity.Player;

public class AdminSetSpawnCommand extends SubCommand {
    SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();
    @Override
    public void execute(Player p, String[] args) {
        if (!spawnManager.getSpawnArea().isWithin(p.getLocation())) {
            p.sendMessage("§cYou're not inside the spawn area.");
            return;
        }
        spawnManager.setSpawn(p.getLocation());
        p.sendMessage("§aSpawn successfully set.");
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
