package fr.Aximoxx.axiCover.manager.mots;

import java.util.Arrays;
import java.util.Random;

public enum Words {
    // ─── 150 mots français ───────────────────────────────────────────────────

    // Animaux
    CHEVAL("Cheval", "Poney", "dictionnaire"),
    LION("Lion", "Tigre", "dictionnaire"),
    AIGLE("Aigle", "Faucon", "dictionnaire"),
    REQUIN("Requin", "Dauphin", "dictionnaire"),
    LOUP("Loup", "Renard", "dictionnaire"),
    ELEPHANT("Éléphant", "Rhinocéros", "dictionnaire"),
    CORBEAU("Corbeau", "Pie", "dictionnaire"),
    TORTUE("Tortue", "Lézard", "dictionnaire"),
    CROCODILE("Crocodile", "Alligator", "dictionnaire"),
    PAPILLON("Papillon", "Libellule", "dictionnaire"),
    FOURMI("Fourmi", "Abeille", "dictionnaire"),
    ARAIGNEE("Araignée", "Scorpion", "dictionnaire"),
    BALEINE("Baleine", "Orque", "dictionnaire"),
    HIBOU("Hibou", "Chouette", "dictionnaire"),
    RENARD("Renard", "Blaireau", "dictionnaire"),

    // Nourriture
    BAGUETTE("Baguette", "Croissant", "dictionnaire"),
    FROMAGE("Fromage", "Beurre", "dictionnaire"),
    CREPE("Crêpe", "Gaufre", "dictionnaire"),
    SOUPE("Soupe", "Bouillon", "dictionnaire"),
    GATEAU("Gâteau", "Tarte", "dictionnaire"),
    CHOCOLAT("Chocolat", "Caramel", "dictionnaire"),
    POMME("Pomme", "Poire", "dictionnaire"),
    RAISIN("Raisin", "Cerise", "dictionnaire"),
    CITRON("Citron", "Orange", "dictionnaire"),
    CHAMPIGNON("Champignon", "Truffe", "dictionnaire"),
    BEURRE("Beurre", "Margarine", "dictionnaire"),
    MIEL("Miel", "Confiture", "dictionnaire"),
    SAUCISSON("Saucisson", "Jambon", "dictionnaire"),
    CAMEMBERT("Camembert", "Brie", "dictionnaire"),
    ESCARGOT("Escargot", "Moule", "dictionnaire"),

    // Nature
    FORET("Forêt", "Jungle", "dictionnaire"),
    MONTAGNE("Montagne", "Colline", "dictionnaire"),
    RIVIERE("Rivière", "Ruisseau", "dictionnaire"),
    PLAGE("Plage", "Falaise", "dictionnaire"),
    VOLCAN("Volcan", "Geyser", "dictionnaire"),
    PRAIRIE("Prairie", "Savane", "dictionnaire"),
    DESERT("Désert", "Steppe", "dictionnaire"),
    MARAIS("Marais", "Tourbière", "dictionnaire"),
    GLACIER("Glacier", "Banquise", "dictionnaire"),
    CAVERNE("Caverne", "Grotte", "dictionnaire"),
    TORRENT("Torrent", "Cascade", "dictionnaire"),
    DUNE("Dune", "Oasis", "dictionnaire"),
    FALAISE("Falaise", "Ravin", "dictionnaire"),
    VALLEE("Vallée", "Plaine", "dictionnaire"),
    TOURBILLON("Tourbillon", "Tempête", "dictionnaire"),

    // Objets du quotidien
    MIROIR("Miroir", "Vitre", "dictionnaire"),
    HORLOGE("Horloge", "Pendule", "dictionnaire"),
    FENETRE("Fenêtre", "Porte", "dictionnaire"),
    CLES("Clés", "Cadenas", "dictionnaire"),
    LAMPE("Lampe", "Bougie", "dictionnaire"),
    MARTEAU("Marteau", "Masse", "dictionnaire"),
    ECHELLE("Échelle", "Escabeau", "dictionnaire"),
    PARAPLUIE("Parapluie", "Imperméable", "dictionnaire"),
    VELO("Vélo", "Trottinette", "dictionnaire"),
    SEAU("Seau", "Arrosoir", "dictionnaire"),
    CRAYON("Crayon", "Stylo", "dictionnaire"),
    BAGUE("Bague", "Bracelet", "dictionnaire"),
    COURONNE("Couronne", "Diadème", "dictionnaire"),
    BOUCLIER("Bouclier", "minecraft", "dictionnaire"),
    EPEE("Épée", "Dague", "dictionnaire"),

