package fr.Aximoxx.axiCover.listener.falseInteraction;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!Main.getInstance().getGameManager().isPlaying()) return;

        if (e.getFrom().getX() != e.getTo().getX() ||
            e.getFrom().getY() != e.getTo().getY() ||
            e.getFrom().getZ() != e.getTo().getZ()) e.setCancelled(true);
    }
}
