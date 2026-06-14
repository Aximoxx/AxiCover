package fr.Aximoxx.axiCover.manager;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.mots.Words;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {
    private Words currentWord;

    // Runnable
    private BukkitRunnable task;
    private BukkitRunnable voteTask;
    private BukkitRunnable playerTurn;

    // Boolean
    private boolean playing = false;
    private boolean votePhase = false;
    private boolean mWhiteGuess = false;

    // List
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> turnPassed = new ArrayList<>();
    private final List<UUID> playerPlaying = new ArrayList<>();

    // Map
    private final Map<UUID, UUID> voteMap = new HashMap<>();
    private final Map<UUID, Roles> playerRoles = new HashMap<>();
    private final Map<UUID, String> playerWords = new HashMap<>();

    // Bossbar
    BossBar playerTurnBar = Bukkit.createBossBar("§fTour de: §a", BarColor.GREEN, BarStyle.SOLID);
    BossBar voteBar = Bukkit.createBossBar("§fTemps restant: §6", BarColor.GREEN, BarStyle.SOLID);
    BossBar whiteBar = Bukkit.createBossBar("§fM. White est entrain de deviné le mot: ", BarColor.WHITE, BarStyle.SOLID);

    // Int
    private int turns = 1;
    private int start_timer = 3;
    private int vote_timer = 600;
    private int turn_timer = 400;
    private int mWhite_timer = 600;

    public void onEnd(){
        setPlaying(false);
        cleanUp();
    }

    private void cleanUp(){
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), players::clear, 3L);

        // Nettoyage des BossBars
        voteBar.removeAll();
        whiteBar.removeAll();
        playerTurnBar.removeAll();

        // Nettoyage des Lists
        turnPassed.clear();
        playerPlaying.clear();

        // Nettoyage des Maps
        voteMap.clear();
        playerRoles.clear();
        playerWords.clear();
    }

    public BukkitRunnable    getTask()                   { return task; }
    public int               getTurns()                  { return turns; }
    public boolean           isPlaying()                 { return playing; }
    public BossBar           getVoteBar()                { return voteBar; }
    public List<UUID>        getPlayers()                { return players; }
    public Map<UUID, UUID>   getVoteMap()                { return voteMap; }
    public BukkitRunnable    getVoteTask()               { return voteTask; }
    public BossBar           getWhiteBar()               { return whiteBar; }
    public boolean           isVotePhase()               { return votePhase; }
    public List<UUID>        getTurnPassed()             { return turnPassed; }
    public BukkitRunnable    getPlayerTurn()             { return playerTurn; }
    public int               getTurnTimer()              { return turn_timer; }
    public int               getVoteTimer()              { return vote_timer; }
    public boolean           ismWhiteGuess()             { return mWhiteGuess; }
    public Map<UUID, Roles>  getPlayerRoles()            { return playerRoles; }
    public Words             getCurrentWord()            { return currentWord; }
    public Map<UUID, String> getPlayerWords()            { return playerWords; }
    public int               getStartTimer()             { return start_timer; }
    public int               getmWhiteTimer()            { return mWhite_timer; }
    public BossBar           getPlayerTurnBar()          { return playerTurnBar; }
    public List<UUID>        getPlayerPlaying()          { return playerPlaying; }
    public String            getCurrentCivilWord()       { return currentWord.getCivil(); }
    public String            getCurrentUndercoverWord()  { return currentWord.getUndercover(); }

    public void setPlaying(boolean playing)             { this.playing = playing; }
    public void setVotePhase(boolean votePhase)         { this.votePhase = votePhase; }
    public void setCurrentWord(Words currentWord)       { this.currentWord = currentWord; }
    public void setMWhiteGuess(boolean mWhiteGuess)     { this.mWhiteGuess = mWhiteGuess; }
}
