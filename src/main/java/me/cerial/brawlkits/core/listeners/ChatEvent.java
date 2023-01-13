package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
    public String uncolor(String message) {
        // Messy 1-liner
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String format;
        Player p = e.getPlayer();

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

        // Send the message
        e.setFormat(Utils.color("&7[&4"+p.getLevel()+"&7] " + p.getDisplayName() + format));
    }
}
