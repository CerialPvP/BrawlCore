package me.cerial.brawlkits.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    
    // No permission message
    public static void noPerm(Player p) {message(p, "&cSorry, but you don't have permission to perform this action. If this message is a mistake, contact a manager.");}

    public static void noPerm(CommandSender s) {message(s, "&cSorry, but you don't have permission to perform this action. If this message is a mistake, contact a manager.");}

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

    public static int randomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max-min+1)+min);
    }

    /**
     * <h1>Format Time</h1>
     * Converts a second to a readable time (with hours, minutes, seconds, ms)
     * @param time The Long you want to convert.
     * @return The final converted time.
     */
    public static String formatTime(long time) {
        // Initialize Variables
        int hours = 0;
        int mins = 0;
        int secs = 0;

        // Check for hour
        while (time >= 3600) {
            hours++;
            time -= 3600;
        } while (time >= 60) {
            mins++;
            time -= 60;
        } while (time >= 1) {
            secs++;
            time -= 1;
        }

        // Make a string
        List<String> strings = new ArrayList<>();

        if (hours > 0) {
            strings.add(hours + "hours");
        }
        if (mins > 0) {
            strings.add(mins + "minutes");
        }
        if (secs > 0) {
            strings.add(secs + "seconds");
        }

        return String.join(", ", strings);
    }
}
