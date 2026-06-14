package fr.Aximoxx.axiCover.gui;

import fr.Aximoxx.axiCover.Main;
import fr.Aximoxx.axiCover.utils.HeadList;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class ConfigGUI extends FastInv {

    private final String configWordType = "config.type";
    private final String configGameType = "config.vocal";
    private final String configTurnsPath = "config.turns";
    private final String configSavedPath = "config.saved";
    private final String configMisterWPath = "config.misterW";
    private final String configUndercoverPath = "config.undercover";

    private final String typeLore = "§8§oCliquez pour modifier la valeur";
    private final String lore1 = "§8§oClique gauche §a§o+1 §8§o| Click droit §c§o-1";
    private final String spawnLore = "§8§oCliquez pour vous §2§oéquipez§8§o de l'outil";
    private final String booleanLore = "§8§oClique gauche §a§o✔ §8§o| Click droit §c§o✘";
    private final String destroyLore = "§8§oCliquez pour §c§osupprimer§8§o tout les spawns";
    private final String asLore = "§8§oClique gauche §a§oafficher §8§o| Click droit §c§osupprimer";
    private final String saveConfigLore = "§8§oClique gauche §a§osave §8§o| Click droit §c§oreset";
    private final String switchToSpawnLore = "§8§oCliquez pour §a§oacceder§8§o à la configuration des §a§ospawns";
    private final String switchToConfigLore = "§8§oCliquez pour §a§oacceder§8§o à la configuration de la §a§opartie";

    public ConfigGUI(Player p) {
        super(45, "§fConfiguration de la partie");

        init(p);
    }

    private void init(Player p){
        Arrays.stream(getCorners()).forEach(corners -> setItem(corners, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("").build()));

        setItem(3, new ItemBuilder(Material.STICK).name("§fGestion des spawns").lore(switchToSpawnLore, "").build(), e -> {
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

            clearItems();
            initSpawns(p);
        });

        setItem(4, new ItemBuilder(Material.WHITE_BANNER).name("§fConfiguration Actuel").lore("",
                "             §7--§7§lRôles§7--",
                "§7Nombre d'Undercovers: §c" + Main.getInstance().getConfig().getInt(configUndercoverPath) + "§7/§c5",
                "§7M. White: " + (Main.getInstance().getConfig().getBoolean(configMisterWPath) ? "§a✔" : "§c✘"),
                "",
                "             §7--§7§lPartie§7--",
                "§7Configuration: " + (Main.getInstance().getConfig().getBoolean(configSavedPath) ? "§a✔ Sauvegardée" : "§c✘ Pas sauvegardée"),
                "§7Nombre de spawns: " + (Main.getInstance().getSpawns().isEmpty() ? "§c✘ Aucun spawn" : "§a✔ " + Main.getInstance().getSpawns().size() + " spawns"),
                "§7Nombre de tours: §6" + Main.getInstance().getConfig().getInt(configTurnsPath) + "§7/§615",
                "§7Type de partie: " + (Main.getInstance().getConfig().getBoolean(configGameType) ? "§eVocal" : "§fTextuelle"),
                "§7Dictionnaire: " + (Main.getInstance().getConfig().getString(configWordType).equals("minecraft") ? "§a§lMin§6§lecraft" : "§9§lFr§f§lan§c§lce"),
                "").build());

        setItem(5, new ItemBuilder(Material.CHEST).name("§fÉtat de la configuration actuel").lore(saveConfigLore, "",
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

                Main.getInstance().getSpawns().clear();
                Main.getInstance().saveSpawn();

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

        setItem(21, new ItemBuilder(HeadList.Walter_White.getItemStack()).name("§fM. White").lore(booleanLore, "",
                "§7Status: " + (Main.getInstance().getConfig().getBoolean(configMisterWPath) ? "§a✔" : "§c✘")).build(), e -> {
            e.setCancelled(true);

            if (e.getClick() == ClickType.LEFT) {
                if (Main.getInstance().getGameManager().getPlayers().size() <= 3){
                    p.sendMessage("§c§lERREUR§7, Il n'y a pas assez de joueur ! §c" + Main.getInstance().getGameManager().getPlayers().size() + "§7/§24");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configMisterWPath, true);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (e.getClick() == ClickType.RIGHT) Main.getInstance().getConfig().set(configMisterWPath, false); p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

            Main.getInstance().saveConfig();
            init(p);
        });

        setItem(22, new ItemBuilder(Main.getInstance().getConfig().getBoolean(configGameType) ? Material.BELL : Material.PAPER).name("§fType de partie").lore(booleanLore, "",
                "§7Status: " + (Main.getInstance().getConfig().getBoolean(configGameType) ? "§eVocal" : "§fTextuelle")).build(), e -> {
            e.setCancelled(true);

            if (e.getClick() == ClickType.LEFT) {
                if (Main.getInstance().getConfig().getBoolean(configGameType)) {
                    p.sendMessage("§c§lERREUR§7, Le mode §eVocal§7 est déjà défini !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configGameType, true);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (e.getClick() == ClickType.RIGHT) {
                if (!Main.getInstance().getConfig().getBoolean(configGameType)) {
                    p.sendMessage("§c§lERREUR§7, Le mode §fTextuelle§7 est déjà défini !");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return;
                }

                Main.getInstance().getConfig().set(configGameType, false);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            }

            Main.getInstance().saveConfig();
            init(p);
        });

        setItem(23, new ItemBuilder(Material.IRON_CHAIN).name("§fNbrs de §7Tours").lore(lore1, "",
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

        setItem(24, new ItemBuilder(Main.getInstance().getConfig().getString(configWordType).equals("dictionnaire") ? Material.BOOK : Material.GRASS_BLOCK)
                .name("§fType de mot").lore(typeLore, "",
                        ("§7Dictionnaire Actuel: " + (Main.getInstance().getConfig().getString(configWordType).equals("dictionnaire") ? "§9§lFr§f§lan§c§lce" : "§a§lMin§6§lecraft"))).build(), e -> {

            String currentType = Main.getInstance().getConfig().getString(configWordType);

            switch (currentType) {
                case "dictionnaire":
                    Main.getInstance().getConfig().set(configWordType, "minecraft");
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Vous avez choisi les mots du dictionnaire §a§lMin§6§lecraft §7!");
                    break;

                case "minecraft":
                    Main.getInstance().getConfig().set(configWordType, "dictionnaire");
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Vous avez choisi les mots du dictionnaire §9§lFra§f§lnc§c§lais §7!");
                    break;

                case null, default:
                    Main.getInstance().getConfig().set(configWordType, "dictionnaire");
                    Main.getInstance().saveConfig();

                    p.sendMessage("§7Une §cerreur§7 c'est produite. Le dictionnaire §9§lFra§f§lnc§c§lais§7 a été choisi par défaut.");
                    break;
            }

            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            init(p);
        });

        setItem(40, new ItemBuilder(Material.LIME_BANNER).name("§aDémarrer la partie").lore("", "§7§oPrêt à vous amusez ?").build(), e -> {
            p.closeInventory();
            Main.getInstance().getStartManager().onStart(p);
        });
    }

    private void initSpawns(Player p) {
        Arrays.stream(getCorners()).forEach(corners -> setItem(corners, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("").build()));

        setItem(3, new ItemBuilder(Material.BOOKSHELF).name("§fGestion de la Partie").lore(switchToConfigLore, "").build(), e -> {
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

            clearItems();
            init(p);
        });

        setItem(4, new ItemBuilder(Material.WHITE_BANNER).name("§fConfiguration Actuel").lore("",
                "             §7--§7§lRôles§7--",
                "§7Nombre d'Undercovers: §c" + Main.getInstance().getConfig().getInt(configUndercoverPath) + "§7/§c5",
                "§7M. White: " + (Main.getInstance().getConfig().getBoolean(configMisterWPath) ? "§a✔" : "§c✘"),
                "",
                "             §7--§7§lPartie§7--",
                "§7Configuration: " + (Main.getInstance().getConfig().getBoolean(configSavedPath) ? "§a✔ Sauvegardée" : "§c✘ Pas sauvegardée"),
                "§7Nombre de spawns: " + (Main.getInstance().getSpawns().isEmpty() ? "§c✘ Aucun spawn" : "§a✔ " + Main.getInstance().getSpawns().size() + " spawns"),
                "§7Nombre de tours: §6" + Main.getInstance().getConfig().getInt(configTurnsPath) + "§7/§615",
                "§7Type de partie: " + (Main.getInstance().getConfig().getBoolean(configGameType) ? "§eVocal" : "§fTextuelle"),
                "§7Dictionnaire: " + (Main.getInstance().getConfig().getString(configWordType).equals("minecraft") ? "§a§lMin§6§lecraft" : "§9§lFr§f§lan§c§lce"),
                "").build());


        setItem(5, new ItemBuilder(Material.CHEST).name("§fÉtat de la configuration actuel").lore(saveConfigLore, "",
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

                Main.getInstance().getSpawns().clear();
                Main.getInstance().saveSpawn();

                p.sendMessage("§7Vous avez §créinitialisé(e)§7 la configuration !");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1f ,1f);
            }

            init(p);
        });

        setItem(20, new ItemBuilder(Material.STICK).name("§fDéfinir").lore(spawnLore, "").build(), e -> {
            p.closeInventory();

            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f ,1f);
            p.getInventory().addItem(new ItemBuilder(Material.STICK).name("§7Bâton de spawn").lore("",
                    "§7Cliquez sur un block pour §2définir§7 un spawn",
                    "§7Cliquez sur un §6ArmorStand§7 pour retirer un spawn.").build());
        });

        setItem(22, new ItemBuilder(Material.ARMOR_STAND).name("§fAfficher").lore(asLore, "").build(), e -> {
            p.closeInventory();

            if (e.getClick() == ClickType.LEFT) {
                if (Main.getInstance().getSpawns().isEmpty()){
                    p.sendMessage("§c§lAucun spawn§7 n'est défini.");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f ,1f);
                    return;
                }
                for (World world : Bukkit.getWorlds())
                    world.getEntitiesByClasses(ArmorStand.class).stream().filter(as -> as.hasMetadata("TAG")).forEach(Entity::remove);

                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        if (i >= Main.getInstance().getSpawns().size()) {
                            cancel();
                            return;
                        }

                        Location loc = Main.getInstance().getSpawns().get(i).clone().add(0.5, 0.5, 0.5);
                        int count = i + 1;

                        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
                        stand.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, stand.getLocation().add(0, 0.5, 0), 25, 0.4, 1, 0.4, 0);
                        p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1f ,1f);

                        stand.setMetadata("TAG", new FixedMetadataValue(Main.getInstance(), false));
                        stand.setArms(false);
                        stand.setGravity(true);
                        stand.setInvulnerable(true);
                        stand.setCustomNameVisible(true);
                        stand.setCustomName("§fSpawn numéro §6" + count);

                        i++;
                    }
                }.runTaskTimer(Main.getInstance(), 0L, 5L);

                for (World world : Bukkit.getWorlds())
                    world.getEntitiesByClasses(ArmorStand.class).stream().filter(as -> as.hasMetadata("TAG")).forEach(as -> world.spawnParticle(Particle.ANGRY_VILLAGER, as.getLocation().add(0, 2, 0), 1, 0, 0, 0));

                p.sendMessage("§7Vous avez §2§lParfaitement§7 affiché(e) §2§l" + Main.getInstance().getSpawns().size() + "§7 Armor(s) Stand(s) !");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f ,1f);

            } else if (e.getClick() == ClickType.RIGHT) {
                for (World world : Bukkit.getWorlds())
                    world.getEntitiesByClasses(ArmorStand.class).stream().filter(as -> as.hasMetadata("TAG")).forEach(Entity::remove);

                p.sendMessage("§7Vous avez §2§lParfaitement§7 retiré(e) le(s) Armor(s) Stand(s) !");
                p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1f ,1f);
            }
        });

        setItem(24, new ItemBuilder(Material.TNT).name("§fDétruire").lore(destroyLore, "").build(), e -> {
            p.closeInventory();
            Main.getInstance().getSpawns().clear();
            Main.getInstance().saveSpawn();

            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f ,1f);


            p.sendMessage("§c§lBOOM !§7 Vous avez explosé(e) tout les spawns !");
            for (World world : Bukkit.getWorlds()){
                world.getEntitiesByClasses(ArmorStand.class).stream().filter(as -> as.hasMetadata("TAG")).forEach(as -> {
                        as.getWorld().strikeLightningEffect(as.getLocation());
                        as.remove();
                });
            }
        });
    }
}
