package atlas.atlas.Managers;

import atlas.atlas.Regions.Settlement;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SettlementManager {

    public HashMap<String, Settlement> settlements;
    public HashMap<UUID, Integer> teleportQueue;
    public HashMap<UUID, Boolean> teleportCancelled;
    public HashMap<String, ArrayList<UUID>> invites;
    public SettlementManager() {
        settlements = new HashMap<>();
        teleportQueue = new HashMap<>();
        teleportCancelled = new HashMap<>();
        invites = new HashMap<>();
    }
    public HashMap<String, Settlement> getSettlements() {
        return settlements;
    }
    public void addSettlement(String uuid, Settlement settlement) {
        settlements.put(uuid, settlement);
    }
    public void removeSettlement(String settlementID) {
        settlements.remove(settlementID);
    }
    public String getSettlementID(Settlement settlement) {
        for (String settlementID : settlements.keySet()) {
            if (settlements.get(settlementID) == settlement) {
                return settlementID;
            }
        }
        return null;
    }
    public Settlement getSettlement(Player p) {
        for (String settlementID : settlements.keySet()) {
            Settlement settlement = settlements.get(settlementID);
            if (settlement.getMembers().contains(p.getUniqueId())) {
                return settlement;
            }
        }
        return null;
    }

    public Settlement getSettlement(OfflinePlayer p) {
        for (String settlementID : settlements.keySet()) {
            Settlement settlement = settlements.get(settlementID);
            if (settlement.getMembers().contains(p.getUniqueId())) {
                return settlement;
            }
        }
        return null;
    }
    public Settlement getSettlement(String rawName) {
        for (String settlementID : settlements.keySet()) {
            Settlement settlement = settlements.get(settlementID);
            if (Objects.equals(settlement.getRawName(), rawName)) {
                return settlement;
            }
        }
        return null;
    }
    public Settlement getSettlement(Location loc) {
        if (!loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return null;
        }
        for (String settlementID : settlements.keySet()) {
            Settlement settlement = settlements.get(settlementID);
            int xA = settlement.getArea().getxA();
            int xB = settlement.getArea().getxB();
            int zA = settlement.getArea().getzA();
            int zB = settlement.getArea().getzB();
            int locX = loc.getBlockX();
            int locZ = loc.getBlockZ();
            int minX = Math.min(xA, xB);
            int maxX = Math.max(xA, xB);
            int minZ = Math.min(zA, zB);
            int maxZ = Math.max(zA, zB);
            if ((minX <= locX && locX <= maxX) && (minZ <= locZ && locZ <= maxZ)) {
                return settlement;
            }
        }
        return null;
    }
    public boolean isAvailable(String name) {
        String rawName = name.replaceAll("&.", "");
        for (String settlementID : settlements.keySet()) {
            Settlement settlement = settlements.get(settlementID);
            if (rawName.equalsIgnoreCase(settlement.getRawName())) {
                return false;
            }
        }
        return true;
    }
    public boolean containsIllegalCharacters(String name) {
        char[] validChars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890-&'".toCharArray();
        return !StringUtils.containsOnly(name, validChars);
    }
    public String convertColor(ChatColor color) {
        if (color == ChatColor.GRAY) {
            return "gray";
        }
        if (color == ChatColor.RED) {
            return "ruby";
        }
        if (color == ChatColor.BLUE) {
            return "cyan";
        }
        if (color == ChatColor.GREEN) {
            return "lime";
        }
        if (color == ChatColor.GOLD) {
            return "orange";
        }
        if (color == ChatColor.YELLOW) {
            return "yellow";
        }
        if (color == ChatColor.BLACK) {
            return "black";
        }
        if (color == ChatColor.WHITE) {
            return "white";
        }
        if (color == ChatColor.AQUA) {
            return "aqua";
        }
        if (color == ChatColor.DARK_PURPLE) {
            return "purple";
        }
        if (color == ChatColor.LIGHT_PURPLE) {
            return "pink";
        }
        if (color == ChatColor.DARK_BLUE) {
            return "blue";
        }
        if (color == ChatColor.DARK_RED) {
            return "red";
        }
        if (color == ChatColor.DARK_GREEN) {
            return "green";
        }
        if (color == ChatColor.DARK_AQUA) {
            return "ocean";
        }
        if (color == ChatColor.DARK_GRAY) {
            return "shadow";
        }
        return null;
    }
    public ChatColor convertColor(String color) {
        if (color.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY;
        }
        if (color.equalsIgnoreCase("ruby")) {
            return ChatColor.RED;
        }
        if (color.equalsIgnoreCase("cyan")) {
            return ChatColor.BLUE;
        }
        if (color.equalsIgnoreCase("lime")) {
            return ChatColor.GREEN;
        }
        if (color.equalsIgnoreCase("orange")) {
            return ChatColor.GOLD;
        }
        if (color.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW;
        }
        if (color.equalsIgnoreCase("black")) {
            return ChatColor.BLACK;
        }
        if (color.equalsIgnoreCase("white")) {
            return ChatColor.WHITE;
        }
        if (color.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA;
        }
        if (color.equalsIgnoreCase("purple")) {
            return ChatColor.DARK_PURPLE;
        }
        if (color.equalsIgnoreCase("pink")) {
            return ChatColor.LIGHT_PURPLE;
        }
        if (color.equalsIgnoreCase("blue")) {
            return ChatColor.DARK_BLUE;
        }
        if (color.equalsIgnoreCase("red")) {
            return ChatColor.DARK_RED;
        }
        if (color.equalsIgnoreCase("green")) {
            return ChatColor.DARK_GREEN;
        }
        if (color.equalsIgnoreCase("ocean")) {
            return ChatColor.DARK_AQUA;
        }
        if (color.equalsIgnoreCase("shadow")) {
            return ChatColor.DARK_GRAY;
        }
        return null;
    }
}
