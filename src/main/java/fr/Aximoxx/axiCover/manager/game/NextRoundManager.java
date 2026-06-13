package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NextRoundManager {
    int turns = Main.getInstance().getGameManager().getTurns();

    public void startNextRound() {
        turns++;
        Main.getInstance().getGameManager().getTurnPassed().clear();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getWordManager().changeWord(), 60L);

        for (UUID id : Main.getInstance().getGameManager().getPlayerPlaying()) {
            Player pls = Bukkit.getPlayer(id);
            if (pls != null) {
                pls.getInventory().clear();
                pls.sendMessage("§7Le round §2§l" + turns + " §7va pouvoir commencer !");
            }
        }
    }
}
