package fr.Aximoxx.axiCover.listener.falseInteraction;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp() || p.getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
    }
}
