package fr.Aximoxx.axiCover.command;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class GameCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player p)){
            sender.sendMessage("§cTu n'est pas du tout qualifié pour parler de mon gros front !");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                if (Main.getInstance().getGameManager().getPlayers().contains(p.getUniqueId())){
                    p.sendMessage("§c§lERREUR§7, Tu est déjà dans la partie !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                Main.getInstance().getGameManager().getPlayers().add(p.getUniqueId());
                for (UUID id : Main.getInstance().getGameManager().getPlayers()){
                    Player pls = Bukkit.getPlayer(id);
                    if (pls != null) {
                        pls.sendMessage("§7[§a➶§7] §2§l" + p.getName() + "§7 a rejoint la partie !");
                        pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    }
                }
                break;

            case "leave":
                if (Main.getInstance().getGameManager().getPlayerPlaying().contains(p.getUniqueId())){
                    p.sendMessage("§c§lERREUR§7, tu ne peux pas quitter pendant une partie !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                Main.getInstance().getGameManager().getPlayers().remove(p.getUniqueId());
                Main.getInstance().getGameManager().getPlayerPlaying().remove(p.getUniqueId());

                for (UUID id : Main.getInstance().getGameManager().getPlayers()){
                    Player pls = Bukkit.getPlayer(id);
                    if (pls != null) {
                        pls.sendMessage("§7[§c➴§7] §c§l" + p.getName() + "§7 a §cquitté(e)§7 la partie !");
                        pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    }
                }
            case "guess":
                if (Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) != Roles.MISTER_WHITE) {
                    Bukkit.broadcastMessage("§c§l" + p.getName().toUpperCase() + "§7 À VOULU TRICHÉ(E) ! cheh");
                    p.sendMessage("§c§lERREUR§7, J'aime §cpas §7les §ctricheurs..");
                    p.playSound(p.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1f, 1f);
                    return true;
                }

                if (!Main.getInstance().getGameManager().ismWhiteGuess()) {
                    p.sendMessage("§c§lERREUR§7, Ce n'est §cpas encore§7 ton tour !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                if (args.length < 2){
                    p.sendMessage("§c§lERREUR§7, Vous devez §cmettre §7un §cmot§7 !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                String guess = args[1];

                for (Player pls : Bukkit.getOnlinePlayers()) {
                    if (guess.equalsIgnoreCase(Main.getInstance().getGameManager().getCurrentCivilWord())) {
                        Main.getInstance().getGameManager().onEnd(pls);
                        Main.getInstance().getGameManager().getTask().cancel();
                        Main.getInstance().getGameManager().getTask().cancel();
                        p.sendMessage("§fM. White§7 a gagné !");
                        pls.sendTitle("§fM. White §7a trouvé(e) le mot !", "§7Le mot était §2§l" + Main.getInstance().getGameManager().getCurrentCivilWord(), 10, 40, 10);
                        pls.playSound(pls.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.7f, 1f);
                    } else {
                        Main.getInstance().getGameManager().getTask().cancel();
                        Main.getInstance().getGameManager().startNextRound();
                    }
                }

                break;

        }

        return false;
    }
}
