package fr.Aximoxx.axiCover;

import fr.Aximoxx.axiCover.command.ConfigCommands;
import fr.Aximoxx.axiCover.command.GameCommands;
import fr.Aximoxx.axiCover.listener.*;
import fr.Aximoxx.axiCover.manager.GameManager;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    private static Main instance;
    private GameManager gameManager;
    private String type = "dictionnaire";

    List<String> serialized = new ArrayList<>();
    private final List<Location> spawns = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (!getConfig().getBoolean("config.saved")) saveResource("config.yml", true);

        FastInvManager.register(this);

        gameManager = new GameManager();
        getCommand("config").setExecutor(new ConfigCommands());
        getCommand("uc").setExecutor(new GameCommands());

        registerListeners();
        loadSpawns();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new InteractAtEntity(), this);
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

    public String           getType()        { return type; }
    public List<Location>   getSpawns()      { return spawns; }
    public static Main      getInstance()    { return instance; }
    public GameManager      getGameManager() { return gameManager; }

    public void setType(String type) { this.type = type; }
}
