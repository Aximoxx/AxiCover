package fr.Aximoxx.axiCover.listener;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage("§7[§c⏪§7]§f " + p.getName());

        if (Main.getInstance().getGameManager().getPlayerPlaying().contains(p.getUniqueId())){
            Main.getInstance().getGameManager().getPlayerPlaying().remove(p.getUniqueId());

            if (Main.getInstance().getGameManager().ismWhiteGuess()) {
                Main.getInstance().getGameManager().getWhiteBar().removeAll();
                Main.getInstance().getGameManager().setMWhiteGuess(false);
            }

            if (Main.getInstance().getGameManager().isVotePhase()){
                Main.getInstance().getGameManager().getVoteBar().removeAll();
                Main.getInstance().getGameManager().setVotePhase(false);

                for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                    Player pl = Bukkit.getPlayer(id);
                    if (pl != null) pl.getInventory().clear();
                }
            }

            for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                Player pl = Bukkit.getPlayer(id);
                if (pl != null){
                    pl.getInventory().clear();

                    pl.sendMessage("§6§lWARNING§7,§c " + p.getName() + "§7 à déconnecté(e) en pleine partie.",
                            "§7Il était " + (Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.MISTER_WHITE ? "§f§lM. White" :
                                    Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.UNDERCOVER ? "§c§lUndercover" : "§2§lCivil"),
                            "§7Une vérification va commencer...");
                }
            }

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                    Player pl = Bukkit.getPlayer(id);
                    if (pl != null) pl.sendMessage("§2Début§7 de la vérification...");
                }

                Main.getInstance().getCheckWinManager().checkWinWithoutVote();
            }, 10);
        }
    }
}
