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
    private BukkitRunnable task;
    private boolean playing = false;
    private final List<UUID> players = new ArrayList<>();
    private final Map<UUID, UUID> voteMap = new HashMap<>();
    private final List<UUID> turnPassed = new ArrayList<>();
    private final List<UUID> playerPlaying = new ArrayList<>();
    private final Map<UUID, Roles> playerRoles = new HashMap<>();
    private final Map<UUID, String> playerWords = new HashMap<>();

    BossBar whiteBar = Bukkit.createBossBar("§fM. White est entrain de deviné le mot: ", BarColor.WHITE, BarStyle.SOLID);
    BossBar voteBar = Bukkit.createBossBar("§fTemps restant: §6", BarColor.GREEN, BarStyle.SOLID);

    private int turns = 1;
    public int start_timer = 3;
    private int mWhite_timer = 30;
    private boolean mWhiteGuess = false;

    public void onEnd(){
        setPlaying(false);
        cleanUp();
    }

    private void cleanUp(){
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), players::clear, 3L);

        voteMap.clear();
        turnPassed.clear();
        playerRoles.clear();
        playerWords.clear();
        whiteBar.removeAll();
        playerPlaying.clear();
    }

    public BukkitRunnable    getTask()                   { return task; }
    public int               getTurns()                  { return turns; }
    public boolean           isPlaying()                 { return playing; }
    public BossBar           getVoteBar()                { return voteBar; }
    public List<UUID>        getPlayers()                { return players; }
    public Map<UUID, UUID>   getVoteMap()                { return voteMap; }
    public BossBar           getWhiteBar()               { return whiteBar; }
    public List<UUID>        getTurnPassed()             { return turnPassed; }
    public boolean           ismWhiteGuess()             { return mWhiteGuess; }
    public Map<UUID, Roles>  getPlayerRoles()            { return playerRoles; }
    public Words             getCurrentWord()            { return currentWord; }
    public Map<UUID, String> getPlayerWords()            { return playerWords; }
    public int               getStart_timer()            { return start_timer; }
    public int               getmWhite_timer()           { return mWhite_timer; }
    public List<UUID>        getPlayerPlaying()          { return playerPlaying; }
    public String            getCurrentCivilWord()       { return currentWord.getCivil(); }
    public String            getCurrentUndercoverWord()  { return currentWord.getUndercover(); }

    public void setCurrentWord(Words currentWord)       { this.currentWord = currentWord; }
    public void setPlaying(boolean playing)             { this.playing = playing; }
    public void setMWhiteGuess(boolean mWhiteGuess)     { this.mWhiteGuess = mWhiteGuess; }
}
