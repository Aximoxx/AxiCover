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
    private final List<UUID> playerPlaying = new ArrayList<>();
    private final Map<UUID, Roles> playerRoles = new HashMap<>();
    private final Map<UUID, String> playerWords = new HashMap<>();
    BossBar bar = Bukkit.createBossBar("§fM. White est entrain de deviné le mot: ", BarColor.WHITE, BarStyle.SOLID);

    private int turns = 0;
    private int start_timer = 3;
    private int mWhite_timer = 30;
    private boolean mWhiteGuess = false;

    public void onStart(Player p){
        if (Main.getInstance().getSpawns().isEmpty()) {
            p.sendMessage("§c§lERREUR§7, Aucun spawn n'est configuré !");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        if (players.isEmpty()){
            p.sendMessage("§c§lERREUR§7, Aucun joueur ne joue !");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        if (players.size() < 3){
            p.sendMessage("§c§lERREUR§7, Il n'y a pas assez de joueur !§c " + players.size() + "§7/§23");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        if (Main.getInstance().getSpawns().size() < players.size()) {
            p.sendMessage("§c§lERREUR§7, Il n'y a pas assez de spawn configuré !§c " + Main.getInstance().getSpawns().size() + "§7/§2" + players.size());
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        new BukkitRunnable() {
            private float pitch = 1f;
            @Override
            public void run() {
                switch (start_timer) {
                    case 2:
                        pitch = 1.2f;
                        break;

                    case 1:
                        int i = 0;

                        for (UUID id : players) {
                            Player pl = Bukkit.getPlayer(id);
                            if (pl != null) {
                                pl.teleport(Main.getInstance().getSpawns().get(i).add(0.5, 0.5, 0.5));
                                playerPlaying.add(pl.getUniqueId());
                            }
                            i++;
                        }
                        pitch = 1.4f;
                        break;

                    case 0:
                        distributeWord();

                        UUID firstId = playerPlaying.stream().findFirst().orElse(null);
                        Player first = Bukkit.getPlayer(firstId);
                        if (first != null)
                            first.getInventory().setItem(0, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());

                        pitch = 1.6f;
                        break;
                }

                if (start_timer == 0) {
                    cancel();
                    return;
                }

                for (Player pls : Bukkit.getOnlinePlayers()){
                    pls.sendTitle("", "§7Début dans §6" + start_timer + " secondes", 10, 10, 10);
                    pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, pitch);
                }

                start_timer--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    public void distributeWord() {
        currentWord = Words.randomWords(Main.getInstance().getConfig().getString("config.type"));
        Collections.shuffle(playerPlaying);

        int index = 0;

        if (Main.getInstance().getConfig().getBoolean("config.misterW"))
            playerRoles.put(players.get(index++), Roles.MISTER_WHITE);

        for (int i = 0; i < Main.getInstance().getConfig().getInt("config.undercover") && index < players.size(); i++)
            playerRoles.put(players.get(index++), Roles.UNDERCOVER);

        for (; index < players.size(); index++)
            playerRoles.put(players.get(index), Roles.CIVIL);

        for (UUID id : playerPlaying) {
            Player pls = Bukkit.getPlayer(id);
            if (pls == null) continue;

            Roles role = playerRoles.get(pls.getUniqueId());
            String playerWord = switch (role) {
                case UNDERCOVER -> currentWord.getUndercover();
                case MISTER_WHITE -> "§knegro";
                default -> currentWord.getCivil();
            };

            playerWords.put(pls.getUniqueId(), playerWord);
            pls.sendTitle("§fTu es " + (role == Roles.MISTER_WHITE ? "§6Mister White" : role == Roles.UNDERCOVER ? "§cUndercover" : "§2Civil"),
                    "§7Ton mot est §f" + playerWord, 10, 60, 10);
        }
    }

    public void changeWord(Player p) {
        currentWord = Words.randomWords(Main.getInstance().getConfig().getString("config.type"));

        Roles role = playerRoles.get(p.getUniqueId());
        String playerWord = role == Roles.UNDERCOVER ? currentWord.getUndercover() : role == Roles.MISTER_WHITE ? "§knegro" : currentWord.getCivil();

        p.sendTitle("§7§lRAPPEL§7: §fTu es " + (role == Roles.MISTER_WHITE ? "§6Mister White" : role == Roles.UNDERCOVER ? "§cUndercover" : "§2Civil"),
                "§7Ton nouveau mot est §f" + playerWord, 10, 60, 10);


        Player first = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (first != null) first.getInventory().setItem(1, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());
    }

    public void checkWin() {
        long undercoverCount = playerPlaying.stream().filter(id -> playerRoles.get(id) == Roles.UNDERCOVER).count();
        long civilCount = playerPlaying.stream().filter(id -> playerRoles.get(id) == Roles.CIVIL).count();
        long misterWhiteCount = playerPlaying.stream().filter(id -> playerRoles.get(id) == Roles.MISTER_WHITE).count();

        boolean undercoverWin = undercoverCount >= civilCount && civilCount > 0
                || turns >= Main.getInstance().getConfig().getInt("config.turns");
        boolean civilWin = undercoverCount == 0 && misterWhiteCount == 0;

        for (Player p : Bukkit.getOnlinePlayers()){
            if (misterWhiteCount > 0 && undercoverCount == 0 && civilCount == 0) {
                misterWhiteGuess();
            } else if (undercoverWin) {
                p.sendMessage("§fLes §c§lUNDERCOVERS§f ont gagné !");
                p.sendTitle("§fVictoire des §c§lUNDERCOVERS", "", 10, 40, 10);
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f ,1f);
                onEnd(p);
            } else if (civilWin) {
                p.sendMessage("§fLes §2§lCIVILS§f ont gagné !");
                p.sendTitle("§fVictoire des §2§lCIVILS", "", 10, 40, 10);
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f, 1f);
                onEnd(p);
            } else startNextRound();
        }
    }

    public void misterWhiteGuess(){
        setMWhiteGuess(true);
        for (UUID id : players) {
            Player pls = Bukkit.getPlayer(id);
            if (pls != null) bar.addPlayer(pls);
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (mWhite_timer == 0){
                    setMWhiteGuess(false);
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

    public void startVotingPhase(Player p){
        BossBar bar = Bukkit.createBossBar("§fTemps restant: §6", BarColor.YELLOW, BarStyle.SOLID);
        bar.addPlayer(p);

        p.sendTitle("§7La phase de §2§lVOTE§7 commence !", "§8Utilisé la boussole dans votre inventaire", 10, 30, 10);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

        p.getInventory().clear();
        p.getInventory().setItem(4, new ItemBuilder(Material.COMPASS).name("§7Sur qui portera ton vote ?").build());

        new BukkitRunnable() {
            int timer = 30;
            @Override
            public void run() {

                if (timer == 0) {
                    bar.removeAll();
                    startNextRound();
                    cancel();
                    return;
                }

                bar.setProgress(timer / 60.0);
                bar.setTitle("§fTemps restant: §6" + timer);
                timer--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    public void startNextRound() {
        for (UUID id : playerPlaying) {
            Player pls = Bukkit.getPlayer(id);
            if (pls == null) continue;

            pls.sendMessage("§bLe prochain round va pouvoir commencer !");
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> changeWord(pls), 60L);
        }

        turns++;
        turnPassed.clear();
    }

    public void onEnd(Player p){
        p.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        cleanUp();
    }

    private void cleanUp(){
        bar.removeAll();
        players.clear();
        turnPassed.clear();
        playerRoles.clear();
        playerWords.clear();
        playerPlaying.clear();
    }

    public BukkitRunnable   getTask()                   { return task; }
    public List<UUID>       getPlayers()                { return players; }
    public List<UUID>       getTurnPassed()             { return turnPassed; }
    public boolean          ismWhiteGuess()             { return mWhiteGuess; }
    public Map<UUID, Roles> getPlayerRoles()            { return playerRoles; }
    public List<UUID>       getPlayerPlaying()          { return playerPlaying; }
    public String           getCurrentCivilWord()       { return currentWord.getCivil(); }
    public String           getCurrentUndercoverWord()  { return currentWord.getUndercover(); }

    public void setMWhiteGuess(boolean mWhiteGuess)     { this.mWhiteGuess = mWhiteGuess; }
}
