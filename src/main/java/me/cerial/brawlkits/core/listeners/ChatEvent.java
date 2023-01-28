package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatEvent implements Listener {
    public String uncolor(String message) {
        // Messy 1-liner
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendFrozen(Player p, String message) {
        List<Player> players = new ArrayList<>();
        players.add(p);
        // Get all the players with the core.trainee permission
        for (Player lpl : Bukkit.getServer().getOnlinePlayers()) {
            if (lpl.hasPermission("core.trainee")) {
                players.add(lpl);
            }
        }

        // Send a message to the players in that list
        for (Player lpl : players) {
            lpl.sendMessage(Utils.color("&4&lFrozen Chat &8> &r"+p.getName()+"&r: "+message));
        }
    }

    public static void sendFrozen(CommandSender p, String message) {
        ArrayList<Object> players = new ArrayList<>();
        players.add(p);

        // Get all the players with the core.trainee permission
        for (Player lpl : Bukkit.getServer().getOnlinePlayers()) {
            if (lpl.hasPermission("core.trainee")) {
                players.add(lpl);
            }
        }

        // Send a message to the players in that list
        for (Object lpl : players) {
            if (lpl instanceof CommandSender) {
                ((CommandSender) lpl).sendMessage(Utils.color("&4&lFrozen Chat &8> &r"+p.getName()+"&r: "+message));
            } else if (lpl instanceof Player) {
                ((Player) lpl).sendMessage(Utils.color("&4&lFrozen Chat &8> &r"+p.getName()+"&r: "+message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        String format;
        Player p = e.getPlayer();

        // Check if the player is frozen
        ServerDataManager data = new ServerDataManager(Core.getInstance());
        if (data.getConfig().getBoolean("players."+p.getUniqueId()+".frozen")) {
            sendFrozen(p, e.getMessage());
            return;
        }

        // Check if the player has frozen chat toggled on.
        if (data.getConfig().getBoolean("players."+p.getUniqueId()+".togglefc")) {
            // Check if the player is trainee
            if (p.hasPermission("core.trainee")) {
                // Send the frozen message
                sendFrozen(p, e.getMessage());
                return;
            }
        }

        // Check if the player is default
        if (p.hasPermission("core.whitechat")) {
            if (p.hasPermission("core.trainee")) {
                format = Utils.color("&r: " + e.getMessage());
            } else {
                format = Utils.color("&r: " + uncolor(e.getMessage()));
            }

        } else {
            format = Utils.color("&7: " + uncolor(e.getMessage()));
        }

        String tag = PlaceholderAPI.setPlaceholders(p, "%deluxetags_tag%");
        String fullFormat = "";
        if (tag.isBlank() || tag.isEmpty()) {
            fullFormat = Utils.color("&7[&4"+p.getLevel()+"&7] " + p.getDisplayName() + format);
        } else {
            fullFormat = Utils.color("&7[&4"+p.getLevel()+"&7] " + tag + " " + p.getDisplayName() + format);
        }

        // Send the message
        e.setFormat(fullFormat);
    }
}
