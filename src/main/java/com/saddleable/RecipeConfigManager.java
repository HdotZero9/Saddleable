package com.saddleable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipeConfigManager {

    private final SaddleablePlugin plugin;
    private FileConfiguration config;

    public RecipeConfigManager(SaddleablePlugin plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        File configFile = new File(plugin.getDataFolder(), "recipe.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("recipe.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadRecipes() {
        for (String recipeName : getAllRecipeNames()) {
            String resultName = config.getString(recipeName + ".result");
            Material resultMaterial = Material.matchMaterial(resultName);
            if (resultMaterial != null) {
                registerRecipe(recipeName, resultMaterial);
            } else {
                Bukkit.getLogger().severe("[Saddleable] Invalid result material: " + resultName);
            }
        }
    }

    private void registerRecipe(String path, Material resultMaterial) {
        List<String> shape = config.getStringList(path + ".shape");
        if (shape.size() != 3) {
            Bukkit.getLogger().severe("[Saddleable] Invalid shape for recipe: " + path);
            return;
        }

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, path), new ItemStack(resultMaterial));
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));

        for (String key : config.getConfigurationSection(path + ".ingredients").getKeys(false)) {
            Material mat = Material.matchMaterial(config.getString(path + ".ingredients." + key));
            if (mat == null) {
                Bukkit.getLogger().severe("[Saddleable] Invalid ingredient for " + path + ": " + key);
                return;
            }
            recipe.setIngredient(key.charAt(0), mat);
        }

        Bukkit.addRecipe(recipe);
        Bukkit.getLogger().info("[Saddleable] Recipe loaded: " + path);
    }

    public boolean recipeExists(String path) {
        return config.contains(path);
    }

    public ItemStack getRecipeResult(String path) {
        String resultName = config.getString(path + ".result");
        Material mat = Material.matchMaterial(resultName);
        return new ItemStack(mat != null ? mat : Material.BARRIER);
    }

    public ItemStack[] getRecipeLayout(String path) {
        ItemStack[] items = new ItemStack[9];
        List<String> shape = config.getStringList(path + ".shape");
        int idx = 0;

        for (String row : shape) {
            for (char c : row.toCharArray()) {
                if (c == ' ') {
                    items[idx++] = new ItemStack(Material.AIR);
                    continue;
                }
                Material mat = Material.matchMaterial(config.getString(path + ".ingredients." + c));
                items[idx++] = (mat != null ? new ItemStack(mat) : new ItemStack(Material.BARRIER));
            }
        }
        return items;
    }

    public List<String> getAllRecipeNames() {
        return new ArrayList<>(config.getKeys(false));
    }
}
