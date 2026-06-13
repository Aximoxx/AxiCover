package fr.Aximoxx.axiCover.gui;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.manager.Roles;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class VoteGUI extends FastInv {

    public VoteGUI(Player p) {
        super(45, "§7Vote");
        Arrays.stream(getCorners()).forEach(corners -> setItem(corners, new ItemBuilder(
                Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.MISTER_WHITE ? Material.WHITE_STAINED_GLASS_PANE :
                        Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.UNDERCOVER ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE).
                name(Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.MISTER_WHITE ? "§7Vous êtes §f§lMr. White" :
                        Main.getInstance().getGameManager().getPlayerRoles().get(p.getUniqueId()) == Roles.UNDERCOVER ? "§7Vous êtes §c§lUndercover" : "§7Vous êtes §2§lCivil").build()));

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        int i = 0;
        for (UUID id : Main.getInstance().getGameManager().getPlayerPlaying()) {
            Player pl = Bukkit.getPlayer(id);
            if (pl != null) {
                setItem(slots[i], new ItemBuilder(buildPlayerHead(pl)).build(), e -> {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(p.getName())) {
                        p.sendMessage("§c§lERREUR§7, Vous ne pouvez pas voter contre vous même !");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        return;
                    }

                    Player target = Bukkit.getPlayer(pl.getUniqueId());
                    Main.getInstance().getGameManager().getVoteMap().put(p.getUniqueId(), target.getUniqueId());

                    p.sendMessage("§7Votre vote se porte sur §c§l" + target.getName() + " §7!");
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.closeInventory();

                    if (Main.getInstance().getGameManager().getVoteMap().size() >= Main.getInstance().getGameManager().getPlayerPlaying().size())
                        Main.getInstance().getVoteManager().countVote();
                });
                i++;
            }
        }
    }

    private ItemStack buildPlayerHead(Player p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setLore(List.of(""));
            meta.setOwnerProfile(p.getPlayerProfile());
            meta.setDisplayName("§7Voter contre §c§l" + p.getName());
        }
        item.setItemMeta(meta);
        return item;
    }
}
