package fr.Aximoxx.axiCover.gui;

import fr.Aximoxx.axiCover.Main;
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

public class PlayerGUI extends FastInv {

    public PlayerGUI(Player p) {
        super(45, "§fAjouter des joueurs");
        Arrays.stream(getCorners()).forEach(corner -> setItem(corner, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build()));

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 41, 42, 43};

        setItem(40, new ItemBuilder(Material.DARK_OAK_DOOR).name("§cRetour").build(), e -> new ConfigGUI(p).open(p));

        int i = 0;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl != null) {
                setItem(slots[i], new ItemBuilder(buildPlayerHead(pl)).build(), e -> {
                    Player target = Bukkit.getPlayer(pl.getUniqueId());

                    if (target != null) {
                        if (Main.getInstance().getGameManager().getPlayers().contains(target.getUniqueId())){
                            p.sendMessage("§c§lERREUR§7, §2" + target.getName() + "§7 est déjà dans la partie !");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            return;
                        }

                        Main.getInstance().getGameManager().getPlayers().add(target.getUniqueId());
                        for (UUID id : Main.getInstance().getGameManager().getPlayers()){
                            Player pls = Bukkit.getPlayer(id);
                            if (pls != null) {
                                pls.sendMessage("§7[§a➶§7] §2§l" + target.getName() + "§7 a rejoint la partie !");
                                pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                            }
                        }
                    }
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
