package fr.Aximoxx.axiCover.command;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.utils.LuckPermsManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PermissionCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command cmd, @NonNull String label, String @NonNull [] args) {
        Player target;
        switch (cmd.getName()) {
            case "sethost":
                if (args.length < 1){
                    sender.sendMessage("§c§lERREUR§7, Il faut préciser un joueur !");
                    if (sender instanceof Player p) p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                target = Bukkit.getPlayer(args[0]);
                if (target == null || target.hasPermission("axicover.host")){
                    sender.sendMessage("§c§lERREUR§7, Se joueur §cn'éxiste pas§7 ou est déjà §d§lHOST§7 !");
                    if (sender instanceof Player p) p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                LuckPermsManager.addPermission(target, "axicover.host");
                sender.sendMessage("§7Vous avez §2§lparfaitement§7 défini §6" + target.getName() + "§7 en tant que §d§lHOST §7!");
                target.sendMessage("§7Vous avez été §2Promu(e)§7 à §d§lHOST§7 par §6" + ((sender instanceof Player p) ? p.getName() : "La Console") + "§7.");
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                if (sender instanceof Player p) p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                Main.getInstance().updateTag(target);
                break;

            case "removehost":
                if (args.length < 1){
                    sender.sendMessage("§c§lERREUR§7, Il faut préciser un joueur !");
                    if (sender instanceof Player p) p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.hasPermission("axicover.host")){
                    sender.sendMessage("§c§lERREUR§7, Se joueur §cn'éxiste pas§7 ou n'est déjà pas §d§lHOST§7 !");
                    if (sender instanceof Player p) p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                LuckPermsManager.removePermission(target, "axicover.host");
                sender.sendMessage("§7Vous avez §2§lparfaitement§7 retiré §6" + target.getName() + "§7 des joueurs §d§lHOST §7!");
                target.sendMessage("§7Vous avez été §cRétrogradé(e)§7 à §7§lJoueur§7 par §6" + ((sender instanceof Player p) ? p.getName() : "La Console") + "§7.");
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
                if (sender instanceof Player p) p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                Main.getInstance().updateTag(target);
                break;
        }

        return false;
    }
}
