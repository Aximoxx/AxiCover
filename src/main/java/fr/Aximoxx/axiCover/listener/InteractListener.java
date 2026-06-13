package fr.Aximoxx.axiCover.listener;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.gui.VoteGUI;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getItem() == null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName()) return;

        switch (e.getItem().getType()) {
            case STICK:
                if (!e.getItem().getItemMeta().getDisplayName().equals("§7Bâton de spawn")) return;

                Location currentLoc = e.getClickedBlock().getLocation();

                if (Main.getInstance().getSpawns().contains(currentLoc)) {
                    p.sendMessage("§c§lERREUR§7, Se bloc est déjà dans la liste des spawns.");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getSpawns().add(currentLoc);
                Main.getInstance().saveSpawn();

                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                p.sendMessage("", "", "",
                        "§7Vous avez ajouté la position ",
                        "§7§l⬝ §bX§7:§f " + e.getClickedBlock().getLocation().getBlockX(),
                        "§7§l⬝ §bY§7:§f " + e.getClickedBlock().getLocation().getBlockY(),
                        "§7§l⬝ §bZ§7:§f " + e.getClickedBlock().getLocation().getBlockZ(),
                        "§7à la liste de spawn !",
                        "",
                        "§7Il y a actuellement: " + (Main.getInstance().getSpawns().isEmpty() ? "§cAucun spawn §7défini" : "§2" + Main.getInstance().getSpawns().size() + "§2 spawn(s) défini(s)"));

                break;
            case ARROW:
                if (!e.getItem().getItemMeta().getDisplayName().equals("§cTermine ton tour")) return;

                e.getItem().setAmount(0);
                Main.getInstance().getGameManager().getTurnPassed().add(p.getUniqueId());

                if (Main.getInstance().getGameManager().getTurnPassed().size() >= Main.getInstance().getGameManager().getPlayerPlaying().size()) {
                    Main.getInstance().getGameManager().checkWin();
                    return;
                }

                UUID nextPlayerId = Main.getInstance().getGameManager().getPlayerPlaying().stream()
                        .filter(pls -> !Main.getInstance().getGameManager().getTurnPassed().contains(pls))
                        .findFirst().orElse(null);

                Player nextPlayer = Bukkit.getPlayer(nextPlayerId);
                if (nextPlayer != null)
                    nextPlayer.getInventory().setItem(0, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());
                break;

            case COMPASS:
                if (!e.getItem().getItemMeta().getDisplayName().equals("§7Sur qui portera ton vote ?")) return;

                new VoteGUI(p).open(p);
                break;
        }
    }


}
