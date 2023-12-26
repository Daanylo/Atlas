package atlas.atlas.Managers;

import atlas.atlas.Regions.Selection;
import org.bukkit.Location;

public class SpawnManager {

    Selection spawnArea;
    Location spawn;

    public SpawnManager() {
        this.spawnArea = null;
        this.spawn = null;
    }

    public Selection getSpawnArea() {
        return spawnArea;
    }

    public void setSpawnArea(Selection spawnArea) {
        this.spawnArea = spawnArea;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
}
