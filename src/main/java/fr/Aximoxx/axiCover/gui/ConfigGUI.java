package fr.Aximoxx.axiCover.gui;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.utils.HeadList;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public class ConfigGUI extends FastInv {
    private final String configWordType = "config.type";
    private final String configTurnsPath = "config.turns";
    private final String configSavedPath = "config.saved";
    private final String configMisterWPath = "config.misterW";
    private final String configUndercoverPath = "config.undercover";

    private final String typeLore = "§8§oCliquez pour modifier la valeur";
    private final String lore1 = "§8§oClique gauche §a§o+1 §8§o| Click droit §c§o-1";
    private final String mWhiteLore = "§8§oClique gauche §a§o✔ §8§o| Click droit §c§o✘";
    private final String saveConfigLore = "§8§oClique gauche §a§osave §8§o| Click droit §c§oreset";

    public ConfigGUI(Player p) {
        super(45, "§fConfiguration de la partie");

        init(p);
    }

    private void init(Player p){
        Arrays.stream(getCorners()).forEach(corners -> setItem(corners, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("").build()));

        setItem(4, new ItemBuilder(Material.WHITE_BANNER).name("§fConfiguration Actuel").lore("",
                "        §7--§7§lRôles§7--",
                "§7Nombre d'Undercovers: §c" + Main.getInstance().getConfig().getInt(configUndercoverPath) + "§7/§c5",
                "§7M. White: " + (Main.getInstance().getConfig().getBoolean(configMisterWPath) ? "§a✔" : "§c✘"),
                "",
                "        §7--§7§lPartie§7--",
                "§7Configuration: " + (Main.getInstance().getConfig().getBoolean(configSavedPath) ? "§a✔ Sauvegardée" : "§c✘ Pas sauvegardée"),
                "§7Nombre de tours: §6" + Main.getInstance().getConfig().getInt(configTurnsPath) + "§7/§615",
                "").build());

        setItem(5, new ItemBuilder(Material.CHEST).name("§7État de la configuration actuel").lore(saveConfigLore, "",
                "§7Status: " + (Main.getInstance().getConfig().getBoolean(configSavedPath) ? "§a✔" : "§c✘")).build(), e -> {

            if (e.getClick() == ClickType.LEFT) {
                if (Main.getInstance().getConfig().getBoolean(configSavedPath)) {
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Vous avez §2mis à jour§7 la configuration actuel !");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f ,1f);
                    return;
                }

                Main.getInstance().getConfig().set(configSavedPath, true);
                Main.getInstance().saveConfig();

                p.sendMessage("§7Vous avez §2sauvegardé(e)§7 la configuration actuel !");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f ,1f);
            } else if (e.getClick() == ClickType.RIGHT) {
                Main.getInstance().saveResource("config.yml", true);
                Main.getInstance().reloadConfig();

                p.sendMessage("§7Vous avez §créinitialisé(e)§7 la configuration !");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1f ,1f);
            }

            init(p);
        });

        setItem(20, new ItemBuilder(Material.RED_BANNER).name("§fNbrs d'§cUndercovers").lore(lore1, "",
                "§7Nombre actuel d'Undercovers: §c" + Main.getInstance().getConfig().getInt(configUndercoverPath)).build(), e -> {
            e.setCancelled(true);

            int count = Main.getInstance().getConfig().getInt(configUndercoverPath);

            if (e.getClick() == ClickType.LEFT) {
                if (count >= 5){
                    p.sendMessage("§cVous ne pouvez pas ajouter autant d'Undercovers!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configUndercoverPath, count + 1);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (e.getClick() == ClickType.RIGHT) {
                if (Main.getInstance().getConfig().getInt(configUndercoverPath) <= 1) {
                    p.sendMessage("§cVous ne pouvez pas désactivé les Undercovers !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configUndercoverPath, count - 1);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            }
            Main.getInstance().saveConfig();

            init(p);
        });

        setItem(21, new ItemBuilder(HeadList.Walter_White.getItemStack()).name("§fM. White").lore(mWhiteLore, "",
                "§7Status: " + (Main.getInstance().getConfig().getBoolean(configMisterWPath) ? "§a✔" : "§c✘")).build(), e -> {
            e.setCancelled(true);

            if (e.getClick() == ClickType.LEFT) {
                Main.getInstance().getConfig().set(configMisterWPath, true);
            } else if (e.getClick() == ClickType.RIGHT) Main.getInstance().getConfig().set(configMisterWPath, false);

            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

            Main.getInstance().saveConfig();
            init(p);
        });

        setItem(22, new ItemBuilder(Material.IRON_CHAIN).name("§fNbrs de §7Tours").lore(lore1, "",
                "§7Nombre de Tours actuel: §6" + Main.getInstance().getConfig().getInt(configTurnsPath)).build(), e -> {
            e.setCancelled(true);

            int count = Main.getInstance().getConfig().getInt(configTurnsPath);

            if (e.getClick() == ClickType.LEFT) {
                if (count >= 15){
                    p.sendMessage("§cVous ne pouvez pas ajouter autant de tours !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configTurnsPath, count + 1);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (e.getClick() == ClickType.RIGHT) {
                if (Main.getInstance().getConfig().getInt(configTurnsPath) <= 3) {
                    p.sendMessage("§cVous ne pouvez pas autant baisser le nombre de tours !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configTurnsPath, count - 1);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            }
            Main.getInstance().saveConfig();

            init(p);
        });

        setItem(23, new ItemBuilder(Main.getInstance().getConfig().getString(configWordType).equals("dictionnaire") ? Material.BOOK : Material.GRASS_BLOCK)
                .name("§fType de mot").lore(typeLore, "",
                        (Main.getInstance().getConfig().getString(configWordType).equals("dictionnaire") ? "§f§lLangue §9§lFra§f§lnça§c§lise" : "§a§lMin§6§lecraft")).build(), e -> {

            String currentType = Main.getInstance().getConfig().getString(configWordType);

            switch (currentType) {
                case "dictionnaire":
                    Main.getInstance().getConfig().set(configWordType, "minecraft");
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Vous avez choisi les mots en rapport avec §a§lMin§6§lecraft §7!");
                    break;

                case "minecraft":
                    Main.getInstance().getConfig().set(configWordType, "dictionnaire");
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Vous avez choisi les mots de la §flangue §9§lfra§f§lça§c§lise §7!");
                    break;

                case null, default:
                    Main.getInstance().getConfig().set(configWordType, "dictionnaire");
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Une §cerreur§7 c'est produite. La §flangue §9§lfra§f§lça§c§lise§7 a été choisi par défaut.");
                    break;
            }

            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            init(p);
        });

        setItem(40, new ItemBuilder(Material.LIME_BANNER).name("§aDémarrer la partie").lore("", "§7§oPrêt à vous amusez ?").build(), e -> {
            p.closeInventory();
            Main.getInstance().getGameManager().onStart();
        });
    }
}
