package fr.Aximoxx.axiCover.manager;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.mots.Words;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {
    private Words currentWord;
    private BukkitRunnable task;
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> turnPassed = new ArrayList<>();
    private final Map<UUID, Roles> playerRoles = new HashMap<>();
    private final Map<UUID, String> playerWords = new HashMap<>();
    BossBar bar = Bukkit.createBossBar("§fM. White est entrain de deviné le mot: ", BarColor.WHITE, BarStyle.SOLID);

    private boolean mWhiteGuess = false;
    private int start_timer = 3;
    private int mWhite_timer = 30;

    public void onStart(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (start_timer == 1) {
                    // todo tp
                } else if (start_timer == 0) {
                    distributeWord();

                    for (Player pls : Bukkit.getOnlinePlayers())
                        players.add(pls.getUniqueId());

                    Player first = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
                    if (first != null)
                        first.getInventory().setItem(0, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());

                    cancel();
                    return;
                }

                for (Player pls : Bukkit.getOnlinePlayers())
                    pls.sendTitle("", "§7Début dans §6" + start_timer + " secondes", 10, 10, 10);

                start_timer--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    public void distributeWord() {
        currentWord = Words.randomWords(Main.getInstance().getConfig().getString("config.type"));
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        int index = 0;

        if (Main.getInstance().getConfig().getBoolean("config.misterW"))
            playerRoles.put(players.get(index++).getUniqueId(), Roles.MISTER_WHITE);

        for (int i = 0; i < Main.getInstance().getConfig().getInt("config.undercover") && index < players.size(); i++)
            playerRoles.put(players.get(index++).getUniqueId(), Roles.UNDERCOVER);

        for (; index < players.size(); index++)
            playerRoles.put(players.get(index).getUniqueId(), Roles.CIVIL);

        // Envoyer à tout le monde
        for (Player p : players) {
            Roles role = playerRoles.get(p.getUniqueId());
            String playerWord = switch (role) {
                case UNDERCOVER -> currentWord.getUndercover();
                case MISTER_WHITE -> "§knegro";
                default -> currentWord.getCivil();
            };

            playerWords.put(p.getUniqueId(), playerWord);
            p.sendTitle("§fTu es " + switch (role) {
                        case UNDERCOVER -> "§cUndercover";
                        case MISTER_WHITE -> "§6Mister White";
                        default -> "§2Civil";
                    }, "§7Ton mot est §f" + playerWord, 10, 60, 10);
        }
    }

    public void changeWord(Player p) {
        currentWord = Words.randomWords(Main.getInstance().getConfig().getString("config.type"));

        Roles role = playerRoles.get(p.getUniqueId());
        String playerWord = role == Roles.UNDERCOVER ? currentWord.getUndercover() : role == Roles.MISTER_WHITE ? "§knegro" : currentWord.getCivil();

        p.sendTitle("", "§7Ton nouveau mot est §f" + playerWord, 10, 60, 10);


        Player first = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (first != null) first.getInventory().setItem(1, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());
    }

    public void checkWin() {
        long undercoverCount = players.stream().filter(id -> playerRoles.get(id) == Roles.UNDERCOVER).count();
        long civilCount = players.stream().filter(id -> playerRoles.get(id) == Roles.CIVIL).count();
        long misterWhiteCount = players.stream().filter(id -> playerRoles.get(id) == Roles.MISTER_WHITE).count();

        boolean undercoverWin = undercoverCount >= civilCount && civilCount > 0
                || Main.getInstance().getConfig().getInt("config.turns") <= 0;
        boolean civilWin = undercoverCount == 0 && misterWhiteCount == 0;

        for (Player p : Bukkit.getOnlinePlayers()){
            if (misterWhiteCount > 0 && undercoverCount == 0 && civilCount == 0) {
                misterWhiteGuess();
            } else if (civilWin) {
                p.sendMessage("§fLes §2§lCIVILS§f ont gagné !");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.6f, 1f);
                onEnd(p);
            } else if (undercoverWin) {
                p.sendMessage("§fLes §c§lUNDERCOVERS§f ont gagné !");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.6f ,1f);
                onEnd(p);
            } else startNextRound();
        }
    }

    public void onEnd(Player p){
        p.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        cleanUp();
    }

    public void misterWhiteGuess(){
        setmWhiteGuess(true);
        for (Player pls : Bukkit.getOnlinePlayers()) bar.addPlayer(pls);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (mWhite_timer == 0){
                    setmWhiteGuess(false);
                    bar.removeAll();
                    startNextRound();

                    cancel();
                    return;
                }

                bar.setTitle("§fTemps restant: " + mWhite_timer);
                bar.setProgress(mWhite_timer / 60.0);

                mWhite_timer--;
            }
        };

        task.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    public void startNextRound() {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.sendMessage("§bLe prochain round va pouvoir commencé !");
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> changeWord(pls), 60L);
        }

        turnPassed.clear();
    }

    private void cleanUp(){
        bar.removeAll();
        playerRoles.clear();
        playerWords.clear();
        turnPassed.clear();
    }

    public BukkitRunnable getTask() { return task; }
    public boolean ismWhiteGuess() { return mWhiteGuess; }
    public List<UUID> getTurnPassed() { return turnPassed; }
    public Map<UUID, Roles> getPlayerRoles() { return playerRoles; }
    public String getCurrentCivilWord() { return currentWord.getCivil(); }
    public String getCurrentUndercoverWord() { return currentWord.getUndercover(); }

    public void setmWhiteGuess(boolean mWhiteGuess) { this.mWhiteGuess = mWhiteGuess; }
}
