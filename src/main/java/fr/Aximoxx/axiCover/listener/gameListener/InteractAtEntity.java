package fr.Aximoxx.axiCover.listener.gameListener;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractAtEntity implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked().getType() != EntityType.ARMOR_STAND || !e.getRightClicked().hasMetadata("TAG")) return;

        Location loc = e.getRightClicked().getLocation().add(-0.5, -0.5, -0.5);

        if (!Main.getInstance().getSpawns().contains(loc)) {
            p.sendMessage("§c§lAucun spawn§7 ne se trouve à ses coordonées !");
            return;
        }

        Main.getInstance().getSpawns().remove(loc);
        Main.getInstance().saveSpawn();

        p.getWorld().spawnParticle(Particle.BLOCK, e.getRightClicked().getLocation().add(0, 1, 0), 20, Material.OAK_PLANKS.createBlockData());
        e.getRightClicked().remove();

        int spawnLeft = Main.getInstance().getSpawns().size();

        p.sendMessage("§7Vous avez §2§lParfaitement§7 retirer le spawn !",
                spawnLeft == 0 ? "§7Il n'y a §cplus de §c§lspawn§7 !" : "§7Il reste " + (spawnLeft <= 3 ? "§c" : spawnLeft <= 6 ? "§6" : "§a") + Main.getInstance().getSpawns().size() + "§7 spawns");
        p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.3f, 1.7f);
    }
}
