package com.saddleable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommand implements CommandExecutor, TabCompleter {

    private final RecipeConfigManager recipeConfigManager;

    public RecipeCommand(RecipeConfigManager recipeConfigManager) {
        this.recipeConfigManager = recipeConfigManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Players only command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /recipe <item>");
            return true;
        }

        String itemName = args[0].toLowerCase();
        if (!recipeConfigManager.recipeExists(itemName)) {
            player.sendMessage(ChatColor.RED + "Recipe not found: " + itemName);
            return true;
        }

        Inventory gui = Bukkit.createInventory(null, InventoryType.WORKBENCH, "Recipe: " + itemName);
        ItemStack[] layout = recipeConfigManager.getRecipeLayout(itemName);

        for (int i = 0; i < 9; i++) {
            gui.setItem(i + 1, layout[i]); // crafting grid starts at slot 1
        }

        gui.setItem(0, recipeConfigManager.getRecipeResult(itemName)); // Correct result slot

        player.openInventory(gui);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return recipeConfigManager.getAllRecipeNames();
        }
        return new ArrayList<>();
    }
}
