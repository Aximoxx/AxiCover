package fr.Aximoxx.axiCover.manager.game;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
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

    public void startVotingPhase() {
        for (UUID id : Main.getInstance().getGameManager().getPlayerPlaying()) {
            Player p = Bukkit.getPlayer(id);
            if (p == null) continue;
            Main.getInstance().getGameManager().getVoteBar().addPlayer(p);
            p.sendTitle("§7La phase de §2§lVOTE§7 commence !", "§8Utilisez la boussole dans votre inventaire", 10, 30, 10);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
            p.getInventory().clear();
            p.getInventory().setItem(4, new ItemBuilder(Material.COMPASS).name("§7Sur qui portera ton vote ?").build());
        }

        new BukkitRunnable() {
            int timer = 30;
            @Override
            public void run() {
                if (timer == 0) {
                    Main.getInstance().getGameManager().getVoteBar().removeAll();
                    countVote();
                    cancel();
                    return;
                }

                if (timer <= 15) Main.getInstance().getGameManager().getVoteBar().setColor(BarColor.YELLOW);
                if (timer <= 5) Main.getInstance().getGameManager().getVoteBar().setColor(BarColor.RED);

                Main.getInstance().getGameManager().getVoteBar().setProgress(timer / 30.0);
                Main.getInstance().getGameManager().getVoteBar().setTitle("§fTemps restant: " + (timer > 15 ? "§a" : timer > 5 ? "§6" : "§c") + timer);
                timer--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    public void countVote(){
        Main.getInstance().getGameManager().getVoteBar().removeAll();
        if (Main.getInstance().getGameManager().getVoteMap().isEmpty()){
            for (UUID id : Main.getInstance().getGameManager().getPlayers()) {
                Player p = Bukkit.getPlayer(id);
                if (p != null) p.sendMessage("§c§lAucun vote§7 n'a été recensé. Passage à la prochaine manche.");
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
