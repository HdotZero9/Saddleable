package com.saddleable;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RecipeMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Recipe: ")) {
            event.setCancelled(true); // Explicitly prevent taking items
            event.getWhoClicked().sendMessage(ChatColor.RED + "You can't take items from the recipe viewer!");
        }
    }
}
