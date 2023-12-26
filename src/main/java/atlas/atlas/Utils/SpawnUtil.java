package atlas.atlas.Utils;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SpawnManager;
import atlas.atlas.Regions.Selection;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class SpawnUtil {

    static SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();

    public static void createSpawn() {
        new File(Atlas.getInstance().getDataFolder() + "/data").mkdir();
        File spawnFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "spawn.yml");
        FileConfiguration fc = new YamlConfiguration();

        if (!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
                fc.save(spawnFile);
                Bukkit.getLogger().info("spawn.yml created.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void saveSpawn() {
        File spawnFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "spawn.yml");
        FileConfiguration fc = new YamlConfiguration();
        if (spawnManager.getSpawnArea() != null) {
            Selection spawnArea = spawnManager.getSpawnArea();
            fc.set("area.xA", spawnArea.getxA());
            fc.set("area.zA", spawnArea.getzA());
            fc.set("area.xB", spawnArea.getxB());
            fc.set("area.zB", spawnArea.getzB());
        }
        if (spawnManager.getSpawn() != null) {
            Location spawn = spawnManager.getSpawn();
            fc.set("spawn", spawn);
        }
        try {
            fc.save(spawnFile);
            Bukkit.getLogger().info("Saving spawn...");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadSpawn() {
        File spawnFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "spawn.yml");
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(spawnFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fc.getKeys(false).size() > 0) {
            Selection spawnArea = new Selection(fc.getInt("area.xA"), fc.getInt("area.zA"), fc.getInt("area.xB"), fc.getInt("area.zB"));
            Location spawn = (Location) fc.get("spawn");
            spawnManager.setSpawnArea(spawnArea);
            spawnManager.setSpawn(spawn);
        }
        Bukkit.getLogger().info("Loading spawn...");
    }
}
