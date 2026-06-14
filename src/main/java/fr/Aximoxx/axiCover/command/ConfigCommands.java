package fr.Aximoxx.axiCover.command;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.gui.ConfigGUI;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ConfigCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {

        if (!(sender instanceof Player p)){
            sender.sendMessage("§cTu n'est pas du tout qualifié pour parler de mon gros front !");
            return true;
        }

        if (!p.hasPermission("axicover.host")) {
            p.sendMessage("§c§lERREUR§7, Vous n'avez pas la bonne permission !");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (args.length < 1){
            new ConfigGUI(p).open(p);
            return true;
        } else switch (args[0]){
            case "stop":
                if (!Main.getInstance().getGameManager().isPlaying()){
                    p.sendMessage("§c§lERREUR§7, Aucune partie en cours.");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                Main.getInstance().getCheckWinManager().checkWinWithVote();
                break;
            case "test1":
                p.sendMessage("cacaprout");
                break;
            default:
                break;
        }

        return false;
    }
}
