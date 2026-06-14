package fr.Aximoxx.axiCover.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Manager statique pour LuckPerms
 * Initialisez-le avec LuckPermsManager.init(api) dans votre onEnable
 */
public class LuckPermsManager {

    private static LuckPerms luckPerms;

    /**
     * Initialise l'instance statique de LuckPerms.
     * @param api L'instance de LuckPerms (via LuckPermsProvider.get())
     */
    public static void init(LuckPerms api) {
        luckPerms = api;
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

    // --- USER RETRIEVAL ---
    public static User getUser(Player player) {
        return (luckPerms != null) ? luckPerms.getUserManager().getUser(player.getUniqueId()) : null;
    }

    public static User getUser(UUID uuid) {
        return (luckPerms != null) ? luckPerms.getUserManager().getUser(uuid) : null;
    }

    /**
     * Sauvegarde les modifications d'un utilisateur de manière asynchrone.
     */
    public static CompletableFuture<Void> saveUser(User user) {
        return luckPerms.getUserManager().saveUser(user);
    }

    // --- PREFIX & RANK GETTERS ---

    public static String getPrefix(Player player) {
        User user = getUser(player);
        return (user != null) ? user.getCachedData().getMetaData().getPrefix() : null;
    }

    public static String getRank(Player player) {
        User user = getUser(player);
        return (user != null) ? user.getPrimaryGroup() : "default";
    }

    /**
     * Récupère le poids (weight) du grade principal du joueur.
     */
    public static int getWeight(Player player) {
        User user = getUser(player);
        if (user == null) return 0;

        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return 0;

        OptionalInt weight = group.getWeight();
        return weight.isPresent() ? weight.getAsInt() : 0;
    }

    // --- PERMISSIONS MANAGEMENT ---

    public static void addPermission(Player player, String permission) {
        User user = getUser(player);
        if (user == null) return;

        user.data().add(PermissionNode.builder(permission).build());
        saveUser(user);
    }

    public static void removePermission(Player player, String permission) {
        User user = getUser(player);
        if (user == null) return;

        user.data().remove(PermissionNode.builder(permission).build());
        saveUser(user);
    }

    public static void setPermission(Player player, String permission, boolean value) {
        User user = getUser(player);
        if (user == null) return;

        user.data().remove(PermissionNode.builder(permission).build());
        user.data().remove(PermissionNode.builder(permission).value(false).build());

        user.data().add(PermissionNode.builder(permission).value(value).build());

        saveUser(user);
    }

    /**
     * Ajoute une permission avec une durée d'expiration.
     */
    public static void addPermissionExpiry(Player player, String permission, Duration duration) {
        User user = getUser(player);
        if (user == null) return;

        user.data().add(PermissionNode.builder(permission).expiry(duration).build());
        saveUser(user);
    }

    // --- RANK / GROUP MANAGEMENT ---

    /**
     * Ajoute un grade au joueur (sans supprimer les anciens héritages).
     */
    public static void addRank(Player player, String groupName) {
        User user = getUser(player);
        if (user == null) return;

        user.data().add(InheritanceNode.builder(groupName).build());
        saveUser(user);
    }

    /**
     * Définit le grade principal : supprime TOUS les autres héritages de groupes
     * et définit celui-ci comme groupe primaire.
     */
    public static void setRank(Player player, String groupName) {
        User user = getUser(player);
        if (user == null) return;

        // On retire tous les groupes existants (nodes d'héritage)
        user.data().clear(NodeType.INHERITANCE::matches);

        // On ajoute le nouveau et on le définit en primaire
        user.data().add(InheritanceNode.builder(groupName).build());
        user.setPrimaryGroup(groupName);

        saveUser(user);
    }

    /**
     * Ajoute un grade temporaire.
     */
    public static void addRankExpiry(Player player, String groupName, Duration duration) {
        User user = getUser(player);
        if (user == null) return;

        user.data().add(InheritanceNode.builder(groupName).expiry(duration).build());
        saveUser(user);
    }

    /**
     * Retire un grade spécifique au joueur.
     */
    public static void removeRank(Player player, String groupName) {
        User user = getUser(player);
        if (user == null) return;

        user.data().remove(InheritanceNode.builder(groupName).build());
        saveUser(user);
    }

    /**
     * Définit une meta (clé = valeur) pour un joueur.
     * Exemple : setMeta(player, "activeCosmetic", "wings")
     */
    public static void setMeta(Player player, String key, String value) {
        User user = getUser(player);
        if (user == null) return;

        // Supprime TOUTES les metas avec cette clé
        user.data().clear(node ->
                NodeType.META.matches(node) &&
                        NodeType.META.cast(node).getMetaKey().equalsIgnoreCase(key)
        );

        // Ajoute la nouvelle valeur
        user.data().add(MetaNode.builder(key, value).build());

        saveUser(user);
    }

    /**
     * Récupère la valeur d'une meta d'un joueur.
     * Retourne null si la meta n'existe pas.
     */
    public static String getMeta(Player player, String key) {
        User user = getUser(player);
        if (user == null) return null;

        return user.getCachedData().getMetaData().getMetaValue(key);
    }

    /**
     * Supprime une meta d'un joueur.
     */
    public static void removeMeta(Player player, String key) {
        User user = getUser(player);
        if (user == null) return;

        user.data().clear(node -> node.getType() == NodeType.META
                && NodeType.META.cast(node).getMetaKey().equals(key));

        saveUser(user);
    }

    // --- UTILS ---

    /**
     * Vérifie si un joueur possède une permission.
     */
    public static boolean hasPermission(Player player, String permission) {
        User user = getUser(player);
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    /**
     * Récupère le temps restant pour un grade temporaire (en ms).
     */
    public static long getRankExpiration(Player player, String groupName) {
        User user = getUser(player);
        if (user == null) return -1;

        return user.getNodes().stream()
            .filter(NodeType.INHERITANCE::matches)
            .map(NodeType.INHERITANCE::cast)
            .filter(node -> node.getGroupName().equalsIgnoreCase(groupName) && node.hasExpiry())
            .mapToLong(node -> node.getExpiry().toEpochMilli() - System.currentTimeMillis())
            .findFirst()
            .orElse(-1L);
    }
}