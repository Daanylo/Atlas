package atlas.atlas.Regions;

import org.bukkit.Location;
import org.bukkit.World;

public class Selection {

    private int xA;
    private int zA;
    private int xB;
    private int zB;

    public Selection(int xA, int zA, int xB, int zB) {
        this.xA = xA;
        this.zA = zA;
        this.xB = xB;
        this.zB = zB;
    }
    public int getxA() {
        return xA;
    }
    public void setxA(int xA) {
        this.xA = xA;
    }
    public int getzA() {
        return zA;
    }
    public void setzA(int zA) {
        this.zA = zA;
    }
    public int getxB() {
        return xB;
    }
    public void setxB(int xB) {
        this.xB = xB;
    }
    public int getzB() {
        return zB;
    }
    public void setzB(int zB) {
        this.zB = zB;
    }
    public int getArea(){
        int sideX = Math.abs((getxA()-getxB())) + 1;
        int sideZ = Math.abs((getzA()-getzB())) + 1;
        return sideX * sideZ;
    }
    public int getMinX() {
        return Math.min(getxA(), getxB());
    }
    public int getMaxX() {
        return Math.max(getxA(), getxB());
    }
    public int getMinZ() {
        return Math.min(getzA(), getzB());
    }
    public int getMaxZ() {
        return Math.max(getzA(), getzB());
    }
    public boolean isWithin(Location location) {
        if (!location.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return false;
        }
        int locX = location.getBlockX();
        int locZ = location.getBlockZ();
        int minX = Math.min(xA, xB);
        int maxX = Math.max(xA, xB);
        int minZ = Math.min(zA, zB);
        int maxZ = Math.max(zA, zB);
        if ((minX <= locX && locX <= maxX) && (minZ <= locZ && locZ <= maxZ)) {
            return true;
        }
        return false;
    }
    public boolean isWithin(int locX, int locZ) {
        int minX = Math.min(xA, xB);
        int maxX = Math.max(xA, xB);
        int minZ = Math.min(zA, zB);
        int maxZ = Math.max(zA, zB);
        if ((minX <= locX && locX <= maxX) && (minZ <= locZ && locZ <= maxZ)) {
            return true;
        }
        return false;
    }

    public Location findNearestLocation(Location playerLocation) {
        Location nearestLocation = null;
        double minDistance = Double.MAX_VALUE;

        int minX = Math.min(xA, xB);
        int maxX = Math.max(xA, xB);
        int minZ = Math.min(zA, zB);
        int maxZ = Math.max(zA, zB);
        double playerX = playerLocation.getX();
        double playerZ = playerLocation.getZ();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                int distance = (int) Math.sqrt(Math.pow((x - playerX), 2) + Math.pow((z - playerZ), 2));

                if (distance < minDistance) {
                    minDistance = distance;
                    nearestLocation = new Location(playerLocation.getWorld(), x, playerLocation.getY(), z);
                }
            }
        }
        return nearestLocation;
    }
}
