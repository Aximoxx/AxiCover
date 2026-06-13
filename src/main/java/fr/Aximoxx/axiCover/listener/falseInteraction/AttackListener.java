package fr.Aximoxx.axiCover.listener.falseInteraction;

import fr.Aximoxx.axiCover.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        if (!Main.getInstance().getGameManager().isPlaying()) return;
        e.setCancelled(true);
    }
}
