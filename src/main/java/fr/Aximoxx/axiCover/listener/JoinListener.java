package fr.Aximoxx.axiCover.listener;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage("§7[§a⏩§7]§f " + p.getName());

        Main.getInstance().updateTag(p);
    }
}
