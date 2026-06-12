package fr.Aximoxx.axiCover.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class CreateSkull {

    public static ItemStack getHead(String name) {

        for (HeadList head : HeadList.values()) {

            if (head.getName().equalsIgnoreCase(name)) {

                return head.getItemStack();
            }
        }
        return null;
    }

    public static ItemStack createSkull(String textureValue, String name) {
        // Utilisation du matériel moderne (au lieu de LEGACY_SKULL_ITEM)
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (textureValue == null || textureValue.isEmpty()) {
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta == null) {
            return head;
        }

        // Création d'un profil officiel via l'API Bukkit
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());

        try {
            URL url;
            // Vérifie si la valeur est déjà un lien http, sinon on décode le Base64
            if (textureValue.startsWith("http://") || textureValue.startsWith("https://")) {
                url = new URL(textureValue);
            } else {
                // Le code récupère souvent un long format Base64 (eyJ0...), on le décode pour extraire l'URL
                byte[] decodedBytes = Base64.getDecoder().decode(textureValue);
                String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

                // Extraction de l'URL dans le JSON
                String urlString = decodedString.substring(decodedString.indexOf("\"url\":\"") + 7);
                urlString = urlString.substring(0, urlString.indexOf("\""));
                url = new URL(urlString);
            }

            // On applique l'URL de la texture au profil
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(url);
            profile.setTextures(textures);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // On applique le profil à la tête (sans utiliser la réflexion !)
        headMeta.setOwnerProfile(profile);
        head.setItemMeta(headMeta);

        return head;
    }
}
