package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StartManager {
    int timer = Main.getInstance().getGameManager().getStartTimer();
    public void onStart(Player p){
        if (Main.getInstance().getSpawns().isEmpty()) {
            p.sendMessage("§c§lERREUR§7, Aucun spawn n'est configuré !");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        if (Main.getInstance().getGameManager().getPlayers().isEmpty()){
            p.sendMessage("§c§lERREUR§7, Aucun joueur ne joue !");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        if (Main.getInstance().getGameManager().getPlayers().size() < 3){
            p.sendMessage("§c§lERREUR§7, Il n'y a pas assez de joueur !§c " + Main.getInstance().getGameManager().getPlayers().size() + "§7/§23");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        if (Main.getInstance().getSpawns().size() < Main.getInstance().getGameManager().getPlayers().size()) {
            p.sendMessage("§c§lERREUR§7, Il n'y a pas assez de spawn configuré !§c " + Main.getInstance().getSpawns().size() + "§7/§2" + Main.getInstance().getGameManager().getPlayers().size());
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        new BukkitRunnable() {
            private float pitch = 1f;
            @Override
            public void run() {
                switch (timer) {
                    case 2:
                        pitch = 1.2f;
                        break;

                    case 1:
                        Main.getInstance().getGameManager().setPlaying(true);
                        List<Location> shuffledSpawns = Main.getInstance().getSpawns();
                        Collections.shuffle(shuffledSpawns);

                        int i = 0;
                        for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                            Player pl = Bukkit.getPlayer(id);
                            if (pl != null) {
                                pl.teleport(shuffledSpawns.get(i).add(0.5, 0.5, 0.5));
                                Main.getInstance().getGameManager().getPlayerPlaying().add(pl.getUniqueId());
                                i++;
                            }
                        }
                        pitch = 1.4f;
                        break;

                    case 0:
                        Main.getInstance().getWordManager().distributeWord();

                        Main.getInstance().getNextRoundManager().startPlayerNextTurn();
                        pitch = 1.6f;
                        break;
                }

                if (timer == 0) {
                    cancel();
                    return;
                }

                for (Player pls : Bukkit.getOnlinePlayers()){
                    pls.sendTitle("", "§7Début dans §6" + timer + " secondes", 10, 10, 10);
                    pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, pitch);
                }

                timer--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }
}
