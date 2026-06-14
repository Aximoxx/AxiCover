package fr.Aximoxx.axiCover;

import fr.Aximoxx.axiCover.command.ConfigCommands;
import fr.Aximoxx.axiCover.command.GameCommands;
import fr.Aximoxx.axiCover.command.PermissionCommands;
import fr.Aximoxx.axiCover.listener.*;
import fr.Aximoxx.axiCover.listener.falseInteraction.AttackListener;
import fr.Aximoxx.axiCover.listener.falseInteraction.BreakListener;
import fr.Aximoxx.axiCover.listener.falseInteraction.MoveListener;
import fr.Aximoxx.axiCover.listener.gameListener.ChatListener;
import fr.Aximoxx.axiCover.listener.gameListener.InteractAtEntityListener;
import fr.Aximoxx.axiCover.listener.gameListener.InteractListener;
import fr.Aximoxx.axiCover.manager.GameManager;
import fr.Aximoxx.axiCover.manager.game.*;
import fr.Aximoxx.axiCover.manager.mots.WordManager;
import fr.Aximoxx.axiCover.utils.LuckPermsManager;
import fr.mrmicky.fastinv.FastInvManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    private static Main instance;
    private LuckPerms luckPerms;
    private String type = "dictionnaire";

    // Manager
    private GameManager gameManager;
    private WordManager wordManager;
    private VoteManager voteManager;
    private StartManager startManager;
    private CheckWinManager checkWinManager;
    private NextRoundManager nextRoundManager;
    private WhiteGuessManager whiteGuessManager;

    List<String> serialized = new ArrayList<>();
    private final List<Location> spawns = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (!getConfig().getBoolean("config.saved")) saveResource("config.yml", true);

        getLogger().info("§7Se plugin à besoin de §6LuckPerms§7 pour fonctionner.");
        getLogger().info("§7Vérification de §6LuckPerms§7...");

        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPermsProvider.get();
            LuckPermsManager.init(luckPerms);
            getLogger().info("§6LuckPerms§7 §2trouvé§7 et §2initialisé§7 avec §2Succès§7 !");
        } else {
            getLogger().warning("§c§lERREUR§7, §6LuckPerms§7 introuvable. §4§lExtinction du plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        FastInvManager.register(this);

        gameManager = new GameManager();
        wordManager = new WordManager();
        voteManager = new VoteManager();
        startManager = new StartManager();
        checkWinManager = new CheckWinManager();
        nextRoundManager = new NextRoundManager();
        whiteGuessManager = new WhiteGuessManager();

        getCommand("uc").setExecutor(new GameCommands());
        getCommand("config").setExecutor(new ConfigCommands());

        getCommand("sethost").setExecutor(new PermissionCommands());
        getCommand("removehost").setExecutor(new PermissionCommands());

        registerListeners();
        loadSpawns();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(),     this);
        getServer().getPluginManager().registerEvents(new JoinListener(),     this);
        getServer().getPluginManager().registerEvents(new MoveListener(),     this);
        getServer().getPluginManager().registerEvents(new BreakListener(),    this);
        getServer().getPluginManager().registerEvents(new LeaveListener(),    this);
        getServer().getPluginManager().registerEvents(new AttackListener(),   this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new InteractAtEntityListener(), this);
    }

    public void updateTag(Player p){
        if (p.hasPermission("axicover.host")) {
            p.setPlayerListName("§d§l✸ HOST §8|§f " + p.getName());
        } else p.setPlayerListName("§7§lJoueur §8|§f " + p.getName());
    }

    public void loadSpawns() {
        List<String> serialized = Main.getInstance().getConfig().getStringList("spawns");

        for (String s : serialized) {
            String[] parts = s.split(",");
            World world = Bukkit.getWorld(parts[0]);
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int z = Integer.parseInt(parts[3]);
            spawns.add(new Location(world, x, y, z));
        }
    }

    public void saveSpawn() {
        serialized.clear();

        for (Location loc : Main.getInstance().getSpawns())
            serialized.add(loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());

        getConfig().set("spawns", serialized);
        Main.getInstance().saveConfig();
    }

    @Override
    public void onDisable() {
        saveSpawn();
    }

    public String            getType()              { return type; }
    public List<Location>    getSpawns()            { return spawns; }
    public static Main       getInstance()          { return instance; }
    public GameManager       getGameManager()       { return gameManager; }
    public WordManager       getWordManager()       { return wordManager; }
    public VoteManager       getVoteManager()       { return voteManager; }
    public StartManager      getStartManager()      { return startManager; }
    public CheckWinManager   getCheckWinManager()   { return checkWinManager; }
    public NextRoundManager  getNextRoundManager()  { return nextRoundManager; }
    public WhiteGuessManager getWhiteGuessManager() { return whiteGuessManager; }

    public void setType(String type) { this.type = type; }
}
