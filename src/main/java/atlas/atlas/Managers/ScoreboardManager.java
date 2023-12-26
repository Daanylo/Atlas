package atlas.atlas.Managers;

import atlas.atlas.Atlas;
import atlas.atlas.Players.AtlasPlayer;
import atlas.atlas.Regions.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class ScoreboardManager {

    static SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    HashMap<UUID, Scoreboard> scoreboards = new HashMap<>();

    HashMap<UUID, Scoreboard> getScoreboards() {
        return scoreboards;
    }

    public void setupScoreboard(Player player) {
        new BukkitRunnable() {
            final String playername = player.getName();
            @Override
            public void run() {
                if (Bukkit.getOfflinePlayer(playername).isOnline()) {
                    updateBoard(player);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Atlas.getInstance(), 0L, 20L);
    }

    private void updateBoard(Player p) {
        AtlasPlayerManager atlasPlayerManager = Atlas.getInstance().getAtlasPlayerManager();
        AtlasPlayer atlasPlayer = atlasPlayerManager.getAtlasPlayer(p.getUniqueId());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (atlasPlayer == null) {
            return;
        }
        org.bukkit.scoreboard.ScoreboardManager scoreboardHandler = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardHandler.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "main");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("            §2§lAt§a§llas§r            ");
        Score emptyLine1 = objective.getScore("");
        emptyLine1.setScore(10);
        Score settlementLine = objective.getScore("Settlement:");
        settlementLine.setScore(9);
        Score nameLine;
        if (settlementManager.getSettlement(p) != null) {
            Settlement settlement = settlementManager.getSettlement(p);
            nameLine = objective.getScore(settlement.getName());
        } else {
            nameLine = objective.getScore("None");
        }
        nameLine.setScore(8);
        Score emptyLine2 = objective.getScore("");
        emptyLine2.setScore(7);
        Score goldLine = objective.getScore("Gold:");
        goldLine.setScore(6);
        Score amountLine;
        amountLine = objective.getScore("§6" + decimalFormat.format(atlasPlayer.getGold()) + "g");
        amountLine.setScore(5);
        p.setScoreboard(scoreboard);
        scoreboards.put(p.getUniqueId(), scoreboard);
    }

}
