package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class NextRoundManager {
    UUID nextPlayerId;
    int turns = Main.getInstance().getGameManager().getTurns();
    int timer = Main.getInstance().getGameManager().getTurnTimer();
    BukkitRunnable task = Main.getInstance().getGameManager().getPlayerTurn();

    public void startPlayerNextTurn(){
        nextPlayerId = Main.getInstance().getGameManager().getPlayerPlaying().stream()
                .filter(pls -> !Main.getInstance().getGameManager().getTurnPassed().contains(pls))
                .findFirst().orElse(null);

        if (nextPlayerId == null) {
            Main.getInstance().getCheckWinManager().checkWinWithVote();
            return;
        }

        Player nextPlayer = Bukkit.getPlayer(nextPlayerId);
        if (nextPlayer != null){
            if (Main.getInstance().getConfig().getBoolean("config.vocal")) {
                nextPlayer.getInventory().setItem(0, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());
            } else nextPlayer.sendMessage("§2C'est à toi !§7 Envoye un mot dans le chat.");

        }

        for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            if (pl != null) {
                pl.sendMessage("§7C'est au tour de §6§l" + nextPlayer.getName() + " §7!");
                pl.playSound(pl.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1f, 1f);
            }
        }

        timer = Main.getInstance().getGameManager().getTurnTimer();
        if (task != null) task.cancel();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer == 400) {
                    Main.getInstance().getGameManager().getPlayerTurnBar().removeAll();
                    for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                        Player pl = Bukkit.getPlayer(id);
                        if (pl != null) Main.getInstance().getGameManager().getPlayerTurnBar().addPlayer(pl);
                    }
                }

                if (timer == 0) {
                    cancel();
                    Main.getInstance().getGameManager().getTurnPassed().add(nextPlayer.getUniqueId());
                    if (nextPlayer.getInventory().contains(Material.ARROW)) nextPlayer.getInventory().remove(Material.ARROW);

                    Main.getInstance().getGameManager().getPlayerTurnBar().removeAll();

                    if (Main.getInstance().getGameManager().getTurnPassed().size() >= Main.getInstance().getGameManager().getPlayerPlaying().size()) {
                        Main.getInstance().getCheckWinManager().checkWinWithVote();
                    } else Main.getInstance().getNextRoundManager().startPlayerNextTurn();

                    return;
                }

                Main.getInstance().getGameManager().getPlayerTurnBar().setProgress((double) timer / Main.getInstance().getGameManager().getTurnTimer());
                Main.getInstance().getGameManager().getPlayerTurnBar().setColor(timer > 200 ? BarColor.GREEN : timer <= 60 ? BarColor.RED : BarColor.YELLOW);
                Main.getInstance().getGameManager().getPlayerTurnBar().setTitle(timer > 200 ? "§fTour de: §a" + nextPlayer.getName() : timer <= 60 ? "§fTour de: §c" + nextPlayer.getName() : "§fTour de: §e" + nextPlayer.getName());
                timer--;
            }
        };
        task.runTaskTimer(Main.getInstance(), 0, 1L);
    }

    public void startNextRound() {
        turns++;
        Main.getInstance().getGameManager().getTurnPassed().clear();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            BukkitRunnable oldTask = Main.getInstance().getGameManager().getPlayerTurn();
            if (oldTask != null) oldTask.cancel();

            Main.getInstance().getWordManager().changeWord();
        }, 60L);

        for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
            Player pls = Bukkit.getPlayer(id);
            if (pls != null) {
                pls.getInventory().clear();
                pls.sendMessage("§7Le round §2§l" + turns + " §7va pouvoir commencer !");
            }
        }
    }

    public UUID getNextPlayerId() {
        return nextPlayerId;
    }

    public void setNextPlayerId(UUID nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }
}
