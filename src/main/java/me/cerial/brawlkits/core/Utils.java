package me.cerial.brawlkits.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Utils {
    public static Logger logger = Core.getInstance().getLogger();

    // Color text
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Get prefix
    public static String getPrefix() {
        return color("&4&lBrawl&c&lKits &8> &r");
    }

    // Message players
    public static void message(Player p, String message) {
        p.sendMessage(color(getPrefix() + message));
    }

    public static void message(CommandSender s, String message) {
        s.sendMessage(color(getPrefix() + message));
    }

    // Register commands & events
    public static void addCommand(CommandExecutor command, String name) {
        try {
            Bukkit.getPluginCommand(name).setExecutor(command);
            logger.info("Registered command " + name + "...");
        } catch (Exception ex) {
            logger.warning("An error occurred whilst registering command "+name+":");
            ex.printStackTrace();
        }
    }
}