    // Métiers
    BOULANGER("Boulanger", "Pâtissier", "dictionnaire"),
    FORGERON("Forgeron", "Charpentier", "dictionnaire"),
    PECHEUR("Pêcheur", "Chasseur", "dictionnaire"),
    MEDECIN("Médecin", "Chirurgien", "dictionnaire"),
    CHEVALIER("Chevalier", "Soldat", "dictionnaire"),
    SORCIER("Sorcier", "Alchimiste", "dictionnaire"),
    ARCHITECTE("Architecte", "Maçon", "dictionnaire"),
    MARCHAND("Marchand", "Colporteur", "dictionnaire"),
    BERGER("Berger", "Fermier", "dictionnaire"),
    PIRATE("Pirate", "Corsaire", "dictionnaire"),

    // Lieux
    CHATEAU("Château", "Forteresse", "dictionnaire"),
    VILLAGE("Village", "Hameau", "dictionnaire"),
    MARCHE("Marché", "Bazar", "dictionnaire"),
    BIBLIOTHEQUE("Bibliothèque", "Librairie", "dictionnaire"),
    EGLISE("Église", "Cathédrale", "dictionnaire"),
    MOULIN("Moulin", "Scierie", "dictionnaire"),
    PONT("Pont", "Viaduc", "dictionnaire"),
    DONJON("Donjon", "Tour", "dictionnaire"),
    TAVERNE("Taverne", "Auberge", "dictionnaire"),
    GRENIER("Grenier", "Cave", "dictionnaire"),
    PUITS("Puits", "Citerne", "dictionnaire"),
    PHARE("Phare", "Jetée", "dictionnaire"),
    ARENE("Arène", "Stade", "dictionnaire"),
    PALAIS("Palais", "Manoir", "dictionnaire"),
    CIMETIERE("Cimetière", "Mausolée", "dictionnaire"),

    // Vêtements / accessoires
    CHAPEAU("Chapeau", "Casquette", "dictionnaire"),
    MANTEAU("Manteau", "Cape", "dictionnaire"),
    BOTTES("Bottes", "Sandales", "dictionnaire"),
    GANTS("Gants", "Moufles", "dictionnaire"),
    CEINTURE("Ceinture", "Bretelles", "dictionnaire"),
    ECHARPE("Écharpe", "Foulard", "dictionnaire"),
    TUNIQUE("Tunique", "Chemise", "dictionnaire"),
    MASQUE("Masque", "Capuche", "dictionnaire"),
    TABLIER("Tablier", "Sarrau", "dictionnaire"),
    COLLIER("Collier", "Pendentif", "dictionnaire"),

    // Actions / concepts
    VOYAGE("Voyage", "Aventure", "dictionnaire"),
    ENIGME("Énigme", "Mystère", "dictionnaire"),
    TRESOR("Trésor", "Butin", "dictionnaire"),
    LEGENDE("Légende", "Mythe", "dictionnaire"),
    MAGIE("Magie", "Sorcellerie", "dictionnaire"),
    ALLIANCE("Alliance", "Pacte", "dictionnaire"),
    TRAHISON("Trahison", "Mensonge", "dictionnaire"),
    VICTOIRE("Victoire", "Triomphe", "dictionnaire"),
    DEFAITE("Défaite", "Capitulation", "dictionnaire"),
    HONNEUR("Honneur", "Gloire", "dictionnaire"),

    // ─── 150 mots Minecraft ──────────────────────────────────────────────────

