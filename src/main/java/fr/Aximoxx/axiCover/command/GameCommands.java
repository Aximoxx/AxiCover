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

public class GameCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player p)){
            sender.sendMessage("§cTu n'est pas du tout qualifié pour parler de mon gros front !");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "guess":
                if (Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) != Roles.MISTER_WHITE) {
                    Bukkit.broadcastMessage("§c§l" + p.getName().toUpperCase() + "§7 À VOULU TRICHÉ(E) ! cheh");
                    p.sendMessage("§cJ'aime pas les tricheurs..");
                    return true;
                }

                if (!Main.getInstance().getGameManager().ismWhiteGuess()) {
                    p.sendMessage("§cCe n'est pas encore ton tour !");
                    return true;
                }

                if (args.length < 2){
                    p.sendMessage("§cIl faut mettre un mot chef !");
                    return true;
                }

                String guess = args[1];

                for (Player pls : Bukkit.getOnlinePlayers()) {
                    if (guess.equalsIgnoreCase(Main.getInstance().getGameManager().getCurrentCivilWord())) {
                        Main.getInstance().getGameManager().onEnd(pls);
                        Main.getInstance().getGameManager().getTask().cancel();
                        Main.getInstance().getGameManager().getTask().cancel();
                        pls.sendTitle("§fM. White a trouvé(e) le mot !", "§7Le mot était §2" + Main.getInstance().getGameManager().getCurrentCivilWord(), 10, 40, 10);
                        pls.playSound(pls.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.7f, 1f);
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
