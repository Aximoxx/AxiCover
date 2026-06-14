package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckWinManager {

    public void checkWinWithVote() {
        // Pour le dev qui fouille dans mon code, sache une chose..
        // J'ai perdu 10 ans d'espérance de vie à cause de cette PUTAIN DE LIGNE.
        if (Main.getInstance().getGameManager().isVotePhase()) return;

        Main.getInstance().getGameManager().getVoteBar().removeAll();
        Main.getInstance().getGameManager().getWhiteBar().removeAll();
        Main.getInstance().getGameManager().getPlayerTurnBar().removeAll();

        long undercoverCount = Main.getInstance().getGameManager().getPlayerPlaying().stream().filter(id -> Main.getInstance().getGameManager().getPlayerRoles().get(id) == Roles.UNDERCOVER).count();
        long civilCount = Main.getInstance().getGameManager().getPlayerPlaying().stream().filter(id -> Main.getInstance().getGameManager().getPlayerRoles().get(id) == Roles.CIVIL).count();
        long misterWhiteCount = Main.getInstance().getGameManager().getPlayerPlaying().stream().filter(id -> Main.getInstance().getGameManager().getPlayerRoles().get(id) == Roles.MISTER_WHITE).count();

        boolean undercoverWin = undercoverCount >= civilCount && civilCount > 0
                || Main.getInstance().getGameManager().getTurns() >= Main.getInstance().getConfig().getInt("config.turns");
        boolean civilWin = undercoverCount == 0 && misterWhiteCount == 0;


        if (misterWhiteCount > 0 && undercoverCount == 0 && civilCount == 0) {
            Main.getInstance().getWhiteGuessManager().misterWhiteGuess();
        } else if (undercoverWin) {
            for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                Player pls = Bukkit.getPlayer(id);
                if (pls != null) {
                    pls.sendMessage("");
                    pls.sendMessage("§fLes §c§lUNDERCOVERS§f ont gagné !");
                    pls.sendMessage("");
                    pls.sendTitle("§fVictoire des §c§lUNDERCOVERS", "", 10, 40, 10);
                    pls.playSound(pls.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f, 1f);
                }
            }
                Main.getInstance().getGameManager().onEnd();
            } else if (civilWin) {
                for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                    Player pls = Bukkit.getPlayer(id);
                    if (pls != null) {
                    }
                    pls.sendMessage("");
                    pls.sendMessage("§fLes §2§lCIVILS§f ont gagné !");
                    pls.sendMessage("");
                    pls.sendTitle("§fVictoire des §2§lCIVILS", "", 10, 40, 10);
                    pls.playSound(pls.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f, 1f);
                }
                Main.getInstance().getGameManager().onEnd();
            } else Main.getInstance().getVoteManager().startVotingPhase();
    }

    public void checkWinWithoutVote() {
        long undercoverCount = Main.getInstance().getGameManager().getPlayerPlaying().stream().filter(id -> Main.getInstance().getGameManager().getPlayerRoles().get(id) == Roles.UNDERCOVER).count();
        long civilCount = Main.getInstance().getGameManager().getPlayerPlaying().stream().filter(id -> Main.getInstance().getGameManager().getPlayerRoles().get(id) == Roles.CIVIL).count();
        long misterWhiteCount = Main.getInstance().getGameManager().getPlayerPlaying().stream().filter(id -> Main.getInstance().getGameManager().getPlayerRoles().get(id) == Roles.MISTER_WHITE).count();

        boolean undercoverWin = undercoverCount >= civilCount && civilCount > 0
                || Main.getInstance().getGameManager().getTurns() >= Main.getInstance().getConfig().getInt("config.turns");
        boolean civilWin = undercoverCount == 0 && misterWhiteCount == 0;


        if (misterWhiteCount > 0 && undercoverCount == 0 && civilCount == 0) {
            Main.getInstance().getWhiteGuessManager().misterWhiteGuess();
        } else if (undercoverWin) {
            for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                Player pls = Bukkit.getPlayer(id);
                if (pls != null) {
                    pls.sendMessage("");
                    pls.sendMessage("§fLes §c§lUNDERCOVERS§f ont gagné !");
                    pls.sendMessage("");
                    pls.sendTitle("§fVictoire des §c§lUNDERCOVERS", "", 10, 40, 10);
                    pls.playSound(pls.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f, 1f);
                }
                Main.getInstance().getGameManager().onEnd();
            }
        }else if (civilWin) {
            for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                Player pls = Bukkit.getPlayer(id);
                if (pls != null) {
                    pls.sendMessage("");
                    pls.sendMessage("§fLes §2§lCIVILS§f ont gagné !");
                    pls.sendMessage("");
                    pls.sendTitle("§fVictoire des §2§lCIVILS", "", 10, 40, 10);
                    pls.playSound(pls.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f, 1f);
                }
                Main.getInstance().getGameManager().onEnd();
            }
        } else Main.getInstance().getNextRoundManager().startNextRound();
    }
}
