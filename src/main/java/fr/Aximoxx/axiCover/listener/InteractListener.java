package fr.Aximoxx.axiCover.listener;

import fr.Aximoxx.axiCover.Main;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (p.getInventory().getItemInMainHand().getType() != Material.ARROW) return;

        p.getInventory().getItemInMainHand().setAmount(0);
        Main.getInstance().getGameManager().getTurnPassed().add(p.getUniqueId());

        if (Main.getInstance().getGameManager().getTurnPassed().size() >= Bukkit.getOnlinePlayers().size()) {
            Main.getInstance().getGameManager().checkWin();
            return;
        }

        Player nextPlayer = Bukkit.getOnlinePlayers().stream()
                .filter(pls -> !Main.getInstance().getGameManager().getTurnPassed().contains(pls.getUniqueId()))
                .findFirst()
                .orElse(null);

        if (nextPlayer != null)
            nextPlayer.getInventory().setItem(0, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());
    }
}
