package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class VoteManager {
    int timer = Main.getInstance().getGameManager().getVoteTimer();
    BukkitRunnable task = Main.getInstance().getGameManager().getVoteTask();

    public void startVotingPhase() {
        Main.getInstance().getGameManager().setVotePhase(true);
        timer = Main.getInstance().getGameManager().getVoteTimer();
        for (UUID id : Main.getInstance().getGameManager().getPlayerPlaying()) {
            Player p = Bukkit.getPlayer(id);
            if (p != null){
                Main.getInstance().getGameManager().getVoteBar().addPlayer(p);

                p.getInventory().clear();
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
                p.getInventory().setItem(4, new ItemBuilder(Material.COMPASS).name("§7Sur qui portera ton vote ?").build());
                p.sendTitle("§7La phase de §2§lVOTE§7 commence !", "§8Utilisez la boussole dans votre inventaire", 10, 30, 10);
            }
        }

        timer = Main.getInstance().getGameManager().getVoteTimer();
        if (task != null) task.cancel();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer == 0) {
                    Main.getInstance().getGameManager().setVotePhase(false);
                    Main.getInstance().getGameManager().getVoteBar().removeAll();
                    countVote();
                    cancel();
                    return;
                }

                Main.getInstance().getGameManager().getVoteBar().setProgress((double) timer / Main.getInstance().getGameManager().getVoteTimer());
                Main.getInstance().getGameManager().getVoteBar().setColor(timer > 300 ? BarColor.GREEN : timer <= 60 ? BarColor.RED : BarColor.YELLOW);
                Main.getInstance().getGameManager().getVoteBar().setTitle(timer > 300 ? "§fTemps restant: §a" + timer / 20 : timer <= 60 ? "§fTemps restant: §c" + timer / 20 : "§fTemps restant: §e" + timer / 20);
                timer--;
            }
        };
        task.runTaskTimer(Main.getInstance(), 0, 1L);
    }

    public void countVote(){
        Main.getInstance().getGameManager().getVoteBar().removeAll();
        if (Main.getInstance().getGameManager().getVoteMap().isEmpty()){
            for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                Player p = Bukkit.getPlayer(id);
                if (p != null) p.sendMessage("§c§lAucun vote§7 n'a été recensé.", "§7Passage à la prochaine manche.");
            }
            Main.getInstance().getNextRoundManager().startNextRound();
            return;
        }

        Map<UUID, Long> frequency = Main.getInstance().getGameManager().getVoteMap().values().stream()
                .collect(Collectors.groupingBy(uuid -> uuid, Collectors.counting()));

        UUID mostVotedId = Main.getInstance().getGameManager().getVoteMap().values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(uuid -> uuid, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        long count = frequency.getOrDefault(mostVotedId, 0L);

        Player mostVoted = Bukkit.getPlayer(mostVotedId);

        Main.getInstance().getGameManager().getPlayerPlaying().remove(mostVoted.getUniqueId());
        for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
            Player pls = Bukkit.getPlayer(id);
            if (pls != null) {
                pls.sendMessage("§c§l" + mostVoted.getName() + "§7 est éliminé(e) avec §2" + count + "§7 voix !");
                pls.sendMessage("§7Il était " + (Main.getInstance().getGameManager().getPlayerRoles().get(mostVoted.getUniqueId()) == Roles.MISTER_WHITE ? "§f§lM. WHITE" :
                        Main.getInstance().getGameManager().getPlayerRoles().get(mostVoted.getUniqueId()) == Roles.UNDERCOVER ? "§c§lUNDERCOVER" : "§2§lCIVIL") + "§7 !");
            }
        }

        Main.getInstance().getCheckWinManager().checkWinWithoutVote();
        Main.getInstance().getGameManager().getVoteMap().clear();
    }
}
