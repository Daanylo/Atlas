package atlas.atlas.Managers;

import atlas.atlas.Atlas;
import atlas.atlas.Players.AtlasPlayer;
import atlas.atlas.Regions.Settlement;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    HashMap<UUID, Scoreboard> scoreboards = new HashMap<>();
    static HashMap<UUID, BossBar> playerBossBars = new HashMap<>();
    public static HashMap<UUID, BossBar> getPlayerBossBars() {
        return playerBossBars;
    }

    HashMap<UUID, Scoreboard> getScoreboards() {
        return scoreboards;
    }


    public void setupScoreboard(Player player) {
        new BukkitRunnable() {
            final UUID uuid = player.getUniqueId();
            @Override
            public void run() {
                if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
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

        p.setPlayerListHeaderFooter("You are playing on §2§lAt§a§llas", "Currently " + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + " players online");

        BossBar customBossBar = BossBar.bossBar(Component.text("Current Settlement: "), 1.0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        if (!playerBossBars.containsKey(p.getUniqueId())) {
            customBossBar.addViewer(p);
            playerBossBars.put(p.getUniqueId(), customBossBar);
        }
        String location = "None";
        if (settlementManager.getSettlement(p.getLocation()) != null) {
            location = settlementManager.getSettlement(p.getLocation()).getName();
        }
        playerBossBars.get(p.getUniqueId()).name(Component.text("Current Settlement: " + location));
    }
}
