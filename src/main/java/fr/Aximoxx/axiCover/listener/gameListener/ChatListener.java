package fr.Aximoxx.axiCover.listener.gameListener;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (Main.getInstance().getGameManager().isPlaying()){
            if (Main.getInstance().getConfig().getBoolean("config.vocal")) {
                p.sendMessage("§c§lERREUR§7, Le chat a été désactivé dans la config !");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                e.setCancelled(true);
                return;
            }

            if (Main.getInstance().getGameManager().isVotePhase() || Main.getInstance().getGameManager().ismWhiteGuess()) {
                e.setCancelled(true);
                return;
            }

            if (Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.CIVIL
                    && e.getMessage().toLowerCase().contains(Main.getInstance().getGameManager().getCurrentCivilWord().toLowerCase())
                    || Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.UNDERCOVER
                    && e.getMessage().toLowerCase().contains(Main.getInstance().getGameManager().getCurrentUndercoverWord().toLowerCase())) {
                p.sendMessage("§c§lERREUR§7, Vous ne pouvez pas envoyez votre propre mot !");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                e.setCancelled(true);
                return;
            }

            e.setFormat("§7" + p.getName() + " §d➙§f " + e.getMessage());

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Main.getInstance().getGameManager().getTurnPassed().add(p.getUniqueId());
                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1f, 1f);

                if (Main.getInstance().getGameManager().getTurnPassed().size() >= Main.getInstance().getGameManager().getPlayerPlaying().size()) {
                    Main.getInstance().getCheckWinManager().checkWinWithVote();
                    return;
                }

                Main.getInstance().getGameManager().getPlayerTurnBar().removeAll();
                Main.getInstance().getNextRoundManager().startPlayerNextTurn();
            }, 2L);

        } else e.setFormat(p.hasPermission("axicover.host")
                ? "§d" + p.getName() + "§8:§f " + e.getMessage()
                : "§7" + p.getName() + "§8:§f " + e.getMessage());
    }
}
