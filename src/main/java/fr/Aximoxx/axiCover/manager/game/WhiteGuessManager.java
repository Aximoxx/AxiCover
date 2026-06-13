package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class WhiteGuessManager {
    int timer = Main.getInstance().getGameManager().getmWhite_timer();
    BukkitRunnable task = Main.getInstance().getGameManager().getTask();

    public void misterWhiteGuess(){
        Main.getInstance().getGameManager().setMWhiteGuess(true);
        for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
            Player pls = Bukkit.getPlayer(id);
            if (pls != null) Main.getInstance().getGameManager().getWhiteBar().addPlayer(pls);
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer == 0){
                    Main.getInstance().getGameManager().setMWhiteGuess(false);
                    Main.getInstance().getGameManager().getWhiteBar().removeAll();
                    Main.getInstance().getNextRoundManager().startNextRound();

                    cancel();
                    return;
                }

                Main.getInstance().getGameManager().getWhiteBar().setTitle("§fTemps restant: " + timer);
                Main.getInstance().getGameManager().getWhiteBar().setProgress(timer / 60.0);

                timer--;
            }
        };

        Main.getInstance().getGameManager().getTask().runTaskTimer(Main.getInstance(), 0, 20L);
    }
}
