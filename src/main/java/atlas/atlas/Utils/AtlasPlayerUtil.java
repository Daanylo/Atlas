package atlas.atlas.Utils;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.AtlasPlayerManager;
import atlas.atlas.Players.AtlasPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class AtlasPlayerUtil {
    static AtlasPlayerManager atlasPlayerManager = Atlas.getInstance().getAtlasPlayerManager();

    public static void createAtlasPlayers() {
        new File(Atlas.getInstance().getDataFolder() + "/data").mkdir();
        File atlasPlayersFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "atlasplayers.yml");
        FileConfiguration fc = new YamlConfiguration();

        if (!atlasPlayersFile.exists()) {
            try {
                atlasPlayersFile.createNewFile();
                fc.save(atlasPlayersFile);
                Bukkit.getLogger().info("atlasplayers.yml created.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void saveAtlasPlayers() {
        File atlasPlayersFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "atlasplayers.yml");
        FileConfiguration fc = new YamlConfiguration();
        ArrayList<AtlasPlayer> atlasPlayers = atlasPlayerManager.getAtlasPlayers();
        if (atlasPlayers != null) {
            for (AtlasPlayer atlasPlayer : atlasPlayers) {
                UUID uuid = atlasPlayer.getUUID();
                double gold = atlasPlayer.getGold();
                fc.set(uuid.toString() + ".gold", gold);
            }
            try {
                fc.save(atlasPlayersFile);
                Bukkit.getLogger().info("Saving atlasplayers...");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    public static void loadAtlasPlayers() {
        File atlasPlayersFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "atlasplayers.yml");
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(atlasPlayersFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<AtlasPlayer> atlasPlayers = new ArrayList<>();
        if (!(fc.getKeys(false).size() == 0)) {
            Set<String> playerUUIDs = fc.getKeys(false);
            for (String playerUUID : playerUUIDs) {
                UUID uuid = UUID.fromString(playerUUID);
                double gold = fc.getDouble(uuid + ".gold");
                AtlasPlayer atlasPlayer = new AtlasPlayer(uuid, gold);
                atlasPlayers.add(atlasPlayer);
            }
        }
        atlasPlayerManager.setAtlasPlayers(atlasPlayers);
        Bukkit.getLogger().info("Loading atlasplayers...");
    }
}
