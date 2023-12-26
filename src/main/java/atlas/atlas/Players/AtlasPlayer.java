package atlas.atlas.Players;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class AtlasPlayer {
    private UUID uuid;
    private double gold;
    public AtlasPlayer(UUID uuid, double gold) {
        this.uuid = uuid;
        this.gold = gold;
    }
    public UUID getUUID() {
        return uuid;
    }
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
    public double getGold() {
        return gold;
    }
    public void setGold(double gold) {
        this.gold = gold;
    }
}
