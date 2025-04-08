package com.saddleable;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaddleablePlugin extends JavaPlugin {

    private RecipeConfigManager recipeConfigManager;

    @Override
    public void onEnable() {
        recipeConfigManager = new RecipeConfigManager(this);
        recipeConfigManager.loadRecipes();

        PluginCommand recipeCommand = this.getCommand("recipe");
        RecipeCommand recipeCmdExecutor = new RecipeCommand(recipeConfigManager);
        recipeCommand.setExecutor(recipeCmdExecutor);
        recipeCommand.setTabCompleter(recipeCmdExecutor);

        PluginCommand saddleablesCommand = this.getCommand("saddleables");
        SaddleablesCommand saddleablesCmdExecutor = new SaddleablesCommand();
        saddleablesCommand.setExecutor(saddleablesCmdExecutor);
        saddleablesCommand.setTabCompleter(saddleablesCmdExecutor);

        // Explicitly registering both listeners
        Bukkit.getPluginManager().registerEvents(new RecipeMenuListener(), this);

        Bukkit.getLogger().info("[Saddleable] Plugin fully enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Saddleable] Plugin disabled.");
    }
}
