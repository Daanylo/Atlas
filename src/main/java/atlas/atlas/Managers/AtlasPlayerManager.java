package atlas.atlas.Managers;

import atlas.atlas.Players.AtlasPlayer;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AtlasPlayerManager {
    public ArrayList<AtlasPlayer> atlasPlayers;
    public AtlasPlayerManager() {
        atlasPlayers = new ArrayList<>();
    }
    public ArrayList<AtlasPlayer> getAtlasPlayers() {
        return atlasPlayers;
    }
    public void setAtlasPlayers(ArrayList<AtlasPlayer> atlasPlayers) {
        this.atlasPlayers = atlasPlayers;
    }
    public AtlasPlayer getAtlasPlayer(UUID uuid) {
        for (AtlasPlayer atlasPlayer : atlasPlayers) {
            if (Objects.equals(atlasPlayer.getUUID(), uuid)) {
                return atlasPlayer;
            }
        }
        return null;
    }
}
