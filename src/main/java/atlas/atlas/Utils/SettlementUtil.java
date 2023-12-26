package atlas.atlas.Utils;

import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Atlas;
import atlas.atlas.Regions.Selection;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class SettlementUtil {

    static SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    public static void createSettlements() {
        new File(Atlas.getInstance().getDataFolder() + "/data").mkdir();
        File settlementsFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "settlements.yml");
        FileConfiguration fc = new YamlConfiguration();

        if (!settlementsFile.exists()) {
            try {
                settlementsFile.createNewFile();
                fc.save(settlementsFile);
                Bukkit.getLogger().info("settlements.yml created.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void saveSettlements() {
        File settlementsFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "settlements.yml");
        FileConfiguration fc = new YamlConfiguration();
        HashMap<String, Settlement> settlements = settlementManager.getSettlements();
        if (settlements != null) {
            Set<String> settlementsIDs = settlements.keySet();
            for (String settlementID : settlementsIDs) {
                Settlement settlement = settlements.get(settlementID);
                ArrayList<String> members = new ArrayList<>();
                for (UUID member : settlement.getMembers()) {
                    members.add(member.toString());
                }
                fc.set(settlementID + ".name", settlement.getName());
                fc.set(settlementID + ".leader", settlement.getLeader().toString());
                fc.set(settlementID + ".members", members);
                fc.set(settlementID + ".xA", settlement.getArea().getxA());
                fc.set(settlementID + ".zA", settlement.getArea().getzA());
                fc.set(settlementID + ".xB", settlement.getArea().getxB());
                fc.set(settlementID + ".zB", settlement.getArea().getzB());
                fc.set(settlementID + ".home", settlement.getHome());
                fc.set(settlementID + ".reserves", settlement.getReserves());
                fc.set(settlementID + ".level", settlement.getLevel());
            }
            try {
                fc.save(settlementsFile);
                Bukkit.getLogger().info("Saving settlements...");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    public static void loadSettlements() {
        File settlementsFile = new File(Atlas.getInstance().getDataFolder() + "/data/" + "settlements.yml");
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(settlementsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(fc.getKeys(false).size() == 0)) {
            Set<String> settlementIDs = fc.getKeys(false);
            for (String settlementID : settlementIDs) {
                ArrayList<UUID> members = new ArrayList<>();
                String name = fc.getString(settlementID + ".name");
                UUID leaderUUID = UUID.fromString(fc.getString(settlementID + ".leader"));
                @SuppressWarnings("unchecked")
                ArrayList<String> membersList = (ArrayList<String>) fc.get(settlementID + ".members");
                if (membersList != null) {
                    for (String memberUUID : membersList) {
                        members.add(UUID.fromString(memberUUID));
                    }
                }
                Selection area = new Selection(fc.getInt(settlementID + ".xA"), fc.getInt(settlementID + ".zA"), fc.getInt(settlementID + ".xB"), fc.getInt(settlementID + ".zB"));
                Location home = (Location) fc.get(settlementID + ".home");
                int reserves = fc.getInt(settlementID + ".reserves");
                int level = fc.getInt(settlementID + ".level");
                Settlement settlement = new Settlement(name, leaderUUID, members, area, home, reserves, level);
                settlementManager.addSettlement(settlementID, settlement);
            }
        }
        Bukkit.getLogger().info("Loading settlements...");
    }
}
