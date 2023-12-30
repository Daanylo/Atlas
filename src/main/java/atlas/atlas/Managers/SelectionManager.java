package atlas.atlas.Managers;

import atlas.atlas.Atlas;
import atlas.atlas.Regions.Settlement;
import atlas.atlas.Regions.Selection;

import java.util.HashMap;
import java.util.UUID;

public class SelectionManager {
    public HashMap<UUID, Selection> selections;
    public HashMap<String, Settlement> settlements = Atlas.getInstance().getSettlementManager().getSettlements();
    SpawnManager spawnManager = Atlas.getInstance().getSpawnManager();

    public SelectionManager() {
        this.selections = new HashMap<>();
    }

    public HashMap<UUID, Selection> getSelections() {
        return selections;
    }

    public void setSelections(HashMap<UUID, Selection> selections) {
        this.selections = selections;
    }

    public boolean selectionOverlaps(Selection selection) {
        for (int i = selection.getMinX(); i <= selection.getMaxX(); i++) {
            for (int j = selection.getMinZ(); j <= selection.getMaxZ(); j++) {
                for (String settlementID : settlements.keySet()) {
                    Settlement settlement = settlements.get(settlementID);
                    int xA = settlement.getArea().getxA();
                    int xB = settlement.getArea().getxB();
                    int zA = settlement.getArea().getzA();
                    int zB = settlement.getArea().getzB();
                    int minX = Math.min(xA, xB);
                    int maxX = Math.max(xA, xB);
                    int minZ = Math.min(zA, zB);
                    int maxZ = Math.max(zA, zB);
                    if ((minX <= i && i <= maxX) && (minZ <= j && j <= maxZ)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Settlement selectionOverlapsSettlement(Selection selection) {
        for (int i = selection.getMinX(); i <= selection.getMaxX(); i++) {
            for (int j = selection.getMinZ(); j <= selection.getMaxZ(); j++) {
                for (String settlementID : settlements.keySet()) {
                    Settlement settlement = settlements.get(settlementID);
                    int xA = settlement.getArea().getxA();
                    int xB = settlement.getArea().getxB();
                    int zA = settlement.getArea().getzA();
                    int zB = settlement.getArea().getzB();
                    int minX = Math.min(xA, xB);
                    int maxX = Math.max(xA, xB);
                    int minZ = Math.min(zA, zB);
                    int maxZ = Math.max(zA, zB);
                    if ((minX <= i && i <= maxX) && (minZ <= j && j <= maxZ)) {
                        return settlement;
                    }
                }
            }
        }
        return null;
    }



    public boolean selectionOverlapsSpawn(Selection selection) {
        if (spawnManager.getSpawnArea() == null) {
            return false;
        }
        for (int i = selection.getMinX(); i <= selection.getMaxX(); i++) {
            for (int j = selection.getMinZ(); j <= selection.getMaxZ(); j++) {
                if (spawnManager.getSpawnArea().isWithin(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }
}
