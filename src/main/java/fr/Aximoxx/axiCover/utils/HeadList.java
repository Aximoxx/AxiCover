package fr.Aximoxx.axiCover.utils;

import org.bukkit.inventory.ItemStack;

public enum  HeadList {
    Walter_White("MTQ1YWJjMTY4MWZiMDM1MmZkMjZkOTljMTc4ZmRiYTdhNjM3NmI2ZDk5YzBlYjYzNzUyZTUyODg4Mjk1N2MxZSJ9fX0=", "ww");

    private final ItemStack item;

    private final String idTag;

    HeadList(String texture, String id) {
        String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
        item = CreateSkull.createSkull(prefix + texture, id);
        idTag = id;

    }

    public ItemStack getItemStack() {
        return item;
    }

    public String getName() {
        return idTag;
    }

}