    // Blocs communs
    COBBLESTONE("Cobblestone", "Gravel", "minecraft"),
    DIRT("Dirt", "Coarse Dirt", "minecraft"),
    SAND("Sand", "Red Sand", "minecraft"),
    GRAVEL("Gravel", "Flint", "minecraft"),
    PLANKS("Planks", "Log", "minecraft"),
    STONE("Stone", "Diorite", "minecraft"),
    GRANITE("Granite", "Andesite", "minecraft"),
    CLAY("Clay", "Mud", "minecraft"),
    SPONGE("Sponge", "Wet Sponge", "minecraft"),
    OBSIDIAN("Obsidian", "Crying Obsidian", "minecraft"),
    BEDROCK("Bedrock", "Deepslate", "minecraft"),
    NETHERRACK("Netherrack", "Crimson Nylium", "minecraft"),
    GLOWSTONE("Glowstone", "Shroomlight", "minecraft"),
    SOULSAND("Soul Sand", "Soul Soil", "minecraft"),
    ENDSTONE("End Stone", "Purpur minecraftk", "minecraft"),

    // Minerais
    COAL("Coal", "Charcoal", "minecraft"),
    IRON("Iron", "Gold", "minecraft"),
    DIAMOND("Diamond", "Emerald", "minecraft"),
    LAPIS("Lapis", "Amethyst", "minecraft"),
    REDSTONE("Redstone", "Observer", "minecraft"),
    QUARTZ("Quartz", "Calcite", "minecraft"),
    COPPER("Copper", "Raw Copper", "minecraft"),
    NETHERITE("Netherite", "Ancient Debris", "minecraft"),
    GOLD("Gold", "Gilded Blackstone", "minecraft"),
    EMERALD("Emerald", "Diamond", "minecraft"),

    // Mobs
    CREEPER("Creeper", "Charged Creeper", "minecraft"),
    ZOMBIE("Zombie", "Drowned", "minecraft"),
    SKELETON("Skeleton", "Stray", "minecraft"),
    SPIDER("Spider", "Cave Spider", "minecraft"),
    ENDERMAN("Enderman", "Endermite", "minecraft"),
    BLAZE("Blaze", "Ghast", "minecraft"),
    SLIME("Slime", "Magma Cube", "minecraft"),
    WITCH("Witch", "Evoker", "minecraft"),
    GUARDIAN("Guardian", "Elder Guardian", "minecraft"),
    WARDEN("Warden", "Elder Guardian", "minecraft"),
    PILLAGER("Pillager", "Vindicator", "minecraft"),
    RAVAGER("Ravager", "Ravager", "minecraft"),
    PHANTOM("Phantom", "Bat", "minecraft"),
    HOGLIN("Hoglin", "Zoglin", "minecraft"),
    PIGLIN("Piglin", "Zombified Piglin", "minecraft"),

    // Outils / armes
    PICKAXE("Pickaxe", "Axe", "minecraft"),
    SWORD("Sword", "Trident", "minecraft"),
    BOW("Bow", "Crossbow", "minecraft"),
    SHOVEL("Shovel", "Hoe", "minecraft"),
    FLINT_STEEL("Flint & Steel", "Fire Charge", "minecraft"),
    FISHING_ROD("Fishing Rod", "Carrot on a Stick", "minecraft"),
    SHEARS("Shears", "Scissors", "minecraft"),
    SHIELD("Shield", "Totem", "minecraft"),
    ARROW("Arrow", "Spectral Arrow", "minecraft"),
    TRIDENT("Trident", "Sword", "minecraft"),

    // Armures
    HELMET("Helmet", "Turtle Shell", "minecraft"),
    CHESTPLATE("Chestplate", "Elytra", "minecraft"),
    LEGGINGS("Leggings", "Chainmail", "minecraft"),
    BOOTS("Boots", "Golden Boots", "minecraft"),
    ELYTRA("Elytra", "Chestplate", "minecraft"),

    // Structures
    DUNGEON("Dungeon", "Stronghold", "minecraft"),
    VILLAGE_MC("Village", "Pillager Outpost", "minecraft"),
    TEMPLE("Temple", "Pyramid", "minecraft"),
    MANSION("Mansion", "Stronghold", "minecraft"),
    FORTRESS("Nether Fortress", "Bastion Remnant", "minecraft"),
    END_CITY("End City", "End Ship", "minecraft"),
    IGLOO("Igloo", "Outpost", "minecraft"),
    SHIPWRECK("Shipwreck", "Ocean Monument", "minecraft"),
    RUINED_PORTAL("Ruined Portal", "Bastion", "minecraft"),
    ANCIENT_CITY("Ancient City", "Deep Dark", "minecraft"),

