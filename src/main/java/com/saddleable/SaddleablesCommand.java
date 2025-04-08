package com.saddleable;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SaddleablesCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GREEN + "---- Saddleables Help ----");
        sender.sendMessage(ChatColor.YELLOW + "/recipe <item>" + ChatColor.WHITE + " - Show crafting recipe for item");
        sender.sendMessage(ChatColor.YELLOW + "/saddleables help" + ChatColor.WHITE + " - Show this help message");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 1 && "help".startsWith(args[0].toLowerCase())) {
            options.add("help");
        }
        return options;
    }
}
