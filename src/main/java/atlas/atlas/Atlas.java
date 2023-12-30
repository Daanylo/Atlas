package atlas.atlas;

import atlas.atlas.Commands.*;
import atlas.atlas.Handlers.*;
import atlas.atlas.Managers.*;
import atlas.atlas.Players.NameTag;
import atlas.atlas.TabCompleters.AdminTabCompleter;
import atlas.atlas.TabCompleters.MarketTabCompleter;
import atlas.atlas.Utils.AtlasPlayerUtil;
import atlas.atlas.Utils.MarketUtil;
import atlas.atlas.Utils.SettlementUtil;
import atlas.atlas.TabCompleters.SettlementTabCompleter;
import atlas.atlas.Utils.SpawnUtil;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Atlas extends JavaPlugin {

    public static Atlas instance;
    public SettlementManager settlementManager;
    public SelectionManager selectionManager;
    public ScoreboardManager scoreboardManager;
    public MarketManager marketManager;
    public AtlasPlayerManager atlasPlayerManager;
    public SpawnManager spawnManager;
    public ProtocolManager protocolManager;
    @Override
    public void onEnable() {

        //initialize managers
        settlementManager = new SettlementManager();
        spawnManager = new SpawnManager();
        selectionManager = new SelectionManager();
        scoreboardManager = new ScoreboardManager();
        marketManager = new MarketManager();
        atlasPlayerManager = new AtlasPlayerManager();
        protocolManager = ProtocolLibrary.getProtocolManager();


        //load data files
        saveDefaultConfig();
        SettlementUtil.createSettlements();
        SettlementUtil.loadSettlements();
        AtlasPlayerUtil.createAtlasPlayers();
        AtlasPlayerUtil.loadAtlasPlayers();
        MarketUtil.createMarkets();
        MarketUtil.loadMarkets();
        SpawnUtil.createSpawn();
        SpawnUtil.loadSpawn();

        //register commands
        getCommand("atlas").setExecutor(new CommandHandler());
        getCommand("claimwand").setExecutor(new ClaimwandCommand());
        getCommand("settlement").setExecutor(new SettlementCommandHandler());
        getCommand("market").setExecutor(new MarketCommandHandler());
        getCommand("admin").setExecutor(new AdminCommandHandler());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("tellcoords").setExecutor(new TellCoordsCommand());

        getCommand("settlement").setTabCompleter(new SettlementTabCompleter());
        getCommand("market").setTabCompleter(new MarketTabCompleter());
        getCommand("admin").setTabCompleter(new AdminTabCompleter());

        //register events
        getServer().getPluginManager().registerEvents(new JoinLeaveHandler(), this);
        getServer().getPluginManager().registerEvents(new SelectionHandler(), this);
        getServer().getPluginManager().registerEvents(new SettlementHandler(), this);
        getServer().getPluginManager().registerEvents(new PvpHandler(), this);
        getServer().getPluginManager().registerEvents(new MessageHandler(), this);
        getServer().getPluginManager().registerEvents(new TeleportHandler(), this);
        getServer().getPluginManager().registerEvents(new MarketHandler(), this);
        getServer().getPluginManager().registerEvents(new CompassHandler(), this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            scoreboardManager.setupScoreboard(p);
            NameTag nametag = new NameTag(p);
            nametag.nameTagUpdater(p);
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
        SettlementUtil.saveSettlements();
        AtlasPlayerUtil.saveAtlasPlayers();
        MarketUtil.saveMarkets();
        SpawnUtil.saveSpawn();
    }

    public Atlas() {
        instance = this;
    }

    public static Atlas getInstance() {
        return instance;
    }
    public SettlementManager getSettlementManager() {
        return settlementManager;
    }
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    public MarketManager getMarketManager() {
        return marketManager;
    }
    public AtlasPlayerManager getAtlasPlayerManager() {
        return atlasPlayerManager;
    }
    public SpawnManager getSpawnManager() {
        return spawnManager;
    }
}