    // Biomes
    TAIGA("Taiga", "Spruce Forest", "minecraft"),
    SAVANNA("Savanna", "Badlands", "minecraft"),
    SWAMP("Swamp", "Mangrove Swamp", "minecraft"),
    TUNDRA("Tundra", "Snowy Plains", "minecraft"),
    JUNGLE("Jungle", "Bamboo Forest", "minecraft"),
    BADLANDS("Badlands", "Eroded Badlands", "minecraft"),
    MUSHROOM("Mushroom Island", "Swamp", "minecraft"),
    DEEP_OCEAN("Deep Ocean", "Warm Ocean", "minecraft"),
    CHERRY_GROVE("Cherry Grove", "Flower Forest", "minecraft"),
    DRIPSTONE("Dripstone Caves", "Lush Caves", "minecraft"),

    // Redstone
    PISTON("Piston", "Sticky Piston", "minecraft"),
    DROPPER("Dropper", "Dispenser", "minecraft"),
    HOPPER("Hopper", "Chest Minecart", "minecraft"),
    REPEATER("Repeater", "Comparator", "minecraft"),
    DAYLIGHT_SENSOR("Daylight Sensor", "Observer", "minecraft"),
    LEVER("Lever", "Button", "minecraft"),
    TRIPWIRE("Tripwire", "Pressure Plate", "minecraft"),
    TNT("TNT", "Minecart TNT", "minecraft"),
    RAIL("Rail", "Powered Rail", "minecraft"),
    NOTEBLOCK("Note Block", "Jukebox", "minecraft"),

    // Nourriture Minecraft
    GOLDEN_APPLE("Golden Apple", "Notch Apple", "minecraft"),
    BREAD("Bread", "Cake", "minecraft"),
    STEAK("Steak", "Porkchop", "minecraft"),
    CARROT("Carrot", "Golden Carrot", "minecraft"),
    POTATO("Potato", "Baked Potato", "minecraft"),
    PUMPKIN_PIE("Pumpkin Pie", "Cookie", "minecraft"),
    MELON("Melon", "Sweet Berries", "minecraft"),
    SPIDER_EYE("Spider Eye", "Poisonous Potato", "minecraft"),
    COD("Cod", "Salmon", "minecraft"),
    CHORUS_FRUIT("Chorus Fruit", "Glow Berries", "minecraft"),

    // Potions / enchantements
    SHARPNESS("Sharpness", "Smite", "minecraft"),
    PROTECTION("Protection", "Thorns", "minecraft"),
    SILK_TOUCH("Silk Touch", "Fortune", "minecraft"),
    LOOTING("Looting", "Luck of the Sea", "minecraft"),
    MENDING("Mending", "Unbreaking", "minecraft"),
    FIRE_ASPECT("Fire Aspect", "Knockback", "minecraft"),
    FEATHER_FALLING("Feather Falling", "Depth Strider", "minecraft"),
    INFINITY("Infinity", "Power", "minecraft"),
    CHANNELING("Channeling", "Riptide", "minecraft"),
    SWIFT_SNEAK("Swift Sneak", "Soul Speed", "minecraft"),

    // Entités / véhicules
    BOAT("Boat", "Chest Boat", "minecraft"),
    MINECART("Minecart", "Chest Minecart", "minecraft"),
    HORSE("Horse", "Donkey", "minecraft"),
    LLAMA("Llama", "Camel", "minecraft"),
    STRIDER("Strider", "Horse", "minecraft");

    // ─────────────────────────────────────────────────────────────────────────

    private final String civil;
    private final String undercover;
    private final String type;
    private static Random random = new Random();

    Words(String civil, String undercover, String type) {
        this.civil = civil;
        this.undercover = undercover;
        this.type = type;
    }

    public static Words randomWords(String type) {
        Words[] filtered = Arrays.stream(values())
                .filter(w -> w.type.equalsIgnoreCase(type))
                .toArray(Words[]::new);

        return filtered[random.nextInt(filtered.length)];
    }

    public String getCivil() { return civil; }
    public String getType() { return type; }
    public String getUndercover() { return undercover; }
}
