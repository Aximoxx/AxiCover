package fr.Aximoxx.axiCover.manager.mots;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class WordManager {

    public void distributeWord() {
        Main.getInstance().getGameManager().setCurrentWord(Words.randomWords(Main.getInstance().getConfig().getString("config.type")));
        Collections.shuffle(Main.getInstance().getGameManager().getPlayerPlaying());

        int index = 0;

        if (Main.getInstance().getConfig().getBoolean("config.misterW"))
            Main.getInstance().getGameManager().getPlayerRoles().put(Main.getInstance().getGameManager().getPlayers().get(index++), Roles.MISTER_WHITE);

        for (int i = 0; i < Main.getInstance().getConfig().getInt("config.undercover") && index < Main.getInstance().getGameManager().getPlayers().size(); i++)
            Main.getInstance().getGameManager().getPlayerRoles().put(Main.getInstance().getGameManager().getPlayers().get(index++), Roles.UNDERCOVER);

        for (; index < Main.getInstance().getGameManager().getPlayers().size(); index++)
            Main.getInstance().getGameManager().getPlayerRoles().put(Main.getInstance().getGameManager().getPlayers().get(index), Roles.CIVIL);

        for (UUID id : Main.getInstance().getGameManager().getPlayerPlaying()) {
            Player pls = Bukkit.getPlayer(id);
            if (pls == null) continue;

            Roles role = Main.getInstance().getGameManager().getPlayerRoles().get(pls.getUniqueId());
            String playerWord = switch (role) {
                case UNDERCOVER -> Main.getInstance().getGameManager().getCurrentWord().getUndercover();
                case MISTER_WHITE -> "§knegro";
                default -> Main.getInstance().getGameManager().getCurrentWord().getCivil();
            };

            Main.getInstance().getGameManager().getPlayerWords().put(pls.getUniqueId(), playerWord);
            pls.sendMessage("§8§m                                        §r");
            pls.sendMessage("§fTu es " + (role == Roles.MISTER_WHITE ? "§fMister White" : role == Roles.UNDERCOVER ? "§cUndercover" : "§2Civil"));
            pls.sendMessage("§7Ton mot est §f" + playerWord);
            pls.sendMessage("§8§m                                        §r");

            pls.sendTitle("§fTu es " + (role == Roles.MISTER_WHITE ? "§fMister White" : role == Roles.UNDERCOVER ? "§cUndercover" : "§2Civil"),
                    "§7Ton mot est §f" + playerWord, 10, 60, 10);
        }
    }

    public void changeWord() {
        Main.getInstance().getGameManager().setCurrentWord(Words.randomWords(Main.getInstance().getConfig().getString("config.type")));

        for (UUID id : Main.getInstance().getGameManager().getPlayerPlaying()) {
            Player pls = Bukkit.getPlayer(id);
            if (pls != null) {
                Roles role = Main.getInstance().getGameManager().getPlayerRoles().get(pls.getUniqueId());
                String playerWord = role == Roles.UNDERCOVER ? Main.getInstance().getGameManager().getCurrentWord().getUndercover() :
                        role == Roles.MISTER_WHITE ? "§knegro" : Main.getInstance().getGameManager().getCurrentWord().getCivil();

                pls.sendMessage("§8§m                                        §r");
                pls.sendMessage("§fTu es " + (role == Roles.MISTER_WHITE ? "§fMister White" : role == Roles.UNDERCOVER ? "§cUndercover" : "§2Civil"));
                pls.sendMessage("§7Ton mot est §f" + playerWord);
                pls.sendMessage("§8§m                                        §r");

                pls.sendTitle("§7§lRAPPEL§7: §fTu es " + (role == Roles.MISTER_WHITE ? "§fMister White" : role == Roles.UNDERCOVER ? "§cUndercover" : "§2Civil"),
                        "§7Ton nouveau mot est §f" + playerWord, 10, 60, 10);
            }
        }

        Player first = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (first != null) first.getInventory().setItem(0, new ItemBuilder(Material.ARROW).name("§cTermine ton tour").build());
    }
}
