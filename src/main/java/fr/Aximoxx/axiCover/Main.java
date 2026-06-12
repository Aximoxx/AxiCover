package fr.Aximoxx.axiCover;

import fr.Aximoxx.axiCover.command.ConfigCommands;
import fr.Aximoxx.axiCover.command.GameCommands;
import fr.Aximoxx.axiCover.listener.ChatListener;
import fr.Aximoxx.axiCover.listener.InteractListener;
import fr.Aximoxx.axiCover.listener.JoinListener;
import fr.Aximoxx.axiCover.listener.LeaveListener;
import fr.Aximoxx.axiCover.manager.GameManager;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;
    private GameManager gameManager;
    private String type = "dictionnaire";

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
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getType() { return type; }
    public static Main getInstance() { return instance; }
    public GameManager getGameManager() { return gameManager; }



    public void setType(String type) { this.type = type; }
}
