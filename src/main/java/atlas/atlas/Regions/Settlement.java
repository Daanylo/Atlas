package atlas.atlas.Regions;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Settlement {

    private String name;
    private UUID leaderUUID;
    private ArrayList<UUID> members;
    private Selection area;
    private Location home;
    private ItemStack banner;
    private double reserves;
    private int level;

    public Settlement(String name, UUID leaderUUID, ArrayList<UUID> members, Selection area, Location home,ItemStack banner, double reserves, int level) {
        this.name = name;
        this.leaderUUID = leaderUUID;
        this.members = members;
        this.area = area;
        this.home = home;
        this.banner = banner;
        this.reserves = reserves;
        this.level = level;
    }

    public String getName() {
        return name;
    }
    public String getRawName() {
        return getName().replaceAll("§.", "");
    }
    public void setName(String name) {
        this.name = name;
    }
    public UUID getLeader() {
        return leaderUUID;
    }
    public void setLeader(UUID leaderUUID) {
        this.leaderUUID = leaderUUID;
    }
    public ArrayList<UUID> getMembers() {
        return members;
    }
    public void setMembers(ArrayList<UUID> members) {
        this.members = members;
    }
    public void removeMember(UUID member) {
        members.remove(member);
    }
    public Selection getArea() {
        return area;
    }
    public void setArea(Selection area) {
        this.area = area;
    }
    public Location getHome() {
        return home;
    }
    public void setHome(Location home) {
        this.home = home;
    }
    public ItemStack getBanner() {
        return banner;
    }
    public void setBanner(ItemStack banner) {
        this.banner = banner;
    }
    public double getReserves() {
        return reserves;
    }
    public void setReserves(double reserves) {
        this.reserves = reserves;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}
